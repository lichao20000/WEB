/**
 * 
 */
package com.linkage.litms.paramConfig;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;

/**
 * @author zhaixf
 * @date 2008-8-5
 */
public class NTPConfigThread implements Runnable {

	private int location; // 批量配置设备device_id_array中的位置

	public NTPConfigThread(int i) {
		location = i;
	}

	/**
	 * @author zhaixf
	 * @date 2008-8-6
	 */
	public void run() {

		int redo_num = 0;	//记录配置次数
		String strSQL = "";
		Map<String, String> resultMap = new HashMap<String, String>();
		long time1 = (new Date()).getTime()/1000;
		String device_id = NTPConfigPram.array_device_id[location];
		int repeateNum = NTPConfigPram.repeatnum;
		NTPConfigAct ntpAct = new NTPConfigAct(device_id);
		while(repeateNum > 0){
			//开始配置
			resultMap = ntpAct.NTPConfigure();
			redo_num++;
			// 更新数据库中的执行状态
			if ("ok".equals(resultMap.get(NTPConfigAct.result))) {
				long time2 = (new Date()).getTime()/1000;
				strSQL = "update gw_conf_task_dev set is_start=1,is_succ=1,start_time="+time1+",end_time="
						+ time2 + ",redo_num=" + redo_num + ",dev_desc='"+resultMap.get(NTPConfigAct.device_name)
						+ "' where device_id='"+device_id+"' and task_id=" + NTPConfigPram.taskId;
				NTPConfigPram.addSuccess();	//执行成功数加1
				break;
			} else {
				if(repeateNum == 1){	//最后一次执行
					long time2 = (new Date()).getTime()/1000;
					strSQL = "update gw_conf_task_dev set is_start=1,is_succ=0,start_time="+time1+",end_time="
						+ time2 + ",redo_num=" + redo_num + ",dev_desc='"+resultMap.get(NTPConfigAct.device_name)
						+ "' where device_id='"+device_id+"' and task_id=" + NTPConfigPram.taskId;
				}
			}
			--repeateNum;
		}
		PrepareSQL psql = new PrepareSQL(strSQL);
		DataSetBean.executeUpdate(psql.getSQL());
		NTPConfigPram.addExecutCount();	//已执行总数加1
	}

}
