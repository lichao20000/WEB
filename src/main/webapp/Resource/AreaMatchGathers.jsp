<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />
<%
request.setCharacterEncoding("GBK");
//获取用户权限范围内的采集机资源信息
List m_ProcessesList = curUser.getUserProcesses();

//获取系统内的所有采集机信息
Map m_GaherIdNameMap = DeviceAct.getGatherIdNameMap();  
String m_AreaId = request.getParameter("area_id");
//获取系统内的域m_AreaId下的采集机信息
ArrayList m_Gathers = DeviceAct.getGathersWithAreaId(m_AreaId);
%>
<%@ include file="../head.jsp"%>
<script language="javascript">
<!--
function saveGathersResult(flag){
	if(flag){
		alert("保存成功");
	}else{
		alert("保存失败");
	}
	window.close();
}
//-->	
</script>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
<FORM NAME="frm" METHOD="post" ACTION="./GathersSave.jsp" target="childFrm">
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				
              <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                <TR> 
                  <TH align="center">配置区域关联采集机</TH>
                </TR>
                <TR bgcolor="#FFFFFF"> 
                  <TD class=column>
                  <%
                  if(user.getAccount().equals("admin")){
                  Iterator it = m_GaherIdNameMap.keySet().iterator();
                  while(it.hasNext()){
                	  String gather_id = (String)it.next();
                	  String gatherDesc = (String)m_GaherIdNameMap.get(gather_id);
                  %>
                  		<input type="checkbox" name="process" id="process" value="<%=gather_id%>" <%=((m_Gathers.contains(gather_id))?"checked":"")%>><%=gatherDesc%>
                  <%                  		
                  	}                  	
                  }else{
	                  for(int k=0;k<m_ProcessesList.size();k++){%>
	                  		<input type="checkbox" name="process" id="process" value="<%=m_ProcessesList.get(k)%>" <%=((m_Gathers.contains(m_ProcessesList.get(k)))?"checked":"")%>><%=m_GaherIdNameMap.get(m_ProcessesList.get(k))%>
	                  <%}
	                }  
	                %>
                  	&nbsp; </TD>
                </TR>
                <TR> 
                  <TD align="center" class=foot> 
                    <INPUT TYPE="submit" value="保存配置" class=btn>
                    <INPUT TYPE="hidden" value="<%=m_AreaId%>" name=area_id>
                    &nbsp;&nbsp; </TD>
                </TR>
              </TABLE>
			</TD>
		</TR>
	</TABLE>
    </FORM>
  </TD></TR>
</TABLE>
<IFRAME name="childFrm" ID=childFrm SRC="" STYLE="display:none"></IFRAME>
<%@ include file="../foot.jsp"%>