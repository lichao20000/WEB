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

//请求来源
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
String strCongfig="未配置";
boolean isConfig=mfc.isConfig();
if(isConfig) {
	strCongfig="已配置";

}

%>
<%@ include file="../head.jsp"%>
<TITLE>设备流量配置</TITLE>

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
				document.all("span1").innerHTML="<font color='red'>请稍等，正在载入端口信息.....</font>";
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
		window.alert("您选择的端口的标识没有采集到值，请选择另外的采集方式!");
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
		window.alert("您选择的端口的标识不是唯一，请选择另外的采集方式!");
	  }
	  eval("frm.radio"+objid)[index].checked=flag;
	  
	
	}

	function CheckForm()
	{	    
		//if(typeof(frm.snmp)!="object")
		//{
		//	alert("您配置的设备类型没有配置snmp采集版本，请与管理员联系!");
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
			//代表非总体配置
			case 0:
			{			   
				if(typeof(frm.ifindex)!="object")
				{
					alert("您选择的设备没有采集到端口信息，暂时不能配置流量");
					return;
				}				
				var ifindexObjects = document.getElementsByName("ifindex");
				var flag = false;						
				for(var i=0;i<ifindexObjects.length;i++)
				{
				    if(ifindexObjects[i].checked)
				    {
				      flag =true;
				      
				      //判断后面的属性有没有选中的			      
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
				        alert("请选择端口要采集的性能");
				        return false;
				      }
				    }
				}				
				
				if(!flag)
				{
				  alert("请选择端口!");
				  return false;
				  
				}		
				
				break;
			}
			//代表总体配置
			case 1:
			{
			
				break;
			}
		
		}		

		if(!IsNumber(frm.polltime.value,"请输入正确的采集间隔时间"))
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
	   //自动配置
	   if(paras==1)
	   {
	     tr1.style.display="none";
	   }
	   
	   //手工配置，提供配置方式的选择
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
            <td width="162"><div align="center" class="title_bigwhite">设备流量配置</div></td>
            <td><img src="../images/attention_2.gif" width="15" height="12">&nbsp;<%=deviceName%>[<%=loopbackip%>]</td>
            
          </tr>
        </table>
        <TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">                
				<TR> 
					<td class="column1" align=right>总体配置:</td><td class="column">
					<input type="radio" name="istotal" value="1" onclick="javascript:changeTotal(1);" checked>是
					<input type="radio" name="istotal" value="0" onclick="javascript:changeTotal(0);">否  <img src="../images/attention_2.gif" width="15" height="12">注：如端口过多请尽量选择总体配置</td>
                </TR>
				<TR> 
					<td class="column1" align=right>采集间隔时间:</td><td class="column"><input type="text" name="polltime" value="300" size="8">秒  <img src="../images/attention_2.gif" width="15" height="12"> 注：采集间隔时间在300秒左右比较合适</td>
                </TR>
                <TR>
                   <td class="column1" align=right>原始数据是否入库：</td>
                   <td  class="column"><input type="radio" name="isdb" value="1" checked>是<input type="radio" name="isdb" value="0">否</td>
                </TR>
				<TR> 
					<td class="column1" align=right>配置状态:</td><td class="column">
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
				<input type="button" value=" 保 存 " class="jianbian" name="save" onclick="javascript:CheckForm();">&nbsp;&nbsp;
				<%if(isConfig){ %>
				<input type="button" value="端口列表" class="jianbian" name="see" onclick="javascript:displayPorts();">&nbsp;&nbsp;
				<input type="button" value="删除配置" class="jianbian" name="delete1" onclick="javascript:deleteConfig();" STYLE="display:" >&nbsp;&nbsp;
				<%} %>
				<input type="button" value=" 关 闭 " class="jianbian" name="close" onclick="javascript:window.close();">
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
