<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.*, java.util.*,java.text.*"%>

<html>
<head>
<title>���ý��</title>
<%
//************************************ִ����������******************************************************//
	String strHTML = "";

	
	String[] array_device_id = request.getParameterValues("device_id");
	String configType = request.getParameter("type");
	String status = request.getParameter("status");
	String main_ntp_server = request.getParameter("main_ntp_server");
	String second_ntp_server = request
			.getParameter("second_ntp_server");
	String successpercent = request.getParameter("successpercent");
	String repeatnum = request.getParameter("repeatnum");
	String testpercent = request.getParameter("testpercent");

	long taskId = (new Date()).getTime()/1000;
	String param = main_ntp_server + "|||" + second_ntp_server;
	long userId = curUser.getUser().getId();
	int testnum = 1, intpercent = 0, repeateNum = 0;
	if(testpercent != null && !"".equals(testpercent)){
		intpercent = Integer.valueOf(testpercent);
	}
	
	if(repeatnum != null && !"".equals(repeatnum)){
		repeateNum = Integer.valueOf(repeatnum);
	}
	
	if(repeateNum <= 0){
		repeateNum = 1;
	}else if (repeateNum > 5){
		repeateNum = 5;
	}
	
	if(array_device_id != null && array_device_id.length > 0){
		if (NTPConfigPram.lockParam()) {
			//��������豸������
			if(intpercent <= 0){
				intpercent = 0;
			}else{
				testnum = (array_device_id.length * intpercent)%100;
				if (testnum != 0) {	//�������������ڽ���ϼ�1
					testnum = (array_device_id.length * intpercent)/100 + 1;
				}else{
					testnum = (array_device_id.length * intpercent)/100;
				}
			}
			//�������ò��Ա����
			int result = BatchCongigDbAct.saveTask(taskId,1,taskId,intpercent,
					Integer.valueOf(successpercent),param,userId,repeateNum);//1��ʾNTP����
			if(result < 1){
				strHTML += "<tr>���ݿ����ʧ��(�������������)</tr>";
			}else{
				//�������ò����豸�����
				result = BatchCongigDbAct.saveDeviceTask(taskId, array_device_id);
				if(result < 1){
					strHTML += "<tr>���ݿ����ʧ��(�������������豸��)</tr>";
				}else{
					//������������ִ�в���
					NTPConfigPram.taskId = taskId;
					NTPConfigPram.array_device_id = array_device_id;
					NTPConfigPram.configType = configType;
					NTPConfigPram.status = status;
					NTPConfigPram.main_ntp_server = main_ntp_server;
					NTPConfigPram.second_ntp_server = second_ntp_server;
					NTPConfigPram.successpercent = Integer.valueOf(successpercent);
					NTPConfigPram.repeatnum = repeateNum;
					NTPConfigPram.testnum = testnum;
					NTPConfigPram.successCount = 0;
					NTPConfigPram.excutCount = 0;
					NTPConfigPram.hasNext = -1;
				}
			}
			
			//strHTML += "<tr>���ڽ������ã����Ժ���в�ѯ</tr>";
		} else {
			strHTML += "<tr><font color='red'>�Ѿ������������ڽ��У����Ժ��ٽ��в���</font></tr>";
		}
	}
//************************************���**************************************************//
	//��ȡ�������������б�
	Cursor cursor = BatchCongigDbAct.getAllConfigTask();
	Map feilds = cursor.getNext();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//��������1��NTP,����ʱ�䣬������Ա�������豸��%,Ԥ�ڳɹ��ʣ�ʵ�ʳɹ��ʣ��Ƿ����ͨ������ɰٷֱ�
	if(feilds != null){
		
		while(feilds != null){
			
			String task_id = (String)feilds.get("task_id");
			String conf_type = (String)feilds.get("conf_type");
			String time = (String)feilds.get("time");
			String acc_loginname = (String)feilds.get("acc_loginname");
			String test_dev_perc = (String)feilds.get("test_dev_perc");
			String expe_succ_perc = (String)feilds.get("expe_succ_perc");
			String real_succ_perc = (String)feilds.get("real_succ_perc");
			String audit_type = (String)feilds.get("audit_type");
			String ok_perc = (String)feilds.get("ok_perc");
			
			if("1".equals(conf_type)){
				conf_type = "NTP����";
			}
			
			if(time != null && !"".equals(time)){
				time = sdf.format(Long.valueOf(time)*1000);
			}else{
				time = "";
			}
			
			if("0".equals(audit_type)){
				audit_type = "��ͨ��";
			}else if("1".equals(audit_type)){
				audit_type = "ͨ��";
			}else{
				audit_type = "";
			}
			
			strHTML += "<tr>";
			strHTML += "<td class=column align=center>" + conf_type + "</td>";
			strHTML += "<td class=column align=center>" + time + "</td>";
			strHTML += "<td class=column align=center>" + acc_loginname + "</td>";
			strHTML += "<td class=column align=center>" + test_dev_perc + "</td>";
			strHTML += "<td class=column align=center>" + expe_succ_perc + "</td>";
			strHTML += "<td class=column align=center>" + real_succ_perc + "</td>";
			strHTML += "<td class=column align=center>" + audit_type + "</td>";
			strHTML += "<td class=column align=center>" + ok_perc + "</td>";
			strHTML += "<td class=column align=center>"
					+ "<a href='TaskDeviceList.jsp?task_id="+task_id+"'>ִ������</a>";
			strHTML += "&nbsp;&nbsp;";
			strHTML += "<a href='DeleteConfigTask.jsp?task_id="+task_id+"'>ɾ��</td>";
			strHTML += "</tr>";
			feilds = cursor.getNext();
		}
		
	}else{
		strHTML += "<tr><td class=column>û���������ü�¼</td></tr>";
	}

%>
</head>
<body>
<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center" id="mytable">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
					<TR>
						<TH>��������</TH>
						<TH>����ʱ��</TH>
						<TH>������Ա</TH>
						<TH>�����豸����(%)</TH>
						<TH>Ԥ�ڲ��Գɹ���(%)</TH>
						<TH>ʵ�ʲ��Գɹ���(%)</TH>
						<TH>�Ƿ����ͨ��</TH>
						<TH>�����(%)</TH>
						<TH>����</TH>
					</TR>
					<%=strHTML%>
					<TR>
						<TD class="green_foot" colspan=9 align="right">
							<input type="button" onclick="window.location.reload()" value="ˢ��">
							&nbsp;&nbsp;
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
</TABLE>
</body>
<script language="javascript">
<!--
	parent.div_config.innerHTML = document.getElementById("mytable").innerHTML;
	
//-->
</script>
</html>