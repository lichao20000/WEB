<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<table class="listtable">
	<caption>
		查询结果
	</caption>
	<thead>
		<tr>
			<th>
				设备序列号
			</th>
			<th>
				用户账号
			</th>
			<th>
				用户类型
			</th>
			<th>
				是否绑定
			</th>
			<th>
				绑定方式
			</th>
			<th>
				上报时间
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="hgwList!=null">
			<s:if test="hgwList.size()>0">
				<s:iterator value="hgwList">
					<tr>
						<td>
							<s:property value="device" />
						</td>
						<td>
							<s:property value="username" />
						</td>
						<td>
							<s:property value="serv_type" />
						</td>
						<td>
							<s:property value="is_bind" />
						</td>
						<td>
							<s:property value="bind_type" />
						</td>
						<td>
							<s:property value="inform_time" />
						</td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<td colspan=6>
					系统没有相关上报信息!
				</td>
			</s:else>
		</s:if>
		<s:else>
			<td colspan=6>
				系统没有<s:property value="starttime" />的信息!
			</td>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="6">
				<lk:pages url="/itms/resource/deviceToUsername.action" styleClass=""
					showType="" isGoTo="true" changeNum="true" />
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="6">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>


