<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%-- 
	/**
 	 * �߼���ѯ:��ȡԱ��web���topN����ҳ��
 	 * 
 	 * @author suixz(5253) 2008-5-6
 	 * @version 1.0
 	 * @category securitygw
 	 */
--%>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<title>webTopN�߼���ѯ</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/Calendar.js"/>"></script>
<link href="<s:url value="/model_vip/css/liulu.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/tablecss.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/css_blue.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript">
$(function(){
	//��ʼ����ѯʱ��
	var d = new Date();
	$("#startDate").val(d.getYear()+"-"+(d.getMonth()+1)+"-"+d.getDate());
	$("#endDate").val(d.getYear()+"-"+(d.getMonth()+1)+"-"+d.getDate());
});
//��ѯ
function queryData(){
//ʱ���ж�
if($("#startDate").val()=="")
			{
				alert("��ʼʱ�䲻��Ϊ��");
				return false;
			}else{
				var t=$("#startDate").val().replace("-","/");
				var d = new Date(t);
				var ltime=d.getTime();
				var now = new Date();
				var time = now.getTime();
				if(ltime>time){
					alert("��ʼʱ�䲻�ܳ�����ǰʱ��");
					return false;
				}
			}
			if($("#endDate").val()=="")
			{
				alert("����ʱ�䲻��Ϊ��");
				return false;
			}else{
				var t=$("#endDate").val().replace("-","/");
				var d = new Date(t);
				var ltime=d.getTime();
				var now = new Date();
				var time = now.getTime();
				if(ltime>time){
					alert("����ʱ�䲻�ܴ��ڵ�ǰʱ��");
					return false;
				}
			}
			if($("#startDate").val()>$("#endDate").val()){
				alert("��ʼʱ�䲻�ܴ��ڽ���ʱ��");
				return false;
			}
	$("#load").show();
	//��ȡtopNͼ��
	var url = "<s:url value="/securitygw/webTopNHighQuery!getWebTopNChartData.action"><s:param name="deviceId" value="deviceId"/></s:url>";
	$("#chartData").attr("src",url+"&startTime="+$("#startDate").val()+"&endTime="+$("#endDate").val());
	//��̬��ȡ������Ϣ
	url = "<s:url value="/securitygw/webTopNHighQuery!getWebTopNTabData.action"><s:param name="deviceId" value="deviceId"/></s:url>";
	$.post(
		url,
		{
			startTime:$("#startDate").val(),
			endTime:$("#endDate").val()
		},
		function(data){
			$("#tableData").show();
			$("#tableData").html(data);
	});
}
</script>
</head>
<body>
<form>
	<table width="99%"  border="0" cellpadding="0" cellspacing="0" class="table">
		<tr class="tab_title">
			<td class="title_white"><span>Ա��WEB���Top<s:property value="topN"/></span>&nbsp;&nbsp;
                    <input id="startDate" name="textfield" readonly="readonly" type="text" class="bk" value="<s:property value="start"/>" size="12">&nbsp;
                    <input name="stime" type="button" class="jianbian" onClick="showCalendar('day',event)" value="��">
                       ��
                    <input id="endDate" name="textfield" readonly="readonly" type="text" class="bk" value="<s:property value="start"/>" size="12">&nbsp;
                    <input name="etime" type="button" class="jianbian" onClick="showCalendar('day',event)" value="��">
                  	<input name="query" type="button" onclick="queryData()" class="jianbian" value="�� ѯ">
			</td>
		</tr>
		<tr>
        	<td class="trOver_blue">
                <div align="center"><span class="style2"></span>
                	<table width="100%"  border="0" cellspacing="0" cellpadding="0">
                    	<tr>
                      		<td><div align="center">�߼���ѯ����</div></td>
                    	</tr>
                  	</table>
                </div>
           	</td>
        </tr>
        <tr>
			<td class="tr_white" colspan="2">
			<div align="center" id="load" style="display:none"><span class="style2"> 
			<img border="0" id="chartData" src="../images/loading.gif" /></span></div>
			<div align="center" id="tableData" style="display:none">
				
			</div>
		</td>
	</tr>
	</table>
</form>
</body>
</html>