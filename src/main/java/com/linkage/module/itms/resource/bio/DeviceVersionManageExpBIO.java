/**
 * 
 */
package com.linkage.module.itms.resource.bio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.itms.resource.dao.DeviceVersionManageDAO;
import com.linkage.module.itms.resource.dao.DeviceVersionManageExpDAO;

/**
 * 新疆需求：版本库管理
 * @author chenjie
 * @version 1.0
 * @since 2012-12-14
 */
public class DeviceVersionManageExpBIO {
	
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(DeviceVersionManageExpBIO.class);
	
	private DeviceVersionManageExpDAO dao;
	
	private int maxPage_splitPage;
	
	public int getMaxPage_splitPage() {
		return maxPage_splitPage;
	}

	public void setMaxPage_splitPage(int maxPage_splitPage) {
		this.maxPage_splitPage = maxPage_splitPage;
	}
	

	/**
	 * 查询所有设备版本库信息
	 * @param vendorId
	 * @param deviceModelId
	 * @param hardVersion
	 * @param softVersion
	 * @param specVersion
	 * @param startTime
	 * @param endTime
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @return
	 */
	public List<Map> queryDeviceVersion(String vendorId, String deviceModelId, String hardVersion, String softVersion, int is_check, int rela_dev_type,  
			String startTime, String endTime, int access_style_relay_id, int spec_id, int curPage_splitPage, int num_splitPage)
	{
		logger.debug("queryDeviceVersion({})",new Object[]{vendorId, deviceModelId, hardVersion, softVersion, startTime, endTime, curPage_splitPage, num_splitPage });
		List<Map> list = dao.queryDeviceVersion(vendorId, deviceModelId, hardVersion, softVersion, is_check, rela_dev_type, startTime, endTime, access_style_relay_id, spec_id, curPage_splitPage, num_splitPage);
		maxPage_splitPage = dao.countDeviceVersion(vendorId, deviceModelId, hardVersion, softVersion, is_check, rela_dev_type, startTime, endTime, access_style_relay_id, spec_id, curPage_splitPage, num_splitPage);
		return list;
	}

	public List<Map<String,String>> getGwDevType(){
		
		return dao.getGwDevType();
	}
	
	/**
	 * 添加版本库文件
	 * @param vendor
	 * @param device_model
	 * @param hard_version
	 * @param soft_version
	 * @param spec_version
	 * @param file_path
	 * @param addTime
	 * @return
	 */
	public String addDeviceVersion(String vendor, String device_model, String hard_version, String soft_version, String spec_version, String file_path, long addTime) {
		logger.debug("addDeviceVersion({})", new Object[]{vendor, device_model, hard_version, soft_version, spec_version, file_path, addTime});
		
		int result = dao.addDeviceVersion(vendor, device_model, hard_version, soft_version, spec_version, file_path, addTime);
		
		// 添加成功
		if(result > 0)
		{
			return "1";
		}
		// 添加失败
		else
		{
			return "0";
		}
	}
	
	/**
	 * 保存文件到WEB工程目录下
	 * @param source
	 * @param newFileDir
	 * @param newFileName
	 * @return
	 */
	public String saveFile(File source, String newFileDir, String newFileName)
	{
		logger.debug("saveFile({})", new Object[]{source.getAbsolutePath(), newFileDir, newFileName});
		
		String result = "1";
		File target = new File(newFileDir, newFileName);
		
		try {
			// 文件复制
			FileUtils.copyFile(source, target);
		} catch (IOException e) {
			logger.error("copy file error:", e);
			result = "0";
		}  
		// 1成功,0失败
		return result;
	}
	
	/**
	 * 获取修改的对象
	 * @param id
	 * @return
	 */
	public Map queryForModify(String id) {
		logger.debug("queryForModify({})", id);
		return dao.queryForModify(id);
	}

	/**
	 * 获取厂商列表
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getVendorList() {
		logger.debug("getVendorList()");
		
		Map<String,String> vendorMap = VendorModelVersionDAO.getVendorMap();
		List<Map> list = new ArrayList<Map>();
		Set<String> keySet = vendorMap.keySet();
		Iterator<String> iter = keySet.iterator();
		while(iter.hasNext())
		{
			String key = iter.next();
			Map map = new HashMap();
			map.put("vendor_id", key);
			map.put("vendor_name", vendorMap.get(key));
			list.add(map);
		}
		return list;
	}

	/**
	 * 获取设备型号列表
	 * @param vendor
	 * @return
	 */
	public List<Map> getDeviceModelList(String vendor) {
		logger.debug("getDeviceModelList({})", vendor);
		
		/**
		Map<String,String> deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		List<Map> list = new ArrayList<Map>();
		Set<String> keySet = deviceModelMap.keySet();
		Iterator<String> iter = keySet.iterator();
		while(iter.hasNext())
		{
			String key = iter.next();
			Map map = new HashMap();
			map.put("device_model_id", key);
			map.put("device_model", deviceModelMap.get(key));
			list.add(map);
		}
		**/
		return dao.getDeviceModelByVendor(vendor);
	}

	/**
	 * 修改版本库
	 * @param id
	 * @param vendor
	 * @param device_model
	 * @param hard_version
	 * @param soft_version
	 * @param spec_version
	 * @param string
	 * @param addTime
	 * @return
	 */
	public String modifyDeviceVersion(String id, String vendor, String device_model,
			String hard_version, String soft_version, String spec_version, String file_path, long addTime) {
		logger.debug("modifyDeviceVersion({})", new Object[]{id, vendor, device_model, hard_version, soft_version, spec_version, file_path, addTime});

		int result = dao.modifyDeviceVersion(id, vendor, device_model, hard_version, soft_version, spec_version, file_path, addTime);

		// 修改成功
		if(result > 0)
		{
			return "1";
		}
		// 修改失败
		else 
		{
			return "0";
		}
	}

	/**
	 * 删除版本库
	 * @param id
	 * @return
	 */
	public String deleteDeviceVersion(String id) {
		logger.debug("deleteDeviceVersion({})", id);
		
		int result = dao.deleteDeviceVersion(id);
		
		// 删除成功
		if(result > 0)
		{
			return "1";
		}
		// 删除失败
		else
		{
			return "0";
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
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
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
	 * 导出版本库内容
	 * @param vendor
	 * @param device_model
	 * @param hard_version
	 * @param soft_version
	 * @param spec_version
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public List<Map> queryDeviceVersion(String vendor, String device_model, String hard_version, String soft_version, String spec_version, String start_time, String end_time) {
		logger.debug("queryDeviceVersion({})", new Object[]{vendor, device_model, hard_version, soft_version, spec_version, start_time, end_time});
		return dao.queryDeviceVersion(vendor, device_model, hard_version, soft_version, spec_version, start_time, end_time);
	}
	
	public DeviceVersionManageExpDAO getDao() {
		return dao;
	}

	public void setDao(DeviceVersionManageExpDAO dao) {
		this.dao = dao;
	}

	
}
