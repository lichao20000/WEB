package com.linkage.module.gtms.stb.resource.serv;

import java.util.List;
import java.util.Map;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gtms.stb.resource.dao.StbUpgradeVersionDAO;
import com.linkage.module.gwms.Global;

/**
 * 
 * @author wangyan10(Ailk NO.76091)
 * @version 1.0
 * @since 2018-1-3
 */
public class StbUpgradeVersionBIO
{
	private String instArea=Global.instAreaShortName;
	
	/** 下发查询用dao */
	private StbUpgradeVersionDAO dao;

	public StbUpgradeVersionDAO getDao() {
		return dao;
	}

	public void setDao(StbUpgradeVersionDAO dao) {
		this.dao = dao;
	}

	/**
	 * 获取列表
	 * @param
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param vendorId
	 * @param deviceModelId
	 * @param source_devicetypeId
	 * @param goal_devicetypeId
	 * @param upgradeType
	 * @param belong
	 * @param hardwareversion 
	 * @return
	 */
	public List<Map<String, String>> getStbUpgradeTempList(
			int curPage_splitPage, int num_splitPage, String vendorId,
			String deviceModelId, String source_devicetypeId,
			String goal_devicetypeId, String upgradeType, String belong, String hardwareversion) {
		return dao.getStbUpgradeTempList(curPage_splitPage, num_splitPage, vendorId,
				deviceModelId, source_devicetypeId, goal_devicetypeId, upgradeType, belong,hardwareversion);
	}
	
	public List<Map<String, String>> getStbUpgradeTempList_hnlt(int curPage_splitPage,
		int num_splitPage,String vendorId,String deviceModelId,String source_devicetypeId,
		String goal_devicetypeId,String upgradeType,String belong,String hardwareversion,String valid)
	{
		return dao.getStbUpgradeTempList_hnlt(curPage_splitPage,num_splitPage,vendorId,
				deviceModelId,source_devicetypeId,goal_devicetypeId,upgradeType,
				belong,hardwareversion,valid);
	}

	/**
	 * 获取总数
	 * @param
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param vendorId
	 * @param deviceModelId
	 * @param source_devicetypeId
	 * @param goal_devicetypeId
	 * @param upgradeType
	 * @param belong
	 * @param hardwareversion 
	 * @return
	 */
	public int countAdverResultList(int curPage_splitPage, int num_splitPage,
			String vendorId, String deviceModelId, String source_devicetypeId,
			String goal_devicetypeId, String upgradeType, String belong, String hardwareversion) {
		return dao.countAdverResultList(curPage_splitPage, num_splitPage, vendorId,
				deviceModelId, source_devicetypeId, goal_devicetypeId, upgradeType, belong,hardwareversion);
	}
	
	public int countAdverResultList_hnlt(int curPage_splitPage, int num_splitPage,String vendorId, 
		String deviceModelId, String source_devicetypeId,String goal_devicetypeId, 
		String upgradeType, String belong, String hardwareversion,String valid) 
	{
		return dao.countAdverResultList_hnlt(curPage_splitPage, num_splitPage, vendorId,
				deviceModelId, source_devicetypeId, goal_devicetypeId, upgradeType, 
				belong,hardwareversion,valid);
	}

	/**
	 * 删除版本映射
	 * @param
	 * @param source_devicetypeId
	 * @return
	 */
	public String deleteUpgradeTemp(String source_devicetypeId) {
		int ier = dao.deleteUpgradeTemp(source_devicetypeId);
		if (ier >= 1) {
			return "1";
		} else {
			return "0";
		}
	}
	

	public String deleteUpgradeTemp_hnlt(String belong,String devicetype_id_old,
			String devicetype_id_new,String temp_id) {
		int ier = dao.deleteUpgradeTemp_hnlt(belong,devicetype_id_new,devicetype_id_old,temp_id);
		if (ier >= 1) {
			return "1";
		} else {
			return "0";
		}
	}
	
	/**
	 * 修改版本映射
	 * @param
	 * @param source_devicetypeId
	 * @param goal_devicetypeId
	 * @param platformId 
	 * @return
	 */
	public String modifyUpgradeTemp(String source_devicetypeId, String goal_devicetypeId, 
			String platformId) {
		int ier = dao.modifyUpgradeTemp(source_devicetypeId ,goal_devicetypeId,platformId);
		if (ier == 1) {
			return "1";
		} else {
			return "0";
		}
	}
	
	public String modifyUpgradeTemp_hnlt(String source_devicetypeId, String goal_devicetypeId, 
			String platformId,String valid)
	{
		int ier = dao.modifyUpgradeTemp_hnlt(source_devicetypeId ,goal_devicetypeId,platformId,valid);
		if (ier == 1) {
			return "1";
		} else {
			return "0";
		}
	}

	/**
	 * 插入版本映射
	 * @param
	 * @param vendorId
	 * @param deviceModelId
	 * @param source_devicetypeId
	 * @param goal_devicetypeId
	 * @param tempId
	 * @param belong
	 * @return
	 */
	public String addUpgradeTemp(String vendorId, String deviceModelId,
			String source_devicetypeId, String goal_devicetypeId,
			String tempId, String belong) {
		int ier = dao.addUpgradeTemp(vendorId,deviceModelId, source_devicetypeId, goal_devicetypeId, tempId, belong);
		if (ier == 1) {
			return "1";
		} else {
			return "0";
		}
	}
	
	public String addUpgradeTemp_hnlt(String vendorId, String deviceModelId,
			String source_devicetypeId, String goal_devicetypeId,
			String tempId, String belong,String valid) {
		int ier = dao.addUpgradeTemp_hnlt(vendorId,deviceModelId,source_devicetypeId,
				goal_devicetypeId,tempId,belong,valid);
		if (ier == 1) {
			return "1";
		} else {
			return "0";
		}
	}

	/**
	 * 获取总数
	 * @param
	 * @return
	 */
	public int getQueryCount() {
		return dao.getQueryCount();
	}

	public String addUpgradeFilePath(String pathId, String vendorId,
			String deviceModelId, String goal_devicetypeId) {
		int ier = dao.addUpgradeFilePath(pathId,vendorId,deviceModelId, goal_devicetypeId);
		if (ier == 1) {
			return "1";
		} else {
			return "0";
		}
	}
	
	public String addUpgradeFilePath_hnlt(String vendorId,String deviceModelId,String goal_devicetypeId,
			String pathId) 
	{
		int i=dao.getIsHave(pathId,goal_devicetypeId);
		if(i>0){
			return "-1";
		}
		
		int ier = dao.addUpgradeFilePath(pathId,vendorId,deviceModelId,goal_devicetypeId);
		if (ier == 1) {
			return "1";
		} 
		
		return "0";
	}

	public String deleteUpgradeFilePath(String pathId, String deviceModelId,
			String goal_devicetypeId) {
		int ier = dao.deleteUpgradeFilePath(pathId,deviceModelId, goal_devicetypeId);
		if (ier >= 1) {
			return "1";
		} else {
			return "0";
		}
	}
	
	public String deleteUpgradeFilePath(String pathId, String deviceModelId,
			String goal_devicetypeId,String vendorId) {
		int ier = dao.deleteUpgradeFilePath(pathId,deviceModelId,goal_devicetypeId,vendorId);
		if (ier >= 1) {
			return "1";
		} else {
			return "0";
		}
	}

	public String modifyUpgradeFilePath(String vendorId,String pathId, String deviceModelId,
			String goal_devicetypeId, String pathIdNew) 
	{
		if(Global.HNLT.equals(instArea)){
			if(0<dao.getIsHave(pathIdNew,goal_devicetypeId)){
				return "-1";
			}
			if(0==dao.getIsHave(pathId,deviceModelId,goal_devicetypeId)){
				int ier = dao.addUpgradeFilePath(pathId,vendorId,deviceModelId,goal_devicetypeId);
				if (ier == 1) {
					return "1";
				} 
			}
		}
		
		int ier = dao.modifyUpgradeFilePath(pathId,deviceModelId,goal_devicetypeId,pathIdNew);
		if (ier >= 1) {
			return "1";
		} 
		return "0";
	}

	public List<Map<String, String>> getStbUpgradeFilePathList(int curPage_splitPage,
			int num_splitPage, String vendorId,String deviceModelId, String goal_devicetypeId) 
	{
		if(Global.HNLT.equals(instArea)){
			return dao.getStbUpgradeFilePathList_hnlt(curPage_splitPage, num_splitPage,goal_devicetypeId);
		}else{
			return dao.getStbUpgradeFilePathList(curPage_splitPage, num_splitPage, vendorId,
					deviceModelId, goal_devicetypeId);
		}
	}

	public int countStbUpgradeFilePathList(int curPage_splitPage,
			int num_splitPage, String vendorId, String deviceModelId,
			String goal_devicetypeId) {
		if(Global.HNLT.equals(instArea)){
			return dao.countStbUpgradeFilePathList_hnlt(curPage_splitPage,num_splitPage,goal_devicetypeId);
		}else{
			return dao.countStbUpgradeFilePathList(curPage_splitPage, num_splitPage, vendorId,
					deviceModelId, goal_devicetypeId);
		}
	}

	public String getPathByModelId(String deviceModelId) {
		List list = dao.getPathByModelId(deviceModelId);
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<list.size();i++){
			Map map = (Map) list.get(i);
			if(i>0){
				bf.append("#");
			}
			bf.append(map.get("id"));
			bf.append("$");
			bf.append(map.get("version_path"));
		}
		return bf.toString();
	}

	public String checkDeviceTypeId(String source_devicetypeId) {
		int ier = dao.checkDeviceTypeId(source_devicetypeId);
		if (ier > 0) {
			return "1";
		} else {
			return "0";
		}
	}

}
