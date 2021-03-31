<%--
Note		: 配置管理-备份设备信息查询
Date		: 2007-04-26
Author		: maym
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import ="com.linkage.liposs.manaconf.*,com.linkage.litms.common.filter.*"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%
request.setCharacterEncoding("GBK");

//调用接口返回添加设备处理后的信息
ManaConfig mc = new ManaConfig(request);
ArrayList list = mc.getDeviceList();
String msg = null;
Map fields = null;
Cursor cursor = null;
String strBar = null;
//add by maym 2007-12-27--------------------------------------------------------
String city_id = request.getParameter("city_id");
String resource_type_id = request.getParameter("resource_type_id");

if ((city_id == null) || (city_id.equals("-1")))
{
   city_id = curUser.getCityId();
}

if ((resource_type_id == null) || (resource_type_id.equals("-1")))
{
   resource_type_id = "";
}
String strResourceList = null;
strResourceList = DeviceAct.getResourceTypeList(true, resource_type_id , "");
SelectCityFilter City = new SelectCityFilter(request);
String city_name = City.getNameByCity_id(city_id);
String strCityList = City.getSelfAndNextLayer(true, city_id, "");
//-------------------------------------------------------------------------------
if (list.size() == 1)
{
   msg = (String)list.get(0);
}
else
{
  strBar = (String)list.get(0);
  cursor =  (Cursor)list.get(1);
  fields = cursor.getNext();
}
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">

function GoList(){
   this.location = "deviceList.jsp";
}

//-----------------立即备份反馈信息等待--------------------
var now = new Date();
function updateDeviceList()
{
   var city_id_value  = document.all("hid_city_id").value;
   var node = document.getElementById("resource_type_id");
   var resource_type_id_value = node.options[node.selectedIndex].value;
   
   var url = "updateDeviceList.jsp"
   var pars = "city_id=" + city_id_value
            + "&resource_type_id=" + resource_type_id_value
            + "&now="+now.getTime();
   
   var myAjax
		= new Ajax.Request(
							url,
							{method:"post",parameters:pars,onSuccess:showDevice,onLoading:showLoading}						
						   );
}
function showDevice(req)
{
    document.all("bk_devicelist").innerHTML = req.responseText;
}
function showLoading()
{
       var load = "<table><tr><td height=100></td></tr></table>"
            + "<TABLE  width=\"50%\" border=0 cellspacing=0 cellpadding=0 align=center>"
            + "<tr><td bgcolor=\"#000000\">"
            + "<TABLE  width=\"100%\" border=0 cellspacing=1 cellpadding=0>"
            + "<tr><td class=column1 height=50 align=center>正在更新设备列表.....请稍候！</td></tr>"
            + "</TABLE></td></tr></table>";
       document.all("bk_devicelist").innerHTML = load;     
}

function PromptBackup(device_id)
{
	var sure = window.confirm("是否立即备份？");
	if(!sure)
	{
	return;
	}
     document.getElementById("wait").style.display="";
   var url = getURL(device_id);
   xmlHttpReuqest = getXMLHttpRequest();
   send(xmlHttpReuqest,url);
}
//获得XMLHttpRequest对象
function getXMLHttpRequest()
{
    var httpReq = false;
    
    if (window.XMLHttpRequest)
    {
        httpReq = new XMLHttpRequest();
        if (httpReq.overrideMimeType)
        {
          httpReq.overrideMimeType("text/xml");
        }
    }
    else if (window.ActiveXObject)
    {
        try
        {
           httpReq = new ActiveXObject("Msxml2.XMLHTTP");
        }
        catch(e)
        {
           try
           {
              httpReq = new ActiveXObject("Microsoft.XMLHTTP");
           }
           catch(e)
           {
           }
        }
    }  
    return httpReq;
}
//发送请求
function send(httpRequest,url)
{
    if(!httpRequest)
    {
       alert("不能创建XMLHttpRequest对象！");
       return false;
    }
    
    httpRequest.onreadystatechange = processRequest;
    httpRequest.open("GET",url,true);
    httpRequest.send(null);
}

//返回信息处理函数
function processRequest()
{
   if (xmlHttpReuqest.readyState == 4)
   {
       if (xmlHttpReuqest.status == 200)
       {
       document.getElementById("wait").style.display="none";
          alert(xmlHttpReuqest.responseText);
       }
       else
       {
          alert("您所访问的页面存在问题!");
       }
   }
}

//根据备份方式确定URL
function getURL(id)
{
    var url = "callImmediatelyBackup.jsp?device_id="+id
            + "&now="+now.getTime();
    return url;
}
//----------------立即备份反馈信息等待-------------------------


//=========================================================================

function delWarn(){
	if(confirm("您确定要删除对该设备的配置吗？")){
		return true;
	}
	else{
		return false;
	}
}
var user_city_id = "<%=city_id%>";//用户所属属地编号
var city_id = "<%=city_id%>";//用户所选中的属地编号
function showChild(param)
{
	var obj = event.srcElement;
	
	if(param =='vendor_id')
	{
		var pid = document.all(param).value;
		if(parseInt(pid) == -1) 
			document.all("strModelList").innerHTML="<select name=\"device_model\" class=bk><option value=-1>请先选择厂商</option></select>";
		else
			document.all("childFrm").src = "getVendorDeviceModel.jsp?vendor_id="+ pid;
	}
    else if(param == 'city_id')
   	{
		city_id = obj.options[obj.selectedIndex].value;
		if(city_id!=-1)
		{
			if(user_city_id != city_id)
			{
	     		//document.all("childFrm").src = "cityFilter.jsp?city_id="+city_id;
			}
			else
			{
				//document.all("my_city<%=city_id%>").innerHTML = "";
				//document.all("city_name").innerHTML = "<font color='red'><%=city_name%></font>";
			}
			document.all("hid_city_id").value = city_id;
		}	
		updateDeviceList();		
	}
	else if (param == 'resource_type_id')
	{
	   updateDeviceList();
	}
}

</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<div id="bk_devicelist">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=14>&nbsp;</TD></TR>
<% 
if (fields == null || msg != null)
{ 
   if (msg == null)
   {
     msg = "没有您要查询的设备备份信息！";
   }
%>
<TR><TD>
	<TABLE width="96%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">已配置设备查询提示信息</TH>
					</TR>
						<TR  height="50">
							<TD align=center valign=middle class=column><%=msg %></TD>
						</TR>
					<TR>
						<TD class=foot align="right">
						<INPUT TYPE="button" NAME="cmdJump" onclick="GoList()" value=" 列 表 " class=jianbian>

						<INPUT TYPE="button" NAME="cmdBack" onclick="javascript:history.go(-1)" value=" 返 回 " class=jianbian>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<% }
else 
{
String strVendorList = DeviceAct.getVendorList(true,"","");
%>
<TR><TD>
	<TABLE width="96%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				    <TR style="display:none">
                        <TD bgcolor="#FFFFFF" align=left colspan="7">
                        &nbsp;&nbsp;<strong>资源类型：</strong>
                        &nbsp;<span><%=strResourceList%></span>
                        &nbsp;&nbsp;&nbsp;&nbsp;<strong>属地： </strong>
                        &nbsp;<span id="initCityFilter"><%=strCityList%> <span id='my_city<%=city_id%>'></span><span>
						&nbsp;</span> 
						<input type="hidden" name = 'hid_city_id' value=<%=city_id%>></TD>
                    </TR>
					<TR>
						<TH>设备IP</TH>
						<TH>设备名称</TH>
						<TH>设备类型</TH>
						<TH>备份方式</TH>
						<TH>备份时间</TH>
						<TH>是否备份</TH>
						<TH>操作</TH>
					</TR>
			      <% while (fields != null)
					 { 
					      String device_model = (String)fields.get("device_model");
					      String loopback_ip = (String)fields.get("loopback_ip");
					      String backup_type = (String)fields.get("backup_type");
					      String backup_time = (String)fields.get("backup_time");
					      String if_backup = (String)fields.get("if_backup"); 
					      String device_id = (String)fields.get("device_id");
					      String time_type = (String)fields.get("time_type");
					      String time_model = (String)fields.get("time_model");
					      String gather_id = (String)fields.get("gather_id");
					      //add 2007-12-27
					      String device_name = (String)fields.get("device_name");
					      String time = "";

					      backup_type = ManaConfTools.getBackupType(backup_type);
					      if_backup = ManaConfTools.getIfBackup(if_backup);
					      time = ManaConfTools.getBackupTime(time_type,time_model,backup_time);
	
				  %>
				       <TR>
				          <TD class=column1 align=center><%=loopback_ip %></TD>
				          <TD class=column1 align=center><%=device_name %></TD>
				          <TD class=column1 align=center><%=device_model %></TD>
				          <TD class=column1 align=center><%=backup_type %></TD>
				          <TD class=column1 align=center><%=time %></TD>
				          <TD class=column1 align=center><%= if_backup%></TD>
				          <TD class=column1 align=center>
				          <a href=deviceModifyForm.jsp?device_id=<%=device_id %>>编辑</a> | 
				           <a href="deviceDelete.jsp?device_id=<%=device_id %>&device_ip=<%=loopback_ip%>&gather_id=<%=gather_id %>" onclick="return delWarn();">删除</a>
				            | <a href="javascript:PromptBackup('<%=device_id %>')">立即备份</a>
				          </TD>
				       </TR>
				   <%
				       fields = cursor.getNext();
				   } %>   
				   <TR><TD class=column COLSPAN=7 align=right><%=strBar%></TD></TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR style="display:none"><TD>
	<TABLE  width="96%" border=0 cellspacing=0 cellpadding=0 align="center">
	<TR><TD HEIGHT=14>&nbsp;</TD></TR>
	<TR>
		<TD><B>快 速 查 询</B><br><hr size=2 color=#646464></TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="frm1" METHOD=POST ACTION="deviceList.jsp" >
		设备IP:&nbsp;<INPUT TYPE="text" NAME="loopback_ip" maxlength=15 class=bk>&nbsp;备份方式:
		<select name=backup_type class=bk onchange=changeBKType()><option value=-1>请选择备份方式</option>
		<option value=1>TELNET</option><option value=2>FTP</option><option value=3>SNMP</option></select><br><br>
		设备厂商:<%=strVendorList%>&nbsp;设备型号:<span id=strModelList><select name="device_model" class=bk><option value=-1>请先选择厂商</option></select></span>&nbsp;
		<INPUT TYPE="submit" name="cmdQuery" value=" 查 询 " class=btn>&nbsp;&nbsp;
		</FORM>
		</TD>
	</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>&nbsp;</TD></TR>

<% } %>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
</div>
<div id="wait" style="position:absolute;display:none;width:320px;height:30px;z-index: 9999;top:80px;left:210px;text-align: center;vertical-align: middle;background-color: white;font-size: 16px;font-weight: bold;border-style: solid;border-color: black;border-width: 1px;"><img src="../images/wait.gif"/>后台正在处理中，请耐心等待...</div>
<%@ include file="../foot.jsp"%>