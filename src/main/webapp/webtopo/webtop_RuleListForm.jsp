<%@ include file="../timelater.jsp"%>
<%@ page import="com.linkage.litms.webtopo.warn.*"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	String strList = "";
	String account = user.getAccount();
	String file = request.getRealPath("webtopo/" + account + "_warnrule.xml");
	DeviceWarnFilter filter = DeviceWarnFilter.getInstance(file);//new DeviceWarnFilter(file);
	String[][] m_strDatas = filter.getWarnRule();
	int m_iRowCount = 0; // 记录数
	if (m_strDatas != null) {
      m_iRowCount = m_strDatas.length;
    }
	for (int row = 0; row < m_iRowCount; row++) {
		strList += "addRow('" + m_strDatas[row][1] + "',"
				+ "'" + m_strDatas[row][2] + "',"
				+ "'" + m_strDatas[row][3] + "',"
				+ "'" + m_strDatas[row][4] + "',"
				+ "'" + m_strDatas[row][4] + "');";
	}
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
Error.prototype.print = function() {printError(this);}
var tabRowIndex = -1;
function printError(_obj){
	alert(_obj.description);
}
//添加规则
function addRule(){
	//var winattr="resizable:no;scroll:yes;dialogHeight:300px;dialogWidth:600px;status:no;help:no";	 
	//var returnObj = window.showModalDialog("./webtop_AddRuleForm.jsp",window,winattr);
	var returnObj = openDialog();
	if(returnObj != null)
		addRow(returnObj.name,returnObj.desc,returnObj.text,returnObj.result);
}
function openDialog(name,desc,text,result){
	var winattr="resizable:no;scroll:yes;dialogHeight:400px;dialogWidth:700px;status:no;help:no";
	var page = "./webtop_AddRuleForm.jsp";
	if(arguments.length >= 4){
			page += "?name=" + name + "&desc=" + desc + "&text=" + text + "&result=" + result;
  }
		
	var returnObj = window.showModalDialog(page,window,winattr);
	return returnObj;
}
//创建行
function createRow(oTab,name,desc,text,result,index)
{   
	  var oRow = oTab.insertRow();
	  oRow.bgColor = "#FFFFFF";
	  oRow.align = "center";
	  //增加行点击事件
	  oRow.onclick=clkRow;
	  var tmpCell = "<a href=\"javascript://\" onclick='up(1)'>↑</a>";
	  tmpCell += "<a href=\"javascript://\" onclick='up(-1)'>↓</a>";
	  tmpCell += "<a href=\"javascript://\" onclick='edit()'>编辑</a>";
	  tmpCell += "<a href=\"javascript://\" onclick='delRow()'>删除</a>";
	  var oCell = oRow.insertCell();
	  oCell.innerHTML = index;
	  oCell = oRow.insertCell();
	  oCell.innerHTML = name;
	  oCell = oRow.insertCell();
	  oCell.innerText = desc;
	  oCell = oRow.insertCell();
	  oCell.innerText = text;
	  oCell = oRow.insertCell();	  
	  oCell.innerHTML = result;
	  oCell = oRow.insertCell();
	  oCell.innerHTML = tmpCell;
}
//删除行
function delRow(){
	 if(!confirm("确实要删除此行吗？"))
	 	return;
	 var intRowIndex = event.srcElement.parentElement.parentElement.rowIndex;
	 var oTab = document.all("myTable");
	 for(var i=intRowIndex;i<oTab.rows.length;i++){
		oTab.rows[i].cells[0].innerHTML --;
	 }
	 oTab.deleteRow(intRowIndex);
}
//新增行
function addRow(name,desc,text,result){
	var oTab = document.all("myTable");
	var tbIndex=oTab.rows.length;
	//加上"/"，防止后台出错
	if(text.indexOf("设备类型")!= -1 && text.lastIndexOf('/') == -1){
		text += "/";
	}
	createRow(oTab,name,desc,text,result,tbIndex-3);	
}
//编辑行
function editRow(rowIndex,name,desc,text,result){
	//加上"/"，防止后台出错
	if(text.indexOf("设备类型")!= -1 && text.lastIndexOf('/') == -1){
		text += "/";
	}
	var oTab = document.all("myTable");
	setCellValue(oTab,rowIndex,1,name);
	setCellValue(oTab,rowIndex,2,desc);
	setCellValue(oTab,rowIndex,3,text);
	setCellValue(oTab,rowIndex,4,result);
}
//表单检查
function CheckForm(){
	var oTab = document.all("myTable");
	var length = oTab.rows.length;
	if(length == 3) return;
	var index = "";
	var name = "";
	var desc = "";
	var text = "";
	var result = "";
	var cellValue = "";
	var intRowIndex = 0;
	for(var i=3;i<length;i++){
		intRowIndex = i;
		index += "$" + getCellValue(oTab,intRowIndex,0);
		name += "$" + getCellValue(oTab,intRowIndex,1);
		desc += "$" + getCellValue(oTab,intRowIndex,2);
		text += "$" + getCellValue(oTab,intRowIndex,3);
		result += "$" + getCellValue(oTab,intRowIndex,4);
	}
	index = index.substr(1);
	name = name.substr(1);
	desc = desc.substr(1);
	text = text.substr(1);
	result =  result.substr(1);
	document.all("rule_Priority").value = 	index;
	document.all("rule_Name").value = 	name;
	document.all("rule_Desc").value = 	desc;
	document.all("rule_Text").value = 	text;
	document.all("rule_Result").value = 	result;
	document.all("arrLength").value = 	length - 3;
	//document.all("frm").target = "childFrm";
	return true;
}
//交换表格行的单元值
function convert(rows,indexSource,indexDetinst){
	var tmpValue = "";
	var sRow = rows[indexSource];
	var dRow = rows[indexDetinst];
	for(var i=1;i<sRow.cells.length-1;i++){
		tmpValue = sRow.cells[i].innerHTML;
		sRow.cells[i].innerHTML = dRow.cells[i].innerHTML;
		dRow.cells[i].innerHTML = tmpValue;
	}
}
//删除
function delBtn(){
	if(tabRowIndex == -1){
		alert("请选择表格行");
		return ;
	}
	var oTab = document.all("myTable");
	var sourceRowIndex = tabRowIndex;
	for(var i=sourceRowIndex;i<oTab.rows.length;i++){
		oTab.rows[i].cells[0].innerHTML --;
	}
	oTab.deleteRow(sourceRowIndex);
	sourceRowIndex = -1;
}
function edit(){
	var oTab = document.all("myTable");
	var sourceRowIndex = event.srcElement.parentElement.parentElement.rowIndex;
	var name = getCellValue(oTab,sourceRowIndex,1);
	var desc = getCellValue(oTab,sourceRowIndex,2);
	var text = getCellValue(oTab,sourceRowIndex,3);
	var result = getCellValue(oTab,sourceRowIndex,4);
	var returnObj = openDialog(name,desc,text,result);
	event.cancelBubble = true;
	if(returnObj != null)
		editRow(sourceRowIndex,returnObj.name,returnObj.desc,returnObj.text,returnObj.result);
}
//单独按钮上下调整
function upBtn(count){
	try{
		if(tabRowIndex == -1){
			alert("请选择表格行");
			return ;
		}
		var oTab = document.all("myTable");
		var sourceRowIndex = tabRowIndex;
		var detinstRowIndex = sourceRowIndex - count;
		if(detinstRowIndex == 2 || detinstRowIndex == oTab.rows.length){
			this.event.cancelBubble = true;
			return;
		}
		convert(oTab.rows,sourceRowIndex,detinstRowIndex);
		tabRowIndex = sourceRowIndex - count;
		chColor(tabRowIndex);
		this.event.cancelBubble = true;
	}catch(e){
		e.print();
	}finally{
		oTab = null;
	}
}
//按钮上下调整
function up(count){	
	var oTab = document.all("myTable");
	var sourceRowIndex = event.srcElement.parentElement.parentElement.rowIndex;
	var detinstRowIndex = sourceRowIndex - count;
	if(detinstRowIndex == 2 || detinstRowIndex == oTab.rows.length){
		event.cancelBubble = true;
		return;
	}
	convert(oTab.rows,sourceRowIndex,detinstRowIndex);
}

//得到表格的单元格value
function getCellValue(oTab,rowIndex,cellIndex){
	try{
		return oTab.rows[rowIndex].cells[cellIndex].innerText;
		//return oTab.rows[rowIndex].cells[cellIndex].innerHTML;
	}catch(e){
		e.print();
	}
}
//设置单元格的值
function setCellValue(oTab,rowIndex,cellIndex,_value){
	try{
		oTab.rows[rowIndex].cells[cellIndex].innerHTML = _value;
	}catch(e){
		e.print();
	}
}
//表格行点击事件
function clkRow(){
	var obj = event.srcElement.parentElement;
	tabRowIndex = obj.rowIndex;
	chColor(tabRowIndex);
	event.cancelBubble = true;
}
//改变行的颜色
function chColor(rowIndex){
	if(rowIndex == null)
		return ;
	try{
		var oTab = document.all("myTable");
		clearColor();
		oTab.rows[rowIndex].bgColor="LightSkyBlue";
	}catch(e){
		e.print();
	}finally{
		oTab = null;	
	}
}
//清除表格颜色
function clearColor(){
	var oTab = document.all("myTable");
	for(var i=3;i<oTab.rows.length;i++){
		oTab.rows[i].bgColor="#ffffff";
	}
}
//body点击事件
function bodyClk(){ 
	clearColor();
	tabRowIndex = -1;
} 
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" onclick="event.cancelBubble = true;">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
  <FORM NAME="frm" METHOD="post" ACTION="webtop_RuleListSave.jsp" target="childFrm" onsubmit="return CheckForm()">
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
	  <TR>
		<TD bgcolor=#000000>
		  <TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="myTable" bordercolorlight="#000000" bordercolordark="#FFFFFF">
				<TR>
				  <TD bgcolor="#ffffff" colspan="6" align="right">
					<input type="button" class="jianbian" value="增加" onclick="addRule()">&nbsp;<input type="submit" class="jianbian" value="保存">
					<input type="button" class="jianbian" onclick="upBtn(1)" value="↑" style="display:">&nbsp;<input type="button" class="jianbian" onclick="upBtn(-1)" value="↓" style="display:">&nbsp;<input type="button" class="jianbian" onclick="delBtn()" value="删除" style="display:">
				  </TD>
				</TR>
				<TR>
					<TH colspan="6" align="center">告警过滤 添加规则</TH>
				</TR>
				<TR >
					<TD class=column align="center" nowrap>优先级</TD>
					<TD class=column align="center" nowrap>规则名称</TD>
					<TD class=column align="center" nowrap>规则描述</TD>
					<TD class=column align="center" nowrap>规则内容</TD>
					<TD class=column align="center" nowrap>处理结果</TD>
					<TD class=column align="center" nowrap>操作</TD>
				</TR> 
		  </TABLE>
	    </TD>
	  </TR>
	</TABLE>
	<input type="hidden" name="rule_Priority">
	<input type="hidden" name="rule_Name">
	<input type="hidden" name="rule_Desc">
	<input type="hidden" name="rule_Text">
	<input type="hidden" name="rule_Result">
	<input type="hidden" name="arrLength">
  </FORM>
	<TR><TD>&nbsp;</TD></TR>
	<TR><TD HEIGHT=20>
		<IFRAME ID="childFrm" name="childFrm" SRC="" STYLE="display:none"></IFRAME>
  </TD></TR>
</TABLE>
<SCRIPT LANGUAGE="JavaScript">
<!--
document.body.onclick=bodyClk;
<%=strList%>
//-->
</SCRIPT>
<%@ include file="../foot.jsp"%>