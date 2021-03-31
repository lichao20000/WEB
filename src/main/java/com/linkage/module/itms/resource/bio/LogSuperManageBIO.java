
package com.linkage.module.itms.resource.bio;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.WebUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.itms.resource.dao.LogSuperManageDAO;

public class LogSuperManageBIO
{

	private static Logger logger = LoggerFactory.getLogger(LogSuperManageBIO.class);
	private LogSuperManageDAO dao;

	public List<Map> getLogInfo(String auth_name, String user_name, String starttime,
			String endtime, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("LogSuperManageBIO->getLogInfo");
		return dao.getLogInfo(auth_name, user_name, starttime, endtime,
				curPage_splitPage, num_splitPage);
	}

	public int countLogInfo(String auth_name, String user_name, String starttime,
			String endtime, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("LogSuperManageBIO->countLogInfo");
		return dao.countLogInfo(auth_name, user_name, starttime, endtime,
				curPage_splitPage, num_splitPage);
	}
	
	public List<Map> excelLogInfo(String auth_name, String user_name, String starttime,
			String endtime){
		logger.debug("LogSuperManageBIO->excelLogInfo");
		return dao.excelLogInfo(auth_name, user_name, starttime, endtime);
	}

	/**
	 * <pre>
	 * 记录超级权限操作日志
	 * 如果系统配置不启用超级权限，则不记录日志
	 * 如果当前操作用户没有登录，则没有任何超级权限，不记录日志
	 * </pre>
	 * 
	 * @param authCode 超级权限简码
	 * @param operDesc 超级权限描述
	 * @see WebUtil#getCurrentUserSuperAuths()
	 */
	public void addSuperAuthLog(String authCode, String operDesc)
	{
		// 1.如果系统配置不启用超级权限，则不记录日志
		if (!LipossGlobals.enableSuperAuth())
		{
			return;
		}
		// 2.未登陆用户没有任何超级权限(see SuperAuth)，故没有必要记录日志
		UserRes currentUser = WebUtil.getCurrentUser();
		if (currentUser == null)
		{
			logger.error("addSuperAuthLog({}, {})", authCode, operDesc);
			logger.error("the current user is not login, no super auths, there is no need to log");
			return;
		}
		// 3.根据超级权限编码查询信息,在JdbcTemplate类中做了Map校验，superAuthMap一定有值
		Map<String, Object> superAuthMap = dao.querySuperAuth(authCode);
		// 4.如果没有传入操作描述，则使用默认描述
		if (StringUtil.IsEmpty(operDesc))
		{
			operDesc = currentUser.getUser().getAccount() + "操作了超级权限["
					+ superAuthMap.get("auth_name") + "]";
		}
		else
		{
			operDesc = operDesc.replace("{user_account}", currentUser.getUser().getAccount());
			operDesc = operDesc.replace("{auth_name}", StringUtil.getStringValue(superAuthMap, "auth_name"));
		}
		
		// 5.记录日志
		dao.addSuperAuthLog(StringUtil.getLongValue(superAuthMap, "auth_id"), currentUser
				.getUser().getId(), operDesc);
	}

	public LogSuperManageDAO getDao()
	{
		return dao;
	}

	public void setDao(LogSuperManageDAO dao)
	{
		this.dao = dao;
	}
}
