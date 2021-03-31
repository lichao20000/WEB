package com.linkage.module.liposs.performance.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.Log;
import com.linkage.module.liposs.performance.bio.pefdef.BasePefDefInterfance;
import com.linkage.module.liposs.performance.bio.pefdef.PefDefByDepartAreaAndDescId;
import com.linkage.module.liposs.performance.bio.pefdef.PefDefByDescId;
import com.linkage.module.liposs.performance.bio.pefdef.PefDefByMidAndDescId;
import com.linkage.module.liposs.performance.bio.pefdef.PefDefByNoSnmpGather;
import com.linkage.module.liposs.performance.bio.pefdef.PefDefByNoneDescId;
import com.linkage.module.liposs.performance.bio.pefdef.PefDefForSE;
import com.linkage.module.liposs.performance.bio.pefdef.PerDefByDomainAndDescId;
import com.linkage.module.liposs.performance.dao.I_configPmeeDao;
import com.linkage.system.utils.DateTimeUtil;

/**
 * @author wangping5221
 * @version 1.0
 * @since 2008-10-9
 * @category com.linkage.liposs.bio.performance 版权：南京联创科技 网管科技部
 * ************************************修改记录*****************************************************************
 * 序号     时间     修改人           需求&BUG单号          			  修改内容                  备注
 *  1  2008-10-14 BENYP(5260) 					    	贵州要求增加判断是否配置，   同时将采集时间间隔、是否入库也保存原有配置
 *                            						    则重新采集实例配置告警，
 *                            						    否则保存原有告警配置
 *  2  2009-11-25 XIEF(6150) BBMS-SDDX_REQ-RWJ-20091110-005    山东需求如果性能指标类型为13，
 *  															则不需要调用SnmpGather进行试采，直接入库
 * ************************************************************************************************************
 *
 */
public class PmConfigChildThread implements Runnable
{
	private static Logger log = LoggerFactory.getLogger(PmConfigChildThread.class);
	// 设备id
	private String device_id;
	// 性能表达式ID
	private int expression_id;
	// 配置参数
	private Pm_Map_Instance param = null;
	// 数据库操作类
	private I_configPmeeDao dao = null;
	// 日志文件
	private String logFileName = "pmeeConfig" + new DateTimeUtil().getShortDate()
			+ ".sql";
	/**
	 * 描述表达式ID
	 */
	private String descId = null;
	/**
	 * 中转表达式ID
	 */
	private String middleId = null;
	/**
	 * 分域域表达式ID
	 */
	private String departAreaId = null;
	/**
	 * 域domain表达式ID add by zhangsong
	 */
	private String domainId = null;
	/**
	 * domain与VR对应关系的表达式ID
	 */
	private String domainVRId = null;
	/**
	 * domain与ip_pool对应关系的表达式ID
	 */
	private String domainIpPoolId = null;
	/**
	 * 构造函数
	 *
	 * @param device_id_
	 *            设备ID
	 * @param expression_id_
	 *            表达式ID
	 * @param param_
	 *            配置参数
	 * @param dao_
	 *            数据库操作对象
	 */
	public PmConfigChildThread(String device_id_, int expression_id_,
			Pm_Map_Instance param_, I_configPmeeDao dao_)
	{
		device_id = device_id_;
		expression_id = expression_id_;
		param = param_;
		dao = dao_;
	}
	/**
	 * 单个设备单个性能表达式的配置流程
	 */
	public void run()
	{
		boolean isConfig = dao.isConfigPmee(device_id, String.valueOf(expression_id));
		Map<String, Pm_Map_Instance> instanceConfParam = null;
		/**
		 * ***************************************************
		 * Modify By BENYP(5260) E-mail:BENYP@LIANCHUANG.COM
		 * 增加判断是否保留原有配置项
		 * ***************************************************
		 */
		if (isConfig && param.isIskeep())
		{
			instanceConfParam = dao.getPmeeConfigParam(device_id, String
					.valueOf(expression_id));
		}
		else
		{
			instanceConfParam = new HashMap<String, Pm_Map_Instance>();
		}
		// 有可能配置不完整，都做下删除性能表达式的操作
		if (!dao.deleteConfig(device_id, String.valueOf(expression_id)))
		{
			log(false, "删除已配置性能失败，退出配置流程！device_id:" + device_id + "    expression_id:"
					+ expression_id);
			// 数据库操作失败返回
			return;
		}
		// 配置性能
		log(false, "开始启动配置流程！device_id:" + device_id + "    expression_id:"
				+ expression_id);
		List<String> sqlList = new ArrayList<String>();
		String sql = "insert into pm_map(expressionid,device_id,isok,interval) values("
				+ expression_id + ",'" + device_id + "',-1," + param.getInterval() + ")";
		log(true, sql);
		PrepareSQL psql = new PrepareSQL(sql);
		sqlList.add(psql.getSQL());
		// pm_map插入失败，就退出配置流程
		if (!dao.pmeeConfig(sqlList))
		{
			log(false, "insert pm_map失败，退出配置流程！ sql:" + sql);
		}

		try
		{
			/**
			 * 配置流程
			 */
			int result = pmeeConfig(instanceConfParam);

			// 更新pm_map中的配置结果
			sqlList.clear();
			if(result==1)
			{
				sql = "update pm_map set isok=1  where device_id='" + device_id
				+ "' and expressionid=" + expression_id;
			}
			else
			{
				sql = "update pm_map set isok=0,remark="+result+"  where device_id='" + device_id
				+ "' and expressionid=" + expression_id;
			}
			log(true, sql);
			psql = new PrepareSQL(sql);
			sqlList.add(psql.getSQL());
			dao.pmeeConfig(sqlList);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * 真正的采集流程
	 *
	 * @param device_id
	 * @param expression_id
	 * @return
	 */
	private int pmeeConfig(Map<String, Pm_Map_Instance> instanceConfParam)
	{
		List<String> sqlList= new ArrayList<String>();
		int result = 1;

		log(false, "开始采集配置  device_id:" + device_id + "  expression_id:" + expression_id);
		log.warn("开始采集配置  device_id:" + device_id + "  expression_id:" + expression_id);
		// 判断使用什么方式进行性能描述采集
		int theWay = judgeTheWay(String.valueOf(expression_id));
		log(false,"theWay:"+theWay);
		log.warn("theWay:"+theWay);
		BasePefDefInterfance pefDef = null;
		/*
		 * 0:不采集描述,直接把索引作为描述 1:直接通过描述表达式采集 2:通过中转和描述表达式采集
		 * 3:先通过分域性能表达式采集性能,再通过描述表达式采集描述 4:先通过分域性能表达式采集性能,再通过中转和描述表达式采集描述
		 */
		switch (theWay)
		{
			case 1:
				pefDef = new PefDefByDescId();
				break;
			case 2:
				pefDef = new PefDefByMidAndDescId();
				break;
			case 3:
				pefDef = new PefDefByDepartAreaAndDescId();
				break;
			case 4:
				// TODO:暂时还没有这个流程,待有需求后再完善
				break;
			case 5:
				// 此处对Juniper设备配置的PPPOE的分域地址池配置采集,在此方法中就将domain和性能实例关联的关系sql生成
				param.setRemark1("domain");
				pefDef = new PerDefByDomainAndDescId();
				break;
			case 6:
				//SE800分域地址池利用率配置
				pefDef = new PefDefForSE();
				break;
			case 7:
				// 表达式分类1为9
				pefDef = new PefDefByNoSnmpGather();
				break;
			default:
				pefDef = new PefDefByNoneDescId();
				break;
		}
		// 设置配置参数
		pefDef.setBaseInfo(device_id, String.valueOf(expression_id), departAreaId, middleId, descId,
				domainId, domainVRId, domainIpPoolId,dao, instanceConfParam,param,sqlList);
		// 性能配置主要流程
		result=pefDef.mainConfig();


		if(result==1)
		{
			log.warn("开始执行性能配置sql  device_id:"+device_id+"  expression_id:"+expression_id);
			//获取sql
			sqlList=pefDef.createSqlList();
			for(int i=0;i<sqlList.size();i++)
			{
				log(true,sqlList.get(i));
			}

			//执行数据库操作
			if(dao.pmeeConfig(sqlList))
			{
				log.warn("性能配置sql执行成功  device_id:"+device_id+"  expression_id:"+expression_id);
			}
			else
			{
				log.warn("性能配置sql执行失败  device_id:"+device_id+"  expression_id:"+expression_id);
				result=-1;
			}
		}
		else
		{
			log.warn("性能配置采集失败  device_id:"+device_id+"  expression_id:"+expression_id);
		}

		log.warn("结束采集配置  device_id:" + device_id + "  expression_id:" + expression_id
				+ "   result:" + result);


		return result;
	}
	/**
	 * 判断采取什么方式采集描述信息
	 *
	 * @param expressionId
	 * @return 0:不采集描述,直接把索引作为描述 1:直接通过描述表达式采集 2:通过中转和描述表达式采集
	 *         3:先采集域信息,再通过性能表达式采集性能,最后通过描述表达式采集描述
	 *         4:先采集域信息,再通过性能表达式采集性能,最后通过中转和描述表达式采集描述
	 *         5：Juniper pppoe分域地址池利用率配置
	 *         6：SE800分域地址池利用率配置
	 *         7:表达式分类1为9,性能指标类型为'EVDO状态'
	 */
	private int judgeTheWay(String expressionId)
	{
		int theWay = 0;
		// 性能表达式指向的表达式ID
		String expression2Id = null;
		// 先通过性能表达式获取到其指向的表达式ID
		String[] infoArr = dao.getBasicInfoByExpressionId(expressionId);
		if (infoArr != null)
		{
			// 表达式分类1为9,性能指标类型为'EVDO状态',不采集直接入库
			if(!"".equals(infoArr[3]) && Integer.parseInt(infoArr[3]) == 9) {
				return 7;
			}
			expression2Id = infoArr[0];
		}
		// 获取性能表达式指向的表达式的具体信息,进行判断
		infoArr = dao.getBasicInfoByExpressionId(expression2Id);
		if (infoArr != null)
		{
			String expression22Id = infoArr[0];
			int class2 = Integer.parseInt(infoArr[1]);
			if (class2 == 2)
			{
				// 1:直接通过描述表达式采集
				theWay = 1;
				descId = expression2Id;
			}
			else if (class2 == 3)
			{
				middleId = expression2Id;
				if (isIdExist(expression22Id))
				{
					// 2:通过中转和描述表达式采集
					theWay = 2;
					descId = expression22Id;
				}
			}
			else if (class2 == 4)
			{
				// 先通过分域域表达式采集
				departAreaId = expression2Id;
				if (isIdExist(expression22Id))
				{
					String[] tempArr = dao.getBasicInfoByExpressionId(expression22Id);
					if (tempArr != null)
					{
						String expression222Id = tempArr[0];
						int tempclass2 = Integer.parseInt(tempArr[1]);
						if (tempclass2 == 2)
						{
							if (isIdExist(expression222Id))
							{
								theWay = 5;
								// 此处的就是domain对应expression222id
								domainId = expression222Id;
								descId = expression22Id;
								departAreaId = expression2Id;
								String[] domainVRArr = dao
										.getBasicInfoByExpressionId(expression222Id);
								if (domainVRArr != null && domainVRArr.length > 0)
								{
									domainVRId = domainVRArr[0];
									String[] domainIpPoolArr = dao
											.getBasicInfoByExpressionId(domainVRId);
									if (domainIpPoolArr != null
											&& domainIpPoolArr.length > 0)
									{
										domainIpPoolId = domainIpPoolArr[0];
									}
								}
							}
							else
							{
								// 3:先采集域信息,再通过性能表达式采集性能,最后通过描述表达式采集描述
								theWay = 3;
								descId = expression22Id;
							}
						}
						else if (tempclass2 == 3)
						{
							middleId = expression22Id;
							if (isIdExist(expression222Id))
							{
								// 4:先采集域信息,再通过性能表达式采集性能,最后通过中转和描述表达式采集描述
								theWay = 4;
								descId = expression222Id;
							}
						}
					}
				}else{
					//SE800地址池利用率采集
					theWay = 6;
				}
			}
		}
		log.debug("[PefDef_MasterTread] theWay = " + theWay + " & descId = "
				+ descId + " & middleId = " + middleId + " & departAreaId = "
				+ departAreaId);
		return theWay;
	}
	/**
	 * 判断 <code>id</code> 是否存在
	 *
	 * @param id
	 * @return
	 */
	protected boolean isIdExist(String id)
	{
		if (id != null && id.trim().length() > 0 && !id.equals("null")
				&& !id.equals("NULL"))
			return true;
		else
			return false;
	}
	/**
	 * 记录日志的方法
	 *
	 * @param isNeedWriteFile
	 *            是否需要写文件，true：刷屏＋写文件 false：只刷屏不写文件
	 * @param msg
	 *            日志内容
	 */
	private void log(boolean isNeedWriteFile, String msg)
	{
		log.debug(msg);
		if(isNeedWriteFile)
		{
			Log.writeLog(msg, logFileName);
		}
	}
}
