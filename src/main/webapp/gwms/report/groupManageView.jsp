<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT type="text/javascript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>

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
                        ������������Ϣ����
                    </th>
                    <td>
                        <img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
                        ������������Ϣ����
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <form id="form" name="selectForm" action="<s:url value='/gwms/report/groupManage!getGroupManageList.action'/>"
                  target="dataForm" >
                <table class="querytable">
                    <tr bgcolor=#ffffff>
                        <td class=column align=center width="15%">
                            �ն�ע��״̬
                        </td>
                        <td>
                            <select name="status" class="bk" style="width: 225px">
                                <option value="">==��ѡ��==</option>
                                <option value="1">ע��</option>
                                <option value="-1">��</option>
                            </select>
                        </td>
                         <td class=column align=center width="15%">
                                �ն˹�����·��
                            </td>
                            <td>
                                <s:select list="cityList" name="cityId" headerKey="00"
                                          headerValue="��ѡ������" listKey="city_id" listValue="city_name"
                                          value="cityId" cssClass="bk"></s:select>
                            </td>
                    </tr>

                    <tr bgcolor=#ffffff>
                        <td class=foot colspan=4 align=right>
                            <input type="button" value=" ͳ �� " onclick="doQuery()">
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>

    <tr id="trData">
        <td class="colum">
            <iframe id="dataForm" name="dataForm" height="0" frameborder="0"
                    scrolling="no" width="100%"></iframe>
        </td>
    </tr>
    <tr>
        <td height="20">
        </td>
    </tr>
</TABLE>

<%@ include file="/foot.jsp" %>
