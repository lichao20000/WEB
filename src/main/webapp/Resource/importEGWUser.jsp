<%@ include file="../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ include file="../head.jsp"%>

<% 
	String isSuccess = request.getParameter("isSuccess");
	if ("1".equals(isSuccess)) {
%>
	<script type="text/javascript">
		//isSuccess = '<%= isSuccess%>';
		alert("文件上传成功！");
	</script>
<%
	}
%>


<script type="text/javascript">
function checkForm(){
	var file = document.importFrm.file.value;
	
	if (file == ''){
		alert('请选择需要导入的.xls文件！');
		return false;
	}
	
	if (file.indexOf('.xls') == -1){
		alert('请选择.xls格式的文件！');
		return false;
	}
	
	document.importFrm.submit();
}

</script>
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<form name="importFrm" action="<s:url value="/Resource/importUser.action"/>" method="POST" enctype="multipart/form-data">
				<TABLE width="95%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite">
										用户资源
									</td>
									<!-- 
									<td>
										<img src="<s:url value="/images/attention_2.gif"/>" width="15"
											height="12">
										
									</td> -->
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<tr><th colspan="4">上传用户信息</th></tr>
								<tr bgcolor="#ffffff">
									<td width="25%" align="right">请选择导入的用户类型:</td>
									<td width="25%">
										<select name="infoType" class="bk">
											<option value="0">--家庭网关--</option>
											<option value="1">--企业网关--</option>
										</select>
									</td>
									<td width="25%" align="right">请选择导入文件的来源:</td>
									<td width="25%">
										<select name="resArea" class="bk">
											<option value="0">--BSS--</option>
											<option value="1">--IPOSS--</option>
										</select>
									</td>
								</tr>
								<tr bgcolor="#ffffff">
									<td align="right">请选择文件:</td>
									<td><input type="file" name="file"></td>
									<td colspan="2" align="right"><input type="button" value=" 提 交 " onclick="checkForm()"></td>
								</tr>
								<TR>
								<TD  colspan="4" align="center" bgcolor=#ffffff>
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td width="2%">&nbsp;</td>
											<td colspan="2">文件格式：</td>
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td width="2%">1、</td>
											<td>文件为.xls格式</td>
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td width="2%">2、</td>
											<td>文件中的第一行不入库，各字段为：用户宽带帐号、用户电话、用户属地、业务受理时间、竣工时间</td>
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td width="2%">3、</td>
											<td>时间格式可以为整数（1209571200），也可以为日期型（2008/01/01）</td>
										</tr>
									</table>
								</TD>
							</TR>
							</TABLE>
						</td>
					</tr>
				</table>
			</form>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>