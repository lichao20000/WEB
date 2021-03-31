/**
 * 
 */
package action.confTaskView;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.system.UserRes;

import dao.confTaskView.ConfTaskViewDao;

/**
 * @author OneLineSky E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2008-12-18
 * @category action.confTaskView
 * 
 */
public class ConfTaskViewAction extends splitPageAction implements
		ServletRequestAware {

	/**
	 * 
	 */
	/** log */
	private static Logger logger = LoggerFactory.getLogger(ConfTaskViewAction.class);
	private static final long serialVersionUID = 1L;

	ConfTaskViewDao confTaskViewDao;

	private String task_name = null;

	private String order_time_start = null;

	private String order_time_end = null;

	private String is_check = null;

	private String is_over = null;

	private List confTaskList = null;

	// request取登陆帐号使用
	private HttpServletRequest request;

	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	public String startQuery() throws Exception {

		String newDateString = new DateTimeUtil().getDate();
		
		order_time_start = newDateString;
		order_time_end = newDateString;
		
		return "query";
	}
	
	public String execute() throws Exception {

		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");

		long area_id = curUser.getAreaId();

		confTaskList = confTaskViewDao.getConfTask(curPage_splitPage,
				num_splitPage, area_id, task_name, order_time_start,
				order_time_end, is_check, is_over);

		maxPage_splitPage = confTaskViewDao.getConfTaskCount(num_splitPage,
				area_id, task_name, order_time_start, order_time_end, is_check,
				is_over);

		logger.debug("ConfTaskViewAction:execute=>" + maxPage_splitPage);
		
		return "confTaskList";
	}

	public String goPage() throws Exception
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");

		long area_id = curUser.getAreaId();
		
		confTaskList = confTaskViewDao.getConfTask(curPage_splitPage,
				num_splitPage, area_id, task_name, order_time_start,
				order_time_end, is_check, is_over);
		
		maxPage_splitPage = confTaskViewDao.getConfTaskCount(num_splitPage,
				area_id, task_name, order_time_start, order_time_end, is_check,
				is_over);
		
		return "confTaskList";
	}
	
	public ConfTaskViewDao getConfTaskViewDao() {
		return confTaskViewDao;
	}

	public void setConfTaskViewDao(ConfTaskViewDao confTaskViewDao) {
		this.confTaskViewDao = confTaskViewDao;
	}

	public String getIs_check() {
		return is_check;
	}

	public void setIs_check(String is_check) {
		this.is_check = is_check;
	}

	public String getIs_over() {
		return is_over;
	}

	public void setIs_over(String is_over) {
		this.is_over = is_over;
	}

	public String getOrder_time_end() {
		return order_time_end;
	}

	public void setOrder_time_end(String order_time_end) {
		this.order_time_end = order_time_end;
	}

	public String getOrder_time_start() {
		return order_time_start;
	}

	public void setOrder_time_start(String order_time_start) {
		this.order_time_start = order_time_start;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}

	public List getConfTaskList() {
		return confTaskList;
	}

	public void setConfTaskList(List confTaskList) {
		this.confTaskList = confTaskList;
	}

}
