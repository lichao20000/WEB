package com.linkage.module.gwms.diagnostics.bio;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gwms.dao.tabquery.ExpertDAO;
import com.linkage.module.gwms.obj.tabquery.ExpertOBJ;

/**
 * @author Jason(3412)
 * @date 2009-9-4
 */
public class ExpertManageBIO {
	
	private static Logger logger = LoggerFactory
			.getLogger(ExpertManageBIO.class);
	//DAO
	private ExpertDAO expertDao;
	
	/**
	 * 获取专家库列表
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-4
	 * @return List
	 */
	public List getExpertList() {
		logger.debug("getExpertList()");
		List rList = expertDao.queryExpertList();
		return rList;
	}

	/**
	 * 更新专家库信息,根据id更新故障描述和专家建议
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-4
	 * @return int
	 */
	public int updateExpert(String id, String faultDesc, String suggest) {
		logger.debug("updateExpert()", new Object[]{id, faultDesc, suggest});
		ExpertOBJ expertObj = new ExpertOBJ();
		expertObj.setId(id);
		expertObj.setExFaultDesc(faultDesc);
		expertObj.setExSuggest(suggest);
		return expertDao.updateExpert(expertObj);
	}

	
	/**
	 * setDAO
	 */
	public void setExpertDao(ExpertDAO expertDao) {
		this.expertDao = expertDao;
	}
	
}
