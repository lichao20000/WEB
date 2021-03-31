<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>

<lk:res />
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css2/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<lk:res />
<SCRIPT LANGUAGE="JavaScript">

$(function(){
Init();
});
function Init(){
	// ��ʼ������
	gwShare_change_select("vendor","-1");
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
	case "vendor":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='vendor']"),selectvalue);
			});
			break;
		case "deviceModel":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='vendor']").val();
			$("select[@name='devicetype']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			if("-1"==vendorId){
				$("select[@name='devicemodel']").html("<option value='-1'>==����ѡ���豸����==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='devicemodel']"),selectvalue);
			});
			break;
		case "devicetype":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
			var vendorId = $("select[@name='vendor']").val();
			var deviceModelId = $("select[@name='devicemodel']").val();
			if("-1"==deviceModelId){
				$("select[@name='devicetype']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='devicetype']"),selectvalue);
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



function detail(devicetype_id,cityId){
	var page = "";
		page="<s:url value='/gwms/resource/countDeviceACT!queryDetail.action'/>?"
		var url = page
				+ "devicetype=" + devicetype_id
				+ "&cityId=" +cityId;
		window.open(url,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");

}
 function detailAll(cityId,filterDevicetype){
	
	var page = "";
	page="<s:url value='/gwms/resource/countDeviceACT!queryDetailAll.action'/>?"
	var url = page
			+ "filterDevicetype=" + filterDevicetype
			+ "&cityId=" +cityId;
	window.open(url,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}



function  queryDataForExcel(vendor,devicemodel,devicetype,rela_dev_type,is_check,startTime,endTime,protocol2,protocol1,protocol0){
	var   page="gwms/resource/countDeviceACT!countDevice.action?";
   var url = page
			+ "excel=excel&vendor="+vendor+"&devicemodel="+devicemodel+"&devicetype="+devicetype+"&rela_dev_type="+rela_dev_type+"&is_check="+is_check+"&startTime="+startTime+"&endTime="+endTime+"&protocol2="+protocol2+"&protocol1="+protocol1+"&protocol0="+protocol0;
	document.frm2.action = url;
	document.frm2.method = "post";
	document.frm2.submit();
	
}

function  doQuery(){
   var vendor=$("select[@name='vendor']").val();
   var devicemodel=$("select[@name='devicemodel']").val();
   var devicetype=$("select[@name='devicetype']").val();
   var rela_dev_type=$("select[@name='rela_dev_type']").val();
   var is_check =$("select[@name='is_check']").val();
    var protocol2=$("input[name='protocol2']:checked").val();
    var protocol1=$("input[name='protocol1']:checked").val();
    var protocol0=$("input[name='protocol0']:checked").val();
   if(vendor=='-1' && devicemodel=='-1' && devicetype=='-1'&& rela_dev_type=='-1' && is_check=='-1'){
   if(typeof(protocol2)=="undefined" && typeof(protocol1)=="undefined" && typeof(protocol0)=="undefined"){
	  alert('��������һ���ѯ����');
	  return;
    }
     
  }
    $("button[@name='button']").attr("disabled", true);
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
	document.selectForm.submit();
	 
}
</SCRIPT>

</head>
<%@ include file="/toolbar.jsp"%>

<body>
<form name="selectForm"
	action="<s:url value="/gwms/resource/countDeviceACT!countDevice.action"/>"
	target="dataForm">
<table border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
	<tr>
		<td HEIGHT=20>&nbsp;</td>
	</tr>
	<tr>
		<td>
		<table class="green_gargtd" width="100%">
			<tr>
				<th>�豸���汾ͳ��</th>
				<td><img src="<s:url value="/images/attention_2.gif"/>"
					width="15" height="12"></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table class="querytable">

			<tr>
				<th colspan="4">�豸���汾ͳ��</th>
			</tr>
			<TR bgcolor="#FFFFFF">
				<TD class=column width="15%" align='right'>�豸����</TD>
				<TD width="35%"><select name="vendor" class="bk"
					onchange="gwShare_change_select('deviceModel','-1')">
				</select></TD>

				<TD class=column width="15%" align='right'>�豸�ͺ�</TD>
				<TD width="35%"><select name="devicemodel" class="bk"
					onchange="gwShare_change_select('devicetype','-1')">
					<option value="-1">==��ѡ����==</option>
				</select></TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD class=column width="15%" align='right'>�豸�汾</TD>
				<TD width="35%"><select name="devicetype" class="bk">
					<option value="-1">==��ѡ���ͺ�==</option>
				</select></TD>

				<TD class=column width="15%" align='right'>�豸����</TD>
				<TD width="35%"><select name="rela_dev_type" class="bk">
					<option value="-1">==��ѡ��==</option>
					<option value="1">e8-b</option>
					<option value="2">e8-c</option>
				</select></TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD class=column width="15%" align='right'>�ն˰汾�Ƿ�淶</TD>
				<TD width="35%"><select name="is_check" class="bk">
					<option value="-1">==��ѡ��==</option>
					<option value="1">��</option>
					<option value="0">��</option>
				</select></TD>

				<TD class=column width="15%" align='right'>����Э������</TD>
				<!-- <input type="checkbox"  style="border-color:transparent"> -->
				<TD width="35%"><input type="checkbox" name="protocol2"
					value="2">H248 <input type="checkbox" name="protocol1"
					value="1">����SIP <input type="checkbox" name="protocol0"
					value="0">IMS SIP</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD class=column width="15%" align='right'>�汾�״��ϱ���ʼʱ��</TD>
				<TD width="35%"><lk:date id="startTime" name="startTime"
					type="all" defaultDate="%{queryTime}"/></TD>
				<TD class=column width="15%" align='right'>�汾�״��ϱ�����ʱ��</TD>
				<TD width="35%"><lk:date id="endTime" name="endTime" type="all" />
				</TD>
			</TR>

			<TR>
				<td colspan="4" class=foot>
				<button id="cha" name="button" onclick="doQuery()">&nbsp;ͳ ��&nbsp;</button>
				&nbsp;&nbsp;
				<button type="reset">&nbsp;�� ��&nbsp;</button>
				</td>
			</TR>
			
		</table>
		</td>
	</tr>
	
	<tr>
		<td height="25">
		</td>
		</tr>
		<tr id="trData" style="display: none">
				<td class="colum">
					<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
						����ͳ�ƣ����Ե�....</div>
				</td>
			</tr>
</table>
<br>
</form>
<table width="100%">
			<tr>
		       <td><iframe id="dataForm" name="dataForm" height="0"
			frameborder="0" scrolling="yes" width="100%" ></iframe></td>
	            </tr>
		</table>
<form name="frm2" id="frm2"></form>

</body>
<%@ include file="/foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
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
   			dyniframe[i].style.display="block";
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
  		tempobj.style.display="block";
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