<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
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

function gwShare_queryField_selected(value){
	$("input[@name='gwShare_queryField_temp']").val(value.value);
}

/*------------------------------------------------------------------------------
//������:		queryChange
//����  :	change 1:�򵥲�ѯ��2:�߼���ѯ
//����  :	���ݴ���Ĳ���������ʾ�Ľ���
//����ֵ:		��������
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function isEmpty(data){
	return (null == data || data == "" || data == undefined) ? true:false;
}
function gwShare_queryDevice(){
	var	width=800;    
	var height=450; 
	var gwShare_queryResultType = $("input[@name='gwShare_queryResultType']").val();
	var url="<s:url value="/gtms/stb/share/shareDeviceQuery!queryDeviceList.action"/>?gwShare_queryResultType="+gwShare_queryResultType;
    var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
    url = url + "&gwShare_queryType=" + gwShare_queryType;
    if("1"==gwShare_queryType){
		var gwShare_queryField = $("input[@name='gwShare_queryField'][@checked]").val();
		var gwShare_queryParam = $("input[@name='gwShare_queryParam']").val();
		if(trim(gwShare_queryParam)==""){
			alert("�������ѯ������");
			return;
		}
		url = url + "&gwShare_queryField=" + gwShare_queryField;
		url = url + "&gwShare_queryParam=" + gwShare_queryParam;
	}else if("2"==gwShare_queryType){
	 	var gwShare_cityId = $("select[@name='gwShare_cityId']").val();
        var gwShare_onlineStatus = $("select[@name='gwShare_onlineStatus']").val();
        var gwShare_vendorId = $("select[@name='gwShare_vendorId']").val();
        var gwShare_deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
        var gwShare_devicetypeId = $("select[@name='gwShare_devicetypeId']").val();
        var gwShare_bindType = $("select[@name='gwShare_bindType']").val();
        var gwShare_deviceSerialnumber = $("input[@name='gwShare_deviceSerialnumber']").val();
        
        url = url + "&gwShare_cityId=" + gwShare_cityId;
        url = url + "&gwShare_onlineStatus=" + gwShare_onlineStatus;
        url = url + "&gwShare_vendorId=" + gwShare_vendorId;
        url = url + "&gwShare_deviceModelId=" + gwShare_deviceModelId;
        url = url + "&gwShare_devicetypeId=" + gwShare_devicetypeId;
        url = url + "&gwShare_bindType=" + gwShare_bindType;
        url = url + "&gwShare_deviceSerialnumber=" + gwShare_deviceSerialnumber;
        <%if("xj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
        //���̣��ͺţ��汾����
        if(isEmpty(gwShare_vendorId) || (gwShare_vendorId.indexOf("-1") != -1)){
        	alert("��ѡ���̣�");
        	return;
        }
        if(isEmpty(gwShare_deviceModelId) || (gwShare_deviceModelId.indexOf("-1") != -1)){
        	alert("��ѡ���ͺţ�");
        	return;
        }
        if(isEmpty(gwShare_devicetypeId) || (gwShare_devicetypeId.indexOf("-1") != -1)){
        	alert("��ѡ��汾��");
        	return;
        }
        
        var gwShare_hardwareVersion = $("select[@name='gwShare_hardwareVersion']").val();
        var gwShare_belong = $("select[@name='belong']").val();
        url = url + "&gwShare_hardwareVersion="+gwShare_hardwareVersion;
        url = url + "&gwShare_belong="+gwShare_belong;
        <%}%>
	}
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
	gwShare_queryChange(queryType);
	
	var str = Array($("input[@name='gwShare_queryParam']"),
					$("div[@id='gwShare_msgdiv']"),
					$("input[@name='gwShare_queryField_temp']"));
	
	//������������������ʱȥ������Ӧƥ��
	//$.autoMatch3("<s:url value='/gtms/stb/share/shareDeviceQuery!getDeviceSn.action'/>",str,"#");
});

/*------------------------------------------------------------------------------
//������:		��дreset
//����  :	change 1:�򵥲�ѯ��2:�߼���ѯ��3�������ѯ
//����  :	��ҳ���������
//����ֵ:		ҳ������
//˵��  :	
//����  :	Create 2010-4-26 of By qxq
------------------------------------------------------------------------------*/
function gwShare_revalue(){
	var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
	if("1"==gwShare_queryType){
		$("input[@name='gwShare_queryField']").get(0).checked = true;
		$("input[@name='gwShare_queryParam']").val("");
	}else if("2"==gwShare_queryType){
		$("select[@name='gwShare_cityId']").attr("value",'-1')
		$("select[@name='gwShare_onlineStatus']").attr("value",'-1')
		$("select[@name='gwShare_vendorId']").attr("value",'-1')
		$("select[@name='gwShare_bindType']").attr("value",'-1')
       	$("input[@name='gwShare_deviceSerialnumber']").val("");
		gwShare_change_select('deviceModel','-1');
	}
}

/*------------------------------------------------------------------------------
//������:		queryChange
//����  :	change 1:�򵥲�ѯ��2:�߼���ѯ
//����  :	���ݴ���Ĳ���������ʾ�Ľ���
//����ֵ:		��������
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function gwShare_queryChange(change){
	var gwShare_gaoji_value = $("input[@name='gwShare_gaoji_value']").val();
	switch (change){
		case "2":
			$("td[@id='gwShare_thTitle']").html("�� �� �� ѯ");
			$("input[@name='gwShare_queryType']").val("2");
			$("button[@name='gwShare_jiadan']").css("display","");
			$("button[@name='gwShare_gaoji']").css("display","none");
			$("tr[@id='gwShare_tr11']").css("display","none");
			$("tr[@id='gwShare_tr12']").css("display","none");
			$("tr[@id='gwShare_tr21']").css("display","");
			$("tr[@id='gwShare_tr22']").css("display","");
			$("tr[@id='gwShare_tr23']").css("display","");
			$("tr[@id='gwShare_tr24']").css("display","");
			$("tr[@id='gwShare_tr31']").css("display","none");
			$("tr[@id='gwShare_tr32']").css("display","none");
			<%if("xj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
			$("tr[@id='gwShare_tr25']").css("display","");
			<%}%>
			$("button[@name='gwShare_queryButton']").val(" �� ѯ ");
			//���������
			gwShare_change_select("city","-1");
			gwShare_change_select("vendor","-1");
			break;
		case "3":
			$("td[@id='gwShare_thTitle']").html("�� �� �� ѯ");
			$("input[@name='gwShare_queryType']").val("3");
			$("button[@name='gwShare_jiadan']").css("display","");
			$("button[@name='gwShare_gaoji']").css("display",gwShare_gaoji_value);
			$("tr[@id='gwShare_tr11']").css("display","none");
			$("tr[@id='gwShare_tr12']").css("display","none");
			$("tr[@id='gwShare_tr21']").css("display","none");
			$("tr[@id='gwShare_tr22']").css("display","none");
			$("tr[@id='gwShare_tr23']").css("display","none");
			$("tr[@id='gwShare_tr24']").css("display","none");
			$("tr[@id='gwShare_tr31']").css("display","");
			$("tr[@id='gwShare_tr32']").css("display","");
			<%if("xj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
			$("tr[@id='gwShare_tr25']").css("display","none");
			<%}%>
			$("button[@name='gwShare_queryButton']").val("�����ļ�");
			break;
		case "1":
			$("td[@id='gwShare_thTitle']").html("�� �� �� ѯ");
			$("input[@name='gwShare_queryType']").val("1");
			$("button[@name='gwShare_jiadan']").css("display","none");
			$("button[@name='gwShare_gaoji']").css("display",gwShare_gaoji_value);
			$("tr[@id='gwShare_tr11']").css("display","");
			$("tr[@id='gwShare_tr12']").css("display","");
			$("tr[@id='gwShare_tr21']").css("display","none");
			$("tr[@id='gwShare_tr22']").css("display","none");
			$("tr[@id='gwShare_tr23']").css("display","none");
			$("tr[@id='gwShare_tr24']").css("display","none");
			$("tr[@id='gwShare_tr31']").css("display","none");
			$("tr[@id='gwShare_tr32']").css("display","none");
			<%if("xj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
			$("tr[@id='gwShare_tr25']").css("display","none");
			<%}%>
			$("button[@name='gwShare_queryButton']").val(" �� ѯ ");
			break;
		default:
			break;
	}
}

/*------------------------------------------------------------------------------
//������:		deviceSelect_change_select
//����  :	type 
	            vendor      �����豸����
	            deviceModel �����豸�ͺ�
	            devicetype  �����豸�汾
//����  :	����ҳ������̡��ͺż�����
//����ֵ:		
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function gwShare_change_select(type,selectvalue){
	switch (type){
		case "city":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getCityNextChild.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_cityId']"),selectvalue);
			});
			break;
		case "vendor":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_vendorId']"),selectvalue);
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==����ѡ���豸����==</option>");
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==����ѡ����==</option>");
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_deviceModelId']"),selectvalue);
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "devicetype":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDevicetype.action"/>";
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			var deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_devicetypeId']"),selectvalue);
			});
			break;	
		case "hardwareversion":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDeviceHardVersion.action"/>";
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			var deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='gwShare_hardwareVersion']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_hardwareVersion']"),selectvalue);
			});
			break;
		default:
			alert("δ֪��ѯѡ�");
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
<input type="hidden" name="gwShare_OrderByUpdateDate" value="1" /><!-- 1������¼�����ϱ�ʱ������0������ -->
<input type="hidden" name="gwShare_RecordNum" value="0" /><!-- Ĭ��3����¼��0Ϊ��ѯȫ�����������ô�С������50 -->

<TABLE width="100%" class="querytable" align="center">
	<tr><td colspan="4" class="title_1" id="gwShare_thTitle">�� �� �� ѯ</td></tr>
	<tr id="gwShare_tr11" STYLE="display:">
		<td colspan="4" align="center" width="100%">
			<div align="center">
				<input type="input" class="bk" name="gwShare_queryParam" size="60" maxlength="60"/>
				<br /> 
				<div id="gwShare_msgdiv" STYLE="display:none" > </div>
			</div>
		</td>
	</tr>
	<tr id="gwShare_tr12" STYLE="display:">
		<td colspan="4" align="center" width="100%">
			<div align="center">
				<input type="radio" class=jianbian name="gwShare_queryField" value="custaccount" checked onclick="gwShare_queryField_selected(this)"/> ҵ���˺� &nbsp;&nbsp;
				<input type="radio" class=jianbian name="gwShare_queryField" value="deviceSn" onclick="gwShare_queryField_selected(this)"/> �豸���к� &nbsp;&nbsp;
				<input type="radio" class=jianbian name="gwShare_queryField" value="deviceIp" onclick="gwShare_queryField_selected(this)"/> �豸IP &nbsp;&nbsp;
				
				<!-- <input type="radio" class=jianbian name="gwShare_queryField" value="all" onclick="gwShare_queryField_selected(this)"/> ȫ �� &nbsp;&nbsp; -->
			</div>
		</td>
	</tr>
	<TR id="gwShare_tr21" STYLE="display:none">
		<TD align="right" class="title_2" width="15%">��    ��</TD>
		<TD align="left" width="35%">
			<select name="gwShare_cityId" class="bk">
				<option value="-1">==��ѡ��==</option>
			</select>
		</TD>
		<TD align="right" class="title_2" width="15%">����״̬</TD>
		<TD width="35%">
			<select name="gwShare_onlineStatus" class="bk">
				<option value="-1">==��ѡ��==</option>
				<option value="0">����</option>
				<option value="1">����</option>
			</select>
		</TD>
	</TR>
	<TR id="gwShare_tr22" STYLE="display:none">
		<TD align="right" class="title_2" width="15%">��    ��</TD>
		<TD width="35%">
			<select name="gwShare_vendorId" class="bk" onchange="gwShare_change_select('deviceModel','-1')">
				<option value="-1">==��ѡ��==</option>
			</select>
		</TD>
		<TD align="right" class="title_2" width="15%">�豸�ͺ�</TD>
		<TD align="left" width="35%">
			<select name="gwShare_deviceModelId" class="bk" onchange="gwShare_change_select('devicetype','-1')">
				<option value="-1">����ѡ����</option>
			</select>
		</TD>
	</TR>
	<TR id="gwShare_tr23"  STYLE="display:none">	
		<TD align="right" class="title_2" width="15%">�豸�汾</TD>
		<TD width="35%">
		<%if("xj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
		<select name="gwShare_devicetypeId" class="bk" onchange="gwShare_change_select('hardwareversion','-1')">
				<option value="-1">����ѡ���豸�ͺ�</option>
			</select>
		<%}else{%>
			<select name="gwShare_devicetypeId" class="bk">
				<option value="-1">����ѡ���豸�ͺ�</option>
			</select>
		<%} %>
		</TD>	
		<TD align="right" class="title_2" width="15%">�Ƿ��</TD>
		<TD width="35%">
			<select name="gwShare_bindType" class="bk">
				<option value="-1">==��ѡ��==</option>
				<option value="0">δ��</option>
				<option value="1">�Ѱ�</option>
			</select>
		</TD>
	</TR>
	<TR id="gwShare_tr25"  STYLE="display:none">
		<TD align="right" class="title_2" width="15%">Ӳ���汾</TD>
		<TD width="35%">
			<select name="gwShare_hardwareVersion" class="bk">
			  <option value="-1">����ѡ���豸�汾</option>
			</select>
		</TD>
		<TD align="right" class="title_2" width="15%">ƽ̨</TD>
		<TD width="35%">
			<select name="belong" class="bk">
			<option value="-1">==��ѡ��==</option>
			 <option value="(0,1)">����</option>
			 <option value="(2,3)">��Ϊ</option>
			 <option value="(4)">�⿴</option>
			</select>
		</TD>
	</TR>
	<TR id="gwShare_tr24"  STYLE="display:none">
		<TD align="right" class="title_2" width="15%">�豸���к�</TD>
		<TD width="35%">
			<input type="text" name="gwShare_deviceSerialnumber" value="" size="25" maxlength="40" class="bk"/>
			<font color="red">��ģ��ƥ��</font>
		</TD>
		<TD align="right" class="title_2" width="15%"> </TD>
		<TD width="35%">
		</TD>
	</TR>
	<tr id="gwShare_tr31" bgcolor="#FFFFFF"  style="display:none">
		<td align="right" width="15%">�ύ�ļ�</td>
		<td colspan="3" width="85%">
			<div id="importUsername">
				<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="<s:url value="/gtms/stb/share/FileUpload.jsp"/>" height="20" width="100%">
				</iframe>
				<input type="hidden" name=gwShare_fileName value=""/>
			</div>
		</td>
	</tr>
	<tr id="gwShare_tr32" style="display:none">
		<td CLASS="green_foot" align="right">ע������</td>
		<td colspan="3" CLASS="green_foot">
		1����Ҫ������ļ���ʽ����Excel���ı��ļ�����xls��txt��ʽ ��
		 <br>
		2���ļ��ĵ�һ��Ϊ�����У������û��˺š������ߡ��豸���кš���
		 <br>
		3���ļ�ֻ��һ�С�
		 <br>
		4���ļ�������Ҫ̫�࣬����Ӱ�����ܡ�
		</td>
	</tr>
	<tr >
		<td colspan="4" align="right" class="foot" width="100%">
			<div align="right">
				<button onclick="javascript:gwShare_queryDevice();" 
				name="gwShare_queryButton" style="CURSOR:hand" style="display:" > �� ѯ </button>
				<button onclick="javascript:gwShare_queryChange('1');" 
				name="gwShare_jiadan" style="CURSOR:hand" style="display: none" >�򵥲�ѯ</button>
				<button onclick="javascript:gwShare_queryChange('2');" 
				name="gwShare_gaoji" style="CURSOR:hand" style="display: none" >�߼���ѯ</button>
				<button onclick="javascript:gwShare_revalue();" 
				name="gwShare_reButto" style="CURSOR:hand" style="display: none" > �� �� </button>
			</div>
		</td>
	</tr>
</TABLE>
</form>