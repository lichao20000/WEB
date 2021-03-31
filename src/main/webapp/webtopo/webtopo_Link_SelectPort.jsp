<%--
@author:hemc
@date:2006-11-28
--%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.webtopo.snmpgather.*" %>
<%@ include file="../timelater.jsp"%>
<%
request.setCharacterEncoding("GBK");
String device_id= request.getParameter("device_id");
String className=request.getParameter("className");
//FluxComm FluxComm = new FluxComm(request);
List list = FluxComm.getDevicePortFromSnmp(device_id,user);
int size = 0;
if(list != null)
    size = list.size();
StringBuffer strBuff = new StringBuffer();
strBuff.append("<select id='_sel_"+ device_id +"' onchange=\"change(this)\">");
for(int i=0;i<size;i++){   
    strBuff.append("<option value='" + list.get(i) + "'>" + list.get(i) + "</option>");
}
strBuff.append("</select>");
//用于javascript函数的参数
String tmpParam = strBuff.toString();
%>

<div name="div_data" id="div_data">
	<%=tmpParam%>
</div>

<script type="text/javascript">
	var control = "div_<%=device_id%>";
	var _obj = parent.document.all(control);
	if(typeof(_obj) != "undefined" && _obj != null){
		//alert(div_data.innerHTML);
		_obj.innerHTML = div_data.innerHTML;
	}
	else
		alert("Error!");
</script>