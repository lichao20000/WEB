<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page
	import="com.linkage.litms.netcutover.*,java.util.*,com.linkage.litms.common.filter.*"%>
<%
	request.setCharacterEncoding("GBK");

	boolean isSZ = SelectCityFilter.isSZ(curUser.getCityId());
	if (isSZ == true)
		response.sendRedirect("Add_adslWork_handForm_sz.jsp");
	String sArea = "";
	if (null != LipossGlobals.getLipossProperty("InstArea.ShortName"))
		sArea = LipossGlobals.getLipossProperty("InstArea.ShortName").substring(0, 2);

	Map tmpMap = com.linkage.litms.common.util.CommonMap.getCityMap();
	ArrayList cityIdList = com.linkage.litms.netcutover.CommonForm
			.getCity(request);
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
//is qh or not?
var isQH = "<%=sArea%>";

function doSend(){
	document.frm.cmdSend.disabled = true;
	var s = "";
	for(var i=0;i<list.length;i++){
		if(s!=""){
			s+= ";"
		}
		for(var j=0;j<16;j++){
			if(j==1){
				s += ","+(i+1);
			}
			else{				
				if(j==0) {
					s += eval("list["+i+"]._f"+(j+1));
				} else if (j==2 && (eval("list["+i+"]._f"+j) == "25") && isQH == "qh") {
					s+= ",";
					s += eval("21");
				} else{
					s+= ",";
					s += eval("list["+i+"]._f"+j);
				}
			}
		}
	}
	
	document.frm.worklist.value = s;
	//alert(s)
	if(confirmProduct()){
		showMsgDlg();
		document.frm.submit();
	}
	else{
		PendingMessage.style.display = "none";
		document.frm.cmdSend.disabled = false;
	}
}

function confirmProduct(){
	var s = "ADSL业务工单";
	for(var i=0;i<document.frm.product_id.length;i++){
		if(document.frm.product_id[i].checked){
			if(document.frm.product_id[i].value=="41"){
				s = "LAN业务工单";
			}
		}
	}
	if(window.confirm("您确定要发送："+s))
		return true;
	else{
		return false;
	}
}

function doBack(){
	this.location.reload();
}

/* 工单参数表格操作 */
var curPos = 0;	//当前选中参数数组下标
var list = new Array();		//参数数组
var iIndex = 4;		//从这个索引开始Draw

function WorkStruct(){
	this._f0 = arguments[1];
	this._f1 = arguments[0];
	this._f2 = arguments[2];
	this._f3 = arguments[3];
	this._f4 = arguments[4];
	this._f5 = arguments[5];
	this._f6 = arguments[6];
	this._f7 = arguments[7];
	this._f8 = arguments[8];
	this._f9 = arguments[9];
	this._f10 = arguments[10];
	this._f11 = arguments[11];
	this._f12 = arguments[12];
	this._f13 = arguments[13];
	this._f14 = arguments[14];
	this._f15 = arguments[15];
}

function doAdd(){
	var o = document.frm;
	o.reset();
	o.hidAction.value = 1;
}

function doEdit(){
	var o = document.frm;
	doPos(o.f1,list[curPos]._f0);
	doPos(o.f2,list[curPos]._f2);
	o.f3.value = list[curPos]._f3;
	o.f4.value = list[curPos]._f4;
	doPos(o.f5,list[curPos]._f5);
	o.f6.value = list[curPos]._f6;
	o.f7.value = list[curPos]._f7;
	o.f8.value = list[curPos]._f8;
	o.f9.value = list[curPos]._f9;
	o.f10.value = list[curPos]._f10;
	o.f11.value = list[curPos]._f11;
	o.f12.value = list[curPos]._f12;
	o.f13.value = list[curPos]._f13;
	doPos(o.f14,list[curPos]._f14);
	o.f15.value = list[curPos]._f15;

	o.hidAction.value = 2;
}

function doDelete(){
	var o = document.frm;
	if(delWarn()){
		o.hidAction.value = 3;
		doSave(3);
	}
}

function delWarn(){
	if(confirm("真的要删除该工单参数吗？\n本操作所删除的不能恢复！！！")){
		return true;
	}
	else{
		return false;
	}
}

function doPos(o,v){
	if(o.length==0) return;
	for(var i=0;i<o.length;i++){
		if(eval(o[i].value == v)){
			o[i].selected = true;
			return;
		}
	}	
}

function doSelect(){
	var o = document.frm;
	if(o.f0.length>1){
		for(var i=0;i<o.f0.length;i++){
			if(o.f0[i].checked){
				curPos = i;
				break;
			}
		}
	}
	else{
		curPos = parseInt(o.f0.value);
	}
	
	document.frm.cmdEdit.disabled = false;
	document.frm.cmdDelete.disabled = false;
}

function doAction(){
	iAction = parseInt(document.frm.hidAction.value);
	if(iAction == 1){
		var device_id = document.all.f6.value;
		//device_id = java.net.URLEncoder.encode(device_id,"gb2312");
		var page = "./CheckDevice.jsp?divice_id=" + device_id +"&deviceIp="+document.all.f7.value + "";
//		page = encodeURI(page);
		document.all.childFrm2.src = page;
	}
	else{
		doSave(iAction);
	}

}

function prtMsg(msg){
	alert(msg);
}

function doSave(iAction){
	var o = document.frm;
	if(iAction == 1 && checkData()){		//增加
		list[list.length] = new WorkStruct(
			o.f1.value,
			list.length,
			o.f2.value,
			o.f3.value,
			o.f4.value,
			o.f5.value,
			o.f6.value,
			o.f7.value,
			o.f8.value,
			o.f9.value,
			o.f10.value,
			o.f11.value,
			o.f12.value,
			o.f13.value,
			o.f14.value,
			o.f15.value
		);
		doDraw(list.length-1);
		init();
	}
	else if(iAction ==2 && checkData()){		//编辑
		list[curPos] = new WorkStruct(
			o.f1.value,
			curPos,
			o.f2.value,
			o.f3.value,
			o.f4.value,
			o.f5.value,
			o.f6.value,
			o.f7.value,
			o.f8.value,
			o.f9.value,
			o.f10.value,
			o.f11.value,
			o.f12.value,
			o.f13.value,
			o.f14.value,
			o.f15.value
		);
		
		doClear(curPos);
		doDraw(curPos);
		init();
	}
	else if(iAction ==3) {		//删除
		tmpPos = RePos();
		list.splice(tmpPos,1);
		doClear(tmpPos);
		init();
	}
}

function RePos(){
	for(var i=0;i<document.frm.f0.length;i++){
		if(document.frm.f0[i].checked)
			return i;
	}
	return 0;
}

function init(){
	var o = document.frm;
	o.reset();
	o.cmdEdit.disabled = true;
	o.cmdDelete.disabled = true;
	o.hidAction.value = 1;
	if(list.length>0){
		o.cmdSend.disabled = false;
	}
	else{
		o.cmdSend.disabled = true;
	}
}

function doDraw(index){
	if(list.length == 0) return;

	var o = document.frm;
	row = idTable.insertRow(iIndex + index);
	row.bgColor = "#ffffff";
	for(var j=0;j<16;j++){
		cell = row.insertCell();
		cell.className = "column";
		v = eval("list["+index+"]._f"+j);
		if(j==0){
			cell.innerHTML = (v+1)+"<input type=radio name=f0 class=bk value='"+v+"' onclick='doSelect()'>";
		}
		else if(j==1||j==2||j==5||j==14){
			cell.innerHTML = parseText(eval("o.f"+j),v);
		}
		else{
			if(v=="") v="&nbsp;";
			cell.innerHTML = v;
		}
	}
} 

function doClear(index){
	idTable.deleteRow(iIndex + index);
}

function parseText(o,v){
	if(o.tagName.toUpperCase() != "SELECT") return "";
	for(var i=0;i<o.length;i++){
		if(eval(o[i].value == v)) 
			return o[i].text;
	}
}

function checkData(){
	var o = document.frm;
	if(!IsNull(o.f3.value,"用户账号")){
		o.f3.focus();
		o.f3.select();
		return false;
	}
	else if(Trim(o.f6.value).length==0 && Trim(o.f7.value).length==0){
		alert("设备编码和IP必须填写一个");
		o.f6.focus();
		o.f6.select();
		return false;
	}
	else if(Trim(o.f7.value).length>0 && !IsIPAddr(o.f7.value)){
		o.f7.focus();
		o.f7.select();
		return false;			
	}
	else if(!IsNumber(o.f8.value,"机架号")){
		o.f8.focus();
		o.f8.select();
		return false;	
	}
	else if(!IsNumber(o.f9.value,"框号")){
		o.f9.focus();
		o.f9.select();
		return false;	
	}
	else if(!IsNumber(o.f10.value,"槽号")){
		o.f10.focus();
		o.f10.select();
		return false;	
	}
	else if(!IsNumber(o.f11.value,"端口号")){
		o.f11.focus();
		o.f11.select();
		return false;	
	}
	else{
		return true;
	}
}

function showMsgDlg(){
	w = document.body.clientWidth;
	h = document.body.clientHeight;

	l = (w-250)/2;
	t = (h-60)/2;
	PendingMessage.style.left = l;
	PendingMessage.style.top  = t;
	PendingMessage.style.display="";
}

//查询数据库自动填FORM
function showMore() {
	if (document.all.f2.value != '21' && document.all.f3.value.length > 0)
	{
//		alert("hello");
		var product_id = "31";
		for(var i=0;i<document.frm.product_id.length;i++){
			if(document.frm.product_id[i].checked)
				product_id = document.frm.product_id[i].value ;
		}
		document.all.childFrm2.src = "./SheetUser.jsp?username=" + document.all.f3.value+"&product_id="+product_id;
	}
	 if(document.all.f2.value == '29'){
	document.all.f5.disabled = true;
	document.all.f6.disabled = true;
	document.all.f7.disabled = true;
	document.all.f8.disabled = true;
	document.all.f9.disabled = true;
	document.all.f10.disabled = true;
	document.all.f11.disabled = true;
	document.all.f14.disabled = true;
	}
}

//回车键转Tab
function   JsOnKeyDownWithTab() {
	if(event.srcElement.type   !=   'button'  && event.srcElement.type   !=   'hiden'  && event.keyCode   ==   13) {	
		event.keyCode   =   9;
	}
} 

//-->
</SCRIPT>
<div id="PendingMessage"
	style="position:absolute;z-index:3;top:240px;left:250px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
	<center>
		<table border="0">
			<tr>
				<td valign="middle">
					<img src="../images/cursor_hourglas.gif" border="0" WIDTH="30"
						HEIGHT="30">
				</td>
				<td>
					&nbsp;&nbsp;
				</td>
				<td valign="middle">
					<span id=txtLoading style="font-size:14px;">请稍等······</span>
				</td>
			</tr>
		</table>
	</center>
</div>
<FORM NAME="frm" action="Add_adslWork_hand.jsp" method="post"
	target="childFrm">
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
		<TR>
			<TD>
				<TABLE width="96%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<TR>
						<TD bgcolor=#999999 id="idBody">
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%"
								id="idTable">
								<TR bgcolor="#ffffff">
									<TD colspan="16">
										手工输入工单参数.&nbsp;&nbsp;&nbsp;
										<font color=red>注：</font>带有
										<font color=red>*</font>必填
									</TD>
								</TR>
								<TR>
									<TD class=column colspan=16>
										<B>工单业务类型:</B>
										<INPUT TYPE="radio" NAME="product_id" value="31" checked>
										ADSL工单&nbsp;
										<INPUT TYPE="radio" NAME="product_id" value="41">
										Lan工单&nbsp;
									</TD>
								</TR>
								<TR bgcolor="#ffffff">
									<TH nowrap>
										序号
									</TH>
									<TH nowrap>
										操作类型
									</TH>
									<TH nowrap>
										用户帐号
										<font color=red>*</font>
									</TH>
									<TH nowrap>
										绑定电话
									</TH>
									<TH nowrap>
										属地
										<font color=red>*</font>
									</TH>
									<TH nowrap>
										下行速率
									</TH>
									<TH nowrap>
										设备编码
									</TH>
									<TH nowrap>
										设备IP
										<font color=red>*</font>
									</TH>
									<TH nowrap>
										架
										<font color=red>*</font>
									</TH>
									<TH nowrap>
										框
										<font color=red>*</font>
									</TH>
									<TH nowrap>
										槽
										<font color=red>*</font>
									</TH>
									<TH nowrap>
										端
										<font color=red>*</font>
									</TH>
									<TH nowrap>
										用户实名
									</TH>
									<TH nowrap>
										地址
									</TH>
									<TH nowrap>
										上行速率
									</TH>
									<TH nowrap>
										管理员
									</TH>
								</TR>
								<TR bgcolor="#ffffff">
									<TD>
										&nbsp;
									</TD>
									<TD>
										<SELECT NAME="f2" class=bk onkeydown="JsOnKeyDownWithTab()">
											<%
												//修改 10000号 只能看到激活的功能 add 肖学逢 07/4/18
												if (user.getRoleId() == 2)
													out.println("<OPTION value=29>激活</OPTION>");
												else {
													out.println("<OPTION value=21>开户</OPTION>");
													out.println("<OPTION value=22>销户</OPTION>");
													out.println("<OPTION value=23>暂停</OPTION>");
													out.println("<OPTION value=24>更改速率</OPTION>");
													out.println("<OPTION value=25>移机</OPTION>");
													out.println("<OPTION value=26>复机</OPTION>");
													out.println("<OPTION value=29>激活</OPTION>");
												}
											%>
										</SELECT>
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="f3" class=bk size=8
											onBlur="javascript:showMore();"
											onkeydown="JsOnKeyDownWithTab()">

									</TD>
									<TD>
										<INPUT TYPE="text" NAME="f4" class=bk size=7
											onkeydown="JsOnKeyDownWithTab()">
										<!-- <span id=sf3><font color=red>*</font></span> -->
									</TD>
									<TD>
										<SELECT NAME="f1" class=bk onkeydown="JsOnKeyDownWithTab()">
											<%
													for (int i = 0; i < cityIdList.size(); i++) {
													out.println("<option value=" + cityIdList.get(i));
													out.println(">" + tmpMap.get(cityIdList.get(i)) + "</option>");
												}
											%>
										</SELECT>
									</TD>
									<TD>
										<SELECT NAME="f5" class=bk onkeydown="JsOnKeyDownWithTab()">
											<OPTION value="128">
												128K
											</OPTION>
											<OPTION value="256">
												256K
											</OPTION>
											<OPTION value="256">
												384K
											</OPTION>
											<OPTION value="512">
												512K
											</OPTION>
											<OPTION value="1024">
												1M
											</OPTION>
											<OPTION value="1536">
												1.5M
											</OPTION>
											<OPTION value="2048" selected>
												2M
											</OPTION>
											<OPTION value="3072">
												3M
											</OPTION>
											<OPTION value="4096">
												4M
											</OPTION>
											<OPTION value="5120">
												5M
											</OPTION>
											<OPTION value="6144">
												6M
											</OPTION>
											<OPTION value="7168">
												7M
											</OPTION>
											<OPTION value="8192">
												8M
											</OPTION>
											<OPTION value="10240">
												10M
											</OPTION>
										</SELECT>
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="f6" class=bk size=10
											onkeydown="JsOnKeyDownWithTab()">
										<!-- <font color=red>*</font> -->
									</TD>

									<TD>
										<INPUT TYPE="text" NAME="f7" class=bk size=15
											onkeydown="JsOnKeyDownWithTab()">
									</TD>

									<TD>
										<INPUT TYPE="text" NAME="f8" class=bk size=2
											onkeydown="JsOnKeyDownWithTab()">
									</TD>

									<TD>
										<INPUT TYPE="text" NAME="f9" class=bk size=2
											onkeydown="JsOnKeyDownWithTab()">
									</TD>

									<TD>
										<INPUT TYPE="text" NAME="f10" class=bk size=2
											onkeydown="JsOnKeyDownWithTab()">
									</TD>

									<TD>
										<INPUT TYPE="text" NAME="f11" class=bk size=2
											onkeydown="JsOnKeyDownWithTab()">
									</TD>

									<TD>
										<INPUT TYPE="text" NAME="f12" class=bk size=6
											onkeydown="JsOnKeyDownWithTab()">
									</TD>

									<TD>
										<INPUT TYPE="text" NAME="f13" class=bk size=10
											onkeydown="JsOnKeyDownWithTab()">
									</TD>

									<TD>
										<SELECT NAME="f14" class=bk onkeydown="JsOnKeyDownWithTab()">
											<OPTION value="128">
												128K
											</OPTION>
											<OPTION value="256">
												256K
											</OPTION>
											<OPTION value="384">
												384K
											</OPTION>
											<OPTION value="512" selected>
												512K
											</OPTION>
											<OPTION value="1024">
												1M
											</OPTION>
										</SELECT>
									</TD>

									<TD>
										<INPUT TYPE="text" NAME="f15" class=bk size=10
											onkeydown="JsOnKeyDownWithTab()"
											VALUE="<%=user.getAccount()%>">
									</TD>

								</TR>

								<TR bgcolor="#ffffff">
									<TD colspan="9" class=foot>
										<INPUT TYPE="button" NAME="cmdEdit" class=jianbian
											value=" 编 辑 " disabled onclick="doEdit()">
										&nbsp;&nbsp;
										<INPUT TYPE="button" NAME="cmdDelete" class=jianbian
											value=" 删 除 " disabled onclick="doDelete()">
									</TD>
									<TD colspan="7" align="center" class=foot>
										<INPUT TYPE="button" NAME="cmdSave" class=jianbian
											value=" 保 存 " onclick="doAction()">

										<INPUT TYPE="hidden" NAME="hidAction" value="1">
										&nbsp;&nbsp;
										<INPUT TYPE="button" NAME="cmdAdd" class=jianbian
											value=" 取 消 " onclick="init()">
									</TD>
								</TR>

								<TR>
									<TD class=foot colspan=16 align=right>
										<input class=jianbian type="button" value="发送工单"
											name="cmdSend" disabled onclick="doSend()">
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD HEIGHT=20>
				&nbsp;
				<IFRAME ID=childFrm name="childFrm" SRC="about:blank"
					STYLE="display:none"></IFRAME>
				<IFRAME ID=childFrm2 name="childFrm2" SRC="about:blank"
					STYLE="display:none"></IFRAME>
			</TD>
		</TR>
	</TABLE>
	<TEXTAREA NAME="worklist" ROWS="16" COLS="80" style="display:none"></TEXTAREA>
</FORM>
<%@ include file="../foot.jsp"%>

