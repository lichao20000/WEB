
package com.linkage.module.gtms.resource.serv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.resource.dao.BachServConfigDao;
import com.linkage.module.gtms.resource.obj.BachServObj;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.SocketChannelClient;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.corba.obj.PreServInfoOBJ;

/**
 * @author xiangzl (Ailk No.)
 * @version 1.0
 * @since 2014-3-24
 * @category com.linkage.module.gtms.resource.serv
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BachServConfigBioImpl implements BachServConfigBio
{

	private static Logger logger = LoggerFactory.getLogger(BachServConfigBioImpl.class);
	private BachServConfigDao dao = null;

	@Override
	public String readUploadFile(File file, int rowNum, UserRes curUser, String gw_type,
			String fileType, String fileName)
	{
		logger.debug("ImportDeviceInitServImp==>readUploadFile()");
		return analyzeExcel(file, rowNum, curUser, gw_type, fileName);
	}

	/**
	 * 解析Excel
	 * 
	 * @param file
	 * @param rowNum
	 * @param curUser
	 * @param gw_type
	 * @param fileName
	 * @return
	 */
	public String analyzeExcel(File file, int rowNum, UserRes curUser, String gw_type,
			String fileName)
	{
		logger.debug("ImportDeviceInitServImp==>analyzeExcel()");
		Workbook wwb = null;
		Sheet ws = null;
		String retMsg = "0";
		try
		{
			wwb = Workbook.getWorkbook(file);
			// 总sheet数
			// int sheetNumber = wwb.getNumberOfSheets();
			int sheetNumber = 1; // 默认取第一页
			for (int m = 0; m < sheetNumber; m++)
			{
				ws = wwb.getSheet(m);
				// 当前页总记录行数和列数
				int rowCount = ws.getRows(); // 行数
				int columeCount = ws.getColumns(); // 列数
				// 导入的Excel的列数不为5，则导入的文件不符合规范
				if (columeCount != 19)
				{
					logger.warn("导入的文件“" + fileName + "”的列数不等于19，不符合模板要求");
					return "-1"; // 导入的文件不符合规范
				}
				// rowNum 为每次最多能处理的记录行数 rowCount 为当前sheet的记录行数
				if (rowCount > rowNum + 1)
				{
					logger.warn("导入文件“" + fileName + "”的总记录数大于规定导入行数，返回不做处理（超过则不处理）");
					return "-2"; // 记录数不可以超过2万，请重新导入
				}
				// 第一行为字段名，所以导入的Excel总行数大于1才做解析
				if (rowCount > 1 && columeCount > 0)
				{
					BachServObj obj = null;
					List<BachServObj> list = new ArrayList<BachServObj>();
					// i 从1开始，是因为第一行是标题行，不需要读取
					Map<String, String> userInfo = null;
					String loid = null;
					long userId;
					for (int i = 1; i < rowCount; i++)
					{
						// logger.warn(col +"_" + ws.getCell(col,
						// i).getContents().trim());
						// 读取Excel单元格的内容
						loid = ws.getCell(0, i).getContents().trim();
						if (StringUtil.IsEmpty(loid))
						{
							retMsg = "" + i;// 退出循环不做
							logger.warn("loid空，行号：" + i);
							list.clear();
							break;
						}
						userInfo = dao.queryUserId(loid, gw_type);
						userId = StringUtil.getLongValue(userInfo, "user_id", -1L);
						if (null == userInfo && -1L == userId)
						{
							logger.warn("loid不存在：" + loid);
							continue;
						}
						obj = new BachServObj();
						obj.setUserId(userId);
						obj.setDevice_serialnumber(StringUtil.getStringValue(userInfo,
								"device_serialnumber"));
						obj.setDeviceId(StringUtil.getStringValue(userInfo, "device_id"));
						obj.setOui(StringUtil.getStringValue(userInfo, "oui"));
						obj.setLoid(ws.getCell(0, i).getContents().trim()); // 第0列，第i行数据
						// obj.setCityId(ws.getCell(1, i).getContents().trim());
						obj.setOldNetUserName(ws.getCell(1, i).getContents().trim()); // 第1列，第i行数据
						obj.setNewNetUserName(ws.getCell(2, i).getContents().trim()); // 第2列，第i行数据
						obj.setNewNetPwd(ws.getCell(3, i).getContents().trim()); // 第3列，第i行数据
						obj.setNetWanType(ws.getCell(4, i).getContents().trim()); // 第4列，第i行数据
						obj.setNetVlanId(ws.getCell(5, i).getContents().trim());
						obj.setOldItvUserName(ws.getCell(6, i).getContents().trim());
						obj.setNewItvUserName(ws.getCell(7, i).getContents().trim());
						obj.setItvBindPort(ws.getCell(8, i).getContents().trim());
						obj.setItvVlanId(ws.getCell(9, i).getContents().trim());
						obj.setOldTelphone(ws.getCell(10, i).getContents().trim());
						obj.setNewTelphone(ws.getCell(11, i).getContents().trim());
						obj.setRegId(ws.getCell(12, i).getContents().trim());
						obj.setMgcIp(ws.getCell(13, i).getContents().trim());
						obj.setMgcPort(ws.getCell(14, i).getContents().trim());
						obj.setStandMgcIp(ws.getCell(15, i).getContents().trim());
						obj.setStandMgcPort(ws.getCell(16, i).getContents().trim());
						// obj.setRegIdType(ws.getCell(18, i).getContents().trim());
						obj.setVoiceVlanId(ws.getCell(17, i).getContents().trim());
						// obj.setVoiceWanType(ws.getCell(20, i).getContents().trim());
						// obj.setVoiceIp(ws.getCell(21, i).getContents().trim());
						// obj.setVoiceIpMask(ws.getCell(22, i).getContents().trim());
						// obj.setVoiceGateway(ws.getCell(23, i).getContents().trim());
						// obj.setVoiceDns(ws.getCell(24, i).getContents().trim());
						obj.setVoipPort(ws.getCell(18, i).getContents().trim());
						// obj.setProtocol(ws.getCell(26, i).getContents().trim());
						list.add(obj);
					}
					int sipId = 0;
					for (BachServObj bach : list)
					{
						// logger.warn(objs.toString());
						// if ("" == objs.getCityId() && "".equals(objs.getLoid())) {
						// logger.warn("loid为空（要求不能为空），返回");
						// continue;
						// }
						// 宽带工单
						if (!StringUtil.IsEmpty(bach.getNetWanType())
								|| !StringUtil.IsEmpty(bach.getNetVlanId()))
						{
							// 修改宽带业务数据
							int row = dao.updateNetServInfo(bach, gw_type);
							int num = dao.updateNetServParam(bach);
							// 调用配置模块下发业务
							if (row > 0 && num > 0
									&& !StringUtil.IsEmpty(bach.getDeviceId()))
							{
								logger.warn(bach.getLoid() + "更新宽带条数：" + num);
								logger.warn("调用下发：" + bach.getLoid());
								PreServInfoOBJ preInfoObj = new PreServInfoOBJ(
										String.valueOf(bach.getUserId()),
										bach.getDeviceId(), bach.getOui(),
										bach.getDevice_serialnumber(), "10", "1");
								if (1 != CreateObjectFactory.createPreProcess(gw_type)
										.processServiceInterface(CreateObjectFactory.createPreProcess()
												.GetPPBindUserList(preInfoObj)))
								{
									logger.warn("通知后台失败:" + bach.getLoid());
									// result = "-1";
								}
							}
							else
							{
								logger.warn("没有绑定设备，或没有修改业务参数：" + bach.getLoid());
							}
						}
						// itv工单
						if (!StringUtil.IsEmpty(bach.getItvVlanId()))
						{
							// itv该账号
							int row = dao.updateItvServInfo(bach, gw_type);
							// 调用配置模块下发业务
							if (row > 0 && null != bach.getDeviceId())
							{
								logger.warn("调用下发：" + bach.getLoid());
								PreServInfoOBJ preInfoObj = new PreServInfoOBJ(
										String.valueOf(bach.getUserId()),
										bach.getDeviceId(), bach.getOui(),
										bach.getDevice_serialnumber(), "11", "1");
								if (1 != CreateObjectFactory.createPreProcess(gw_type)
										.processServiceInterface(CreateObjectFactory.createPreProcess()
												.GetPPBindUserList(preInfoObj)))
								{
									logger.warn("通知后台失败:" + bach.getLoid());
									// result = "-1";
								}
							}
							else
							{
								logger.warn("没有绑定设备，或没有修改业务参数：" + bach.getLoid());
							}
						}
						// 语音工单
						if (!StringUtil.IsEmpty(bach.getRegId())
								|| !StringUtil.IsEmpty(bach.getMgcIp())
								|| !StringUtil.IsEmpty(bach.getMgcPort())
								|| !StringUtil.IsEmpty(bach.getStandMgcIp())
								|| !StringUtil.IsEmpty(bach.getStandMgcPort())
								|| !StringUtil.IsEmpty(bach.getVoiceVlanId()))
						{
							if (!StringUtil.IsEmpty(bach.getMgcIp())
									&& !StringUtil.IsEmpty(bach.getMgcPort())
									&& !StringUtil.IsEmpty(bach.getStandMgcIp())
									&& !StringUtil.IsEmpty(bach.getStandMgcPort()))
							{
								// 查询sip_id
								sipId = dao.getSipId(bach);
							}
							// 更新语音业务
							int row = dao.updateVoipServInfo(bach, gw_type);
							// 更新语音业务参数
							dao.updateVoipParam(bach, gw_type, sipId);
							// 调用配置下发
							if (row > 0 && null != bach.getDeviceId())
							{
								logger.warn("调用下发：14——" + bach.getLoid());
								PreServInfoOBJ preInfoObj = new PreServInfoOBJ(
										String.valueOf(bach.getUserId()),
										bach.getDeviceId(), bach.getOui(),
										bach.getDevice_serialnumber(), "14", "1");
								if (1 != CreateObjectFactory.createPreProcess(gw_type)
										.processServiceInterface(CreateObjectFactory.createPreProcess()
												.GetPPBindUserList(preInfoObj)))
								{
									logger.warn("通知后台失败:" + bach.getLoid());
									// result = "-1";
								}
							}
							else
							{
								logger.warn("没有绑定设备，或没有修改业务参数：" + bach.getLoid());
							}
						}
					}
					// retMsg = "0"; // 导入成功
				}
				else
				{
					logger.warn("上传的Excel文件为空！");
					return "-3"; // 上传的Excel文件为空，请重新上传
				}
			}
			return retMsg;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.warn("解析“" + fileName + "”失败");
			logger.error("解析“" + fileName + "”失败，msg=({})", e.getMessage());
			return "-5"; // 解析Excel失败！
		}
	}

	private String sendSheet(String sheet)
	{
		String retMessage = "";
		SocketChannelClient client = null;
		try
		{
			client = LipossGlobals.getSocketChannelClientInstance();
			retMessage = client.sendMsg(sheet);
		}
		catch (IOException e)
		{
			logger.error("错误：" + e);
			retMessage = "网络问题";
		}
		finally
		{
			if (client != null)
			{
				try
				{
					client.close();
				}
				catch (IOException e)
				{
					logger.error("关闭SocketChannelClient错误：" + e);
					retMessage = "网络问题";
				}
			}
		}
		return retMessage;
	}

	@Override
	public void WriteFile(File file, String fileName)
	{
		File localPath = new File(LipossGlobals.G_ServerHome + "/temp/");
		try
		{
			FileUtils.copyFileToDirectory(file, localPath);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BachServConfigDao getDao()
	{
		return dao;
	}

	public void setDao(BachServConfigDao dao)
	{
		this.dao = dao;
	}
}
