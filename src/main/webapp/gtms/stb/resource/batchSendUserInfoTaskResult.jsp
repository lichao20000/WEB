<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
			type="text/css">
		<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<lk:res />
		<SCRIPT LANGUAGE="JavaScript">
	$(function(){
		
	});	
</SCRIPT>

	</head>
	<body>
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD>
					<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
					您当前的位置：根据业务账号进行批量业务下发任务结果界面
				</TD>
			</TR>
		</TABLE>
		<br>
		<s:form action="openDeviceShowPic!batchImportUp.action" method="post" enctype="multipart/form-data" name="batchexform">
		<table width="98%" class="listtable" align="center">
		<thead>
				<tr>
					<th align="center" width="18%">
						业务账号
					</th>
					<th align="center" width="22%">
						下发结果
					</th>
					<th align="center" width="60%">
						详细
					</th>
				</tr>
			</thead>
                <s:if test="taskResultList.size()>0">
                        <tbody>
                                <s:iterator value="taskResultList">
                                        <tr>
                                                <td align="center"><s:property value="serv_account" /></td>
                                                <td align="center"><s:property value="status" /></td>
                                                <td align="center"><s:property value="checkRes" /></td>
                                        </tr>
                                </s:iterator>
                        </tbody>
                </s:if>
		<s:else>
			<tbody>
				<tr>
					<td colspan="3">没有查询结果！</td>
				</tr>
            </tbody>
		</s:else>
		<tfoot>
			<tr>
				<td colspan="3" align="right" class=column>
					<lk:pages url="/gtms/stb/resource/batchSendUserInfo!getShowBatchSendUserInfoTaskResult.action" 
					styleClass="" showType="" isGoTo="true" changeNum="true"/>
				</td>
			</tr>
		</tfoot>
		</table>
		<div id="divDetail"
			style="position: absolute; z-index: 255; top: 200px; left: 350px; width: 40%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none"></div>
</s:form>
</body>
