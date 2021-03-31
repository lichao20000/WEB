package com.linkage.module.liposs.performance.bio.pefdef;

/**
 * 通过分域域表达式进行性能配置的流程
 * 
 * @author Duangr
 * @version 1.0
 * @since 2008-7-2 15:08:47
 * @category PerDef
 * 
 */
public abstract class AbstractDepartAreaPefDefImp extends AbstractBasePefDefImp
{

	/**
	 * 通过表达式获取性能和描述信息
	 */
	abstract protected void getPerfAndDescDataByExpression();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.linkage.liposs.webtopo.common.pefdef.BasePefDefInterfance#run()
	 */
	public int mainConfig()
	{
		print("开始性能配置 expressionid = " + expressionId + " & device_id = "
				+ device_id + " 先通过分域域表达式采集各个分域的信息");
		/*
		 * 先通过分域域表达式采集各个分域的信息 分域域表达式对应的OID是唯一的
		 */
		returnFlag = getPerfData(departAreaId, device_id, readCom);

		// 域信息采集成功,准备采集各个分域的性能信息
		if (returnFlag == 1)
		{
			getPerfAndDescDataByExpression();

			if (notNeedDesc(device_id, expressionId))
			{
				// 这里是直接将描述信息入库为 slot(索引-1) 的情况
				createDescByIndex(indexList);
			}
		}
		return returnFlag;
	}
}
