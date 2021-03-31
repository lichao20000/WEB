<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>

<script language="JavaScript">


function doQuery(){
	var serviceId = $.trim($("select[@name='serviceId']").val());
	var device_serialnumber = $.trim($("input[@name='device_serialnumber']").val());
    var userName = $.trim($("input[@name='userName']").val());

	if(device_serialnumber==""&&userName==""){
		alert("���������û��˺Ż��豸���к�����һ�");
		$("input[@name='userName']").focus();
		return false;
	}
	if(0<device_serialnumber.length&&device_serialnumber.length<6){
		alert("���������豸���кź���λ��");
		$("input[@name='device_serialnumber']").focus();
		return false;
	}
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
    var url = '<s:url value='/bbms/report/serviceQuery.action'/>'; 
	$.post(url,{
		serviceId:serviceId,
		device_serialnumber:device_serialnumber,
		userName:userName
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
}

function ToExcel(serviceId,device_serialnumber,userName) {
	var page="<s:url value='/bbms/report/serviceQuery!getExcel.action'/>?"
		+ "&serviceId=" + serviceId
		+ "&device_serialnumber=" + device_serialnumber
		+ "&userName=" + userName;
	document.all("childFrm").src=page;
}

</script>

<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>
						ҵ��ʹ�ò�ѯ
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15"
							height="12">
						��ѯ�豸��ҵ�������
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name=frm>
				<table class="querytable">
					<tr>
						<th colspan=4>
							ҵ��ʹ�ò�ѯ
						</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column width="15%">
							�û��˺�
						</td>
						<td>
							<input type="text" name="userName" value="" class="bk">
						</td>
						<td class=column width="15%">
							�豸���к�
						</td>
						<td>
							<input type="text" name="device_serialnumber" class='bk' value="">
							<font color="red">(�������6λ)</font>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column width="15%">
							ҵ������
						</td>
						<td colspan="3">
							<s:select list="serviceTypeList" name="serviceId" headerKey="-1"
								headerValue="����ҵ��" listKey="service_id" listValue="service_name"
								value="serviceId" cssClass="bk"></s:select>
						</td>
						
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

	<tr id="trData" style="display: none">
		<td class="colum">
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				���ڲ�ѯ�����Ե�....
			</div>
		</td>
	</tr>
	<tr>
		<td height="20">
		</td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>
