<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
function CheckForm(){
	if(!deviceSelectedCheck()){
		alert("��ѡ���豸��");
		return false;
	}
}
function deviceResult(returnVal){
		
	$("tr[@id='trDeviceResult']").css("display","");

	$("td[@id='tdDeviceSn']").html("");
	$("td[@id='tdDeviceCityName']").html("");
	$("tr[@id='jg']").show();
	for(var i=0;i<returnVal[2].length;i++){
		$("input[@name='textDeviceId']").val(returnVal[2][i][0]);
		$("td[@id='tdDeviceSn']").append(returnVal[2][i][1]+" -"+returnVal[2][i][2]);
		$("td[@id='tdDeviceCityName']").append(returnVal[2][i][5]);		
	}
}
</SCRIPT>

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
									�豸���
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									�ն�����ҵ��͸��ģʽ
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

			</table>
		</TD>
	</TR>
	<br>
	<tr style='display: none' id="jg">
		<td>
			<FORM NAME="frm" METHOD="post" ACTION="">
				<TABLE width="98%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<TR bgcolor="#FFFFFF" id="trDeviceResult" style="display: none">
									<td nowrap align="right" class=column width="15%">
										�豸����
									</td>
									<td id="tdDeviceCityName" width="35%">
									</td>
									<td nowrap align="right" class=column width="15%">
										�豸���к�
										<input type="hidden" name="textDeviceId" value="">
									</td>
									<td id="tdDeviceSn" width="35%">
									</td>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right" width="10%">
										ģʽ�޸�
									</TD>
									<TD colspan="4">
										<select id="selectMode" name="selectMode">
											<option value="0">==��ѡ��==</option>
											<option value="1">==����==</option>
											<option value="2">==POS��==</option>
										</select>
									</TD>
								</TR>

								<TR>
									<TD colspan="4" align="right" class=foot>
										<INPUT TYPE="button" onclick="changeModel()" name="changemodel"
											value="ִ ��" class=btn>
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
					<TR>
						<TD>
							<DIV id="tableView" style="overflow: auto; display: ;"
								align="center"></DIV>
						</TD>
					</TR>
				</TABLE>
			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm3 SRC="" STYLE="display: none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<script language="javascript">
function changeModel() {
		if("0"==document.frm.selectMode.value){
			alert("��ѡ���޸�ģʽ")
			return false;
		}
		document.all.tableView.display = "";
		document.all.tableView.innerHTML = "<BR><BR>�����޸�,���Ժ�...";
		var url = "<s:url value='/ids/TerminalVoipModelACT!changeModel.action'/>";
		var textDeviceId = $("input[@name='textDeviceId']").val();
		var tdDeviceSn = $("#tdDeviceSn").html();
		$.post(url, {
			textDeviceId : textDeviceId,
			selectMode : document.frm.selectMode.value
		}, function(ajax) {
			$("div[@id='tableView']").html("");
			$("div[@id='tableView']").append(ajax);
		});
}
</script>
<%@ include file="../foot.jsp"%>