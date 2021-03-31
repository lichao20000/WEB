<%@ page language="java" contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@page import="com.linkage.litms.system.*"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="../../Js/jquery.js"></script>
<script type="text/javascript" src="../../Js/jQuerySplitPage-linkage.js"></script>

<%
	UserRes current_user = (UserRes) session.getAttribute("curUser");
	String city_id = current_user.getUser().getCityId();
	long user_id = current_user.getUser().getId();
%>

<script type="text/javascript">

   var request = false;
   try {
     request = new XMLHttpRequest();
   } catch (trymicrosoft) {
     try {
       request = new ActiveXObject("Msxml2.XMLHTTP");
       
     } catch (othermicrosoft) {
       try {
         request = new ActiveXObject("Microsoft.XMLHTTP");
       } catch (failed) {
         request = false;
       }  
     }
   }
   if (!request)
     alert("Error initializing XMLHttpRequest!");
	
	$(function(){
		parent.dyniframesize();
	});
	
</script>


<table width="100%" class="listtable" id=userTable>
    <caption> ��ͼ����ģ���б� </caption>
	<thead>
       <tr>
          <th width="19%"> ģ������</th>
          <th width="12%"> ����</th>
          <th width="16%"> �豸����</th>
          <th width="17%"> �豸�ͺ�</th>
          <th width="16%"> ����汾</th>
          <th width="6%"> �Ƿ�Ĭ��</th>
          <th width="14%"> ����</th>
       </tr>
	</thead>
	<tbody>
		<s:if test="digitMapList!=null ">
			<s:if test="digitMapList.size()>0">
				<s:iterator value="digitMapList">
					<tr>
						<td>
							<s:property value="map_name" />
						</td>
						<td>
							<s:property value="city_name" />
						</td>
						<td>
							<s:property value="vendor_add" />
						</td>
						<td>
							<s:property value="device_model" />
						</td>
						<td>
							<s:property value="softwareversion" />
						</td>
						<s:if test = 'is_default=="0"'>
						<td>
							��
						</td>
						</s:if>
						<s:else>
						<td>
							��
						</td>
						</s:else>
						<td align="center">
							<a href="javascript:deleteMap('<s:property value='map_id' />',
														'<s:property value='acc_oid' />')">ɾ��</a>|
							<a href="javascript:updateMap('<s:property value='map_id' />',
														'<s:property value='acc_oid' />',
														'<s:property value='vendor_id' />',
														'<s:property value='device_model_id' />',
														'<s:property value='devicetype_id' />',
														'<s:property value='is_default' />')">�༭</a>
						</td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=8 align=left> û�в�ѯ��������ݣ� </td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=8 align=left> û�в�ѯ��������ݣ� </td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td align=right colspan=8> <button onclick="add()"> �� �� </button> </td>
		</tr>
	</tfoot>
</table>
		
<script LANGUAGE="JavaScript">
	// ��ǰ�û��ĵ���
	city_id = "<%=city_id%>";
	// ��ǰ�û���id
	user_id = "<%=user_id%>";
	
	// �������
	function add(){
		var url = "<s:url value='/itms/config/digitMapConfig!addInit.action'/>?action=add";
		window.open(url,"","left=10,top=10,resizable=yes,scrollbars=yes,width=600,height=400");
	}
	
	function deleteMap(id,acc_oid){
		// ʡ�����û������е�Ȩ��
		if(city_id == "00"){
		}else{
			if( (acc_oid+"") != user_id){
				alert("ֻ��ģ��Ĵ����߲���Ȩ��ɾ����ģ�壡");
				return;
			}
		}
		
		if(!window.confirm("ȷ��Ҫɾ����ģ�壿")){
			return;
		}
		
		var url = "<s:url value='/itms/config/digitMapConfig!delete.action'/>";
		
		$.post(url,{
			map_id:id
		},function(ajax){
			var result = parseInt(ajax);
			if(result < 0){
				alert("ɾ��ʧ�ܣ�");
				return;
			}
			if(result > 0){
				alert("ɾ���ɹ���");
				//window.location.reload();
				// ��ͨ��ʽ�ύ
				var form = window.parent.document.getElementById("mainForm");
				form.action = "<s:url value='/itms/config/digitMapConfig!queryList.action'/>";
				form.submit();
			}
		});
	}
	
	function updateMap(id, acc_oid, vendor_id, device_model_id, devicetype_id,is_default)
	{	
		// ʡ�����û������е�Ȩ��
		if(city_id != "00"){
			if( (acc_oid+"") != user_id){
				alert("ֻ��ģ��Ĵ����߲���Ȩ�ޱ༭��ģ�壡");
				return;
			}
		}
		
		var url = "<s:url value='/itms/config/digitMapConfig!updateQuery.action'/>"
					+"?map_id="+id+"&vendor_id="+vendor_id
					+"&device_model_id="+device_model_id
					+"&devicetype_id="+devicetype_id+"&is_default="+is_default;

		window.open(url,"","left=10,top=10,resizable=yes,scrollbars=yes,width=600,height=400");
	}
</script>