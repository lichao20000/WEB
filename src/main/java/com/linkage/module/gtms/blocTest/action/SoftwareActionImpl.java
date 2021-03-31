
package com.linkage.module.gtms.blocTest.action;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.blocTest.serv.SoftwareServ;

/**
 * 软件升级
 * 
 * @author wuchao
 */
public class SoftwareActionImpl implements SoftwareAction
{

	/** log */
	private static Logger log = LoggerFactory.getLogger(SoftwareActionImpl.class);
	private SoftwareServ serv;
	private String urlParameter;
	private File file1;
	private String response;

	/**
	 * <p>
	 * [文件上传到WEB]
	 * </p>
	 * 
	 * @return
	 */
	public String uploadLocalFile()
	{
		log.warn("uploadLocalFile");
		response=serv.uploadLocalFile(urlParameter, response, file1);
		log.warn("response:"+response);
		return "response";
	}

	public SoftwareServ getServ()
	{
		return serv;
	}

	public void setServ(SoftwareServ serv)
	{
		this.serv = serv;
	}

	public String getUrlParameter()
	{
		return urlParameter;
	}

	public void setUrlParameter(String urlParameter)
	{
		// logger.error("setUrlParameter:{}",urlParameter);
		this.urlParameter = urlParameter;
		//DBOperation.getMaxId(id, tableName);
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
}
