
package com.linkage.litms.paramConfig;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ACS.DevRpc;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.acs.soap.object.AnyObject;
import com.linkage.litms.acs.soap.object.ParameterValueStruct;
import com.linkage.litms.acs.soap.service.SetParameterValues;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.VendorUtil;
import com.linkage.litms.system.User;
import com.linkage.module.gwms.Global;

public class ConfigDevice
{

	/** log */
	private static Logger logger = LoggerFactory.getLogger(ConfigDevice.class);

	/**
	 * 初始化
	 */
	public ConfigDevice()
	{
	}

	/**
	 * 通过tr069方式获取设备信息，需要采集的节点以数组方式传入
	 * 
	 * @param device_id
	 * @param user
	 * @param arr
	 * @return 以节点序号为key的HashMap，有多条记录的节点以“，”分割
	 */
	public static Map<String, String> getDevInfo_tr069(String device_id, User user,
			String[] arr)
	{
//		// 获取采集点
//		String gather_id = getGather_id(device_id);
//		String ior = user.getIor("ACS_" + gather_id, "ACS_Poa_" + gather_id);
//		if (ior == null)
//		{
//			return null;
//		}
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
		// 调ACS接口获取设备信息
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		paramTreeObject.setGwType(gw_type);
		Map paraValueMap = null;
		Map<String, String> InfoMap = new HashMap<String, String>();
		// 取出所有采集节点信息
		if (arr != null)
		{
			for (int i = 0; i < arr.length; i++)
			{
				if (!"".equals(arr[i]))
				{
					paraValueMap = paramTreeObject.getParaValueMap(arr[i], device_id);
					if (paraValueMap != null)
					{
						InfoMap.put("" + i, (String) paraValueMap.get(arr[i]));
					}
				}
			}
		}
		return InfoMap;
	}

	/**
	 * 批量设置节点值
	 * 
	 * @param device_id
	 * @param user
	 * @param arr
	 * @param valueArr
	 * @return 1：成功 0：失败
	 */
	public static int[] setDevInfo_tr069(String device_id, User user, String[] arr,
			String[] valueArr, String[] typeArr)
	{
		int[] ret = new int[arr.length];
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
//		AppInitDAO.getACSIOR(gw_type);
		
//		String ior = user.getIor("ACS_" + gather_id, "ACS_Poa_" + gather_id);
//		if (ior == null)
//		{
//			return ret;
//		}
		// 调ACS接口获取设备信息
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		paramTreeObject.setGwType(gw_type);
		// 节点数组和值数组长度必须一致，否则退出
		if (arr != null && valueArr != null && arr.length > 0 && valueArr.length > 0
				&& arr.length == valueArr.length)
		{
			for (int i = 0; i < arr.length; i++)
			{
				if (!"".equals(arr[i]) && !"".equals(valueArr[i]))
				{
					ret[i] = paramTreeObject.setParaValueFlagByType(arr[i], device_id,
							valueArr[i], typeArr[i]);
				}
			}
		}
		return ret;
	}

//	/**
//	 * 返回设备的采集点
//	 * 
//	 * @param device_id
//	 * @return
//	 */
//	private static String getGather_id(String device_id)
//	{
//		String sql = "select * from tab_gw_device where device_id='" + device_id + "'";
//		PrepareSQL psql = new PrepareSQL(sql);
//		psql.getSQL();
//		Cursor cursor = DataSetBean.getCursor(sql);
//		Map fields = cursor.getNext();
//		if (fields != null)
//		{
//			return (String) fields.get("gather_id");
//		}
//		else
//		{
//			return "-1";
//		}
//	}

	/**
	 * 改进版
	 * 
	 * @param device_id
	 * @param user
	 * @param arr
	 * @param indexPara
	 * @return
	 */
	public Map<String, Map<String, String>> getDevInfo_tr069_multi_(String device_id,
			User user, String[] arr, String indexPara)
	{
//		Map<String, Map<String, String>> resultMap = new TreeMap<String, Map<String, String>>(
//				new NumberComparator());
		Map<String, Map<String, String>> resultMap = new TreeMap<String, Map<String, String>>();

//		// 获取采集点
//		String gather_id = getGather_id(device_id);
//		String ior = user.getIor("ACS_" + gather_id, "ACS_Poa_" + gather_id);
//		if (ior == null)
//		{
//			return null;
//		}
		String gw_type = LipossGlobals.getGw_Type(device_id);
		// 调ACS接口获取设备信息
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		paramTreeObject.setGwType(gw_type);
		Map paraValueMap = null;
		String para_value = "";
//		Map<String, String> InfoMap = new HashMap<String, String>();
		// 获取所有索引
		String[] indexArr = paramTreeObject
				.getIndex(indexPara, device_id);
		String tmpStr = "";
		for (int i = 0; i < indexArr.length; i++)
		{
			// test need
			// if (i > 1)
			// break;
			Map<String, String> map = new HashMap<String, String>();
			for (int j = 0; j < arr.length; j++)
			{
				if (!"".equals(arr[j]))
				{
					logger.debug("arr[j]====",arr[j]);
					// 替换“{i}”，并获取所有节点数据
					tmpStr = arr[j].replace("{i}", indexArr[i]);
					logger.debug("11111paraName=====" + tmpStr);
					// 调ACS采集数据
					paraValueMap = paramTreeObject.getParaValueMap(tmpStr, device_id);
					if (paraValueMap != null)
					{
						para_value = (String) paraValueMap.get(tmpStr);
						logger.debug("para_value====",para_value);
						if (para_value != null && para_value.indexOf("XXX") == -1)
						{
							map.put("" + j, para_value);
						}
					}
				}
			}
			
			logger.debug("indexArr[i]====",indexArr[i]);
			resultMap.put(indexArr[i], map);
		}
		return resultMap;
	}

	/**
	 * 通过tr069方式获取设备信息，需要采集的节点以数组方式传入（支持多索引的节点，即带".{i}."的节点）
	 * 
	 * @param device_id
	 * @param user
	 * @param arr
	 * @return 以节点序号为key的HashMap，有多条记录的节点以“，”分割
	 */
	public static Map<String, String> getDevInfo_tr069_multi(String device_id, User user,
			String[] arr, String indexPara)
	{
		//得到设备类型
//		String gw_type = LipossGlobals.getGw_Type(device_id);
		
//		// 获取采集点
//		String gather_id = getGather_id(device_id);
//		String ior = user.getIor("ACS_" + gather_id, "ACS_Poa_" + gather_id);
//		if (ior == null)
//		{
//			return null;
//		}
		String gw_type = LipossGlobals.getGw_Type(device_id);
		// 调ACS接口获取设备信息
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		paramTreeObject.setGwType(gw_type);
		Map paraValueMap = null;
		String para_value = "";
		Map<String, String> InfoMap = new HashMap<String, String>();
		// 获取所有索引
		String[] indexArr = paramTreeObject
				.getIndex(indexPara, device_id);
		// 等待5秒
		sleep(5000);
		String tmpStr = "";
		for (int i = 0; i < indexArr.length; i++)
		{
			for (int j = 0; j < arr.length; j++)
			{
				if (!"".equals(arr[j]))
				{
					// 替换“{i}”，并获取所有节点数据
					tmpStr = arr[j].replace("{i}", indexArr[i]);
					logger.debug("paraName=====" + tmpStr);
					// 调ACS采集数据
					paraValueMap = paramTreeObject.getParaValueMap(tmpStr, device_id);
					if (paraValueMap != null)
					{
						para_value = (String) paraValueMap.get(tmpStr);
						if (para_value != null && para_value.indexOf("XXX") == -1)
						{
							if (InfoMap.get("" + j) != null)
							{
								InfoMap.put("" + j, InfoMap.get("" + j) + "#"
										+ para_value);
							}
							else
							{
								InfoMap.put("" + j, para_value);
							}
						}
						else
						{
							InfoMap.put("" + j, InfoMap.get("" + j) + "#");
						}
					}
					else
					{
						InfoMap.put("" + j, InfoMap.get("" + j) + "#");
					}
					if (j == 4)
					{
						logger.debug("############# i = " + i + "; "
								+ InfoMap.get("" + j));
					}
				}
			}
		}
		return InfoMap;
	}

	/**
	 * 改进版
	 * 
	 * @param device_id
	 * @param user
	 * @param arr
	 * @param valueArr
	 * @param indexPara
	 * @param typeArr
	 * @return
	 */
	public int[] setDevInfo_tr069_multi_(String device_id, User user, String[] arr,
			String[] valueArr, String indexPara, String[] typeArr)
	{
		int[] ret = new int[arr.length];
		logger.debug("^^^^^device_id=" + device_id);
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
//		// 获取采集点
//		String gather_id = getGather_id(device_id);
//		logger.debug("^^^^^gather_id=" + gather_id);
//		String ior = user.getIor("ACS_" + gather_id, "ACS_Poa_" + gather_id);
//		logger.debug("^^^^^ior=" + ior);
//		if (ior == null)
//		{
//			return ret;
//		}
		// 调ACS接口获取设备信息
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		paramTreeObject.setGwType(gw_type);
		// 解析参数值
		int len = 0;
		String[][] valueMap = null;
		if (valueArr != null && valueArr.length > 0)
		{
			String s = valueArr[0];
			if (null == s)
			{
				logger.debug("^^^^^s=null");
				return ret;
			}
			logger.debug("^^^^^s=" + s);
			String[] temp = s.split("\\|");
			if (temp == null)
			{
				logger.debug("^^^^^temp=null");
				return ret;
			}
			len = temp.length;
			logger.debug("^^^^^len=" + len);
			valueMap = new String[valueArr.length][len];
			logger.debug("^^^^^valueMap.length=" + valueMap.length);
			logger.debug("^^^^^valueMap[0].length=" + valueMap[0].length);
			for (int i = 0; i < valueArr.length; i++)
			{
				String[] tmp = valueArr[i].split("\\|");
				logger.debug("^^^^^tmp.length=" + tmp.length);
				for (int j = 0; j < tmp.length; j++)
				{
					valueMap[i][j] = tmp[j];
					logger.debug("^^^^^valueMap[" + i + "][" + j + "]=" + valueMap[i][j]);
				}
			}
		}
		else
		{
			logger.debug("^^^^^valueArr=null");
			return ret;
		}
		// 节点数组和值数组长度必须一致，否则退出
		if (arr != null && valueArr != null && arr.length > 0 && valueArr.length > 0)
		{
			logger.debug("^^^^^here=" + len);
			// 获取所有索引
			String[] indexArr = valueArr[5].split("\\|");
			logger.debug("^^^^^indexArr.length=" + indexArr.length);
			String tmpStr = "";
			int flag = 0;
			for (int i = 0; i < indexArr.length; i++)
			{
				String idx = indexArr[i];
				if (null == idx || "".equals(idx))
					continue;
				// ParameterValueStruct[] parameterValueStructArr = new
				// ParameterValueStruct[arr.length];
				ParameterValueStruct[] parameterValueStructArr = null;
				ParameterValueStruct apply = null;
				if (VendorUtil.IsCiscoByDeviceId(device_id))
				{
					String tmpStr_ = arr[0].replace("{i}", idx);
					logger.debug("^^^^^^^^^ tmpStr_=" + tmpStr_);
					String tmp = tmpStr_.substring(0, tmpStr_.lastIndexOf("."))
							+ ".Apply";
					Map mapTmp = paramTreeObject.getParaValueMap(tmp, device_id);
					if (mapTmp != null)
					{
						logger.debug("==================================存在APPLY节点");
						AnyObject anyObject = new AnyObject();
						anyObject.para_value = "1";
						anyObject.para_type_id = "1";
						apply = new ParameterValueStruct();
						apply.setName(tmp);
						apply.setValue(anyObject);
					}
					else
					{
						tmp = tmpStr_.substring(0, tmpStr_.lastIndexOf("."))
								+ ".X_CT-COM_Apply";
						Map mapTmp_ = paramTreeObject.getParaValueMap(tmp, device_id);
						if (mapTmp_ != null)
						{
							logger
									.debug("==================================存在X_CT-COM_Apply节点");
							AnyObject anyObject = new AnyObject();
							anyObject.para_value = "1";
							anyObject.para_type_id = "1";
							apply = new ParameterValueStruct();
							apply.setName(tmp);
							apply.setValue(anyObject);
						}
					}
				}
				if (apply != null)
				{
					parameterValueStructArr = new ParameterValueStruct[arr.length + 1];
					parameterValueStructArr[arr.length] = apply;
				}
				else
				{
					parameterValueStructArr = new ParameterValueStruct[arr.length];
				}
				for (int j = 0; j < arr.length; j++)
				{
					// if (!"".equals(arr[j]) && !"".equals(valueMap[j][i])) {
					// 替换“{i}”，并获取所有节点数据
					tmpStr = arr[j].replace("{i}", idx);
					logger.debug("paraName=====" + tmpStr);
					logger.debug("paraValue=====" + valueMap[j][i]);
					AnyObject anyObject = new AnyObject();
					anyObject.para_value = valueMap[j][i];
					anyObject.para_type_id = typeArr[j];
					parameterValueStructArr[j] = new ParameterValueStruct();
					parameterValueStructArr[j].setName(tmpStr);
					parameterValueStructArr[j].setValue(anyObject);
				}
				logger.debug("apply paraName====="
						+ parameterValueStructArr[parameterValueStructArr.length - 1]
								.getName());
				logger.debug("apply paraValue====="
						+ parameterValueStructArr[parameterValueStructArr.length - 1]
								.getValue().para_value);
				SetParameterValues setParameterValues = new SetParameterValues();
				setParameterValues.setParameterList(parameterValueStructArr);
				// getParameterValues.
				DevRpc[] devRPCArr = paramTreeObject.getDevRPCArr(device_id,
						setParameterValues);
				List<DevRpcCmdOBJ> devRpcCmdOBJList = paramTreeObject
						.getDevRPCResponse(devRPCArr,Global.RpcCmd_Type);
				// 一个设备返回的命令
				if (devRpcCmdOBJList == null || devRpcCmdOBJList.size() == 0
						|| devRpcCmdOBJList.get(0) == null)
				{
					return ret;
				}
				// 一个设备返回的命令
				if (devRpcCmdOBJList.get(0).getStat() == 1)
				{
					flag = 1;
				}
				else
				{
					flag = 0;
				}
				// 调ACS采集数据
				// flag = paramTreeObject.setParaValueFlagByType(tmpStr, ior,
				// device_id, gather_id, valueMap[j][i] == null ? ""
				// : valueMap[j][i], typeArr[j]);
				// ret[j] = flag;
				for (int j = 0; j < arr.length; j++)
				{
					ret[j] = 1;
					logger.debug("^^^^^ret[" + j + "]=" + ret[j]);
				}
				// }
			}
		}
		return ret;
	}

	/**
	 * 批量设置节点值（支持多索引的节点，即带".{i}."的节点）
	 * 
	 * @param device_id
	 * @param user
	 * @param arr
	 * @param valueArr
	 * @return 结果数组
	 */
	public static int[] setDevInfo_tr069_multi(String device_id, User user, String[] arr,
			String[] valueArr, String indexPara, String[] typeArr)
	{
		int[] ret = new int[arr.length];
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
		
		// 获取采集点
//		String gather_id = getGather_id(device_id);
//		String ior = user.getIor("ACS_" + gather_id, "ACS_Poa_" + gather_id);
//		if (ior == null)
//		{
//			return ret;
//		}
		// 调ACS接口获取设备信息
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		paramTreeObject.setGwType(gw_type);
		// 解析参数值
		int len = 0;
		String[][] valueMap = null;
		if (valueArr != null && valueArr.length > 0)
		{
			String s = valueArr[0];
			if (null == s)
			{
				return ret;
			}
			String[] temp = s.split("#");
			if (temp == null)
			{
				return ret;
			}
			len = temp.length;
			// len = valueArr[0].split("#").length;
			valueMap = new String[valueArr.length][len];
			for (int i = 0; i < valueArr.length; i++)
			{
				String[] tmp = valueArr[i].split("#");
				for (int j = 0; j < tmp.length; j++)
				{
					valueMap[i][j] = tmp[j];
				}
			}
		}
		else
		{
			return ret;
		}
		// 节点数组和值数组长度必须一致，否则退出
		if (arr != null && valueArr != null && arr.length > 0 && valueArr.length > 0
				&& arr.length == valueArr.length)
		{
			// 获取所有索引
			String[] indexArr = paramTreeObject.getIndex(indexPara, device_id);
			// 等待5秒
			sleep(5000);
			String tmpStr = "";
			int flag = 0;
			for (int i = 0; i < indexArr.length; i++)
			{
				for (int j = 0; j < arr.length; j++)
				{
					if (!"".equals(arr[j]) && !"".equals(valueMap[j][i]))
					{
						// 替换“{i}”，并获取所有节点数据
						tmpStr = arr[j].replace("{i}", indexArr[i]);
						logger.debug("paraName=====" + tmpStr);
						// 调ACS采集数据
						flag = paramTreeObject.setParaValueFlagByType(tmpStr, device_id,
								valueMap[j][i], typeArr[j]);
						ret[j] = flag;
					}
				}
			}
		}
		return ret;
	}

	/**
	 * 等待
	 * 
	 * @param time
	 *            时长（豪秒 ）
	 */
	private static void sleep(long time)
	{
		try
		{
			logger
					.debug("================================sleep=============================");
			java.lang.Thread.sleep(time);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 查询设备参数节点
	 * 
	 * @param oid
	 * @param device_id
	 * @return
	 */
	public static String getParaArr(String oid, String device_id)
	{
		String sql = "select a.oid from tab_gw_model_oper_oid a,tab_gw_device b where a.device_model=b.device_model_id and b.device_id='"
				+ device_id + "' and a.oid_type=" + oid;
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if (fields != null)
		{
			return (String) fields.get("oid");
		}
		else
		{
			return "";
		}
	}

	/**
	 * 使用tr069方式获取设备参数（可以包括固定参数和带i的参数）
	 * 
	 * @param device_id
	 * @param user
	 * @param arr
	 * @return
	 */
	public static Map<String, String> getDevInfo_tr069_all(String device_id, User user,
			String[] arr)
	{
		Map<String, String> InfoMap = new HashMap<String, String>();
		for (int i = 0; i < arr.length; i++)
		{
			InfoMap.put(String.valueOf(i), getTr069Info(device_id, arr[i], user));
		}
		return InfoMap;
	}

	/**
	 * 使用tr069方式设置设备参数（可以包括固定参数和带i的参数）
	 * 
	 * @param device_id
	 * @param user
	 * @param arr
	 * @param value
	 * @return 1：成功 0：失败
	 */
	public static int[] setDevInfo_tr069_all(String device_id, User user, String[] arr,
			List<String[]> value)
	{
		int[] ret = new int[arr.length];
		for (int i = 0; i < arr.length; i++)
		{
			if (value.size() > i)
			{
				ret[i] = setTr069Info(device_id, arr[i], user, value.get(i));
			}
			else
			{
				ret[i] = 0;
			}
		}
		return ret;
	}

	/**
	 * 使用tr069方式获取设备参数
	 * 
	 * @param device_id
	 * @param para_name
	 * @param user
	 * @return
	 */
	private static String getTr069Info(String device_id, String para_name, User user)
	{
		if (para_name == null || "".equals(para_name))
		{
			return null;
		}
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
		
//		// 获取采集点
//		String gather_id = getGather_id(device_id);
//		String ior = user.getIor("ACS_" + gather_id, "ACS_Poa_" + gather_id);
		// 调ACS接口获取设备信息
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		paramTreeObject.setGwType(gw_type);
		if (para_name.indexOf(".{i}.") != -1)
		{
			String[] value = paramTreeObject.getParaValueMap_multi(para_name, device_id);
			String tmp = "";
			for (int i = 0; i < value.length; i++)
			{
				if (value[i].indexOf("XXX") == -1)
				{
					tmp += value[i] + "<br>";
				}
			}
			if ("".equals(tmp))
			{
				return null;
			}
			else
			{
				return tmp;
			}
		}
		else
		{
			Map paraValueMap = paramTreeObject.getParaValueMap(para_name, device_id);
			if (paraValueMap == null)
			{
				return null;
			}
			else
			{
				String paraValue = paramTreeObject.getParaVlue(paraValueMap);
				if (paraValue.indexOf("XXX") == -1)
				{
					return paraValue;
				}
				else
				{
					return null;
				}
			}
		}
	}

	/**
	 * 使用tr069方式设置设备参数
	 * 
	 * @param device_id
	 * @param para_name
	 * @param user
	 * @param valueArr
	 * @return 1：成功 0：失败
	 */
	private static int setTr069Info(String device_id, String para_name, User user,
			String[] valueArr)
	{
		if (para_name == null || "".equals(para_name))
		{
			return 0;
		}
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
		
//		// 获取采集点
//		String gather_id = getGather_id(device_id);
//		String ior = user.getIor("ACS_" + gather_id, "ACS_Poa_" + gather_id);
		// 调ACS接口获取设备信息
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		paramTreeObject.setGwType(gw_type);
		if (para_name.indexOf(".{i}.") != -1)
		{
			return paramTreeObject.setParaValueMap_multi(para_name, device_id, valueArr);
		}
		else
		{
			if (valueArr != null && valueArr.length > 0)
			{
				return paramTreeObject
						.setParaValueFlag(para_name, device_id, valueArr[0]);
			}
			else
			{
				return 0;
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
	}

	/**
	 * 根据index排序（非自然排序）
	 * 
	 * @author Bruce
	 */
	private class NumberComparator implements Comparator
	{

		public int compare(Object arg0, Object arg1)
		{
			Integer a = new Integer(arg0.toString());
			Integer b = new Integer(arg1.toString());
			return a.compareTo(b);
			// String a = (String)arg0;
			// a = a.substring(a.lastIndexOf(".") + 1);
			// String b = (String)arg1;
			// b = b.substring(b.lastIndexOf(".") + 1);
			// return new Integer(a).compareTo(new Integer(b));
		}
	}
}
