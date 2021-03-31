package action.resource;

import static action.cst.AJAX;
import static action.cst.EDIT;
import static action.cst.LIST;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bio.resource.TerminalTemplateBIO;

import com.opensymphony.xwork2.ActionSupport;
/**
 * 定制终端模板管理 Action
 * 
 * @author 段光锐（5250）
 * @version 1.0
 * @since 2008-6-11
 * @category 定制终端模板管理
 */
public class TerminalTemplateAction extends ActionSupport
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2691143652197062865L;

	private static  Logger log = LoggerFactory.getLogger(TerminalTemplateAction.class);

	TerminalTemplateBIO terminalTemplateBio = null;

	private List resultList = null;// 查询返回的结果

	private List wanPortList = null; // WAN 端口信息结果集

	private List vendorList = null;// 查询返回的结果

	private int action = 0;// 操作类型 ,0:无操作 1:新增 2:编辑

	private int flag = 0; // 操作是否成功,返回给页面提示信息

	private long templateId = 0;// 模板ID,删除和修改模板时使用

	private String template_name = null;// 模板名称

	private String vendor_id = null;// 厂商ID

	private String device_type = null;// 定制终端类型

	private int access_style_id = 0;// 管理方式 0:ADSL接入 1:LAN接入

	private int max_lan_num = 0;// 最大支持LAN端口数

	private int max_ssid_num = 0;// 最大支持SSID数

	private int max_wan_num = 0;// 最大支持WAN端口数

	private String addresspool = null;// WAN端口portNum支持最大地址数

	private String wan_type = null;// WAN端口portNum连接方式
	
	private String ajax = null;// ajax返回信息

	/**
	 * 保存模板信息
	 */
	public String execute() throws Exception
	{
		vendorList = terminalTemplateBio.getVendorList();
		log.debug("vendorList = " + vendorList);
		if (action == 1)
		{
			// 新增模板
			log.debug("template_name = " + template_name);
			log.debug("vendor_id = " + vendor_id);
			log.debug("device_type = " + device_type);
			log.debug("access_style_id = " + access_style_id);
			log.debug("max_lan_num = " + max_lan_num);
			log.debug("max_ssid_num = " + max_ssid_num);
			log.debug("max_wan_num = " + max_wan_num);
			log.debug("addresspool = " + addresspool);
			log.debug("wan_type = " + wan_type);
			boolean saveFlag = terminalTemplateBio.addTemplate(template_name,
					vendor_id, device_type, access_style_id, max_lan_num,
					max_ssid_num, max_wan_num, addresspool, wan_type);
			flag = (saveFlag ? 1 : 0);
			log.debug("flag = " + flag);
		} else if (action == 2)
		{
			// 编辑模板
			log.debug("templateId = " + templateId);
			log.debug("template_name = " + template_name);
			log.debug("vendor_id = " + vendor_id);
			log.debug("device_type = " + device_type);
			log.debug("access_style_id = " + access_style_id);
			log.debug("max_lan_num = " + max_lan_num);
			log.debug("max_ssid_num = " + max_ssid_num);
			log.debug("max_wan_num = " + max_wan_num);
			log.debug("addresspool = " + addresspool);
			log.debug("wan_type = " + wan_type);
			boolean saveFlag = terminalTemplateBio.updateTemplate(templateId,
					template_name, vendor_id, device_type, access_style_id,
					max_lan_num, max_ssid_num, max_wan_num, addresspool,
					wan_type);
			flag = (saveFlag ? 1 : 0);
			log.debug("flag = " + flag);
			return editTemplate();
		}
		return SUCCESS;
	}

	/**
	 * 显示模板列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String showList() throws Exception
	{
		resultList = terminalTemplateBio.showTemplateList();
		return LIST;
	}

	/**
	 * 编辑模板
	 * 
	 * @return
	 * @throws Exception
	 */
	public String editTemplate() throws Exception
	{
		log.debug("编辑模板 templateId = " + templateId);
		if (vendorList == null){
			vendorList = terminalTemplateBio.getVendorList();
		}
		log.debug("vendorList = " + vendorList);
		List[] listArr = terminalTemplateBio.getTemplateDetailInfo(templateId);
		resultList = listArr[0];
		wanPortList = listArr[1];
		log.debug("resultList = " + resultList);
		log.debug("wanPortList = " + wanPortList);
		return EDIT;
	}

	/**
	 * 删除模板
	 * 
	 * @return
	 * @throws Exception
	 */
	public String delTemplate() throws Exception
	{
		log.debug("删除模板 templateId = " + templateId);
		boolean delFlag = terminalTemplateBio.delTemplate(templateId);
		ajax = (delFlag ? "1" : "0");
		log.debug("ajax = " + ajax);
		return AJAX;
	}

	public void setTerminalTemplateBio(TerminalTemplateBIO terminalTemplateBio)
	{
		this.terminalTemplateBio = terminalTemplateBio;
	}

	public List getResultList()
	{
		return resultList;
	}

	public List getVendorList()
	{
		return vendorList;
	}

	public int getFlag()
	{
		return flag;
	}

	public void setAction(int action)
	{
		this.action = action;
	}

	public int getAction()
	{
		return action;
	}

	public void setTemplate_name(String template_name)
	{
		this.template_name = template_name;
	}

	public void setVendor_id(String vendor_id)
	{
		this.vendor_id = vendor_id;
	}

	public void setDevice_type(String device_type)
	{
		this.device_type = device_type;
	}

	public void setAccess_style_id(int access_style_id)
	{
		this.access_style_id = access_style_id;
	}

	public void setMax_lan_num(int max_lan_num)
	{
		this.max_lan_num = max_lan_num;
	}

	public void setMax_ssid_num(int max_ssid_num)
	{
		this.max_ssid_num = max_ssid_num;
	}

	public void setMax_wan_num(int max_wan_num)
	{
		this.max_wan_num = max_wan_num;
	}

	public void setAddresspool(String addresspool)
	{
		this.addresspool = addresspool;
	}

	public void setWan_type(String wan_type)
	{
		this.wan_type = wan_type;
	}

	public void setTemplateId(long templateId)
	{
		this.templateId = templateId;
	}

	public String getAjax()
	{
		return ajax;
	}

	public List getWanPortList()
	{
		return wanPortList;
	}

}
