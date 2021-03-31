<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import ="com.linkage.litms.common.database.Cursor, com.linkage.litms.common.database.DataSetBean,java.util.*,com.linkage.litms.webtopo.QOSConfigInfoAct"%>
<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<%
	request.setCharacterEncoding("GBK");
	String str_device_id = request.getParameter("device_id");
	String title = request.getParameter("title");

	ArrayList array = new ArrayList();
	array.clear();
	QOSConfigInfoAct qosConfig = new QOSConfigInfoAct(request);
	LinkedHashMap QOSObject_type = QOSConfigInfoAct.getQOSObject_type();
	
	//ArrayList index 0 ��ʶλ��1 �����òɼ���0δ���òɼ���
	//			index 1 Cursor��
	array = qosConfig.getQOSDeviceConfig();
	
	Cursor cursor = null;
	Cursor[] myCursor = null;
	Map fields = null;
	boolean flag = false;
	String msg = "";
	String msg1 = "";
	
	if(array.get(0).equals("0")){
		msg = "���豸��δ����QOS���ã�����֪ͨ��̨���á����Ժ���в鿴��";
		flag = true;
	}
	else{
		cursor = (Cursor)array.get(1);
		if(cursor==null||cursor.getRecordSize()==0){
			msg = "����QOS������Ϣ�����Ժ���в鿴��֪ͨ��̨���²ɼ���";
			msg1 = "���޸��豸��QOS���ö���������Ϣ�����Ժ���в鿴��֪ͨ��̨���²ɼ���";
		}			
		else{
			fields = cursor.getNext();
			//����豸����QOS���÷�������ʵ����Ϣ
			myCursor = qosConfig.getAllQOSConfigObjectInfo();
			if(myCursor==null||cursor.getRecordSize()==0)
				msg1 = "���޸��豸��QOS���ö���������Ϣ�����Ժ���в鿴��֪ͨ��̨���²ɼ���";
		}
	}
		
	
	if(flag){
		//�رմ���
		%>
		<script language="JavaScript">
		<!--
		alert("<%=msg%>");
		window.close();
		//-->
		</script>
		<%
	}
%>
<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
	<TR>
		<TD>
<table width="100%" border="0" align="center">
	<TR>
		<TH>�豸&nbsp;��<%=title%>��&nbsp;��QOS������Ϣ</TH>
	</TR>
	<TR >
		<TD bgcolor=#000000 >
		<table width="100%" border=0 cellspacing=1 cellpadding=1 weigth="100%" valign="bottom">
		<TR>
			<TD bgcolor="#ffffff" colspan="4">���豸���˿ڵ�QOS����ʵ����Ϣ</TD>
			<td bgcolor="#ffffff" align="center"><input type="button" value=" ���²ɼ� " onclick="callQOSConfigDev(<%=str_device_id%>)"></td>
		</TR>
		<TR> 
			<TH>�˿�����</TH>
			<TH>�˿�����</TH>
			<TH>QOS����</TH>
			<TH>���Է���</TH>
			<TH>�鿴ָ������</TH>
		</TR>
		<%
		if(cursor==null||cursor.getRecordSize()==0){
			%>
		<TR>
			<TD bgcolor="#ffffff" colspan="5" align="center"><%=msg%></TD>
		</TR>
			<%
		}else{
			String tem_s = "";
			while(fields != null){
				if(((String)fields.get("policydirection")).equals("1"))
					tem_s = "input";
				else
					tem_s = "output";
				%>
		<TR>
			<TD bgcolor="#ffffff" align="center"><%=fields.get("ifindex")%></TD>
			<TD bgcolor="#ffffff" align="center"><%=fields.get("ifdesc")%></TD>
			<TD bgcolor="#ffffff" align="center"><%=fields.get("qosindex")%></TD>
			<TD bgcolor="#ffffff" align="center"><%=tem_s%></TD>
			<TD bgcolor="#ffffff" align="center"><input type="button" value=" �鿴 " onclick="getQosDetail(<%=str_device_id%>,<%=fields.get("qosindex")%>)" class=btn></TD>
		</TR>
				<%				
				fields = cursor.getNext();
			}
		}
		fields=null;
		cursor=null;
		%>
		</table>
		</TD>
	</TR>
	<tr><td bgcolor="#ffffff"><br></td></tr>
	<TR>
		<TD bgcolor=#000000>
		<table width="100%" border=0 cellspacing=1 cellpadding=1>
		<TR>
			<TD bgcolor="#ffffff" colspan="4">���豸�����õĸ�QOS����ʵ��������Ϣ</TD>
		</TR>
		<%
		//δ�����κ�QOS����
		if(myCursor==null){
			%>
		<TR>
			<TD bgcolor="#ffffff" colspan="5" align="center"><%=msg1%></TD>
		</TR>
			<%
		}else{
			
			String qosobjectstype = "";
			String qosobjectName = "";
			String strdesc = "";
			
			java.util.Iterator keyValuePairs = QOSObject_type.entrySet().iterator();
			for(int j =0;j<QOSObject_type.size();j++){
				Map.Entry entry = (Map.Entry) keyValuePairs.next();
				qosobjectstype = (String)entry.getKey();
				qosobjectName = (String)entry.getValue();
				%>
		<TR> 
			<TH><%=qosobjectName%></TH>
		</TR>
				<%
				cursor = myCursor[Integer.parseInt(qosobjectstype)-1];
				//ĳQOS����δ����
				if(cursor==null||cursor.getRecordSize()==0){
					msg1 = "�Դ��豸��δ���� "+qosobjectName+" ����ʵ����";
				%>
		<TR>
			<TD bgcolor="#ffffff"><%=msg1%></TD>
		</TR>
				<%
				}else{
					fields = cursor.getNext();					
					int count = 0;
					String tem_strdesc = "";
					
					while(fields != null){
						if(qosobjectName.equals("classmap")){
							tem_strdesc = (String)fields.get("strdesc") + "/" + (String)fields.get("strdesc1");
						}else
							tem_strdesc = (String)fields.get("strdesc");
						
						if(strdesc.equals(""))//��һ��
							strdesc = tem_strdesc;
						else if(count%2!=0)//����
							strdesc += "\t\t"+tem_strdesc+"<br>";
						else
							strdesc += tem_strdesc;

						fields = cursor.getNext();
						count++;
					}//end of while
						
				%>
					<TR>
						<TD bgcolor="#ffffff"><%=strdesc%></TD>
					</TR>
				<%
					strdesc = "";
				}
			}// end of for
		}// end of else
		fields=null;
		cursor=null;
		%>
		</table>
		</TD>
	</TR>
</TABLE>
</TD></TR></TABLE>
<IFRAME ID="childFrm" name="childFrm" SRC="" STYLE="display:none"></IFRAME>
<script language="JavaScript">
<!--
//QOS����������������
function getQosDetail(v1,v2){
	//�����û���Ϣʱ
	var page="getQosDetail.jsp?device_id="+v1+"&qosindex="+v2+"&tt="+new Date().getTime();
	document.all("childFrm").src=page;
}

function callQOSConfigDev(v){
	//�����û���Ϣʱ
	var page="callQOSConfigDev.jsp?device_id="+v+"&tt="+new Date().getTime();
	document.all("childFrm").src=page;
}
//-->
</script>
