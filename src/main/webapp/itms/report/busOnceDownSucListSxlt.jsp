<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
      type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
      type="text/css">

<table class="listtable">
    <caption>
        ��ѯ���
    </caption>
    <s:if test="data.size()>0">
        <thead>
        <tr>
            <th class="title_1" rowspan="2">
                ������
            </th>
            <th colspan="4">
                ���
            </th>
            <th colspan="4">
                IPTV
            </th>
            <th colspan="4">
                VoIP
            </th>
            <th colspan="4">
                ����
            </th>
        </tr>
        <tr>

            <th>
                �·��ɹ���
            </th>
            <th>
                �·�ʧ����
            </th>
            <th>
                �·�����
            </th>
            <th>
                �ɹ���
            </th>

            <th>
                �·��ɹ���
            </th>
            <th>
                �·�ʧ����
            </th>
            <th>
                �·�����
            </th>
            <th>
                �ɹ���
            </th>

            <th>
                �·��ɹ���
            </th>
            <th>
                �·�ʧ����
            </th>
            <th>
                �·�����
            </th>
            <th>
                �ɹ���
            </th>

            <th>
                �·��ɹ���
            </th>
            <th>
                �·�ʧ����
            </th>
            <th>
                �·�����
            </th>
            <th>
                �ɹ���
            </th>
        </tr>
        </thead>

        <tbody>
        <s:iterator value="data" var="map1">
            <tr>
                    <%-- �������� --%>
                <td align="center">
                    <s:property value="cityName"/>
                </td>
                    <%-- ��� --%>

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
                    <%-- ������ --%>

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
                <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
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
                û�������Ϣ
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


