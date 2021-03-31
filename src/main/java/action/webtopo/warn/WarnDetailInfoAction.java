package action.webtopo.warn;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bio.webtopo.warn.WarnDetailInfoBIO;

import com.linkage.litms.system.UserRes;
import com.opensymphony.xwork2.ActionSupport;

/**
 * WebTopo实时告警牌告警详情查询的Action
 * <li>REQ: GZDX-REQ-20080402-ZYX-001
 * <li>REQ: HBDX-REQ-20080220-WANGBING-001
 * 
 * @author 段光锐
 * @version 1.0
 * @since 2008-4-19
 * @category WebTopo/实时告警牌/告警详情查询
 * 
 */
public class WarnDetailInfoAction extends ActionSupport implements SessionAware
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6620768503397890265L;

	private static Logger m_logger = LoggerFactory.getLogger(WarnDetailInfoAction.class);

	private WarnDetailInfoBIO warnDetailInfoBio = null;// 业务封装类

	private List resultList = null;// 查询返回的结果

	private List sysLogList = null; // SysLog的相关结果集

	private String serialNo = null;// 告警序列号

	private int subSerialNo = 0; // 子序列号(写死)

	private long createTime = 0; // 告警创建时间(秒),通过它来拼表名(例如:event_raw_2008_16)

	private String gatherId = null; // 采集点

	private String remark = null; // 告警附加说明

	// 告警确认相关数据,入的是告警知识库表tab_knowledge

	private String subject = null; // 告警主题,相当与event_raw_2008_16表中的displaytitle字段

	private String content = null; // 内容,,相当与event_raw_2008_16表中的displaystring

	private String warnReason = null; // 故障原因

	private String warnResove = null; // 解决方法

	private int deviceType = 0; // 告警设备类型

	private String sourceIP = null; // 告警源IP

	private int createType = 0; // 告警创建者类型

	private String sourceName = null; // 告警源设备名称

	private int firstTime = 1; // 是否是第一次确认

	private boolean flag = false; // 操作结果

	private boolean isSave = false; // 判读是否是保存操作
	
	private String buttonflg = null; // 是否隐藏页面上的确认与清除按钮

	private Map session;// 当前会话

	/**
	 * 通过告警序列号,子序列号,采集点查询告警详情,包含SysLog日志的相关信息
	 */
	public String execute() throws Exception
	{
		if (subject != null)
		{
			// 确认或者清除告警请求
			saveWarnResoveInfo();
		}
		List[] listArr = warnDetailInfoBio.getWarnDetailInfo(serialNo,
				subSerialNo, gatherId, createTime);
		resultList = listArr[0];
		sysLogList = listArr[1];
		m_logger.debug("resultList = " + resultList);
		m_logger.debug("sysLogList = " + sysLogList);
		return SUCCESS;
	}

	/**
	 * 保存告警修复建议的信息
	 * 
	 * @throws Exception
	 */
	private void saveWarnResoveInfo() throws Exception
	{
		isSave = true;
		// 获取当前登陆用户名
		String acc_loginname = ((UserRes) session.get("curUser")).getUser()
				.getAccount();
		m_logger.debug("acc_loginname = " + acc_loginname);
		// 获取告警确认时间,即当前时间
		long curTime = System.currentTimeMillis() / 1000;
		m_logger.debug("开始保存,确认或者清除告警  ... " + firstTime + " " + serialNo + " "
				+ subSerialNo + " " + gatherId + " " + remark + " " + subject
				+ " " + content + " " + acc_loginname + " " + createTime + " "
				+ warnReason + " " + warnResove + " " + curTime + " "
				+ deviceType + " " + sourceIP + " " + createType + " "
				+ sourceName);
		flag = warnDetailInfoBio.saveTabKnowledgeInfo(firstTime, serialNo,
				subSerialNo, gatherId, remark, subject, content, acc_loginname,
				createTime, warnReason, warnResove, curTime, deviceType,
				sourceIP, createType, sourceName);
	}

	public List getResultList()
	{
		return resultList;
	}

	public void setWarnDetailInfoBio(WarnDetailInfoBIO warnDetailInfoBio)
	{
		this.warnDetailInfoBio = warnDetailInfoBio;
	}

	public void setSerialNo(String serialNo)
	{
		this.serialNo = serialNo;
	}

	public void setCreateTime(long createTime)
	{
		this.createTime = createTime;
	}

	public long getCreateTime()
	{
		return createTime;
	}

	public void setGatherId(String gatherId)
	{
		this.gatherId = gatherId;
	}

	public String getGatherId()
	{
		return gatherId;
	}

	public String getSerialNo()
	{
		return serialNo;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public void setWarnReason(String warnReason)
	{
		this.warnReason = warnReason;
	}

	public void setWarnResove(String warnResove)
	{
		this.warnResove = warnResove;
	}

	public void setDeviceType(int deviceType)
	{
		this.deviceType = deviceType;
	}

	public void setSourceIP(String sourceIP)
	{
		this.sourceIP = sourceIP;
	}

	public void setCreateType(int createType)
	{
		this.createType = createType;
	}

	public void setSourceName(String sourceName)
	{
		this.sourceName = sourceName;
	}

	public void setFirstTime(int firstTime)
	{
		this.firstTime = firstTime;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public boolean getFlag()
	{
		return flag;
	}

	public boolean getIsSave()
	{
		return isSave;
	}

	public List getSysLogList()
	{
		return sysLogList;
	}

	public String getButtonflg()
	{
		return buttonflg;
	}

	public void setButtonflg(String buttonflg)
	{
		this.buttonflg = buttonflg;
	}

	public int getSubSerialNo() {
		return subSerialNo;
	}

	public void setSubSerialNo(int subSerialNo) {
		this.subSerialNo = subSerialNo;
	}

}
