<%--
Author      : fanjiaming
Date		: 2017-6-14
Desc		: ����HTTP����ҵ���������(������ͨ)
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ include file="../head.jsp"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>
<%
	String gwType = request.getParameter("gw_type");
%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">

<SCRIPT LANGUAGE="JavaScript">
$(function(){
	$("input[@name='gwShare_queryResultType']").val("checkbox");
	gwShare_setGaoji();
	gwShare_setImport();
});
var deviceIds = "";
function CheckForm(){
	var task_name = $("input[@name='task_name']").val();
	var TOTAL_TIMES = $("input[@name='TOTAL_TIMES']").val();
	var PERIOD = $("input[@name='PERIOD']").val();
	var BEGIN_TIME = $("input[@name='BEGIN_TIME']").val();
	var END_TIME = $("input[@name='END_TIME']").val();
	if(task_name===""){
		alert("�������������ƣ�");
		return false;
	}

	if(TOTAL_TIMES===""){
		alert("�����������");
		return false;
	}
	
	if(PERIOD===""){
		alert("�����봥����С���ڣ�");
		return false;
	}
	
	if(BEGIN_TIME===""){
		alert("����������ʼʱ�䣡");
		return false;
	}

	if(END_TIME===""){
		alert("�������������ʱ�䣡");
		return false;
	}

	

	/* var url = "<s:url value='/gwms/resource/batchHttpTest!checkTime.action'/>";
	var ajaxre=0;
	$.ajaxSettings.async = false;
	$.post(url, {
		startDate : startDate,
		endDate:endDate
	}, function(ajax) {
		if(ajax>0){

			ajaxre=1;
		}
	});
	$.ajaxSettings.async = true;
	if(ajaxre===1){
		alert("��ѡʱ��δ��������ص���������ѡȡ��");
		return false;
	} */

	if($("input[@name='deviceIds']").val()==""){
		alert("��ѡ���豸��");
		return false;
	}

	return true;
}

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
									��������
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
						�����������ٲ���
					</TH>
				</TR>
				<tr>
					<td>
						<FORM NAME="frm" METHOD="post"
							ACTION="<s:url value="/gwms/resource/batchHttpTest!speedTestZJLT.action"/>"
							onsubmit="return CheckForm()">
							<input type="hidden" name="deviceIds" value="" />
							<input type="hidden" name="param" value="" />
							<input type="hidden" name="gw_type" value="1" />
							<input type="hidden" name="hblt_SpeedTest_flg" value="true" />
							<input type="hidden" name="hblt_BatchSpeedTest_flag" value="true" />
							<input type="hidden" name="fileName_st" value="" />
							<input type="hidden" name="gw_type" value="<%=gwType%>" />
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<tr bgcolor="#FFFFFF">
												<td colspan="4">
													<div id="selectedDev" >
														���ѯ�豸��
													</div>
												</td>
											</tr>
											<ms:inArea areaCode="hb_lt" notInMode="false">
											<tr bgcolor="#FFFFFF">
												<TD colspan="1" class="column" >
													��������ģʽ��
												</TD>
												<TD colspan="3" class="column" >
													<input type="radio" name="type" size="20" value="1" checked="checked" />���в���
													<input type="radio" name="type" size="20" value="2" />���в���
												</TD>
											</tr>
											</ms:inArea>
											<TR bgcolor="#FFFFFF" >
												<TD align="right" class=column width="15%">��������</TD>
												<TD colspan="3">
													<input type="text" name="task_name" value="" size="25" maxlength="50" class="bk" required="required"/>
													<font color="red">*</font>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" >
												<TD align="right" class=column width="15%">���Դ���</TD>
												<TD>
													<input type="number" name="TOTAL_TIMES" value="" size="25" maxlength="50" class="bk" required="required"/>
													<font color="red">*��λ���Σ�</font>
												</TD>
												<TD align="right" class=column width="15%">��С����</TD>
												<TD>
													<input type="number" name="PERIOD" value="" size="25" maxlength="50" class="bk" required="required"/>
													<font color="red">*��λ���죩</font>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" >
												<TD align="right" class=column width="15%">��ʼʱ��</TD>
												<TD width="35%">
													<input type="text"   name="BEGIN_TIME" readonly value="" class=bk required="required" >
													<img name="shortDateimg"
														 onClick="WdatePicker({el:document.frm.BEGIN_TIME,dateFmt:'HH:mm:ss',minDate:'00:00:00', skin:'whyGreen'})"
														 src="../../images/dateButton.png" width="15" height="12"
														 border="0" alt="ѡ��">
													<font color="red">*</font>
												</TD>
												<TD align="right" class=column width="15%" >����ʱ��</TD>
												<TD width="35%" >
													<input type="text" name="END_TIME" readonly value="" class=bk required="required">
													<img name="shortDateimg"
														 onClick="WdatePicker({el:document.frm.END_TIME,dateFmt:'HH:mm:ss',minDate:'00:00:00',skin:'whyGreen'})"
														 src="../../images/dateButton.png" width="15" height="12"
														 border="0" alt="ѡ��">
													<font color="red">*</font>
												</TD>
											</TR>
											<!-- <tr bgcolor="#FFFFFF" id="softUp" >
												<TD colspan="1" class="column"  >
													����url��
												</TD>
												<TD colspan="1" class="column">
													<input type="text" name="http_url" class="bk" value="http://121.28.169.98:8084/services/contractrate/contract/rate/"/>
												</TD>
												<TD colspan="1" class="column" >
													�ϱ�url��
												</TD>
												<TD colspan="1" class="column" >
													<input type="text" name="report_url" class="bk" value="http://121.28.169.98:8084/services/contractrate/results/insert/"/>
												</TD>
											</tr> -->
											<TR>
												<TD colspan="4" align="right" CLASS="green_foot">
													<INPUT TYPE="submit" value=" ִ �� " class=btn>
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
