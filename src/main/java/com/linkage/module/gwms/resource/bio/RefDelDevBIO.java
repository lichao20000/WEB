
package com.linkage.module.gwms.resource.bio;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ailk.tr069.devrpc.dao.corba.AcsCorbaDAO;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.corba.ACSManager;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.resource.dao.RefDelDevDAO;
import com.linkage.module.itms.midware.bio.MidWareBIO;

/**
 * @author 王森博
 */
public class RefDelDevBIO
{

	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(RefDelDevBIO.class);
	private MidWareBIO midWareBIO;
	private RefDelDevDAO refDelDevDao;
	private UserInstReleaseBIO userInstReleaseBIO = new UserInstReleaseBIO();

	/**
	 * 通知ACS刷新设备
	 * 
	 * @author wangsenbo
	 * @param password
	 * @param account
	 * @date Apr 14, 2010
	 * @param
	 * @return String
	 */
	public String refresh(String deviceId, String account, String passwd, String oui,
			String deviceSN, String gw_type)
	{
		logger.debug("refresh({})", deviceId);
		String deviceIdS[] = new String[] { deviceId };
		String deSns[] = new String[] { deviceSN };
		String devOuis[] = new String[] { oui };
		// 使用融合版的TR069，所以将原来的注释
		// int ret = new ACSCorba().chgDev(deviceIdS, deSns, devOuis);
		int ret = new AcsCorbaDAO(Global.getPrefixName(gw_type) + Global.SYSTEM_ACS)
				.chgDev(deviceIdS, deSns, devOuis, LipossGlobals.getClientId());
		if (ret == 1)
		{
			if (LipossGlobals.IsITMS(gw_type))
			{
				return "更新成功！";
			}
			else
			{
				boolean flag = ACSManager.getInstance(account, passwd).manageCPE(
						deviceId, "1");
				if (Global.HLJDX.equals(Global.instAreaShortName))
				{
					flag = true;
				}
				if (Global.JLDX.equals(Global.instAreaShortName))
				{
					flag = true;
				}
				if (flag)
				{
					return "更新成功！";
				}
				else
				{
					return "更新失败:通知MC失败";
				}
			}
		}
		else if (ret == 0)
		{
			return "更新失败:参数错误";
		}
		else if (ret == -1)
		{
			return "更新失败:通知失败";
		}
		else
		{
			return "更新失败";
		}
	}

	/**
	 * 删除设备，通知ACS
	 * 
	 * @author wangsenbo
	 * @param deviceSN
	 * @param oui
	 * @param string2
	 * @param string
	 * @date Apr 14, 2010
	 * @param
	 * @return String
	 */
	public String delete(String deviceId, String oui, String deviceSN, String account,
			String passwd, String gw_type)
	{
		logger.debug("delete({},{},{},{},{})", new Object[] { deviceId, oui, deviceSN,
				account, passwd });
		Map<String, String> args = null;
		String msg = null;
		StringBuffer sbSQL = new StringBuffer();
		// if ("1".equals(cpe_allocatedstatus))
		// {
		// 查询这个设备所绑定的用户,没有则返回null
		args = getUserInfoByDeviceID(deviceId);
		// 通知资源绑定模块解绑设备
		if (null != args)
		{
			logger.debug("通知资源绑定");
			// String gwType=LipossGlobals.getGw_Type(deviceId);
			sbSQL.append(refDelDevDao.release(deviceId, gw_type));
			sbSQL.append(";");
			// 如果查询到有user_id，则调资源管理模块，否则不调
			// 解除绑定
			msg = userInstReleaseBIO.itmsRelease(args.get("user_id"), args
					.get("user_name"), args.get("city_id"), deviceId, account, 1);
			logger.debug(msg);
			// 通知更新设备表、更新用户表
			logger.debug("username:" + args.get("username"));
			msg = userInstReleaseBIO.itmsUpdateUser(args.get("username"));
		}
		// }
		sbSQL.append(refDelDevDao.delete(deviceId));
		logger.debug("sql====>" + sbSQL.toString());
		try
		{
			@SuppressWarnings("unused")
			int[] rs = refDelDevDao.batchUpdate(sbSQL.toString().split(";"));
			String deviceIdS[] = new String[] { deviceId };
			String deSns[] = new String[] { deviceSN };
			String devOuis[] = new String[] { oui };
			// 使用融合版的TR069，所以将原来的注释
			// int ret = new ACSCorba().chgDev(deviceIdS, deSns, devOuis, gw_type);
			int ret = new AcsCorbaDAO(Global.getPrefixName(gw_type) + Global.SYSTEM_ACS)
					.chgDev(deviceIdS, deSns, devOuis, LipossGlobals.getClientId());
			if (ret == 1)
			{
				if (LipossGlobals.IsITMS(gw_type))
				{
					// 通知中间件:
					try
					{
						logger.debug("通知中间件");
						// if (LipossGlobals.getMidWare())
						if (LipossGlobals.getMidWare())
						{
							midWareBIO.deleteMidWareDevice(deviceId, oui, deviceSN);
						}
						// 删除设备
						msg = userInstReleaseBIO.itmsDelDevice(deviceId, gw_type);
						// 通知更新设备表
						msg = userInstReleaseBIO.itmsUpdateDevice(deviceId, gw_type);
						logger.debug("返回值：" + msg);
						return "删除成功！";
					}
					catch (Exception exx)
					{
						exx.printStackTrace();
						logger.debug("－－－－－－－－通知中间件失败!－－－－－－－－－－");
						return "删除失败:通知中间件失败";
					}
				}
				else
				{
					boolean flag = ACSManager.getInstance(account, passwd).manageCPE(
							deviceId, "-1");
					if (Global.HLJDX.equals(Global.instAreaShortName))
					{
						flag = true;
					}
					if (flag)
					{
						return "删除成功！";
					}
					else
					{
						return "删除失败:通知MC失败";
					}
				}
			}
			else
			{
				if (ret == 0)
				{
					return "删除失败:参数错误";
				}
				else if (ret == -1)
				{
					return "删除失败:通知ACS失败";
				}
				else
				{
					return "删除失败";
				}
			}
		}
		catch (Exception e)
		{
			return "删除失败";
		}
	}

	private Map<String, String> getUserInfoByDeviceID(String device_id)
	{
		// 查询UserName
		Cursor cursor = DataSetBean.getCursor(
				"select user_id,username,city_id from tab_hgwcustomer where device_id='"
						+ device_id + "'", 1);
		// logger.info("======================user_id=" + user_id);
		// logger.info("======================userNames=" + userNames.toString());
		if (null == cursor || 0 == cursor.getRecordSize())
		{
			return null;
		}
		else
		{
			Map fields = cursor.getNext();
			Map<String, String> reArgs = new HashMap<String, String>();
			reArgs.put("user_id", (String) fields.get("user_id"));
			reArgs.put("username", (String) fields.get("username"));
			reArgs.put("city_id", (String) fields.get("city_id"));
			return reArgs;
		}
	}

	/**
	 * @return the refDelDevDao
	 */
	public RefDelDevDAO getRefDelDevDao()
	{
		return refDelDevDao;
	}

	/**
	 * @param refDelDevDao
	 *            the refDelDevDao to set
	 */
	public void setRefDelDevDao(RefDelDevDAO refDelDevDao)
	{
		this.refDelDevDao = refDelDevDao;
	}

	/**
	 * @return the midWareBIO
	 */
	public MidWareBIO getMidWareBIO()
	{
		return midWareBIO;
	}

	/**
	 * @param midWareBIO
	 *            the midWareBIO to set
	 */
	public void setMidWareBIO(MidWareBIO midWareBIO)
	{
		this.midWareBIO = midWareBIO;
	}
}
