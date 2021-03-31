<%@ include file="/timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%><%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ include file="/toolbar.jsp"%>


<html>
<head>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<lk:res />

<SCRIPT LANGUAGE="JavaScript">
var area = "<%=LipossGlobals.getLipossProperty("InstArea.ShortName")%>";

$(function(){
	$("input[@name='gwShare_queryResultType']").val("checkbox");
	gwShare_setGaoji();
	gwShare_setImport();
	
	var vendor='<s:property value="vendor"/>';
	var device_model='<s:property value="device_model"/>';
	var devicetypeId='<s:property value="devicetypeId"/>';
	// 初始化厂家
	change_select("city","-1");
	change_select("vendor",vendor);
	setTimeout("change_select('deviceModel',"+device_model+")",1000);
	setTimeout("change_select('devicetype',"+devicetypeId+")",2500);
	Init(vendor,device_model,devicetypeId);
});


function Init(vendor,device_model,devicetypeId){
	// 普通方式提交
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/itms/resource/deviceWhiteList!queryWhiteList.action'/>?vendor="+vendor+"&device_model="+device_model+"&devicetypeId="+devicetypeId
	form.submit();
}

function doQuery(){
	// 普通方式提交
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/itms/resource/deviceWhiteList!queryWhiteList.action'/>";
	form.submit();
}

var deviceIds = "";
function deviceResult(returnVal){
	deviceIds="";
	if(returnVal[0]==0){
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[2].length+"</strong></font>");
		for(var i=0;i<returnVal[2].length;i++){
			deviceIds = deviceIds + returnVal[2][i][0]+",";			
		}
		$("input[@name='deviceIds']").val(deviceIds);
	}else{
		if(returnVal[0]>400000){
			alert("定制数量不可以超过40万！");
			return;
		}
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[0]+"</strong></font>");
		$("input[@name='deviceIds']").val("0");
		$("input[@name='param']").val(returnVal[1]);
	}
}

function add(){
	var deviceIds = $("input[@name='deviceIds']").val();
	var param = $("input[@name='param']").val();
	var gw_type = $("input[@name='gw_type']").val();
	var task_name = $("input[@name='task_name']").val();
	
	if(deviceIds == ""){
		alert("请选择设备");
		return false;
	}
	
	if(task_name==""){
		alert("请输入任务名称！");
		return false;
	}
		
	$("button[@id='btn']").attr("disabled",true);
	$("tr[@id='trData']").show();
	$("div[@id='QueryData']").html("正在执行批量定制操作，请稍等....");
	url = "<s:url value='/itms/resource/deviceWhiteList!addWhiteList.action'/>";
	$.post(url,{
		task_name : task_name,
		gw_type : gw_type,
		deviceIds : deviceIds,
		param : param
	},function(ajax){
		$("div[@id='QueryData']").html(ajax);
		$("button[@id='btn']").attr("disabled",false);
		alert(ajax);
		$("table[@id='addTable']").hide();
		 location.reload();
	});
}

</SCRIPT>
</head>

<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION="" target="dataForm">
		<table width="98%" height="30" border="0" align="center" cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162"> <div align="center" class="title_bigwhite">设备白名单管理</div> </td>
				<td><img src="/itms/images/attention_2.gif" width="15" height="12"></td>
			</tr>
		</table>
		<!-- 查询part begin -->
		<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
			<tr>
				<td bgcolor=#999999>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center">
					<tr>
						<th colspan="4" id="gwShare_thTitle">设备白名单管理</th>
					</tr>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="15%">属地</TD>
						<TD align="left" width="35%" colspan="3">
							<select name="cityId" class="bk">
								<option value="-1">==请选择==</option>
							</select>
						</TD>
					</TR>
					
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="15%">设备厂商</TD>
						<TD align="left" width="35%">
							<select name="vendor" class="bk" onchange="change_select('deviceModel','-1')">
							</select>
						</TD>
						<TD align="right" class=column width="15%">设备型号</TD>
						<TD width="35%">
							<select name="device_model" class="bk" onchange="change_select('devicetype','-1');change_select('devicetypeHD','-1');">
							</select>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="15%">设备版本</TD>
						<TD align="left" width="35%" >
							<select name="devicetypeId" class="bk"">
							</select>
						</TD>
						<TD align="right" class=column width="15%">设备序列号</TD>
						<TD align="left" width="35%">
							<input name="deviceSerialnumber" value="">
						</TD>
					</TR>
					<tr bgcolor="#FFFFFF">
						<td colspan="4" align="right" class="green_foot" width="100%">
							<input type="button" class=jianbian onclick="javascript:doQuery()" name="queryButton" value=" 查 询 " /> 
							<input type="button" class=jianbian onclick="javascript:resetFrm();" name="reButto" value=" 重 置 "  />
							<input type="button" class=jianbian onclick="javascript:doAdd()" name="addButto" value=" 新 增 " />
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</FORM>
		<!-- 查询part end -->
		
		<!-- 展示结果part begin -->
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999 id="idData">
					<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe>
				</TD>
			</TR>
		</TABLE>
		<!-- 展示结果part end -->
		<br/><br/>
		<!-- 添加和编辑part begin --> 
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center" id="addTable" style="display: none">
			<tr>
				<th colspan="4" id="">批量配置设备白名单</th>
			</tr>
			<TR bgcolor="#FFFFFF">
				<td colspan="4">
					<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
				</td>
			</TR>
				<tr>
					<td>
						<FORM NAME="frm" METHOD="post" ACTION="" target="childFrm" >
							<input type="hidden" name="deviceIds" value="" />
							<input type="hidden" name="param" value="" />
							<input type="hidden" name="gw_type" value="1" />
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<tr bgcolor="#FFFFFF">
												<td colspan="4">
													<div id="selectedDev">
														请查询设备！
													</div>
												</td>
											</tr>
											
											<tr bgcolor="#FFFFFF">
												<TD colspan="1" class="column" >
													任务名称：
												</TD>
												<TD colspan="3" class="column" >
													<input type="text" name="task_name" class="bk" value=""/>
												</TD>
											</tr>
						
											<tr bgcolor=#ffffff>
												<td class=foot colspan=4 align=right>
													<button id="btn" onclick="add();">&nbsp;定&nbsp;制&nbsp;</button>
												</td>
											</tr>
																	
											<tr id="trData" style="display: none" bgcolor=#ffffff>
												<td colspan=4>
													<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
														正在努力为您查询，请稍等....</div>
												</td>
											</tr>						
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</FORM>
					</td>
				</tr>
		</TABLE>
		<!-- 添加和编辑part end --> 
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>&nbsp;</TD>
	</TR>

</TABLE>
</body>

<%@ include file="/foot.jsp"%>

<SCRIPT LANGUAGE="JavaScript">
	var area = "<%=LipossGlobals.getLipossProperty("InstArea.ShortName")%>";

	


	//添加
	function doAdd() {
		showAddPart(true);
	}
	
	// 隐藏页面下面的添加区域
	function showAddPart(tag){
		if(tag)
			$("table[@id='addTable']").show();
		else
			$("table[@id='addTable']").hide();
	}
		

	function change_select(type,selectvalue){
		switch (type){
			case "city":
				var url = "<s:url value='/gwms/share/gwDeviceQuery!getCityNextChild.action'/>";
				$.post(url,{
				},function(ajax){
					parseMessage(ajax,$("select[@name='cityId']"),selectvalue);
				});
				break;
			case "vendor":
				var url = "<s:url value='/gwms/share/gwDeviceQuery!getVendor.action'/>";
				$.post(url,{
				},function(ajax){
					parseMessage(ajax,$("select[@name='vendor']"),selectvalue);
					parseMessage(ajax,$("select[@name='vendor_add']"),selectvalue);
				});
				break;
			case "deviceModel":
				var url = "<s:url value='/gwms/share/gwDeviceQuery!getDeviceModel.action'/>";
				var vendorId = $("select[@name='vendor']").val();
				if("-1" == vendorId){
					$("select[@name='device_model']").html("<option value='-1'>==请先选择设备厂商==</option>");
					break;
				}
				$.post(url,{
					gwShare_vendorId:vendorId
				},function(ajax){
					parseMessage(ajax,$("select[@name='device_model']"),selectvalue);
				});
				break;
			case "devicetype":
				var url = "<s:url value='/gwms/share/gwDeviceQuery!getDevicetype.action'/>";
				var deviceModelId = $("select[@name='device_model']").val();
				if("-1" == deviceModelId){
					$("select[@name='devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
					break;
				}
				$.post(url,{
					gwShare_deviceModelId:deviceModelId
				},function(ajax){
					parseMessage(ajax,$("select[@name='devicetypeId']"),selectvalue);
				});
				break;	
			case "devicetypeHD":
				var url = "<s:url value='/gwms/share/gwDeviceQuery!getDevicetypeHD.action'/>";
				var deviceModelId = $("select[@name='device_model']").val();
				if("-1" == deviceModelId){
					$("select[@name='hardwareversion']").html("<option value='-1'>==请先选择设备型号==</option>");
					break;
				}
				$.post(url,{
					gwShare_deviceModelId:deviceModelId
				},function(ajax){
					parseMessage(ajax,$("select[@name='hardwareversion']"),selectvalue);
				});
				break;	
			default:
				alert("未知查询选项！");
				break;
		}	
	}
	

	//解析查询设备型号返回值的方法
	function parseMessage(ajax,field,selectvalue){
		var flag = true;
		if(""==ajax){
			option = "<option value='-1' selected>==此项没有记录==</option>";
			field.html(option);
			return;
		}
		var lineData = ajax.split("#");
		if(!typeof(lineData) || !typeof(lineData.length)){
			return false;
		}
		field.html("");
	
		option = "<option value='-1' selected>==请选择==</option>";
		field.append(option);
		for(var i=0;i<lineData.length;i++){
			var oneElement = lineData[i].split("$");
			var xValue = oneElement[0];
			var xText = oneElement[1];
			if(selectvalue==xValue){
				flag = false;
				//根据每组value和text标记的值创建一个option对象
				option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
			}else{
				//根据每组value和text标记的值创建一个option对象
				option = "<option value='"+xValue+"'>=="+xText+"==</option>";
			}
			try{
				field.append(option);
			}catch(e){
				alert("设备型号检索失败！");
			}
		}
		if(flag){
			field.attr("value","-1");
		}
	}

	function resetFrm(){
		document.mainForm.vendor.value = "-1";
		$("select[@name='device_model']").html("<option value=-1>--请先选择厂商--</option>");
		$("select[@name='devicetypeId']").html("<option value=-1>--请先选择型号--</option>");
	}


	//** iframe自动适应页面 **//
	//输入你希望根据页面高度自动调整高度的iframe的名称的列表
	//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
	//定义iframe的ID
	var iframeids=["dataForm"]
	
	//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
	var iframehide="yes"
	
	function dyniframesize() 
	{
		var dyniframe=new Array()
		for (i=0; i<iframeids.length; i++)
		{
			if (document.getElementById)
			{
				//自动调整iframe高度
				dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera)
				{
	     			dyniframe[i].style.display="block"
	     			//如果用户的浏览器是NetScape
	     			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
	      				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
	      			//如果用户的浏览器是IE
	     			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
	      				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
	   			 }
	   		}
			//根据设定的参数来处理不支持iframe的浏览器的显示问题
			if ((document.all || document.getElementById) && iframehide=="no")
			{
				var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
	    		tempobj.style.display="block"
			}
		}
	}
	
	
	$(function(){
		dyniframesize();
	});
	
	$(window).resize(function(){
		dyniframesize();
	}); 
</SCRIPT>

</html>