<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
			type="text/css">
		<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<lk:res />
<SCRIPT LANGUAGE="JavaScript">
//系统类型
$(function(){
	$("input[@name='gwShare_queryResultType']").val("checkbox");
	gwShare_setGaoji();
	gwShare_setImport();
});

$(function()
{
if(batchType!=0)
	{
	var gwShare_import_value = $("input[@name='gwShare_import_value']").val();
	var gwShare_upimport_value = $("input[@name='gwShare_upimport_value']").val();
	$("tr[@id='gwShare_tr12']").css("display","none");
	//$("th[@id='gwShare_thTitle']").css("display","none");
	//$("tr[@id='gwShare_tr11']").css("display","none"); 
	$("th[@id='gwShare_thTitle']").html("高 级 查 询");
	$("input[@name='gwShare_queryType']").val("2");
	$("input[@name='gwShare_gaoji']").css("display","none");
	$("input[@name='gwShare_import']").css("display",gwShare_import_value);
	$("input[@name='gwShare_up_import']").css("display",gwShare_upimport_value);
	$("tr[@id='gwShare_tr11']").css("display","none");
	$("tr[@id='gwShare_tr12']").css("display","none");
	$("tr[@id='gwShare_tr21']").css("display","");
	$("tr[@id='gwShare_tr22']").css("display","");
	$("tr[@id='gwShare_tr23']").css("display","");
	$("tr[@id='gwShare_tr24']").css("display","");
	$("tr[@id='gwShare_tr31']").css("display","none");
	$("tr[@id='gwShare_tr32']").css("display","none");
	$("input[@name='gwShare_queryButton']").val(" 查 询 ");
	//加载相关项
	gwShare_change_select("city","-1");
	gwShare_change_select("vendor","-1");
	}
});
$(function(){
	if(batchType=1)
		{
	$("input[@name='1']").val("执行");
		}
	if(batchType=3)
	{
$("input[@name='3']").val("执行");
	}
});

function deviceResult(returnVal){	
	deviceIds="";
	if(returnVal[0]==0){
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[2].length+"</strong></font>");
		for(var i=0;i<returnVal[2].length;i++){
			deviceIds = deviceIds + returnVal[2][i][0]+",";			
		}
		alert(deviceIds+"=====1");
		$("input[@name='deviceIds']").val(deviceIds);
	}else{
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[0]+"</strong></font>");
		$("input[@name='deviceIds']").val("0");
		$("input[@name='param']").val(returnVal[1]);
	}
}
	//定制
	function ExecMod(batchType){
		//策略名
		var batchType = $("input[@name='batchType']").val();
		var taskName =  $("#taskName").val();
		var start_time="";
		var end_time="";
		var netWanType="";
		var netServNum="";
		var gwShare_fileName="";
		var url="<s:url value='/itms/resource/BatchRestartClient!saveBatchRestartClientTask.action'/>";
		if(taskName == null || $.trim(taskName) == ""){
			alert("请输入任务名");
			return false;
		}
		if( batchType == "1" || batchType == "3" ){
			var start_time = document.getElementById("start_time").value;
			var end_time = document.getElementById("end_time").value;
			var netWanType = $.trim($("select[@name='netWanType']").val());
			if(start_time == null || start_time == ""){
				alert("请选择开始时间");
				return false;
			}
			if(end_time == null || end_time == ""){
				alert("请选择结束时间");
				return false;
			}
			if(start_time >= end_time){
				alert("开始时间应小于结束时间");
				return false;
			}
		}
		if( batchType == "2" ){ 
			var gwShare_cityId = $.trim($("select[@name='gwShare_cityId']").val());
			var netServNum = $("input[@name='netServNum']").val();
			if(gwShare_cityId=="-1")
				{
				alert("请先查询");
				return ;
				}
			if(netServNum == null || $.trim(netServNum) == ""){
				alert("请输入终端接入数");
				return;
			}
			if(netServNum.length >3){
				alert("终端接入数不可超过100");
				return;
			}
			if(!checkIsNumber(netServNum)){
				alert("终端接入数必须为数字格式");
				return;
			}
			if(parseInt(netServNum) > 100){
				alert("终端接入数不可超过100");
				return;
			}
		}
		alert(netServNum);
		alert(taskName);
		
		var gwShare_cityId = $.trim($("select[@name='gwShare_cityId']").val());
        var gwShare_onlineStatus = $.trim($("select[@name='gwShare_onlineStatus']").val());
        var gwShare_vendorId = $.trim($("select[@name='gwShare_vendorId']").val());
        var gwShare_deviceModelId = $.trim($("select[@name='gwShare_deviceModelId']").val());
        var gwShare_devicetypeId = $.trim($("select[@name='gwShare_devicetypeId']").val());
        var gwShare_bindType = $.trim($("select[@name='gwShare_bindType']").val());
        var gwShare_queryType=$("input[@name='gwShare_queryType']").val();
        var gwShare_deviceSerialnumber = $.trim($("input[@name='gwShare_deviceSerialnumber']").val());
        var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
        $.post(url,{
        	gwShare_fileName:gwShare_fileName,
        	gwShare_queryType:gwShare_queryType,
        	gwShare_cityId:gwShare_cityId,
        	gwShare_onlineStatus:gwShare_onlineStatus,
        	gwShare_vendorId:gwShare_vendorId,
        	gwShare_deviceModelId:gwShare_deviceModelId,
        	gwShare_devicetypeId:gwShare_devicetypeId,
        	gwShare_bindType:gwShare_bindType,
        	gwShare_deviceSerialnumber:gwShare_deviceSerialnumber,
        	netServNum:netServNum,
        	taskName:taskName,
        	start_time:start_time,
        	end_time:end_time,
        	netWanType:netWanType,
        	batchType:batchType
        	},function(ajax){
				$("div[@id='QueryData']").html("");
				$("div[@id='QueryData']").append(ajax);
			});
        //document.batchexform.submit();
		$("#doButton").attr("disabled","disabled");
	}
	
	function checkIsNumber(str){
		if(str == null || str == ""){
			return false;
		}
		var reg = /^[0-9]+$/;
		if(reg.test(str)){
			return true;
		}else{
			return false;
		}
	}
	
	//中文校验
	function checkChinese(str){
		var regTest = /^[\u4e00-\u9fa5]+$/;
		var flag = false;
		if(str != null && $.trim(str) != ""){
			if(str.indexOf("\\") != -1){
				var strArr = str.split("\\");
				str = strArr[strArr.length-1];
			}
	    	for(var i=0 ; i<str.length ; i++){
				var word = str.substring(i,i+1);
				if(regTest.test(word)){
					flag = true ; 
					break;
				}
			}
	    }
		return flag;
	}

	
	//导入帐号信息
    function importCustomer(){
    	var myFileCustomer = $("input[@name='uploadCustomer']").val();
		if(""==myFileCustomer){	
			alert("请选择文件!");
			return false;
		}
		var filetCustomer = myFileCustomer.split(".");
		if(filetCustomer.length<2)
		{
			alert("请选择文件!");
			return false;
		}
		if("xls" == filetCustomer[filetCustomer.length-1] || "txt" == filetCustomer[filetCustomer.length-1])
		{
			var file2 = myFileCustomer.split("\\");
			var fileName2 = file2[file2.length-1];
			$("input[@name='uploadFileName4Customer']").attr("value",fileName2);
			return true;
		}else
		{
			alert("仅支持后缀为xls或txt的文件");
			return false;
		}
    }
    
	function toExportCustExcel()
	{
		$("form[@name='batchexform']").attr("action","<s:url value='/itms/resource/BatchRestartClient!downloadTemplateCustExcel.action'/>");
		$("form[@name='batchexform']").submit();
	}
	
	function toExportCustTxt()
	{
		$("form[@name='batchexform']").attr("action","<s:url value='/itms/resource/BatchRestartClient!downloadTemplateCustTxt.action'/>");
		$("form[@name='batchexform']").submit();
	}
	
	//去掉空格
	function trim(str){
      return str.replace(/(^\s*)|(\s*$)/g,"");
	}
</SCRIPT>
	</head>
	<body>
	<% String batch_Type = request.getParameter("batch_Type"); %>
			
			<%
				if(batch_Type == null || batch_Type.trim().length() == 0){
					response.sendRedirect(request.getContextPath() + "/login.jsp");
				}
			%>
		<TABLE width="98%" align="center" class="querytable">
		<input type="hidden" name="batchType" 	value="<%=batch_Type %>">
			<TR>
				<TD>
					<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
					<%
						if("1".equals(batch_Type))
						{
							%>您当前的位置：批量重启终端<%
						}
						else if("2".equals(batch_Type))
						{
							%>您当前的位置：批量修改上网数量<%
						}
						else if("3".equals(batch_Type))
						{
							%>您当前的位置：变更上网方式<%
						}
					%>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
			  <td colspan="4">
				  <%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
			  </td>
			</TR>
		</TABLE>
		<br>
		<form action="" method="post" enctype="multipart/form-data" name="batchexform" onsubmit="" id="batchexform">
			<input type="hidden" name="custSG"   value="">
			<input type="hidden" name="custCheck"  value="">
			<input type="hidden" name="btnValue4Customer"   value="">
			
			<table class="querytable" width="98%" align="center">
				<tr>
					<td colspan="4" class="title_1">
					<%
						if("1".equals(batch_Type))
						{
							%>批量重启终端任务定制<%
						}
						else if("2".equals(batch_Type))
						{
							%>批量修改上网数量任务定制<%
						}
						else if("3".equals(batch_Type))
						{
							%>变更上网方式任务定制<%
						}
					%>
					</td>
				</tr>
						<tr bgcolor="#FFFFFF">
								<td colspan="3">
									<div id="selectedDev">
										请查询设备！
									</div>
								</td>
						</tr>
					<TR>
					<TD class="title_2" align="center" width="15%">任务名称</TD>
					<TD colspan="3"> <input type="text" id="taskName" name="taskName" width="500" maxlength="50"> </TD>
					</TR>
			<%
				if("1".equals(batch_Type) || "3".equals(batch_Type))
				{
					if("3".equals(batch_Type))
					{
						%>
						<TR>
						<TD class="title_2" align="center" width="15%">变更上网方式选择</TD>
						<TD colspan="3">
						<select name="netWanType">
						<option value="1">路由改桥接</option>
						<option value="2">桥接改路由</option>
						</select>
						</TD>
					</TR>
						<%
					}
					%>
					<tr>
			<td class="title_1" width="15%">
						<%
							if("1".equals(batch_Type)){
								%>重启开始时间<%
							}else if("3".equals(batch_Type)){
								%>开始执行时间<%
							}
						%>
				</td>
				<td width="35%">
					<input type="text" name="startTime" id="start_time" class='bk' readonly
						value="<s:property value='startTime'/>">
					<img name="shortDateimg"
						onClick="WdatePicker({el:document.batchexform.startTime,dateFmt:'HH:mm:ss',skin:'whyGreen'})"
						src="../../images/dateButton.png" width="15" height="12"
						border="0" alt="选择" />
				</td>
				<td class="title_1" width="15%">
						<%
							if("1".equals(batch_Type)){
								%>重启结束时间<%
							}else if("3".equals(batch_Type)){
								%>结束执行时间<%
							}
						%>
				</td>
				<td width="35%">
				<input type="text" name="endTime" id="end_time" class='bk' readonly
						value="<s:property value='endTime'/>">
					<img name="shortDateimg"
						onClick="WdatePicker({el:document.batchexform.endTime,dateFmt:'HH:mm:ss',skin:'whyGreen'})"
						src="../../images/dateButton.png" width="15" height="12"
						border="0" alt="选择" />
				</td>
			</tr>
					<%
				}
				else if("2".equals(batch_Type))
				{
					%>
					<TR>
					<TD class="title_2" align="center" width="15%">终端接入数</TD>
					<TD colspan="3">
					<input type="text" id="netServNum" name="netServNum" width="500" maxlength="3">
					</TD>
				</TR>
					<%
				}
			%>	
				
				
				<TR>
					<td colspan="4" class="foot" align="right">
						<div class="right">
							<input type="button" onclick="ExecMod(this.name)" name="<%=batch_Type %>" value="定制"> 
						</div>
					</td>
				</TR>
				<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				</div>
			</table>
		</form>
		<br>  
		<br>
		
	</body>
</html>