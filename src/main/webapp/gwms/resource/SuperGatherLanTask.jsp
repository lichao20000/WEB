<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<script language="JavaScript" src="<s:url value='/Js/jquery.js'/>"></script>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>" type="text/css">
<script LANGUAGE="JavaScript">
	
</script>
<%@ include file="../../toolbar.jsp"%>

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr>
		<td height=20>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>

			<table width="98%" border="0" align="center" cellpadding="0"
				cellspacing="0" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									批量采集光猫LAN1信息
								</td>
								<td nowrap>
									<img src="<s:url value='/images/attention_2.gif'/>" width="15"
										height="12">

								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/gwms/resource/SuperGatherLanTaskCreate.jsp"%>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td height=20>
			&nbsp;
			<iframe id=childfrm src="" style="display: none"></iframe>
			<iframe id=childfrm1 src="" style="display: none"></iframe>
			<iframe id=childfrm2 src="" style="display: none"></iframe>
		</td>
	</tr>
</table>
<%@ include file="../foot.jsp"%>
