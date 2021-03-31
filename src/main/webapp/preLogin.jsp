<%-- 当系统超时时，跳转到该页面，进行重新登录 --%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
	top.window.location.href = "<s:url value='/'/>";
</script>