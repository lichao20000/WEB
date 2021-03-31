
package com.linkage.module.itms.resource.bio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.dao.E8cVersionQueryDAO;

public class E8cVersionQueryBIO
{

	private static Logger logger = LoggerFactory
			.getLogger(E8cVersionQueryBIO.class);
	private E8cVersionQueryDAO dao;

	/**
	 * 获取全部厂商信息
	 * 
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getVendorList()
	{
		return dao.getVendorList();
	}

	/**
	 * 获取设备类型
	 * 
	 * @return
	 */
	public List<Map<String, String>> getGwDevType()
	{
		return dao.getGwDevType();
	}

	/**
	 * 获取设备型号
	 * 
	 * @param vendor_id
	 * @return list
	 */
	@SuppressWarnings("rawtypes")
	public List getDeviceModel(String vendor_id)
	{
		return dao.getDeviceModel(vendor_id);
	}

	/**
	 * 填充设备型号 根据MapList获取<option value=value>textarea<option>
	 * 
	 * @param value值
	 *            ; textarea显示文字
	 * @author Jason(3412)
	 * @date 2009-4-9
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public String getSelectOptiones(List mapList, String value, String textarea)
	{
		return dao.getSelectOptiones(mapList, value, textarea);
	}

	/**
	 * 获取终端规格
	 * 
	 * @return
	 */
	public List<Map<String, Object>> querySpecList()
	{
		return dao.querySpecList();
	}

	/**
	 * e8-c规范版本结果查询
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param vendor_id
	 * @param devicemodel
	 * @param deviceType
	 * @param starttime
	 * @param endtime
	 * @return list
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getE8cVersionList(int curPage_splitPage, int num_splitPage,
			String vendor_id, String device_model, String device_type, String spec_id,
			String starttime, String endtime)
	{
		return dao.getE8cVersionList(curPage_splitPage, num_splitPage, vendor_id,
				device_model, device_type, spec_id, starttime, endtime);
	}

	/**
	 * 查询总记录数
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param vendor_id
	 * @param devicemodel
	 * @param deviceType
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public int getE8cVersionCount(int curPage_splitPage, int num_splitPage,
			String vendor_id, String device_model, String device_type, String spec_id,
			String starttime, String endtime)
	{
		return dao.getE8cVersionCount(curPage_splitPage, num_splitPage, vendor_id,
				device_model, device_type, spec_id, starttime, endtime);
	}

	/**
	 * e8-c规范版本下载和审核结果查询
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param vendor_id
	 * @param devicemodel
	 * @param deviceType
	 * @param starttime
	 * @param endtime
	 * @return list
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getE8cVersionOperList(int curPage_splitPage, int num_splitPage,
			String vendor_id, String device_model, String device_type, String spec_id,
			String starttime, String endtime)
	{
		return dao.getE8cVersionOperList(curPage_splitPage, num_splitPage, vendor_id,
				device_model, device_type, spec_id, starttime, endtime);
	}

	/**
	 * 查询下载和审核总记录数
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param vendor_id
	 * @param devicemodel
	 * @param deviceType
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public int getE8cVersionOperCount(int curPage_splitPage, int num_splitPage,
			String vendor_id, String device_model, String device_type, String spec_id,
			String starttime, String endtime)
	{
		return dao.getE8cVersionOperCount(curPage_splitPage, num_splitPage, vendor_id,
				device_model, device_type, spec_id, starttime, endtime);
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getDetailInfo(long deviceTypeId, long detailSpecId)
	{
		return dao.getDetailInfo(deviceTypeId, detailSpecId);
	}

	/**
	 * 版本审核功能
	 * 
	 * @param deviceTypeId
	 */
	public void updateIsCheck(long deviceTypeId)
	{
		dao.updateIsCheck(deviceTypeId);
	}

	/**
	 * 下载记录入库
	 * 
	 * @return
	 */
	public int saveDownE8cVersionRecord(String devicetype_id, String operType,
			long acc_id, String downTime, String fullFileName)
	{
		return dao.saveDownE8cVersionRecord(devicetype_id, operType, acc_id, downTime,
				fullFileName);
	}

	/**
	 * 获取上传文件的服务器路径
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getResourcePath()
	{
		return dao.getResourcePath();
	}

	/**
	 * 根据设备型号查询软件版本
	 * 
	 * @param device_model
	 * @return
	 */
	public List<Map<String, Object>> getsoftVersion(String device_model)
	{
		return dao.getsoftVersion(device_model);
	}

	/**
	 * 上传文件入库
	 * 
	 * @param dir_url
	 * @param fileName
	 * @param uploadFileName
	 * @param myFile
	 */
	public void saveUploadRecord(String devicetype_id, String operType, long acc_id,
			String uploadTime, String fullFileName)
	{
		dao.saveUploadRecord(devicetype_id, operType, acc_id, uploadTime, fullFileName);
	}

	/**
	 * 文件操作记录查询(1.下载2.上传)
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param vendor_id
	 * @param device_model
	 * @param devicetype_id
	 * @param operType
	 * @param starttime
	 * @param endtime
	 * @return list
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getFileOperRecordList(int curPage_splitPage, int num_splitPage,
			String devicetype_id, String operType, String starttime, String endtime)
	{
		return dao.getFileOperRecordList(curPage_splitPage, num_splitPage, devicetype_id,
				operType, starttime, endtime);
	}

	/**
	 * 统计文件操作记录数
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param vendor_id
	 * @param device_model
	 * @param devicetype_id
	 * @param operType
	 * @param starttime
	 * @param endtime
	 * @return int
	 */
	public int getFileOperRecordCount(int curPage_splitPage, int num_splitPage,
			String devicetype_id, String operType, String starttime, String endtime)
	{
		return dao.getFileOperRecordCount(curPage_splitPage, num_splitPage,
				devicetype_id, operType, starttime, endtime);
	}

	/**
	 * 上传文件到web
	 * 
	 * @param urlParameter
	 * @param response
	 * @param file1
	 * @return String
	 */
	public String uploadLocalFile(String urlParameter, String response, File file1,
			String uploadName, String ip)
	{
		int index = urlParameter.indexOf("path");
		String strPath = urlParameter.substring(index + 5,
				urlParameter.indexOf("&", index));
		// 目录
		File localPath = new File(LipossGlobals.G_ServerHome + "/" + strPath);
		if (!localPath.exists())
		{
			if (!localPath.mkdir())
			{
				response = "-1#缓存文件目录创建失败！";
			}
		}
		File localFile = new File(LipossGlobals.G_ServerHome + "/" + strPath + "/"
				+ file1.getName());
		if (localFile.exists())
		{
			if (!localFile.delete())
			{
				response = "-1#旧缓存文件删除失败！";
			}
		}
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try
		{
			fis = new FileInputStream(file1);
			fos = new FileOutputStream(localFile);
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
			response = "-1#本地文件不存在！";
		}
		catch (IOException e)
		{
			// 读取文件失败
			response = "-1#读取本地文件失败！";
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
		FTPClient ftp = new FTPClient();
		try
		{
			int reply;
			String username = LipossGlobals.getLipossProperty("ftp.username");
			String password = LipossGlobals.getLipossProperty("ftp.password");
			int port = StringUtil.getIntegerValue(LipossGlobals
					.getLipossProperty("ftp.port"));
			String path = LipossGlobals.getLipossProperty("ftp.ftpDir");
			File ftpLocalPath = new File(path);
			if (!ftpLocalPath.exists())
			{
				ftpLocalPath.mkdir();
			}
			ftp.connect(ip, port);// 连接FTP服务器
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			ftp.setBufferSize(100000);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply))
			{
				ftp.disconnect();
			}
			ftp.changeWorkingDirectory(path);
			FileInputStream in = new FileInputStream(file1);
			ftp.storeFile(uploadName, in);
			in.close();
			ftp.logout();
			response = "1#文件上传成功!";
		}
		catch (Exception e)
		{
			response = "-1#文件上传失败!";
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

	/**
	 * 上传版本入库
	 * 
	 * @param urlParameter
	 * @param devicetype_id
	 * @param fileserver
	 */
	public void saveUploadFile(String urlParameter, String devicetype_id,
			String fileserver)
	{
		String[] arrStr = urlParameter.split("&");
		String versionfile_path = fileserver + "/" + arrStr[1].substring(5);
		String versionfile_name = arrStr[2].substring(11);
		dao.saveUploadFile(versionfile_name, devicetype_id, versionfile_path);
	}

	/**
	 * 检查上传文件名称已存在
	 * 
	 * @param uploadFileName
	 * @return String
	 */
	public String checkFillName(String file_name)
	{
		return dao.checkFillName(file_name);
	}

	/**
	 * 检查版本是否已上传
	 * 
	 * @param devicetype_id
	 * @return
	 */
	public String checkSoftVersion(String devicetype_id)
	{
		return dao.checkSoftVersion(devicetype_id);
	}

	public static Logger getLogger()
	{
		return logger;
	}

	public E8cVersionQueryDAO getDao()
	{
		return dao;
	}

	public void setDao(E8cVersionQueryDAO dao)
	{
		this.dao = dao;
	}
}
