<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.*, java.util.*,java.text.*"%>

<html>
<head>
<title>配置结果</title>
<%
//************************************执行批量配置******************************************************//
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
			//计算测试设备的数量
			if(intpercent <= 0){
				intpercent = 0;
			}else{
				testnum = (array_device_id.length * intpercent)%100;
				if (testnum != 0) {	//不能整除，则在结果上加1
					testnum = (array_device_id.length * intpercent)/100 + 1;
				}else{
					testnum = (array_device_id.length * intpercent)/100;
				}
			}
			//批量配置策略表入库
			int result = BatchCongigDbAct.saveTask(taskId,1,taskId,intpercent,
					Integer.valueOf(successpercent),param,userId,repeateNum);//1表示NTP配置
			if(result < 1){
				strHTML += "<tr>数据库操作失败(网关配置任务表)</tr>";
			}else{
				//批量配置策略设备表入库
				result = BatchCongigDbAct.saveDeviceTask(taskId, array_device_id);
				if(result < 1){
					strHTML += "<tr>数据库操作失败(网关配置任务设备表)</tr>";
				}else{
					//设置批量配置执行参数
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
			
			//strHTML += "<tr>正在进行配置，请稍后进行查询</tr>";
		} else {
			strHTML += "<tr><font color='red'>已经有批量配置在进行，请稍后再进行操作</font></tr>";
		}
	}
//************************************完成**************************************************//
	//获取批量配置任务列表
	Cursor cursor = BatchCongigDbAct.getAllConfigTask();
	Map feilds = cursor.getNext();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//配置类型1：NTP,定制时间，定制人员，测试设备数%,预期成功率，实际成功率，是否审核通过，完成百分比
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
				conf_type = "NTP配置";
			}
			
			if(time != null && !"".equals(time)){
				time = sdf.format(Long.valueOf(time)*1000);
			}else{
				time = "";
			}
			
			if("0".equals(audit_type)){
				audit_type = "不通过";
			}else if("1".equals(audit_type)){
				audit_type = "通过";
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
					+ "<a href='TaskDeviceList.jsp?task_id="+task_id+"'>执行详情</a>";
			strHTML += "&nbsp;&nbsp;";
			strHTML += "<a href='DeleteConfigTask.jsp?task_id="+task_id+"'>删除</td>";
			strHTML += "</tr>";
			feilds = cursor.getNext();
		}
		
	}else{
		strHTML += "<tr><td class=column>没有批量配置记录</td></tr>";
	}

%>
</head>
<body>
<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center" id="mytable">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
					<TR>
						<TH>配置类型</TH>
						<TH>定制时间</TH>
						<TH>定制人员</TH>
						<TH>测试设备比率(%)</TH>
						<TH>预期测试成功率(%)</TH>
						<TH>实际测试成功率(%)</TH>
						<TH>是否审核通过</TH>
						<TH>完成率(%)</TH>
						<TH>操作</TH>
					</TR>
					<%=strHTML%>
					<TR>
						<TD class="green_foot" colspan=9 align="right">
							<input type="button" onclick="window.location.reload()" value="刷新">
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