<%--
FileName	: proProcess.jsp
Date		: 2013��5��21��
Desc		: �½��·����������
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%
request.setCharacterEncoding("gbk");
String gwType = request.getParameter("gw_type");
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript">

$(function(){
	$("input[@name='gwShare_queryResultType']").val("checkbox");
	//gwShare_setOnlyGaoji();
	gwShare_setGaoji();
});
var device_id = "";
var param = "";
var gw_type = "<%= gwType%>";
var deviceIds="";

function checkForm(){
	if($("input[@name='ttNumber']").val()==""){
		alert("��������������");
		return false;
	}
	return true;
}

function deviceResult(returnVal){	
	deviceIds="";
	var totalNum = returnVal[0];
	if(returnVal[0]==0){
		totalNum = returnVal[2].length;
		if(100000<totalNum){
			alert("���������豸��ӦС��100000̨����������ذ汾���������ˣ�");
       		$("#exeButton").attr("disabled",true);
            return;
		}
		$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+returnVal[2].length+"</strong></font>");
		for(var i=0;i<returnVal[2].length;i++){
			deviceIds = deviceIds + returnVal[2][i][0]+",";			
		}
		$("input[@name='deviceIds']").val(deviceIds);
	}else{
		$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+returnVal[0]+"</strong></font>");
		deviceIds = "0";
		param=returnVal[1];
	}
	if(100000<returnVal[0]){
		alert("���������豸��ӦС��100000̨����������ذ汾���������ˣ�");
   		$("#exeButton").attr("disabled",true);
        return;
	}
}

//ִ������
function doExecute(){
    var url = "<s:url value='/gtms/config/batchConfigMaxTerminal!maxTerminalConfig.action'/>"; 
    var mode = $("select[@name='mode']").val();
	var total_number = $("input[@name='ttNumber']").val();
	if(checkForm()){
        $.post(
                url,{
                deviceIds : deviceIds,
                mode : mode,
                total_number : total_number,
                param:param
         } ,function(ajax){
                $("#resultDIV").html("");
                $("#doButton").attr("disabled",false);
                if("1"==ajax){
                      $("#resultDIV").append("��ִ̨�гɹ�");
                 }else{
                     $("#resultDIV").append("��ִ̨��ʧ��");
                 }
        });
    }
}

</SCRIPT>
<%@ include file="../../toolbar.jsp"%>


<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="text">
				<TR>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									��������ն�������
								</td>
								<td nowrap>
									<img src="../../images/attention_2.gif" width="15" height="12">
									&nbsp;��������ն�������
								</td>
							</tr>
						</table>
					</td>
				</TR>

				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
					</td>
				</TR>
				<TR>
					<TH colspan="4" align="center">
						�����ն�������
					</TH>
				</TR>
				<tr>
					<td>
							<input type="hidden" name="deviceIds" value="" />
							<input type="hidden" name="param" value="" />
							<input type="hidden" name="gw_type" value="<%=gwType%>" />
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<tr bgcolor="#FFFFFF">
												<td colspan="6">
													<div id="selectedDev" >
														���ѯ�豸��
													</div>
												</td>
											</tr>
											<tr bgcolor="#FFFFFF">

												<td align="right" width="15%">
													���Է�ʽ��
												</td>
												<td width="30%">
													<select id="strategy_type" name ="strategy_type">
														<option value="1">����ִ��</option>
													</select>
												</td>
												<td align="right" width="15%">
													ģʽ��
												</td>
												<td width="30%">
													<select id="mode" name ="mode">
														<option value="0">�ر�</option>
														<option value="1">����</option>
													</select>
												</td>

												<td align="right" width="15%">
													��������
												</td>
												<td align="center">
													<input type="text" name="ttNumber" value="" class="bk">&nbsp;&nbsp; 
												</td>
											</tr>
											<TR bgcolor="#FFFFFF">
													<TD colspan="6" align="right" class="green_foot">
														<button type="button" id="exeButton" name="exeButton"
															onclick="doExecute();" class=btn>
																	&nbsp;ִ&nbsp;�� &nbsp;
														</button>
													</TD>
												</TR>
											<TR bgcolor="#FFFFFF">
													<TD colspan="6" align="left" class="green_foot"><div id="resultDIV" /></TD>
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
