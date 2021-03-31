<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<%@ include file="/toolbar.jsp"%>

<!-- ���ն˰汾ͳ�Ʊ��� -->
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>

<SCRIPT LANGUAGE="JavaScript">

function Init(){
	// ��ʼ������
	gwShare_change_select("vendor","-1");
	
	// ��ͨ��ʽ�ύ
	/* var form = document.getElementById("mainForm");
	form.action = "<s:url value='/gwms/report/bindDevTypeStat!queryBindDevTypeList.action'/>";
	form.submit(); */
}

//��ѯ
function doQuery(){
	flowDiv();
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/gwms/report/bindDevTypeStat!queryBindDevTypeList.action'/>";
	form.submit();
}

function flowDiv(){
	$("input[name='gwShare_queryButton']").attr("disabled",true);
	$(".tips").show();
}
function hide(){
	$("input[name='gwShare_queryButton']").attr("disabled",false);
	$(".tips").hide();
}

//reset
function resetFrm() {
	$("select[@name='cityId']").attr("value",'-1')
	$("select[@name='vendorId']").attr("value",'-1')
    gwShare_change_select('deviceModel','-1');
}

function ToExcel(){
	var mainForm = document.getElementById("mainForm");
	mainForm.action="<s:url value='/gwms/report/bindDevTypeStat!queryBindDevTypeListExcel.action'/>";
	mainForm.submit();
	mainForm.action="<s:url value='/gwms/report/bindDevTypeStat!queryBindDevTypeList.action'/>";
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
			var url = "<s:url value='/gwms/share/gwDeviceQuery!getCityNextChild.action'/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='cityId']"),selectvalue);
			});
			break;
		case "vendor":
			var url = "<s:url value='/gwms/share/gwDeviceQuery!getVendor.action'/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='vendorId']"),selectvalue);
				$("select[@name='deviceModelId']").html("<option value='-1'>==����ѡ���豸����==</option>");
				//$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value='/gwms/share/gwDeviceQuery!getDeviceModel.action'/>";
			var vendorId = $("select[@name='vendorId']").val();
			if("-1"==vendorId){
				//$("select[@name='deviceModelId']").html("<option value='-1'>==����ѡ����==</option>");
				//$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
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
			var url = "<s:url value='/gwms/share/gwDeviceQuery!getDevicetype.action'/>";
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
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION="" target="dataForm" >
	<table width="98%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162">
				<div align="center" class="title_bigwhite">���ն˰汾����ͳ��</div>
				</td>
				<td>&nbsp; 
                    <img src="<s:url value="/images/attention_2.gif"/>" width="15"  height="12">
                </td>
			</tr>
	</table>
	<!-- ��ѯpart -->
	<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
		<tr>
			<td bgcolor=#999999>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center">
						<tr><th colspan="4" id="gwShare_thTitle">���ն˰汾����ͳ��</th></tr>
						<TR bgcolor=#ffffff>
	                       <td class="column" width='20%' align="right"> ����</td>
	                        <td width='30%' align="left">
								<select name="vendorId" class="bk" onchange="gwShare_change_select('deviceModel','-1')">
									<option value="-1">==��ѡ��==</option>
								</select>
	                        </td>
	                        <TD align="right" class=column width="15%">�ͺ�</TD>
							<TD align="left" width="35%">
                                <select name="deviceModelId" class="bk" onchange="gwShare_change_select('devicetype','-1')">
									<option value="-1">==��ѡ��==</option>
								</select>
							</TD>
	                   </TR>
	        
					   <TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD align="right" class=column width="15%">����ʱ��</TD>
							<TD width="35%">
                                <input type="text" name="starttime" class='bk' readonly value="<s:property value='starttime'/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.mainForm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��" />
								~
							<input type="text" name="endtime" class='bk' readonly value="<s:property value='endtime'/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.mainForm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��" />
							</TD>
                       		<td colspan="5" align="center" width="100%" class="column">
								<input type="button" class=jianbian  onclick="javascript:doQuery()"  
								name="gwShare_queryButton" value=" �� ѯ " />
								<input type="button" class=jianbian onclick="javascript:resetFrm()" 
								name="gwShare_reButto" value=" �� �� " />
							</td>
						</TR>
				</table>
			</td>
		</tr>
	</table>
	<div class="tips" style="background-color:#F5F5F5;z-index:999;padding:4;margin:4;display:none">
	 <div class="tipContent" style="font-size:18px">���ڲ�ѯ���Ե�.......</div>
	</div>
	<input type="hidden" value="" id="excelContent" name="excelContent"/>
	</FORM>
	<!-- չʾ���part -->
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center" height="100%">
		<TR>
			<TD id="idData">
              	<iframe id="dataForm" name="dataForm" height="600px !important" frameborder="0"  width="100%" src="" scrolling="yes"></iframe>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>&nbsp;</TD></TR>
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
	//dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 


</SCRIPT>
