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
		alert("�ļ��ϴ��ɹ���");
	</script>
<%
	}
%>


<script type="text/javascript">
function checkForm(){
	var file = document.importFrm.file.value;
	
	if (file == ''){
		alert('��ѡ����Ҫ�����.xls�ļ���');
		return false;
	}
	
	if (file.indexOf('.xls') == -1){
		alert('��ѡ��.xls��ʽ���ļ���');
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
										�û���Դ
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
								<tr><th colspan="4">�ϴ��û���Ϣ</th></tr>
								<tr bgcolor="#ffffff">
									<td width="25%" align="right">��ѡ������û�����:</td>
									<td width="25%">
										<select name="infoType" class="bk">
											<option value="0">--��ͥ����--</option>
											<option value="1">--��ҵ����--</option>
										</select>
									</td>
									<td width="25%" align="right">��ѡ�����ļ�����Դ:</td>
									<td width="25%">
										<select name="resArea" class="bk">
											<option value="0">--BSS--</option>
											<option value="1">--IPOSS--</option>
										</select>
									</td>
								</tr>
								<tr bgcolor="#ffffff">
									<td align="right">��ѡ���ļ�:</td>
									<td><input type="file" name="file"></td>
									<td colspan="2" align="right"><input type="button" value=" �� �� " onclick="checkForm()"></td>
								</tr>
								<TR>
								<TD  colspan="4" align="center" bgcolor=#ffffff>
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td width="2%">&nbsp;</td>
											<td colspan="2">�ļ���ʽ��</td>
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td width="2%">1��</td>
											<td>�ļ�Ϊ.xls��ʽ</td>
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td width="2%">2��</td>
											<td>�ļ��еĵ�һ�в���⣬���ֶ�Ϊ���û�����ʺš��û��绰���û����ء�ҵ������ʱ�䡢����ʱ��</td>
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td width="2%">3��</td>
											<td>ʱ���ʽ����Ϊ������1209571200����Ҳ����Ϊ�����ͣ�2008/01/01��</td>
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