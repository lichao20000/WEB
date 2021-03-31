<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import ="com.linkage.litms.vipms.ConfigCPE" %>
<jsp:useBean id="bcusa" scope="request" class="com.linkage.litms.vipms.action.BaseCustomerAct"/>
<jsp:useBean id="bcira" scope="request" class="com.linkage.litms.vipms.action.BaseCircuitAct"/>
<jsp:useBean id="bna" scope="request" class="com.linkage.litms.vipms.action.BaseNodeAct"/>
<jsp:useBean id="bsa" scope="request" class="com.linkage.litms.vipms.action.BaseServerAct"/>
<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("device_id");
String objid=request.getParameter("objid");
DeviceResourceInfo deviceInfo = new DeviceResourceInfo();
Cursor cursorinfo = deviceInfo.getDeviceResource(device_id);
Map myMap = cursorinfo.getNext();
String deviceName= (String)myMap.get("device_name");
String loopbackip= (String)myMap.get("loopback_ip");


String strData = "";

//��ҳ��
ConfigCPE cpe = new ConfigCPE(request);
Cursor cursor = cpe.getCPECursor();
Map fields = cursor.getNext();

if(fields == null){
	strData = "<TR ><TD COLSPAN=6 HEIGHT=30 bgcolor=#FFFFFF>���豸û������cpe��Ϣ</TD></TR>";
}
else{	
	String customname="";
	while(fields != null){
		customname=bcusa.getCustomerName((String)fields.get("customid"));
		strData += "<TR bgcolor=#FFFFFF>";

		strData += "<TD >"+ (String)fields.get("port_id") + "</TD>";
		strData += "<TD >"+ customname + "</TD>";
		strData += "<TD >"+ bsa.getServerName((String)fields.get("serviceid")) + "</TD>";
		strData += "<TD >"+ bna.getNodeName((String)fields.get("nodeid")) + "</TD>";
		strData += "<TD >"+ bcira.getCircuitName((String)fields.get("circuitid")) + "</TD>";
		
		strData += "<TD align=center><A HREF=\"javascript:Edit('"+fields.get("inid")+"','"+fields.get("customid")+"','"+customname+"','"+fields.get("port_type")+"','"+fields.get("port_id")+"','"+(String)fields.get("serviceid")+"','"+(String)fields.get("nodeid")+"','"+(String)fields.get("circuitid")+"');\">�༭</A> | <A HREF=\"javascript:Del('"+(String)fields.get("inid")+"','"+device_id+"','"+objid+"');\">ɾ��</A></TD>";
		strData += "</TR>";		
		
		fields = cursor.getNext();
	}
	
}


%>
<%@ include file="../head.jsp"%>
<TITLE>ҵ���·����</TITLE>

<SCRIPT LANGUAGE="JavaScript" src="../Js/CheckForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
var isCall=0;
var iTimerID;
var isSel=0;
var id="";
var isCPE="-1";
function CallPro()
{	
	switch(parseInt(isCall,10))
	{
		case 1:
		{
			window.alert("���óɹ�!");
			window.clearInterval(iTimerID);				
			if(isCPE!="-1")
			{
				var oindex=opener.findDevObjByID(frm.objid.value);
				opener.arrDev[oindex]._iscpe=parseInt(isCPE,10);
			}			
			location.href="config_cpe.jsp?objid="+frm.objid.value+"&device_id="+frm.device_id.value;

			break;
		}
		case -1:
		{
			window.alert("���ݿ����ʧ��,�����²���!");
			window.clearInterval(iTimerID);
			frm.button1.disabled=false;
			isCall=0;
			break;
		}
		case -98:
		{
			window.alert("�˿ڲ������óɹ���֪ͨ��̨ʧ��!");
			window.clearInterval(iTimerID);			
			location.href="config_cpe.jsp?device_id="+frm.device_id.value+"&objid="+frm.objid.value+"";
			isCall=0;
			break;
		}	
		case -99:
		{
			window.alert("��ѡ��Ķ˿��Ѿ�������ҵ����ռ�ã������²���!");
			window.clearInterval(iTimerID);
			frm.button1.disabled=false;
			isCall=0;
			break;
		}	
		case -100:
		{
			window.alert("��ѡ��ĵ�·�ڸ��豸���Ѿ����ڣ������²���!");
			window.clearInterval(iTimerID);
			frm.button1.disabled=false;
			isCall=0;
			break;
		}	
	}

}

function Del(_id,_device_id,_objid)
{
	if(window.confirm(("��ȷ��ɾ����������?")))
	{
		isCPE="-1";
		var page="config_cpe_save.jsp?device_id="+_device_id+"&inid="+_id+"&action=delete&objid="+_objid+"";
		document.all("childFrm").src=page;
		iTimerID = window.setInterval("CallPro()",1000);
	}
}

function Add(){
	document.frm.reset();
	document.frm.action.value="add";
	actLabel.innerHTML = '���������Ϣ';	
	ClearAll();
}

function Edit(_id,_customid,_customname,_port_type,_port_id,_serviceid,_nodeid,_circuitid){
	frm.inid.value=_id;
	document.frm.action.value="edit";
	actLabel.innerHTML = '�༭������Ϣ';	
	frm.port_type.value=_port_type;
	frm.port_id.value=_port_id;
	frm.customid.value=_customid;
	frm.customname.value=_customname;

	var page="config_getVpnInfo.jsp?method=all&key="+_customid+"&serviceid="+_serviceid;
	document.all("childFrm").src=page;	
	isSel=0;
	iTimerID = window.setInterval("CallSel('"+_serviceid+"','"+_nodeid+"','"+_circuitid+"')",1000);	
}

function CallSel(_serviceid,_nodeid,_circuitid)
{
	if(isSel==1)
	{
		
		
		for(var i=0;i<frm.service.options.length;i++)
		{			
			if(frm.service.options[i].value==_serviceid)
			{				
				frm.service.options[i].selected=true;			
				break;
			}

		}
		

		for(var i=0;i<frm.node.options.length;i++)
		{
			//alert(frm.drt_mid.options[i].value);
			if(frm.node.options[i].value==_nodeid)
			{
				
				frm.node.options[i].selected=true;			
				break;
			}

		}

		for(var i=0;i<frm.circuit.options.length;i++)
		{
			//alert(frm.drt_mid.options[i].value);
			if(frm.circuit.options[i].value==_circuitid)
			{
				
				frm.circuit.options[i].selected=true;			
				break;
			}

		}
		
		window.clearInterval(iTimerID);	
	}

}


function CheckForm()
{
	if(frm.customname.value=="")
	{
		alert("��ѡ���ͻ�");
		return;
	}

	if(frm.service.selectedIndex==0)
	{
		alert("��ѡ��ҵ��");
		return;
	}

	if(frm.node.selectedIndex==0)
	{
		alert("��ѡ������");
		return;
	}

	if(frm.circuit.selectedIndex==0)
	{
		alert("��ѡ���·");
		return;
	}

	if(frm.port_id.value=="")
	{
		alert("��ѡ��˿�");
		return;
	}
	if(frm.action.value=="add" || (frm.action.value=="edit" && window.confirm("��ȷ����Ҫ�޸ĸ���Ϣ��?")))
	{
		
		frm.button1.disabled=true;
		var page="config_cpe_save.jsp?action="+frm.action.value+"&inid="+frm.inid.value+"&device_id="+frm.device_id.value+"&customid="+frm.customid.value+"&serviceid="+frm.service.options[frm.service.selectedIndex].value+"&nodeid="+frm.node.options[frm.node.selectedIndex].value+"&circuitid="+frm.circuit.options[frm.circuit.selectedIndex].value+"&port_type="+frm.port_type.value+"&port_id="+frm.port_id.value+"&objid="+frm.objid.value+"";
		//alert(page);
		isCPE="-1";
		document.all("childFrm").src=page;
		iTimerID = window.setInterval("CallPro()",1000);		
	}
}




function SelUser()
{
	var page="SelVpnUser.jsp";
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,width=500,height=300";
	window.open(page,"ѡ���ͻ�",otherpra);
}

function setUserProperty(customid,customname)
{
	frm.customid.value=customid;
	frm.customname.value=customname;
	var page="config_getVpnInfo.jsp?method=two&key="+customid;
	document.all("childFrm").src=page;	
}

function setPortProperty(type,value)
{
	frm.port_type.value=type;
	frm.port_id.value=value;

}

function showChild(method)
{
	
	if(method=="service")
	{
		var value=frm.service.options[frm.service.selectedIndex].value;
		//alert(value);
		if(value=="-1")
		{
			return;
		}
		else
		{
			var page="config_getVpnInfo.jsp?method=circuitList&key="+value;
			document.all("childFrm").src=page;
		}
	
	}


}

function selPort()
{
	var page="SelDevPort.jsp?device_id="+frm.device_id.value;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,width=500,height=300";
	window.open(page,"ѡ���豸�˿�",otherpra);

}

function ClearAll()
{
	frm.customid.value="";
	frm.customname.value="";
	frm.port_type.value="";
	frm.port_id.value="";
	document.all("serviceList").innerHTML ="<select class=bk name='service'><option value='-1'>����ѡ��ͻ�</option></select>";
	document.all("nodeList").innerHTML = "<select class=bk name='node'><option value='-1'>����ѡ��ͻ�</option></select>";
	document.all("circuitList").innerHTML =  "<select class=bk name='circuit'><option value='-1'>����ѡ��ҵ��</option></select>";
}



//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">

<TR><TD>
	<FORM NAME="frm" METHOD="post">
        <table width="100%" height="30" border="0" align="center" cellpadding="0" cellspacing="0"  class="blue_gargtd">
          <tr> 
            <td width="162"><div align="center" class="title_bigwhite">CPE�豸����</div></td>
            <td><img src="../images/attention_2.gif" width="15" height="12">&nbsp;<%=deviceName%>[<%=loopbackip%>]����</td>
            <td> <div align="right">
                <input type="button" name="add1" value=" �� �� " onClick="javascript:Add();" class="jianbian">
              </div></td>
          </tr>
        </table>
        <TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                <TR class="blue_title"> 
				  <TD>�˿ڱ�ʶ</TD>
                  <TD>�ͻ�</TD>
                  <TD>ҵ��</TD>
                  <TD>����</TD>
				  <TD>��·</TD>
                  <TD>����</TD>
                </TR>
                <%=strData%> </TABLE>
			</TD>
		</TR>
	</TABLE>
	<BR>
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">					
                <TR bgcolor="#FFFFFF" class="blue_title"> 
                  <td colspan="2" align="center"><SPAN id="actLabel" style="border:0;">���������Ϣ</SPAN></td>
				</TR>				
				
				<TR bgcolor="#FFFFFF" >
                  <TD width="33%" align=right>
										
                    ��ͻ�
                  </TD>
                  <TD class="" width="67%">
					<input type="hidden" name="customid">
                    <input type="text" class=bkreadOnly name="customname" readonly>
					<input type="button" name="seluser" onclick="javascript:SelUser();" value="..." class="jianbian">
                  </TD>
                </TR>

				
				<tr bgcolor="#FFFFFF">
					<td width="33%" align=right>ѡ��ҵ��</td>
					<td class="" width="67%">
						<span id="serviceList">
						<select class=bk name="service">
							<option value="-1">����ѡ��ͻ�</option>
						</select>
						</span>
					</td>					
				</tr>
				
				<tr bgcolor="#FFFFFF">
					<td width="33%" align=right>ѡ������</td>
					<td class="" width="67%">
						<span id="nodeList">
						<select class=bk name="node">
							<option value="-1">����ѡ��ͻ�</option>
						</select>
						</span>
					</td>					
				</tr>
				
				<tr bgcolor="#FFFFFF">
					<td width="33%" align=right>ѡ���·</td>
					<td class="" width="67%">
						<span id="circuitList">
						<select class=bk name="circuit">
							<option value="-1">����ѡ��ҵ��</option>
						</select>
						</span>
					</td>					
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="33%" align=right>
						ѡ�����ҵ��˿�
					</td>
					<TD class="" width="67%">
						<input type="hidden" name="port_type">
						<input type="text" class=bkreadOnly name="port_id" readonly>
						<input type="button" name="selport" onclick="javascript:selPort();" value="..." class="jianbian">
					</td>
				</tr>
										
                <TR bgcolor="#FFFFFF" > 
                  <TD colspan="4" align="right" CLASS="blue_foot"> 
                    <INPUT TYPE="button" value=" �� �� " class="jianbian" name="button1"  onclick="javascript:CheckForm();">&nbsp;
							<INPUT TYPE="hidden" name="action" value="add">&nbsp;
							<input type="hidden" name="device_id" value="<%=device_id%>">
							<input type="hidden" name="objid" value="<%=objid%>">
							<input type="hidden" name="inid">
							<INPUT TYPE="button" value=" �ء��� " class="jianbian" onclick="javascript:window.close();">
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>

	</TABLE>
	</FORM>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME></TD></TR>
</TABLE>
<%@ include file="../openfoot.jsp"%>
