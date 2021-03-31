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
	
	//ArrayList index 0 标识位：1 已配置采集，0未配置采集；
	//			index 1 Cursor；
	array = qosConfig.getQOSDeviceConfig();
	
	Cursor cursor = null;
	Cursor[] myCursor = null;
	Map fields = null;
	boolean flag = false;
	String msg = "";
	String msg1 = "";
	
	if(array.get(0).equals("0")){
		msg = "此设备暂未进行QOS配置，现已通知后台配置。请稍后进行查看。";
		flag = true;
	}
	else{
		cursor = (Cursor)array.get(1);
		if(cursor==null||cursor.getRecordSize()==0){
			msg = "暂无QOS配置信息，请稍后进行查看或通知后台重新采集。";
			msg1 = "暂无该设备各QOS配置对象描述信息，请稍后进行查看或通知后台重新采集。";
		}			
		else{
			fields = cursor.getNext();
			//获得设备所有QOS配置服务对象的实例信息
			myCursor = qosConfig.getAllQOSConfigObjectInfo();
			if(myCursor==null||cursor.getRecordSize()==0)
				msg1 = "暂无该设备各QOS配置对象描述信息，请稍后进行查看或通知后台重新采集。";
		}
	}
		
	
	if(flag){
		//关闭窗口
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
		<TH>设备&nbsp;〖<%=title%>〗&nbsp;的QOS配置信息</TH>
	</TR>
	<TR >
		<TD bgcolor=#000000 >
		<table width="100%" border=0 cellspacing=1 cellpadding=1 weigth="100%" valign="bottom">
		<TR>
			<TD bgcolor="#ffffff" colspan="4">此设备各端口的QOS配置实例信息</TD>
			<td bgcolor="#ffffff" align="center"><input type="button" value=" 重新采集 " onclick="callQOSConfigDev(<%=str_device_id%>)"></td>
		</TR>
		<TR> 
			<TH>端口索引</TH>
			<TH>端口描述</TH>
			<TH>QOS索引</TH>
			<TH>策略方向</TH>
			<TH>查看指令描述</TH>
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
			<TD bgcolor="#ffffff" align="center"><input type="button" value=" 查看 " onclick="getQosDetail(<%=str_device_id%>,<%=fields.get("qosindex")%>)" class=btn></TD>
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
			<TD bgcolor="#ffffff" colspan="4">此设备所配置的各QOS对象实例描述信息</TD>
		</TR>
		<%
		//未配置任何QOS对象
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
				//某QOS对象未配置
				if(cursor==null||cursor.getRecordSize()==0){
					msg1 = "对此设备暂未配置 "+qosobjectName+" 对象实例。";
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
						
						if(strdesc.equals(""))//第一次
							strdesc = tem_strdesc;
						else if(count%2!=0)//换行
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
//QOS配置命令描述呈现
function getQosDetail(v1,v2){
	//新增用户信息时
	var page="getQosDetail.jsp?device_id="+v1+"&qosindex="+v2+"&tt="+new Date().getTime();
	document.all("childFrm").src=page;
}

function callQOSConfigDev(v){
	//新增用户信息时
	var page="callQOSConfigDev.jsp?device_id="+v+"&tt="+new Date().getTime();
	document.all("childFrm").src=page;
}
//-->
</script>
