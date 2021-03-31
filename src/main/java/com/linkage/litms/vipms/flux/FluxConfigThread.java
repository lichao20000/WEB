package com.linkage.litms.vipms.flux;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.corba.interfacecontrol.FluxManagerInterface;
import com.linkage.litms.common.corba.interfacecontrol.SnmpGatherInterface;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.litms.webtopo.common.ThreadPool;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class FluxConfigThread extends Thread
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(FluxConfigThread.class);
	int threadnum = 0;
	String alreadyDo = "0";
	public ArrayList sqlList = new ArrayList();
	private String account = null;
	private String passwd = null;
	// private HttpSession session = null;
	// Connection connection = null;
	int portnum = 0;
	private ArrayList portsList = null;
	/** 是否根据端口类型过滤 */
	private boolean bIfypeFilter = true;
	private Map filterMap = new HashMap();
	// RemoteDB.DataManager manager = null;
	// private int type = 1;
	// VpnScheduler schedule = null;
	// 是否自动配置，1：自动配置，0：手工配置
//	private int isauto = 1;
//	// 采集方式，只有在手工配置时，才起作用，版本号_计数器，
//	// 默认V2版本的64位计数器
//	private String gatherFlag = "3";
	public FluxConfigThread(int threadnum)
	{
		this.threadnum = threadnum;
	}
//	public void setIsAuto(int auto_)
//	{
//		isauto = auto_;
//	}
//	public void setGatherFlag(String gatherFlag_)
//	{
//		gatherFlag = gatherFlag_;
//	}
//	public void setSession(HttpSession session)
//	{
//	
//		UserRes userRes = (UserRes) session.getAttribute("curUser");
//		this.account = userRes.getUser().getAccount();
//		this.passwd = userRes.getUser().getPasswd();
//	}
	public void setPortsList(ArrayList list)
	{
		this.portsList = list;
	}
	
	/**
	 * 
	 * @param flag
	 *            boolean
	 */
	public void setBIftypeFilter(boolean flag)
	{
		this.bIfypeFilter = flag;
	}
	/**
	 * 
	 * @return boolean
	 */
	public boolean getBIftypeFilter()
	{
		return bIfypeFilter;
	}
	/**
	 * 
	 * @param flag
	 *            boolean
	 */
	public void setFilterMap(Map map)
	{
		this.filterMap = map;
	}
	/**
	 * 
	 * @return boolean
	 */
	public Map getFilterMap()
	{
		return filterMap;
	}
	public void run()
	{
		// 初始化log,把流量配置中的sql记录下面的文件中
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String fileLog = LipossGlobals.getLipossHome() + File.separator + "logs";
		PrintWriter Log = null;
		try
		{
			File file = new File(fileLog);
			if (!file.exists())
			{
				file.mkdirs();
			}
			fileLog += File.separator + "fluxConfig" + sdf.format(new java.util.Date())
					+ ".sql";
			file = new File(fileLog);
			if (!file.exists())
			{
				file.createNewFile();
			}
			Log = new PrintWriter(new FileWriter(fileLog, true), true);
		}
		catch (Exception e)
		{
			logger.warn("无法打开日志文件");
			Log = new PrintWriter(System.err);
		}
		Log.println("------------------------------------------");
		// 休眠后再初始化数据库连接
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException ex2)
		{
		}
		ArrayList devList = new ArrayList();
		// 创建获取数据线程
		if (portsList != null)
		{
			portnum = portsList.size();
			if ((portnum + 1) < threadnum)
			{
				threadnum = portnum + 1;
			}
			// 生成线程池
			ThreadPool threadPool = new ThreadPool(threadnum);
			// 创建数据库入库线程
			// threadPool.runTask(UpdateDB(Log));
			FluxConfigInfo fci = null;
			logger.debug("createTask_begin:" + portnum + "     alreadyDo:"
					+ alreadyDo + "     " + new DateTimeUtil().getLongDate());
			for (int i = 0; i < portnum; i++)
			{
				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException ex4)
				{
				}
				fci = (FluxConfigInfo) portsList.get(i);
				if (devList.indexOf(fci.getDevice_id()) == -1)
				{
					devList.add(fci.getDevice_id());
					// 删除设备的流量配置信息
					String deleteSQL = "delete from flux_deviceportconfig where device_id='"
							+ fci.getDevice_id() + "'";
					Log.println(deleteSQL);
					sqlList.add(deleteSQL);
					deleteSQL = "delete from flux_interfacedeviceport where device_id='"
							+ fci.getDevice_id() + "'";
					Log.println(deleteSQL);
					sqlList.add(deleteSQL);
					DataSetBean.doBatch(sqlList);
					int[] resultCode = DataSetBean.doBatch(sqlList);
					if (null == resultCode || 0 == resultCode.length)
					{
						Log
								.println("-------------------------------SQL执行失败--------------------------");
					}
					else
					{
						Log
								.println("-------------------------------SQL执行成功--------------------------");
					}
					sqlList.clear();					
				}
				try
				{
					threadPool.runTask(createTask(fci, this.account, this.passwd, Log));
					
					// createTask(fci, this.account, this.passwd).run();
				}
				catch (Exception e)
				{
				}
			}
			// 关闭线程池并等待所有任务完成
			while (sqlList.size() > 0 || Integer.parseInt(alreadyDo) < portnum)
			{
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			threadPool = null;
			logger.debug("createTask_end!");
		}
		// 形成ip列表
		logger.debug("数据库线程处理完毕+++++++++++++++++++++++++++++++++");
		logger.debug("开始调用后台接口！ " + new DateTimeUtil().getLongDate());
		String msg = "性能采集初始化设备成功！";
		try
		{
			logger.debug("devList:" + devList.size());
			int num = devList.size();
			String[] ids = new String[num];
			for (int k = 0; k < num; k++)
			{
				ids[k] = (String) devList.get(k);
			}
			int retflag = -1;
			if (FluxManagerInterface.GetInstance().readDevices(ids))
			{
				retflag = 1;
			}
			if (retflag == 1)
			{
				logger.debug("调用后台接口成功！");
				msg += "调用后台接口成功";
			}
			else
			{
				logger.warn("调用后台接口失败");
				msg += "调用后台接口失败";
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			logger.warn("调用后台接口失败！");
			msg += "调用后台接口失败";
		}
		finally
		{
			devList.clear();
			Log.flush();
			Log.close();
			Log = null;
		}
	}
	public Runnable UpdateDB(final PrintWriter Log)
	{
		return new Runnable()
		{
			public void run()
			{
				try
				{
					while (sqlList.size() > 0 || Integer.parseInt(alreadyDo) < portnum)
					{
						Thread.sleep(1000);
						logger.debug("alreayDo=" + alreadyDo + "         portnum="
								+ portnum + "   sqlList.size()=" + sqlList.size());
						synchronized (sqlList)
						{
							if (sqlList.size() > 0)
							{
								Log
										.println("-----------------------------------SQL执行开始------------------------");
								for (int i = 0; i < sqlList.size(); i++)
								{
									Log.println((String) sqlList.get(i));
								}
								int[] resultCode = DataSetBean.doBatch(sqlList);
								if (null == resultCode || 0 == resultCode.length)
								{
									Log
											.println("-------------------------------SQL执行失败--------------------------");
								}
								else
								{
									Log
											.println("-------------------------------SQL执行成功--------------------------");
								}
								sqlList.clear();
							}
							logger.debug("测试......" + "alreayDo=" + alreadyDo
									+ "         portnum=" + portnum
									+ "   sqlList.size()=" + sqlList.size());
						}
					}
					logger.debug("DB结束！");
				}
				catch (InterruptedException ex)
				{
					ex.printStackTrace();
				}
			}
		};
	}
	/**
	 * 获取性能数据的线程
	 */
	public Runnable createTask(final FluxConfigInfo fci, final String account,
			final String passwd, final PrintWriter Log)
	{
		return new Runnable()
		{
			public long ParseRealSpeed(long value)
			{
				long r_value = 0;
				if (value > 0)
				{
					int TriZeroCount = 0;
					r_value = value;
					if (r_value % 1000 == 0)
					{
						TriZeroCount++;
						r_value /= 1000;
						// 处理剩余的最多12个0
						for (int j = 0; j < 4; j++)
						{
							if ((r_value % 1000) == 0)
							{
								TriZeroCount++;
								r_value /= 1000;
							}
							else
							{
								break;
							}
						}
					}
					for (int i = 0; i < TriZeroCount; i++)
						r_value *= 1000;
				}
				return r_value;
			}
			public void run()
			{
				logger.debug("begin:" + fci.getIfindex());
				// 首先判断该端口的性能采集是否能够采集
				// logger.debug("getDataManager begin!");
				Performance.Data[] datalist = null;
//				if ("".equals(gatherFlag))
//				{
//					synchronized (alreadyDo)
//					{
//						alreadyDo = String.valueOf(Integer.parseInt(alreadyDo) + 1);
//					}
//					return;
//				}
//				// 版本
//				String snmpversion = gatherFlag.split("_")[0];
//				// 计数器
//				String counternum = gatherFlag.split("_")[1];
				// flux_oid_map中的model
				String model = "0";
				String counternum="32";
				String snmpversion="";
				snmpversion=ManagerFluxConfig.getSnmpVersion(fci.getDevice_id());
				
				if("".equals(snmpversion))
				{
					logger.debug("device_id:"+fci.getDevice_id()+" 没有配置设备读口令！");
					synchronized (alreadyDo)
					{
						alreadyDo = String.valueOf(Integer.parseInt(alreadyDo) + 1);
					}
					return;					
				}
				
				ArrayList tempList = ManagerFluxConfig.getOIDList(fci.getSerial(),snmpversion, counternum, "1");
				ArrayList perfList = new ArrayList();
				if (tempList.size() < 2)
				{
					logger.debug("没有获取到性能列表的oid，端口" + fci.getGetway() + ","
							+ fci.getIfindex() + "," + fci.getIfdescr() + ","
							+ fci.getIfportip() + "不做配置");
					synchronized (alreadyDo)
					{
						alreadyDo = String.valueOf(Integer.parseInt(alreadyDo) + 1);
					}
					return;
				}
				for (int i = 0; i < tempList.size(); i++)
				{
					if (i == 0)
					{
						model = ((PortJudgeAttr) tempList.get(i)).getModel();
					}
					perfList.add(((PortJudgeAttr) tempList.get(i)).getValue() + "."
							+ fci.getIfindex());
				}
				String[] oidArray = new String[perfList.size()];
				perfList.toArray(oidArray);
				try
				{
//					if ("1".equals(snmpversion))
//					{						
//						datalist = SnmpGatherInterface.GetInstance().GetFillDataArrayIMD(
//								fci.getDevice_id(), "", oidArray, oidArray.length);
//					}
//					else
//					{						
//						datalist = SnmpGatherInterface.GetInstance()
//								.GetFillDataArrayIMDv2(fci.getDevice_id(), "", oidArray,
//										oidArray.length);
//					}
					datalist=SnmpGatherInterface.GetInstance().GetFillDataArrayIMDFull(fci.getDevice_id(), "", oidArray,oidArray.length);
					if (null == datalist || 0 == datalist.length)
					{
						logger.debug("获取性能列表的值的长度为0,端口" + fci.getGetway() + ","
								+ fci.getIfindex() + "," + fci.getIfdescr() + ","
								+ fci.getIfportip() + "不做配置");
						return;
					}
					// 因为上面对oid的长度不能低于2控制了，这边就不控制了
					if ((null == datalist[0].dataStr
							|| "".equals(datalist[0].dataStr.trim())
							|| "error".equalsIgnoreCase(datalist[0].dataStr) || "NODATA"
							.equalsIgnoreCase(datalist[0].dataStr))
							|| (null == datalist[1].dataStr
									|| "".equals(datalist[1].dataStr.trim())
									|| "error".equalsIgnoreCase(datalist[1].dataStr) || "NODATA"
									.equalsIgnoreCase(datalist[1].dataStr)))
					{
						logger.debug("流入、流出字节数没有都采到数据 " + fci.getGetway() + ","
								+ fci.getIfindex() + "," + fci.getIfdescr() + ","
								+ fci.getIfportip() + "不做配置");
						return;
					}
					// 设置采集参数
					String getGetarg = fci.getGetarg();
					for (int i = 0; i < datalist.length; i++)
					{
						// 采不到数据的情况
						if (null==datalist[i].dataStr||"".equals(datalist[i].dataStr.trim())||"error".equalsIgnoreCase(datalist[i].dataStr)||"NODATA".equalsIgnoreCase(datalist[i].dataStr))
						{
							int num = Integer.parseInt(((PortJudgeAttr) tempList.get(i))
									.getOrder());
							getGetarg = getGetarg.substring(0, num) + "0"
									+ getGetarg.substring(num + 1);
						}
					}
					fci.setGetarg(getGetarg);
					// 准备采速率
					ArrayList baseList = ManagerFluxConfig.getOIDList(fci.getSerial(),
							snmpversion, counternum, "3");
					String[] oidList = new String[4];
					int totalnum = baseList.size();
					PortJudgeAttr pja = null;
					for (int i = 0; i < totalnum; i++)
					{
						pja = (PortJudgeAttr) baseList.get(i);
						oidList[i] = pja.getValue() + "." + fci.getIfindex();
					}
//					if ("1".equals(snmpversion))
//					{
//						datalist = SnmpGatherInterface.GetInstance().GetDataArrayAllIMD(
//								fci.getDevice_id(), "", oidList, oidList.length);
//					}
//					else
//					{
//						datalist = SnmpGatherInterface.GetInstance()
//								.GetDataArrayAllIMDv2(fci.getDevice_id(), "", oidList,
//										oidList.length);
//					}
					datalist=SnmpGatherInterface.GetInstance().GetDataArrayAllIMDFull(fci.getDevice_id(), "", oidList,oidList.length);
//					for(int i=0;i<datalist.length;i++)
//					{
//						logger.debug("++++++++++++++++++++++++index:"+fci.getIfindex()+"oid:"+oidList[i]+"  "+datalist[i].dataStr);
//					}
					String[] datas = new String[5];
					//赋值
					for(int i=0;i<datalist.length;i++)
					{						
						if(null!=datalist[i]&&!"NODATA".equalsIgnoreCase(datalist[i].dataStr)
								&&null!=datalist[i].dataStr
								&&!"".equals(datalist[i].dataStr.trim())
								&&!"0".equals(datalist[i].dataStr.trim()))
						{
							datas[i]=datalist[i].dataStr;
						}
						else
						{
							datas[i]="0";
						}
						
					}
					// 获取高速带宽值,如果能采集到端口速率的话，就乘1000000
					if (!"0".equals(datas[3]))
					{
						datas[3] = String
								.valueOf(Long.parseLong(datas[3]) * 1000000);
					}
					else
					{
						datas[3] = datas[2];
					}
					// 能采到端口速率的情况
					if (!"0".equals(datas[3]))
					{
						// 实际带宽
						datas[4] = String.valueOf(this.ParseRealSpeed(Long.valueOf(
								datas[3]).longValue()));
					}
					else
					{
						datas[4] = "0";
					}

					// 获取port_info字段
					String value = fci.getIfindex();
					switch (fci.getGetway())
					{
						case 5:
							value = fci.getIfportip();
							break;
						case 4:
							value = fci.getIfnamedefined();
							break;
						case 3:
							value = fci.getIfname();
							break;
						case 2:
							value = fci.getIfdescr();
							break;
					}
					String[] sqlArray = new String[2];
					// 生成sql脚本
					StringBuffer sql = new StringBuffer();
					sql
							.append(
									"insert into flux_deviceportconfig(device_id,ifindex,ifdescr,"
											+ "ifname,ifnamedefined,ifportip,port_info,snmpversion,model,counternum,polltime,"
											+ "gatherflag,ifconfigflag,getway,intodb) values('")
							.append(fci.getDevice_id()).append("','").append(
									fci.getIfindex()).append("','").append(
									fci.getIfdescr().replaceAll("'", "\\$\\$\\$")
											.toString()).append("','").append(
									fci.getIfname().replaceAll("'", "\\$\\$\\$")
											.toString()).append("','").append(
									fci.getIfnamedefined().replaceAll("'", "\\$\\$\\$")
											.toString()).append("','").append(
									fci.getIfportip()).append("','").append(
									value.replaceAll("'", "\\$\\$\\$")).append("',")
							.append(snmpversion).append(",'").append(model).append("',")
							.append(counternum).append(",").append(fci.getPolltime())
							.append(",1,1,").append(fci.getGetway()).append(",").append(
									fci.getIntodb()).append(")");
					// 后台要求varchar型的字段，如果snmpGather没有采到的情况，填null字符串
					sqlArray[0] = sql.toString().replaceAll(",''", ",'null'").replaceAll(
							"'',", "'null',").replaceAll("\\$\\$\\$", "''");
					PrepareSQL psql = new PrepareSQL(sqlArray[0]);
			        psql.getSQL();
					Log.println(sqlArray[0]);
					sql = new StringBuffer();
					sql
							.append(
									"insert into flux_interfacedeviceport(device_id,ifindex,ifdescr,"
											+ "ifname,ifnamedefined,ifportip,port_info,getway,getarg,ifportwarn,ifexam,"
											+ "portwarnlevel,iftype,ifmtu,ifspeed,ifhighspeed,if_real_speed,"
											+ "ifinoct_maxtype,ifinoctetsbps_max,ifoutoct_maxtype,ifoutoctetsbps_max,ifindiscardspps_max,ifoutdiscardspps_max,"
											+ "ifinerrorspps_max,ifouterrorspps_max,warningnum,warninglevel,reinstatelevel,"
											+ "ifinoct_mintype,ifinoctetsbps_min,ifoutoct_mintype,ifoutoctetsbps_min,warningnum_min,warninglevel_min,reinlevel_min,"
											+ "overmax,overper,overnum,overlevel,reinoverlevel,com_day,overmin,overper_min,overnum_min,overlevel_min,reinoverlevel_min,"
											+ "ifinoctets,inwarninglevel,inreinstatelevel,inoperation,intbflag,ifoutoctets,outwarninglevel,outreinstatelevel,"
											+ "outoperation,outtbflag) values(")
							.append("'" + fci.getDevice_id() + "'").append(
									",'" + fci.getIfindex() + "'").append(
									",'"
											+ fci.getIfdescr().replaceAll("'",
													"\\$\\$\\$").toString() + "'")
							.append(
									",'"
											+ fci.getIfname().replaceAll("'",
													"\\$\\$\\$").toString() + "'")
							.append(
									",'"
											+ fci.getIfnamedefined().replaceAll("'",
													"\\$\\$\\$").toString() + "'")
							.append(",'" + fci.getIfportip() + "'").append(
									",'" + value.replaceAll("'", "\\$\\$\\$") + "'")
							.append("," + fci.getGetway() + "").append(
									",'" + fci.getGetarg() + "'").append(
									"," + fci.getIfportwarn() + "").append(
									"," + fci.getIfexam() + "").append(
									"," + fci.getPortwarnlevel() + "").append(
									"," + datas[0] + "").append("," + datas[1] + "")
							.append("," + datas[2] + "").append("," + datas[3] + "")
							.append("," + datas[4] + "").append(
									"," + fci.getIfinoct_maxtype() + "").append(
									"," + fci.getIfinoctetsbps_max()).append(
									"," + fci.getIfoutoct_maxtype() + "").append(
									"," + fci.getIfoutoctetsbps_max() + "").append(
									"," + fci.getIfindiscardspps_max() + "").append(
									"," + fci.getIfoutdiscardspps_max() + "").append(
									"," + fci.getIfinerrorspps_max() + "").append(
									"," + fci.getIfouterrorspps_max() + "").append(
									"," + fci.getWarningnum() + "").append(
									"," + fci.getWarninglevel() + "").append(
									"," + fci.getReinstatelevel() + "").append(
									"," + fci.getIfinoct_mintype() + "").append(
									"," + fci.getIfinoctetsbps_min() + "").append(
									"," + fci.getIfoutoct_mintype() + "").append(
									"," + fci.getIfoutoctetsbps_min() + "").append(
									"," + fci.getWarningnum_min() + "").append(
									"," + fci.getWarninglevel_min() + "").append(
									"," + fci.getReinlevel_min() + "").append(
									"," + fci.getOvermax() + "").append(
									"," + fci.getOverper() + "").append(
									"," + fci.getOvernum() + "").append(
									"," + fci.getOverlevel() + "").append(
									"," + fci.getReinoverlevel() + "").append(
									"," + fci.getCom_day() + "").append(
									"," + fci.getOvermin() + "").append(
									"," + fci.getOverper_min() + "").append(
									"," + fci.getOvernum_min() + "").append(
									"," + fci.getOverlevel_min() + "").append(
									"," + fci.getReinoverlevel_min() + "").append(
									"," + fci.getIfinoctets() + "").append(
									"," + fci.getInwarninglevel() + "").append(
									"," + fci.getInreinstatelevel() + "").append(
									"," + fci.getInoperation() + "").append(
									"," + fci.getIntbflag() + "").append(
									"," + fci.getIfoutoctets() + "").append(
									"," + fci.getOutwarninglevel() + "").append(
									"," + fci.getOutreinstatelevel() + "").append(
									"," + fci.getOutoperation() + "").append(
									"," + fci.getOuttbflag() + "").append(")");
				
					sqlArray[1] = sql.toString().replaceAll(",''", ",'null'").replaceAll(
							"'',", "'null',").replaceAll("\\$\\$\\$", "''");
					psql = new PrepareSQL(sqlArray[1]);
			        psql.getSQL();
					Log.println(sqlArray[1]);
					int[] resultCode = DataSetBean.doBatch(sqlArray);
					if (null == resultCode || 0 == resultCode.length)
					{
						Log
								.println("-------------------------------SQL执行失败--------------------------");
					}
					else
					{
						Log
								.println("-------------------------------SQL执行成功--------------------------");
					}
					sqlArray = null;					
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
				finally
				{
					synchronized (alreadyDo)
					{
						alreadyDo = String.valueOf(Integer.parseInt(alreadyDo) + 1);
					}
					logger.debug("end:" + fci.getIfindex());
				}
			}
		};
	}
}