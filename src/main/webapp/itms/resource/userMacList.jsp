<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<table class="listtable">
	<caption>
		��ѯ���
	</caption>
	<thead>
		<tr>
			<th width="25%">
				����
			</th>
			<th width="25%">
				�û��˺�
			</th>
			<th width="25%">
				�豸MAC��ַ
			</th>
			<th width="25%">
				DSLAM��IP
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="hgwList.size()>0">
			<s:iterator value="hgwList">
				<tr>
					<td>
							<s:property value="city_name" />	
					</td>
					<td>
						<s:property value="username" />	
					</td>
					<td>
						<s:property value="cpe_mac" />
					</td>
					<td>
						<s:property value="dslam_ip" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
		<td colspan=4>
					ϵͳû����ص��û���Ϣ!
				</td>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="4">
				<lk:pages
					url="/itms/resource/hgwByMac.action" styleClass=""
					showType="" isGoTo="true" changeNum="true" />
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="4">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>


