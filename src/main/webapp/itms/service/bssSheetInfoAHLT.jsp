<%@ page language="java" contentType="text/html; charset=GBK"
pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags" %>

<html xmlns:lk="http://schemas.xmlsoap.org/soap/encoding/">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <title>BSS异常单处理</title>
    <%

    /**
    * 业务与用户分离的BSS业务查询
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
                            BSS异常单处理
                        </td>
                        <td>
                            <img src="<s:url value="/images/attention_2.gif"/>" width="15"
                            height="12" />
                            针对2020年5月11日零点之前的工单可修改开户时间
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
                            BSS业务查询
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
                                &nbsp;查 询&nbsp;
                            </button>-->
                            <input type="button"
                                   onclick="javascript:query()" class=jianbian
                                   name="queryButton" value=" 查 询 "/>
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
                编辑开户时间
            </th>
        </tr>

        <TR>
            <TD class=column width="25%" align='right'>用户资料开通时间</TD>
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
                       name="queryButton" value=" 保 存 "/>
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
            alert("请填写LOID！");
            return false;
        }

        $("input[@name='loid']").val(loid);
		var form = document.getElementById("selectForm");
		form.submit();
    }

    function saveUserOpenDate() {
        var openDate = Trim($("input[@name='openDate']").val());
        if (openDate == null || openDate == '') {
            alert("请填写用户资料开户时间！");
            return false;
        }
        var url = "<s:url value='/itms/service/bssSheetServ!changeOPenDateByUserId.action'/>";
        $.post(url, {
            userId: Trim($("input[@name='userId']").val()),
            openDate: openDate
        }, function (ajax) {
            alert(ajax);
            if (ajax.indexOf("成功") > -1) {
                var form = document.getElementById("selectForm");
                form.action = "<s:url value='/itms/service/bssSheetServ!getBSSInfoByLoid.action'/>?loid=" + $("input[@name='loid']").val();
                form.submit();
            }
            $("table[@id='editUserOpenDateDiv']").hide();
        })
    }


    //** iframe自动适应页面 **//
    //输入你希望根据页面高度自动调整高度的iframe的名称的列表
    //用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
    //定义iframe的ID
    var iframeids = ["dataForm"]

    //如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
    var iframehide = "yes"

    function dyniframesize() {
        var dyniframe = new Array()
        for (i = 0; i < iframeids.length; i++) {
            if (document.getElementById) {
                //自动调整iframe高度
                dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
                if (dyniframe[i] && !window.opera) {
                    dyniframe[i].style.display = "block"
                    //如果用户的浏览器是NetScape
                    if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
                        dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
                    //如果用户的浏览器是IE
                    else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight)
                        dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
                }
            }
            //根据设定的参数来处理不支持iframe的浏览器的显示问题
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