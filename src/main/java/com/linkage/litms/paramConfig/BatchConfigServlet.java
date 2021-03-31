package com.linkage.litms.paramConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 监听批量配置事件
 * 
 * @author zhaixf
 * @date 2008-8-6
 */
 public class BatchConfigServlet implements Runnable{
	 /** log */
		private static Logger logger = LoggerFactory.getLogger(BatchConfigServlet.class);
	static final long serialVersionUID = 1L;
	
	private static int count = 0;
	
	private static Object obj = new Object();
	
	/**
	 * 保证只启一个线程
	 * 
	 * @param 
	 * @author zhaixf
	 * @date 2008-8-7
	 * @return void
	 */
	public static void startLitener(){
		if(count == 0){
			synchronized(obj){
				if(count == 0){
					new Thread(new BatchConfigServlet()).start();
				}
			}
		}
	}
	
	/**
	 * 启动监听线程(批量配置)
	 * 
	 * @param 
	 * @author zhaixf
	 * @date 2008-8-7
	 * @return void
	 */
	public void run(){
		count++;
		logger.debug("-------BatchConfigServlet-------");
		while(true){
			//如果有需要批量配置的设备
			if (NTPConfigPram.getFlag() > 0){
				//需要配置设备的数量
				int num = NTPConfigPram.array_device_id.length;
				logger.debug("需要配置设备的数量：" + num);
				if(num > 0){
					//测试配置的数量
					int firstConfigNum = NTPConfigPram.testnum;
					logger.debug("测试设备的数量：" + firstConfigNum);
					//测试配置成功的百分比，判断是否需要进行下一步配置
					int percent = NTPConfigPram.successpercent;
					//NTP配置的主，备服务器
					NTPConfigAct.main_ntp_server = NTPConfigPram.main_ntp_server;
					NTPConfigAct.second_ntp_server = NTPConfigPram.second_ntp_server;
					NTPConfigAct.configType = NTPConfigPram.configType;
					NTPConfigAct.status = NTPConfigPram.status;
					//配置测试设备
					for(int i = 0; i < firstConfigNum; i++){
						Thread configThread = new Thread(new NTPConfigThread(i));
						logger.debug("第i个设备配置线程启动：" + i);
						configThread.start();
					}
					//等待测试设备配置结果
					if(firstConfigNum == num){
						//设备已全部配置
					}else{
						//未全部配置，需要判断是否进行下一步配置
						
						//等待测试设备执行出结果
						logger.debug("等待测试结果");
						boolean flag = true;
						while(flag){
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
								NTPConfigPram.freeParam();
							}
							logger.debug("等待测试结果，以判断是否需要继续执行下一步");
//							//执行过程中，设备的成功率已超过预期的成功率
//							if((NTPConfigPram.getSuccessCount()/firstConfigNum)*100 >= percent){
//								NTPConfigPram.hasNext = 1;
//								flag = false;//跳出
//								logger.debug("需要执行其余设备");
//								
//								//执行过程中，设备的失败率已超过预期的失败率
//							}else if (((NTPConfigPram.getExcutCount() - NTPConfigPram.getSuccessCount())/firstConfigNum)*100 >(100-percent)){
//								NTPConfigPram.hasNext = 0;
//								flag = false;
//								logger.debug("不需要执行其余设备");
//							}
							if(firstConfigNum == NTPConfigPram.getExcutCount()){
								int succ_perc = 0;
								if(firstConfigNum ==0){
									logger.debug("测试设备为0台,测试成功率设置为100%");
									succ_perc = 100;
								}else{
									succ_perc = NTPConfigPram.getSuccessCount()*100/firstConfigNum;
								}
								if(succ_perc >= percent){
									NTPConfigPram.hasNext = 1;
									logger.debug("需要执行其余设备");
								}else{
									NTPConfigPram.hasNext = 0;
									logger.debug("不需要执行其余设备");
								}
								int ok_perc = NTPConfigPram.getExcutCount()*100/NTPConfigPram.array_device_id.length;
								BatchCongigDbAct.updateExecutState(NTPConfigPram.taskId, succ_perc, NTPConfigPram.hasNext, ok_perc);
								flag = false;	//跳出
							}
						}
						//如果需要执行下一步
						flag = true;
						if(NTPConfigPram.hasNext == 1){
							for(int i = firstConfigNum; i < num; i++){
								Thread configThread = new Thread(new NTPConfigThread(i));
								logger.debug("开始执行其余设备中的第i个：" + i);
								configThread.start();
							}
							while(flag){
								try {
									Thread.sleep(5000);
								} catch (InterruptedException e) {
									e.printStackTrace();
									NTPConfigPram.freeParam();
								}
								int ok_perc = NTPConfigPram.getExcutCount()*100/NTPConfigPram.array_device_id.length;
								BatchCongigDbAct.updateExecutState(NTPConfigPram.taskId, ok_perc);
								if(NTPConfigPram.array_device_id.length == NTPConfigPram.getExcutCount()){
									BatchCongigDbAct.updateExecutState(NTPConfigPram.taskId);
									flag = false;
								}
							}
						}
					}
				}else{
					NTPConfigPram.freeParam();
				}
			}
			try {
				NTPConfigPram.freeParam();
				Thread.sleep(10000);
				//logger.debug("设备配置listening---");
			} catch (InterruptedException e) {
				e.printStackTrace();
				NTPConfigPram.freeParam();
			}
		}

	}   
	
	public static void destroy(){
		if(Thread.interrupted()){
			count = 0;
		}
	}
}