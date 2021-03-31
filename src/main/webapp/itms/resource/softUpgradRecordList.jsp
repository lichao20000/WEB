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
	 //函数名: dele
	 //功能  :	  根据recordId删除相应的软件升级记录
	 //返回值:	  调整界面
	 //说明  :	
	 //描述  :	Create 2015-6-4 of By yinlei3
	 ------------------------------------------------------------------------------*/
	function dele(recordId, fileName) {

		if (!window.confirm("真的要删除该条信息吗？\n本操作所删除的不能恢复！")) {
			return;
		}

		var url = "<s:url value='/itms/resource/softUpgradRecordQuery!deleRecordByRecordId.action'/>";

		$.post(url, {
			recordId : recordId,
			fileName : fileName
		}, function(ajax) {
			// 入库结果
			var dbResult = ajax.split("|")[0];
			// 文件删除结果
			var fileResult = ajax.split("|")[1];

			var str = "";
			if(dbResult == "1")
			{
				str += "数据库删除成功！";
			}
			else
			{
				str += "数据库删除成功！";
			}

			if(fileResult == "-1")
			{
				//不需要删除文件
			}
			else if(fileResult == "1")
			{
				str += "文件删除成功！";
			}
			else
			{
				str += "文件删除成失败！";
			}
			alert(str);
			// 普通方式提交
			var form = window.parent.document.getElementById("mainForm");
			form.submit();
		});
	}

	/*------------------------------------------------------------------------------
	 //函数名: modify
	 //功能  :	  根据recordId修改相应的软件升级记录
	 //返回值:	  修改页面
	 //说明  :	
	 //描述  :	Create 2015-6-4 of By yinlei3
	 ------------------------------------------------------------------------------*/
	function modify(recordId) {
		var url="<s:url value='/itms/resource/softUpgradRecordQuery!findRecordByRecordId.action'/>?" + "recordId=" + recordId;
		window.parent.location.href=url;
	}

	
</script>


<table width="100%" class="listtable" id=userTable>
	<thead>
		<tr>
			<th width="4%">终端厂家</th>
			<th width="4%">终端型号</th>
			<th width="6%">现有版本</th>
			<th width="6%">目标版本</th>
			<th width="10%">升级范围</th>
			<th width="5%">终端数量</th>
			<th width="15%">升级原因</th>
			<th width="9%">升级方式</th>
			<th width="15%">升级时间</th>
			<th width="10%">终端厂家联系方式</th>
			<th width="10%">附件</th>
			<th width="14%">操作</th>
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
								<IMG SRC="<s:url value="/images/edit.gif"/>" BORDER='0' ALT='编辑'
									onclick="modify('<s:property value='recordId' />')"
									style='cursor: hand'>
								<IMG SRC="<s:url value="/images/del.gif"/>" BORDER="0" ALT="删除"
									onclick="dele('<s:property value="recordId"/>', '<s:property value="file"/>')"
									style="cursor: hand">
							</ms:hasAuth></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=12 align=left>没有查询到相关数据！</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=12 align=left>没有查询到相关数据！</td>
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