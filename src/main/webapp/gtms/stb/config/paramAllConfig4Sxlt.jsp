<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="../../../css/liulu.css" type="text/css">
<link rel="stylesheet" href="../../../css/css_green.css" type="text/css">
<link rel="stylesheet" href="../../../css/tab.css" type="text/css">
<link rel="stylesheet" href="../../../css/listview.css" type="text/css">
<link rel="stylesheet" href="../../../css/css_ico.css" type="text/css">
<style type="text/css">
.mybutton {
	height: 18px;
	background-color: #D6E1F7;
	font-size: 12px;
	vertical-align: middle;
	padding-top: 2px;
	color: #000000;
	border-width: 1px;
	border-color: #7f9db9;
	text-align: absmiddle;
	text-decoration: blink;
	cursor: hand;
	margin-left: 3px;
	margin-right: 3px;
	FILTER: progid:DXImageTransform.Microsoft.Gradient(GradientType=0,
		StartColorStr=#889CD7, EndColorStr=#FFFFFF );
	line-height: normal;
}
</style>
<%@ include file="../../../toolbar.jsp"%>
<%
	String gwType = request.getParameter("gw_type");

    //������Ϊ0��ʾ��ȡ�豸ʵ����ֻ�ܻ�ȡ������������
    String authType = request.getParameter("type");

	//read_flag �ж��Ƿ�Ϊ��ѯ���߲����˵�
	//1�����ѯ�˵��� 2��������˵�
	String read_flag = request.getParameter("read_flag");
%>
<SCRIPT LANGUAGE="JavaScript" SRC="../../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
	$(function() {
		gwShare_setGaoji();
	});

	function CheckForm() {
		var __device_id = $("input[@name='device_id']").val();
		if (__device_id == null || __device_id == "") {
			alert("���Ȳ�ѯ�豸!");
			return false;
		}
		return true;
	}

	function deviceResult(returnVal) {

		$("tr[@id='trDeviceResult']").css("display", "");

		$("td[@id='tdDeviceSn']").html("");
		$("td[@id='tdDeviceCityName']").html("");

		$("input[@name='device_id']").val(returnVal[2][0][0]);
		$("td[@id='tdDeviceSn']").append(
				returnVal[2][0][1] + "-" + returnVal[2][0][2]);
		$("td[@id='tdDeviceCityName']").append(returnVal[2][0][5]);

	}
</SCRIPT>
<TABLE width="98%" align="center" class="querytable">
	<tr>
		<td><IMG height=20 src="<s:url value='/images/bite_2.gif'/>"
			width=24> ����ǰ��λ�ã�����ʵ������</td>
	</tr>
</TABLE>
<table width="98%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td><%@ include file="../share/gwShareDeviceQuery.jsp"%>
		</td>
	</tr>
	<tr id="trDeviceResult" style="display: none;">
		<td bgcolor="#FFFFFF">
			<table width="100%" class="querytable">
				<FORM NAME="frm" METHOD="post" ACTION="getConfig_Allparam4Sxlt.jsp"
					onsubmit="return CheckForm()">
					<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
						align="center">
						<tr>
							<td bgcolor=#999999>
								<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
									<tr bgcolor="#FFFFFF">
										<td nowrap align="right" class=column width="15%">�豸���� <input
											type="hidden" name="gw_type" value="<%=gwType%>" /> <input
											type="hidden" name="read_flag" value="<%=read_flag%>" />
											<input type="hidden" value="<%=authType%>" name="authType"/>
										</td>
										<td id="tdDeviceCityName" width="35%"></td>
										<td nowrap align="right" class=column width="15%">�豸���к� <input
											type="hidden" name="device_id" value="">
										</td>
										<td id="tdDeviceSn" width="35%"></td>
									</tr>
									<tr bgcolor="#FFFFFF">
										<td colspan="4" align="right" class="green_foot"><INPUT
											TYPE="submit" value=" �� ȡ " class=mybutton></td>
									</tr>
								</TABLE>
							</td>
						</tr>
					</TABLE>
				</FORM>
			</table>
		</td>
	</tr>
	<tr>
		<td HEIGHT=20>&nbsp; <IFRAME ID=childFrm SRC=""
				STYLE="display: none"></IFRAME> <IFRAME ID=childFrm1 SRC=""
				STYLE="display: none"></IFRAME> <IFRAME ID=childFrm2 SRC=""
				STYLE="display: none"></IFRAME>
		</td>
	</tr>
</table>