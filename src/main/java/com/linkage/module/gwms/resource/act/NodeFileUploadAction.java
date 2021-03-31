/**
 * 
 */
package com.linkage.module.gwms.resource.act;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-12-25
 * @category com.linkage.module.stb.resource.act
 * 
 */
public class NodeFileUploadAction extends ActionSupport {

	//日志记录
	private static Logger logger = LoggerFactory
			.getLogger(NodeFileUploadAction.class);

	
	private static final long serialVersionUID = 572146812454l;

	private static final int BUFFER_SIZE = 16 * 1024 * 1024;

	private File myFile;

	@SuppressWarnings("unused")
	private String contentType;

	@SuppressWarnings("unused")
	private String fileName;

	private List nodeList = new ArrayList();
	
	private String deviceIds = "";

	
	public String getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}

	public List getNodeList() {
		return nodeList;
	}

	public void setNodeList(List nodeList) {
		this.nodeList = nodeList;
	}

	public void setMyFileContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setMyFileFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	

	@Override
	public String execute() {
		//获取系统的绝对路径
		List node_list = new ArrayList();
		BufferedReader in=null;
		try{
				List<String> list = new ArrayList<String>();
				in = new BufferedReader(new FileReader(myFile));
				String line = in.readLine();
				//从第二行开始读取数据，并对每行数据去掉首尾空格
				while ((line = in.readLine()) != null) {
					if(!"".equals(line.trim())){
						node_list.add(line.trim());
					}
				}
							
		}catch(FileNotFoundException e){
				logger.error("文件没找到！");
				return null;
			}catch(IOException e){
				logger.error("文件解析出错！");
				return null;
			}catch(Exception e){
				logger.error("文件解析出错！");
				return null;
			}finally{
				try {
					if(in!=null){
						in.close();
						in=null;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				in = null;
			}
		
		for (int i=0;i<node_list.size();i++)
		{
			Map<String,String> map = new HashMap<String, String>();
			map.put("node",(String) node_list.get(i));
			nodeList.add(map);
			if (i == 0)
			{
				deviceIds = (String) node_list.get(i);
			}else
			{
				deviceIds = deviceIds + ";" + (String) node_list.get(i);
			}
		}

		return SUCCESS;
	}
}
