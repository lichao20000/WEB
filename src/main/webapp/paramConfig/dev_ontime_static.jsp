<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil"%>

<jsp:include page="../share/selectDeviceJs.jsp">
        <jsp:param name="selectType" value="checkbox"/>
        <jsp:param name="jsFunctionName" value=""/>
</jsp:include>

<%
request.setCharacterEncoding("GBK");

DateTimeUtil dt=new DateTimeUtil();
//String startime=dt.getDate()+" 00:00:00";
String endtime=dt.getDate();

%>
<script type="text/javascript" src="../Js/jquery.js"></script>
<script type="text/javascript" src="../Js/jQeuryExtend-linkage.js"></script>
<script type="text/javascript" src="../Js/jsDate/WdatePicker.js"></script>

<SCRIPT LANGUAGE="JavaScript">
var diviceIds = "";
function CheckForm(){
	
//	if(document.frm.starttime.value == ""){
//		alert("��ѡ��ʼʱ��!");
//		document.frm.startimg.focus();
//		return false;
//	}else if(document.frm.endtime.value==""){

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
			  break;
			}
		}

 	}
 	if(num ==0){
		alert("��ѡ���豸��");
		return false;
	}
	if(document.frm.endtime.value==""){
		alert("��ѡ�����ʱ��!");
		document.frm.endimg.focus();
		return false;
	}
	return true;
}

function getDeviceIds() {
	diviceIds = "";
	var oselect = document.all("device_id");
	if(typeof(oselect.length)=="undefined"){
		if(oselect.checked){
			diviceIds = oselect.value;
		}
 	}else{
		for(var i=0;i<oselect.length;i++){
			if(oselect[i].checked){
				diviceIds += oselect[i].value + ",";
			}
		}
		diviceIds += 0;
 	}
	return diviceIds;
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
			<FORM NAME="frm">
				<input type="hidden" name="strDevice" value="">
				<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									�����ն�ʹ��ͳ��
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									�豸����ʱ��ͳ�ơ�
								</td>
							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR>
												<TH colspan="4" align="center">
													�豸����ʱ��ͳ��
												</TH>
											</TR>
											<TR bgcolor="#FFFFFF">
											    <TD colspan="4">
													<div id="selectDevice">
										                <span>������....</span>
										        	</div>
												</TD>
											</TR>
											
											<TR bgcolor="#FFFFFF" id="ntp_type" style="display:">
											    <TD align="right" width="15%">
													ѡ��ʱ��:
												</TD>
												<TD colspan=3 width="85%">
													<input type="text" name="endtime" value="<%=endtime%>" readonly class=bk>
													<img name="endimg" onclick="new WdatePicker(document.frm.endtime,'%Y-%M-%D',false,'whyGreen')" src="../images/search.gif" width="15" height="12" border="0" alt="ѡ��">(YYYY-MM-DD)
													<label style="color:  #FF0000">*</label>
												</TD>
												
											</TR>
											<tr bgcolor="#FFFFFF" style="display:;">
												<td align="right" width="15%">ѡ�񱨱�����:</td>
												<td colspan=3 width="85%">
													<select name="sel_type" onchange="chgDesc()">
														<option value="1">�ձ���</option>
														<option value="2">�ܱ���</option>
														<option value="3">�±���</option>
													</select>&nbsp;&nbsp;
												</td>
											</tr>
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">
													<INPUT TYPE="button" id="bt_set" onclick="ExecMod();" value=" �� ѯ " class=btn >
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr001" style="display:none">
												<TH colspan="4">��ѯ���</TH>
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
			<IFRAME ID=childFrm SRC="" STYLE="display:none;"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display:none;"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display:none;"></IFRAME>
		</TD>
	</TR>
</TABLE>

<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	 function chgDesc(type) {
	 	switch(type){
			case "1"://�ձ���
				document.all("dateDesc").innerHTML=" ����ǰ��ѡ���ڣ�";
				break;
			case "2"://�ܱ���
				document.all("dateDesc").innerHTML=" ����ǰ�������ڵ��ܣ�";
				break;
			case "3"://�±���
				document.all("dateDesc").innerHTML=" ����ǰ�������ڵ��£�";
				break;
		}
	 }
     function ExecMod(){
     		if(CheckForm()){
				//ChangeTime($("select[@name='sel_type']").val());
			
		        page = "dev_ontime_static_data.jsp?device_id=" + getDeviceIds()
		        	+ "&starttime=" + $("input[@name='endtime']").val() //document.frm.starttime.value
		        	+ "&type=" + document.frm.sel_type.value
		        	//+ "&endtime=" + getCaleToSec(document.frm.endtime.value + " 23:59:59")
		        	+ "&refresh=" + new Date().getTime();
		        //alert(page);
				document.all("div_ping").innerHTML = "���ڲɼ�����ʱ���������ĵȴ�....";
				document.getElementById("tr001").style.display = "";
				document.getElementById("tr002").style.display = "";
				document.all("childFrm").src = page;
			}else{
				return false;
			}	
    }
	//ת��ʱ��
	function ChangeTime(type){
		switch(type){
			case "1"://�ձ���
				$("input[@name='starttime']").val($.getTime($("input[@name='endtime']").val(),"day","-",1,false));
				break;
			case "2"://�ܱ���
				$("input[@name='starttime']").val($.getTime($("input[@name='endtime']").val(),"week","-",1,false));
				break;
			case "3"://�±���
				$("input[@name='starttime']").val($.getTime($("input[@name='endtime']").val(),"month","-",1,false));
				break;
		}
	}
	//��ʱ��ת��Ϊ��
	function getCaleToSec(time){
		time=time.replace("-","/").replace("-","/");
		var t=new Date(time);
		return t.getTime()/1000;
	}

//-->
</SCRIPT>