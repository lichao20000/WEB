package action.resource;

import bio.resource.QueryDeviceBio;

import com.opensymphony.xwork2.ActionSupport;

public class UnValidatedDeviceAction extends ActionSupport {

	private QueryDeviceBio queryDeviceBio;
	
	
	
	
	/**
	 * EXECUTE
	 */
	public String execute() throws Exception {
		
		return SUCCESS;
	}
	
	
	/**
	 * 查询未确认设备
	 * @author gongsj
	 * @date 2009-8-26
	 * @return
	 */
	public String getUnValidatedDevice() {
		
		
		return "result";
	}


	public void setQueryDeviceBio(QueryDeviceBio queryDeviceBio) {
		this.queryDeviceBio = queryDeviceBio;
	}


	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
