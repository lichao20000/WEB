<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
      type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
      type="text/css">

<table class="listtable">
    <caption>
        查询结果
    </caption>
    <s:if test="data.size()>0">
        <thead>
        <tr>
            <th class="title_1" rowspan="2">
                本地网
            </th>
            <th colspan="4">
                宽带
            </th>
            <th colspan="4">
                IPTV
            </th>
            <th colspan="4">
                VoIP
            </th>
            <th colspan="4">
                汇总
            </th>
        </tr>
        <tr>

            <th>
                下发成功数
            </th>
            <th>
                下发失败数
            </th>
            <th>
                下发总数
            </th>
            <th>
                成功率
            </th>

            <th>
                下发成功数
            </th>
            <th>
                下发失败数
            </th>
            <th>
                下发总数
            </th>
            <th>
                成功率
            </th>

            <th>
                下发成功数
            </th>
            <th>
                下发失败数
            </th>
            <th>
                下发总数
            </th>
            <th>
                成功率
            </th>

            <th>
                下发成功数
            </th>
            <th>
                下发失败数
            </th>
            <th>
                下发总数
            </th>
            <th>
                成功率
            </th>
        </tr>
        </thead>

        <tbody>
        <s:iterator value="data" var="map1">
            <tr>
                    <%-- 城市名称 --%>
                <td align="center">
                    <s:property value="cityName"/>
                </td>
                    <%-- 宽带 --%>

                <td align="center">
                    <a href="javascript:openHgw('<s:property value="cityId"/>','<s:property value="starttime1"/>',
													'<s:property value="endtime1"/>','10','<s:property value="gwType"/>','success')">
                        <s:property value="broadbandSucNum"/> </a>
                </td>
                <td align="center">
                    <a href="javascript:openHgw('<s:property value="cityId"/>','<s:property value="starttime1"/>',
													'<s:property value="endtime1"/>','10','<s:property value="gwType"/>','failure')">
                        <s:property value="broadbandFalNum"/> </a>
                </td>
                <td align="center">
                    <a href="javascript:openHgw('<s:property value="cityId"/>','<s:property value="starttime1"/>',
						   							'<s:property value="endtime1"/>','10','<s:property value="gwType"/>','total')">
                        <s:property value="broadbandTotalNum"/> </a>
                </td>
                <td align="center">
                    <s:property value="broadbandSucRate"/>
                </td>
                    <%-- IPTV --%>

                <td align="center">
                    <a href="javascript:openHgw('<s:property value="cityId"/>','<s:property value="starttime1"/>',
													'<s:property value="endtime1"/>','11','<s:property value="gwType"/>','success')">
                        <s:property value="iptvSucNum"/> </a>
                </td>
                <td align="center">
                    <a href="javascript:openHgw('<s:property value="cityId"/>','<s:property value="starttime1"/>',
													'<s:property value="endtime1"/>','11','<s:property value="gwType"/>','failure')">
                        <s:property value="iptvFalNum"/> </a>
                </td>
                <td align="center">
                    <a href="javascript:openHgw('<s:property value="cityId"/>','<s:property value="starttime1"/>',
						   							'<s:property value="endtime1"/>','11','<s:property value="gwType"/>','total')">
                        <s:property value="iptvTotalNum"/> </a>
                </td>
                <td align="center">
                    <s:property value="iptvSucRate"/> </a>
                </td>
                    <%-- VOIP --%>

                <td align="center">
                    <a href="javascript:openHgw('<s:property value="cityId"/>','<s:property value="starttime1"/>',
													'<s:property value="endtime1"/>','14','<s:property value="gwType"/>','success')">
                        <s:property value="voipSucNum"/> </a>
                </td>
                <td align="center">
                    <a href="javascript:openHgw('<s:property value="cityId"/>','<s:property value="starttime1"/>',
													'<s:property value="endtime1"/>','14','<s:property value="gwType"/>','failure')">
                        <s:property value="voipFalNum"/> </a>
                </td>
                <td align="center">
                    <a href="javascript:openHgw('<s:property value="cityId"/>','<s:property value="starttime1"/>',
						   							'<s:property value="endtime1"/>','14','<s:property value="gwType"/>','total')">
                        <s:property value="voipTotalNum"/> </a>
                </td>
                <td align="center">
                    <s:property value="voipSucRate"/>
                </td>
                    <%-- 汇总数 --%>

                <td align="center">
                    <a href="javascript:openHgw('<s:property value="cityId"/>','<s:property value="starttime1"/>',
													'<s:property value="endtime1"/>','-1','<s:property value="gwType"/>','success')">
                        <s:property value="totalSucNum"/> </a>
                </td>
                <td align="center">
                    <a href="javascript:openHgw('<s:property value="cityId"/>','<s:property value="starttime1"/>',
													'<s:property value="endtime1"/>','-1','<s:property value="gwType"/>','failure')">
                        <s:property value="totalFalNum"/> </a>
                </td>
                <td align="center">
                    <a href="javascript:openHgw('<s:property value="cityId"/>','<s:property value="starttime1"/>',
						   							'<s:property value="endtime1"/>','-1','<s:property value="gwType"/>','total')">
                        <s:property value="totalNum"/> </a>
                </td>
                <td align="center">
                    <s:property value="totalSucRate"/>
                </td>

            </tr>
        </s:iterator>
        </tbody>
        <tfoot>
        <tr>
            <td colspan='17'>
                <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
                     style='cursor: hand'
                     onclick="ToExcel('<s:property value="cityId"/>','<s:property value="starttime1"/>',
                             '<s:property value="endtime1"/>','<s:property value="gwType"/>')">
            </td>
        </tr>
        </tfoot>
    </s:if>
    <s:else>
        <tfoot>
        <tr>
            <td align="left">
                没有相关信息
            </td>
        </tr>
        </tfoot>
    </s:else>
    <tr STYLE="display: none">
        <td colspan="9">
            <iframe id="childFrm" src=""></iframe>
        </td>
    </tr>
</table>


