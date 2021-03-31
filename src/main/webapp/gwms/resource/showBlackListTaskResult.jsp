<%@page import="com.linkage.litms.LipossGlobals"%>
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
		
		<script language="text/javascript">
		</script>
		

	</head>
	<body>
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD>
					<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
					您当前的位置：黑名单任务结果界面
				</TD>
			</TR>
		</TABLE>
		<br>
		<form id="selectForm" name="selectForm" action="" target="childFrm">
		<input type="hidden" name="upResult" value='<s:property value="upResult"/>'/>
		<input type="hidden" name="taskId" value='<s:property value="taskId"/>'/>
		<table width="98%" class="listtable" align="center">
		<%	String area= LipossGlobals.getLipossProperty("InstArea.ShortName"); %>
		<thead>
				<tr>
					<th align="center" <%if("hb_lt".equals(area)){%>width="6%"<%}else{%>width="8%"<%}%> >
						属地
					</th>
					<th align="center" width="8%">
						设备序列号
					</th>
					<th align="center" width="8%">
						宽带账号
					</th>				 
					<th align="center" <%if("hb_lt".equals(area)){%>width="10%"<%}else{%>width="12%"<%}%> >
						添加时间
					</th>
				</tr>
			</thead>
                <s:if test="taskResultList != null && taskResultList.size()>0">
                        <tbody>
                                <s:iterator value="taskResultList">
                                        <tr>
                                                <td align="center"><s:property value="cityName" /></td>
	                                            <td align="center"><s:property value="deviceSerialNumber" /></td>
	                                            <td align="center"><s:property value="pppoe_name" /></td>
	                                            <td align="center"><s:property value="add_time" /></td>
                                        </tr>
                                </s:iterator>
                        </tbody>
                </s:if>
		<s:else>
			<tbody>
				<tr>
					<td colspan="10">没有查询到相关设备！</td>
				</tr>
            </tbody>
		</s:else>
		<tfoot>
			<tr>
				<%
					if("hb_lt".equals(area)){
					%>
					<td colspan="8" align="right" class=column>
						<lk:pages url="/gwms/resource/batchHttpTestBlackListMana!getTestSpeedTaskResult.action"
						styleClass="" showType="" isGoTo="true" changeNum="true"/>
					</td>
					<%
					}else{
				%>
					<td colspan="7" align="right" class=column>
						<lk:pages url="/gwms/resource/batchHttpTestBlackListMana!getTestSpeedTaskResult.action"
						styleClass="" showType="" isGoTo="true" changeNum="true"/>
					</td>
				<%
					}
				%>
				
			</tr>
			<tr STYLE="display: none">
				<td colspan="4">
					<iframe id="childFrm" name="childFrm" src=""></iframe>
				</td>
			</tr>
		</tfoot>
		</table>
		<div id="divDetail"
			style="position: absolute; z-index: 255; top: 200px; left: 350px; width: 40%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none"></div>
</form>
</body>
