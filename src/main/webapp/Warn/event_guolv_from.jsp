<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="eventservice" scope="request"
	class="com.linkage.litms.warn.WarnService" />
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<%--
 * �������� 2007-3-6
 * Administrator liuli
--%>
<%
	request.setCharacterEncoding("GBK");
		String ShortName = LipossGlobals.getLipossProperty("InstArea.ShortName");
		if (ShortName != null && ShortName.equals("js_dx"))
			{
				String path = request.getServletPath();
				String role = String.valueOf(user.getRoleId());
			}
		//����
		String strVendorList = DeviceAct.getVendorList(true, "", "");
		//�豸�ͺ�
		String strDeviceModel = DeviceAct.getDeviceModelByVendorID1(request, "",
				"device_model_id");
		String strData = eventservice.getEventGuolvHtml(request);
		String strevent = eventservice.geteventTypeList("");
		String streventList = eventservice.geteventnameTypeList();
		String strSQL4 = "select distinct a.event_attr_oid,a.event_attr_name from event_attr_def a ,event_def b,event_attr_relation c where b.event_oid=c.event_oid and c.event_attr_oid = a.event_attr_oid";
		Cursor cursor4 = DataSetBean.getCursor(strSQL4);
		Map fields4 = cursor4.getNext();
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
function Init(){
	document.all("childFrm1").src="getcityxinxi.jsp";
}
function getradiovalue(name){
��var ip="";
����var rPort = document.all(name);
����for(i=0;i<rPort.length;i++)
����{
 ����if(rPort[i].checked)
   ����ip=rPort[i].value;
����}
	return ip;
}

function Add(){
	document.frm.reset();
	document.frm.action.value="add";
}

function IsSelect(elmID){
	var t_obj = document.all(elmID);
	if(!t_obj) return false;
	for(var i=0; i<t_obj.length; i++){
		if(t_obj[i].checked)
			return true;
	}
	return false;
}
function selectAll(elmID){
	var t_obj = document.all(elmID);
	if(!t_obj) return;
	var obj = event.srcElement;
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
	}
	else{
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
		//alert("3333");
}
 var iPos = 0;
 function addipport(){
	t_obj = document.all("Mycheckbox");
	if(!t_obj) {
		return;
	}
	for(var i=0; i<t_obj.length; i++){
		if(t_obj[i].checked){
			addip(t_obj[i]);
		}
	}

 }
function DoSearch(obj,text){
	var bz=true;
	for(var i=0;i<obj.options.length;i++){
		if(obj.options[i].Value==text){
			bz=false;
		}
	}
	if(bz){return true;}
	else{return false;}
}

function addip(v){
��var oValue = v.value;
  var oSelect = document.all("ipport");
��if(DoSearch(oSelect,oValue)){
	var oOption = document.createElement("OPTION");
	oSelect.options.add(oOption);
	oOption.innerText =v.iText;
	oOption.Value = v.value;
	oSelect.selectedIndex = iPos;
	iPos++;
  }
}
function delip(){
	var oSelect = document.all("ipport");
	var oPos = oSelect.selectedIndex;
	if(oPos!=-1){
		oSelect.remove(oPos);
		iPos--;
		oSelect.selectedIndex = 0;
	}
}


function AutoMultiple(elmID){
	t_obj = document.all(elmID);
	for(var i=0; i<t_obj.length; i++){
		t_obj[i].selected = true;
	}
}


function OnEditTime()
{
	var i=getradiovalue("ruletype");
	if(i==1){
		window.open("./RulerTimeFormGv.jsp?ruletype="+i,"�༭ʱ��","left=20,top=20,width=740,height=450,resizable=yes,scrollbars=yes");
	}else{
		window.open("./RulerTimeFormGv.jsp?ruletype="+i,"�༭ʱ��","left=300,top=20,width=500,height=250,resizable=yes,scrollbars=yes");
	}

}

function ExecMod(){
	t_obj = document.all('Mycheckbox1');
	var checkvalue = "";
	if (typeof(t_obj.length) != "undefined"){
		for(var i=0; i<t_obj.length; i++){
			if(t_obj[i].checked == true){
				if(checkvalue ==""){
					checkvalue = t_obj[i].value;
				}else{
					checkvalue +="','" + t_obj[i].value;
				}
			}
		}
	}
	else{
		if (t_obj.checked){
			checkvalue = t_obj.value;
		}
	}

	if(checkvalue == ""){
		alert("��ѡ��ɼ���!");
		return false;
	}
	var page="event_device_list.jsp?device_model_id="
			+ document.all("device_model_id").value + "&Mycheckbox1="
			+ checkvalue +"&start_ip="+ document.frm.start_ip.value + "&over_ip=" + document.frm.over_ip.value;
	window.open(page,"","left=20,top=20,width=740,height=450,resizable=yes,scrollbars=yes");

}

function CheckChkBox() {
	var oSelect = document.all("Mycheckbox1");
	if(typeof(oSelect) == "object" ) {
		if(typeof(oSelect.length) != "undefined") {
			for(var i=0; i<oSelect.length; i++) {
				if(oSelect[i].checked) {
					return true;
				}
			}
			return false;
		}
		else {
		    //  alert(oSelect.checked);
			if(oSelect.checked) {
				return true;
			}
			return false;
		}
	}
}


function removeAll(){
	var allWarn="";
	var t_obj = document.all("Mycheckbox");
	for(var i=0; i<t_obj.length; i++){
		if(t_obj[i].checked == true){
			allWarn += t_obj[i].value + ",";
		}
	}
	if(allWarn == ""){
		alert("��ѡ��澯�¼���");
		return false;
	}else{
	      // alert(allWarn);
		   document.frm1.hid_allwarn.value=allWarn;
	}
	return true;
}

function delWarn(){
	if(confirm("���Ҫɾ���ü�¼��\n��������ɾ���Ĳ��ָܻ�������")){
		return true;
	}
	else{
		return false;
	}
}

function Add(){
	document.frm.reset();
	document.frm.action.value="add";
	actLabel.innerHTML = '���';
}

function showChild(parname){
	var obj = event.srcElement;
	if(parname=='event_oid'){

		var pid = obj.options[obj.selectedIndex].value;
		if(parseInt(pid) == 0)
			document.all("eventlist").innerHTML="<select name=\"asas\" class=bk><option value=-1>����ѡ���¼�����</option></select>";
		else
			document.all("childFrm").src = "getDingyilist.jsp?event_oid="+ pid;
	}
	if(parname=="vendor_id"){
		page = "../Resource/showDevicetype.jsp?vendor_id="+document.frm.vendor_id.value;
		document.all("childFrm").src = page;
	}
}
//function getDeviceModel(request){
//	$("sp_DeviceModel").innerHTML = request.responseText;
//}
//Debug
function showError(request){
	//alert(request.responseText);
	$("debug").innerHTML = request.responseText;
}
function CheckForm(){
	var reg=/^[0-9]+$/g;
	var obj = document.frm;
	if(!IsNull(obj.ruler_name.value,'��������')){
		obj.ruler_name.focus();
		return false;
	}
	else if(!CheckChkBox())
	{
		alert("��ѡ��ɼ��㣡");
		return false;
	}
	else if(!IsNull(obj.ruler_time.value,'����ʱ��')){
		obj.ruler_time.focus();
		return false;
	}
   else if(Trim(obj.start_ip.value)!="" && !IsIPAddr(obj.start_ip.value)){
		obj.start_ip.focus();
		obj.start_ip.select();
		return false;
	}
	else if(Trim(obj.over_ip.value)!="" && !IsIPAddr(obj.over_ip.value)){
		obj.over_ip.focus();
		obj.over_ip.select();
		return false;
	}
	else if (document.frm.savetime.value.search(reg)==-1){
		alert('����ʱ���������������');
		document.frm.savetime.focus();
		return false;
	}
	else{
		AutoMultiple('ipport');
		return true;
	}
}

function changeAttrflag(){
	if (document.frm.attr_flag.value == '1' || document.frm.attr_flag.value == '3'){
		document.frm.savetime.readOnly = false;
	}
	else{
		document.frm.savetime.readOnly = true;
	}
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="frm" METHOD="POST" ACTION="event_guolv_from_save.jsp"
			onSubmit="return CheckForm();">
		<table width="95%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162">
						<div align="center" class="title_bigwhite">�澯���˹���</div>
						</td>
						<td><img src="../images/attention_2.gif" width="15"
							height="12"> ע����<font color="#FF0000">*</font>��Ϊ��ѡ��,��ѡ��"�澯�ȼ�"�Ͳ鿴"�豸�б�"ʱ����ѡ��ǰ�������</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF">
				<table width="100%" border=0 align="center" cellpadding="1"
					cellspacing="1" bgcolor="#999999" class="text">
					<tr bgcolor="#FFFFFF" class="blue_title">
						<TH colspan="6" align="center">�澯���˹���</TH>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td width="10%" nowrap><font color="#FF0000">*</font>��������:</td>
						<td width="23%"><INPUT TYPE="text" NAME="ruler_name"
							maxlength=20 class=bk size="18"></td>
						<td width="10%" nowrap>�¼�����:</td>
						<td width="23%"><%=streventList%></td>
						<td width="10%" nowrap>�澯�����ߣ�</td>
						<td width="23%"><select name="creat_name" size="1"
							id="creat_name" class=bk>
							<option value="-1">--��ѡ��--</option>
							<option value="hostman">������Ӧ�ø澯</option>
							<option value="pmee">�������ܸ澯</option>
							<option value="Trap Probe 1">SnmpTrap�澯</option>
							<option value="ServiceAgent">ҵ��ģ�����</option>
							<option value="ipcheck">Ping����豸ͨ��ϵͳ</option>
							<option value="visualman">visualman���������澯</option>
							<option value="Engine">˲�ϸ澯</option>
						</select></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td nowrap>���̣�</td>
						<td bgcolor="#FFFFFF"><%=strVendorList%></td>
						<td nowrap>�豸�ͺ�:</td>
						<td id="div_devicetype"><%=strDeviceModel%></td>
						<td><font color="#FF0000"></font>��������:</td>
						<td><input name="ruletype"
							type="radio" value="1" checked> ���� <input type="radio"
							name="ruletype" value="0"> ��ʱ </td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td rowspan="3" class="text" nowrap>
						<div align="center"><font color="#FF0000">*</font>�ɼ���:<br>
						<INPUT TYPE="checkbox" onclick="selectAll('Mycheckbox1')">
						ȫѡ</div>
						</td>
						<td rowspan="3">
						<table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="1" bgcolor="#999999">
							<div id="Layer"
								style="overflow: scroll; width: '150px'; height: '120px'"><SPAN
								id=device_info1></SPAN></div>
						</table>
						</td>
						<td nowrap>�豸��ʼIP:</td>
						<td><INPUT TYPE="text" NAME="start_ip"
							maxlength=20 class=bk size="18"></td>
						<td nowrap>�豸����IP:</td>
						<td><INPUT TYPE="text" NAME="over_ip"
							maxlength=20 class=bk size="18"></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td><font color="#FF0000">*</font>�Ƿ�����:</td>
						<td><span><span> <input name="radiobutton"
							type="radio" value="1" checked> ���� <input type="radio"
							name="radiobutton" value="0"> ��ͣ </span></span></td>
						<td nowrap>�澯�ȼ�:</td>
						<td><select name="dengji" class=bk>
							<option value="0">==</option>
							<option value="1">></option>
							<option value="2">>=</option>
							<option value="3"><</option>
							<option value="4"><=</option>
						</select><%=strevent%></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td>��������:</td>
						<td><textarea name="ruler_desc" cols="18" rows="2" class=bk></textarea></td>
						<td>����ʱ��:</td>
						<td><TEXTAREA name="ruler_time" cols="18" rows="2" readonly
							class=bk></TEXTAREA><br>
						<INPUT name="EditTime" type="button" onClick="OnEditTime();"
							value="�༭ʱ��" class=btn></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td>�豸�б�</td>
						<td><input type="button" value="�鿴" name="cmdMod"
							onclick="ExecMod()" class="btn"> <INPUT TYPE="hidden"
							name="deviceid">
						</td>
						<td>���˷�ʽ��</td>
						<td>
							<select name="attr_flag" class=bk onchange="changeAttrflag()">
								<option value="-1">==��ѡ��==</option>
								<option value="0">--�˸澯Ҫ�����--</option>
								<option value="1">--�˸澯ֻ���--</option>
								<option value="2">--�˸澯ֻ��ʾ--</option>
								<option value="3">--�˸澯���������ʾ--</option>
							</select>
						</td>
						<td>�������ڣ�</td>
						<td><input type="text" name="savetime" class=bk readonly value="0">��</td>
					</tr>
					<tr bgcolor="#FFFFFF" style="display:none">
						<td colspan="2" class="blue_foot">
						<div align="center"><strong>�澯����</strong></div>
						</td>
						<td colspan="4" class="blue_foot">
						<div align="left"><strong>�Ѷ���ĸ澯���� <input
							type="radio" name="radiobutton1" value="1" checked> </strong>��<strong>
						<input type="radio" name="radiobutton1" value="0"> </strong>��</div>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" style="display:none">
						<td colspan="2">
						<table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="1" bgcolor="#999999">
							<tr bgcolor="#FFFFFF">
								<td class="text">
								<div align="center">������:</div>
								</td>
								<td nowrap><span id=eventlist><select
									name="event_name11" class=bk>
									<option value=-1>=����ѡ���¼�����=</option>
								</select></span></td>
							</tr>
							<tr bgcolor="#FFFFFF">
								<td class="text">
								<div align="center">����ֵ:</div>
								</td>
								<td><INPUT TYPE="text" NAME="shuxing_oid" maxlength=225
									class=bk size="20"></td>
							</tr>
							<tr bgcolor="#FFFFFF">
								<td class="text">
								<div align="center">��������:</div>
								</td>
								<td class="text"><select name="guanlian" class="form_kuang">
									<option value="" selected>��ѡ���������</option>
									<option value="1">==</option>
									<option value="2">like</option>
								</select></td>
							</tr>
						</table>
						</td>
						<td>
						<div align="center">
						<p>
						<button class="text" onclick="OnEditTime1();">���� >></button>
						</p>
						<p>
						<button class="text" onclick="delip()"><< ɾ��</button>
						</p>
						</div>
						</td>
						<td colspan="3">
						<table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="1" bgcolor="#999999">
							<TR bgcolor="#FFFFFF">
								<td><select name="ipport"
									style="width: '200px'; height: '100px'" multiple class=bk>
								</select></TD>
							</TR>
						</table>
						</td>

					</tr>
					<tr bgcolor="#FFFFFF">
						<td colspan="6" class="blue_foot">
						<div align="right">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="82%">
								<div align="right"></div>
								</td>
								<td width="18%">
								<%
									if (ShortName != null && ShortName.equals("js_dx"))
											{
								%>
								<div align="right"><INPUT TYPE="submit" value=" �� �� "
									class=btn>&nbsp;&nbsp;&nbsp;&nbsp; <INPUT TYPE="hidden"
									name="action" value="add"></div>
								<%
									}
										else
											{
								%>
								<div align="right"><INPUT TYPE="submit" value=" �� �� "
									class=btn>&nbsp;&nbsp;&nbsp;&nbsp; <INPUT TYPE="hidden"
									name="action" value="add"></div>
								<%
									}
								%>
								</td>
							</tr>
						</table>
						</div>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</FORM>
		<br>
		<FORM NAME="frm1" METHOD="POST" ACTION="event_guolv_list_delete.jsp"
			onSubmit="return removeAll();">
		<table width="95%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="blue_gargtd">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162">
						<div align="center" class="title_bigwhite">�澯���˹����б�</div>
						</td>
						<td><img src="../images/attention_2.gif" width="15"
							height="12"> <font color="#FF0000">ע��</font>�ı��б�˳��ʱֻ��"��ѡ".</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%"
					id="myTable" bordercolorlight="#000000" bordercolordark="#FFFFFF">
					<TR>
						<TH colspan="9" align="center" bgcolor='white'>�澯���˹����б�</TH>
					</TR>
					<TR bgcolor='white'>
						<TH nowrap>ѡ��</TH>
						<TH nowrap>��������</TH>
						<TH nowrap>�豸�ͺ�</TH>
						<TH nowrap>ʱ�䷶Χ</TH>
						<TH nowrap>�ɼ���</TH>
						<TH nowrap>��������</TH>
						<TH nowrap>������ʱ��</TH>
						<TH nowrap>����</TH>
					</TR>
					<%
						out.println(strData);
					%>
					<tr bgcolor="#FFFFFF" class=text
						onmouseout="className='blue_trOut'">
						<td colspan="9" class="blue_foot">&nbsp;&nbsp;</td>
					</tr>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
		&nbsp;</TD>
	</TR>
	<TR>
		<TD HEIGHT=20><IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
		&nbsp;</TD>
	</TR>
</TABLE>
<div id="debug"></div>
<script type="text/javascript">
    Init();

	function OnEditTime1()
{
	var obj = document.frm;
	if(obj.event_name.value=="-1"){
	   alert("��ѡ����������");
	   return false;
     }
    if(!IsNull(obj.shuxing_oid.value,'����ֵ')){
		obj.shuxing_oid.focus();
		return false;
	}

	if(obj.guanlian.value=="-1"){
	   alert("��ѡ��������ͣ�");
	    return false;
    }
	var output = "";
	var link = "";
	if(obj.guanlian.value == 1){
		link = "|==|";
	}else{
		link = "|like| ";
	}
	var _txt = obj.event_name.options[obj.event_name.selectedIndex].text;
	_txt = _txt.substring(2,_txt.length);
	_txt = _txt.substring(0,_txt.length-2);
	//_txt = _txt.substring(2,_txt.length);
	// _txt = _txt.substring(0,_txt.length-2);
	output = _txt + link + obj.shuxing_oid.value;
	var oSelect = document.all("ipport");
	var oOption = document.createElement("OPTION");
    oOption.text = output;
	oOption.value= obj.event_name.value + "|" + obj.guanlian.value + "|" + obj.shuxing_oid.value;
	oSelect.add(oOption);
}
</script>
<%@ include file="../foot.jsp"%>
