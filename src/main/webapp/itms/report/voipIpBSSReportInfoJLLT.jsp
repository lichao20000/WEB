<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<script type="text/javascript">
    function ListToExcel(cityId,startTimeSec,endTimeSec,type) {
        var page="<s:url value='/itms/report/VoipXIPBSSReportJL!getVoipBSSInfoExcel.action'/>?"
            + "cityId=" + cityId
            + "&startTimeSec=" + startTimeSec
            + "&endTimeSec=" +endTimeSec
            + "&type="+type;
        document.all("childFrm").src = page;
    }

</script>

<table class="listtable">
    <caption>
        工单接收详情
    </caption>
    <thead>
    <tr>
    <s:if test="type == 0 || type == 2 || type == -1">
        <th>
            地市编码
        </th>
        <th>
            地市名称
        </th>
    </s:if>
        <th>
            LOID
        </th>
        <th>
            语音IP
        </th>
    <s:if test="type != 0 && type != 2 && type != -1">
        <th>
            失败原因
        </th>
    </s:if>
    </tr>
    </thead>
    <tbody>
    <s:if test="resultReportInfoList.size()>0">
        <s:iterator value="resultReportInfoList">
            <tr>
                <s:if test="type == 0 || type == 2 || type == -1">
                <td>
                    <s:property value="cityId" />
                </td>
                <td>
                    <s:property value="cityName" />
                </td>
                </s:if>
                <td>
                    <s:property value="loid" />
                </td>
                <td>
                    <s:property value="ip" />
                </td>
                <s:if test="type != 0 && type != 2 && type != -1">
                <td>
                    <s:property value="failReason" />
                </td>
                </s:if>
            </tr>
        </s:iterator>
    </s:if>
    <s:else>
        <tr>
            <s:if test="type == 0 || type == 2 || type == -1">
            <td colspan=4>
                系统没有相关的工单信息!
            </td>
            </s:if>
            <s:if test="type != 0 && type != 2 && type != -1">
                <td colspan=3>
                    系统没有相关的工单信息!
                </td>
            </s:if>
        </tr>
    </s:else>
    </tbody>
    <tfoot>
    <tr>
        <td colspan="10">
				<span style="float: right;"> <lk:pages
                        url="/itms/report/VoipXIPBSSReportJL!getVoipBSSReportInfo.action" styleClass=""
                        showType="" isGoTo="true" changeNum="true" /> </span>
            <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
                 style='cursor: hand'
                 onclick="ListToExcel('<s:property value="cityId"/>','<s:property value="startTimeSec"/>',
                         '<s:property value="endTimeSec"/>','<s:property value="type"/>')">
        </td>
    </tr>


    <TR>
        <TD align="center" colspan="10">
            <button onclick="javascript:window.close();">
                &nbsp;关 闭&nbsp;
            </button>
        </TD>
    </TR>
    </tfoot>

    <tr STYLE="display: none">
        <td colspan="5">
            <iframe id="childFrm" src=""></iframe>
        </td>
    </tr>
</table>

<%@ include file="/foot.jsp"%>
