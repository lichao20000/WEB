<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="com.linkage.litms.LipossGlobals" %>


<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<<script type="text/javascript">
<!--
var instAreaName = '<%= LipossGlobals.getLipossProperty("InstArea.ShortName")%>';
function goBack(){
	 if(!"sx_lt" == instAreaName){
		 history.go(-1);
	 }else{
		 self.location=document.referrer;
	 }
}
//-->
</script>
<br>
<br>
<TABLE width="80%" class="querytable" align="center">
	<TR>
		<TH class="title_1">
			批量策略版本升级任务定制
		</TH>
	</TR>
	<tr height="40">
		<td>
			<div align="center">
				<s:property value="message" />
			</div>
		</td>
	</tr>
	<tr>
		<td class=foot align="right">
			<div class="right">
				<button name="back" onclick="javascript:goBack();">
					返 回
				</button>
			</div>
		</td>
	</tr>
</TABLE>