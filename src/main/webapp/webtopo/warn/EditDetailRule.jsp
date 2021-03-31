<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ include file="../../timelater.jsp"%>
<%@page import="com.linkage.litms.common.database.Cursor" %>
<%@page import="com.linkage.litms.common.database.DataSetBean" %>
<%@page import="bio.webtopo.warn.filter.ConstantEventEnv" %>
<%@page import="java.util.Map,java.util.HashMap,java.util.Iterator" %>
<%@page import="com.linkage.litms.common.util.FormUtil" %>
<%
/**
 * WebTopo实时告警牌告警模板详情编辑页面
 * <li>REQ: GZDX-REQ-20080402-ZYX-001
 * <li>REQ: HBDX-REQ-20080220-WANGBING-001
 *
 * @author	段光锐
 * @version 1.0
 * @since	2008-4-8
 * @category	WebTopo/实时告警牌/告警规则
 * 
 */
	request.setCharacterEncoding("GBK");
	String rowIndex = request.getParameter("rowIndex");
	String text = "";
	text = request.getParameter("text");

	String strSQL = "";
	String strVendorList = "";
	String strModelList = "<select ><option>--请先选择厂商--</option>";
	String strCityList = "<select ><option>--请稍等--</option>";

	Cursor cursor = null;
	//获取下拉框的MAP
	//ConstantEventEnv obj = new ConstantEventEnv();
	//-------------------------------------
	// 告警等级
	int LEVEL = ConstantEventEnv.LEVEL;
	// 设备IP
	int IP = ConstantEventEnv.IP;
	// 设备名称
	int DEVICE_NAME = ConstantEventEnv.DEVICE_NAME;
	// 事件类型
	int EVENT_OID = ConstantEventEnv.EVENT_OID;
	// 设备类型
	int DEVICE_TYPE = ConstantEventEnv.DEVICE_TYPE;
	// 创建者
	int CREATOR_NAME = ConstantEventEnv.CREATOR_NAME;
	// 属地
	int CITY = ConstantEventEnv.CITY;
	//
	
	//--------------------------------------
	// 规则类型Map
	Map RULE_TYPE_MAP = ConstantEventEnv.RULE_TYPE_MAP;
	// 告警等级Map,与 LEVEL 对应
	Map LEVEL_MAP = ConstantEventEnv.LEVEL_MAP;
	// 事件类型Map,与 EVENT_OID 对应
	Map EVENTOID_MAP = ConstantEventEnv.EVENTOID_MAP;
	// 创建者Map,与 CREATOR_NAME 对应
	Map CREATE_EVENT_MAP = ConstantEventEnv.CREATE_EVENT_MAP;
	// 操作关系Map,目前暂不使用
	Map OPERATOR_MAP = ConstantEventEnv.OPERATOR_MAP;
	//--------------------------------------

	//设备厂商
	strSQL = "select vendor_id,vendor_name from tab_vendor";
	cursor = DataSetBean.getCursor(strSQL);
	strVendorList = FormUtil.createListBox(cursor, "vendor_id",
			"vendor_name", true, "", "");
	//设备型号
	//strSQL = "select  serial,(convert(varchar(10),serial)+'/'+device_name+'/'+os_version) as device_name from tab_devicetype_info where 1=1 ";
	//if (LipossGlobals.isOracle()
	//{
	//	strSQL = "select  serial, serial||'/'||device_name||'/'||os_version as device_name from tab_devicetype_info where 1=1 ";
	//}
	//cursor = DataSetBean.getCursor(strSQL);
	//strModelList = FormUtil.createListBox(cursor, "vendor_id","device_name", true, "", "device_model");
	//属地
	//String city_id = curUser.getCityId();
	//SelectCityFilter CityFilter = new SelectCityFilter(request);
	//PrepareSQL pSQL = new PrepareSQL("select city_id,city_name,parent_id from tab_city where city_id in (?) order by city_id");
	//pSQL.setStringExt(1, CityFilter.getAllSubCityIds(city_id, true), false);
	//cursor = DataSetBean.getCursor(pSQL.getSQL());
	//strCityList = FormUtil.createListBox(cursor, "city_name",
	//		"city_name", false, city_id, "cityName");
	
	
	// 将编辑的信息展示到页面
	String[] textArray = null;
	String createRowStr = "";
	if (text != null && !text.equals(""))
	{
		textArray = text.split("(\\s*or\\s*)");
		int len = textArray.length;
		for (int i = 0; i < len; i++)
		{
			String[] createRowArray = ConstantEventEnv
					.formatCotont(textArray[i]);
			createRowStr += "createRow('or','" + createRowArray[0]
					+ "','" + createRowArray[1] + "','"
					+ createRowArray[2] + "');";
		}
	}
%>
<HTML>
<HEAD>
<title>编辑规则详情</title>
<xml id=alarm>
<?xml version="1.0"?>
<root>
<%
	Map myRuleTypeMap = new HashMap();
	Iterator ruleTypeMapKeyIter = RULE_TYPE_MAP.keySet().iterator();
	//int len = myRuleTypeMap.size();
	//Integer[] AllKey = new Integer[len];
	//int count = 0;
	while (ruleTypeMapKeyIter.hasNext())
	{
		String key = (String) ruleTypeMapKeyIter.next();
		int value = (Integer) RULE_TYPE_MAP.get(key);
		//AllKey[count] = value;
		//count ++;
		// name数字为代码,value为显示的汉字名称
		// 例如:<column name='1' value='设备IP'></column>
%>
		<column name='<%=value %>' value='<%=key %>'></column>
		<%
			// myRuleTypeMap中key=数字代码,value为汉字名称
				myRuleTypeMap.put(value, key);
			}
		%>
</root>
</xml>
<META content="text/html;charset=gb2312" http-equiv="Content-Type">
<link href="../../css/css_green.css" rel="stylesheet"
	type="text/css">
<link href="../../css/tree.css" rel="stylesheet"
	type="text/css">
<link href="../../css/tab.css" rel="stylesheet"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript">
<!--
var object = window.dialogArguments;
var root = alarm.documentElement;
var index = 1;
var isAdd = false;
var hasCityList = false;
// 页面初始化
function init()
{
	if((typeof(window.opener.initButton) == "object")||(typeof(window.opener.initButton) == "function")){
		if(document.form1.t2.value == ""){
			window.opener.initButton();
			isAdd = true;		
		}
	}else{
		alert("编辑告警规则页面已经关闭,该页面即将关闭");
		window.close();
	}
	//从XML中读数据生成下拉框
	var aNode = root.selectNodes("//column");
	var oSel = document.form1.s1;
	oSel.length = 1;
	for(var i=0;i<aNode.length;i++)
	{
		oSel.add(createOption(aNode.item(i).getAttribute("value"),aNode.item(i).getAttribute("name")));
	}
}
// 添加详情
function doAdd()
{
	var key=document.form1.s1.value;
	var logic=document.form1.s2.value;	
	var createKey = "";
    if(key=="-1"){
       alert("请选择规则类型!");
	   return true; 
	}
	else if(logic=="-1"){
       alert("请选择规则条件!");
	   return true; 
	}
	else if (key=="<%=IP %>"){
		createKey = "<%=myRuleTypeMap.get(IP) %>";
        if(document.form1.t1.value==""){
          alert("请输入"+createKey+"!");
		  return true; 
		}
		value=document.form1.t1.value;
	}
	else if(key=="<%=DEVICE_TYPE %>"){
		createKey = "<%=myRuleTypeMap.get(DEVICE_TYPE) %>";
        if(typeof(document.form1.device_model)=="undefined" || document.form1.device_model.value=="-1"){
          alert("请选择"+createKey+"!");
		  return true; 
		}
		tmpObj = document.form1.device_model;
		tmpValue = tmpObj.options[tmpObj.selectedIndex].text;
		value=trim(tmpValue);
	}
	else if(key=="<%=DEVICE_NAME %>"){
		createKey = "<%=myRuleTypeMap.get(DEVICE_NAME) %>";
       if(document.form1.t1.value==""){
          alert("请输入"+createKey+"!");
		  return true; 
		}
		value=document.form1.t1.value;
	}
	else if(key=="<%=EVENT_OID %>"){
		createKey = "<%=myRuleTypeMap.get(EVENT_OID) %>";
        if(document.form1.eventno.value=="-1"){
          alert("请选择"+createKey+"!");
		  return true; 
		}
		value=document.form1.eventno.value;
	}
	else if(key=="<%=LEVEL %>"){
		logic = document.form1.serv2.value;
		createKey = "<%=myRuleTypeMap.get(LEVEL) %>";
        if(document.form1.str_severity.value=="-1"){
          alert("请选择"+createKey+"!");
		  return true; 
		}
		value=document.form1.str_severity.value; 
	}
	else if(key=="<%=CREATOR_NAME %>"){
        createKey = "<%=myRuleTypeMap.get(CREATOR_NAME) %>";
        if (document.form1.creat_name.value=="-1"){
          alert("请选择"+createKey+"!");
		  return true; 
        }
        value=document.form1.creat_name.value;
	}
	else if(key=="<%=CITY %>"){
        createKey = "<%=myRuleTypeMap.get(CITY) %>";
        if (typeof(document.form1.cityName)=="undefined" || document.form1.cityName.value=="-1"){
          alert("请输入"+createKey+"!");
		  return true; 
        }
        value=document.form1.cityName.value;
	}
	createRow("or",createKey,logic,value);
	resetText();
}
function trim(str){
	var ch;
	var value = "";
	for(var i=0;i<str.length;i++){
		ch = str.charAt(i);
		if(ch != '-' && ch != '='){
			value += ch;
		}
	}
	return value
}
// 创建行
function createRow(para1,para2,para3,para4)
{   
	var oTab = document.all("tab_alarm");
	var oRow = oTab.insertRow();
	oRow.align = "center";
	var oCell = oRow.insertCell();
	oCell.innerHTML = para1;
	oCell = oRow.insertCell();
	oCell.innerText = para2;
	oCell = oRow.insertCell();
	oCell.innerText = para3;
	oCell = oRow.insertCell();
	oCell.innerText = para4;
	oCell = oRow.insertCell();
	oCell.innerHTML = "<a href=# onclick='delRow()'>删除</a>";
}
// 删除行记录
function delRow()
{
	if(!confirm("确认删除?"))
		return;
	var oTab = document.all("tab_alarm");
	var delRowIndex = event.srcElement.parentElement.parentElement.rowIndex;
 	oTab.deleteRow(delRowIndex);
 	resetText();
}
//重置规则文本框中的内容
function resetText()
{
	var oTab = document.all("tab_alarm");
	var length = oTab.rows.length;
	var key = "";
	var logic = "";
	var value = "";
	var text = "";
	for(var i=1;i<length;i++){
		key = getCellValue(oTab,i,1);
		logic = getCellValue(oTab,i,2);
		value = getCellValue(oTab,i,3);
		text += " or " + key + logic + value;
	}
	if(text != "")
		text = text.substr(4)
	document.form1.t2.value = text;
}
//得到表格的单元格value
function getCellValue(oTab,rowIndex,cellIndex){
	try{
		return oTab.rows[rowIndex].cells[cellIndex].innerText;
	}catch(e){
		e.print();
	}
}
// 创建下拉框
function createOption(text,value)
{
	var obj = document.createElement('OPTION');
	obj.text = text;
	obj.value = value;
	obj.selected = false;	
	return obj;
}

function setChange()
{	
	var sValue = document.form1.s1.value;
	document.form1.t1.value = "";
	if(sValue=="<%=IP %>" || sValue=="<%=DEVICE_NAME %>"){
		severity1.style.display="";
		severity2.style.display="none";
		x1.style.display="";
		x2.style.display="none";
		x3.style.display="none";
		x4.style.display="none";
		x5.style.display="none";
		x6.style.display="none";
		x7.style.display="none";
	}
	else if(sValue=="<%=DEVICE_TYPE %>"){
		severity1.style.display="";
		severity2.style.display="none";
		x1.style.display="none";
		x2.style.display="";
		x3.style.display="";
		x4.style.display="none";
		x5.style.display="none";
		x6.style.display="none";
		x7.style.display="none";
	}
	else if(sValue=="<%=EVENT_OID %>"){
		severity1.style.display="";
		severity2.style.display="none";
		x1.style.display="none";
		x2.style.display="none";
		x3.style.display="none";
		x4.style.display="none";
		x5.style.display="none";
		x6.style.display="none";
		x7.style.display="";
	}else if(sValue=="<%=LEVEL %>"){
		severity1.style.display="none";
		severity2.style.display="";
		x1.style.display="none";
		x2.style.display="none";
		x3.style.display="none";
		x4.style.display="none";
		x5.style.display="";
		x6.style.display="none";
		x7.style.display="none";
	}else if(sValue=="<%=CREATOR_NAME %>"){
		severity1.style.display="";
		severity2.style.display="none";
		x1.style.display="none";
		x2.style.display="none";
		x3.style.display="none";
		x4.style.display="none";
		x5.style.display="none";
		x6.style.display="";
		x7.style.display="none";
	}else if(sValue=="<%=CITY %>"){
		severity1.style.display="";
		severity2.style.display="none";
		x1.style.display="none";
		x2.style.display="none";
		x3.style.display="none";
		getCityList();
		x4.style.display="";
		x5.style.display="none";
		x6.style.display="none";
		x7.style.display="none";
	}
}
// 提交数据
function doOver()
{
	var $ = document.getElementById;
	var rowIndex = $("rowIndex").value;
	var returnVal = document.form1.t2.value;
	if(returnVal != ""){
		if((typeof(window.opener.addRow) == "object" && typeof(window.opener.editRow) == "object") ||(typeof(window.opener.addRow) == "function" && typeof(window.opener.editRow) == "function" )){
			if(rowIndex == -1){
				window.opener.addRow(returnVal);
			}else{
				window.opener.editRow(rowIndex,returnVal);
			}
		}else{
			alert("编辑告警规则页面已经关闭,该页面即将关闭");
		}
	}
	window.close();
}

// 关闭页面时事件
function resetOpener(){
	if(isAdd){
		try{
			if((typeof(window.opener.resetButton) == "object") || (typeof(window.opener.resetButton) == "function")) 
				window.opener.resetButton();
		}catch(e){
			e.print();
		}
	}
	window.close();
}
//过滤设备类型
function showChild(parname){
	if(parname=="vendor_id"){
		var o = document.all("vendor_id");
		var v_id = o.options[o.selectedIndex].value;
		var para = "?refresh="+(new Date()).getTime();
		if(v_id!=-1) para += "&vendor_id="+v_id;
		document.all("childFrm").src = "getVendorDeviceModel.jsp"+para;
	}
}
function getCityList(){
	if(!hasCityList){
		document.all("childFrm").src = "getCityList.jsp";
	}
	hasCityList = true;
}
//退出前重置父页面按钮
//window.onbeforeunload=resetOpener;
//-->
</SCRIPT>
</HEAD>

<BODY onload="init()" onunload="resetOpener()" >
<form name="form1">
	<input type="hidden" name="rowIndex" id="rowIndex" value=<%=rowIndex %> />
	<BR>
<TABLE bgcolor="#666666" width="75%" border=0 cellspacing="1"
	cellpadding="0" align="center">
	<TR>
		<TD colspan="12" class=column2>
		<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
			<TR>
				<TH colspan="2" align="center">编辑规则详情</TH>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<TR BGCOLOR=#ffffff>
		<TD width="118%" colspan="9" align=center class=column>
		<table width="95%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td>
					<select name="s1" onChange="setChange()">
						<option value="-1">--请选择--</option>
					</select>
				</td>
				<td id="severity1">
					<select name="s2">
						<option value="-1">--请选择--</option>
						<option value="=" selected>=</option>
					</select>
				</td>
				<td id="severity2" style="display: none">
					<select name="serv2">
						<option value="-1">--请选择--</option>
						<option value="=" selected>=</option>
						<option value=">=">>=</option>
						<option value=">">></option>
						<option value="&lt;=">&lt;=</option>
						<option value="&lt;">&lt;</option>
					</select>
				</td>
				<td id="x1" style="display: ">
					<input type=text name=t1>
				</td>
				<td id="x2" style="display: none"><%=strVendorList%></td>
				<td id="x3" style="display: none">
					<span id="strModelList"><%=strModelList%></span>
				</td>
				<td id="x4" style="display: none"><span id="strCityList"><%=strCityList%></span></td>
				<td id="x5" style="display: none">
					<select name="str_severity" size="1" id="str_severity">
						<option value="-1">--请选择--</option>
						<%
							Iterator levelMapIt = LEVEL_MAP.keySet().iterator();
							while (levelMapIt.hasNext())
							{
								String str = (String) levelMapIt.next();
						%>
						<option value="<%=str %>"><%=str%></option>
						<%
							}
						%>
					</select>
				</td>
				<td id="x6" style="display: none">
					<select name="creat_name" size="1" id="creat_name">
						<option value="-1">--请选择--</option>
						<%
							Iterator createEventMapIt = CREATE_EVENT_MAP.keySet().iterator();
							while (createEventMapIt.hasNext())
							{
								String str = (String) createEventMapIt.next();
						%>
						<option value="<%=str %>"><%=str%></option>
						<%
							}
						%>
					</select>
				</td>
				<td id="x7" style="display: none">
					<select name="eventno" size="1" id="eventno">
						<option value="-1">--请选择--</option>
						<%
							Iterator eventOidMapIt = EVENTOID_MAP.keySet().iterator();
							while (eventOidMapIt.hasNext())
							{
								String str = (String) eventOidMapIt.next();
						%>
						<option value="<%=str %>"><%=str%></option>
						<%
							}
						%>
					</select>
				</td>
				<td>
					<input type=button name=b1 class=jianbian value="确定" onClick="doAdd()">
				</td>
			</tr>
		</table>
		</TD>
	</TR>
	<TR BGCOLOR=#ffffff>
		<TD colspan="9" align=center class=column>
		<div id="" align=center
			style="width: 780px; height: 300px; overflow: auto" valign=top>
		<table width="100%" border=1 align="center" cellspacing=0
			bordercolorlight="#000000" bordercolordark="#FFFFFF" id="tab_alarm" >
			<tr align=center>
				<td>逻辑关系</td>
				<td>字段名</td>
				<td>条件</td>
				<td>值</td>
				<td>操作</td>
			</tr>
		</table>
		</div>
		<hr>
		<textarea name="t2" cols=100 rows=3 readonly><%=text%></textarea></TD>
	</TR>
	<TR BGCOLOR=#ffffff>
		<TD colspan="9" align=center class=column><input type=button
			name=B1 value="完 成" onclick="doOver()" class=jianbian> <input
			type=button name=B1 value="取 消" onclick="window.close()"
			class=jianbian></TD>
	</TR>
	<TR BGCOLOR=#ffffff>
		<TD colspan="9" class=column>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="2%">&nbsp;</td>
				<td colspan="2">说明：</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td width="2%">1</td>
				<td>设备IP：输入要过滤的IP地址</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>2</td>
				<td>设备型号：先要选择设备的厂商，然后要选择设备的型号</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>3</td>
				<td>设备名称：要过滤告警具体设备的名称</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>4</td>
				<td>事件类型：即产生告警的原因</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>5</td>
				<td>告警等级：就是要过滤告警的级别，如：一般告警，严重告警</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>6</td>
				<td>告警创建者：就是该告警是由那个程序去采集或发送的告警</td>
			</tr>
			<tr style="color=blue">
				<td>&nbsp;</td>
				<td>7</td>
				<td>请勿刷新该页面,不然将会关闭</td>
			</tr>
		</table>
		</TD>
	</TR>
	<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
</table>
<SCRIPT LANGUAGE="JavaScript">
<!--
<%=createRowStr %>
//-->
</SCRIPT>
</form>
</body>
</html>
