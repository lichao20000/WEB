<html>
<head>
<title>�豸Ping��Ѳ��ʼ��</title>
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
			window.alert("�豸�������óɹ���");
			window.clearInterval(iTimerID);	
			
			isCall = 0
			location.href="dev_pingpollinit.jsp";
			break;
		}
		case -1:
		{
			window.alert("���ݿ�����ɹ������޷����̨ͨѶ������ϵ����Ա��");
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
		window.alert("������ѡ��һ���豸���ͣ�");
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
//-->
</SCRIPT>

<body>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<form name="frm" method="post" action="testpollinit.jsp" target="childFrm">
	<table width="95%"  border="0" cellspacing="0" cellpadding="0" align="center">
		<tr><td colspan="4" height="20">&nbsp;&nbsp;&nbsp;</td></tr>
		<tr>
		<td><table width="100%"  border="0" cellspacing="0" cellpadding="0">
			<tr>
			<td bgcolor="#000000"><table width="100%"  border="0" cellspacing="1" cellpadding="2">
				<tr>
				<TH>���к�</TH>
				<TH>�豸����</TH>
				<TH>�豸����</TH>
				<TH>ѡ��<input name="select_all" type="checkbox" onclick="javascript:selectAll('select_one')">��ȫѡ��</TH>
			</tr>
			
			<%
				request.setCharacterEncoding("GBK");
				Dev_pollInit dev_pollinit = new Dev_pollInit(request);
				ArrayList serialList = dev_pollinit.get_DevSerialList();
				HashMap List_TypeNameMap = dev_pollinit.getTypeNameMap();
				String str_Devseria = null;
				ArrayList list_DevID = null; 
				HashMap devList = null;
				String str_TypeNameMap = null;
				String str_DevID = new String();
				int num = 0;

					if (!serialList.isEmpty()) {
						devList = dev_pollinit.get_DevList();

						if (!devList.isEmpty()) {
							
							for (int i=0; i<serialList.size(); i++) {
								num = i+1;
								str_Devseria = (String)serialList.get(i);
								str_TypeNameMap = (String)List_TypeNameMap.get(str_Devseria);
								list_DevID = (ArrayList)devList.get(str_Devseria);
                                
								for (int j=0; j<list_DevID.size(); j++) {

									str_DevID = str_DevID +(String)list_DevID.get(j) + ",";
								} 
								str_DevID = str_DevID.substring(0,str_DevID.length()-1);

								out.println("<tr>");
								out.println("<td class=column1 align=center>" + num + "</td>");
								out.println("<td class=column1 align=center>" + str_TypeNameMap + "</td>");
								out.println("<td class=column1 align=center>" + list_DevID.size() + "</td>");
								out.println("<td class=column1 align=center> <input name=select_one type=checkbox value=" + str_DevID + "></td>");
								out.println("</tr>");

								str_DevID = new String();
							}
						}
						else {
							out.println("<td colspan=4 class=column1 align=center>��ǰ�û�û�п������õ��豸���ͣ�</td>");
						}
					}
					else {
						out.println("<td colspan=4 class=column1 align=center>��ǰ�û�û�п������õ��豸���ͣ�</td>");
					}
			%>
			
			<tr>
			<td colspan="2" class=column1 align="center">�澯��ʽ</td>
			<td colspan="2" class=column2 align="left">
				<select name="alarm_mode" style="width:145">
				<option value="0">��ѡ��澯��ʽ</option>
				<option value="1">ֻ����һ�θ澯��Ϣ</option>
				<option value="2">�������͸澯��Ϣ</option>
				</select>
			</td>
			</tr>
		  
			<tr>
			<td colspan="2" class=column1 align="center">�澯�ȼ�</td>
			<td colspan="2" class=column2 align="left">
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
			<td colspan="4" class=foot align="center">
				<input name="ok_button" type="button" value=" ȷ  �� " onclick="javascript:CheckForm();" class="jianbian">
			</td>
			</tr>
			</table>
		</td>
		</tr>
		</table></td>
		</tr>
    
	<tr>
      <td><IFRAME ID=childFrm name="childFrm" SRC="" STYLE="display:none"></IFRAME></td>
    </tr>
	</table>
</form>
<%@ include file="../foot.jsp"%>
</body>
</html>
