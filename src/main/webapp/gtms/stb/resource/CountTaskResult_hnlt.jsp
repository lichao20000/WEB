<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" LANGUAGE="JavaScript">
function queryDetail(upResult,cityId)
{
	var taskId = '<s:property value="taskId" />';
	var page = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!queryUpRecordByTaskIdHnlt.action'/>?taskId=" + taskId + "&upResult=" + upResult+"&cityId="+cityId;
	window.open(page,"","left=20,top=20,width=1200,height=600,resizable=no,scrollbars=yes");
}
function exportData()
{
    var taskId = '<s:property value="taskId" />';
    var cityId = '<s:property value="cityId" />';
	var page = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!exportSoftupTaskResult.action'/>?taskId=" + taskId + "&cityId=" + cityId;
	window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
}



</script>
</head>
<body>
<table width="100%" class="listtable">

	<thead>
		<tr>
		    <th align="center">属地</th>
			<th align="center">任务触发成功</th>
			<th align="center">软件升级成功</th>
			<th align="center">待触发</th>
			<th align="center">不支持</th>
			<th align="center">小计</th>
			<th align="center">总量占比</th>
			<th align="center">成功占比</th>
		</tr>
	</thead>
	<s:if test="taskResultMapList!=null && taskResultMapList.size()>0">
        <tbody>
            <s:iterator value="taskResultMapList">
                <tr>
                    <td align="center">
                        <s:property value="city_name" />
                    </td>
                    <s:if test='city_name =="占比"'>
                        <td align="center">
                           <s:property value="updatesucc" />
                        </td>
                        <td align="center">
                            <s:property value="softupsucc" />
                        </td>
                        <td align="center">
                            <s:property value="updatefailed" />
                        </td>
                        <td align="center">
                            <s:property value="unsupport" />
                        </td>
                        <td align="center">
                            <s:property value="total" />
                        </td>
                    </s:if>
                    <s:else>
                        <td align="center">
                            <a href="javascript:void(0);" onclick="queryDetail(1,'<s:property value="city_id"/>');"><s:property value="updatesucc" />
                        </td>
                        <td align="center">
                            <a href="javascript:void(0);" onclick="queryDetail(2,'<s:property value="city_id"/>');"><s:property value="softupsucc" />
                        </td>
                        <td align="center">
                            <a href="javascript:void(0);" onclick="queryDetail(-1,'<s:property value="city_id"/>');"><s:property value="updatefailed" />
                        </td>
                        <td align="center">
                            <a href="javascript:void(0);" onclick="queryDetail(3,'<s:property value="city_id"/>');"><s:property value="unsupport" />
                        </td>
                        <td align="center">
                            <a href="javascript:void(0);" onclick="queryDetail('','<s:property value="city_id"/>');"><s:property value="total" />
                        </td>
                    </s:else>
                    <td align="center">
                        <s:property value="totalPer" />
                    </td>
                    <td align="center">
                        <s:property value="succPer" />
                    </td>

                </tr>
            </s:iterator>
        </tbody>
        <tfoot>
            <tr bgcolor="#FFFFFF">
                <td colspan="8" align="left">
                    <button onclick="exportData()">
                        导出
                    </button>
                </td>
            </tr>
        </tfoot>
	</s:if>
</table>
</body>
</html>
