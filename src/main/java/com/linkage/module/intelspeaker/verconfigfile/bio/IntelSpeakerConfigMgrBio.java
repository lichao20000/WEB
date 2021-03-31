package com.linkage.module.intelspeaker.verconfigfile.bio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.ftp.FtpClient;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.xml.XMLProperties;
import com.linkage.module.intelspeaker.verconfigfile.IntelSpeakerGlobal;
import com.linkage.module.intelspeaker.verconfigfile.dao.IntelSpeakerConfigMgrDao;
import com.linkage.module.intelspeaker.verconfigfile.entity.Filter;
import com.linkage.module.intelspeaker.verconfigfile.entity.FilterTerminal;
import com.linkage.module.intelspeaker.verconfigfile.entity.FilterTerminalList;
import com.linkage.module.intelspeaker.verconfigfile.entity.Router;
import com.linkage.module.intelspeaker.verconfigfile.entity.RouterTerminal;
import com.linkage.module.intelspeaker.verconfigfile.entity.RouterTerminalList;
import com.linkage.module.intelspeaker.verconfigfile.entity.RouterTerminalListBak;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;

public class IntelSpeakerConfigMgrBio {
	private static Logger logger = LoggerFactory
			.getLogger(IntelSpeakerConfigMgrBio.class);
	private IntelSpeakerConfigMgrDao dao;
	
	/**
	 * 更新 路由版本和白名单版本表信息
	 * @param
	 * @param version
	 * @param newRouterVer
	 * @param downloadUrl
	 * @param bakFileName
	 * @param userId
	 * @param type
	 */
	public void updateVersion(String version, String newRouterVer,String downloadUrl,String bakFileName,String userId, int type){
		long time=System.currentTimeMillis();
		dao.insertVersionLog(version, bakFileName, type, time, userId);
		dao.updateNestVersionConfig(newRouterVer, downloadUrl, type, time);
	}
	
	//查询总数
	public int countHistory(int type){
		return dao.countHistory(type);
	}
	
	//查询第startPage页
	public List<Map<String,String>> listHistory(int type,int startPage,int pageSize,int total){
		int endIndex=((int)total/pageSize>=startPage)?startPage*pageSize:((startPage-1)*pageSize+total%pageSize);
		return dao.queryHistoryVer(type, (startPage-1)*pageSize, endIndex);
	}
	
	//用ftp上传到各个文件服务器
	private String upLoadToFileServer(String version, String absolutePath, String ip, String path, 
			String user, String password) {
		String localAbsoluteFile =IntelSpeakerGlobal.IntelSpeakerConfFPath+File.separator +absolutePath;
		String remoteAbsoluteFile = path +"/" +absolutePath;
		try {
			
			boolean result = false;
			
			FtpClient ftpClient = new FtpClient(ip, user, password);
			boolean connectResult = ftpClient.connect();  // 连接FTP服务器
			
			if(connectResult){
				/** remoteAbsoluteFile 远程路径   localAbsoluteFile 本地路劲*/
				result = ftpClient.put(remoteAbsoluteFile, localAbsoluteFile, false);
			}
			
			if (!result) {
				logger.warn("文件\""+localAbsoluteFile+"\"FTP至 "+ip+"服务器失败！");
				return "1";
			}else {
				logger.warn("文件\""+localAbsoluteFile+"\"FTP至 "+ip+"服务器成功！");
				return "0";
			}
		} catch (Exception e) {
			logger.warn("FTP至"+ip+"服务器文件时异常，msg=({})", e.getMessage());
			return "1";
		}
	}

	//用scp上传到各个文件服务器
	private int upLoadToFileServerByScp(String version, String absolutePath, String ip, String remotePath, 
			String user, String password, int port) {
		String localAbsoluteFile =IntelSpeakerGlobal.IntelSpeakerConfFPath+File.separator +absolutePath;
		
		boolean isSucc = true;
		
		try {
			if (InetAddress.getByName(ip).isReachable(1500)) {
                Connection conn = new Connection(ip, port);// 连接服务器
                conn.connect();
                boolean isAuthed = conn.authenticateWithPassword(user, password);
                if (isAuthed) {
            		SCPClient scpClient = conn.createSCPClient();
                    // localAbsoluteFile 本地路径  remotePath 远程路径
                    scpClient.put(localAbsoluteFile, remotePath);
                    conn.close();
                }else {
                	isSucc = false;
                }
            }else {
            	isSucc = false;
            }
		} catch (Exception e) {
			isSucc = false;
			logger.error("message:{}",e.getMessage());
		}
		if(!isSucc) {
			logger.warn("文件\""+localAbsoluteFile+"\"SCP至 "+ip+"服务器失败！");
			return 0;
		}else {
			logger.warn("文件\""+localAbsoluteFile+"\"SCP至 "+ip+"服务器成功！");
			return 1;
		}
	}
	
	
	/**
	 * 返回一个下载到页面
	 * @param type 
	 * @param request
	 * @param resp
	 * @return 
	 */
	public InputStream returnDownLoadFile(String newFileName){
		File downFile=new File(newFileName);
		FileInputStream fs=null;
		if(downFile.exists()){
			try {
				fs=new FileInputStream(downFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return fs;
	}
	
	/**
	 * 页面查看文件
	 * @param
	 * @param type
	 * @param version
	 * @return
	 */
	public String readOneFile(int type,String version){
		String path = IntelSpeakerGlobal.getNewestFilePath(type).getPath();
		String newFileName = path.substring(0, path.length()-4)+version+".xml";
		File downFile=new File(newFileName);
		StringBuilder sb=new StringBuilder();
		if(downFile.exists()){
			BufferedReader buf=null;
				try {
					
					buf = new BufferedReader(new FileReader(downFile));
					String str = null;
			        while((str=buf.readLine())!=null){
			            sb.append(str.replaceAll("<", "&lt;").replaceAll(">", "&gt;")).append("<br>");
			        }
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					try {
						if(buf!=null){
							buf.close();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return sb.toString();
		}else{
			return null;
		}
			
	}

	public IntelSpeakerConfigMgrDao getDao() {
		return dao;
	}

	public void setDao(IntelSpeakerConfigMgrDao dao) {
		this.dao = dao;
	}
	
	/**
	 * 获取最新的路由表版本配置对象
	 * 单个Terminal
	 * 没有文件时，会自动创建一个版本号为0.0.0的文件
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public RouterTerminalList getRouterTmConfList(){
		File confF=IntelSpeakerGlobal.getNewestFilePath(1);
		RouterTerminalList rtList=null;
		RouterTerminal rt=null;
		if(confF.exists()){
			rtList = new RouterTerminalList();
			XMLProperties properties= new XMLProperties(confF.getAbsolutePath());
			// 获取terminalList
			List<Element> terminalInfo = properties.getRootElement().getChildren("TerminalInfo");
			
			rtList.setRouteVer(properties.getRootElement().getAttributeValue("RouteVer"));
			// 循环遍历terminalList
			for(Element el : terminalInfo){
				rt=new RouterTerminal();
				List<Element> routes=el.getChildren("Routes");
				List<Router> routers=new ArrayList<Router>();
				rt.setType(el.getChildText("Type"));
				rt.setVLANID(el.getChildText("VLANID"));
				rt.setSoftver(el.getChildText("Softver"));
				rt.setDNSSlect(el.getChildText("DNSSlect"));
				for(Element rouEl:routes){
					rouEl.toString();
					Router router=new Router(rouEl.getChildText("Destination"),rouEl.getChildText("Netmask"),rouEl.getChildText("Plane"));
					routers.add(router);
					rt.setRouters(routers);
				}
			}
			rtList.setRouterTerminals(rt);
			
		}else{
			rtList = new RouterTerminalList();
			rtList.setRouteVer("0.0.0");
			rt=new RouterTerminal();
			List<Router> routers=new ArrayList<Router>();
			rt.setType("");
			rt.setVLANID("");
			rt.setSoftver("");
			rt.setDNSSlect("");
			Router router=new Router("","","");
			routers.add(router);
			rt.setRouters(routers);
			rtList.setRouterTerminals(rt);
		}
		return rtList;
	}
	
	
	/**
	 * 编辑保存单个路由表
	 * @param
	 * @param version
	 * @param termVerInst
	 * @param userId
	 * @return
	 */
	public int updateConfigFile(String version,RouterTerminalList termVerInst,String userId){
		// 获取新版本
		String newRouterVer = termVerInst.getRouteVer();
		// 拼接新文件的内容
		String newFileContent=termVerInst.toXMLString();
		// 获取当前文件名
		File srcFile=IntelSpeakerGlobal.getNewestFilePath(1);
		
		String bakFileName = srcFile.getPath().substring(0, srcFile.getPath().length()-4)+version+".xml";
		
		File aimFile=new File(bakFileName);
		if(!srcFile.exists()){
			return -1;
		}
		//如果该版本文件已存在，返回错误。
		if(aimFile.exists()){
			return -2;
		};
		//重命名
		if(!srcFile.renameTo(aimFile)){
			return -3;
		};
		File f=new File(srcFile.getAbsolutePath());
		FileWriter fw=null;
		try {
			f.createNewFile();
			fw=new FileWriter(f);
			fw.write(newFileContent);
			fw.flush();
			if(null!=fw){
				fw.close();
			}
			this.updateVersion(version,newRouterVer, srcFile.getAbsolutePath(),bakFileName, userId,1);
			//最新文件上传到各个文件服务器
			String ip = "";
			String path = "";
			String user = "";
			String password = "";
			int port = 0;
			int size = IntelSpeakerGlobal.ftpUrls.size();
			if (size>0){
				for (int i =0;i< IntelSpeakerGlobal.ftpUrls.size();i++){
					ip = IntelSpeakerGlobal.ftpUrls.get(i).getFtpip();
					path = IntelSpeakerGlobal.ftpUrls.get(i).getPath();
					user = IntelSpeakerGlobal.ftpUrls.get(i).getUser(); 
					password = IntelSpeakerGlobal.ftpUrls.get(i).getPassword(); 
					port = IntelSpeakerGlobal.ftpUrls.get(i).getPort();
					
					// 如果是甘肃电信使用scp方式上传文件到文件服务器
					if(LipossGlobals.isGSDX()) {
						this.upLoadToFileServerByScp(version, f.getName(), ip, path, user, password, port);
					}else {
						this.upLoadToFileServer(version, f.getName(), ip, path, user, password);
					}
				}
			}
			IntelSpeakerGlobal.COMMON_PUBLISHER.getPublisher().publish("term.newestinfo", "routerVer:" + newRouterVer);			
		} catch (IOException e) {
			logger.error(e.getMessage());
			return -4;
		}finally{
			if(null!=fw){
				try {
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return 1;
	}
	
	
	/**
	 * 获取最新的路由表版本配置对象
	 * 多个Terminal
	 * 没有文件时，会自动创建一个版本号为0.0.0的文件
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public RouterTerminalListBak getRouterTmConfListBak(){
		File confF=IntelSpeakerGlobal.getNewestFilePath(1);
		RouterTerminalListBak rtList=null;
		RouterTerminal rt=null;
		if(confF.exists()){
			rtList = new RouterTerminalListBak();
			XMLProperties properties= new XMLProperties(confF.getAbsolutePath());
			List<RouterTerminal> list = new ArrayList<RouterTerminal>();
			// 获取terminalList
			List<Element> terminalInfo = properties.getRootElement().getChildren("TerminalInfo");
			
			rtList.setRouteVer(properties.getRootElement().getAttributeValue("RouteVer"));
			// 循环遍历terminalList
			for(Element el : terminalInfo){
				rt=new RouterTerminal();
				List<Element> routes=el.getChildren("Routes");
				List<Router> routers=new ArrayList<Router>();
				rt.setType(el.getChildText("Type"));
				rt.setVLANID(el.getChildText("VLANID"));
				rt.setSoftver(el.getChildText("Softver"));
				rt.setDNSSlect(el.getChildText("DNSSlect"));
				for(Element rouEl:routes){
					rouEl.toString();
					Router router=new Router(rouEl.getChildText("Destination"),rouEl.getChildText("Netmask"),rouEl.getChildText("Plane"));
					routers.add(router);
					rt.setRouters(routers);
				}
				list.add(rt);
			}
			rtList.setRouterTerminals(list);
			
		}else{
			File x=IntelSpeakerGlobal.getNewestFilePath(1);
			try {
				x.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			rtList = new RouterTerminalListBak();
			rtList.setRouteVer("0.0.0");
			List<RouterTerminal> list = new ArrayList<RouterTerminal>();
			rt=new RouterTerminal();
			rt.setType("");
			rt.setVLANID("");
			rt.setSoftver("");
			rt.setDNSSlect("");
			List<Router> routers=new ArrayList<Router>();
			Router router=new Router("","","");
			routers.add(router);
			rt.setRouters(routers);
			list.add(rt);
			rtList.setRouterTerminals(list);
		}
		return rtList;
	}
	
	/**
	 * 先将原始RouteConfig.xml文件重命名为RouteConfig+版本号.xml，
	 * 然后创建RouteConfig.xml文件，把编辑后的内容写进去
	 * 编辑多个terminal
	 * 将信息入库，然后再将最新的文件Ftp到指定文件服务器
	 * @param
	 * @param version
	 * @param termVerInst
	 * @param userId
	 * @return
	 */
	public int updateRouteConfigFile(String version,RouterTerminalListBak termVerInst,String userId){
		// 获取新版本
		String newRouterVer = termVerInst.getRouteVer();
		// 拼接新文件的内容
		String newFileContent=termVerInst.toXMLString();
		// 获取当前文件名
		File srcFile=IntelSpeakerGlobal.getNewestFilePath(1);
		
		String bakFileName = srcFile.getPath().substring(0, srcFile.getPath().length()-4)+version+".xml";
		
		File aimFile=new File(bakFileName);
		if(!srcFile.exists()){
			return -1;
		}
		//如果该版本文件已存在，返回错误。
		if(aimFile.exists()){
			return -2;
		};
		
		//重命名
		if(!srcFile.renameTo(aimFile)){
			return -3;
		};
		
		File f=new File(srcFile.getAbsolutePath());
		FileWriter fw=null;
		
		int result = 1;
		
		try {
			f.createNewFile();
			fw=new FileWriter(f);
			fw.write(newFileContent);
			fw.flush();
			if(null!=fw){
				fw.close();
			}
			this.updateVersion(version,newRouterVer, srcFile.getAbsolutePath(),bakFileName, userId,1);
			//最新文件上传到各个文件服务器
			String ip = "";
			String path = "";
			String user = "";
			String password = "";
			int port = 0;
			int size = IntelSpeakerGlobal.ftpUrls.size();
			if (size>0){
				for (int i =0;i< IntelSpeakerGlobal.ftpUrls.size();i++){
					ip = IntelSpeakerGlobal.ftpUrls.get(i).getFtpip();
					path = IntelSpeakerGlobal.ftpUrls.get(i).getPath();
					user = IntelSpeakerGlobal.ftpUrls.get(i).getUser(); 
					password = IntelSpeakerGlobal.ftpUrls.get(i).getPassword(); 
					port = IntelSpeakerGlobal.ftpUrls.get(i).getPort();
					
					// 如果是甘肃电信使用scp方式上传文件到文件服务器
					if(LipossGlobals.isGSDX()) {
						result = this.upLoadToFileServerByScp(version, f.getName(), ip, path, user, password, port);
					}else {
						this.upLoadToFileServer(version, f.getName(), ip, path, user, password);
					}
				}
			}
			try {
				IntelSpeakerGlobal.COMMON_PUBLISHER.getPublisher().publish("term.newestinfo", "routerVer:" + newRouterVer);
			} catch (Exception e) {
				logger.warn("主题:[term.newestinfo],消息:[{}]发布失败: {}", "routerVer:" + newRouterVer, e.getMessage());
			}
			
		} catch (IOException e) {
			logger.error(e.getMessage());
			return -4;
		}
		return result;
	}
	
	
	/**
	 * 获取最新的白名单版本配置对象
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public FilterTerminalList getFilterTmConfList(){
		File confF =IntelSpeakerGlobal.getNewestFilePath(2);
		FilterTerminalList rtList=null;
		FilterTerminal rt=null;
		if(confF.exists()){
			rtList = new FilterTerminalList();
			XMLProperties properties= new XMLProperties(confF.getAbsolutePath());
			List<FilterTerminal> list = new ArrayList<FilterTerminal>();
			// 获取terminalList
			List<Element> terminalInfo = properties.getRootElement().getChildren("TerminalInfo");
			
			rtList.setFilterVer(properties.getRootElement().getAttributeValue("FilterVer"));
			// 循环遍历terminalList
			for(Element el : terminalInfo){
				rt=new FilterTerminal();
				List<Element> filts=el.getChildren("Filters");
				List<Filter> routers=new ArrayList<Filter>();
				rt.setType(el.getChildText("Type"));
				rt.setSoftver(el.getChildText("Softver"));
				for(Element f:filts){
					f.toString();
					Filter filter=new Filter(f.getChildText("Destination"),f.getChildText("Netmask"));
					routers.add(filter);
					rt.setFilters(routers);
				}
				list.add(rt);
			}
			rtList.setFilterTerminals(list);
			
		}else{
			File x=IntelSpeakerGlobal.getNewestFilePath(2);
			try {
				x.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			rtList = new FilterTerminalList();
			rtList.setFilterVer("0.0.0");
			List<FilterTerminal> list = new ArrayList<FilterTerminal>();
			rt=new FilterTerminal();
			rt.setType("");
			rt.setSoftver("");
			List<Filter> routers=new ArrayList<Filter>();
			Filter filter=new Filter("","");
			routers.add(filter);
			rt.setFilters(routers);
			list.add(rt);
			rtList.setFilterTerminals(list);
		}
		return rtList;
	}
	
	/**
	 * 先将原始FilterConfig.xml文件重命名为FilterConfig+版本号.xml，
	 * 然后创建FilterConfig.xml文件，把编辑后的内容写进去
	 * @param
	 * @param version
	 * @param termVerInst
	 * @param userId
	 * @return
	 */
	public int updateFilterConfigFile(String version,FilterTerminalList termVerInst,String userId){
		// 获取新版本
		String newFilterVer = termVerInst.getFilterVer();
		// 拼接新文件的内容
		String newFileContent=termVerInst.toXMLString();
		// 获取当前文件名
		File srcFile=IntelSpeakerGlobal.getNewestFilePath(2);
		
		String bakFileName = srcFile.getPath().substring(0, srcFile.getPath().length()-4)+version+".xml";
		File aimFile=new File(bakFileName);
		if(!srcFile.exists()){
			return -1;
		}
		//如果该版本文件已存在，返回错误。
		if(aimFile.exists()){
			return -2;
		};
		//重命名
		if(!srcFile.renameTo(aimFile)){
			return -3;
		};
		File f=new File(srcFile.getAbsolutePath());
		FileWriter fw=null;
		try {
			f.createNewFile();
			fw=new FileWriter(f);
			fw.write(newFileContent);
			fw.flush();
			if(null!=fw){
				fw.close();
				fw=null;
			}
			this.updateVersion(version,newFilterVer, srcFile.getAbsolutePath(),bakFileName, userId,2);
			//最新文件上传到各个文件服务器
			String ip = "";
			String path = "";
			String user = "";
			String password = "";
			int port = 0;
			int size = IntelSpeakerGlobal.ftpUrls.size();
			if (size>0){
				for (int i =0;i< IntelSpeakerGlobal.ftpUrls.size();i++){
					ip = IntelSpeakerGlobal.ftpUrls.get(i).getFtpip();
					path = IntelSpeakerGlobal.ftpUrls.get(i).getPath();
					user = IntelSpeakerGlobal.ftpUrls.get(i).getUser(); 
					password = IntelSpeakerGlobal.ftpUrls.get(i).getPassword(); 
					port = IntelSpeakerGlobal.ftpUrls.get(i).getPort();
					
					// 如果是甘肃电信使用scp方式上传文件到文件服务器
					if(LipossGlobals.isGSDX()) {
						this.upLoadToFileServerByScp(version, f.getName(), ip, path, user, password, port);
					}else {
						this.upLoadToFileServer(version, f.getName(), ip, path, user, password);
					}
				}
			}
			IntelSpeakerGlobal.COMMON_PUBLISHER.getPublisher().publish("term.newestinfo", "routerVer:" + newFilterVer);
		} catch (IOException e) {
			logger.error(e.getMessage());
			return -4;
		}finally{
			try {
				if(fw!=null){
					fw.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 1;
	}
}
