<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String gwType = request.getParameter("gw_type");
	String flag = request.getParameter("flag");
 %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>������ͼ�����޸�</title>
		<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
		<link href="../../css/listview.css" rel="stylesheet" type="text/css">
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckForm.js"></SCRIPT>
		
<SCRIPT LANGUAGE="JavaScript">
var deviceId ="";
$(function(){
	gwShare_setGaoji();
	gwShare_setImport();
	$("input[@name='gwShare_queryResultType']").val("checkbox");
});
function deviceResult(returnVal){
	
	$("#doButton").attr("disabled",false);
	
	deviceId="";
	$("#resultDIV").html("");
	
	var totalNum = returnVal[0];
	if(returnVal[0]==0)
	{
		totalNum = returnVal[2].length;
	}
		
	var deviceIdArray = returnVal[2];
	for(var i=0 ;i<deviceIdArray.length;i++){
		//����������deviceId
		deviceId +=  deviceIdArray[i][0]+",";
	}
	var endIndex = deviceId.lastIndexOf(",");
	deviceId = deviceId.substring(0,endIndex);
	if(totalNum > 100){
		alert("�豸��������100̨��Ӱ�쵽��������");
		$("#doButton").attr("disabled",true);
		return;
	}
	$("div[@id='selectedDev']").html("<font size=2>"+totalNum+"</font>");
	
	// �ж�δ�����Ե�����
	var url = "<s:url value='/gtms/config/paramNodeBatchConfigAction!queryUndoNum.action'/>"; 
	var maxNum = 50000;
	$.post(url,{} ,function(ajax){
           var num = parseInt(ajax);
           if(num > maxNum)
           {
           		alert("�����������Ѵﵽ���ޣ������������ã�");
           		$("#doButton").attr("disabled",true);
	           return;
           }
    });
}
function downloadCase() {
	var url = "<s:url value='/gtms/config/digitMapConfig!download.action'/>";
		var caseDownload = $('input[name="caseDownload"]:checked').val();
			document.getElementById("frm").action = url;
		document.getElementById("frm").submit();
	}
function doExecute(){
	var map_id = $("select[@id='map_id']").val();
	var url = "<s:url value='/gtms/config/digitMapConfig!doConfigAll.action'/>";
	$("#doButton").attr("disabled",true);
	$.post(url,{
			          flag : <%=flag%>,
              map_id : map_id, 
              device_id : deviceId,
              gw_type: <%=gwType%>
           },function(ajax){
           	$("#resultDIV").html("");
	         	$("#doButton").attr("disabled",false);
				if("1"==ajax){
					$("#resultDIV").append("��ִ̨�гɹ�");
				}else if ("-4"==ajax){
					$("#resultDIV").append("��ִ̨��ʧ��");
				}else{
					$("#resultDIV").append("��ִ̨���쳣");
				}
            });
}

</SCRIPT>
</head>
	<body>
		<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
			<TR>
				<TD HEIGHT=20>
					&nbsp;
				</TD>
			</TR>
			<TR>
				<TD>

					<table width="98%" border="0" align="center" cellpadding="0"
						cellspacing="0" class="text">
						<tr>
							<td>
								<table width="100%" height="30" border="0" cellspacing="0"
									cellpadding="0" class="green_gargtd">
									<tr>
										<td width="162" align="center" class="title_bigwhite" nowrap>
											������������ͼ�ֹ�����
										</td>
										<td nowrap>
											<img src="../../images/attention_2.gif" width="15"
												height="12">
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
						<tr bgcolor="#FFFFFF">
							<td>
								<FORM NAME="frm" METHOD="post" ACTION="" >
									<TABLE id="table_showConfig" width="100%" border=0
										cellspacing=0 cellpadding=0 align="center"
										style="" class="querytable">
										<tr>
											<th colspan="2"  align="center">
												������������ͼ�ֹ�����
											</th>
										</tr>
										<TR>
											
											<TD bgcolor=#999999>
												<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
													<TR bgcolor="#FFFFFF">
														<td nowrap align="right" class=column width="30%">
															�����豸������
														</td>
														<td width="70%">
															<div id="selectedDev"></div>
														</td>
													</TR>
													<TR bgcolor="#FFFFFF" >
														<td  align="right" class=column width="30%">
															��ͼģ������
														</td>
														<td  width="70%" >
															<s:select list="digitMapList" name="map_id" id="map_id" listKey="map_id" listValue="map_name" 
															 cssClass="bk"></s:select>
														<span >&nbsp;&nbsp;&nbsp;
															�����ѯ����ģ������:
															<input type="radio" width="22%" name="caseDownload" value="1" onclick="downloadCase()">txtģ��</input>
														<input type="radio" width="22%" name="caseDownload" value="0" onclick="downloadCase()">xlsģ��</input>
														</span>
														</td>
														
													</TR>
													<TR bgcolor="#FFFFFF">
														<TD colspan="2" align="right" class="green_foot">
															<button type="button" id="doButton" name="doButton"
																onclick="doExecute();" class=btn disabled="true">
																&nbsp;��&nbsp;��&nbsp;��&nbsp;�� &nbsp;
															</button>
														</TD>
													</TR>
													<TR bgcolor="#FFFFFF">
														<TD colspan="4" align="left" class="green_foot">
															<div id="resultDIV" />
														</TD>
													</TR>
												</TABLE>
											</TD>
										</TR>
									</TABLE>
								</FORM>
							</td>
							
						</tr>
					</table>
				</TD>
			</TR>
		</TABLE>
	</body>
</html>
<%@ include file="../../foot.jsp"%>