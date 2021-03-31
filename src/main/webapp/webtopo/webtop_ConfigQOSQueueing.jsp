<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import ="com.linkage.litms.vipms.flux.ManagerQOSQueueConfig" %>

<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("device_id");
String serial=request.getParameter("serial");

DeviceResourceInfo deviceInfo = new DeviceResourceInfo();
Cursor cursorinfo = deviceInfo.getDeviceResource(device_id);

Map myMap = cursorinfo.getNext();
String deviceName= (String)myMap.get("device_name");
String loopbackip= (String)myMap.get("loopback_ip");
String device_model = (String)myMap.get("device_model");
ManagerQOSQueueConfig mfc=new ManagerQOSQueueConfig(request);
ArrayList snmpList=mfc.getSnmpVersion(device_model);
String strCongfig="未配置";
boolean isConfig=mfc.isConfig();
if(isConfig) {
	strCongfig="已配置";

}
%>
<%@ include file="../head.jsp"%>
<TITLE>设备QOS配置</TITLE>

<FORM NAME="frm" METHOD="post" action="webtop_ConfigQOSQueueing_Save.jsp" target="childFrm">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD>
	
        <table width="100%" height="30" border="0" align="center" cellpadding="0" cellspacing="0"  class="blue_gargtd">
          <tr> 
            <td width="162"><div align="center" class="title_bigwhite">设备QOS队列流量配置</div></td>
            <td><img src="../images/attention_2.gif" width="15" height="12">&nbsp;<%=deviceName%>[<%=loopbackip%>]</td>
            
          </tr>
        </table>
        <TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                <TR> 
					<td class="column1" align=right>snmp采集版本:</td><td class="column">
						<%
							for(int i=0;i<snmpList.size();i++)
							{
								out.println("<input type=\"radio\" name=\"snmp\" value="+snmpList.get(i)+">V"+snmpList.get(i)+"&nbsp;&nbsp;");			
							}
						%>
					</td>
                </TR>
				<TR> 
					<td class="column1" align=right>总体配置:</td><td class="column">
					<input type="radio" name="istotal" value="1" checked>是
					<input type="radio" name="istotal" value="0">否  <img src="../images/attention_2.gif" width="15" height="12">注：如端口过多请尽量选择总体配置</td>
                </TR>
				<TR> 
					<td class="column1" align=right>采集间隔时间:</td><td class="column"><input type="text" name="polltime" value="300" size="8">秒  <img src="../images/attention_2.gif" width="15" height="12"><span>注：采集间隔时间在300秒左右比较合适</span></td>
                </TR>
				<TR> 
					<td class="column1" align=right>原始数据是否入库:</td><td class="column"><input type="radio" name="intodb" value="1">是<input type="radio" name="intodb" value="0" checked>否<img src="../images/attention_2.gif" width="15" height="12"><span>注：建议不入库</span></td>
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
				<input type="hidden" name="device_model" value="<%=device_model%>">
				<input type="hidden" name="serial" value="<%=serial%>">
				<input type="button" value=" 保 存 " class="jianbian" name="save" onclick="javascript:CheckForm();">
				<input type="button" value=" 关 闭 " class="jianbian" name="close" onclick="javascript:window.close();">
			</td>
		</tr>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;<IFRAME id="childFrm" name="childFrm" SRC="" STYLE="display:none"></IFRAME></TD></TR>
</TABLE>
</FORM>
<SCRIPT LANGUAGE="JavaScript">
<!--
	 function CheckForm()
	{
		if(typeof(frm.snmp)!="object")
		{
			alert("您配置的设备类型没有配置snmp采集版本，请与管理员联系!");
			return;
		}

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
				var flag=false;
				var e;
				var ro;
				var ro_flag=false;
				var co;
				var jo_flag=false;
				  for (var i=0;i<frm.elements.length;i++)
				  {
					e = frm.elements[i];
					//如果选择了，则判断后面的东东有没有选择
					if (e.name == 'ifindex' && e.checked==true)
					{
						
						flag=true;
						ro=eval("frm.radio"+e.value);
						
						for(var j=0;j<ro.length;j++)
						{
							if(ro[j].checked)
							{
								ro_flag=true;
								break;
							}
						}

						if(!ro_flag)
						{
							window.alert("请选择端口的采集方式");
							return;
						}

						co=eval("frm.checkbox"+e.value);
						for(var j=0;j<co.length;j++)
						{
							if(co[j].checked)
							{
								jo_flag=true;
								break;
							}
						}
						if(!jo_flag)
						{
							window.alert("请选择端口要采集的性能");
							return;
						}						
					}				  
				  }			
				
				if(!flag)
				{
					window.alert("请选择端口！");
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
	//初始化一些默认参数
	function initLoad()
	{
		
		if(typeof(frm.snmp)=="object")
		{
			
			if(typeof(frm.snmp.length)!="undefined")
			{
				for(var i=0;i<frm.snmp.length;i++)
				{
					if(frm.snmp[i].value=="2")
					{
						frm.snmp[i].checked=true;
						break;
					}
				}
			}
			else
			{
				frm.snmp.checked=true;
			}
		
		}
	
	}
	initLoad();
//-->
</SCRIPT>
<%@ include file="../openfoot.jsp"%>