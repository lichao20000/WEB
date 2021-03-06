<%@ include file="/timelater.jsp" %>
<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="lk" uri="/linkage" %>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
      type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
      type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
        src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<script type="text/javascript">

    function doQuery() {
        document.getElementById("loadingTxt").style.display="block";
        document.getElementById("info").style.display="none";
        var url = "<s:url value='/itms/report/deviceCountReport!getDualStack.action'/>";
        $.post(url,function(ajax){
            var json = eval ("(" + ajax + ")");

            $("td[@id='manualSucc']").html(json.manualsucc.replace(".00000",""));
            $("td[@id='manualUndo']").html(json.manualundo.replace(".00000",""));
            $("td[@id='manualFail']").html(json.manualfail.replace(".00000",""));
            $("td[@id='automaticSucc']").html(json.automaticsucc.replace(".00000",""));
            $("td[@id='automaticUndo']").html(json.automaticundo.replace(".00000",""));
            $("td[@id='automaticFail']").html(json.automaticfail.replace(".00000",""));
            $("td[@id='notTianyi']").html(json.nottianyi.replace(".00000",""));
            $("td[@id='tianyi']").html(json.tianyi.replace(".00000",""));
            $("td[@id='dualStackSucc']").html(json.dualstacksucc.replace(".00000",""));
            $("td[@id='dualStackUndo']").html(json.dualstackundo.replace(".00000",""));
            $("td[@id='dualStackFail']").html(json.dualstackfail.replace(".00000",""));
            $("td[@id='total']").html(json.total.replace(".00000",""));
            document.getElementById("loadingTxt").style.display="none";
            document.getElementById("info").style.display="block";
        },"json");
    }

</script>

<table class="listtable">
    <caption>
        ????????????????
    </caption>
    <tbody id="info">
    <tr>
        <td rowspan="6">??????????????????</td>
        <td>??????????????????</td>
        <td id="manualSucc">0</td>
    </tr>
    <tr>
        <td>??????????????????</td>
        <td id="manualUndo" >0</td>
    </tr>
    <tr>
        <td>??????????????????</td>
        <td id="manualFail" >0</td>
    </tr>
    <tr>
        <td>??????????????????</td>
        <td id="automaticSucc" >0</td>
    </tr>
    <tr>
        <td>??????????????????</td>
        <td id="automaticUndo" >0</td>
    </tr>
    <tr>
        <td>??????????????????</td>
        <td id="automaticFail" >0</td>
    </tr>
    <tr>
        <td colspan="2">????????????????????????</td>
        <td id="notTianyi" >0</td>
    </tr>
    <tr>
        <td colspan="2">????1.0??????????</td>
        <td id="tianyi" >0</td>
    </tr>
    <tr>
        <td colspan="3" align="center">????</td>
    </tr>
    <tr>
        <td colspan="2">????????????????</td>
        <td id="dualStackSucc">0</td>
    </tr>
    <tr>
        <td colspan="2">??????????????????????????</td>
        <td id="dualStackUndo">0</td>
    </tr>
    <tr>
        <td colspan="2">????????????????????????????</td>
        <td id="dualStackFail">0</td>
    </tr>
    <tr>
        <td colspan="2">??????</td>
        <td id="total">0</td>
    </tr>
    <tr>
        <td colspan="3"><button onclick="doQuery()" name="button" id="button">&nbsp;????&nbsp;</button></td>
    </tr>
    </tbody>
    <tr id="loadingTxt" STYLE="display: none">
        <td colspan="3">
            <font style='display:block; text-align:center;font-size:20px' >????????????????....</font>
        </td>
    </tr>
</table>

<%@ include file="/foot.jsp" %>
