package dao.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;

/**
 * 定制终端模板管理 DAO
 * 
 * @author 段光锐（5250）
 * @version 1.0
 * @since 2008-6-11
 * @category 定制终端模板管理
 * 
 */
public class TerminalTemplateDAO
{
	// jdbc模板
	private JdbcTemplate jt;

	
	/**
	 * 添加定制终端模板的SQL
	 */
	private final String addTemplateSQL = "insert into tab_terminal_template(template_id,template_name,vendor_id,device_type,access_style_id,max_lan_num,max_ssid_num,max_wan_num) ";

	/**
	 * 添加终端定制WAN口模板表SQL
	 */
	private final String addWanPortTemplateSQL = "insert into tab_wanport_template (template_id,wanport_idex,addresspool,wan_type) ";

	/**
	 * 删除终端定制WAN口模板SQL
	 */
	private final String delWanPortTemplateSQL = "delete from tab_wanport_template where template_id=";

	/**
	 * 删除定制终端模板的SQL
	 */
	private final String delTemplateSQL = "delete from tab_terminal_template where template_id=";

	/**
	 * 查看定制终端模板详情SQL
	 */
	private final String getTemplateDetailInfoSQL = "select template_id,template_name,vendor_id,device_type,access_style_id,"
			+ "max_lan_num,max_ssid_num,max_wan_num from tab_terminal_template where template_id=";

	/**
	 * 查看终端定制WAN口模板详情SQL
	 */
	private final String getTemplateWANPortInfoSQL = "select wanport_idex,addresspool,wan_type from tab_wanport_template where template_id=";

	/**
	 * 初始化数据库连接
	 * 
	 * @param dao
	 */
	public void setDao(DataSource dao)
	{
		jt = new JdbcTemplate(dao);
	}

	/**
	 * 获取设备厂商列表
	 * 
	 * @return
	 */
	public List getVendorList()
	{
		String getVendorListSQL = "select vendor_id,vendor_name,vendor_add from tab_vendor";
		PrepareSQL psql = new PrepareSQL(getVendorListSQL);
		psql.getSQL();
		return jt.queryForList(getVendorListSQL);
	}

	/**
	 * 新增定制终端模板信息
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
	 *            WAN端口portNum支持最大地址数
	 * @param wan_type
	 *            WAN端口portNum连接方式
	 * @return
	 */
	public boolean addTemplate(long templateId, String template_name,
			String vendor_id, String device_type, int access_style_id,
			int max_lan_num, int max_ssid_num, int max_wan_num,
			String[] addresspool, String[] wan_type)
	{
		boolean flag = false;
		String addTemplateSQL = this.addTemplateSQL + "values(" + templateId
				+ ",'" + template_name + "','" + vendor_id + "','"
				+ device_type + "'," + access_style_id + "," + max_lan_num
				+ "," + max_ssid_num + "," + max_wan_num + ")";
		PrepareSQL psql = new PrepareSQL(addTemplateSQL);
		psql.getSQL();
		int addNum = 0;
		addNum = jt.update(addTemplateSQL);
		flag = (addNum > 0 ? true : false);
		if (flag)
		{
			// 如果模板添加成功,则在添加每个WAN端口的信息模板
			flag = saveWanPortTemplate(templateId, max_wan_num, addresspool,
					wan_type);
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
	 *            WAN端口portNum支持最大地址数
	 * @param wan_type
	 *            WAN端口portNum连接方式
	 * @return
	 */
	public boolean updateTemplate(long templateId, String template_name,
			String vendor_id, String device_type, int access_style_id,
			int max_lan_num, int max_ssid_num, int max_wan_num,
			String[] addresspool, String[] wan_type)
	{
		boolean flag = false;
		String updateTemplate = "update tab_terminal_template set template_name='"
				+ template_name
				+ "', vendor_id='"
				+ vendor_id
				+ "',device_type='"
				+ device_type
				+ "',access_style_id="
				+ access_style_id
				+ ",max_lan_num="
				+ max_lan_num
				+ ",max_ssid_num="
				+ max_ssid_num
				+ ",max_wan_num="
				+ max_wan_num + "  where template_id=" + templateId;
		PrepareSQL psql = new PrepareSQL(updateTemplate);
		psql.getSQL();
		int updateNum = 0;
		updateNum = jt.update(updateTemplate);
		flag = (updateNum > 0 ? true : false);
		if (flag)
		{
			// 如果模板添加成功,则在添加每个WAN端口的信息模板
			flag = saveWanPortTemplate(templateId, max_wan_num, addresspool,
					wan_type);
		}
		return flag;
	}

	/**
	 * 保存终端定制WAN口模板表信息,新增和修改都是调用该方法
	 * 
	 * @param templateId
	 *            模板ID
	 * @param max_wan_num
	 *            最大支持LAN端口数
	 * @param addresspool
	 *            WAN口支持的最大地址池数
	 * 
	 * @param wan_type
	 *            WAN口连接方式
	 * 
	 * @return
	 */
	private boolean saveWanPortTemplate(long templateId, int max_wan_num,
			String[] addresspool, String[] wan_type)
	{
		String[] bacthSQL = new String[max_wan_num + 1];
		bacthSQL[0] = this.delWanPortTemplateSQL + templateId;
		PrepareSQL psql = new PrepareSQL(bacthSQL[0]);
		psql.getSQL();
		String sql = this.addWanPortTemplateSQL + " values(";
		for (int i = 0; i < max_wan_num; i++)
		{
			bacthSQL[i + 1] = sql + templateId + "," + (i + 1) + ","
					+ addresspool[i] + "," + wan_type[i] + ")";
			psql = new PrepareSQL(bacthSQL[i + 1]);
			psql.getSQL();
		}
		int[] saveNum = jt.batchUpdate(bacthSQL);
		int len = saveNum.length;
		for (int i = 1; i < len; i++)
		{
			if (saveNum[i] != 1)
				return false;
		}
		return true;
	}

	/**
	 * 展示模板列表信息
	 * 
	 * @return
	 */
	public List showTemplateList()
	{
		String showTemplateListSQL = "select template_id,template_name from tab_terminal_template";
		PrepareSQL psql = new PrepareSQL(showTemplateListSQL);
		psql.getSQL();
		return jt.queryForList(showTemplateListSQL);
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
		String[] bacthSQL = new String[2];
		bacthSQL[0] = this.delWanPortTemplateSQL + templateId;
		PrepareSQL psql = new PrepareSQL(bacthSQL[0]);
		psql.getSQL();
		bacthSQL[1] = this.delTemplateSQL + templateId;
		psql = new PrepareSQL(bacthSQL[1]);
		psql.getSQL();
		int[] delNum = jt.batchUpdate(bacthSQL);
		int len = delNum.length;
		for (int i = 1; i < len; i++)
		{
			if (delNum[i] != 1)
				return false;
		}
		return true;
	}

	/**
	 * 获取模板详情信息
	 * 
	 * @param templateId
	 *            模板ID
	 * @return List
	 */
	public List<Map>[] getTemplateDetailInfo(long templateId)
	{
		String getTemplateDetailInfoSQL = this.getTemplateDetailInfoSQL
				+ templateId;
		PrepareSQL psql = new PrepareSQL(getTemplateDetailInfoSQL);
		psql.getSQL();
		String getTemplateWANPortInfoSQL = this.getTemplateWANPortInfoSQL
				+ templateId;
		psql = new PrepareSQL(getTemplateWANPortInfoSQL);
		psql.getSQL();
		List[] listArr = new ArrayList[2];
		listArr[0] = jt.queryForList(getTemplateDetailInfoSQL);
		listArr[1] = jt.queryForList(getTemplateWANPortInfoSQL);
		return listArr;
	}

	/**
	 * 获取定制终端模板表中的最大模板ID
	 * 
	 * @return
	 */
	public long getMaxTemplateId()
	{
		String getMaxTemplateIdSQL = "select max(template_id) from tab_terminal_template";
		PrepareSQL psql = new PrepareSQL(getMaxTemplateIdSQL);
		psql.getSQL();
		long result = jt.queryForLong(getMaxTemplateIdSQL);
		return result;
	}

}
