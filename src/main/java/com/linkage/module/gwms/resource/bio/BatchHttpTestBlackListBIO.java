
package com.linkage.module.gwms.resource.bio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.module.gwms.resource.dao.BatchHttpTestBlackListDAO;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author zszhao6 (Ailk No.78987)
 * @version 1.0
 * @since 2018-7-29
 * @category com.linkage.module.gwms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BatchHttpTestBlackListBIO
{

	private static Logger logger = LoggerFactory
			.getLogger(BatchHttpTestBlackListBIO.class);
	private BatchHttpTestBlackListDAO dao;
	// 回传消息
	private String msg = null;
	// 查询条件
	private String importQueryField = "username";

	/**
	 * 查询黑名单表中的设备信息（即设备是否在黑名单中）
	 * 
	 * @param gwShare_queryField
	 * @param gwShare_queryParam
	 * @return
	 */
	public List queryDevcieDetailWithBlackList(String gwShare_queryField,
			String gwShare_queryParam)
	{
		return dao.queryDeviceDetailWithBlackList(gwShare_queryField, gwShare_queryParam);
	}

	/**
	 * 生成任务表(插入sql)
	 * 
	 * @param accoid
	 *            操作员id
	 * @param sql
	 *            查询sql
	 * @param deviceId
	 *            device_id
	 * @param fileName
	 *            文件名
	 * @param param0
	 *            参数
	 * @param task_desc
	 *            文件描述
	 */
	public void creatHttpTaskBlackListTaskSql(long accoid, String sql, String[] deviceId,
			String fileName, String param0, String task_desc)
	{
		Object[] param = new Object[13];
		Long task_id = dao.getMaxTaskID() + 1;
		Long time = new DateTimeUtil().getLongTime();
		String task_name = StringUtil.getStringValue(accoid)
				+ StringUtil.getStringValue(time)
				+ StringUtil.getStringValue((int) (Math.random() * 999));
		int task_status = 0;// 未执行
		param[0] = task_name;
		param[1] = task_id;
		logger.warn("task_id = " + task_id + ",dao.getMaxTaskID()=" + dao.getMaxTaskID());
		param[2] = accoid;
		param[3] = time;
		param[4] = task_status;
		param[5] = sql;
		param[6] = fileName;
		param[12] = task_desc;
		// 入黑名单任务表，多个设备也只入一条
		int res = dao.createHttpTaskSQL(param);
		if (res != 1)
		{
			logger.error("插入批量定制黑名单任务表执行失败,param={}", param);
		}
		// 直接传设备id（非文件非sql方式）入任务表需直接入黑名单明细表，并将任务表的状态置为完成。
		if (null == fileName && null == sql && res == 1)
		{
			Long task_id_dev = task_id;
			Object[] param_dev = new Object[11];
			param_dev[0] = task_id_dev;
			param_dev[4] = 1; // 添加黑名单状态
			param_dev[5] = new DateTimeUtil().getLongTime(); // 添加时间
			for (int i = 0; i < deviceId.length; i++)
			{
				param_dev[1] = deviceId[i];
				Map<String, String> map = dao.getdeviceDetailByid((String) param_dev[1]);
				param_dev[2] = map.get("device_serialnumber");
				param_dev[3] = map.get("pppoe_name");
				param_dev[6] = map.get("vendor_id");
				param_dev[7] = map.get("device_model_id");
				param_dev[8] = map.get("devicetype_id");
				param_dev[9] = map.get("city_id");
				// 入库黑名单表 flag为1代表入库成功，0代表设备已在表中
				int falg = dao.batchBlackList(param_dev);
				// 入库黑名单任务详细表
				dao.batchBlackListTask_dev(param_dev,falg);
			}
			// 更新任务表状态
			dao.updateHttpTask(task_id);
			logger.warn("简单查询方式插入黑名单任务完成，task_id={},设备数={}", task_id, deviceId.length);
		}
		if (null != fileName)
		{
			// 解析文件入黑名单表,获取设备序列号集合
			List<String> device_SNs = upDate2BlackListByFile(fileName);
			Object[] param_dev = new Object[11];
			Long task_id_dev = task_id;
			param_dev[0] = task_id_dev;
			param_dev[9] = new DateTimeUtil().getLongTime(); // 添加时间
			for (String deviceSN : device_SNs)
			{
				Map<String, String> deviceDetail = dao.getdeviceDetailBySN(deviceSN);
				// 如果根据序列号查不到设备信息，非法数据不入库
				if (null == deviceDetail || deviceDetail.isEmpty())
				{
					logger.warn("未查询到设备信息，不添加进黑名单，deviceSN:: " + deviceSN);
					continue;
				}
				param_dev[1] = deviceDetail.get("device_id");
				param_dev[2] = deviceDetail.get("device_serialnumber");
				param_dev[3] = deviceDetail.get("pppoe_name");
				param_dev[4] = deviceDetail.get("vendor_id");
				param_dev[5] = deviceDetail.get("device_model_id");
				param_dev[6] = deviceDetail.get("devicetype_id");
				param_dev[7] = deviceDetail.get("city_id");
				param_dev[8] = param_dev[9];
				// 入黑名单表
				dao.upDataBlackListBySN(param_dev);
				// 入黑名单任务详细表
				dao.upDataBlackListTaskDev(param_dev);
			}
			dao.updateHttpTask(task_id);
			logger.warn("导入文件方式插入黑名单任务完成，task_id={}", task_id);
		}
	}

	public List<String> upDate2BlackListByFile(String fileName_st)
	{
		logger.warn("upDate2BlackListByFile{}", new Object[] { fileName_st });
		if (fileName_st.length() < 4)
		{
			this.msg = "上传的文件名不正确！";
			return null;
		}
		String fileName_ = fileName_st.substring(fileName_st.length() - 3,
				fileName_st.length());
		logger.warn("fileName_;{}", fileName_);
		if (!"txt".equals(fileName_))
		{
			this.msg = "上传的文件格式不正确！";
			return null;
		}
		List<String> dataList = null;
		try
		{
			String realName = fileName_st.substring(fileName_st.length() - 38,
					fileName_st.length());
			dataList = getImportDataByTXT(realName);
			logger.warn("dataList;{}", dataList);
		}
		catch (FileNotFoundException e)
		{
			logger.warn("{}文件没找到！", fileName_st);
			this.msg = "文件没找到！";
			return null;
		}
		catch (IOException e)
		{
			logger.warn("{}文件解析出错！", fileName_st);
			this.msg = "文件解析出错！";
			return null;
		}
		catch (Exception e)
		{
			logger.warn("{}文件解析出错！", fileName_st);
			this.msg = "文件解析出错！";
			return null;
		}
		return dataList;
	}

	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
	public String getFilePath()
	{
		// 获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try
		{
			lipossHome = java.net.URLDecoder.decode(
					a.substring(0, a.lastIndexOf("WEB-INF") - 1), "UTF-8");
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径", lipossHome);
		return lipossHome + "/temp/";
	}

	/**
	 * 解析文件（txt格式）
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws FileNotFoundException
	 *             IOException
	 */
	public List<String> getImportDataByTXT(String fileName) throws FileNotFoundException,
			IOException
	{
		logger.debug("getImportDataByTXT{}", fileName);
		List<String> list = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(getFilePath() + fileName));
		String line = in.readLine();
		this.importQueryField = "username";
		// 从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null)
		{
			if (!"".equals(line.trim()))
			{
				list.add(line.trim());
			}
		}
		in.close();
		in = null;
		// 处理完毕时，则删掉文件
		File f = new File(getFilePath() + fileName);
		f.delete();
		f = null;
		return list;
	}

	/**
	 * 上传文件，拷贝文件到web目录下的temp/file目录下，命名new DateTimeUtil().getYYYYMMDDHHMMSS() +"_"+
	 * fileName
	 * 
	 * @param fileName
	 *            上传后缓存的文件名
	 * @return 拷贝后的文件的资源路径(http://开头)
	 */
	public String saveUpFile(String fileName)
	{
		logger.warn("saveUpFile(fileName={})", new Object[] { fileName });
		String filePath = "";
		if (null != fileName)
		{
			// 如果是导入帐号，将导入帐号的文件放到指定的目录底下
			String targetDirectory = "";
			filePath = "/temp/file";
			String targetFileName = "";
			HttpServletRequest request = null;
			try
			{
				// 将文件存放到指定的路径中
				request = ServletActionContext.getRequest();
				targetDirectory = ServletActionContext.getServletContext().getRealPath(
						filePath);
				// 由于传输的文件名中到后台有中文乱码所以存储到数据库更改文件名
				targetFileName = new DateTimeUtil().getYYYYMMDDHHMMSS() + "_" + fileName;
				File target = new File(getFilePath() + "file/" + targetFileName);
				File src = new File(getFilePath() + fileName);
				if (!target.exists())
				{
					target.createNewFile();
				}
				FileUtils.copyFile(src, target);
			}
			catch (IOException e)
			{
				logger.error("批量导入升级，上传文件时出错");
			}
			filePath = "http://" + request.getLocalAddr() + ":" + request.getLocalPort()
					+ request.getContextPath() + filePath + "/" + targetFileName;
		}
		logger.warn("filePath = " + filePath);
		return filePath;
	}

	/**
	 * 根据设备序列号，批量入库黑名单表，成功后更新任务详细表
	 */
	public void upDataBlackListBySN(List<String> deviceSNs)
	{
		for (String deviceSN : deviceSNs)
		{
			Object[] param = new Object[9];
			Long time = new DateTimeUtil().getLongTime();
			// 添加时间
			param[8] = time;
			Map<String, String> deviceDetail = dao.getdeviceDetailBySN(deviceSN);
			param[1] = deviceDetail.get("device_id");
			param[2] = deviceDetail.get("device_serialnumber");
			param[3] = deviceDetail.get("pppoe_name");
			param[4] = deviceDetail.get("vendor_id");
			param[5] = deviceDetail.get("device_model_id");
			param[6] = deviceDetail.get("devicetype_id");
			param[7] = deviceDetail.get("city_id");
			try
			{
				dao.upDataBlackListBySN(param);
				logger.warn("加入黑名单成功::序列号" + param[2]);
			}
			catch (DataAccessException e)
			{
				// 捕获异常，数据已经在黑名单中
				logger.warn("设备已经列入黑名单，序列号::" + param[2]);
			}
		}
	}

	/**
	 * 通过高级设置添加进黑名单
	 * 
	 * @param accoid操作员id
	 * @param param
	 *            参数 （公共页面传来，仅需要属地、厂商、型号、版本即可）
	 * @param task_desc
	 *            任务描述
	 */
	public void upDataBlackListByAdvanCedSetting(long accoid, Object[] param,
			String task_desc)
	{
		// 添加黑名单三步走，一：添加黑名单任务表
		Long task_id = dao.getMaxTaskID() + 1;
		Object[] param_task = new Object[13];
		Long time = new DateTimeUtil().getLongTime();
		String task_name = StringUtil.getStringValue(accoid)
				+ StringUtil.getStringValue(time)
				+ StringUtil.getStringValue((int) (Math.random() * 999));
		int task_status = 0;// 未执行
		param_task[0] = task_name;
		param_task[1] = task_id;
		param_task[2] = accoid;
		param_task[3] = time;
		param_task[4] = task_status;
		param_task[5] = param[10]; // 高级设置，通过sql来从后台获取设备信息
		param_task[6] = null; // 高级设置无filePath
		param_task[12] = task_desc;
		dao.createHttpTaskSQL(param_task);
		// 第二步，入库黑名单表和黑名单任务详细表
		Object[] param_bl = new Object[13];
		param_bl[0] = (String) param[2]; // 属地
		param_bl[1] = (String) param[5]; // 厂商
		param_bl[2] = (String) param[6]; // 型号
		param_bl[3] = (String) param[7]; // 版本
		dao.upDataBlackListByAdSet(param_bl, task_name, task_desc, task_id);
		// 修改黑名单任务表，修改任务状态
		dao.updateHttpTask(task_id);
		logger.warn("高级设置黑名单任务完成，task_id={}", task_id);
	}

	/**
	 * 
	 */
	public void removeBlackListByDeviceId(List<String> deviceIds)
	{
		List<String> deviceids = new ArrayList<String>();
		for (String deviceId : deviceIds)
		{
			// 传入的deviceid都已'/'结尾，需要去掉
			String deviceid = deviceId.substring(0, deviceId.length() - 1);
			deviceids.add(deviceid);
		}
		// 转成数组传入
		Object[] param = (String[]) deviceids.toArray(new String[deviceids.size()]);
		dao.removeBLbyDeviceIds(param);
	}

	/**
	 * @return the dao
	 */
	public BatchHttpTestBlackListDAO getDao()
	{
		return dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(BatchHttpTestBlackListDAO dao)
	{
		this.dao = dao;
	}
}
