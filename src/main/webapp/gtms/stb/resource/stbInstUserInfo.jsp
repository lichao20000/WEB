<%--
家庭网关手工解绑查询用户界面
Author: 王森博
Version: 1.0.0
Date: 2009-11-14
--%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<table class="listtable" width="100%" align="center" >
	<thead>
	<tr >
		<th width="10%">
			选择
		</th>
		<th width="45%">
			业务账号
		</th>
		<th width="45%">
			属地
		</th>
	</tr>
	</thead>

	<s:if test="userList!=null && userList.size()>0">
		<s:iterator value="userList">
			<tr>
				<td class=column align="center">
					<input type="radio" name="radioUserId" value="1"
						onclick="userOnclick('<s:property value="customer_id"/>',
											 '<s:property value="serv_account"/>',
											 '<s:property value="city_id"/>')" />
				</td>
				<td>
					<s:property value="serv_account" />
				</td>
				<td>
					<s:property value="city_name" />
				</td>
			</tr>
		</s:iterator>
	</s:if>
	<s:else>
		<tr>
			<td colspan="4" >
				<font color="red">未查到符合条件的业务账号！请核实输入的是否正确！</font>
			</td>
		</tr>
	</s:else>
</table>
