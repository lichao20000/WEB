<%@ page contentType="text/html; charset=GBK"%>
<%@ include file="/timelater.jsp"%>
<%@ include file="/head.jsp"%>
<%@ include file="/toolbar.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%--
	/**
	 * AP����ҵ���·�
	 *
	 * @author ����(����) Tel:�绰
	 * @version 1.0
	 * @since ${date} ${time}
	 * 
	 * <br>��Ȩ���Ͼ������Ƽ� ���ܿƼ���
	 * 
	 */
 --%>

<html>
  <head>
  	<meta http-equiv="Content-Type" content="text/html; charset=GBK">
    <title>Ӧ���ն�ҵ���·�</title>
    <script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
    <link href="/css/css_green.css" rel="stylesheet" type="text/css">
    
	<script type="text/javascript">
		$(function(){
			gwShare_setGaoji();
		});
		
		var gw_type = "<%=request.getParameter("gw_type")%>";
		
		function doBusiness(){
		    var _device_id = $("input[@name='device_id']").val();
		    //var servTypeId = $("select[@name='queryServTypeId']").val();
		    var _ssid = $("input[@name='ssid']").val();
		    var _matchExp = document.getElementById('matchExpression').value
		    var url = "<s:url value='/gtms/blocTest/apDeviceBusinessConfig!doBusiness.action'/>"; 
		    
			if(null == _device_id || "" == _device_id){
				alert("���Ȳ�ѯ�豸!");
				return false;
			}
			if(null == _ssid || "" == _ssid){
				alert("������SSID��");
				return false;
			}
			if(null == _matchExp || "" == _matchExp){
				alert("�����ģ��ƥ����ʽ��");
				return false ;
			}
			
			// Ϊ�˷�ֹ��ҵ���·��ڼ��ظ����[ҵ���·�]��ť���˴���[ҵ���·�]��ť�û�,��ҵ���·������سɹ���ʧ�ܺ󣬽���ť��Ϊ�ɵ��
			$("input[id='doButton']").attr("disabled", true); 
			
			$.post(url,{
				deviceId:_device_id,
				gw_type:gw_type,
				ssid:_ssid,
				//servTypeId:servTypeId,
				templatePara:encodeURIComponent(_matchExp)
			},function(ajax){
				alert(ajax);
			});
			// ��[ҵ���·�]��ť��Ϊ�ɵ����
			$("input[id='doButton']").attr("disabled", false);
		}
		
		function deviceResult(returnVal){
				
			$("tr[@id='tr02']").css("display","");
		
			$("td[@id='tdDeviceSn']").html("");
			$("td[@id='tdDeviceCityName']").html("");
			
			$("input[@name='device_id']").val(returnVal[2][0][0]);
			$("td[@id='tdDeviceSn']").append(returnVal[2][0][1]+" -"+returnVal[2][0][2]);
			$("td[@id='tdDeviceCityName']").append(returnVal[2][0][5]);	
		}
	</script>
</head>
<body>
	<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
		<TR> <TD HEIGHT=20> &nbsp; </TD> </TR>
		<TR> 
		<TD>
			<table width="100%" border="0" cellpadding="0" cellspacing="0"	class="text">
				<tr>
				<td>
					<table width="100%" height="30" border="0" cellspacing="0"
						cellpadding="0" class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite" nowrap>
								AP����ҵ���·�
							</td>
							<td nowrap>
								<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
								&nbsp;��������ҵ���·���AP�ն�
							</td>
						</tr>
					</table>
				</td>
				</tr>

				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
					</td>
				</TR>

				<TR  bgcolor="#FFFFFF">
					<TD colspan="4">
						<FORM NAME="frm">
							<TABLE id="tb1" border=0 cellspacing=1 cellpadding=2 width="100%" bgcolor=#999999>
								<TR>
									<TH colspan="4"> ҵ����� </TH>
								</TR>
								<TR bgcolor="#FFFFFF" id="tr01" >
									<td nowrap align="right" class=column width="15%">
										�ն�
										<input type="hidden" name="device_id" value="">
									</td>
									<td id="tdDeviceSn" width="35%">
									</td>
									<td nowrap align="right" class=column width="15%">
										����
									</td>
									<td id="tdDeviceCityName" width="35%">
									</td>
								</TR>
								
								<!-- 
								<TR id="bizshow" bgcolor="#FFFFFF" STYLE="display:none">
									<TD class=column align="right" nowrap width="15%">ҵ������:</TD>
									<TD width="35%" >
										<SELECT name="queryServTypeId" class="bk">
										<OPTION value="0">����ҵ��</OPTION>
										<OPTION value="10">����ҵ��</OPTION>
										<OPTION value="11">IPTVҵ��</OPTION>
										<OPTION value="14">VOIPҵ��</OPTION>
										</SELECT>
									</TD>
									<TD width="15%" class=column align="right">��������:</TD>
									<TD width="35%" id="operationTypeTd">����</TD>
								</TR>
								 -->
								 
								<TR id="bizshow" bgcolor="#FFFFFF" >
									<TD class=column align="right" nowrap width="15%">
										<font color="red">*</font>&nbsp;SSID:
									</TD>
									<TD width="35%" colspan="3">
										<input type="text" name="ssid" maxlength=255 class=bk size=20>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" id="tr02" style="display: none">
									<TD width="" colspan="4">
										<%@ include file="/share/apMatchTemplate.jsp"%>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD colspan="4" align="right" CLASS="green_foot">
										<INPUT type="button" value="ҵ���·�" id="doButton" onClick="javascript:doBusiness()" class=jianbian>
									</TD>
								</TR>
							</TABLE>
						</FORM>
					</TD>
				</TR>
			</table>
		</TD>
		</TR>
		<tr> <td height="20"></td> </tr>
	</TABLE>
</body>
<%@ include file="/foot.jsp"%>
</html>
