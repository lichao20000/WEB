<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�豸��ѯ</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript">
// function gwShare_queryField_selected(value){
// 	$("input[@name='gwShare_queryField_temp']").val(value.value);
// }

/*------------------------------------------------------------------------------
//������:		queryChange
//����  :	change 1:�򵥲�ѯ��2:�߼���ѯ
//����  :	���ݴ���Ĳ���������ʾ�Ľ���
//����ֵ:		��������
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function gwShare_queryDevice(){
	var	width=800;    
	var height=450; 
	var gwShare_queryResultType = $("input[@name='gwShare_queryResultType']").val();
	var url="<s:url value="/gtms/stb/share/shareDeviceQuery!queryDeviceList.action"/>?gwShare_queryResultType="+gwShare_queryResultType;
    var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
    url = url + "&gwShare_queryType=" + gwShare_queryType;
    
		var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
		if(""==gwShare_fileName){
			alert("�����ϴ��ļ���");
			return;
		}
		url = url + "&gwShare_fileName=" + gwShare_fileName;
	
    var gwShare_RecordNum = $("input[@name='gwShare_RecordNum']").val();
    url = url + "&gwShare_RecordNum=" + gwShare_RecordNum;
    var gwShare_OrderByUpdateDate = $("input[@name='gwShare_OrderByUpdateDate']").val();
    url = url + "&gwShare_OrderByUpdateDate=" + gwShare_OrderByUpdateDate;
	url = url + "&refresh=" + new Date().getTime();
	var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');    
	if(typeof(returnVal)=='undefined'){
		return;
	}else{
		deviceResult(returnVal);
	}
}

/*------------------------------------------------------------------------------
//������:		��ʼ��������ready��
//����  :	��
//����  :	��ʼ�����棨DOM��ʼ��֮��
//����ֵ:		
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
$(function(){
	var queryType = "<s:property value="queryType"/>";
	var queryResultType = "<s:property value="queryResultType"/>";
	var startQuery = "<s:property value="startQuery"/>";
	if(""!=queryType && null!=queryType){
		$("input[@name='gwShare_queryType']").val(queryType);
	}
	if(""!=queryResultType && null!=queryResultType){
		$("input[@name='gwShare_queryResultType']").val(queryResultType);
	}
// 	gwShare_queryChange(queryType);
	gwShare_queryChange('3');
	
	var str = Array($("input[@name='gwShare_queryParam']"),
					$("div[@id='gwShare_msgdiv']"),
					$("input[@name='gwShare_queryField_temp']"));
	
	//������������������ʱȥ������Ӧƥ��
	//$.autoMatch3("<s:url value='/gtms/stb/share/shareDeviceQuery!getDeviceSn.action'/>",str,"#");
});


/*------------------------------------------------------------------------------
//������:		queryChange
//����  :	change 1:�򵥲�ѯ��2:�߼���ѯ��3:�����ѯ
//����  :	���ݴ���Ĳ���������ʾ�Ľ���
//����ֵ:		��������
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function gwShare_queryChange(change){
	switch (change){
		case "3":
			$("td[@id='gwShare_thTitle']").html("�� �� �� ѯ");
			$("input[@name='gwShare_queryType']").val("3");
			$("button[@name='gwShare_import']").css("display","none");
			$("tr[@id='gwShare_tr31']").css("display","");
			$("tr[@id='gwShare_tr32']").css("display","");
			$("button[@name='gwShare_queryButton']").val("�����ļ�");
			break;
		default:
			break;
	}
}


/*------------------------------------------------------------------------------
//������:		deviceSelect_parseMessage
//����  :	ajax 
            	������XXX$XXX#XXX$XXX
            field
            	��Ҫ���ص�jquery����
//����  :	����ajax���ز���
//����ֵ:		
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
//������ѯ�豸�ͺŷ���ֵ�ķ���
function gwShare_parseMessage(ajax,field,selectvalue){
	var flag = true;
	if(""==ajax){
		return;
	}
	var lineData = ajax.split("#");
	if(!typeof(lineData) || !typeof(lineData.length)){
		return false;
	}
	field.html("");
	option = "<option value='-1' selected>==��ѡ��==</option>";
	field.append(option);
	for(var i=0;i<lineData.length;i++){
		var oneElement = lineData[i].split("$");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		if(selectvalue==xValue){
			flag = false;
			//����ÿ��value��text��ǵ�ֵ����һ��option����
			option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
		}else{
			//����ÿ��value��text��ǵ�ֵ����һ��option����
			option = "<option value='"+xValue+"'>=="+xText+"==</option>";
		}
		try{
			field.append(option);
		}catch(e){
			alert("�豸�ͺż���ʧ�ܣ�");
		}
	}
	if(flag){
		field.attr("value","-1");
	}
}

/*------------------------------------------------------------------------------
//������:		gwShare_setGaoji
//����  :	
//����  :	���ظ߼���ѯ
//����ֵ:		
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function gwShare_setGaoji(){
	$("input[@name='gwShare_gaoji_value']").val("");
	$("button[@name='gwShare_gaoji']").css("display","");
}
/*------------------------------------------------------------------------------
//������:		gwShare_setImport
//����  :	
//����  :	���ص����ѯ
//����ֵ:		
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function gwShare_setImport(){
	$("input[@name='gwShare_import_value']").val("");
	$("button[@name='gwShare_import']").css("display","");
}
/*------------------------------------------------------------------------------
//������:		trim
//����  :	str �������ַ���
//����  :	���ݴ���Ĳ�������ȥ�����ҵĿո�
//����ֵ:		trim��str��
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function trim(str){
     return str.replace(/(^\s*)|(\s*$)/g,"");
}

/*------------------------------------------------------------------------------
//������:		gwShare_reButto
//����  :	
//����  :	�������ð�ť
//����ֵ:		
//˵��  :	
//����  :	Create 2018-10-17 of By zzs
------------------------------------------------------------------------------*/
function gwShare_revalue(){
	$("input[@name='gwShare_reButto_value']").val("");
	$("button[@name='gwShare_reButto']").css("display","");
}
</SCRIPT>
<style>
span
{
	position:static;
	border:0;
}
</style>
</head>
<form name="gwShare_selectForm" action="<s:url value="/gtms/stb/share/shareDeviceQuery!queryDeviceList.action"/>" target="dataForm">
<input type="hidden" name="gwShare_queryType" value="1" />
<input type="hidden" name="gwShare_queryResultType" value="checkbox" />
<input type="hidden" name="gwShare_queryField_temp" value="deviceSn" />
<input type="hidden" name="gwShare_gaoji_value" value="none" />
<input type="hidden" name="gwShare_import_value" value="none" />
<input type="hidden" name="gwShare_OrderByUpdateDate" value="1" /><!-- 1������¼�����ϱ�ʱ������0������ -->
<input type="hidden" name="gwShare_RecordNum" value="0" /><!-- Ĭ��3����¼��0Ϊ��ѯȫ�����������ô�С������50 -->

<TABLE width="100%" class="querytable" align="center">
	<tr><td colspan="4" class="title_1" id="gwShare_thTitle">�� �� �� ѯ</td></tr>
	
	<tr id="gwShare_tr31" bgcolor="#FFFFFF"  style="display:">
		<td align="right" width="15%">�ύ�ļ�</td>
		<td colspan="3" width="85%">
			<div id="importUsername">
				<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="<s:url value="/gwms/share/FileUpload.jsp"/>" height="20" width="100%">
				</iframe>
				<input type="hidden" name=gwShare_fileName value=""/>
			</div>
		</td>
	</tr>
	<tr id="gwShare_tr32" style="display:">
		<td CLASS="green_foot" align="right">ע������</td>
		<td colspan="3" CLASS="green_foot">
		1����Ҫ������ļ���ʽ����Excel���ı��ļ�����xls��txt��ʽ ��txt�ļ�ע���ļ�����ΪANSI���룬��������ᵼ�½����ļ�����
		 <br>
		2���ļ��ĵ�һ��Ϊ�����У�����ҵ���˺š������ߡ��豸���кš���
		 <br>
		3���ļ�ֻ��һ�С�
		 <br>
		4���ļ�������Ҫ̫�࣬����Ӱ�����ܡ�
		<br>
		<% if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))
				|| "xj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
		5���ļ�������Ҫ����30000�У����ֻ����ǰ30000�С�
		<% 
		} else{
		%>
		5���ļ�������Ҫ����3000�У����ֻ����ǰ3000�С�
		<% 
		} 
		%>
		</td>
	</tr>
	<tr >
		<td colspan="4" align="right" class="foot" width="100%">
			<div align="right">
				<button onclick="javascript:gwShare_queryDevice();" 
				name="gwShare_queryButton" style="CURSOR:hand" style="display:" > �� ѯ </button>
				<button onclick="javascript:gwShare_queryChange('3');" 
				name="gwShare_import" style="CURSOR:hand" style="display: none" >�����ѯ</button>
				<button onclick="javascript:gwShare_revalue();" 
				name="gwShare_reButto" style="CURSOR:hand" style="display: none" > �� �� </button>
			</div>
		</td>
	</tr>
</TABLE>
</form>