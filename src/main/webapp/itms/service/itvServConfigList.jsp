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
        <th>loid</th>
        <th>itv账号</th>
        <th>vlanId</th>
    </tr>
</thead>
<tbody>
	<s:if test="data!=null&&data.size()>0">
		<s:iterator value="data">
			<tr align="center">
				<td style="text-align: center;">
					<s:property value="loid" />
				</td>
				<td style="text-align: center;">
					<s:property value="itvaccount" />
				</td>
				<td style="text-align: center;">
					<s:property value="vlanid" />
				</td>
			</tr>
			<tr >
			    <td colspan="3" align="right" class="foot" width="100%">
                    <input type='button' value = '升级一线同传' onclick="doServ()"></input>
                </td>
			</tr>
		</s:iterator>
	</s:if>
	<s:else>
		<tr bgcolor="#FFFFFF">
			<td colspan="3">
				<font color="red">未查到符合条件的用户！请核实输入的是否正确！</font>
			</td>
		</tr>
	</s:else>
</tbody>
</table>
