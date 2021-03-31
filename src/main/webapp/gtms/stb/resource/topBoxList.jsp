<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%--
	/**
	 * 机顶盒列表
	 *
	 * @author zhumiao
	 * @version 1.0
	 * @since 2011-12-5 上午10:13:16
	 *
	 * <br>版权：南京联创科技 网管科技部
	 *
	 */
 --%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK">
	<title>Insert title here</title>
	<lk:res />
	<style type="text/css">

	</style>
	<script type="text/javascript">
		$(function(){
	   		parent.setFrameHeight($("body").attr("scrollHeight")+50);//设置iframe的高度
		});
		function fixed(){
			if($('input[name=keyStr]:checked').length == 0){
				alert('至少选择一个进行查询');
				return false;
			}
			var keystr = $("input[name=keyStr]:checked").val().split("|");
			$.ajax({type:'POST',
				url:'<s:url value="/gtms/stb/resource/zeroconfManual!manualConfiguration.action"/>',
				data:{
					"oui":keystr[0],
					"device_serialnumber":keystr[1],
					"device_id":keystr[2],
					"customer_id":keystr[3]
				},
				success:function(data){
					alert(data);
				},
				error:function(){

				}
			});
		}
	</script>
</head>
<body>
	<table class="listtable" align="center" style="margin-top:10px;width:100%;top: 10px;text-align: center">
		<thead>
			<tr align="center">
				<th width="3%">选择</th>
				<th width="10%">用户帐号</th>
				<th width="8%">设备厂商</th>
				<th width="10%">设备型号</th>
				<th width="16%">软件版本</th>
				<th width="12%">属地</th>
				<th width="16%">上线时间</th>
				<th width="25%">设备序列号</th>
			</tr>
		</thead>
		<tbody>
		<s:if test="topBoxList != null">
			<s:iterator var="topBox" value="topBoxList">
				<tr>
					<td><input type="radio" name="keyStr" value="<s:property value='#topBox.oui'/>|<s:property value='#topBox.device_serialnumber'/>|<s:property value="#topBox.device_id"/>|<s:property value="#topBox.customer_id"/>"/></td>
					<td><s:property value="#topBox.serv_account"/></td>
					<td><s:property value="#topBox.vendor_add"/></td>
					<td><s:property value="#topBox.device_model"/></td>
					<td><s:property value="#topBox.softwareversion"/></td>
					<td><s:property value="#topBox.city_name"/></td>
					<td><s:property value="#topBox.last_time"/></td>
					<td><s:property value="#topBox.device_serialnumber"/></td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="3">未找到相关数据...</td>
			</tr>
		</s:else>
		</tbody>
		<tfoot id="acc_foot">
				<tr align="right">
					<td align="right" class="foot" colspan="8">
						<button onclick="fixed()">确 定</button>
					</td>
				</tr>
			</tfoot>
	</table>
</body>
</html>
