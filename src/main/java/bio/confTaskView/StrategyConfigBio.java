/**
 * 终端业务下发业务逻辑
 */
package bio.confTaskView;

import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.StaticTypeCommon;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.ConstantClass;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.init.bio.AppInitBIO;
import com.linkage.module.gwms.obj.tabquery.HgwCustObj;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.gwms.util.corba.obj.PreServInfoOBJ;

import dao.confTaskView.StrategyConfigDao;

/**
 * @author ASUS E-mail：qixq@lianchuang.com
 * @version $Revision$
 * @since 2009-3-12
 * @category dao.confTaskView
 * 
 */
public class StrategyConfigBio {

	private static final Logger LOG = LoggerFactory.getLogger(StrategyConfigBio.class);

	// 1-1设备
	private String NAVIGATOR11 = "Navigator1-1";

	// 1-2设备
	private String NAVIGATOR12 = "Navigator1-2";

	// 2-1设备
	private String NAVIGATOR21 = "Navigator2-1";

	// 2-2设备
	private String NAVIGATOR22 = "Navigator2-2";

	/**
	 * db
	 */
	private StrategyConfigDao strategyConfigDao;

	/**
	 * @return the strategyConfigDao
	 */
	public StrategyConfigDao getStrategyConfigDao() {
		return strategyConfigDao;
	}

	/**
	 * @param strategyConfigDao the strategyConfigDao to set
	 */
	public void setStrategyConfigDao(StrategyConfigDao strategyConfigDao) {
		this.strategyConfigDao = strategyConfigDao;
	}
	
	/**
	 * 家庭网关现场安装之后，业务开始下发
	 * 
	 * @param userId
	 * @param userId
	 * 
	 * @return String
	 */
	public String itmsBindStrategyConfigRun(long accOid, String username,String userId,
			String deviceId, String gw_type) {

		LOG.debug("bindStrategyConfigRun({},{})", username, deviceId);
		
		List usernameList = strategyConfigDao.getBssSheetId(username);
		String bssSheetId = "";
		boolean hasflagStrategy = false;
		if(0<usernameList.size()){
			bssSheetId = String.valueOf(((Map)usernameList.get(0)).get("bss_sheet_id")).toString();
		}
		
		// 不需要业务下发，则直接返回
		if (!strategyConfigDao.isDoBusiness()) {

			LOG.warn("不需要业务下发，直接返回！");
			return "不需要业务下发";
		}

		//判断有没有配置具体的业务，如没有则返回
		String param = strategyConfigDao.getDoBusinessParam();
		if("".equals(param)||null==param){
			LOG.warn("没有配置任何的具体业务，直接返回！");
			return "没有配置任何的具体业务";
		}
		
		Map userInfoMap = strategyConfigDao.getItmsUserInfo(username,userId);
		
		//无法获取业务用户，则直接返回
		if(null==userInfoMap){
			LOG.warn("无法获取业务用户，直接返回！");
			return "无法获取业务用户";
		}
		
		//取出具体的设备信息
		Map deviceInfoMap = strategyConfigDao.getDeviceInfo(deviceId);
		// 0为立即执行
		int type = 0;
		
		//取出具体的业务
		String[] doBusinessParam = param.split(",");
		
		//执行单PVC业务
		if(0<doBusinessParam.length){
			
			String pvcParam = doBusinessParam[0];
			
			if("1".equals(pvcParam)){
				
				//生成策略ID
				String[] idArr = new String[1];
				idArr[0] = StaticTypeCommon.generateId();
				String wan_type = String.valueOf(userInfoMap.get("wan_type")).toString();
				String vpiid = String.valueOf(userInfoMap.get("vpiid")).toString();
				String vciid = String.valueOf(userInfoMap.get("vciid")).toString();
				String passwd = String.valueOf(userInfoMap.get("passwd")).toString();
				String ipaddress = String.valueOf(userInfoMap.get("ipaddress")).toString();
				String ipmask = String.valueOf(userInfoMap.get("ipmask")).toString();
				String gateway = String.valueOf(userInfoMap.get("gateway")).toString();
				String adsl_ser = String.valueOf(userInfoMap.get("adsl_ser")).toString();
				String bind_port = String.valueOf(userInfoMap.get("bind_port")).toString();
				
				int serviceId = -1;
				if ("1".equals(wan_type)) {
					//桥接
					serviceId = 1001;
				} else if ("2".equals(wan_type)) {
					//路由
					serviceId = 1008;
				} else if ("3".equals(wan_type)) {
					//静态IP
					serviceId = 1010;
				} else if ("4".equals(wan_type)) {
					LOG.warn("device_serialnumber:{}暂时不支持DHCP",deviceInfoMap.get("device_serialnumber"));
				} else {
					LOG.warn("device_serialnumber:{}无法匹配serviceId",deviceInfoMap.get("device_serialnumber"));
				}
					
				if(-1!=serviceId){
					
					StringBuffer sheetPara = new StringBuffer();
					
					sheetPara.append("PVC:");
					sheetPara.append(vpiid);
					sheetPara.append("/");
					sheetPara.append(vciid);
					
					if (1008==serviceId) {
						//路由(参数顺序是PVC,username,password,绑定端口)
						sheetPara.append("|||" + username + "|||" + passwd);
					} else if (1010==serviceId) {
						//静态IP(参数顺序是PVC,ipaddress,ipmask,gateway,adsl_ser,绑定端口)
						sheetPara.append("|||" + ipaddress + "|||" + ipmask + "|||" + gateway + "|||" + adsl_ser);
					}
					
					//绑定端口
					if (null == bind_port || "".equals(bind_port)) {
						sheetPara.append("|||InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.1,InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.3,InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.4");
					} else {
						sheetPara.append("|||");
						sheetPara.append(bind_port);
					}

					strategyConfigDao.doStratery(idArr[0], accOid, deviceId, sheetPara.toString(),
							serviceId, username, type, deviceInfoMap);
					
					if(!"".equals(bssSheetId) && null!=bssSheetId){
						strategyConfigDao.insertGwStrategySheet(idArr[0],bssSheetId);
						hasflagStrategy = true;
					}
					//通知预读模块
					invokePreProcess(idArr, gw_type);
				}
			}
		}
		
		if(1<doBusinessParam.length){
			//调预读模块,判断是否需要做VOIP下发
			String voipParam = doBusinessParam[1];
			if("1".equals(voipParam)){
				HgwCustObj hgwCustObj = getCustObjByDevId(deviceId);
				if(null != hgwCustObj){
					PreServInfoOBJ preInfoObj = new PreServInfoOBJ(StringUtil
							.getStringValue(hgwCustObj.getUserId()), deviceId,
							hgwCustObj.getOui(), hgwCustObj.getDeviceSerial(), StringUtil
									.getStringValue(""), StringUtil
									.getStringValue(ConstantClass.SERV_OPEN));
					LOG.warn("deviceId:"+deviceId+"调PreProcess");
					int _flag = CreateObjectFactory.createPreProcess(gw_type).processServiceInterface(
							CreateObjectFactory.createPreProcess().GetPPBindUserList(preInfoObj));
					if(1==_flag){
						LOG.warn("deviceId:"+deviceId+"调PreProcess成功");
					}else{
						LOG.warn("deviceId:"+deviceId+"调PreProcess失败");
					}
				}
			}
		}
		
		if(!"".equals(bssSheetId) && null!=bssSheetId){
		
			if(hasflagStrategy){
				strategyConfigDao.updateTabBssSheet(bssSheetId,"2");
			}else{
				strategyConfigDao.updateTabBssSheet(bssSheetId,"1");
			}
		}
		
		return "下发成功";
	}
	
	/**
	 * 商务领航现场安装之后，业务开始下发
	 * 
	 * @param userId
	 * @param userId
	 * 
	 * @return String
	 */
	public String bbmsBindStrategyConfigRun(long accOid, String username,
			String deviceId, String gw_type) {

		LOG.debug("bindStrategyConfigRun({},{})", username, deviceId);
		
		List usernameList = strategyConfigDao.getBssSheetId(username);
		String bssSheetId = "";
		boolean hasflagStrategy = false;
		
		//判断是否需要下发业务，主要是根据前提条件判断，如果不入，最终也要更新工单表
		boolean isGo = true;
		
		if(0<usernameList.size()){
			bssSheetId = String.valueOf(((Map)usernameList.get(0)).get("bss_sheet_id")).toString();
		}
		
		// 不需要业务下发，则直接返回
		if (!strategyConfigDao.isDoBusiness()) {

			LOG.warn("不需要业务下发，直接返回！");
			isGo = false;
		}

		//判断有没有配置具体的业务，如没有则返回
		String param = strategyConfigDao.getDoBusinessParam();
		if("".equals(param)||null==param){
			LOG.warn("没有配置任何的具体业务，直接返回！");
			isGo = false;
		}
		
		int userId = strategyConfigDao.getBbmsUserId(username);
		
		//无法获取业务用户，则直接返回
		if(-1==userId){
			LOG.warn("无法获取业务用户，直接返回！");
			isGo = false;
		}
		
		//取出具体的设备信息
		Map deviceInfoMap = strategyConfigDao.getDeviceInfo(deviceId);
		
		String vendorId = (null==deviceInfoMap.get("vendor_id"))?"":String.valueOf(deviceInfoMap.get("vendor_id"));
		if("8".equals(vendorId)){
			LOG.warn("该设备为H3C设备，无需下发，直接返回！");
			isGo = false;
		}
		
		// 0为立即执行
		int type = 0;
		
		//取出设备终端类型
		String deviceType = (null==deviceInfoMap.get("device_type"))?"":String.valueOf(deviceInfoMap.get("device_type"));
		
		//取出具体的业务
		String[] doBusinessParam = param.split(",");
		
		//执行单PVC业务
		if(isGo && 0<doBusinessParam.length){
			
			String pvcParam = doBusinessParam[0];
			
			if("1".equals(pvcParam)){
				
				if ("".equals(deviceType) || null == deviceType) {
					LOG.warn("无法获取终端类型！");
				}else{

					//判断该业务是否已经配置
					if(!strategyConfigDao.isAlreadyConfig(deviceId,12)){
						
						//下发单PVC开户工单
						if (strategyConfigDao.isSinglePVC(userId)) {
							if (NAVIGATOR11.equals(deviceType)
									|| NAVIGATOR12.equals(deviceType)) {

									//生成策略ID
									String[] idArr = new String[1];
									idArr[0] = StaticTypeCommon.generateId();
								
									String sheetPara = "";
									int serviceId =  1201;
									strategyConfigDao.doStratery(idArr[0], accOid, deviceId, sheetPara,
											serviceId, username, type, deviceInfoMap);
									
									if(!"".equals(bssSheetId) && null!=bssSheetId){
										strategyConfigDao.insertGwStrategySheet(idArr[0],bssSheetId);
										hasflagStrategy = true;
									}
									
									//通知预读模块
									invokePreProcess(idArr,gw_type);
								}
						} else {
							LOG.warn("支持多PVC,无需下发单PVC策略！");
						}
						
					}else{
						LOG.warn("该设备已配置过单PVC业务！");
					}
					
				}
			}else{
				LOG.warn("单PVC业务配置为不需要下发，直接返回！");
			}
		}
		
		//执行多终端业务
		if(isGo && 1<doBusinessParam.length){
			
			String maxUserParam = doBusinessParam[1];
			
			if("1".equals(maxUserParam)){
				
				if ("".equals(deviceType) || null == deviceType) {
					LOG.warn("无法获取终端类型！");
				}else{
					
					if (NAVIGATOR12.equals(deviceType)
							|| NAVIGATOR21.equals(deviceType)
							|| NAVIGATOR22.equals(deviceType)) {

						//生成策略ID
						String[] idArr = new String[1];
						idArr[0] = StaticTypeCommon.generateId();
						
						int maxUserNumber = strategyConfigDao
								.getChildDeviceTotal(userId);

						StringBuffer sheetPara = new StringBuffer();
						sheetPara.append("1|||");
						sheetPara.append(maxUserNumber);

						int serviceId = 101;

						strategyConfigDao.doStratery(idArr[0], accOid, deviceId, sheetPara
								.toString(), serviceId, username, type, deviceInfoMap);
						
						if(!"".equals(bssSheetId) && null!=bssSheetId){
							strategyConfigDao.insertGwStrategySheet(idArr[0],bssSheetId);
							hasflagStrategy = true;
						}
						
						invokePreProcess(idArr, gw_type);
					}
				}
				
			}else{
				LOG.warn("多终端业务配置为不需要下发，直接返回！");
			}
		}
		
		//执行IPTV业务
		if(isGo && 2<doBusinessParam.length){
			
			String iptvParam = doBusinessParam[2];
			
			if("1".equals(iptvParam)){
				
				int serviceId = 1101;
				
				//判断是否已经配置该业务
				if(!strategyConfigDao.isAlreadyConfigIPTV(deviceId,serviceId)){
					
					//生成策略ID
					String[] idArr = new String[1];
					idArr[0] = StaticTypeCommon.generateId();

					String LAN2 = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.2";
					int vpi = 8;
					int vci = 85;
					StringBuffer sheetParam = new StringBuffer();
					sheetParam.append("PVC:");
					sheetParam.append(vpi);
					sheetParam.append("/");
					sheetParam.append(vci);
					sheetParam.append("|||");
					sheetParam.append(LAN2);
					
					strategyConfigDao.doStratery(idArr[0], accOid, deviceId, sheetParam.toString(), serviceId, username, type, deviceInfoMap);
					
					if(!"".equals(bssSheetId) && null!=bssSheetId){
						strategyConfigDao.insertGwStrategySheet(idArr[0],bssSheetId);
						hasflagStrategy = true;
					}
					
					invokePreProcess(idArr, gw_type);
					
				}else{
					LOG.warn("该设备已配置过IPTV业务！");
				}
			}else{
				LOG.warn("IPTV业务配置为不需要下发，直接返回！");
			}
		}
		
		//执行ITMS2.0业务
		if(isGo && 3<doBusinessParam.length){
			
			String itms2Param = doBusinessParam[3];
			
			if("1".equals(itms2Param)){
				
				//生成策略ID
				String[] idArr = new String[1];
				idArr[0] = StaticTypeCommon.generateId();

				//TODO
				String sheetPara = null;
				//TODO
				int serviceId = -1;

				strategyConfigDao.doStratery(idArr[0], accOid, deviceId, sheetPara
						.toString(), serviceId, username, type, deviceInfoMap);
				
				if(!"".equals(bssSheetId) && null!=bssSheetId){
					strategyConfigDao.insertGwStrategySheet(idArr[0],bssSheetId);
					hasflagStrategy = true;
				}
				
				invokePreProcess(idArr, gw_type);
				
			}else{
				LOG.warn("ITMS2.0业务配置为不需要下发，直接返回！");
			}
		}

		if(!"".equals(bssSheetId) && null!=bssSheetId){
		
			if(hasflagStrategy){
				strategyConfigDao.updateTabBssSheet(bssSheetId,"2");
			}else{
				strategyConfigDao.updateTabBssSheet(bssSheetId,"1");
			}
		}
		
		return "下发成功";
	}
	
	/**
	 * 开始调用预读模块
	 * @param invokeStruct
	 */
	@SuppressWarnings("unused")
	public void invokePreProcess(String[] idArr, String gw_type) {
		
		LOG.debug("invokePreProcess({})", idArr);
		
		if(null != idArr){
			try{
//				if (Global.GW_TYPE_ITMS.equals(gw_type)) {
//					Global.G_PPManager_itms.processOOBatch(idArr);
//				} else {
//					Global.G_PPManager_bbms.processOOBatch(idArr);
//				}
				CreateObjectFactory.createPreProcess(gw_type).processOOBatch(idArr);
			}catch(Exception e){
				LOG.warn("rebind pp corba");
				try{
					AppInitBIO.initPreProcess(gw_type);
//					if (Global.GW_TYPE_ITMS.equals(gw_type)) {
//						Global.G_PPManager_itms.processOOBatch(idArr);
//					} else {
//						Global.G_PPManager_bbms.processOOBatch(idArr);
//					}
					CreateObjectFactory.createPreProcess(gw_type).processOOBatch(idArr);
				}catch(Exception e2){
					LOG.error("rebind pp corba fail");
					e2.printStackTrace();
				}
			}
		}
	}
	
	
	/**
	 * 获取业务用户ID,根据设备ID
	 * 
	 * @param
	 * @author Jason(3412)
	 * @date 2009-6-26
	 * @return String
	 */
	public HgwCustObj getCustObjByDevId(String deviceId) {
		LOG.debug("getCustObjByDevId({})", deviceId);
		HgwCustObj hgwCustObj = null;
		
		String strSQL = "select * from tab_hgwcustomer where device_id=?";

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			strSQL = "select user_id, device_id, oui, device_serialnumber, city_id, office_id from tab_hgwcustomer where device_id=?";
		}

		PrepareSQL psql = new PrepareSQL(strSQL);
		psql.setString(1, deviceId);
		
		Map rMap = DataSetBean.getRecord(psql.getSQL());
		if (null != rMap && false == rMap.isEmpty()) {
			hgwCustObj = new HgwCustObj();
			hgwCustObj.setUserId(StringUtil.getStringValue((rMap.get("user_id"))));
			hgwCustObj.setDeviceId(StringUtil.getStringValue((rMap.get("device_id"))));
			hgwCustObj.setOui(StringUtil.getStringValue((rMap.get("oui"))));
			hgwCustObj.setDeviceSerial(StringUtil.getStringValue((rMap.get("device_serialnumber"))));
			hgwCustObj.setCityId(StringUtil.getStringValue((rMap.get("city_id"))));
			hgwCustObj.setOfficeId(StringUtil.getStringValue((rMap.get("office_id"))));
		}
		return hgwCustObj;
	}
	
	
}
