<%@ page import="com.linkage.commons.db.DBUtil" %><%--
Author		: yanhj
Date		: 2006-10-24
Desc		: 
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<!--ȡ����һ��ҳ���ύ�Ĳ���-->

<%
request.setCharacterEncoding("GBK");
String strSQL="",strVendorList="",strModelList="",strResourceList="";
Cursor cursor=null;
/*2006-3-2 shenkejian �޸�*/
//�豸����
strSQL = "select vendor_id,vendor_name from tab_vendor";
cursor = DataSetBean.getCursor(strSQL);
strVendorList = FormUtil.createListBox(cursor, "vendor_id", "vendor_name", true,"","");
//�豸�ͺ�
//strSQL = "select distinct vendor_id,device_name from tab_devicetype_info";
strSQL = "select  serial,(device_name+\'/\'+os_version) as device_name from tab_devicetype_info where 1=1 ";
if(LipossGlobals.isOracle())
{
	strSQL = "select  serial,CONCAT(CONCAT(device_name,\'/\'),os_version) as device_name  from tab_devicetype_info where 1=1 ";
}

// teledb
if (DBUtil.GetDB() == 3) {
    strSQL = "select  serial,CONCAT(device_name, \'/\', os_version) as device_name  from tab_devicetype_info where 1=1 ";
}
cursor = DataSetBean.getCursor(strSQL);
strModelList = FormUtil.createListBox(cursor, "vendor_id","device_name",true,"","device_model");
//��Դ����
strSQL = "select resource_type_id,resource_name from tab_resourcetype order by resource_type_id";
cursor = DataSetBean.getCursor(strSQL);
strResourceList = FormUtil.createListBox(cursor, "resource_type_id", "resource_name", false,"","");
	
%>
<HTML>
<HEAD>
<TITLE>�༭��������</TITLE>
<xml id=alarm>
<?xml version="1.0"?>
<root>
<column name='�豸IP' value='sourceip'></column>
<column name='�豸����' value='devicetype'></column>
<column name='��Դ����' value='resourcetype'></column>
<column name='�¼����' value='eventno'></column>
<column name='�澯����' value='severity'></column>
<column name='�澯������' value='creat_name'></column>
<column name='�澯�豸����' value='sourcename'></column>

<columnValue name='sourceip' value='=' text='='></columnValue>
<columnValue name='sourceip' value='!=' text='!='></columnValue>
<columnValue name='sourceip' value='>' text='>'></columnValue>
<columnValue name='sourceip' value='>=' text='>='></columnValue>
<columnValue name='sourceip' value='&lt;' text='&lt;'></columnValue>
<columnValue name='sourceip' value='&lt;=' text='&lt;='></columnValue>
 

<columnValue name='devicetype' value='=' text='='></columnValue>
<columnValue name='devicetype' value='!=' text='!='></columnValue>
 

<columnValue name='resourcetype' value='=' text='='></columnValue>
<columnValue name='resourcetype' value='!=' text='!='></columnValue>
 

<columnValue name='eventno' value='=' text='='></columnValue>
<columnValue name='eventno' value='!=' text='!='></columnValue>
 

<columnValue name='severity' value='=' text='='></columnValue>
<columnValue name='severity' value='!=' text='!='></columnValue>
<columnValue name='severity' value='>' text='>'></columnValue>
<columnValue name='severity' value='>=' text='>='></columnValue>
<columnValue name='severity' value='&lt;' text='&lt;'></columnValue>
<columnValue name='severity' value='&lt;=' text='&lt;='></columnValue>
 

<columnValue name='creat_name' value='=' text='='></columnValue>
<columnValue name='creat_name' value='!=' text='!='></columnValue>
 

<columnValue name='sourcename' value='=' text='='></columnValue>
<columnValue name='sourcename' value='!=' text='!='></columnValue>
 
</root>
</xml>
<META content="text/html;charset=gb2312" http-equiv="Content-Type">
<link rel="stylesheet" href="../css/css_blue.css" type="text/css">
<link rel="stylesheet" href="../css/tree.css" type="text/css">
<link rel="stylesheet" href="../css/tab.css" type="text/css">
<SCRIPT LANGUAGE="JavaScript" src="../Js/MyCalendar.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" src="../Js/LayWriter.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
var object = window.dialogArguments;
var root = alarm.documentElement;
var index = 1;

function init()
{
	var aNode = root.selectNodes("//column");
	var oSel = document.form1.s1;
	oSel.length = 1;
	for(var i=0;i<aNode.length;i++)
	{
		oSel.add(createOption(aNode.item(i).getAttribute("name"),aNode.item(i).getAttribute("value")));
	}
}

function doAdd()
{
	var oTab = document.all('tab_alarm');
	var s1=document.form1.s1.value;
	var s2=document.form1.s2.value;	
	var s3=document.form1.s3.value;	
    //shenkejian 2006-3-2 ���,�޸�
	var t1="";
    if(s1=="-1"){
       alert("��ѡ���������!");
	   return true; 
	}else if(s2=="-1"){
       alert("��ѡ���������!");
	   return true; 
	}else if (s1=="sourceip"){
        if(document.form1.t1.value==""){
          alert("������IP��ַ!");
		  return true; 
		}
	}else if(s1=="devicetype"){
        if(document.form1.device_model.value=="-1"){
          alert("��ѡ���豸����!");
		  return true; 
		}
	}else if(s1=="resourcetype"){
        if(document.form1.resource_type_id.value=="-1"){
          alert("��ѡ����������!");
		  return true; 
		}
	}else if(s1=="eventno"){
        if(document.form1.eventno.value=="-1"){
          alert("��ѡ���¼����!");
		  return true; 
		}
	}else if(s1=="severity"){
        if(document.form1.str_severity.value=="-1"){
          alert("��ѡ��澯����!");
		  return true; 
		}         
	}else if(s1=="creat_name"){
        if (document.form1.creat_name.value=="-1"){
          alert("��ѡ��澯������!");
		  return true; 
        }
	}else if(s1=="sourcename"){
        if(document.form1.t1.value==""){
          alert("������澯�豸������!");
		  return true; 
		}
	}else if(s3="-1"){
          alert("��ѡ���߼���ϵ!");
		  return true; 
	}
    if(s1=="sourceip"){
        t1=document.form1.t1.value;
	}else if(s1=="devicetype"){
		//modify by hmc
		tmpObj = document.form1.device_model;
		tmpValue = tmpObj.options[tmpObj.selectedIndex].text;
		t1=trim(tmpValue);
	}else if(s1=="resourcetype"){
		tmpObj = document.form1.resource_type_id;
		tmpValue = tmpObj.options[tmpObj.selectedIndex].text;
		t1=trim(tmpValue);
	}else if(s1=="eventno"){
        t1=document.form1.eventno.value;
	}else if(s1=="severity"){
        t1=document.form1.str_severity.value;
	}else if(s1=="creat_name"){
        t1=document.form1.creat_name.value;
	}else if(s1=="sourcename"){
       t1=document.form1.t1.value;
	}
	createRow(oTab,s3,s1,s2,t1,index);
	var tNode = alarm.createNode(1,"alarmStr","");
	tNode.setAttribute("fieldname",s1);
	tNode.setAttribute("condition",s2);
	tNode.setAttribute("value",t1);
	tNode.setAttribute("logic",s3);
	tNode.setAttribute("index",index);
	alarm.documentElement.insertBefore(tNode,null);
	index++;
	var aNode = root.selectNodes("//alarmStr");
	pingStr();
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
function showInfo(node)
{
	clearCell('tab_alarm');
	var tabobj = document.all('tab_alarm');
	for(var i=0;i<node.length;i++)
	{
		var oRow = tabobj.insertRow();
		//oRow.bgColor = "#FFFFFF";
		oRow.align = "center";
		var oCell = oRow.insertCell();
		oCell.innerText = node.item(i).getAttribute("logic");
		oCell = oRow.insertCell();
		oCell.innerText = node.item(i).getAttribute("fieldname");
		oCell = oRow.insertCell();
		oCell.innerText = node.item(i).getAttribute("condition");
		oCell = oRow.insertCell();
		oCell.innerText = node.item(i).getAttribute("value");
		oCell = oRow.insertCell();
		oCell.innerHTML = "<a href=# onclick='doDelete("+node.item(i).getAttribute("index")+")'>ɾ��</a>";
	}
}

function createRow(oTab,para1,para2,para3,para4,para5)
{   
  oRow = oTab.insertRow();
  //oRow.bgColor = "#FFFFFF";
  oRow.align = "center";
  var oCell = oRow.insertCell();
  oCell.innerHTML = para1;
  oCell = oRow.insertCell();
  oCell.innerText = para2;
  oCell = oRow.insertCell();
  oCell.innerText = para3;
  oCell = oRow.insertCell();
  oCell.innerHTML = para4;
  oCell = oRow.insertCell();
  oCell.innerHTML = "<a href=# onclick='doDelete("+para5+")'>ɾ��</a>";
}

function doDelete(para)
{
	var node =  root.selectNodes("//alarmStr[@index="+para+"]");
	if(node!=null)
	node.removeAll();
	pingStr();
	var aNode = root.selectNodes("//alarmStr");
	showInfo(aNode);
}

function clearCell(tablename)
{
	var tabobj = document.all(tablename);
	while(tabobj.rows.length>1)
	tabobj.deleteRow(1);
}

function pingStr()
{
	var tempstr = '';
	var str = '';
	var aNode = root.selectNodes("//alarmStr[@fieldname='sourceip'&&@logic='or']");
	if(aNode.length>0)
	{
	for(var i=0;i<aNode.length;i++)
		tempstr += ' or ^sourceip^'+aNode.item(i).getAttribute("condition")+'^'+aNode.item(i).getAttribute("value")+'^';
	str += '(' +tempstr.substring(4,tempstr.length)+ ')';
	}
	aNode = root.selectNodes("//alarmStr[@fieldname='sourceip'&&@logic='and']");
	tempstr = '';
	if(aNode.length>0)
	{
	if(str != '')
	str += ' and ';
	for(var i=0;i<aNode.length;i++)
		tempstr += ' and (^sourceip^'+aNode.item(i).getAttribute("condition")+'^'+aNode.item(i).getAttribute("value")+'^)';
	str += tempstr.substring(5,tempstr.length);
	}

	aNode = root.selectNodes("//alarmStr[@fieldname='devicetype'&&@logic='or']");
	tempstr = '';
	if(aNode.length>0)
	{
	for(var i=0;i<aNode.length;i++)
		tempstr += ' or ^devicetype^'+aNode.item(i).getAttribute("condition")+'^'+aNode.item(i).getAttribute("value")+'^';
	str += '(' +tempstr.substring(4,tempstr.length)+ ')';
	}
	aNode = root.selectNodes("//alarmStr[@fieldname='devicetype'&&@logic='and']");
	tempstr = '';
	if(aNode.length>0)
	{
	if(str != '')
	str += ' and ';
	for(var i=0;i<aNode.length;i++)
		tempstr += ' and (^devicetype^'+aNode.item(i).getAttribute("condition")+'^'+aNode.item(i).getAttribute("value")+'^)';
	str += tempstr.substring(5,tempstr.length);
	}

	aNode = root.selectNodes("//alarmStr[@fieldname='resourcetype'&&@logic='or']");
	tempstr = '';
	if(aNode.length>0)
	{
	for(var i=0;i<aNode.length;i++)
		tempstr += ' or ^resourcetype^'+aNode.item(i).getAttribute("condition")+'^'+aNode.item(i).getAttribute("value")+'^';
	str += '(' +tempstr.substring(4,tempstr.length)+ ')';
	}
	aNode = root.selectNodes("//alarmStr[@fieldname='resourcetype'&&@logic='and']");
	tempstr = '';
	if(aNode.length>0)
	{
	if(str != '')
	str += ' and ';
	for(var i=0;i<aNode.length;i++)
		tempstr += ' and (^resourcetype^'+aNode.item(i).getAttribute("condition")+'^'+aNode.item(i).getAttribute("value")+'^)';
	str += tempstr.substring(5,tempstr.length);
	}

	aNode = root.selectNodes("//alarmStr[@fieldname='eventno'&&@logic='or']");
	tempstr = '';
	if(aNode.length>0)
	{
	for(var i=0;i<aNode.length;i++)
		tempstr += ' or ^eventno^'+aNode.item(i).getAttribute("condition")+'^'+aNode.item(i).getAttribute("value")+'^';
	str += '(' +tempstr.substring(4,tempstr.length)+ ')';
	}
	aNode = root.selectNodes("//alarmStr[@fieldname='eventno'&&@logic='and']");
	tempstr = '';
	if(aNode.length>0)
	{
	if(str != '')
	str += ' and ';
	for(var i=0;i<aNode.length;i++)
		tempstr += ' and (^eventno^'+aNode.item(i).getAttribute("condition")+'^'+aNode.item(i).getAttribute("value")+'^)';
	str += tempstr.substring(5,tempstr.length);
	}

	aNode = root.selectNodes("//alarmStr[@fieldname='severity'&&@logic='or']");
	tempstr = '';
	if(aNode.length>0)
	{
	if(str != '')
	str += ' and ';
	for(var i=0;i<aNode.length;i++)	
		tempstr += ' or ^severity^'+aNode.item(i).getAttribute("condition")+'^'+aNode.item(i).getAttribute("value")+'^';
	str += '(' +tempstr.substring(4,tempstr.length)+ ')';
	}
	aNode = root.selectNodes("//alarmStr[@fieldname='severity'&&@logic='and']");
	tempstr = '';
	if(aNode.length>0)
	{
	if(str != '')
	str += ' and ';
	for(var i=0;i<aNode.length;i++)
		tempstr += ' and (^severity^'+aNode.item(i).getAttribute("condition")+'^'+aNode.item(i).getAttribute("value")+'^)';
	str += tempstr.substring(5,tempstr.length);
	}

	aNode = root.selectNodes("//alarmStr[@fieldname='creat_name'&&@logic='or']");
	tempstr = '';
	if(aNode.length>0)
	{
	if(str != '')
	str += ' and ';
	for(var i=0;i<aNode.length;i++)	
		tempstr += ' or ^creat_name^'+aNode.item(i).getAttribute("condition")+'^'+aNode.item(i).getAttribute("value")+'^';
	str += '(' +tempstr.substring(4,tempstr.length)+ ')';
	}
	aNode = root.selectNodes("//alarmStr[@fieldname='creat_name'&&@logic='and']");
	tempstr = '';
	if(aNode.length>0)
	{
	if(str != '')
	str += ' and ';
	for(var i=0;i<aNode.length;i++)
		tempstr += ' and (^creat_name^'+aNode.item(i).getAttribute("condition")+'^'+aNode.item(i).getAttribute("value")+'^)';
	str += tempstr.substring(5,tempstr.length);
	}

	aNode = root.selectNodes("//alarmStr[@fieldname='sourcename'&&@logic='or']");
	tempstr = '';
	if(aNode.length>0)
	{
	if(str != '')
	str += ' and ';
	for(var i=0;i<aNode.length;i++)	
		tempstr += ' or ^sourcename^'+aNode.item(i).getAttribute("condition")+'^'+aNode.item(i).getAttribute("value")+'^';
	str += '(' +tempstr.substring(4,tempstr.length)+ ')';
	}
	aNode = root.selectNodes("//alarmStr[@fieldname='sourcename'&&@logic='and']");
	tempstr  = '';
	if(aNode.length>0)
	{
	if(str != '')
	str += ' and ';
	for(var i=0;i<aNode.length;i++)
		tempstr += ' and (^sourcename^'+aNode.item(i).getAttribute("condition")+'^'+aNode.item(i).getAttribute("value")+'^)';
	str += tempstr.substring(5,tempstr.length);
	}
	
	var r1 = /sourceip/g;
	var r2 = /devicetype/g;
	var r3 = /resourcetype/g;
	var r4 = /eventno/g;
	var r5 = /severity/g;
	var r6 = /creat_name/g;
	var r7 = /sourcename/g;
	str = str.replace(r1,"�豸IP");
	str = str.replace(r2,"�豸����");
	str = str.replace(r3,"��Դ����");
	str = str.replace(r4,"�¼����");
	str = str.replace(r5,"�澯����");
	str = str.replace(r6,"�澯������");
	str = str.replace(r7,"�澯�豸����");
	document.form1.t2.value = str;

}

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
	var oSel = document.form1.s2;
	oSel.length = 1;
	var bNode = root.selectNodes("//columnValue[@name='"+sValue+"']");
	if(bNode!=null)
	for(var i=0;i<bNode.length;i++)
	{
		oSel.add(createOption(bNode.item(i).getAttribute("text"),bNode.item(i).getAttribute("value")));
	}
	//shenkejian 2006-3-2 ���
    if(sValue=="sourceip"){
	  x1.style.display="";
	  x2.style.display="none";
	  x3.style.display="none";
	  x4.style.display="none";
	  x5.style.display="none";
	  x6.style.display="none";
	  x7.style.display="none";
	}else if(sValue=="devicetype"){
	  x1.style.display="none";
	  x2.style.display="";
	  x3.style.display="";
	  x4.style.display="none";
	  x5.style.display="none";
	  x6.style.display="none";
	  x7.style.display="none";
	}else if(sValue=="resourcetype"){
	  x1.style.display="none";
	  x2.style.display="none";
	  x3.style.display="none";
	  x4.style.display="";
	  x5.style.display="none";
	  x6.style.display="none";
	  x7.style.display="none";
	}else if(sValue=="eventno"){
	  x1.style.display="none";
	  x2.style.display="none";
	  x3.style.display="none";
	  x4.style.display="none";
	  x5.style.display="none";
	  x6.style.display="none";
	  x7.style.display="";
	}else if(sValue=="severity"){
	  x1.style.display="none";
	  x2.style.display="none";
	  x3.style.display="none";
	  x4.style.display="none";
	  x5.style.display="";
	  x6.style.display="none";
	  x7.style.display="none";
	}else if(sValue=="creat_name"){
	  x1.style.display="none";
	  x2.style.display="none";
	  x3.style.display="none";
	  x4.style.display="none";
	  x5.style.display="none";
	  x6.style.display="";
	  x7.style.display="none";
	}else if(sValue=="sourcename"){
	  x1.style.display="";
	  x2.style.display="none";
	  x3.style.display="none";
	  x4.style.display="none";
	  x5.style.display="none";
	  x6.style.display="none";
	  x7.style.display="none";
	}
}

function doOver()
{
	//Object.document.frm.ruler_content.value = document.form1.t2.value;
	window.returnValue=document.form1.t2.value;
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
//-->
</SCRIPT>
</HEAD>

<BODY onload="init()">
	<form name="form1" >
	<BR>  	
  <TABLE bgcolor="#666666" width="75%" border=0 cellspacing="1" cellpadding="0" align="center">
    <TR> 
      <TD colspan="12" class=column2> <TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
          <TR> 
            <TH colspan="2" align="center">�༭��������</TH>
          </TR>
        </TABLE></TD>
    </TR>
    <TR BGCOLOR=#ffffff > 
      <TD width="118%" colspan="9" align=center class=column><table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr> 
            <td> <select name=s1 onChange="setChange()">
                <option value="-1">--��ѡ��--</option>
              </select> </td>
            <td> <select name=s2>
                <option value="-1">--��ѡ��--</option>
              </select> </td>
            <td id="x1" style="display:"> <input type=text name=t1> </td>
            <td id="x2" style="display:none"><%=strVendorList%></td>
            <td id="x3" style="display:none"><span id=strModelList><%=strModelList%></span></td>
            <td id="x4" style="display:none"><%=strResourceList%></td>
            <td id="x5" style="display:none"> <select name="str_severity" size="1" id="str_severity">
                <option value="-1">--��ѡ��--</option>
                <option value="����澯">����澯</option>
                <option value="�¼��澯">�¼��澯</option>
                <option value="����澯">����澯</option>
                <option value="��Ҫ�澯">��Ҫ�澯</option>
                <option value="��Ҫ�澯">��Ҫ�澯</option>
                <option value="���ظ澯">���ظ澯</option>
                <!-- <option value="��ȷ���澯">��ȷ���澯</option> -->
              </select></td>
            <td id="x6" style="display:none">
				<select name="creat_name" size="1" id="creat_name">
                	<option value="-1">--��ѡ��--</option>
					<option value="Trap">Trap</option>
					<option value="Syslog">Syslog</option>
					<option value="���ܲɼ�ϵͳ">���ܲɼ�ϵͳ</option>
					<option value="�����ɼ�ϵͳ">�����ɼ�ϵͳ</option>
					<option value="�������˹���">�������˹���</option>
					<option value="ҵ��ģ��ϵͳ">ҵ��ģ��ϵͳ</option>
					<option value="Ping���">Ping���</option>
					<option value="��Ϊ�豸�˿ڼ��">��Ϊ�豸�˿ڼ��</option>
					<option value="��Ϊ�豸�˿ڼ��">����ϵͳ</option>
					<option value="��Ϊ�豸�˿ڼ��">ARP���ϵͳ</option>
					<option value="VPN���ϵͳ">VPN���ϵͳ</option>
              </select>
			</td>
            <td id="x7" style="display:none"> <select name="eventno" size="1" id="eventno">
                <option value="-1">--��ѡ��--</option>
                <option value="�豸���ɴ�">�豸���ɴ�</option>
                <option value="�豸�ɴ�">�豸�ɴ�</option>
                <option value="�豸����">�豸����</option>
                <option value="�˿�Down">�˿�Down</option>
                <option value="�˿�Up">�˿�Up</option>
                <option value="�˿�����">�˿�����</option>
                <option value="�˿ڼ���">�˿ڼ���</option>
                <option value="snmp�ɼ�����ʱ">snmp�ɼ�����ʱ</option>
                <option value="snmp�ɼ��������δ֪����">snmp�ɼ��������δ֪����</option>
                <option value="�豸��֧�ִ�MIB����">�豸��֧�ִ�MIB����</option>
                <option value="�豸�澯">�豸�澯</option>
                <option value="�˿ڸ澯">�˿ڸ澯</option>
                <option value="��������Trap�澯">��������Trap�澯</option>
                <option value="Խ����ֵ�澯">Խ����ֵ�澯</option>
                <option value="�ָ���ֵ�澯">�ָ���ֵ�澯</option>
                <option value="�ʼ�����ʧ��">�ʼ�����ʧ��</option>
                <option value="�ʼ����ͳɹ�">�ʼ����ͳɹ�</option>
                <option value="�ʼ�����ʧ��">�ʼ�����ʧ��</option>
                <option value="�ʼ����ճɹ�">�ʼ����ճɹ�</option>
                <option value="Dns����ʧ��">Dns����ʧ��</option>
                <option value="Dns�����ɹ�">Dns�����ɹ�</option>
                <option value="Radius��֤ʧ��">Radius��֤ʧ��</option>
                <option value="Radius��֤�ɹ�">Radius��֤�ɹ�</option>
                <option value="����ֹͣ����">����ֹͣ����</option>
                <option value="���ָ̻�����">���ָ̻�����</option>
                <option value="LAN�û���·���ɴ�">LAN�û���·���ɴ�</option>
                <option value="LAN�û���·�ɴ�">LAN�û���·�ɴ�</option>
                <option value="VPN�û���·���ɴ�">VPN�û���·���ɴ�</option>
                <option value="VPN�û���·���ɴ�">VPN�û���·���ɴ�</option>
              </select> </td>
            <td> <select name=s3>
                <option value="or" selected>or</option>
                <option value="and">and</option>
              </select> </td>
            <td> <input type=button name=b1 class=jianbian value="ȷ��" onClick="doAdd()"> 
            </td>
          </tr>
        </table></TD>
    </TR>
    <TR BGCOLOR=#ffffff> 
      <TD colspan="9" align=center class=column> <div id="" align=center style="width:780px;height:300px;overflow:auto" valign=top>	
          <table width="100%" border=1 align="center" cellspacing=0 bordercolorlight="#000000" bordercolordark="#FFFFFF" id=tab_alarm>
            <tr align=center> 
              <td >�߼���ϵ</td>
              <td >�ֶ���</td>
              <td >����</td>
              <td >ֵ</td>
              <td >����</td>
            </tr>
          </table>
        </div>
        <hr> <textarea name=t2 cols=100 rows=3></textarea> 
      </TD>
    </TR>
    <TR BGCOLOR=#ffffff> 
      <TD colspan="9" align=center class=column> <input type=button name=B1 value="�� ��" onclick="doOver()" class=jianbian> 
        <input type=button name=B1 value="ȡ ��" onclick="window.close()" class=jianbian> 
      </TD>
    </TR>
    <TR BGCOLOR=#ffffff>
      <TD colspan="9" class=column><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td width="2%">&nbsp;</td>
            <td colspan="2">˵����</td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td width="2%">1��</td>
            <td>�豸IP������Ҫ���˵�IP��ַ</td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>2��</td>
            <td>�豸���ͣ���Ҫѡ���豸�ĳ��̣�Ȼ��Ҫѡ���豸���ͺ�</td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>3��</td>
            <td>��Դ���ͣ�Ҫ��ѡ��������Դ���ͣ��磺���Ĳ�--����·����</td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>4��</td>
            <td>�¼���ţ��������澯��ԭ��</td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>5��</td>
            <td>�澯���𣺾���Ҫ���˸澯�ļ����磺һ��澯�����ظ澯</td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>6��</td>
            <td>�澯�����ߣ����Ǹø澯�����Ǹ�����ȥ�ɼ����͵ĸ澯</td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>7��</td>
            <td>�澯�豸���ƣ�Ҫ���˸澯�����豸������</td>
          </tr>
        </table></TD>
    </TR>
    <IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
  </table>
</form>
</body>
</html>
