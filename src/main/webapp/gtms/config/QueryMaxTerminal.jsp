<%--
FileName	: proProcess.jsp
Date		: 2013年5月21日
Desc		: 新疆下发上网数设计
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%
request.setCharacterEncoding("gbk");
String gwType = request.getParameter("gw_type");
%>
<%@ include file="../../head.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript">

$(function(){
	gwShare_setGaoji();
});
var device_id = 0;
var gw_type = "<%= gwType%>";

function deviceResult(returnVal){
		
	$("tr[@id='trDeviceResult']").css("display","");

	$("td[@id='tdDeviceSn']").html("");
	$("td[@id='tdDeviceCityName']").html("");
	
	$("input[@name='device_id']").val(returnVal[2][0][0]);
	$("td[@id='tdDeviceSn']").append(returnVal[2][0][1]+" -"+returnVal[2][0][2]);
	$("td[@id='tdDeviceCityName']").append(returnVal[2][0][5]);	
	
	$('#query').attr("disabled",false);
	document.getElementById("args2").style.display="none";
    document.getElementById("tr003").style.display="none";
    document.getElementById("args1").style.display="none";
    document.getElementById("tr002").style.display="none";
	
}

function query(){
	
	
	
	$('#query').attr("disabled",true);
	 document.getElementById("args1").style.display="none";
	 document.getElementById("args2").style.display="none";
	
	var _device_id = $("input[@name='device_id']").val();
	var url = "<s:url value='/gtms/config/configMaxTerminal!maxTerminal.action'/>";
	
	$.post(url,{
		gw_type:gw_type,
		device_id:_device_id
	},querydesc);
}

function querydesc(msg){

	var arr = msg.split("#");
	if("-1"==arr[0]){
		alert(arr[1]);
	}else{
		//清空数据
		$("select[@name='mode']").val("0");
		$("input[@name='ttNumber']").val("");
		
		//添加数据
		//如果取到的默认数为2，默认页面也显示为关闭(0)
		if(arr[1]=="2"){
			$("select[@name='mode']").val("0");
		}else{
			$("select[@name='mode']").val(arr[1]);
		}
		
		$("input[@name='ttNumber']").val(arr[2]);
		
	    document.getElementById("args1").style.display="block";
	    document.getElementById("tr002").style.display="block";
	}
	
	$('#query').attr("disabled",false);
}

function save(){
	
	var maxNum = $("input[@name='ttNumber']").val();
	if(maxNum>16){
		alert("最大上网数不能超过16");
		return false;
	}
	
	$('#save').attr("disabled",true);

	var device_id = $("input[@name='device_id']").val();
	var mode = $("select[@name='mode']").val();
	var total_number = $("input[@name='ttNumber']").val();
	
	var url = "<s:url value='/gtms/config/configMaxTerminal!maxTerminalSave.action'/>";
	
	$.post(url,{
		device_id:device_id,
			mode:mode,
			total_number:total_number
			},savedesc);
}

function savedesc(msg){
	var arr = msg.split("#");
	
	if(arr[0]=="-1"){
		alert(arr[1]);
	}else{
		//保存或刷新页面数据
		 analyData(msg);	
	     document.getElementById("args2").style.display="block";
	     document.getElementById("tr003").style.display="block";
	     $('#query').attr("disabled",false);
	}
	
	$('#save').attr("disabled",false);
}


function refresh(){
	
	var device_id = $("input[@name='device_id']").val();
	
	var url = "<s:url value='/gtms/config/configMaxTerminal!maxTerminalRefresh.action'/>";
	
	$.post(url,{
		device_id:device_id
	},function(ajax){
		analyData(ajax);
	});
}


function analyData(msg){
	var arr = msg.split("#");
	
	if(arr[1]=="-1"){
		 document.getElementById("data").style.display="none";
		 document.getElementById("noData").style.display="block";
	}else if(arr[1]=="-2"){
		 alert("刷新失败！！");
	}else{
		
		document.getElementById("data").style.display="block";
		document.getElementById("noData").style.display="none";
		//清空数据
		$("input[@name='id']").val("");
		$("td[@id='service_name']").html("");
		$("td[@id='start_time']").html("");
		$("td[@id='end_time']").html("");
		$("td[@id='status']").html("");
		$("td[@id='fault_desc']").html("");
		
		//添加数据
		$("input[@name='id']").val(arr[1]);
		$("td[@id='service_name']").append(arr[2]);
		$("td[@id='start_time']").append(arr[3]);
		$("td[@id='end_time']").append(arr[4]);
		$("td[@id='status']").append(arr[5]);
		$("td[@id='fault_desc']").append(arr[6]);
		 
	}
	
}

</SCRIPT>
<%@ include file="../../toolbar.jsp"%>


<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="text">
				<TR>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									最大终端数配置
								</td>
								<td nowrap>
									<img src="../../images/attention_2.gif" width="15" height="12">
									&nbsp;最大终端数配置
								</td>
							</tr>
						</table>
					</td>
				</TR>

				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
					</td>
				</TR>

				<TR  bgcolor="#FFFFFF" id="trDeviceResult" style="display: none">
					<TD colspan="4">
							<TABLE id="tb1" border=0 cellspacing=1 cellpadding=2 width="100%" bgcolor=#999999>
								<TR bgcolor="#FFFFFF" >
									<td nowrap align="right" class=column width="15%">
										设备属地
									</td>
									<td id="tdDeviceCityName" width="35%">
									</td>
									<td nowrap align="right" class=column width="15%">
										设备序列号
										<input type="hidden" name="device_id" value="">
									</td>
									<td id="tdDeviceSn" width="35%">
									</td>
								</TR>
								<TR bgcolor="#FFFFFF">
									<td class="green_foot" colSpan="4" align="right">
										<button id="query" onclick="query()">&nbsp;获取上网数&nbsp;</button>
										<!-- 点击获取上网数,调用采集模块 -->
									</td>
								</TR>
							</TABLE>
					</TD>
				</TR>
				
				<TR bgcolor="#FFFFFF" id="tr002" style="display: none">
					<td valign="top" colspan="4" class=column>
					<div id="div_strategy" style="width: 100%; z-index: 1; top: 100px">
					</div>
					</td>
				</TR>
						
				<!-- 点击“保存”按钮后，调用配置模块 -->
				<TR  id="args1" bgcolor="#FFFFFF" style="display: none">
					<TD colspan="4">
							<table id="changeConnType" border=0 cellspacing=1 cellpadding=2
									width="100%" bgcolor=#999999 style="display: ">
									<TR>
										<TH  align="center">模式</TH>
										<TH  align="center">最大值</TH>
										<TH  align="center">描述</TH>
									</TR>
									<tr bgcolor=#ffffff>
										<td align="center">
											<select name="mode" class="bk">
												 	<option value="0" selected="selected">关闭</option>
													<option value="1">开启</option>
											<select>
										</td>
										<td align="center"><input type="text" name="ttNumber" value=""></td>
										<td>关闭不限制，开启可以设置最大终端数</td>
									</tr>
									<tr bgcolor=#ffffff>
										<td class=foot colspan=4 align=right>
										<button id="save" onclick="save()">&nbsp;保存&nbsp;</button>
										</td>
									</tr>
								</table>
					</TD>
				</TR>
				
				 <TR bgcolor="#FFFFFF" id="tr003" style="display: none">
                                        <td valign="top" colspan="4" class=column>
                                        <div id="div_strategy" style="width: 100%; z-index: 1; top: 100px">
                                        </div>
                                        </td>
               </TR>
			
				
				<!-- 点击“获取上网数”按钮，显示下半部 -->
	
				<TR  id="args2" bgcolor="#FFFFFF" style="display: none">
					<TD colspan="4"> 
							<input type="hidden" name="id" value="" />
							<table  id="listTable" border=0 cellspacing=1 cellpadding=2
									width="100%" bgcolor=#999999>
										<caption style="font-size:10pt;border:1px solid #ACA899;font-weight: bold;background-color: #D1D1D1;" >策略执行情况</caption>
										
									
											<tr>
												<th>业务名称</th>
												<th>执行时间</th>
												<th>下发完成时间</th>
												<th>执行状态</th>
												<th>结果描述</th>
												<th>操作</th>
											</tr>
										
									
											<tr id="data" style="dispay:block;background-color:#ffffff">
												<td id="service_name"></td>
												<td id="start_time"></td>
												<td id="end_time"></td>
												<td id="status"></td>
												<td id="fault_desc"></td>
												<td align="center"><img alt="刷新" src="../../images/refresh.png" onclick="refresh()" id="refresh" style="cursor:pointer"></td>
											</tr>
											<tr id="noData" style="dispay:none;background-color:#ffffff">
												<td colspan="5" align="center"> 后台正在录入策略信息，请刷新</td>
												<td align="center"><img alt="刷新" src="../../images/refresh.png" onclick="refresh()" id="refresh" style="cursor:pointer"></td>
											</tr>
							</table>
					</TD>
				</TR>
			
			</table>
		</TD>
	</TR>
</TABLE>

<%@ include file="../../foot.jsp"%>
