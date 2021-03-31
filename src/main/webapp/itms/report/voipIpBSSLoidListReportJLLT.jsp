<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
      type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
      type="text/css">

<table class="listtable">
    <caption>ͳ�ƽ��</caption>
    <s:if test="loidBSSReportMap.size()>0">
        <thead>

        <tr>
            <th>���б���</th>
            <th>��������</th>
            <th>LOID</th>
            <th>����IP</th>
            <th>�Ƿ��д�loid����</th>
            <th>�Ƿ��·��ɹ�</th>
            <th>�Ƿ�����</th>
            <th>�Ƿ��ް�</th>
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
            <td align="left">û�������Ϣ</td>
        </tr>
        </tfoot>
    </s:else>
    <tr STYLE="display: none">
        <td colspan="8"><iframe id="childFrm" src=""></iframe></td>
    </tr>
</table>


