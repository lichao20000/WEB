<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%
request.setCharacterEncoding("gbk");
String gwType = request.getParameter("gw_type");
%>
<%@ include file="../../head.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">

$(function(){
	gwShare_setGaoji();
});

function deviceResult(returnVal){
	$("tr[@id='trDeviceResult']").css("display","");
	$("td[@id='tdDeviceSn']").html("");
	$("td[@id='tdDeviceCityName']").html("");
	$("input[name='device_id']").val("");
	$("select[@name='interval']").val("-1");
	$("input[@name='count']").val("");
	
	$("input[@name='device_id']").val(returnVal[2][0][0]);
	$("td[@id='tdDeviceSn']").append(returnVal[2][0][1]+" -"+returnVal[2][0][2]);
	$("td[@id='tdDeviceCityName']").append(returnVal[2][0][5]);	
	$('#bt').attr("disabled",false);
}
function custom(){
	var _count = $("input[@name='count']");
	var _interval = $("select[@name='interval']");
	
	if("-1"==_interval.val()){
		alert("�ɼ����ʱ�䣬����Ϊ�գ�");
		_interval.focus();
		return false;
	}
	
	if(Trim(_count.val()) == ""){
		alert("�ɼ���������Ϊ��!");
		_count.focus();
		return false;
	}else{
		var reg =/^[1-9]d*$/;
		var count = _count.val();
	    if(count.match(reg)) {
	    	//��������ȷ
	    } else {
	    	 alert("�ɼ���������Ϊ����");
	    	 _count.focus();
	         return false;
	    }
	}
	
	$('#bt').attr("disabled",true);
	var device_id = $("input[name='device_id']").val();
	var interval = $("select[@name='interval']").val();
	var count = $("input[@name='count']").val();
	var url = "<s:url value='/itms/service/monitorFlowinfo!customTask.action'/>";
	$.post(url,{
		device_id:device_id,
		interval:interval,
		count:count
	},function(ajax){
		if("1"==ajax){
			alert("����������� �ɹ�!");
		}
		$('#bt').attr("disabled",false);
	});
}

</SCRIPT>
<%@ include file="../../toolbar.jsp"%>


<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="text">
				<TR>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									���Ʋɼ�����
								</td>
								<td nowrap>
									<img src="../../images/attention_2.gif" width="15" height="12">
									&nbsp;
								</td>
							</tr>
						</table>
					</td>
				</TR>

				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
					</td>
				</TR>

				<TR  bgcolor="#FFFFFF" id="trDeviceResult" style="display: none">
					<TD colspan="4">
							<TABLE id="tb1" border=0 cellspacing=1 cellpadding=2 width="100%" bgcolor=#999999>
								<TR bgcolor="#FFFFFF" >
									<td nowrap align="right" class=column width="15%">
										�豸����
									</td>
									<td id="tdDeviceCityName" width="35%">
									</td>
									<td nowrap align="right" class=column width="15%">
										�豸���к�
										<input type="hidden" name="device_id" value="">
									</td>
									<td id="tdDeviceSn" width="35%">
									</td>
								</TR>
								<TR bgcolor="#FFFFFF">
									<td nowrap align="right" class=column width="15%">
										�ɼ�ʱ����(����)
									</td>
									<td width="35%">
										<SELECT name="interval"  class="bk">
											<OPTION value="-1" selected="selected">--��ѡ��ɼ�ʱ����--</OPTION>
											<OPTION value="5">5</OPTION>
											<OPTION value="10">10</OPTION>
											<OPTION value="30">30</OPTION>
											<OPTION value="60">60</OPTION>
										</SELECT>
									</td>
									<td nowrap align="right" class=column width="15%">
										�ɼ�����
									</td>
									<td width="35%">
										<input type="text" name="count" id="count" value="" onmouseout="check()"/>
										&nbsp;&nbsp;&nbsp;&nbsp;
										<font color="red">*&nbsp;&nbsp;����Ϊ����</font>
									</td>
								</TR>
								
								<TR bgcolor="#FFFFFF">
									<td class="green_foot" colSpan="4" align="right" height="40">
										<button id="bt" onclick="custom()">&nbsp;<b>���Ƽ������</b> &nbsp;</button>
									</td>
								</TR>
							</TABLE>
					</TD>
				</TR>
				
				<TR bgcolor="#FFFFFF" id="tr002" style="display: none">
					<td valign="top" colspan="4" class=column>
					<div id="div_strategy" style="width: 100%; z-index: 1; top: 100px">
					</div>
					</td>
				</TR>
			</table>
		</TD>
	</TR>
</TABLE>

<%@ include file="../../foot.jsp"%>
