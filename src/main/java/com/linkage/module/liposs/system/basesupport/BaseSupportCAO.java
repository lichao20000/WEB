package com.linkage.module.liposs.system.basesupport;

import com.linkage.system.utils.corba.CorbaContainer;
import com.linkage.system.utils.corba.CorbaUtil;

/**
 * CAO基类，所有的CAO都应该继承该类
 * 
 * @author 王志猛(工号) tel：13701409234
 * @version 1.0
 * @since 2008-8-14
 * @category com.linkage.module.bcms.system 版权：南京联创科技 网管科技部
 * 
 */
public class BaseSupportCAO
{
	protected CorbaUtil corbaUtil = null;// corbautil工具类
	protected CorbaContainer corbaCon = null;// corba对象容器
	public void setCorbaUtil(CorbaUtil corbaUtil)
	{
		this.corbaUtil = corbaUtil;
	}
	public void setCorbaCon(CorbaContainer corbaCon)
	{
		this.corbaCon = corbaCon;
	}
}
