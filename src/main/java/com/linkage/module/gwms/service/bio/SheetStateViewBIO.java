/**
 * 
 */
package com.linkage.module.gwms.service.bio;

import java.util.List;

import com.linkage.module.gwms.service.bio.interf.I_SheetStateViewBIO;
import com.linkage.module.gwms.service.dao.interf.I_SheetStateViewDAO;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-6-17
 * @category com.linkage.module.gwms.service.bio
 * 
 */
@SuppressWarnings("unchecked")
public class SheetStateViewBIO implements I_SheetStateViewBIO {

	I_SheetStateViewDAO sheetStateViewDAO;

	/**
	 * @return the sheetStateViewDAO
	 */
	public I_SheetStateViewDAO getSheetStateViewDAO() {
		return sheetStateViewDAO;
	}

	/**
	 * @param sheetStateViewDAO
	 *            the sheetStateViewDAO to set
	 */
	public void setSheetStateViewDAO(I_SheetStateViewDAO sheetStateViewDAO) {
		this.sheetStateViewDAO = sheetStateViewDAO;
	}

	/**
	 * 
	 */
	public List getAllCity(String cityId) {

		return sheetStateViewDAO.getAllCity(cityId);
	}

	/**
	 * @category 查询操作类型
	 * 
	 * @param cityId
	 * @return
	 */
	public List getGwOperType() {

		return sheetStateViewDAO.getGwOperType();
	}
	
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
			String operTypeId,String bindState){
		return sheetStateViewDAO.getSheetStateExcel(username, cityId, 
				productSpecId, timeStart, timeEnd, operTypeId, bindState);
	}
	
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
	public List getSheetState(int curPage_splitPage, int num_splitPage,
			String username, String cityId, String productSpecId,
			long timeStart, long timeEnd,String operTypeId,String bindState) {

		return sheetStateViewDAO.getSheetState(curPage_splitPage,
				num_splitPage, username, cityId, productSpecId, timeStart,
				timeEnd, operTypeId, bindState);
	}

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
	public int getSheetStateCount(int curPage_splitPage, int num_splitPage,
			String username, String cityId, String productSpecId,
			long timeStart, long timeEnd,String operTypeId,String bindState) {

		return sheetStateViewDAO.getSheetStateCount(curPage_splitPage,
				num_splitPage, username, cityId, productSpecId, timeStart,
				timeEnd,operTypeId,bindState);
	}

}
