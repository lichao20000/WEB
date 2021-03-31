
package com.linkage.module.gtms.stb.resource.action;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.stb.resource.serv.FileServerManageBIO;
import com.linkage.module.gtms.stb.utils.XMLFormatUtil;
import com.linkage.module.ids.util.WSClientUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
*    
* 项目名称：ailk-itms-web   
* 类名称：FileServerManageACT   
* 类描述：   
* 创建人：guxl3   
* 创建时间：2019年5月28日 下午3:30:31   
* @version
 */
public class FileServerManageACT extends ActionSupport implements SessionAware,ServletRequestAware
{

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 5112559061684202962L;
	private static Logger logger = LoggerFactory.getLogger(FileServerManageACT.class);
	
	private String server_name;
	private String host;
	private String ajax;
	private FileServerManageBIO bio;
	private Map session;
	private HttpServletRequest request;
	private int colspanNum;
	private int rowspanNum;

	private List<Map<String, String>> serverList = null;
	private List<Map<String, String>> fileDirectoryList=new ArrayList<Map<String,String>>();
	
	public String serverQueryList() {
		serverList = bio.queryServer();
		return "init";
	}
	

	
	public String fileQueryList() {
		colspanNum=0;
		String id = request.getParameter("id");
		List<Map> list = bio.queryServer(id);
		if (list!=null && list.size()>0) {
			server_name=list.get(0).get("server_name").toString();
			host=list.get(0).get("host").toString();
			String port=list.get(0).get("port").toString();
			String username=list.get(0).get("username").toString();
			String password=list.get(0).get("password").toString();
			
			StringBuffer sb=new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>	\n");
			sb.append("<root> \n");
			sb.append("<host>"+host+"</host> \n");
			sb.append("<port>"+port+"</port> \n");
			sb.append("<username>"+username+"</username> \n");
			sb.append("<password>"+password+"</password> \n");
			sb.append("</root>");
			
			logger.warn("入参xml："+sb.toString());
			//调接口
			String methodName = "fileServerFTP_hb";
			String url="http://192.168.28.92:8080/ItmsService/services/CallService";
			//回参
			String callBack =  WSClientUtil.callItmsService(url, sb.toString(), methodName);
			logger.warn("回参xml："+callBack);
			
			try {
				SAXReader reader = new SAXReader();
				Document document = null;
				document = reader.read(new StringReader(callBack));
				Element root = document.getRootElement();
				colspanNum = Integer.parseInt(root.elementText("colspanNum"));
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			
			
			callBack=callBack.split("<colspanNum>")[0]+callBack.split("<colspanNum>")[1].split("</colspanNum>")[1];
			fileDirectoryList = XMLFormatUtil.xmlToList(callBack);
			
		}
		rowspanNum=fileDirectoryList.size();
		return "fileDirector";
	}
	
	
	/*public String fileQueryList() {
		colspanNum=0;
		String id = request.getParameter("id");
		List<Map> list = bio.queryServer(id);
		if (list!=null && list.size()>0) {
			server_name=list.get(0).get("server_name").toString();
			host=list.get(0).get("host").toString();
			String port=list.get(0).get("port").toString();
			String username=list.get(0).get("username").toString();
			String password=list.get(0).get("password").toString();
			
			FTPListAllFiles f = new FTPListAllFiles(false);
	        try {
				if (f.login(host, Integer.parseInt(port), username, password)) {
				    f.List("/export/home/stb/FileServer/STB/STB/");
				    f.disConnection();
				}
			}catch (IOException e) {
				e.printStackTrace();
			}
	        for (String arFile : f.arFiles) {
	        	Map<String, String> map=new LinkedHashMap<String, String>();
	        	String[] split = arFile.split("/STB/STB/");
	        	//文件服务器相对路径
	        	String path=split[1];
	        	String[] paths = path.split("/");
	        	//记录最大路径
	        	if (paths.length>colspanNum) {
	        		colspanNum=paths.length;
				}
	        	for (int i = 0; i < paths.length; i++) {
					map.put("path"+i, paths[i]);
				}
	        	
	        	fileDirectoryList.add(map);
	        }
		}
		rowspanNum=fileDirectoryList.size();
		return "fileDirector";
	}*/

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	public FileServerManageBIO getBio() {
		return bio;
	}

	public void setBio(FileServerManageBIO bio) {
		this.bio = bio;
	}

	public List<Map<String, String>> getServerList() {
		return serverList;
	}

	public void setServerList(List<Map<String, String>> serverList) {
		this.serverList = serverList;
	}

	
	public List<Map<String, String>> getFileDirectoryList() {
		return fileDirectoryList;
	}

	public void setFileDirectoryList(List<Map<String, String>> fileDirectoryList) {
		this.fileDirectoryList = fileDirectoryList;
	}

	public String getServer_name() {
		return server_name;
	}

	public void setServer_name(String server_name) {
		this.server_name = server_name;
	}


	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}


	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}


	public int getColspanNum() {
		return colspanNum;
	}


	public void setColspanNum(int colspanNum) {
		this.colspanNum = colspanNum;
	}


	public int getRowspanNum() {
		return rowspanNum;
	}


	public void setRowspanNum(int rowspanNum) {
		this.rowspanNum = rowspanNum;
	}

	
}
