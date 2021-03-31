<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../head.jsp"%>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
</SCRIPT>

<body>
<s:if test="digitMapList == null || digitMapList.size == 0">
	<font color='red'>没有查询到合适的数图模板！</font>
</s:if>
<s:else>	
	<s:select list="digitMapList" name="map_id" id="map_id" listKey="map_id" listValue="map_name"
						cssClass="bk">
	</s:select>
</s:else>

</body>