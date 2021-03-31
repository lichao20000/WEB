/**
 * 
 */
package com.linkage.litms.paramConfig;

/**
 * 同时只支持一个批量配置操作
 * @author zhaixf
 * @date 2008-8-5
 */
public class NTPConfigPram {

	
	private static int flag = 0;
	public static String[] array_device_id = null;
	public static String configType = "";
	public static String status = "";
	public static String main_ntp_server = "";
	public static String second_ntp_server = "";
	public static int successpercent = 0;
	public static int repeatnum = 0;
	public static int testnum = 0;
	
	public static int successCount = 0;	//执行成功的数量
	public static int excutCount = 0;	//执行完成的数量
	public static int hasNext = -1;	//是否继续执行的状态: 0表示不继续，1表示继续
	
	public static long taskId = 0;
	//public static List paramList = new ArrayList(); //用户多个批量配置同时进行
	
/**
	public NTPConfigPram(String[] arrayDeviceId, String paramconfigType,
			String paramstatus, String parammain_ntp,
			String paramsecond_ntp, String parampercent,
			String paramrepeatnum) {
		array_device_id = arrayDeviceId;
		configType = paramconfigType;
		status = paramstatus;
		main_ntp_server = parammain_ntp;
		second_ntp_server = paramsecond_ntp;
		successpercent = parampercent;
		repeatnum = paramrepeatnum;

	}
	*/
	

	/**
	 * 同步参数置1
	 * 
	 * @param 
	 * @author zhaixf
	 * @date 2008-8-6
	 * @return int
	 */
	public synchronized static int freeParam(){
		if(flag != 0){
			flag = 0;
		}
		return flag;
	}
	
	/**
	 * 同步参数加1
	 * 
	 * @param 
	 * @author zhaixf
	 * @date 2008-8-6
	 * @return int
	 */
	public synchronized static boolean lockParam(){
		if(flag == 0){
			++flag;
			return true;
		}else{
			return false;
		}
	}
	
	public static int getFlag(){
		return flag;
	}
	
	public synchronized static void addSuccess(){
		successCount++;
	}
	
	public synchronized static void addExecutCount(){
		excutCount++;
	}

	public static int getSuccessCount() {
		return successCount;
	}

	public static int getExcutCount() {
		return excutCount;
	}
	
}
