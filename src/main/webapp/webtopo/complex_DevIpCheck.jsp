<html>
<head>
<title>�����û��豸</title>
</head>
<SCRIPT LANGUAGE="JavaScript">
<!--

var isCall=0;
var iTimerID;

function CallPro()
{
	switch(parseInt(isCall,10))
	{
		case 1:
		{
			window.alert("IP״̬���óɹ���");
			window.clearInterval(iTimerID);	
			
			isCall=0;
			location.href="complex_DevIpCheck.jsp";
			break;
		}
		case -1:
		{
			window.alert("���ݿ����ʧ��,�����²�������ϵ����Ա��");
			window.clearInterval(iTimerID);
			
			isCall=0;
			break;
		}
		case 0:
		{
			window.alert("û�ж����ݿ�����κβ�����");
			window.clearInterval(iTimerID);
			
			isCall=0;
			break;
		}
		case 2:
		{
			window.alert("���ݿ�����ɹ������޷����̨ͨѶ������ϵ����Ա��");
			window.clearInterval(iTimerID);
			
			isCall=0;
			break;
		}
		case 3:
		{
			window.alert("�豸��¼��ɾ����");
			window.clearInterval(iTimerID);

			isCall=0;
			location.href="complex_DevIpCheck.jsp";
			break;

		}
		case 4:
		{
			window.alert("��¼ɾ�������޷����̨ͨѶ������ϵ����Ա��");
			window.clearInterval(iTimerID);
			
			isCall=0;
			break;
		}
	}

}

//ѡ��ȫ��
function selectAll(oStr){
	
	var oSelect = document.all(oStr);
	for(var i=0; i<oSelect.length; i++){
		oSelect[i].checked = frm.select_all.checked;
	}
}

//ȷ��
function CheckForm()
{
	
	var oSelect = document.all("select_one");
	var flag=false;
	
	for(var i=0; i<oSelect.length; i++){
		if(oSelect[i].checked)
		{
			flag=true;
			break;
		}		
	}
	if(!flag)
	{
		window.alert("������ѡ��һ̨�豸��");
		return false;
	
	}

	if(frm.alarm_mode.selectedIndex==0)
	{
		window.alert("��ѡ��澯��ʽ��");
		return false;
	}

	if(frm.alarm_grade.selectedIndex==0)
	{
		window.alert("��ѡ��澯�ȼ���");
		return false;
	}

	frm.submit();
	iTimerID = window.setInterval("CallPro()",1000);
}

//ɾ��
function CheckFormDel()
{
	var oSelect = document.all("select_one");
	var flag=false;
	
	for(var i=0; i<oSelect.length; i++){
		if(oSelect[i].checked)
		{
			flag=true;
			break;
		}		
	}
	if(!flag)
	{
		window.alert("������ѡ��һ̨�豸��");
		return false;
	
	}

	if (confirm("ȷ��ɾ����"))
	{
		frm.action="testipcheckDel.jsp";
		frm.submit();
		iTimerID = window.setInterval("CallPro()",1000);
	}
	else
	{
		return;
	}
}
//-->
</SCRIPT>
<body>

<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<form name="frm" method="post" action="testipcheckList.jsp" target="childFrm">
  <table width="95%"  border="0" cellspacing="0" cellpadding="0" align="center">
	<tr><td height="20"></td></tr>
    <tr>
      <td><table width="100%"  border="0" cellspacing="0" cellpadding="0">
		    <tr>
			<td bgcolor=#000000><table width="100%"  border="0" cellspacing="1" cellpadding="2">
			  <tr>
                <TH>�豸���</TH>
                <TH>�豸����</TH>
                <TH>�豸��ַ</TH>
                <TH>ѡ�� <input name="select_all" type="checkbox" onclick="javascript:selectAll('select_one')">��ȫѡ��</TH>
              </tr>
              

<%
	request.setCharacterEncoding("GBK");
	IpCheck ipconfig = new IpCheck();
	Cursor cursor_UserDevID = ipconfig.getIPCheckList(request);

	if(cursor_UserDevID != null) {
		Map map_UserDevID = cursor_UserDevID.getNext();
			
			if(map_UserDevID!=null) {
				String device_id = "";
				String deviceName ="";
				String loopbackip = "";
				String gather_id = "";
				
				while(map_UserDevID!=null) {
					device_id = (String)map_UserDevID.get("device_id");
					deviceName= (String)map_UserDevID.get("device_name");
					loopbackip= (String)map_UserDevID.get("loopback_ip");
					gather_id = (String)map_UserDevID.get("gather_id");
					
					out.println("<tr>");
					out.println("<td class=column1 align=center>" + device_id + "</td>");
					out.println("<td class=column1 align=center>" + deviceName + "</td>");
					out.println("<td class=column1 align=center>" + loopbackip + "</td>");
					out.println("<td class=column1 align=center> <input name=select_one type=checkbox value=" + device_id + "/" + gather_id +  "> </td>");
					out.println("</tr>");
					map_UserDevID = cursor_UserDevID.getNext();
				}
			}	
			else {
				out.println("<td colspan=5 class=column1 align=center>��ǰ�û�û�п��Թ�����豸��</td>");
			}
	}
	else {
		out.println("<td colspan=5 class=column1 align=center>��ǰ�û�û�п��Թ�����豸��</td>");
	}
%>
              <tr>
                <td colspan="2" class=column1 align="center">�澯��ʽ</td>
                <td colspan="3" class=column2 align="left">
				<select name="alarm_mode" style="width:145">
                    <option value="0">��ѡ��澯��ʽ</option>
                    <option value="1">ֻ����һ�θ澯��Ϣ</option>
                    <option value="2">�������͸澯��Ϣ</option>
                    </select>
			  	</td>
              </tr>
              <tr>
                <td colspan="2" class=column1 align="center">�澯�ȼ�</td>
                <td colspan="3" class=column2 align="left">
					<select name="alarm_grade" style="width:145">
				    <option value="0">��ѡ��澯�ȼ�</option>
					<option value="1">��־</option>
					<option value="2">��ʾ</option>
					<option value="3">һ��澯</option>
					<option value="4">���ظ澯</option>
					<option value="5">�����澯</option>
					</select>
				</td>
              </tr>
			
              <tr>
                <td colspan="5" class=foot align="center"><input name="delete" type="button" value=" ɾ  �� " onclick="javascript:CheckFormDel()" class="jianbian">&nbsp;&nbsp;<input name="ok_button" type="button" value=" ȷ  �� " onclick="javascript:CheckForm();" class="jianbian"></td>
                </tr>
            </table></td>
		    </tr>
		  </table>
	  </td>
    </tr>
	
	<tr><td><IFRAME ID=childFrm name="childFrm" SRC="" STYLE="display:none"></IFRAME></td></tr>
  </table>
</form>
<%@ include file="../foot.jsp"%>
</body>
</html>

