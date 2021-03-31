package com.linkage.module.intelspeaker.verconfigfile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.commons.jms.MQPublisher;
import com.linkage.commons.jms.obj.MQConfig;
import com.linkage.module.intelspeaker.verconfigfile.entity.FtpUrl;
/**
 * 智能印象全局配置项
 * @author jlp
 *
 */
public class IntelSpeakerGlobal {
	/**文件服务器地址信息，用于上传最新版本文件**/
	public static List<FtpUrl> ftpUrls=null;
	/**web中智能音箱配置文件目录**/
	public static String IntelSpeakerConfFPath;
	/**web路由表配置文件名**/
	public static final String routerConfFName="RouteConfig.xml";
	/**web白名单配置文件名**/
	public static final String filterConfFName="FilterConfig.xml";
	/**web路由表配置文件名**/
	public static final String routerConfFNameT="RouteConfigTwo.xml";
	
	public static String G_MQPoolPath = null;
	
	/** cm.Interface主题的生产者*/
	public static MQPublisher COMMON_PUBLISHER = null;
	
	public static Map<String, MQConfig> MQ_POOL_PUBLISHER_MAP = new HashMap<String, MQConfig>();
	
	/**
	 * 获取配置文件path
	 * @param type
	 * @return
	 */
	public static File getNewestFilePath(int type){
		if(IntelSpeakerConfFPath==null){
			new InitIntelSpeaker().init();
		}
		if(1==type){
			return new File(IntelSpeakerConfFPath+(IntelSpeakerConfFPath.endsWith(File.separator)?"":File.separator)+routerConfFName);
		}else if(2==type){
			return new File(IntelSpeakerConfFPath+(IntelSpeakerConfFPath.endsWith(File.separator)?"":File.separator)+filterConfFName);
		}else if(3==type){
			return new File(IntelSpeakerConfFPath+(IntelSpeakerConfFPath.endsWith(File.separator)?"":File.separator)+routerConfFNameT);
		}else{
			return null;
		}
	}
}
