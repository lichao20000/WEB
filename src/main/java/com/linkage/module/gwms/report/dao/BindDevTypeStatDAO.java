package com.linkage.module.gwms.report.dao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.gw.VendorModelVersionDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;

/**
 * 
 * @author yaoli (Ailk No.)
 * @version 1.0
 * @since 2019年3月4日
 * @category com.linkage.module.gwms.report.dao
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class BindDevTypeStatDAO extends SuperDAO
{
//	日志记录
	private static Logger logger = LoggerFactory.getLogger(BindDevTypeStatDAO.class);
	private HashMap<String, String> vendorMap = null;
	private HashMap<String, String> deviceModelMap = null;
	private HashMap<String,List<String>> vendorIdMap = new HashMap<String,List<String>>();
	private HashMap<String,List<String>> deviceModelIdMap = new HashMap<String,List<String>>();
	private HashMap<String,List<String>> hardMap = new HashMap<String,List<String>>();
	private HashMap<String,List<String>> softMap = new HashMap<String,List<String>>();
	private HashMap<String,String> totalMap = new HashMap<String,String>();
	private UserRes user = null;
	String start = null;
	String end = null;
	
	
 
 /**
  * 需求变更，在后台组装table
  * @param curUser
  * @param vendorId
  * @param deviceModelId
  * @param startTime
  * @param endTime
  * @param curPage_splitPage
  * @param num_splitPage
  * @return
  */
public String queryBindDevTypeList(UserRes curUser,String vendorId,
		String deviceModelId,String startTime,String endTime)
{
     logger.debug("queryBindDevTypeList({},{},{},{},{})", 
    		 new Object[]{curUser,vendorId,deviceModelId,startTime,endTime});
		
       String sql = "";
       PrepareSQL psql = null;
       List<String> cityIdList = null;
		 //用户所在省市
       user = curUser;
       start = startTime;
       end = endTime;
       String userCity = curUser.getCityId();
		if(!StringUtil.IsEmpty(userCity) && !"-1".equals(userCity) && !"00".equals(userCity)){
			cityIdList = CityDAO.getAllNextCityIdsByCityPid(userCity);
		} 
		
		vendorMap = VendorModelVersionDAO.getVendorMap();
		deviceModelMap = VendorModelVersionDAO.getDeviceModelMap();
		
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		sql = "select b.vendor_id,b.device_model_id,b.hardwareversion,b.softwareversion,count(*) total,t.DEVICE_VERSION_TYPE "
			    +" from tab_gw_device a,tab_devicetype_info b,tab_hgwcustomer c,gw_device_model d,tab_vendor e ,tab_device_version_attribute t "
				+" where a.devicetype_id = b.devicetype_id and a.device_id=c.device_id "
			    +" and b.device_model_id=d.device_model_id and b.vendor_id=e.vendor_id "
				+" and a.devicetype_id=t.devicetype_id ";
		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			sql = sql + " and b.vendor_id='" + vendorId +"' ";
		}
		if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			sql = sql + " and b.device_model_id='" + deviceModelId +"' ";
		}
		if(null != cityIdList && !cityIdList.isEmpty() && cityIdList.size() > 1){
			sql = sql + " and a.city_id in (" + StringUtils.weave(cityIdList) + ")";
		}
		//入网开始时间
		if(!StringUtil.IsEmpty(startTime)){
			sql = sql + " and a.complete_time>="+startTime;
		}
		//入网结束时间
		if(!StringUtil.IsEmpty(endTime)){
			
			sql = sql + " and a.complete_time<="+endTime;
		}
		
		sql = sql + " group by b.vendor_id,b.device_model_id,b.hardwareversion,b.softwareversion,t.DEVICE_VERSION_TYPE";
		sql = sql + " order by b.vendor_id,b.device_model_id,b.hardwareversion,b.softwareversion";
		psql = new PrepareSQL(sql);
		
		//查询结果
		List<HashMap<String,String>> list = DBOperation.getRecords(psql.getSQL()); 
		
		//组装需要的信息
		for(HashMap<String,String> map : list){
			//30  414  V1.0 Tianyi_V1.0.1 1  0
			//厂商:型号
			String venId = map.get("vendor_id");
			String vendorName = vendorMap.get(venId);
			String devModelId = map.get("device_model_id");
			String deviceMode = deviceModelMap.get(devModelId);
			List<String> deviceModelList  = null;
			if(vendorIdMap.containsKey(vendorName)){
				deviceModelList = vendorIdMap.get(vendorName);
				deviceModelList.add(deviceMode);
				vendorIdMap.put(vendorName, deviceModelList);
			}else{
				deviceModelList = new ArrayList<String>();
				deviceModelList.add(deviceMode);
				vendorIdMap.put(vendorName, deviceModelList);
			}
			//厂商#型号 ： 硬件版本
			String hardwareversion = map.get("hardwareversion");
			String modelKey = vendorName+"#"+deviceMode;
			List<String> hardList = null;
			if(deviceModelIdMap.containsKey(modelKey)){
				hardList = deviceModelIdMap.get(modelKey);
				hardList.add(hardwareversion);
				deviceModelIdMap.put(modelKey, hardList);
			}else{
				hardList = new ArrayList<String>();
				hardList.add(hardwareversion);
				deviceModelIdMap.put(modelKey, hardList);
			}

			//厂商#型号#硬件版本:软件版本
			String softwareversion = map.get("softwareversion");
			String hardKey =  vendorName + "#" + deviceMode + "#" + hardwareversion;
			List<String> softList = null;
			if(hardMap.containsKey(hardKey)){
				softList = hardMap.get(hardKey);
				softList.add(softwareversion);
				hardMap.put(hardKey, softList);
			}else{
				softList = new ArrayList<String>();
				softList.add(softwareversion);
				hardMap.put(hardKey, softList);
			}

			//厂商#型号#硬件版本#软件版本:版本类型
			String versionType = getDevVersionType(map.get("device_version_type"));
			String softKey =  vendorName + "#" + deviceMode + "#" + hardwareversion+"#"+softwareversion;
			List<String> versionList = null;
			if(softMap.containsKey(softKey)){
				versionList = softMap.get(softKey);
				versionList.add(versionType);
				softMap.put(softKey, versionList);
			}else{
				versionList = new ArrayList<String>();
				versionList.add(versionType);
				softMap.put(softKey, versionList);
			}

			//厂商#型号#硬件版本#软件版本#版本类型:total
			Integer total = Integer.valueOf(map.get("total"));
			int finalTotal = 0;
			try
			{
				finalTotal = venModelHardCount(venId, devModelId,hardwareversion);
			}
			catch (Exception e)
			{
				return null;
			}
			
			String percentStr = "0";
			if(finalTotal != 0){
				int count = total.intValue();//1
				 percentStr = getPercentPar(count, finalTotal);
			}
			
			String totalKey = vendorName + "#" + deviceMode + "#" + hardwareversion+"#"+softwareversion + "#" + versionType;
			if(!totalMap.containsKey(totalKey)){
				totalMap.put(totalKey, total.intValue()+"#"+percentStr);
			} 
		}
		
		//组装table
		StringBuffer table = new StringBuffer("");
		StringBuffer modelTable = new StringBuffer("");
		StringBuffer hardTable = new StringBuffer("");
		StringBuffer softTable = new StringBuffer("");
		StringBuffer versionTable = new StringBuffer("");
		
		Iterator<String> it = vendorIdMap.keySet().iterator();
		String vendorName = "";
		List<String> deviceModelList = null;
		while(it.hasNext()){
			int vendorRowSpan = 0;  //厂商跨行
			int modelRowSpan = 0;   //型号跨行
			int hardRowSpan = 0 ;
			int softRowSpan = 0;
			table.append("<tr>");
			vendorName = it.next();
			deviceModelList = vendorIdMap.get(vendorName);
			List<String> noDuplicateModelList = removeDuplicate(deviceModelList);
			
			int curModelIndex = 0 ;//当前型号的索引,如果大于2就要换行
			for(String deviceModel : noDuplicateModelList){
				curModelIndex++; 
				String modelKey = vendorName + "#" + deviceModel;
				List<String> hardwareList = deviceModelIdMap.get(modelKey);
				List<String> noDuplicateHardList = removeDuplicate(hardwareList);
				
				int curHardIndex = 0 ;//当前硬件版本的索引,如果大于2就要换行
				for(String hard : noDuplicateHardList){
					curHardIndex++;
					String hardKey = vendorName + "#" + deviceModel + "#" + hard;
					List<String> softList = hardMap.get(hardKey);
					List<String> noDuplicateSoftList = removeDuplicate(softList);
					
					int curSoftIndex = 0 ;//当前软件版本的索引,如果大于2就要换行
					for(String soft : noDuplicateSoftList){
						curSoftIndex++;
						String versionKey = vendorName + "#" + deviceModel + "#" + hard + "#" + soft;
						List<String> versionList = softMap.get(versionKey);
						List<String> noDuplicateVersionList = removeDuplicate(versionList);
						//跨行
						vendorRowSpan = vendorRowSpan + 1;
						modelRowSpan = modelRowSpan + 1 ;
						hardRowSpan = hardRowSpan + 1;
						
						for(String version : noDuplicateVersionList){
							String totalkey = vendorName + "#" + deviceModel + "#" + hard + "#" + soft + "#" + version;
							String totalAndPer = totalMap.get(totalkey);
							int total = Integer.valueOf(totalAndPer.split("#")[0]);
							String percentStr = totalAndPer.split("#")[1];
							//占比
							logger.warn("total:"+total+",finalTotal:"+percentStr);
							versionTable.append("<td align='center' rowspan='"+softRowSpan+"'>"+getDevVersionType(version)+"</td>");
							versionTable.append("<td align='center' rowspan='"+softRowSpan+"'>"+total+"</td>");
							versionTable.append("<td align='center' rowspan='"+softRowSpan+"'>"+percentStr+"</td>");
						}
						
						if(curSoftIndex >= 2){
							softTable.append("<tr>");
						}
						softTable.append("<td align='center' rowspan='"+softRowSpan+"'>"+soft+"</td>").append(versionTable);
						if(curSoftIndex >= 2){
							softTable.append("</tr>");
						}
						versionTable.delete(0, versionTable.length());
					}
					
					if(curHardIndex >= 2){
						hardTable.append("<tr>");
					}
					hardTable.append("<td align='center' rowspan='"+getRealRowspan(hardRowSpan)+"'>"+hard+"</td>").append(softTable);
					if(curHardIndex >= 2){
						hardTable.append("</tr>");
					}
					softTable.delete(0, softTable.length());
					hardRowSpan = 0;
				}
				if(curModelIndex >= 2){
					modelTable.append("<tr>");
				}
				modelTable.append("<td align='center' rowspan='"+getRealRowspan(modelRowSpan)+"'>"+deviceModel+"</td>").append(hardTable);
				if(curModelIndex >= 2){
					modelTable.append("</tr>");
				}
				hardTable.delete(0, modelTable.length());
				modelRowSpan = 0;
			}
			
			table.append("<td class='column' align='center' rowspan='"+getRealRowspan(vendorRowSpan)+"'>"+vendorName+"</td>");
			table.append(modelTable.toString());
			table.append("</tr>");
			modelTable.delete(0, modelTable.length());
			hardTable.delete(0, modelTable.length());
			softTable.delete(0, modelTable.length());
			versionTable.delete(0, modelTable.length());
		}
		return table.toString();
	}

 public int getSameHardTotal(List<String> hardList,String hard){
	 int hardTotal = 0 ;
	 for(String curr : hardList){
		 if(curr.equals(hard)){
			 hardTotal++;
		 }
	 }
	return hardTotal; 
 }
 public static List<String> removeDuplicate(List<String> list){  
     List<String> listTemp = new ArrayList<String>();  
     for(String val : list){  
         if(!listTemp.contains(val)){  
             listTemp.add(val);  
         }  
     }  
     return listTemp;  
 }
 
 
 public String getPercentPar(int total,int finalTotal){
	     float percent = (float)total/finalTotal * 100; 
	     DecimalFormat format = new DecimalFormat("0.000000");
         System.out.println(format.format(percent));
	     String head = String.valueOf(format.format(percent));
 		 String percentStr = "";
		 int len = 0;
		 if(head.split("\\.").length == 2){
			 if(head.split("\\.")[1].length() < 2){
				 int max = 2 - head.split("\\.")[1].length();
				 for(int i = 0 ; i < max ; i++){
					 head = head + "0";
				 }
			 }
			 len = head.split("\\.")[0].length()+3;
		 }else{
			 len = head.length();
		 }
		 percentStr = head.substring(0, len) + "%";	
		 return percentStr;
 }
 
 public static void main(String args[]){
	 //int total =114680;
	// int finalTotal = 114686;
	// new BindDevTypeStatDAO().getPercentPar(total, finalTotal);
	 }
 
 
 public void test3(){
		
		StringBuffer table = new StringBuffer("");
		StringBuffer modelTable = new StringBuffer();;
		StringBuffer hardTable = new StringBuffer();;
		StringBuffer softTable = new StringBuffer();;
		StringBuffer versionTable = new StringBuffer();;
		
		Iterator<String> it = vendorIdMap.keySet().iterator();
		String vendorName = "";
		List<String> deviceModelList = null;
		while(it.hasNext()){
			int vendorRowSpan = 0;  //厂商跨行
			int modelRowSpan = 0;   //型号跨行
			int hardRowSpan = 0 ;
			int softRowSpan = 0;
			int versionRowSize = 0;
			table.append("<tr>");
			vendorName = it.next();
			deviceModelList = vendorIdMap.get(vendorName);
			List<String> noDuplicateModelList = removeDuplicate(deviceModelList);
			logger.warn("vendorName:"+vendorName+",deviceModelList:"+deviceModelList);
			int dev = 0;
			for(String deviceModel : noDuplicateModelList){
				dev++;
				String modelKey = vendorName + "#" + deviceModel;
				List<String> hardwareList = deviceModelIdMap.get(modelKey);
				List<String> noDuplicateHardList = removeDuplicate(hardwareList);
				logger.warn("modelKey:"+modelKey+",hardwareList:"+hardwareList);
				
				for(String hard : noDuplicateHardList){
					
					int hardTotal = getSameHardTotal(hardwareList, hard);
					String hardKey = vendorName + "#" + deviceModel + "#" + hard;
					List<String> softList = hardMap.get(hardKey);
					List<String> noDuplicateSoftList = removeDuplicate(softList);
					logger.warn("hardKey:"+hardKey+",softList:"+softList);
					
					for(String soft : noDuplicateSoftList){
						
						String versionKey = vendorName + "#" + deviceModel + "#" + hard + "#" + soft;
						List<String> versionList = softMap.get(versionKey);
						List<String> noDuplicateVersionList = removeDuplicate(versionList);
						logger.warn("versionKey:"+versionKey+",versionList:"+versionList);
						versionRowSize = noDuplicateVersionList.size();
						//rowspan
						vendorRowSpan = vendorRowSpan + 1;
						modelRowSpan = modelRowSpan + 1 ;
						hardRowSpan = hardRowSpan + 1;
						softRowSpan = 0 ;
						
						for(String version : noDuplicateVersionList){
							
							String totalkey = vendorName + "#" + deviceModel + "#" + hard + "#" + soft + "#" + version;
							String total = totalMap.get(totalkey);
							
							//占比
							logger.warn("totalkey:"+totalkey);
							logger.warn("total:"+total+",finalTotal:"+hardTotal);
							String percentStr = "0";
							if(hardTotal != 0){
								int count = Integer.valueOf(total).intValue();//1
								 float percent = (float)count/hardTotal * 100;//100%
								 String head = String.valueOf(Float.valueOf(percent));
								 int len = 0;
								 if(head.split("\\.").length == 2){
									 if(head.split("\\.")[1].length() < 2){
										 int max = 2 - head.split("\\.")[1].length();
										 for(int i = 0 ; i < max ; i++){
											 head = head + "0";
										 }
									 }
									 len = head.split("\\.")[0].length()+3;
								 }else{
									 len = head.length();
								 }
								 percentStr = head.substring(0, len) + "%";
							} 
							versionTable.append("<td align='center' rowspan='"+getRealRowspan(softRowSpan)+"'>"+getDevVersionType(version)+"</td>");
							versionTable.append("<td align='center' rowspan='"+getRealRowspan(softRowSpan)+"'>"+total+"</td>");
							versionTable.append("<td align='center' rowspan='"+getRealRowspan(softRowSpan)+"'>"+percentStr+"</td>");
						}
						softTable.append("<td align='center' rowspan='"+getRealRowspan(softRowSpan)+"'>"+soft+"</td>").append(versionTable);
						versionTable.delete(0, versionTable.length());
					}
					hardTable.append("<td align='center' rowspan='"+getRealRowspan(hardRowSpan)+"'>"+hard+"</td>").append(softTable);
					softTable.delete(0, softTable.length());
					hardRowSpan = 0;
				}
				if(dev >= 2){
					modelTable.append("<tr>");
				}
				modelTable.append("<td align='center' rowspan='"+getRealRowspan(modelRowSpan)+"'>"+deviceModel+"</td>").append(hardTable);
				if(dev >= 2){
					modelTable.append("</tr>");
				}
				hardTable.delete(0, modelTable.length());
				modelRowSpan = 0;
			}
			
			table.append("<td align='center' rowspan='"+getRealRowspan(vendorRowSpan)+"'>"+vendorName+"</td>");
			table.append(modelTable.toString());
			table.append("</tr>");
			modelTable.delete(0, modelTable.length());
			hardTable.delete(0, modelTable.length());
			softTable.delete(0, modelTable.length());
			versionTable.delete(0, modelTable.length());
		}
		
		System.out.println(table.toString());
 }
 
 public int getRealRowspan(int currRowspan){
	 return currRowspan == 1 ? 0 : currRowspan; 
 }
 
	public int venModelHardCount(String vendorId,String deviceModelId,String hardwareversion){
		
		List<String> cityIdList = null;
	/**	if(DBUtil.GetDB()==Global.DB_MYSQL){
			//TODO wait
		}else{
			
		} */
		String sqlStr = "select count(*) total "
			    +" from tab_gw_device a,tab_devicetype_info b,tab_hgwcustomer c,tab_device_version_attribute d "
				+" where a.devicetype_id = b.devicetype_id and a.device_id=c.device_id and a.devicetype_id=d.devicetype_id ";
		if(!StringUtil.IsEmpty(vendorId) && !"-1".equals(vendorId)){
			sqlStr = sqlStr + " and b.vendor_id='" + vendorId +"' ";
		}
		if(!StringUtil.IsEmpty(deviceModelId) && !"-1".equals(deviceModelId)){
			sqlStr = sqlStr + " and b.device_model_id='" + deviceModelId +"' ";
		}
		if(!StringUtil.IsEmpty(hardwareversion)){
			sqlStr = sqlStr +" and b.hardwareversion='" + hardwareversion +"' ";
		}
		//入网开始时间
		if(!StringUtil.IsEmpty(start)){
			sqlStr = sqlStr + " and a.complete_time>="+start;
		}
		//入网结束时间
		if(!StringUtil.IsEmpty(end)){
			sqlStr = sqlStr + " and a.complete_time<="+end;
		}
		if(null != user){
			 String userCity = user.getCityId();
			 if(!StringUtil.IsEmpty(userCity) && !"-1".equals(userCity) && !"00".equals(userCity)){
				cityIdList = CityDAO.getAllNextCityIdsByCityPid(userCity);
				if(null != cityIdList && !cityIdList.isEmpty() && cityIdList.size() > 1){
					sqlStr = sqlStr + " and a.city_id in (" + StringUtils.weave(cityIdList) + ")";
				}
			 } 
		}
		try
		{
			PrepareSQL psql = new PrepareSQL(sqlStr);
			return jt.queryForInt(psql.getSQL());
		}
		catch (Exception e)
		{
			//当返回没结果得时候会报EmptyResultDataAccessException: Incorrect result size: expected 1, actual 0，直接返回0即可
			 logger.error("查询数据失败 msg:"+e.getMessage());
			 return 0;
		}
	}
	
	public String getDevVersionType(String typeId){
		String res = "E8C";
		if("1".equals(typeId)){
			res = "E8C";
		}else if("2".equals(typeId)){
			res = "天翼网关1.0";
		}else if("3".equals(typeId)){
			res = "天翼网关2.0";
		}else if("4".equals(typeId)){
			res = "融合网关";
		}else if("5".equals(typeId)){
			res = "天翼网关3.0";
		}
		return res;
	}
	
	public String weave(List<Map> list){
		String res = "";
		if(list.size() > 0){
			String split = "";
			 for(Map maps : list){
				 String lis = String.valueOf(maps.get("device_version_type"));
				 if(null == lis || "".equals(lis) || "0".equals(lis)){
					 continue;
				 }
				 res = res + lis + split;
				 split = ",";
			 }
		}
		return res;
	}
}
