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
		out.println("Ŀ¼./system/download/doc�����ڣ��뽨��Ŀ¼��");
		return;
	}

	path = request.getRealPath("/system/download/software");
	dirName = new java.io.File(path);
	if(!dirName.exists()){
		out.println("Ŀ¼./system/download/software�����ڣ��뽨��Ŀ¼��");
		return;
	}
%>

<tr bgcolor='#ccddee' height="20"> 
    <TH  width="50%"> <div align="center"><%="�ĵ�����"%></div></TH>
    <TH  width="20%"> <div align="center"><%="�ļ���С"%></div></TH>
    <TH  width="30%"> <div align="center"><%="����ʱ��"%></div></TH>
</tr> 
<%
	//ȡ��ĿǰĿ¼�Ĵ���Ŀ¼
	path = request.getRealPath("/system/download/doc");
	//��������ĿǰĿ¼λ�õ�d����
	java.io.File d = new java.io.File(path);
	//ȡ�ô���Ŀ¼�������ļ�
	java.io.File list[] = d.listFiles();
	
	//�ļ�������ģʽ 1�������������ڽ���Z-A����2���ļ�������A-Z����3���ļ�������Z-A��
	int sortType = 2;
	
	//�ļ����������� 1��downloadAction.jsp��ת��2��ֱ��URL����
	
	int linkType = 1;
	if("hn_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
		linkType=2;
	}

	if(1 == sortType)
	{
     	//ð������ʹ�ļ������������ʱ������
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
        //add by zhangcong �����ļ�������
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
	<td colspan=3><%="����./system/download/docĿ¼������������Դ��"%></td>
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
        // ����ֽ�������1024����ֱ����BΪ��λ�������ȳ���1024����3λ��̫��������
        double value = (double) size;
        if (value < 1024) {
        	fileSize = String.valueOf(value) + "B";
        } else {
        	value = new java.math.BigDecimal(value / 1024).setScale(2, java.math.BigDecimal.ROUND_DOWN).doubleValue();
        	// ���ԭ�ֽ�������1024֮������1024�������ֱ����KB��Ϊ��λ
        	// ��Ϊ��û�е���Ҫʹ����һ����λ��ʱ��
        	// ����ȥ�Դ�����
        	if (value < 1024) {
       			fileSize = String.valueOf(value) + "KB";
            } else {
                value = new java.math.BigDecimal(value / 1024).setScale(2, java.math.BigDecimal.ROUND_DOWN).doubleValue();
                if (value < 1024) {
                    fileSize = String.valueOf(value) + "MB";
                } else {
                    // �������Ҫ��GBΪ��λ�ģ��ȳ���1024����ͬ���Ĵ���
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
	//ȡ��ĿǰĿ¼�Ĵ���Ŀ¼
	path = request.getRealPath("/system/download/software");
	d = new java.io.File(path);
	list = d.listFiles();
	
	if(1 == sortType)
	{
     	//ð������ʹ�ļ������������ʱ������
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
        // �����ļ�������
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
    <th width="50%"><div align="center"><%="��������"%></th>
    <th width="20%"><div align="center"><%="�ļ���С"%></th>
    <th width="30%"><div align="center"><%="����ʱ��"%></th>
  </tr>    
<%
		for(int i=0; i < list.length; i++)
		{
			String strClr;
			if((i%2)==0) strClr="#e7e7e7";
			else strClr = "#FFFFFF";
			
			String fileSize = "0B";
        	long size = list[i].length();
        	// ����ֽ�������1024����ֱ����BΪ��λ�������ȳ���1024����3λ��̫��������
        	double value = (double) size;
        	if (value < 1024) {
        		fileSize = String.valueOf(value) + "B";
        	} else {
        		value = new java.math.BigDecimal(value / 1024).setScale(2, java.math.BigDecimal.ROUND_DOWN).doubleValue();
        		// ���ԭ�ֽ�������1024֮������1024�������ֱ����KB��Ϊ��λ
        		// ��Ϊ��û�е���Ҫʹ����һ����λ��ʱ��
        		// ����ȥ�Դ�����
        		if (value < 1024) {
       				fileSize = String.valueOf(value) + "KB";
            	} else {
                	value = new java.math.BigDecimal(value / 1024).setScale(2, java.math.BigDecimal.ROUND_DOWN).doubleValue();
                	if (value < 1024) {
                    	fileSize = String.valueOf(value) + "MB";
                	} else {
                    	// �������Ҫ��GBΪ��λ�ģ��ȳ���1024����ͬ���Ĵ���
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
