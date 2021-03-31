package com.linkage.module.liposs.performance.bio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.corba.interfacecontrol.PmeeInterface;
import com.linkage.litms.common.util.Log;
import com.linkage.module.liposs.performance.dao.I_configPmeeDao;
import com.linkage.system.utils.DateTimeUtil;

/**
 * @author wangping5221
 * @version 1.0
 * @since 2008-10-9
 * @category com.linkage.liposs.bio.performance
 * @版权：南京联创科技 网管科技部
 */
public class PmConfigMainThread implements Runnable
{
	private static Logger log = LoggerFactory.getLogger(PmConfigMainThread.class);
	// 配置参数
	private Pm_Map_Instance param =null;
	
	//设备列表
	private List<String> deviceList=null;
	
	//表达式列表
	List<Integer> expressionList=null;
	
	// 线程池
	private ThreadPool threadPool;
	
	//日志文件
	private String logFileName = "pmeeConfig" + new DateTimeUtil().getShortDate()+ ".sql";
	
	
	//数据库操作类	
    private I_configPmeeDao dao =null;
	/**
	 * 构造函数
	 * 
	 * @param param_
	 * @param threadPool_
	 */
	public PmConfigMainThread(Pm_Map_Instance param_, List<String> deviceList_,List<Integer> expressionList_,ThreadPool threadPool_,I_configPmeeDao dao_)
	{
		param = param_;
		// 设备列表
		deviceList= deviceList_;
		// 表达式列表
		expressionList = expressionList_;
		threadPool = threadPool_;
		dao=dao_;
	}
	
	
	
	public void run()
	{		
		if (null == deviceList || deviceList.isEmpty() || null == expressionList
				|| expressionList.isEmpty())
		{
			log(false,"参数错误  deviceList为空或expressionList为空");
			log.warn("参数错误  deviceList为空或expressionList为空");
			return;
		}
		Iterator<String> deviceIt = deviceList.iterator();
		Iterator<Integer> expressionIt = expressionList.iterator();
		String device_id="";
		int expressionId;
		Future temp=null;
		List<Future> taskResultList = new ArrayList<Future>();
		
		//遍历创建配置子任务
		while(deviceIt.hasNext())
		{
			device_id=deviceIt.next();
			expressionIt=expressionList.iterator();
			while(expressionIt.hasNext())
			{
				expressionId=expressionIt.next();
				log(false,"配置 device_id:"+device_id+"     expressionId:"+expressionId);
				log.warn("配置 device_id:"+device_id+"     expressionId:"+expressionId);
				PmConfigChildThread task = new PmConfigChildThread(device_id,expressionId,param,dao);
				temp =threadPool.submitLowerLevelTask(task);
				taskResultList.add(temp);				
			}
		}
		
		/**
		 * 等待子任务完成
		 */
		while(!isComplete(taskResultList))
		{
			try
			{
				//等待5秒
				Thread.sleep(5000);
			}
			catch (InterruptedException e)
			{
				log(false,"等待子任务失败！");
			}
		}
		
		
		/**
		 * 通知后台
		 */
		String[] devices = new String[deviceList.size()];
		deviceList.toArray(devices);
		int result =PmeeInterface.GetInstance().readDevices(devices);
		if(0==result)
		{
			log(false,"通知后台成功！");
		}
		else
		{
			log(false,"通知后台失败！");
		}	
		
		
		/**
		 * clear
		 */
		temp=null;
		taskResultList=null;
		deviceIt=null;
		expressionIt=null;
		deviceList=null;
		expressionList=null;
	}
	
	
	
	/**
	 * 判断指定线程任务是否执行完
	 * @param taskResultList
	 * @return
	 */
	private boolean isComplete(List<Future> taskResultList)
	{
		Iterator<Future> it =taskResultList.iterator();
		Future temp ;
		while(it.hasNext())
		{
			temp=it.next();
			if(!temp.isDone())
			{
				return false;				
			}
		}
		
		return true;
	}
	
	
	/**
	 * 记录日志的方法
	 * @param isNeedWriteFile  是否需要写文件，true：刷屏＋写文件 false：只刷屏不写文件
	 * @param msg  日志内容
	 */
	private void log(boolean isNeedWriteFile,String msg)
	{
		log.debug(msg);
		if(isNeedWriteFile)
		{
			Log.writeLog(msg, logFileName);
		}
		
	}
}
