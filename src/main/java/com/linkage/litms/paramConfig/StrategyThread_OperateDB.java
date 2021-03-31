package com.linkage.litms.paramConfig;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DataSetBean;

public class StrategyThread_OperateDB {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(StrategyThread_OperateDB.class);
	/**
	 * 单态实例
	 */
	private static StrategyThread_OperateDB instance = null;
	/**
	 * 单态实例锁
	 */
	private static Object lock = new Object();
	
	/** the sleep-time of Schedule thread */
	private final int ScheduleSleepTime = 10000;

	/** the min number */
	private final int ScheduleMinNum = 0;

	/** the temp ArrayList */
	private ArrayList<String> tmpList = null;
	
	public static StrategyThread_OperateDB getInstance() {
		synchronized(lock) {
			if(null==instance)
			{
				instance = new StrategyThread_OperateDB();
			}
		}
		
		return instance;
	}
	
	private StrategyThread_OperateDB() {
		Thread ODBThread = new Thread(new OperateDB());
		ODBThread.start();
	}
	
	/**
	 * get sql list,and remove it.
	 */
	public static ArrayList<String> GetScheduleSQLList(int length) {
		ArrayList<String> tmpList = null;
		if (length <= 0) {
			return null;
		}
		tmpList = new ArrayList<String>();
		for (int j = 0; j < length; j++) {
			tmpList.add(LipossGlobals.ALL_SQL_IPTV.get(0));
			LipossGlobals.ALL_SQL_IPTV.remove(0);
		}

		return tmpList;
	}
	
	/**
	 * 操作数据库线程
	 * @author Administrator
	 *
	 */
	class OperateDB implements Runnable {
		@Override
		public void run() {
			//logger.debug("BEGIN-----------");
			int length = 0;
			while (true) {
				length = LipossGlobals.ALL_SQL_IPTV.size();
				if (length > ScheduleMinNum) {
					tmpList = GetScheduleSQLList(length);
					logger.debug("执行的SQL(IPTV)-----------:" + tmpList);
					DataSetBean.doBatch(tmpList);
					tmpList.clear();
				}
				try {
					Thread.sleep(ScheduleSleepTime);
				} catch (InterruptedException e) {
				}
			}
			
		}
	}
	
	
}
