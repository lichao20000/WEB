<%--
Author      : ������
Date		: 2016-10-12
Desc		: �����������������
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>

<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
	<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript">
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
var deviceIds = "";
var devicetypeIds = "";
$(function(){
	$("input[@name='gwShare_queryResultType']").val("checkbox");
	gwShare_setGaoji();
	gwShare_setImport();
});

function CheckForm(){
	if($("input[@name='bsuBean.deviceIds']").val()==""){
		alert("��ѡ���豸��");
		return false;
	}
	if($("input[@name='bsuBean.up_begin']").val()==""){
		alert("��ѡ��������ʼʱ�䣡");
		$("input[@name='bsuBean.up_begin']").focus();
		return false;
	}
	if($("input[@name='bsuBean.up_end']").val()==""){
		alert("��ѡ����������ʱ�䣡");
		$("input[@name='bsuBean.up_end']").focus();
		return false;
	}
	if($("input[@name='bsuBean.expire_t']").val()==""){
		alert("��ѡ������ʧЧʱ�䣡");
		$("input[@name='bsuBean.expire_t']").focus();
		return false;
	}

	if($("input[@name='bsuBean.task_name']").val()==""){
		alert("����д�������ƣ�");
		$("input[@name='bsuBean.task_name']").focus();
		return false;
	} 

		var up_begin_check = $("input[@name='bsuBean.up_begin']").val().replace(":","");
		var up_end_check = $("input[@name='bsuBean.up_end']").val().replace(":","");
		if(up_begin_check>up_end_check){
			alert("������ʼʱ��ӦС����������ʱ��");
			return false;
		}
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/gwms/resource/batchSoftUpACT!batchSoftwareUp.action'/>";
	form.submit();
	document.getElementById("civiButton").style.display="none";
}

function deviceResult(returnVal){	
	
	
	deviceIds="";
	devicetypeIds="";
	if(returnVal[0]==0){
		$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+returnVal[2].length+"</strong></font>");
		for(var i=0;i<returnVal[2].length;i++){
			deviceIds = deviceIds + returnVal[2][i][0]+",";
			devicetypeIds = devicetypeIds + returnVal[2][i][11]+",";		
		}
		deviceIds = deviceIds.substring(0,deviceIds.lastIndexOf(",") );
		devicetypeIds = devicetypeIds.substring(0,devicetypeIds.lastIndexOf(","));
		$("input[@name='bsuBean.deviceIds']").val(deviceIds);
		$("input[@name='bsuBean.devicetypeIds']").val(devicetypeIds);
	}else{
		$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+returnVal[0]+"</strong></font>");
		$("input[@name='bsuBean.deviceIds']").val(deviceIds);
		$("input[@name='bsuBean.devicetypeIds']").val(devicetypeIds);
	}
}
	
</SCRIPT>
<%@ include file="../../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>

			<table width="98%" border="0" align="center" cellpadding="0"
				cellspacing="0" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									�����������������
								</td>
								<td nowrap>
									<img src="<s:url value='/images/attention_2.gif'/>" width="15"
										height="12">

								</td>
							</tr>
						</table>
					</td>
				</tr>
				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/gwms/share/gwShareDeviceQuery_upgrade.jsp"%>
					</td>
				</TR>
				<TR>
					<TH colspan="4" align="center">
						������������
					</TH>
				</TR>
				<tr>
					<td>
						<FORM NAME="mainForm" id="mainForm" METHOD="post"
							ACTION=""  target="dataForm">
							<input type="hidden" name="bsuBean.deviceIds" value="" />
							<input type="hidden" name="bsuBean.devicetypeIds" value="" />
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<tr bgcolor="#FFFFFF">
												<td colspan="4">
													<div id="selectedDev">
														���ѯ�豸��
													</div>
												</td>
											</tr>
											<tr bgcolor="#FFFFFF">

												<td align="right" width="15%">
													����������Է�ʽ��
												</td>
												<td align="left" width="30%">
													����ִ��
												</td>

												<td align="right" width="15%">
													�������Ŀ��汾��
												</td>
												<td align="left" width="30%">
													�����豸�Զ�ƥ��
												</td>
											</tr>
											<tr bgcolor="#FFFFFF">

												<td align="right" width="15%">
													������ʼʱ�䣺
												</td>
												<td align="left" width="30%">
													<input type="text"  id="up_begin" name="bsuBean.up_begin" readonly class=bk >
														<img name="shortDateimg"
									 						onClick="WdatePicker({el:document.getElementById('up_begin'),dateFmt:'HH:mm',skin:'whyGreen'})"
									 							src="../../images/dateButton.png" width="15" height="12"
									 								border="0" alt="ѡ��">&nbsp;<font color="#FF0000">*</font>
												</td>

												<td align="right" width="15%">
													��������ʱ�䣺
												</td>
												<td align="left" width="30%">
													<input type="text" id="up_end" name="bsuBean.up_end" readonly class=bk >
														<img name="shortDateimg"
									 						onClick="WdatePicker({el:document.getElementById('up_end'),dateFmt:'HH:mm',skin:'whyGreen'})"
									 							src="../../images/dateButton.png" width="15" height="12"
									 								border="0" alt="ѡ��">&nbsp;<font color="#FF0000">*</font>
												</td>
											</tr>
											<tr bgcolor="#FFFFFF">

												<td align="right" width="15%">
													����ʧЧʱ�䣺
												</td>
												<td align="left" width="30%">
													<input type="text" id="expire_time" name="bsuBean.expire_t" readonly class=bk >
														<img name="shortDateimg"
									 						onClick="WdatePicker({el:document.getElementById('expire_time'),dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
									 							src="../../images/dateButton.png" width="15" height="12"
									 								border="0" alt="ѡ��">&nbsp;<font color="#FF0000">*</font>
												</td>

												<td align="right" width="15%">
													�Ƿ�ִ��������
												</td>
												<TD align="left" width="30%"><select name="bsuBean.status" class="bk">
													<option value="1">==��==</option>
													<option value="0">==��==</option>
													</select>&nbsp;<font color="#FF0000">*</font></TD>
											</tr>
											<tr bgcolor="#FFFFFF">

												<td align="right" width="15%">
													�����������ƣ�
												</td>
												<td align="left" width="30%">
													<INPUT TYPE="text" NAME="bsuBean.task_name"
														maxlength=60 class=bk size=20>&nbsp;<font color="#FF0000">*</font>
												</td>
												<td align="right" width="15%">
													
												</td>
												<td align="left" width="30%">
													
												</td>
												
											</tr>
											<TR>
												<TD colspan="4" align="right" CLASS="green_foot">
													<INPUT TYPE="button" id="civiButton" onclick="javascript:CheckForm()" value=" �� �� " class=btn>
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</FORM>
						<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center" id="query_Form">
			<TR>
				<TD bgcolor=#999999 id="idData"><iframe id="dataForm"
					name="dataForm" height="0" frameborder="0" scrolling="no"
					width="100%" src=""></iframe></TD>
			</TR>
		</TABLE>
					</td>
				</tr>
			</table>
		</TD>
	</TR>
	<TR>
		
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
