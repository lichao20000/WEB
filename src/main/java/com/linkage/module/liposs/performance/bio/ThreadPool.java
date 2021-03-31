package com.linkage.module.liposs.performance.bio;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangping5221
 * @version 1.0
 * @since 2008-9-26
 * @category 版权：南京联创科技 网管科技部
 * 
 */
public class ThreadPool
{	
	private static Logger log = LoggerFactory.getLogger(ThreadPool.class);
	
	/**
	 * 高优先级的线程池单态实例
	 */	
	private static ExecutorService highThreadPool = null;
	
	/**
	 * 高优先级的线程池锁
	 */
	private static Object highThreadPoolLock = new Object();
	
	
	/**
	 * 低优先级的线程池单态实例
	 */
	private static ExecutorService lowerThreadPool=null;
	
	
	/**
	 * 低优先级的线程池锁
	 */
	private static Object lowerThreadPoolLock =new Object();
	
	
	/**
	 * 构造函数
	 */
	public  ThreadPool()
	{
		//初始化高优先级的线程
		synchronized(highThreadPoolLock)
		{
			if(null==highThreadPool||highThreadPool.isShutdown())
			{
				log.debug("init threadPool!");
				highThreadPool=Executors.newFixedThreadPool(5);
			}			
		}
		
		//初始化低优先级的线程
		synchronized(lowerThreadPoolLock)
		{
			if(null==lowerThreadPool||lowerThreadPool.isShutdown())
			{
				log.debug("init threadPool!");
				lowerThreadPool=Executors.newFixedThreadPool(20);
			}			
		}		
		
	}
	
	
	public void destroy()
	{
		//关闭
		if(null!=highThreadPool)
		{
			highThreadPool.shutdownNow();
		}
		
		if(null!=lowerThreadPool)
		{
			lowerThreadPool.shutdownNow();
		}
	}
	
	
	/**
	 * 提交高优先级任务
	 * @param task
	 * @return
	 */
	public Future submitHighLevelTask(Runnable task)
	{
		return highThreadPool.submit(task);
	}
	
	
	
	/**
	 * 提交低优先级任务
	 * @param task
	 * @return
	 */
	public Future submitLowerLevelTask(Runnable task)
	{
		return lowerThreadPool.submit(task);
	}
	
	
	
	
	public static void main(String args[])
	{		
		ThreadPool pool = new ThreadPool();
		pool.destroy();
		
	}	
}
