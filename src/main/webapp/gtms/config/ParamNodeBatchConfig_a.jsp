<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String gwType = request.getParameter("gw_type");
	if(""==gwType||null==gwType){
		gwType="1";
	}
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title >批量配置参数节点(灵活配置)</title>
		<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
		<link href="../../css/listview.css" rel="stylesheet" type="text/css">
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckForm.js"></SCRIPT>
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
										<td width="173" style="Font-size:12px" align="left" class="title_bigwhite" nowrap>
											批量配置参数节点(灵活配置)
										</td>
										<td><img src="../../images/attention_2.gif" width="15"
							height="12"><span id="msg">批量配置参数策略为立即执行 </span></td>
									</tr>
								</table>
							</td>
						</tr>

						<TR bgcolor="#FFFFFF">
							<td colspan="4">
								<%@ include file="/gwms/share/gwShareDeviceQuery_ParamNodeBatch.jsp"%>
							</td>
						</TR>
						
						<tr bgcolor="#FFFFFF">
							<td>
									<input type="hidden" name="param" value="" />
									<TABLE id="table_showConfig" width="100%" border=0
										cellspacing=0 cellpadding=0 align="center" class="querytable">
										<tr>
											<th  style='display:inline-block;'  width="100%">
												<div align="center">
													配置    策略
												</div>
											
											</th>
												
										</tr>
										<tr bgcolor="#FFFFFF">
															<td colspan="4">
																<div id="selectedDev">
																	请查询设备！
																</div>
															</td>
														</tr>
										<TR>
											<TD bgcolor=#999999>
											 <div id="fatherDiv">
												<div id="paramAddDiv">
													<table border=0 cellspacing=1 cellpadding=2 width="100%">
														
														<TR bgcolor="#FFFFFF">
															<td nowrap align="right" class=column width="20%">
																参数节点路径
															</td>
															<td width="70%" colspan="3" >
																<input type="text" id="gather_path" name="gather_path" style="width:800px;"> 
															</td>
														</TR>
														
														</table>
														</div>
													</div>
													<div>
													<table border=0 cellspacing=1 cellpadding=2 width="100%">
														<form id="frm">
														<TR bgcolor="#FFFFFF">
															
															<td  align="right" class=column width="20%">
																生成文件名称
															</td>
															
															<td  width="70%" >
																<span name="currentDate" id ="currentDate"></span>
																<input type="text" id="fileName" name="file_Name" value=""/>
																<span>文件名称格式为<span style = "color:blue">日期(系统自动生成)+用户输入内容格式(不支持中文)</span></span>
																
																<span>上传附件模板:<input type="radio" width="22%" name="caseDownload" value="1" id="txt" onclick="downloadCase()" /><label for="txt">txt模板下载</lable>
																<input type="radio" width="22%" name="caseDownload" value="0" id="xls" onclick="downloadCase()" /><label for="xls">xls模板下载</lable>
																</span>
															</td>
															
														</TR>
														</form>
														
														</table>
														</div>
													<table  border=0 cellspacing=1 cellpadding=2 width="100%">
															<TR bgcolor="#FFFFFF">
																<TD colspan="4" align="right" class="green_foot">
																	<button type="button" id="exeButton" name="exeButton"
																		disabled = "true" onclick="doExecute();" class=btn>
																				&nbsp;执&nbsp;行 &nbsp;
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
	$(function(){
		$("#msg").css({color:"red"}); 
		var starttime = "01:00:00";
	var endtime = "23:00:00";
	starttime = parseInt(starttime.split(":")[0]) * 3600 + parseInt(starttime.split(":")[1]) * 60 + parseInt(starttime.split(":")[2]);
	endtime = parseInt(endtime.split(":")[0]) * 3600 + parseInt(endtime.split(":")[1]) * 60 + parseInt(endtime.split(":")[2]);
	var currenttime = parseInt(new Date().toLocaleTimeString().split(":")[0]) * 3600 + parseInt(new Date().toLocaleTimeString().split(":")[1]) * 60 + parseInt(new Date().toLocaleTimeString().split(":")[2]);
	if(!(currenttime >= starttime && currenttime <= endtime)){
			$("#gwShare_queryButton,#gwShare_reButto,#doAddButton,#exeButton,#gather_path,#fileName").attr("disabled",true);
			$("#deletePath").removeAttr("href");
			
			$("#gwShare_tr31").hide();
		}
		var date = new Date();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() +""+ month + strDate;
    $("span[@name='currentDate']").text(currentdate);
		$("input[@name='gwShare_queryResultType']").val("checkbox");
	});
	
	var device_id="";
	var device_serialnumber="";
	var city_id="";
	var paramvalue="";
	var loid="";
	function deviceResult(returnVal){
			$("#gather_path").val("");
      $("#fileName").val("");
    
		var checkTotalNum;
		if(returnVal[0]==0){
			checkTotalNum = returnVal[2].length;
		}else{
			checkTotalNum = returnVal[0];
		}
		if(2000 < checkTotalNum){
			alert("设备数量超过2000台,影响到交互性能");
				return;
			}
		$("#exeButton").attr("disabled","");
		device_id="";
	  device_serialnumber="";
		city_id="";
		paramvalue="";
		loid="";
		var deviceIdArray = returnVal[2];
		for(var i=0 ;i<deviceIdArray.length;i++){
			device_id +=  $.trim(deviceIdArray[i][0])+",";
			if(""!=deviceIdArray[i][2]&&null!=deviceIdArray[i][2]){
				device_serialnumber += $.trim(deviceIdArray[i][2])+",";
			}else{
				device_serialnumber += "NULL"+",";
			}
			
			city_id += $.trim(deviceIdArray[i][4])+",";
			paramvalue += $.trim(deviceIdArray[i][12])+",";
			if(""!=deviceIdArray[i][13]&&null!=deviceIdArray[i][13]){
				loid += $.trim(deviceIdArray[i][13])+",";
			}else{
				loid += "NULL"+",";
			}
			
		}
		var endIndex = device_id.lastIndexOf(",");
		device_id = device_id.substring(0,endIndex);
		endIndex = device_serialnumber.lastIndexOf(",");
		device_serialnumber = device_serialnumber.substring(0,endIndex);
		endIndex = city_id.lastIndexOf(",");
		city_id = city_id.substring(0,endIndex);
		endIndex = paramvalue.lastIndexOf(",");
		paramvalue = paramvalue.substring(0,endIndex);
		endIndex = loid.lastIndexOf(",");
		loid = loid.substring(0,endIndex);
		$("#exeButton").attr("disabled",false);
		deviceIds ="";
		$("table[@id='table_showConfig']").css("display","");
		$("#resultDIV").html("");
		var totalNum = returnVal[0];
		if(returnVal[0]==0){
			totalNum = returnVal[2].length;
			$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[2].length+"</strong></font>");
			for(var i=0;i<returnVal[2].length;i++){
				deviceIds = deviceIds + returnVal[2][i][0]+",";			
			}
		}else{
			$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[0]+"</strong></font>");
			deviceIds = "0";
			param=returnVal[1];
		}
		
	}
		 var dateName;
		 $.ajaxSetup({  
    		async : false  
			}); 
   function downloadCase() {
	  var url = "<s:url value='/gtms/config/paramNodeBatchConfigAction!downModle.action'/>";
		var caseDownload = $('input[name="caseDownload"]:checked').val();
		document.getElementById("frm").action = url;
		document.getElementById("frm").submit();
			
	}
  function  checkForm(){
  	
        var fileName = $("#fileName").val();
        if(fileName==""){
        	alert("文件名不可为空！");
				return false;
        }
        var gather_path = $("input[@name='gather_path']").val();
        if(null==gather_path||""==$.trim(gather_path)){
        	alert("请输入参数节点路径!");
        	return false;
        }
        var reg = /[\u4E00-\u9FA5\uF900-\uFA2D]/;
        if(reg.test(fileName)){
        	alert("文件名不支持中文!");
        	return false;
        }
   			var checkRepeatUrl = "<s:url value='/gtms/config/paramNodeBatchConfigAction!checkRepeat.action'/>";
   			var judge = "0";
        $.post(
	                checkRepeatUrl,{
	                file_name : dateName
	         			} ,function(ajax){
	         				if(ajax != "0"){
	         					judge = ajax;
	         					$("#exeButton").attr("disabled","");
											alert("今天已存在此文件名的任务！");
									}
	        		});
	        		if(judge != "0"){
	        			return false;
	        		}

	      return true;
  	}
	//执行配置
	function doExecute(){
		$("#resultDIV").text("正在配置,请稍等...");
		var currentDate = $("#currentDate").text();
		 var fileName = $("#fileName").val();
		dateName = currentDate+""+fileName;
		var urlcheckNum = "<s:url value='/gtms/config/paramNodeBatchConfigAction!queryCustomNum.action'/>";
		var flag =false;
		$.post(
	                urlcheckNum,{
	                
	         } ,function(ajax){
	         	if(ajax>=50000){
	         		alert("今天导入配置数据已达上限");
	         		$("#resultDIV").text("");
	         		flag=true;
	         	}
			});
		if(flag){
			return;
		}
		var url = "<s:url value='/gtms/config/paramNodeBatchConfigAction!doConfigParamBatch.action'/>";
	  // $("#doButton").attr("disabled",true);
	   var len = $("#fatherDiv div").size();
	   var gather_path = $("input[@name='gather_path']").val();
	      var gwType=<%=gwType%>
				var username =device_serialnumber+"*"+loid;
				if(checkForm()){
					$.post(
	                url,{
	                gw_type : gwType,
	                paramNodePath : gather_path,
	                deviceIds : device_id,
	                cityIds : city_id,
	                username : username,
	                paramValue :paramvalue,
	                file_name : dateName
	         } ,function(ajax){
	         	$("#resultDIV").text(ajax);
	         	if (ajax == "1"){
						$("#exeButton").attr("disabled","false");
						$("#resultDIV").html("");
						$("#resultDIV").html("<FONT COLOR='blue'>参数节点配置成功!</FONT>");
						} else {
						$("#resultDIV").html("<FONT COLOR='red'>参数节点配置失败!</FONT>");
					}
			});
     }
	}
	 	
	</script>
</html>
<%@ include file="../../foot.jsp"%>