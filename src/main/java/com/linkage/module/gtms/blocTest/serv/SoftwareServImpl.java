
package com.linkage.module.gtms.blocTest.serv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;

public class SoftwareServImpl implements SoftwareServ
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(SoftwareServImpl.class);

	public String uploadLocalFile(String urlParameter, String response, File file1)
	{
		logger.debug("urlParameter:" + urlParameter);
		int index = urlParameter.indexOf("path");
		String strPath = urlParameter.substring(index + 5, urlParameter.indexOf("&",
				index));
		// 目录
		File localPath = new File(LipossGlobals.G_ServerHome + "/" + strPath);
		if (!localPath.exists())
		{
			if (!localPath.mkdir())
			{
				logger.warn("缓存文件目录:{}创建失败！", LipossGlobals.G_ServerHome + "/" + strPath);
				response = "缓存文件目录创建失败！";
				// return "response";
			}
		}
		// logger.error("-----------------------" + LipossGlobals.G_ServerHome + "/" +
		// strPath);
		// 缓存到本地
		File localFile = new File(LipossGlobals.G_ServerHome + "/" + strPath + "/"
				+ file1.getName());
		logger.warn("LipossGlobals.G_ServerHome:" + LipossGlobals.G_ServerHome);
		if (localFile.exists())
		{
			if (!localFile.delete())
			{
				logger.warn("旧缓存文件:{}删除失败！", LipossGlobals.G_ServerHome + "/" + strPath
						+ "/" + file1.getName());
				response = "旧缓存文件删除失败！";
				// return "response";
			}
		}
		// logger.error("-----------------------" + LipossGlobals.G_ServerHome + "/" +
		// strPath + "/" + file1.getName());
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try
		{
			fis = new FileInputStream(file1);
			fos = new FileOutputStream(localFile);
			// byte[] tmps = new byte[1024];
			int ch = fis.read();
			while (ch != -1)
			{
				fos.write((char) ch);
				fos.flush();
				ch = fis.read();
			}
			fos.flush();
		}
		catch (FileNotFoundException e1)
		{
			// 本地文件不存在
			logger.warn("本地文件不存在！", e1);
			response = "本地文件不存在！";
			// return "response";
		}
		catch (IOException e)
		{
			// 读取文件失败
			logger.warn("读取本地文件失败！", e);
			response = "读取本地文件失败！";
			// return "response";
		}
		finally
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
		
		logger.warn("WEB文件{}写入成功", localFile.getAbsolutePath());
		// 转发文件服务器
		try
		{
			logger.debug("urlParameter:" + urlParameter);
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
			
			BufferedReader reader=null;
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
					
					//String str = sb.toString();
					//int ind = str.indexOf("idMsg");
					response="文件入库成功！";
					//response = str.substring(ind + 7, str.indexOf("</SPAN>", ind));
				}
				// if (statusCode == HttpStatus.SC_OK)
				// {
				// // 读取为 InputStream，在网页内容数据量大时候推荐使用
				// BufferedReader reader = new BufferedReader(new InputStreamReader(
				// method.getResponseBodyAsStream(), "GBK"));
				// String line;
				// while ((line = reader.readLine()) != null)
				// {
				// sb.append(line);
				// }
				// reader.close();
				// String str = sb.toString();
				// //int ind = str.indexOf("idMsg");
				// logger.warn("response:"+response);
				// //response = str.substring(ind + 7, str.indexOf("</SPAN>", ind));
				// }
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
			if (file1.exists())
			{
				if (file1.delete())
				{
					logger.warn("WEB文件{}删除成功", file1.getAbsolutePath());
				}
				else
				{
					logger.warn("WEB文件{}删除失败", file1.getAbsolutePath());
				}
			}
			localFile = null;
			file1 = null;
		}
		return response;
	}
}
