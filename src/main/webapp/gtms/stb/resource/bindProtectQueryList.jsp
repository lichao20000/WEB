<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css"></link>
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css"></link>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<script language="JavaScript">
$(function() {
	parent.dyniframesize();
});


function Detail(username,mac)
{

	var page="<s:url value='/gtms/stb/resource/stbBindProtect!getdetail.action'/>?"+
	"mac="+mac+"&userName="+username;

	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");

}

</script>
<div>
<table class="listtable">
	<caption>
		查询结果
	</caption>
	<s:if test="data.size()>0">
		<thead>

			<tr>
				<th>
					业务账号
				</th>
				<th>
					机顶盒MAC
				</th>
				<th>
					操作员
				</th>
				<th>
					添加时间
				</th>
				<th>
					操作
				</th>
			</tr>
		</thead>

		<tbody>
			<s:iterator value="data" var="map1">
				<tr>
					<td align="center">
						<s:property value="username" />
					</td>
					<td align="center">
						<s:property value="mac" />
					</td>
					<td align="center">
						<s:property value="acc_oid" />
					</td>
					<td align="center">
						<s:property value="addtime" />
					</td>
					<td align="center">
						<IMG
							SRC="<s:url value="/images/view.gif"/>" BORDER="0" ALT="详细信息"
									onclick="Detail('<s:property value="username"/>','<s:property value="mac"/>')"
									style="cursor: hand">
					</td>
				</tr>
			</s:iterator>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="5" align="right"><lk:pages
					url="/gtms/stb/resource/stbBindProtect!query.action"
					styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
		</tfoot>
	</s:if>
	<s:else>
		<tfoot>
			<tr>
				<td align="left">
					没有相关绑定信息
				</td>
			</tr>
		</tfoot>
	</s:else>
	<tr STYLE="display: none">
		<td colspan="5">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>
</div>

