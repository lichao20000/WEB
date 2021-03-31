<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%> 
<%@taglib prefix="lk" uri="/linkage"%> 
<%--
	/**
	 * 手动下发配置
	 *
	 * @author zm
	 * @version 1.0
	 * @since 2011-12-5 上午09:49:26
	 * 
	 * <br>版权：南京联创科技 网管科技部
	 * 
	 */
 --%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK">
	<title>手动下发配置</title>
	<lk:res />
	<style type="text/css">
	
	</style>
	<script type="text/javascript">
		function setFrameHeight(dataHeight){
			$("#first").height(dataHeight);
		}
		function chooseQuery(){
			var account = $("input[name='queryStr']").val();
			if(account == "" || account == null || account == undefined){
				alert("请输入业务帐号！");
				return;
			}
			document.selectForm.action = "<s:url value='/gtms/stb/resource/zeroconfManual!getUserAccount.action'/>";
			document.selectForm.submit();
		}
	</script>
</head>
<body>
	<form id="selectForm" name="selectForm" action="<s:url value='/gtms/stb/resource/zeroconfManual!getUserAccount.action'/>" target="first" method="post">
		<table class="querytable" align="center" style="margin-top:10px;width:98%" id="tabs">
			<tr>
				<td class="title_1" colspan="4">手动下发配置</td>
			</tr>
			<tr align="center">
				<td>
				<div align="center"><input name="queryStr" size="50"/></div>
				</td>
			</tr>
			<tr align="center">
				<td>
				<div align="center">
					<input name="queryType" type="radio" value="1" checked="checked">业务帐号
					<input name="queryType" type="radio" value="2">设备序列号
					</div>
				</td>
			</tr>
			<tr align="right">
			<td class="foot" align="right">
				<div class="right">
					<button type="button" onclick="chooseQuery()">查询</button>
				</div>
			</td>
			</tr>
		</table>
	</form>
	<iframe id="first" name="first" width="98%" frameborder="0" scrolling="no" align="center" ></iframe>
</body>
</html>