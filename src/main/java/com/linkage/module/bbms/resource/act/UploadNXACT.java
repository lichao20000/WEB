package com.linkage.module.bbms.resource.act;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.resource.act.SoftwareACT;
import com.linkage.module.gwms.resource.bio.SoftwareBIO;
import com.linkage.module.gwms.share.bio.GwDeviceQueryBIO;
import com.linkage.module.gwms.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author songxq
 * @version 1.0
 * @since 2018-11-15 上午11:02:08
 * @category 
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class UploadNXACT extends ActionSupport implements SessionAware
{
	
	private static Logger logger = LoggerFactory.getLogger(SoftwareACT.class);
	private String deviceIds;
	private String ajax;
	private SoftwareBIO bio;
	// session
	private Map session;
	private String softStrategyHTML;
	private String goal_devicetype_id;
	private String softStrategy_type;
	private String param;
	
	private String strRename;
	private String urlParameter;
	private File file1;
	private String response;
	private String path;
	private String gw_type;
	private GwDeviceQueryBIO gwDeviceQueryBio;
	private String fileName;
	private String importQueryField;
	private String gwShare_queryType;
	private String gwShare_queryType_this;
	private String starttime;
	private String endtime;
	
	private String taskId;
	private String mode;
	private List taskList ;
	private String maxActive;
	private String taskName;
	
	
	
	/**
	 * <p>
	 * [文件上传到WEB]
	 * </p>
	 * @return
	 */
	public String uploadLocalFile()
	{
//        logger.error("开始!");
	    int index =  urlParameter.indexOf("path");
	   
        String strPath = urlParameter.substring(index + 5,urlParameter.indexOf("&", index));

        //目录
        File localPath = new File(LipossGlobals.G_ServerHome + "/" + strPath);
        if(!localPath.exists())
        {
            if(!localPath.mkdirs())
            {
                logger.error("缓存文件目录:{}创建失败！",LipossGlobals.G_ServerHome + "/" + strPath);
                this.response = "缓存文件目录创建失败！";
                return "response";
            }
        }
        
//        logger.error("-----------------------" + LipossGlobals.G_ServerHome + "/" + strPath);
        
        //缓存到本地
        File localFile = new File(LipossGlobals.G_ServerHome + "/" + strPath + "/" + file1.getName());
        if(localFile.exists())
        {
            if(!localFile.delete())
            {
                logger.error("旧缓存文件:{}删除失败！",LipossGlobals.G_ServerHome + "/" + strPath + "/" + file1.getName());
                this.response = "旧缓存文件删除失败！";
                return "response";
            }
        }
        
//        logger.error("-----------------------" + LipossGlobals.G_ServerHome + "/" + strPath + "/" + file1.getName());
        
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try
        {
            fis = new FileInputStream(file1);
            fos = new FileOutputStream(localFile);
            
//            byte[] tmps = new byte[1024];
            
            int ch = fis.read();
            while(ch != -1)
            {
                fos.write((char)ch);
                fos.flush();
                ch = fis.read();
            }
            fos.flush();
        }
        catch (FileNotFoundException e1)
        {
            //本地文件不存在
            logger.error("本地文件不存在！",e1);
            this.response = "本地文件不存在！";
            return "response";
        }
        catch (IOException e)
        {
            //读取文件失败
            logger.error("读取本地文件失败！",e);
            this.response = "读取本地文件失败！";
            return "response";
        }finally
        {
        	 try {
        		 if(fos!=null){
        			 fos.close();
        			 fos=null;
        		 }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	 
             try {
            	 if(fis!=null){
            		 fis.close();
            		 fis=null;
            	 }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        logger.info("WEB文件{}写入成功",localFile.getAbsolutePath());
        
        
        	
    	//转发文件服务器
        try  
        {   
        	String url = urlParameter.substring(0,urlParameter.indexOf("doUploadNX.jsp"));
            String[] urls = url.split(";");
            String urlLeft = urlParameter.substring(urlParameter.indexOf("doUploadNX.jsp"));
            logger.info("urlLeft:  "+urlLeft);
            logger.warn("urlLeft  :"+urlLeft);
            StringBuffer response = new StringBuffer();   
          //防止FTP的账号密码对WEB上传时候造成影响，在通过WEB上传的时候将FTP密码置为空
            String serUser = urlLeft.substring(urlLeft.indexOf("seruser="), urlLeft.indexOf("&serpass"));
            String serPass = urlLeft.substring(urlLeft.indexOf("serpass="), urlLeft.indexOf("&remark"));
            String urlLeftNew = urlLeft; 
            urlLeftNew = com.linkage.commons.util.StringUtil.replace(urlLeftNew, serUser, "seruser=");
            urlLeftNew = com.linkage.commons.util.StringUtil.replace(urlLeftNew, serPass, "serpass=");
            logger.warn("urlLeftNew:"+urlLeftNew);
            logger.info("urlLeftNew:  "+urlLeftNew);
            logger.info("11111");
            HttpClient client = new HttpClient();
            PostMethod method = new PostMethod(urls[0]+(urls.length==1?"":"/")+urlLeftNew);
            logger.info("22222");
            method.setRequestHeader("Content-type", "multipart/form-data");
//            logger.error("urlParameter:{}",urlParameter);
      
            //设置Http Post数据，这里是上传文件   
//            File f= new File(LipossGlobals.G_ServerHome + "/" + strPath + "/" + localFile.getName());
//            FileInputStream fi=new FileInputStream(f);
//            FileInputStream fi=new FileInputStream(localFile);
//            
//            InputStreamRequestEntity fr=new InputStreamRequestEntity(fi);   
//            method.setRequestEntity((RequestEntity)fr);
//            NameValuePair[] data = { new NameValuePair("file1", localFile.getAbsolutePath())};
//            method.setRequestBody(fi);

//            StringPart sp  =   new  StringPart( " TEXT " ,  " testValue " ,  " GBK " );
            
            FilePart fp  =   new  FilePart( " file " ,  localFile.getName() ,  localFile,  null ,  " GBK " );

            method.getParams().setContentCharset( " GBK " );
             MultipartRequestEntity mrp =   new  MultipartRequestEntity( new  Part[]  { fp} , method
                    .getParams());
            method.setRequestEntity(mrp);

            //第一个通过web服务上传
            BufferedReader reader=null;
            try  
            {	
            		
                client.executeMethod(method); //这一步就把文件上传了   
                //下面是读取网站的返回网页，例如上传成功之类的
                int statusCode = method.getStatusCode();
                if (statusCode == HttpStatus.SC_OK)   
                {
                    //读取为 InputStream，在网页内容数据量大时候推荐使用     
                    reader = new BufferedReader(     
                            new InputStreamReader(method.getResponseBodyAsStream(),     
                                    "GBK"));     
                    String line;     
                    while ((line = reader.readLine()) != null)   
                    {
                            response.append(line);
                    }
                    String str = response.toString();
                    int ind =  str.indexOf("idMsg");
                    this.response = str.substring(ind + 7,str.indexOf("</SPAN>", ind));
                }else
                {
                    this.response = urls[0]+"传输文件时异常";
                    logger.error("传输出现异常:{}!","传输文件时异常");
                }
            }   
            catch (IOException e)   
            {     
//                System.out.println("执行HTTP Post请求" + urlParameter + "时，发生异常！");     
//                e.printStackTrace();     
                logger.error("传输文件发生异常:{}", e);
                this.response = "执行HTTP Post请求时异常";
            }   
            finally  
            {   
            	try{
            		if(reader!=null){
                		reader.close();
                	}
            	}catch(IOException e){}
            	
                method.releaseConnection();
            }
            
            //其他采用ftp上传
            String newName = urlParameter.substring(urlParameter.indexOf("=", urlParameter.indexOf("fileRename"))+1, urlParameter.indexOf("&", urlParameter.indexOf("fileRename")));
            String seruser = urlParameter.substring(urlParameter.indexOf("=", urlParameter.indexOf("seruser"))+1, urlParameter.indexOf("&", urlParameter.indexOf("seruser")));
            String serpass = urlParameter.substring(urlParameter.indexOf("=", urlParameter.indexOf("serpass"))+1, urlParameter.indexOf("&", urlParameter.indexOf("serpass")));
            for(int i=1;i<urls.length;i++){
            	FTPClient ftp = new FTPClient();
            	int portIndex = urls[i].lastIndexOf(":");
            	String tmpUrl = urls[i].substring(urls[i].indexOf("://")+3, portIndex);
            	
            	//String prot = urls[i].substring(portIndex+1,urls[i].indexOf("/", portIndex));
        		logger.info(tmpUrl);
            	try
        		{
        			int reply;
        			String path = LipossGlobals.getLipossProperty("ftp.ftpDir");
        			int port = StringUtil.getIntegerValue(LipossGlobals
        					.getLipossProperty("ftp.port"));
        			File ftpLocalPath = new File(path);
        			if (!ftpLocalPath.exists())
        			{
        				ftpLocalPath.mkdir();
        			}
        			ftp.connect(tmpUrl,port);// 连接FTP服务器
        			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
        			//ftp.connect(tmpUrl);
        			ftp.login(seruser, serpass);// 登录
        			ftp.setBufferSize(100000);
        			reply = ftp.getReplyCode();
        			if (!FTPReply.isPositiveCompletion(reply))
        			{
        				ftp.disconnect();
        			}
        			ftp.changeWorkingDirectory(path);
        			FileInputStream in = new FileInputStream(localFile);
        			ftp.storeFile(newName, in);
        			in.close();
        			ftp.logout();
        			this.response = this.response + "\n"+urls[i]+"1#FTP文件上传成功!";
        		}
        		catch (Exception e)
        		{
        			this.response = this.response + "\n"+urls[i]+"-1#FTP文件上传失败!";
        		}
            }
         }
         catch (Exception e)   
         {
             logger.error("传输文件发生异常:{}", e);
             this.response = "传输文件发生异常";
         }finally
         {
           //删除缓存文件
             if(localFile.exists())
             {
                 if(localFile.delete())
                 {
                     logger.info("WEB文件{}删除成功",localFile.getAbsolutePath());
                 }else
                 {
                     logger.error("WEB文件{}删除失败",localFile.getAbsolutePath());
                 }
             }
             if(file1.exists())
             {
                 if(file1.delete())
                 {
                     logger.info("WEB文件{}删除成功",file1.getAbsolutePath());
                 }else
                 {
                     logger.error("WEB文件{}删除失败",file1.getAbsolutePath());
                 }
             }
             localFile = null;
             file1 = null;
         }
        
        return "response";
	}


	
	
	public Map getSession()
	{
		return session;
	}


	
	public void setSession(Map session)
	{
		this.session = session;
	}


	public String getDeviceIds()
	{
		return deviceIds;
	}


	
	public void setDeviceIds(String deviceIds)
	{
		this.deviceIds = deviceIds;
	}


	
	public String getAjax()
	{
		return ajax;
	}


	
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}


	
	public SoftwareBIO getBio()
	{
		return bio;
	}


	
	public void setBio(SoftwareBIO bio)
	{
		this.bio = bio;
	}


	
	public String getSoftStrategyHTML()
	{
		return softStrategyHTML;
	}


	
	public void setSoftStrategyHTML(String softStrategyHTML)
	{
		this.softStrategyHTML = softStrategyHTML;
	}


	
	public String getGoal_devicetype_id()
	{
		return goal_devicetype_id;
	}


	
	public void setGoal_devicetype_id(String goal_devicetype_id)
	{
		this.goal_devicetype_id = goal_devicetype_id;
	}


	
	public String getSoftStrategy_type()
	{
		return softStrategy_type;
	}


	
	public void setSoftStrategy_type(String softStrategy_type)
	{
		this.softStrategy_type = softStrategy_type;
	}


	
	public String getParam()
	{
		return param;
	}


	
	public void setParam(String param)
	{
		this.param = param;
	}


	
	public String getStrRename()
	{
		return strRename;
	}


	
	public void setStrRename(String strRename)
	{
		this.strRename = strRename;
	}


	
	public String getUrlParameter()
	{
		return urlParameter;
	}


	
	public void setUrlParameter(String urlParameter)
	{
		this.urlParameter = urlParameter;
	}


	
	public File getFile1()
	{
		return file1;
	}


	
	public void setFile1(File file1)
	{
		this.file1 = file1;
	}


	
	public String getResponse()
	{
		return response;
	}


	
	public void setResponse(String response)
	{
		this.response = response;
	}


	
	public String getPath()
	{
		return path;
	}


	
	public void setPath(String path)
	{
		this.path = path;
	}


	
	public String getGw_type()
	{
		return gw_type;
	}


	
	public void setGw_type(String gw_type)
	{
		this.gw_type = gw_type;
	}


	
	public GwDeviceQueryBIO getGwDeviceQueryBio()
	{
		return gwDeviceQueryBio;
	}


	
	public void setGwDeviceQueryBio(GwDeviceQueryBIO gwDeviceQueryBio)
	{
		this.gwDeviceQueryBio = gwDeviceQueryBio;
	}


	
	public String getFileName()
	{
		return fileName;
	}


	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}


	
	public String getImportQueryField()
	{
		return importQueryField;
	}


	
	public void setImportQueryField(String importQueryField)
	{
		this.importQueryField = importQueryField;
	}


	
	public String getGwShare_queryType()
	{
		return gwShare_queryType;
	}


	
	public void setGwShare_queryType(String gwShare_queryType)
	{
		this.gwShare_queryType = gwShare_queryType;
	}


	
	public String getGwShare_queryType_this()
	{
		return gwShare_queryType_this;
	}


	
	public void setGwShare_queryType_this(String gwShare_queryType_this)
	{
		this.gwShare_queryType_this = gwShare_queryType_this;
	}


	
	public String getStarttime()
	{
		return starttime;
	}


	
	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}


	
	public String getEndtime()
	{
		return endtime;
	}


	
	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}


	
	public String getTaskId()
	{
		return taskId;
	}


	
	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}


	
	public String getMode()
	{
		return mode;
	}


	
	public void setMode(String mode)
	{
		this.mode = mode;
	}


	
	public List getTaskList()
	{
		return taskList;
	}


	
	public void setTaskList(List taskList)
	{
		this.taskList = taskList;
	}


	
	public String getMaxActive()
	{
		return maxActive;
	}


	
	public void setMaxActive(String maxActive)
	{
		this.maxActive = maxActive;
	}


	
	public String getTaskName()
	{
		return taskName;
	}


	
	public void setTaskName(String taskName)
	{
		this.taskName = taskName;
	}


	
	public static Logger getLogger()
	{
		return logger;
	}
}

