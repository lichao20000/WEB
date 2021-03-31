package com.linkage.module.gtms.stb.resource.serv;

/**
 * 零配件BSS工单统计bio类
 * 
 * @author 田启明
 * @version 1.0
 * @since 2011-12-02
 * @category com.linkage.module.itv.zeroconf.bio<br>
 *           版权：南京联创科技 网管科技部
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.linkage.module.gtms.stb.resource.dao.BSSStatisticsDAO;
import com.linkage.module.gtms.stb.resource.dto.BSSStatisticsDTO;

public class BSSStatisticsBIOImp implements BSSStatisticsBIO{

	public static Logger log=Logger.getLogger(BSSStatisticsBIOImp.class);
	
	private BSSStatisticsDAO bSSStatisticsDao;

	public void setbSSStatisticsDao(BSSStatisticsDAO bSSStatisticsDao) {
		this.bSSStatisticsDao = bSSStatisticsDao;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.lims.itv.zeroconf.bio.BSSStatisticsBIO#exportBSS(com.linkage.module.lims.itv.zeroconf.dto.BSSStatisticsDTO)
	 */
	public List<Map> exportBSS(BSSStatisticsDTO dto,int firstResult,int pageSize){
		dto.setBeginTime(formatTime(dto.getFromTime(),false));
		dto.setEndTime(formatTime(dto.getToTime(),true));
		List<Map> list=bSSStatisticsDao.queryPageData(dto, firstResult, pageSize);
		List<Map> temp=new ArrayList<Map>();
		Map m=null;
		
		for(int i=0;i<list.size();i++){
			
			m=fiterBSSMap(list.get(i));
			if(null==m){
				continue;
			}
			temp.add(m);
		}
		
		return temp;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.lims.itv.zeroconf.bio.BSSStatisticsBIO#queryPageData(com.linkage.module.lims.itv.zeroconf.dto.BSSStatisticsDTO, int, int)
	 */
	public List<BSSStatisticsDTO> queryPageData(BSSStatisticsDTO dto,int firstResult,int pageSize){
		dto.setBeginTime(formatTime(dto.getFromTime(),false));
		dto.setEndTime(formatTime(dto.getToTime(),true));
		List<Map> list= bSSStatisticsDao.queryPageData(dto, firstResult, pageSize);
		return getBSSDtos(list);
	}

	
	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.lims.itv.zeroconf.bio.BSSStatisticsBIO#fiterBSSMap(java.util.Map)
	 */
	public Map<String,Object> fiterBSSMap(Map map){
		if(null==map){
			return null;
		}

		//属地名称
		if(null==map.get("city_name")){
			map.put("city_name","");
		}
		//业务帐号
		if(null==map.get("serv_account")){
			map.put("serv_account","");
		}
		
		//操作类型
		if(null==map.get("oper_type")){
			map.put("oper_type","");
		}else {
		   int type= Integer.parseInt(map.get("oper_type").toString());
		   map.put("oper_type",getOperation(type));
		}
		//产品ID
		if(null==map.get("prod_id")){
			map.put("prod_id","");
		}
		
		//接入方式
		if(null==map.get("addressing_type")){
			map.put("addressing_type","");
		}
		//BSS工单时间
		if(null==map.get("bss_date")){
			map.put("bss_date","");
		}else{
			
			long bdate=Long.parseLong(map.get("bss_date").toString());
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			map.put("bss_date",sdf.format(new Date(bdate*1000)));
		}
		
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.lims.itv.zeroconf.bio.BSSStatisticsBIO#getBSSDtos(java.util.List)
	 */
	public List<BSSStatisticsDTO> getBSSDtos(List<Map> list){
		
		List<BSSStatisticsDTO> dtos=new ArrayList<BSSStatisticsDTO>();
		if(null!=list && list.size()>0){
			BSSStatisticsDTO tempDto=null;
			for(Map<String,Object> map : list){
				tempDto=getDtoByMap(map);
				if(null!=tempDto){
					dtos.add(tempDto);
				}
			}
		}
		
		return dtos;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.lims.itv.zeroconf.bio.BSSStatisticsBIO#getDtoByMap(java.util.Map)
	 */
	public BSSStatisticsDTO getDtoByMap(Map<String,Object> map){
		
		if(null==map){
			return null;
		}
		BSSStatisticsDTO dto=new BSSStatisticsDTO();

		//属地名称
		if(null!=map.get("city_name")){
			dto.setCityName(map.get("city_name").toString());
		}else{
			dto.setCityName("");
		}
		//业务帐号
		if(null!=map.get("serv_account")){
			dto.setServAccount(map.get("serv_account").toString());
		}else{
			dto.setServAccount("");
		}
		//操作类型
		if(null!=map.get("oper_type")){
			
			int type=Integer.parseInt(map.get("oper_type").toString());
			dto.setOperation(getOperation(type));
		}else{
			dto.setOperation(getOperation(4));
		}
		//接入方式
		if(null!=map.get("addressing_type")){
			dto.setAddressingType(map.get("addressing_type").toString());
		}else{
			dto.setAddressingType("");
		}
		//产品ID
		if(null!=map.get("prod_id")){
			dto.setProdId(map.get("prod_id").toString());
		}else{
			dto.setProdId("");
		}
		
		//bss工单时间
		if(null!=map.get("bss_date")){
			
			long bdate= Long.parseLong(map.get("bss_date").toString());
			Date date=new Date(bdate*1000);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dto.setBssDate(sdf.format(date));
		}else{
			dto.setBssDate("");
		}
		
		return dto;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.lims.itv.zeroconf.bio.BSSStatisticsBIO#getOperation(int)
	 */
	public String getOperation(int type){
	    final	String[] oprs=new String[]{"开户","销户","移机","修正","无"};
        
	    if(type>=0 && type<=3){
	    	return oprs[type];
	    }else{
	    	return oprs[oprs.length-1];
	    }

	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.lims.itv.zeroconf.bio.BSSStatisticsBIO#formatTime(java.lang.String, boolean)
	 */
	public Long formatTime(String time,boolean next){
		
		if(null==time){
			return 0L;
		}
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date=sdf.parse(time);
			Long t=date.getTime()/1000;
			if(next){
				return t+24*60*60;
			}
			
			return t;
		} catch (ParseException e) {
			return 0L;
		}
		
	}
	

	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.lims.itv.zeroconf.bio.BSSStatisticsBIO#queryTotalNum(com.linkage.module.lims.itv.zeroconf.dto.BSSStatisticsDTO)
	 */
	public	int queryTotalNum(BSSStatisticsDTO dto){
		dto.setBeginTime(formatTime(dto.getFromTime(),false));
		dto.setEndTime(formatTime(dto.getToTime(),true));
		return bSSStatisticsDao.queryTotalNum(dto);
	}
}
