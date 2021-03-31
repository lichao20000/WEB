package com.linkage.litms.system;

import java.util.HashMap;

/**
 * 模块日志统计数据
 * @author wangp
 *
 */
public class ModuleLog
{
	 /**
	  * 模块名称
	  */
	 private String moduleName="";
	 
	 /**
	  * 模块下的功能点日志统计
	  * key:itemName,value:统计数
	  */
	 private HashMap itemMap = new HashMap();
	 
	 /**
	  * 模块下的日志统计数
	  */
	 private long moduleLogNum=0;
	 
	 
	 public ModuleLog(String _moduleName)
	 {
		 moduleName = _moduleName;
	 }
	 
	 /**
	  * 更新功能点下的日志统计数
	  * @param _itemName
	  */
	 public void update(String _itemName,long num)
	 {
		 if(null==_itemName||"".equals(_itemName))
		 {
			 return;
		 }
		 
		 long logNum=0;
		 //如果itemMap中已经有这个item的统计数据，要在原有的数据上增加
		 /*synchronized(itemMap)
		 {
			 if(itemMap.containsKey(_itemName))
			 {
				 logNum = Long.parseLong((String)itemMap.get(_itemName));
			 }
			 logNum++;
			 itemMap.put(_itemName,String.valueOf(logNum));
			 
			 moduleLogNum++;
		 }*/
		 moduleLogNum+=num;
		 
	 }
	 
	 /**
	  * 返回模块名称
	  * @return
	  */
	 public String getModuleName()
	 {
		 return moduleName;
	 }
	 
	 /**
	  * 返回模块下的日志统计数
	  * @return
	  */
	 public long getModuleLogNum()
	 {
		 return moduleLogNum;
	 }
	 
	 
	 /**
	  * 返回功能点下的日志统计map
	  * @return
	  */
	 public HashMap getItemMap()
	 {
		 return itemMap;
	 }
	 
}
