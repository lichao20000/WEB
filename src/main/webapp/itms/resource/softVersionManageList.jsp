<%@ page language="java" contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="../../Js/jquery.js"></script>
<script type="text/javascript" src="../../Js/jQuerySplitPage-linkage.js"></script>


<script type="text/javascript">

	$(function(){
		parent.dyniframesize();
	});


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
	
</script>


<table width="100%" class="listtable" id=userTable>
	<thead>
       <tr>
          <ms:inArea areaCode="sd_lt,sd_dx" notInMode="false">
          <th width="20%"> ����</th>
          </ms:inArea>
          <th width="10%"> �豸����</th>
          <th width="10%"> �豸�ͺ�</th>
          <th width="10%"> Ӳ���汾</th>
          <th width="15%"> ԭʼ�汾</th>
          <th width="15%"> Ŀ��汾</th>
          <th width="10%"> ��Ӧ��ϵ����</th>
          <th width="10%"> ����</th>
       </tr>
	</thead>
	<tbody>
		<s:if test="softVersionList != null ">
			<s:if test="softVersionList.size() > 0">
				<s:iterator value="softVersionList">
					<tr>
						<ms:inArea areaCode="sd_lt,sd_dx" notInMode="false">
				         <td>
							<s:property value="city_name" />
						</td>
				         </ms:inArea>
						<td>
							<s:property value="vendorName" />
						</td>
						<td>
							<s:property value="device_model" />
						</td>
						<td>
							<s:property value="hardwareversion" />
						</td>
						<td>
							<s:property value="origSoftWareVersion" />
						</td>
						<td>
							<s:property value="targetSoftWareVersion" />
						</td>
						<td>
							<s:property value="tempName" />
						</td>
						<td align="center">
							<a href="javascript:dele('<s:property value='devicetype_id_old' />','<s:property value='devicetype_id_new' />','<s:property value='tempId' />','<s:property value='city_id' />')">ɾ��</a>
						</td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
				<ms:inArea areaCode="sd_lt,sd_dx" notInMode="false">
					<td colspan=8 align=left> û�в�ѯ��������ݣ� </td>
				</ms:inArea>
				<ms:inArea areaCode="sd_lt,sd_dx" notInMode="true">
					<td colspan=7 align=left> û�в�ѯ��������ݣ� </td>
				</ms:inArea>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<ms:inArea areaCode="sd_lt,sd_dx" notInMode="false">
					<td colspan=8 align=left> û�в�ѯ��������ݣ� </td>
				</ms:inArea>
				<ms:inArea areaCode="sd_lt,sd_dx" notInMode="true">
					<td colspan=7 align=left> û�в�ѯ��������ݣ� </td>
				</ms:inArea>
			</tr>
		</s:else>
		<tfoot>
		<tr>
		<ms:inArea areaCode="sd_lt,sd_dx" notInMode="false">
			<td colspan="8" align="right">
		 	<lk:pages
				url="/itms/resource/softVerManage!getVersionList.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</ms:inArea>
		<ms:inArea areaCode="sd_lt,sd_dx" notInMode="true">
			<td colspan="7" align="right">
		 	<lk:pages
				url="/itms/resource/softVerManage!getVersionList.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</ms:inArea>
		</tr>
	</tfoot>
	</tbody>
</table>
		
<script LANGUAGE="JavaScript">
	function dele(id,targetId,tempId,city_id){
		
		if(!window.confirm("���Ҫɾ��������Ϣ��\n��������ɾ���Ĳ��ָܻ���")){
			return;
		}
		
		var url = "<s:url value='/itms/resource/softVerManage!deleInfo.action'/>";
		
		$.post(url,{
			devicetypeIdOld : id,
			devicetypeIdNew :targetId,
			tempId : tempId,
			city_id:city_id
		},function(ajax){
		
			var result = ajax.split(";");
			
			alert(result[1]);
			
			if("1" == result[0]){// ɾ���ɹ��������ύ��ѯ
				// ��ͨ��ʽ�ύ
				var form = window.parent.document.getElementById("mainForm");
				form.action = "<s:url value='/itms/resource/softVerManage!getVersionList.action'/>";
				parent.resetFrm();  /** �ύ��������� */
				form.submit();
			}
		});
	}
</script>