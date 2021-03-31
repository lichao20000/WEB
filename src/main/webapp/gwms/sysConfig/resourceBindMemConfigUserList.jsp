<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title></title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<script type="text/javascript">
	function DetailDevice(device_id){
		var strpage = "<s:url value='/Resource/DeviceShow.jsp'/>?device_id=" + device_id;
		window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
	function GoContent(user_id,gw_type){
		 if(gw_type=="2"){
		 	var strpage="<s:url value='/Resource/EGWUserRelatedInfo.jsp'/>?user_id=" + user_id;
		 }else{
			var strpage="<s:url value='/Resource/HGWUserRelatedInfo.jsp'/>?user_id=" + user_id;
		 }
			window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
	function configInfo(username){
		var url = "<s:url value="/gwms/sysConfig/resourceBindMemConfig!getUserDetail.action"/>";
		$("div[@id='userInfoDbMem']").hide();
		$.post(url,{
			username:username,
			gw_type:1
	    },function(mesg){
	    	$("div[@id='userInfoDbMem']").show();
			$("div[@id='userInfoDbMem']").html(mesg);
			parent.dyniframesize();
	    });
	}
</script>

</head>

<body>

<table class="listtable" id="listTable">
	<caption>查询结果</caption>
	<thead>
		<tr>
			<th>LOID</th>
			<th>属地</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="userList!=null">
			<s:iterator value="userList">
				<tr>
					<td align="center">
					<a href="javascript:GoContent('<s:property value="user_id" />',1);">
						<s:property value="username" />
					</a>
					</td>
					<td align="center"><s:property value="city_name" /></td>
					<td align="center"><a href="javascript:configInfo('<s:property value="username" />')">DB与MEM信息比对</a></td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=8>
					系统没有此用户!
					<a href="javascript:configInfo('<s:property value="username" />')">获取MEM信息比对</a>
				</td>
			</tr>
		</s:else>
	</tbody>
</table>
</body>
</html>