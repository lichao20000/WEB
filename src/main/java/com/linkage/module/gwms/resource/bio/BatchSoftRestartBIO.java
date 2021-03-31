package com.linkage.module.gwms.resource.bio;

import com.linkage.module.gwms.resource.dao.BatchSoftRestartDAO;

public class BatchSoftRestartBIO {
	
	private BatchSoftRestartDAO dao;
	
	public BatchSoftRestartDAO getDao() {
		return dao;
	}

	public void setDao(BatchSoftRestartDAO dao) {
		this.dao = dao;
	}

	public void insertTask(long task_id,long add_time,long starttime,long accoid,String task_desc,int status)
	{
	    dao.insertTask(task_id,add_time,starttime,accoid,task_desc,status);
	}
	
	public void insertTmp(long task_id,String[] deviceId_array,long  add_time)
	{
		dao.insertTmp(task_id,deviceId_array,add_time);
	}
	
	public void deleteSameData()
	{
		dao.deleteSameData();
	}
	
	public void exportDataToBatch()
	{
		dao.exportDataToBatch();
	}
	
	public void truncateTmp()
	{
		dao.truncateTmp();
	}
	
	public void updateBatchSuccess(long add_time)
	{
		dao.updateBatchSuccess(add_time);
	}
}
