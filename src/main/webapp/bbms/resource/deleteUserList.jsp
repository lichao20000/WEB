<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
	function back(){
		var url="<s:url value='/bbms/resource/deleteUserQuery.jsp'/>";
		window.location.href=url;
	}
	function delUser(userIds){
		if(userIds==""){
			alert("û�п���ɾ�����û�����");
			return;
		}
		$("button[@name='deleteUser']").attr("disabled", true); 
		var url = '<s:url value='/bbms/resource/deleteBBMSUser!deleteUser.action'/>'; 
		$.post(url,{
			userIds:userIds
		},function(ajax){	
		    alert(ajax);
		});
	
	}

</SCRIPT>
<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>
						δʹ�ö��������û�����
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15"
							height="12">

					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table class="listtable">
				<caption>
					�������
				</caption>

				<thead>

					<tr>
						<th>
							�û��˺�
						</th>
						<th>
							����
						</th>
						<th>
							���豸
						</th>
					</tr>
				</thead>


				<s:if test="msg!=null">
					<tbody>
						<tr>
							<td colspan="3">
								<s:property value="msg" />
							</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td colspan='3'>
								<button onclick="back()">
									����
								</button>
							</td>
						</tr>
					</tfoot>
				</s:if>
				<s:else>
					<tbody>
						<tr>
							<td colspan="3">
								�����ǿ��в����ڵ��û�(��<s:property value="notExistUserList.size()" />��)
							</td>
						</tr>
						<s:iterator value="notExistUserList">
							<tr>
								<td>
									<s:property value="username" />
								</td>
								<td>
									-
								</td>
								<td>
									-
								</td>
							</tr>
						</s:iterator>
						<tr>
							<td colspan="3">
								�������Ѿ����豸���û�(��<s:property value="bindUserList.size()" />��)
							</td>
						</tr>
						<s:iterator value="bindUserList">
							<tr>
								<td>
									<s:property value="username" />
								</td>
								<td>
									<s:property value="city_name" />
								</td>
								<td>
									<s:property value="device_serialnumber" />
								</td>
							</tr>
						</s:iterator>
						<tr>
							<td colspan="3">
								�����ǿ���ɾ�����û�(��<s:property value="canDelUserList.size()" />��)
							</td>
						</tr>
						<input type="hidden" value="<s:property value="userIds" />">
						<s:iterator value="canDelUserList">
							<tr>
								<td>
									<s:property value="username" />
								</td>
								<td>
									<s:property value="city_name" />
								</td>
								<td>
									<s:property value="device_serialnumber" />
								</td>
							</tr>
						</s:iterator>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="3" align="left">
								��Է����Ľ����Ҫ��Ϊ�����֣�
								<br>
								��һ���֣��ϴ��ļ��е��û��˺���ϵͳ�в����ڣ��ⲿ����������ɾ����
								<br>
								�ڶ����֣��ϴ��ļ��е��û��˺���ϵͳ���Ѿ����豸���ⲿ���û�������ٴ���
								<br>
								�������֣��ϴ��ļ��е��û��˺���ϵͳ�д��ڣ�����δ���豸���ⲿ���û�����ɾ����


							</td>
						</tr>
						<tr>
							<td colspan='3'>
								<button name="deleteUser" onclick="delUser('<s:property value="userIds" />')">
									ɾ��
								</button>
							</td>
						</tr>
					</tfoot>
				</s:else>
			</table>
		</td>
	</tr>
</TABLE>


