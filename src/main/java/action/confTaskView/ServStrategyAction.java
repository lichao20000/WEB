/**
 * 
 */
package action.confTaskView;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.gw.StrategyDao;

import dao.confTaskView.ServStrategyDao;

/**
 * @author ASUS E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2008-12-18
 * @category action.confTaskView
 * 
 */
public class ServStrategyAction extends splitPageAction {

	/**
	 * 
	 */
	/** log */
	private static Logger logger = LoggerFactory.getLogger(ServStrategyAction.class);
	private static final long serialVersionUID = 1L;

	ServStrategyDao servStrategyDao;

	private String task_id = null;

	private String task_name = null;

	private String status = null;

	private String device_serialnumber = null;

	private String service_id = null;

	private List servStrategyList = null;

	private List service_idLsit = null;
	//策略ID
	private String strategyId = null;
	
	private Map servStrategyMap = null;
	
	private List<Map> servStragegyCounts = null;
	
	private String gw_type = null;
	/**
	 * @return the servStragegyCounts
	 */
	public List<Map> getServStragegyCounts()
	{
		return servStragegyCounts;
	}

	
	/**
	 * @param servStragegyCounts the servStragegyCounts to set
	 */
	public void setServStragegyCounts(List<Map> servStragegyCounts)
	{
		this.servStragegyCounts = servStragegyCounts;
	}

	private StrategyDao strategyDao;

	public String execute() throws Exception {

		logger.debug("ServStrategyAction:task_id=>" + task_id);
		
		servStrategyList = servStrategyDao.getServStrategList(
				curPage_splitPage, num_splitPage, task_id, status,
				device_serialnumber, service_id);

		maxPage_splitPage = servStrategyDao.getServStrategyCount(num_splitPage,
				task_id, status, device_serialnumber, service_id);

		return "list";
	}

	@SuppressWarnings("unchecked")
	public String goPage() throws Exception
	{
		if (Global.HBLT.equals(Global.instAreaShortName))
		{
			servStragegyCounts = strategyDao.getStrategyBySN(device_serialnumber,strategyId,gw_type,curPage_splitPage,num_splitPage);
			
			maxPage_splitPage =  strategyDao.getStrategyBySNCounts(device_serialnumber, strategyId,gw_type,num_splitPage);
			
			return "strategyList";
		}
		else
		{
			servStrategyList = servStrategyDao.getServStrategList(curPage_splitPage,
					num_splitPage, task_id, status, device_serialnumber, service_id);
			maxPage_splitPage = servStrategyDao.getServStrategyCount(num_splitPage,
					task_id, status, device_serialnumber, service_id);
			return "list";
		}
	}
	
	public String servStrategyFrame() throws Exception {

		return "initstart";
	}

	public String startQuery() throws Exception {

		task_name = servStrategyDao.getTaskName(task_id);
		
		service_idLsit = servStrategyDao.getAllService_idList();

		return "query";
	}

	public String getDevice_serialnumber() {
		return device_serialnumber;
	}

	public void setDevice_serialnumber(String device_serialnumber) {
		this.device_serialnumber = device_serialnumber;
	}

	public String getService_id() {
		return service_id;
	}

	public void setService_id(String service_id) {
		this.service_id = service_id;
	}

	public ServStrategyDao getServStrategyDao() {
		return servStrategyDao;
	}

	public void setServStrategyDao(ServStrategyDao servStrategyDao) {
		this.servStrategyDao = servStrategyDao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public List getService_idLsit() {
		return service_idLsit;
	}

	public void setService_idLsit(List service_idLsit) {
		this.service_idLsit = service_idLsit;
	}

	public List getServStrategyList() {
		return servStrategyList;
	}

	public void setServStrategyList(List servStrategyList) {
		this.servStrategyList = servStrategyList;
	}

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}
	
	/**
	 * @return the strategyId
	 */
	public String getStrategyId()
	{
		return strategyId;
	}

	/**
	 * @param strategyId
	 *            the strategyId to set
	 */
	public void setStrategyId(String strategyId)
	{
		this.strategyId = strategyId;
	}

	/**
	 * @return the servStrategyMap
	 */
	public Map getServStrategyMap()
	{
		return servStrategyMap;
	}

	/**
	 * @param servStrategyMap
	 *            the servStrategyMap to set
	 */
	public void setServStrategyMap(Map servStrategyMap)
	{
		this.servStrategyMap = servStrategyMap;
	}

	/**
	 * @return the strategyDao
	 */
	public StrategyDao getStrategyDao()
	{
		return strategyDao;
	}

	/**
	 * @param strategyDao
	 *            the strategyDao to set
	 */
	public void setStrategyDao(StrategyDao strategyDao)
	{
		this.strategyDao = strategyDao;
	}

	/**
	 * 通过策略ID得到策略执行结果
	 * 
	 * @author wangsenbo
	 * @date Nov 5, 2009
	 * @return String
	 */
	public String getStrategy()
	{
		servStrategyMap = strategyDao.getStrategyById(strategyId);
		return "strategy";
	}

	/**
	 * 获取策略执行状态 河北用
	 * @author zzs
	 * @date 20181117
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String getStrategyCounts()
	{
		servStragegyCounts = strategyDao.getStrategyBySN(device_serialnumber,strategyId,gw_type,curPage_splitPage,num_splitPage);
		
		maxPage_splitPage =  strategyDao.getStrategyBySNCounts(device_serialnumber, strategyId,gw_type,num_splitPage);
		
		return "strategyList";
	}

	/**
	 * @return the gw_type
	 */
	public String getGw_type()
	{
		return gw_type;
	}
	/**
	 * @param gw_type the gw_type to set
	 */
	public void setGw_type(String gw_type)
	{
		this.gw_type = gw_type;
	}
}
