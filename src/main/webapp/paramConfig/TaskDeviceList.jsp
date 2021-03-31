<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.paramConfig.*, java.util.*,java.text.*"%>
<html>
<head>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<title>任务设备列表</title>
<%
	String task_id = request.getParameter("task_id");
	long taskId = 0L;
	//获取任务设备列表
	if(task_id != null){
		taskId = Long.valueOf(task_id);
	}
	Cursor cursor = BatchCongigDbAct.getDeviceListByTask(taskId);
	String strHTML = "";
	Map feilds = cursor.getNext();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	int excnum = 0, sucnum = 0, allnum = 0;
	//配置类型1：NTP,定制时间，定制人员，测试设备数%,预期成功率，实际成功率，是否审核通过，完成百分比
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
				is_start = "已经执行";
				excnum++;
			}else{
				is_start = "未执行";
			}
			if(start_time != null && !"".equals(start_time)){
				start_time = sdf.format(Long.valueOf(start_time)*1000);
			}
			if(end_time != null && !"".equals(end_time)){
				end_time = sdf.format(Long.valueOf(end_time)*1000);
			}
			if("1".equals(is_succ)){
				is_succ = "成功";
				sucnum++;
			}else if("0".equals(is_succ)){
				is_succ = "失败";
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
						<TH>设备信息</TH>
						<TH>是否执行</TH>
						<TH>开始时间</TH>
						<TH>结束时间</TH>
						<TH>执行次数</TH>
						<TH>执行结果</TH>
						<TH>结果描述</TH>
					</TR>
					<%=strHTML%>
					<tr>
						<td class="green_foot" colspan=7 align="right">
							配置总数:<%=allnum %>(台) &nbsp;&nbsp;&nbsp;&nbsp;
							配置完成:<%=excnum %>(台) &nbsp;&nbsp;&nbsp;&nbsp;
							配置成功:<%=sucnum %>(台)&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="button" onclick="window.location.reload();" value="刷 新">&nbsp;&nbsp;
							<input type="button" onclick="back();" value="后 退">&nbsp;&nbsp;
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