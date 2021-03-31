<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script language="JavaScript">
var gw_type = '<s:property value="gw_type"/>';
function doQuery(){
	var custSQL = $.trim($("textarea[@name='custSQL']").val());            // ��ѯSQL
	custSQL = custSQL.replace("%", "%25").replace(new RegExp("'", 'g'), "[");
	custSQL = custSQL.replace(/\+/g, "]");
	$("textarea[@name='custSQL']").val(custSQL);
    if(custSQL == ""){
         alert("�������ѯSQL");
         return false;
    }
	var tmplSQL = custSQL.toLowerCase();
    if(tmplSQL.indexOf("select") == -1 || tmplSQL.indexOf("from") == -1 ||tmplSQL.indexOf("where") == -1){
        alert("���Ƶ�SQL�������'select'��'from'��'where'�ȹؼ��֣����Ż�");
        return false;
    }
    if(tmplSQL.indexOf("*") > 0){
        alert("���Ƶ�SQL�а�����'*'����ָ����ȷ�Ĳ�ѯ�ֶ�");
        return false;
    }
    if(tmplSQL.split("select").length-1 > 1){
        alert("���Ƶ�SQL�г�������Ƕ�ײ�ѯ�����Ż�");
        return false;
    }
    /**$("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
    $("button[@name='button']").attr("disabled", true);
    var url = '<s:url value='/itms/report/customerSQLReport!queryList.action'/>'; 
	$.post(url,{
		custSQL:custSQL
	},function(ajax){
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
		 $("button[@name='button']").attr("disabled", false);
	});**/
    document.selectForm.submit();
	
}
function ToExcel(){
	var custSQL = $.trim($("textarea[@name='custSQL']").val());            // ��ѯSQL
	custSQL = custSQL.replace("%", "%25").replace(new RegExp("'", 'g'), "[");
	custSQL = custSQL.replace(/\+/g, "]");
	var url = "<s:url value='/itms/report/customerSQLReport!queryListCount.action'/>";
	$.post(url, {
		custSQL:custSQL
	}, function(ajax) {
		var total=parseInt(ajax);
		if(ajax>100000){
	   alert("������̫��֧�ֵ��� ");
	   return;
		}
		else{

			var mainForm = document.getElementById("form");
			mainForm.action="<s:url value='/itms/report/customerSQLReport!getAllResultExcel.action'/>";
			mainForm.submit();
		    mainForm.action="<s:url value='/itms/report/customerSQLReport!queryList.action'/>";
		}
	});
}

//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=["dataForm"]

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
   			dyniframe[i].style.display="block"
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
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
  		tempobj.style.display="block"
		}
	}
}

$(function(){
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

</script>

<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>
						�Զ����ѯ����
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
						���Ʋ�ѯSQL
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form id="form" name="selectForm" action="<s:url value='/itms/report/customerSQLReport!queryList.action'/>"
			target="dataForm">
				<table class="querytable">
					<tr>
						<th colspan=4>
							�Զ����ѯ����
						</th>
						
					</tr>
					<TR bgcolor="#FFFFFF" id="" STYLE="">
						<TD align="right" class=column width="10%">
							SQL��ѯ���
						</TD>
						<TD align="left" width="60%" >
                               <textarea name="custSQL" cols="100" rows="4" class=bk></textarea>
						</TD>
						<TD align="left" width="30%" colspan=2>
                               <font color="red">1�����Ƶ�SQL�������'select'��'from'��'where'�ȹؼ���<br>2�����Ƶ�SQL�в�����'*'����ָ����ȷ�Ĳ�ѯ�ֶ�<br>3�����Ƶ�SQL�в�֧��Ƕ�ײ�ѯ<br>4����֧�ֵ�������10W��������</font>
						</TD>
					</TR>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()" name="button">
								&nbsp;ͳ ��&nbsp;
							</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>

	<!-- tr id="trData" style="display: none">
		<td class="colum">
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				����ͳ�ƣ����Ե�....
			</div>
		</td>
	</tr-->
	<tr>
		<td>
			<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
				scrolling="no" width="100%" src="">
				
				</iframe>
		</td>
	</tr>
	<tr>
		<td height="20">
		</td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>

