<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%
request.setCharacterEncoding("GBK");

String pingtask_id = request.getParameter("pingtask_id");
String device_name = request.getParameter("device_name");
String response_ip = request.getParameter("response_ip");
String username = request.getParameter("username");

Date now = new Date();
String tableName = "vpnping_"+(now.getYear()+1900)+"_"+(now.getMonth()+1);
String today=(now.getYear()+1900)+"-"+(now.getMonth()+1)+"-"+now.getDate();
java.sql.Date startGatherDate = java.sql.Date.valueOf(today);
long startTime = startGatherDate.getTime()/1000;
long endTime = startTime+24*3600;
String linkDetailSql = "select gather_time,packet_sent,packet_received,minrtt,avgrtt,maxrtt,dither,singlertt,singledither from "+tableName
     +" where ping_task_id="+pingtask_id+" and gather_time>="+startTime+" and gather_time<"+endTime+" order by gather_time desc";
Map fields = cursor.getNext(); 
%>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR> 
<TR><TD>	
	<TABLE width="96%" border=0 cellspacing=0 cellpadding=0 align="center">	 
	  <TR><TD bgcolor=#999999>
	    <TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="idTable">
          <TR>
		    <TD bgcolor="#ffffff" colspan=9 align=center><B>�����豸���ƣ�<%=device_name %> �հ��豸IP��<%= response_ip%>�û����ƣ�<%=username %>��·��ϸ��Ϣ</B>
			</TD>
		  </TR>
		  <TR>
		    <TH>ʱ��</TH>		    
            <TH>���Ͱ���</TH>
            <TH>�յ�����</TH>           
            <TH>˫����Сʱ��(ms)</TH>
            <TH>˫��ʱ��(ms)</TH>
            <TH>˫�����ʱ��(ms)</TH>
            <TH>˫��ʱ�Ӷ���(ms)</TH>                  
            <TH>����ʱ��(ms)</TH>
            <TH>���򶶶�(ms)</TH>
		  </TR>
		  <%
		  	if(fields!=null)
		  	{				
		  		while(fields != null)
		  		{						
		  			out.println("<TR bgcolor=#ffffff align=center >");
		  			out.println("<TD class=column>"+new DateTimeUtils(Long.parseLong((String)fields.get("gather_time")) * 1000).getLongDate()+"</TD>");					
		  			out.println("<TD class=column>"+ (String)fields.get("packet_sent")+"</TD>");
		  			out.println("<TD class=column>"+(String)fields.get("packet_received") +"</TD>");					
		  			out.println("<TD class=column>"+(String)fields.get("minrtt") +"</TD>");
		  			out.println("<TD class=column>"+(String)fields.get("avgrtt") +"</TD>");
		  			out.println("<TD class=column>"+(String)fields.get("maxrtt") +"</TD>");
		  			out.println("<TD class=column>"+(String)fields.get("dither") +"</TD>");
		  			out.println("<TD class=column>"+(String)fields.get("singlertt")+"</TD>");
		  			out.println("<TD class=column>"+(String)fields.get("singledither")+"</TD>");			
		  			out.println("</TR>");
		  			fields = cursor.getNext();
		  		}					
		  	}
		  	else
		  	{
		  		out.println("<TR bgcolor=#ffffff>");
		  		out.println("<TD class=column  colspan=9>û����·����</TD>");
		  		out.println("</TR>");
		  	}
		  %>
		  <TR>
			<TD colspan=9 class=foot align=right><input type="button" class=btn value="�� ��" onclick="javascript:history.go(-1)"></TD>
		  </TR>			   
		</TABLE>
	  </TD></TR>
	  <TR><TD HEIGHT=10>&nbsp;</TD></TR>
	</TABLE>	
</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>