<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%> 
<%--
/**
 * 模板管理--使用模板配置Form页面
 * REQ:XXXX-XXXX-XXXX
 * BUG:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-MAIL:BENYP@LIANCHUANG.COM
 * @version 1.0
 * @since 2008-8-25 10:13:42
 * 
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>使用模板配置</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">
	function getData(url){
		$("frame[@name='data']").attr("src",url);
	}
</script>
</head>
<frameset rows="80,*" border="0" FRAMESPACING="0" >
	<frame src="<s:url value="/performance/useMoudle!getModule.action"/>?device_id=<s:property value="device_id"/>" frameborder="0" name="title"></frame>
	<frame src="" name="data" frameborder="0"></FRAME>
</FRAMESET>

</html>