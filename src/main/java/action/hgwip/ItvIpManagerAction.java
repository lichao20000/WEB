package action.hgwip;

import static action.cst.ADD;
import static action.cst.AJAX;
import static action.cst.DEL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;
import bio.hgwip.IPGlobal;

import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.opensymphony.xwork2.ActionSupport;

import dao.hgwip.IPManagerDAO;
import dao.hgwip.ItvIPManagerDAO;
import dao.hgwip.SubnetOperationDAO;

/**
 * ip地址管理的入口类，起始action
 * 
 * @author liyl10(71496) 
 * @version 1.0
 * @since 2015-01-29
 * @category itvipmg
 */
public class ItvIpManagerAction extends splitPageAction implements SessionAware
{
	/**
	 * 
	 */
	/** log */
	private static Logger logger = LoggerFactory.getLogger(ItvIpManagerAction.class);
	private static final long serialVersionUID = -1619955441753753481L;
	private Map session;// 服务器的会话对象
	private ItvIPManagerDAO itvdao;
	private UserRes res;// 用户资源
	private String ajax;// ajax结果的字符串
	
	private String cityId;
	private String startIp;
	private String endIp;
	private String execTime;
	private String assigner;//制订人
	private List<Map<String,String>> cityList;
	private String start_time = "";
	private String end_time = "";
	
	private int ipId;
	
	private List<Map> ipList;
	
	/**
	 * 起始的查询界面
	 */
	@Override
	public String execute() throws Exception
	{
		//res = (UserRes) session.get("curUser");
		UserRes curUser = WebUtil.getCurrentUser();
		String city_id = curUser.getCityId();
		long acc_oid = curUser.getUser().getId();
		cityList = CityDAO.getAllNextCityListByCityPid(city_id);//itvdao.getCityList(city_id);
		ipList = itvdao.getIpList(curPage_splitPage, num_splitPage, start_time, end_time, acc_oid, "ip");
		maxPage_splitPage = itvdao.getIpListMax(curPage_splitPage, num_splitPage, start_time, end_time, acc_oid, "ip");
		return SUCCESS;
	}
	
	
	/**
	 * ip地址划分新增页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addInit() throws Exception
	{
		//res = (UserRes) session.get("curUser");
		UserRes curUser = WebUtil.getCurrentUser();
		String city_id = curUser.getCityId();
		cityList = CityDAO.getAllNextCityListByCityPid(city_id);//itvdao.getCityList(city_id);
		return "addInit";
	}
	
	/**
	 * 新增ip地址划分
	 * 
	 * @return
	 * @throws Exception
	 */
	public String add() throws Exception
	{
		logger.warn("ItvIpManagerAction--add()");
		//res = (UserRes) session.get("curUser");
		UserRes curUser = WebUtil.getCurrentUser();
		String city_id = curUser.getCityId();
		long acc_oid = curUser.getUser().getId();
		cityList = CityDAO.getAllNextCityListByCityPid(city_id);//itvdao.getCityList(city_id);
		int flag = itvdao.insertInto(acc_oid, cityId, startIp, endIp);
		if(flag == 1){
			ajax = "添加成功！";
		}else{
			ajax = "添加失败！";
		}
		return "add";
	}
	
	/**
	 * 新增ip地址划分
	 * 
	 * @return
	 * @throws Exception
	 */
	public String edit() throws Exception
	{
		logger.warn("ItvIpManagerAction--edit()");
		//res = (UserRes) session.get("curUser");
		UserRes curUser = WebUtil.getCurrentUser();
		String city_id = curUser.getCityId();
		cityList = CityDAO.getAllNextCityListByCityPid(city_id);//itvdao.getCityList(city_id);
		int flag = itvdao.update(ipId, startIp, endIp);
		if(flag == 1){
			ajax = "修改成功！";
		}else{
			ajax = "修改失败！";
		}
		return "edit";
	}
	
	
	/**
	 * 删除ip地址划分
	 * 
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception
	{
		//res = (UserRes) session.get("curUser");
		UserRes curUser = WebUtil.getCurrentUser();
		long acc_oid = curUser.getUser().getId();
		int flag = itvdao.delete(ipId, acc_oid);
		if(flag == 1){
			ajax = "删除成功！";
		}else{
			ajax = "删除失败！";
		}
		return "del";
	}
	
	public void setSession(Map session)
	{
		this.session = session;
	}
	public String getAjax()
	{
		return ajax;
	}
	
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getStartIp() {
		return startIp;
	}
	public void setStartIp(String startIp) {
		this.startIp = startIp;
	}
	public String getEndIp() {
		return endIp;
	}
	public void setEndIp(String endIp) {
		this.endIp = endIp;
	}
	public String getExecTime() {
		return execTime;
	}
	public void setExecTime(String execTime) {
		this.execTime = execTime;
	}
	public String getAssigner() {
		return assigner;
	}
	public void setAssigner(String assigner) {
		this.assigner = assigner;
	}
	public List<Map<String,String>> getCityList() {
		return cityList;
	}
	public void setCityList(List<Map<String,String>> cityList) {
		this.cityList = cityList;
	}
	public ItvIPManagerDAO getItvdao() {
		return itvdao;
	}
	public void setItvdao(ItvIPManagerDAO itvdao) {
		this.itvdao = itvdao;
	}
	public List<Map> getIpList() {
		return ipList;
	}
	public void setIpList(List<Map> ipList) {
		this.ipList = ipList;
	}
	public String getStart_time() {
		return start_time;
	}


	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	
	public String getEnd_time() {
		return end_time;
	}


	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	
	public int getIpId() {
		return ipId;
	}


	public void setIpId(int ipId) {
		this.ipId = ipId;
	}

}
