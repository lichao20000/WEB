
package com.linkage.module.gwms.resource.bio;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.resource.dao.BatchVoipSheetDAO;
import com.linkage.module.gwms.util.SocketUtil;
import com.linkage.module.gwms.util.StringUtil;

public class BatchVoipSheetBIO {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(BatchVoipSheetBIO.class);
	// 总数
	private int totalNum = 0;
	// 成功数
	private int successNum = 0;
	// 发送失败
	private int failNum = 0;
	// 格式错误
	private int formatErrorNum = 0;
	
	private BatchVoipSheetDAO dao;
	
	/**
	 * 发送工单
	 * @param arr
	 */
	public void simulateSheet(List<String[]> arr) {
		if (null == arr || arr.isEmpty() || arr.size() == 0) {
			return;
		}
		for (int i = 0; i < arr.size(); i++) {
			totalNum ++;
			String[] temp = arr.get(i);
			if(temp.length < 15) {
				formatErrorNum ++;
		        continue;
	        }
			String servType = temp[0];
			String operType = temp[1];
			String loid = temp[4];
			String cityId = temp[6];
			// 不是H248新装工单
			if (!"15".equals(servType) || !"1".equals(operType) ) {
				formatErrorNum ++;
				continue;
			}
			if (StringUtil.IsEmpty(loid) || StringUtil.IsEmpty(cityId)) {
				formatErrorNum ++;
				continue;
			}
			StringBuffer sb = new StringBuffer();
			// 业务类型
			sb.append(15).append("|||");
			// 操作类型
			sb.append(1).append("|||");
			// 业务受理时间
			sb.append(temp[2]).append("|||");
			// 设备类型
			sb.append(temp[3]).append("|||");
			// LOID
			sb.append(temp[4]).append("|||");
			// 业务电话号码
			sb.append(temp[5]).append("|||");
			// 属地
			sb.append(cityId).append("|||");
			// 终端标识：域名
			sb.append(temp[7]).append("|||");
			// 终端标识的类型
			sb.append(temp[8]).append("|||");
			// 主用MGC地址
			sb.append(temp[9]).append("|||");
			// 主用MGC器端口
			sb.append(temp[10]).append("|||");
			// 备用MGC地址
			sb.append(temp[11]).append("|||");
			// 备用MGC端口
			sb.append(temp[12]).append("|||");
			// 终端物理标示（TID与语音端口间存在对应关系）
			sb.append(temp[13]).append("|||");
			// VOIP语音协议类型
			sb.append(temp[14]).append("LINKAGE");
			
			String server = Global.G_ITMS_Sheet_Server;
			int port = Global.G_ITMS_Sheet_Port;
			String retResult = SocketUtil.sendStrMesg(server, port, sb.toString() + "\n");
			logger.warn("sheet is:[{}] result is：[{}]", sb.toString(), retResult);
			// 批量语音工单下发记录表
			Long time = System.currentTimeMillis()/1000;
			if (dao.getInfoCount(loid, time) > 0) {
				// 重复数据
				formatErrorNum ++;
				continue;
			}
			if (!"0".equals(retResult.substring(0, 1))) {
				failNum ++;
				dao.saveInfo(loid, cityId, time, -1);
			}
			else{
				dao.saveInfo(loid, cityId, time, 0);
				successNum ++;
			}
		}
	}

	/**
	 * 统计信息
	 * @param starttime
	 * @param endtime
	 * @param cityId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getListInfo(String starttime, String endtime, String cityId) {
		List<Map> list = new ArrayList<Map>();
		// 按属地统计
		Map<String,String> cityMap = CityDAO.getCityIdCityNameMap();
		// 根据属地cityId获取下一层属地ID(包含自己)
		ArrayList<String> cityList = CityDAO.getNextCityIdsByCityPid(cityId);
		Collections.sort(cityList);
		// 升级结果
		List resultlist = dao.getListInfo(starttime, endtime, cityId);
		for (int i = 0; i < cityList.size(); i++) {
			String city_id = cityList.get(i);
			ArrayList<String> tlist = CityDAO.getAllNextCityIdsByCityPid(city_id);
			// 总配置数
			long allup = 0;
			// 成功数
			long successnum = 0;
			// 失败（需重配）数
			long failnum = 0;
			// 未做数
			long noupnum = 0;

			Map<String, Object> tmap = new HashMap<String, Object>();
			tmap.put("city_id", city_id);
			tmap.put("city_name", cityMap.get(city_id));
			for (int j = 0; j < tlist.size(); j++) {
				String cityId2 = tlist.get(j);
				for (int k = 0; k < resultlist.size(); k++) {
					Map rmap = (Map) resultlist.get(k);
					if (cityId2.equals(rmap.get("city_id"))) {
						// 成功数
						if ("1".equals(StringUtil.getStringValue(rmap.get("open_status")))) {
							successnum = successnum + StringUtil.getLongValue(rmap.get("total"));
						}
						// 失败数
						else if ("-1".equals(StringUtil.getStringValue(rmap.get("open_status")))) {
							failnum = failnum + StringUtil.getLongValue(rmap.get("total"));
						}
						// 未做数
						else {
							noupnum = noupnum + StringUtil.getLongValue(rmap.get("total"));
						}
						allup = allup + StringUtil.getLongValue(rmap.get("total"));
					}
				}
			}
			tmap.put("allup", allup);
			tmap.put("successnum", successnum);
			tmap.put("noupnum", noupnum);
			tmap.put("failnum", failnum);
			tmap.put("percent", percent(successnum, allup));
			logger.warn("map:" + tmap.toString());
			list.add(tmap);
		}
		return list;
	}

	/**
	 * 计算百分数
	 * @param p1
	 * @param p2
	 * @return
	 */
	public String percent(long p1, long p2) {
		double p3;
		if (p2 == 0) {
			if(LipossGlobals.inArea(Global.NXDX)) {
				return "0";
			} else {
				return "N/A";
			}
		}
		else {
			p3 = (double) p1 / p2;
		}
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		String str = nf.format(p3);
		return str;
	}

	/**
	 * 输入excel文件，解析后返回ArrayList
	 * 
	 * @param file 输入的excel文件
	 * @return ArrayList<String>
	 */
	public List<String[]> analyticFile(File file) {
		List<String[]> arr = new ArrayList<String[]>();
		Workbook wwb = null;
		Sheet ws = null;
		try {
			// 读取excel文件
			wwb = Workbook.getWorkbook(file);
			// 总sheet数
			int sheetNumber = 1;
			for (int m = 0; m < sheetNumber; m++) {
				ws = wwb.getSheet(m);
				// 当前页总记录行数和列数
				int rowCount = ws.getRows();
				logger.warn("rowCount:" + rowCount);
				int columeCount = ws.getColumns();
				logger.warn("columeCount:" + columeCount);
				if (101 < rowCount) {
					rowCount = 101;
				}
				// 第一行为字段名，所以行数大于1才执行
				if (rowCount > 1 && columeCount > 0) {
					// 取当前页所有值放入list中
					for (int i = 1; i < rowCount; i++) {
						Cell[] cell = ws.getRow(i);
						String[] str = new String[cell.length];
						for (int j = 0; j < str.length; j++) {
							str[j] = cell[j].getContents().trim();
						}
						arr.add(str);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				wwb.close();
			}
			catch (Exception e) {
				logger.debug(e.getMessage());
			}
		}
		return arr;
	}
	
	@SuppressWarnings("rawtypes")
	public List<Map> getDevList(String starttime, String endtime, String cityId, String result, 
			int curPage_splitPage, int num_splitPage) {
		return dao.getDevList(starttime, endtime, cityId, result, curPage_splitPage, num_splitPage);
	}

	public int getDevCount(String starttime, String endtime, String cityId, String result, int curPage_splitPage,
			int num_splitPage) {
		return dao.getDevCount(starttime, endtime, cityId, result, curPage_splitPage, num_splitPage);
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getSuccessNum() {
		return successNum;
	}

	public void setSuccessNum(int successNum) {
		this.successNum = successNum;
	}

	public int getFailNum() {
		return failNum;
	}

	public void setFailNum(int failNum) {
		this.failNum = failNum;
	}

	public int getFormatErrorNum() {
		return formatErrorNum;
	}

	public void setFormatErrorNum(int formatErrorNum) {
		this.formatErrorNum = formatErrorNum;
	}

	public BatchVoipSheetDAO getDao() {
		return dao;
	}

	public void setDao(BatchVoipSheetDAO dao) {
		this.dao = dao;
	}
}
