package bio.resource;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dao.resource.TerminalTemplateDAO;

/**
 * 定制终端模板管理 BIO
 * 
 * @author 段光锐（5250）
 * @version 1.0
 * @since 2008-6-11
 * @category 定制终端模板管理
 * 
 */
public class TerminalTemplateBIO
{
	TerminalTemplateDAO terminalTemplateDao = null;

	/**
	 * 同步锁,在新增告警规则模板的时候用到
	 */
	private final static Object LOCK = new Object();

	public void setTerminalTemplateDao(TerminalTemplateDAO terminalTemplateDao)
	{
		this.terminalTemplateDao = terminalTemplateDao;
	}

	/**
	 * 获取设备厂商列表
	 * 
	 * @return
	 */
	public List getVendorList()
	{
		return terminalTemplateDao.getVendorList();
	}

	/**
	 * 新增定制终端模板信息
	 * 
	 * @param template_name
	 *            模板名称
	 * @param vendor_id
	 *            厂商ID
	 * @param device_type
	 *            定制终端类型
	 * @param access_style_id
	 *            管理方式 0:ADSL接入 1:LAN接入
	 * @param max_lan_num
	 *            最大支持LAN端口数
	 * @param max_ssid_num
	 *            最大支持SSID数
	 * @param max_wan_num
	 *            最大支持WAN端口数
	 * @param addresspool
	 *            WAN端口portNum支持最大地址数,逗号分隔
	 * @param wan_type
	 *            WAN端口portNum连接方式,逗号分隔
	 * @return
	 */
	public boolean addTemplate(String template_name, String vendor_id,
			String device_type, int access_style_id, int max_lan_num,
			int max_ssid_num, int max_wan_num, String addresspool,
			String wan_type)
	{
		boolean flag = false;
		long templateId = -1;
		synchronized (LOCK)
		{
			// 获取最大模板ID,在此处加1
			templateId = terminalTemplateDao.getMaxTemplateId() + 1;
			// 开始保存模板信息
			flag = terminalTemplateDao.addTemplate(templateId, template_name,
					vendor_id, device_type, access_style_id, max_lan_num,
					max_ssid_num, max_wan_num, toArray(addresspool),
					toArray(wan_type));

		}
		return flag;
	}

	/**
	 * 修改定制终端模板信息
	 * 
	 * @param templateId
	 *            模板ID
	 * @param template_name
	 *            模板名称
	 * @param vendor_id
	 *            厂商ID
	 * @param device_type
	 *            定制终端类型
	 * @param access_style_id
	 *            管理方式 0:ADSL接入 1:LAN接入
	 * @param max_lan_num
	 *            最大支持LAN端口数
	 * @param max_ssid_num
	 *            最大支持SSID数
	 * @param max_wan_num
	 *            最大支持WAN端口数
	 * @param addresspool
	 *            WAN端口portNum支持最大地址数,逗号分隔
	 * @param wan_type
	 *            WAN端口portNum连接方式,逗号分隔
	 * @return
	 */
	public boolean updateTemplate(long templateId, String template_name,
			String vendor_id, String device_type, int access_style_id,
			int max_lan_num, int max_ssid_num, int max_wan_num,
			String addresspool, String wan_type)
	{
		boolean flag = false;
		// 开始保存模板信息
		flag = terminalTemplateDao.updateTemplate(templateId, template_name,
				vendor_id, device_type, access_style_id, max_lan_num,
				max_ssid_num, max_wan_num, toArray(addresspool),
				toArray(wan_type));
		return flag;
	}

	/**
	 * 展示模板列表信息
	 * 
	 * @return
	 */
	public List showTemplateList()
	{
		return terminalTemplateDao.showTemplateList();
	}

	/**
	 * 删除模板信息
	 * 
	 * @param templateId
	 *            模板ID
	 * @return
	 */
	public boolean delTemplate(long templateId)
	{
		return terminalTemplateDao.delTemplate(templateId);
	}

	/**
	 * 获取模板详情信息
	 * 
	 * @param templateId
	 *            模板ID
	 * @return
	 */
	public List<Map>[] getTemplateDetailInfo(long templateId)
	{
		List<Map>[] listArr = terminalTemplateDao
				.getTemplateDetailInfo(templateId);
		return listArr;
	}

	/**
	 * 处理数据库查询获得的数据,将两个List整合成一个List抛到页面上
	 * 
	 * @param listArr
	 *            两个List
	 */
	private Map dealResultList(List<Map>[] listArr)
	{
		// 如果不是两个List,说明数据有问题,返回空Map
		if (listArr.length != 2){
			return new HashMap(0);
		}
		Map resultMap = new HashMap();
		// 这个List中存放的是模板的主要信息
		List list1 = listArr[0];
		// 这个List中存放的是WANport信息
		List list2 = listArr[1];
		
		Iterator<Map> it = list2.iterator();
		Map map = null;
		while (it.hasNext())
		{
			map = it.next();
			int wanport_idex = ((BigDecimal) map.get("wanport_idex"))
					.intValue();
			int addresspool = ((BigDecimal) map.get("addresspool")).intValue();
			int wan_type = ((BigDecimal) map.get("wan_type")).intValue();
			resultMap.put("addresspool" + wanport_idex, addresspool);
			resultMap.put("wan_type" + wanport_idex, wan_type);
		}
		return resultMap;
	}

	/**
	 * 将 <code>str</code> (asdf,sdfsdf,sfdsfa) 转换成数组,分隔符为 (,)
	 * 
	 * @param str
	 * @return
	 */
	private String[] toArray(String str)
	{
		return str.split(",");
	}

}
