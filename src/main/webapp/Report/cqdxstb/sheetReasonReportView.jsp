<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>����ʧ��ԭ��ͳ��</title>
		<%
			 /**
			 * ����ͳ�Ʊ����ѯ
			 * 
			 * @author chenjie(67371)
			 * @version 1.0
			 * @since 2011-08-26
			 * @category
			 */
		%>
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
			type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript"
			src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
		<script type="text/javascript">
function query(){
	
	/* var qryType = $.trim($("#servTypeId").val());
    if(qryType == "-1"){
         alert("��ѡͳ������");
         return false;
    } */
    $("#querybtn").attr("disabled","disabled");
    document.selectForm.action = "<s:url value='/gtms/stb/report/sheetReasonReport!getStatsReport.action'/>";
    document.selectForm.target = "dataForm";
	document.selectForm.submit();
}

//������Ҫѡ��߼���ѯѡ��
function ShowDialog(leaf){
	//pobj = obj.offsetParent;
	oTRs = document.getElementsByTagName("TR");
	var m_bShow;
	var setvalueTemp = 0;
	for(var i=0; i<oTRs.length; i++){
		if(oTRs[i].leaf == leaf){
			m_bShow = (oTRs[i].style.display=="none");
			oTRs[i].style.display = m_bShow?"":"none";
		}
	}
	if(m_bShow){
		setvalueTemp = "1";
	}
	setValue(setvalueTemp);
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
		<form name="selectForm" action="<s:url value='/gtms/stb/report/sheetReasonReport!getStatsReport.action'/>"
			target="dataForm">
			<input type="hidden" name="selectType" value="0" />
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
									����ʧ��ԭ��ͳ��
								</td>
								<td>
									<img src="<s:url value="/images/attention_2.gif"/>" width="15"
										height="12" />
									��ѯ����ִ�������ͳ��ʧ��ԭ��
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
									����ʧ��ԭ��ͳ��
								</th>
							</tr>

							<TR>
								<TD class=column width="15%" align='right'>
									BSS����ʼʱ��
								</TD>
								<TD width="35%">
									<input type="text" name="startOpenDate" readonly class=bk
										value="<s:property value="startOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
									&nbsp;
									<font color="red"> *</font>
								</TD>
								<TD class=column width="15%" align='right'>
									BSS�������ʱ��
								</TD>
								<TD width="35%">
									<input type="text" name="endOpenDate" readonly class=bk
										value="<s:property value="endOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
									&nbsp;
									<font color="red"> *</font>
								</TD>
							</TR>
							<TR>
								<td colspan="4" align="right" class=foot>
									<button onclick="query()"  id="querybtn">
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
				<td>
				  <div style="border:1px solid #ACA899;width:100%;font-size:12px;text-align:left;padding:8px" id="sheetMsgs">
			               ����ʧ��ԭ��ͳ�ƣ�����ģ����ܹ���ʧ��ԭ��ͳ�ơ�<br/>
 			 ����ֶθ�����<br/>
			 &nbsp;&nbsp;&nbsp;&nbsp;�����ɹ�����ָ�����ع���ִ�гɹ���������<br/>
			 &nbsp;&nbsp;&nbsp;&nbsp;loid���û�ע���룻<br/>
			 &nbsp;&nbsp;&nbsp;&nbsp;work_asgn_id��������ˮ�ţ�<br/>
 			 &nbsp;&nbsp;&nbsp;&nbsp;Order_Remark������˵����������������ʩ�������˵���<br/>
			 &nbsp;&nbsp;&nbsp;&nbsp;STBID�������б�ʶ��</br>
			 &nbsp;&nbsp;&nbsp;&nbsp;���ŷ�ʽ���û�������װ����������Ա���Ű�װ
			 
			</div>
				</td>
				</tr>
			</table>
			<br>
		</form>
	</body>
</html>