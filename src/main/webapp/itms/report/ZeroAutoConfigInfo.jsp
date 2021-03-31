<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ITMSϵͳ������ҵ���Զ���ͨ���</title>
<%
	/**
	 * ITMSϵͳ������ҵ���Զ���ͨ�����ѯ
	 * 
	 * @author gaoyi
	 * @version 1.0
	 * @since 2013-06-17
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	function query() {
		document.selectForm.submit();
		$("#qy").attr('disabled',true);
		showMsgDlg();
	}

	function ToExcel() {
		var starttime = $("input[@name='startOpenDate']").val();
		var endtime = $("input[@name='endOpenDate']").val();
		var city_id = $("select[@name='city_id']").val();
		var servType = $("select[@name='servType']").val();
		var resultType = $("select[@name='resultType']").val();

		var url = "<s:url value='/itms/report/zeroAutoConfig!getZeroAutoConfigInfoCount.action'/>";
		$.post(url, {starttime : starttime,
					 endtime : endtime,
					 city_id : city_id,
					 servType : servType,
					 resultType : resultType
					},
						function(ajax) {
							var total = parseInt(ajax);
							if (ajax > 100000) {
								alert("������̫��֧�ֵ��� ");
								return;
							} else {
								
								var mainForm = document.selectForm;
								mainForm.action = "<s:url value='/itms/report/zeroAutoConfig!getZeroAutoConfigInfoExcel.action'/>"
								mainForm.submit();
								mainForm.action = "<s:url value='/itms/report/zeroAutoConfig!getZeroAutoConfigInfo.action'/>"
							}
						});
	}

	//** iframe�Զ���Ӧҳ�� **//
	//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
	//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
	//����iframe��ID
	var iframeids = [ "dataForm" ]

	//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
	var iframehide = "yes"

	function dyniframesize() {
		var dyniframe = new Array()
		for (i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//�Զ�����iframe�߶�
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block"
					//����û����������NetScape
					if (dyniframe[i].contentDocument
							&& dyniframe[i].contentDocument.body.offsetHeight)
						dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
					//����û����������IE
					else if (dyniframe[i].Document
							&& dyniframe[i].Document.body.scrollHeight)
						dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
				}
			}
			//�����趨�Ĳ�����������֧��iframe�����������ʾ����
			if ((document.all || document.getElementById) && iframehide == "no") {
				var tempobj = document.all ? document.all[iframeids[i]]
						: document.getElementById(iframeids[i])
				tempobj.style.display = "block"
			}
		}
	}

	$(function() {
		dyniframesize();
	});

	$(window).resize(function() {
		dyniframesize();
	});
	
	
	//��ʼ����ʱ�����
	function showMsgDlg(){
			w = document.body.clientWidth;
			h = document.body.clientHeight;
			
			l = (w-250)/2;
			t = h/2-100;
			PendingMessage.style.left = l;
			PendingMessage.style.top  = t;
			PendingMessage.style.display="";
	}
	
	//������ݣ�����ҳ��
	function closeMsgDlg(){
			PendingMessage.style.display="none";
	}
	
	
</script>
</head>

<body>
	<form id="form" name="selectForm" id="selectForm"
		action="<s:url value='/itms/report/zeroAutoConfig!getZeroAutoConfigInfo.action'/>"
		target="dataForm">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
							BSSҵ��״̬ͳ��</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /> ʱ��ΪBSS����ʱ��</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">�û��󶨷�ʽͳ��</th>
						</tr>

						<TR>
							<TD class=column width="15%" align='right'>��ʼʱ��</TD>
							<TD width="35%">
									<input type="text" name="startOpenDate" readonly class=bk
										value="<s:property value="startOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
							</TD>
							<TD class=column width="15%" align='right'>����ʱ��</TD>
							<TD width="35%">
									<input type="text" name="endOpenDate" readonly class=bk
										value="<s:property value="endOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
							</TD>
						</TR>


						<TR>

							<TD class=column width="15%" align='right'>����</TD>
							<TD width="35%"><s:select list="cityList" name="city_id"
									headerKey="-1" headerValue="��ѡ������" listKey="city_id"
									listValue="city_name" cssClass="bk"></s:select></TD>

							<TD class="column" width='15%' align="right">ҵ������</td>
							<td width='35%' align="left"><select name="servType"
								class="bk">
									<option value="-1" <s:property value='"-1".eqauals(servType)?"selected":""'/>>==��ѡ��==</option>
									<option value="10" <s:property value='"10".eqauals(servType)?"selected":""'/>>==����==</option>
									<option value="11" <s:property value='"11".eqauals(servType)?"selected":""'/>>==IPTV==</option>
									<option value="14" <s:property value='"14".eqauals(servType)?"selected":""'/>>==VOIP==</option>
							</select></TD>

						</TR>
						<TR>
							<TD class="column" width='15%' align="right">��ͨ״̬</TD>
							<TD width='35%' colspan="3" align="left" id="allOrderType"><select
								name="resultType" class="bk">
									<option value="2"
										<s:property value='"2".equals(resultType)?"selected":""'/>>
										==��ѡ��==</option>
									<option value="0"
										<s:property value='"0".equals(resultType)?"selected":""'/>>
										==δ��==</option>
									<option value="1"
										<s:property value='"1".equals(resultType)?"selected":""'/>>
										==�ɹ�==</option>
									<option value="-1"
										<s:property value='"-1".equals(resultType)?"selected":""'/>>
										==ʧ��==</option>
							</select></TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class=foot>
								<button id="qy" onclick="query()"  >&nbsp;ͳ��&nbsp;</button>
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
			<tr>
				<td>
				<div id="PendingMessage"
						style="position:absolute;z-index:3;top:240px;left:250px;width:300;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;filter:alpha(opacity=80);display:none">
							<center>
									<table border="0" style="background-color:#eeeeee">
										<tr>
											<td valign="middle"><img src="<s:url value='/images/cursor_hourglas.gif'/>"  
												border="0" WIDTH="30" HEIGHT="30"></td>
											<td>&nbsp;&nbsp;</td>
											<td valign="middle"><span id=txtLoading
												style="font-size:14px;font-family: ����">���Ե�,���ڲ�ѯBSS��Ϣ������������</span></td>
										</tr>
									</table>
							</center>
				</div>
				<iframe id="dataForm" name="dataForm" height="0"
						frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
			</tr>
			<tr>
				<td height="25"></td>
			</tr>
			<tr>
				<td id="bssSheetInfo"></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>