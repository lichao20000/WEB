<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ include file="../../toolbar.jsp"%> 
<%@taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>�⹦�ʲɼ���Χ�趨</title>
<script type="text/javascript" src="../../Js/jquery.js"></script>
<script type="text/javascript">
	function doOrder(){
		var cityId = $.trim($("select[@name='cityId']").val());
		var colType = $.trim($("select[@name='colType']").val());		
		if(cityId == "-1"){
			alert("��ѡ������");
			$("select[@name='cityId']").focus();
			return;
		}
		if(colType == "-1"){
			alert("��ѡ��ɼ�����");
			$("select[@name='colType']").focus();
			return;
		}
		$("#resultDIV").html("<font color='green'>���ڶ�����...</font>");
		url = "<s:url value='/itms/resource/VersionQuery!orderPower.action'/>";
		$("button[@name='button']").attr("disabled",true);
		$.post(url,{
			cityId:cityId,
			colType:colType
	    },function(ajax){
			$("div[@id='QueryData']").html("");
			if(ajax == 1){
				$("#resultDIV").html("<font color='blue'>���Ƴɹ�!</font>");
			}else if(ajax == 0){
				$("#resultDIV").html("<font color='red'>����ʧ��!</font>");
			}else if(ajax == 2){
				$("#resultDIV").html("<font color='red'>�õ���û�й⹦���쳣�豸!</font>");
			}else{
				$("#resultDIV").html("<font color='red'>�豸�ɼ��У��ϴ�����δ�ɼ����豸!</font>");
			}
	   		$("button[@name='button']").attr("disabled",false);
	    });
	}
</script>
<body>
	<table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
		<tr>
			<td height="20">&nbsp;</td>
		</tr>
		<tr>
			<td>
				<table width="98%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd" align="center">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							�ɼ���Χ�趨
						</td>
						<td>
							<img src="../../images/attention_2.gif" width="15" height="12">
							��'<font color="#FF0000">*</font>'�ı�������д��ѡ��
						</td>
					</tr>	
				</table>
			</td>
		</tr>
		<tr>
			<td align="center">
				<form name="frm">
					<table width="98%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td bgcolor="#999999">
								<table border="0" cellspacing="1" cellpadding="2" width="100%" align="center">
									<tr>
										<th bgcolor="#ffffff" colspan="8" >�⹦�ʲɼ���Χ����</th>
									</tr>
									<tr bgcolor="#ffffff">
										<td class="column" align="right" width="30%" >
											�����زɼ�
										</td>
										<td colspan="3" class="column">
											<s:select list="cityList" name="cityId" headerKey="-1"
												headerValue="��ѡ������" listKey="city_id" listValue="city_name"
												value="cityId" cssClass="bk"></s:select>
										</td>
										<td class="column" align="right" width="15%" >
											����ֵ�ɼ�
										</td>
										<td colspan="3" align="left" class="column">
											<select name="colType" class="bk" onchange="">
												<option value="-1">==��ѡ��ɼ�����==</option>
												<option value="0">��</option>
												<option value="1">���͹⹦���쳣</option>
												<option value="2">���ܹ⹦���쳣</option>
												<option value="3">ȫ���쳣</option>
											</select>&nbsp; <font color="#FF0000">* </font>
										</td>
									</tr>
									<tr bgcolor="#ffffff">
										<td class="foot" colspan="8" align="right">
											<button onclick="doOrder();" name="button">&nbsp;����&nbsp;</button>
										</td>
									</tr>
									<tr bgcolor="#ffffff">
										<td colspan="8" align="left" class="green_foot">
											<div id="resultDIV"></div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					<table>
						<tr>
							<td height="20">
							</td>
						</tr>
						<tr id="trData" style="display: none">
							<td class="colum">
								<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
									���ڶ��ƣ����Ե�....
								</div>
							</td>
						</tr>
						<tr>
							<td height="20">
							</td>
						</tr>
					</table>
				</form>
			</td>
		</tr>
	</table>
	<!-- <div style="position:absolute; bottom:50px;">
		
	</div> -->
</body>
</html>
<%@ include file="../foot.jsp"%>

