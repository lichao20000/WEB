package com.linkage.module.liposs.performance.bio.pefdef;

/**
 * 不需要采集描述信息,直接把索引作为描述
 * 
 * @author Duangr
 * @version 1.0
 * @since 2008-7-4 8:55:18
 * @category PerDef
 */
public class PefDefByNoneDescId extends AbstractCommonPefDefImp
{

	/**
	 * 不需要采集描述信息,直接把性能的索引作为描述
	 */
	protected void getDescDataByExpression()
	{
		// Nothing to do
		print("不需要采集描述信息,直接将索引作为描述");
	}

}
