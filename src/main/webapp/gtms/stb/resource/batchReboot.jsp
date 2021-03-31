<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
// 定制
function ExecMod()
{
	var task_desc = $("input[@name='taskDesc']").val();
	if(trim(task_desc).length<1){	
		alert("请填写任务简要描述！");
		return false;
	}
	
	$("#doButton").attr("disabled",true);
	//$("#doButton1").attr("disabled",true);
	
	var str = $("#choseSoftV").attr("value");
	$("input[@name='deviceTypeIds']").val(str);
	
	var vendorId = $("select[@name='vendorId']").val();
	if(vendorId=="-1"){
		$("input[@name='deviceTypeIds']").val("");
	}
	
	var data_type=$("input[@name='data_type']").val();
	if(data_type==1 || data_type==2)
	{
		var uploadFileName = $("input[@name='file']").val();
		if(""==uploadFileName){	
			alert("请选择文件!");
			$("#doButton").attr("disabled",false);
		//	$("#doButton1").attr("disabled",false);
			return false;
		}
		
		var fileName = uploadFileName.split(".");
		if(fileName.length<2){
			alert("请选择文件!");
			$("#doButton").attr("disabled",false);
	//		$("#doButton1").attr("disabled",false);
			return false;
		}
		
		if("xls" != fileName[fileName.length-1]){
			alert("仅支持后缀为xls的文件");
			$("#doButton").attr("disabled",false);
		//	$("#doButton1").attr("disabled",false);
			return false;
		}
	}
	
	$("form[@name='batchexform']").attr("action","batchReboot!addTask.action");
	$("form[@name='batchexform']").submit();
}
/*
function showTrImportFile()
{
	var task_type=$("input[@name='data_type']").val();
	if(task_type==0){
		$("tr[id=trCity]").attr("style","display:none");
		$("tr[id=trVendor]").attr("style","display:none");
		$("tr[id=trDeviceType]").attr("style","display:none");
		$("tr[id=trVersion]").attr("style","display:none");
		
		$("tr[id=importFile1]").attr("style","display:");
		$("tr[id=importFile2]").attr("style","display:");
		$("input[@name='data_type']").val(1);
		$("input[@id='choseSoftV']").val("");
		$("button[id=doButton1]").val("范围定制");
	}else{
		$("tr[id=trCity]").attr("style","display:");
		$("tr[id=trVendor]").attr("style","display:");
		$("tr[id=trDeviceType]").attr("style","display:");
		$("tr[id=trVersion]").attr("style","display:");
		
		$("tr[id=importFile1]").attr("style","display:none");
		$("tr[id=importFile2]").attr("style","display:none");
		$("input[@name='data_type']").val(0);
		$("input[@name='uploadFileName']").val("");
		$("button[id=doButton1]").val("导入文件");
	}
}
*/

function changeDataType(type)
{
	$("input[@name='data_type']").val(type);
}

function softdivcl()
{
	$("div[@id='div_css']").hide();
}

function trim(str)
{
    return str.replace(/(^\s*)|(\s*$)/g,"");
}
</SCRIPT>
</head>

<body>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			您当前的位置：机顶盒批量重启任务管理
		</TD>
	</TR>
</TABLE>
<br>

<s:form method="post" enctype="multipart/form-data" name="batchexform" onSubmit="return false;">
	<input type="hidden" name="data_type" value="<s:property value="data_type"/>">
	<input type="hidden" id="choseSoftV" value=""/>
	<input type="hidden" name="deviceTypeIds" value=""/>
	<table class="querytable" width="98%" align="center">
		<tr>
			<td colspan="4" class="title_1">机顶盒批量重启任务定制</td>
		</tr>

        <tr>
            <td colspan="1" width="15%" align="center" class="title_2">任务简要描述</td>
            <td colspan="3" width="85%">
                <input type="text" name="taskDesc" size="40" 
                	maxlength="64" style="width:40%;" class=bk /><font color="red">*</font>
            </td>
        </tr>
        <s:if test="data_type==0">
	        <TR id="trCity" style="display:">
				<TD class="title_2" align="center" width="15%">属地</TD>
				<TD width="85%" colspan="3">
	               <s:select list="cityList" name="cityId" headerKey="00"
						headerValue="省中心" listKey="city_id" listValue="city_name"
						value="cityId" cssClass="bk" theme="simple">
					</s:select>
				</TD>
			</TR>
			<TR id="trVendor" style="display:">
				<TD class="title_2" align="center" width="15%">厂商</TD>
				<TD width="85%" colspan="3">
					<s:select list="vendorList" name="vendorId" headerKey="-1"
						headerValue="请选择厂商" listKey="vendor_id" listValue="vendor_add"
						value="vendorId" cssClass="bk" onchange="vendorChange()"
						theme="simple">
					</s:select>
				</TD>
			</TR>
			<tr id="trDeviceType" style="display:">
				<td colspan="1" width="15%" align="center" class="title_2">要触发的版本</td>
				<TD width="85%" colspan="3">
					<div id="devicetype">请选择厂商</div>
				</TD>
			</tr>
			<tr id="trVersion" style="display:">
				<td colspan="1" width="15%" align="center" class="title_2">已选择版本</td>
				<td colspan="3">
					<div id="devicecheckdev" width='85%'></div>
				</td>
			</tr>
		</s:if>
		<s:else>
			<tr id="importFile1" style="display:">
				<td class="title_2" align="center" width="15%">导入数据类型</td>
				<td width="85%" colspan="3">
					<input type="radio" name="checkBox" checked="checked" onclick="changeDataType(1)">业务账号
					<input type="radio" name="checkBox"  onclick="changeDataType(2)">MAC地址
				</td>
			</tr>
			<tr id="importFile2" style="display:">
				<td class="title_2" align="center" width="15%">请选择文件</td>
				<td width="85%" colspan="3">
					<input type="file" size="60" name="file"/><font color="red">*</font>xls格式文档
				</td>
			</tr>
		</s:else>
		<tr>
			<TD class="title_2" align="center" width="15%">策略方式</td>
			<TD width="85%" colspan="3">
				<SELECT name="strategyType" class="bk">
					<option value="4">机顶盒下次连接</option>
				</SELECT>
			</td>
		</tr>
        <tr>
            <td colspan="1" width="15%" align="center" class="title_2">任务详细描述</td>
            <td colspan="3">
                <textarea name="taskDetail" style="width:60%;" maxlength="256" ></textarea>
            </td>
        </tr>

		<tr>
			<td colspan="4" class="foot" align="right">
				<div class="right">
					<!-- <button id="doButton1" onclick="showTrImportFile()">导入文件</button> -->
					&nbsp;&nbsp;&nbsp;&nbsp;
					<button id="doButton" onclick="ExecMod()">定制</button>
				</div>
			</td>
		</tr>
	</table>
</s:form>
		
<div id="div_css" style="position: absolute; z-index: 255; top: 200px; left: 350px; width: 40%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none">
	<table class="querytable" align="center" width="100%" style="table-layout: fixed;text-align:left !important" >
		  <tr>
			<td width="120px" class="title_2" align="center">请选择软件版本型号</td>
			<td id="softVershow" class="title_2" align="center"></td>
		</tr>
		<tr>
			<td colspan="2" class="title_2" align="center">
				<button align="center" name="softdivtbn" onclick="softdivcl()">
				关闭
				</button>
			</td>
		</tr>
	</table>
</div>
</body>

<SCRIPT LANGUAGE="JavaScript">
function vendorChange()
{
	var vendorId = $("select[@name='vendorId']").val();
	$("div[@id='devicetype']").html("请选择厂商");
	if(vendorId=="-1"){
		$("tr[@id='trdevicetype']").hide();
		return;
	}
	
	var url = "<s:url value='/gtms/stb/resource/batchReboot!getTargetVersion.action'/>";
	$("tr[@id='trdevicetype']").show();
	
	$.post(url,{
		vendorId:vendorId
	},function(ajax){
		if(ajax!=""){
			var lineData = ajax.split("|");
			var innerHV = "";
			if(typeof(lineData)&&typeof(lineData.length)){
				//按照硬件版本型号分组
				for(var i=0;i<lineData.length;i++){
					var oneElement = lineData[i].split('\1');
					innerHV += "<input type=checkbox name='" + oneElement[0] 
							+"' value='"+ oneElement[1] + "' onclick='hardVersionCLK(this)' />" 
							+ oneElement[0]+"&nbsp;&nbsp;";
				}
				
				$("div[@id='devicetype']").html(innerHV);
			}
		}
	});
}

function hardVersionCLK(obj)
{
   	var tempMV = obj.value.split("#");
   	if(obj.checked)
	{
		//保存选择的硬件版本和软件版本格式为：B600v2(ker,ker);B700()
		var choseYStr = obj.name + "(";
		var chosedYStr = document.getElementById("devicecheckdev").innerHTML;
		var choseID = "";
		var tempHtml = "";
		for(var i=0;i<tempMV.length;i++)
		{
			var tempIDK = tempMV[i].split("$");
			//组装硬件版本和软件版本
			choseYStr = choseYStr + tempIDK[1] + ",";
			//组装型号ID
			choseID = choseID + tempIDK[0] + ",";
			//生成多选框
			tempHtml += "<input type=checkbox checked=true name='softVersionCheck' value='" 
						+ tempIDK[0] + "' title='" + tempIDK[1] 
						+ "' onclick='softVersionCLK(this)' fname='" 
						+ $(obj).attr("name") + "' />" + tempIDK[1];
		}
		
		//设备硬件型号
		choseYStr = choseYStr.substring(0,choseYStr.length-1) + ")";
		if(null != chosedYStr && undefined != chosedYStr)
		{
			if(chosedYStr.length > 0){
				document.getElementById("devicecheckdev").innerHTML += ";&nbsp;&nbsp;" + choseYStr;
			}else{
				document.getElementById("devicecheckdev").innerHTML = choseYStr;
			}
		}
		//保存型号ID
		document.getElementById("choseSoftV").value += choseID;
		
		//设置多选框列表
		$("div[@id='div_css']").show();
		$("td[@id=softVershow]").html("");
		$("td[@id=softVershow]").append(tempHtml);
	}
   	//取消的时候
	else
	{
		//删除保存的硬件版本和软件版本
		var chosedYStr = document.getElementById("devicecheckdev").innerHTML;
		var chosedYSA = chosedYStr.split(";");
		var tempChose = "";
		for(var i=0;i<chosedYSA.length;i++)
		{
			if(obj.name != chosedYSA[i].substring(0,obj.name.length)){
				tempChose += chosedYSA[i] + ";" ;
			}
		}
		document.getElementById("devicecheckdev").innerHTML = tempChose.substring(0,tempChose.length-1);
		
		//删除保存的型号ID
		var chosedID = document.getElementById("choseSoftV").value;
		var idArray = chosedID.split(",");
		var tempID = "";
		for(var i=0;i<idArray.length-1;i++)
		{
			var idcount = 0;
			for(var j=0;j<tempMV.length;j++)
			{
				if(tempMV[j].split("$")[0] == idArray[i]){
					idcount++;
				}
			}
			
			if(idcount == 0){
				tempID += idArray[i] + ",";
			}
		}
		document.getElementById("choseSoftV").value = tempID;
		
		//设置多选框列表
		var tempHtml = "";
		for(var i=0;i<tempMV.length;i++)
		{
			var tempIDK = tempMV[i].split("$");
			//生成多选框
			tempHtml += "<input type=checkbox name='softVersionCheck' value='" 
						+ tempIDK[0] + "' title='" + tempIDK[1] 
						+ "' onclick='softVersionCLK(this)' fname='" + $(obj).attr("name") + "' />" 
						+ tempIDK[1];
		}
		
		$("div[@id='div_css']").show();
		$("td[@id=softVershow]").html("");
		$("td[@id=softVershow]").append(tempHtml);
	}
}
    
//点击软件版本的处理方式
function softVersionCLK(obj)
{
	var fname = $(obj).attr("fname");
	var softnames = $("#devicecheckdev").attr("innerHTML");
	var nameArr = softnames.split(";");
	
	var softids = $("#choseSoftV").attr("value");
	var idArr = softids.split(",");
	
	var idstr = '';
	var namestr = '';

	for (var i = 0; i < nameArr.length; i++) {
		if (nameArr[i].indexOf(fname) == -1) {
			namestr += nameArr[i] + ";";
		}
		if (i == nameArr.length - 1 && namestr != "") {
			namestr = namestr.substring(0, namestr.length - 1);
		}
	}
	
	for (var j = 0; j < idArr.length - 1; j ++) {
		if (idArr[j].indexOf($(obj).attr("value")) == -1) {
			idstr += idArr[j] + ",";
		}
	}
	
	var thisname = "";
	$("input[name='softVersionCheck']").each(function(i, el) {
		if($(this).attr("checked")){
			if (softids.indexOf($(this).val()) == -1) {
				idstr += $(this).val() + ",";
			}
			thisname += $(this).attr("title") + ",";
		}
	});
	
	namestr = namestr == "" ? namestr : namestr + ";";
	$("#choseSoftV").attr("value", idstr);
	if (thisname == "") {
		$("[name='" + fname+ "']").click();
	}else {
		$("#devicecheckdev").attr("innerHTML", namestr + fname + "(" 
				+ thisname.substring(0, thisname.length-1)+")");
	}
}
</SCRIPT>
