/**
 * 
 */
package bio.share;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;

import dao.share.SelectDeviceTagDAO;

/**
 * @author ASUS E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-2-2
 * @category bio.share
 * 
 */
public class SelectDeviceTagBIO {

	/** log */
	private static final Logger LOG = LoggerFactory.getLogger(SelectDeviceTagBIO.class);
	
	private SelectDeviceTagDAO selectDeviceTagDAO;
	
	/**
	 * @category getAllCityId 获取所有的属地
	 * 
	 * @param city_id 当前登录人的属地，
	 * 
	 * @return List
	 */
	public List getAllCityId(String city_id) {

		return selectDeviceTagDAO.getAllCityId(city_id);
	}
	
	/**
	 * @category getVendor 获取所有的厂商
	 * 
	 * @param city_id
	 * 
	 * @return List 
	 */
	public List getVendor() {

		return selectDeviceTagDAO.getVendor();
	}
	
	/**
	 * @category getDevicetype 获取所有的设备型号
	 * 
	 * @param vendor
	 * 
	 * @return String 
	 */
	public String getDeviceModel(String vendor) {

		List listDevicetype = selectDeviceTagDAO.getDeviceModel(vendor);
		
		StringBuffer bfDeviceType = new StringBuffer();
		
		for(int i=0;i<listDevicetype.size();i++){
			if(0!=i){
				bfDeviceType.append("#");
			}
			Map mapDevicetype = (Map) listDevicetype.get(i);
			bfDeviceType.append(mapDevicetype.get("device_model_id"));
			bfDeviceType.append(",");
			bfDeviceType.append(mapDevicetype.get("device_model"));
		}
		
		return bfDeviceType.toString();
	}

	/**
	 * @category getVersionList 获取所有的设备版本
	 * 
	 * @param vendor_id
	 * @param deviceModelId
	 * 
	 * @return String 
	 */
	public String getVersionList(String deviceModelId) {
		
		List listVersion = selectDeviceTagDAO.getVersionList(deviceModelId);
		
		StringBuffer bfVersion = new StringBuffer();
		
		for(int i=0;i<listVersion.size();i++){
			if(0!=i){
				bfVersion.append("#");
			}
			Map mapDevicetype = (Map) listVersion.get(i);
			bfVersion.append(mapDevicetype.get("devicetype_id"));
			bfVersion.append(",");
			bfVersion.append(mapDevicetype.get("softwareversion"));
		}
		
		return bfVersion.toString();
		
	}
	
	/**
	 * @category getOfficeList 获取特定属地的局向
	 * 
	 * @param city_id
	 * 
	 * @return String 
	 */
	public List getOfficeList(String city_id) {
		
		return selectDeviceTagDAO.getOfficeList(city_id);
		
	}
	
	/**
	 * @category getDevice 获取所有的终端设备，返回拼接的字符串为单选框的
	 * 
	 * @param onchangeType
	 * @param city_id
	 * @param vendor_id
	 * @param device_model_id
	 * @param devicetype_id
	 * @param hguser
	 * @param device_serialnumber
	 * @param loopback_ip
	 * 
	 * @return String 返回拼接的HTML字符串
	 */
	public String getDeviceCheckbox(String selectType,int gw_type,String onchangeType,String jsFunctionNameBySn,String city_id,String type,String param1,String param2,String listControl) {
		
		List listDevice = null;
		if(param1 == null || StringUtil.IsEmpty(param1.trim()))
		{
			listDevice = null;
		} else {
		//根据用户名
			if ("1".equals(type)) {
				listDevice = selectDeviceTagDAO.getDeviceUsername(gw_type,city_id,param1,param2);
			} else if ("2".equals(type)) {
				//根据设备序列号查询s
				listDevice = selectDeviceTagDAO.getDeviceBySerialno(gw_type,city_id,param1,param2);
			} else if ("3".equals(type)) {
				// 用于宁夏 根据VOIP电话号码查询
				listDevice = selectDeviceTagDAO.getDeviceByVoipTelNo(gw_type,city_id,param1);
			} else if ("5".equals(type)) {
				// 按iTV业务帐号  江苏需求单：JSDX_ITMS-REQ-20120222-LUHJ-001  add by zhangchy 2012-03-01
				listDevice = selectDeviceTagDAO.getDeviceByItvUserName(gw_type,city_id,param1);
			} else if ("6".equals(type)) {
				// 按宽带帐号   江苏需求单：JSDX_ITMS-REQ-20120222-LUHJ-001  add by zhangchy 2012-03-01
				listDevice = selectDeviceTagDAO.getDeviceByWideNetAccount(gw_type,city_id,param1);
			}
		}
		return getDeviceHtml(listDevice,onchangeType,jsFunctionNameBySn,selectType,listControl);
		
	}
	
	/**
	 * @category getDevice 获取所有的终端设备，返回拼接的字符串为单选框的
	 * 
	 * @param onchangeType
	 * @param city_id
	 * @param vendor_id
	 * @param device_model_id
	 * @param devicetype_id
	 * @param hguser
	 * @param device_serialnumber
	 * @param loopback_ip
	 * 
	 * @return String 返回拼接的HTML字符串
	 */
	public String getDeviceCheckboxBySenior(int gw_type,String city_id,String office_id,String vendor_id, String device_model_id , String devicetype_id,String loopback_ip,String online_status,String selectType,String jsFunctionName,String jsFunctionNameBySn,String listControl) {
		
		List listDevice = selectDeviceTagDAO.getDeviceBySenior(gw_type,city_id,office_id,vendor_id,device_model_id ,devicetype_id,loopback_ip,online_status);
		
		return getDeviceHtml(listDevice,jsFunctionName,jsFunctionNameBySn,selectType,listControl);
		
	}
	
	/**
	 * @category getDevice 获取所有的终端设备，返回拼接的字符串为单选框的
	 * 
	 * @param onchangeType
	 * @param city_id
	 * @param vendor_id
	 * @param device_model_id
	 * @param devicetype_id
	 * @param hguser
	 * @param device_serialnumber
	 * @param loopback_ip
	 * 
	 * @return String 返回拼接的HTML字符串
	 */
	public String getDeviceCheckboxByImport(int gw_type,String city_id,List<String> usernameList,String selectType,String jsFunctionName,String jsFunctionNameBySn,String listControl) {
		
		List listDevice = null;
		
		if(usernameList.size()>0){
			listDevice = selectDeviceTagDAO.getDeviceNormalList(gw_type,city_id,usernameList);
		}
	
		return getDeviceHtml(listDevice,jsFunctionName,jsFunctionNameBySn,selectType,listControl);
		
	}
	
	/**
	 * @category getDevice 根据传入的List拼接具体的字符串
	 * 
	 * @param listDevice
	 * @param jsFunctionName
	 * @param selectType
	 * 
	 * @return String 返回拼接的HTML字符串
	 */
	public String getDeviceHtml(List listDevice,String jsFunctionName,String jsFunctionNameBySn,String selectType,String listControl){
		
		StringBuffer serviceHtml = new StringBuffer();
		
		int listSize = 0;
		try{
			listSize = Integer.valueOf(listControl);
		}catch(Exception e){
			LOG.debug("SelectDeviceTagBIO=>getDeviceHtml():listControl为非数字！");
			listSize = 20;
		}
		if (null == listDevice || 1 > listDevice.size())
		{
			serviceHtml.append("当前条件查询的设备不存在！");
		}
		else if (listSize < listDevice.size())
		{
			serviceHtml.append("当前的条件查询的记录数为：");
			serviceHtml.append(listDevice.size());
			serviceHtml.append("，最多可显示");
			serviceHtml.append(listSize);
			serviceHtml.append("条记录，请重新选择条件查询！");
		}
		else
		{
			for (int i = 0; i < listDevice.size(); i++)
			{
				Map mapDevice = (Map) listDevice.get(i);
				serviceHtml.append("<input name='device_id' type='");
				serviceHtml.append(selectType);
				serviceHtml.append("' value='");
				serviceHtml.append(mapDevice.get("device_id"));
				serviceHtml.append("'");
				if (null != jsFunctionName && !"".equals(jsFunctionName))
				{
					if ("true".equals(jsFunctionNameBySn))
					{
						serviceHtml.append("onclick='");
						serviceHtml.append(jsFunctionName);
						serviceHtml.append("(");
						serviceHtml.append(mapDevice.get("device_id"));
						serviceHtml.append(",\"");
						serviceHtml.append(mapDevice.get("device_serialnumber"));
						serviceHtml.append("\")'");
					}
					else
					{
						serviceHtml.append("onclick='");
						serviceHtml.append(jsFunctionName);
						serviceHtml.append("(");
						serviceHtml.append(mapDevice.get("device_id"));
						serviceHtml.append(")'");
					}
				}
				serviceHtml.append("\\>");
				serviceHtml.append(mapDevice.get("oui"));
				serviceHtml.append("-");
				serviceHtml.append(mapDevice.get("device_serialnumber"));
				serviceHtml.append("|");
				serviceHtml.append(mapDevice.get("loopback_ip"));
				serviceHtml.append("<br>");
			}
		}
		
		return serviceHtml.toString();
	}
	
	/**
	 * @return the selectDeviceTagDAO
	 */
	public SelectDeviceTagDAO getSelectDeviceTagDAO() {
		return selectDeviceTagDAO;
	}

	/**
	 * @param selectDeviceTagDAO the selectDeviceTagDAO to set
	 */
	public void setSelectDeviceTagDAO(SelectDeviceTagDAO selectDeviceTagDAO) {
		this.selectDeviceTagDAO = selectDeviceTagDAO;
	}
	
	/**
	 * 输入excel文件，解析后返回ArrayList
	 * @param file 输入的excel文件
	 * @return ArrayList<String>
	 */
	public List<String> getUsernameByIportFile(File file,String fileReadCount){
		
		LOG.debug("getUsernameByIportFile({})",file);
		
		//初始化返回值和字段名数组
		ArrayList<String> arr = new ArrayList<String>();
		
		Workbook wwb = null;
		Sheet ws = null;
		
		try{
			
			int count = Integer.parseInt(fileReadCount) + 1;
			
			//读取excel文件
			wwb = Workbook.getWorkbook(file);
			
			//总sheet数
			//int sheetNumber = wwb.getNumberOfSheets();
			int sheetNumber = 1;
			LOG.debug("sheetNumber:" + sheetNumber);
			
			for (int m=0;m<sheetNumber;m++){
				ws = wwb.getSheet(m);
				
				//当前页总记录行数和列数
				int rowCount = ws.getRows();
				int columeCount = ws.getColumns();
				
				LOG.debug("rowCount:" + rowCount);
				LOG.debug("columeCount:" + columeCount);
				
				if(count<rowCount){
					rowCount = count;
				}
				
				//第一行为字段名，所以行数大于1才执行
				if (rowCount > 1 && columeCount > 0){
					
					//取当前页所有值放入list中
					for (int i=1;i<rowCount;i++){
						String temp = ws.getCell(0, i).getContents().trim();
						if(null!=temp && !"".equals(temp)){
							arr.add(ws.getCell(0, i).getContents().trim());
						}
					}
				}
				
			}
		}
		catch(Exception e){
			LOG.debug(e.getMessage());
		}
		finally{
			try{
				wwb.close();
			}catch(Exception e){
				LOG.debug(e.getMessage());
			}
			
		}
		
		return arr;
	}
}
