package bio.bbms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.linkage.commons.db.DBUtil;
import org.omg.CORBA.IntHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.corba.interfacecontrol.SnmpGatherInterface;
import com.linkage.litms.paramConfig.ConfigDevice;
import com.linkage.litms.paramConfig.ParamTreeObject;
import com.linkage.litms.system.User;
import com.linkage.module.gwms.Global;

/**
 * @author wangzhimeng(工号) tel：12345678
 * @version 1.0
 * @since 2008-6-5
 * @category bio.bbms 版权：南京联创科技 网管科技部
 * 
 */
public class GetSnmpInfoBIO
{
	private static final Logger LOG = LoggerFactory.getLogger(GetSnmpInfoBIO.class);
	// db
	private JdbcTemplate jt;
	// tr069使用，存放参数名
	private static Map<String, String> paraMap = new HashMap<String, String>();
	// tr069
	private ParamTreeObject paramTreeObject;
	/**
	 * 初始化
	 */
	public GetSnmpInfoBIO()
	{
	}
	public String getSnmpInfoBIO(String device_id, String oid_type, User user,
			int gatherType)
	{
		// 使用tr069方式采集
		if (1 == gatherType)
			{
				return getTr069Info(device_id, oid_type, user);
			}
		// 如果是思科的则用walk方法
		String isCISCO = "," + LipossGlobals.getLipossProperty("isCISCO.device_model_id") + ",";
		LOG.debug("getDeviceModel(device_id)=======" + getDeviceModel(device_id)
				+ "=======");
		if (isCISCO.indexOf("," + getDeviceModel(device_id) + ",") != -1)
			{
				return walkSnmpInfoBIO(device_id, oid_type, user, gatherType);
			}
		LOG.debug("====================get性能===================" + device_id
				+ "////" + oid_type);
		String sql = "select oid ,oid_para_type,unit from tab_gw_model_oper_oid a,tab_gw_device b where a.device_model=b.device_model_id and b.device_id='"
				+ device_id + "' and a.oid_type=" + oid_type;
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> res = jt.queryForList(psql.getSQL());
		if (res.size() == 0)
			{
				return "暂不支持该属性采集";
			}
		else
			{
				Map info = res.get(0);
				Object oid = info.get("oid");
				if ((oid == null) || (oid.toString().trim().equals("")))
					{
						return "没有配置采集oid";
					}
				Performance.Data da = new Performance.Data();
				try
					{
						da = SnmpGatherInterface.GetInstance().getDataIMDFull(device_id,
								oid.toString());
						LOG.debug("*****************" + da.dataDou + "===="
								+ da.dataStr);
					}
				catch (Exception e)
					{
						e.printStackTrace();
					}
				if ((da == null) || (da.dataStr == null))
					{
						return "无法采集到数据";
					}
				LOG.debug("*********GETINDex" + da.index);
				return da.dataStr
						+ (info.get("unit") == null ? "" : info.get("unit").toString());
			}
	}
	/**
	 * 通过walk获取性能
	 * 
	 * @param device_id
	 *            设备id
	 * @param oid_type
	 *            oid的type类型
	 * @param user
	 *            用户信息给tr069用的
	 * @param gatherType
	 *            采集方式
	 * @return
	 */
	public String walkSnmpInfoBIO(String device_id, String oid_type, User user,
			int gatherType)
	{
		LOG.debug("====================walk性能===================" + device_id
				+ "////" + oid_type);
		// 使用tr069方式采集
		if (1 == gatherType)
			{
				return getTr069Info(device_id, oid_type, user);
			}
		String sql = "select oid ,oid_para_type,unit from tab_gw_model_oper_oid a,tab_gw_device b where a.device_model=b.device_model_id and b.device_id='"
				+ device_id + "' and a.oid_type=" + oid_type + " order by sequence";
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> res = jt.queryForList(psql.getSQL());
		String restring = "";
		int len = res.size();
		if (len == 0)
			{
				return "暂不支持该属性采集";
			}
		else
			{
				if (len > 1)
					{
						String[] oid = new String[len];
						for (int i = 0; i < len; i++)
							{
								oid[i] = res.get(i).get("oid").toString();
								IntHolder ih = new IntHolder();
								Performance.Data[] da = SnmpGatherInterface.GetInstance()
										.GetDataListPortFull(ih, device_id, "", oid[i]);
								int j = 0;
								for (Performance.Data d : da)
									{
										j++;
										restring += ((ih.value == 1) ? (d.dataDou)
												: (d.dataStr));
										if (j < da.length && da.length != 1)
											{
												restring += "<br>";
											}
										LOG.debug("****************多index**********"
														+ d.index);
									}
								if (i < oid.length && oid.length != 1)
									{
										restring += "<br>";
									}
							}
					}
				else
					{
						Map info = res.get(0);
						Object oid = info.get("oid");
						if ((oid == null) || (oid.toString().trim().equals("")))
							{
								return "没有配置采集oid";
							}
						IntHolder ih = new IntHolder();
						Performance.Data[] da = SnmpGatherInterface.GetInstance()
								.GetDataListPortFull(ih, device_id, "", oid.toString());
						if ((da == null) || (da.length == 0))
							{
								return "无法采集到数据";
							}
						String unit = (info.get("unit") == null ? "" : info.get("unit")
								.toString());
						int j = 0;
						for (Performance.Data d : da)
							{
								j++;
								restring += ((ih.value == 1) ? (d.dataDou + unit)
										: (d.dataStr + unit));
								if (j < da.length && da.length != 1)
									{
										restring += "<br>";
									}
								LOG.debug("+++++++++++++++index+++++++++++++"
										+ d.index);
							}
					}
				return restring;
			}
	}
	public void setDao(DataSource dao)
	{
		jt = new JdbcTemplate(dao);
	}
	/**
	 * 获取设备vlan端口信息
	 * 
	 * @param device_id
	 * @return
	 */
	public String getVlanPort(String device_id, User user, int gatherType)
	{
		// 使用tr069方式采集
		if (1 == gatherType)
			{
				return getTr069Info(device_id, "354", user);
			}
		// 思科的设备特殊处理则用walk方法
		String isCISCO = "," + LipossGlobals.getLipossProperty("isCISCO.device_model_id") + ",";
		LOG.debug("getDeviceModel(device_id)=======" + getDeviceModel(device_id)
				+ "=======");
		if (isCISCO.indexOf("," + getDeviceModel(device_id) + ",") != -1)
			{
				return walkSnmpInfoBIO(device_id, "354", user, gatherType);
			}
		// 使用SNMP方式采集
		// 返回的字符串
		String portStr = "";
		IntHolder ih = new IntHolder();
		String oid = "";
		// 获取接口索引与端口索引对应关系
		Map<String, String> portInfo = new HashMap<String, String>();
		oid = getOID(device_id, "355");
		if ("".equals(oid))
			{
				return "暂不支持该属性采集！";
			}
		// 采集接口索引与端口索引对应关系信息，将其放入map中
		Performance.Data[] da1 = SnmpGatherInterface.GetInstance().GetDataListIMDFull(ih,
				device_id, "", oid);
		for (int i = 0; i < da1.length; i++)
			{
				portInfo.put(da1[i].dataStr, da1[i].index);
				LOG.debug("portIndex===" + da1[i].dataStr + "====="
						+ da1[i].index);
			}
		// 获取接口描述
		Map<String, String> portDesc = new HashMap<String, String>();
		oid = getOID(device_id, "356");
		if ("".equals(oid))
			{
				return "暂不支持该属性采集！";
			}
		// 采集接口描述信息，将其放入map中
		Performance.Data[] da2 = SnmpGatherInterface.GetInstance().GetDataListIMDFull(ih,
				device_id, "", oid);
		for (int j = 0; j < da2.length; j++)
			{
				portDesc.put(da2[j].index, da2[j].dataStr);
				LOG.debug("portInfo===" + da2[j].index + "======="
						+ da2[j].dataStr);
			}
		// 获取vlan对应端口索引
		String[] oidArr = getOIDArr(device_id, "354");
		if (oidArr == null || oidArr.length == 0)
			{
				return "暂不支持该属性采集！";
			}
		else
			{
				for (int k = 0; k < oidArr.length; k++)
					{
						Performance.Data[] da = SnmpGatherInterface.GetInstance()
								.GetDataListIMDFull(ih, device_id, "", oidArr[k]);
						if (da != null && da.length > 0 && da[0].dataStr != null)
							{
								String str = da[0].dataStr;
								long num = 0;
								if (str.length() > 2)
									{
										num = Long.parseLong(str.trim().substring(0, 2),
												16);
									}
								LOG.debug("num================" + num);
								int i = 1;
								while (num > 0)
									{
										if (num % 2 == 1)
											{
												if ("".equals(portStr))
													{
														portStr += (String) portDesc
																.get((String) portInfo
																		.get(String
																				.valueOf(i)));
													}
												else
													{
														portStr += ","
																+ (String) portDesc
																		.get((String) portInfo
																				.get(String
																						.valueOf(i)));
													}
											}
										num = num / 2;
										i++;
									}
							}
						portStr += "<br>";
					}
			}
		return portStr;
	}
	/**
	 * 查询oid信息
	 * 
	 * @param device_id
	 * @param oid_type
	 * @return
	 */
	private String getOID(String device_id, String oid_type)
	{
		String sql = "select a.oid from tab_gw_model_oper_oid a,tab_gw_device b where a.device_model=b.device_model_id and b.device_id='"
				+ device_id + "' and a.oid_type=" + oid_type;
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> res = jt.queryForList(psql.getSQL());
		if (res.size() == 0)
			{
				return "";
			}
		else
			{
				Map map = res.get(0);
				return (String) map.get("oid");
			}
	}
	private String[] getOIDArr(String device_id, String oid_type)
	{
		String sql = "select a.oid from tab_gw_model_oper_oid a,tab_gw_device b where a.device_model=b.device_model_id and b.device_id='"
				+ device_id + "' and a.oid_type=" + oid_type + " order by sequence";
		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> res = jt.queryForList(psql.getSQL());
		String[] oidArr = new String[res.size()];
		for (int i = 0; i < res.size(); i++)
			{
				oidArr[i] = res.get(i).get("oid").toString();
			}
		return oidArr;
	}
	/**
	 * 使用tr069方式获取设备参数（固定节点名称）
	 * 
	 * @param device_id
	 * @param ior
	 * @param gather_id
	 * @param oid_type
	 * @return
	 */
	private String getTr069Info(String device_id, String oid_type, User user)
	{
		// 获取参数名
		String para_name = getOID(device_id, oid_type);
		if (para_name == null || "".equals(para_name))
			{
				return "暂不支持该属性采集";
			}
		// 获取采集点
		String gather_id = getGather_id(device_id);
		String ior = user.getIor("ACS_" + gather_id, "ACS_Poa_" + gather_id);
		// 调ACS接口获取设备信息
		ParamTreeObject paramTreeObject = new ParamTreeObject();
		if (para_name.indexOf(".{i}.") != -1)
			{
				String[] value = paramTreeObject.getParaValueMap_multi(para_name,
						device_id);
				// 等待5秒
				try
					{
						java.lang.Thread.sleep(5000);
					}
				catch (Exception e)
					{
						e.printStackTrace();
					}
				String tmp = "";
				for (int i = 0; i < value.length; i++)
					{
						if (value[i].indexOf("XXX") == -1)
							{
								tmp += value[i];
							}
						if ((i < value.length - 1) && (value.length != 1))
							{
								tmp += "<br>";
							}
					}
				return tmp;
			}
		else
			{
				Map paraValueMap = paramTreeObject.getParaValueMap(para_name,
						device_id);
				// 等待5秒
				try
					{
						java.lang.Thread.sleep(5000);
					}
				catch (Exception e)
					{
						e.printStackTrace();
					}
				if (paraValueMap == null)
					{
						return "无法采集到数据";
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
								return "无法采集到数据";
							}
					}
			}
	}
	/**
	 * 返回设备的采集点
	 * 
	 * @param device_id
	 * @return
	 */
	private String getGather_id(String device_id)
	{
		String sql = "select * from tab_gw_device where device_id='" + device_id + "'";

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select gather_id from tab_gw_device where device_id='" + device_id + "'";
		}

		PrepareSQL psql = new PrepareSQL(sql);
		List<Map> list = jt.queryForList(psql.getSQL());
		if (list.size() == 0)
			{
				return "";
			}
		else
			{
				Map map = list.get(0);
				return (String) map.get("gather_id");
			}
	}
	/**
	 * 通过tr069方式采集数据，其中oid_type可以是由“#”分割的多个oid
	 * 
	 * @param device_id
	 * @param oid_type
	 * @param user
	 * @return 返回数据，不同节点数据以“#”分割，相同节点的多个数据用“<br>
	 *         ”分割
	 */
	public String getInfoByTr069(String device_id, String oid_type, User user)
	{
		String retStr = "";
		String[] oidArr = oid_type.split("#");
		// 取得tr069节点
		String[] paraArr = new String[oidArr.length];
		String tmp = "";
		String tmp_multi = "";
		for (int i = 0; i < oidArr.length; i++)
			{
				paraArr[i] = getOID(device_id, oidArr[i]);
				if (paraArr[i].indexOf("{i}") == -1)
					{
						tmp += paraArr[i] + "#";
					}
				else
					{
						tmp_multi += paraArr[i] + "#";
					}
			}
		// 分开带i的参数节点
		String[] para_name = tmp.split("#");
		String[] para_name_multi = tmp_multi.split("#");
		Map<String, String> paraValue = new HashMap<String, String>();
		Map<String, String> paraValue_multi = new HashMap<String, String>();
		String[] value_total = new String[paraArr.length];
		// 获取两种参数值
		int index = -1;
		if (para_name != null && para_name.length > 0)
			{
				paraValue = ConfigDevice.getDevInfo_tr069(device_id, user, para_name);
				for (int k = 0; k < para_name.length; k++)
					{
						index = getArrIndex(para_name[k], paraArr);
						if (index != -1)
							{
								value_total[index] = paraValue.get("" + k);
							}
					}
			}
		if (para_name_multi != null && para_name_multi.length > 0)
			{
				String indexPara = para_name_multi[0].substring(0, para_name_multi[0]
						.indexOf("{i}"));
				paraValue_multi = ConfigDevice.getDevInfo_tr069_multi(device_id, user,
						para_name_multi, indexPara);
				for (int k = 0; k < para_name_multi.length; k++)
					{
						index = getArrIndex(para_name_multi[k], paraArr);
						if (index != -1)
							{
								value_total[index] = paraValue_multi.get("" + k)
										.replaceAll("#", "<br>");
							}
					}
			}
		// 处理结果集
		for (int m = 0; m < value_total.length; m++)
			{
				LOG.debug("====" + value_total[m]);
				retStr += value_total[m] + "#";
			}
		return retStr;
	}
	/**
	 * 获取数组的序号
	 * 
	 * @param str
	 * @param arr
	 * @return
	 */
	private int getArrIndex(String str, String[] arr)
	{
		int index = -1;
		if (arr != null && arr.length > 0 && str != null)
			{
				for (int i = 0; i < arr.length; i++)
					{
						if (str.equals(arr[i]))
							{
								index = i;
							}
					}
			}
		return index;
	}
	/**
	 * 
	 * @param device_id
	 * @return
	 */
	private String getDeviceModel(String device_id)
	{
		String sql = "select device_model_id from tab_gw_device where device_id='"
				+ device_id + "'";
		PrepareSQL psql = new PrepareSQL(sql);
		List list = jt.queryForList(psql.getSQL());
		if (list != null && list.size() > 0)
			{
				Map map = (Map) list.get(0);
				return map.get("device_model_id").toString();
			}
		else
			{
				return "";
			}
	}
	/**
	 * 采集设备的MAC地址
	 * 
	 * @param device_id
	 * @param user
	 * @param gatherType
	 * @return
	 */
	public String getMacInfo(String device_id)
	{
		// 思科的设备特殊处理
		String isCISCO = "," + LipossGlobals.getLipossProperty("isCISCO.device_model_id") + ",";
		LOG.debug("getDeviceModel(device_id)=======" + getDeviceModel(device_id)
				+ "=======");
		if (isCISCO.indexOf("," + getDeviceModel(device_id) + ",") != -1)
			{
				return getMacInfo_cisco(device_id);
			}
		// 返回的字符串
		String macStr = "";
		IntHolder ih = new IntHolder();
		String oid = "";
		// 获取MAC地址配置
		String macOid = getOID(device_id, "390");
		if ("".equals(macOid))
			{
				return "暂不支持该属性采集！";
			}
		// 获取接口索引与端口索引对应关系
		Map<String, String> portInfo = new HashMap<String, String>();
		oid = getOID(device_id, "355");
		if ("".equals(oid))
			{
				return "暂不支持该属性采集！";
			}
		// 采集接口索引与端口索引对应关系信息，将其放入map中
		Performance.Data[] da1 = SnmpGatherInterface.GetInstance().GetDataListIMDFull(ih,
				device_id, "", oid);
		for (int i = 0; i < da1.length; i++)
			{
				portInfo.put(da1[i].dataStr, da1[i].index);
				LOG.debug("portIndex===" + da1[i].dataStr + "====="
						+ da1[i].index);
			}
		// 获取vlan对应端口索引
		String[] oidArr = getOIDArr(device_id, "354");
		String tmpIndex = "";
		String tmp = "";
		if (oidArr == null || oidArr.length == 0)
			{
				return "暂不支持该属性采集！";
			}
		else
			{
				for (int k = 0; k < oidArr.length; k++)
					{
						Performance.Data[] da = SnmpGatherInterface.GetInstance()
								.GetDataListIMDFull(ih, device_id, "", oidArr[k]);
						if (da != null && da.length > 0 && da[0].dataStr != null)
							{
								String str = da[0].dataStr;
								long num = 0;
								if (str.length() > 2)
									{
										num = Long.parseLong(str.trim().substring(0, 2),
												16);
									}
								LOG.debug("num================" + num);
								int i = 1;
								while (num > 0)
									{
										if (num % 2 == 1)
											{
												tmpIndex = portInfo
														.get(String.valueOf(i));
												if (tmpIndex != null
														&& !"".equals(tmpIndex))
													{
														Performance.Data tmpda = new Performance.Data();
														try
															{
																tmpda = SnmpGatherInterface
																		.GetInstance()
																		.getDataIMDFull(
																				device_id,
																				macOid
																						+ "."
																						+ tmpIndex);
																LOG.debug("*****************"
																				+ tmpda.dataStr);
															}
														catch (Exception e)
															{
																e.printStackTrace();
															}
														if ((tmpda != null)
																&& (tmpda.dataStr != null))
															{
																macStr = tmpda.dataStr
																		.trim()
																		.substring(0, 17);
															}
													}
											}
										num = num / 2;
										i++;
									}
							}
						macStr += "<br>";
					}
			}
		return macStr;
	}
	/**
	 * 采集MAC地址（思科设备专用）
	 * 
	 * @param device_id
	 * @return
	 */
	public String getMacInfo_cisco(String device_id)
	{
		// 返回的字符串
		String macStr = "";
		IntHolder ih = new IntHolder();
		String oid = "";
		// 获取MAC地址配置
		String macOid = getOID(device_id, "392");
		if ("".equals(macOid))
			{
				return "暂不支持该属性采集！";
			}
		// 获取接口索引与端口索引对应关系
		Map<String, String> portInfo = new HashMap<String, String>();
		oid = getOID(device_id, "391");
		if ("".equals(oid))
			{
				return "暂不支持该属性采集！";
			}
		// 采集接口索引与端口索引对应关系信息，将其放入map中
		Performance.Data[] da1 = SnmpGatherInterface.GetInstance().GetDataListIMDFull(ih,
				device_id, "", oid);
		for (int i = 0; i < da1.length; i++)
			{
				portInfo.put(da1[i].dataStr, da1[i].index);
				LOG.debug("portIndex===" + da1[i].dataStr + "====="
						+ da1[i].index);
			}
		// 获取vlan对应端口索引
		String[] oidArr = getOIDArr(device_id, "354");
		String tmpIndex = "";
		String tmp = "";
		if (oidArr == null || oidArr.length == 0)
			{
				return "暂不支持该属性采集！";
			}
		else
			{
				for (int k = 0; k < oidArr.length; k++)
					{
						Performance.Data[] da = SnmpGatherInterface.GetInstance()
								.GetDataListIMDFull(ih, device_id, "", oidArr[k]);
						for (int j = 0; j < da.length; j++)
							{
								tmp = da[j].dataStr.replaceAll("eth", "GE");
								LOG.debug("tmpStr=======" + tmp);
								String[] strArr = tmp.split("\\." + (j+1));
								for (int m = 0; m < strArr.length; m++)
									{
										LOG.debug("tmpIndexStr=======" + strArr[m]);
										tmpIndex = portInfo.get(strArr[m].trim());
										LOG.debug("tmpIndex=======" + tmpIndex);
										if (tmpIndex != null)
											{
												Performance.Data tmpda = new Performance.Data();
												try
													{
														tmpda = SnmpGatherInterface
																.GetInstance()
																.getDataIMDFull(
																		device_id,
																		macOid
																				+ "."
																				+ tmpIndex);
													}
												catch (Exception e)
													{
														e.printStackTrace();
													}
												if ((tmpda != null)
														&& (tmpda.dataStr != null))
													{
														macStr += tmpda.dataStr.trim()
																.substring(0, 17)
																+ "<br>";
													}
											}
									}
								macStr += "<cisco>";
							}
					}
			}
		LOG.debug("cisco MAC ====" + macStr);
		return macStr;
	}
}
