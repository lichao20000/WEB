<%--
Author: 王森博
Version: 1.0.0
Date: 2009-11-11
--%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<table class="listtable">
<thead>
			<tr>
				<th>选择</th>
				<th>业务账户</th>
				<th>mac</th>
				<th>属地</th>
			</tr>
		</thead>
<tbody>

	<s:if test="date!=null&&date.size()>0">
		<s:iterator value="date">
			<tr align="center">
				<td style="text-align: center;">
					<input type="radio" name="radioUserId" value="1"
						onclick="userOnclick('<s:property value="mac"/>',
									 		 '<s:property value="servaccount"/>')" />
				</td>
				<td style="text-align: center;">
					<s:property value="servaccount" />
				</td>
				<td style="text-align: center;">
					<s:property value="mac" />
				</td>
				<td style="text-align: center;">
					<s:property value="city_name" />
				</td>
			</tr>
		</s:iterator>
	</s:if>
	<s:else>
		<tr bgcolor="#FFFFFF">
			<td colspan="4">
				<font color="red">未查到符合条件的用户！请核实输入的是否正确！</font>
			</td>
		</tr>
	</s:else>
		</tbody>
</table>
