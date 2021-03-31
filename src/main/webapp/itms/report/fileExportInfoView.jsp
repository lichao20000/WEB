<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<link rel="stylesheet" href="<s:url value='../../css/inmp/css/css_green.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css/inmp/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/inmp/jquery.js"/>"></script>
<SCRIPT type="text/javascript" SRC="../../Js/inmp/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/inmp/My97DatePicker/WdatePicker.js"/>"></script>

<script language="JavaScript">
    //** iframe�Զ���Ӧҳ�� **//
    //������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
    //�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
    //����iframe��ID
    var iframeids=["dataForm"];

    //����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
    var iframehide="yes";

    function dyniframesize()
    {
        var dyniframe=new Array();
        for (var i=0; i<iframeids.length; i++)
        {
            if (document.getElementById)
            {
                //�Զ�����iframe�߶�
                dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
                if (dyniframe[i] && !window.opera)
                {
                    dyniframe[i].style.display="block";
                    //����û����������NetScape
                    if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
                        dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
                    //����û����������IE
                    else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight)
                        dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
                }
            }
            //�����趨�Ĳ���������֧��iframe�����������ʾ����
            if ((document.all || document.getElementById) && iframehide=="no")
            {
                var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i]);
                tempobj.style.display="block";
            }
        }
    }

    $(function(){
        //setValue();
        dyniframesize();
    });

    $(window).resize(function(){
        dyniframesize();
    });

    function doQuery() {
       /* var isNew = '1';
        var cityId = $.trim($("select[@name='cityId']").val());
        var startTime = $.trim($("input[@name='startTime']").val());
        var endTime = $.trim($("input[@name='endTime']").val());
        var fileDesc = $.trim($("input[@name='fileDesc']").val());

        $("tr[@id='trData']").show();
        $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
        $("button[@name='button']").attr("disabled", true);
        var url = "<s:url value='/itms/report/fileExportReport!getFileExportInfo.action'/>";
        $.post(url, {
            cityId: cityId,
            startTime: startTime,
            endTime: endTime,
            fileExportDesc:fileDesc
        }, function (ajax) {
            $("div[@id='QueryData']").html("");
            $("div[@id='QueryData']").append(ajax);
            $("button[@name='button']").attr("disabled", false);
        });*/
        document.selectForm.submit();

    }


</script>

<br>
<TABLE>
    <tr>
        <td>
            <table class="green_gargtd">
                <tr>
                    <th>
                        ������������ͳ��
                    </th>
                    <td>
                        <img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
                        ������������ͳ��
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <form id="form" name="selectForm" action="<s:url value='/itms/report/fileExportReport!getFileExportInfo.action'/>"
                  target="dataForm" >
                <table class="querytable">
                    <tr bgcolor=#ffffff>
                        <td class=column align=center width="15%">
                            ��ʼʱ��
                        </td>
                        <td>
                            <input type="text" name="startTime" class='bk' readonly
                                   value="<s:property value='startTime'/>">
                            <img name="shortDateimg"
                                 onClick="WdatePicker({el:document.selectForm.startTime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
                                 src="<s:url value='/images/dateButton.png'/>" width="15" height="12"
                                 border="0" alt="ѡ��"/>

                        </td>
                        <td class=column align=center width="15%">
                            ����ʱ��
                        </td>
                        <td>
                            <input type="text" name="endTime" class='bk' readonly
                                   value="<s:property value='endTime'/>">
                            <img name="shortDateimg"
                                 onClick="WdatePicker({el:document.selectForm.endTime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
                                 src="<s:url value='/images/dateButton.png'/>" width="15" height="12"
                                 border="0" alt="ѡ��"/>
                        </td>
                    </tr>

                    <tr bgcolor=#ffffff>
                        <td class=column align=center width="15%">
                            ˵ ��
                        </td>
                        <td>
                           <input name="fileExportDesc" value="">
                        </td>

                        <!--��Ϊadmin.com �иò�ѯ����-->
                        <s:if test="#session.curUser.areaId==1">
                            <td class=column align=center width="15%">
                                �� ��
                            </td>
                            <td>
                                <s:select list="cityList" name="cityId" headerKey="00"
                                          headerValue="��ѡ������" listKey="city_id" listValue="city_name"
                                          value="cityId" cssClass="bk"></s:select>
                            </td>
                        </s:if>
                    </tr>

                    <tr bgcolor=#ffffff>
                        <td class=foot colspan=4 align=right>
                            <button onclick="doQuery()" name="button">&nbsp;ͳ ��&nbsp;</button>
                            <s:property value="areaId"/>
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>

    <tr id="trData">
        <td class="colum">
            <iframe id="dataForm" name="dataForm" height="0" frameborder="0"
                    scrolling="no" width="100%" src=""></iframe>
        </td>
    </tr>
    <tr>
        <td height="20">
        </td>
    </tr>
</TABLE>

<%@ include file="/foot.jsp" %>
