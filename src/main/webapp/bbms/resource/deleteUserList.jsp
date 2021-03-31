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
			alert("没有可以删除的用户！！");
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
						未使用定制网关用户处理
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
					分析结果
				</caption>

				<thead>

					<tr>
						<th>
							用户账号
						</th>
						<th>
							属地
						</th>
						<th>
							绑定设备
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
									返回
								</button>
							</td>
						</tr>
					</tfoot>
				</s:if>
				<s:else>
					<tbody>
						<tr>
							<td colspan="3">
								以下是库中不存在的用户(有<s:property value="notExistUserList.size()" />条)
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
								以下是已经绑定设备的用户(有<s:property value="bindUserList.size()" />条)
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
								以下是可以删除的用户(有<s:property value="canDelUserList.size()" />条)
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
								针对分析的结果主要分为三部分：
								<br>
								第一部分，上传文件中的用户账号在系统中不存在，这部分数据无需删除；
								<br>
								第二部分，上传文件中的用户账号在系统中已经绑定设备，这部分用户请解绑后再处理；
								<br>
								第三部分，上传文件中的用户账号在系统中存在，而且未绑定设备，这部分用户可以删除；


							</td>
						</tr>
						<tr>
							<td colspan='3'>
								<button name="deleteUser" onclick="delUser('<s:property value="userIds" />')">
									删除
								</button>
							</td>
						</tr>
					</tfoot>
				</s:else>
			</table>
		</td>
	</tr>
</TABLE>


