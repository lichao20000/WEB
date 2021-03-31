/**
 * 
 */
package com.linkage.module.gwms.service.bio.interf;

import java.util.List;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-6-17
 * @category com.linkage.module.gwms.service.bio.impl
 * 
 */
@SuppressWarnings("unchecked")
public interface I_SheetStateViewBIO {
	
	/**
	 * @category 查询所有的属地
	 * 
	 * @param cityId
	 * @return
	 */
	public List getAllCity(String cityId) ;
	
	/**
	 * @category 查询操作类型
	 * 
	 * @param cityId
	 * @return
	 */
	public List getGwOperType() ;
	
	/**
	 * @category 查询满足条件的所有参数(针对excel导出)
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param username
	 * @param cityId
	 * @param productSpecId
	 * @param timeStart
	 * @param timeEnd
	 * @return
	 */
	public List getSheetStateExcel(String username, String cityId,
			String productSpecId, long timeStart, long timeEnd,
			String operTypeId,String bindState);
	
	/**
	 * @category 查询满足条件的所有参数
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param username
	 * @param cityId
	 * @param productSpecId
	 * @param timeStart
	 * @param timeEnd
	 * @return
	 */
	public List getSheetState(int curPage_splitPage, int num_splitPage,String username, String cityId,
			String productSpecId, long timeStart, long timeEnd,String operTypeId,String bindState) ;
	
	/**
	 * @category 查询页数
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param username
	 * @param cityId
	 * @param productSpecId
	 * @param timeStart
	 * @param timeEnd
	 * @return
	 */
	public int getSheetStateCount(int curPage_splitPage, int num_splitPage,String username, String cityId,
			String productSpecId, long timeStart, long timeEnd,String operTypeId,String bindState);
	
}
