<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	Flux_Config flux_con = new Flux_Config();
	flux_con.setRequest(request);
	Cursor cursor = flux_con.getDevResource();
	Map result = cursor.getNext();
	String device_id = null;
	String loopback_ip = null;
	String device_name = null;
	String device_model = null;
	String snmp_ro_community = null;
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function SelectAll()
{
	var str=new String();
	var len = frm.elements.length;
	var i;
	if(frm.select_all.checked)
	{
		for (i=0;i<len;i++ )
		{
			if (frm.elements[i].name=='select_one') {
				str += ","+frm.elements[i].value;
				if(str.length>2000)
				{
					alert("ȫѡ�豸����,��ֻ���������豸!");
					return false;
				}
				frm.elements[i].checked = true;
			}
		}
	}
	else
	{
		for (i=0;i<len;i++ )
		{
			if (frm.elements[i].name=='select_one') {
				frm.elements[i].checked = false;
			}
		}
	}
}

function SelectOneTrue()
{
	var len = frm.elements.length;
	var i;
	var flag;

	for(i=0; i<len; i++){
			if(frm.elements[i].name=='select_one')
			{
				if (frm.elements[i].checked)
				{
					flag=true;
					break;
				}
			}		
		}

		if(!flag)
		{	
			return false;	
		}
		else
		{
			return true;
		}
}

var isCall=0;
var iTimerID;

function CallPro()
{	
	switch(parseInt(isCall,10))
	{
		case 1:
		{
			window.alert("�豸ɾ���ɹ���");
			window.clearInterval(iTimerID);	
			
			isCall = 0
			location.reload();
			break;
		}
		case -1:
		{
			window.alert("����ʧ�ܣ������²�����");
			window.clearInterval(iTimerID);
			
			isCall=0;
			break;
		}
	}
}

function CheckForm(param)
{
	var len = frm.elements.length;
	var ip_add = new String();
	var i;
	var flag;
	var j =0;
	//alert(len);
	if (param == "query")
	{
		var loopback_ip = frm.ip_address.value.trim();
		
		if (IsIPAddr(loopback_ip))
		{
			var ip = frm.ip_address.value.trim();
			document.all("childFrm").src = "flux_devInfo.jsp?ip=" + ip;
		}
		return false;
	}
	if (!SelectOneTrue())
	{
		alert("��ѡ��һ����¼��");
		return false;
	}
	if (param == "Alter")
	{
		for (i=0;i<len;i++)
		{
			if (frm.elements[i].name=='select_one')
			{
				if (frm.elements[i].checked)
				{
					j++;
					if(j==1)
					ip_add = frm.elements[i].value.trim();
					else
					ip_add += ","+frm.elements[i].value.trim();
				}
			}
		}
		//ip_add = ip_add.substring(0,ip_add.length-1);
		if(ip_add.length>2000)
		{
			alert("ѡ���豸����,������ѡ��!");
			return false;
		}

		if (j == 1)
		{
			//alert(ip_add);
			page = "flux_DevPortList.jsp?dev_info=" + ip_add;
			otherpra = "channelmode=0,directories=0,location=no,menubar=no,resizable=no,scrollbars=yes,status=0,toolbar=0,height=500,width=600,top=200,left=200";
			window.open(page,"�豸�����ɼ�����",otherpra);
		}
		else
		{
			//alert(ip_add);
			page = "flux_DevConfigComplex.jsp?dev_info=" + ip_add;
			otherpra = "channelmode=0,directories=0,location=no,menubar=no,resizable=no,scrollbars=no,status=0,toolbar=0,height=320,width=500,top=200,left=200";
			window.open(page,"�豸�����ɼ�����",otherpra);
		}
	}

	if (param == "Del")
	{
		if(!window.confirm("ȷ��ɾ���豸��������?"))
		{
			return false;
		}
			frm.action = "action_delDev.jsp?";
			frm.submit();
			iTimerID = window.setInterval("CallPro()",1000);
	}
}

//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<form method="post" name="frm" target="childFrm">
<table width="95%"  border="0" cellspacing="0" cellpadding="0" align="center">
  <tr><TD HEIGHT=21>&nbsp;</TD></tr>
  
  <tr>
    <td bgcolor="#000000">
      <table width="100%"  border="0" cellspacing="1" cellpadding="2">
      	<TH>�豸�������ù���</TH>
      	<tr>
			    <td class="column">
			      <table width="100%" border="0" cellspacing="1" cellpadding="2">
			      	<tr>
			      		<td align="center">IP��ַ</td>
			    			<td align="left"><input name="ip_address" type="text"></td>
			    			<td align="right">
			    				<input value=" ��  ѯ " type="button" onclick="javascript:CheckForm('query');" class="jianbian">
			    			</td>
			      	</tr>
			      </table>	
			    </td> 
			  </tr>
      </table>
    </td> 
  </tr>
  <tr><td HEIGHT="2"></td></tr>
  <tr>
    <td bgcolor="#000000">
			<DIV id="idLayerView1">
			  <table width="100%"  border="0" cellspacing="1" cellpadding="2">
					<tr>
						<td class="column" align="center">�豸�ڲ�ID</td>
						<td class="column" align="center">�豸IP</td>
						<td class="column" align="center">�豸����</td>
						<td class="column" align="center">�豸����</td>
						<td class="column" align="center">������</td>
						<td class="column" align="center">ѡ��&nbsp;&nbsp;<input name="select_all" type="checkbox" onclick="javascript:SelectAll();">(ȫѡ)</td>
					</tr>
					<%
					//fix by wzm
						//int t=0;
						while (result != null) {
							device_id = (String)result.get("device_id");
							loopback_ip = (String)result.get("loopback_ip");
							device_name = (String)result.get("device_name");
							device_model = (String)result.get("device_model");
							snmp_ro_community = (String)result.get("snmp_ro_community");
							out.println("<tr>");
							out.println("<td class=\"column\" align=\"left\">" + device_id + "</td>");
							out.println("<td class=\"column\" align=\"left\">" + loopback_ip + "</td>");
							out.println("<td class=\"column\" align=\"left\">" + device_name + "</td>");
							out.println("<td class=\"column\" align=\"left\">" + device_model + "</td>");
							out.println("<td class=\"column\" align=\"left\">" + snmp_ro_community + "</td>");
							out.println("<td class=\"column\" align=\"center\"><input name=\"select_one\" type=\"checkbox\" value=\"" + device_id + "/" + device_name + "/" + loopback_ip +"\""+ " /></td>");
							out.println("</tr>");
							result = cursor.getNext();
							//fix by wzm
							//t++;
						}
						
					%>
				</table>
			</div>
		</td>
  </tr>
  <tr><td HEIGHT="2"></td></tr>
  <tr>
    <td bgcolor="#000000">
    	<table width="100%"  border="0" cellspacing="1" cellpadding="2">
    		<tr>
    			<td class=blue_foot align="center">
    				<input type="button" value=" �� �� " class="jianbian" onclick="javascript:CheckForm('Alter');">&nbsp;&nbsp;
  					<input type="button" value=" ɾ �� " class="jianbian" onclick="javascript:CheckForm('Del');">&nbsp;&nbsp;
    			</td>
    		</tr>
    	</table>
  	</td>
  </tr>
</table>
<IFRAME ID="childFrm" name="childFrm" STYLE="display:none"></IFRAME>
</form>
<%@ include file="../openfoot.jsp"%>