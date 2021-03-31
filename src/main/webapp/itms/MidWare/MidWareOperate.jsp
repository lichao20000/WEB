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

function add(){
	var oui = $.trim($("input[@name='oui']").val());
	var deviceSn = $.trim($("input[@name='deviceSn']").val());
	var deviceModel = $.trim($("input[@name='deviceModel']").val());
	var adNumber = $.trim($("input[@name='adNumber']").val());
	var status = $("input[@name='status'][@checked]").val();
	if(oui==""){
		alert("�����볧�����ͣ�");
		$("input[@name='oui']").focus();
		return false;
	}
	if(deviceSn==""){
		alert("�������豸���кţ�");
		$("input[@name='deviceSn']").focus();
		return false;
	}
	if(deviceModel==""){
		alert("�������豸���ͣ�");
		$("input[@name='deviceModel']").focus();
		return false;
	}
	if(adNumber==""){
		alert("������AD��ţ�");
		$("input[@name='adNumber']").focus();
		return false;
	}
	if(status==""||status=="-1"){
		alert("��ѡ��״̬��");
		$("input[@name='status']").focus();
		return false;
	}
	$("input[@name='des']").val("add device");
	var url = '<s:url value='/itms/midware/midWare!add.action'/>'; 
	operate(url);
}

function update(){
	var oui = $.trim($("input[@name='oui']").val());
	var deviceSn = $.trim($("input[@name='deviceSn']").val());
	var deviceModel = $.trim($("input[@name='deviceModel']").val());
	var adNumber = $.trim($("input[@name='adNumber']").val());
	var status = $("input[@name='status'][@checked]").val();
	if(oui==""){
		alert("�����볧�����ͣ�");
		$("input[@name='oui']").focus();
		return false;
	}
	if(deviceSn==""){
		alert("�������豸���кţ�");
		$("input[@name='deviceSn']").focus();
		return false;
	}
	if(deviceModel==""){
		alert("�������豸���ͣ�");
		$("input[@name='deviceModel']").focus();
		return false;
	}
	if(adNumber==""){
		alert("������AD��ţ�");
		$("input[@name='adNumber']").focus();
		return false;
	}
	if(status==""||status=="-1"){
		alert("��ѡ��״̬��");
		$("input[@name='status']").focus();
		return false;
	}
	$("input[@name='des']").val("change device info");
	var url = '<s:url value='/itms/midware/midWare!update.action'/>'; 
	operate(url);
}

function del(){
	var oui = $.trim($("input[@name='oui']").val());
	var deviceSn = $.trim($("input[@name='deviceSn']").val());

	if(oui==""){
		alert("�����볧�����ͣ�");
		$("input[@name='oui']").focus();
		return false;
	}
	if(deviceSn==""){
		alert("�������豸���кţ�");
		$("input[@name='deviceSn']").focus();
		return false;
	}
	
	$("input[@name='des']").val("delete device");
	var url = '<s:url value='/itms/midware/midWare!delete.action'/>'; 
	operate(url);
}

//�м������  
function operate(url){
	var deviceId = $.trim($("input[@name='deviceId']").val());
	var oui = $.trim($("input[@name='oui']").val());
	var deviceSn = $.trim($("input[@name='deviceSn']").val());
	var deviceModel = $.trim($("input[@name='deviceModel']").val());
	var adNumber = $.trim($("input[@name='adNumber']").val());
	var status = $.trim($("input[@name='status'][@checked]").val());
	var des = $.trim($("input[@name='des']").val());
	var area = $.trim($("input[@name='area']").val());
	var group = $.trim($("input[@name='group']").val());
	var phone = $.trim($("input[@name='phone']").val());
	$.post(url,{
		deviceId:deviceId,
		oui:oui,
		deviceSn:deviceSn,
		deviceModel:deviceModel,
		adNumber:adNumber,
		status:status,
		des:des,
		area:area,
		group:group,
		phone:phone
	},function(ajax){	
	    alert(ajax);
	});
}




</script>

<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>
						�м���豸����
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15"
							height="12">

					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name=frm>
			<input type="hidden" name="des" value="">
			<input type="hidden" name="deviceId" value="">
				<table class="querytable">
					<tr>
						<th colspan=4>
							�м���豸����
						</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							��������
						</td>
						<td>
							<input type="text" name="oui" value="">
							<font color="red">&nbsp;*</font>
						</td>
						<td class=column align=center width="15%">
							�豸���к�
						</td>
						<td>
							<input type="text" name="deviceSn" value="">
							<font color="red">&nbsp;*</font>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							�豸����
						</td>
						<td>
							<input type="text" name="deviceModel" value="">
							<font color="red">&nbsp;*</font>
						</td>
						<td class=column align=center width="15%">
							AD���
						</td>
						<td>
							<input type="text" name="adNumber" value="">
							<font color="red">&nbsp;*</font>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							״̬
						</td>
						<td>
							&nbsp;&nbsp;
							<input type="radio" name="status" value="1" checked>
							&nbsp;&nbsp;����&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="radio" name="status" value="0">
							&nbsp;&nbsp;������ &nbsp;&nbsp;
							<font color="red">*</font>
						</td>
						<td class=column align=center width="15%">
							�豸��������
						</td>
						<td>
							<input type="text" name="area" value="">
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							�豸������
						</td>
						<td>
							<input type="text" name="group" value="">
						</td>
						<td class=column align=center width="15%">
							�绰����
						</td>
						<td>
							<input type="text" name="phone" value="">
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="add()">
								&nbsp;�� ��&nbsp;
							</button>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<button onclick="update()">
								&nbsp;�� ��&nbsp;
							</button>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<button onclick="del()">
								&nbsp;ɾ ��&nbsp;
							</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>

	<tr>
		<td height="20">
		</td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>
