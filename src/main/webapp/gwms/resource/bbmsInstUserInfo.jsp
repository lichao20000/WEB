<%--
企业网关手工解绑查询用户界面
Author: 王森博
Version: 1.0.0
Date: 2009-11-11
--%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<table border=0 cellspacing=1 cellpadding=1 width="100%" align="center"
	bgcolor="#999999">
	<input type="hidden" name="instArea"
		value="<s:property value='instArea'/>">
	<tr>
		<td class="green_title" width="5%">
			选择
		</td>
		<td class="green_title" width="15%">
			客户ID
		</td>
		<td class="green_title" width="15%">
			用户账号
		</td>
		<td class="green_title" width="20%">
			属地
		</td>
		<td class="green_title" width="15%">
			绑定设备oui
		</td>
		<td class="green_title" width="20%">
			绑定设备序列号
		</td>
		<td class="green_title" width="10%">
			详细信息
		</td>
	</tr>


	<s:if test="userList!=null">
		<s:iterator value="userList">
			<tr bgcolor="#FFFFFF">
				<td class=column align="center">
					<input type="radio" name="radioUserId" value="1"
						onclick="userOnclick('<s:property value="user_id"/>',
											 '<s:property value="userName"/>',
									 		 '<s:property value="city_id"/>',
									 		 '<s:property value="customer_id"/>',
									 		 '<s:property value="device_id"/>',
											 '<s:property value="oui"/>',
											 '<s:property value="device_serialnumber"/>')" />
				</td>
				<td class=column align="center">
					<s:property value="customer_id" />
				</td>
				<td class=column align="center">
					<s:property value="username" />
				</td>
				<td class=column align="center">
					<s:property value="city_name" />
				</td>
				<s:if test="device_id==''">
					<td class=column align="center">
					</td>
					<td class=column align="center">
					</td>
				</s:if>
				<s:else>
					<td class=column align="center">
						<s:property value="oui" />
					</td>
					<td class=column align="center">
						<s:property value="device_serialnumber" />
					</td>
				</s:else>
				<td align="center" class=column>
					<a><IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0"
							ALT="详细信息" onclick="GoContent('<s:property value="user_id"/>')"
							style="cursor: hand"> </A>
				</td>
			</tr>
		</s:iterator>
	</s:if>
	<s:else>
		<tr bgcolor="#FFFFFF">
			<td colspan="7">
				<font color="red">未查到符合条件的用户！请核实输入的是否正确！</font>
			</td>
		</tr>
	</s:else>
</table>
