<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	String vendor_id=request.getParameter("vendor_id");
	String mysql="select max(serial) as serial from tab_devicetype_info where vendor_id="+vendor_id+"";
	Map fields=DataSetBean.getRecord(mysql);
	int MaxSerial=0;
	//如果能获取到最大的号
	if(fields!=null && !((String)fields.get("SERIAL".toLowerCase())).equals("") )
	{
		MaxSerial=Integer.parseInt((String)fields.get("SERIAL".toLowerCase()))+1;
	}
	else
	{
		//如果该厂商暂时还没有列表，则百位上加一
		mysql="select max(serial) as serial from tab_devicetype_info";
		fields=DataSetBean.getRecord(mysql);
		if(fields!=null && !((String)fields.get("SERIAL".toLowerCase())).equals(""))
		{
			String serial=(String) fields.get("SERIAL".toLowerCase());
			if(serial.length()<3)
			{
				MaxSerial=101;
			}
			else
			{				
				serial=serial.substring(0,(serial.length()-2));
				MaxSerial=Integer.parseInt(String.valueOf(Integer.parseInt(serial)+1)+"01");
				
			}		
		}
		else
		{
			MaxSerial=1;
		}
	}

%>
<%@ include file="../head.jsp"%>

<%@ include file="../foot.jsp"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="java.util.Map"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	parent.frm.MaxSerial.value=<%=MaxSerial%>;
	parent.isFlag=true;
//-->
</SCRIPT>