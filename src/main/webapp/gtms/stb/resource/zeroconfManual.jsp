<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%> 
<%@taglib prefix="lk" uri="/linkage"%> 
<%--
	/**
	 * �ֶ��·�����
	 *
	 * @author zm
	 * @version 1.0
	 * @since 2011-12-5 ����09:49:26
	 * 
	 * <br>��Ȩ���Ͼ������Ƽ� ���ܿƼ���
	 * 
	 */
 --%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK">
	<title>�ֶ��·�����</title>
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
				alert("������ҵ���ʺţ�");
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
				<td class="title_1" colspan="4">�ֶ��·�����</td>
			</tr>
			<tr align="center">
				<td>
				<div align="center"><input name="queryStr" size="50"/></div>
				</td>
			</tr>
			<tr align="center">
				<td>
				<div align="center">
					<input name="queryType" type="radio" value="1" checked="checked">ҵ���ʺ�
					<input name="queryType" type="radio" value="2">�豸���к�
					</div>
				</td>
			</tr>
			<tr align="right">
			<td class="foot" align="right">
				<div class="right">
					<button type="button" onclick="chooseQuery()">��ѯ</button>
				</div>
			</td>
			</tr>
		</table>
	</form>
	<iframe id="first" name="first" width="98%" frameborder="0" scrolling="no" align="center" ></iframe>
</body>
</html>