package com.linkage.module.ids.bio;

import java.util.List;

import com.linkage.module.ids.dao.DegradationPathsDAO;

public class DegradationPathsBIO {

	private DegradationPathsDAO dao;

	@SuppressWarnings("rawtypes")
	public List getDegradationPathsList(int curPage_splitPage,
			int num_splitPage, String starttime1, String endtime1,
			String cityId, String oltip) {
		return dao.getDegradationPaths(curPage_splitPage, num_splitPage,
				starttime1, endtime1, cityId, oltip);
	}

	public int getDegradationPathsCount(int num_splitPage, String starttime1,
			String endtime1, String cityId, String oltip) {
		return dao.getDegradationPathsCount(num_splitPage, starttime1,
				endtime1, cityId, oltip);
	}

	@SuppressWarnings("rawtypes")
	public List getDegradationPathsInfoList(int curPage_splitPage,
			int num_splitPage, String oltip, String ponid, String starttime1,
			String endtime1) {
		return dao.getDegradationPathsInfoList(curPage_splitPage,
				num_splitPage, oltip, ponid, starttime1, endtime1);
	}

	public int getDegradationPathsInfoCount(int num_splitPage, String oltip,
			String ponid, String starttime1, String endtime1) {
		return dao.getDegradationPathsInfoCount(num_splitPage, oltip, ponid,
				starttime1, endtime1);
	}

	public void setDao(DegradationPathsDAO dao) {
		this.dao = dao;
	}
}
