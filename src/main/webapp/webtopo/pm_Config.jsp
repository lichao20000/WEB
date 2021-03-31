<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<title>设备性能配置</title>
<SCRIPT LANGUAGE="JavaScript" id="idParentFun"></SCRIPT>
<%@ include file="../head.jsp"%>
<%
	String device_id = request.getParameter("device_id");
	String dev_serial = request.getParameter("dev_serial");
%>
<form name="frm" method="post" target="childFrm">
  <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
      <td>	
		<table width="100%"  border="0" cellspacing="0" cellpadding="2">
		  <TR>
			<TH >性能</TH>
		  </TR>
		  
		  <tr>
			<td class=column1> <%@ include file="./pm_tablehead.jsp"%> 
			</td>
		  </tr>

		  <tr>
			<td class=column1><%@ include file="./pm_table.jsp"%></td>
		  </tr>
		  
		  <tr>
			<td class=column1><%@ include file="./pm_map_instance.jsp"%></td>
		  </tr>
		  
		</table>	  
	   </td>
      </tr>
      <tr>
	 <td><IFRAME ID=childFrm name="childFrm" STYLE="display:none;width:500;height:500"></IFRAME></td>
    </tr>
  </table>
</form>
<%@ include file="../foot.jsp"%>