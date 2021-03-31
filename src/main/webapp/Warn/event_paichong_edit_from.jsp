<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<jsp:useBean id="qryp" scope="page"
	class="com.linkage.litms.common.database.QueryPage" />
<%-- 
 * 创建日期 2007-3-6
 * Administrator liuli
 * =================================
 * suixz(5253) 2008-03-31
 * req:JSDX_JS-REQ-20080313-zhur-001
--%>
<%
            request.setCharacterEncoding("GBK");
            String str_rule_id = request.getParameter("rule_id");
            String str_is_active = request.getParameter("is_active");
            if (str_rule_id == null)
				response.sendRedirect("../error.jsp?errid=0");
			String sql = "select rule_id,rule_name,gather_id,event_level_id,device_ip,event_oid,repeat_count,elapse_time,is_active,fault_desc,creatorname from fault_filter_multi where rule_id="+str_rule_id+" ";
			Cursor cursor = DataSetBean.getCursor(sql);
			Map fields = cursor.getNext();
			String str_gather_id =(String)fields.get("gather_id");
			String str_level_id1 =(String)fields.get("event_level_id");
			String str_level_id =(String)fields.get("event_oid");
			String str_creator_name =(String)fields.get("creatorname");
			
			String strSQL = "select distinct event_level_id,event_level_name from event_level_def";
			Cursor cursor4 = DataSetBean.getCursor(strSQL);
			Map fields1 = cursor4.getNext();

			String strSQL2 = "select distinct event_oid,event_name from event_def where event_type=2 or event_type=3";
			Cursor cursor2 = DataSetBean.getCursor(strSQL2);
			Map fields2 = cursor2.getNext();

%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function Init(){
	var v_gather = "<%=str_gather_id%>";
	document.all("childFrm1").src="getCity.jsp?gather_id="+ v_gather;
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

function IsSelect(elmID){
	t_obj = document.all(elmID);
	if(!t_obj) return false;
	for(var i=0; i<t_obj.length; i++){
		if(t_obj[i].checked)
			return true;
	}
	return false;
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



function AutoMultiple(elmID){
	t_obj = document.all(elmID);
	for(var i=0; i<t_obj.length; i++){
		t_obj[i].selected = true;
	}
}


function OnEditTime()
{
	var winattr="center:yes;dialogWidth:800px;dialogHeight:550px;"
	var StringPara="",pos1=0,pos2=0;
	StringPara=window.showModalDialog("./RulerTimeForm.jsp","编辑时间",winattr);
	if(StringPara!=null)
	{
		if(StringPara!="")
		document.frm.ruler_time.value=StringPara;
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
		} else {
		    //  alert(oSelect.checked);
			if(oSelect.checked) {
				return true;
			}
			return false;
		}
	} 
}

function CheckForm(){
	var obj = document.frm;
     if(!IsNull(obj.event_name.value,'事件名称')){
		obj.event_name.focus();
		return false;
	}
	 else if(Trim(obj.source_ip.value)!="" && !IsIPAddr(obj.source_ip.value)){
		obj.source_ip.focus();
		obj.source_ip.select();
		return false;
	}
//   else if(obj.event_lei.value=="0"){
//	 alert("请选择事件类型！");
//     return false;
//   }
   else if(obj.paichong_time.value!="" && !IsNumber(obj.paichong_time.value,"排重持续时间")){	
		obj.paichong_time.focus();
		obj.paichong_time.select();
		return false;
	}
	else if(obj.gao_count.value!="" && !IsNumber(obj.gao_count.value,"告警次数")){	
		obj.gao_count.focus();
		obj.gao_count.select();
		return false;
	}else if(obj.ti_level.value=="0"){
	 alert("请选择告警提升级别！");
     return false;
   }
    else if(!CheckChkBox())
	{
		alert("请选择采集点！");
		return false;
	}
	else{
		return true;
	}	
}
//-->
</SCRIPT>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="frm" METHOD="POST" ACTION="event_paichong_edit_from_save.jsp"
			onSubmit="return CheckForm();">
		<table width="95%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162">
						<div align="center" class="title_bigwhite">告警排重规则</div>
						</td>
						<td><img src="../images/attention_2.gif" width="15" height="12">
						注：带<font color="#FF0000">*</font>号为必选项；</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF">
				<table width="100%" border=0 align="center" cellpadding="1"
					cellspacing="1" bgcolor="#999999" class="text">
					<tr bgcolor="#FFFFFF" class="blue_title">
						<TH align="center" colspan="6">告警排重规则</TH>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td nowrap>
						<div align="center"><font color="#FF0000">*</font>规则名称:</div>
						</td>
						<td width="33%"><INPUT TYPE="text" NAME="event_name" maxlength=20
							class=bk size="18" value="<%=fields.get("rule_name")%>" disable></td>
							<td nowrap>
						<div align="center">告警创建者:<br>
						</div>
						</td>
						<%
							String select1 = "";
							String select2 = "";
							String select3 = "";
							String select4 = "";
							String select5 = "";
							String select6 = "";
							String select7 = "";
							if (str_creator_name.equals("-1")) {
								select1 = "selected";
								select2 = "";
							    select3 = "";
								select4 = "";
								select5 = "";
								select6 = "";
								select7 = "";
							} else  if(str_creator_name.equals("hostman")){
								select1 = "";
								select2 = "selected";
								select3 = "";
								select4 = "";
								select5 = "";
								select6 = "";
								select7 = "";
							}else  if(str_creator_name.equals("pmee")){
								select1 = "";
								select2 = "";
								select3 = "selected";
								select4 = "";
								select5 = "";
								select6 = "";
								select7 = "";
							}else  if(str_creator_name.equals("Trap Probe 1")){
								select1 = "";
								select2 = "";
								select3 = "";
								select4 = "selected";
								select5 = "";
								select6 = "";
								select7 = "";
							}else  if(str_creator_name.equals("ServiceAgent")){
								select1 = "";
								select2 = "";
								select3 = "";
								select4 = "";
								select5 = "selected";
								select6 = "";
								select7 = "";
							}else  if(str_creator_name.equals("ipcheck")){
								select1 = "";
								select2 = "";
								select3 = "";
								select4 = "";
								select5 = "";
								select6 = "selected";
								select7 = "";
							}else  if(str_creator_name.equals("visualman")){
								select1 = "";
								select2 = "";
								select3 = "";
								select4 = "";
								select5 = "";
								select6 = "";
								select7 = "selected";
							}
						%>
						<td width="33%">
						<select name="creat_name" size="1" id="creat_name" class=bk  value="<%=fields.get("creator_name")%>">
		                <option value="-1" <%=select1%>>--请选择--</option>
		                <option value="hostman" <%=select2%>>主机及应用告警</option>
		                <option value="pmee" <%=select3%>>网络性能告警</option>
		                <option value="Trap Probe 1" <%=select4%>>SnmpTrap告警</option>
		                <option value="ServiceAgent" <%=select5%>>业务模拟测试</option>
		                <option value="ipcheck" <%=select6%>>Ping检测设备通断系统</option>
		                <option value="visualman" <%=select7%>>visualman网络流量告警</option>
		                <option value="Engine"<%=str_creator_name.equals("Engine")?"winkbreak":""%>>瞬断告警</option>
		                </select> 
						</td>								
						<td nowrap>
						<div align="center">告警源IP:</div>
						</td>
						<td width="33%"><INPUT TYPE="text" NAME="source_ip" maxlength=20
							class=bk size="18" value="<%=fields.get("device_ip")%>"></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td nowrap>
						<div align="center">事件类型:</div>
						</td>
						<td nowrap><%if (fields2 == null) {
				out
						.println("<SELECT name=event_lei class=bk><option value='0'>系统没有事件类型信息</option></SELECT>");
			} else {
				out.println("<SELECT name=event_lei class=bk>");
				out.println("<OPTION VALUE=0>=请选择事件类型=</OPTION>");
				while (fields2 != null) {
					String strselected="";
					if(((String)fields2.get("event_oid")).equals(str_level_id)){
						strselected="selected";
						
					}
					out.println("<option value='" + fields2.get("event_oid")
							+ "'  " + strselected + ">" + fields2.get("event_name") + "</option>");
					fields2 = cursor2.getNext();
				}
				out.println("</select>");
			}

			%></td>
						<td nowrap>
						<div align="center"><span>排重持续时间</span>:</div>
						</td>
						<td width="33%"><INPUT TYPE="text" NAME="paichong_time"
							maxlength=20 class=bk size="18" value="<%=fields.get("elapse_time")%>"></td>
							<td nowrap>
						<div align="center">告警提升级别:</div>
						</td>
						<td nowrap><%if (fields1 == null) {
				out.println("<SELECT name=ti_level class=bk><option value='0'>系统没有告警提升级别</option></SELECT>");
			} else {
				out.println("<SELECT name=ti_level class=bk>");
				out.println("<OPTION VALUE=0>=请选择告警提升级别=</OPTION>");
				while (fields1 != null) {
					String str_selected="";
					if(((String)fields1.get("event_level_id")).equals(str_level_id1)){
						str_selected="selected";
						
					}
					out.println("<option value='"
							+ fields1.get("event_level_id") + "'  " + str_selected + ">"
							+ fields1.get("event_level_name") + "</option>");
					fields1 = cursor4.getNext();
				}
				out.println("</select>");
			}

			%></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td nowrap>
						<div align="center">告警次数:</div>
						</td>
						<td width="33%"><INPUT TYPE="text" NAME="gao_count" maxlength=20
							class=bk size="18" value="<%=fields.get("repeat_count")%>"></td>
						<td>
						<div align="center">是否启用:</div>
						</td>
						<td><span><span><strong> <input type="radio" name="radiobutton"
							value="1" checked> </strong>是<strong> <input type="radio"
							name="radiobutton" value="0"> </strong>否 </span></span></td>
							<td></td>
							<td></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td rowspan="1" class="text" nowrap>
						<div align="center"><font color="#FF0000">*</font>采集点:<br>
						<INPUT TYPE="checkbox" onclick="selectAll('Mycheckbox1')">
						全选</div>
						</td>
						<td rowspan="1">
						<table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="1" bgcolor="#999999">
							<div id="Layer"
								style="overflow:scroll;width:'250px';height:'120px'"><SPAN
								id=device_info1></SPAN></div>
						</table>
						</td>
						<td nowrap>
						<div align="center">告警详细描述:</div>
						</td>
						<td class="text" colspan="3"><textarea name="textarea" cols="35"
							rows="6" value="<%=fields.get("fault_desc")%>" class=bk><%=fields.get("fault_desc")%></textarea></td>
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
								<div align="right"><INPUT TYPE="submit" value=" 保 存 " class=btn>&nbsp;&nbsp;
								<INPUT TYPE="hidden" name="rule_id" value=<%=str_rule_id%>>	
								<INPUT TYPE="hidden" name="is_active" value=<%=str_is_active%>>								
								<INPUT TYPE="hidden" name="action" value="update"> &nbsp;&nbsp;</div>
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
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
		&nbsp;</TD>
	</TR>
	<TR>
		<TD HEIGHT=20><IFRAME ID=childFrm1 SRC="" STYLE="display:none"></IFRAME>
		&nbsp;</TD>
	</TR>
</TABLE>
<script type="text/javascript">
Init();
</script>
<%@ include file="../foot.jsp"%>
