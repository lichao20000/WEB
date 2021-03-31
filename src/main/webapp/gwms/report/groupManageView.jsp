<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT type="text/javascript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>

<script language="JavaScript">
    //** iframe自动适应页面 **//
    //输入你希望根据页面高度自动调整高度的iframe的名称的列表
    //用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
    //定义iframe的ID
    var iframeids=["dataForm"];

    //如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
    var iframehide="yes";

    function dyniframesize()
    {
        var dyniframe=new Array();
        for (var i=0; i<iframeids.length; i++)
        {
            if (document.getElementById)
            {
                //自动调整iframe高度
                dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
                if (dyniframe[i] && !window.opera)
                {
                    dyniframe[i].style.display="block";
                    //如果用户的浏览器是NetScape
                    if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
                        dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
                    //如果用户的浏览器是IE
                    else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight)
                        dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
                }
            }
            //根据设定的参数来处理不支持iframe的浏览器的显示问题
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
                        分组管理基本信息报表
                    </th>
                    <td>
                        <img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
                        分组管理基本信息报表
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
                            终端注册状态
                        </td>
                        <td>
                            <select name="status" class="bk" style="width: 225px">
                                <option value="">==请选择==</option>
                                <option value="1">注册</option>
                                <option value="-1">绑定</option>
                            </select>
                        </td>
                         <td class=column align=center width="15%">
                                终端归属域路径
                            </td>
                            <td>
                                <s:select list="cityList" name="cityId" headerKey="00"
                                          headerValue="请选择属地" listKey="city_id" listValue="city_name"
                                          value="cityId" cssClass="bk"></s:select>
                            </td>
                    </tr>

                    <tr bgcolor=#ffffff>
                        <td class=foot colspan=4 align=right>
                            <input type="button" value=" 统 计 " onclick="doQuery()">
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
