/**
 * 
 */
package com.linkage.litms.common.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenjie(67371)
 * 
 * 定时删除EXCEL指定目录下的文件
 */
public class ExcelDeleteTimeTask extends TimerTask{
	
	private Logger logger = LoggerFactory.getLogger(ExcelDeleteTimeTask.class);

	/** EXCEL的文件名格式,目前在ExportExcelUtil中是这么定义的：xxx_130000.xls **/
	public static final String REG = "^[\\S]+_[\\d]+\\.xls$";
	
	public void run() {
		
		logger.warn("start clear excel files...");
		
		// excel存放路径
		File file = new File(ExportExcelUtil.EXCEL_EXPORT_PATH);
			
		// 获取里面所有的文件
		File[] excels = file.listFiles();

		// 
		List<File> dealExcels = new ArrayList<File>();
		
		for(File temp : excels)
		{
			String fileName = temp.getName();
			// 筛选需要处理的excel文件
			if(temp.exists() && temp.isFile() && fileName.matches(REG))
			{
				//System.err.println(temp.getName());
				//System.err.println(temp.getPath());
				dealExcels.add(temp);
			}
		}
		
		// 删除文件
		for(int i=0; i<dealExcels.size(); i++)
		{
			File temp2 = dealExcels.get(i);
			//System.err.println(temp2.getName() + " " + needDelete(temp2.getName()));
			if(needDelete(temp2.getName()))
			{
				logger.warn("delete excel file: " + temp2.getName());
				temp2.delete();
			}
		}
	}
	
	/**
	 * 考虑到可能有网络原因或者文件正在被写入读取等情况,只删除当前时间10分钟前的文件
	 *  
	 * @param fileName
	 * @return
	 */
	public boolean needDelete(String fileName)
	{
		String strs[] = fileName.split("[\\.]")[0].split("_");
		// 取最后面得时间
		String time = strs[strs.length - 1]; 
		//String time = fileName.split(".")[0].split("_")[1];
		//
		Date target = new Date(Long.parseLong(time) * 1000);
		
		GregorianCalendar   now   =   new   GregorianCalendar(); 
		//SimpleDateFormat   fmtrq   =   new   SimpleDateFormat( "yyyy-MM-dd : HH:mm:ss",Locale.US); 
		now.add(GregorianCalendar.MINUTE, -10); //10分钟前
		Date deletePoint = now.getTime(); 
		
		if(deletePoint.after(target))
		{
			return true;
		}
		return false;
	}

}
