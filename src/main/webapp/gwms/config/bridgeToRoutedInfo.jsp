<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">

<table class="listtable">
	<caption>
		��ѯ���
	</caption>
	<s:if test="data.size()>0">
		<s:iterator value="data">
			<thead>
				<tr>
					<th width="15%">
						�豸����
					</th>
					<th width="15%">
						�豸�ͺ�
					</th>
					<th width="15%">
						�豸���к�
					</th>
					<th width="15%">
						�Ƿ�֧��·��
					</th>
					<th width="15%">
						�û��˺�
					</th>
					<th width="10%">
						����ģʽ
					</th>
					<th width="15%">
						�û�����
					</th>
				</tr>
			</thead>

			<tbody>

				<tr>
					<td>
						<s:property value="DevFactory" />
					</td>
					<td>
						<s:property value="DevModel" />
					</td>
					<td>
						<s:property value="DevSn" />
					</td>
					<td>
						<s:if test='RouteSupported=="1"'>֧��</s:if>
						<s:else>��֧��</s:else>
					</td>
					<td>
						<s:property value="username" />
					</td>
					<td>
						<s:if test='NetType=="1"'>�Ž�</s:if>
						<s:else>·��</s:else>
					</td>
					<td>
						<input type="text" name="password" class='bk' value="">
					</td>
				</tr>
			</tbody>
			<tfoot>
				<tr>
					<td class=foot colspan=7 align=right>
						<button name="edit"
							onclick="edit('<s:property value="DevSn" />','<s:property value="username" />')"
							<s:property value='RouteSupported=="-1"?"disabled":NetType=="2"?"disabled":""' />>
							&nbsp;·���·�&nbsp;
						</button>
						&nbsp;&nbsp;&nbsp;
						<button name="RoutedQuery"
							onclick="RoutedQuery('<s:property value="DevSn" />','<s:property value="username" />')"
							<s:property value='RouteSupported=="-1"?"disabled":""' />>
							&nbsp;�·������ѯ&nbsp;
						</button>
					</td>
				</tr>
			</tfoot>
		</s:iterator>
	</s:if>
	<s:else>
		<tbody>
			<tr>
				<td colspan="7">
					<font color="red"><s:property value="message" /> </font>
				</td>
			</tr>
		</tbody>
		<tfoot>
		</tfoot>
	</s:else>
	<TR>
		<TD align="center" colspan="7">
			<div id="div_query" style="display: none"
				style="width: 100%; z-index: 1; top: 100px">
			</div>
		</TD>
	</TR>

</table>


