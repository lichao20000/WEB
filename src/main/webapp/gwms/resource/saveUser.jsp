<%--
业务手工工单
Author: Jason
Version: 1.0.0
Date: 2009-09-28
--%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<title> 家庭网关客户添加编辑结果页面 </title>
<script type="text/javascript">
<!--//

//load
$(document).ready(function(){
	var result = '<s:property value="saveResult"/>';
	if(result == '1'){
		$("div[@id='div_mesg']").html("网关用户操作成功");
	}else{
		$("div[@id='div_mesg']").html("网关用户操作失败");
	}
	
})

function GoList(page){
	this.location = page;
}

//-->
</script>
</head>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="50%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">网关客户资源操作提示信息</TH>
					</TR>
						<TR  height="50">
							<TD align=center valign=middle class=column><div id="div_mesg"></div></TD>
						</TR>
					<TR>
						<TD class=foot align="right">
						
                        <INPUT TYPE="button" NAME="cmdJump" onclick="GoList('hgwcustManage.action')" value=" 添加新用户 " class=btn >
                        <s:if test='isAdd=="2"'>
						<INPUT TYPE="button" NAME="cmdBack" onclick="GoList('../../Resource/HGWUserInfoList.jsp?opt=edit')" value=" 用户列表 " class=btn>
						</s:if>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>
</body>
</html>