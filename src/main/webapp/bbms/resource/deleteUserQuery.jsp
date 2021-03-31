<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>设备查询</title>
		<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript"
			src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
		<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
		<SCRIPT LANGUAGE="JavaScript">
		function queryUser(){
			var filename = $.trim($("input[@name='gwShare_fileName']").val());
			if(filename==""){
				alert("请上传文件！！");
				return;
			}
			var url="<s:url value='/bbms/resource/deleteBBMSUser!queryUser.action'/>?"
			+ "&gwShare_fileName=" + filename
			window.location.href=url;
		}

		</SCRIPT>
		<style>
span {
	position: static;
	border: 0;
}
</style>
	</head>


	<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center" >
		<tr>
			<td height="20"></td>
		</tr>
		<TR>
			<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">
							未使用定制网关用户处理
						</TD>
						<td>
							&nbsp;
							<img src="<s:url value="/images/attention_2.gif"/>" width="15"
								height="12">

						</td>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<tr>
			<td bgcolor=#FFFFFF>
				<form action="#" name="frm">
					<table border=0 cellspacing=1 cellpadding=2 width="100%"
						align="center"  bgcolor=#999999>
						<tr>
							<th colspan="4" id="gwShare_thTitle">
								上传文件
							</th>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td align="right" width="15%">
								提交文件
							</td>
							<td colspan="3" width="85%">

								<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO
									src="<s:url value="/gwms/share/FileUpload.jsp"/>" height="20"
									width="100%">
								</iframe>
								<input type="hidden" name=gwShare_fileName value="" />

							</td>
						</tr>
						<tr>
							<td CLASS="green_foot" align="right">
								注意事项
							</td>
							<td colspan="3" CLASS="green_foot">
								1、需要导入的文件格式限于Excel、文本文件，即xls、csv、txt格式 。
								<br>
								2、文件的第一行为标题行，即【用户账号】。
								<br>
								3、文件只有一列。
								<br>
								4、上传的文件最多放入100条数据。
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td colspan="4" align="right" class="green_foot" width="100%">
								<input type="button" onclick="javascript:queryUser()"
									class=jianbian value=" 分析文件 " />
							</td>
						</tr>
					</table>
				</form>
			</td>
		</tr>
	</TABLE>
</html>