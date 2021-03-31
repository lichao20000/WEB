<%@ page import="com.linkage.commons.db.DBUtil" %><%--
Author		: yanhj
Date		: 2006-10-24
Desc		: 
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<!--取得上一个页面提交的参数-->

<%
request.setCharacterEncoding("GBK");
String strSQL="",strVendorList="",strModelList="",strResourceList="";
Cursor cursor=null;
/*2006-3-2 shenkejian 修改*/
//设备厂商
strSQL = "select vendor_id,vendor_name from tab_vendor";
cursor = DataSetBean.getCursor(strSQL);
strVendorList = FormUtil.createListBox(cursor, "vendor_id", "vendor_name", true,"","");
//设备型号
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
//资源类型
strSQL = "select resource_type_id,resource_name from tab_resourcetype order by resource_type_id";
cursor = DataSetBean.getCursor(strSQL);
strResourceList = FormUtil.createListBox(cursor, "resource_type_id", "resource_name", false,"","");
	
%>
<HTML>
<HEAD>
<TITLE>编辑规则内容</TITLE>
<xml id=alarm>
<?xml version="1.0"?>
<root>
<column name='设备IP' value='sourceip'></column>
<column name='设备类型' value='devicetype'></column>
<column name='资源类型' value='resourcetype'></column>
<column name='事件编号' value='eventno'></column>
<column name='告警级别' value='severity'></column>
<column name='告警创建者' value='creat_name'></column>
<column name='告警设备名称' value='sourcename'></column>

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
    //shenkejian 2006-3-2 添加,修改
	var t1="";
    if(s1=="-1"){
       alert("请选择规则类型!");
	   return true; 
	}else if(s2=="-1"){
       alert("请选择规则条件!");
	   return true; 
	}else if (s1=="sourceip"){
        if(document.form1.t1.value==""){
          alert("请输入IP地址!");
		  return true; 
		}
	}else if(s1=="devicetype"){
        if(document.form1.device_model.value=="-1"){
          alert("请选择设备类型!");
		  return true; 
		}
	}else if(s1=="resourcetype"){
        if(document.form1.resource_type_id.value=="-1"){
          alert("请选择资料类型!");
		  return true; 
		}
	}else if(s1=="eventno"){
        if(document.form1.eventno.value=="-1"){
          alert("请选择事件编号!");
		  return true; 
		}
	}else if(s1=="severity"){
        if(document.form1.str_severity.value=="-1"){
          alert("请选择告警级别!");
		  return true; 
		}         
	}else if(s1=="creat_name"){
        if (document.form1.creat_name.value=="-1"){
          alert("请选择告警创建者!");
		  return true; 
        }
	}else if(s1=="sourcename"){
        if(document.form1.t1.value==""){
          alert("请输入告警设备的名称!");
		  return true; 
		}
	}else if(s3="-1"){
          alert("请选择逻辑关系!");
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
		oCell.innerHTML = "<a href=# onclick='doDelete("+node.item(i).getAttribute("index")+")'>删除</a>";
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
  oCell.innerHTML = "<a href=# onclick='doDelete("+para5+")'>删除</a>";
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
	str = str.replace(r1,"设备IP");
	str = str.replace(r2,"设备类型");
	str = str.replace(r3,"资源类型");
	str = str.replace(r4,"事件编号");
	str = str.replace(r5,"告警级别");
	str = str.replace(r6,"告警创建者");
	str = str.replace(r7,"告警设备名称");
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
	//shenkejian 2006-3-2 添加
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
            <TH colspan="2" align="center">编辑规则内容</TH>
          </TR>
        </TABLE></TD>
    </TR>
    <TR BGCOLOR=#ffffff > 
      <TD width="118%" colspan="9" align=center class=column><table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr> 
            <td> <select name=s1 onChange="setChange()">
                <option value="-1">--请选择--</option>
              </select> </td>
            <td> <select name=s2>
                <option value="-1">--请选择--</option>
              </select> </td>
            <td id="x1" style="display:"> <input type=text name=t1> </td>
            <td id="x2" style="display:none"><%=strVendorList%></td>
            <td id="x3" style="display:none"><span id=strModelList><%=strModelList%></span></td>
            <td id="x4" style="display:none"><%=strResourceList%></td>
            <td id="x5" style="display:none"> <select name="str_severity" size="1" id="str_severity">
                <option value="-1">--请选择--</option>
                <option value="清除告警">清除告警</option>
                <option value="事件告警">事件告警</option>
                <option value="警告告警">警告告警</option>
                <option value="次要告警">次要告警</option>
                <option value="主要告警">主要告警</option>
                <option value="严重告警">严重告警</option>
                <!-- <option value="不确定告警">不确定告警</option> -->
              </select></td>
            <td id="x6" style="display:none">
				<select name="creat_name" size="1" id="creat_name">
                	<option value="-1">--请选择--</option>
					<option value="Trap">Trap</option>
					<option value="Syslog">Syslog</option>
					<option value="性能采集系统">性能采集系统</option>
					<option value="流量采集系统">流量采集系统</option>
					<option value="网络拓扑管理">网络拓扑管理</option>
					<option value="业务模拟系统">业务模拟系统</option>
					<option value="Ping检测">Ping检测</option>
					<option value="华为设备端口检测">华为设备端口检测</option>
					<option value="华为设备端口检测">主机系统</option>
					<option value="华为设备端口检测">ARP检测系统</option>
					<option value="VPN检测系统">VPN检测系统</option>
              </select>
			</td>
            <td id="x7" style="display:none"> <select name="eventno" size="1" id="eventno">
                <option value="-1">--请选择--</option>
                <option value="设备不可达">设备不可达</option>
                <option value="设备可达">设备可达</option>
                <option value="设备增加">设备增加</option>
                <option value="端口Down">端口Down</option>
                <option value="端口Up">端口Up</option>
                <option value="端口增加">端口增加</option>
                <option value="端口减少">端口减少</option>
                <option value="snmp采集请求超时">snmp采集请求超时</option>
                <option value="snmp采集请求出现未知错误">snmp采集请求出现未知错误</option>
                <option value="设备不支持此MIB对象">设备不支持此MIB对象</option>
                <option value="设备告警">设备告警</option>
                <option value="端口告警">端口告警</option>
                <option value="其他类型Trap告警">其他类型Trap告警</option>
                <option value="越界阈值告警">越界阈值告警</option>
                <option value="恢复阈值告警">恢复阈值告警</option>
                <option value="邮件发送失败">邮件发送失败</option>
                <option value="邮件发送成功">邮件发送成功</option>
                <option value="邮件接收失败">邮件接收失败</option>
                <option value="邮件接收成功">邮件接收成功</option>
                <option value="Dns解析失败">Dns解析失败</option>
                <option value="Dns解析成功">Dns解析成功</option>
                <option value="Radius认证失败">Radius认证失败</option>
                <option value="Radius认证成功">Radius认证成功</option>
                <option value="进程停止运行">进程停止运行</option>
                <option value="进程恢复运行">进程恢复运行</option>
                <option value="LAN用户线路不可达">LAN用户线路不可达</option>
                <option value="LAN用户线路可达">LAN用户线路可达</option>
                <option value="VPN用户线路不可达">VPN用户线路不可达</option>
                <option value="VPN用户线路不可达">VPN用户线路不可达</option>
              </select> </td>
            <td> <select name=s3>
                <option value="or" selected>or</option>
                <option value="and">and</option>
              </select> </td>
            <td> <input type=button name=b1 class=jianbian value="确定" onClick="doAdd()"> 
            </td>
          </tr>
        </table></TD>
    </TR>
    <TR BGCOLOR=#ffffff> 
      <TD colspan="9" align=center class=column> <div id="" align=center style="width:780px;height:300px;overflow:auto" valign=top>	
          <table width="100%" border=1 align="center" cellspacing=0 bordercolorlight="#000000" bordercolordark="#FFFFFF" id=tab_alarm>
            <tr align=center> 
              <td >逻辑关系</td>
              <td >字段名</td>
              <td >条件</td>
              <td >值</td>
              <td >操作</td>
            </tr>
          </table>
        </div>
        <hr> <textarea name=t2 cols=100 rows=3></textarea> 
      </TD>
    </TR>
    <TR BGCOLOR=#ffffff> 
      <TD colspan="9" align=center class=column> <input type=button name=B1 value="完 成" onclick="doOver()" class=jianbian> 
        <input type=button name=B1 value="取 消" onclick="window.close()" class=jianbian> 
      </TD>
    </TR>
    <TR BGCOLOR=#ffffff>
      <TD colspan="9" class=column><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td width="2%">&nbsp;</td>
            <td colspan="2">说明：</td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td width="2%">1、</td>
            <td>设备IP：输入要过滤的IP地址</td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>2、</td>
            <td>设备类型：先要选择设备的厂商，然后要选择设备的型号</td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>3、</td>
            <td>资源类型：要求选择具体的资源类型，如：核心层--出口路由器</td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>4、</td>
            <td>事件编号：即产生告警的原因</td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>5、</td>
            <td>告警级别：就是要过滤告警的级别，如：一般告警，严重告警</td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>6、</td>
            <td>告警创建者：就是该告警是由那个程序去采集或发送的告警</td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>7、</td>
            <td>告警设备名称：要过滤告警具体设备的名称</td>
          </tr>
        </table></TD>
    </TR>
    <IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
  </table>
</form>
</body>
</html>
