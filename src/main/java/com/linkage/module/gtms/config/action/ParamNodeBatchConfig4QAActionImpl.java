package com.linkage.module.gtms.config.action;

import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DBOperation;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.SocketUtil;
import com.linkage.module.gwms.util.StringUtil;

public class ParamNodeBatchConfig4QAActionImpl {
	private static Logger logger = LoggerFactory.getLogger(VOIPDigitMapBatchActionImpl.class);
	
	private String ajax = null;
	/**区分ITMS和BBMS的功能*/
	private String gw_type ="";
	private String  deviceIds ;
	/** 参数节点路径 */
	private String  paramNodePath ;
	/**参数值 */
	private String  paramValue ;
	/** 参数类型 */
	private String  paramType ;
	
	private String param;
	/**
	 * 批量修改参数节点
	 * 
	 * @return
	 */
	public String doConfigAll(){
		logger.warn("doConfigAll({},{},{},{})",new Object[]{deviceIds,paramNodePath,paramValue,paramType});
		try
		{
			String[] paramNodePaths = paramNodePath.split(",");
			String[] paramValues = paramValue.split(",");
			String[] paramTypes = paramType.split(",");

			long taskid = new Date().getTime();
			long time = taskid/1000;
			StringBuffer value = new StringBuffer();
			
			int len = paramNodePaths.length;
			for(int i=0 ;i<len-1 ;i++){
				if(!StringUtil.IsEmpty(value.toString()))
				{
					value.append(",");
				}
				
				value.append(paramNodePaths[i+1]).append("=").append(paramValues[i+1]).append("=").append(paramTypes[i+1]);
				
			}
			logger.warn("taskid={},time={},value={},deviceIds={}",new Object[]{taskid,time,value,deviceIds});
			if (true == StringUtil.IsEmpty(deviceIds))
			{
				logger.debug("任务中没有设备");
				ajax="任务中没有设备";
			}
			//直接传deviceId数组调配置模块接口
			else if(!"0".equals(deviceIds))
			{
				logger.warn("批量参数配置小于50，直接传deviceId数组调配置模块接口");
				String[] deviceId_array = null;
				deviceId_array = deviceIds.split(",");
				if(deviceId_array.length>500)
				{
					logger.warn("导入设备或者用户超过500，不予执行。");
					ajax = "-2";
				}
				else
				{
					for (int i = 0; i < deviceId_array.length; i++)
					{
						int index = 0;
						String deviceId = deviceId_array[i];
						PrepareSQL pSQL = new PrepareSQL();
						pSQL.setSQL("insert into tab_yog_security(task_id,device_id,add_time,start_time,value) values (?,?,?,?,?)");
						pSQL.setLong(++index, taskid);
						pSQL.setString(++index, deviceId);
						pSQL.setLong(++index, time);
						pSQL.setLong(++index, time);
						pSQL.setString(++index, value.toString());
						DBOperation.executeUpdate(pSQL.getSQL(),"proxool");
					}
					String server = Global.G_ITMS_Sheet_Server;
					int port = Global.G_ITMS_Sheet_Port;
					//通知后台模块进行配置处理
					SocketUtil.sendStrMesg(server, port, "ParamNodeBatchConfig");
					ajax = "1";
				}
				
			}
			else
			{//调用后台corba接口
				logger.warn("直接把SQL传给配置模块做批量参数修改="+param);
//				String[] _param = param.split("\\|");
//				String matchSQL = _param[10];
//				ajax  = bio.doConfigAll(new String[]{matchSQL.replace("[", "\'")}, serviceId, paramArr,gw_type);
				ajax = "1";
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
			logger.warn("Exception---"+e.getMessage());
			ajax = "-4";
			return "ajax";
		}
		
		return "ajax";
		
	}
	
	
	public String getAjax() {
		return ajax;
	}
	public void setAjax(String ajax) {
		this.ajax = ajax;
	}
	public String getGw_type() {
		return gw_type;
	}
	public void setGw_type(String gwType) {
		gw_type = gwType;
	}
	public String getParamNodePath() {
		return paramNodePath;
	}
	public void setParamNodePath(String paramNodePath) {
		this.paramNodePath = paramNodePath;
	}
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	public String getParamType() {
		return paramType;
	}
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	
	
	public String getDeviceIds()
	{
		return deviceIds;
	}

	
	public void setDeviceIds(String deviceIds)
	{
		this.deviceIds = deviceIds;
	}

	public String getParam()
	{
		return param;
	}

	
	public void setParam(String param)
	{
		this.param = param;
	}
	
}
