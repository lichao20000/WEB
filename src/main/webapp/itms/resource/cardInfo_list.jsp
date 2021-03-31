<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
			type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript"
			src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

		<script type="text/javascript">
	$(function(){
		parent.dyniframesize();
	});

</script>

	</head>

	<body>
		<table class="listtable">
			<caption>
				查询结果
			</caption>
			<thead>
				<tr>
					<th width="10%">
						卡编号
					</th>
					<th width="30%">
						卡序列号
					</th>
					<th width="30%">
						用户账号
					</th>
					<th width="30%">
						在线状态
					</th>
					<!-- 
					<th width="15%">	
						操作
					</th>
					 -->
				</tr>
			</thead>
			<tbody>
				<s:if test="card_list!=null">
					<s:if test="card_list.size()>0">
						<s:iterator value="card_list">
							<tr>
								<td>
									<s:property value="card_id" />
								</td>
								<td>
									<s:property value="card_serialnumber" />
								</td>
								<td>
									<a href="javascript:GoContent('<s:property value="user_id" />',1);" ><s:property value="username" /></a>
								</td>
								<!-- 
								<td>
									<s:if test="online_status == 0">
										下线
									</s:if>
									<s:if test="online_status == 1">
										在线
									</s:if>
								</td>
								 -->
								<td>
									<span style="width:125"></span>&nbsp;&nbsp;
									<button onclick="testOnline(this,'<s:property value="device_id" />')">检测在线状态</button>
			<!-- 						<a href="javascript:testOnline('<s:property value="device_id" />');" >检测在线状态</a>   -->
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td colspan=4>
								没有查询到相关卡信息！
							</td>
						</tr>
					</s:else>
				</s:if>
				<s:else>
					<tr>
						<td colspan=4>
							没有查询到相关卡信息！
						</td>
					</tr>
				</s:else>
			</tbody>
  
			<tfoot>
				<tr>
					<td colspan="4" align="right">
						<lk:pages
							url="/itms/resource/cardInfo!queryCard.action"
							styleClass="" showType="" isGoTo="true" changeNum="true" />
					</td>
				</tr>
			</tfoot>

		</table>
	</body>
<script>
function GoContent(user_id,gw_type){
	 if(gw_type=="2"){
	 	var strpage="<s:url value='/Resource/EGWUserRelatedInfo.jsp'/>?user_id=" + user_id;
	 }else{
		var strpage="<s:url value='/Resource/HGWUserRelatedInfo.jsp'/>?user_id=" + user_id;
	 }
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

function testOnline(obj,device_id)
{
	if(device_id=="")
	{
		alert("卡没有绑定设备！");
		return;
	}
	obj.disabled = true;
	var span = obj.parentNode.firstChild;
	//var content = obj.parentNode.previousSibling.innerHTML;
	span.innerHTML = "<font color=blue>正在获取在线状态...</font>";
	
	var url = "<s:url value='/itms/resource/cardInfo!getCardStatus.action'/>";
	$.post(url,{device_id:device_id
	},function(ajax){
		//alert(ajax);
	    // 采集成功
		if(parseInt(ajax.split("|")[0]) == 1)
		{
			if(parseInt(ajax.split("|")[1]) == 1)
			{
				span.innerHTML = "<font color=green>在线(实时)</font>";
			}
			else
			{
				span.innerHTML = "<font color=red>下线(实时)</font>";
			}
		}
		// 采集失败
		else
		{
			obj.parentNode.firstChild.innerHTML = "";
			alert(ajax.split("|")[1]);
		}
		obj.disabled = false;
	});
}

</script>
</html>