<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>

<script language="JavaScript">

var gw_type = '<%=request.getParameter("gw_type")%>';

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
	
	$("tr[@id='trUserData']").show();
	var url = '<s:url value='/gwms/blocTest/QueryCustomerInfo!query.action'/>'; 
	$.post(url,{
		device_id:returnVal[2][0][0]
	},function(ajax){	
	    $("div[@id='UserData']").html("");
		$("div[@id='UserData']").append(ajax);
	});
}

function doQuery(){

	var tdobj = document.getElementById('td1');
	if(tdobj != null){
		if("�豸û�а��û� ���û��Ϳͻ���Ϣ��" == tdobj.innerText){
			alert("�豸û�а��û����ݲ�����ϣ�");
			return;
		}
	}
	
    var txtdeviceId = $.trim($("input[@name='txtdeviceId']").val());
    if(txtdeviceId == ""){
         alert("���ѯ�豸");
         return false;
    }
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("������ϣ����Ե�....");
    var url = '<s:url value='/gwms/blocTest/bbmsDiag!wanServDiag.action'/>'; 
	$.post(url,{
		deviceId:txtdeviceId,
		gw_type:gw_type
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
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

</script>

<br>
<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0"
				cellpadding="0" class="green_gargtd">
				<tr>
					<td width="164" align="center" class="title_bigwhite">
						��ҵ���ؿ��ҵ�����
					</td>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15"
							height="12">
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
				<table border=0 cellspacing=1 cellpadding=2 width="100%"
					bgcolor=#999999>
					<TR>
						<TH colspan="4" align="center">
							��ҵ���ؿ��ҵ��������
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
					<tr id="trUserData" style="display: none" bgcolor="#FFFFFF">
						<td class="colum" colspan="4">
							<div id="UserData" style="width: 100%; z-index: 1; top: 100px">
							</div>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()">
								&nbsp;�� ��&nbsp;
							</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>
	<tr id="trData" style="display: none">
		<td class="colum">
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				������ϣ����Ե�....
			</div>
		</td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>
