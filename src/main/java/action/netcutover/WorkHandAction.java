package action.netcutover;

import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;

import dao.netcutover.WorkHandDao;

/**
 * 
 * @author benny
 * @since 2007-10-26
 * @version 1.0
 */

public class WorkHandAction extends ActionSupport {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -3859978282885397788L;
	
	//业务下拉框
	private List<Map> serviceList;
	//Dao
	private WorkHandDao workHandDao;
	
	

	public String execute() throws Exception{
		
		serviceList = workHandDao.getServiceList();
		
		return SUCCESS;
	}

	public void setWorkHandDao(WorkHandDao workHandDao) {
		this.workHandDao = workHandDao;
	}

	public List<Map> getServiceList() {
		return serviceList;
	}

}
