
package com.linkage.module.gwms.util.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.jms.MQConfigParser;
import com.linkage.commons.jms.MQPublisher;
import com.linkage.module.gwms.Global;

/**
 * 向文件生成模块发送消息公共类
 * 
 * @author guankai (Ailk NO.)
 * @version 1.0
 * @since 20200422
 * @category com.linkage.module.gwms.util.message
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class FileExportMQ
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(FileExportMQ.class);
	private MQPublisher FILE_EXPORT_PUBLISHER;
	/** 系统类型 **/
	private String systemType;

	public FileExportMQ()
	{
		FILE_EXPORT_PUBLISHER = Global.FILE_EXPORT_PUBLISHER;
	}

	/**
	 * 传taskid
	 * 
	 * @param bindInfo
	 * @return
	 */
	public int creatFileTask(String taskId)
	{
		try
		{
			FILE_EXPORT_PUBLISHER.publishMQ(taskId);
		}
		catch (Exception e)
		{
			try
			{
				initMQPool();
				FILE_EXPORT_PUBLISHER.publishMQ(taskId);
			}
			catch (Exception ex)
			{
				logger.error("FILE_EXPORT_PUBLISHER Error.\n{}", ex);
				return 0;
			}
		}
		return 1;
	}

	private void initMQPool()
	{
		Global.MQ_POOL_PUBLISHER_MAP_ITMS = MQConfigParser.getMQConfig(Global.G_MQPoolPath, "itms");
		FILE_EXPORT_PUBLISHER = new MQPublisher("fileExport", Global.MQ_POOL_PUBLISHER_MAP_ITMS);
	}	
	
}
