package action.resource;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.linkage.litms.system.UserRes;

import dao.resource.AdvancedDevInfoDAO;

/**
 * @author Jason(3412)
 * @date 2009-5-22
 */
public class AdvancedDevInfoAction implements SessionAware {

	// 查询结果设备列表
	private List devList;
	//查询到的设备数
	private int resultNum;
	// 查询设备序列号
	private String devSn;
	// dao
	private AdvancedDevInfoDAO devDAO;
	// session
	private Map session;

	/**
	 * 查询设备
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-5-26
	 * @return String
	 */
	public String execute() {
		UserRes curUser = (UserRes) session.get("curUser");
		boolean isAdmin = curUser.getUser().isAdmin();
		if (isAdmin) {
			devList = devDAO.queryDevice(devSn);
		} else {
			long areaId = curUser.getAreaId();
			devList = devDAO.queryDevice(areaId, devSn);
		}
		devList = devDAO.procList(devList);
		if(null == devList){
			resultNum = 0;	
		}else{
			resultNum = devList.size();
		}
		return "queryData";
	}

	/** getter, setter method field */

	public List getDevList() {
		return devList;
	}

	public void setDevSn(String devSn) {
		this.devSn = devSn;
	}
	
	public int getResultNum() {
		return resultNum;
	}

	/**
	 * 
	 */
	public void setDevDAO(AdvancedDevInfoDAO paramDevDAO) {
		this.devDAO = paramDevDAO;
	}

	@Override
	public void setSession(Map arg0) {
		session = arg0;
	}
}
