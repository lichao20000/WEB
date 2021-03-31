package com.linkage.module.gwms.util;

import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.corba.PreProcessCorba;
import com.linkage.module.gwms.util.corba.ResourceBindCorba;
import com.linkage.module.gwms.util.message.PreProcessMQ;
import com.linkage.module.gwms.util.message.ResourceBindMQ;

/**
 * 创建Corab、MQ公用类对象
 * 
 * @author jiafh (Ailk NO.)
 * @version 1.0
 * @since 2016-11-3
 * @category com.linkage.module.gwms.util
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 * 
 */
public class CreateObjectFactory {

	// 常量
	private static final String TWO = "2";
	
	
	/**
	 * 生成调用配置模块的对象，不带参数
	 * 
	 * @return
	 */
	public static PreProcessInterface createPreProcess() {
		
		// 如果配置的为2，则使用发送消息方式，否则使用corba方式
		if (TWO.equals(Global.PRE_PROCESS_TYPE)) {
			return new PreProcessMQ();
		} else {
			return new PreProcessCorba();
		}
	}


	/**
	 * 生成调用配置模块的对象，带参数
	 * 
	 * @param gwType
	 * @return
	 */
	public static PreProcessInterface createPreProcess(String gwType) {
		if(Global.GW_TYPE_STB.equals(gwType)){
			// 如果配置的为2，则使用发送消息方式，否则使用corba方式
			if (TWO.equals(Global.STB_PRE_PROCESS_TYPE)) {
				
				return new PreProcessMQ(gwType);
			} else {
				return new PreProcessCorba(gwType);
			}
		}
		else
		{
			// 如果配置的为2，则使用发送消息方式，否则使用corba方式
			if (TWO.equals(Global.PRE_PROCESS_TYPE)) {
				
				return new PreProcessMQ(gwType);
			} else {
				return new PreProcessCorba(gwType);
			}
		}
		
		
	}
	
	/**
	 * 生成调用绑定模块的对象，不带参数
	 * 
	 * @return
	 */
	public static ResourceBindInterface createResourceBind() {

		// 如果配置的为2，则使用发送消息方式，否则使用corba方式
		if (TWO.equals(Global.RESOURCE_BIND_TYPE)) {
			return new ResourceBindMQ();
		} else {
			return new ResourceBindCorba();
		}
	}

	/**
	 * 生成调用绑定模块的对象，带参数
	 * 
	 * @param gwType
	 * @return
	 */
	public static ResourceBindInterface createResourceBind(String gwType) {
		if(Global.GW_TYPE_STB.equals(gwType)){
			// 如果配置的为2，则使用发送消息方式，否则使用corba方式
			if (TWO.equals(Global.STB_RESOURCE_BIND_TYPE)) {
				return new ResourceBindMQ(gwType);
			} else {
				return new ResourceBindCorba(gwType);
			}
		}
		else
		{
			// 如果配置的为2，则使用发送消息方式，否则使用corba方式
			if (TWO.equals(Global.RESOURCE_BIND_TYPE)) {
				return new ResourceBindMQ(gwType);
			} else {
				return new ResourceBindCorba(gwType);
			}
		}

		
	}
}
