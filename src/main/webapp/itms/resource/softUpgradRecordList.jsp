<%@ page language="java" contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="../../Js/jquery.js"></script>
<script type="text/javascript" src="../../Js/jQuerySplitPage-linkage.js"></script>


<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});

	function downLoad(fileName) {
		var url = "<s:url value='/itms/resource/softUpgradRecordQuery!download.action'/>";
		document.downloadForm.action = url;
		$("input[@name='fileName']").val(fileName);
		document.downloadForm.submit();
		return;

	}
	/*------------------------------------------------------------------------------
	 //������: dele
	 //����  :	  ����recordIdɾ����Ӧ�����������¼
	 //����ֵ:	  ��������
	 //˵��  :	
	 //����  :	Create 2015-6-4 of By yinlei3
	 ------------------------------------------------------------------------------*/
	function dele(recordId, fileName) {

		if (!window.confirm("���Ҫɾ��������Ϣ��\n��������ɾ���Ĳ��ָܻ���")) {
			return;
		}

		var url = "<s:url value='/itms/resource/softUpgradRecordQuery!deleRecordByRecordId.action'/>";

		$.post(url, {
			recordId : recordId,
			fileName : fileName
		}, function(ajax) {
			// �����
			var dbResult = ajax.split("|")[0];
			// �ļ�ɾ�����
			var fileResult = ajax.split("|")[1];

			var str = "";
			if(dbResult == "1")
			{
				str += "���ݿ�ɾ���ɹ���";
			}
			else
			{
				str += "���ݿ�ɾ���ɹ���";
			}

			if(fileResult == "-1")
			{
				//����Ҫɾ���ļ�
			}
			else if(fileResult == "1")
			{
				str += "�ļ�ɾ���ɹ���";
			}
			else
			{
				str += "�ļ�ɾ����ʧ�ܣ�";
			}
			alert(str);
			// ��ͨ��ʽ�ύ
			var form = window.parent.document.getElementById("mainForm");
			form.submit();
		});
	}

	/*------------------------------------------------------------------------------
	 //������: modify
	 //����  :	  ����recordId�޸���Ӧ�����������¼
	 //����ֵ:	  �޸�ҳ��
	 //˵��  :	
	 //����  :	Create 2015-6-4 of By yinlei3
	 ------------------------------------------------------------------------------*/
	function modify(recordId) {
		var url="<s:url value='/itms/resource/softUpgradRecordQuery!findRecordByRecordId.action'/>?" + "recordId=" + recordId;
		window.parent.location.href=url;
	}

	
</script>


<table width="100%" class="listtable" id=userTable>
	<thead>
		<tr>
			<th width="4%">�ն˳���</th>
			<th width="4%">�ն��ͺ�</th>
			<th width="6%">���а汾</th>
			<th width="6%">Ŀ��汾</th>
			<th width="10%">������Χ</th>
			<th width="5%">�ն�����</th>
			<th width="15%">����ԭ��</th>
			<th width="9%">������ʽ</th>
			<th width="15%">����ʱ��</th>
			<th width="10%">�ն˳�����ϵ��ʽ</th>
			<th width="10%">����</th>
			<th width="14%">����</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="softUpgradRecordList != null ">
			<s:if test="softUpgradRecordList.size() > 0">
				<s:iterator value="softUpgradRecordList">
					<tr>
						<td align="center"><s:property value="vendorName" /></td>
						<td align="center"><s:property value="device_model" /></td>
						<td align="center"><s:property value="currentSoftWareVersion" />
						</td>
						<td align="center"><s:property value="targetSoftWareVersion" />
						</td>
						<td align="center"><s:property value="upgradeRange" /></td>
						<td align="center"><s:property value="deviceCount" /></td>
						<td align="center"><s:property value="upgradeReason" /></td>
						<td align="center"><s:property value="upgradeMethod" /></td>
						<td align="center"><s:property value="upgradeTime" /></td>
						<td align="center"><s:property value="contactWay" /></td>
						<td align="center"><a href="javascript:void(0);"
							onclick="downLoad('<s:property value="file" />')"><s:property
									value="file" /></a></td>
						<td align="center"><ms:hasAuth authCode="ShowDevPwd">
								<IMG SRC="<s:url value="/images/edit.gif"/>" BORDER='0' ALT='�༭'
									onclick="modify('<s:property value='recordId' />')"
									style='cursor: hand'>
								<IMG SRC="<s:url value="/images/del.gif"/>" BORDER="0" ALT="ɾ��"
									onclick="dele('<s:property value="recordId"/>', '<s:property value="file"/>')"
									style="cursor: hand">
							</ms:hasAuth></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=12 align=left>û�в�ѯ��������ݣ�</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=12 align=left>û�в�ѯ��������ݣ�</td>
			</tr>
		</s:else>
	<tfoot>
		<tr>
			<td colspan="12" align="right"><lk:pages
					url="/itms/resource/softUpgradRecordQuery!getSoftUpgradRecordQuery.action"
					styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
	</tfoot>
	</tbody>
	<FORM method="post" action="" id="downloadForm" name="downloadForm">
		<input type="hidden" name="fileName" id="downloadInput" />
	</FORM>
</table>