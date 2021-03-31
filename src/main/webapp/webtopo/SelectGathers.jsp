<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="DeviceAct" scope="session" class="com.linkage.litms.resource.DeviceAct" />
<%
	request.setCharacterEncoding("GBK");
	List m_ProcessesList = curUser.getUserProcesses();
	Map m_GaherIdNameMap = DeviceAct.getGatherIdNameMap();  
	long m_AreaId = user.getAreaId();
	ArrayList	m_Gathers = DeviceAct.getGathersWithAreaId(String.valueOf(m_AreaId));
	 
	Cursor c_GatherList = null;
	if(user.getAccount().equals("admin")){
		c_GatherList = DeviceAct.getGatherList();
	}
	else
	{
		
	}
	Map field = null;

	//拓扑类型
	String type = request.getParameter("type");
	
	//拓扑发现类型（add/增量发现、reset/重新发现）
	String oper = request.getParameter("oper");
	
	String cmd_name = "增量发现";
	if(oper.equals("add")){
		cmd_name = "增量发现";
	}else if(oper.equals("reset")){
		cmd_name = "重新发现";
	}
%>
<%@ include file="../head.jsp"%>
<title>请选择采集点</title>
<script language="javascript">
<!--
var isCall = -100;
function CallPro()
{
	switch(parseInt(isCall,10))
	{
		case 0:
		{
			window.alert("增量发现通知成功!");
			window.clearInterval(iTimerID);			
			window.close();
			break;
		}
		case -1:
		{
			window.alert("正在接收数据，不能重新开始发现");
			window.clearInterval(iTimerID);	
			isCall=-100;
			break;
		}
		case -2:
		{
			window.alert("通知后台发现拓扑失败");
			window.clearInterval(iTimerID);	
			isCall=-100;
			break;
		}
		case -3:
		{
			window.alert("从数据库中读取数据出错");
			window.clearInterval(iTimerID);	
			isCall=-100;
			break;
		}
	}	

}


function CheckForm()
{

	var co=frm.process;
	var jo_flag=false;
	
	for (var i=0;i<frm.elements.length;i++)
    {
		var e = frm.elements[i];
		if (e.name == 'process' && e.checked==true)
		{
			jo_flag=true;
			break;
		}
    }

	

	if(!jo_flag)
	{
		window.alert("请选择采集点");
		return;
	}				
	frm.submit();

	iTimerID = window.setInterval("CallPro()", 1000);
}

//-->	
</script>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
<FORM NAME="frm" METHOD="post" ACTION="./sayFindTopo.jsp?oper=<%=oper %>&type=<%=type %>" target="childFrm">
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				
              <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                
                <TR bgcolor="#FFFFFF"> 
                  <TD class=column>
                  <%
                  if(user.getAccount().equals("admin")){
                  	field = c_GatherList.getNext();
                  	while(field != null){%>
                  		<input type="checkbox" name="process" id="process" value="<%=field.get("gather_id")%>" <%=((m_Gathers.contains(field.get("gather_id")))?"checked":"")%>><%=field.get("descr")%>
                  <%
                  		field = c_GatherList.getNext();
                  	}
                  	field = null;
                  	c_GatherList = null;
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
					<input type="hidden" name="oper" value="add">
                    <INPUT TYPE="button" value="<%=cmd_name %>" class=btn onclick="javascript:CheckForm();">                   
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
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%> 
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@ include file="../foot.jsp"%>