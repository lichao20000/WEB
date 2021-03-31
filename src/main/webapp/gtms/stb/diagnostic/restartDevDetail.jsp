<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
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
		<script type="text/javascript" src="../../../Js/My97DatePicker/WdatePicker.js"></script>
		<lk:res />
		<SCRIPT LANGUAGE="JavaScript">
		</SCRIPT>
	</head>
		<table width="98%" class="listtable" align="center">
			<thead>
				<tr>
					<th align="center" width="20%">
						属地
					</th>
					<th align="center" width="20%">
						业务账号
					</th>
					<th align="center" width="20%">
						设备序列号
					</th>
					<th align="center" width="20%">
						重启时间
					</th>
					<th align="center" width="20%">
						执行结果
					</th>
				</tr>
			</thead>
			<s:if test="devRestartResList!=null">
				<s:if test="devRestartResList.size()>0">
					<tbody>
						<s:iterator value="devRestartResList">
							<tr>
								<td align="center">
									<s:property value="cityName" />
								</td>
								<td align="center">
									<s:property value="servAcc" />
								</td>
								<td align="center">
									<s:property value="devSn" />
								</td>
								<td align="center">
									<s:property value="time" />
								</td>
								<td align="center">
									<s:property value="status" />
								</td>
							</tr>
						</s:iterator>
					</tbody>
					<tfoot>
						<tr bgcolor="#FFFFFF">
							<td colspan="5" align="right">
								<lk:pages url="/gtms/stb/diagnostic/stbDeviceBatchReboot!queryRestartDev.action"
									styleClass="" showType="" isGoTo="true" changeNum="false" />
							</td>
						</tr>
					</tfoot>
				</s:if>
				<s:else>
					<tbody>
						<tr>
							<td colspan="5">
								<font color="red">没有设备执行结果</font>
							</td>
						</tr>
					</tbody>
				</s:else>
			</s:if>
			<s:else>
				<tbody>
					<tr>
						<td colspan="5">
							<font color="red">没有设备执行结果</font>
						</td>
					</tr>
				</tbody>
			</s:else>


		</table>
			
		
			
