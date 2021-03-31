<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>路由表版本信息列表</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
/* $(function() {
	$("#trData",parent.document).hide();
	$("#btn",parent.document).attr('disabled',false);
	parent.dyniframesize();
});

function ListToExcel(serialnumber,starttime3,endtime3){
	var page = "<s:url value='/ids/batchPingTest!queryPingResultExcel.action'/>?"
		+ "serialnumber="+serialnumber
		+ "&starttime3="+starttime3
		+ "&endtime3=" + endtime3;
	document.all("childFrm").src=page;
} */
</script>
</head>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
		<TR>
			<TD HEIGHT=20>&nbsp;</TD>
		</TR>
		<TR>
			<TD>

				<table width="98%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="text">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite" nowrap>
										路由表版本信息列表</td>
								</tr>
							</table>
						</td>
					</tr>

					<tr bgcolor="#FFFFFF">
						<td><input type="hidden" name="param" value="" />


											<table class="listtable" id="listTable">
												<thead>
													<tr>
														<th width="5%">版本id</th>
														<th width="10%">版本名称</th>
														<th width="20%">文件地址</th>
														<th width="10%">创建者id</th>
														<th width="10%">创建时间</th>
														<th width="8%">操作</th>
													</tr>
												</thead>
												<tbody>
													<s:if test="verHistoryList!=null">
														<s:if test="verHistoryList.size()>0">
															<s:iterator value="verHistoryList">
																<tr>
																	<td align="center"><s:property value="ver_id" /></td>
																	<td align="center"><s:property value="version" /></td>
																	<td align="center"><s:property value="down_path" /></td>
																	<td align="center"><s:property value="updator" /></td>
																	<td align="center"><s:property value="updatetime" /></td>
																	<td align="center"><a class="viewbtn" ver='<s:property value="version" />' 
																			href="javascript:view('<s:property value="version" />')">查看</a>|
																			 <a class="downloadbtn" ver='<s:property value="version" />' 
																					href="javascript:download('<s:property value="version" />')">下载</a>
																			</td>
																</tr>
															</s:iterator>
														</s:if>
														<s:else>
															<tr>
																<td colspan=13>系统没有匹配到相应信息!</td>
															</tr>
														</s:else>
													</s:if>
													<s:else>
														<tr>
															<td colspan=13>系统没有匹配到相应信息!</td>
														</tr>
													</s:else>
												</tbody>
										
												<tfoot>
													<tr>
														<td colspan="13" align="right"><span  style="float:right">
														<lk:pages
																url="/intelspeaker/intelSpeakerConfigMgrAction!listHistory.action?type=1" styleClass=""
																showType="" isGoTo="true" changeNum="true" /></span>
																</td>
													</tr>
										<tr STYLE="display: none">
													<td colspan="8"><iframe id="childFrm" src=""></iframe></td>
											</tr>
												</tfoot>
											</table>
					</td>
					</tr>
				</table>
			</TD>
		</TR>
	</TABLE>
</body>
<script type="text/javascript">
$(document).ready(function(){
	$(".viewbtn").bind('click',function(){
		var viewurl='<s:url value="/intelspeaker/intelSpeakerConfigMgrAction!queryOneVersion.action?type=1&version="/>'+$(this).attr("ver");
		window.open(viewurl,"_blanket","","");
	})	
	$(".downloadbtn").bind('click',function(){
		var downloadUrl='<s:url value="/intelspeaker/intelSpeakerConfigMgrAction!downloadOneVerFile.action?type=1&version="/>'+$(this).attr("ver");
		window.open(downloadUrl,"_blanket","","");
	})	
})
</script>
</html>
