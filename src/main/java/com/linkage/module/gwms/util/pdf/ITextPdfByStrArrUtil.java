/**
 * 
 */
package com.linkage.module.gwms.util.pdf;

import java.awt.Color;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-6-27
 * @category com.linkage.module.gwms.util.pdf
 * 
 */
public class ITextPdfByStrArrUtil implements Result{

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 1L;

	//response
	private HttpServletResponse rep;
	//下载文件名
	private String pdfFileName;
	//标题
	private String pdfTitle;
	//数据
	private String pdfArrAata[][];
	
	public void execute(ActionInvocation invocation) throws Exception {
		
		pdfFileName = (String) invocation.getStack().findValue("pdfFileName");
		pdfArrAata = (String[][]) invocation.getStack().findValue("pdfArrAata");
		pdfTitle = (String) invocation.getStack().findValue("pdfTitle");
		
		if (pdfFileName == null)
		{
			throw new NullPointerException("没有定义输出文件名，或者没有提供get方法");
		}
		if (pdfTitle == null)
		{
			throw new NullPointerException("没有定义标题集合，或者没有提供get方法");
		}
		if (pdfArrAata == null)
		{
			throw new NullPointerException("没有定义导出数据结果集合，或者没有提供get方法");
		}
		
		rep = ServletActionContext.getResponse();
		rep.reset();
		//设置文件下载参数
		rep.setContentType("application/pdf");
		rep.setCharacterEncoding("GBK");
		rep.setHeader("Content-Disposition", "attachment; filename="
				+ URLEncoder.encode(pdfFileName + ".pdf", "UTF-8"));

        //定义中文字体
		BaseFont bfChinese = BaseFont.createFont("STSong-Light",
				"UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		Font fontCN = new Font(bfChinese, 12,Font.NORMAL);
		
		//步骤1：创建一个大小为A4的文档
		Document document = new Document(PageSize.A4);
        //第2个参数是输出流
        PdfWriter.getInstance(document, rep.getOutputStream());
        
        document.open();
        Font fontTilte = new Font(bfChinese, 14,Font.BOLD);
        Paragraph pg = new Paragraph(pdfTitle,fontTilte);
        pg.setAlignment(Element.ALIGN_CENTER);
        document.add(pg);
        document.add(new Paragraph(" "));
        
        //创建表格
        PdfPTable table = new PdfPTable(pdfArrAata[0].length);
        
        for(int i=0;i<pdfArrAata.length;i++){
        	for(int j=0;j<pdfArrAata[0].length;j++){
        		
        		String strCell = pdfArrAata[i][j];
        		
        		if(null == strCell){
        			strCell = "";
        		}
        		//定义一个表格单元
    			PdfPCell cell = new PdfPCell(new Paragraph(strCell,fontCN));
        		
    			if(0==i){
    				// 定义单元格的背景颜色
        			cell.setBackgroundColor(new Color(0xC0, 0xC0, 0xC0));
    			}
    			// 把单元加到表格中
    			table.addCell(cell);
        	}
        }
        
		// 设置表格大小为可用空白区域的100%
		table.setWidthPercentage(100);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);

		// 增加到文档中2
		document.add(table);
		// 关闭
		document.close();
	}

}
