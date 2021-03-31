package com.linkage.litms.resource;

import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 线程池
 * @author chenzm
 *
 */

public class CheckUserThreadPool{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(CheckUserThreadPool.class);
	public Vector vector;
	
	/**
	 * 建立线程池，maxThread最大线程数
	 */
	public CheckUserThreadPool(int maxThread) {
		
		vector = new Vector();
		for(int i = 1; i <= maxThread; i++)
		{
			SimpleThread thread = new SimpleThread();
			vector.addElement(thread);
			thread.start();
		}
	}
	
	/**
	 * 启动一个线程
	 */
	public boolean process(String str1,String str2){
		for(int i = 0; i < vector.size(); i++){
			SimpleThread currentThread = (SimpleThread)vector.elementAt(i);
			
			if (!currentThread.isRunning()){
				currentThread.setRunning(true);
				currentThread.setUsername(str1);
				currentThread.setCityID(str2);
				return true;
			}
			
			//线程池已满
			if(i == vector.size())
			{
				logger.warn("pool is full, try in another time.");
			}
		}
		return false;
	}

}
