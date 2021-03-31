package com.linkage.litms.webtopo.common;

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
import com.linkage.litms.common.corba.interfacecontrol.PmeeInterface;
import com.linkage.litms.common.corba.interfacecontrol.SnmpGatherInterface;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;


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

public class PefDef_MasterTread extends Thread
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(PefDef_MasterTread.class);
	PM_instance pm = null;

	int threadnum = 0;

	String alreadyDo = "0";

	private ArrayList sqlList = new ArrayList();

	private String beforSql = "";

	private String endSql = "";

	private String account = null;

	private String passwd = null;

	private int type = 1;

	// Connection connection = null;

	public PefDef_MasterTread(int threadnum)
	{
		this.threadnum = threadnum;
	}

	/**
	 * 把要配置的表达式对象传过来
	 * 
	 * @param pm
	 */
	public void setPM(PM_instance pm)
	{
		this.pm = pm;
	}

	public void setAccountInfo(String account, String passwd)
	{
		this.account = account;
		this.passwd = passwd;
	}

	public void setType(int _type)
	{
		logger.debug("begin setType!");
		this.type = _type;
		logger.debug("end setType!");
	}

	public void run()
	{
		// 初始化Corba

		// 休眠后再初始化数据库连接
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException ex2)
		{
		}

		// 生成后半端的sql语句
		this.beforSql = "insert into pm_map_instance(expressionid,collect,intodb,mintype,"
						+ "mindesc,minthres,mincount,minwarninglevel,minreinstatelevel,maxtype,maxdesc,"
						+ "maxcount,maxthres,maxwarninglevel,maxreinstatelevel,dynatype,dynadesc,"
						+ "dynacount,beforeday,dynathres,dynawarninglevel,dynareinstatelevel,mutationtype,"
						+ "mutationdesc,mutationcount,mutationthres,mutationwarninglevel,mutationreinstatelevel,id,device_id,indexid,descr) values("
						+ pm.getExpressionid()
						+ ",1,"
						+ pm.getIntodb()
						+ ","
						+ pm.getMintype()
						+ ",'"
						+ pm.getMindesc()
						+ "',"
						+ pm.getMinthres()
						+ ","
						+ pm.getMincount()
						+ ","
						+ pm.getMinwarninglevel()
						+ ","
						+ pm.getMinreinstatelevel()
						+ ","
						+ pm.getMaxtype()
						+ ",'"
						+ pm.getMaxdesc()
						+ "',"
						+ pm.getMaxcount()
						+ ","
						+ pm.getMaxthres()
						+ ","
						+ pm.getMaxwarninglevel()
						+ ","
						+ pm.getMaxreinstatelevel()
						+ ","
						+ pm.getDynatype()
						+ ",'"
						+ pm.getDynadesc()
						+ "',"
						+ pm.getDynacount()
						+ ","
						+ pm.getBeforeday()
						+ ","
						+ pm.getDynathres()
						+ ","
						+ pm.getDynawarninglevel()
						+ ","
						+ pm.getDynareinstatelevel()
						+ ","
						+ pm.getMutationtype()
						+ ",'"
						+ pm.getMutationdesc()
						+ "',"
						+ pm.getMutationcount()
						+ ","
						+ pm.getMutationthres()
						+ ","
						+ pm.getMutationwarninglevel()
						+ ","
						+ pm.getMutationreinstatelevel();
		this.endSql = ") ";

		// 创建获取数据线程
		String[] ids = null;
		if (pm != null)
		{
			ArrayList list = pm.getDevList();
			if (list != null)
			{
				if ((list.size() + 1) < threadnum)
				{
					threadnum = list.size() + 1;
				}
				// 生成线程池
				ThreadPool threadPool = new ThreadPool(threadnum);

				// 初始化log,把流量配置中的sql记录下面的文件中
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				String fileLog = LipossGlobals.getLipossHome() + File.separator
								+ "logs";
				PrintWriter Log = null;
				try
				{
					File file = new File(fileLog);
					if (!file.exists())
					{
						file.mkdirs();
					}
					fileLog += File.separator + "pmee"
									+ sdf.format(new java.util.Date()) + ".sql";
					file = new File(fileLog);
					if (!file.exists())
					{
						file.createNewFile();
					}

					Log = new PrintWriter(new FileWriter(fileLog, true), true);
				}
				catch (Exception e)
				{
					logger.debug("无法打开日志文件");
					Log = new PrintWriter(System.err);
				}

				logger.debug("begin UpdateDB()!");
				// 创建数据库入库线程
				threadPool.runTask(UpdateDB(Log));
				logger.debug("end UpdateDB()!");

				logger.debug("size:" + list.size());
				ids = new String[list.size()];
				for (int i = 0; i < list.size(); i++)
				{
					ids[i] = (String) list.get(i);
					String device_id = (String) list.get(i);
					try
					{
						// Thread.sleep(1000);
						threadPool.runTask(createTask(device_id, this.account,
										this.passwd));
					}
					catch (Exception e)
					{

					}

				}

				// 关闭线程池并等待所有任务完成
				threadPool.join();
				threadPool.close();
				// logger.debug("线程运行结束，开始关闭数据库连接");

			}

		}

		// 形成ip列表
		logger.debug("数据库线程处理完毕+++++++++++++++++++++++++++++++++");
		logger.debug("开始调用后台接口！");

		String msg = "性能采集初始化设备成功！";

		// begin modified by w5221 山东要求性能配置直接通知PMEE
		int retflag = PmeeInterface.GetInstance().readDevices(ids);
		if (retflag == 0)
		{
			logger.debug("调用后台接口成功！");
			msg += "调用后台接口成功";
		}
		else
		{
			logger.debug("调用后台接口失败");
			msg += "调用后台接口失败";
		}
		// end modified by w5221 山东要求性能配置直接通知PMEE
	}

	public Runnable UpdateDB(final PrintWriter Log)
	{
		return new Runnable()
		{

			public void run()
			{

				try
				{
					logger.debug("alreadyDo:" + alreadyDo
									+ "|pm.devList.size():" + pm.devList.size()
									+ "| sqlList.size():" + sqlList.size());
					while (Integer.parseInt(alreadyDo) < pm.devList.size()
									|| sqlList.size() > 0)
					{
						Thread.sleep(1000);
						logger.debug("性能入库线程继续生成sql脚本，插入数据库...........");
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
								if (null == resultCode
												|| 0 == resultCode.length)
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
						}

					}
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
	public Runnable createTask(final String device_id, final String account,
					final String passwd)
	{

		return new Runnable()
		{
			// Statement statement = null;

			// 获取端口总数

			// RemoteDB.DataManager manager = null;

			String[][] datas = null;

			org.omg.CORBA.IntHolder datatype = new org.omg.CORBA.IntHolder();

			Performance.Data[] datalist = null;

			// 存放性能数据
			HashMap dataMap = new HashMap();

			ArrayList dataList = new ArrayList();

			HashMap typeMap = new HashMap();

			// 存放描述数据
			HashMap descMap = new HashMap();

			ArrayList descList = new ArrayList();

			HashMap descTypeMap = new HashMap();

			// ResultSet rs = null;			

			private String getIDCode(String device_id)
			{
				String temp = "";
				for (int i = device_id.length(); i < 22; i++)
				{
					temp += "0";
				}
				return temp + device_id;
			}

			private String getNum(int i)
			{
				String tempStr = String.valueOf(i);
				switch (tempStr.length())
				{
					case 1:
					{
						tempStr = "000" + tempStr;
						break;
					}
					case 2:
					{
						tempStr = "00" + tempStr;
						break;
					}
					case 3:
					{
						tempStr = "0" + tempStr;
						break;
					}
					case 4:
					{
						break;
					}

				}

				return tempStr;

			}

			private int getData(String expressionid, String _device_id,
							String readCom, boolean isCal)
			{
				int flag = 1;
				if (expressionid.length() > 5)
				{
					flag = -6;
					return flag;
				}

				String mysql = "select distinct oid from pm_expression_context where nodetype=2 and expressionid="
								+ expressionid;
				logger.debug(mysql);
				PrepareSQL psql = new PrepareSQL(mysql);
      			psql.getSQL();
				Cursor cursor = DataSetBean.getCursor(mysql);
				Map fields = cursor.getNext();
				while (null != fields)
				{
					String oid = (String) fields.get("oid");
					// 如果不存在的话再进行采集
					if (isCal)
					{
						logger.debug("开始进行性能数据的采集");
						if (dataList.indexOf(oid) == -1)
						{
							logger.debug(_device_id + "," + readCom + ","
											+ oid);
//							datalist = SnmpGatherInterface.GetInstance()
//											.GetDataListIMD(datatype,
//															_device_id,
//															readCom, oid);
							datalist = SnmpGatherInterface.GetInstance().GetDataListPortFull(datatype,_device_id,readCom, oid);
							logger.debug("datalist length is"
											+ datalist.length);
							dataList.add(oid);
							dataMap.put(oid, datalist);
							typeMap.put(oid, datatype);
						}
					}
					else
					{
						logger.debug("开始进行描述信息的采集");
						if (descList.indexOf(oid) == -1)
						{
							logger.debug(_device_id + "," + readCom + ","
											+ oid);
//							datalist = SnmpGatherInterface.GetInstance()
//											.GetDataListIMD(datatype,
//															_device_id,
//															readCom, oid);
							datalist = SnmpGatherInterface.GetInstance().GetDataListPortFull(datatype,_device_id,readCom, oid);
							descList.add(oid);
							descMap.put(oid, datalist);
							descTypeMap.put(oid, datatype);
						}
					}
					fields = cursor.getNext();
				}

				int tempLen = -1;
				// 对描述信息进行验证
				if (descList.size() > 0)
				{
					logger.debug("开始判断描述信息...........");
					for (int i = 0; i < descList.size(); i++)
					{
						String oid = (String) descList.get(i);
						Performance.Data[] data = (Performance.Data[]) descMap
										.get(oid);
						logger.debug("获取到的性能描述数据的长度:" + data.length);
						if (data == null || data.length == 0)
						{
							flag = -2;
							break;
						}
						else
						{
							if (i == 0)
							{
								flag = 1;
								tempLen = data.length;
							}
							else
							{
								logger.debug("tempLen is " + tempLen
												+ ";data.length is"
												+ data.length);
								if (tempLen != data.length)
								{
									flag = -4;
									break;
								}
							}
						}
					}
				}
				else
				{
					logger.debug("没有描述信息需要采集...........");
				}

				// 对采集到的性能数据进行验证
				if (flag == 1 && isCal && dataList.size() > 0)
				{
					logger.debug("开始判断性能信息...........");
					for (int i = 0; i < dataList.size(); i++)
					{
						String oid = (String) dataList.get(i);
						logger.debug("要判断的oid" + oid);
						Performance.Data[] data = (Performance.Data[]) dataMap
										.get(oid);
						logger.debug("获取到的性能数据的长度：" + data.length);
						if (data == null || data.length == 0)
						{
							flag = -21;
							break;
						}
						else
						{
							if (i == 0 && tempLen == -1)
							{
								tempLen = data.length;
							}
							else if (tempLen != data.length)
							{
								logger.debug("tempLen is " + tempLen
												+ ";data.length is"
												+ data.length);
								flag = -4;
								break;
							}
						}
					}
				}
				else
				{
					logger.debug("没有性能信息需要采集...........");
				}

				// 对采集到的数据进行处理并形成sql脚本
				if (flag == 1 && isCal && dataList.size() > 0)
				{
					logger.debug("开始生成sql脚本...........");
					for (int i = 0; i < dataList.size(); i++)
					{
						String oid = (String) dataList.get(i);
						// 获取数据
						Performance.Data[] data = (Performance.Data[]) dataMap
										.get(oid);
						// 获取数据类型
						datatype = (org.omg.CORBA.IntHolder) typeMap.get(oid);
						// 存放索引和描述的值0:放索引，1:放描述,只有在第一次的时候才创建
						if (i == 0)
						{
							datas = new String[data.length][2];
						}
						// 判断是否符合规范
						boolean IsFlag = true;
						for (int j = 0; j < data.length; j++)
						{
							if (datatype.value == 1)
							{
								if (i == 0)
								{
									datas[j][0] = String
													.valueOf(data[j].dataDou);
									datas[j][1] = String
													.valueOf(data[j].dataDou);
								}
								else
								{
									if (!datas[j][0].equals(String
													.valueOf(data[j].dataDou)))
									{
										System.out
														.println("在判断数字索引是否相等的时候:datas[j][0]="
																		+ datas[j][0]
																		+ ",data["
																		+ j
																		+ "].dataDou="
																		+ data[j].dataDou);
										IsFlag = false;
										break;
									}
								}
							}
							else if (datatype.value == 2)
							{
								if (i == 0)
								{
									datas[j][0] = data[j].index;
									datas[j][1] = data[j].index;
								}
								else
								{
									if (!datas[j][0].equals(data[j].index))
									{
										System.out
														.println("在判断字符索引是否相等的时候:datas[j][0]="
																		+ datas[j][0]
																		+ ",data["
																		+ j
																		+ "].index="
																		+ data[j].index);
										IsFlag = false;
										break;
									}
								}

							}
							
						}
						if (!IsFlag)
						{
							flag = -4;
							break;
						}
					}

					// 开始获取描述信息
					if (flag == 1 && descList.size() > 0 && datas != null)
					{
						boolean IsFlag = true;
						mysql = "select equation from pm_expression where expressionid="
										+ "(select expression2id from pm_expression where expressionid="
										+ expressionid + ")";
						String equation = "";
						psql = new PrepareSQL(mysql);
		      			psql.getSQL();
						HashMap record = DataSetBean.getRecord(mysql);
						if(null!=record)
						{
							equation =(String)record.get("equation");
						}						

						// 获取所有的描述信息
						for (int i = 0; i < descList.size(); i++)
						{
							String oid = (String) descList.get(i);
							// 获取数据
							Performance.Data[] data = (Performance.Data[]) descMap
											.get(oid);
							// 获取数据类型
							datatype = (org.omg.CORBA.IntHolder) descTypeMap
											.get(oid);
							for (int j = 0; j < data.length; j++)
							{
								String compdata = "";
								if (datatype.value == 1)
								{
									compdata = String.valueOf(data[j].dataDou);
								}
								else if (datatype.value == 2)
								{
									compdata = data[j].index;
								}

								if (datas[j][0].equals(compdata))
								{
									if (i == 0)
									{
										logger.debug(equation + "&&&&&&"
														+ "." + oid + "&&&&&&&"
														+ data[j].dataStr);
										String temp_equation = equation
														.replaceAll(
																		"."
																						+ oid,
																		data[j].dataStr);
										datas[j][1] = temp_equation;
									}
									else
									{
										datas[j][1] = datas[j][1].replaceAll(
														"." + oid,
														data[j].dataStr);
									}
								}
								else
								{
									IsFlag = false;
									break;
								}

							}
							if (!IsFlag)
							{
								flag = -41;
								break;
							}
						}
					}

					// 形成sql脚本
					if (flag == 1 && datas != null)
					{						
						String idCode = getIDCode(_device_id);
						String exID = getNum(Integer.parseInt(expressionid));
						for (int i = 0; i < datas.length; i++)
						{
							String desc = datas[i][1].replaceAll("'", "''");
							mysql = beforSql + ",'"
											+ (idCode + exID + getNum(i + 1))
											+ "','" + _device_id + "','"
											+ datas[i][0] + "','" + desc + "' "
											+ endSql;
							//logger.debug("pmee_SQL:"+mysql);
							synchronized (sqlList)
							{
								psql = new PrepareSQL(mysql);
				      			psql.getSQL();
								sqlList.add(mysql);
							}
						}

						// 对ippool是总体统计的表达式增加总体统计的实例ID
						String sql = "select class1,statall from pm_expression where expressionid="
										+ expressionid + "";
						psql = new PrepareSQL(mysql);
		      			psql.getSQL();
						HashMap record = DataSetBean.getRecord(sql);
						if(null!=record)
						{
							int class1 = Integer.parseInt((String)record.get("class1"));
							int statall =Integer.parseInt((String)record.get("statall"));
							//begin modified by w5221
							// BJWT-BUG-20070815-SHENKJ-002
							// 地址池利用率、在线用户一定弄个整体统计出来
							if (class1 == 3 || class1 == 7||statall==1)
							{
								mysql = beforSql + ",'"
												+ (idCode + exID + "0000")
												+ "','" + _device_id
												+ "','-1','总体统计'" + endSql;
								synchronized (sqlList)
								{
									psql = new PrepareSQL(mysql);
					      			psql.getSQL();
									sqlList.add(mysql);
								}

							}
							// end modified by w5221
							// BJWT-BUG-20070815-SHENKJ-002
							// 地址池利用率、在线用户一定弄个整体统计出来
						}						
					}
				}
				else
				{
					logger.debug("没有sql脚本需要生成。。。。。。。");
				}

				return flag;
			}

			public void run()
			{
				// 增加耗时
				try
				{
					// Thread.sleep(1000);

					logger.debug("begin createTask!");

					// 是否需要传送readcom
					String readCom = "";
					String expression2id = "";
					String mysql = "select expression2id from pm_expression where expressionid="
									+ pm.getExpressionid();
					PrepareSQL psql = new PrepareSQL(mysql);
	      			psql.getSQL();
					HashMap record = DataSetBean.getRecord(mysql);
					if (null == record)
					{
						return;
					}
					expression2id = (String) record.get("expression2id");					
					int returnFlag = 1;
					if (expression2id != null
									&& expression2id.trim().length() > 0
									&& !expression2id.equals("null"))
					{
						logger.debug("开始获取描述信息");
						try
						{
							logger.debug("expression2id is"
											+ expression2id);
							returnFlag = getData(expression2id, device_id,
											readCom, false);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
					// 代表描述信息采集成功，当然也可以不采集
					if (returnFlag == 1)
					{
						logger.debug("开始获取性能信息");
						try
						{
							returnFlag = getData(pm.getExpressionid(),
											device_id, readCom, true);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}

						// 性能信息采集成功
						if (returnFlag == 1)
						{
							mysql = "update pm_map set isok=1 where expressionid="
											+ pm.getExpressionid()
											+ " and device_id='"
											+ device_id
											+ "'";
						}
						else
						{
							mysql = "update pm_map set isok=0,remark="
											+ returnFlag
											+ " where expressionid="
											+ pm.getExpressionid()
											+ " and device_id='" + device_id
											+ "'";
						}
						synchronized (sqlList)
						{
							psql = new PrepareSQL(mysql);
			      			psql.getSQL();
							sqlList.add(mysql);
						}
					}
					else
					{
						mysql = "update pm_map set isok=0,remark=-3 where expressionid="
										+ pm.getExpressionid()
										+ " and device_id='" + device_id + "'";
						synchronized (sqlList)
						{
							psql = new PrepareSQL(mysql);
			      			psql.getSQL();
							sqlList.add(mysql);
						}
					}

					

					
				}

				catch (Exception ex)
				{
					ex.printStackTrace();
				}
				finally
				{
					synchronized (alreadyDo)
					{
						alreadyDo = String
										.valueOf(Integer.parseInt(alreadyDo) + 1);
					}
				}

			}
		};
	}

}
