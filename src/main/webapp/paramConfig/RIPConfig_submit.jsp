<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@page import="com.linkage.litms.paramConfig.RIPConfig"%>
<%          
            RIPConfig ripConfig = new RIPConfig();
			String device_id = request.getParameter("device_id");			
			String configWay = request.getParameter("configWay");
			ArrayList list = new ArrayList();
			boolean isConfig =true;
			//TR09��ʱ��ʵ��
			if ("1".equals(configWay))
			{
			}
			else
			{
				isConfig = ripConfig.isConfigSnmp(device_id);
				//���������豸��������ܲɼ����豸�˿���Ϣ
				if (isConfig)
				{
					list = ripConfig.getDeviceInfo(device_id);
				}
			}
			%>
<DIV id="idLayer" style="width:100%;height:100%">
<table width="100%" border=0 align="center" cellpadding="1" cellspacing="1" bgcolor="#999999" class="text">
	<%	
		if(null==list||0==list.size())
		{
			out.println("<TR bgcolor=\"#FFFFFF\"><TD>û�ж˿���Ϣ</TD></TR>");
		}
		else
		{
			String color = "";					
			String fontColor = "white";
			String manage_state = null;
			String run_state = null;
			for(int i=0;i<list.size();i++)
			{
				String[] str=(String[])list.get(i);
				int itemp = ((String[])list.get(0)).length;
				if(i==0)
				{
					out.println("<tr class='green_title'>");
				}
				else
				{
					if(str != null && str.length > 7){
						manage_state = str[6].trim();
						run_state = str[7].trim();
						if(manage_state.equals(run_state))
						{
							color = "#C5E3B9";
							fontColor = "#000000";
						}
						else if(manage_state.equals("Up") && run_state.equals("Down"))
						{
							color = "#FE7D7A";
							fontColor = "#000000";
						}
					}else{
						color = "#C5E3B9";
						fontColor = "#000000";
					}
					out.println("<tr bgcolor='" + color + "'>");
				}
				for(int j=0;j<str.length;j++){
					out.println("<td >"+str[j].trim()+"</td>");
					itemp--;
				}
				for(;itemp > 0; itemp--){
					out.println("<td ></td>");
				}
				out.println("</tr>");
			}
		}
	%>
</table>
</DIV>
<SCRIPT LANGUAGE="JavaScript">
parent.closeMsgDlg();
if(typeof(parent.data) == "object")
{	
	<%
	  if(!isConfig)
	  {
		  out.println("�豸û�����ö�����");
	  }	
	  else
	  {
		  out.println("parent.data.innerHTML=idLayer.innerHTML;");
	  }
	%>		
}
</SCRIPT>


