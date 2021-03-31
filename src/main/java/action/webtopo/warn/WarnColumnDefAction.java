package action.webtopo.warn;

import static action.cst.AJAX;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bio.webtopo.warn.WarnFilterBIO;

import com.opensymphony.xwork2.ActionSupport;

import dao.webtopo.warn.WarnFilterDAO;
/**
 * WebTopo实时告警牌列名显示配置与修改Action,该页面只对管理员开放
 * <li>REQ: GZDX-REQ-20080402-ZYX-001
 * <li>REQ: HBDX-REQ-20080220-WANGBING-001
 * 
 * @author 段光锐
 * @version 1.0
 * @since 2008-4-14
 * @category WebTopo/实时告警牌/告警规则
 * 
 */
public class WarnColumnDefAction extends ActionSupport
{

	private static Logger m_logger = LoggerFactory.getLogger(WarnColumnDefAction.class);

	private WarnFilterBIO warnFilterBio = null;// 业务封装类

	private WarnFilterDAO warnFilterDao = null;// 数据库访问对象

	private List resultList = null;// 查询返回的结果

	private String sequence = null;// 显示顺序

	private String fieldName = null;// 列ID

	private String visible = null;// 是否显示

	private String ajax = "";

	/**
	 * 
	 */
	private static final long serialVersionUID = -4781169330052544773L;

	/**
	 * 显示告警牌列展示配置信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String execute() throws Exception
	{
		resultList = warnFilterDao.getWarnColumn();
		m_logger.debug("resultList = " + resultList);
		// /webtopo/warn/WarnColumnDef.jsp
		return SUCCESS;
	}

	/**
	 * 保存告警牌列展示配置信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String saveWarnColumn() throws Exception
	{
		boolean flag = false;
		m_logger.debug("fieldName = " + fieldName);
		m_logger.debug("visible = " + visible);
		m_logger.debug("sequence = " + sequence);
		flag = warnFilterBio.saveWarnColumnInfo(fieldName, visible, sequence);
		m_logger.debug("flag = " + flag);
		ajax = (flag ? "1" : "0");
		return AJAX;
	}

	public void setWarnFilterBio(WarnFilterBIO warnFilterBio)
	{
		this.warnFilterBio = warnFilterBio;
	}

	public void setWarnFilterDao(WarnFilterDAO warnFilterDao)
	{
		this.warnFilterDao = warnFilterDao;
	}

	public List getResultList()
	{
		return resultList;
	}

	public void setSequence(String sequence)
	{
		this.sequence = sequence;
	}

	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

	public void setVisible(String visible)
	{
		this.visible = visible;
	}

	public String getAjax()
	{
		return ajax;
	}

}
