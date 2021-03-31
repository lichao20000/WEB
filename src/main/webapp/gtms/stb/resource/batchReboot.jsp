<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
// ����
function ExecMod()
{
	var task_desc = $("input[@name='taskDesc']").val();
	if(trim(task_desc).length<1){	
		alert("����д�����Ҫ������");
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
			alert("��ѡ���ļ�!");
			$("#doButton").attr("disabled",false);
		//	$("#doButton1").attr("disabled",false);
			return false;
		}
		
		var fileName = uploadFileName.split(".");
		if(fileName.length<2){
			alert("��ѡ���ļ�!");
			$("#doButton").attr("disabled",false);
	//		$("#doButton1").attr("disabled",false);
			return false;
		}
		
		if("xls" != fileName[fileName.length-1]){
			alert("��֧�ֺ�׺Ϊxls���ļ�");
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
		$("button[id=doButton1]").val("��Χ����");
	}else{
		$("tr[id=trCity]").attr("style","display:");
		$("tr[id=trVendor]").attr("style","display:");
		$("tr[id=trDeviceType]").attr("style","display:");
		$("tr[id=trVersion]").attr("style","display:");
		
		$("tr[id=importFile1]").attr("style","display:none");
		$("tr[id=importFile2]").attr("style","display:none");
		$("input[@name='data_type']").val(0);
		$("input[@name='uploadFileName']").val("");
		$("button[id=doButton1]").val("�����ļ�");
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
			����ǰ��λ�ã����������������������
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
			<td colspan="4" class="title_1">��������������������</td>
		</tr>

        <tr>
            <td colspan="1" width="15%" align="center" class="title_2">�����Ҫ����</td>
            <td colspan="3" width="85%">
                <input type="text" name="taskDesc" size="40" 
                	maxlength="64" style="width:40%;" class=bk /><font color="red">*</font>
            </td>
        </tr>
        <s:if test="data_type==0">
	        <TR id="trCity" style="display:">
				<TD class="title_2" align="center" width="15%">����</TD>
				<TD width="85%" colspan="3">
	               <s:select list="cityList" name="cityId" headerKey="00"
						headerValue="ʡ����" listKey="city_id" listValue="city_name"
						value="cityId" cssClass="bk" theme="simple">
					</s:select>
				</TD>
			</TR>
			<TR id="trVendor" style="display:">
				<TD class="title_2" align="center" width="15%">����</TD>
				<TD width="85%" colspan="3">
					<s:select list="vendorList" name="vendorId" headerKey="-1"
						headerValue="��ѡ����" listKey="vendor_id" listValue="vendor_add"
						value="vendorId" cssClass="bk" onchange="vendorChange()"
						theme="simple">
					</s:select>
				</TD>
			</TR>
			<tr id="trDeviceType" style="display:">
				<td colspan="1" width="15%" align="center" class="title_2">Ҫ�����İ汾</td>
				<TD width="85%" colspan="3">
					<div id="devicetype">��ѡ����</div>
				</TD>
			</tr>
			<tr id="trVersion" style="display:">
				<td colspan="1" width="15%" align="center" class="title_2">��ѡ��汾</td>
				<td colspan="3">
					<div id="devicecheckdev" width='85%'></div>
				</td>
			</tr>
		</s:if>
		<s:else>
			<tr id="importFile1" style="display:">
				<td class="title_2" align="center" width="15%">������������</td>
				<td width="85%" colspan="3">
					<input type="radio" name="checkBox" checked="checked" onclick="changeDataType(1)">ҵ���˺�
					<input type="radio" name="checkBox"  onclick="changeDataType(2)">MAC��ַ
				</td>
			</tr>
			<tr id="importFile2" style="display:">
				<td class="title_2" align="center" width="15%">��ѡ���ļ�</td>
				<td width="85%" colspan="3">
					<input type="file" size="60" name="file"/><font color="red">*</font>xls��ʽ�ĵ�
				</td>
			</tr>
		</s:else>
		<tr>
			<TD class="title_2" align="center" width="15%">���Է�ʽ</td>
			<TD width="85%" colspan="3">
				<SELECT name="strategyType" class="bk">
					<option value="4">�������´�����</option>
				</SELECT>
			</td>
		</tr>
        <tr>
            <td colspan="1" width="15%" align="center" class="title_2">������ϸ����</td>
            <td colspan="3">
                <textarea name="taskDetail" style="width:60%;" maxlength="256" ></textarea>
            </td>
        </tr>

		<tr>
			<td colspan="4" class="foot" align="right">
				<div class="right">
					<!-- <button id="doButton1" onclick="showTrImportFile()">�����ļ�</button> -->
					&nbsp;&nbsp;&nbsp;&nbsp;
					<button id="doButton" onclick="ExecMod()">����</button>
				</div>
			</td>
		</tr>
	</table>
</s:form>
		
<div id="div_css" style="position: absolute; z-index: 255; top: 200px; left: 350px; width: 40%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none">
	<table class="querytable" align="center" width="100%" style="table-layout: fixed;text-align:left !important" >
		  <tr>
			<td width="120px" class="title_2" align="center">��ѡ������汾�ͺ�</td>
			<td id="softVershow" class="title_2" align="center"></td>
		</tr>
		<tr>
			<td colspan="2" class="title_2" align="center">
				<button align="center" name="softdivtbn" onclick="softdivcl()">
				�ر�
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
	$("div[@id='devicetype']").html("��ѡ����");
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
				//����Ӳ���汾�ͺŷ���
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
		//����ѡ���Ӳ���汾������汾��ʽΪ��B600v2(ker,ker);B700()
		var choseYStr = obj.name + "(";
		var chosedYStr = document.getElementById("devicecheckdev").innerHTML;
		var choseID = "";
		var tempHtml = "";
		for(var i=0;i<tempMV.length;i++)
		{
			var tempIDK = tempMV[i].split("$");
			//��װӲ���汾������汾
			choseYStr = choseYStr + tempIDK[1] + ",";
			//��װ�ͺ�ID
			choseID = choseID + tempIDK[0] + ",";
			//���ɶ�ѡ��
			tempHtml += "<input type=checkbox checked=true name='softVersionCheck' value='" 
						+ tempIDK[0] + "' title='" + tempIDK[1] 
						+ "' onclick='softVersionCLK(this)' fname='" 
						+ $(obj).attr("name") + "' />" + tempIDK[1];
		}
		
		//�豸Ӳ���ͺ�
		choseYStr = choseYStr.substring(0,choseYStr.length-1) + ")";
		if(null != chosedYStr && undefined != chosedYStr)
		{
			if(chosedYStr.length > 0){
				document.getElementById("devicecheckdev").innerHTML += ";&nbsp;&nbsp;" + choseYStr;
			}else{
				document.getElementById("devicecheckdev").innerHTML = choseYStr;
			}
		}
		//�����ͺ�ID
		document.getElementById("choseSoftV").value += choseID;
		
		//���ö�ѡ���б�
		$("div[@id='div_css']").show();
		$("td[@id=softVershow]").html("");
		$("td[@id=softVershow]").append(tempHtml);
	}
   	//ȡ����ʱ��
	else
	{
		//ɾ�������Ӳ���汾������汾
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
		
		//ɾ��������ͺ�ID
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
		
		//���ö�ѡ���б�
		var tempHtml = "";
		for(var i=0;i<tempMV.length;i++)
		{
			var tempIDK = tempMV[i].split("$");
			//���ɶ�ѡ��
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
    
//�������汾�Ĵ���ʽ
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
