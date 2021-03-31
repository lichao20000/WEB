
package com.linkage.module.itms.resource.bio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.itms.resource.dao.SoftUpgradRecordQueryDAO;

/**
 * @author yinlei3 (73167)
 * @version 1.0
 * @since 2015年6月3日
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class SoftUpgradRecordQueryBIO
{

	private static Logger logger = LoggerFactory
			.getLogger(SoftUpgradRecordQueryBIO.class);
	/** 软件升级记录查询用dao */
	private SoftUpgradRecordQueryDAO dao;

	/**
	 * 查询设备厂商
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getVendors()
	{
		logger.debug("SoftUpgradRecordQueryBIO=>getVendor()");
		List list = dao.getVendors();
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++)
		{
			Map map = (Map) list.get(i);
			if (i > 0)
			{
				bf.append("#");
			}
			bf.append(map.get("vendor_id"));
			bf.append("$");
			bf.append(map.get("vendor_add"));
			bf.append("(");
			bf.append(map.get("vendor_name"));
			bf.append(")");
		}
		return bf.toString();
	}

	/**
	 * 查询设备型号
	 * 
	 * @param vendorId 厂商Id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getDeviceModels(String vendorId)
	{
		logger.debug("SoftUpgradRecordQueryBIO=>getDeviceModels(vendorId:{})", vendorId);
		List list = dao.getDeviceModels(vendorId);
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++)
		{
			Map map = (Map) list.get(i);
			if (i > 0)
			{
				bf.append("#");
			}
			bf.append(map.get("device_model_id"));
			bf.append("$");
			bf.append(map.get("device_model"));
		}
		return bf.toString();
	}

	/**
	 * 查询设备版本
	 * @param deviceModelId  型号Id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getDevicetypes(String deviceModelId)
	{
		logger.debug("SoftUpgradRecordQueryBIO=>getDevicetypes(deviceModelId:{})", deviceModelId);
		List list = dao.getVersionList(deviceModelId);
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < list.size(); i++)
		{
			Map map = (Map) list.get(i);
			if (i > 0)
			{
				bf.append("#");
			}
			bf.append(map.get("devicetype_id"));
			bf.append("$");
			bf.append(map.get("softwareversion"));
		}
		return bf.toString();
	}

	/**
	 * 软件升级记录查询
	 * 
	 * @param vendor
	 *            终端厂家Id
	 * @param device_model
	 *            终端型号Id
	 * @param starttime
	 *            升级开始起始时间
	 * @param endtime
	 *            升级开始截止时间
	 * @param curPage_splitPage
	 *            当前页
	 * @param num_splitPage
	 *            每页行数
	 * @return 软件升级记录集合
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getSoftUpgradRecordQuery(String vendor, String device_model,
			String starttime, String endtime, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("SoftUpgradRecordQueryBIO==>getSoftUpgradRecordQuery({},{},{},{})",
				new Object[] { vendor, device_model, starttime, endtime, });
		return dao.getSoftUpgradRecordQuery(vendor, device_model, starttime, endtime,
				curPage_splitPage, num_splitPage);
	}

	/**
	 * 软件升级记录页数查询
	 * 
	 * @param vendor
	 *            终端厂家Id
	 * @param device_model
	 *            终端型号Id
	 * @param starttime
	 *            升级开始起始时间
	 * @param endtime
	 *            升级开始截止时间
	 * @param curPage_splitPage
	 *            当前页
	 * @param num_splitPage
	 *            每页行数
	 * @return 页数
	 */
	public int getCountSoftUpgradRecordQuery(String vendor, String device_model,
			String starttime, String endtime, int curPage_splitPage, int num_splitPage)
	{
		logger.debug(
				"SoftUpgradRecordQueryBIO==>getCountSoftUpgradRecordQuery({},{},{},{})",
				new Object[] { vendor, device_model, starttime, endtime, });
		return dao.getCountSoftUpgradRecordQuery(vendor, device_model, starttime,
				endtime, curPage_splitPage, num_splitPage);
	}

	/**
	 * 删除所选记录
	 * 
	 * @param recordId
	 *            软件升级记录Id
	 * @return 删除结果
	 */
	public int deleRecordByRecordId(String recordId)
	{
		logger.debug("SoftUpgradRecordQueryBIO==>deleRecordByRecordId({})",
				new Object[] { recordId });
		return dao.deleRecordByRecordId(recordId);
	}

	/**
	 * 新增软件升级记录
	 * @param vendor_add  终端厂家
	 * @param device_model_add 终端型号
	 * @param currentVersion_add 现有版本
	 * @param targetVersion_add 目标版本
	 * @param upgradeRange_add 升级范围
	 * @param deviceCount_add 终端数量
	 * @param upgradeReason_add 升级原因
	 * @param upgradeMethod_add 升级方式
	 * @param starttime_add 升级开始时间
	 * @param endtime_add 升级结束时间
	 * @param contactWay_add 终端厂家联系方式
	 * @param file_name 附件
	 * @return 新增结果
	 */
	public String addSoftUpgradRecord(String vendor_add, String device_model_add,
			String currentVersion_add, String targetVersion_add, String upgradeRange_add,
			String deviceCount_add, String upgradeReason_add, String upgradeMethod_add,
			String starttime_add, String endtime_add, String contactWay_add,
			String file_name)
	{
		logger.debug(
				"SoftUpgradRecordQueryBIO==>addSoftUpgradRecord({},{},{},{},{},{},{},{},{},{},{},{})",
				new Object[] { vendor_add, device_model_add, currentVersion_add,
						targetVersion_add, upgradeRange_add, deviceCount_add,
						upgradeReason_add, upgradeMethod_add, starttime_add, endtime_add,
						contactWay_add, file_name });
		return dao.addSoftUpgradRecord(vendor_add, device_model_add, currentVersion_add,
				targetVersion_add, upgradeRange_add, deviceCount_add, upgradeReason_add,
				upgradeMethod_add, starttime_add, endtime_add, contactWay_add, file_name);
	}

	/**
	 * 修改所选记录
	 * 
	 * @param recordId
	 *            软件升级记录Id
	 * @param targetVersion_add
	 *            目标版本
	 * @param upgradeRange_add
	 *            升级范围
	 * @param deviceCount_add
	 *            终端数量
	 * @param upgradeReason_add
	 *            升级原因
	 * @param upgradeMethod_add
	 *            升级方式
	 * @param starttime_add
	 *            升级开始时间
	 * @param endtime_add
	 *            升级结束时间
	 * @param contactWay_add
	 *            终端厂家联系方式
	 * @param file_name
	 *            附件
	 * @return 修改结果
	 */
	public String modifyByRecordId(String recordId, String targetVersion_add,
			String upgradeRange_add, String deviceCount_add, String upgradeReason_add,
			String upgradeMethod_add, String starttime_add, String endtime_add,
			String contactWay_add, String file_name)
	{
		logger.debug(
				"SoftUpgradRecordQueryBIO==>modifyByRecordId({},{},{},{},{},{},{},{},{},{})",
				new Object[] { recordId, targetVersion_add, upgradeRange_add,
						deviceCount_add, upgradeReason_add, upgradeMethod_add,
						starttime_add, endtime_add, contactWay_add, file_name });
		return dao.modifyByRecordId(recordId, targetVersion_add, upgradeRange_add,
				deviceCount_add, upgradeReason_add, upgradeMethod_add, starttime_add,
				endtime_add, contactWay_add, file_name);
	}

	/**
	 * 保存文件到WEB工程目录下
	 * 
	 * @param source
	 * @param newFileDir
	 * @param newFileName
	 * @return
	 */
	public String saveFile(File source, String newFileDir, String newFileName)
	{
		logger.debug("saveFile({})", new Object[] { source.getAbsolutePath(), newFileDir,
				newFileName });
		String result = "1";
		File target = new File(newFileDir, newFileName);
		try
		{
			// 文件复制
			FileUtils.copyFile(source, target);
		}
		catch (Exception e)
		{
			logger.error("copy file error:", e);
			result = "0";
		}
		// 1成功,0失败
		return result;
	}

	/**
	 * 根据recordId查询记录
	 * 
	 * @param recordId
	 *            软件升级记录Id
	 * @return 删除结果
	 */
	@SuppressWarnings("rawtypes")
	public Map findRecordByRecordId(String recordId)
	{
		logger.debug("SoftUpgradRecordQueryBIO==>findRecordByRecordId({})",
				new Object[] { recordId });
		return dao.findRecordByRecordId(recordId);
	}

	/***
	 * 下载版本文件
	 * @param filepath
	 * @param response
	 */
	public void download(String filepath, HttpServletResponse response) {
		logger.debug("download({},{})", new Object[]{filepath, response});
		
		InputStream fis=null;
		OutputStream os=null;
		try
		{
			// path是指欲下载的文件的路径
			File file = new File(filepath);
			// 取得文件名
			String filename = file.getName();

			// 以流的形式下载文件。
			fis = new BufferedInputStream(new FileInputStream(filepath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			fis=null;
			
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("GBK")));
			response.addHeader("Content-Length", "" + file.length());
			os = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			os.write(buffer);
			os.flush();
			os.close();
			os=null;
		}
		catch (IOException e)
		{
			logger.error("download file:[{}], error:", filepath, e);
		}finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 删除文件
	 * @param file
	 * @return
	 */
	public String deleteFile(String file_path)
	{
		logger.debug("deleteFile({})", file_path);
		if(StringUtil.IsEmpty(file_path))
		{
			return "0";
		}
		File file = new File(file_path);
		boolean result = true;
		if(file.exists())
		{
			result = file.delete();
		}
		return result?"1":"0"; //1成功,0失败
	}

	public String insertBroadInfo(String title, String content){
		logger.debug(
				"SoftUpgradRecordQueryBIO==>insertBroadInfo({},{})",
				new Object[] { title, content });
		return dao.insertBroadInfo(title, content);
	}

	public void setDao(SoftUpgradRecordQueryDAO dao)
	{
		this.dao = dao;
	}

}
