<%@ page language="java" contentType="text/html; charset=GBK"
pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags" %>

<html xmlns:lk="http://schemas.xmlsoap.org/soap/encoding/">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <title>BSS�쳣������</title>
    <%

    /**
    * ҵ�����û������BSSҵ���ѯ
    */
    %>
    <link rel="stylesheet" href="../../css3/c_table.css" type="text/css">
    <link rel="stylesheet" href="../../css3/global.css" type="text/css">
    <script type="text/javascript" src="../../Js/jquery.js"></script>
    <SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
    <script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>
    <lk:res />
</head>


<body>
<form name="selectForm" id="selectForm" action="<s:url value='/itms/service/bssSheetServ!getBSSInfoByLoid.action'/>"
      target="dataForm">
    <input type="hidden" name="userId" value="0"/>
    <input type="hidden" name="loidNow" value="0"/>
    <table>
        <tr>
            <td HEIGHT=20>
                &nbsp;
            </td>
        </tr>
        <tr>
            <td>
                <table class="green_gargtd">
                    <tr>
                        <td width="162" align="center" class="title_bigwhite">
                            BSS�쳣������
                        </td>
                        <td>
                            <img src="<s:url value="/images/attention_2.gif"/>" width="15"
                            height="12" />
                            ���2020��5��11�����֮ǰ�Ĺ������޸Ŀ���ʱ��
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <table class="querytable">

                    <TR>
                        <th colspan="4">
                            BSSҵ���ѯ
                        </th>
                    </tr>

                    <TR>
                        <TD class=column width="25%" align='right'>LOID</TD>
                        <TD width="45%">
                            <input type="input" name="loid" size="20" maxlength="20"
                                   class=bk/>
                        </TD>
                    </TR>
                    <TR>
                        <td colspan="4" align="right" class=foot>
                            <!--<button onclick="javascript:query()">
                                &nbsp;�� ѯ&nbsp;
                            </button>-->
                            <input type="button"
                                   onclick="javascript:query()" class=jianbian
                                   name="queryButton" value=" �� ѯ "/>
                        </td>
                    </TR>
                </table>
            </td>
        </tr>
        <tr>
            <td height="25" id="resultStr">

            </td>
        </tr>
        <tr>
            <td>
                <iframe id="dataForm" name="dataForm" height="0" frameborder="0"
                        scrolling="no" width="100%" src=""></iframe>
            </td>
        </tr>
    </table>
    <br>
</form>
<div id="editUserOpenDateDiv" style="display: none;width: 95%">
    <table class="querytable">

        <TR>
            <th colspan="4">
                �༭����ʱ��
            </th>
        </tr>

        <TR>
            <TD class=column width="25%" align='right'>�û����Ͽ�ͨʱ��</TD>
            <TD width="45%">
                <lk:date id="openDate"
                         name="openDate" type="all" />
                &nbsp;
                <font color="red"> *</font>
            </TD>
        </TR>
        <TR>
            <td colspan="4" align="right" class=foot>
                <input type="button"
                       onclick="javascript:saveUserOpenDate()" class=jianbian
                       name="queryButton" value=" �� �� "/>
            </td>
        </TR>
    </table>
</div>
</body>
<%@ include file="../../foot.jsp"%>

<script type="text/javascript">

    function query() {
        var loid = Trim($("input[@name='loid']").val());
        if (loid == null || loid == '') {
            alert("����дLOID��");
            return false;
        }

        $("input[@name='loid']").val(loid);
		var form = document.getElementById("selectForm");
		form.submit();
    }

    function saveUserOpenDate() {
        var openDate = Trim($("input[@name='openDate']").val());
        if (openDate == null || openDate == '') {
            alert("����д�û����Ͽ���ʱ�䣡");
            return false;
        }
        var url = "<s:url value='/itms/service/bssSheetServ!changeOPenDateByUserId.action'/>";
        $.post(url, {
            userId: Trim($("input[@name='userId']").val()),
            openDate: openDate
        }, function (ajax) {
            alert(ajax);
            if (ajax.indexOf("�ɹ�") > -1) {
                var form = document.getElementById("selectForm");
                form.action = "<s:url value='/itms/service/bssSheetServ!getBSSInfoByLoid.action'/>?loid=" + $("input[@name='loid']").val();
                form.submit();
            }
            $("table[@id='editUserOpenDateDiv']").hide();
        })
    }


    //** iframe�Զ���Ӧҳ�� **//
    //������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
    //�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
    //����iframe��ID
    var iframeids = ["dataForm"]

    //����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
    var iframehide = "yes"

    function dyniframesize() {
        var dyniframe = new Array()
        for (i = 0; i < iframeids.length; i++) {
            if (document.getElementById) {
                //�Զ�����iframe�߶�
                dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
                if (dyniframe[i] && !window.opera) {
                    dyniframe[i].style.display = "block"
                    //����û����������NetScape
                    if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
                        dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
                    //����û����������IE
                    else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight)
                        dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
                }
            }
            //�����趨�Ĳ���������֧��iframe�����������ʾ����
            if ((document.all || document.getElementById) && iframehide == "no") {
                var tempobj = document.all ? document.all[iframeids[i]] : document.getElementById(iframeids[i])
                tempobj.style.display = "block"
            }
        }
    }

    $(function () {
        //setValue();
        dyniframesize();
    });

    $(window).resize(function () {
        dyniframesize();
    });
</script>

</html>