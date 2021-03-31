package bio.webtopo.warn;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bio.webtopo.warn.filter.CacheBaseFilter;
import dao.webtopo.warn.WarnFilterDAO;

/**
 * WebTopo实时告警牌告警规则定义与修改BIO
 * <li>REQ: GZDX-REQ-20080402-ZYX-001
 * <li>REQ: HBDX-REQ-20080220-WANGBING-001
 * 
 * @author 段光锐
 * @version 1.0
 * @since 2008-4-8
 * @category WebTopo/实时告警牌/告警规则
 * 
 */
public class WarnFilterBIO
{
	private WarnFilterDAO warnFilterDao = null;

	private static final Logger LOG = LoggerFactory.getLogger(WarnFilterBIO.class);

	/**
	 * 同步锁,在新增告警规则模板的时候用到
	 */
	private final static Object LOCK = new Object();

	/**
	 * 新增告警规则模板
	 * 
	 * @param acc_loginname
	 *            登陆用户名
	 * @param ruleName
	 *            规则模板名称
	 * @param maxNum
	 *            最大显示告警数量
	 * @param selected
	 *            是否默认
	 * @param isPublic
	 *            是否共享
	 * @param visible
	 *            是否显示
	 * @return
	 */
	public boolean addRuleInfo(String acc_loginname, String ruleName,
			long maxNum, int selected, int isPublic, int visible)
	{
		long ruleId = -1;
		boolean addSuccess = false;
		synchronized (LOCK)
		{
			ruleId = warnFilterDao.getMaxRuleId() + 1;
			addSuccess = warnFilterDao.addRuleInfo(acc_loginname, (ruleId),
					ruleName, maxNum, selected, isPublic, visible);
		}
		// if (addSuccess)
		// {
		// reloadRule(ruleId);
		// }
		LOG.debug("添加告警规则 " + acc_loginname + "@" + ruleId + " "
				+ (addSuccess ? "成功" : "失败"));
		return addSuccess;
	}

	/**
	 * 修改告警ID所对应的告警规则模板
	 * 
	 * @param acc_loginname
	 *            登陆用户名
	 * @param ruleId
	 *            规则模板ID
	 * @param ruleName
	 *            规则模板名称
	 * @param maxNum
	 *            最大显示告警数量
	 * @param isPublic
	 *            是否共享
	 * @param visible
	 *            是否显示
	 * @return
	 */
	public boolean updateRuleInfo(String acc_loginname, long ruleId,
			String ruleName, long maxNum, int selected, int isPublic,
			int visible)
	{
		boolean updateSuccess = false;
		updateSuccess = warnFilterDao.updateRuleInfo(acc_loginname, ruleId,
				ruleName, maxNum, selected, isPublic, visible);
		// if (updateSuccess)
		// {
		// setFilterType(ruleId, visible);
		// }
		LOG.debug("修改告警规则 " + ruleId + " " + (updateSuccess ? "成功" : "失败"));
		return updateSuccess;
	}

	/**
	 * 根据告警规则ID来删除当前告警规则
	 * 
	 * @param ruleId
	 * @return
	 */
	public boolean delRuleInfo(long ruleId)
	{
		boolean delSuccess = false;

		delSuccess = warnFilterDao.delRuleInfo(ruleId);
		// 删除规则时需要通知过滤模块
		if (delSuccess)
		{
			delFilterUtil(ruleId);
		}
		return delSuccess;
	}

	/**
	 * 将其他用户的共享规则模板Copy到当前用户下
	 * 
	 * @param acc_loginname
	 *            当前登陆用户名
	 * @param fromRuleId
	 *            需要COPY的规则ID
	 * @return
	 */
	public boolean copyRuleInfo(String acc_loginname, long fromRuleId)
	{
		boolean copySuccess = false;
		long toRuleId = -1;
		synchronized (LOCK)
		{
			toRuleId = warnFilterDao.getMaxRuleId() + 1;
			copySuccess = warnFilterDao.copyRuleInfo(acc_loginname, fromRuleId,
					toRuleId);
		}
		// 复制规则时不需要通知过滤模块
		// if (copySuccess)
		// {
		// reloadRule(toRuleId);
		// }
		return copySuccess;
	}

	/**
	 * 将告警规则详情保存入库
	 * 
	 * @param rulePriority
	 *            规则详情优先级(多条规则之间用'#'分隔)
	 * @param ruleContent
	 *            规则详情内容(多条规则之间用'#'分隔)
	 * @param ruleInvocatio
	 *            规则详情是否启用(多条规则之间用'#'分隔)
	 * @param ruleLength
	 *            规则详情长度(多条规则之间用'#'分隔)
	 * @return
	 */
	public boolean saveRuleDetailInfo(long ruleId, String rulePriority,
			String ruleContent, String ruleInvocation, int ruleLength)
	{
		boolean saveSuccess = false;
		if (ruleLength < 0)
			return saveSuccess;

		saveSuccess = warnFilterDao.saveRuleDetailInfo(ruleId,
				toArray(rulePriority), toArray(ruleContent),
				toArray(ruleInvocation), ruleLength);
		// 修改规则条目更改时需要通知过滤模块
		if (saveSuccess)
		{
			reloadRule(ruleId);
		}
		return saveSuccess;
	}

	/**
	 * 保存实时告警牌列配置信息
	 * 
	 * @param fieldName
	 *            列名
	 * @param visible
	 *            是否显示
	 * @param sequence
	 *            显示顺序
	 * @return
	 */
	public boolean saveWarnColumnInfo(String fieldName, String visible,
			String sequence)
	{
		boolean saveSuccess = false;
		saveSuccess = warnFilterDao.saveWarnColumnInfo(toArray(fieldName),
				toArray(visible), toArray(sequence));
		if (saveSuccess)
		{
			// 保存成功后要通知告警牌展示模块
			reloadWarnColumn();
		}
		return saveSuccess;
	}

	/**
	 * 将 <code>str</code> (asdf#sdfsdf#sfdsfa) 转换成数组,分隔符为 (#)
	 * 
	 * @param str
	 * @return
	 */
	private String[] toArray(String str)
	{
		return str.split("#");
	}

	/**
	 * 通知告警过滤模块重新加载ruleId的告警规则
	 * 
	 * @param ruleId
	 */
	private boolean reloadRule(long ruleId)
	{
		boolean flag = false;
		CacheBaseFilter cacheFilter = CacheBaseFilter.getInstance();
		flag = cacheFilter.reload(Long.valueOf(ruleId).intValue());
		return flag;
	}

	/**
	 * 过滤规则配置时改变模板过滤类型需要通知
	 * 
	 * @param ruleId
	 *            规则ID
	 * @param visible
	 *            是否显示
	 * @return
	 */
	// private void setFilterType(long ruleId, int visible)
	// {
	// CacheBaseFilter cacheFilter = CacheBaseFilter.getInstance();
	// cacheFilter.setFilterType(Long.valueOf(ruleId).intValue(),
	// (visible == 1 ? true : false));
	// }
	/**
	 * 删除模板时通知
	 * 
	 * @param ruleId
	 * @return
	 */
	private boolean delFilterUtil(long ruleId)
	{
		boolean flag = false;
		CacheBaseFilter cacheFilter = CacheBaseFilter.getInstance();
		flag = cacheFilter.delFilterUtil(Long.valueOf(ruleId).intValue());
		return flag;
	}

	/**
	 * 通知告警展示模块重新加载列展示配置信息
	 * 
	 * @return
	 */
	private boolean reloadWarnColumn()
	{
		boolean flag = false;
		// TODO:通知显示列已经被更改
		LOG.debug("ColumnInit.reloadColumn() Begin ......");
		ColumnInit instance = ColumnInit.getInstance();
		flag = instance.reloadColumn();
		LOG.debug("ColumnInit.reloadColumn() End ...... flag=" + flag);
		return flag;
	}

	public void setWarnFilterDao(WarnFilterDAO warnFilterDao)
	{
		this.warnFilterDao = warnFilterDao;
	}
}
