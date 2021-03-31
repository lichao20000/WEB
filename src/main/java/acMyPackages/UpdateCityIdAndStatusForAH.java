
package acMyPackages;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.util.IpUtil;
import com.linkage.system.utils.database.DataSetBean;


public class UpdateCityIdAndStatusForAH {

	private static final Logger LOG = LoggerFactory
			.getLogger(UpdateCityIdAndStatusForAH.class);

	public String getDeviceIdAndIp(String param) {
		ArrayList<String> arrayList = new ArrayList<String>();
		String preDeviceId = null;
		String deviceStatus = null;
		String[] arrStr = param.split(";");
		if (arrStr.length > 1) {
			preDeviceId = arrStr[0];
			deviceStatus = arrStr[1];
		} else {
			preDeviceId = arrStr[0];
		}
		PrepareSQL psql = new PrepareSQL();
		psql.append("select device_id, loopback_ip from tab_gw_device where gw_type = 2 and device_id like '"
				+ preDeviceId + "%'");
		if (deviceStatus != null) {
			psql.append(" and device_status = " + deviceStatus);
		}
		psql.append(" and city_id = '00' ");
		ArrayList<HashMap<String, String>> deviceList = DBOperation.getRecords(psql.getSQL());
		LOG.warn("查询OK");
		ArrayList<String []> devIpArrList = readExcel();
		for (Map<String, String> map : deviceList) {
			String deviceId = StringUtil.getStringValue(map, "device_id");
			String loopbackIp = StringUtil.getStringValue(map, "loopback_ip");
			long loopBackIpLong = IpUtil.getFullAddr(loopbackIp);
			Map<String, String> cityMap = new HashMap<String, String>();
			cityMap.put("滁州", "550");
			cityMap.put("合肥", "551");
			cityMap.put("蚌埠", "552");
			cityMap.put("芜湖", "553");
			cityMap.put("淮南", "554");
			cityMap.put("马鞍山", "555");
			cityMap.put("安庆", "556");
			cityMap.put("宿州", "557");
			cityMap.put("阜阳", "558");
			cityMap.put("黄山", "559");
			cityMap.put("淮北", "561");
			cityMap.put("铜陵", "562");
			cityMap.put("宣城", "563");
			cityMap.put("六安", "564");
			cityMap.put("池州", "566");
			cityMap.put("亳州", "567");
			String cityName = "";
			String cityId = "";
			String startIp = "";
			String endIp = "";
			for (String[] ipArray : devIpArrList) {
				cityName = ipArray[0];
				startIp = ipArray[1];
				endIp = ipArray[2];
				cityId = (String) cityMap.get(cityName);
				long startIpLong = IpUtil.getFullAddr(startIp);
				long endIpLong = IpUtil.getFullAddr(endIp);
				if ((deviceStatus != null) && ("0".equals(deviceStatus))) {
					if ((loopBackIpLong > startIpLong) && (loopBackIpLong < endIpLong)) {
						LOG.warn("update tab_gw_device set city_id = '" + cityId
								+ "', device_status = 1 where device_id = '" + deviceId
								+ "'");
						arrayList.add("update tab_gw_device set city_id = '" + cityId
								+ "', device_status = 1 where device_id = '" + deviceId
								+ "'");
					}
				} else if ((loopBackIpLong > startIpLong) && (loopBackIpLong < endIpLong)) {
					LOG.warn("update tab_gw_device set city_id = '" + cityId
							+ "' where device_id = '" + deviceId + "'");
					arrayList.add("update tab_gw_device set city_id = '" + cityId
							+ "' where device_id = '" + deviceId + "'");
				}
			}
		}
		if (arrayList != null && arrayList.size() > 0) {
			int[] iCodes = DataSetBean.doBatch(arrayList);
			LOG.warn("返回：" + iCodes.length);
		}
		return "成功！";
	}

	public ArrayList<String[]> readExcel() {
		ArrayList<String[]> devIpList = new ArrayList<String[]>();
		try {
			File file = new File("/export/home/zhangchy/ah_ip.xls");
			Workbook wwb = Workbook.getWorkbook(file);
			Sheet ws = wwb.getSheet(0);
			String cityName = "";
			String startIp = "";
			String endIp = "";
			int rowCount = ws.getRows();
			int columeCount = ws.getColumns();
			LOG.warn("====行rowCount==" + rowCount + "====");
			LOG.warn("====列columeCount==" + columeCount + "===");
			if ((rowCount > 1) && (columeCount > 0)) {
				for (int i = 1; i < rowCount; i++) {
					startIp = ws.getCell(0, i).getContents().trim();
					endIp = ws.getCell(1, i).getContents().trim();
					cityName = ws.getCell(3, i).getContents().trim();
					String[] devIpArr = { cityName, startIp, endIp };
					devIpList.add(devIpArr);
				}
			} else{
				LOG.warn("上传的Excel文件为空！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.warn("解析失败");
			LOG.error("解析失败，msg=({})", e.getMessage());
		}
		return devIpList;
	}
}
