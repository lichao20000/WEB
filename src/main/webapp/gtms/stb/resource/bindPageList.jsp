<%--
Author: ��ɭ��
Version: 1.0.0
Date: 2009-11-11
--%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<table class="listtable">
<thead>
			<tr>
				<th>ѡ��</th>
				<th>ҵ���˻�</th>
				<th>mac</th>
				<th>����</th>
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
				<font color="red">δ�鵽�����������û������ʵ������Ƿ���ȷ��</font>
			</td>
		</tr>
	</s:else>
		</tbody>
</table>
