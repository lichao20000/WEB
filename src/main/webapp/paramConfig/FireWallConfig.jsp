<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<%
request.setCharacterEncoding("GBK");
%>
<jsp:include page="/share/selectDeviceJs.jsp">
	<jsp:param name="selectType" value="radio"/>
	<jsp:param name="jsFunctionName" value=""/>
</jsp:include>
<SCRIPT LANGUAGE="JavaScript">

var did = "";

function CheckForm(){
	if(!deviceSelectedCheck()){
		alert("��ѡ���豸��");
		return false;
	}

	var oSelect = document.all("device_id");
	
	if(oSelect !=null ) {
		for(var i=0; i<oSelect.length; i++) {
			if(oSelect[i].checked) {
				did = oSelect[i].value;
			}
		}
	}
	if(oSelect.checked){
		did = oSelect.value;
	}
	return true;
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
			<FORM NAME="frm" METHOD="post" ACTION="paramAllList.jsp" onsubmit="return CheckForm()" target = "childFrm">
				<input type="hidden" name="strDevice" value="">
				<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									����ʵ������
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									�����ն˵�ʱ���������
								</td>
							</tr>
						</table>
						</td>
					</tr>
					<TR>
						<TH colspan="4" align="center">
							�豸��ѯ
						</TH>
					</TR>
					<TR bgcolor="#FFFFFF" >
						<td colspan="4">
							<div id="selectDevice"><span>������....</span></div>
						</td>
					</TR>
					<TR>
						<TH colspan="4" align="center">
							FireWall ����
						</TH>
					</TR>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											
											<TR bgcolor="#FFFFFF">
												<td align="right">����ǽ</td>
												<td><select name="Enable"><option value="1">����</option><option value="0">����</option></select></td>
												<td align="right">��ֹDDOS����:</td>
												<td><select name="DdosEnabled"><option value="1">����</option><option value="0">����</option></select></td>
											</TR>

											<TR bgcolor="#FFFFFF">
												<td align="right">��ֹ�˿�ɨ�蹦��</td>
												<td><select name="PortscanEnabled"><option value="1">����</option><option value="0">����</option></select></td>
												<td align="right">�����ܿص�Ӧ������</td>
												<td><input type="text" name="AppControlName"></td>
											</TR>

											
											<TR bgcolor="#FFFFFF">
												<td align="right">WeekDays</td>
												<td>
													<select name="WeekDays">
														<option value="">��</option>
														<option value="all">all</option>
														<option value="1">1</option>
														<option value="2">2</option>
														<option value="3">3</option>
														<option value="4">4</option>
														<option value="5">5</option>
														<option value="6">6</option>
														<option value="7">7</option>
													</select>
												</td>
												<td align="right">����Ӧ�ó����Ƿ�����</td>
												<td><select name="AppControlEnabled"><option value="1">����</option><option value="0">����</option></select></td>
											</TR>

											<TR bgcolor="#FFFFFF">
												<td align="right" width="15%">SourceInterface</td>
												<td width="35%">
													<select name="SourceInterface">
														<option value="">��</option>
														<option value="ANY">ANY</option>
														<option value="VLAN1">VLAN1</option>
														<option value="VLAN2">VLAN2</option>
														<option value="VLAN3">VLAN3</option>
														<option value="VLAN4">VLAN4</option>
														<option value="VLAN5">VLAN5</option>
														<option value="VLAN6">VLAN6</option>
													</select>
												</td >
												<td align="right" width="15%">TimeRange</td>
												<td width="35%"><input type="text" name="TimeRange"></td>
											</TR>

											<TR bgcolor="#FFFFFF">
												<td align="right">���Կ��Ƶ�Ӧ���б�</td>
												<td colspan=3><input type="text" name="AppControlSupportList"></td>
											</TR>


											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">
													<INPUT TYPE="button" id="bt_set" onclick="ExecMod();" value=" �� �� " class=btn >
													<INPUT TYPE="button" id="bt_get" onclick="getStatus();" value=" �� ȡ " class=btn >
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr001" style="display:none">
												<TH colspan="4">�������</TH>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr002" style="display:none">
												<td colspan="4" valign="top" class=column>
												<div id="div_ping"
													style="width: 100%; height: 120px; z-index: 1; top: 100px;"></div>
												</td>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</td>
					</tr>
				</table>
			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm name="childFrm" SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	function getStatus() {
		if(CheckForm()){
			var page = "FireWallStatus.jsp?device_id=" +did + "&refresh=" + new Date().getTime();
			document.all("div_ping").innerHTML = "���ڻ�ȡ�豸FireWall��Ϣ�������ĵȴ�....";
			document.getElementById("tr001").style.display = "";
			document.getElementById("tr002").style.display = "";
			document.all("childFrm").src = page;
		}else{
			return false;
		}
	}

     function ExecMod(){
		if(CheckForm()){
			document.getElementById("tr001").style.display = "";
			document.getElementById("tr002").style.display = "";
			document.all("div_ping").innerHTML = "���������豸FireWall��Ϣ�������ĵȴ�....";
			var page = "FireWallSave.jsp?device_id=" +did  + "&type=1" + "&refresh=" + new Date().getTime();
			document.all("frm").action = page;
			document.all("frm").submit();
			document.all("frm").target = "childFrm";			
			//document.all("childFrm").src = page;
		}else{
			return false;
		}	
    }
//-->
</SCRIPT>