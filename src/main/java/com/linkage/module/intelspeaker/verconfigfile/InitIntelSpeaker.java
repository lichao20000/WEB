package com.linkage.module.intelspeaker.verconfigfile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.xml.XML;
import com.linkage.litms.LipossGlobals;
import com.linkage.module.intelspeaker.verconfigfile.entity.FtpUrl;

public class InitIntelSpeaker {
	private static Logger logger = LoggerFactory
			.getLogger(InitIntelSpeaker.class);
	@SuppressWarnings("unchecked")
	public void init(){
		logger.warn("初始化智能音箱-版本配置文件管理相关配置");
		IntelSpeakerGlobal.ftpUrls=new ArrayList<FtpUrl>();
		XML xml = new XML(LipossGlobals.G_ServerHome+File.separator+"WEB-INF"+File.separator + "classes" + File.separator+"liposs_cfg.xml");
		//XML xml=xml = new XML("D:\\workspaceitms\\ailk-itms-web_js\\src\\main\\resources\\liposs_cfg.xml");
		Element termVerConfig=xml.getElement("termVerConfig");
		logger.warn("termVerConfig---"+termVerConfig);
		if(null==termVerConfig){
			logger.warn("初始化智能音箱-失败，liposs_cfg.xml中未配置");
			return;
		}
		IntelSpeakerGlobal.IntelSpeakerConfFPath=termVerConfig.getChildText("configFileLocal");
		logger.warn("本地版本配置文件目录：{}",IntelSpeakerGlobal.IntelSpeakerConfFPath);
		//配置文件放在web根目录下
		IntelSpeakerGlobal.IntelSpeakerConfFPath=LipossGlobals.G_ServerHome+File.separator+IntelSpeakerGlobal.IntelSpeakerConfFPath;
//		IntelSpeakerGlobal.IntelSpeakerConfFPath="D:"+File.separator+IntelSpeakerGlobal.IntelSpeakerConfFPath;

		File IntelSpeakerConfDir=new File(IntelSpeakerGlobal.IntelSpeakerConfFPath);
		if(!IntelSpeakerConfDir.exists()){
			IntelSpeakerConfDir.mkdir();
		}
		List<Element> fileServers=termVerConfig.getChild("fileServers").getChildren();
		for(Element ele:fileServers){
			
			String ip = ele.getChildText("ftpIp");
			String path = ele.getChildText("ftpUrl");
			String username = ele.getChildText("username");
			String password = ele.getChildText("password");
			// 20200401 增加port
			String port_temp = ele.getChildText("port");
					
			int port = 0;
			if(port_temp != null) {
				try {
					port = Integer.parseInt(port_temp);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
			FtpUrl url = new FtpUrl(ip, path, username, password, port);
			
			IntelSpeakerGlobal.ftpUrls.add(url);
			logger.warn(url.toString());
		}
	}
	
	
	public static void main(String[] args) {
		new InitIntelSpeaker().init();
	}
	
}
