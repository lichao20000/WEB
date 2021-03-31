package com.linkage.litms.common;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Jason(3412)
 * @date 2008-9-26
 */
public class PageQueryActionSupport extends ActionSupport {

	protected int currentPage = 0;
	protected int totalCount = 0;
	protected int totalPage;
	//每页多少条，可在继承类的方法中做修改
	protected static int numperpage = 15;

	/**
	 * 对要返回的结果list处理，返回页面numperpage个纪录数
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2008-11-21
	 * @return List
	 */
	public final List QueryPage(List resultList) {
		List recordList = new ArrayList();
		if (resultList == null || resultList.size() == 0)
			return null;
		
		totalCount = resultList.size();
		if (currentPage == 0) {
			currentPage = 1;
		}
		int tmp = getCurrentPageRecordNum();
		int i = 0;
		while (i < tmp) {
			Object obj = resultList.get((currentPage - 1) * numperpage + i);
			recordList.add(obj);
			i++;
		}
		resultList = null;
		setTotalPage();
		return recordList;
	}

	private int getCurrentPageRecordNum() {

		if (isLastPage()){
			if (totalCount % numperpage == 0)
				return numperpage;
			else
				return totalCount % numperpage;
		}else{
			return numperpage;
		}

	}

	private boolean isLastPage() {
		if (totalCount % numperpage == 0) {
			if (totalCount / numperpage == currentPage)
				return true;
		} else {
			if (totalCount / numperpage + 1 == currentPage)
				return true;
		}
		return false;
	}

	private void setTotalPage() {
		totalPage = totalCount % numperpage == 0 ? totalCount / numperpage
				: totalCount / numperpage + 1;
	}

	public String getCurrentPage() {
		return String.valueOf(currentPage);
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = Integer.parseInt(currentPage);
	}

	public String getTotalCount() {
		return String.valueOf(totalCount);
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = Integer.parseInt(totalCount);
	}

	public String getTotalPage() {
		return String.valueOf(totalPage);
	}

	public String getNumperpage() {
		return String.valueOf(numperpage);
	}

}
