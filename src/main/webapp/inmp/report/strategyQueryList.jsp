<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>
<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/inmp/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/inmp/jQuerySplitPage-linkage.js"/>"></script>


<script type="text/javascript">
	
	$(function(){
		parent.dyniframesize();
		$("#button",parent.document).attr('disabled',false);
	});

</script>

<table class="listtable">
	<thead>
		<tr>
			<th>
				ҵ������
			</th>
			<th>
				����ʱ��
			</th>
			<th>
				ִ��ʱ��
			</th>
			<th>
				����״̬
			</th>
			<th>
				���Խ��
			</th>
			<th>
				�������
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="device_idList.size()>1">
			<tr>
				<td colspan=6 >
					����˺Ŷ�Ӧ����û���������û�LOID��ѯ!
				</td>
			</tr>
		</s:if>
		<s:elseif test="device_idList.size()==0">
			<tr>
				<td colspan=6 >
					û���������������ݣ�
				</td>
			</tr>
		</s:elseif>
		<s:else>
			<s:if test="list.size()>0">
				<s:iterator value="list">
					<tr style="vertical-align:middle; text-align:center;">
						<td>
							<s:property value="service_id"  />
						</td>
						<td>
							<s:property value="time" /> 
						</td>
						<td>
							<s:property value="start_time" /> 
						</td>
						<td>
							<s:property value="status" />
						</td>
						<td>
							<s:property value="result_id" />
						</td>
						<td>
							<s:property value="result_desc" />
						</td>
					</tr>
				</s:iterator>
				<tr>
					<td colspan=6 align="right">
						<lk:pages  url="/inmp/report/strategyQuery!goPage.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
					</td>
				</tr>
			</s:if>
			<s:else>
				<tr>
					<td colspan=6 >
						û���������������ݣ�
					</td>
				</tr>
			</s:else>
		</s:else>
	</tbody>
	
</table>







