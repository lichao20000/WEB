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
 * WebTopoʵʱ�澯�Ƹ澯ģ������༭ҳ��
 * <li>REQ: GZDX-REQ-20080402-ZYX-001
 * <li>REQ: HBDX-REQ-20080220-WANGBING-001
 *
 * @author	�ι���
 * @version 1.0
 * @since	2008-4-8
 * @category	WebTopo/ʵʱ�澯��/�澯����
 * 
 */
	request.setCharacterEncoding("GBK");
	String rowIndex = request.getParameter("rowIndex");
	String text = "";
	text = request.getParameter("text");

	String strSQL = "";
	String strVendorList = "";
	String strModelList = "<select ><option>--����ѡ����--</option>";
	String strCityList = "<select ><option>--���Ե�--</option>";

	Cursor cursor = null;
	//��ȡ�������MAP
	//ConstantEventEnv obj = new ConstantEventEnv();
	//-------------------------------------
	// �澯�ȼ�
	int LEVEL = ConstantEventEnv.LEVEL;
	// �豸IP
	int IP = ConstantEventEnv.IP;
	// �豸����
	int DEVICE_NAME = ConstantEventEnv.DEVICE_NAME;
	// �¼�����
	int EVENT_OID = ConstantEventEnv.EVENT_OID;
	// �豸����
	int DEVICE_TYPE = ConstantEventEnv.DEVICE_TYPE;
	// ������
	int CREATOR_NAME = ConstantEventEnv.CREATOR_NAME;
	// ����
	int CITY = ConstantEventEnv.CITY;
	//
	
	//--------------------------------------
	// ��������Map
	Map RULE_TYPE_MAP = ConstantEventEnv.RULE_TYPE_MAP;
	// �澯�ȼ�Map,�� LEVEL ��Ӧ
	Map LEVEL_MAP = ConstantEventEnv.LEVEL_MAP;
	// �¼�����Map,�� EVENT_OID ��Ӧ
	Map EVENTOID_MAP = ConstantEventEnv.EVENTOID_MAP;
	// ������Map,�� CREATOR_NAME ��Ӧ
	Map CREATE_EVENT_MAP = ConstantEventEnv.CREATE_EVENT_MAP;
	// ������ϵMap,Ŀǰ�ݲ�ʹ��
	Map OPERATOR_MAP = ConstantEventEnv.OPERATOR_MAP;
	//--------------------------------------

	//�豸����
	strSQL = "select vendor_id,vendor_name from tab_vendor";
	cursor = DataSetBean.getCursor(strSQL);
	strVendorList = FormUtil.createListBox(cursor, "vendor_id",
			"vendor_name", true, "", "");
	//�豸�ͺ�
	//strSQL = "select  serial,(convert(varchar(10),serial)+'/'+device_name+'/'+os_version) as device_name from tab_devicetype_info where 1=1 ";
	//if (LipossGlobals.isOracle()
	//{
	//	strSQL = "select  serial, serial||'/'||device_name||'/'||os_version as device_name from tab_devicetype_info where 1=1 ";
	//}
	//cursor = DataSetBean.getCursor(strSQL);
	//strModelList = FormUtil.createListBox(cursor, "vendor_id","device_name", true, "", "device_model");
	//����
	//String city_id = curUser.getCityId();
	//SelectCityFilter CityFilter = new SelectCityFilter(request);
	//PrepareSQL pSQL = new PrepareSQL("select city_id,city_name,parent_id from tab_city where city_id in (?) order by city_id");
	//pSQL.setStringExt(1, CityFilter.getAllSubCityIds(city_id, true), false);
	//cursor = DataSetBean.getCursor(pSQL.getSQL());
	//strCityList = FormUtil.createListBox(cursor, "city_name",
	//		"city_name", false, city_id, "cityName");
	
	
	// ���༭����Ϣչʾ��ҳ��
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
<title>�༭��������</title>
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
		// name����Ϊ����,valueΪ��ʾ�ĺ�������
		// ����:<column name='1' value='�豸IP'></column>
%>
		<column name='<%=value %>' value='<%=key %>'></column>
		<%
			// myRuleTypeMap��key=���ִ���,valueΪ��������
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
// ҳ���ʼ��
function init()
{
	if((typeof(window.opener.initButton) == "object")||(typeof(window.opener.initButton) == "function")){
		if(document.form1.t2.value == ""){
			window.opener.initButton();
			isAdd = true;		
		}
	}else{
		alert("�༭�澯����ҳ���Ѿ��ر�,��ҳ�漴���ر�");
		window.close();
	}
	//��XML�ж���������������
	var aNode = root.selectNodes("//column");
	var oSel = document.form1.s1;
	oSel.length = 1;
	for(var i=0;i<aNode.length;i++)
	{
		oSel.add(createOption(aNode.item(i).getAttribute("value"),aNode.item(i).getAttribute("name")));
	}
}
// �������
function doAdd()
{
	var key=document.form1.s1.value;
	var logic=document.form1.s2.value;	
	var createKey = "";
    if(key=="-1"){
       alert("��ѡ���������!");
	   return true; 
	}
	else if(logic=="-1"){
       alert("��ѡ���������!");
	   return true; 
	}
	else if (key=="<%=IP %>"){
		createKey = "<%=myRuleTypeMap.get(IP) %>";
        if(document.form1.t1.value==""){
          alert("������"+createKey+"!");
		  return true; 
		}
		value=document.form1.t1.value;
	}
	else if(key=="<%=DEVICE_TYPE %>"){
		createKey = "<%=myRuleTypeMap.get(DEVICE_TYPE) %>";
        if(typeof(document.form1.device_model)=="undefined" || document.form1.device_model.value=="-1"){
          alert("��ѡ��"+createKey+"!");
		  return true; 
		}
		tmpObj = document.form1.device_model;
		tmpValue = tmpObj.options[tmpObj.selectedIndex].text;
		value=trim(tmpValue);
	}
	else if(key=="<%=DEVICE_NAME %>"){
		createKey = "<%=myRuleTypeMap.get(DEVICE_NAME) %>";
       if(document.form1.t1.value==""){
          alert("������"+createKey+"!");
		  return true; 
		}
		value=document.form1.t1.value;
	}
	else if(key=="<%=EVENT_OID %>"){
		createKey = "<%=myRuleTypeMap.get(EVENT_OID) %>";
        if(document.form1.eventno.value=="-1"){
          alert("��ѡ��"+createKey+"!");
		  return true; 
		}
		value=document.form1.eventno.value;
	}
	else if(key=="<%=LEVEL %>"){
		logic = document.form1.serv2.value;
		createKey = "<%=myRuleTypeMap.get(LEVEL) %>";
        if(document.form1.str_severity.value=="-1"){
          alert("��ѡ��"+createKey+"!");
		  return true; 
		}
		value=document.form1.str_severity.value; 
	}
	else if(key=="<%=CREATOR_NAME %>"){
        createKey = "<%=myRuleTypeMap.get(CREATOR_NAME) %>";
        if (document.form1.creat_name.value=="-1"){
          alert("��ѡ��"+createKey+"!");
		  return true; 
        }
        value=document.form1.creat_name.value;
	}
	else if(key=="<%=CITY %>"){
        createKey = "<%=myRuleTypeMap.get(CITY) %>";
        if (typeof(document.form1.cityName)=="undefined" || document.form1.cityName.value=="-1"){
          alert("������"+createKey+"!");
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
// ������
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
	oCell.innerHTML = "<a href=# onclick='delRow()'>ɾ��</a>";
}
// ɾ���м�¼
function delRow()
{
	if(!confirm("ȷ��ɾ��?"))
		return;
	var oTab = document.all("tab_alarm");
	var delRowIndex = event.srcElement.parentElement.parentElement.rowIndex;
 	oTab.deleteRow(delRowIndex);
 	resetText();
}
//���ù����ı����е�����
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
//�õ����ĵ�Ԫ��value
function getCellValue(oTab,rowIndex,cellIndex){
	try{
		return oTab.rows[rowIndex].cells[cellIndex].innerText;
	}catch(e){
		e.print();
	}
}
// ����������
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
// �ύ����
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
			alert("�༭�澯����ҳ���Ѿ��ر�,��ҳ�漴���ر�");
		}
	}
	window.close();
}

// �ر�ҳ��ʱ�¼�
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
//�����豸����
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
//�˳�ǰ���ø�ҳ�水ť
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
				<TH colspan="2" align="center">�༭��������</TH>
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
						<option value="-1">--��ѡ��--</option>
					</select>
				</td>
				<td id="severity1">
					<select name="s2">
						<option value="-1">--��ѡ��--</option>
						<option value="=" selected>=</option>
					</select>
				</td>
				<td id="severity2" style="display: none">
					<select name="serv2">
						<option value="-1">--��ѡ��--</option>
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
						<option value="-1">--��ѡ��--</option>
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
						<option value="-1">--��ѡ��--</option>
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
						<option value="-1">--��ѡ��--</option>
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
					<input type=button name=b1 class=jianbian value="ȷ��" onClick="doAdd()">
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
				<td>�߼���ϵ</td>
				<td>�ֶ���</td>
				<td>����</td>
				<td>ֵ</td>
				<td>����</td>
			</tr>
		</table>
		</div>
		<hr>
		<textarea name="t2" cols=100 rows=3 readonly><%=text%></textarea></TD>
	</TR>
	<TR BGCOLOR=#ffffff>
		<TD colspan="9" align=center class=column><input type=button
			name=B1 value="�� ��" onclick="doOver()" class=jianbian> <input
			type=button name=B1 value="ȡ ��" onclick="window.close()"
			class=jianbian></TD>
	</TR>
	<TR BGCOLOR=#ffffff>
		<TD colspan="9" class=column>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="2%">&nbsp;</td>
				<td colspan="2">˵����</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td width="2%">1</td>
				<td>�豸IP������Ҫ���˵�IP��ַ</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>2</td>
				<td>�豸�ͺţ���Ҫѡ���豸�ĳ��̣�Ȼ��Ҫѡ���豸���ͺ�</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>3</td>
				<td>�豸���ƣ�Ҫ���˸澯�����豸������</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>4</td>
				<td>�¼����ͣ��������澯��ԭ��</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>5</td>
				<td>�澯�ȼ�������Ҫ���˸澯�ļ����磺һ��澯�����ظ澯</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>6</td>
				<td>�澯�����ߣ����Ǹø澯�����Ǹ�����ȥ�ɼ����͵ĸ澯</td>
			</tr>
			<tr style="color=blue">
				<td>&nbsp;</td>
				<td>7</td>
				<td>����ˢ�¸�ҳ��,��Ȼ����ر�</td>
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
