<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>ҵ��������ʷ��Ϣ</title>
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
			type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript">
		function configDetailInfo(id, deviceId, deviceSN){
			var page = "<s:url value='/gtms/stb/resource/stbInst!getConfigLogDetail.action'/>?id=" + id + "&deviceId=" + deviceId + "&deviceSN=" + deviceSN;
			window.open(page, "", "left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
		}
</script>
	</head>
	<body>
		<input type="hidden" name="gw_type" value='<s:property value="gw_type" />' />
		<table class="listtable">
			<caption>
				�豸
				<s:property value="deviceSN" />
				������Ϣ
			</caption>
			<thead>
				<tr>
					<th>
						ҵ������
					</th>
					<th>
						ִ��ʱ��
					</th>
					<th>
						�·����ʱ��
					</th>
					<th>
						����ִ��״̬
					</th>
					<th>
						�������������
					</th>
					<th>
						����
					</th>
				</tr>
			</thead>
			<tbody>
				<s:if test="configLogInfoList!=null">
					<s:if test="configLogInfoList.size()>0">
						<s:iterator value="configLogInfoList">
							<tr>
								<td>
									<s:property value="serviceName" />
								</td>
								<td>
									<s:property value="start_time" />
								</td>
								<td>
									<s:property value="end_time" />
								</td>
								<td>
									<s:property value="status" />
								</td>
								<s:if test="instAreaName=='sx_lt'">
									<td>
										<s:property value="fault_desc" />
									</td>
								</s:if>
								<s:else>
									<td>
										<s:property value="result_id" />
									</td>
								</s:else>
								<td>
									<a href="javascript:configDetailInfo('<s:property value="id" />','<s:property value="device_id" />','<s:property value="deviceSN" />')">��ϸ��Ϣ</a>
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td colspan=6>
								ϵͳû��������Ϣ!
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr>
						<td colspan=6>
							ϵͳû�д�ҵ����Ϣ!
						</td>
					</tr>
				</s:else>
			</tbody>
		</table>
	</body>