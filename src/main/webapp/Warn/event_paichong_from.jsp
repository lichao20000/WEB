<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="eventservice" scope="request"
	class="com.linkage.litms.warn.WarnService" />
<%-- 
 * 创建日期 2007-3-6
 * Administrator liuli
--%>
<%
	request.setCharacterEncoding("GBK");
	String ShortName = LipossGlobals.getLipossProperty("InstArea.ShortName");
	if(ShortName!=null && ShortName.equals("js_dx")){
	String path=request.getServletPath();
	String role=String.valueOf(user.getRoleId());
	}
	//add end 
    String strevent_lei = eventservice.geteventleiList("");
    String strevent_ti = eventservice.geteventtiList("");
    String strData = eventservice.getEventpaichongHtml(request);
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
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


function removeAll(){
	var allWarn="";
	var t_obj = document.all("Mycheckbox");
	if(typeof(t_obj) == "object" ) {
		if(typeof(t_obj.length) != "undefined") {
			for(var i=0; i<t_obj.length; i++) {
				if(t_obj[i].checked == true) {
					allWarn += t_obj[i].value + ",";
				}
			}
		}
		else {
			if(t_obj.checked) {
				allWarn = t_obj.value;
			}
		}
	} 
	if(allWarn == ""){
		alert("请选择告警事件！");
		return false;
	}else{
		document.frm1.hid_allwarn.value=allWarn;		
	}
	return true;
}

function delWarn(){
	if(confirm("真的要删除该记录吗？\n本操作所删除的不能恢复！！！")){
		return true;
	}
	else{
		return false;
	}
}
function Init(){
	document.all("childFrm1").src="getcityxinxi.jsp";
}

function Add(){
	document.frm.reset();
	document.frm.action.value="add";
}

function showChild(parname){
	var obj = event.srcElement;
	if(parname=='event_oid'){
	
		var pid = obj.options[obj.selectedIndex].value;
		if(parseInt(pid) == 0) 
			document.all("eventlist").innerHTML="<select name=\"asas\" class=bk><option value=-1>请先选择事件名称</option></select>";
		else
			document.all("childFrm").src = "getDingyilist.jsp?event_oid="+ pid;
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

function addip(v){
　var oValue = v.value;
  var oSelect = document.all("ipport");
　if(DoSearch(oSelect,oValue)){
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
	var winattr="center:yes;dialogWidth:800px;dialogHeight:550px;"
	var StringPara="",pos1=0,pos2=0;
	StringPara=window.showModalDialog("./RulerTimeForm.jsp","编辑时间",winattr);
	if(StringPara!=null)
	{
		if(StringPara!="")
		document.frm.ruler_time.value=StringPara;
	}
}

function ExecMod(){
	var page="event_device_list.jsp?device="+document.all.deviceid.value;	
	location.href=page;

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
	 else if(!CheckChkBox()){
		alert("请选择采集点！");
		return false;
	}
	else if (!IsNull(obj.paichong_time.value,'排重持续时间')){
		obj.paichong_time.focus();
		return false;
	}
	else if(obj.paichong_time.value!="" && !IsNumber(obj.paichong_time.value,"排重持续时间")){	
		obj.paichong_time.focus();
		obj.paichong_time.select();
		return false;
	}
	else if(obj.gao_count.value!="" && !IsNumber(obj.gao_count.value,"告警次数")){	
		obj.gao_count.focus();
		obj.gao_count.select();
		return false;
	}else if(obj.event_level_id.value=="-1"){
			 alert("请选择告警提升级别！");
		     return false;
		   }
	 
    // else if(obj.event_oid.value=="-1"){
	// alert("请选择事件类型！");
   //  return false;
    //}
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
	<FORM NAME="frm" METHOD="POST" ACTION="event_paichong_from_save.jsp" onSubmit="return CheckForm();">
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
						<td><img src="../images/attention_2.gif" width="15"
							height="12"> 注：带<font color="#FF0000">*</font>号为必选项。</td>
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
						<td width="10%" nowrap>
						<div align="center"><span><font color="#FF0000">*</font>规则名称:</span></div>
						</td>
						<td width="33%"><INPUT TYPE="text" NAME="event_name"
							maxlength=20 class=bk size="18"></td>
					<td nowrap>
						<div align="center">告警创建者:<br>
						</div>
						</td>
						<td width="33%">
						<select name="creat_name" size="1" id="creat_name" class=bk>
		                <option value="-1">--请选择--</option>
		                <option value="hostman">主机及应用告警</option>
		                <option value="pmee">网络性能告警</option>
		                <option value="Trap Probe 1">SnmpTrap告警</option>
		                <option value="ServiceAgent">业务模拟测试</option>
		                <option value="ipcheck">Ping检测设备通断系统</option>
		                <option value="visualman">visualman网络流量告警</option>
		                <option value="Engine">瞬断告警</option>
		                </select>
						</td>	
						<td nowrap>
						<div align="center"><span>告警源IP:</span></div>
						</td>
						<td width="33%"><INPUT TYPE="text" NAME="source_ip"
							maxlength=20 class=bk size="18"></td>												
					</tr>						
					<tr bgcolor="#FFFFFF">
						<td rowspan="3" class="text" nowrap>
						<div align="center"><font color="#FF0000">*</font>采集点:<br>
						<INPUT TYPE="checkbox" onclick="selectAll('Mycheckbox1')">
						全选</div>
						</td>
						<td rowspan="3">
						<table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="1" bgcolor="#999999">
							<div id="Layer"
								style="overflow:scroll;width:'150px';height:'120px'"><SPAN
								id=device_info1></SPAN></div>
						</table>
						</td>
						<td>
						<div align="center">事件类型:</div>
						</td>
						<td width="33%"><%=strevent_lei%></td>
						<td nowrap>
						<div align="center"><font color="#FF0000">*</font>排重持续时间:</div>
						</td>
						<td width="33%"><INPUT TYPE="text" NAME="paichong_time"
							maxlength=20 class=bk size="18" value="300">(S)</td>												
					</tr>
					<tr bgcolor="#FFFFFF">					
						<td nowrap>
						<div align="center">告警次数:</div>
						</td>
						<td><INPUT TYPE="text" NAME="gao_count" class=bk ></td>		
						<td nowrap>
						<div align="center"><span class="style1" name=device_model>告警提升级别:</span></div>
						</td>
						<td width="33%"><%=strevent_ti%></td>						
					</tr>
					<tr bgcolor="#FFFFFF">
						<td nowrap>
						<div align="center">告警详细描述:</div>
						</td>
						<td><textarea name="textarea" cols="18" rows="4" class=bk></textarea></td>
						<td>
						<div align="center"><font color="#FF0000">*</font>是否启用:</div>
						</td>
						<td><span><span> <input name="radiobutton"
							type="radio" value="1" checked> 启用 <input type="radio"
							name="radiobutton" value="0"> 暂停 </span></span></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td colspan="6" class="blue_foot">
						<div align="right">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td>
										<%	
										if(ShortName!=null && ShortName.equals("js_dx")){%>
								<div align="right"><INPUT TYPE="submit" value=" 保　存 "
									class=btn>&nbsp;&nbsp; <INPUT TYPE="hidden"
									name="action" value="add"> &nbsp;&nbsp;</div>
										<%
					} else {
									%>
								<div align="right"><INPUT TYPE="submit" value=" 保　存 "
									class=btn>&nbsp;&nbsp; <INPUT TYPE="hidden"
									name="action" value="add"> &nbsp;&nbsp;</div>
									<% } %>
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
		<FORM NAME="frm1"  METHOD="POST" ACTION="event_paichong_list_delete.jsp" onSubmit="return removeAll();">
		<table width="95%" height="30" border="0" align="center" cellpadding="0" cellspacing="0" class="blue_gargtd">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162">
						<div align="center" class="title_bigwhite">告警排重规则列表</div>
						</td>
						<td><img src="../images/attention_2.gif" width="15"
							height="12"> <font color="#FF0000">注：</font>改变列表顺序时只能"单选".</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" bordercolorlight="#000000" bordercolordark="#FFFFFF">
					<TR>
						<TH colspan="10" align="center">告警排重规则列表</TH>
					</TR>
					<TR>
						<TH nowrap>选择</TH>
						<TH nowrap>规则名称</TH>
						<TH nowrap>事件类型</TH>
						<TH nowrap>告警源IP</TH>
						<TH nowrap>排重持续时间</TH>
						<TH nowrap>告警提升级别</TH>
						<TH nowrap>是否启用</TH>
						<TH nowrap>规则定义者</TH>
						<TH nowrap>规则定义时间</TH>
						<TH nowrap>操作</TH>
					</TR>
					<%out.println(strData);%>
				<tr bgcolor="#FFFFFF" class=text onmouseout="className='blue_trOut'">
				<td colspan="10" class="blue_foot">&nbsp;&nbsp;
				<%	
					if(ShortName!=null && ShortName.equals("js_dx")){%>
					<INPUT TYPE="checkbox" onclick="selectAll('Mycheckbox')"> 全选 <input
					name="Submit22" type="submit" value=" 删除 "> <input type="hidden"
					name="hid_allwarn" value=""> <input type="hidden" name="action"
					value="delete">
					<%
					} else {
									%>
					 <INPUT
					TYPE="checkbox" onclick="selectAll('Mycheckbox')"> 全选 <input
					name="Submit22" type="submit" value=" 删除 "> <input type="hidden"
					name="hid_allwarn" value=""> <input type="hidden" name="action"
					value="delete">
					<% } %>
					</td>
			      </tr>
				</TABLE>
				</TD>
			</TR>
		  </TABLE>
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
document.getElementsByName("event_oid")[0].style.width=150;
document.getElementsByName("event_level_id")[0].style.width=150;
</script>
<%@ include file="../foot.jsp"%>
