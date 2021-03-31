<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String gwType = request.getParameter("gw_type");
	String tempdpi = request.getParameter("dpi");
 %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<%if("true".equals(request.getParameter("dpi"))){%><title>dpi配置下发</title><%}else{ %><title>批量配置修改参数节点</title><%}%>
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
										<td width="162" align="center" class="title_bigwhite" nowrap>
											批量采集参数节点
										</td>
										<td><img src="../../images/attention_2.gif" width="15"
							height="12"><span id="msg">此功能仅限21:00-23:00点使用 </span></td>
									</tr>
								</table>
							</td>
						</tr>

						<TR bgcolor="#FFFFFF">
							<td colspan="4">
								<%@ include file="/gwms/share/getNodeBatchConfigQuery.jsp"%>
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
												<div align="right">
													<button type="button" id="doAddButton"
													onclick="doAdd();" class=btn>
															&nbsp;增&nbsp;加 &nbsp;
											</button>
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
												<div id="paramAddDiv0">
													<table border=0 cellspacing=1 cellpadding=2 width="100%">
														
														<TR bgcolor="#FFFFFF">
															<td nowrap align="right" class=column width="20%">
																<span >
																	<a id="deletePath" href="javascript:doDelete()">
																		删除    |
																	 </a>
																</span>
																参数节点路径
															</td>
															<td width="70%" colspan="3" >
																<input type="text" id="gather_path0" name="gather_path0" style="width:800px;"> 
															</td>
														</TR>
														
														</table>
														</div>
													</div>
													<div>
													<table border=0 cellspacing=1 cellpadding=2 width="100%">
														<TR bgcolor="#FFFFFF">
															<td  align="right" class=column width="20%">
																生成文件名称
															</td>
															
															<td  width="70%" >
																<span name="currentDate" id ="currentDate"></span>
																<input type="text" id="fileName" name="fileName" value=""/>
																<span>文件名称格式为<span style = "color:blue">日期(系统自动生成)+用户输入内容格式(不支持中文)</span></span>
															</td>
															
														</TR>
														
														
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
		var starttime = "21:00:00";
	var endtime = "23:00:00";
	starttime = parseInt(starttime.split(":")[0]) * 3600 + parseInt(starttime.split(":")[1]) * 60 + parseInt(starttime.split(":")[2]);
	endtime = parseInt(endtime.split(":")[0]) * 3600 + parseInt(endtime.split(":")[1]) * 60 + parseInt(endtime.split(":")[2]);
	var currenttime = parseInt(new Date().toLocaleTimeString().split(":")[0]) * 3600 + parseInt(new Date().toLocaleTimeString().split(":")[1]) * 60 + parseInt(new Date().toLocaleTimeString().split(":")[2]);
	if(!(currenttime >= starttime && currenttime <= endtime)){
			$("#gwShare_queryButton,#gwShare_reButto,#doAddButton,#exeButton,#gather_path0,#fileName").attr("disabled",true);
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
    
    var currentdate = date.getFullYear() + month + strDate;
    $("span[@name='currentDate']").text(currentdate);
		$("input[@name='gwShare_queryResultType']").val("checkbox");
	});
	
	var device_id="";
	var oui="";
	var device_serialnumber="";
	var city_id="";
	var loid="";
	function deviceResult(returnVal){
			
    	$("#fatherDiv div").each(function(i){
			//遍历配置	
			$("#gather_path"+i).val("");
			});
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
	  oui="";
		device_serialnumber="";
		loid="";
		city_id="";
		var deviceIdArray = returnVal[2];
		for(var i=0 ;i<deviceIdArray.length;i++){
			device_id +=  $.trim(deviceIdArray[i][0])+",";
			oui += $.trim(deviceIdArray[i][1])+",";
			device_serialnumber += $.trim(deviceIdArray[i][2])+",";
			city_id += $.trim(deviceIdArray[i][4])+",";
			loid += $.trim(deviceIdArray[i][11])+",";
		}
		var endIndex = device_id.lastIndexOf(",");
		device_id = device_id.substring(0,endIndex);
		endIndex = oui.lastIndexOf(",");
		oui = oui.substring(0,endIndex);
		endIndex = device_serialnumber.lastIndexOf(",");
		device_serialnumber = device_serialnumber.substring(0,endIndex);
		endIndex = city_id.lastIndexOf(",");
		city_id = city_id.substring(0,endIndex);
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
		// 判断未作策略的数量
		var url = "<s:url value='/gtms/config/getNodeBatchConfigAction!queryCustomNum.action'/>"; 
		var maxNum = 50000;
		$.post(url,{} ,function(ajax){
	          var num = parseInt(ajax);
	          if(num >= maxNum)
	           {
	           		alert("今天导入采集数据已达上限");
	           		$("#exeButton").attr("disabled",true);
		            return;
	           }
	    });
	}
	
	//点击增加的时候增加一组
	var flag = 0;
    function doAdd(){
    	if($("#fatherDiv div").size()>=4){
				alert("采集最多4个入参");
		   	return;
		   }
    	flag++ ;
    	//复制paramAddDiv0里的元素
		$("#fatherDiv").append($("#paramAddDiv0").clone(true).attr("id","paramAddDiv"+flag));
		$("#paramAddDiv"+flag).each(function(){
			//将克隆过来的输入框全部置空
			$(this).find("input[type='text']").val("");
			$(this).find("select[name='paramType0']").val("");
			//更改克隆过来div的各个输入框的id
			$(this).find("input[name='gather_path0']").attr("id","gather_path"+flag).attr("name","gather_path"+flag);
		});
	}
	//动态的删除删除掉一组,并且只删除最后一组
	function doDelete(){
		   if($("#fatherDiv div").size()>1){
		   		$("#fatherDiv div:last-child").remove();
		   }else{
		   		alert("这是最后一行，无法再删除");
		   		return;
		   }
		    flag--;
		}
		 var dateName;
		 $.ajaxSetup({  
    		async : false  
			}); 
    function checkForm(){
    	var condition = 0;
    	$("#fatherDiv div").each(function(i){
			//遍历配置	
			
			var gather_path = $("#gather_path"+i).val();
			if(gather_path==""){
				alert("第"+(i+1)+"个参数节点路径为空！");
				condition = 1;
					}
			if(gather_path.indexOf(";")>=0){
				alert("第"+(i+1)+"个参数节点路径输入框不可存在多条参数节点路径！");
				condition = 1;
			}
				});
				if(condition == 1){
					return false;
				}
				var currentDate = $("#currentDate").val();
        var fileName = $("#fileName").val();
        if(fileName==""){
        	alert("文件名不可为空！");
				return false;
        }
        var reg = /[\u4E00-\u9FA5\uF900-\uFA2D]/;
        if(reg.test(fileName)){
        	alert("文件名不支持中文!");
        	return false;
        }
   			
				
        var checkRepeatUrl = "<s:url value='/gtms/config/getNodeBatchConfigAction!checkRepeat.action'/>";
        var judge = "0";
				$.post(
	                checkRepeatUrl,{
	                checkRepeatname : dateName
	         			} ,function(ajax){
	         				if(ajax != "0"){
	         						alert("今天已存在此文件名的任务！");
	         						judge = ajax;
	         				}
	        		});
	        		if(judge != "0"){
	        			return false;
	        		}
	      return true;
    }
   
	//执行配置
	function doExecute(){
		var currentDate = $("#currentDate").text();
		 var fileName = $("#fileName").val();
		dateName = currentDate+""+fileName;
		
		$("#resultDIV").text();
		var url = "<s:url value='/gtms/config/getNodeBatchConfigAction!getConfig.action'/>"; 
	  // $("#doButton").attr("disabled",true);
	   var len = $("#fatherDiv div").size();
	   var gather_path = ""; 
	   $("#fatherDiv div").each(function(i){
			//遍历配置	
			gather_path += $("#gather_path"+i).val()+";";
					});
				var	endIndex = gather_path.lastIndexOf(";");
				gather_path = gather_path.substring(0,endIndex);
				if(checkForm()){
					var loidVal = loid.replace(/,/g, "");
					if(loidVal==""){
						loid="";
					}
					$.post(
	                url,{
	                gather_path : gather_path,
	                device_id : device_id,
	                oui : oui,
	                city_id : city_id,
	                device_serialnumber : device_serialnumber,
	                loid : loid,
	                file_name : dateName
	         } ,function(ajax){
	         	$("#resultDIV").text(ajax);
	         	$("#exeButton").attr("disabled","true");
	         	
	        });
        }
	}
	 	
	</script>
</html>
<%@ include file="../../foot.jsp"%>