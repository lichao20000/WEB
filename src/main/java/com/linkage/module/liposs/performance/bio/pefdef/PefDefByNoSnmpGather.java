package com.linkage.module.liposs.performance.bio.pefdef;

/**
 * 表达式分类1为9,不需要采集,直接入库
 * 例:性能指标类型为'EVDO状态'
 * 
 * @author xief
 * @version 1.0
 * @since 2009-11-25 9:55:18
 * @category PerDef
 */
public class PefDefByNoSnmpGather extends AbstractBasePefDefImp
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linkage.liposs.webtopo.common.pefdef.BasePefDefInterfance#run()
	 */
	public int mainConfig()
	{
		print("开始性能配置 expressionid = " + expressionId + " & device_id = "
				+ device_id);

		// 表达式分类1为9,不需要采集,默认成功
		returnFlag = 1;
		// indexid:默认只有一个，值为1
		this.indexList.add("1");

		return returnFlag;
	}
}
