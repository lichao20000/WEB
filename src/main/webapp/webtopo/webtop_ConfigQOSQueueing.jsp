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
String strCongfig="δ����";
boolean isConfig=mfc.isConfig();
if(isConfig) {
	strCongfig="������";

}
%>
<%@ include file="../head.jsp"%>
<TITLE>�豸QOS����</TITLE>

<FORM NAME="frm" METHOD="post" action="webtop_ConfigQOSQueueing_Save.jsp" target="childFrm">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD>
	
        <table width="100%" height="30" border="0" align="center" cellpadding="0" cellspacing="0"  class="blue_gargtd">
          <tr> 
            <td width="162"><div align="center" class="title_bigwhite">�豸QOS������������</div></td>
            <td><img src="../images/attention_2.gif" width="15" height="12">&nbsp;<%=deviceName%>[<%=loopbackip%>]</td>
            
          </tr>
        </table>
        <TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                <TR> 
					<td class="column1" align=right>snmp�ɼ��汾:</td><td class="column">
						<%
							for(int i=0;i<snmpList.size();i++)
							{
								out.println("<input type=\"radio\" name=\"snmp\" value="+snmpList.get(i)+">V"+snmpList.get(i)+"&nbsp;&nbsp;");			
							}
						%>
					</td>
                </TR>
				<TR> 
					<td class="column1" align=right>��������:</td><td class="column">
					<input type="radio" name="istotal" value="1" checked>��
					<input type="radio" name="istotal" value="0">��  <img src="../images/attention_2.gif" width="15" height="12">ע����˿ڹ����뾡��ѡ����������</td>
                </TR>
				<TR> 
					<td class="column1" align=right>�ɼ����ʱ��:</td><td class="column"><input type="text" name="polltime" value="300" size="8">��  <img src="../images/attention_2.gif" width="15" height="12"><span>ע���ɼ����ʱ����300�����ұȽϺ���</span></td>
                </TR>
				<TR> 
					<td class="column1" align=right>ԭʼ�����Ƿ����:</td><td class="column"><input type="radio" name="intodb" value="1">��<input type="radio" name="intodb" value="0" checked>��<img src="../images/attention_2.gif" width="15" height="12"><span>ע�����鲻���</span></td>
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
				<input type="hidden" name="device_model" value="<%=device_model%>">
				<input type="hidden" name="serial" value="<%=serial%>">
				<input type="button" value=" �� �� " class="jianbian" name="save" onclick="javascript:CheckForm();">
				<input type="button" value=" �� �� " class="jianbian" name="close" onclick="javascript:window.close();">
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
			alert("�����õ��豸����û������snmp�ɼ��汾���������Ա��ϵ!");
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
			//�������������
			case 0:
			{
				if(typeof(frm.ifindex)!="object")
				{
					alert("��ѡ����豸û�вɼ����˿���Ϣ����ʱ������������");
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
					//���ѡ���ˣ����жϺ���Ķ�����û��ѡ��
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
							window.alert("��ѡ��˿ڵĲɼ���ʽ");
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
							window.alert("��ѡ��˿�Ҫ�ɼ�������");
							return;
						}						
					}				  
				  }			
				
				if(!flag)
				{
					window.alert("��ѡ��˿ڣ�");
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
	//��ʼ��һЩĬ�ϲ���
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