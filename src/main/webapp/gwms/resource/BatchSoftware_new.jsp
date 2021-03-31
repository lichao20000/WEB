<%--
Author      : 王森博
Date		: 2010-4-15
Desc		: 手动的批量软件升级
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<%
 String gwType = request.getParameter("gw_type");
 %>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	$("input[@name='gwShare_queryResultType']").val("checkbox");
	// gwShare_setGaoji();
	gwShare_setImport();
	$("input[@name='gwShare_queryType']").val(3);

});
var deviceIds = "";
function CheckForm(){
	if($("input[@name='deviceIds']").val()==""){
		alert("请选择设备！");
		return false;
	}

	var taskid = $('input[@name="taskid"]').val();

	if(isNaN(taskid)){
		alert('任务号必须一个数字，请重新输入。');
		return false;
	}

	// 校验任务号状态，看是否已经执行。
	$.ajax({
        url:'<s:url value="/gwms/resource/softwareNew!checkTaskId.action"/>',
        type:'POST',
        data:'taskid='+taskid,
        async:false,
        success:function (data) {
           	if(data == 1){
           		alert('该任务号已执行，不能再次添加。请更换任务号。');
           		$('#task_status').val('1');
           		return false;
           	}
           	else{
           		$('#task_status').val('0');
           	}
        },
        error:function(){
        	alert('error');
        	return false;
        }
    });

	if($('#task_status').val()=='1'){
		return false;
	}
	return true;
}

function deviceResult(returnVal){
	deviceIds="";
	if(returnVal[0]==0){
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[2].length+"</strong></font>");
		for(var i=0;i<returnVal[2].length;i++){
			deviceIds = deviceIds + returnVal[2][i][0]+",";
		}
		$("input[@name='deviceIds']").val(deviceIds);
	}else{
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[0]+"</strong></font>");
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
									批量软件升级
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
						<!-- 上面包含的一个表单 -->
						<%@ include file="/gwms/share/gwShareDeviceQuery_new.jsp"%>
					</td>
				</TR>
				<TR>
					<TH colspan="4" align="center">
						配置升级策略
					</TH>
				</TR>
				<tr>
					<td>
						<FORM NAME="frm" METHOD="post"
							ACTION="<s:url value="/gwms/resource/softwareNew!batchUp.action"/>"
							onsubmit="return CheckForm()">
							<input type="hidden" name="deviceIds" value="" />
							<input type="hidden" name="param" value="" />
							<input type="hidden" name="gw_type" value="<%=gwType%>" />
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<tr bgcolor="#FFFFFF">
												<td colspan="4">
													<div id="selectedDev">
														请查询设备！
													</div>
												</td>
											</tr>
											<tr bgcolor="#FFFFFF" id="softUp">

												<TD align="right" width="15%">
													软件升级策略方式：
												</TD>
												<TD width="30%">
													<s:property value="softStrategyHTML" escapeHtml="false" />
												</TD>

												<td align="right" width="15%">
													任务号：
												</td>
												<td align="left" width="30%">
													<input type="text" name="taskid"/>
													&nbsp; 任务号输入格式：日期+序号，例如2015010101
												</TD>
												</td>
											</tr>
											<TR>
												<TD colspan="4" align="right" CLASS="green_foot">
													<INPUT TYPE="submit" value=" 执 行 " class=btn>
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

<input type="hidden" id="task_status">

<%@ include file="../foot.jsp"%>
