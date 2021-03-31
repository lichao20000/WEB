<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage"/>
<%@ page import="com.linkage.litms.common.database.Cursor" %>
<%@ page import="java.util.Map"%>
<%
String task_id = request.getParameter("task_id");
String task_name= request.getParameter("task_name");
String type = request.getParameter("type");
//只有测试配置失败，转向这个页面的请求才会有result参数
String result = request.getParameter("result");

//测试配置失败，让重新配置，不需要再判断是否转向测试结果页面
if(null==result &&versionManage.isTest(task_id))
{
	response.sendRedirect("./testAutoUpdateTaskDetail.jsp?task_id="+task_id+"&task_name="+task_name+"&type="+type);
	return;
}

Cursor cursor = null;
if (type != null && "3".equals(type)){
	cursor = versionManage.getDeviceByTaskID_jiangsu(task_id);
}
else{
	cursor = versionManage.getDeviceByTaskID(task_id);
}

Map fields = cursor.getNext();
%>
<SCRIPT LANGUAGE="JavaScript">
<%
if("false".equals(result))
{
	out.println("alert('发送工单失败，请稍后重试！');");
}
%>
function selectAll(elmID){
		t_obj = document.all(elmID);
		if(!t_obj) return;
		obj = event.srcElement;
		if(obj.checked){
			if(typeof(t_obj) == "object" ) {
				if(typeof(t_obj.length) != "undefined") {
					for(var i=0; i<t_obj.length; i++){
						t_obj[i].checked = true;
					}
				} else {
					t_obj.checked = true;
				}
			}
		
		}else{
			if(typeof(t_obj) == "object" ) {
				if(typeof(t_obj.length) != "undefined") {
					for(var i=0; i<t_obj.length; i++){
						t_obj[i].checked = false;
					}
				} else {
					t_obj.checked = false;
				}
			}
		}
	}

//检查有没有选择设备	
function CheckForm()
{
    var oselect = document.all("device_id");
    if(oselect == null)
    {
		alert("请选择设备！");
		return false;
	}
	var num = 0;
	if(typeof(oselect.length)=="undefined")
	{
		if(oselect.checked)
		{
			num = 1;
		}
	}
	else
	{
		for(var i=0;i<oselect.length;i++)
		{
			if(oselect[i].checked)
			{
				num++;
			}
		}
	}
	if(num ==0)
	{
		alert("请选择设备！");
		return false;
	}
	
	return true;
}

function goback()
{
  window.location.href = "./checkAutoUpdateTask.jsp?type="+<%=type%>+"&action=check";
}
</SCRIPT>
<TABLE boder=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>	
		<FORM NAME="frm" METHOD="post" ACTION="testAutoUpdateTask_submit.jsp" onsubmit="return CheckForm()">
		<input type="hidden" name="task_id" value=<%=task_id %>>
		<input type="hidden" name="task_name" value=<%=task_name %>>
		<input type="hidden" name="type" value=<%=type %>>		
		<TABLE width="80%" border=0 cellspacing=0 cellpadding=0 align="center">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite"><%=task_name %>策略升级测试</td>
					</tr>
				</table>
				</td>
			</tr>
			<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="outTable">					
					<% 
					if(null!=fields)
					{
						String device_id = "";
						String oui = "";
						String deviceSerial="";
						String sheetID="";
						while(null!=fields)
						{
							device_id =(String)fields.get("device_id");
							oui = (String)fields.get("oui");
							deviceSerial = (String)fields.get("device_serialnumber");
							sheetID = (String)fields.get("sheet_id");
							out.println("<TR align=center bgcolor= #ffffff>");
							out.println("<TD colspan=2 align=left class=column><input type='checkbox' name='device_id' value='"
									+device_id+"|"+sheetID+"'>"+oui+"-"+deviceSerial+"</TD>");							
							out.println("</TR>");
							
							fields = cursor.getNext();
						}
						out.println("<TR bgcolor= #ffffff align=center>");
						out.println("<TD colspan=2 align=left class=column><input type='checkbox' name='all' onclick=\"selectAll('device_id')\">全选</TD>");
						out.println("</TR>");
						out.println("<TR class='green_foot'><TD colspan=2 align=right><INPUT TYPE=\"submit\" value=\" 测 试 \" class=btn>&nbsp;&nbsp;<input type='button' value='返 回' class=btn onclick='goback()'></TD></TR>");
					}
					else
					{
						out.println("<TR bgcolor= #ffffff><TD align=center colspan=2 class=column>没有对应的设备</TD></TR>");
						out.println("<TR class='green_foot'><TD colspan=2 align=right><input type='button' value='返 回' class=btn onclick='goback()'></TD></TR>");
					}
					%>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
	</FORM>
</TD>
</TR>
</TABLE>
					
					