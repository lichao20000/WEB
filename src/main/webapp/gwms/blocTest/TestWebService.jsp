<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags" %>


<%@page import="java.net.URL"%>
<%@page import="javax.xml.namespace.QName"%>
<%@page import="org.apache.axis.client.Call"%>
<%@page import="org.apache.axis.client.Service"%>


<%
	request.setCharacterEncoding("GBK");
	String wsdl = "";
	String methodName = "";
	String inParam = "";
	String retParam = "";
	
	
	wsdl = request.getParameter("wsdl") == null? "" : request.getParameter("wsdl") ;
	methodName = request.getParameter("methodName") == null? "" : request.getParameter("methodName");
	inParam = request.getParameter("inParam");
	if(inParam==null){
		inParam = "";
	}
	else{
		String str = new String(request.getParameter("inParam").getBytes("iso-8859-1"), "GBK");
		inParam = str == null? "" : str;
	}
	
	
	if(wsdl != null && !"".equals(wsdl) && 
	   methodName != null && !"".equals(methodName) && 
	   inParam != null && !"".equals(inParam)){
		try {
			// 入参：xml字符串
			final String endPointReference = wsdl;  
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPointReference));

			QName qn = new QName(endPointReference, methodName);
			call.setOperationName(qn);
			// 调用的服务器端方法
			// 回参：xml字符串
			retParam = (String) call.invoke(new Object[] { inParam });
			
		} catch (Exception e) {
			e.printStackTrace();
			retParam = "调用失败！";
		}
	}
	
%>

<html>
<head>
<title>WebService调试</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<lk:res />
<script type="text/javascript">

function query(){
	var wsdl = Trim($("input[@name='wsdl']").val());
	var methodName = Trim($("input[@name='methodName']").val());
	var inParam = document.getElementById('inParam').value;
	
	if(wsdl == ""){
		alert("WSDL不可为空");
		$("input[@name='wsdl']").focus();
		return ;
	}
	if(methodName == ""){
		alert("方法名不可为空");
		$("input[@name='methodName']").focus();
		return ;
	}
	if(inParam == ""){
		alert("入参不可为空");
		document.getElementById('inParam').focus();
		return ;
	}
	
	document.selectForm.submit();
}



function ReSet(){
	window.location.href ="TestWebService.jsp";
}


</script>
</head>
<body>
	<form name="selectForm" action="TestWebService.jsp">
		<table >
			<tr>
				<td HEIGHT=20>
					&nbsp;
				</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<th>
								WebService
							</th>
							<td>
								<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
							</td>
							<td align="right">
								<button onclick="query()">&nbsp;发  送&nbsp;</button>
								<button onclick="ReSet()">&nbsp;重  置&nbsp;</button>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<tr leaf="simple">
							<th colspan="4">
								Test WebService
							</th>
						</tr>
						<TR bgcolor="#FFFFFF" leaf="simple">
							<TD class=column width="15%" align='right'>
								WSDL：
							</TD>
							<TD colspan ="3" width="85%">
								<input type="input" name="wsdl" size="50" class=bk  value="<%=wsdl%>" />
								<font color="red">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" leaf="simple">
							<TD class=column width="15%" align='right'>
								方法名：
							</TD>
							<TD colspan ="3" width="85%">
								<input type="input" name="methodName" size="50" class=bk value="<%=methodName%>" />
								<font color="red">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" leaf="simple">
							<TD class=column width="15%" align='right'>
								入参：
							</TD>
							<TD colspan ="3"  width="85%">
								<textarea name="inParam" id="inParam" cols="80" class="bk" rows="15"><%=inParam %></textarea>
								<font color="red">*&nbsp;&nbsp;注：入参格式为XML格式，“<”后面不可有空格，“>”前面不可有空格</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" leaf="simple">
							<TD class=column width="15%" align='right'>
								回参：
							</TD>
							<TD colspan ="3"  width="85%">
								<textarea name="inParam" id="inParam" cols="120" class="bk" rows="15" readonly><%=retParam %></textarea>
							</TD>
						</TR>
					</table>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
<%@ include file="../foot.jsp"%>
