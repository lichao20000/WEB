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
					您当前的位置：批量策略任务结果界面
				</TD>
			</TR>
		</TABLE>
		<br>
		<s:form action="batchConfigNode!batchImportUp.action" method="post" enctype="multipart/form-data" name="batchexform">
		<table width="98%" class="listtable" align="center">
		<thead>
				<tr>
					<th align="center" width="8%">
						属地
					</th>
					<th align="center" width="8%">
						厂商
					</th>
					<th align="center" width="8%">
						型号
					</th>
					<th align="center" width="8%">
						软件版本
					</th>
					<th align="center" width="8%">
						设备序列号
					</th>
					<th align="center" width="12%">
						业务账号
					</th>
					<th align="center" width="12%">
						ip地址
					</th>
					<th align="center" width="12%">
						mac地址
					</th>
					<th align="center" width="12%">
						配置结果
					</th>
					<th align="center" width="12%">
						操作时间
					</th>
				</tr>
			</thead>
			<s:if test="taskResultList!=null">
                <s:if test="taskResultList.size()>0">
                        <tbody>
                                <s:iterator value="taskResultList">
                                        <tr>
                                                <td align="center"><s:property value="citName" /></td>
                                                <td align="center"><s:property value="vendorName" /></td>
                                                <td align="center"><s:property value="deviceModelName" /></td>
                                                <td align="center"><s:property value="deviceTypeName" /></td>
	                                            <td align="center"><s:property value="deviceSn" /></td>
	                                            <td align="center"><s:property value="servAccount" /></td>
	                                            <td align="center"><s:property value="loopbackIp" /></td>
	                                            <td align="center"><s:property value="cpeMac" /></td>
	                                            <td align="center"><s:property value="result" /></td>
	                                            <td align="center"><s:property value="updateTime" /></td>
                                        </tr>
                                </s:iterator>
                        </tbody>
                </s:if>
</s:if>
		</table>
		<div id="divDetail"
			style="position: absolute; z-index: 255; top: 200px; left: 350px; width: 40%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none"></div>
</s:form>
</body>
