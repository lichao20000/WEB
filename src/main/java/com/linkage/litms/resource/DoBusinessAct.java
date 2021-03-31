package com.linkage.litms.resource;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

/**
 * 
 * 
 * @author Alex.Yan (yanhj@lianchuang.com)
 * @version 2.0, Aug 26, 2009
 * @see
 * @since 1.0
 */
public class DoBusinessAct {

	/** log */
	private static Logger logger = LoggerFactory
			.getLogger(DoBusinessAct.class);

	/** 策略ID */
	private long id;

	/**
	 * 
	 * @param gw_type
	 * @param device_id
	 * @param gather_id
	 * @param oui
	 * @param device_serialnumber
	 * @param username
	 */
	@SuppressWarnings("unchecked")
	public void doOpenBusiness(String gw_type, String device_id,
			String gather_id, String oui, String device_serialnumber,
			String username) {
		logger.debug("doOpenBusiness()");

		String doBusiness = LipossGlobals.getLipossProperty("doBusiness");

		// 如果不需要下发工单业务
		if (null == doBusiness || "".equals(doBusiness)
				|| "0".equals(doBusiness) || "2".equals(gw_type)) {
			// UserinstLog.debug("openBusiness",
			// "[DoBusinessAct]:该设备(用户)不需要绑定时开户||" + device_serialnumber +
			// "("+username+")");
			return;
		}

		// 如果需要下发工单
		String passwd = "";
		String vpiid = "";
		String vciid = "";
		// String serv_type_id = "";
		String wan_type = "";
		String ipaddress = "";
		String ipmask = "";
		String gateway = "";
		String adsl_ser = "";
		String bind_port = "";

		String service_id = "";

		String userInfoSQL = "select * from hgwcust_serv_info where username='"
				+ username + "' and serv_status=1";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			userInfoSQL = "select passwd, vpiid, vciid, wan_type, ipaddress, ipmask, gateway, adsl_ser, bind_port " +
					"from hgwcust_serv_info where username='"
					+ username + "' and serv_status=1";
		}
		PrepareSQL psql = new PrepareSQL(userInfoSQL);
    	psql.getSQL();
		Map<String, String> userInfoMap = DataSetBean.getRecord(userInfoSQL);
		if (null == userInfoMap) {

			return;
		} else {
			// 取得最大策略ID (OR随机生成)
			id = DataSetBean.getMaxId("gw_serv_strategy", "id");
			passwd = userInfoMap.get("passwd");
			vpiid = userInfoMap.get("vpiid");
			vciid = userInfoMap.get("vciid");
			// serv_type_id = userInfoMap.get("serv_type_id");
			wan_type = userInfoMap.get("wan_type");
			ipaddress = userInfoMap.get("ipaddress");
			ipmask = userInfoMap.get("ipmask");
			gateway = userInfoMap.get("gateway");
			adsl_ser = userInfoMap.get("adsl_ser");
			bind_port = userInfoMap.get("bind_port");
			if ("1".equals(wan_type)) {
				// 桥接
				service_id = "1001";
			} else if ("2".equals(wan_type)) {
				// 路由
				service_id = "1008";
			} else if ("3".equals(wan_type)) {
				// 静态IP
				service_id = "1010";
			} else if ("4".equals(wan_type)) {
				// DHCP
				logger.warn("暂时不支持DHCP");

				return;
			} else {
				logger.warn("业务类型service_id不正确");

				return;
			}
		}

		DoBusinessObject dbo = new DoBusinessObject();
		dbo.setId(id);
		dbo.setDevice_id(device_id);
		dbo.setGather_id(gather_id);
		dbo.setOui(oui);
		dbo.setDevice_serialnumber(device_serialnumber);
		dbo.setVpiid(vpiid);
		dbo.setVciid(vciid);
		dbo.setUsername(username);
		dbo.setPasswd(passwd);
		dbo.setWan_type(wan_type);
		dbo.setService_id(service_id);
		dbo.setIpaddress(ipaddress);
		dbo.setIpmask(ipmask);
		dbo.setGateway(gateway);
		dbo.setAdsl_ser(adsl_ser);
		dbo.setBind_port(bind_port);

		logger.warn("往线程中增加了开户对象:{},{}", device_serialnumber, username);

		LipossGlobals.DoBusinessObjList.add(dbo);

	}

}
