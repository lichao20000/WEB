package action.webtopo.warn;

import static action.cst.AJAX;
import static action.cst.EDIT;
import static action.cst.LIST;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bio.webtopo.warn.WarnFilterBIO;

import com.linkage.litms.system.UserRes;
import com.opensymphony.xwork2.ActionSupport;

import dao.webtopo.warn.WarnFilterDAO;

/**
 * WebTopo实时告警牌告警规则定义与修改Action
 * <li>REQ: GZDX-REQ-20080402-ZYX-001
 * <li>REQ: HBDX-REQ-20080220-WANGBING-001
 * 
 * @author 段光锐
 * @version 1.0
 * @since 2008-4-8
 * @category WebTopo/实时告警牌/告警规则
 * 
 */
public class WarnRuleDefAction extends ActionSupport implements SessionAware
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2479266209320911424L;

	private static Logger m_logger = LoggerFactory.getLogger(WarnRuleDefAction.class);

	private Map session;// 当前会话

	private WarnFilterBIO warnFilterBio = null;// 业务封装类

	private WarnFilterDAO warnFilterDao = null;// 数据库访问对象

	private String acc_loginname = null;// 当前用户的登陆名

	private long ruleId = -1;// 告警规则ID

	private String ruleName = null;// 告警规则名称

	private String userName = null; // 规则所属用户名

	private long maxNum = 200;// 最大显示告警数

	private int selected = 0;// 是否默认模板

	private int isPublic = 0;// 是否共享

	private int visible = 1;// 符合过滤条件的告警是否展示

	private List resultList = null;// 查询返回的结果

	private List shareList = null;// 共享规则模板的结果集

	private String rulePriority = null;// 规则优先级

	private String ruleContent = null;// 规则内容

	private String ruleInvocation = null;// 是否启用

	private int ruleLength = 0;// 规则数量

	private String ajax = "";

	/**
	 * 获取告警规则模板信息
	 */
	public String execute() throws Exception
	{
		initLoginName();
		if (acc_loginname != null)
		{
			resultList = warnFilterDao.getWarnRuleByUser(acc_loginname);
			shareList = warnFilterDao.getShareWarnRuleExceptUser(acc_loginname);
		}
		m_logger.debug("resultList = " + resultList);
		m_logger.debug("shareList = " + shareList);
		// SUCCESS = /webtopo/warn/WarnRuleDef.jsp
		return SUCCESS;
	}

	/**
	 * 根据告警规则ID来显示详细告警规则,跳转到可编辑页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String editDetailInfo() throws Exception
	{
		getWarnRuleDetailByRuleId();
		return EDIT;
	}

	/**
	 * 根据告警规则ID来显示详细告警规则,跳转到不可编辑页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String showDetailInfo() throws Exception
	{
		getWarnRuleDetailByRuleId();
		return LIST;
	}

	/**
	 * 将其他用户的共享规则模板Copy到当前用户下
	 * 
	 * @return
	 * @throws Exception
	 */
	public String copyRuleInfo() throws Exception
	{
		boolean flag = false;
		initLoginName();
		if ((acc_loginname != null) && (ruleId > -1)){
			flag = warnFilterBio.copyRuleInfo(acc_loginname, ruleId);
		}
		ajax = (flag ? "1" : "0");
		return AJAX;
	}

	/**
	 * 保存告警规则模板
	 * 
	 * @return
	 * @throws Exception
	 */
	public String saveRuleInfo() throws Exception
	{
		boolean flag = false;
		initLoginName();
		ruleName = myURLDecoder(ruleName);
		m_logger.debug("acc_loginname = " + acc_loginname + " & ruleId = "
				+ ruleId + " & ruleName = " + ruleName + " & maxNum = "
				+ maxNum + " & selected = " + selected + " & isPublic = "
				+ isPublic + " & show = " + visible);
		if (ruleId > -1)
		{
			m_logger.debug("修改告警规则模板 ...");
			// 修改告警规则
			flag = warnFilterBio.updateRuleInfo(acc_loginname, ruleId,
					ruleName, maxNum, selected, isPublic, visible);
		} else
		{
			m_logger.debug("添加告警规则模板 ...");
			// 添加告警规则
			flag = warnFilterBio.addRuleInfo(acc_loginname, ruleName, maxNum,
					selected, isPublic, visible);
		}
		// execute();
		ajax = (flag ? "1" : "0");
		return AJAX;
	}

	/**
	 * 删除告警规则模板
	 * 
	 * @return
	 * @throws Exception
	 */
	public String delRuleInfo() throws Exception
	{
		boolean flag = false;
		if (ruleId > -1){
			flag = warnFilterBio.delRuleInfo(ruleId);
		}
		// execute();
		ajax = (flag ? "1" : "0");
		return AJAX;
	}

	/**
	 * 保存告警详情
	 * 
	 * @return
	 * @throws Exception
	 */
	public String saveDetailInfo() throws Exception
	{
		boolean flag = false;
		ruleContent = myURLDecoder(ruleContent);
		m_logger.debug("rulePriority = " + rulePriority);
		m_logger.debug("ruleContent = " + ruleContent);
		m_logger.debug("ruleInvocatio = " + ruleInvocation);
		m_logger.debug("ruleLength = " + ruleLength);
		flag = warnFilterBio.saveRuleDetailInfo(ruleId, rulePriority,
				ruleContent, ruleInvocation, ruleLength);
		m_logger.debug("flag = " + flag);
		ajax = (flag ? "1" : "0");
		return AJAX;
	}

	/**
	 * 初始化acc_loginname
	 */
	private void initLoginName()
	{
		if (acc_loginname == null){
			acc_loginname = ((UserRes) session.get("curUser")).getUser()
					.getAccount();
		}
	}

	/**
	 * 通过<code>ruleId</code>获取告警规则模板的详情
	 * 
	 * @throws Exception
	 */
	private void getWarnRuleDetailByRuleId() throws Exception
	{
		if (ruleId > -1){
			resultList = warnFilterDao.getWarnRuleDetailByRuleId(ruleId);
		}
		m_logger.debug("resultList = " + resultList);
	}

	/**
	 * 转码
	 * 
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String myURLDecoder(String str) throws UnsupportedEncodingException
	{
		return URLDecoder.decode(str, "UTF-8");
	}

	public void setSession(Map session)
	{
		this.session = session;

	}

	public void setWarnFilterBio(WarnFilterBIO warnFilterBio)
	{
		this.warnFilterBio = warnFilterBio;
	}

	public void setWarnFilterDao(WarnFilterDAO warnFilterDao)
	{
		this.warnFilterDao = warnFilterDao;
	}

	public void setRuleId(long ruleId)
	{
		this.ruleId = ruleId;
	}

	public long getRuleId()
	{
		return ruleId;
	}

	public List getResultList()
	{
		return resultList;
	}

	public List getShareList()
	{
		return shareList;
	}

	public void setRuleName(String ruleName)
	{
		this.ruleName = ruleName;
	}

	public String getRuleName()
	{
		return ruleName;
	}

	public void setMaxNum(long maxNum)
	{
		this.maxNum = maxNum;
	}

	public void setSelected(int selected)
	{
		this.selected = selected;
	}

	public void setIsPublic(int isPublic)
	{
		this.isPublic = isPublic;
	}

	public void setVisible(int visible)
	{
		this.visible = visible;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setRulePriority(String rulePriority)
	{
		this.rulePriority = rulePriority;
	}

	public void setRuleContent(String ruleContent)
	{
		this.ruleContent = ruleContent;
	}

	public void setRuleInvocation(String ruleInvocation)
	{
		this.ruleInvocation = ruleInvocation;
	}

	public void setRuleLength(int ruleLength)
	{
		this.ruleLength = ruleLength;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

}
