package com.linkage.module.gwms.report.bio;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.report.dao.AutoBindReportDAO;
import com.linkage.module.gwms.util.FileUtil;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Jason(3412)
 * @date 2009-8-6
 */
public class AutoBindReportBIO {

	private static Logger logger = LoggerFactory
		.getLogger(AutoBindReportBIO.class);
	
	AutoBindReportDAO autoBindReportDao;

	/**
	 * 查询用户绑定，以及回综调的情况
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-8-7
	 * @return void
	 */
	public List queryBindUser(String username){
		logger.debug("queryBindUser({})", username);
		List resList = null;
		Map tMap = autoBindReportDao.queryBindUser(username);
		if(null != tMap){
			resList = new ArrayList();
			resList.add(tMap);
		}else{
			logger.debug("该用户不存在");
		}
		return resList;
	}
	
	/**
	 * 文件中所有用户的绑定以及回综调的情况
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-8-10
	 * @return List
	 */
	public List queryBindUser(File userfile){
		logger.debug("queryBindUser(file)");
		if(null == userfile){
			logger.warn("queryBindUser(file), file is null");
			return null;
		}
		//解析文件为username的List
		List userList = FileUtil.file2list(userfile);
		return queryBindUser(userList);
	}
	
	/**
	 * 获取List中所有用户的绑定以及回综调的情况
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-8-10
	 * @return List
	 */
	public List queryBindUser(List userList){
		logger.debug("queryBindUser({})", userList);
		int length = 50;
		List resList = null;
		List hgwuserList = null;
		List dispatchList = null;
		if(null != userList){
			int size = userList.size();
			logger.debug("userList.size: " + size);
			int times = size/length;
			if(size%length != 0){
				times++;
			}
			logger.debug("for times: " + times);
			hgwuserList = new ArrayList();
			dispatchList = new ArrayList();
			for(int i = 0; i < times; i++){
				int toSize = (i+1)*length;
				List userTempList = null;
				if(size > toSize){
					userTempList = userList.subList(i*length, toSize);
				}else{
					userTempList = userList.subList(i*length, size);
				}
				//调用DAO的方法，查询这50个用户的信息
				List tl = autoBindReportDao.queryBindUser(userTempList);
				if(null != tl){
					hgwuserList.addAll(tl);
				}
				//调用DAO的方法，查询这50个回综调的信息
				List tl2 = autoBindReportDao.querydispatchUser(userTempList);
				if(null != tl2){
					dispatchList.addAll(tl2);
				}
			}
			resList = leftJoinList(userList, hgwuserList, dispatchList);
		}
		return resList;
	}
	
	
	/**
	 * 三个List按用户账号进行整合
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-8-10
	 * @return void
	 */
	public List leftJoinList(List list1, List list2, List list3){
		logger.debug("");
		List resList = null;
		Map resMap = null;
		String tmpUser = null;
		
		if(null != list1 && null != list2 && null != list3){
			int size1 = list1.size();
			int size2 = list2.size();
			int size3 = list3.size();
			resList = new ArrayList();
			//循环用户账号list
			for(int i = 0; i < size1; i++){
				resMap = new HashMap();
				tmpUser = StringUtil.getStringValue(list1.get(i));
				resMap.put("username", tmpUser);
				//循环用户表中记录
				for(int j = 0; j < size2; j++){
					Map map2 = (Map)list2.get(j);
					if(tmpUser.equals(map2.get("username"))){
						resMap.putAll(map2);
						//为自助绑定的，需要获取回综调的信息
						if("1".equals(map2.get("back"))){
							//循环综调回单表表中记录
							for(int k = 0; k < size3; k++){
								Map map3 = (Map)list3.get(k);
								if(tmpUser.equals(map3.get("username"))){
									resMap.putAll(map3);
									break;
								}
							}
						}else{
							break;
						}
					}
				}
				resList.add(resMap);
			}
		}
		return resList;
	}
	
	
	
	public void setAutoBindReportDao(AutoBindReportDAO autoBindReportDao) {
		this.autoBindReportDao = autoBindReportDao;
	}
	
}
