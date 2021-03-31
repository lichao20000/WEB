package com.linkage.module.gtms.config.serv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gtms.config.dao.ControlTimeOfUserDao;
import com.linkage.module.gwms.util.StringUtil;

public class ControlTimeOfUserServImpl implements ControlTimeOfUserServ{
	
	private static Logger logger = LoggerFactory
	     .getLogger(ControlTimeOfUserServImpl.class);
	
	private ControlTimeOfUserDao dao;
	/**
	 * 增加一条记录
	 * (non-Javadoc)
	 * @return int 是否增加成功
	 */
	public int addRecord(String typeId,String conTime1){
		logger.debug("addRecord({},{})",new Object[]{typeId,conTime1});
		int isRes = -1 ;
		isRes = dao.addRecord(typeId,conTime1);
		return isRes;
	}
	/**
	 * 根据Id获取要修改的记录
	 */
	public Map<String, Object> getRecordById(String cuId){
		logger.debug("getRecordById({})",cuId);
		List lt = null ; 
		Map<String,Object> map = new HashMap<String,Object>(); 
		lt = dao.getRecordById(StringUtil.getIntegerValue(cuId));
		if(null != lt && lt.size()>0){
			map = (Map<String, Object>) lt.get(0);
		}
		return map;
	}
	/**
	 * 更新一条记录
	 * (non-Javadoc)
	 * @return int 是否更新成功
	 */
	public int updateRecord(String cuId,String typeId,String conTime1){
		logger.debug("updateRecord({},{},{})",new Object[]{cuId,typeId,conTime1});
		int isRes = -1;
		if(isRecordExsist(cuId)){
			isRes = dao.updateRecord(cuId, typeId, conTime1);
		}
		return isRes ;
	}
	/**
	 * 删除一条记录
	 * (non-Javadoc)
	 * @return int 是否删除成功
	 */
	public int deleteRecord(String cuId){
		logger.debug("deleteRecord({})",cuId);
		int isRes = -1;
		if(isRecordExsist(cuId)){
			isRes = dao.deleteRecord(cuId);
		}
		return isRes ;
	}
	/**
	 * 根据时间查询记录
	 * @param conTimeStart
	 * @param conTimeEnd
	 * @return
	 */
	public List getRecordByTime(String typeId,String conTimeStart,String conTimeEnd,String flag){
		logger.debug("getRecordByTime({},{})",new Object[]{conTimeStart,conTimeEnd});
		long conTime = 0l;
		String timeType ="";
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> tmpList = dao.getAllRecord(typeId,conTimeStart, conTimeEnd);
		Map<String,Object> map = null ;
		if(null != tmpList && tmpList.size()>0){
			map = new HashMap<String,Object>();
			for(int i = 0 ; i <tmpList.size(); i++){
				map = tmpList.get(i);
				try{
					conTime = StringUtil.getLongValue(map.get("con_time"));
					DateTimeUtil dt = new DateTimeUtil(conTime * 1000);
					map.put("con_time", dt.getLongDate());
			    }catch (NumberFormatException e){
			    	map.put("con_time", "");
			    }	
			    timeType = StringUtil.getStringValue(map.get("type_id"));
			    if("1".equals(timeType)&&"1".equals(flag)){
			    	map.put("type_id", "用户受理时间"); 
			    }
			    list.add(map);
		    }
		}
		return list; 
	}
	/**
	 * 判断修改的记录是否存在
	 * @param cuId
	 * @return
	 */
	public boolean isRecordExsist(String cuId){
		logger.debug("isRecordExsist({})",cuId);
		logger.warn("isRecordExsist******cuId= "+cuId);
		boolean flag = false;
		
		List list = dao.getRecordById(StringUtil.getIntegerValue(cuId));
		if(null != list && list.size() > 0 ){
			flag = true;
		}
		return flag;
	}
	public ControlTimeOfUserDao getDao() {
		return dao;
	}
	public void setDao(ControlTimeOfUserDao dao) {
		this.dao = dao;
	}
	
}
