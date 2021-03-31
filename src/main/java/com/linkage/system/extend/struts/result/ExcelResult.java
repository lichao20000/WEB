
package com.linkage.system.extend.struts.result;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;

/**
 * 自定义的result 该类主要提供EXCEL文档的导出功能<br/>
 * 使用举例：（type是按照struts-plugin.xml文件中该类分配填写）<br/>
 * <action name="A" class="A"><br/>
 *		<result name="success" type="excel"></result><br/>
 * </action><br/>
 * @author 陈仲民（5243）
 * @version 1.0
 * @since 2007-10-29
 * @category msg
 */
public class ExcelResult implements Result
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1569392110700217637L;
	private String[] title;
	private String[] column;
	private ArrayList<Map> data;
	private String fileName;
	private HttpServletResponse rep;
	private OutputStream outputStream;

	@SuppressWarnings("unchecked")
	public void execute(ActionInvocation invocation) throws Exception
	{
		title = (String[]) invocation.getStack().findValue("title");
		column = (String[]) invocation.getStack().findValue("column");
		data = (ArrayList<Map>) invocation.getStack().findValue("data");
		fileName = (String) invocation.getStack().findValue("fileName");
		if (title == null)
		{
			throw new NullPointerException("没有定义标题集合，或者没有提供get方法");
		}
		if (column == null)
		{
			throw new NullPointerException("没有定义字段集合，或者没有提供get方法");
		}
		if (data == null)
		{
			throw new NullPointerException("没有定义导出数据结果集合，或者没有提供get方法");
		}
		if (fileName == null)
		{
			throw new NullPointerException("没有定义输出文件名，或者没有提供get方法");
		}
		rep = ServletActionContext.getResponse();
		rep.reset();
		// 设置文件下载参数
		rep.setContentType("application/vnd.ms-excel; charset=GBK");
		rep.setCharacterEncoding("GBK");
		rep.setHeader("Content-Disposition", "attachment; filename="
				+ URLEncoder.encode(fileName + ".xls", "UTF-8"));
		// 得到输出流
		outputStream = rep.getOutputStream();
		getOutputFile();
	}

	/**
	 * 将数据转化成输出流
	 */
	private void getOutputFile()
	{
		// 将结果集转化为Excel输出
		HSSFWorkbook wwb = null;
		try
		{
			// 创建工作表
			wwb = new HSSFWorkbook();
			HSSFSheet ws = null;
			HSSFRow row = null;
			HSSFCell cell = null;
			Iterator it = data.iterator();
			int i = 0;
			// 在第一页第一行添加标题
			ws = wwb.createSheet("Sheet0");
			row = ws.createRow(0);
			// 在每页的第一行输入标题
			for (int m = 0; m < title.length; m++)
			{
				cell = row.createCell((short) m);
				cell.setCellValue(new HSSFRichTextString(title[m]));
			}
			int k = 0;
			// 逐行添加数据
			while (it.hasNext())
			{
				// 每10000条记录分一页
				if (i / 50000 > k)
				{
					k = i / 50000;
					ws = wwb.createSheet("Sheet" + k);
					row = ws.createRow(0);
					// 在每页的第一行输入标题
					for (int l = 0; l < title.length; l++)
					{
						cell = row.createCell((short) l);
						cell.setCellValue(new HSSFRichTextString(title[l]));
					}
				}
				Map dataMap = (Map) it.next();
				row = ws.createRow(i - 50000 * k + 1);
				// 输出数据
				for (int j = 0; j < column.length; j++)
				{
					cell = row.createCell((short) j);
					// 按字段取值
					String columnName = column[j];
					cell.setCellValue(new HSSFRichTextString(
							getValue(dataMap, columnName)));
				}
				i++;
			}
			// 写入流
			wwb.write(outputStream);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				outputStream.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 处理null值
	 * @param dataMap
	 * @param columnName
	 * @return
	 */
	private String getValue(Map dataMap, String columnName)
	{
		return (dataMap.get(columnName) == null || String
				.valueOf(dataMap.get(columnName)).equalsIgnoreCase("null")) ? "" : String
				.valueOf(dataMap.get(columnName)).trim();
	}
}
