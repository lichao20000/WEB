<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.linkage.litms.vipms.flux.ManagerFluxConfig"%>
<%@ page import ="com.linkage.litms.webtopo.common.*"%>
<%
ManagerFluxConfig fluxConfig = new ManagerFluxConfig(request);
			String device_id = request.getParameter("device_id");
			String action = request.getParameter("action");
			int resultCode=0;
			if(null!=action&&"delete".equals(action))
			{
				resultCode =fluxConfig.deleteFluxportConfig();
			}
			
			//������Դ
			String srcType = request.getParameter("type");
			if(null==srcType)
			{
				srcType ="1";
			}
			HashMap deviceInfo = fluxConfig.getDeviceInfo(device_id);
			String device_name = "";
			String loopback_ip = "";
			if (null != deviceInfo.get("device_name")) 
			{
				device_name = (String) deviceInfo.get("device_name");
			}

			if (null != deviceInfo.get("loopback_ip")) {
				loopback_ip = (String) deviceInfo.get("loopback_ip");
			}

			Cursor cursor = fluxConfig.getDeviceConfigPort(device_id);
			//cursor.sort("ifindex","asc");
			Map fields = cursor.getNext();
			boolean isHasPort = false;
%>
<SCRIPT LANGUAGE="JavaScript" src="../Js/CheckForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<%if(null!=action&&"delete".equals(action))
{
	if(0==resultCode)
	{
		out.println("alert(\"ɾ���˿����óɹ���\");");
	}
	else if(-1==resultCode)
	{
		out.println("alert(\"��������ɾ��ʧ�ܣ�\");");
	}
	else if(-2==resultCode)
	{
		out.println("alert(\"���ݿ�ɾ��ʧ�ܣ�\");");
	}
	else
	{
		out.println("alert(\"֪ͨ��̨ʧ�ܣ�\");");
	}
}
%>
function checkAllPort(name)
{
   var isCheck = document.all("all").checked;
   var objects = document.getElementsByName(name);
   for(var i=0;i<objects.length;i++)
   {
      objects[i].checked=isCheck;
   }
}

function modifyConfig()
{   
   if(!checkAll('port'))
   {
     alert("��ѡ��˿�!");
     return false;
   }  
   
   frm.submit();
}


function deleteConfig()
{
  if(!checkAll('port'))
   {
     alert("��ѡ��˿�!");
     return false;
   }
  
   frm.action="webtop_devicePortsList.jsp?action=delete";
   frm.submit();
}

</SCRIPT>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD>
		<FORM NAME="frm" METHOD="post" action="webtop_devicePortModify.jsp">		
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
			align="center">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				    <TR>
				       <TH colspan=8 align=center><%=device_name%>[<%=loopback_ip%>]�˿��б�</TH>
				    </TR>				    
					<TR>
					    <TH>&nbsp;&nbsp;</TH>
						<TH>�˿�����</TH>
						<TH>�˿�����</TH>
						<TH>�˿�����</TH>
						<TH>�˿ڱ���</TH>
						<TH>�˿�IP</TH>
						<TH>�˿ڱ�ʶ��ʽ</TH>
						<TH>�������</TH>
					</TR>
					<%
					if(null==fields)
					{
						out.println("<TR><TD class=\"column1\" colspan=8>û�ж�Ӧ�Ķ˿�����</TD></TR>");
					}
					else
					{
						isHasPort = true;
						String getwayStr="";						
						int getway=0;						
						int gatherflag=1;
						int ifconfigflag=1;
						while(null!=fields)
						{
							getway = Integer.parseInt((String) fields.get("getway"));
							gatherflag=Integer.parseInt((String)fields.get("gatherflag"));
							ifconfigflag=Integer.parseInt((String)fields.get("ifconfigflag"));
							switch (getway)
							{
								case 1:
								{
									getwayStr=getway + ":����";									
									break;
								}
								case 2:
								{
									getwayStr=getway + ":����";									
									break;
								}
								case 3:
								{
									getwayStr=getway + ":����";									
									break;
								}
								case 4:
								{
									getwayStr=getway + ":����";									
									break;
								}
								case 5:
								{
									getwayStr=getway + ":�˿�IP";									
									break;
								}
								default:
									getwayStr=getway + ":����";								    
							}
							out.println("<TR>");
							out.println("<TD class=\"column1\"><input type=\"checkbox\" name=\"port\" value=\""+getway+"|||"
									+((String)fields.get("port_info")).replaceAll("\"","\\$#!").replaceAll("'","#\\$!")+"\"></TD>");
							out.println("<TD class=\"column1\">"+fields.get("ifindex")+"</TD>");
							out.println("<TD class=\"column1\">"+("null".equalsIgnoreCase((String)fields.get("ifdescr"))?"":(String)fields.get("ifdescr"))+"</TD>");
							out.println("<TD class=\"column1\">"+("null".equalsIgnoreCase((String)fields.get("ifname"))?"":(String)fields.get("ifname"))+"</TD>");
							out.println("<TD class=\"column1\">"+("null".equalsIgnoreCase((String)fields.get("ifnamedefined"))?"":(String)fields.get("ifnamedefined"))+"</TD>");
							out.println("<TD class=\"column1\">"+("null".equalsIgnoreCase((String)fields.get("ifportip"))?"":(String)fields.get("ifportip"))+"</TD>");
							out.println("<TD class=\"column1\">"+getwayStr+"</TD>");
							if(1!=gatherflag||1!=ifconfigflag)
							{
								out.println("<TD class=\"column1\">δ����</TD>");
							}
							else
							{
								out.println("<TD class=\"column1\">������</TD>");
							}
							out.println("</TR>");
							fields = cursor.getNext();
						}
						out.println("<TR><TD colspan=8 class=\"column1\"><input type=\"checkbox\" name=\"all\" onclick=\"checkAllPort('port')\">ȫѡ</TD></TR>");
					}
					
					//clear
					fields = null;
					cursor = null;
					deviceInfo = null;					
					%>
					<TR>
					  <TD class="column1" align="right" colspan=8>
					    <input type="hidden" name="device_id" value="<%=device_id %>">
					    <input type="hidden" name="device_ip" value="<%=loopback_ip %>">
					    <input type="hidden" name="type" value="<%=srcType%>">					    
					    <%if(isHasPort) {%>
					    <input type="button" value=" �� �� " class="jianbian" name="save" onclick="javascript:modifyConfig();">&nbsp;&nbsp;
					    <input type="button" value=" ɾ �� " class="jianbian" name="delete" onclick="javascript:deleteConfig();">&nbsp;&nbsp;
					    <%} %>
				        <input type="button" value=" �� �� " class="jianbian" name="close" onclick="javascript:window.close();">
					  </TD>
				    </TR>
			  </TABLE>
		      </TD>
	       </TR>
       </TABLE>
       </FORM>       
    </TD>
  </TR>
</TABLE>

					