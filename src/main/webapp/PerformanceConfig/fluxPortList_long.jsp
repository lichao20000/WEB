<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%--
/**
 * 流量端口列表;
 * REQ:XXXX-XXXX-XXXX
 * BUG:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-Mail:benyp@lianchuang.com
 * @version 1.0
 * @since 2008-10-28 ??06:13:02
 *
 * @版权 南京联创网络科技 网管产品部;
 *
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title><s:property value="device_name"/>【<s:property value="loopback_ip"/>】端口列表</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jquery.tablesorter_LINKAGE.js"/>"></script>
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript">
	$(function(){
		//$('#tab_port').tablesorter({1:sorter("integer"), 5: { sorter: "ipAddress"},8:{sorter: "integer"}});
		$('#tab_port').tablesorter({headers:{0:{sorter:false},5:{sorter:"ipAddress"}},sortList:[[1,0],[1,1]]});
	});
	//删除端口
	function delPort(){
		if($("input[@name='chk'][@checked]").length<1){
			alert("请至少选择一个端口！");
			return false;
		}
		var chk="";
		$("input[@name='chk'][@checked]").each(function(){
			chk+="-/-"+$(this).val();
		});
		chk=chk.substring(3);
		$.post(
			"<s:url value="/performance/configFlux!delPort.action"/>",
			{
				device_id:"<s:property value="device_id"/>",
				port_info:chk
			},
			function(data){
				if(data=="0"){
					alert("删除成功！");
				}else if(data=="-1"){
					alert("删除失败！");
				}else if(data=="-2"){
					alert("数据删除成功，通知后台失败！");
				}else{
					alert("超时，请重试！");
				}
				window.location=window.location;
			}
		);
	}
	//编辑端口
	function EditPort(){
		if($("input[@name='chk'][@checked]").length<1){
			alert("请至少选择一个端口！");
			return false;
		}
		var chk="";
		$("input[@name='chk'][@checked]").each(function(){
			chk+="-/-"+$(this).val();
		});
		chk=chk.substring(3);
		var url="<s:url value="/performance/configFlux!EditPort.action"/>?device_id=<s:property value="device_id"/>&port_info="
		+chk+"&device_name=<s:property value="device_name"/>&loopback_ip=<s:property value="loopback_ip"/>"+"&t="+new Date();
		window.open(url);
	}
</script>
<style type="text/css">
</style>
</head>
<body>
	<form action="">
		<br>
		<table width="98%" align="center" class="listtable" id="tab_port">
			<thead>
				<tr>
					<th colspan="9"><s:property value="device_name"/>【<s:property value="loopback_ip"/>】端口列表</th>
				</tr>
				<tr>
					<th>选择</th>
					<th>端口索引</th>
					<th>端口描述</th>
					<th>端口名称</th>
					<th>端口别名</th>
					<th>端口IP</th>
					<th>端口标识方式</th>
					<th>采集情况</th>
					<th>已配阈值</th>
				</tr>
			</thead>
			<tbody>
				<s:if test="cfgPortList==null || cfgPortList.size()==0">
					<tr>
						<td colspan="9">没有对应的端口数据</td>
					</tr>
				</s:if>
				<s:iterator var="cpl" value="cfgPortList" status="rowstatus">
					<tr class="odd">
						<td>
							<input type="checkbox" name="chk" value="<s:property value="#cpl.getway+'|||'+#cpl.port_info"/>">
						</td>
						<td><s:property value="#cpl.ifindex"/></td>
						<td><s:property value="#cpl.ifdescr"/></td>
						<td><s:property value="#cpl.ifname"/></td>
						<td><s:property value="#cpl.ifnamedefined"/></td>
						<td><s:property value="#cpl.ifportip"/></td>
						<td><s:property value="#cpl.getway+':'+#cpl.getwayStr"/></td>
						<td><s:property value="#cpl.gatherflag"/></td>
						<td><s:property value="#cpl.confignum"/></td>
					</tr>
				</s:iterator>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="9" align="right">
						<button onclick="EditPort();">修 改</button><button onclick="delPort();">删 除</button><button onclick="window.close();">关 闭</button>
					</td>
				</tr>
			</tfoot>
		</table>
	</form>
</body>
</html>
