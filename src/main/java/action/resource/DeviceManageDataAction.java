/**
 * 
 */
package action.resource;

import java.util.List;
import java.util.Map;

import dao.resource.DeviceManageDataDAO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-7-28
 * @category action.resource
 * 
 */
public class DeviceManageDataAction {

	private String start_time = null;
	
	private String end_time = null;
	
	private String username = null;
	
	private String cityId = null;
	
	//	下载文件名
	private String pdfFileName = null;
	//标题
	private String pdfTitle = null;
	//table tile
	private String[] tbTitle = null;
	//需要取的列
	private String[] tbName = null;
	//数据
	private List<Map<String,String>> pdfListData = null;
	
	DeviceManageDataDAO dao;
	
	public String execute(){
		
		this.pdfFileName = "Radius-ITMS异步设备";
		this.pdfTitle = "Radius-ITMS异步设备";
		this.tbTitle = new String[5];
		this.tbName = new String[5];
		tbTitle[0] = "用户帐号";
		tbTitle[1] = "异步时间";
		tbTitle[2] = "属地";
		tbTitle[3] = "厂商OUI";
		tbTitle[4] = "设备序列号";
		tbName[0] = "username";
		tbName[1] = "outdate";
		tbName[2] = "city_id";
		tbName[3] = "oui";
		tbName[4] = "device_serialnumber";
		this.pdfListData = dao.getData(start_time,end_time, username, cityId);
		return "pdfByList";
	}

	/**
	 * @return the pdfFileName
	 */
	public String getPdfFileName() {
		return pdfFileName;
	}

	/**
	 * @param pdfFileName the pdfFileName to set
	 */
	public void setPdfFileName(String pdfFileName) {
		this.pdfFileName = pdfFileName;
	}

	/**
	 * @return the pdfListData
	 */
	public List<Map<String, String>> getPdfListData() {
		return pdfListData;
	}

	/**
	 * @param pdfListData the pdfListData to set
	 */
	public void setPdfListData(List<Map<String, String>> pdfListData) {
		this.pdfListData = pdfListData;
	}

	/**
	 * @return the pdfTitle
	 */
	public String getPdfTitle() {
		return pdfTitle;
	}

	/**
	 * @param pdfTitle the pdfTitle to set
	 */
	public void setPdfTitle(String pdfTitle) {
		this.pdfTitle = pdfTitle;
	}

	/**
	 * @return the tbName
	 */
	public String[] getTbName() {
		return tbName;
	}

	/**
	 * @param tbName the tbName to set
	 */
	public void setTbName(String[] tbName) {
		this.tbName = tbName;
	}

	/**
	 * @return the tbTitle
	 */
	public String[] getTbTitle() {
		return tbTitle;
	}

	/**
	 * @param tbTitle the tbTitle to set
	 */
	public void setTbTitle(String[] tbTitle) {
		this.tbTitle = tbTitle;
	}

	/**
	 * @return the cityId
	 */
	public String getCityId() {
		return cityId;
	}

	/**
	 * @param cityId the cityId to set
	 */
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	/**
	 * @return the dao
	 */
	public DeviceManageDataDAO getDao() {
		return dao;
	}

	/**
	 * @param dao the dao to set
	 */
	public void setDao(DeviceManageDataDAO dao) {
		this.dao = dao;
	}

	/**
	 * @return the start_time
	 */
	public String getStart_time() {
		return start_time;
	}

	/**
	 * @param start_time the start_time to set
	 */
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the end_time
	 */
	public String getEnd_time() {
		return end_time;
	}

	/**
	 * @param end_time the end_time to set
	 */
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	
}
