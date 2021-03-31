
package com.linkage.module.gtms.config.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.config.serv.ParamNodeCfgBIO;

public class ParamNodeCfgACT extends splitPageAction implements ServletRequestAware
{

	// 序列化
	private static final long serialVersionUID = 1L;
	// 日志记录
	private static Logger logger = LoggerFactory.getLogger(ParamNodeCfgACT.class);
	private HttpServletRequest request = null;
	// 设备id
	private String deviceIds;
	// 查询设备信息
	private List<Map<String, String>> deviceList = null;
	private ParamNodeCfgBIO bio;
	private String gwType;
	// 配置类型名称
	private List<Map<String, String>> nodeTypeList = null;
	private String ajax = "";
	// 配置类型ID
	private int conf_type_id;
	// 配置节点列表
	private List<Map<String, String>> paramNodeList = null;
	// 实际上传文件
	private File upload;
	// 节点ID
	private String node_id;
	// 节点值
	private String node_value;
	// 备注
	private String remark;
	// 设备序列号
	private String device_serialnumber;
	// 操作方式
	private String do_type;
	// 属地
	private String cityId;
	// 在线状态
	private String onlineStatus;
	// 厂商
	private String vendorId;
	// 型号
	private String deviceModelId;
	// 软件版本ID
	private String devicetypeId;
	// 绑定方式
	private String bindType;
	// 节点chekcbox
	private String[] select_checkbox;
	// 节点input值
	private List<String> input_param_name;
	// 节点option值
	private List<String> select_param_name;
	// 单台设备序列号
	private String devSn;
	// 单台设备ID
	private String devId;
	// 批量设备序列号
	private String devSerialnumber;
	// 任务ID
	private long taskId;
	// 入库时间
	private long currTime;

	/*
	 * 查询设备详细信息(单台)
	 */
	public String querySingleDeviceList()
	{
		logger.debug("ParamNodeCfgACT=>querySingleDeviceList()");
		deviceList = bio.querySingleDeviceList(deviceIds, gwType);
		return "list";
	}

	/*
	 * 获取配置类型
	 */
	public String getNodeTypeList()
	{
		logger.debug("ParamNodeCfgACT=>getNodeTypeList()");
		nodeTypeList = bio.getNodeTypeList();
		ajax = bio.getSelectOptiones(nodeTypeList, "conf_type_id", "conf_type_name");
		return "ajax";
	}

	/*
	 * 获取配置节点列表
	 */
	public String getConfParam()
	{
		logger.debug("ParamNodeCfgACT=>getConfParam()");
		paramNodeList = bio.getConfParam(conf_type_id);
		return "paramNodeList";
	}

	public String doConfigAll()
	{
		logger.debug("ParamNodeCfgACT=>doConfigAll()");
		String flag = "";
		List<String> nodeIdList = new ArrayList<String>();
		List<String> nodeValueList = new ArrayList<String>();
		List<String> checkedList = new ArrayList<String>();
		if (null != select_checkbox && select_checkbox.length > 0)
		{
			for (int i = 0; i < select_checkbox.length; i++)
			{
				checkedList.add(select_checkbox[i]);
			}
		}
		if (null != input_param_name && input_param_name.size() > 0)
		{
			for (int i = 0; i < input_param_name.size(); i++)
			{
				int index = input_param_name.get(i).indexOf("#");
				if (!"".equals(input_param_name.get(i).substring(index + 1,
						input_param_name.get(i).length())))
				{
					String inputId = input_param_name.get(i).split("#")[0];
					String inputValue = input_param_name.get(i).split("#")[1];
					if (checkedList.contains(inputId))
					{
						nodeIdList.add(inputId);
						nodeValueList.add(inputValue.trim());
					}
				}
			}
		}
		if (null != nodeIdList && nodeIdList.size() > 0)
		{
			for (int i = 0; i < nodeIdList.size(); i++)
			{
				String nodeCheckId = nodeIdList.get(i);
				String inputCheckVal = nodeValueList.get(i);
				String checkVal = bio.getCheckVal(nodeCheckId);
				if (null != checkVal && !"".equals(checkVal))
				{
					if (true == inputCheckVal.matches(checkVal))
					{
						flag = "1";
					}
					else
					{
						flag = "-1";
					}
				}
				else
				{
					flag = "0";
				}
			}
		}
		else
		{
			flag = "1";
		}
		if (null != select_param_name && select_param_name.size() > 0)
		{
			for (int i = 0; i < select_param_name.size(); i++)
			{
				String selectId = select_param_name.get(i).split("#")[0];
				String selectValue = select_param_name.get(i).split("#")[1];
				if (checkedList.contains(selectId))
				{
					nodeIdList.add(selectId);
					nodeValueList.add(selectValue);
				}
			}
		}
		if ("1".equals(flag))
		{
			if ("1".equals(do_type))
			{
				ajax = bio.batchParmNode(taskId, currTime, cityId, onlineStatus,
						vendorId, deviceModelId, devicetypeId, bindType, devSerialnumber,
						nodeIdList, conf_type_id, nodeValueList, remark);
			}
			else if ("2".equals(do_type))
			{
				String filePath = "/batchConfig/upload";
				String targetFileName = "";
				HttpServletRequest request = null;
				// 将文件存放到指定的路径中
				request = ServletActionContext.getRequest();
				// 由于传输的文件名中到后台有中文乱码所以存储到数据库更改文件名
				targetFileName = taskId + ".txt";
				// 插入到表中
				String saveDir = LipossGlobals.getLipossProperty("bathConfig.saveDir");
				filePath = "http://" + request.getLocalAddr() + ":"
						+ request.getServerPort() + saveDir + filePath + "/"
						+ targetFileName;
				ajax = bio.importBatchParmNode(taskId, nodeIdList, conf_type_id,
						nodeValueList, remark, targetFileName, filePath);
			}
			else if ("3".equals(do_type))
			{
				int num = bio.checkDevExist(devId, conf_type_id, nodeIdList);
				if (num > 0)
				{
					ajax = "该设备已定制,还没完成配置!";
				}
				else
				{
					ajax = bio.doSingleConfig(taskId, currTime, nodeIdList, conf_type_id,
							nodeValueList, devSn, devId, do_type);
				}
			}
		}
		else if ("0".equals(flag))
		{
			ajax = "暂时无法完成输入框数字范围验证，请联系管理员!";
		}
		else
		{
			ajax = "输入框输入的数字不在范围之内!";
		}
		return "configSuccess";
	}

	public String uploadFile()
	{
		logger.debug("ParamNodeCfgACT=>uploadFile()");
		String filePath = "/batchConfig/upload";
		String targetDirectory = "";
		String targetFileName = "";
		try
		{
			// 将文件存放到指定的路径中
			request = ServletActionContext.getRequest();
			targetDirectory = ServletActionContext.getServletContext().getRealPath(
					filePath);
			// 目录
			File localPath = new File(targetDirectory);
			if (!localPath.exists())
			{
				if (!localPath.mkdir())
				{
					logger.error("创建目录失败!");
				}
			}
			// 由于传输的文件名中到后台有中文乱码所以存储到数据库更改文件名
			targetFileName = taskId + ".txt";
			File target = new File(targetDirectory, targetFileName);
			FileUtils.copyFile(upload, target);
		}
		catch (IOException e)
		{
			logger.error("批量导入定制，上传文件时出错");
		}
		return "uploadSuccess";
	}

	public void setServletRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public HttpServletRequest getRequest()
	{
		return request;
	}

	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	public String getDeviceIds()
	{
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds)
	{
		this.deviceIds = deviceIds;
	}

	public List<Map<String, String>> getDeviceList()
	{
		return deviceList;
	}

	public void setDeviceList(List<Map<String, String>> deviceList)
	{
		this.deviceList = deviceList;
	}

	public String getGwType()
	{
		return gwType;
	}

	public void setGwType(String gwType)
	{
		this.gwType = gwType;
	}

	public void setBio(ParamNodeCfgBIO bio)
	{
		this.bio = bio;
	}

	public void setNodeTypeList(List<Map<String, String>> nodeTypeList)
	{
		this.nodeTypeList = nodeTypeList;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public int getConf_type_id()
	{
		return conf_type_id;
	}

	public void setConf_type_id(int conf_type_id)
	{
		this.conf_type_id = conf_type_id;
	}

	public List<Map<String, String>> getParamNodeList()
	{
		return paramNodeList;
	}

	public void setParamNodeList(List<Map<String, String>> paramNodeList)
	{
		this.paramNodeList = paramNodeList;
	}

	public File getUpload()
	{
		return upload;
	}

	public void setUpload(File upload)
	{
		this.upload = upload;
	}

	public String getNode_id()
	{
		return node_id;
	}

	public void setNode_id(String node_id)
	{
		this.node_id = node_id;
	}

	public String getNode_value()
	{
		return node_value;
	}

	public void setNode_value(String node_value)
	{
		this.node_value = node_value;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public String getDevice_serialnumber()
	{
		return device_serialnumber;
	}

	public void setDevice_serialnumber(String device_serialnumber)
	{
		this.device_serialnumber = device_serialnumber;
	}

	public String getDo_type()
	{
		return do_type;
	}

	public void setDo_type(String do_type)
	{
		this.do_type = do_type;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public String getOnlineStatus()
	{
		return onlineStatus;
	}

	public void setOnlineStatus(String onlineStatus)
	{
		this.onlineStatus = onlineStatus;
	}

	public String getVendorId()
	{
		return vendorId;
	}

	public void setVendorId(String vendorId)
	{
		this.vendorId = vendorId;
	}

	public String getDeviceModelId()
	{
		return deviceModelId;
	}

	public void setDeviceModelId(String deviceModelId)
	{
		this.deviceModelId = deviceModelId;
	}

	public String getDevicetypeId()
	{
		return devicetypeId;
	}

	public void setDevicetypeId(String devicetypeId)
	{
		this.devicetypeId = devicetypeId;
	}

	public String getBindType()
	{
		return bindType;
	}

	public void setBindType(String bindType)
	{
		this.bindType = bindType;
	}

	public String[] getSelect_checkbox()
	{
		return select_checkbox;
	}

	public void setSelect_checkbox(String[] select_checkbox)
	{
		this.select_checkbox = select_checkbox;
	}

	public List<String> getInput_param_name()
	{
		return input_param_name;
	}

	public void setInput_param_name(List<String> input_param_name)
	{
		this.input_param_name = input_param_name;
	}

	public List<String> getSelect_param_name()
	{
		return select_param_name;
	}

	public void setSelect_param_name(List<String> select_param_name)
	{
		this.select_param_name = select_param_name;
	}

	public String getDevSn()
	{
		return devSn;
	}

	public void setDevSn(String devSn)
	{
		this.devSn = devSn;
	}

	public String getDevId()
	{
		return devId;
	}

	public void setDevId(String devId)
	{
		this.devId = devId;
	}

	public String getDevSerialnumber()
	{
		return devSerialnumber;
	}

	public void setDevSerialnumber(String devSerialnumber)
	{
		this.devSerialnumber = devSerialnumber;
	}

	public long getTaskId()
	{
		return taskId;
	}

	public void setTaskId(long taskId)
	{
		this.taskId = taskId;
	}

	public long getCurrTime()
	{
		return currTime;
	}

	public void setCurrTime(long currTime)
	{
		this.currTime = currTime;
	}
}
