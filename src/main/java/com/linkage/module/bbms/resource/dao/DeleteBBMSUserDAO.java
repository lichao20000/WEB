
package com.linkage.module.bbms.resource.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.gwms.util.StringUtil;
import sun.management.resources.agent;

/**
 * 云南未使用定制终端用户删除
 *
 * @author 王森博
 */
@SuppressWarnings("unchecked")
public class DeleteBBMSUserDAO extends SuperDAO
{

	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory.getLogger(DeleteBBMSUserDAO.class);

	/**
	 * 查询用户
	 *
	 * @author wangsenbo
	 * @date Jul 28, 2010
	 * @param
	 * @return Map<String,Map>
	 */
	public Map<String, Map> queryUserByImportUsername(String cityId, List<String> userList)
	{
		logger.debug("queryUserByImportUsername()");
		String tableName = "tab_egwcustomer";
		StringBuffer sql = new StringBuffer();


		sql.append(" select c.user_id,c.username,c.device_id,c.device_serialnumber,c.city_id from ");
		if (LipossGlobals.IsITMS())
		{
			tableName = "tab_hgwcustomer";
			sql.append(tableName);
			sql.append(" c left join tab_gw_device b on c.device_id=b.device_id where c.username in (");

		}else{
			sql.append(tableName);
			sql.append(" a left join tab_gw_device b on a.device_id=b.device_id,tab_customerinfo c where a.customer_id=c.customer_id and a.username in (");

		}
		sql.append(StringUtils.weave(userList)).append(")  ");
		if (null != cityId && !"00".equals(cityId))
		{
			ArrayList cityArray = CityDAO.getAllNextCityIdsByCityPid(cityId);
			sql.append(" and c.city_id in (" + StringUtils.weave(cityArray) + ")");
			cityArray = null;
		}
		PrepareSQL psql = new PrepareSQL(sql.toString());
		List<Map> list = jt.queryForList(psql.getSQL());
		Map<String, Map> map = new HashMap<String, Map>();
		Map cityMap = CityDAO.getCityIdCityNameMap();
		for (Map tmap : list)
		{
			tmap.put("city_name", cityMap.get(tmap.get("city_id")));
			map.put(StringUtil.getStringValue(tmap.get("username")), tmap);
		}
		cityMap = null;
		return map;
	}

	public String deleteUser(List userList)
	{
		// 企业用户表和家庭用户表字段不一样
		String tableName = "";
		if (LipossGlobals.IsITMS())
		{
			tableName = "tab_hgwcustomer";
            bakCustomer(tableName, userList);
		}else{
			tableName = "tab_egwcustomer";
            bakCustomer(tableName, userList);
		}

		StringBuffer deleteSql = new StringBuffer();
		deleteSql.append("delete from ").append(tableName).append(" where user_id in (")
				.append(StringUtils.weaveNumber(userList)).append(")");
		PrepareSQL psql = new PrepareSQL(deleteSql.toString());

		int result = jt.update(psql.getSQL());

		if (result == 1) {
			return "删除成功！";
		} else {
			return "删除失败！";
		}
	}

    /**
	 * 备份用户表
	 * @param tableName
	 * @param userList
	 */
	private void bakCustomer(String tableName, List userList) {

	    // 获取企业用户和家庭用户公共字段
        List<String> columnList = getCommonColumnList();

            // 政企用户表
        if(tableName.equals("tab_egwcustomer")){
            columnList.add("customer_id");
        }else{
            // 家庭用户表
            columnList.add("is_pon");
            columnList.add("is_active");
            columnList.add("user_sub_name");
        }

        // 把需要备份的数据先查出来
        StringBuffer selectSql = new StringBuffer();
        selectSql.append("select ").append(StringUtils.weaveNumber(columnList))
                .append(" from ").append(tableName)
                .append(" where user_id in (").append(StringUtils.weaveNumber(userList)).append(")");
        PrepareSQL psql = new PrepareSQL(selectSql.toString());

        List<Map> list = jt.queryForList(psql.getSQL());

        if(list == null || list.isEmpty()){
            logger.warn("DeleteBBMSUserDAO.bakCustomer 获取用户列表为空, 不需要备份。");
            return;
        }

        // 获取值列表
        List valueList = getValueList(list, columnList);

        // 插入到备份表
        StringBuffer inserttSql = new StringBuffer();
        inserttSql.append("insert into ").append(tableName + "_bak ")
                .append(" (").append(StringUtils.weaveNumber(columnList)).append(") ")
                .append(" values ")
                .append("(").append(StringUtils.weaveNumber(valueList)).append(")")
                .append(" where user_id in (").append(StringUtils.weaveNumber(userList)).append(")");
        psql = new PrepareSQL(inserttSql.toString());

        int result = jt.update(psql.getSQL());
        if(result == 1){
            logger.debug("备份表{}成功", tableName + "_bak ");
        }

    }

    /**
     * 获取企业用户和家庭用户公共字段
     */
    private List<String> getCommonColumnList() {
        List<String> columnList = new ArrayList<String>();
        columnList.add("user_id");
        columnList.add("gather_id");
        columnList.add("username");
        columnList.add("passwd");
        columnList.add("city_id");
        columnList.add("cotno");
        columnList.add("bill_type_id");
        columnList.add("next_bill_type_id");
        columnList.add("cust_type_id");
        columnList.add("user_type_id");
        columnList.add("bindtype");
        columnList.add("virtualnum");
        columnList.add("numcharacter");
        columnList.add("access_style_id");
        columnList.add("aut_flag");
        columnList.add("service_set");
        columnList.add("realname");
        columnList.add("sex");
        columnList.add("cred_type_id");
        columnList.add("credno");
        columnList.add("address");
        columnList.add("office_id");
        columnList.add("zone_id");
        columnList.add("access_kind_id");
        columnList.add("trade_id");
        columnList.add("licenceregno");
        columnList.add("occupation_id");
        columnList.add("education_id");
        columnList.add("vipcardno");
        columnList.add("contractno");
        columnList.add("linkman");
        columnList.add("linkman_credno");
        columnList.add("linkphone");
        columnList.add("linkaddress");
        columnList.add("mobile");
        columnList.add("email");
        columnList.add("agent");
        columnList.add("agent_credno");
        columnList.add("agentphone");
        columnList.add("adsl_res");
        columnList.add("adsl_card");
        columnList.add("adsl_dev");
        columnList.add("adsl_ser");
        columnList.add("isrepair");
        columnList.add("bandwidth");
        columnList.add("ipaddress");
        columnList.add("overipnum");
        columnList.add("ipmask");
        columnList.add("gateway");
        columnList.add("macaddress");
        columnList.add("device_id");
        columnList.add("device_ip");
        columnList.add("device_shelf");
        columnList.add("device_frame");
        columnList.add("device_slot");
        columnList.add("device_port");
        columnList.add("basdevice_id");
        columnList.add("basdevice_ip");
        columnList.add("basdevice_shelf");
        columnList.add("basdevice_frame");
        columnList.add("basdevice_slot");
        columnList.add("basdevice_port");
        columnList.add("vlanid");
        columnList.add("workid");
        columnList.add("user_state");
        columnList.add("opendate");
        columnList.add("onlinedate");
        columnList.add("pausedate");
        columnList.add("closedate");
        columnList.add("updatetime");
        columnList.add("staff_id");
        columnList.add("remark");
        columnList.add("phonenumber");
        columnList.add("cableid");
        columnList.add("bwlevel");
        columnList.add("vpiid");
        columnList.add("vciid");
        columnList.add("adsl_hl");
        columnList.add("userline");
        columnList.add("dslamserialno");
        columnList.add("movedate");
        columnList.add("dealdate");
        columnList.add("opmode");
        columnList.add("maxattdnrate");
        columnList.add("upwidth");
        columnList.add("oui");
        columnList.add("device_serialnumber");
        columnList.add("serv_type_id");
        columnList.add("max_user_number");
        columnList.add("wan_value_1");
        columnList.add("wan_value_2");
        columnList.add("open_status");
        columnList.add("wan_type");
        columnList.add("lan_num");
        columnList.add("ssid_num");
        columnList.add("work_model");
        columnList.add("bind_port");
        columnList.add("flag_pvc");
        columnList.add("binddate");
        columnList.add("stat_bind_enab");
        columnList.add("bind_flag");
        columnList.add("is_chk_bind");
        columnList.add("spec_id");
        columnList.add("sip_id");
        columnList.add("network_spec");
        columnList.add("protocol");

        return columnList;
    }

    /**
     * 获取值列表
     * @param list
     * @return
     */
    private List getValueList(List<Map> list, List<String> columnList) {
        if(list == null || list.isEmpty()){
            return null;
        }

        List valueList = new ArrayList();

        String value;
        for (int i = 0; i < list.size(); i++) {
            value = StringUtil.getStringValue(list.get(i), columnList.get(i));
            if(value == null){
                value = "";
            }
            valueList.add(value);
        }
        return valueList;
    }
}
