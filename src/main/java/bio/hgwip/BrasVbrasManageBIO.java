package bio.hgwip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

import dao.hgwip.BrasVbrasManageDAO;
/**
 * 
 * @author fanjm (Ailk No.35572)
 * @version 1.0
 * @since 2017年2月20日
 *
 */
public class BrasVbrasManageBIO {
	
	//日志
	private static final Logger LOG = LoggerFactory.getLogger(BrasVbrasManageBIO.class);
	
	//brasVbras对应DAO
	BrasVbrasManageDAO brasVbrasManageDAO ;
	
	/**
	 * 用户新增网络地址（只能省局用户使用）
	 * 
	 * @param ipAdr
	 *            用户输入的ip
	 * @param netMaskLen
	 *            子网掩码位数
	 * @param netMask
	 *            子网掩码
	 * @param brasType
	 *            BRAS/VBRAS
	 * @param city
	 *            属地
	 * @return -1:参数错误 0：成功 -2:地址已存在 -3:数据库操作失败
	 */
	public int addBras(String ipAdr, int netMaskLen, String netMask, String brasType, String city) {
		LOG.debug("addBras()==>方法开始{}",new Object[]{ipAdr,netMaskLen,netMask,brasType,city});
		String subnet = IpTool.getLowAddr(ipAdr, netMaskLen);
		if ("".equals(subnet)) {
			LOG.error("addBras()==>方法结束,参数错误，返回{}",IPGlobal.PARAM_ERROR);
			return IPGlobal.PARAM_ERROR;
		}

		String subnetGrp = "root";
		Subnet subnetObj = new Subnet(subnetGrp, netMaskLen, subnet);
		subnetObj.setAssignTime(StringUtil.getStringValue(new DateTimeUtil().getLongTime()));
		subnetObj.setCity_id(city);
		subnetObj.setComment("");
		subnetObj.setGrandGrp("");
		subnetObj.setIgroupMask(0);

		// 地址冲突，不能完成增加
		if (brasVbrasManageDAO.isConfigSubnet(subnetObj)) {
			LOG.error("addBras()==>方法结束,地址冲突，返回{}",IPGlobal.SUBNET_EXIST);
			return IPGlobal.SUBNET_EXIST;
		}
		
		//查询同属地、vbras类型的网段已经存在的数量
		int exNum = brasVbrasManageDAO.qryCountByVbrasCity(brasType, city);

		//BRAS/VBRAS名称:VBRAS1等
		String name = ("BRAS".equals(brasType)?"Bras":"VBras")+StringUtil.getStringValue(exNum+1);
		int res = brasVbrasManageDAO.addBras(subnetObj, name, brasType);
		
		LOG.debug("addBras()==>方法结束{}",res);
		return res;

	}

	/**
	 * 根据IP查询vbras bras结果集合
	 * @param ip 查询条件vbras bras
	 * @return vbras bras结果集合
	 */
	public List<Map<String, String>> getBrasList(String ip) {
		LOG.debug("getBrasList()==>方法开始{}",ip);
		String fip = "";
		if(!StringUtil.IsEmpty(ip)){
			fip = IpTool.getFillIP(ip);
		}
		
		List<Map<String, String>> res = new ArrayList<Map<String, String>>();
		res = brasVbrasManageDAO.getBrasList(fip);
		
		LOG.debug("getBrasList()==>方法结束{}", res);
		return res;
	}
	
	
	
	/**
	 * 删除Vbras/Bras网段
	 * @param ip 网段地址
	 * @param netMask 子网掩码
	 * @return 删除结果:-1 参数错误 -3 数据库操作失败 0成功
	 */
	public int delVbras(String ip, String netMask) {
		return brasVbrasManageDAO.delVbras(ip, netMask);
	}
	
	
	/**
	 * 统计Vbras/Bras网段
	 * @param city 属地
	 * @return 设备Vbras/Bras属地统计结果集合
	 */
	public List<Map<String, String>> brasState(String city) {
		LOG.debug("brasState()==>方法开始{}",city);
		List<Map<String, String>> cityList = new ArrayList<Map<String, String>>();
		if("00".equals(city)||StringUtil.IsEmpty(city)){
			//一级地市+省中心的List
			cityList = (ArrayList<Map<String, String>>) CityDAO.getNextCityListByCityPid("00");
		}else{
			Map<String, String> map= new HashMap<String, String>();
			map.put("city_id", city);
			cityList.add(map);
		}
		
		
		List<Map<String, String>> daoList = brasVbrasManageDAO.brasState(city);
		List<Map<String, String>> list= new ArrayList<Map<String,String>>(cityList.size());
		String[] types = new String[]{"BRAS","VBRAS"};
		int[] rootNum = new int[]{0,0}; //对应存放总地区的BRAS和VBRAS数目
				
		//遍历一级地市(包括省中心)
		for(Map<String, String> cityMap:cityList){
			String city_id = cityMap.get("city_id");
			Map<String, String> oneLine = new HashMap<String, String>();
			oneLine.put("cityId", city_id);
			String cityName = CityDAO.getNextCityMapByCityPid("00").get(city_id);
			oneLine.put("cityName", cityName);
			oneLine.put("BRASNUM", "0");
			oneLine.put("VBRASNUM", "0");
			
			//每个属地构造2个type
			for(int j=0;j<types.length;j++){
				String tmpType = types[j];
				oneLine.put("type", tmpType);
				//遍历dao全部查询结果，获取每个type的数量
				for(int i=0; i<daoList.size(); i++){
					Map<String,String> daoMap = daoList.get(i);
					if(city_id.equals(daoMap.get("cityId")) && tmpType.equals(daoMap.get("type"))){
						oneLine.put(tmpType+"NUM", daoMap.get("num"));
						rootNum[j] += StringUtil.getIntegerValue(daoMap.get("num"));
						break;
					}
					//dao的结果中没有符合属地 type条件的，赋值0个
					else if(i==daoList.size()-1){
						oneLine.put(tmpType+"NUM", "0");
					}
				}
			}
			
			list.add(oneLine);
		}
		for(int i=0;i<list.size();i++){
			if("00".equals(list.get(i).get("cityId"))){
				list.get(i).put("BRASNUM", StringUtil.getStringValue(rootNum[0]));
				list.get(i).put("VBRASNUM", StringUtil.getStringValue(rootNum[1]));
				break;
			}
		}
		LOG.debug("brasState()==>方法结束{}",list);
		return list;
	}
	
	
	public BrasVbrasManageDAO getBrasVbrasManageDAO() {
		return brasVbrasManageDAO;
	}

	public void setBrasVbrasManageDAO(BrasVbrasManageDAO brasVbrasManageDAO) {
		this.brasVbrasManageDAO = brasVbrasManageDAO;
	}
	
	


}
