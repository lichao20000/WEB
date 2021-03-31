<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.resource.*" %>
<jsp:useBean id="sheetManage" scope="request" class="com.linkage.litms.netcutover.SheetManage" />
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%
	request.setCharacterEncoding("GBK");
	String serviceList = sheetManage.getServiceList("");
	DeviceAct deviceAct = new DeviceAct();
	String gatherList = deviceAct.getGatherList(session, "", "", true);

%>
<SCRIPT LANGUAGE="JavaScript">
	function selectAll(elmID){
		t_obj = document.all(elmID);
		if(!t_obj) return;
		obj = event.srcElement;
		if(obj.checked){
			if(typeof(t_obj) == "object" ) {
				if(typeof(t_obj.length) != "undefined") {
					for(var i=0; i<t_obj.length; i++){
						t_obj[i].checked = true;
					}
				} else {
					t_obj.checked = true;
				}
			}
		
		}else{
			if(typeof(t_obj) == "object" ) {
				if(typeof(t_obj.length) != "undefined") {
					for(var i=0; i<t_obj.length; i++){
						t_obj[i].checked = false;
					}
				} else {
					t_obj.checked = false;
				}
			}
		}
	}
	function showChild(param){
		var page="";
		if(param == "service_id"){
			page = "getServiceCode.jsp?service_id=" + document.frm.service_id.value;
			document.all("div_service").innerHTML = "������������....";			
			document.all("childFrm").src = page;
		}else if(param == "servicecode"){
			document.all("div_gather").innerHTML = "<%=gatherList%>";		
		}

		if(param == "gather_id"){
			page = "getDeviceList.jsp?servicecode=" + document.frm.servicecode.value+ "&gather_id=" +document.frm.gather_id.value;
			document.all("div_device").innerHTML = "������������....";
			document.all("childFrm").src = page;	
		}
	}
	function CheckForm(){
		if(document.frm.service_id.value== -1){
			alert("��ѡ��ҵ��");
			document.frm.service_id.focus();
			return false;
		}               
		if(document.frm.servicecode.value== -1){
			alert("��ѡ��ҵ����룡");
			document.frm.servicecode.focus();
			return false;
		}
		if(document.frm.gather_id.value== -1){
			alert("��ѡ��ɼ��㣡");
			document.frm.gather_id.focus();
			return false;
		}
		
		var oselect = document.all("device_id");
		if(oselect == null){
			alert("��ѡ���豸��");
			return false;
		}
		var num = 0;
		if(typeof(oselect.length)=="undefined"){
			if(oselect.checked){
				num = 1;
			}
		}else{
			for(var i=0;i<oselect.length;i++){
				if(oselect[i].checked){
					num++;
				}
			}

		}
		if(num ==0){
			alert("��ѡ���豸��");
			return false;
		}
	
			
	}
</SCRIPT>

<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
	<TR>
		<TD valign=top>
			<FORM NAME="frm" METHOD="post" ACTION="sendSheet.jsp" onsubmit="return CheckForm()">
			<br>
			<br>
				<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" class="title_bigwhite" align="center">
										��������
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">
										��ѡ����豸�·�ָ��ҵ��Ĺ�����
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=s2 width="100%">
								<TR>
									<TH colspan="4" align="center">
										�����·�
									</TH>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right" width="20%" nowrap>
										ҵ��
									</TD>

									<TD class=column align="" width="30%" nowrap>
										<%=serviceList%>
										
									</TD>
									<TD class=column align="right" width="20%" nowrap>
										ҵ�����
									</TD>
									<TD class=column width="30%" nowrap>
										<div id="div_service">
											<select name="" class="bk">
												<option value="-1">
													--����ѡ��ҵ��--
												</option>
											</select>
										</div>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right" nowrap>
										�ɼ���
									</TD>
									<TD class=column align="left" colspan="" nowrap>
										<div id="div_gather">
											<select name="gather_id" class="bk">
												<option value="-1">
													--����ѡ��ҵ�����--
												</option>
											</select>
										</div>

									</TD>
									<TD class=column align="right" width="20%" nowrap>
										ִ�з�ʽ
									</TD>
									<TD class=column width="30%" nowrap>
										<input type="radio" name="execu_type" value="0" checked>
										����ִ��
										<input type="radio" name="execu_type" value="1">
										�ƻ�ִ��
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD align="right" nowrap>
										�豸�б�
										<br>
										<INPUT TYPE="checkbox" onclick="selectAll('device_id')" name="device">
										ȫѡ
									</TD>
									<td colspan="3" valign="top">
										<div id="div_device" style="width:98%; height:100px; z-index:1; top: 100px; overflow:scroll">
											<span>����ѡ��ɼ��㣡</span>
										</div>
									</td>
								</TR>
								<TR height="23">
									<TD colspan="4" align="right" class=foot>
										<INPUT TYPE="submit" value=" ���͹��� " class=btn>
										&nbsp;&nbsp;
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
				</TABLE>
			</FORM>
		</TD>
	</TR>
	<IFRAME width="800" name="childFrm" SRC="" style="display:none"></IFRAME>
</TABLE>
<%@ include file="../foot.jsp"%>
