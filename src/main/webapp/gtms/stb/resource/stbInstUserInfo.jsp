<%--
��ͥ�����ֹ�����ѯ�û�����
Author: ��ɭ��
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
			ѡ��
		</th>
		<th width="45%">
			ҵ���˺�
		</th>
		<th width="45%">
			����
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
				<font color="red">δ�鵽����������ҵ���˺ţ����ʵ������Ƿ���ȷ��</font>
			</td>
		</tr>
	</s:else>
</table>
