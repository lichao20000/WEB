/**
 * 
 */
package com.linkage.litms.common.util;

import java.io.File;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import com.linkage.litms.LipossGlobals;
import com.linkage.module.gwms.util.StringUtil;

/**
 * @author chenjie(67371)
 * 
 * 导出excel工具类
 */
public class ExportExcelUtil {

	/**
	 * 日志
	 */
	private static Logger logger = Logger.getLogger(ExportExcelUtil.class);
	
	public static final String EXCEL_EXPORT_PATH = LipossGlobals.G_ServerHome + File.separator + "excels" + File.separator;
	
	/**
	 * 参数构造
	 * @param title        excel中的标题（可以不输入）
	 * @param titleColumns 列的名字（必须输入）
	 */
	public ExportExcelUtil(String title, String[] titleColumns)
	{
		if(!StringUtil.IsEmpty(title))
		{
			this.excelTitle = title;
		}
		
		if(titleColumns != null && titleColumns.length != 0)
		{
			this.titleColumns = titleColumns;
		}
		else
		{
			logger.error("导出字段列名称为空，导出错误！");
		}
		
		// 初始化excel存放路径，在web根路径下的excels下
		String path = getClass().getResource("/").toString();
		EXCEL_PATH = File.separator + path.substring(6, path.length() - 17) + File.separator + "excels" + File.separator;
	}
	
	/**
	 * excel标题
	 */
	private String excelTitle = "统计结果";
	
	/**
	 * 列的名字
	 */
	private String[] titleColumns;
	
	/**
	 * 存储excel文件的路径
	 */
	private String EXCEL_PATH;
	
	/**
	 * 导出数据
	 * @param response  请求
	 * @param rs        结果集，请在DAO中实现
	 * @param fileName  文件名，如"device"，导出后文件名为device_2011-05-04_19:13:01.xls
	 */
	public void export(HttpServletResponse response, SqlRowSet rs, String fileName) throws Exception
	{
		// 时间戳
		/**
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String dateStr = sdf.format(date);
		**/
		
		File file = new File(EXCEL_PATH + fileName + "_" + System.currentTimeMillis() / 1000 + ".xls");
		/*
		logger.debug("Excel_path: " + EXCEL_PATH + fileName);
		
		// 创建文件，统一定时删除
		file.createNewFile();
		*/
			
		// 写文件
		//BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		
		response.reset(); //设置为没有缓存
		response.setContentType("Application/msexcel;charset=GBK");
		response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
		//response.setCharacterEncoding("GBK");//这个必须设置   
		Writer writer = response.getWriter();
		writer.write(getHtmlHead());
		writer.write(getTitleStr());
		
		// 遍历数据集
		while(rs.next())
		{
			SqlRowSetMetaData metadata = rs.getMetaData();
			String value;
			writer.write("<TR>");
			for (int i = 1; i <= metadata.getColumnCount(); i++) 
			{
				value = rs.getString(metadata.getColumnName(i));
				if (value == null)
					value = "";
				writer.write("<TD>" + value.trim() + "</TD>");
			}
			writer.write("</TR>");
			writer.write("\r\n");
			writer.flush();
		}
		
		writer.write(getHtmlTail());
		writer.flush();
		writer.close();
		
		// 在页面输出
		/**
		FileInputStream fin = new FileInputStream(file);
		  
		byte[] buf = new byte[1024];
		int r = 0;
		while ((r = fin.read(buf, 0, buf.length)) != -1)
		{
			output.write(buf, 0, r);
		}
		response.getOutputStream().flush();
		**/
	}
	
	/**
	 * 导出数据(数据集是list)
	 * @param response  请求
	 * @param list      数据结果
	 * @param column    列名
	 * @param fileName  文件名，如"device"，导出后文件名为device_2011-05-04_19:13:01.xls
	 */
	public void export(HttpServletResponse response, List<Map> list, String[] columns, String fileName) throws Exception
	{
		// 时间戳
		/**
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String dateStr = sdf.format(date);
		**/
		
		File file = new File(EXCEL_PATH + fileName + "_" + System.currentTimeMillis() / 1000 + ".xls");
		/*
		logger.debug("Excel_path: " + EXCEL_PATH + fileName);
		
		// 创建文件，统一定时删除
		file.createNewFile();
		*/
			
		// 写文件
		//BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		
		response.reset(); //设置为没有缓存
		response.setContentType("Application/msexcel;charset=GBK");
		response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
		//response.setCharacterEncoding("GBK");//这个必须设置   
 		Writer writer = response.getWriter();
		
		writer.write(getHtmlHead());
		writer.write(getTitleStr());
		
		// 遍历数据集
		String value = null;
		Map one = null;
		for(int i=0; i<list.size(); i++)
		{
			writer.write("<TR>");
		    one = list.get(i);
			for(int j=0; j<columns.length; j++)
			{
				value = StringUtil.getStringValue(one.get(columns[j]));
				writer.write("<TD style='mso-number-format:\\@'>" + value.trim().toString() + "</TD>");
			}
			writer.write("</TR>");
			writer.write("\r\n");
		}
	
		writer.write(getHtmlTail());
		writer.flush();
		writer.close();
		
		// 在页面输出
		/**
		FileInputStream fin = new FileInputStream(file);
		  
		byte[] buf = new byte[1024];
		int r = 0;
		while ((r = fin.read(buf, 0, buf.length)) != -1)
		{
			output.write(buf, 0, r);
		}
		response.getOutputStream().flush();
		**/
	}
	
	/**
	 * 写html头
	 * @return
	 */
	private String getHtmlHead()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<HTML>").append("<HEAD>").append("<TITLE></TITLE>")
			.append("<meta http-equiv=Content-Type content='text/html; charset=GBK'>")
			.append("<style>TD{FONT-FAMILY: '宋体', 'Tahoma'; FONT-SIZE: 14px; text-align:left}")
			.append("</style></HEAD>")
			.append("<BODY>").append("<TABLE border=1 cellspacing=0 cellpadding=0 width=100%>")
			.append("<TR><TD>")
			.append("<TABLE width=90% border=1 cellspacing=0 cellpadding=0 align=center>")
			.append("<tr><td style='text-align:center'><b>" + excelTitle + "</b></td></tr>")
			.append("<TR><TD>").append("<TABLE border=1 cellspacing=0 cellpadding=0 width=100%>");
		return sb.toString();
	}
	
	/**
	 * 写html尾
	 * @return
	 */
	private String getHtmlTail()
	{
		String htmlStr = "</TABLE></TD></TR></TABLE></TD></TR></TABLE></BODY></HTML>";
		return htmlStr;
	}
	
	/**
	 * 写列的名字
	 * @return
	 */
	private String getTitleStr()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<TR>");
		for(String col : titleColumns)
		{
			sb.append("<TH>").append(col).append("</TH>");
		}
		sb.append("</TR>");
		return sb.toString();
	}

	public String getExcelTitle() {
		return excelTitle;
	}

	public void setExcelTitle(String excelTitle) {
		this.excelTitle = excelTitle;
	}

	public String[] getTitleColumns() {
		return titleColumns;
	}

	public void setTitleColumns(String[] titleColumns) {
		this.titleColumns = titleColumns;
	}
	
    /** 
     *  把字符串转成utf8编码，保证中文文件名不会乱码
     *  @param  s
     *  @return 
      */ 
     public   static  String toUtf8String(String s){
        StringBuffer sb  =   new  StringBuffer();
         for  ( int  i = 0 ;i < s.length();i ++ ){
             char  c  =  s.charAt(i);
            if  (c  >=   0   &&  c  <=   255 ){sb.append(c);}
            else {
                byte [] b;
                try  { b  =  Character.toString(c).getBytes("utf-8");}
                catch  (Exception ex) {
                   System.out.println(ex);
                   b  =   new   byte [ 0 ];
               }
                for  ( int  j  =   0 ; j  <  b.length; j ++ ) {
                    int  k  =  b[j];
                    if  (k  <   0 ) k  +=   256 ;
                   sb.append( " % "   +  Integer.toHexString(k).toUpperCase());
               }
           }
       }
         return sb.toString();
     }
	
}