<HTML>
<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@ page contentType="text/html;charset=GBK" import="java.io.*"%>
<%@page import="com.linkage.litms.LipossGlobals" %>
<%request.setCharacterEncoding("GBK");%>

<br>
<TABLE  border=0 cellspacing=0 cellpadding=0 width="100%">
<tr>
<td>
<TABLE width="90%"  align="center" border=0 cellspacing=0 cellpadding=0 >
<TR>
<TD >
<table width="100%" align="center" cellpadding="0"  cellspacing="1" bgcolor="#666666">
 
<%
	String path = request.getRealPath("/system/download/doc");
	java.io.File dirName = new java.io.File(path);
	if(!dirName.exists()){
		out.println("目录./system/download/doc不存在！请建立目录。");
		return;
	}

	path = request.getRealPath("/system/download/software");
	dirName = new java.io.File(path);
	if(!dirName.exists()){
		out.println("目录./system/download/software不存在！请建立目录。");
		return;
	}
%>

<tr bgcolor='#ccddee' height="20"> 
    <TH  width="50%"> <div align="center"><%="文档名称"%></div></TH>
    <TH  width="20%"> <div align="center"><%="文件大小"%></div></TH>
    <TH  width="30%"> <div align="center"><%="更新时间"%></div></TH>
</tr> 
<%
	//取得目前目录的磁盘目录
	path = request.getRealPath("/system/download/doc");
	//建立代表目前目录位置的d变量
	java.io.File d = new java.io.File(path);
	//取得代表目录中所有文件
	java.io.File list[] = d.listFiles();
	
	//文件名排序模式 1：按最后更新日期降序（Z-A）；2：文件名升序（A-Z）；3：文件名降序（Z-A）
	int sortType = 2;
	
	//文件超链接类型 1：downloadAction.jsp跳转；2：直接URL下载
	
	int linkType = 1;
	if("hn_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
		linkType=2;
	}

	if(1 == sortType)
	{
     	//冒泡排序，使文件按照最近更新时间排序
     	for (int i=0; i<list.length; ++i)
     	{
              for(int j=i+1; j<list.length; ++j)
              {
                      if (list[i].lastModified() < list[j].lastModified())
                      {
                              java.io.File ftmp=list[i];
                              list[i]=list[j];
                              list[j]=ftmp;
                      }
              }
     	}
	} 
	else 
	{
        //add by zhangcong 按照文件名排序
        for (int i=0; i<list.length; ++i)
        {
                for(int j=i+1; j<list.length; ++j)
                {
                	int compareResultInt = list[i].getName().compareTo( list[j].getName() );
                	boolean compareResult = (2 == sortType) ? (compareResultInt > 0):(compareResultInt < 0);
                        if (compareResult)
                        {
                                java.io.File ftmp=list[i];
                                list[i]=list[j];
                                list[j]=ftmp;
                        }
                }
        }
	}

	if (list.length<=0)
	{
%>
<tr bgcolor='#FFFFFF' height='20' align='center'>
	<td colspan=3><%="请在./system/download/doc目录下增加下载资源！"%></td>
</tr>
<%
	}

	for(int i=0; i < list.length; i++)
	{
		String strClr;
		if((i%2)==0) strClr="#e7e7e7";
		else strClr = "#FFFFFF";
		
		String fileSize = "0B";
        long size = list[i].length();
        // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        double value = (double) size;
        if (value < 1024) {
        	fileSize = String.valueOf(value) + "B";
        } else {
        	value = new java.math.BigDecimal(value / 1024).setScale(2, java.math.BigDecimal.ROUND_DOWN).doubleValue();
        	// 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        	// 因为还没有到达要使用另一个单位的时候
        	// 接下去以此类推
        	if (value < 1024) {
       			fileSize = String.valueOf(value) + "KB";
            } else {
                value = new java.math.BigDecimal(value / 1024).setScale(2, java.math.BigDecimal.ROUND_DOWN).doubleValue();
                if (value < 1024) {
                    fileSize = String.valueOf(value) + "MB";
                } else {
                    // 否则如果要以GB为单位的，先除于1024再作同样的处理
                    value = new java.math.BigDecimal(value / 1024).setScale(2, java.math.BigDecimal.ROUND_DOWN).doubleValue();
                    fileSize = String.valueOf(value) + "GB";
                }
            }
        }
        
%>
<tr bgcolor=<%=strClr%> height='20' >
	<td height='16' >&nbsp;&nbsp;
<%
String linkURL = "";
if(1 == linkType){
	linkURL = "downloadAction.jsp?filename=" + list[i].getName() + "&path=" + path;
}else{
	linkURL = "download/doc/" + list[i].getName();
}
%>
		<a HREF="<%=linkURL%>"><%=list[i].getName()%></a>
	</td>
	<td height='16' >&nbsp;&nbsp;<%=fileSize%></td>
	<td height='16' >
		&nbsp;&nbsp;<%=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(list[i].lastModified()))%>
	</td>
</tr>
<%}%>

</table>
</td>
</tr>
<tr height="20"><td></td></tr>
<tr>
<td>
<%
	//取得目前目录的磁盘目录
	path = request.getRealPath("/system/download/software");
	d = new java.io.File(path);
	list = d.listFiles();
	
	if(1 == sortType)
	{
     	//冒泡排序，使文件按照最近更新时间排序
     	for (int i=0; i<list.length; ++i)
     	{
              for(int j=i+1; j<list.length; ++j)
              {
                      if (list[i].lastModified() < list[j].lastModified())
                      {
                              java.io.File ftmp=list[i];
                              list[i]=list[j];
                              list[j]=ftmp;
                      }
              }
     	}
	} 
	else if(2 == sortType)
	{
        // 按照文件名排序
        for (int i=0; i<list.length; ++i)
        {
                for(int j=i+1; j<list.length; ++j)
                {
                        if (list[i].getName().compareTo( list[j].getName() ) < 0)
                        {
                                java.io.File ftmp=list[i];
                                list[i]=list[j];
                                list[j]=ftmp;
                        }
                }
        }
	}
	
	if (list.length>0)
	{	
%>
<table width="100%" align="center" cellpadding="0"  cellspacing="1" bgcolor="#666666">
  <tr bgcolor='#ccddee' height="20" > 	
    <th width="50%"><div align="center"><%="程序名称"%></th>
    <th width="20%"><div align="center"><%="文件大小"%></th>
    <th width="30%"><div align="center"><%="更新时间"%></th>
  </tr>    
<%
		for(int i=0; i < list.length; i++)
		{
			String strClr;
			if((i%2)==0) strClr="#e7e7e7";
			else strClr = "#FFFFFF";
			
			String fileSize = "0B";
        	long size = list[i].length();
        	// 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        	double value = (double) size;
        	if (value < 1024) {
        		fileSize = String.valueOf(value) + "B";
        	} else {
        		value = new java.math.BigDecimal(value / 1024).setScale(2, java.math.BigDecimal.ROUND_DOWN).doubleValue();
        		// 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        		// 因为还没有到达要使用另一个单位的时候
        		// 接下去以此类推
        		if (value < 1024) {
       				fileSize = String.valueOf(value) + "KB";
            	} else {
                	value = new java.math.BigDecimal(value / 1024).setScale(2, java.math.BigDecimal.ROUND_DOWN).doubleValue();
                	if (value < 1024) {
                    	fileSize = String.valueOf(value) + "MB";
                	} else {
                    	// 否则如果要以GB为单位的，先除于1024再作同样的处理
                    	value = new java.math.BigDecimal(value / 1024).setScale(2, java.math.BigDecimal.ROUND_DOWN).doubleValue();
                    	fileSize = String.valueOf(value) + "GB";
                	}
            	}
        	}
        
%>
<tr bgcolor=<%=strClr%> height='20' >
	<td height='16' >&nbsp;&nbsp;
<%
String linkURL = "download/software/" + list[i].getName();
if(1 == linkType){
	linkURL = "downloadAction.jsp?filename=" + list[i].getName() + "&path=" + path;
}
%>
       <a HREF="<%=linkURL%>"><%=list[i].getName()%></a>
	</td>
	<td height='16' >&nbsp;&nbsp;<%=fileSize%></td>
	<td height='16'>
		&nbsp;&nbsp;<%=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(list[i].lastModified()))%>
	</td>
</tr>
<%
		}
	}
%>

</table>
</td>
</tr>
</TABLE>
</td>
</TABLE>
<br>
<%@ include file="../foot.jsp"%>
