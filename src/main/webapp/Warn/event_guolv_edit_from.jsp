<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.util.FormUtil"%>
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<%-- 
 * �������� 2007-3-6
 * Administrator liuli
--%>
<%
		request.setCharacterEncoding("GBK");
		////flag 1:�༭  2:��ϸ��Ϣ
		String flag = request.getParameter("flag");
		String strrule_id = request.getParameter("rule_id");
		//�쳣
		if (strrule_id == null)
			response.sendRedirect("../error.jsp?errid=0");
		//��ѯ������Ϣ
		String sql = "select * from fault_filter_ruler where rule_id="
				+ strrule_id + " ";
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql = "select gather_id, device_model, resource_type_id, event_level_id, event_oid, rule_time, " +
					"creator_name, attr_flag, savetime, event_rule_name, " +
					"fault_desc, device_ip_min, device_ip_max, event_level_oper, is_active, attr_logic" +
					" from fault_filter_ruler where rule_id="
					+ strrule_id + " ";
		}
		com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if (fields == null){
			response.sendRedirect("../error.jsp?errid=0");
		}
		String str_gather_id = (String) fields.get("gather_id");
		String str_level_id = (String) fields.get("device_model");
		String str_level_id1 = (String) fields.get("resource_type_id");
		String str_level_id2 = (String) fields.get("event_level_id");
		String str_event_oid = (String) fields.get("event_oid");
		String rule_time = (String) fields.get("rule_time");
		String ruletype = "1";
		if (rule_time != null && !rule_time.endsWith("*"))
			{
				ruletype = "0";
			}
		String str_creator_name = (String) fields.get("creator_name");
		String device_model_id = (String) fields.get("device_model");
		//��ѯ�¼���Ϣ
		String strSQL3 = "select distinct event_level_id, event_level_name from event_level_def";
		Cursor cursor3 = DataSetBean.getCursor(strSQL3);
		Map fields3 = cursor3.getNext();
		String strSQL4 = "select distinct a.event_attr_name,a.event_attr_oid  from event_attr_def a ,"
				+ "event_def b,event_attr_relation c where b.event_oid=c.event_oid and c.event_attr_oid = a.event_attr_oid"
				+ " and b.event_oid='" + str_event_oid + "'";
		Cursor cursor4 = DataSetBean.getCursor(strSQL4);
		Map fields4 = cursor4.getNext();
		String strSQL5 = "select distinct  event_oid,event_name from event_def where event_type=2 or event_type=3";
		Cursor cursor5 = DataSetBean.getCursor(strSQL5);
		String streventList = FormUtil.createListBox(cursor5, "event_oid",
				"event_name", true, str_event_oid, "");
		//�Ѷ���ĸ澯����
		String ipportOption = "";
		strSQL5 = "select distinct a.event_attr_name,c.event_attr_oid,c.event_attr_value,c.attr_oper from event_attr_def a,"
				+ "event_attr_relation b,fault_filter_attr_info c where a.event_attr_oid=b.event_attr_oid and "
				+ "a.event_attr_oid=c.event_attr_oid and b.event_oid='"
				+ str_event_oid + "' and c.rule_id=" + strrule_id;
		cursor5 = DataSetBean.getCursor(strSQL5);
		Map fields1 = cursor5.getNext();
		while (null != fields1)
			{
				ipportOption += "<option value='" + fields1.get("event_attr_oid")
						+ "|" + fields1.get("attr_oper") + "|"
						+ fields1.get("event_attr_value") + "'>"
						+ fields1.get("event_attr_name") + "|"
						+ ("1".equals(fields1.get("attr_oper")) ? "==" : "like")
						+ "|" + fields1.get("event_attr_value") + "</option>";
				fields1 = cursor5.getNext();
			}
		//�����豸�ͺŲ�ѯ����
//		cursor5 = DataSetBean.getCursor("select oui,device_model from gw_device_model where device_model_id='"+device_model_id+"'");
		cursor5 = DataSetBean.getCursor("select vendor_id,device_model from gw_device_model where device_model_id='"+device_model_id+"'");
		Map vendor = cursor5.getNext();
		String vendor_id = "";
		String device_model = "";
		if (vendor != null){
			vendor_id = (String)vendor.get("vendor_id");
			device_model = (String)vendor.get("device_model");
		}
		//����
		String strVendorList = DeviceAct.getVendorList(true, vendor_id, "");
		//�豸�ͺ�
		String strDeviceModel = DeviceAct.getOnlyDeviceModelByOUI(request, device_model,
				"");
		//���˷�ʽ
		String attr_flag = (String) fields.get("attr_flag");
		//��������
		String savetime = (String) fields.get("savetime");
		long timeNum = 0;
		if (savetime != null && !"".equals(savetime)){
			timeNum = Long.parseLong(savetime)/(60*60*24);
		}
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function Init(){
	var v_gather = "<%=str_gather_id%>";
	document.all("childFrm1").src="getCity.jsp?gather_id="+ v_gather;
	
	//��ʼ������
	document.frm.attr_flag.value = '<%=attr_flag%>';
	changeAttrflag();
	document.frm.savetime.value = '<%=timeNum%>';
}

function Add(){
	document.frm.reset();
	document.frm.action.value="add";
}

function IsSelect(elmID){
	t_obj = document.all(elmID);
	if(!t_obj) return false;
	for(var i=0; i<t_obj.length; i++){
		if(t_obj[i].checked)
			return true;
	}
	return false;
}
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

function OnEditTime()
{	
	var i=getradiovalue("ruletype");
	if(i==1){
		window.open("./RulerTimeFormGv.jsp?ruletype="+i,"�༭ʱ��","left=20,top=20,width=740,height=450,resizable=yes,scrollbars=yes");
	}else{
		window.open("./RulerTimeFormGv.jsp?ruletype="+i,"�༭ʱ��","left=300,top=20,width=500,height=250,resizable=yes,scrollbars=yes");
	}
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
		page = "showDevicetype.jsp?vendor_id="+document.frm.vendor_id.value;
		document.all("childFrm").src = page;
	}
}
function getDeviceModel(request){
	$("sp_DeviceModel").innerHTML = request.responseText;
}
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
	

function GoList(page){
	this.location = page;
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
		<FORM NAME="frm" METHOD="POST" ACTION="event_guolv_from_edit_save.jsp"
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
							height="12"> ע����<font color="#FF0000">*</font>��Ϊ��ѡ��:</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF">
				<table width="100%" border=0 align="center" cellpadding="1"
					cellspacing="1" bgcolor="#999999" class="text">
					<tr bgcolor="#FFFFFF" class="blue_title">
						<TH align="center" colspan="6">�澯���˹���</TH>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td width="10%" nowrap><font color="#FF0000">*</font>��������:</td>
						<td width="23%"><INPUT TYPE="text" NAME="ruler_name"
							maxlength=20 class=bk size="18"
							value="<%=fields.get("event_rule_name")%>"></td>
						<td nowrap width="10%">�¼����ƣ�</td>
						<td width="23%"><%=streventList%></td>
						<td nowrap width="10%">�澯�����ߣ�</td>
						<%
							String select1 = "";
								String select2 = "";
								String select3 = "";
								String select4 = "";
								String select5 = "";
								String select6 = "";
								String select7 = "";
								if (str_creator_name.equals("-1"))
									{
										select1 = "selected";
										select2 = "";
										select3 = "";
										select4 = "";
										select5 = "";
										select6 = "";
										select7 = "";
									}
								else
									if (str_creator_name.equals("hostman"))
										{
											select1 = "";
											select2 = "selected";
											select3 = "";
											select4 = "";
											select5 = "";
											select6 = "";
											select7 = "";
										}
									else
										if (str_creator_name.equals("pmee"))
											{
												select1 = "";
												select2 = "";
												select3 = "selected";
												select4 = "";
												select5 = "";
												select6 = "";
												select7 = "";
											}
										else
											if (str_creator_name.equals("Trap Probe 1"))
												{
													select1 = "";
													select2 = "";
													select3 = "";
													select4 = "selected";
													select5 = "";
													select6 = "";
													select7 = "";
												}
											else
												if (str_creator_name.equals("ServiceAgent"))
													{
														select1 = "";
														select2 = "";
														select3 = "";
														select4 = "";
														select5 = "selected";
														select6 = "";
														select7 = "";
													}
												else
													if (str_creator_name.equals("ipcheck"))
														{
															select1 = "";
															select2 = "";
															select3 = "";
															select4 = "";
															select5 = "";
															select6 = "selected";
															select7 = "";
														}
													else
														if (str_creator_name.equals("visualman"))
															{
																select1 = "";
																select2 = "";
																select3 = "";
																select4 = "";
																select5 = "";
																select6 = "";
																select7 = "selected";
															}
						%>
						<td width="23%"><select name="creat_name" size="1"
							id="creat_name" class=bk value="<%=fields.get("creator_name")%>">
							<option value="-1" <%=select1%>>--��ѡ��--</option>
							<option value="hostman" <%=select2%>>������Ӧ�ø澯</option>
							<option value="pmee" <%=select3%>>�������ܸ澯</option>
							<option value="Trap Probe 1" <%=select4%>>SnmpTrap�澯</option>
							<option value="ServiceAgent" <%=select5%>>ҵ��ģ�����</option>
							<option value="ipcheck" <%=select6%>>Ping����豸ͨ��ϵͳ</option>
							<option value="visualman" <%=select7%>>visualman���������澯</option>
							<option value="Engine"
								<%=str_creator_name.equals("Engine")?"winkbreak":""%>>˲�ϸ澯</option>
						</select></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td nowrap>���̣�</td>
						<td bgcolor="#FFFFFF"><%=strVendorList%></td>
						<td nowrap>�豸�ͺ�:</td>
						<td><span id="sp_DeviceModel"><%=strDeviceModel%></span></td>
						<td nowrap><font color="#FF0000"></font>��������:</td>
						<td><input name="ruletype"
							type="radio" value="1" <% if(ruletype.equals("1")){%> checked
							<%} %>> ���� <input type="radio" name="ruletype" value="0"
							<% if(ruletype.equals("0")){%> checked <%} %>> ��ʱ </td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td rowspan="1" class="text" nowrap>
						<div align="center"><font color="#FF0000">*</font>�ɼ���:<br>
						<INPUT TYPE="checkbox" onclick="selectAll('Mycheckbox1')">
						ȫѡ</div>
						</td>
						<td rowspan="1">
						<table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="1" bgcolor="#999999">
							<div id="Layer"
								style="overflow: scroll; width: '250px'; height: '120px'"><SPAN
								id=device_info1></SPAN></div>
						</table>
						</td>
						<td rowspan="1"><font color="#FF0000">*</font>����ʱ��:</td>
						<td rowspan="1"><TEXTAREA name="ruler_time" cols="30"
							rows="5" readonly class=bk><%=fields.get("rule_time")%></TEXTAREA>
						<INPUT name="EditTime" type="button" onClick="OnEditTime();"
							value="�༭ʱ��" class=btn></td>
						<td rowspan="1">��������:</td>
						<td rowspan="1"><textarea name="ruler_desc" cols="20"
							rows="5" class=bk><%=fields.get("fault_desc")%></textarea></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td nowrap>�豸��ʼIP:</td>
						<td><INPUT TYPE="text" NAME="start_ip"
							maxlength=20 class=bk size="18"
							value="<%=fields.get("device_ip_min")%>"></td>
						<td nowrap>�豸����IP:</td>
						<td><INPUT TYPE="text" NAME="over_ip"
							maxlength=20 class=bk size="18"
							value="<%=fields.get("device_ip_max")%>"></td>
						<td class="text" nowrap>�澯�ȼ�:</td>
						<td nowrap><select name="dengji" class="form_kuang">
							<option value="0"
								<%="0".equals(fields.get("event_level_oper"))?"selected": ""%>>==</option>
							<option value="1"
								<%="1".equals(fields.get("event_level_oper"))?"selected": ""%>>></option>
							<option value="2"
								<%="2".equals(fields.get("event_level_oper"))?"selected": ""%>>>=</option>
							<option value="3"
								<%="3".equals(fields.get("event_level_oper"))?"selected": ""%>><</option>
							<option value="4"
								<%="4".equals(fields.get("event_level_oper"))?"selected": ""%>><=</option>
						</select> <%
 	if (fields3 == null)
 			{
 				out
 						.println("<SELECT name=dengjixi class=bk><option value='0'>ϵͳû�и澯�ȼ���Ϣ</option></SELECT>");
 			}
 		else
 			{
 				out.println("<SELECT name=dengjixi class=bk>");
 				out.println("<OPTION VALUE=-1>=��ѡ��澯�ȼ�=</OPTION>");
 				while (fields3 != null)
 					{
 						String strselected = "";
 						if (((String) fields3.get("event_level_id"))
 								.equals(str_level_id2))
 							{
 								strselected = "selected";
 							}
 						out.println("<option value='"
 								+ fields3.get("event_level_id") + "'  "
 								+ strselected + ">"
 								+ fields3.get("event_level_name") + "</option>");
 						fields3 = cursor3.getNext();
 					}
 				out.println("</select>");
 			}
 %>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td><font color="#FF0000">*</font>�Ƿ�����:</td>
						<td><input name="radiobutton" type="radio" value="1"
							<%="0".equals(fields.get("is_active"))?"": "checked"%>>
						���� <input type="radio" name="radiobutton" value="0"
							<%="0".equals(fields.get("is_active"))?"checked": ""%>>
						��ͣ</td>
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
						<td><input type="text" name="savetime" class=bk readonly>��</td>
					</tr>
					<tr bgcolor="#FFFFFF" style="display:none">
						<td colspan="2" class="blue_foot">
						<div align="center">�澯����</div>
						</td>
						<td colspan="4" class="blue_foot">
						<div align="left">�Ѷ���ĸ澯���� <input type="radio"
							name="radiobutton1" value="1"
							<%="0".equals(fields.get("attr_logic"))?"": "checked"%>>
						�� <input type="radio" name="radiobutton1" value="0"
							<%="0".equals(fields.get("attr_logic"))?"checked": ""%>>��</div>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" style="display:none">
						<td colspan="2">
						<table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="1" bgcolor="#999999">
							<tr bgcolor="#FFFFFF">
								<td nowrap>
								<div align="center">������:</div>
								</td>
								<td><span id=eventlist>
								<%
									if (fields4 == null)
											{
												out
														.println("<SELECT name=event_name class=bk><option value='0'>ϵͳû�и澯��������Ϣ</option></SELECT>");
											}
										else
											{
												out.println("<SELECT name=event_name class=bk>");
												out.println("<OPTION VALUE=0>=��ѡ��澯������=</OPTION>");
												while (fields4 != null)
													{
														out.println("<option value='"
																+ fields4.get("event_attr_oid") + "'>"
																+ fields4.get("event_attr_name") + "</option>");
														fields4 = cursor4.getNext();
													}
												out.println("</select>");
											}
								%>
								</span></td>
							</tr>
							<tr bgcolor="#FFFFFF">
								<td class="text">
								<div align="center">����ֵ:</div>
								</td>
								<td><INPUT TYPE="text" NAME="shuxing_oid" maxlength=225
									class=bk size="18"></td>
							</tr>
							<tr bgcolor="#FFFFFF">
								<td class="text">
								<div align="center">��������:</div>
								</td>
								<td class="text"><select name="guanlian" class="form_kuang">
									<option selected>��ѡ���������</option>
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
									<%=ipportOption%>
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
								<TD class=foot align="right">
								<%
									if (null != flag && !"2".equals(flag))
											{
								%> <INPUT TYPE="submit"
									value=" �� �� " class=btn>
								<%
									}
								%> <INPUT TYPE="hidden" name="rule_id" value=<%=strrule_id%>>
								<INPUT TYPE="hidden" name="action" value="update"> <INPUT
									TYPE="button" NAME="cmdJump"
									onclick="GoList('event_guolv_from.jsp')" value=" �� �� "
									class=btn></TD>
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
<script type="text/javascript">
Init();
function OnEditTime1()
{
	var obj = document.frm;
	// alert(obj.event_name.value);
	if(obj.event_name.value=="-1"){
	   alert("��ѡ����������");
	   return false;
     }
	// alert(obj.guanlian.value);
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
	oOption.value = obj.event_name.value + "|" + obj.guanlian.value + "|" + obj.shuxing_oid.value;	
	oSelect.add(oOption);	
}
</script>
<%@ include file="../foot.jsp"%>