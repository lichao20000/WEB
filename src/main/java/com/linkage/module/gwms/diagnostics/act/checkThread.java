package com.linkage.module.gwms.diagnostics.act;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ailk.tr069.devrpc.obj.rpc.DevRpcCmdOBJ;
import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.acs.soap.io.XML;
import com.linkage.litms.acs.soap.io.XmlToRpc;
import com.linkage.litms.acs.soap.object.AnyObject;
import com.linkage.litms.acs.soap.object.ParameterValueStruct;
import com.linkage.litms.acs.soap.object.SoapOBJ;
import com.linkage.litms.acs.soap.service.GetParameterValues;
import com.linkage.litms.acs.soap.service.GetParameterValuesResponse;
import com.linkage.litms.acs.soap.service.SetParameterValues;
import com.linkage.litms.common.corba.DevRPCManager;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.obj.tr069.ParameValueOBJ;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.FtpUtil;
import com.linkage.module.gwms.util.corba.ACSCorba;
import com.linkage.module.gwms.util.corba.DevReboot;
import com.linkage.module.gwms.util.corba.PreProcessCorba;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;
import com.linkage.module.gwms.util.corba.obj.PreServInfoOBJ;

import ACS.DevRpc;
import ACS.Rpc;


public class checkThread extends Thread {

	public static String SERV_LIST_INTERNET = "INTERNET";
	public static String SERV_LIST_TR069 = "TR069";
	public static String SERV_LIST_VOIP = "VOIP";
	public static String SERV_LIST_OTHER = "OTHER";
	public static int GATHER_WAN = 2;
	String usernames="";
	
	private static Logger logger = LoggerFactory.getLogger(checkThread.class);
	private ACSCorba acsCorba = new ACSCorba();
	private String gw_type;
	private String account;
	private String ids;
	private String device_serialnumber;
	private String deviceId;
	private String loid;
	private String user_id;
	private String oui;
	private String city_id;



	public checkThread(String gw_type,String account, String ids,String device_serialnumber, String deviceId,String loid,String user_id,String oui,String city_id) {
		this.gw_type = gw_type;
		this.account = account;
		this.ids = ids;
		this.device_serialnumber = device_serialnumber;
		this.deviceId = deviceId;
		this.loid = loid;
		this.user_id = user_id;
		this.oui = oui;
		this.city_id = city_id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() 
	{
		String vendor_id="";
		String device_model_id="";
		String hardwareversion="";
		String softwareversion="";
		String str=" select a.vendor_id,a.device_model_id,c.hardwareversion,c.softwareversion from  ";
		str+="  tab_gw_device a left join tab_devicetype_info c on a.devicetype_id = c.devicetype_id where a.device_id='"+deviceId+"' ";
		logger.info(str);
		ArrayList query = DataSetBean.executeQuery(str, null);
		if(query!=null && query.size()>0) {
			Map<String, String> map = (Map<String, String>) query.get(0);
			vendor_id=map.get("vendor_id")==null?"":map.get("vendor_id").toString();
			device_model_id=map.get("device_model_id")==null?"":map.get("device_model_id").toString();
			hardwareversion=map.get("hardwareversion")==null?"":map.get("hardwareversion").toString();
			softwareversion=map.get("softwareversion")==null?"":map.get("softwareversion").toString();
		}
		
		long id=System.currentTimeMillis();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = df.format(new Date());
				
		StringBuffer sql=new StringBuffer();
		sql.append(" insert into tab_check_project_log a     \n"); 
		sql.append(" (a.id,                                  \n"); 
		sql.append("  a.acc_loginname,                       \n"); 
		sql.append("  a.loid,                                \n"); 
		sql.append("  a.device_serialnumber,                 \n"); 
		sql.append("  a.vendor_id,                           \n"); 
		sql.append("  a.device_model_id,                     \n"); 
		sql.append("  a.hardwareversion,                     \n"); 
		sql.append("  a.softwareversion,                     \n"); 
		sql.append("  a.test_time)                           \n"); 
		sql.append(" values ("+id+",'"+account+"','"+loid+"','"+device_serialnumber+"', \n");
		sql.append("  '"+vendor_id+"','"+device_model_id+"' ,'"+hardwareversion+"','"+softwareversion+"','"+time+"'\n");
		sql.append("  )\n");
		logger.info(sql.toString());
		DataSetBean.executeUpdate(sql.toString());
		
		//ping通道时需要查询的节点
		Map<String,String> wanConnDeviceMap = getPingInterface(deviceId, gw_type);
		logger.warn("通道:"+wanConnDeviceMap.toString());
		
		acsCorba = new ACSCorba(gw_type);
		List<Map<String, String>> netList=new ArrayList<Map<String,String>> ();
		List<Map<String, String>> voipList=new ArrayList<Map<String,String>> ();
		List<Map<String, String>> iptvList=new ArrayList<Map<String,String>> ();
		List<Map<String, String>> resultList=new ArrayList<Map<String,String>> ();
		ArrayList itemList = queryCheck_itemList(gw_type);
		//最终结果是否合格
		int is_qualified=1;
		
		String checkValue="";
		for (int i = 0; i < itemList.size(); i++) 
		{
			Map<String, String> itemMap = (Map<String, String>) itemList.get(i);
			String operate =itemMap.get("operate")==null?"":itemMap.get("operate").toString();
			String name =itemMap.get("name")==null?"":itemMap.get("name").toString();
			//业务分类.
			String classify =itemMap.get("classify")==null?"":itemMap.get("classify").toString();
			
			if("4".equals(operate)) {
				//特殊处理
				logger.warn(name+"特殊处理");
				
				if (name.equals("stb_mac")) {
					checkValue="";
					ArrayList macList = queryMacList(deviceId);
					if (macList!=null && macList.size()>0) {
						for (int j = 0; j < macList.size(); j++) {
							Map<String, String> map = (Map<String, String>) macList.get(j);
							String stb_mac =map.get("stb_mac")==null?"":map.get("stb_mac").toString();
							checkValue+=stb_mac+";";
						}
						itemMap.put("checkValue",checkValue);
						itemMap.put("result", "合格");
					}else {
						itemMap.put("checkValue","");
						itemMap.put("result", "不合格");
						is_qualified=0;
					}
					
					
				}else if (name.equals("auto_service")) {
					//业务注册下发测试,设备发起注册，ITMS完整下发宽带、IPTV、VOIP业务参数，业务在3分钟类下发成功
					Map<String, String> map = checkAuto_service();
					checkValue = map.get("checkValue");
					String is_qual = map.get("is_qualified")==null?"":map.get("is_qualified").toString();
					if(!is_qual.equals("0")) {
						Map<String, String> checkTime = checkTime(gw_type);
						is_qual = checkTime.get("is_qualified")==null?"":checkTime.get("is_qualified").toString();
						if(is_qual.equals("0")) {
							checkValue = "开通时间超过3分钟";
						}
					}
					
					itemMap.put("checkValue",checkValue);
					if (is_qual.equals("0")) {
						itemMap.put("result", "不合格");
						is_qualified=0;
					}else {
						itemMap.put("result", "合格");
					}
					
				}else if (name.equals("ping_manage_channel")) {
					//此管理通道不用ping
					itemMap.put("result", "合格");
					itemMap.put("checkValue","ping管理通道成功");
				}else if (name.equals("ping_net_channel")) {
					if(wanConnDeviceMap == null || wanConnDeviceMap.isEmpty())
					{
						itemMap.put("result", "不合格");
						itemMap.put("checkValue","获取通道失败");
						is_qualified=0;
					}else {
						//宽带口
						String internet="";
						for(String key: wanConnDeviceMap.keySet())
						{
							logger.warn("key:" +key);
							logger.warn("VALUE:" +wanConnDeviceMap.get(key));
							
							if(key.startsWith("INTERNET"))
							{
								internet=wanConnDeviceMap.get(key).split("￥")[0];//默认取的是第一个
							}
						}
						if(!"".equals(internet)){
								Map<String, String> defaultdiag = getDefaultdiag();
								Map<String, String> map = PingList(gw_type, deviceId,device_serialnumber, internet, defaultdiag.get("column1"), defaultdiag.get("column2"), defaultdiag.get("column3"), defaultdiag.get("column4"));
								String result=map.get("result");
								String errMessage = map.get("resultDesc")==null?"":map.get("resultDesc").toString();
								if ("0".equals(result)) {
									String DevSn =map.get("DevSn");
									String SuccesNum=map.get("SuccesNum");
									String FailNum=map.get("FailNum");
									String AvgResponseTime=map.get("AvgResponseTime");
									String MinResponseTime=map.get("MinResponseTime");
									String MaxResponseTime=map.get("MaxResponseTime");
									String PacketLossRate=map.get("PacketLossRate");
									String IPOrDomainName=map.get("IPOrDomainName");
									checkValue="设备序列号:"+DevSn+";成功数:"+SuccesNum+";失败数:"+FailNum+";平均响应时间:"+AvgResponseTime+";";
									checkValue+="最小响应时间:"+MinResponseTime+";最大响应时间:"+MaxResponseTime+";丢包率(%):"+PacketLossRate+";IP地址或域名"+IPOrDomainName+"";
									itemMap.put("result", "合格");
									itemMap.put("checkValue",checkValue);
								}else {
									itemMap.put("result", "不合格");
									itemMap.put("checkValue",errMessage);
									is_qualified=0;
								}
						}else {
							itemMap.put("result", "不合格");
							itemMap.put("checkValue","获取上网通道失败");
							is_qualified=0;
						}
					}	
					
				}else if (name.equals("ping_voip")) {
					if(wanConnDeviceMap == null || wanConnDeviceMap.isEmpty())
					{
						itemMap.put("result", "不合格");
						itemMap.put("checkValue","获取通道失败");
						is_qualified=0;
					}else {
						//语音口
						String voipWan="";
						for(String key: wanConnDeviceMap.keySet())
						{
							logger.warn("key:" +key);
							logger.warn("VALUE:" +wanConnDeviceMap.get(key));
							
							if(key.startsWith("VOIP"))
							{
								voipWan=wanConnDeviceMap.get(key).split("￥")[0];//默认取的是第一个
							}
						}
						if(!"".equals(voipWan)){
								Map<String, String> defaultdiag = getDefaultdiag();
								Map<String, String> map = PingList(gw_type, deviceId,device_serialnumber, 
										voipWan, defaultdiag.get("column1"), defaultdiag.get("column2"), 
										defaultdiag.get("column3"), defaultdiag.get("column4"));
								String result=map.get("result");
								String errMessage = map.get("resultDesc")==null?"":map.get("resultDesc").toString();
								if ("0".equals(result)) {
									String DevSn =map.get("DevSn");
									String SuccesNum=map.get("SuccesNum");
									String FailNum=map.get("FailNum");
									String AvgResponseTime=map.get("AvgResponseTime");
									String MinResponseTime=map.get("MinResponseTime");
									String MaxResponseTime=map.get("MaxResponseTime");
									String PacketLossRate=map.get("PacketLossRate");
									String IPOrDomainName=map.get("IPOrDomainName");
									checkValue="设备序列号:"+DevSn+";成功数:"+SuccesNum+";失败数:"+FailNum+";平均响应时间:"+AvgResponseTime+";";
									checkValue+="最小响应时间:"+MinResponseTime+";最大响应时间:"+MaxResponseTime+";丢包率(%):"+PacketLossRate+";IP地址或域名"+IPOrDomainName+"";
									itemMap.put("result", "合格");
									itemMap.put("checkValue",checkValue);
								}else {
									itemMap.put("result", "不合格");
									itemMap.put("checkValue",errMessage);
									is_qualified=0;
								}
						}else {
							itemMap.put("result", "不合格");
							itemMap.put("checkValue","获取VOIP通道失败");
							is_qualified=0;
						}	
				}	
					
				}else if (name.equals("manual_service")) {
					updateCustStatus(Long.parseLong(user_id));
					PreServInfoOBJ preInfoObj = new PreServInfoOBJ(
							user_id, deviceId, oui,
							device_serialnumber, "", "1");
					//新版本调配置模块
					if (1 == CreateObjectFactory.createPreProcess(gw_type).processServiceInterface(CreateObjectFactory.createPreProcess()
					.GetPPBindUserList(preInfoObj))) {
				//老版本调配置模块  江西现网
				//if (1 == new PreProcessCorba(gw_type).processServiceInterface(
				//PreProcessCorba.GetPPBindUserList(preInfoObj))) {
						
						try {
							Thread.sleep(60*3*1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Map<String, String> map = checkAuto_service();
						checkValue = map.get("checkValue");
						String is_qual = map.get("is_qualified")==null?"":map.get("is_qualified").toString();
						itemMap.put("checkValue",checkValue);
						if (is_qual.equals("0")) {
							itemMap.put("result", "不合格");
							is_qualified=0;
						}else {
							itemMap.put("result", "合格");
						}
						
					}else {
						itemMap.put("checkValue","手动业务下发失败");
						itemMap.put("result", "不合格");
						is_qualified=0;
					}
					
				}else if (name.equals("reboot")) {
					int irt = DevReboot.reboot(deviceId,gw_type);
					if(1 == irt){
						//等3分钟检测在线
						try {
							Thread.sleep(3*60*1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//重启后检测在线
						int int_flag = getConnectionFlag(deviceId);
						if(int_flag == 1){
							itemMap.put("result", "合格");
							itemMap.put("checkValue","重启成功");
						}else {
							itemMap.put("result", "不合格");
							itemMap.put("checkValue","重启失败");
							is_qualified=0;
						}
					}else {
						itemMap.put("result", "不合格");
						itemMap.put("checkValue","重启失败");
						is_qualified=0;
					}
					
				}else if (name.equals("resume")) {
					
					updateCustStatus(Long.parseLong(user_id));
					int irt =DevReboot.reset(deviceId,gw_type);
					if (1 == irt) {
						itemMap.put("result", "合格");
						itemMap.put("checkValue","恢复出厂设置成功");
					}else {
						//下发恢复出厂设置命令失败后，业务用户表修改成成功状态
						updateCustStatusFailure(Long.parseLong(user_id));
						itemMap.put("result", "不合格");
						itemMap.put("checkValue","恢复出厂设置失败");
						is_qualified=0;
					}
				}
				
			}else {
				//采集处理
				String node =itemMap.get("node")==null?"":itemMap.get("node").toString();
				String standard_value =itemMap.get("standard_value")==null?"":itemMap.get("standard_value").toString();
				logger.warn("采集节点:"+node);
				if(!"".equals(node)) {
					
					//对于一个节点值包含用户名和密码的处理
					if (node.indexOf(",")!=-1) {
						String[] nodes = node.split(",");
						String[] gatherPath = new String[]{nodes[0],nodes[1]};
						
						ArrayList<ParameValueOBJ> objLlist = acsCorba.getValue(deviceId, gatherPath);
						String value="";
						for(ParameValueOBJ pvobj : objLlist){
							value+= pvobj.getValue()+"/";
							//比较标准值和测试值
							}
						
						if (!"".equals(value)) {
							checkValue=value.substring(0, value.length()-1);
						}else {
							checkValue="";
						}
						String compareValue = compare(standard_value, checkValue,Integer.parseInt(operate));
						if (compareValue.equals("0")) {
							itemMap.put("checkValue",checkValue);
							itemMap.put("result", "不合格");
							is_qualified=0;
						}else {
							itemMap.put("checkValue",checkValue);
							itemMap.put("result", "合格");
						}
						
					}else {
						
						//节点下有多个口需要采集
						if (node.indexOf("{i}")!=-1) {
							
							//对应要获取通道的特殊处理
							if (name.equals("igmp_snooping")) {
								Map<String, List<String>> ijMap = gatherNetIJList(deviceId);
								List<String> list = ijMap.get("OTHER");
								if (list!=null&&list.size()>0) {
									String iptv = list.get(0);
									String[] iptvs = iptv.split("##");
									node=node.replace("{i}", iptvs[0]).replace("{j}", iptvs[1]);
									ArrayList<ParameValueOBJ> nodeValueList = acsCorba.getValue(deviceId, node);
									if (nodeValueList==null ||nodeValueList.size()<0) {
										itemMap.put("checkValue",node+"未采集到节点值");
										itemMap.put("result", "不合格");
										is_qualified=0;
									}else {
										checkValue=nodeValueList.get(0).getValue()==null?"":nodeValueList.get(0).getValue().toString();
										String compareValue = compare(standard_value, checkValue,Integer.parseInt(operate));
										if (compareValue.equals("0")) {
											itemMap.put("checkValue",checkValue);
											itemMap.put("result", "不合格");
											is_qualified=0;
										}else {
											itemMap.put("checkValue",checkValue);
											itemMap.put("result", "合格");
										}
									}
								}else {
									itemMap.put("checkValue", "没有获取到iptv通道，无法采集测试");
									itemMap.put("result", "不合格");
									is_qualified=0;
								}
							}else if (name.equals("IPv4/v6")) {
								Map<String, List<String>> ijMap = gatherNetIJList(deviceId);
								List<String> list = ijMap.get("INTERNET");
								if (list!=null&&list.size()>0) {
									String iptv = list.get(0);
									String[] iptvs = iptv.split("##");
									node=node.replace("{i}", iptvs[0]).replace("{j}", iptvs[1]);
									ArrayList<ParameValueOBJ> nodeValueList = acsCorba.getValue(deviceId, node);
									if (nodeValueList==null ||nodeValueList.size()<0) {
										itemMap.put("checkValue",node+"未采集到节点值");
										itemMap.put("result", "不合格");
										is_qualified=0;
									}else {
										checkValue=nodeValueList.get(0).getValue()==null?"":nodeValueList.get(0).getValue().toString();
										String compareValue = compare(standard_value, checkValue,Integer.parseInt(operate));
										if (compareValue.equals("0")) {
											itemMap.put("checkValue",checkValue);
											itemMap.put("result", "不合格");
											is_qualified=0;
										}else {
											itemMap.put("checkValue",checkValue);
											itemMap.put("result", "合格");
										}
									}
								}else {
									itemMap.put("checkValue", "没有获取到INTERNET通道，无法采集测试");
									itemMap.put("result", "不合格");
									is_qualified=0;
								}
							}else {
								String[] nodes = node.split("\\{i\\}");
								List<String> iList =acsCorba.getIList(deviceId, nodes[0]);
								if (null == iList || iList.isEmpty())
								{
									logger.warn("[{}]"+node+"获取iList失败，返回", deviceId);
									itemMap.put("checkValue", "");
									itemMap.put("result", "不合格,"+node+"获取iList失败");
									is_qualified=0;
								}else{
									logger.warn("[{}]"+node+"获取iList成功，iList.size={}", deviceId,iList.size());
									
									//每个检测项是否合格
									int is_itemQualified=1;
									String itemCheckValue="";
									for(String ii : iList){
										String[] gatherPath = new String[]{nodes[0]+ii+nodes[1]};
										
										ArrayList<ParameValueOBJ> objLlist = acsCorba.getValue(deviceId, gatherPath);
										if (objLlist==null) {
											is_itemQualified=0;
											itemCheckValue="未采集到节点值";
										}else {
											for(ParameValueOBJ pvobj : objLlist){
												checkValue = pvobj.getValue();
												//比较标准值和测试值
												String compareValue = compare(standard_value, checkValue,Integer.parseInt(operate));
												itemCheckValue+=gatherPath[0]+"的采集值为:"+checkValue+"  ";
												if (compareValue.equals("0")) {
													//单独项不合格
													is_itemQualified=0;
												}
											}
										}
										
									}
									if (is_itemQualified==0) {
										itemMap.put("checkValue",itemCheckValue);
										itemMap.put("result", "不合格");
										is_qualified=0;
									}else {
										itemMap.put("checkValue", itemCheckValue);
										itemMap.put("result", "合格");
									}
									
								}
							}
							
							
						}else {
							ArrayList<ParameValueOBJ> nodeValueList = acsCorba.getValue(deviceId, node);
							if (nodeValueList==null ||nodeValueList.size()<0) {
								itemMap.put("checkValue","未采集到节点值");
								itemMap.put("result", "不合格");
								is_qualified=0;
							}else {
								checkValue=nodeValueList.get(0).getValue()==null?"":nodeValueList.get(0).getValue().toString();
								String compareValue = compare(standard_value, checkValue,Integer.parseInt(operate));
								if (compareValue.equals("0")) {
									itemMap.put("checkValue",checkValue);
									itemMap.put("result", "不合格");
									is_qualified=0;
								}else {
									itemMap.put("checkValue",checkValue);
									itemMap.put("result", "合格");
								}
							}
						}
					}
				}else {
					itemMap.put("checkValue", "");
					itemMap.put("result", "不合格,采集节点为空");
					is_qualified=0;
				}
			}
			if ("1".equals(classify)) {
				netList.add(itemMap);
			}else if ("2".equals(classify)) {
				voipList.add(itemMap);
			}else if ("3".equals(classify)) {
				iptvList.add(itemMap);
			}else {
				resultList.add(itemMap);
			}
		}
		//生成excel文件
		Map<String, String> fileMap= createFile(resultList,netList,voipList,iptvList);
		String localPath = fileMap.get("localPath");
		
		String creatFileTime = df.format(new Date());
		
		String sql1="update tab_check_project_log a set a.file_path='"+localPath
				+"',a.is_qualified='"+is_qualified+"',a.file_time='"+creatFileTime
				+"' ,a.state=1 where id ="+id+"";
		logger.info(sql1);
		DataSetBean.executeUpdate(sql1.toString());
	}

	
	public Map<String, String> createFile(List<Map<String, String>>	resultList,
			List<Map<String, String>>  netList,List<Map<String, String>>  voipList,
			List<Map<String, String>>  iptvList) 
	{
		SXSSFWorkbook book = new SXSSFWorkbook(100);
		Sheet sheet = book.createSheet("检测结果");
		
		 // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        Row row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        // 设置这些样式
        // 生成并设置另一个样式
        CellStyle style2 = book.createCellStyle();
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style2.setWrapText(true);
        style2.setFillPattern(HSSFCellStyle.NO_FILL); 
        // 生成另一个字体
        Font font2 = book.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style2.setFont(font2);
        
        Cell cell = row.createCell((short) 0);  
        cell.setCellValue("测试项目");  
        cell.setCellStyle(style2);  
        
        cell = row.createCell((short) 1);  
        cell.setCellValue("测试要求");  
        cell.setCellStyle(style2);  
        
        cell = row.createCell((short) 2);  
        cell.setCellValue("通过标准");  
        cell.setCellStyle(style2);  
        
        cell = row.createCell((short) 3);  
        cell.setCellValue("采集节点");  
        cell.setCellStyle(style2);  
        
        cell = row.createCell((short) 4);  
        cell.setCellValue("测试值");  
        cell.setCellStyle(style2); 
        
        cell = row.createCell((short) 5);  
        cell.setCellValue("测试结果");  
        cell.setCellStyle(style2);
        
        cell = row.createCell((short) 6);  
        cell.setCellValue("备注");  
        cell.setCellStyle(style2);
        
        //计数 ，有没有分类标题
        int count=1;
        
        int resultListSize=resultList.size();
        if (resultListSize>0) {
        	//创建分类
        	Row row1 = sheet.createRow((int) 1);
        	cell = row1.createCell((short) 0);  
        	cell.setCellValue("采集和其他业务测试");  
        	cell.setCellStyle(style2);
        	CellRangeAddress region = new CellRangeAddress(count, count, (short) 0, (short) 6);
        	sheet.addMergedRegion(region);
        	count++;
		}
        
        
        for (int i = 0; i < resultList.size(); i++)  
        {
            row = sheet.createRow((int) i + count);  
            Map totalMap = (Map) resultList.get(i);  
            // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue((String)totalMap.get("test_name"));  
            row.createCell((short) 1).setCellValue((String)totalMap.get("test_require"));  
            row.createCell((short) 2).setCellValue((String)totalMap.get("standard_value"));  
            row.createCell((short) 3).setCellValue((String)totalMap.get("node"));
            row.createCell((short) 4).setCellValue((String)totalMap.get("checkValue"));  
            row.createCell((short) 5).setCellValue((String)totalMap.get("result")); 
            row.createCell((short) 6).setCellValue((String)totalMap.get("remark")); 
        } 
        
        
        int netListSize=netList.size();
        if (netListSize>0) {
        	//创建分类
        	Row row2 = sheet.createRow((int) count+resultListSize);
        	cell = row2.createCell((short) 0);  
        	cell.setCellValue("宽带业务测试");  
        	cell.setCellStyle(style2);
        	CellRangeAddress region1 = new CellRangeAddress(count+resultListSize, count+resultListSize, (short) 0, (short) 6);
        	sheet.addMergedRegion(region1);
        	count++;
		}
        
        for (int i = 0; i < netList.size(); i++)  
        {
            row = sheet.createRow((int) i + count+resultListSize);  
            Map totalMap = (Map) netList.get(i);  
            // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue((String)totalMap.get("test_name"));  
            row.createCell((short) 1).setCellValue((String)totalMap.get("test_require"));  
            row.createCell((short) 2).setCellValue((String)totalMap.get("standard_value"));  
            row.createCell((short) 3).setCellValue((String)totalMap.get("node"));
            row.createCell((short) 4).setCellValue((String)totalMap.get("checkValue"));  
            row.createCell((short) 5).setCellValue((String)totalMap.get("result")); 
            row.createCell((short) 6).setCellValue((String)totalMap.get("remark")); 
        } 
        
        int voipListSize=voipList.size();
        if (voipListSize>0) {
        	//创建分类
        	Row row3 = sheet.createRow((int) count+resultListSize+netListSize);
        	cell = row3.createCell((short) 0);  
        	cell.setCellValue("语音业务测试");  
        	cell.setCellStyle(style2);
        	CellRangeAddress region2 = new CellRangeAddress(count+resultListSize+netListSize, count+resultListSize+netListSize, (short) 0, (short) 6);
        	sheet.addMergedRegion(region2);
        	count++;
		}
        
        for (int i = 0; i < voipList.size(); i++)  
        {
            row = sheet.createRow((int) i + count+resultListSize+netListSize);  
            Map totalMap = (Map) voipList.get(i);  
            // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue((String)totalMap.get("test_name"));  
            row.createCell((short) 1).setCellValue((String)totalMap.get("test_require"));  
            row.createCell((short) 2).setCellValue((String)totalMap.get("standard_value"));  
            row.createCell((short) 3).setCellValue((String)totalMap.get("node"));
            row.createCell((short) 4).setCellValue((String)totalMap.get("checkValue"));  
            row.createCell((short) 5).setCellValue((String)totalMap.get("result")); 
            row.createCell((short) 6).setCellValue((String)totalMap.get("remark")); 
        } 
        
        int iptvListSize=iptvList.size();
        if (iptvListSize>0) {
        	//创建分类
        	Row row4 = sheet.createRow((int) count+resultListSize+netListSize+voipListSize);
        	cell = row4.createCell((short) 0);  
        	cell.setCellValue("IPTV业务测试");  
        	cell.setCellStyle(style2);
        	CellRangeAddress region3 = new CellRangeAddress(count+resultListSize+netListSize+voipListSize,
        			count+resultListSize+netListSize+voipListSize, (short) 0, (short) 6);
        	sheet.addMergedRegion(region3);
        	count++;
		}
        
        for (int i = 0; i < iptvList.size(); i++)  
        {
            row = sheet.createRow((int) i + count+resultListSize+netListSize+voipListSize);  
            Map totalMap = (Map) iptvList.get(i);  
            // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue((String)totalMap.get("test_name"));  
            row.createCell((short) 1).setCellValue((String)totalMap.get("test_require"));  
            row.createCell((short) 2).setCellValue((String)totalMap.get("standard_value"));  
            row.createCell((short) 3).setCellValue((String)totalMap.get("node"));
            row.createCell((short) 4).setCellValue((String)totalMap.get("checkValue"));  
            row.createCell((short) 5).setCellValue((String)totalMap.get("result")); 
            row.createCell((short) 6).setCellValue((String)totalMap.get("remark")); 
        }
        
        
        String ftpPath= getFilePath();
        File dir = new File(ftpPath);
		if (dir.exists()) {
             if (dir.isDirectory()) {
                 logger.warn("dir exists");
             } else {
            	 logger.warn("the same name file exists, can not create dir");
             }
         } else {
             dir.mkdir();
         }
		String batch=System.currentTimeMillis()+"";
		//文件名
		String fileName=device_serialnumber+"_"+batch+".xlsx";
		//本地文件地址
		String localPath=ftpPath+fileName;
		logger.warn(localPath);
		try {
			File file = new File(localPath);
		    FileOutputStream out = new FileOutputStream(file);
			book.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//同步ftp
		try{
			String host = LipossGlobals.getLipossProperty("FTP.HOST");
			int port = Integer.parseInt(LipossGlobals.getLipossProperty("FTP.PORT"));
			String username = LipossGlobals.getLipossProperty("FTP.USERNAME");
			String password = LipossGlobals.getLipossProperty("FTP.PASSWORD");
			InetAddress address = InetAddress.getLocalHost();
			String[] hosts = host.split(",");
			for (int i = 0; i < hosts.length; i++) {
				if(!address.getHostAddress().equals(hosts[i])){
					FileInputStream in=new FileInputStream(new File(localPath));
		            boolean test = FtpUtil.uploadFile(hosts[i], username, password, port, "/export/home/itms/WEB_GTMS/excels", fileName,in);
		            logger.warn("同步文件到主机："+hosts[i]+":"+test);
				}
			}
		 }catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, String> map =new HashMap<String, String>();
		map.put("localPath", localPath);
		map.put("creatFileTime", batch);
		
		return map;
	}
	
	public String getFilePath()
	{
		// 获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try
		{
			lipossHome = java.net.URLDecoder.decode(a.substring(0, a
					.lastIndexOf("WEB-INF") - 1), "UTF-8");
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径", lipossHome);
		return lipossHome + "/excels/";
	}
	
	
	public Map<String, String> checkAuto_service() {
		Map<String, String> map =new HashMap<String, String>();
		String tableName="tab_hgwcustomer";
		String tableName1="hgwcust_serv_info";
		if (gw_type.equals("2")) {
			tableName="tab_egwcustomer";
			tableName1="egwcust_serv_info";
		}
		
		String checkValue="";
		int[] serv_type_id= {10,11,14};
		String[] serv_type_name= {"宽带","IPTV","语音"};
		for (int i = 0; i < serv_type_id.length; i++) {
			StringBuffer sql=new StringBuffer();
			sql.append(" select a.user_id, a.username,a.device_id,b.serv_type_id, b.dealdate,b.opendate, b.open_status,b.username as \n");
			sql.append(" serUsername from "+tableName+" a \n");
			sql.append(" inner join "+tableName1+" b on a.user_id = b.user_id where 1 = 1 and b.open_status=1 and b.serv_status=1 and b.serv_type_id ="+serv_type_id[i]+" \n");
			sql.append(" and a.user_id in ("+user_id+") \n");
			logger.info(sql.toString());
			ArrayList<Map> list = DataSetBean.executeQuery(sql.toString(), null);
			if (list!=null && list.size()>0) {
				String serUsername=StringUtil.getStringValue(list.get(0).get("serusername"));
				usernames+="'"+serUsername+"',";
				checkValue+="成功开通"+serv_type_name[i]+"业务,";
			}else {
				checkValue+="未开通"+serv_type_name[i]+"业务";
				map.put("is_qualified", "0");
			}
		}
		if (usernames.length()>0) {
			usernames=usernames.substring(0, usernames.length()-1);
		}
		map.put("checkValue", checkValue);
		return map;
	}
	
	public Map<String, String> checkTime(String gw_type) {
		Map<String, String> map =new HashMap<String, String>();
		String tableName="gw_serv_strategy_serv";
		if (gw_type.equals("2")) {
			tableName="gw_serv_strategy";
		}
		
		String checkValue="";
			StringBuffer sql=new StringBuffer();
			sql.append("select  min(start_time) start_time ,max(end_time) end_time from "+tableName+" where ");
			sql.append(" device_id='"+deviceId+"' and service_id in (1001,1101,1401) and username in ("+usernames+")");
			logger.info(sql.toString());
			List<Map> list = DataSetBean.executeQuery(sql.toString(), null);
			
			String start_time=StringUtil.getStringValue(list.get(0).get("start_time"));
			String end_time=StringUtil.getStringValue(list.get(0).get("end_time"));
			
			long time=Long.parseLong(end_time)-Long.parseLong(start_time);
			if (time>3*60) {
				checkValue+="开通时间超过3分钟";
				map.put("is_qualified", "0");
			}
			map.put("checkValue", checkValue);
		return map;
	}
	
	public ArrayList queryMacList(String device_id) 
	{
		String sql="select stb_mac from tab_gw_device_stbmac  where device_id ='"+device_id+"'";
		logger.info(sql);
		return DataSetBean.executeQuery(sql, null);
	}
	
	public ArrayList queryCheck_itemList(String gw_type) 
	{
		
		String tableName="tab_hgw_gather_node_config";
		if (gw_type.equals("2")) {
			tableName="tab_egw_gather_node_config";
		}
		String sql="select * from "+tableName+" where id in ("+ids+") order by priority asc";
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			sql="select operate,name,classify,node,standard_value from "
					+ tableName+" where id in ("+ids+") order by priority asc";
		}
		logger.info(sql);
		return DataSetBean.executeQuery(sql, null);
	}
	
	/**
	 * 1.测试结果和标准值相等比较 2.测试结果包含标准结果 3.测试结果不为空 4.特殊处理
	 */
	public String compare(String standard_value,String checkValue,int operate) {
			String compareValue="0";
			switch(operate){
		    case 1 :
				if (standard_value.equals(checkValue)) {
					compareValue = "1";
				} else {
					compareValue = "0";
				}
		       break;
		    case 2 :
				if (checkValue.indexOf(standard_value)!=-1) {
					compareValue = "1";
				} else {
					compareValue = "0";
				}
		       break;
		    case 3 :
		    	if (!checkValue.equals("")) {
					compareValue = "1";
				} else {
					compareValue = "0";
				}
		       break;
		}
			
			return compareValue;
		}
	
	/**
	 * 检测在线
	 */
	public int getConnectionFlag(String device_id){
		logger.debug("getConnectionFlag({})", device_id);
		
		//得到设备类型
		String gw_type = LipossGlobals.getGw_Type(device_id);
		
		DevRpc[] devRPCArr = new DevRpc[1];
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = device_id;
		Rpc[] rpcArr = new Rpc[1];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "";
		rpcArr[0].rpcValue = "";
		devRPCArr[0].rpcArr = rpcArr;
		// corba
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		devRPCRep = devRPCManager.execRPC(devRPCArr, Global.RpcTest_Type);
		int flag = 0;
		if (devRPCRep == null || devRPCRep.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", device_id);
		}
		else if (devRPCRep.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ返回为空！", device_id);
		}
		else
		{
			flag = devRPCRep.get(0).getStat();
		}
		return flag;
	}
	
	/**
	 * 设备恢复出厂设置后更新用户状态
	 * 
	 * @param gwType
	 * @param userId
	 */
	public void updateCustStatus(long userId)
	{
		String tableName= " hgwcust_serv_info ";
		if("2".equals(gw_type)){
			tableName = "egwcust_serv_info";
		}
		PrepareSQL pSql = new PrepareSQL();
		pSql.append("update "+tableName);
		pSql.append(" set open_status=0,updatetime=? where user_id=? and serv_status in (1,2) and open_status<>0");
		int index = 0;
		pSql.setLong(++index, new DateTimeUtil().getLongTime());
		pSql.setLong(++index, userId);
		int updateRows = DBOperation.executeUpdate(pSql.getSQL());
		logger.info("update table[{}] rows[{}].", tableName, updateRows);
	}
	
	public void updateCustStatusFailure(long userId) {
		String tableName= " hgwcust_serv_info ";
		if("2".equals(gw_type)){
			tableName = " egwcust_serv_info ";
		}
		PrepareSQL pSql = new PrepareSQL();
		pSql.append("update ");
		pSql.append(tableName);
		pSql.append(" set open_status=1,updatetime=? where user_id=? and serv_status in (1,2) and open_status=0");
		int index = 0;
		pSql.setLong(++index, new DateTimeUtil().getLongTime());
		pSql.setLong(++index, userId);
		int updateRows = DBOperation.executeUpdate(pSql.getSQL());
		logger.info("update table[{}] rows[{}].", tableName, updateRows);
	}
	
	
	/**
	 * 获取通道
	 */
	public Map<String,String> getPingInterface(String device_id, String gw_type)
	{
		logger.debug("getPingInterface({},{})",new Object[]{device_id,gw_type});
		
		Map<String,String> restMap = new HashMap<String, String>();
		
		String value = "";
		
		String wanConnDevice = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
		
		SuperGatherCorba sgCorba = new SuperGatherCorba(gw_type);
		// 获取Wan
		// 1、调用采集,采集InternetGatewayDevice.WANDevice下节点
		int irt = sgCorba.getCpeParams(device_id, GATHER_WAN, 1);
		logger.warn("[{}]调用采集获取Wan的结果：" + irt,device_id);
		String errorMsg = "";
		if (irt != 1)
		{
			errorMsg = "调用采集失败";
			logger.warn("[{}]"+errorMsg,device_id);
		}
		else
		{
			// 2、从数据库获取wan_conn_id/wan_conn_sess_id
			List<Map> wanConnIds = getWanConnIds(device_id,gw_type);
			if (wanConnIds == null || wanConnIds.isEmpty())
			{
				errorMsg = "没有获取到Wan接口";
				logger.warn("[{}]"+errorMsg,device_id);
			}
			else
			{

				for (Map map : wanConnIds)
				{
					String wan_conn_id = StringUtil
							.getStringValue(map.get("wan_conn_id"));
					String wan_conn_sess_id = StringUtil.getStringValue(map
							.get("wan_conn_sess_id"));
					String sessType = StringUtil.getStringValue(map.get("sess_type"));
					String serv_list = StringUtil.getStringValue(map.get("serv_list"));
					String vlanid = StringUtil.getStringValue(map.get("vlan_id"));
					if (sessType.equals("1"))
					{
						value = wanConnDevice + wan_conn_id + ".WANPPPConnection."
								+ wan_conn_sess_id + ".";
					}
					else if (sessType.equals("2"))
					{
						value = wanConnDevice + wan_conn_id + ".WANIPConnection."
								+ wan_conn_sess_id + ".";
					}
					else
					{
						logger.warn("[{}]sessType值不对：" + sessType,device_id);
						continue;
					}
					if(SERV_LIST_INTERNET.equals(serv_list))
					{
						restMap.put(SERV_LIST_INTERNET+"###"+vlanid, value);
					}
					else if (SERV_LIST_VOIP.equals(serv_list))
					{
						restMap.put(SERV_LIST_VOIP+"###"+vlanid, value);
					}
					else if (SERV_LIST_TR069.equals(serv_list))
					{
						restMap.put(SERV_LIST_TR069+"###"+vlanid, value);
					}
					else if (SERV_LIST_OTHER.equals(serv_list))
					{
						restMap.put(SERV_LIST_OTHER+"###"+vlanid, value);
					}
					else
					{
						logger.warn("[{}]serv_list值不对：" + serv_list,device_id);
						continue;
					}

				}
			
			}
		}
		return restMap;
	}
	
	
	private List getWanConnIds(String device_id,String gw_type)
	{
		String tableName="gw_wan_conn";
		String tableName1="gw_wan_conn_session";
		if (gw_type.equals("2")) {
			tableName="gw_wan_conn_bbms";
			tableName1="gw_wan_conn_session_bbms";
		}
		
		PrepareSQL psql = new PrepareSQL();
		psql.append("select b.sess_type,b.serv_list,a.vlan_id,b.wan_conn_id,b.wan_conn_sess_id,");
		//oracle db
		if(Global.DB_ORACLE == DBUtil.GetDB()){
			psql.append("to_char(a.vpi_id) || '/' || to_char(a.vci_id) pvc ");
		}else if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("concat(a.vpi_id,'/',a.vci_id) pvc");
		}else{
			psql.append("convert(varchar,a.vpi_id)+'/'+convert(varchar,a.vci_id) pvc ");
		}
		psql.append("from "+tableName+" a,"+tableName1+" b where a.device_id=b.device_id ");
		psql.append("and a.wan_conn_id=b.wan_conn_id and a.device_id='"+device_id+"'");
		
		return DataSetBean.executeQuery(psql.getSQL(), null);
	}
	
	/**
	 * ping检测
	 */
	public Map<String, String> PingList(String gw_type, String device_id,String devSn,
			String waninterface,String column1, String column2,
			String column3, String column4) 
	{
		Map<String, String> map=new HashMap<String, String>();
		DevRpc[] devRPCArr = new DevRpc[1];
		
		AnyObject anyObject = new AnyObject();
		SetParameterValues setParameterValues = new SetParameterValues();
		
		ParameterValueStruct[] ParameterValueStruct = new ParameterValueStruct[7];
		
		ParameterValueStruct[0] = new ParameterValueStruct();
		ParameterValueStruct[0].setName("InternetGatewayDevice.IPPingDiagnostics.DiagnosticsState");
		anyObject.para_value = "Requested";
		anyObject.para_type_id = "1";
		ParameterValueStruct[0].setValue(anyObject);
		
		ParameterValueStruct[1] = new ParameterValueStruct();
		ParameterValueStruct[1].setName("InternetGatewayDevice.IPPingDiagnostics.Interface");
		anyObject = new AnyObject();
		anyObject.para_value = waninterface;
		anyObject.para_type_id = "1";
		ParameterValueStruct[1].setValue(anyObject);
		
		ParameterValueStruct[2] = new ParameterValueStruct();
		ParameterValueStruct[2].setName("InternetGatewayDevice.IPPingDiagnostics.Host");
		anyObject = new AnyObject();
		anyObject.para_value = column2;
		anyObject.para_type_id = "1";
		ParameterValueStruct[2].setValue(anyObject);
		
		ParameterValueStruct[3] = new ParameterValueStruct();
		ParameterValueStruct[3].setName("InternetGatewayDevice.IPPingDiagnostics.NumberOfRepetitions");
		anyObject = new AnyObject();
		anyObject.para_value = column3;
		anyObject.para_type_id = "3";
		ParameterValueStruct[3].setValue(anyObject);
		
		ParameterValueStruct[4] = new ParameterValueStruct();
		ParameterValueStruct[4].setName("InternetGatewayDevice.IPPingDiagnostics.Timeout");
		anyObject = new AnyObject();
		anyObject.para_value = column4;
		anyObject.para_type_id = "3";
		ParameterValueStruct[4].setValue(anyObject);
		
		ParameterValueStruct[5] = new ParameterValueStruct();
		ParameterValueStruct[5].setName("InternetGatewayDevice.IPPingDiagnostics.DataBlockSize");
		anyObject = new AnyObject();
		anyObject.para_value = column1;
		anyObject.para_type_id = "3";
		ParameterValueStruct[5].setValue(anyObject);
		
		ParameterValueStruct[6] = new ParameterValueStruct();
		ParameterValueStruct[6].setName("InternetGatewayDevice.IPPingDiagnostics.DSCP");
		anyObject = new AnyObject();
		anyObject.para_value = "0";
		anyObject.para_type_id = "3";
		ParameterValueStruct[6].setValue(anyObject);
		
		setParameterValues.setParameterList(ParameterValueStruct);
		setParameterValues.setParameterKey("Ping");
		GetParameterValues getParameterValues = new GetParameterValues();
		
		String[] parameterNamesArr = new String[5];
		parameterNamesArr[0] = "InternetGatewayDevice.IPPingDiagnostics.SuccessCount";
		parameterNamesArr[1] = "InternetGatewayDevice.IPPingDiagnostics.FailureCount";
		parameterNamesArr[2] = "InternetGatewayDevice.IPPingDiagnostics.AverageResponseTime";
		parameterNamesArr[3] = "InternetGatewayDevice.IPPingDiagnostics.MinimumResponseTime";
		parameterNamesArr[4] = "InternetGatewayDevice.IPPingDiagnostics.MaximumResponseTime";
		getParameterValues.setParameterNames(parameterNamesArr);
		devRPCArr[0] = new DevRpc();
		devRPCArr[0].devId = device_id;
		Rpc[] rpcArr = new Rpc[2];
		rpcArr[0] = new Rpc();
		rpcArr[0].rpcId = "1";
		rpcArr[0].rpcName = "SetParameterValues";
		rpcArr[0].rpcValue = setParameterValues.toRPC();
		rpcArr[1] = new Rpc();
		rpcArr[1].rpcId = "2";
		rpcArr[1].rpcName = "GetParameterValues";
		rpcArr[1].rpcValue = getParameterValues.toRPC();
		devRPCArr[0].rpcArr = rpcArr;
		
		List<DevRpcCmdOBJ> devRPCRep = null;
		DevRPCManager devRPCManager = new DevRPCManager(gw_type);
		devRPCRep = devRPCManager.execRPC(devRPCArr, Global.DiagCmd_Type);
		
		String errMessage = "";
		Map PingMap = null;
		if (devRPCRep == null || devRPCRep.size() == 0)
		{
			logger.warn("[{}]List<DevRpcCmdOBJ>返回为空！", device_id);
			errMessage = "设备未知错误";
			map.put("result", "10051");
			map.put("resultDesc", errMessage);
			logger.warn("PingDiagnostic==>ReturnXml:"+map.toString());
			return map;
			
		}
		else if (devRPCRep.get(0) == null)
		{
			logger.warn("[{}]DevRpcCmdOBJ返回为空！", device_id);
			errMessage = "设备未知错误";
			map.put("result", "10052");
			map.put("resultDesc", errMessage);
			logger.warn("PingDiagnostic==>ReturnXml:"+map.toString());
			return map;
		}
		else
		{
			int stat = devRPCRep.get(0).getStat();
			logger.warn("设备 stat：[{}] ", stat);
			if (stat != 1)
			{
				errMessage = Global.G_Fault_Map.get(stat).getFaultDesc();
				map.put("result", "1006");
				map.put("resultDesc", errMessage);
				logger.warn("PingDiagnostic==>ReturnXml:"+map.toString());
				return map;
			}
			else
			{
				errMessage = "系统内部错误";
				if (devRPCRep.get(0).getRpcList() == null
						|| devRPCRep.get(0).getRpcList().size() == 0)
				{
					logger.warn("[{}]List<ACSRpcCmdOBJ>返回为空！", device_id);
					map.put("result", "1007");
					map.put("resultDesc", errMessage);
					logger.warn("PingDiagnostic==>ReturnXml:"+map.toString());
					return map;
				}
				else
				{
					List<com.ailk.tr069.devrpc.obj.mq.Rpc> rpcList = devRPCRep.get(0).getRpcList();
					if (rpcList != null && !rpcList.isEmpty())
					{
						for (int k = 0; k < rpcList.size(); k++)
						{
							if ("GetParameterValuesResponse".equals(rpcList.get(k).getRpcName()))
							{
								String resp = rpcList.get(k).getValue();
								logger.warn("[{}]设备返回：{}", device_id, resp);
								if (resp == null || "".equals(resp))
								{
									logger.debug("[{}]DevRpcCmdOBJ.value == null", device_id);
									map.put("result", "1011");
									map.put("resultDesc", "系统内部错误，无返回值");
									logger.warn("PingDiagnostic==>ReturnXml:"+map.toString());
									return map;
								}
								else
								{
									SoapOBJ soapOBJ = XML.getSoabOBJ(XML.CreateXML(resp));
									if (soapOBJ != null)
									{
										Element element = soapOBJ.getRpcElement();
										if (element != null)
										{
											GetParameterValuesResponse getParameterValuesResponse = XmlToRpc
													.GetParameterValuesResponse(element);
											if (getParameterValuesResponse != null)
											{
												ParameterValueStruct[] parameterValueStructArr = getParameterValuesResponse
														.getParameterList();
												PingMap = new HashMap<String, String>();
												for (int j = 0; j < parameterValueStructArr.length; j++)
												{
													PingMap.put(parameterValueStructArr[j].getName(),
																	parameterValueStructArr[j].getValue().para_value);
												}
											} else {
												map.put("result", "1008");
												map.put("resultDesc", "系统内部错误，无返回值");
												logger.warn("PingDiagnostic==>ReturnXml:"+map.toString());
												return map;
											}
										} else {
											map.put("result", "1009");
											map.put("resultDesc", "系统内部错误，无返回值");
											logger.warn("PingDiagnostic==>ReturnXml:"+map.toString());
											return map;
										}
									} else {
										map.put("result", "1010");
										map.put("resultDesc", "系统内部错误，无返回值");
										logger.warn("PingDiagnostic==>ReturnXml:"+map.toString());
										return map;
									}
								}
							} 
						}
					} else {
						map.put("result", "1013");
						map.put("resultDesc", "系统内部错误，无返回值");
						logger.warn("PingDiagnostic==>ReturnXml:"+map.toString());
						return map;
					}
				}
				
				if (PingMap == null){
					map.put("result", "1014");
					map.put("resultDesc", "返回值为空，Ping仿真失败");
					logger.warn("PingDiagnostic==>ReturnXml:"+map.toString());
					return map;
				}else {
					// 成功数
					String succesNum = ""+PingMap.get("InternetGatewayDevice.IPPingDiagnostics.SuccessCount");
					String failNum = ""+PingMap.get("InternetGatewayDevice.IPPingDiagnostics.FailureCount");
					String avgResponseTime = ""+PingMap.get("InternetGatewayDevice.IPPingDiagnostics.AverageResponseTime");
					String minResponseTime = ""+PingMap.get("InternetGatewayDevice.IPPingDiagnostics.MinimumResponseTime");
					String maxResponseTime = ""+PingMap.get("InternetGatewayDevice.IPPingDiagnostics.MaximumResponseTime");
					long FailureCount = StringUtil.getLongValue(PingMap.get("InternetGatewayDevice.IPPingDiagnostics.FailureCount"));
					long PackageCount = StringUtil.getIntegerValue(column3);
					String packetLossRate = percent(FailureCount, PackageCount);
					String iPOrDomainName =column2;
					
					map.put("result", "0");
					map.put("resultDesc", "成功");
					map.put("DevSn",devSn);
					map.put("SuccesNum",succesNum);
					map.put("FailNum",failNum);
					map.put("AvgResponseTime",avgResponseTime);
					map.put("MinResponseTime",minResponseTime);
					map.put("MaxResponseTime",maxResponseTime);
					map.put("PacketLossRate",packetLossRate);
					map.put("IPOrDomainName",iPOrDomainName);
					logger.warn("PingDiagnostic==>ReturnXml:"+map.toString());
					return map;
				}
			}
		}
	}
	
	public String percent(long p1, long p2) {
		double p3;
		if (p2 == 0) {
			return "N/A";
		} else {
			p3 = (double) p1 / p2;
		}
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		String str = nf.format(p3);
		return str;
	}
	
	
	//采集通道
	private Map<String, List<String>> gatherNetIJList(String deviceId)
	{
		acsCorba = new ACSCorba(gw_type);
		HashMap<String, List<String>> ijMap = new HashMap<String, List<String>>();
		List<String> ijInternetList = new ArrayList<String>();
		List<String> ijVoipList = new ArrayList<String>();
		List<String> ijTrzsnList = new ArrayList<String>();
		List<String> ijOtherList = new ArrayList<String>();
		
		// "1.1;DHCP_Routed;45;TR069","3.1;Bridged;43;OTHER","4.1;DHCP_Routed;42;VOIP","5.1;PPPoE_Routed;312;INTERNET"
		// 获取不到走ijk
		logger.warn("走ijk采集", deviceId);
		String wanConnPath = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
		String wanServiceList = ".X_CT-COM_ServiceList";
		String wanPPPConnection = ".WANPPPConnection.";
		String wanIPConnection = ".WANIPConnection.";
		ArrayList<String> wanConnPathsList = null;
		wanConnPathsList =acsCorba.getParamNamesPath(deviceId, wanConnPath, 0);
		if (wanConnPathsList == null || wanConnPathsList.size() == 0
				|| wanConnPathsList.isEmpty())
		{
			logger.warn("[{}]获取WANConnectionDevice下所有节点路径失败，逐层获取", deviceId);
			wanConnPathsList = new ArrayList<String>();
			List<String> jList =acsCorba.getIList(deviceId, wanConnPath);
			if (null == jList || jList.size() == 0 || jList.isEmpty())
			{
				logger.warn("[{}]获取" + wanConnPath + "下实例号失败", deviceId);
			}
			else
			{
				for (String j : jList)
				{
					List<String> kPPPList = acsCorba.getIList(deviceId, wanConnPath
							+ j + wanPPPConnection);
					if (null == kPPPList || kPPPList.size() == 0 || kPPPList.isEmpty())
					{
						logger.warn("[{}]获取" + wanConnPath + wanConnPath + j
								+ wanPPPConnection + "下实例号失败", deviceId);
					}
					else
					{
						for (String kppp : kPPPList)
						{
							wanConnPathsList.add(wanConnPath + j + wanPPPConnection
									+ kppp + wanServiceList);
						}
					}
				}
			}
		}
		ArrayList<String> serviceListList = new ArrayList<String>();
		ArrayList<String> paramNameList = new ArrayList<String>();
		for (int i = 0; i < wanConnPathsList.size(); i++)
		{
			String namepath = wanConnPathsList.get(i);
			if (namepath.indexOf(wanServiceList) >= 0)
			{
				serviceListList.add(namepath);
				paramNameList.add(namepath);
				continue;
			}
		}
		if (serviceListList.size() == 0 || serviceListList.isEmpty())
		{
			logger.warn("[{}]不存在WANIP下的X_CT-COM_ServiceList节点，返回", deviceId);
		}
		else
		{
			String[] paramNameArr = new String[paramNameList.size()];
			int arri = 0;
			for (String paramName : paramNameList)
			{
				paramNameArr[arri] = paramName;
				arri = arri + 1;
			}
			Map<String, String> paramValueMap = new HashMap<String, String>();
			for (int k = 0; k < (paramNameArr.length / 20) + 1; k++)
			{
				String[] paramNametemp = new String[paramNameArr.length - (k * 20) > 20 ? 20
						: paramNameArr.length - (k * 20)];
				for (int m = 0; m < paramNametemp.length; m++)
				{
					paramNametemp[m] = paramNameArr[k * 20 + m];
				}
				Map<String, String> maptemp = acsCorba.getParaValueMap(deviceId,
						paramNametemp);
				if (maptemp != null && !maptemp.isEmpty())
				{
					paramValueMap.putAll(maptemp);
				}
				logger.warn("获取节点值...");
				logger.warn("k : " + k);
			}
			if (paramValueMap.isEmpty())
			{
				logger.warn("[{}]获取ServiceList失败", deviceId);
			}
			for (Map.Entry<String, String> entry : paramValueMap.entrySet())
			{
				logger.debug("[{}]{}={} ",
						new Object[] { deviceId, entry.getKey(), entry.getValue() });
				String paramName = entry.getKey();
				if (paramName.indexOf(wanPPPConnection) >= 0)
				{
				}
				else if (paramName.indexOf(wanIPConnection) >= 0)
				{
					continue;
				}
				if (paramName.indexOf(wanServiceList) >= 0)
				{
					if (!StringUtil.IsEmpty(entry.getValue())
							&& entry.getValue().indexOf("OTHER") >= 0)
					{
						logger.warn("param path is .." + entry.getKey());
						String res = entry.getKey().substring(0,
								entry.getKey().indexOf("X_CT-COM_ServiceList"));
						try
						{
							String i = res.split("WANConnectionDevice.")[1]
									.split(".WANPPPConnection")[0];
							String j = res.split("WANPPPConnection.")[1].split("\\.")[0];
							ijOtherList.add((i + "##" + j));
							logger.warn("i is : " + i + " j is : " + j);
						}
						catch (Exception e)
						{
						}
					}
					
					if (!StringUtil.IsEmpty(entry.getValue())
							&& entry.getValue().indexOf("INTERNET") >= 0)
					{
						logger.warn("param path is .." + entry.getKey());
						String res = entry.getKey().substring(0,
								entry.getKey().indexOf("X_CT-COM_ServiceList"));
						try
						{
							String i = res.split("WANConnectionDevice.")[1]
									.split(".WANPPPConnection")[0];
							String j = res.split("WANPPPConnection.")[1].split("\\.")[0];
							ijInternetList.add((i + "##" + j));
							logger.warn("i is : " + i + " j is : " + j);
						}
						catch (Exception e)
						{
						}
					}
				}
			}
		}
		ijMap.put("INTERNET", ijInternetList);
		ijMap.put("OTHER", ijOtherList);
		return ijMap;
	}
	
	
		public Map<String,String> getDefaultdiag(){
		
		PrepareSQL psql = new PrepareSQL("select column1,column2,column3,column4 from tab_diag_default where default_type_id=1");
		
		List<Map> list = DataSetBean.executeQuery(psql.getSQL(), null);
		
		Map<String,String> map = new HashMap<String, String>();
		
		if(null!=list && list.size()>0){
			map.put("column1", StringUtil.getStringValue(list.get(0).get("column1")));
			map.put("column2", StringUtil.getStringValue(list.get(0).get("column2")));
			map.put("column3", StringUtil.getStringValue(list.get(0).get("column3")));
			map.put("column4", StringUtil.getStringValue(list.get(0).get("column4")));
		}
		return map;
	}
	
	private String getStr(String str){
		if(!StringUtil.IsEmpty(str)){
			return str.substring(0, str.length()-1);
		}else{
			return "";
		}
	}

}
