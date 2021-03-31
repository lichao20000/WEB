<%@ page language="java" contentType="text/html; charset=GBK"
         pageEncoding="GBK" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="lk" uri="/linkage" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <title></title>
    <link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
          type="text/css">
    <link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
          type="text/css">
    <script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
    <script type="text/javascript"
            src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

    <script type="text/javascript">
        $(function () {
            $("#trData", parent.document).hide();
            $("#btn", parent.document).attr('disabled', false);
            parent.dyniframesize();
        });

        function ListToExcel(taskid,taskname, starttime1, endtime1, httpType) {
            var page = "<s:url value='/ids/httpDownload!querySingleHttpExcel.action'/>?"
                + "taskid=" + taskid
                + "&taskname=" + taskname
                + "&starttime2=" + starttime1
                + "&endtime2=" + endtime1
                + "&httpType=" + httpType;
            document.all("childFrm").src = page;
        }

    </script>
</head>
<body>
    <table class="listtable">
        <caption>
            ���ٽ��
        </caption>
        <thead>
        <tr>
            <th>
                �� ��
            </th>
            <th>
                LOID
            </th>
            <th>
                ����
            </th>
            <th>
                ����
            </th>
            <th>
                �ͺ�
            </th>
            <th>
                Ӳ���汾
            </th>
            <th>
                ����汾
            </th>
            <th>
                ��������
            </th>
            <th>
                �Ƿ���
            </th>
            <th>
                ����ʱ��
            </th>
            <th>
                ǩԼ����
            </th>
            <th>
                ������Դ
            </th>
        </tr>
        </thead>
        <tbody>
        <s:if test="list.size()>0">
            <s:iterator value="list">
                <tr>
                    <td>
                        <s:property value="city_name"/>
                    </td>
                    <td>
                        <s:property value="loid"/>
                    </td>
                    <td>
                        <s:property value="desn"/>
                    </td>
                    <td>
                        <s:property value="vendor_name"/>
                    </td>
                    <td>
                        <s:property value="device_model"/>
                    </td>
					<td>
						<s:property value="hardwareversion"/>
					</td>
					<td>
						<s:property value="softwareversion"/>
					</td>
					<td>
						<s:property value="maxsampledtotalvalues"/>
					</td>
					<td>
						<s:property value="isreach"/>
					</td>
					<td>
						<s:property value="test_time"/>
					</td>
					<td>
						<s:property value="downlink"/>
					</td>
					<td>
						<s:property value="httptype"/>

					</td>
                </tr>
            </s:iterator>
        </s:if>
        <s:else>
            <tr>
                <td colspan=12>
                    ϵͳû����ص��û���Ϣ!
                </td>
            </tr>
        </s:else>
        </tbody>
        <tfoot>
        <tr>
            <td colspan="12">
				<span style="float: right;"> <lk:pages
                        url="/ids/httpDownload!querySingleHttpList.action" styleClass="" showType=""
                        isGoTo="true" changeNum="true"/> </span>
                    <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
                         style='cursor: hand'
                         onclick="ListToExcel('<s:property value="taskid"/>','<s:property value="taskname"/>','<s:property value="starttime2"/>',
                                 '<s:property value="endtime2"/>','<s:property value="httpType"/>')">
            </td>
        </tr>

		<tr STYLE="display: none">
			<td colspan="8"><iframe id="childFrm" src=""></iframe></td>
		</tr>
		</tfoot>
	</table>
	</body>
</html>
