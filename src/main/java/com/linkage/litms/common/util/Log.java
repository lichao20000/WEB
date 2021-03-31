package com.linkage.litms.common.util;

import java.io.BufferedWriter;
import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;

public class Log
{
	private static Logger m_logger = LoggerFactory.getLogger(Log.class);
	public static void writeLog(String log,String name)
	{
		String path =LipossGlobals.getLipossHome()+File.separator+ "logs";
		File fileDS = null;
		BufferedWriter writer = null;
		try
		{
			fileDS = new File(path);
			if (!fileDS.exists())
			{
				fileDS.mkdirs();				
			}
			path +=File.separator+name;
			fileDS = new File(path);
			fileDS.createNewFile();
			writer = new java.io.BufferedWriter(
							new java.io.FileWriter(fileDS, true));
			writer.write(log);	
			writer.newLine();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				writer.close();	
			}
			catch(Exception e1)
			{
				m_logger.error("文件操作关闭失败！");
			}
			
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO 自动生成方法存根

	}

}
