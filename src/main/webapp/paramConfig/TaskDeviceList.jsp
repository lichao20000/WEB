<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.*, java.util.*,java.text.*"%>
<html>
<head>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<title>�����豸�б�</title>
<%
	String task_id = request.getParameter("task_id");
	long taskId = 0L;
	//��ȡ�����豸�б�
	if(task_id != null){
		taskId = Long.valueOf(task_id);
	}
	Cursor cursor = BatchCongigDbAct.getDeviceListByTask(taskId);
	String strHTML = "";
	Map feilds = cursor.getNext();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	int excnum = 0, sucnum = 0, allnum = 0;
	//��������1��NTP,����ʱ�䣬������Ա�������豸��%,Ԥ�ڳɹ��ʣ�ʵ�ʳɹ��ʣ��Ƿ����ͨ������ɰٷֱ�
	if(feilds != null){
		while(feilds != null){
			allnum++;
			String device_serialnumber = (String)feilds.get("device_serialnumber");
			String is_start = (String)feilds.get("is_start");
			String start_time = (String)feilds.get("start_time");
			String end_time = (String)feilds.get("end_time");
			String is_succ = (String)feilds.get("is_succ");
			String dev_desc = (String)feilds.get("dev_desc");
			String redo_num = (String)feilds.get("redo_num");

			if("1".equals(is_start)){
				is_start = "�Ѿ�ִ��";
				excnum++;
			}else{
				is_start = "δִ��";
			}
			if(start_time != null && !"".equals(start_time)){
				start_time = sdf.format(Long.valueOf(start_time)*1000);
			}
			if(end_time != null && !"".equals(end_time)){
				end_time = sdf.format(Long.valueOf(end_time)*1000);
			}
			if("1".equals(is_succ)){
				is_succ = "�ɹ�";
				sucnum++;
			}else if("0".equals(is_succ)){
				is_succ = "ʧ��";
			}else{
				is_succ = "";
			}
			
			strHTML += "<tr>";
			strHTML += "<td class=column>" + device_serialnumber + "</td>";
			strHTML += "<td class=column>" + is_start + "</td>";
			strHTML += "<td class=column>" + start_time + "</td>";
			strHTML += "<td class=column>" + end_time + "</td>";
			strHTML += "<td class=column>" + redo_num + "</td>";
			strHTML += "<td class=column>" + is_succ + "</td>";
			strHTML += "<td class=column>" + dev_desc + "</td>";
			strHTML += "</tr>";
			feilds = cursor.getNext();
		}
	}

%>
</head>
<body>
<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD height=20>
			</TD>
		</TR>
		
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH>�豸��Ϣ</TH>
						<TH>�Ƿ�ִ��</TH>
						<TH>��ʼʱ��</TH>
						<TH>����ʱ��</TH>
						<TH>ִ�д���</TH>
						<TH>ִ�н��</TH>
						<TH>�������</TH>
					</TR>
					<%=strHTML%>
					<tr>
						<td class="green_foot" colspan=7 align="right">
							��������:<%=allnum %>(̨) &nbsp;&nbsp;&nbsp;&nbsp;
							�������:<%=excnum %>(̨) &nbsp;&nbsp;&nbsp;&nbsp;
							���óɹ�:<%=sucnum %>(̨)&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="button" onclick="window.location.reload();" value="ˢ ��">&nbsp;&nbsp;
							<input type="button" onclick="back();" value="�� ��">&nbsp;&nbsp;
						</td>
					</tr>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</body>
<script language="javascript">
<!--
	function back(){
		window.location = "BatchNTPConfig.jsp";
	}
//-->
</script>
</html>
<%@ include file="../foot.jsp"%>