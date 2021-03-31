<%@ page language="java" contenttype="text/html; charset=utf-8"
	pageencoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%string abspath=request.getcontextpath(); %>
<!doctype html public "-//w3c//dtd html 4.01 transitional//en" "http://www.w3.org/tr/html4/loose.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<title>基本信息</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/js/jquerysplitpage-linkage.js"/>"></script>
<script type="text/javascript">
	function failexcel(failnum,add_time,task_name,type)
	{
		
		var url="<s:url value='/itms/resource/logrestartquery!fialexcel.action'/>?"
			+"&failnum="+failnum
			+"&add_time="+add_time
			+"&task_name="+task_name			
			+"&type="+type;
		document.all("childfrm").src=url;
	}
	</script>

</head>
<body>
	<form action="">
		<input type="hidden" name="add_time" id="add_time"
			value='<s:property value="add_time"/>'> <input type="hidden"
			name="task_name" id="task_name"
			value='<s:property value="task_name"/>'> <input type="hidden"
			name="failnum" id="failnum" value='<s:property value="failnum"/>'>
		<input type="hidden" name="type" id="type"
			value='<s:property value="type"/>'>
	</form>
	<table class="listtable" id="listtable">
		<th>用户基本信息</th>
		<s:if test="relatedBaseInfo!=null">
			<s:if test="relatedBaseInfo.size()>0">
				<s:iterator value="relatedBaseInfo">
					<tr>
						<td>用户账号：</td>
						<td><s:property value="user_name" /></td>
						<td>用户id:</td>
						<td><s:property value="user_id" /></td>
					</tr>
					<tr>
						<td>用户姓名：</td>
						<td><s:property value="realname" /></td>
						<td>接入方式:</td>
						<td><s:property value="type_name" /></td>
					</tr>
					<tr>
						<td>终端规格：</td>
						<td><s:property value="spec_name" /></td>
						<td>用户状态:</td>
						<td><s:property value="user_state" /></td>
					</tr>
					<tr>
						<td>用户来源：</td>
						<td><s:property value="userresource" /></td>
						<td>证件类型:</td>
						<td><s:property value="cred_type_id" /></td>
					</tr>
					<tr>
						<td>证件号码：</td>
						<td><s:property value="credno" /></td>
						<td>所属省市:</td>
						<td><s:property value="city_name" /></td>
					</tr>
					<tr>
						<td>证件号码：</td>
						<td><s:property value="credno" /></td>
						<td>所属省市:</td>
						<td><s:property value="city_name" /></td>
					</tr>
					<tr>
						<td>局向标识：</td>
						<td><s:property value="office_name" /></td>
						<td>管理域:</td>
						<td><s:property value="area_name" /></td>
					</tr>
					<tr>
						<td>用户类型：</td>
						<td><s:property value="usertypename" /></td>
						<td>受理时间:</td>
						<td><s:property value="dealdate" /></td>
					</tr>
					<tr>
						<td>开户时间：</td>
						<td><s:property value="opendatea" /></td>
						<td>开通时间:</td>
						<td><s:property value="onlinedate" /></td>
					</tr>
					<tr>
						<td>暂停时间：</td>
						<td><s:property value="pausedate" /></td>
						<td>销户时间:</td>
						<td><s:property value="closedate" /></td>
					</tr>
					<tr>
						<td>更新时间：</td>
						<td><s:property value="updatetime" /></td>
						<td>地址方式:</td>
						<td><s:property value="iptype" /></td>
					</tr>

				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan="8">系统中没有查询出需要的信息!</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan="8">系统中没有查询出需要的信息!</td>
			</tr>
		</s:else>

		<thead>
			<td colspan="4">用户绑定设备信息</td>
		</thead>
		<s:if test="null!=relatedbaseinfo['device_id']">
			<s:if test="''!=relatedbaseinfo['device_id']">
				<tr>
					<td>当前使用的设备</td>
					<td><a
						onclick="detaildevice('<s:property value="device_id" />')"> <s:property
								value="device_id" /></a></td>
					<td>设备序列号</td>
					<td><s:property value="oui" />-<s:property
							value="device_serialnumber" /></td>
				</tr>
				<tr>
					<td>设备mac地址</td>
					<td><s:property value="cpe_mac" /></td>
					<td>绑定时间</td>
					<td><s:property value="binddate" /></td>
				</tr>
				<tr>
					<td>绑定方式</td>
					<td><s:if test="null!=bindtype">
							<s:property value="bindtype" />
						</s:if> <s:else>-</s:else></td>
					<s:if test="'js_dx'==province">
						<td>绑定验证</td>
						<td><s:if test="'0'==is_chk_bind">
										未验证&nbsp;<img src='../images/button_s.gif' border='0'
									alt='未验证' style='cursor: pointer;'>
							</s:if> <s:if test="'1'==is_chk_bind">
										mac绑定验证成功&nbsp;<img src='../images/check.gif' border='0'
									alt='验证成功' style='cursor: pointer'>
							</s:if> <s:if test="'2'==is_chk_bind">
										桥接账号验证成功&nbsp;<img src='../images/check.gif' border='0'
									alt='验证成功' style='cursor: pointer'>
							</s:if></td>
					</s:if>
					<s:else>
						<td></td>
						<td></td>
					</s:else>
				</tr>
			</s:if>
			<s:else>
				<tr>
					<td class=column colspan="4" align="center"><font color="red">用户未绑定设备！</font>
						<s:property value="device_serialnumber" /> <s:if
							test="null==device_serialnumber||''==device_serialnumber">
							<br>
							<font color="red">以下设备序列号是bss发过来的工单中带的，但设备没有上报，无法绑定！</font>
						</s:if></td>
				</tr>
				<tr>
					<td>设备序列号</td>
					<td><s:property value="oui" />- <s:property
							value="device_serialnumber" /></td>

					<td>绑定时间</td>
					<td><s:property value="binddate" /></td>
				</tr>
			</s:else>
		</s:if>
		<thead id="3">
			<td colspan="4">用户绑定卡信息</td>
		</thead>
		<tr>
			<s:if test="null!=usercardinfo">
				<td>卡序列号</td>
				<td><s:property value="card_serialnumber" /></td>
				<td>在线状态</td>
				<td>
					<button type="button" class=btn
						onclick="testonline(this,'device_id')">检测在线状态</button>
				</td>
			</s:if>
			<s:else>
				<td colspan="4" align="center"><font color="red">用户未绑定卡信息！</font></td>
			</s:else>
		</tr>
		<thead id="4">
			<td colspan="4">用户当前拥有业务</td>
		</thead>
		<s:iterator value="serviceinfo" status="sta">
			<tr>
				<td>业务名称<s:property value="#sta.count" /></td>
				<td>${serv_type_name }</td>
				<td>业务账号<s:property value="#sta.count" /></td>
				<td>${username }</td>
			</tr>
			<s:if test="'10'==serv_type_id">
				<s:if test="null!=tmap">
					<s:if test="0<tmap.size">
						<tr align="center">
							<td><strong>上网方式</strong></td>
							<td><strong>用户账号</strong></td>
							<td><strong>密码</strong></td>
							<td><strong>状态</strong></td>
						</tr>
						<tr align="center">
							<td><s:if test="'1'==parm_type_id">桥接</s:if> <s:elseif
									test="'2'==parm_type_id">路由</s:elseif> <s:else>未知</s:else></td>
							<td><s:property value="tmap['username']" /></td>
							<td><s:property value="tmap['passwd']" /></td>
							<td><s:if test="'1'==parm_stat">开通</s:if> <s:else>未开通</s:else>
							</td>
						</tr>
					</s:if>
					<s:else>
						<tr align="center">
							<td colspan="4"><strong>未查询到相关数据</strong></td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr align="center">
						<td colspan="4"><strong>后台查询数据异常</strong></td>
					</tr>
				</s:else>
			</s:if>
			<s:elseif test="'14'==serv_type_id">
				<s:if test="null!=vmap">
					<s:if test="0<vmap.size">
						<tr>
							<td align="center"><strong>线路</strong></td>
							<td align="center"><strong>用户账号</strong></td>
							<td align="center" colspan="2"><strong>电话号码</strong></td>
						</tr>
						<tr align="center">
							<td><s:property value="vmap['line_id']" /></td>
							<td><s:property value="vmap['voip_username']" /></td>
							<td colspan="2"><s:property value="vmap['voip_phone']" /></td>
						</tr>
					</s:if>
					<s:else>
						<tr align="center">
							<td colspan="4"><strong>未查询到相关数据</strong></td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr align="center">
						<td colspan="4"><strong>后台查询数据异常</strong></td>
					</tr>
				</s:else>
			</s:elseif>
		</s:iterator>
		<tr style="">
		<TD colspan="4" align="right" class=foot><INPUT
									TYPE="button" value=" 关 闭 " class=btn
									onclick="javascript:window.close()"> &nbsp;&nbsp;</TD>
		</tr>
	</table>
</body>
</html>