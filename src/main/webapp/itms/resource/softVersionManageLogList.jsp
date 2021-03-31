<%@ page language="java" contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

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
          <th width="10%"> �豸����</th>
          <th width="10%"> �豸�ͺ�</th>
          <th width="10%"> Ӳ���汾</th>
          <th width="15%"> ԭʼ�汾</th>
          <th width="15%"> Ŀ��汾</th>
          <th width="12%"> ��Ӧ��ϵ����</th>
          <th width="5%"> ��������</th>
          <th width="8%"> ������Ա</th>
          <th width="15%"> ����ʱ��</th>
       </tr>
	</thead>
	<tbody>
		<s:if test="softVersionLogList != null ">
			<s:if test="softVersionLogList.size() > 0">
				<s:iterator value="softVersionLogList">
					<tr>
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
							 <s:property value="operate_type" />
						</td>
						<td align="center">
							 <s:property value="loginname" />
						</td>
						<td align="center">
							 <s:property value="operateTime" />
						</td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=9 align=left> û�в�ѯ��������ݣ� </td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=9 align=left> û�в�ѯ��������ݣ� </td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="9" align="right">
		 	<lk:pages
				url="/itms/resource/softVerManage!getVersionLogList.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
	</tfoot>
</table>