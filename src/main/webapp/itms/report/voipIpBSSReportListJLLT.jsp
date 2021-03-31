<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
      type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
      type="text/css">

<table class="listtable">
    <caption>统计结果</caption>
    <s:if test="resultReportList.size()>0">
        <thead>

        <tr>
            <th>地市</th>
            <th>接收工单总数量</th>
            <th>接收成功工单数量</th>
            <th>接收失败工单数量</th>
            <th>无绑定设备数量</th>
            <th>工单接收成功率</th>
        </tr>
        </thead>

        <tbody>
        <s:iterator value="resultReportList" var="map1">
            <tr>
                <td align="center">
                    <s:property value="cityName" />
                </td>
                <td align="center"><a
                        href="javascript:viewDetail('<s:property value="cityId"/>','<s:property value="startTimeSec"/>',
													'<s:property value="endTimeSec"/>','-1','<s:property value="total" />')">
                    <s:property value="total" />
                </a>
                </td>
                <td align="center"><a
                        href="javascript:viewDetail('<s:property value="cityId"/>','<s:property value="startTimeSec"/>',
													'<s:property value="endTimeSec"/>','0','<s:property value="successNum" />')">
                    <s:property value="successNum" />
                </a>
                </td>
                <td align="center"><a
                        href="javascript:viewDetail('<s:property value="cityId"/>','<s:property value="startTimeSec"/>',
													'<s:property value="endTimeSec"/>','1','<s:property value="failNum" />')">
                    <s:property value="failNum" />
                </a>
                </td>
                <td align="center"><a
                        href="javascript:viewDetail('<s:property value="cityId"/>','<s:property value="startTimeSec"/>',
													'<s:property value="endTimeSec"/>','2','<s:property value="unBindNum" />')">
                    <s:property value="unBindNum" />
                </a>
                </td>
                <td align="center">
                    <s:property value="successRate" />
                </td>

            </tr>
        </s:iterator>
        </tbody>
        <tfoot>
        <tr>
            <td colspan='6'></td>
        </tr>
        </tfoot>
    </s:if>
    <s:else>
        <tfoot>
        <tr>
            <td align="left">没有相关信息</td>
        </tr>
        </tfoot>
    </s:else>
    <tr STYLE="display: none">
        <td colspan="9"><iframe id="childFrm" src=""></iframe></td>
    </tr>
</table>


