<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>版本统计优化</title>
<%
	/**
	 * 版本统计优化
	 * 
	 * @author gaoyi
	 * @version 1.0
	 * @since 2013-09-10
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	function query() {
		$("#qy").attr('disabled',true);
		showMsgDlg();
		var url = "<s:url value='/itms/resource/DeviceService!deviceServiceInfo.action'/>";
		$("input[@name='isprt']").val("0");
		$("input[@name='isflag']").val("0");
		var starttime = $("input[@name='startOpenDate']").val();
		var endtime = $("input[@name='endOpenDate']").val();
		var vendorId = $("select[@name='vendorId']").val();
		var modelId = $("select[@name='modelId']").val();
		var deviceTypeId = $("select[@name='deviceTypeId']").val();
		var rela_dev_type_id = $("select[@name='rela_dev_type_id']").val();
		var gw_type = $("input[@name='gw_type']").val();
		$.post(url,{startOpenDate:starttime,
			endOpenDate:endtime,
			vendorId:vendorId,
			modelId:modelId,
			deviceTypeId:deviceTypeId,
			rela_dev_type_id:rela_dev_type_id,
			gw_type:gw_type},function(ajax){
				$("div[@id='QueryData']").html("");
				$("div[@id='QueryData']").append(ajax);
				closeMsgDlg();
			});
	}

	function change_select(str){
		if("deviceModel"==str){
			$("select[@name='modelId']").html("");
			$("select[@name='deviceTypeId']").html("");
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='modelId']").html("<option value='-1'>==请先选择厂商==</option>");
				$("select[@name='deviceTypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				$("select[@name='modelId']").attr('disabled',true);
				$("select[@name='deviceTypeId']").attr('disabled',true);
				return;
			}
			$("select[@name='modelId']").attr('disabled',false);
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				parseMessage(ajax,$("select[@name='modelId']"));
				$("select[@name='deviceTypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
		}
		
		if("deviceType"==str){
			$("select[@name='deviceTypeId']").html("");
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			var deviceModelId = $("select[@name='modelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='deviceTypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				$("select[@name='deviceTypeId']").attr('disabled',true);
				return;
			}
			$("select[@name='deviceTypeId']").attr('disabled',false);
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				parseMessage(ajax,$("select[@name='deviceTypeId']"));
			});
		}
	}
	
	//解析查询设备型号返回值的方法
	function parseMessage(ajax,field){
		var flag = true;
		if(""==ajax){
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
			option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
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
	
	function detailVendor(vendorId,modelId,softwareversion,gw_type){
		showMsgDlg();
		var url = "<s:url value='/itms/resource/DeviceService!detailVendor.action'/>";
		var starttime = $("input[@name='startOpenDate']").val();
		var endtime = $("input[@name='endOpenDate']").val();
		var rela_dev_type_id = $("select[@name='rela_dev_type_id']").val();
		var isflag = "1";
		$.post(url,{startOpenDate:starttime,
			endOpenDate:endtime,
			vendorId:vendorId,
			modelId:modelId,
			rela_dev_type_id:rela_dev_type_id,
			softwareversion:softwareversion,
			isflag:isflag,
			gw_type:gw_type},function(ajax){
				$("div[@id='QueryData']").html("");
				$("div[@id='QueryData']").append(ajax);
				closeMsgDlg();
			});
	}
	
	function excelDeviceVendor(vendorId,modelId,softwareversion,gw_type){
		$("input[@name='isprt']").val("1");
		$("input[@name='isflag']").val("1");
		$("input[@name='gw_type']").val(gw_type);
		$("input[@name='vid']").val(vendorId);
		$("input[@name='mid']").val(modelId);
		$("input[@name='softwareversion']").val(softwareversion);
		var mainForm = document.getElementById("selectForm");
		mainForm.action = "<s:url value='/itms/resource/DeviceService!excelDevice.action'/>";
		mainForm.submit();
		mainForm.action = "<s:url value='/itms/resource/DeviceService!deviceServiceInfo.action'/>"
	}
	
	function excelDevice(isprt){
			$("input[@name='isprt']").val(isprt);
			var mainForm = document.getElementById("selectForm");
			mainForm.action = "<s:url value='/itms/resource/DeviceService!excelDevice.action'/>"
			mainForm.submit();
			mainForm.action = "<s:url value='/itms/resource/DeviceService!deviceServiceInfo.action'/>"
	}
	
	//初始化的时候调用
	function showMsgDlg(){
			w = document.body.clientWidth;
			h = document.body.clientHeight;
			
			l = (w-250)/2;
			t = h/2-100;
			PendingMessage.style.left = l;
			PendingMessage.style.top  = t;
			PendingMessage.style.display="";
	}

	//完成数据，隐藏页面
	function closeMsgDlg(){
			$("#qy").attr('disabled',false);
			PendingMessage.style.display="none";
	}
	
	//查看详细信息
	function detail(city_id,softwareversion,hardwareversion, gw_type){
		var vendorId = $("select[@name='vendorId']").val();
		var startOpenDate = $("input[@name='startOpenDate']").val();
		var endOpenDate = $("input[@name='endOpenDate']").val();
        var page =  "<s:url value='/itms/resource/DeviceService!DeviceVendorList.action'/>?city_id="+city_id+"&softwareversion="+softwareversion + "&hardwareversion="+hardwareversion+"&gw_type=" + gw_type + "&vendorId="+vendorId+"&startOpenDate="+startOpenDate+"&endOpenDate="+endOpenDate;
		//alert(page);
		window.open(page,"","left=20,top=20,width=900,height=600,resizable=no,scrollbars=yes");
	}
	
</script>
</head>

<body>
	<form id="form" name="selectForm"
		action="<s:url value='/itms/resource/DeviceService!deviceServiceInfo.action'/>">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
							版本统计优化</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">版本统计优化查询</th>
							<input type="hidden" name="gw_type" value="<%=request.getParameter("gw_type")  %>" />
							<input type="hidden" name="isprt" value="0" />
							<input type="hidden" name="isflag" value="0" />
							<input type="hidden" name="vid" value="-1" />
							<input type="hidden" name="mid" value="-1" />
							<input type="hidden" name="softwareversion" value="" />
						</tr>
						<TR>
							<TD class=column width="15%" align='right'>开始时间</TD>
							<TD width="35%">
									<input type="text" name="startOpenDate" readonly class=bk
										value="<s:property value="startOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择">
							</TD>
							<TD class=column width="15%" align='right'>结束时间</TD>
							<TD width="35%">
									<input type="text" name="endOpenDate" readonly class=bk
										value="<s:property value="endOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择">
							</TD>
						</TR>


						<TR>

							<TD class=column width="15%" align='right'>厂商</TD>
							<TD width="35%"><s:select list="vendorMap" name="vendorId" onchange="change_select('deviceModel')"
									headerKey="-1" headerValue="请选择厂商" listKey="key"
									listValue="value" cssClass="bk"></s:select></TD>

							<TD class="column" width='15%' align="right">设备型号</td>
							<td width='35%' align="left"><select name="modelId" class="bk" disabled="disabled"
								onchange="change_select('deviceType')">
								<option value="-1" selected="selected">请先选择厂商</option>
								</select></TD>
						</TR>
						<TR>
							<TD class="column" width='15%' align="right">版本</TD>
							<TD width='35%' align="left"><select name="deviceTypeId" class="bk" disabled="disabled">
								<option value="-1" selected="selected">请先选择型号</option>
							</select></TD>
							<TD class="column" width='15%' align="right">设备类型</TD>
							<TD width='35%' align="left"><select name="rela_dev_type_id" class="bk">
									<option value="-1">==请选择==</option>
									<option value="1">e8-b</option>
									<option value="2">e8-c</option>
									</select></TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class=foot>
								<button id="qy" onclick="javaScript:query()" >&nbsp;过&nbsp;&nbsp;&nbsp;虑&nbsp;</button>
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
			<tr>
				<td>
				<div id="PendingMessage"
						style="position:absolute;z-index:3;top:240px;left:250px;width:300;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;filter:alpha(opacity=80);display:none">
							<center>
									<table border="0" style="background-color:#eeeeee">
										<tr>
											<td valign="middle"><img src="<s:url value='/images/cursor_hourglas.gif'/>"  
												border="0" WIDTH="30" HEIGHT="30"></td>
											<td>&nbsp;&nbsp;</td>
											<td valign="middle"><span id=txtLoading
												style="font-size:14px;font-family: 仿宋">请稍等,版本统计优化信息······</span></td>
										</tr>
									</table>
							</center>
				</div>
				<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				</div>
			</tr>
			
			<tr>
				<td height="25"></td>
			</tr>
			<tr>
				<td id="bssSheetInfo"></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>