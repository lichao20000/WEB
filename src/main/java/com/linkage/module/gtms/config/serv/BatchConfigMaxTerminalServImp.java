
package com.linkage.module.gtms.config.serv;

import java.util.ArrayList;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.TimeUtil;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.obj.StrategyOBJ;
import com.linkage.module.gwms.util.CreateObjectFactory;

/**
 * @author Administrator (Ailk No.)
 * @version 1.0
 * @since 2013-5-22
 * @category com.linkage.module.gtms.config.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BatchConfigMaxTerminalServImp
{

	private static Logger logger = LoggerFactory
			.getLogger(BatchConfigMaxTerminalServImp.class);


	public String doConfigAll(String[] deviceIds, String serviceId, String[] paramArr,
			String gwType)
	{
		logger.debug("serv-->doConfigAll({},{},{},{})", new Object[] { deviceIds,
				serviceId, paramArr, gwType });
		if (deviceIds.length > 0)
		{
			if (deviceIds[0].indexOf("select") == -1)
			{
				String strategyXmlParam = "";
				strategyXmlParam = paramNodeBXml(paramArr);
				// 入策略表，调预读
				ArrayList<String> sqllist = new ArrayList<String>();
				SuperDAO dao = new SuperDAO();
				String[] stragetyIds = new String[deviceIds.length];
				// 得到设备类型
				int gw_type_int = -1;
				gw_type_int = Integer.parseInt(gwType);
				// 配置的service_id
				int serviceId_int = Integer.parseInt(serviceId);
				for (int i = 0; i < deviceIds.length; i++)
				{
					StrategyOBJ strategyObj = new StrategyOBJ();
					// 策略ID
					strategyObj.createId();
					// 策略配置时间
					strategyObj.setTime(TimeUtil.getCurrentTime());
					// 用户id
					strategyObj.setAccOid(1);
					// 立即执行
					strategyObj.setType(1);
					// 设备ID
					strategyObj.setDeviceId(deviceIds[i]);
					// serviceId
					strategyObj.setServiceId(serviceId_int);
					// 顺序,默认1
					strategyObj.setOrderId(1);
					// 工单类型: 新工单,工单参数为xml串的工单
					strategyObj.setSheetType(2);
					// 参数
					strategyObj.setSheetPara(strategyXmlParam);
					strategyObj.setTempId(serviceId_int);
					strategyObj.setIsLastOne(1);
					stragetyIds[i] = String.valueOf(strategyObj.getId());
					// 入策略表
					sqllist.addAll(dao.strategySQL(strategyObj));
				}
				// 立即执行
				int iCode[] = DataSetBean.doBatch(sqllist);
				if (iCode != null && iCode.length > 0)
				{
					logger.warn("批量执行策略入库：  成功");
				}
				else
				{
					logger.warn("批量执行策略入库：  失败");
				}
				logger.warn("立即执行，开始调用预处理...deviceId[{}],strategyId[{}]", deviceIds,
						stragetyIds);
				// 调用预读
				if (true == CreateObjectFactory.createPreProcess(String.valueOf(gw_type_int))
						.processOOBatch(stragetyIds))
				{
					logger.warn("预读完成");
					logger.debug("调用后台预读模块成功");
					return "1";
				}
				else
				{
					logger.warn("调用后台预读模块失败");
					return "-4";
				}
			}
			else
			{
				try
				{
					// 调用接口
					logger.warn("开始调用配置模块进行配量参数配置(deviceIds：{},serviceId：{},paramArr：{})",
							new Object[] { deviceIds, serviceId, paramArr });
					int ret = CreateObjectFactory.createPreProcess(gwType)
							.processDeviceStrategy(deviceIds, serviceId, paramArr);
					logger.warn("调用配置模块进行配量参数配置结果为(ret={})", new Object[] { ret });
					if (1 == ret)
					{
						logger.debug("调用后台预读模块成功");
						return "1";
					}
					else
					{
						logger.warn("调用后台预读模块失败");
						return "-4";
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					logger.warn("Exception---" + e.getMessage());
					return "-4";
				}
			}
		}
		else
		{
			return "-4";
		}
	}
	public String paramNodeBXml(String[] paramArr)
	{
		String strXml = null;
		// new root
		Document doc = DocumentHelper.createDocument();
		// root node
		Element root = doc.addElement("parameterConfig");
		Element parameterList = root.addElement("parameterList");
		for (int i = 0; i < paramArr.length; i++)
		{
			String parameterStr = paramArr[i];
			if (parameterStr == null || parameterStr.split("ailk!@#").length != 3)
			{
				logger.warn("[{}]批量配置参数的参数不正确,结束");
				logger.error("[{}]批量配置参数的参数不正确,结束");
				return null;
			}
			else
			{
				String[] parameterArr = parameterStr.split("ailk!@#");
				Element parameterEle = parameterList.addElement("parameter");
				parameterEle.addElement("name").addText(parameterArr[0]);
				parameterEle.addElement("value").addText(parameterArr[1]);
				parameterEle.addElement("type").addText(parameterArr[2]);
			}
		}
		strXml = root.asXML();
		return strXml;
	}



}
