<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>

<%@taglib prefix="s" uri="/struts-tags"%>
<%--
/**
 * 批量配置时获取设备
 * REQ:XXXX-XXXX-XXXX
 * BUG:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-Mail:benyp@lianchuang.com
 * @version 1.0
 * @since 2008-10-9 PM02:18:24
 *
 * @版权 南京联创 网管产品部;
 *
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>批量配置<s:property value="pmeeflg?'性能':'流量'" /></title>

<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/PerformanceConfig/jquery.configPmee.js"/>"></script>
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">

<script type="text/javascript">

	var gw_type = '<s:property value="gw_type"/>';

	$(function(){
		init();
		//选择查询方式
		$("input[@name='sel_type']").click(function(){
			SelType($(this).val());
		});
		//选择厂商
		$("select[@name='vendor_id']").change(function(){
			if($(this).val()!=""){
				$("select[@name='device_model']").html("<option value=''>正在获取设备型号...</option>");
				$.post(
					"<s:url value="/performance/configPmee!getDevModelByVendor.action"/>",
					{
						vendor_id:$(this).val()
					},
					function(data){
						data="<option value=''>请选择</option>"+data;
						$("select[@name='device_model']").html(data);
					}
				);
			}
		});

	});
	//查询方式
	function SelType(type){
		if(type==1){
			$("tr[name='tr_model']").show();
			$("tr[@name='tr_name']").hide();
		}else{
			$("tr[name='tr_model']").hide();
			$("tr[@name='tr_name']").show();
		}
	}

	//查询设备
	function Query(){
		var type=$("input[@name='sel_type'][@checked]").val();
		var pmeeflg="<s:property value="pmeeflg"/>";
		if(type==1){//按厂商查询
			$.SelDevByModel(pmeeflg, gw_type);
		}else{//按设备IP查询
			$.SelDevByName(pmeeflg, gw_type);
		}
	}

	//配置单个性能表达式
	function ConSPmee(device_id){
		var url="<s:url value="/performance/configPmee.action"/>"+"?isbatch=false&device_id="+device_id;
		window.open(url,"性能配置");
	}
	//配置单个流量
	function ConSFlux(device_id){
		var url="<s:url value="/performance/configFlux.action"/>?isbatch=false&device_id="+device_id;
		window.open(url,"流量配置");
	}
	//流量配置
	function ConfigFlux(){
		var device_id="";
		$("input[@name='chk'][checked]").each(function(){
			device_id+=","+$(this).val();
		});
		if(device_id==""){
			alert("请选择需要配置的设备!");
			return false;
		}
		var url="<s:url value="/performance/configFlux.action"/>?isbatch=true&device_id="+device_id.substring(1);
		window.open(url);
	}
	//配置性能
	function ConfigPmee(){
		var device_id="";
		$("input[@name='chk'][checked]").each(function(){
			device_id+=","+$(this).val();
		});
		if(device_id==""){
			alert("请选择需要配置的设备!");
			return false;
		}
		var url="<s:url value="/performance/configPmee.action"/>?isbatch=true&device_id="+device_id.substring(1);
		window.open(url);
	}
	//初始化
	function init(){

	}
</script>
</head>
<body>
<br>
<table width="94%" height="30" border="0" cellspacing="0"
	cellpadding="0" class="green_gargtd" align="center">
	<tr>
		<s:if test="pmeeflg">
			<td width="162" align="center" class="title_bigwhite">性能批量配置</td>
		</s:if>
		<s:else>
			<td width="162" align="center" class="title_bigwhite">流量批量配置</td>
		</s:else>
	</tr>
</table>

<table width="94%" align="center" class="querytable">

	<tr>
		<th colspan="4" class="title_1">设备查询</th>
	</tr>
	<tr>
		<td class="title_2" width="15%" align="right">查询方式：</td>
		<td colspan="3" align="left" width="85%">
			<input type="radio" name="sel_type" value="1" id="sel_1" checked>
			<label for="sel_1" onclick="SelType(1)">高级查询</label>

			<input type="radio" name="sel_type" value="2" id="sel_2" style="margin-left: 80px;" >
			<label for="sel_2" onclick="SelType(2)">按设备查询</label>

		</td>
	</tr>
	<tr name="tr_name" style="display: none;">
		<td class="title_2" width="15%" align="right">设备序列号：</td>
		<td width="35%"><input type="text" name="device_name"></td>
		<td class="title_2" width="15%" align="right">设备IP：</td>
		<td width="35%"><input type="text" name="loopback_ip"></td>
	</tr>

	<tr name="tr_model">
		<td class="title_2" width="15%" align="right">属地：</td>
		<td colspan="3" width="85%">
			<select name="city_id" style="width:150px">
			<option value="">请选择：</option>
			<s:iterator var="clist" value="CityList">
				<option value="<s:property value="#clist.city_id"/>"><s:property
					value="#clist.city_name" /></option>
			</s:iterator>
		</select></td>
	</tr>

	<tr name="tr_model">
		<td class="title_2" width="15%" align="right">设备厂商：</td>
		<td width="35%">
			<select name="vendor_id" style="width:150px">
			<option value="">请选择：</option>
			<s:iterator var="vlist" value="VendorList">
				<option value="<s:property value="#vlist.vendor_id"/>"><s:property
					value="#vlist.vendor_add" /></option>
			</s:iterator>
		</select></td>
		<td class="title_2" width="15%" align="right">设备型号：</td>
		<td width="35%">
			<select name="device_model" style="width:150px">
				<option value="">请先选择厂商：</option>
			</select>
		</td>
	</tr>

	<tr>
		<td align="right" class="foot" colspan="4">
		<button onclick="Query()">&nbsp;查&nbsp;询&nbsp;</button>
		</td>
	</tr>
</table>
<br>
<table width="94%" align="center" class="listtable">
	<thead>
		<tr>
			<th colspan="6">设备列表：</th>
		</tr>
		<tr>
			<th width="5%"><input type="checkbox" id="chkall"><label
				for="chkall"></label></th>
			<th>属地</th>
			<th>设备名称</th>
			<th>设备IP</th>
			<th>设备序列号</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody name="tbody">
	</tbody>
	<tfoot>
		<tr>
			<td colspan="6" align="right">
				<s:if test="pmeeflg">
					<button onclick="ConfigPmee()">性能配置</button>
				</s:if>
				<s:else>
					<button onclick="ConfigFlux()">流量配置</button>
				</s:else>
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>
