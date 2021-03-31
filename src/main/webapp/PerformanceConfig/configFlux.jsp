<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%--
/**
 * 配置流量页面;
 * BUG:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-Mail:benyp@lianchuang.com
 * @version 1.0
 * @since 2008-10-23 ??03:32:08
 *
 * @版权 南京联创 网管产品部;
 *
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>流量配置</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryCheckForm-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/PerformanceConfig/jquery.configPmee.js"/>"></script>
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/tab.css"/>" rel="stylesheet" type="text/css">
<style type="text/css">
   td.no{
		color:red;
	}
</style>
<script type="text/javascript">
	$(function(){
		var ajax="<s:property value="ajax"/>";
		if(ajax=="true" || ajax=="false"){
			if(ajax=="true"){
				alert("已成功进入线程配置，请等待片刻查看配置结果，配置需要一段时间！");
			}else{
				alert("配置失败，请重新配置！");
			}
			window.close();
		}
		var isbatch="<s:property value="isbatch"/>";
		if(isbatch=='true'){
			window.setInterval("getConfigFlux();",1000*60*2);//刷新获取已经配置的性能表达式
		}
		//采集方式切换
		$("input[@name='auto']").click(function(){
			$("#tr_coltype").toggle();
		});
		//总体配置切换
		$("input[@name='total']").click(function(){
			$("#tr_total").toggle();
			getPort();
		});
		//显示隐藏配置告警框
		$("td[@name='td_show']").toggle(function(){
			$(this).html("隐藏配置 <img src='<s:url value="/images/ico_9_up.gif"/>' width='10' hight='12' align='center' border='0' alt='点击隐藏配置告警' >");
			$("#tr_show").show();
			getWarn();
		},function(){
			$(this).html("配置告警 <img src='<s:url value="/images/ico_9.gif"/>' width='10' hight='12' align='center' border='0' alt='点击配置告警' >");
			$("#tr_show").hide();
		});
	});
	//获取端口
	function getPort(){
		if($("#div_port").html()==""){
			$("#div_port").html("正在获取端口信息，请等待........");
			$.post(
				"<s:url value="/performance/configFlux!getDevPortStr.action"/>",
				{
					device_id:"<s:property value="device_id"/>",
					coltype:$("input[@name='coltype'][@checked]").val(),
					auto:$("input[@name='auto'][@checked]").val(),
					serial:"<s:property value="serial"/>"
				},
				function(data){
					data="<table width='100%' class='listtable'>"
					     +"<thead><tr><th width='10%'>"
					     +"<input type='checkbox' onclick='chkAllPort()' id='sel_1'><label for='sel_1'>全选</label></th>"
					     +"<th width='45%'>采集方式</th><th width='45%'>采集参数</th>"+data+"</table>";
					  $("#div_port").html($(data));
				}
			);
	    }
	}
	function chkAllPort(){
		var chk=$("#sel_1").attr("checked");
		chk=chk?chk:false;
		$("input[@name='chk']").attr("checked",chk);
	}
	function dualPort(){
		$("#_div_port_flg").hide();

	}
	//获取告警页面
	function getWarn(){
		if($("#td_warn").html()==""){
			$("#td_warn").html("正在获取告警配置项，请等待......");
			$.post(
				"<s:url value="/performance/configFlux!getWarnJSP.action"/>",
				function(data){
					$("#td_warn").html($(data));
				}
			);
		}
	}
	//获取设备配置状态
	function getConfigFlux(){
		$.post(
			"<s:url value="/performance/configFlux!getConfigResult.action"/>",
			{
				device_id:"<s:property value="device_id"/>"
			},
			function(data){
				$("#tbody").html(data);
			}
		);
	}
	//显示端口详细信息
	function showPortDetail(device_name,loopback_ip,device_id){
		var url="<s:url value="/performance/configFlux!getConfigPortList.action"/>?device_id="+device_id
		+"&device_name="+device_name+"&loopback_ip="+loopback_ip+"&t="+new Date();
		window.open(url);
	}
	//删除配置
	function DelConfig(device_id){
		if(window.confirm("删除后将不会继续采集，确认要删除配置？")){
			$.post(
				"<s:url value="/performance/configFlux!DelFlux.action"/>",
				{
					device_id:device_id
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
	}
	//
	function SaveFlux(device_id){
		//告警
		if($("#td_warn").html()!=""){
			if(!$.checkFluxWarn()){
				return false;
			}
		}
		//端口
		if($("input[@name='total'][@checked]").val()==0){
			if($("input[@name='chk'][@checked]").length<1){
				alert("请至少选择一个端口！");
				return false;
			}
			var chk="";
			var param="";
			var flg=true;
			$("input[@name='chk'][@checked]").each(function(){
				chk+="-/-"+$(this).val();
				if($(this).parent().parent().find("table input[@checked]").length<1){
					flg=false;
				}
				$(this).parent().parent().find("table input[@checked]").each(function(){
					chk+="|||"+$(this).val();
				});
			});
			if(!flg){
				alert("采集参数不能为空！请至少选择一个采集参数！");
				return false;
			}
			chk=chk.substring(3);
			$("input[@name='port_info']").val(chk);
		}
		$("input[@name='dev_id']").val(device_id);
		$("form[@name='frm']").submit();
		return false;
	}
</script>
</head>
<body>
	<form name="frm" action="<s:url value="/performance/configFlux!saveFlux.action"/>" method="post">
	<input type="hidden" name="dev_id">
	<input type="hidden" name="device_id" value="<s:property value="device_id"/>">
	<input type="hidden" name="port_info">
	<br>
	<table width="94%" align="center" class="querytable">
		<tr>
			<s:if test="isbatch">
				<th colspan="2" class="title_1">流量批量配置</th>
			</s:if>
			<s:else>
				<th colspan="2" class="title_1">流量配置(<s:property value="device_name"/>【<s:property value="loopback_ip"/>】)</th>
			</s:else>
		</tr>
		<tr style="display:none">
			<td width="20%" class="title_2">自动配置</td>
			<td width="80%">
				<input type="radio" name="auto" value="1" checked id="a_0"><label for="a_0">&nbsp;是&nbsp;</label>&nbsp;
				<input type="radio" name="auto" value="0" id="a_1"><label for="a_1">&nbsp;否&nbsp;</label>&nbsp;
				<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">注：请尽量选择自动配置
			</td>
		</tr>
		<tr id="tr_coltype" style="display:none;">
			<td class="title_2">采集方式</td>
			<td>
				<input type="radio" name="coltype" value="2_64" checked id="c_0"><label for="c_0">V2版本64位计数器</label>&nbsp;
				<input type="radio" name="coltype" value="1_32" id="c_1"><label for="c_1">V1版本32位计数器</label>&nbsp;
				<input type="radio" name="coltype" value="2_32" id="c_2"><label for="c_2">V2版本32位计数器 </label>&nbsp;
			</td>
		</tr>
		<tr style="display:<s:property value="isbatch?'none;':''"/>">
			<td class="title_2">总体配置</td>
			<td>
				<input type="radio" name="total" value="1" checked id="t_0"><label for="t_0">&nbsp;是&nbsp;</label>&nbsp;
				<input type="radio" name="total" value="0" id="t_1"><label for="t_1">&nbsp;否&nbsp;</label>&nbsp;
				<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">注：如端口过多请尽量选择总体配置
			</td>
		</tr>
		<tr>
			<td class="title_2">采集间隔时间</td>
			<td>
				<input type="text" name="polltime" value="900" size="10">&nbsp;&nbsp;
				<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">注：采集间隔时间在900秒左右比较合适
			</td>
		</tr>
		<tr>
			<td class="title_2">原始数据是否入库</td>
			<td>
				<input type="radio" name="intodb" value="1" checked id="i_0"><label for="i_0">&nbsp;是&nbsp;</label>&nbsp;
				<input type="radio" name="intodb" value="0" id="i_1"><label for="i_1">&nbsp;否&nbsp;</label>&nbsp;
			</td>
		</tr>
		<tr>
			<td class="title_2">是否保留原有配置</td>
			<td>
				<select name="iskeep">
					<option value="true">保留原有配置</option>
					<option value="false">重新配置</option>
				</select>
			</td>
		</tr>
		<tr style="display:<s:property value="isbatch?'none;':''"/>">
			<td class="title_2">配置状态</td>
			<td class="<s:property value="configresult==true?'':'no'"/>">
				<s:property value="configresult==true?'已配置':'未配置'"/>
			</td>
		</tr>
		<tr>
			<td colspan="2" class="title_1" name="td_show">
				配置告警
				<img src="<s:url value="/images/ico_9.gif"/>" width="10" hight="12" align="center" border="0" alt="点击配置告警" >
			</td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr id="tr_show" style="display:none;">
			<td id="td_warn" colspan="2">

			</td>
		</tr>
		<tr id="tr_total" style="display:none;">
			<td colspan="2">
				<div id="div_port" style="width:100%;height:350px;overflow:auto;">

				</div>
			</td>
		</tr>
		<tr>
			<td class="foot" colspan="2" align="right">
				<s:if test="isbatch">
					<button name="save" onclick="SaveFlux('<s:property value="device_id"/>');">&nbsp;配&nbsp;置&nbsp;</button>
				</s:if>
				<s:elseif test="configresult">
					<button name="save" onclick="SaveFlux('<s:property value="device_id"/>');">&nbsp;刷新配置&nbsp;</button>
					<button name="b_list" onclick="showPortDetail('<s:property value="device_name"/>',
					                                              '<s:property value="loopback_ip"/>',
					                                              '<s:property value="device_id"/>')">&nbsp;端口列表&nbsp;</button>
					<button name="del" onclick="DelConfig('<s:property value="device_id"/>')">&nbsp;删除配置&nbsp;</button>
				</s:elseif>
				<s:else>
					<button name="save" onclick="SaveFlux('<s:property value="device_id"/>');">&nbsp;配&nbsp;置&nbsp;</button>
				</s:else>
				<s:if test="!ismodule">
					<button onclick="window.close();">&nbsp;关&nbsp;闭&nbsp;</button>
				</s:if>
			</td>
		</tr>
	</table>
	<br>
	<s:if test="isbatch">
		<table width="94%" align="center" class="listtable">
			<thead>
				<tr>
					<th width="20%">设备名称</th>
					<th width="20%">设备IP</th>
					<th width="10%">设备厂商</th>
					<th width="20%">设备型号</th>
					<th width="10%">配置情况</th>
					<th width="20%">操作</th>
				</tr>
			</thead>
			<tbody id="tbody">
				<s:iterator var="cl" value="configList" status="rowstatus">
					<tr class="<s:property value="#rowstatus.odd==true?'odd':'even'"/>"
						onmouseover="className='odd'"
						onmouseout="className='<s:property value="#rowstatus.odd==true?'odd':'even'"/>'"
					>
						<td><s:property value="#cl.device_name"/></td>
						<td><s:property value="#cl.loopback_ip"/></td>
						<td><s:property value="#cl.vendor_name"/></td>
						<td><s:property value="#cl.device_model"/></td>
						<td class="<s:property value="#cl.result=='true'?'':'no'"/>"><s:property value="#cl.result=='true'?'已配置':'未配置'"/></td>
						<s:if test="#cl.result">
							<td>
								<a href="#" onclick="SaveFlux('<s:property value="#cl.device_id"/>');">刷新配置</a>
								<a href="#" onclick="showPortDetail('<s:property value="#cl.device_name"/>',
								                                    '<s:property value="#cl.loopback_ip"/>',
								                                    '<s:property value="#cl.device_id"/>')">端口列表</a>
								<a href="#" onclick="DelConfig('<s:property value="#cl.device_id"/>')">删除配置</a>
							</td>
						</s:if>
						<s:else>
							<td>
								<a href="#" onclick="SaveFlux('<s:property value="#cl.device_id"/>');">配置流量</a>
							</td>
						</s:else>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</s:if>
	</form>
</body>
</html>
