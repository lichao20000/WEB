<%--
Author      : ���ı�
Date		: 2013-6-5
Desc		: �����������
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<%
 String gwType = request.getParameter("gw_type");
 boolean flag = false;
 if(LipossGlobals.inArea("gs_dx") && "true".equals(request.getParameter("online_time"))){
	 flag = true;
 }
 %>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<script type="text/javascript"
			src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript">

/**
*���߼���ѯ�������ѯ�����������ѯ��ʾ����
**/
$(function(){
	$("input[@name='gwShare_queryResultType']").val("checkbox");
	gwShare_setGaoji();
	gwShare_setImport();
});
var deviceIds = "";
/**
* ��֤���ύ
**/
function CheckForm(){
	var filename = $("input[@name='fileName']").val();
	var starttime =$.trim($("input[@name='starttime']").val());
	if (null == filename || filename == "")
	{
		if($("input[@name='deviceIds']").val()==""){
		alert("��ѡ���豸��");
		return false;
		}
	}
	
	if($("select[@name='goal_devicetype_id']").val()=="-1")
	{
		alert("��ѡ��Ŀ��汾��");
		return  false;
	}
	if(null == starttime || ""==starttime){
		alert("��ѡ����ʼʱ��");
		return  false;
	}
	var qryType = $("input[@name='gwShare_queryType']").val();
	if(<%=flag%> && qryType == 2){
		var time = $("input[name='online_time']").val();
		$("input[name='reboot_time']").val(time);
	}
	document.frm.action='<s:url value="/gwms/resource/batchSoftRestartACT!importTask.action"/>';
	document.frm.submit();	
}

/**
* ��ѯ���豸
**/
function deviceResult(returnVal){	
	deviceIds="";
	if(returnVal[0]==0){
		$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+returnVal[2].length+"</strong></font>");
		for(var i=0;i<returnVal[2].length;i++){
			deviceIds = deviceIds + returnVal[2][i][0]+",";			
		}
		$("input[@name='deviceIds']").val(deviceIds);
	}else{
		$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+returnVal[0]+"</strong></font>");
		$("input[@name='deviceIds']").val("0");
		$("input[@name='param']").val(returnVal[1]);
	}
	
}
/**
*��ѯ���豸
**/
function deviceUpResult(returnVal){	
		$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+returnVal[0]+"</strong></font>");
		var gw_type = $("input[@name='gw_type']").val();
		$("input[@name='fileName']").val(returnVal[1]);
		$("input[@name='gwShare_queryType']").val(returnVal[2]);
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
								<%if(flag){ %>
									��ͥ���������������
									<%}else{ %>
									�����������
									<%} %>
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
						<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
					</td>
				</TR>
				<TR>
					<TH colspan="4" align="center">
					<%if(flag){ %>
					������������
				 <%}else{ %>
					������������
					<%} %>				
					</TH>
				</TR>
				<tr>
					<td>
						<FORM NAME="frm" METHOD="post">
							<input type="hidden" name="deviceIds" value="" />
							<input type="hidden" name="fileName" value="" />
							<input type="hidden" name="param" value="" />
							<input type="hidden" name="gw_type" value="<%=gwType%>" />
							<input type="hidden" name="gwShare_queryType" value="" />
							<input type="hidden" name="reboot_time" value=""> 
							
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
											<tr bgcolor="#FFFFFF" id="softUp">

												<TD align="right" width="15%">
													��ʼʱ�䣺
												</TD>
												<td>
													<input type="text" name="starttime" class='bk' readonly
														value="<s:property value='starttime'/>">
													<img name="shortDateimg"
															onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
															src="../../images/dateButton.png" width="15" height="12"
															border="0" alt="ѡ��" />
												</td>
												<td align="right" width="15%">
													����:
												</td>
												<td align="left" width="30%">
													<input type="text" id="task_desc" name="task_desc">
												</td>
											</tr>
											<TR>
												<TD colspan="4" align="right" CLASS="green_foot">
													<INPUT TYPE="button" value=" ִ �� " class=btn" onclick="CheckForm()">
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</FORM>
					</td>
				</tr>
			</table>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
