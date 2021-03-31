<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<%@ include file="/toolbar.jsp"%>


<html>
<head>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>
<lk:res />

<SCRIPT LANGUAGE="JavaScript">
$(function(){
	Init();
});


function Init(){
	// 初始化厂家
	change_select("vendor","-1");
}

function doQuery(){
	var url = "<s:url value='/gwms/diagnostics/deviceDiagnostics!getCheckList.action'/>";
	document.mainForm.submit();
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
			<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION="<s:url value='/gwms/diagnostics/deviceDiagnostics!getCheckList.action'/>" target="dataForm">
			<table width="98%" height="30" border="0" align="center" cellpadding="0" cellspacing="0" class="green_gargtd">
				<tr>
					<td width="162"> <div align="center" class="title_bigwhite">自动化检测查询</div> </td>
					<td><img src="/itms/images/attention_2.gif" width="15" height="12"></td>
				</tr>
			</table>
			<!-- 查询part begin -->
			<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
				<tr>
					<td bgcolor=#999999>
					<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center">
						<tr>
							<th colspan="4" id="gwShare_thTitle">查询</th>
						</tr>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD align="right" class=column width="15%">测试账号</TD>
							<TD align="left" width="35%">
								<input type="text" id="account" name="account" class="bk" value="">
							</TD>
							<TD align="right" class=column width="15%">设备序列号</TD>
							<TD width="35%">
								<input type="text" id="device_serialnumber" name="device_serialnumber" class="bk" value="">
							</TD>
						</TR>
						
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD align="right" class=column width="15%">设备厂商</TD>
							<TD width="35%">
								<select name="vendor" class="bk" onchange="change_select('deviceModel','-1')">
									<option value="-1">==请选择==</option>
								</select>
							</TD>
							<TD align="right" class=column width="15%">设备型号</TD>
							<TD width="35%">
								<select name="device_model" class="bk" onchange="change_select('softwareversion','-1');change_select('devicetypeHD','-1');">
									<option value="-1">==请先选择设备厂商==</option>
								</select>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD align="right" class=column width="15%">硬件版本</TD>
							<TD align="left" width="35%">
								<select name="hardwareversion" class="bk"">
									<option value="-1">==请先选择设备型号==</option>
								</select>
							</TD>
							<TD align="right" class=column width="15%">软件版本</TD>
							<TD align="left" width="35%">
								<select name="softwareversion" class="bk"">
									<option value="-1">==请先选择设备型号==</option>
								</select>
							</TD>
						</TR>
						
						<tr bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<td align="right" class=column width="15%">开始时间</td>
							<td align="left" width="35%">
								<input type="text" name="startTime" id="startTime" readonly
														value="" class=bk>
								<img name="shortDateimg" onClick="WdatePicker({el:'startTime',dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="选择">
							</td>
							<td align="right" class=column width="15%">结束时间</td>
							<td align="left" width="35%">
								<input type="text" name="endTime" id="endTime" readonly
														value="" class=bk>
								<img name="shortDateimg" onClick="WdatePicker({el:'endTime',dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="选择">
							</td>
						</tr>
						
						<tr bgcolor="#FFFFFF">
							<td colspan="4" align="right" class="green_foot" width="100%">
								<input type="button" class=jianbian onclick="javascript:doQuery()" name="queryButton" value=" 查 询 " /> 
								<input type="button" class=jianbian onclick="javascript:resetFrm();" name="reButto" value=" 重 置 "  />
							</td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			
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
		</FORM>	
		</TD>
	</TR>
</TABLE>
</body>

<%@ include file="/foot.jsp"%>

<SCRIPT LANGUAGE="JavaScript">
	
	function change_select(type,selectvalue){
		switch (type){
			case "city":
				var url = "<s:url value='/gwms/share/gwDeviceQuery!getCityNextChild.action'/>";
				$.post(url,{
				},function(ajax){
					parseMessage(ajax,$("select[@name='gwShare_cityId']"),selectvalue);
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
			case "softwareversion":
				var url = "<s:url value='/gwms/share/gwDeviceQuery!getDevicetypeST.action'/>";
				var deviceModelId = $("select[@name='device_model']").val();
				if("-1" == deviceModelId){
					$("select[@name='softwareversion']").html("<option value='-1'>==请先选择设备型号==</option>");
					break;
				}
				$.post(url,{
					gwShare_deviceModelId:deviceModelId
				},function(ajax){
					parseMessage(ajax,$("select[@name='softwareversion']"),selectvalue);
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
		$("select[@name='softwareversion']").html("<option value=-1>--请先选择型号--</option>");
	}
	
	
	$(function(){
		dyniframesize();
	});

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
	
</SCRIPT>

</html>