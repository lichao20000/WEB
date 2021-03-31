package com.linkage.litms.common.database;

import com.linkage.litms.LipossGlobals;

public class DatabaseUtil {
	public static final int SYBASE_DATABASE =0;
	public static final int ORACLE_DATABASE =1;
	/**
	 * 判断当前系统连接的是甚么类型的数据库
	 * 
	 * @return 数据库类型，默认返回sybase数据库 0：sybase数据库 1：oracle数据库
	 */
	public static int databaseKind() 
	{		
		int kind = SYBASE_DATABASE;//默认为sysbase数据库
		String dataBaseStr="";
		try
		{
			dataBaseStr=LipossGlobals.getLipossProperty("database.driver");
		}
		catch(Exception e)
		{
			dataBaseStr="";
		}
		if(null!=dataBaseStr&&dataBaseStr.length()>0)
		{
			int pos1 = dataBaseStr.indexOf("sybase");
			int pos2 = dataBaseStr.indexOf("oracle");	
			if(-1!=pos1) //sybase数据库
			{
				kind =SYBASE_DATABASE;
			}
			else if(-1!=pos2) //oarcle数据库
			{
				kind =ORACLE_DATABASE;
			}		
		}		
		return kind;

	}

}
