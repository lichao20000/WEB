<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%
  long acc_oid = Long.parseLong(request.getParameter("acc_oid"));
  String area_name = request.getParameter("area_name");
  UserAct act = new UserAct();
  HashMap userMap = act.getUsersByAreaByUserID(acc_oid,area_name);
  Iterator iterator = userMap.keySet().iterator();
  String user_id ="";
  String user_name ="";
  String userSelectStr = null;
  if(iterator.hasNext())
  {
	  while(iterator.hasNext())
	  {		  
		  user_id = (String)iterator.next();
		  user_name = (String)userMap.get(user_id);
		  if(null==userSelectStr)
		  {
			  userSelectStr = "<select name='userSelect'>";
			  // NXDX-REQ-ITMS-20190613-LX-001(��־������¼��־����־��ѯ�Ż�) ƴһ���ڵ㣬��ѯ��������
			  if ("nx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))) {
				  userSelectStr += "<option value='-1'>��ѡ��</option>";
			  }
		  }
		  userSelectStr +="<option value='"+user_id+"'>"+user_name+"</option>";
	  }
	  userSelectStr +="</select>";
  }
  else
  {
	  userSelectStr ="<select name='userSelect'><option value='-1'>û�м�¼</option></select>";
  }  
  
%>
<%@page import="com.linkage.litms.system.dbimpl.UserAct"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<SCRIPT LANGUAGE="JavaScript">
var userSpanStr = "<%=userSelectStr%>";
parent.userSpan.innerHTML=userSpanStr;
</script>