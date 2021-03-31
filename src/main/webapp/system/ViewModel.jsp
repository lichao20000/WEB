<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	String device=request.getParameter("device");
	String query=" where a.isflag=0";
	String s1="selected";
	String s2="";
	String s3="";
	if(device!=null && device.equals("1"))
	{
		query=" where a.isflag=1";
		s1="";
		s2="selected";
		s3="";
	}
	else if(device!=null && device.equals("all"))
	{
		query=" ";
		s1="";
		s2="";
		s3="selected";
	}
	String strSQL = "select a.city_id,b.city_name,sys_id,device_name,device_ip,read_com,oid_desc,isflag FROM tab_new_model a left outer join tab_city b on a.city_id=b.city_id "+query;
	//out.println(strSQL);
	String strData = "";
	String strClr = "";
	Cursor cursor = DataSetBean.getCursor(strSQL);
	Map fields = cursor.getNext();



	if(fields == null){
		strData = "<TR ><TD class=column COLSPAN=8 HEIGHT=30>暂时没有您查询的设备类型</TD></TR>";
	}
	else{
		int i=1;
		while(fields != null){		
			strData += "<TR>";
			strData += "<TD class=column1>"+ fields.get("city_name".toLowerCase()) + "&nbsp;</TD>";
			strData += "<TD  class=column1>"+ fields.get("sys_id".toLowerCase()) + "&nbsp;</TD>";
			strData += "<TD class=column1>"+ fields.get("device_ip".toLowerCase()) +"&nbsp;</TD>";
			strData += "<TD class=column1>"+ fields.get("device_name".toLowerCase()) +"&nbsp;</TD>";
			strData += "<TD  class=column1>"+ fields.get("read_com".toLowerCase()) + "&nbsp;</TD>";
			strData += "<TD  class=column1>"+ fields.get("oid_desc".toLowerCase()) + "&nbsp;</TD>";
			String isflag="未生成";
			if(((String)fields.get("isflag".toLowerCase())).equals("1"))
			{
				isflag="已生成";
			}
			strData += "<TD  class=column1>"+ isflag + "&nbsp;</TD>";
			if(((String)fields.get("isflag".toLowerCase())).equals("0"))
			{
				strData += "<TD class=column1 align=center><A HREF=\"javascript:Edit('"+fields.get("SYS_ID".toLowerCase())+"')\">设置成已知设备类型</A></TD>";
			}
			else
			{	
				strData += "<TD class=column1 align=center>&nbsp;</TD>";
			}
			strData += "</TR>";
			i++;
			fields = cursor.getNext();
		}
		
	}
		
%>
<%@ include file="../head.jsp"%>

<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.Map"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	var iTimerID;
	var isFlag=false;
	var iCode=0;
	function Edit(sys_id)
	{	
		if(window.confirm("您确认已经成功添加该设备类型了？"))
		{
			var page="SetDeviceModel.jsp?sys_id="+sys_id;
			document.all("childFrm").src=page;
			iTimerID = window.setInterval("CallPro()",1000);
		}
	}

	function CallPro()
	{
		
		if(isFlag)
		{	
			isFlag=false;
			window.clearInterval(iTimerID);
			if(iCode>=0)
			{
				window.alert("您的操作成功!");
			}
			else
			{
				window.alert("数据库操作失败，请重试!");
			}
			location.href="ViewModel.jsp?device="+frm.device.options[frm.device.selectedIndex].value;
		}
		
	
	}
//-->
</SCRIPT>
<form name="frm" action="ViewModel.jsp" method="post">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
				<TD bgcolor=#000000>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
						<TR>
							<TD bgcolor="#ffffff" colspan="8">各地市未知设备信息
								<select name="device" class="bk" onchange="javascript:frm.submit();">
									<option value="0" <%=s1%>>未知设备类型</option>
									<option value="1" <%=s2%>>配置过的设备类型</option>
									<option value="all" <%=s3%>>所有发现的设备类型</option>
								</select>
							</TD>
						</TR>
						<TR>
							<TH>属地</TH>
							<TH>设备sysObjectID</TH>
							<TH>设备IP</TH>
							<TH>设备名称</TH>
							<TH>读口令</TH>
							<TH>系统描述信息</TH>
							<TH>是否生成设备类型</TH>
							<TH>操作</TH>
						</TR>
						<%=strData%>
						
						<TR>
							<td class=column1 colspan=8 align=right><input type="button" name="close" value=" 关 闭 " onclick="javascript:window.close();" class="btn"></td>
						</TR>
					</TABLE>
				</TD>
			</TR>
			<tr>
							<td valign="middle" width="100%" height="100" colspan=8>
								<IFRAME width="100%" align="center" height="100%" ID="childFrm" SRC="" STYLE="display:none">
								</IFRAME>
							</td>
						</tr>
	</TABLE>
</TD>
</TR>
</TABLE>
</form>

