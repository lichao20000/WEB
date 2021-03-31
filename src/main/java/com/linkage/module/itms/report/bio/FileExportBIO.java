
package com.linkage.module.itms.report.bio;

import java.util.List;
import java.util.Map;

import com.linkage.litms.system.User;

import com.linkage.module.itms.report.dao.FileExportDAO;

/**
 * @author guankai (Ailk No.300401)
 * @version 1.0
 * @since 2020年4月20日
 * @category com.linkage.module.itms.report.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class FileExportBIO
{

	private FileExportDAO dao;
	
	public FileExportDAO getDao()
	{
		return dao;
	}

	
	public void setDao(FileExportDAO dao)
	{
		this.dao = dao;
	}

	public List<Map> getFileExportInfo(String fileExportDesc, String startTime, String endTime, String cityId, User curUser, int curPage_splitPage, int num_splitPage)
	{
		return dao.getFileExportInfo(fileExportDesc,startTime, endTime, cityId,curUser,curPage_splitPage,num_splitPage);
	}

	public int getFileExportCount(String fileExportDesc, String startTime, String endTime, String cityId, User curUser)
	{
		// TODO Auto-generated method stub
		return dao.getFileExportCount(fileExportDesc,startTime, endTime, cityId,curUser);
	}
	
	
}
