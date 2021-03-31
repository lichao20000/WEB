package action.netcutover;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import dao.netcutover.HandSheetDao;
import dao.netcutover.WorkHandDao;

public class WorkSheetAction extends ActionSupport {

	/**
	 * @author lizj （5202）
	 * @since
	 */
	/** log */
	private static Logger logger = LoggerFactory.getLogger(WorkSheetAction.class);
	private static final long serialVersionUID = -1714286655911777466L;

	private int serviceid;

	private String user_name;

	private String erroMsg;// 错误提示信息

	private HandSheetDao handSheetDao;

	private List<Map> user;
	
	//业务下拉框
	private List<Map> serviceList;
	
	private WorkHandDao workHandDao;

	/*
	 * 
	 */

	public String execute() throws Exception {
		
		
		serviceList = workHandDao.getServiceList();

		if (user_name == null || user_name.equals("")) {

			logger.debug("usernameError :" + user_name);
			setErroMsg(getText("USER_ERROR"));
			return INPUT;
		}

		user = handSheetDao.getUserInfo(user_name);

		if (user == null || user.size() < 1) {
			setErroMsg(getText("NO_USER_ERROR"));
			return INPUT;
		}

		return SUCCESS;

	}

	public void setHandSheetDao(HandSheetDao handSheetDao) {
		this.handSheetDao = handSheetDao;
	}



	public int getServiceid() {
		return serviceid;
	}

	public void setServiceid(int serviceid) {
		this.serviceid = serviceid;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public void setErroMsg(String erroMsg) {
		this.erroMsg = erroMsg;
	}

	public String getErroMsg() {
		return erroMsg;
	}

	public List<Map> getUser() {
		return user;
	}

	public List<Map> getServiceList() {
		return serviceList;
	}

	public void setWorkHandDao(WorkHandDao workHandDao) {
		this.workHandDao = workHandDao;
	}

	public void setServiceList(List<Map> serviceList) {
		this.serviceList = serviceList;
	}

}
