<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<SCRIPT LANGUAGE="JavaScript">
<!--
var flag = true;
//-->
</SCRIPT>

<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>" type="text/css">
<script type="text/javascript" src="../Js/jquery.js"></script>
<script type="text/javascript" src="../Js/jQeuryExtend-linkage.js"></script>
<script type="text/javascript" src="../Js/jsDate/WdatePicker.js"></script>

<SCRIPT LANGUAGE="JavaScript">

//-->
</SCRIPT>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>


<FORM NAME="delFrom" METHOD="post" ACTION="HGWUserInfoSave.jsp" onSubmit="return CheckFormDel()">
<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
	
			<TR>
			<TD height="15">
			<font color=red>统计用户条件：</font>
			</TD>
			</TR>
			
	<TR>
		<TD bgcolor=#999999>
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH>用户帐号</TH>
					<TH>属&nbsp;&nbsp;地</TH>
					<TH>用户来源</TH>		
					<TH>绑定设备</TH>
					<TH>开户时间</TH>
					<TH>竣工状态</TH>
					<TH width=100>操作</TH>
				</TR>
				<TR>
					<Td class=column1 align='center'>用户帐号</Td>
					<Td class=column1 align='center'>属&nbsp;&nbsp;地</Td>
					<Td class=column1 align='center'>用户来源</Td>		
					<Td class=column1 align='center'>绑定设备</Td>
					<Td class=column1 align='center'>开户时间</Td>
					<Td class=column1 align='center'>竣工状态</Td>
					<Td width=100 class=column1 align='center'><IMG SRC='../../images/view.gif' BORDER='0' ALT='详细信息' style='cursor:hand'></A></Td>
				</TR>
				<TR>
					<Td class=column1 align='center'>用户帐号</Td>
					<Td class=column1 align='center'>属&nbsp;&nbsp;地</Td>
					<Td class=column1 align='center'>用户来源</Td>		
					<Td class=column1 align='center'>绑定设备</Td>
					<Td class=column1 align='center'>开户时间</Td>
					<Td class=column1 align='center'>竣工状态</Td>
					<Td width=100 class=column1 align='center'><IMG SRC='../../images/view.gif' BORDER='0' ALT='详细信息' style='cursor:hand'></A></Td>
				</TR>
				<TR>
					<Td class=column1 align='center'>用户帐号</Td>
					<Td class=column1 align='center'>属&nbsp;&nbsp;地</Td>
					<Td class=column1 align='center'>用户来源</Td>		
					<Td class=column1 align='center'>绑定设备</Td>
					<Td class=column1 align='center'>开户时间</Td>
					<Td class=column1 align='center'>竣工状态</Td>
					<Td width=100 class=column1 align='center'><IMG SRC='../../images/view.gif' BORDER='0' ALT='详细信息' style='cursor:hand'></A></Td>
				</TR>
				<TR>
					<Td class=column1 align='center'>用户帐号</Td>
					<Td class=column1 align='center'>属&nbsp;&nbsp;地</Td>
					<Td class=column1 align='center'>用户来源</Td>		
					<Td class=column1 align='center'>绑定设备</Td>
					<Td class=column1 align='center'>开户时间</Td>
					<Td class=column1 align='center'>竣工状态</Td>
					<Td width=100 class=column1 align='center'><IMG SRC='../../images/view.gif' BORDER='0' ALT='详细信息' style='cursor:hand'></A></Td>
				</TR>
				<TR>
					<Td class=column1 align='center'>用户帐号</Td>
					<Td class=column1 align='center'>属&nbsp;&nbsp;地</Td>
					<Td class=column1 align='center'>用户来源</Td>		
					<Td class=column1 align='center'>绑定设备</Td>
					<Td class=column1 align='center'>开户时间</Td>
					<Td class=column1 align='center'>竣工状态</Td>
					<Td width=100 class=column1 align='center'><IMG SRC='../../images/view.gif' BORDER='0' ALT='详细信息' style='cursor:hand'></A></Td>
				</TR>
				<TR>
					<Td class=column1 align='center'>用户帐号</Td>
					<Td class=column1 align='center'>属&nbsp;&nbsp;地</Td>
					<Td class=column1 align='center'>用户来源</Td>		
					<Td class=column1 align='center'>绑定设备</Td>
					<Td class=column1 align='center'>开户时间</Td>
					<Td class=column1 align='center'>竣工状态</Td>
					<Td width=100 class=column1 align='center'><IMG SRC='../../images/view.gif' BORDER='0' ALT='详细信息' style='cursor:hand'></A></Td>
				</TR>
				<TR>
					<Td class=column1 align='center'>用户帐号</Td>
					<Td class=column1 align='center'>属&nbsp;&nbsp;地</Td>
					<Td class=column1 align='center'>用户来源</Td>		
					<Td class=column1 align='center'>绑定设备</Td>
					<Td class=column1 align='center'>开户时间</Td>
					<Td class=column1 align='center'>竣工状态</Td>
					<Td width=100 class=column1 align='center'><IMG SRC='../../images/view.gif' BORDER='0' ALT='详细信息' style='cursor:hand'></A></Td>
				</TR>
				<TR>
					<Td class=column1 align='center'>用户帐号</Td>
					<Td class=column1 align='center'>属&nbsp;&nbsp;地</Td>
					<Td class=column1 align='center'>用户来源</Td>		
					<Td class=column1 align='center'>绑定设备</Td>
					<Td class=column1 align='center'>开户时间</Td>
					<Td class=column1 align='center'>竣工状态</Td>
					<Td width=100 class=column1 align='center'><IMG SRC='../../images/view.gif' BORDER='0' ALT='详细信息' style='cursor:hand'></A></Td>
				</TR>
				<TR>
					<Td class=column1 align='center'>用户帐号</Td>
					<Td class=column1 align='center'>属&nbsp;&nbsp;地</Td>
					<Td class=column1 align='center'>用户来源</Td>		
					<Td class=column1 align='center'>绑定设备</Td>
					<Td class=column1 align='center'>开户时间</Td>
					<Td class=column1 align='center'>竣工状态</Td>
					<Td width=100 class=column1 align='center'><IMG SRC='../../images/view.gif' BORDER='0' ALT='详细信息' style='cursor:hand'></A></Td>
				</TR>
				<TR>
					<Td class=column1 align='center'>用户帐号</Td>
					<Td class=column1 align='center'>属&nbsp;&nbsp;地</Td>
					<Td class=column1 align='center'>用户来源</Td>		
					<Td class=column1 align='center'>绑定设备</Td>
					<Td class=column1 align='center'>开户时间</Td>
					<Td class=column1 align='center'>竣工状态</Td>
					<Td width=100 class=column1 align='center'><IMG SRC='../../images/view.gif' BORDER='0' ALT='详细信息' style='cursor:hand'></A></Td>
				</TR>
				<TR>
					<Td class=column1 align='right' colspan="7">首页 前页 后页 尾页  页次：1/2478页 15条/页 共37169条</Td>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</FORM>
</TD></TR>

</TABLE>

<SCRIPT LANGUAGE="JavaScript">

</SCRIPT>
<%@ include file="/foot.jsp"%>

