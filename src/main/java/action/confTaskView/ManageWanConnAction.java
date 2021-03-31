package action.confTaskView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.paramConfig.ParamInfoAct;

/**
 * @author Jason(3412)
 * @date 2008-12-24
 */
public class ManageWanConnAction {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(ManageWanConnAction.class);
	private String deviceId;
	private String wanId;
	private String wanConnId;

	private String wanConnSessionType;
	private String wanConnSessionId;

	private String ajax;

	public String delWanConn() {
		
		this.ajax = "";
		
		boolean flag = (new ParamInfoAct(true)).deleteConnection(deviceId, wanId,wanConnId);
		
		logger.debug("ManageWanConnAction:delWanConn:flag=>" + flag);
		
		if (flag) {
			
			this.ajax = "结点删除成功";
		}else{
			this.ajax = "结点删除失败";
		}
		
		logger.debug("ManageWanConnAction:delWanConn:ajax=>" + ajax);
		
		return "ajax";
	}

	public String delWanConnSession() {
		
		this.ajax = "";
		
		boolean flag = (new ParamInfoAct(true)).deleteConnection(deviceId, wanId,
				wanConnId, wanConnSessionType, wanConnSessionId);

		logger.debug("ManageWanConnAction:delWanConnSession:flag=>" + flag);
		
		if (flag) {
			this.ajax = "结点删除成功";
		}else{
			this.ajax = "结点删除失败";
		}
		
		logger.debug("ManageWanConnAction:delWanConnSession:ajax=>" + ajax);
		
		return "ajax";
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getWanId() {
		return wanId;
	}

	public void setWanId(String wanId) {
		this.wanId = wanId;
	}

	public String getWanConnId() {
		return wanConnId;
	}

	public void setWanConnId(String wanConnId) {
		this.wanConnId = wanConnId;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public String getWanConnSessionType() {
		return wanConnSessionType;
	}

	public void setWanConnSessionType(String wanConnSessionType) {
		this.wanConnSessionType = wanConnSessionType;
	}

	public String getWanConnSessionId() {
		return wanConnSessionId;
	}

	public void setWanConnSessionId(String wanConnSessionId) {
		this.wanConnSessionId = wanConnSessionId;
	}
}
