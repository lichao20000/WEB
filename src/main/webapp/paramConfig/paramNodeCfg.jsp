<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Date"%>
<%@page import="com.linkage.commons.util.StringUtil"%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>������������</title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css" />
<link href="<s:url value="/css/listview.css"/>" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/CheckForm.js"/>"></script>
<script type="text/javascript">
//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=["dataForm"]

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
   			dyniframe[i].style.display="block"
   			//����û����������NetScape
   			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
    				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
    			//����û����������IE
   			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
    				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
 			 }
 		}
		//�����趨�Ĳ���������֧��iframe�����������ʾ����
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

var gwType = <%=request.getParameter("gw_type")%>;
$(function(){
	if(gwType == null){
		gwType = "1";
	}
	$("input[@name='gwShare_jiadan']").css("display","none");
	gwShare_setGaoji();
	gwShare_setImport();
	$("input[@name='gwShare_gaoji']").css("display","none");
	$("input[@name='gwShare_up_import']").css("display","none");
	$("input[@name='gwShare_import']").css("display","none");
	getConfType();
	$("#checkAll").click( 
	  function(){ 
	    if(this.checked){ 
	        $("input[name='select_checkbox']").attr('checked', true)
	    }else{ 
	        $("input[name='select_checkbox']").attr('checked', false)
	    } 
	  });
});

var deviceIds = "";
var param = "";
function deviceResult(returnVal){
	deviceIds ="";
	for(var i=0;i<returnVal[2].length;i++){
		if(returnVal[2].length == 1){
			deviceIds = deviceIds + returnVal[2][i][0];
		}else{
			deviceIds = deviceIds + returnVal[2][i][0]+",";
		}
	}
	$("tr[@id='trData']").show();
		$.post("<s:url value='/gtms/config/paramNodeCfg!querySingleDeviceList.action'/>",{
			deviceIds:deviceIds,
			gwType:gwType
		},
		function(ajax){	
			$("tr[@id='trData1']").show();
		    $("div[@id='QueryData']").html("");
			$("div[@id='QueryData']").append(ajax);
			$("tr[@id='trData2']").show();
		});
	
}

function getConfParam(obj){
	var conf_type_id = obj.value;
	$("tr[@id='trData3']").show();
	 $("div[@id='QueryData3']").html("���ڲ�ѯ�����б�...");
	var url = "<s:url value='/gtms/config/paramNodeCfg!getConfParam.action'/>";
	$.post(url,{
		conf_type_id:conf_type_id
	},function(ajax){
	    $("div[@id='QueryData3']").html("");
		$("div[@id='QueryData3']").append(ajax);
		$("tr[@id='trData4']").show();
	});
}

function doExecute(){
	var select_checkbox = "";
	var taskId = parseInt(Date.parse(new Date()) / 1000) + "" + parseInt((Math.floor(Math.random() * 900)) + 100);
	$("input[name='taskId']").val(taskId);
	var currTime = Date.parse(new Date()) / 1000;
	$("input[name='currTime']").val(currTime);
	var inputs = document.getElementsByName("input_param_name");
	var flag = "";
    for(var i = 0; i < inputs.length; i++) {
    	var inputVal = inputs[i].value;
    	if(inputVal == ""){
    		flag = "-1";
    	}else{
    		inputs[i].value = inputs[i].id+"#"+inputVal;
    	}
    }
    if(flag == "-1"){
    	alert("������ֵ����Ϊ��!");
    	return false;
    }
    var conf_type_id = $("select[name='conf_type_id']").val();
    $("input[name='conf_type_id']").val(conf_type_id);
    var do_type = $("input[name='dotype']").val();
    $("input[name='do_type']").val(do_type);
    $("input[name='select_checkbox']:checkbox").each(function(){ 
        if($(this).attr("checked")){
        	select_checkbox += $(this).val() + ",";
        }
    });
    if(select_checkbox == ""){
		alert("������ѡ��һ������!");
		return false;
	}
	if(do_type == 1){
		var cityId = $.trim($("select[@name='gwShare_cityId']").val());
	 	$("input[name='cityId']").val(cityId);
        var onlineStatus = $.trim($("select[@name='gwShare_onlineStatus']").val());
        $("input[name='onlineStatus']").val(onlineStatus);
        var vendorId = $.trim($("select[@name='gwShare_vendorId']").val());
        $("input[name='vendorId']").val(vendorId);
        var deviceModelId = $.trim($("select[@name='gwShare_deviceModelId']").val());
        $("input[name='deviceModelId']").val(deviceModelId);
        var devicetypeId = $.trim($("select[@name='gwShare_devicetypeId']").val());
        $("input[name='devicetypeId']").val(devicetypeId);
        var bindType = $.trim($("select[@name='gwShare_bindType']").val());
        $("input[name='bindType']").val(bindType);
        var deviceSerialnumber = $.trim($("input[@name='gwShare_deviceSerialnumber']").val());
        $("input[name='devSerialnumber']").val(deviceSerialnumber);
        document.selectForm.submit();
	}else if(do_type == 2){
		var myFile = $("#gwShare_loadForm").contents().find("#upload").val();
		$("#gwShare_loadForm").contents().find("#taskId").val(taskId);
		$("input[name='uploadDoType']").val(do_type);
		if("" == myFile){
			alert("��ѡ���ļ�!");
			return false;
		}else if(typeof(myFile)=="undefined"){
			alert("���������ϴ��ļ���");
			return false;
		}
		if("txt"!= myFile.substr(myFile.length-3,3)){
			alert("�밴��ע�������ϴ��ļ���");
			return false;
		}
		var form = $("#gwShare_loadForm").contents().find("#batchform");
		form.submit();  
	    document.selectForm.submit();
	}else if(do_type == 3){
		var deviceSerialnumber = $("input[name='deviceSerialnumber']").val();
		$("input[name='devSn']").val(deviceSerialnumber);
		$("input[name='devId']").val(deviceIds);
	    document.selectForm.submit();
	}
	$("input[@name='input_param_name']").val("");
	$("input[name='taskId']").val("");
	$("input[name='currTime']").val("");
	$("#doButton").attr("disabled",true);
}

function checkForm(select_checkbox){
	if(select_checkbox == ""){
		alert("������ѡ��һ������!");
		return false;
	}
	return true;
}

function getConfType(){
	$.post("<s:url value='/gtms/config/paramNodeCfg!getNodeTypeList.action'/>",function(ajax){
		$("#confType']").html("");
		$("#confType").append(ajax);
	}); 
}
</script>
</head>
<body>
	<table border="0" cellspacing="0" cellpadding="0" width="100%">
		<tr>
			<td HEIGHT="20">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td>
				<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite" nowrap>
										�����������ý���
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td colspan="4">
							<%@ include file="/gwms/share/gwShareDeviceQuery_paramNodeByJx.jsp"%>
						</td>
					</tr>
					<tr id="trData" style="display: none">
						<td class="colum">
							<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
								����չʾ�豸��Ϣ...
							</div>
						</td>
					</tr>
					<tr id="trData1" style="display: none">
						<th align="center" width="100%" colspan="6">
							����������������
						</th>
					</tr>
					<tr id="trData2" style="display: none">
						<td>
							<select id="confType" name="conf_type_id" onchange="getConfParam(this);">
								<option value="-1">==��ѡ���������==</option>
							</select>
						</td>
					</tr>
					<tr id="trData3" style="display: none">
						<td class="colum">
							<div id="QueryData3" style="width: 100%; z-index: 1; top: 100px;margin-bottom: -19px;">
								���ڲ�ѯ�����б�...
							</div>
						</td>
					</tr>
					<tr id="trData4" style="display: none">
						<td colspan="6"  style="background-image: url(../images/green_title_bg.jpg)">
							<input align="left" type="checkbox" id="checkAll" name="checkAllButton" onclick="checkAll();">ȫѡ
							<input align="right" style="margin-left: 1047px;" type="button" id="doButton" name="doButton" onclick="doExecute();" value="����" class="btn">
						</td>
					</tr>
					<tr id="trData5" bgcolor="#FFFFFF" style="display: none">
						<td colspan="6" align="left" class="green_foot">
							<div id="resultDIV"></div>
						</td>
					</tr>
				</table>
				<div class="it_table">
					<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe>
				</div>
			</td>
		</tr>
	</table>
</body>
</html>
<%@ include file="../foot.jsp"%>