<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>BBMSά��Ԥ��ҳ��</title>
<%
	 /**
	 * BBMSά��Ԥ��ҳ��
	 * 
	 * @author chenjie
	 * @since 2012-12-06
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">

// �����ѯ
function query()
{
	// ȫ��trim
	trimAll();
	
	// �������
	var cityId = $.trim($("select[@name='cityId']").val());
    if(cityId == "-1"){
         alert("��ѡ������!");
         return false;
    }
    
    var vendorId = $.trim($("select[@name='vendorId']").val());
    if(vendorId == "-1"){
    	 alert("��ѡ����!");
         return false;
    }
    
    // �ύ��ѯ
	document.selectForm.submit();
}

function trimAll()
{
	var inputs = document.getElementsByTagName("input");
	for(var i=0;i<inputs.length;i++)
	{
		var input = $(inputs[i]);
		// �ı�input
		if(/text/gi.test(input.attr("type")))
		{
			input.val($.trim(input.val()));
		}
	}
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
	//setValue();
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

</script>
</head>

	<body>
		<form id="form" name="selectForm" action="<s:url value='/bbms/report/warnReportACT!queryWarnReport.action'/>"
			target="dataForm">
			<input type="hidden" name="gw_type" value='<s:property value="gw_type" />' />
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
									��ҵ����ά��Ԥ��
								</td>
								<td>
									<img src="<s:url value="/images/attention_2.gif"/>" width="15"
										height="12" />
									��ѯ��ҵ����ά��Ԥ�����
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
									��ҵ����ά��Ԥ����ѯ
								</th>
							</tr>

							<TR>
								<TD class=column width="15%" align='right'>
									������
								</TD>
								<TD width="35%">
									<s:select list="cityList" name="cityId" headerKey="-1"
										headerValue="��ѡ������" listKey="city_id" listValue="city_name"
										 cssClass="bk"></s:select>
								</TD>
								<TD class=column width="15%" align='right'>
									�ͻ���ϵ�绰
								</TD>
								<TD width="35%">
									<input type='text' name="linkphone" />
								</TD>
							</TR>
							<TR>
								<TD class=column width="15%" align='right'>
									װ����ַ
								</TD>
								<TD width="35%">
									<input type='text' name="customerAddress" />
								</TD>
								<TD class=column width="15%" align='right'>
									������Ʒ����
								</TD>
								<TD width="35%">
									<input type='text' name="productType" />
								</TD>
							</TR>
							<TR>
								<TD class=column width="15%" align='right'>
									װ����ʼʱ��
								</TD>
								<TD width="35%">
									<input type="text" name="startOpenDate" readonly class=bk
										value="<s:property value="startOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
									&nbsp;
									<font color="red"> *</font>
								</TD>
								<TD class=column width="15%" align='right'>
									װ������ʱ��
								</TD>
								<TD width="35%">
									<input type="text" name="endOpenDate" readonly class=bk
										value="<s:property value="endOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
									&nbsp;
									<font color="red"> *</font>
								</TD>
							</TR>
							<TR>
								<TD class=column width="15%" align='right'>
									�豸����
								</TD>
								<TD width="35%">
									<s:select list="vendorList" name="vendorId" headerKey="-1"
										headerValue="��ѡ����" listKey="vendor_id" listValue="vendor_name"
										 cssClass="bk"></s:select>
								</TD>
								<TD class=column width="15%" align='right'>
									��������
								</TD>
								<TD width="35%">
									<SELECT name="adsl_hl" >
										<option value="1">
											==ADSL==
										</option>
										<option value="2">
											==LAN==
										</option>
										<option value="3">
											==EPON==
										</option>
										<option value="4">
											==GPON==
										</option>
									</SELECT>
								</TD>
							</TR>
							<TR>
								<TD class=column width="15%" align='right'>
									��ҵ����
								</TD>
								<TD width="35%">
									<input type='text' name="industry" />
								</TD>
								<TD class=column width="15%" align='right'>
									Ԥ��ԭ��
								</TD>
								<TD width="35%">
									<input type='text' name="warningReason" />
								</TD>
							</TR>
							<TR>
								<TD class=column width="15%" align='right'>
									Ԥ����ʼʱ��
								</TD>
								<TD width="35%">
									<input type="text" name="startWarningDate" readonly class=bk
										value="<s:property value="startWarningDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.startWarningDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
									&nbsp;
									<font color="red"> *</font>
								</TD>
								<TD class=column width="15%" align='right'>
									Ԥ������ʱ��
								</TD>
								<TD width="35%">
									<input type="text" name="endWarningDate" readonly class=bk
										value="<s:property value="endWarningDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.endWarningDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
									&nbsp;
									<font color="red"> *</font>
								</TD>
							</TR>
							<TR>
								<TD class=column width="15%" align='right'>
									�豸����
								</TD>
								<TD width="35%" colspan=3>
									<input type='text' name="deviceType" />
								</TD>
							</TR>
							<TR>
								<td colspan="4" align="right" class=foot>
									<button onclick="query()">
										&nbsp;�� ѯ&nbsp;
									</button>
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
				<tr>
					<td height="25">
					</td>
				</tr>
				<tr>
					<td id="queryInfo">

					</td>
				</tr>
			</table>
			<br>
		</form>
	</body>
</html>
<%@ include file="../../foot.jsp"%>