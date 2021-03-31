/**
 * 单台设备检索
 */
package action.resource;

import java.util.List;

import dao.resource.QueryDeviceByAllDao;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-4-9
 * @category action.resource
 * 
 */
public class QueryDeviceByAllCityAction {

	private String device_serialnumber = null;
	
	private List deviceList = null;

	private int resultNumMore = 0;
	
	QueryDeviceByAllDao queryDeviceByAllDao;
	
	/**
	 * 查询商务领航所有设备的单台
	 * 
	 * @return
	 */
	public String getDeviceByAllCityInBBMS(){
		
		this.deviceList = this.queryDeviceByAllDao.getDeviceByAllCityInBBMS(this.device_serialnumber);
		
		if(1<this.deviceList.size()){
			this.resultNumMore = 2;
		}else if(1==this.deviceList.size()){
			this.resultNumMore = 1;
		}else{
			this.resultNumMore = 0;
		}
		
		return "result";
	}
	
	/**
	 * @return the device_srialnumber
	 */
	public String getDevice_serialnumber() {
		return device_serialnumber;
	}

	/**
	 * @param device_srialnumber the device_srialnumber to set
	 */
	public void setDevice_serialnumber(String device_serialnumber) {
		this.device_serialnumber = device_serialnumber;
	}

	/**
	 * @return the deviceList
	 */
	public List getDeviceList() {
		return deviceList;
	}

	/**
	 * @param deviceList the deviceList to set
	 */
	public void setDeviceList(List deviceList) {
		this.deviceList = deviceList;
	}

	/**
	 * @return the queryDeviceByAllDao
	 */
	public QueryDeviceByAllDao getQueryDeviceByAllDao() {
		return queryDeviceByAllDao;
	}

	/**
	 * @param queryDeviceByAllDao the queryDeviceByAllDao to set
	 */
	public void setQueryDeviceByAllDao(QueryDeviceByAllDao queryDeviceByAllDao) {
		this.queryDeviceByAllDao = queryDeviceByAllDao;
	}

	/**
	 * @return the resultNumMore
	 */
	public int getResultNumMore() {
		return resultNumMore;
	}

	/**
	 * @param resultNumMore the resultNumMore to set
	 */
	public void setResultNumMore(int resultNumMore) {
		this.resultNumMore = resultNumMore;
	}
	
}
