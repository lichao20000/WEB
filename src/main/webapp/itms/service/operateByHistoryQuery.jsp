<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ԭʼ�ֹ�������ѯ</title>
<%
	/**
	 * ԭʼ�ֹ�������ѯ
	 * 
	 * @author gaoyi
	 * @version 1.0
	 * @since 2013-05-15
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
var nowDate = "";
	function query() {
		if(check()){
			document.selectForm.submit();
		}
	}
	
	
	function check(){
		var starttime = $("input[@name='startOpenDate']").val();
		var endtime = $("input[@name='endOpenDate']").val();
		var username = Trim($("input[@name='username']").val());
		var usernameType = $("select[@name='usernameType']").val();
		if(username!=""){
			if(usernameType=="1" && username.length<6){
				alert("Ψһ��ʶ����С��6λ��");
			}
			//$("input[@name='startOpenDate']").val("");
			//$("input[@name='endOpenDate']").val("");
		}
		else{
			if(starttime==""){
				alert("�����뿪ʼʱ����û���Ϣ��");
				return false;
			}
			if(endtime==""){
				$("input[@name='endOpenDate']").val(nowDate);
				endtime = nowDate;
			}
		    var start=new Date(starttime.replace("-", "/").replace("-", "/"));  
		    var end=new Date(endtime.replace("-", "/").replace("-", "/"));  
		    if(end<start){
		        alert("����ʱ��Ӧ���ڿ�ʼʱ�䣡");
		        return false;
		    }
		    else if((end-start)>24*3600*1000*30){
		    	alert("ʱ����ӦС��30�죡");
		    	return false;
		    }
		}
		return true;
		
	}
	
	
	function changeisGl(){
		var servType= $("select[name='servType']").val();
		
		var glObj = $("select[name='isGl']");
		
		if(servType=="-1"){
			glObj.attr("disabled",false);
		}else{
			glObj.val("0");
			glObj.attr("disabled",true);
			
		}
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
			//�����趨�Ĳ���������֧��iframe�����������ʾ����
			if ((document.all || document.getElementById) && iframehide == "no") {
				var tempobj = document.all ? document.all[iframeids[i]]
						: document.getElementById(iframeids[i])
				tempobj.style.display = "block"
			}
		}
	}

	$(function() {
		dyniframesize();
		nowDate = $("input[@name='endOpenDate']").val();
	});

	$(window).resize(function() {
		dyniframesize();
	});
</script>
</head>

<body>
	<form id="form" name="selectForm"
		action="<s:url value='/itms/service/operateByHistoryQuery!getOperateByHistoryInfo.action'/>"
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
								ԭʼ������ѯ</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /> ��ѯԭʼ������Ϣ���</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">ԭʼ������ѯ</th>
						</TR>
						<TR>
							<TD class="column" width='15%' align="right">
								<SELECT  name="usernameType">
										<ms:inArea areaCode="sx_lt" notInMode="true">
										<option value="1" selected="selected">
											LOID
										</option>
										</ms:inArea>
										<ms:inArea areaCode="sx_lt" notInMode="false">
										<option value="1" selected="selected">
											Ψһ��ʶ
										</option>
										</ms:inArea>
										<option value="2">
											����˺�
										</option>
										<option value="3">
											������ˮ��
										</option>
									</SELECT>
							</TD>
							<TD width="35%"><input type="text" name="username" size="20"
								maxlength="30" class=bk /></TD>
						 	<TD class=column width="15%" align='right'>����</TD>
							<TD width="35%"><s:select list="cityList" name="city_id"
									headerKey="-1" headerValue="��ѡ������" listKey="city_id"
									listValue="city_name" cssClass="bk"></s:select></TD>
						</TR>
						
						<TR>
							<TD class="column" width='15%' align="right">ҵ������</td>
							<td width='35%' align="left"><select name="servType"  onchange="changeisGl()"
								class="bk">
									<option value="-1"
										<s:property value='"-1".equals(servType)?"selected":""'/>>
										==��ѡ��==</option>
									<option value="20"
										<s:property value='"20".equals(servType)?"selected":""'/>>
										==�û�����==</option>
									<option value="10"
										<s:property value='"10".equals(servType)?"selected":""'/>>
										==���ҵ��==</option>
									<option value="11"
										<s:property value='"11".equals(servType)?"selected":""'/>>
										==IPTVҵ��==</option>
									<option value="14"
										<s:property value='"14".equals(servType)?"selected":""'/>>
										==VOIPҵ��==</option>
									<ms:inArea areaCode="sx_lt" notInMode="true">
									<option value="23"
										<s:property value='"23".equals(servType)?"selected":""'/>>
										==WIFIҵ��==</option>
									</ms:inArea>
							</select></TD>
							
							<TD class="column" width='15%' align="right">��������</TD>
							<TD width='35%' align="left" id="allOrderType"><select 
								name="resultType"  class="bk">
									<option value="-1"
										<s:property value='"-1".equals(resultType)?"selected":""'/>>
										==��ѡ��==</option>
									<option value="1"
										<s:property value='"1".equals(resultType)?"selected":""'/>>
										==����==</option>
									<option value="2"
										<s:property value='"2".equals(resultType)?"selected":""'/>>
										==���==</option>
									<option value="3"
										<s:property value='"3".equals(resultType)?"selected":""'/>>
										==����==</option>
									<ms:inArea areaCode="sx_lt" notInMode="true">
									<option value="4"
										<s:property value='"4".equals(resultType)?"selected":""'/>>
										==��ͣ==</option>
									<option value="5"
										<s:property value='"5".equals(resultType)?"selected":""'/>>
										==����==</option>
									</ms:inArea>
							</select></TD>

						</TR>
						<TR>
							<TD class=column width="15%" align='right'>��ʼʱ��</TD>
							<TD width="35%">
									<input type="text" name="startOpenDate" readonly class=bk
										value="<s:property value="startOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
										&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>
							</TD>
							<TD class=column width="15%" align='right'>����ʱ��</TD>
							<TD width="35%">
									<input type="text" name="endOpenDate" readonly class=bk
										value="<s:property value="endOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
										&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>
							</TD>
						</TR>

						<TR>
							<TD class="column" width='15%' align="right">��ͨ״̬</TD>
							<TD width='35%' align="left"><select
								name="resultId" class="bk">
									<option value="-1"
										<s:property value='"-1".equals(resultId)?"selected":""'/>>
										==��ѡ��==</option>
									<option value="0"
										<s:property value='"0".equals(resultId)?"selected":""'/>>
										==�ɹ�==</option>
									<option value="1"
										<s:property value='"1".equals(resultId)?"selected":""'/>>
										==ʧ��==</option>
							</select></TD>
							<TD class="column" width='15%' align="right">�Ƿ�������Ϲ���</TD>
							<TD width='35%' align="left"><select
								name="isGl" class="bk">
									<option value="0" selected="selected">==��==</option>
									<option value="1" >==��==</option>
							</select></TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class=foot>
								<input type="hidden" name="gw_type" id="gw_type" value="<%=request.getParameter("gw_type") %>" />
								<button onclick="javaScript:query();" >&nbsp;��&nbsp;&nbsp;ѯ&nbsp;</button>
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
			<tr>
				<td><iframe id="dataForm" name="dataForm" height="0"
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
</body>
</html>