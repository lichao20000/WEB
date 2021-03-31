<%--
@author:hemc 
@date:2006-11-28
--%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../timelater.jsp"%>
<%
request.setCharacterEncoding("GBK");
String ip1 = request.getParameter("ip1");
String ip2 = request.getParameter("ip2");

Map deviceLoopIp = FluxComm.getDeviceLoopIP();
String[] tempArr = null;
String device_id1 = null;
String device_name1 = null;
String gather_id1 = null;
String device_id2 = null;
String device_name2 = null;
String gather_id2 = null;
tempArr = (String[])deviceLoopIp.get(ip1);
if(tempArr != null){
	device_id1 = tempArr[0];
	device_name1 = tempArr[1];
	gather_id1 = tempArr[2];
}
String ifindex1 = request.getParameter("ifindex1").trim();
String ifindex2 = request.getParameter("ifindex2").trim();
String ifname1 = request.getParameter("ifname1").trim();
String ifname2 = request.getParameter("ifname2").trim();
String ifalias1 = request.getParameter("ifalias1").trim();
String ifalias2 = request.getParameter("ifalias2").trim();
String ifportip1 = request.getParameter("ifportip1").trim();
String ifportip2 = request.getParameter("ifportip2").trim();
String ifdescr1 = request.getParameter("ifdescr1").trim();
String ifdescr2 = request.getParameter("ifdescr2").trim();
tempArr = (String[])deviceLoopIp.get(ip2);
if(tempArr != null){
	device_id2 = tempArr[0];
	device_name2 = tempArr[1];
	gather_id2 = tempArr[2];
}
%>
<%@ include file="../head.jsp"%>
<style type="text/css">
input
{
	width:80%;
}
select{
    width:100%;
}
</style>
<%@ include file="../toolbar.jsp"%>
<form name="frm" action="webtopo_Link_Add_Save.jsp" method="post" target="_subFrm" onsubmit="return checkForm()">
	<table width="100%" border="0" cellspacing="1" cellpadding="1" class="text" align="center">
		<tr>
			<td width="30%" valign="top">
				<table width="100%" border="0" cellspacing="1" cellpadding="1" bgcolor=#999999 class="text">
					<tr bgcolor=#ffffff>
						<td colspan="3"><span>链路:<%=device_name1%> 到 <%=device_name2%></span></td>
					</tr>				
					<tr>
						<th width="30" nowarp>
							<div align="center">&nbsp;</div>
						</th> 
						<th width="40%">
							<div align="center">网元1(<%=device_name1%>)</div>
						</th> 
						<th width="40%">
							<div align="center">网元2(<%=device_name2%>)</div>
						</th>
					</tr>
					<tr bgcolor=#ffffff>
	 					<td nowrap>端口索引</td>
	 					<td><input type="text" id="ifindex1" name="ifindex1" value="<%=ifindex1%>"></td>
	 					<td><input type="text" id="ifindex2" name="ifindex2" value="<%=ifindex2%>"></td>
	 				</tr>
					<tr bgcolor=#ffffff>
	 					<td nowrap>端口IP</td>
	 					<td><input type="text" id="ifportip1" name="ifportip1" value="<%=ifportip1%>"></td>
	 					<td><input type="text" id="ifportip2" name="ifportip2" value="<%=ifportip2%>"></td>
	 				</tr>
	 				<tr bgcolor=#ffffff>
	 					<td nowrap>端口名称</td>
	 					<td><input type="text" id="ifname1" name="ifname1" value="<%=ifname1%>"></td>
	 					<td><input type="text" id="ifname2" name="ifname2" value="<%=ifname2%>"></td>
	 				</tr>
	 				<tr bgcolor=#ffffff>
	 					<td nowrap>端口描述</td>
	 					<td><input type="text" id="ifdescr1" name="ifdescr1" value="<%=ifdescr1%>"></td>
	 					<td><input type="text" id="ifdescr2" name="ifdescr2" value="<%=ifdescr2%>"></td>
	 				</tr>
	 				<tr bgcolor=#ffffff>
	 					<td nowrap>端口别名</td>
	 					<td><input type="text" id="ifalias1" name="ifalias1" value="<%=ifalias1%>"></td>
	 					<td><input type="text" id="ifalias2" name="ifalias2" value="<%=ifalias2%>"></td>
	 				</tr>
					
	 				<tr bgcolor=#ffffff>
	 					<td nowrap>设备端口数据</td>
	 					<td><div name="div_<%=device_id1%>" id="div_<%=device_id1%>" _val=1></div></td>
	 					<td><div name="div_<%=device_id2%>" id="div_<%=device_id2%>" _val=2></div></td>
	 				</tr>
					
					<tr bgcolor=#ffffff>
	 					<td nowrap>备注:</td>
	 					<td colspan=2>端口索引|端口描述|端口名称|端口别名|端口IP</td>
	 				</tr>

	 				<tr>
	 					<td colspan="3" class="blue_foot"><div align="center"><button type="submit" class="jianbian">保 存</button><button type=reset class="jianbian">重置</button></div></td>
	 				</tr>
				</table>
			</td>
		</tr>
	</table>
	<input type="hidden" name="gather_id1" value="<%=gather_id1%>">
	<input type="hidden" name="gather_id2" value="<%=gather_id2%>">
	<input type="hidden" name="ip1" value="<%=ip1%>">
	<input type="hidden" name="ip2" value="<%=ip2%>">
	<input type="hidden" name="old_ifindex1" value="<%=ifindex1%>">
	<input type="hidden" name="old_ifindex2" value="<%=ifindex2%>">
	<input type="hidden" name="$action" value="update">
</form>
<iframe id="subFrm1" name="subFrm1" src="" scrolling="no" frameborder="0" style="display:none"></iframe>
<iframe id="subFrm2" name="subFrm2" src="" scrolling="no" frameborder="0" style="display:none"></iframe>
<iframe id="_subFrm" name="_subFrm" src="" scrolling="no" frameborder="0" style="display:none"></iframe>
<script type="text/javascript">
	var $ = document.all;
	//ifindex + "|" + ifdescr + "|" + ifname+ "|" + ifalias+ "|" + ifportip
	function change(_obj){
		var _value = _obj.value;
		var arrValue = _value.split("\|");
		var _val = event.srcElement.parentElement._val;
		
		$("ifindex"+_val).value = arrValue[0] == "null" ? "" : arrValue[0] ;
		$("ifdescr"+_val).value = arrValue[1] == "null" ? "" : arrValue[1] ;
		$("ifname"+_val).value = arrValue[2] == "null" ? "" : arrValue[2] ;
		$("ifalias"+_val).value = arrValue[3] == "null" ? "" : arrValue[3] ;
		$("ifportip"+_val).value = arrValue[4] == "null" ? "" : arrValue[4] ;
		
	}
	function checkForm(){
		if(!confirm("确定要提交吗?")){
			return false;
		}
		return true;
	}
	function init(){
		var device_id1 = "div_<%=device_id1%>";
		var device_id2 = "div_<%=device_id2%>";
		$(device_id1).innerHTML = "正在获取端口信息.....";
		$(device_id2).innerHTML = "正在获取端口信息.....";
		$("subFrm1").src = "./webtopo_Link_SelectPort.jsp?className=ReadDevicePort&device_id=<%=device_id1%>";
		$("subFrm2").src = "./webtopo_Link_SelectPort.jsp?className=ReadDevicePort&device_id=<%=device_id2%>" ;
	}
	init();
</script>
<%@ include file="../foot.jsp"%>