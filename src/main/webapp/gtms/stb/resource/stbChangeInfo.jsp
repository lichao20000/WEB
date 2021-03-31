<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css"></link>
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css"></link>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<table class="listtable">
	<thead>
		<tr>
			<th width="5%" align="center">选择</th>
			<th align="center">业务账号</th>
			<th align="center">MAC地址</th>
			<th align="center">属地</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="userList!=null">
			<s:if test="userList.size() > 0">
				<s:iterator value="userList">
					<tr bgcolor="#FFFFFF">
						<td align="center">
						<input type="radio" name="radioUserId" 
						onclick="javascript:userOnclick('<s:property value="customer_id"/>',
					 		 '<s:property value="cpe_mac"/>','<s:property value="serv_account"/>',
					 		 '<s:property value="city_id"/>');" />
					 		 </td>
						<td align="center"><s:property value="serv_account" /></td>
						<td align="center"><s:property value="cpe_mac" /></td>
						<td align="center"><s:property value="cityName" /></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=4 align=left><font color="red">未查到符合条件的用户！请核实输入的是否正确！</font></td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr bgcolor="#FFFFFF">
				<td colspan="4"><font color="red">未查到符合条件的用户！请核实输入的是否正确！</font>
				</td>
			</tr>
		</s:else>
	</tbody>
<!-- 	<tfoot> -->
<!-- 		<tr> -->
<%-- 				<td colspan="4" align="right" height="15">[ 统计总数 : <s:property --%>
<%--  						value='queryCount' /> ]&nbsp;<lk:pages --%>
<%--  						url="/gtms/stb/resource/stbChange!getMacList.action" showType=""  --%>
<%-- 						isGoTo="true" changeNum="true" /></td> --%>
<!-- 		</tr> -->
<!-- 	</tfoot> -->
</table>
