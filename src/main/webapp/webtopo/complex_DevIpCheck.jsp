<html>
<head>
<title>配置用户设备</title>
</head>
<SCRIPT LANGUAGE="JavaScript">
<!--

var isCall=0;
var iTimerID;

function CallPro()
{
	switch(parseInt(isCall,10))
	{
		case 1:
		{
			window.alert("IP状态配置成功！");
			window.clearInterval(iTimerID);	
			
			isCall=0;
			location.href="complex_DevIpCheck.jsp";
			break;
		}
		case -1:
		{
			window.alert("数据库操作失败,请重新操作或联系管理员！");
			window.clearInterval(iTimerID);
			
			isCall=0;
			break;
		}
		case 0:
		{
			window.alert("没有对数据库进行任何操作！");
			window.clearInterval(iTimerID);
			
			isCall=0;
			break;
		}
		case 2:
		{
			window.alert("数据库操作成功，但无法与后台通讯，请联系管理员！");
			window.clearInterval(iTimerID);
			
			isCall=0;
			break;
		}
		case 3:
		{
			window.alert("设备记录已删除！");
			window.clearInterval(iTimerID);

			isCall=0;
			location.href="complex_DevIpCheck.jsp";
			break;

		}
		case 4:
		{
			window.alert("记录删除，但无法与后台通讯，请联系管理员！");
			window.clearInterval(iTimerID);
			
			isCall=0;
			break;
		}
	}

}

//选择全部
function selectAll(oStr){
	
	var oSelect = document.all(oStr);
	for(var i=0; i<oSelect.length; i++){
		oSelect[i].checked = frm.select_all.checked;
	}
}

//确定
function CheckForm()
{
	
	var oSelect = document.all("select_one");
	var flag=false;
	
	for(var i=0; i<oSelect.length; i++){
		if(oSelect[i].checked)
		{
			flag=true;
			break;
		}		
	}
	if(!flag)
	{
		window.alert("请至少选择一台设备！");
		return false;
	
	}

	if(frm.alarm_mode.selectedIndex==0)
	{
		window.alert("请选择告警方式！");
		return false;
	}

	if(frm.alarm_grade.selectedIndex==0)
	{
		window.alert("请选择告警等级！");
		return false;
	}

	frm.submit();
	iTimerID = window.setInterval("CallPro()",1000);
}

//删除
function CheckFormDel()
{
	var oSelect = document.all("select_one");
	var flag=false;
	
	for(var i=0; i<oSelect.length; i++){
		if(oSelect[i].checked)
		{
			flag=true;
			break;
		}		
	}
	if(!flag)
	{
		window.alert("请至少选择一台设备！");
		return false;
	
	}

	if (confirm("确定删除？"))
	{
		frm.action="testipcheckDel.jsp";
		frm.submit();
		iTimerID = window.setInterval("CallPro()",1000);
	}
	else
	{
		return;
	}
}
//-->
</SCRIPT>
<body>

<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<form name="frm" method="post" action="testipcheckList.jsp" target="childFrm">
  <table width="95%"  border="0" cellspacing="0" cellpadding="0" align="center">
	<tr><td height="20"></td></tr>
    <tr>
      <td><table width="100%"  border="0" cellspacing="0" cellpadding="0">
		    <tr>
			<td bgcolor=#000000><table width="100%"  border="0" cellspacing="1" cellpadding="2">
			  <tr>
                <TH>设备编号</TH>
                <TH>设备名称</TH>
                <TH>设备地址</TH>
                <TH>选择 <input name="select_all" type="checkbox" onclick="javascript:selectAll('select_one')">（全选）</TH>
              </tr>
              

<%
	request.setCharacterEncoding("GBK");
	IpCheck ipconfig = new IpCheck();
	Cursor cursor_UserDevID = ipconfig.getIPCheckList(request);

	if(cursor_UserDevID != null) {
		Map map_UserDevID = cursor_UserDevID.getNext();
			
			if(map_UserDevID!=null) {
				String device_id = "";
				String deviceName ="";
				String loopbackip = "";
				String gather_id = "";
				
				while(map_UserDevID!=null) {
					device_id = (String)map_UserDevID.get("device_id");
					deviceName= (String)map_UserDevID.get("device_name");
					loopbackip= (String)map_UserDevID.get("loopback_ip");
					gather_id = (String)map_UserDevID.get("gather_id");
					
					out.println("<tr>");
					out.println("<td class=column1 align=center>" + device_id + "</td>");
					out.println("<td class=column1 align=center>" + deviceName + "</td>");
					out.println("<td class=column1 align=center>" + loopbackip + "</td>");
					out.println("<td class=column1 align=center> <input name=select_one type=checkbox value=" + device_id + "/" + gather_id +  "> </td>");
					out.println("</tr>");
					map_UserDevID = cursor_UserDevID.getNext();
				}
			}	
			else {
				out.println("<td colspan=5 class=column1 align=center>当前用户没有可以管理的设备！</td>");
			}
	}
	else {
		out.println("<td colspan=5 class=column1 align=center>当前用户没有可以管理的设备！</td>");
	}
%>
              <tr>
                <td colspan="2" class=column1 align="center">告警方式</td>
                <td colspan="3" class=column2 align="left">
				<select name="alarm_mode" style="width:145">
                    <option value="0">请选择告警方式</option>
                    <option value="1">只发送一次告警信息</option>
                    <option value="2">连续发送告警信息</option>
                    </select>
			  	</td>
              </tr>
              <tr>
                <td colspan="2" class=column1 align="center">告警等级</td>
                <td colspan="3" class=column2 align="left">
					<select name="alarm_grade" style="width:145">
				    <option value="0">请选择告警等级</option>
					<option value="1">日志</option>
					<option value="2">提示</option>
					<option value="3">一般告警</option>
					<option value="4">严重告警</option>
					<option value="5">紧急告警</option>
					</select>
				</td>
              </tr>
			
              <tr>
                <td colspan="5" class=foot align="center"><input name="delete" type="button" value=" 删  除 " onclick="javascript:CheckFormDel()" class="jianbian">&nbsp;&nbsp;<input name="ok_button" type="button" value=" 确  定 " onclick="javascript:CheckForm();" class="jianbian"></td>
                </tr>
            </table></td>
		    </tr>
		  </table>
	  </td>
    </tr>
	
	<tr><td><IFRAME ID=childFrm name="childFrm" SRC="" STYLE="display:none"></IFRAME></td></tr>
  </table>
</form>
<%@ include file="../foot.jsp"%>
</body>
</html>

