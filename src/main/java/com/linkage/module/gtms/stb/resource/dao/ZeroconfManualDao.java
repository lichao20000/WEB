
package com.linkage.module.gtms.stb.resource.dao;

import java.util.List;
import java.util.Map;

import com.linkage.module.gtms.stb.resource.dto.ZeroconfManualDTO;

/**
 * itv手动下发配置dao
 * 
 * @author zhumiao
 * @version 1.0
 * @since 2011-12-5 下午03:08:03
 * @category com.linkage.module.lims.itv.zeroconf.dao<br>
 * @copyright 南京联创科技 网管科技部
 */
public interface ZeroconfManualDao
{

	/**
	 * 获取业务帐号下的机顶盒信息
	 * 
	 * @param serv_account
	 *            业务帐号
	 * @return
	 */
	public List<Map> getUserAccount(ZeroconfManualDTO dto);

	/**
	 * 手动下发配置
	 * 
	 * @param dto
	 * @return
	 */
	public void manualConfiguration(ZeroconfManualDTO dto);
}
