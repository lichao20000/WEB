<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@ page import="com.linkage.litms.resource.FileSevice"%>
<jsp:useBean id="deviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />
<%
request.setCharacterEncoding("GBK");

%>
<style type='text/css'>
<!--
BODY.bd {
	BACKGROUND-COLOR: #ffffff;
	COLOR: #000000;
	FONT-FAMILY: '����', 'Arial';
	FONT-SIZE: 12px;
	MARGIN: 0px
}
TH.thh {
	background-color: #B0E0E6;
	font-size: 9pt;
	color: #000000;
	text-decoration: none;
	font-weight: bold;
	line-height: 22px;
	text-align: center;
}
TR.trr {
	background-color:'#FFFFFF';
}
TD.tdd {
	FONT-FAMILY: '����', 'Tahoma';
	FONT-SIZE: 12px;
	background-color:'#FFFFFF';
}
TD.hd {
	background-color:'#EEE8AA';
}
-->
</style>
<SCRIPT LANGUAGE="JavaScript">
<!--
//-->
</SCRIPT>
<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center" >
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="running_report_build_query_list.jsp" onsubmit="return CheckForm()">
			<input type="hidden" name="query_type" value="1"/>
				<table width="98%" border="0" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									���б����ѯ
								</td>
								<td nowrap>
									<input type="radio" value="1" onclick="ChangeQueryType(this.value)" id="qt1" name="queryType" checked><label for="qt1">���豸</label>&nbsp;&nbsp;
			                        <input type="radio" value="2" onclick="ChangeQueryType(this.value)" id="qt2" name="queryType"><label for="qt2">���ͻ�</label>&nbsp;&nbsp;
			                        <input type="radio" value="3" onclick="ChangeQueryType(this.value)" id="qt3" name="queryType"><label for="qt3">���û�</label>
								</td>
							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR>
												<TH colspan="4" align="center">
													�豸��ѯ
												</TH>
											</TR>
											<TR bgcolor="#FFFFFF" id="query_type_dev">
												<TD align="right" width="100" class='column'>
													�豸���к�
												</TD>
												<TD  colspan="3" align="left">
													<input type="text" name="device_serialnumber"/>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" style="display:none" id="query_type_cust">
												<TD align="right" width="100" class='column'>
													�ͻ���
												</TD>
												<TD  colspan="3" align="left">
													<input type="text" name="customer_name"/>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" style="display:none" id="query_type_user">
												<TD align="right" width="100" class='column'>
													�û���
												</TD>
												<TD  colspan="3" align="left">
													<input type="text" name="user_name"/>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
											    <TD align="right" class='column'>
													����״̬
												</TD>
												<TD align="left">
													<select style="width:120" name="is_send">
														<option value="-1" selected>(����)
														<option value="0">δ����
														<option value="1">�ѷ���
														<option value="2">δ����(δ��������)
														<option value="3">�ʼ�����ʧ��
													</select>
												</TD>
											    <TD align="right" class='column'>
													��������
												</TD>
												<TD align="left">
													<select style="width:120" name="report_type">
														<option value="-1" selected>(����)
														<!--<option value="0">ʵʱ����
														<option value="1">�ձ���
														<option value="2">�ܱ���
														<option value="4">�걨��-->
														<option value="3">�±���
														<option value="5">�ֶ�����
													</select>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
											    <TD align="right" class='column'>
													��ʼʱ��
												</TD>
												<TD align="left">
													<INPUT type="text" name="start" size="20" >
													<input type="button" value="��" class=jianbian onClick="showCalendar('all')">
													<INPUT TYPE="hidden" name="startTime">
												</TD>
												<TD align="right" class='column'>
													����ʱ��
												</TD>
												<TD align="left">
        											<input type="text" name="end" size="20" >
													<input type="button" value="��" class=jianbian onClick="showCalendar('all')">
													<INPUT TYPE="hidden" name="endTime">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr_filelist" STYLE="display:none">
											    <TD align="right">
													δ���ͱ����ļ��б�
												</TD>
												<TD>
													<span id="id_filelist"></span>
												</TD>
												<TD colspan="2" align="left">
													<input type="button" class=btn value="�鿴�����ļ�����" onclick="viewFile()">
													<input type="button" class=btn value="���ͱ���" onclick="sendFile()">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr_filecontext" STYLE="display:none">
												<TD colspan="3">
													<span id="id_filecontext"></span>
												</TD>
												<TD align="left">
													ר�����<TEXTAREA name="suggestion" cols="15" rows="4"></TEXTAREA>
													<input type="button" class="btn" value="����" onclick="saveSuggestion()">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">
													<INPUT TYPE="submit" value=" �� ѯ " class="btn">
													<%--<INPUT TYPE="button" onclick="getReportList()" value="���ұ���" class="btn">--%>
												</TD>
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
			<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
var queryType = 1;
function ChangeQueryType(value) {
	var tr1 = "query_type_dev";
	var tr2 = "query_type_cust";
	var tr3 = "query_type_user";
	var query_type = document.forms[0].query_type;
	if (1 == value) {
		queryType = 1;
		query_type.value = 1;
		showId(tr1);
		hideId(tr2);
		hideId(tr3);
	} else if (2 == value) {
		queryType = 2;
		query_type.value = 2;
		showId(tr2);
		hideId(tr1);
		hideId(tr3);
	} else {
		queryType = 3;
		query_type.value = 3;
		showId(tr3);
		hideId(tr1);
		hideId(tr2);
	}
}
function Init(){
	var dt = new Date();
	var date = dt.getYear()+"-"+(dt.getMonth()+1)+"-"+dt.getDate();
	document.frm.start.value = date+" 00:00:00";
	document.frm.end.value = date+" 23:59:59";
}
function IsNull(strValue,strMsg){
	if(Trim(strValue).length>0) return true;
	else{
		//alert(strMsg+'����Ϊ��');
		return false;
	}
}

function Trim(strValue){
	var v = strValue;
	var i = 0;
	while(i<v.length){
	  if(v.substring(i,i+1)!=' '){
		v = v.substring(i,v.length) 
		break;
	  }
	  i = i + 1;
	  if(i==v.length){
        v="";
      }
	}

	i = v.length;
	while(i>0){
	  if(v.substring(i-1,i)!=' '){
	    v = v.substring(0,i);
		break;
	  }	
	  i = i - 1;
	}

	return v;
}

function DateToDes(v){
	if(v != ""){
		pos = v.indexOf("-");
		if(pos != -1 && pos == 4){
			y = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}
		pos = v.indexOf("-");
		if(pos != -1){
			m = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}
		pos = v.indexOf(" ");
		if(pos != -1){
			d = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}
		dt = new Date(m+"/"+d+"/"+y+" "+v);
		var s  = dt.getTime();
		return s;
	}
	else
		return 0;
}
	function CheckForm(){
		var s = DateToDes(document.frm.start.value);
		//alert(s);
		var e = DateToDes(document.frm.end.value);
		//alert(e);

		/*if(!IsNull(document.frm.start.value,"��ʼʱ��")){
			document.frm.start.focus();
			document.frm.start.select();
			return false;
		}
		else if(!IsNull(document.frm.end.value,"����ʱ��")){
			document.frm.end.focus();
			document.frm.end.select();
			return false;
		}*/
		
		if(!IsNull(document.frm.start.value,"��ʼʱ��")){
			if(!IsNull(document.frm.end.value,"����ʱ��")){
				document.frm.startTime.value = "";
				document.frm.endTime.value = "";
				return true;
			} else {
				document.frm.startTime.value = "";
				document.frm.endTime.value = e;
				//alert(document.frm.endTime.value);
				return true;
			}
		} else {
			if(!IsNull(document.frm.end.value,"����ʱ��")){
				document.frm.startTime.value = s;
				document.frm.endTime.value = "";
				//alert(document.frm.startTime.value);
				return true;
			} else if(s>e){
					alert("��ʼʱ�䲻�ܴ��ڽ���ʱ��");
					return false;
				} else {
					document.frm.startTime.value = s;
					document.frm.endTime.value = e;
					//alert(document.frm.startTime.value);
					//alert(document.frm.endTime.value);
					return true;
				}
		}
		return true;
	}

	function getReportList() {
		if (!CheckForm()) return;
		//alert(did);
  		page = "running_report_build_save.jsp?device_id=" + did + "&action_type=1&refresh=" + new Date().getTime();
		//alert(page);
		document.all("childFrm").src = page;
	}

	function sendFile() {
		var file_path = document.forms[0].fileList.value ;
  		page = "running_report_build_save.jsp?file_path=" + file_path + "&action_type=4&refresh=" + new Date().getTime();
		//alert(page);
		document.all("childFrm").src = page;
	}

	function viewFile() {
		var file_path = document.forms[0].fileList.value ;
		//alert(file_path);
  		page = "running_report_build_save.jsp?file_path=" + file_path + "&action_type=2&refresh=" + new Date().getTime();
		document.all("childFrm").src = page;
	}
	
	function saveSuggestion() {
		var file_path = document.forms[0].fileList.value ;
		var suggestion = document.forms[0].suggestion.value;
  		page = "running_report_build_save.jsp?file_path=" + file_path + "&suggestion=" + suggestion + "&action_type=3&refresh=" + new Date().getTime();
		document.all("childFrm").src = page;
	}

	function setStatus(type, suggestion) {
		if (type == 1) {
		} else if (type == 2) {
			document.forms[0].suggestion.value = suggestion;
			showId("tr_filecontext");
		}
	}

	function setId(id, msg) {
		document.getElementById(id).innerHTML = msg;
	}

	function showId(id) {
		document.getElementById(id).style.display = "";
	}

	function hideId(id) {
		document.getElementById(id).style.display = "none";
	}
//-->
</SCRIPT>