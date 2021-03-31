package com.linkage.module.gwms.report.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author Jason(3412)
 * @date 2009-9-4
 */
public class ExecuteSqlDAO extends SuperDAO{

	
	/**
	 * 查询数据
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-4
	 * @return List
	 */
	public List<String[]> queryData(String strSQL){
		
		if(StringUtil.IsEmpty(strSQL)){
			return null;
		}
		List<String[]> resList = null;
		List rlist = null;
		rlist = jt.queryForList(strSQL);
		if(null != rlist && rlist.size() > 0){
			resList = new ArrayList<String[]>();
			int size = rlist.size();
			Map rmap = (Map)rlist.get(0);
			Set keySet = rmap.keySet();
			int titSize = keySet.size();
			String[] strTitle = new String[titSize];
			Iterator itor = keySet.iterator();
			int iarr = 0;
			//标题列
			while(itor.hasNext()){
				strTitle[iarr++] = StringUtil.getStringValue(itor.next());
			}
			resList.add(strTitle);
			for(int i = 0; i < size; i++){
				String[] resArr = new String[titSize];
				Map tmap = (Map)rlist.get(i);
				for(int j = 0; j < titSize; j++){
					resArr[j] = StringUtil.getStringValue(tmap.get(strTitle[j]));
				}
				resList.add(resArr);
			}
		}
		return resList;
	}
	
	
	/**
	 * 批SQL更新数据
	 * 
	 * @param 
	 * @author Jason(3412)
	 * @date 2009-9-6
	 * @return int[]
	 */
	public int[] updateData(String[] sqlArr){
		
		int[] iret = null;
		if(null == sqlArr || sqlArr.length == 0){
			return null;
		}else{
			iret = jt.batchUpdate(sqlArr);
		}
		return iret;
	}
}
