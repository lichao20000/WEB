<%--
Author		: Yan.HaiJian
Date		: 2006-9-30
Desc		: config flux.
--%>

<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@ page import ="com.linkage.litms.vipms.flux.ManagerFluxConfig" %>
<%@ page import ="com.linkage.litms.webtopo.common.*"%>
<%@ page import="com.linkage.litms.webtopo.DeviceResourceInfo" %>
<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("device_id");
String serial=request.getParameter("serial");
if(null==serial||"".equals(serial))
{
	DeviceCommonOperation dco = new DeviceCommonOperation();
	serial = dco.getSerialByDeviceid(device_id);
}

//������Դ
String srcType = request.getParameter("type");
if(null==srcType)
{
	srcType ="1";
}
DeviceResourceInfo deviceInfo = new DeviceResourceInfo();
Cursor cursorinfo = deviceInfo.getDeviceResource(device_id);
Map myMap = cursorinfo.getNext();
String deviceName= (String)myMap.get("device_name");
String loopbackip= (String)myMap.get("loopback_ip");
ManagerFluxConfig mfc=new ManagerFluxConfig(request);
//ArrayList snmpList=mfc.getSnmpVersion();
String strCongfig="δ����";
boolean isConfig=mfc.isConfig();
if(isConfig) {
	strCongfig="������";

}

%>
<%@ include file="../head.jsp"%>
<TITLE>�豸��������</TITLE>

<SCRIPT LANGUAGE="JavaScript" src="../Js/CheckForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
	var isCall=0;

	function CheckAllPorts()
	{
	  var isCheck = document.all("checkPort").checked;
	  var Objects= document.getElementsByName("ifindex");	 
	  for (var i=0;i<Objects.length;i++)
	  {
		Objects[i].checked =isCheck;
	  }	
	
	}	


	function changeTotal(type)
	{
		switch(type)
		{
			case 0:
			{
			   	
				//document.all("delete1").style.display="none";
				if(isCall==1)
				{
					document.all("span1").style.display="";
					return;
				}
				
				document.all("td1").style.background="#FFFFFF";
				document.all("span1").innerHTML="<font color='red'>���Եȣ���������˿���Ϣ.....</font>";
				//var page="webtop_getDevicePort.jsp?device_id="+frm.device_id.value+"&serial="+frm.serial.value+"&snmp="+snmp;
				var page="webtop_getDevicePort.jsp?device_id="+frm.device_id.value+"&serial="+frm.serial.value
				//alert(page);
				document.all("childFrm").src=page;
				document.all("span1").style.display="";
				break;
			}
			case 1:
			{
			    //document.all("delete1").style.display="";
				document.all("span1").style.display="none";
				break;
			}	
		}
	}

	function CheckRepeat(objid,index)
	{
	  var e;
	  var comp=eval("frm.radio"+objid)[index].value;	
	  var flag=true;
	  
	  var comps=comp.split("\|\|\|");
	  //alert(comps[1]);
	  if(comps.length==1 || comps[1]=="" || comps[1]=="null")
	  {
		window.alert("��ѡ��Ķ˿ڵı�ʶû�вɼ���ֵ����ѡ������Ĳɼ���ʽ!");
		eval("frm.radio"+objid)[index].checked=false;
		return ;
	  }
	  

	  for (var i=0;i<frm.elements.length;i++)
	  {
		e = frm.elements[i];
		if (e.name.indexOf("radio")>=0  &&  e.name!="radio"+objid)
		{
			if(e.value==comp)
			{
				flag=false;
				break;
			}
		}
		  
	  }	


	  if(!flag)
	  {
		window.alert("��ѡ��Ķ˿ڵı�ʶ����Ψһ����ѡ������Ĳɼ���ʽ!");
	  }
	  eval("frm.radio"+objid)[index].checked=flag;
	  
	
	}

	function CheckForm()
	{	    
		//if(typeof(frm.snmp)!="object")
		//{
		//	alert("�����õ��豸����û������snmp�ɼ��汾���������Ա��ϵ!");
		//	return;
		//}

		var istotal=0;
		for(var i=0;i<frm.istotal.length;i++)
		{
			if(frm.istotal[i].checked)
			{
				istotal=frm.istotal[i].value;
				break;
			}
		}		
		
		switch(parseInt(istotal,0))
		{
			//�������������
			case 0:
			{			   
				if(typeof(frm.ifindex)!="object")
				{
					alert("��ѡ����豸û�вɼ����˿���Ϣ����ʱ������������");
					return;
				}				
				var ifindexObjects = document.getElementsByName("ifindex");
				var flag = false;						
				for(var i=0;i<ifindexObjects.length;i++)
				{
				    if(ifindexObjects[i].checked)
				    {
				      flag =true;
				      
				      //�жϺ����������û��ѡ�е�			      
				      var atrributes =document.getElementsByName("checkbox"+ifindexObjects[i].value.split("|||")[0]);				      
				      var atrributeFlag = false;
				      for(var j=0;j<atrributes.length;j++)
				      {
				        if(atrributes[j].checked)
				        {
				          atrributeFlag = true;
				          break;
				        }
				      }
				      if(!atrributeFlag)
				      {
				        alert("��ѡ��˿�Ҫ�ɼ�������");
				        return false;
				      }
				    }
				}				
				
				if(!flag)
				{
				  alert("��ѡ��˿�!");
				  return false;
				  
				}		
				
				break;
			}
			//������������
			case 1:
			{
			
				break;
			}
		
		}		

		if(!IsNumber(frm.polltime.value,"��������ȷ�Ĳɼ����ʱ��"))
		{
			frm.polltime.focus();
			return;			
		}

		frm.submit();

	
	}
	
	
	function deleteConfig()
	{
	  var page="webtop_ConfigFlux_save.jsp?action=delete&device_id=<%=device_id%>&type=<%=srcType%>";
	  this.location.href=page;	  
	}
	
	function displayPorts()
	{
	  var page="webtop_devicePortsList.jsp?device_id=<%=device_id%>&type=<%=srcType%>";
	  window.open(page);
	}
	
	function changeAuto(paras)
	{
	   //�Զ�����
	   if(paras==1)
	   {
	     tr1.style.display="none";
	   }
	   
	   //�ֹ����ã��ṩ���÷�ʽ��ѡ��
	   if(paras==0)
	   {
	     tr1.style.display="";
	   }
	  
	}
	
//-->
</SCRIPT>


<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>
	    <FORM NAME="frm" METHOD="post" action="webtop_ConfigFlux_save.jsp" target="childFrm">
        <table width="100%" height="30" border="0" align="center" cellpadding="0" cellspacing="0"  class="green_gargtd">
          <tr> 
            <td width="162"><div align="center" class="title_bigwhite">�豸��������</div></td>
            <td><img src="../images/attention_2.gif" width="15" height="12">&nbsp;<%=deviceName%>[<%=loopbackip%>]</td>
            
          </tr>
        </table>
        <TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">                
				<TR> 
					<td class="column1" align=right>��������:</td><td class="column">
					<input type="radio" name="istotal" value="1" onclick="javascript:changeTotal(1);" checked>��
					<input type="radio" name="istotal" value="0" onclick="javascript:changeTotal(0);">��  <img src="../images/attention_2.gif" width="15" height="12">ע����˿ڹ����뾡��ѡ����������</td>
                </TR>
				<TR> 
					<td class="column1" align=right>�ɼ����ʱ��:</td><td class="column"><input type="text" name="polltime" value="300" size="8">��  <img src="../images/attention_2.gif" width="15" height="12"> ע���ɼ����ʱ����300�����ұȽϺ���</td>
                </TR>
                <TR>
                   <td class="column1" align=right>ԭʼ�����Ƿ���⣺</td>
                   <td  class="column"><input type="radio" name="isdb" value="1" checked>��<input type="radio" name="isdb" value="0">��</td>
                </TR>
				<TR> 
					<td class="column1" align=right>����״̬:</td><td class="column">
					<%=strCongfig%></td>
                </TR>
				</TABLE>
			</TD>
		</TR>
		<tr>
			<td  bgcolor="#999999" width="100%" id="td1">				
				<div id="span1" style="display:none;border:0;height:350px;overflow:auto;"></div>
			</td>
		</tr>

		<tr>
			<td class="column1" align="right">
				<input type="hidden" name="device_id" value="<%=device_id%>">
				<input type="hidden" name="serial" value="<%=serial%>">
				<input type="hidden" name="type" value='<%=srcType %>'>				
				<input type="button" value=" �� �� " class="jianbian" name="save" onclick="javascript:CheckForm();">&nbsp;&nbsp;
				<%if(isConfig){ %>
				<input type="button" value="�˿��б�" class="jianbian" name="see" onclick="javascript:displayPorts();">&nbsp;&nbsp;
				<input type="button" value="ɾ������" class="jianbian" name="delete1" onclick="javascript:deleteConfig();" STYLE="display:" >&nbsp;&nbsp;
				<%} %>
				<input type="button" value=" �� �� " class="jianbian" name="close" onclick="javascript:window.close();">
			</td>
		</tr>

	</TABLE>
	<BR>
	
	</FORM>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;<IFRAME id="childFrm" name="childFrm" SRC="" STYLE="display:none;width:500;height:500"></IFRAME></TD></TR>
</TABLE>

<SCRIPT LANGUAGE="JavaScript">
<!--
	//initLoad();
//-->
</SCRIPT>
<%@ include file="../foot.jsp"%>
