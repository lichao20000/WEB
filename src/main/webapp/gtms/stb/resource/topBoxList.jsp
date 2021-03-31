<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%--
	/**
	 * �������б�
	 *
	 * @author zhumiao
	 * @version 1.0
	 * @since 2011-12-5 ����10:13:16
	 *
	 * <br>��Ȩ���Ͼ������Ƽ� ���ܿƼ���
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
	   		parent.setFrameHeight($("body").attr("scrollHeight")+50);//����iframe�ĸ߶�
		});
		function fixed(){
			if($('input[name=keyStr]:checked').length == 0){
				alert('����ѡ��һ�����в�ѯ');
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
				<th width="3%">ѡ��</th>
				<th width="10%">�û��ʺ�</th>
				<th width="8%">�豸����</th>
				<th width="10%">�豸�ͺ�</th>
				<th width="16%">����汾</th>
				<th width="12%">����</th>
				<th width="16%">����ʱ��</th>
				<th width="25%">�豸���к�</th>
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
				<td colspan="3">δ�ҵ��������...</td>
			</tr>
		</s:else>
		</tbody>
		<tfoot id="acc_foot">
				<tr align="right">
					<td align="right" class="foot" colspan="8">
						<button onclick="fixed()">ȷ ��</button>
					</td>
				</tr>
			</tfoot>
	</table>
</body>
</html>
