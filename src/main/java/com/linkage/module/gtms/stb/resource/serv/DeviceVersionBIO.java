// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2013/7/31 16:09:04
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   DeviceVersionBIO.java

package com.linkage.module.gtms.stb.resource.serv;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.stb.dao.GwStbVendorModelVersionDAO;
import com.linkage.module.gtms.stb.resource.dao.DeviceVersionDAO;

public class DeviceVersionBIO
{

	 private static Logger logger = LoggerFactory.getLogger(DeviceVersionBIO.class);
	 private DeviceVersionDAO dao;
	 private GwStbVendorModelVersionDAO vmvDaoStb;
	
    public DeviceVersionBIO()
    {
    }

    public int getCountVersion(int curPage_splitPage, int num_splitPage, String queryVendorId, String querySoftwareversion)
    {
        return dao.getCountVersion(curPage_splitPage, num_splitPage, queryVendorId, querySoftwareversion);
    }

    public List getVersion(int curPage_splitPage, int num_splitPage, String queryVendorId, String querySoftwareversion)
    {
        return dao.getVersion(curPage_splitPage, num_splitPage, queryVendorId, querySoftwareversion);
    }

    public List getStsVersion()
    {
        return dao.getStsVersion();
    }

    public List getVendor()
    {
        return vmvDaoStb.getVendor();
    }

    public String getDeviceModel(String vendorId)
    {
        List list = vmvDaoStb.getDeviceModel(vendorId);
        StringBuffer bf = new StringBuffer();
        for(int i = 0; i < list.size(); i++)
        {
            Map map = (Map)list.get(i);
            if(i > 0)
               bf.append("#");
            bf.append(map.get("device_model_id"));
            bf.append("$");
            bf.append(map.get("device_model"));
        }

        return bf.toString();
    }

    public DeviceVersionDAO getDao()
    {
        return dao;
    }

    public void setDao(DeviceVersionDAO dao)
    {
        this.dao = dao;
    }

    public GwStbVendorModelVersionDAO getVmvDaoStb()
    {
        return vmvDaoStb;
    }

    public void setVmvDaoStb(GwStbVendorModelVersionDAO vmvDaoStb)
    {
        this.vmvDaoStb = vmvDaoStb;
    }

    public String addVersion(long accoid, String vendorId, String versionDesc, String versionPath, String version_type, String softwareversion, 
            String deviceModelId,String dcnPath,String specialPath)
    {
        int ier = dao.addVersion(accoid, vendorId, versionDesc, versionPath, version_type, softwareversion, deviceModelId, dcnPath, specialPath);
        if(ier == 1)
            return "1,添加记录成功！";
        else
            return "0,添加记录失败！";
    }

    public String editVersion(String pathId, String vendorId, String versionDesc, String versionPath, String version_type, String softwareversion,
    		String deviceModelId,String dcnPath,String specialPath)
    {
        int ier = dao.editVersion(pathId, vendorId, versionDesc, versionPath, version_type, softwareversion, deviceModelId, dcnPath, specialPath);
        if(ier == 1)
            return "1,修改记录成功！";
        else
            return "0,修改记录失败！";
    }

    public String addStsVersion(long accoid, String vendorId, String versionDesc, String versionPath, String softwareversion, String versionType)
    {
        int ier = dao.addStsVersion(accoid, vendorId, versionDesc, versionPath, softwareversion, versionType);
        if(ier == 1)
            return "1,添加记录成功！";
        if(ier == -1)
            return "0,所选厂商已经存在赛特斯版本，不允许新增";
        else
            return "0,添加记录失败！";
    }

    public String editStsVersion(String pathId, String vendorId, String versionDesc, String versionPath, String softwareversion, String versionType)
    {
        int ier = dao.editStsVersion(pathId, vendorId, versionDesc, versionPath, softwareversion, versionType);
        if(ier == 1)
            return "1,修改记录成功！";
        if(ier == -1)
            return "0,所选厂商已经存在赛特斯版本，请重新选择厂商";
        else
            return "0,修改记录失败！";
    }

    public String isStsExsit(String vendorId, String versionType)
    {
        int ier = dao.isStsExsit(vendorId, versionType);
        if(ier == 0)
        {
            if(versionType.equals("1"))
            {
                logger.debug("所选厂商已经存在赛特斯版本，不允许新增");
                return "0#所选厂商已经存在赛特斯版本，不允许新增/修改";
            } else
            {
                logger.debug("所选厂商已经存在回退赛特斯版本，不允许新增");
                return "0#所选厂商已经存在回退赛特斯版本，不允许新增/修改";
            }
        } else
        {
            return "1";
        }
    }

    public String deleteVersion(String pathId)
    {
//        int is = dao.queryForTask(pathId);
//        if(is > 0)
//            return "2,该路径有升级配置任务，不能删除！";
        int ier = dao.deleteVersion(pathId);
        if(ier == 1)
            return "1,删除记录成功！";
        else
            return "0,删除记录失败！";
    }

    public String uploadFile(File file, String urlParameter, String filename, String path)
    {
        String response = "";
        File localFile = null;
        FileOutputStream fos = null;
        FileInputStream fis = null;
        File localPath = new File((new StringBuilder(String.valueOf(path))).append("/tempFile").toString());
        if(!localPath.exists() && !localPath.mkdir())
        {
            logger.warn("缓存文件目录:{}创建失败！", (new StringBuilder(String.valueOf(path))).append("/tempFile").toString());
            response = "缓存文件目录创建失败！";
            return response;
        }
        localFile = new File((new StringBuilder(String.valueOf(path))).append("/tempFile/").append(filename).toString());
        if(localFile.exists() && !localFile.delete())
        {
            logger.warn("旧缓存文件:{}删除失败！", (new StringBuilder(String.valueOf(path))).append("/tempFile/").append(filename).toString());
            response = "旧缓存文件删除失败！";
            return response;
        }
        try {
			fis = new FileInputStream(file);
			fos = new FileOutputStream(localFile);
		        byte buffer[] = new byte[1024];
		        for(int len = 0; (len = fis.read(buffer)) > 0;)
		        {
		            fos.write(buffer, 0, len);
		            fos.flush();
		        }
			fos.close();
			fis.close();
		} catch (FileNotFoundException e1) {
			// 本地文件不存在
			logger.warn("本地文件不存在！", e1);
			response = "本地文件不存在！";
			return response;
		} catch (IOException e2) {
			// 读取文件失败
			logger.warn("读取本地文件失败！", e2);
			response = "读取本地文件失败！";
			return response;
		} finally{
			 if(fis != null)
		            try
		            {
		                fis.close();
		            }
		            catch(Exception e3)
		            {
		                logger.warn((new StringBuilder("FileInputStream关闭失败:")).append(e3.getMessage()).toString());
		            }
		        if(fos != null)
		            try
		            {
		                fos.close();
		            }
		            catch(Exception e3)
		            {
		                logger.warn((new StringBuilder("FileOutputStream关闭失败:")).append(e3.getMessage()).toString());
		            }

		}
        logger.warn("WEB文件{}写入成功", localFile.getAbsolutePath());
        
     // 转发文件服务器
        BufferedReader reader=null;
     		try
     		{
     			logger.warn("urlParameter:" + urlParameter);
     			StringBuffer sb = new StringBuffer();
     			HttpClient client = new HttpClient();
     			PostMethod method = new PostMethod(urlParameter);
     			method.setRequestHeader("Content-type", "multipart/form-data");
     			FilePart fp = new FilePart(" file ", localFile.getName(), localFile, null,
     					" GBK ");
     			method.getParams().setContentCharset(" GBK ");
     			MultipartRequestEntity mrp = new MultipartRequestEntity(new Part[] { fp },
     					method.getParams());
     			method.setRequestEntity(mrp);
     			try
     			{
     				client.executeMethod(method); // 这一步就把文件上传了
     				// 下面是读取网站的返回网页，例如上传成功之类的
     				int statusCode = method.getStatusCode();
     				logger.warn("statusCode:{}", statusCode);
     				if (statusCode == HttpStatus.SC_OK)
     				{
     					// 读取为 InputStream，在网页内容数据量大时候推荐使用
     					reader = new BufferedReader(new InputStreamReader(
     							method.getResponseBodyAsStream(), "GBK"));
     					String line;
     					while ((line = reader.readLine()) != null)
     					{
     						sb.append(line);
     					}
     					
     					response="文件入库成功！";
     				}
     				else
     				{
     					response = "传输文件时异常";
     					logger.warn("传输出现异常:{}!", response);
     				}
     			}
     			catch (IOException e)
     			{
     				// System.out.println("执行HTTP Post请求" + urlParameter + "时，发生异常！");
     				// e.printStackTrace();
     				logger.warn("传输文件发生异常:{}", e);
     				response = "执行HTTP Post请求时异常";
     				return response;
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
     		}
     		catch (Exception e)
     		{
     			logger.warn("传输文件发生异常:{}", e);
     			response = "传输文件发生异常";
     			return response;
     		}
     		finally
     		{
     			// 删除缓存文件
     			if (localFile.exists())
     			{
     				if (localFile.delete())
     				{
     					logger.warn("WEB文件{}删除成功", localFile.getAbsolutePath());
     				}
     				else
     				{
     					logger.warn("WEB文件{}删除失败", localFile.getAbsolutePath());
     				}
     			}
     			if (file.exists())
     			{
     				if (file.delete())
     				{
     					logger.warn("WEB文件{}删除成功", file.getAbsolutePath());
     				}
     				else
     				{
     					logger.warn("WEB文件{}删除失败", file.getAbsolutePath());
     				}
     			}
     			localFile = null;
     			file = null;
     		}
        return response;
    }

    public String getNewVersionPath()
    {
        List list = dao.getNewVersionPath();
        StringBuffer sb = new StringBuffer();
        sb.append("==请选择版本文件路径==");
        if(!list.isEmpty())
        {
            for(int i = 0; i < list.size(); i++)
                sb.append("#").append(((Map)list.get(i)).get("server_url"));

        } else
        {
            sb.append("#");
        }
        return sb.toString();
    }

}