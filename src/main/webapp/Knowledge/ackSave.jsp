<%@page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@include file="../timelater.jsp"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean"%>
<%@ page import="com.linkage.litms.knowledge.Knowledge"%>
<%@ page import="java.lang.Long"%>
<%@ page
	import="com.linkage.litms.webtopo.MCDataSource,com.linkage.litms.webtopo.warn.DeviceWarnInfo"%>
<%
            request.setCharacterEncoding("GBK");
            String creator = (String) user.getAccount();
            String subject = request.getParameter("subject");
            String content = request.getParameter("content");
            String warnReason = request.getParameter("warnReason");
            String warnResove = request.getParameter("warnResove");
            //String  createTime=request.getParameter("createTime");
            String deviceType = request.getParameter("deviceType");
            String sourceIp = request.getParameter("sourceIp");
            String creatorType = request.getParameter("creatorType");
            String gather_Id = request.getParameter("gatherId");
            String sourceName = request.getParameter("sourceName");
            String temp = request.getParameter("ack_CreateTime");
            long ack_CreateTime = Long.valueOf(temp).longValue() / 1000;
            Knowledge knowledge = new Knowledge(); //knowledge.id 系统生成
            knowledge.subject = subject;
            knowledge.content = content;
            knowledge.creator = creator;
            knowledge.warnReason = warnReason;
            knowledge.warnResove = warnResove;
            //knowledge 中增加的新属性
            knowledge.deviceType = Long.valueOf(deviceType).longValue();
            knowledge.sourceIp = sourceIp;
            knowledge.creatorType = Long.valueOf(creatorType).longValue();
            knowledge.gather_Id = gather_Id;
            knowledge.sourceName = sourceName;
            knowledge.ack_CreateTime = ack_CreateTime;
            int flag = 0;
            if (knowledge.add()) {
                String m_ObserverName = user.getAccount();
                MCDataSource ms = new MCDataSource(user.getAccount(), user
                        .getPasswd());
                String alarmStr = request.getParameter("alarmString");
                //===== duangrduangrduangr =====
                //String [] alarmList = {alarmStr.split(";")[0]};
              	//===== duangrduangrduangr =====
                String[] alarmList = null;
                if (alarmStr != null) {
                    alarmList = alarmStr.split(",");
                }
                //将告警确认信息写入 tab_faultalarm_YYYY_MM 表中
                //===== duangrduangrduangr =====
                //flag = ms.AckAlarm(alarmList,"");
                flag = ms.AckAlarm(alarmList);//Modified by duangr 2007-11-15 后台接口改变
                //===== duangrduangrduangr =====
                if (flag == 1) {
                    // 将告警信息写入告警事件表 event_raw_YYYY_周 
                    String tableName = request.getParameter("tableName");
                    String alarmId = request.getParameter("alarmId");
                    String sql = " update   " + tableName
                            + " set activestatus=4  where serialno='" + alarmId
                            + "'   and gather_id='" + gather_Id + "'";
                    flag = DataSetBean.executeUpdate(sql);
                    if (flag != 0)
                        flag = 1;
                }
                DeviceWarnInfo dwi = new DeviceWarnInfo();
                dwi.setOnlyWarnInfo(session);
                dwi.removeAlarmList(Arrays.asList(alarmList), session);
            }
%>
<html>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
</head>
<body>
<script language="javascript">
	  <!--
	  var flag="<%=flag%>"; 
	  if(flag!="0")
	  { 
	    opener.ack_Store_Warn_Result();
	    //changeColorOList(oList.currentRow.rowIndex,"backgroundColor:green;color:#000000;cursor:default");
	    //opener.document.all("oList").deleteRow(); 
	  	alert("告警确认成功");   
	  	window.close() ;
	  	var page="ackView.jsp?id=<%=knowledge.id%>";
      window.open(page,"告警查看","left=20,top=20,width=600,height=400,resizable=yes,scrollbars=yes"); 
	 } 
	 else{
	 	  alert("告警确认失败!");
      window.close() ;
	  } 	
   --></script>
</body>
</html>
