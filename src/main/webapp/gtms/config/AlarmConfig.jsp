<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../timelater.jsp"%>
<%
	String gwType = request.getParameter("gw_type");
	if(null==gwType){
		gwType = "1";
	}
 %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>�澯�·�</title>
		<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
		<link href="../../css/listview.css" rel="stylesheet" type="text/css">
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckForm.js"></SCRIPT>
	<%@ include file="../../toolbar.jsp"%>
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
											�澯�·�
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
									<input type="hidden" name="param" value="" />
									<TABLE id="table_showConfig" width="100%" border=0
										cellspacing=0 cellpadding=0 align="center" class="querytable">
										<tr>
											<th  align="center" width="100%">
												�·��ڵ�
											</th>
												
										</tr>
										<tr bgcolor="#FFFFFF">
															<td colspan="4">
																<div id="selectedDev">
																	���ѯ�豸��
																</div>
															</td>
										</tr>
										<TR>
											<TD bgcolor=#999999>
											 <div id="fatherDiv">
												<div>
													<table border=0 cellspacing=1 cellpadding=2 width="100%">
														
														<TR bgcolor="#FFFFFF">
															<td nowrap align="right" class=column width="20%">
																�澯�ϱ�URL��ַ
															</td>
															<td width="80%" colspan="3" >
																<input type="text" id="url" name="url" style="width:800px;"> 
															</td>
														</TR>
														<TR bgcolor="#FFFFFF">
															<td  align="right" class=column width="20%">
																����������ֵ(kbps)
															</td>
															<td  width="30%" >
																<input type="text"  id="threshold" name="threshold" value="" onkeyup="onlyNum(this);"/>
															</td>
															<td  align="right" class=column width="20%">
																�ϴ�������ֵ(kbps)
															</td>
															<td  width="30%" >
																<input type="text"  id="upload" name="upload" value="" onkeyup="onlyNum(this);"/>
															</td>
														</TR>
														<TR bgcolor="#FFFFFF">
															<td  align="right" class=column width="20%">
																��������(s)
															</td>
															<td  width="30%" >
																<input type="text" id="samplePeriod" name="period" value="" onkeyup="onlyNum(this);"/>
															</td>
															<td  align="right" class=column width="20%">
																�ϱ�����(s)
															</td>
															<td  width="30%" >
																<input type="text" id="upPeriod" name="upPeriod" value="" onkeyup="onlyNum(this);"/>
															</td>
														</TR>
														<TR bgcolor="#FFFFFF">
															<td  align="right" class=column width="20%">
																�澯��������
															</td>
															<td  width="30%" >
																<input type="text" id="alarmTime" name="alarmTime" value="" onkeyup="onlyNum(this);"/>
															</td>
															<td nowrap align="right" class=column width="20%">
																�ϻ�ʱ��
															</td>
															<td width="30%" >
																<input type="text" id="oldTime" name="oldTime" value="" onkeyup="onlyNum(this);"/> 
															</td>
														</TR>
														</table>
														</div>
													</div>
													<table  border=0 cellspacing=1 cellpadding=2 width="100%">
															<TR bgcolor="#FFFFFF">
																<TD colspan="4" align="right" class="green_foot">
																	<button type="button" id="exeButton" name="exeButton"
																		onclick="doExecute();" class=btn>
																				&nbsp;��&nbsp;��&nbsp;
																	</button>
																</TD>
															</TR>
															<TR bgcolor="#FFFFFF">
																<TD colspan="4" align="left" class="green_foot">
																	<div id="resultDIV" />
																</TD>
															</TR>
													</table>
											</TD>
										</TR>
									</TABLE>
							</td>
						</tr>
					</table>
				</TD>
			</TR>
		</TABLE>
	</body>
<script type="text/javascript">
	var deviceIds = "";
	var param = "";
	var gwShare_queryType  ="";
	var fileName = "";
	$(function(){
		gwShare_setGaoji();
		gwShare_setImport();
		$("input[@name='gwShare_queryResultType']").val("checkbox");
	});
	
	function deviceResult(returnVal){

		$("#exeButton").attr("disabled",false);
		deviceIds ="";
		$("#resultDIV").html("");
		var totalNum = returnVal[0];
		if(returnVal[0]==0){
			totalNum = returnVal[2].length;
			$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+returnVal[2].length+"</strong></font>");
			for(var i=0;i<returnVal[2].length;i++){
				deviceIds = deviceIds + returnVal[2][i][0]+",";			
			}
		}else{
			$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+returnVal[0]+"</strong></font>");
			deviceIds = "0";
			param=returnVal[1];
		}
	}
	/**
*��ѯ���豸
**/
function deviceUpResult(returnVal){	
		$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+returnVal[0]+"</strong></font>");
		var gw_type = $("input[@name='gw_type']").val();
		fileName = returnVal[1] ;
		gwShare_queryType = returnVal[2] ;
	}
	// ���ƽ�����������
	function onlyNum(v){
		v.value=v.value.replace(/[^\d]/g,"");
	}
	//��֤
    function checkForm(){
			var url = $("#url");
			var threshold = $("#threshold");
			var upload = $("#upload");
			var samplePeriod = $("#samplePeriod");
			var upPeriod = $("#upPeriod");
			var alarmTime = $("#alarmTime");
			var oldTime = $("#oldTime");
			if(url.val()==null||""==url.val()){
				url.focus();
				return false;
			}
			if(threshold.val()==""||threshold.val()==null){
				threshold.focus();
				return false;
			}
			if(upload.val()==""||upload.val()==null){
				upload.focus();
				return false;
			}
			if(samplePeriod.val()==""||samplePeriod.val()==null){
				samplePeriod.focus();
				return false;
			}
			if(upPeriod.val()==null||upPeriod.val()==""){
				upPeriod.focus();
				return false;
			}
			if(alarmTime.val()==null||alarmTime.val()==""){
				alarmTime.focus();
				return false;
			}
			if(oldTime.val()==null||oldTime.val()==""){
				oldTime.focus();
				return false;
			}
			if(samplePeriod.val()%10!=0){
				alert("�������ڱ���Ϊ10��������");
				return false;
			}
			if((upPeriod.val()%samplePeriod.val())!=0){
				alert("�ϱ����ڱ���Ϊ�������ڵ�������");
				return false;
			}
			if((upPeriod.val()/samplePeriod.val())<=alarmTime.val()){
				alert("�澯��������Ӧ��С���ϱ�������������ڵı�ֵ");
				return false
			}
			if((oldTime.val()%upPeriod.val())!=0){
				alert("�ϻ����ڱ���Ϊ�ϱ����ڵ�������");
				return false;
			}
        return true;
    }
	//ִ������
	function doExecute(){
		var ur = "<s:url value='/gtms/config/alarmConfig!doConfig.action'/>";
		var url = $("#url").val();
		var threshold = $("#threshold").val();
		var upload = $("#upload").val();
		var samplePeriod = $("#samplePeriod").val();
		var upPeriod = $("#upPeriod").val();
		var alarmTime = $("#alarmTime").val();
		var oldTime = $("#oldTime").val();
		var gwType = <%=gwType%> ;
		if(checkForm()){
			 $.post(
	                ur,{
	                deviceIds : deviceIds,
	                param : param,
	                url : url,
	                threshold : threshold,
	                upload : upload,
	                samplePeriod : samplePeriod,
	                upPeriod : upPeriod,
	                alarmTime : alarmTime,
	                oldTime : oldTime,
	                gwShare_queryType : gwShare_queryType,
	                fileName : fileName,
	                gw_type : gwType
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
	</script>
</html>
<%@ include file="../../foot.jsp"%>