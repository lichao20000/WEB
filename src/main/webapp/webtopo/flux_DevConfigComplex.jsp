<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	ArrayList list_devinfo = new ArrayList();
	String dev_info = request.getParameter("dev_info");
	String[] dev_info_one = dev_info.split(",");
	String[] ip_info = null;
	BaseDevInfo baseinfo = null;
	for(int i=0;i<dev_info_one.length;i++) {
		BaseDevInfo base = new BaseDevInfo();
		ip_info = dev_info_one[i].split("/");
		base.setDevice_id(ip_info[0]);
		base.setDevice_name(ip_info[1]);
		base.setLoopback_ip(ip_info[2]);
		list_devinfo.add(base);
	}
	String ip = new String();
	String device_id = new String();
	for(int i=0; i<list_devinfo.size();i++) {
		baseinfo = (BaseDevInfo)list_devinfo.get(i);
		ip = ip + baseinfo.getLoopback_ip() + ",";
		device_id = device_id + "'" + baseinfo.getDevice_id() + "',";
	}
	ip = ip.substring(0,ip.length()-1);
	device_id = device_id.substring(0,device_id.length()-1);
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function UpdateCommand() 
{
	if (frm.select_command.checked)
	{
		frm.text_command.disabled = false;
		frm.text_command.className = "";
	}
	else
	{
		frm.text_command.disabled = true;
		frm.text_command.className = "bk";
	}
}
var isCall=0;
var iTimerID;
function CallPro()
{	
	switch(parseInt(isCall,10))
	{
		case 1:
		{
			window.alert("配置成功！");
			window.clearInterval(iTimerID);	
			
			isCall = 0
			window.colse();
			break;
		}
		case 0:
		{
			window.alert("操作失败，请重新操作！");
			window.clearInterval(iTimerID);
			
			isCall=0;
			break;
		}
	}
}

function CheckForm()
{
	if (frm.select_command.checked)
	{
		frm.action = "action_editDev.jsp?flag=1";
		frm.submit();
	}	
	else {
		frm.action = "action_editDev.jsp?flag=0";
		frm.submit();
	}
	iTimerID = window.setInterval("CallPro()",1000);
}
//-->
</SCRIPT>

<title>设备流量采集配置</title>
<form method="post" name="frm" target="childFrm">
	<table width="100%"  border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td bgcolor="#000000">
				<table width="100%" border="0" cellspacing="1" cellpadding="2">
					<TH>设 备 流 量 采 集 配 置</TH>
					<tr>
						<td class="blue_title">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td class="blue_title" width="30%" align="left">您选择如下IP进行配置</td>
									<td>&nbsp;&nbsp;</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="column" align="center">
							<textarea name="content" cols="1" rows="5" style="width:494px"><%= ip%></textarea>
						</td>
					</tr>
					<tr>
						<td class="blue_title">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td class="blue_title" width="25%" align="left">设备流量采集配置</td>
									<td>&nbsp;&nbsp;</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="column">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr><td HEIGHT="10"></td><tr>
								<tr>
									<td>
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td width="20%">&nbsp;&nbsp;</td>
												<td width="5%"><input name="select_command" type="checkbox" onclick="javascript:UpdateCommand();"></td>
												<td width="18%">更新读设备口令</td>
												<td align="left"><input name="text_command" type="text" class="bk" style="width:180px" disabled></td>
												<td>&nbsp;&nbsp;</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr><td HEIGHT="10"></td></tr>
								<tr>
									<td>
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td width="20%">&nbsp;&nbsp;</td>
												<td width="23%">是否采集流量信息</td>
												<td align="left">
												  <select name="gatherflag" style="width:180px">
												    <option value="0">不采集流量</option>
												    <option value="1" selected>采集流量</option>
										      </select>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr><td HEIGHT="10"></td></tr>
								<tr>
									<td>
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td width="20%">&nbsp;&nbsp;</td>
												<td width="23%">轮询间隔时间(S)</td>
												<td align="left"><input name="polltime" type="text" style="width:180px" value="300"></td>
												<td>&nbsp;&nbsp;</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr><td HEIGHT="15"></td><tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="blue_foot" align="right" height="30">
							<input type="button" value=" 保 存 " class="jianbian" onclick="javascript:CheckForm();">&nbsp;&nbsp;
							<input type="button" value=" 关 闭 " class="jianbian" onclick="javascript:window.close();">&nbsp;&nbsp;
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<input name="device_id" type="hidden" value="<%= device_id%>">
	<IFRAME ID="childFrm" STYLE="display:"></IFRAME>
</form>
