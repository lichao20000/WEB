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
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<script type="text/javascript" src="<s:url value="/Js/commonUtil.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<%
	String gw_type = request.getParameter("gw_type");
	if(null == gw_type ||  "".equals(gw_type)){
		gw_type="1";
	}
%>

<SCRIPT LANGUAGE="JavaScript">


function do_query(){
	$("div[@id='QueryData']").html("");
	setTimeout("gwShare_queryDevice()", 2000);
}


function gwShare_queryDevice(){
	var	width=800;
	var height=450;
	var gwShare_queryResultType = $("input[@name='gwShare_queryResultType']").val();
    var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
    var gwShare_fileName = $("input[@name='gwShare_fileName']").val();

    if(""==gwShare_fileName){
        alert("�����ϴ��ļ���");
        return;
    }
    var url="<s:url value="/gwms/report/multicastDownReport!getFileSize.action"/>?gwShare_fileName="+gwShare_fileName;
    $.post(url,{
    },function(ajax){
            if (ajax>2000)
            {
                alert("�ļ��嵥�������������2000!");
                return false;
            }
            else
            {
                document.gwShare_selectForm.submit();
            }
    });
}

function toExcel(){
    var gwShare_fileName = $("input[@name='gwShare_fileName']").val();

    if(""==gwShare_fileName){
        alert("�����ϴ��ļ���");
        return;
    }

    var url="<s:url value="/gwms/report/multicastDownReport!getFileSize.action"/>?gwShare_fileName="+gwShare_fileName;
    $.post(url,{
    },function(ajax){
            if (ajax>2000)
            {
                alert("�ļ��嵥�������������2000!");
                return false;
            }
            else
            {
                var page="<s:url value='/gwms/report/multicastDownReport!toExcel.action'/>?gwShare_fileName="+gwShare_fileName;
                document.all("childFrm").src=page;
            }
    });

}

function checkFileSize(){

    var gwShare_fileName = $("input[@name='gwShare_fileName']").val();

    if(""==gwShare_fileName){
        alert("�����ϴ��ļ���");
        return;
    }
 	var url="<s:url value="/gwms/report/multicastDownReport!getFileSize.action"/>?gwShare_fileName="+gwShare_fileName;
    $.post(url,{
    },function(ajax){
            if (ajax>2000)
            {
                alert("�ļ��嵥�������������2000!");
                return false;
            }
            return true;
    });
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
<form name="gwShare_selectForm" action="<s:url value="/gwms/report/multicastDownReport!queryDeviceList.action"/>"
    target="dataForm" >
<input type="hidden" name="gwShare_queryType" value="3" />
<input type="hidden" name="gwShare_import_value" value="none" />

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
	<tr>
        <td>
            <table width="100%" height="30" border="0" cellspacing="0"
                cellpadding="0" class="green_gargtd">
                <tr>
                    <td width="162" align="center" class="title_bigwhite" nowrap>
                        �鲥���Ʊ����ѯ
                    </td>
                </tr>
            </table>
        </td>
    </tr>

	<tr>
		<td bgcolor=#999999>
			<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center">
				<tr id="gwShare_tr31" bgcolor="#FFFFFF" >
					<td align="right" width="15%">�ύ�ļ�</td>
					<td colspan="5" width="85%">
						<div id="importUsername">
							<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="<s:url value="/gwms/share/FileUpload.jsp"/>" height="30" width="100%">
							</iframe>
							<input type="hidden" name=gwShare_fileName value=""/>
						</div>
					</td>
				</tr>
				<tr id="gwShare_tr32">
					<td CLASS="green_foot" align="right">ע������</td>
					<td colspan="5" CLASS="green_foot">
					1����Ҫ������ļ���ʽ����Excel���ı��ļ�����xls��ʽ��txt��ʽ�� <br>
					2���ļ��ĵ�һ��Ϊ�����У������û��˺š���
					 <br>
					3���ļ�ֻ��һ�С�
					 <br>
					4���ļ�������Ҫ����2000������2000������ε��롣
					<br>
                    5�����ϴ��ļ��󣬿ɲ���ѯֱ�ӵ�������ļ���
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td colspan="3" align="right" class="green_foot" width="100%">
						<input type="button" onclick="javascript:do_query()" class=jianbian
						name="gwShare_queryButton" value=" ��ѯ " />
					</td>
					<td colspan="3" align="right" class="green_foot" width="100%">
                        <input type="button" onclick="javascript:toExcel()" class=jianbian
                        name="gwShare_queryButton" value=" ���� " />
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