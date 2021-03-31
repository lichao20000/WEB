<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<%@ include file="/toolbar.jsp"%>


<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript"
	SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">

function Init(){
	// ��ʼ������
	gwShare_change_select("city","-1");
	gwShare_change_select("vendor","-1");
}


function CheckForm(){   
	 var cityId = $("select[@name='cityId']").val();
	 var vendorId = $("select[@name='vendorId']").val();
	 var deviceModelId = $("select[@name='deviceModelId']").val();

	 var device_serialnumber = $("input[@name='device_serialnumber']").val();
	 var loopback_ip = $("input[@name='loopback_ip']").val();
		var task_name = $("input[@name='task_name']").val();
	 var deviceTypeId = $("select[@name='deviceTypeId']").val();

//	 alert("device_serialnumber:"+device_serialnumber);
//	 alert("loopback_ip:"+loopback_ip);
//	 alert("cityId:"+cityId);
//	   alert("vendorId:"+vendorId);
//	   alert("deviceModelId:"+deviceModelId);
//	   alert("deviceTypeId:"+deviceTypeId);
//	   alert("task_name:"+task_name);

//&& deviceModelId=="-1"
   if(device_serialnumber == ""){
	  
	   if(loopback_ip == "" ){
		    if(cityId=="-1" && vendorId=="-1" )
			   {
			    if( task_name=="")
	                   {
	            	  
	                     alert("�������ƻ����豸���кš��豸IP�����е�һ��������ء����̡��ͺ�������һ������һ������Ϊ�� ");
		                 return false;
		               }
		       }
     }
 }

   var loopback_ip = $.trim(document.mainForm.loopback_ip.value);
	   if(loopback_ip!=""){
			if(!IsIPAddr2(loopback_ip,"�豸IP")){
				document.mainForm.loopback_ip.focus();
				return false;
			}
		}
	   return true;
}


function doQuery()
{    
trimAll();
if(!CheckForm()){
	return;
}
// ��ͨ��ʽ�ύ  �Ͼ�
	var form = document.getElementById("mainForm");
	form.action = "<s:url value="/itms/config/digitDeviceACT!query.action"/>";
	form.submit();
	
}

/*LTrim(string):ȥ����ߵĿո�*/
function LTrim(str){
    var whitespace = new String("�� \t\n\r");
    var s = new String(str);   

    if (whitespace.indexOf(s.charAt(0)) != -1){
        var j=0, i = s.length;
        while (j < i && whitespace.indexOf(s.charAt(j)) != -1){
            j++;
        }
        s = s.substring(j, i);
    }
    return s;
}

/*RTrim(string):ȥ���ұߵĿո�*/
function RTrim(str){
    var whitespace = new String("�� \t\n\r");
    var s = new String(str);
 
    if (whitespace.indexOf(s.charAt(s.length-1)) != -1){
        var i = s.length - 1;
        while (i >= 0 && whitespace.indexOf(s.charAt(i)) != -1){
            i--;
        }
        s = s.substring(0, i+1);
    }
    return s;
}

/*Trim(string):ȥ���ַ������ߵĿո�*/
function trim(str){
    return RTrim(LTrim(str)).toString();
}

//ȫ��trim
function trimAll()
{
	var inputs = document.getElementsByTagName("input");
	for(var i=0;i<inputs.length;i++)
	{
		var input = inputs[i];
		
		if(/text/gi.test(input.type))
		{
			input.value = trim(input.value);
		}
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
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getCityNextChild.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='cityId']"),selectvalue);
			});
			break;
		case "vendor":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='vendorId']"),selectvalue);
				$("select[@name='deviceModelId']").html("<option value='-1'>==����ѡ���豸����==</option>");
				$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='deviceModelId']").html("<option value='-1'>==����ѡ����==</option>");
				$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='deviceModelId']"),selectvalue);
				$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "devicetype":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			var deviceModelId = $("select[@name='deviceModelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='deviceTypeId']"),selectvalue);
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

</SCRIPT>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION=""
			target="dataForm">
		<table width="98%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162">
				<div align="center" class="title_bigwhite">��ͼ����ģ�����</div>
				</td>
				<td><img src="../../images/attention_2.gif" width="15"
					height="12">��ͼ�����·�����</td>
				<td>&nbsp;</td>

			</tr>
		</table>
		<!-- ��ѯpart -->
		<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
			<tr>
				<td bgcolor=#999999>
				<table border=0 cellspacing=1 cellpadding=2 width="100%"
					align="center">
					<tr>
						<th colspan="4" id="gwShare_thTitle">��ͼ�����·������ѯ</th>
					</tr>
					<TR bgcolor=#ffffff>
						<td class="column" width='20%' align="right">�豸���кţ�</td>
						<td width='30%' align="left"><input
							name="device_serialnumber" type="text" class='bk' value="">
						</td>

						<td class="column" width='20%' align="right">�豸IP��</td>
						<td width='30%' align="left"><input name="loopback_ip"
							type="text" class='bk' value=""></td>
					</TR>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="20%">�豸���̣�</TD>
						<TD align="left" width="30%"><select name="vendorId"
							class="bk" onchange="gwShare_change_select('deviceModel','-1')">
							<option value="-1">==��ѡ��==</option>
						</select></TD>
						<TD align="right" class=column width="20%">�豸�ͺţ�</TD>
						<TD width="30%"><select name="deviceModelId" class="bk"
							onchange="gwShare_change_select('devicetype','-1')">
							<option value="-1">����ѡ����</option>
						</select></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<td class="column" width='20%' align="right">����汾��</td>
						<td width='30%' align="left"><select name="deviceTypeId"
							class="bk"">
							<option value="-1">����ѡ���豸�ͺ�</option>
						</select></td>
						<td class="column" width='20%' align="right">�豸���أ�</td>
						<td width='30%' align="left"><!--<s:select list="cityList" name="cityId" headerKey="-1"
									headerValue="��ѡ������" listKey="city_id" listValue="city_name"
									value="digitMap.city_id" cssClass="bk"></s:select>--> <select
							name="cityId" class="bk">
							<option value="-1">==��ѡ��==</option>
						</select></td>

					</TR>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<td class="column" width='20%' align="right">loid��</td>
						<td width='30%' align="left"><input name="device_id_ex"
							type="text" class='bk' value=""></td>

						<td class="column" width='20%' align="right">�������ƣ�</td>
						<td width='30%' align="left"><input name="task_name"
							type="text" class='bk' value=""> <font color="red">*ֻ֧����ȫƥ��</font>
						</td>
					</TR>

					<tr bgcolor="#FFFFFF">
						<td colspan="4" align="right" class="green_foot" width="100%">
						<input type="button" class=jianbian onclick="javascript:doQuery()"
							name="gwShare_queryButton" value=" �� ѯ " /> <input type="reset"
							class=jianbian name="gwShare_reButto" value=" �� �� " /></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</FORM>
		<!-- չʾ���part -->
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999 id="idData"><iframe id="dataForm"
					name="dataForm" height="0" frameborder="0" scrolling="no"
					width="100%" src=""></iframe></TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>&nbsp;</TD>
	</TR>
</TABLE>

<%@ include file="/foot.jsp"%>

<SCRIPT LANGUAGE="JavaScript">

Init();

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


</SCRIPT>
