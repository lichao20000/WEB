<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>

<TABLE>
    <tr>
        <td>
            <table class="green_gargtd">
                <tr>
                    <th>
                        语音修改IP工单LOID统计
                    </th>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <form name=frm>
                <table class="querytable">
                    <tr>
                        <th colspan=4>
                            VOIP修改IP工单LOID统计
                        </th>

                    </tr>
                    <tr bgcolor=#ffffff>
                        <td class=column align="center" width="15%">
                            LOID
                        </td>
                        <td width="85%">
                            <input type="text" name="loid" size="20"
                                   maxlength="30" class=bk  style="width: 200px"/>&nbsp; <font color="red">*</font>
                        </td>
                    </tr>

                    <tr bgcolor=#ffffff>
                        <td class=foot colspan=4 align="right" padding="4px">
                            <input type="button" onclick="doQuery()" name="button" value="查 询"/>
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>

    <tr id="trData" style="display: none">
        <td class="colum">
            <div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
                正在统计，请稍等....
            </div>
        </td>
    </tr>
    <tr>
        <td height="20">
        </td>
    </tr>
</TABLE>

<%@ include file="/foot.jsp"%>
<script language="JavaScript">

    function doQuery(){
        var loid = $.trim($("input[@name='loid']").val());
        if(loid === ""){
            alert("请填写LOID！");
            return false;
        }

        $("tr[@id='trData']").show();
        $("button[@name='button']").attr("disabled", true);
        $("div[@id='QueryData']").html("正在统计，请稍等....");
        var url = '<s:url value='/itms/report/VoipXIPBSSReportJL!getVoipBSSByLoid.action'/>';
        $.post(url,{
            loid : loid
        },function(ajax){
            $("div[@id='QueryData']").html("");
            $("div[@id='QueryData']").append(ajax);
            $("button[@name='button']").attr("disabled", false);
        });
    }

</script>

