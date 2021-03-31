<%@page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@include file="../timelater.jsp"%>
<%
request.setCharacterEncoding("GBK");
String action = request.getParameter("action");

String serialno = request.getParameter("serialno");
String gather_id = request.getParameter("gather_id");

String subject = request.getParameter("subject");
String content = request.getParameter("content");

String creator = (String)user.getAccount();
String warnReason = request.getParameter("warnReason");
String warnResove = request.getParameter("warnResove");


Knowledge knowledge = new Knowledge();
//É¾³ý
if("delete".equals(action)){
    knowledge.serialno = serialno;
    knowledge.gather_id = gather_id;
    if(knowledge.delete())
        response.sendRedirect("index.jsp");
    else{
%>
        <%@page import="com.linkage.litms.knowledge.Knowledge"%>
<script language="javascript"><!--
            alert("É¾³ýÊ§°Ü¡£");
            history.back();
        //--></script>
<%
    }
}
//ÐÞ¸Ä
else if("modify".equals(action)){
	knowledge.serialno = serialno;
    knowledge.gather_id = gather_id;
    knowledge.subject = subject;
    knowledge.content = content;
    knowledge.warnReason = warnReason;
	knowledge.warnResove = warnResove;
	if(knowledge.modify())
        response.sendRedirect("view.jsp?serialno="+serialno +"&gather_id="+gather_id);
    else{
%>
        <script language="javascript"><!--
            alert("±£´æÊ§°Ü¡£");
            history.back();
        //--></script>
<%
    }
}
//Ìí¼Ó
else{
    knowledge.subject = subject;
    knowledge.content = content;
    knowledge.creator = creator;
    knowledge.warnReason = warnReason;
	knowledge.warnResove = warnResove;
    if(knowledge.add())
        response.sendRedirect("view.jsp?serialno="+serialno +"&gather_id="+gather_id);
    else{
%>
        <script language="javascript"><!--
            alert("±£´æÊ§°Ü¡£");
            history.back();
        //--></script>
<%
    }
}
%>
