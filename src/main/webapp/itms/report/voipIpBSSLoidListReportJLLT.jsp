<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
      type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
      type="text/css">

<table class="listtable">
    <caption>统计结果</caption>
    <s:if test="loidBSSReportMap.size()>0">
        <thead>

        <tr>
            <th>地市编码</th>
            <th>城市名称</th>
            <th>LOID</th>
            <th>语音IP</th>
            <th>是否有此loid工单</th>
            <th>是否下发成功</th>
            <th>是否在线</th>
            <th>是否无绑定</th>
        </tr>
        </thead>

        <tbody>

            <tr>
                <td align="center">
                    <s:property value="loidBSSReportMap.cityId" />
                </td>
                <td align="center">
                    <s:property value="loidBSSReportMap.cityName" />
                </a>
                </td>
                <td align="center">
                    <s:property value="loidBSSReportMap.loid" />
                </a>
                </td>
                <td align="center">
                    <s:property value="loidBSSReportMap.ip" />
                </a>
                </td>
                <td align="center">
                    <s:property value="loidBSSReportMap.isBSS" />
                </a>
                </td>
                <td align="center">
                    <s:property value="loidBSSReportMap.isDownSuccess" />
                </td>
                <td align="center">
                    <s:property value="loidBSSReportMap.isOnline" />
                </td>
                <td align="center">
                    <s:property value="loidBSSReportMap.isUnbind" />
                </td>
            </tr>

        </tbody>
        <tfoot>
        <tr>
            <td colspan='8'></td>
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
        <td colspan="8"><iframe id="childFrm" src=""></iframe></td>
    </tr>
</table>


