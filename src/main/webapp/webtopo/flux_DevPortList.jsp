<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
		request.setCharacterEncoding("GBK");
		String dev_info = request.getParameter("dev_info");
		String[] info = dev_info.split("/");
		String dev_id = info[0];
		String device_name = info[1];
		String device_ip = info[2];

		Flux_Config flux_con = new Flux_Config();
		Cursor cursor = flux_con.getPortList(dev_id);
		Map result = cursor.getNext();
		String device_id = null;
		String ifindex = null;
		String ifportip = null;
		String ifname = null;
		String ifdescr = null;
		String ifnamedefined = null;
		String getway = null;
		int num_getway = 0;
		String getway_value = null;
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">

function SelectAll()
{
	var len = frm.elements.length;
	var i;
	for (i=0;i<len;i++ )
	{
		if (frm.elements[i].name=='select_one') {
			frm.elements[i].checked = frm.select_all.checked;
		}
	}
}

function SelectOneTrue()
{
	var len = frm.elements.length;
	var i;
	//fix by 王志猛 11月2日
	var num=0;
	
	//var flag;
	//end fix
	for(i=0; i<len; i++){
			if(frm.elements[i].name=='select_one')
			{
				if (frm.elements[i].checked)
				{
					num++;
				}
			}		
		}
		//fix by 王志猛 11月2日
	return num;
		/*if(!flag)
		{	
			return false;	
		}
		else
		{
			return true;
		}*/
}
//end fix
var isCall=0;
var iTimerID;

function CallPro()
{	
	switch(parseInt(isCall,10))
	{
		case 1:
		{
			window.alert("端口删除成功！");
			window.clearInterval(iTimerID);	
			
			isCall = 0
			location.reload();
			break;
		}
		case -1:
		{
			window.alert("操作失败，请重新操作！");
			window.clearInterval(iTimerID);
			
			isCall=0;
			break;
		}
	}
}

function CheckForm(param)
{
	var len = frm.elements.length;
	var key = new String();
	var i;
	var flag;
	var j =0;
	//fix by 王志猛 2006-11-2
	if (SelectOneTrue()==0)
	{
		alert("请选择一条记录！");
		return false;
	}
	else if(SelectOneTrue()!=1)
	{
		alert("不能同时对多个端口进行配置!");
		return false;
	}
	//end fix
	for (i=0;i<len;i++)
	{
		if (frm.elements[i].name=='select_one')
		{
			if (frm.elements[i].checked)
			{
				j++;
				key = key + frm.elements[i].value + ";";
			}
		}
	}
	key = key.substring(0,key.length-1);
	
	if (param == "alter")
	{
			if (j == 1)
			{
				page = "flux_editPort.jsp?dev_ip=" + frm.dev_ip.value + "&key=" + key;
				otherpra = "channelmode=0,directories=0,location=no,menubar=no,resizable=no,scrollbars=no,status=0,toolbar=0,height=700,width=700,top=200,left=200";
				window.open(page,"编辑端口配置",otherpra);
			}
			else
			{
				page = "";
				otherpra = "channelmode=0,directories=0,location=no,menubar=no,resizable=no,scrollbars=no,status=0,toolbar=0,height=700,width=700,top=200,left=200";
				window.open(page,"编辑端口配置",otherpra);
			}
	}
	
	if (param == "del")
	{
		if(!window.confirm("确认删除设备流量配置?"))
		{
			return false;
		}
		frm.action = "action_delPort.jsp?";
		frm.submit();
		iTimerID = window.setInterval("CallPro()",1000);
	}
}
</SCRIPT>

<title>设备的所有端口列表</title>
<form method="post" name="frm" target="childFrm">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td bgcolor="#000000">
			<table width="100%" border="0" cellspacing="1" cellpadding="2">
				<TH><%= device_ip%>&nbsp;设备的所有端口列表</TH>
				
				<tr>
					<td class="column">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="10%" align="right">设备IP</td>
								<td width="2%">&nbsp;&nbsp;</td>
								<td><input name="dev_ip" type="text" class="bk" style="width:193px" readonly="true" value="<%= device_ip%>"></td>
								<td width="10%" align="right">设备名称</td>
								<td width="2%">&nbsp;&nbsp;</td>
								<td><input name="dev_name" type="text" class="bk" style="width:193px" readonly="true" value="<%= device_name%>"></td>
							</tr>
							<tr><td HEIGHT="10"></td></tr>
							<tr>
								<td colspan="6">
									<table width="96%" border="0" cellspacing="1" cellpadding="2" align="center" bgcolor="#000000">
										<tr>
											<td class="blue_title" nowrap>端口索引</td>
											<td class="blue_title" nowrap>端口IP</td>
											<td class="blue_title" nowrap>端口名称</td>
											<td class="blue_title" nowrap>端口描述</td>
											<td class="blue_title" nowrap>端口别名</td>
											<td class="blue_title" nowrap>采集方式</td>
											<td class="blue_title" nowrap>选择&nbsp;&nbsp;<input name="select_all" type="checkbox" onclick="javascript:SelectAll();">(全选)</td>
										</tr>
										<%
											while (result != null) {
												device_id = (String)result.get("device_id");
												ifindex = (String)result.get("ifindex");
												ifportip = (String)result.get("ifportip");
												
												if (ifportip != null) {
													if (ifportip.equals("null")) {
														ifportip = "";
													}
												}
												ifname = (String)result.get("ifname");
												ifdescr = (String)result.get("ifdescr");
												ifnamedefined = (String)result.get("ifnamedefined");
												getway = (String)result.get("getway");
												num_getway = Integer.parseInt(getway);

												switch (num_getway) {
													case 1: {
														getway_value = ifindex;
														break;
													}
													case 2: {
														getway_value = ifdescr;
														break;
													}
													case 3: {
														getway_value = ifname;
														break;
													}
													case 4: {
														getway_value = ifnamedefined;
														break;
													}
													case 5: {
														getway_value = ifportip;
														break;
													}
												}
												out.println("<tr>");
												out.println("<td class=\"column\" align=\"left\">" + ifindex + "</td>");
												out.println("<td class=\"column\" align=\"left\">" + ifportip + "</td>");
												out.println("<td class=\"column\" align=\"left\">" + ifname + "</td>");
												out.println("<td class=\"column\" align=\"left\">" + ifdescr + "</td>");
												out.println("<td class=\"column\" align=\"left\">" + ifnamedefined + "</td>");
												out.println("<td class=\"column\" align=\"left\">" + getway + "</td>");
												out.println("<td class=\"column\" align=\"center\"><input name=\"select_one\" type=\"checkbox\" value=\"" + device_id + "," + getway + "," + getway_value + "\"></td>");
												out.println("</tr>");
												result = cursor.getNext();
											}
										%>
									</table>
								</td>
							</tr>
							<tr><td HEIGHT="30"></td></tr>
							<tr>
								<td align="center" colspan="6">
									<input type="button" value=" 修 改 " class="jianbian" onclick="javascript:CheckForm('alter');">&nbsp;&nbsp;
									<input type="button" value=" 删 除 " class="jianbian" onclick="javascript:CheckForm('del');">&nbsp;&nbsp;
									<input type="button" value=" 关 闭 " class="jianbian" onclick="javascript:window.close();">&nbsp;&nbsp;
								</td>
							<tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<IFRAME ID="childFrm" name="childFrm" STYLE="display:none"></IFRAME>
</form>