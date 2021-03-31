<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head></head>
<SPAN ID="child1">
<s:property value="byImportList" escapeHtml="false"/>
</SPAN>
<SPAN ID="child2">
<iframe name="loadForm" FRAMEBORDER=0 SCROLLING=NO src="selectDeviceTag!initImport.action?selectType=<s:property value="selectType"/>&jsFunctionName=<s:property value="jsFunctionName"/>&maxFileNum=<s:property value="maxFileNum"/>" height="30" width="100%"></iframe>
</SPAN>
<SCRIPT LANGUAGE="JavaScript">
parent.document.all("div_device").innerHTML = child1.innerHTML;
parent.document.all("importUsername").innerHTML = child2.innerHTML;
</SCRIPT>
</html>
