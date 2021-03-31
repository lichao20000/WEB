<%@ include file="../timelater.jsp"%>
<%@ page import="com.linkage.litms.webtopo.common.DeviceCommonOperation" %>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%
request.setCharacterEncoding("GBK");
String xvalue=request.getParameter("x");
String yvalue=request.getParameter("y");
String parent_id=request.getParameter("parent_id");
String strVendorList = DeviceAct.getVendorList(true,"","");
DeviceCommonOperation dco=new DeviceCommonOperation(session);
String strGather=dco.getGatherInfoForm(false,"","");

%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<%@ include file="../toolbar.jsp"%>

<SCRIPT LANGUAGE="JavaScript">
//0 操作失败
//1 添加正常
//2 设备已存在 不允许添加
var isCall=0;
var iTimerID;


function CallPro()
{
	
	switch(parseInt(isCall,10))
	{
		case 1:
		{
			window.alert("添加对象成功!");
			window.clearInterval(iTimerID);	
			window.close();
			break;
		}
		case 2:
		{
			window.alert("新增设备的OUI＋serialNum在系统中已经存在,如果你看不到该设备, 可能是没有权限，请与管理员联系!");
			window.clearInterval(iTimerID);
			isCall=0;
			break;
		}
		case 0:
		{
			window.alert("操作失败，请重试或与管理员联系！");
			window.clearInterval(iTimerID);
			isCall=0;
			break;
		}
		
	}

}

function CheckForm()
{	
	var index= eval(frm.obj_type.value);
	var obj_type="device";
	var serial="";
	var port="";
	var path="";
	switch(index)
	{	
		case 0:
		{
			if(frm.vendor_id.selectedIndex==0)
			{
				window.alert("请选择厂商");
				return ;
			}

			if(typeof(frm.devicetype_id)!="object" || (typeof(frm.devicetype_id)=="object" && frm.devicetype_id.selectedIndex==0))
			{
				window.alert("请选择设备型号");
				return ;
			}

			if(frm.device_ip.value=="")
			{
				window.alert("请输入ip地址");
				frm.device_ip.focus();
				return ;
			}

			if(!IsIPAddr(frm.device_ip.value))
			{
				frm.device_ip.focus();
				return ;
			}

			if(frm.device_name.value=="")
			{
				window.alert("请输入设备名称");
				frm.device_name.focus();
				return ;			
			}

			if(frm.port.value=="")
			{
				window.alert("请输入端口");
				frm.port.focus();
				return ;
			}
			
			if(frm.gather_id.selectedIndex==0)
			{
				window.alert("请选择采集点");
				return ;
			
			}

			serial=frm.devicetype_id.options[frm.devicetype_id.selectedIndex].value;
			port=frm.port.value;
			break;
		}
		case 1:
		{
			if(frm.device_ip.value=="")
			{
				window.alert("请输入ip地址");
				frm.device_ip.focus();
				return ;
			}

			if(!IsIPAddr(frm.device_ip.value))
			{
				frm.device_ip.focus();
				return ;
			}

			if(frm.device_name.value=="")
			{
				window.alert("请输入设备名称");
				frm.device_name.focus();
				return ;			
			}

			if(frm.port.value=="")
			{
				window.alert("请输入端口");
				frm.port.focus();
				return ;
			}
			
			if(frm.gather_id.selectedIndex==0)
			{
				window.alert("请选择采集点");
				return ;
			
			}			
			serial="-2";
			port=frm.port.value;
			break;
		}
		case 2:
		{
			serial="-1";
			port=frm.port.value;
			break;
		}
		case 3:
		{
			if(frm.device_name.value=="")
			{
				window.alert("请输入网段名称");
				frm.device_name.focus();
				return ;			
			}
			
			if(frm.gather_id.selectedIndex==0)
			{
				window.alert("请选择采集点");
				return ;
			
			}		

			obj_type="segment";
			serial="0";
			break;
		}

	}
	var parent_id=frm.parent_id.value;
	var oui = $("vendor_id").value
	var gather_id=frm.gather_id.options[frm.gather_id.selectedIndex].value;
	var labelname=frm.device_name.value;
	var x=frm.xvalue.value;
	var y=frm.yvalue.value;
	var device_ip=frm.device_ip.value;	
	var path = frm.path.value;	
	var device_serialnum = $("device_serialnum").value;
	var page="AddDevice.jsp?obj_type="+obj_type+"&oui=" + oui + "&parent_id="+parent_id+"&gather_id="+gather_id+"&labelname="+labelname+"&x="+x+"&y="+y+"&device_ip="+device_ip+"&devicetype_id=" + serial + "&port=" + port + "&path="+ path + "&device_serialnum=" + device_serialnum;
	//alert(page);
	
	document.all("childFrm").src=page;
	//iTimerID = window.setInterval("CallPro()",1000);


}


function showChild(parname){
	if(parname=="vendor_id"){		
		var o = $("vendor_id");
		var id = o.options[o.selectedIndex].value;
		var url = "../Resource/getVendorDeviceModel.jsp";
		var pars = "vendor_id=" + id;
		var myAjax
			= new Ajax.Request(
							url,
							{method:"post",parameters:pars,onComplete:getDeviceModel,onFailure:showError}						
						   );
	}
}
function getDeviceModel(request){
	$("sp_DeviceModel").innerHTML = request.responseText;
}
//Debug
function showError(request){
	alert(request.responseText);
}
function TypeChange()
{
	var index= eval(frm.obj_type.value)
	switch(index)
	{
		//已知设备
		case 0:
		{
			frm.vendor_id.disabled=false;
			frm.vendor_id.className="form_kuang";
			if(typeof(frm.devicetype_id)=="object")
			{
				frm.devicetype_id.disabled=false;
				frm.devicetype_id.className="form_kuang";
			}
			frm.device_ip.readOnly=false;
			frm.device_ip.className="form_kuang";
			frm.device_name.readOnly=false;
			frm.device_name.className="form_kuang";
			frm.port.readOnly=false;
			frm.port.className="form_kuang";
			frm.path.readOnly=false;
			frm.path.className="form_kuang";
			frm.device_serialnum.readOnly=false;
			frm.device_serialnum.className="form_kuang";			
			frm.gather_id.disabled=false;
			frm.gather_id.className="form_kuang";			
			break;
		}
		//未知设备
		case 1:
		{
			frm.vendor_id.disabled=true;
			frm.vendor_id.className="bkreadOnly";
			if(typeof(frm.devicetype_id)=="object")
			{
				frm.devicetype_id.disabled=true;
				frm.devicetype_id.className="bkreadOnly";
			}
			frm.device_ip.readOnly=false;
			frm.device_ip.className="form_kuang";
			frm.device_name.readOnly=false;
			frm.device_name.className="form_kuang";
			frm.port.readOnly=false;
			frm.port.className="form_kuang";
			frm.path.readOnly=false;
			frm.path.className="form_kuang";
			frm.device_serialnum.readOnly=false;
			frm.device_serialnum.className="form_kuang";
			frm.gather_id.disabled=false;
			frm.gather_id.className="form_kuang";
			break;
		}
		//虚拟设备
		case 2:
		{
			frm.vendor_id.disabled=true;
			frm.vendor_id.className="bkreadOnly";
			if(typeof(frm.devicetype_id)=="object")
			{
				frm.devicetype_id.disabled=true;
				frm.devicetype_id.className="bkreadOnly";
			}
			frm.device_ip.readOnly=true;
			frm.device_ip.className="bkreadOnly";
			frm.device_name.readOnly=true;
			frm.device_name.className="bkreadOnly";
			frm.port.readOnly=true;
			frm.port.className="bkreadOnly";
			frm.path.readOnly=true;
			frm.path.className="bkreadOnly";
			frm.device_serialnum.readOnly=true;
			frm.device_serialnum.className="bkreadOnly";
			frm.gather_id.disabled=true;
			frm.gather_id.className="bkreadOnly";
			break;
		}
		//网段对象
		case 3:
		{
			frm.vendor_id.disabled =true;
			frm.vendor_id.className="bkreadOnly";
			if(typeof(frm.devicetype_id)=="object")
			{
				frm.devicetype_id.disabled=true;
				frm.devicetype_id.className="bkreadOnly";
			}
			frm.device_ip.readOnly=true;
			frm.device_ip.className="bkreadOnly";
			frm.device_name.readOnly=false;
			frm.device_name.className="form_kuang";
			frm.device_name.focus();
			frm.port.readOnly=true;
			frm.port.className="bkreadOnly";
			frm.path.readOnly=true;
			frm.path.className="bkreadOnly";
			frm.device_serialnum.readOnly=true;
			frm.device_serialnum.className="bkreadOnly";
			frm.gather_id.disabled=false;
			frm.gather_id.className="form_kuang";
			break;
		}
	
	}

	
}

function createNet(id,title,x,y,ip,icon,type,pid,state)
{
	opener.createNet(id,title,x,y,ip,icon,type,pid,state);
}
</SCRIPT>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>
    <FORM NAME="frm" METHOD="post" ACTION="" onsubmit="return CheckForm()" target="childFrm">
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH colspan="4" align="center">新建网元对象</TH>
					<input type="hidden" name="xvalue" value=<%=xvalue%>>
					<input type="hidden" name="yvalue" value=<%=yvalue%>>
					<input type="hidden" name="parent_id" value="<%=parent_id%>">
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="30%">对象类型</TD>
					<TD><span id="span1"><select  class=form_kuang name="obj_type" onchange="javascript:TypeChange();">
							<option value="0">已知设备</option>
<!--							<option value="1">未知设备</option>-->
<!--							<option value="2">虚拟设备</option>-->
							<option value="3">网段对象</option>
						</select></span></TD>					
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="30%">请选择厂商</TD>
					<TD><span id="span2"><%=strVendorList%></span></TD>					
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="30%">请选择设备型号</TD>
					<TD><span id="sp_DeviceModel"></span></TD>					
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="30%">IP地址</TD>
					<TD><span id="span3"><input type="text" class=form_kuang name="device_ip"></span></TD>					
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="30%">名称</TD>
					<TD><span id="span4"><input type="text" class=form_kuang name="device_name"></span></TD>					
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="30%">序列号</TD>
					<TD><span id="span4"><input type="text" class=form_kuang name="device_serialnum"></span></TD>					
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="30%">端口</TD>
					<TD><span id="span5"><input type="password" class=form_kuang name="port"></span></TD>					
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="30%">路径</TD>
					<TD><span id="span5"><input type="password" class=form_kuang name="path"></span></TD>					
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="30%">采集机</TD>
					<TD><span id="span6"><%=strGather%></span></TD>					
				</TR>
				<TR>
					<TD colspan="4" align="center" class=foot>
						<INPUT TYPE="button" value=" 保 存 " onclick="javascript:CheckForm();" class=btn>&nbsp;&nbsp;
						<INPUT TYPE="button" value=" 关 闭 " class=btn onclick="javascript:window.close();">						
					</TD>
				</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
    </FORM>
<TR><TD>&nbsp;</TD></TR>
<TR><TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
&nbsp;</TD></TR>
</TABLE>