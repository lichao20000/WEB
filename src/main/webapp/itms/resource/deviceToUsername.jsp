<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
			src="../../Js/My97DatePicker/WdatePicker.js"></script>

<script language="JavaScript">

function deviceResult(returnVal){

	//for(var i=0;i<returnVal[2].length;i++){
	//	$("td[@id='tdDeviceId']").append("<br>"+returnVal[2][i][0]);
	//	$("td[@id='tdDeviceOui']").append("<br>"+returnVal[2][i][1]);
	//	$("td[@id='tdDeviceSn']").append("<br>"+returnVal[2][i][2]);
	//	$("td[@id='tdDeviceLoopbackIp']").append("<br>"+returnVal[2][i][3]);
	//	$("td[@id='tdDeviceCityId']").append("<br>"+returnVal[2][i][4]);
	//	$("td[@id='tdDeviceCityName']").append("<br>"+returnVal[2][i][5]);
	//}
	document.all("txtdeviceId").value=returnVal[2][0][0];
	document.all("txtdeviceSn").value=returnVal[2][0][2];
	document.all("txtoui").value=returnVal[2][0][1];
	$("div[@id='selectedDev']").html("<strong>�������豸���к�:"+returnVal[2][0][2]+"</strong>");
	
	this.tr1.style.display="";
	document.all("deviceResult").style.display="none";
	document.all("btnDevRes").value="չ����ѯ";
	
	}

function doQuery(){
	var url="<s:url value='/itms/resource/deviceToUsername.action?deviceId='/>";
	var deviceId = $.trim($("input[@name='txtdeviceId']").val());
	var deviceSn = $.trim($("input[@name='txtdeviceSn']").val());
	var oui = $.trim($("input[@name='txtoui']").val());
	var starttime = $.trim($("input[@name='starttime']").val());
    //var endtime = $.trim($("input[@name='endtime']").val());
    if(starttime==""){
    	alert("�������ѯʱ�䣡");
    	return;
    }
    //if(endtime==""){
    //	alert("���������ʱ�䣡");
    //	return;
    //}
    //if(starttime.substring(0,7)!=endtime.substring(0,7)){
    //	alert("��ʼʱ��ͽ���ʱ��Ҫ��ͬһ�����ڣ�");
    //	return;
    //}
	document.all("myiframe1").src = url+deviceId+"&deviceSn="+deviceSn+"&oui="+oui+"&starttime="+starttime+"&refresh="+(new Date()).getTime();
}

function txtSelectDevice()
{		
	if("none"==document.all("deviceResult").style.display){
		document.all("deviceResult").style.display="";
		document.all("btnDevRes").value="���ز�ѯ";
	}else{
		document.all("deviceResult").style.display="none";
		document.all("btnDevRes").value="չ����ѯ";
	}
}

//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=["myiframe1"]

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


$(window).resize(function(){
	dyniframesize();
}); 
</script>

<br>
<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0"
				cellpadding="0" class="green_gargtd">
				<tr>
					<td width="164" align="center" class="title_bigwhite">
						�豸�ϱ��˺���־��ѯ
					</td>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15"
							height="12">
						ͨ���豸��ѯ�û��˺�,ʱ��Ϊ�ϱ�ʱ�䡣��ѡ��ʱ�䡣
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr bgcolor=#ffffff>
		<td>
			<table id="deviceResult" width="100%" border=0 cellspacing=0
				cellpadding=0 align="center" valign="middle" STYLE="display: ">
				<tr bgcolor=#ffffff>
					<td class=column colspan="4">
						<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr id="tr1" STYLE="display: none" bgcolor="#FFFFFF">
		<td>
			<form name="frm">
				<table border=0 cellspacing=1 cellpadding=2 width="100%" bgcolor=#999999>
					<TR>
						<TH colspan="4" align="center">
							�豸�ϱ��˺���־��ѯ
						</TH>
					</TR>
					<tr bgcolor="#FFFFFF">
						<td width="50%" colspan="2">
							<div id="selectedDev">
								���ѯ�豸��
							</div>
							<input type="hidden" name="txtdeviceId" value="" />
							<input type="hidden" name="txtdeviceSn" value="" />
							<input type="hidden" name="txtoui" value="" />
						</td>
						<td align="right" width="50%" colspan="2">
							<input type="button" name="btnDevRes" class=jianbian value="���ز�ѯ"
								onclick="txtSelectDevice()" />
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column width="15%" align="right">
							��ѯʱ��
						</td>
						<td align="left" width="35%" colspan="3">
							<input type="text" name="starttime" class='bk' readonly
								value="<s:property value="starttime" />">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��" />
							
						</td>
						<!-- <td class=column align="right" width="15%">
							����ʱ��
						</td>
						<td align="left" width="35%">
							<input type="text" name="endtime" class='bk' readonly
								value="<s:property value="endtime" />">
							<img
								onclick="new WdatePicker(document.frm.endtime,'%Y-%M-%D',true,'whyGreen')"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="ѡ��">
						</td> -->
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()">
								&nbsp;�� ѯ&nbsp;
							</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>
	<TR bgcolor=#ffffff>
		<td>
			<DIV id=qh_con0 style="DISPLAY: block">
				<iframe id=myiframe1 src="" frameborder="0" scrolling="no"
					height="100%" width="100%"
					onload=this.height=myiframe1.document.body.scrollHeight;></iframe>
			</DIV>
		</td>
	</TR>

</TABLE>

<%@ include file="/foot.jsp"%>
