<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="com.linkage.litms.system.*"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�豸��ѯ</title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<s:url value="/Js/commonUtil.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<%
	String gw_type = request.getParameter("gw_type");
	if(null == gw_type ||  "".equals(gw_type)){
		gw_type="1";
	}
%>

<SCRIPT LANGUAGE="JavaScript">


function query(){
	$("div[@id='QueryData']").html("");
	var cityId = $.trim($("select[@name='cityId']").val());
	var startOpenDate=$("input[@name='startOpenDate']").val();
    var endOpenDate=$("input[@name='endOpenDate']").val();
    document.gwShare_selectForm.submit();

}







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




</SCRIPT>
<style>
span
{
	position:static;
	border:0;
}
</style>
</head>
<body>
<form name="gwShare_selectForm" action="<s:url value="/gwms/report/lanGatherInfo!query.action"/>"
    target="dataForm" >

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
	<tr>
        <td>
            <table width="100%" height="30" border="0" cellspacing="0"
                cellpadding="0" class="green_gargtd">
                <tr>
                    <td width="162" align="center" class="title_bigwhite" nowrap>
                        lan�ɼ�ͳ��
                    </td>
                </tr>
            </table>
        </td>
    </tr>

	<tr>
		<td bgcolor=#999999>
			<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center">
                <tr bgcolor="#FFFFFF">
                    <TD class=column width="15%" align='right'>����</TD>
                    <TD width="35%">
                        <s:select list="cityList" name="cityId" headerKey="-1" headerValue="��ѡ������"
                            listKey="city_id" listValue="city_name" cssClass="bk">
                        </s:select>
                    </TD>
                    <TD class=column width="15%" align='right'></TD>
                    <TD width="35%">
                    </TD>
                </tr>
                <TR bgcolor="#FFFFFF">
                    <TD class=column width="15%" align='right'>��ʼʱ��</TD>
                    <TD width="35%">
                        <input type="text" name="startOpenDate" readonly class=bk
                            value="<s:property value="startOpenDate" />">
                            <img name="shortDateimg"
                                onClick="WdatePicker({el:document.gwShare_selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
                                src="../../images/dateButton.png" width="15" height="12"
                                border="0" alt="ѡ��">
                        &nbsp;
                        <font color="red"> *</font>
                    </TD>
                    <TD class=column width="15%" align='right'>����ʱ��</TD>
                    <TD width="35%">
                        <input type="text" name="endOpenDate" readonly class=bk
                            value="<s:property value="endOpenDate" />">
                            <img name="shortDateimg"
                                onClick="WdatePicker({el:document.gwShare_selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
                                src="../../images/dateButton.png" width="15" height="12"
                                border="0" alt="ѡ��">
                        &nbsp;
                        <font color="red"> *</font>
                    </TD>
                </TR>



				<tr bgcolor="#FFFFFF">
					<td colspan="4" align="right" class="green_foot" width="100%">
						<input type="button" onclick="javascript:query()" class=jianbian
						name="gwShare_queryButton" value=" ��ѯ " />
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
        <td>
            <iframe id="dataForm" name="dataForm" height="500" frameborder="0"
                scrolling="auto" width="100%" src=""></iframe>
        </td>

    </tr>
    <tr STYLE="display: none">
        <td>
            <iframe id="childFrm" src=""></iframe>
        </td>
    </tr>
</TABLE>
</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>