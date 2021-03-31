package com.linkage.module.liposs.performance.bio.pefdef;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通过普通的性能和描述表达式进行性能配置的流程
 * 
 * @author Duangr
 * @version 1.0
 * @since 2008-7-2 15:08:47
 * @category PerDef
 * 
 */
public abstract class AbstractCommonPefDefImp extends AbstractBasePefDefImp
{
	private static Logger log = LoggerFactory.getLogger(AbstractCommonPefDefImp.class);
	/**
	 * 通过表达式获取描述信息
	 */
	abstract protected void getDescDataByExpression();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linkage.liposs.webtopo.common.pefdef.BasePefDefInterfance#run()
	 */
	public int mainConfig()
	{
		print("开始性能配置 expressionid = " + expressionId + " & device_id = "
				+ device_id);
		log.warn("AbstractCommonPefDefImp==>开始性能配置 expressionid = " + expressionId + " & device_id = "
				+ device_id);

		returnFlag = getPerfData(expressionId, device_id, readCom);

		// 采集性能信息成功,准备采集描述信息
		if (returnFlag == 1)
		{
			getDescData();
		}
		
		return returnFlag;
	}

	/**
	 * 获取描述信息,要先判断是否需要采集描述,还是直接将slot(索引-1)作为描述
	 * 
	 */
	private void getDescData()
	{
		if (notNeedDesc(device_id, expressionId))
		{
			// 这里是直接将描述信息入库为 slot(索引-1) 的情况
			createDescByIndex(indexList);
		} else
		{
			getDescDataByExpression();
		}
	}
}
