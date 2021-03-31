<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../timelater.jsp"%>
<%
	String gwType = request.getParameter("gw_type");
	String tempdpi = request.getParameter("dpi");
	String isShowGJ = request.getParameter("isShowGJ");
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK">
	<%if("true".equals(request.getParameter("dpi"))){%><title>dpi配置下发</title><%}else{ %><title>机顶盒批量参数配置</title><%}%>
	<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
	<link href="../../css/listview.css" rel="stylesheet" type="text/css">
	<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
	<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckForm.js"></SCRIPT>
	<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<%@ include file="../../toolbar.jsp"%>
	<input type="hidden" name="instArea" value="<s:property value="instArea"/>" />
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
		<TR><TD HEIGHT=20>&nbsp;</TD></TR>
		<TR>
			<TD>
				<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION="" target="dataForm">
				<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
				align="center">
					<tr>
						<td>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<tr>
									<td width="162" align="center" class="title_bigwhite" nowrap>
										批量参数下发
									</td>
									<td align="right">
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<TR bgcolor="#FFFFFF">
						<td colspan="4">
							<%@ include file="/gwms/share/gwShareDeviceQuery_newBatchSet.jsp"%>
						</td>
					</TR>
					<tr>
						<TD bgcolor=#000000>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<tr>
									<th align="center" width="100%" colspan="4">配置 模板</th>
								</tr>
								<tr style="cursor: hand; background-color: #cccccc">
									<TD class="column text" nowrap align="center" width="15%" colspan="4">
										<div id="selectedDev">
											请查询设备！
										</div>
									</td>
								</tr>
								<tr style="cursor: hand; background-color: #cccccc">
									<TD class="column text" nowrap align="center" width="15%">任务名称</TD>
									<TD colspan="1" class="column text" nowrap width="35%">
										<INPUT TYPE="text" style="font-size: 12px;" id="task_name" name="task_name" size=15>&nbsp;<font color="#FF0000"></font>
									</TD>
									<TD class="column text" nowrap align="center" width="15%">策略触发方式</TD>
									<TD colspan="1" class="column text" nowrap width="35%">
										<input type="checkbox" name="dotype" class="bk" value="2"><span>周期上报</span>
										<input type="checkbox" name="dotype" class="bk" value="5"><span>重新启动</span>
										<input type="checkbox" name="dotype" class="bk" value="4"><span>下次连接到系统</span>
										<input type="checkbox" name="dotype" class="bk" value="6"><span>参数改变</span>
										<input type="checkbox" name="donow" checked="checked" class="bk" value="1"><span>主动触发</span>
									</TD>
								</tr>
								<tr style="cursor: hand; background-color: #cccccc">
									<TD class="column text" nowrap align="center" width="15%">模板名称</TD>
									<TD colspan="1" class="column text" nowrap width="35%">
										<INPUT TYPE="text" style="font-size: 12px;" id="name" name="name" size=15 value="<s:property value='name'/>">&nbsp;<font color="#FF0000"></font>
									</TD>
									<TD class="column text" nowrap align="center" width="15%">
										<INPUT TYPE="button" style="font-size: 12px;" size=15 value="过滤模板" onclick="loadSelect()">
									</TD>
									<TD colspan="1" class="column text" nowrap width="35%">
										<select name="template" id="template" style="color: black;font-size: 12px;height:24px;">
										<option value='-1' style="height: 24px;">请选择</option>
										</select>
									</TD>
								</tr>
								<tr style="cursor: hand; background-color: #F4F4FF">
									<td align="center" width="15%">
										触发开始日期：
									</td>
									<td align="left" width="30%">
										<input type="text"   name="expire_date_start" value="<s:property value='expire_date_start'/>" class=bk >
											<img name="shortDateimg"
						 						onClick="WdatePicker({el:document.mainForm.expire_date_start,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
						 							src="../../images/dateButton.png" width="15" height="12"
						 								border="0" alt="选择">
									</td>

									<td align="center" width="15%">
										触发结束日期：
									</td>
									<td align="left" width="30%">
										<input type="text"  name="expire_date_end" value="<s:property value='expire_date_end'/>" class=bk >
											<img name="shortDateimg"
						 						onClick="WdatePicker({el:document.mainForm.expire_date_end,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
						 							src="../../images/dateButton.png" width="15" height="12"
						 								border="0" alt="选择">
									</td>
								</tr>
								<tr style="cursor: hand; background-color: #F4F4FF">
									<td align="center" width="15%">
										触发开始时间：
									</td>
									<td align="left" width="30%">
										<input type="text"   name="expire_time_start" value="<s:property value='expire_time_start'/>" class=bk >
											<img name="shortDateimg"
						 						onClick="WdatePicker({el:document.mainForm.expire_time_start,dateFmt:'HH:mm:ss',skin:'whyGreen'})"
						 							src="../../images/dateButton.png" width="15" height="12"
						 								border="0" alt="选择">
									</td>

									<td align="center" width="15%">
										触发结束时间：
									</td>
									<td align="left" width="30%">
										<input type="text"  name="expire_time_end" value="<s:property value='expire_time_end'/>" class=bk >
											<img name="shortDateimg"
						 						onClick="WdatePicker({el:document.mainForm.expire_time_end,dateFmt:'HH:mm:ss',skin:'whyGreen'})"
						 							src="../../images/dateButton.png" width="15" height="12"
						 								border="0" alt="选择">
									</td>
								</tr>
								<!-- <tr style="cursor: hand; background-color: #cccccc">
									<TD class="column text" nowrap align="center" width="15%">尝试主动触发</TD>
									<TD colspan="3" class="column text" nowrap width="35%">
										<input type="checkbox" name="donow" checked="checked" class="bk" value="1">
									</TD>
								</tr> -->
								<TR>
									<TD colspan="4" align="right" class=foot style="vertical-align: middle;"><input TYPE="button" value="定制" style="height: 22px;font-size: 13px;" onclick="batchSet('<s:property value="id" />')"> </TD>
								</TR>
							</TABLE>
						</td>
					</tr>
				</table>
				</FORM>
			</TD>
		</TR>
	</TABLE>
</body>

<script type="text/javascript">
	var deviceIds = "";
	var param = "";
	var isShowGJ = "1";
	$(function(){
		if("1"==isShowGJ){
			gwShare_setGaoji();
			setIsGJ(isShowGJ);
		}
		$("input[@name='gwShare_queryResultType']").val("checkbox");
		gwShare_setImport();
		loadSelect();
	});
	
	function deviceResult(returnVal){
		$("#exeButton").attr("disabled",false);
		deviceIds ="";
		$("table[@id='table_showConfig']").css("display","");
		$("#resultDIV").html("");
		var totalNum = returnVal[0];
		device_id = returnVal[3];
		if(returnVal[0]==0){
			totalNum = returnVal[2].length;
			if(5000<totalNum && "nx_dx"!=returnVal[3]){
				alert("单次配置设备数应小于5000台！");
	       		$("#exeButton").attr("disabled",true);
	            return;
			}
			$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[2].length+"</strong></font>");
			deviceCount = returnVal[2].length;
			for(var i=0;i<returnVal[2].length;i++){
				deviceIds = deviceIds + returnVal[2][i][0]+",";			
			}
		}else{
			$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[0]+"</strong></font>");
			deviceCount = returnVal[0];
			deviceIds = "0";
			param=returnVal[1];
		}

		if(5000<returnVal[0] && "nx_dx"!=returnVal[3]){
			alert("单次配置设备数应小于5000台！");
       		$("#exeButton").attr("disabled",true);
            return;
		}
		
		if("nx_dx"!=returnVal[3]){
			// 判断未作策略的数量
			var url = "<s:url value='/gtms/config/paramNodeBatchConfigAction!queryUndoNum.action'/>"; 
			var maxNum = 50000;
			$.post(url,{} ,function(ajax){
		          var num = parseInt(ajax);
		           if(num > maxNum){
		           		alert("今天配置数已达到上限，请明日再配置！");
		           		$("#exeButton").attr("disabled",true);
			            return;
		           }
		    });
		}
	}
	
	//点击增加的时候增加一组
	var flag = 0;
    function doAdd(){
    	flag++ ;
    	//复制paramAddDiv0里的元素
		$("#fatherDiv").append($("#paramAddDiv0").clone(true).attr("id","paramAddDiv"+flag));
		$("#paramAddDiv"+flag).each(function(){
			//将克隆过来的输入框全部置空
			$(this).find("input[type='text']").val("");
			$(this).find("select[name='paramType0']").val("");
			//更改克隆过来div的各个输入框的id
			$(this).find("input[name='paramNodePath0']").attr("id","paramNodePath"+flag).attr("name","paramNodePath"+flag);
			$(this).find("input[name='paramValue0']").attr("id","paramValue"+flag).attr("name","paramValue"+flag);
			$(this).find("select[name='paramType0']").attr("id","paramType"+flag).attr("name","paramType"+flag);
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
	
    
    
	//执行配置
	function doExecute(){
		alert(deviceCount+device_id+queryTypeNew+$("#gwShare_queryParam").val());
		return;
	   var url = "<s:url value='/gtms/config/paramNodeBatchConfigAction!doConfigAll.action'/>"; 
	  // $("#doButton").attr("disabled",true);
	   var len = $("#fatherDiv div").size();
	   var gwType =<%=gwType%>;
	   var dpi=<%=tempdpi%>;
	   var paramNodePath = ""; 
	   var paramValue = "";
	   var paramType = "";
		$("#fatherDiv div").each(function(i){
			//遍历配置	
			paramNodePath += "," + $("#paramNodePath"+i).val();
			paramValue    += "," + $("#paramValue"+i).val();
			paramType     += "," + $("#paramType"+i).val();
        });

		if(checkForm()){
	        $.post(
	                url,{
	                deviceIds : deviceIds,
	                param:param,
	                paramNodePath : paramNodePath,
	                paramValue : paramValue,
	                paramType : paramType,
	                dpi : dpi,
	                gw_type : gwType
	         } ,function(ajax){
	                $("#resultDIV").html("");
	                $("#doButton").attr("disabled",false);
	                if("1"==ajax){
	                      $("#resultDIV").append("后台执行成功");
	                 }else{
	                     $("#resultDIV").append("后台执行失败");
	                 }
	        });
        }
	}
	
	function loadSelect(){
		$.ajax({
    	    type:"POST",
    	    url:"<s:url value='/gwms/resource/servTemplate!queryTemplateStr.action' />",
    	    data: {"name":$("#name").val()},
    	    success:function (ajax) {
    	    	var template = $("#template");
    			template.html("");
    			option = "<option value='-1' selected style='height: 24px;'>请选择</option>";
    			template.append(option);
    			if(""==ajax){
    				return false;
    			}
    			
    			var lineData = ajax.split("#");
    			if(!typeof(lineData) || !typeof(lineData.length)){
    				return false;
    			}
    			
    			for(var i=0;i<lineData.length;i++){
    				var oneElement = lineData[i].split("$");
    				var xValue = oneElement[0];
    				var xText = oneElement[1];
    				//根据每组value和text标记的值创建一个option对象
    				option = "<option value='"+xValue+"' style='height: 24px;'>"+xText+"</option>";
    				try{
    					template.append(option);
    				}catch(e){
    					alert("模板加载失败！");
    				}
    			}
    	    }
    	});
	}
	
	/*批量定制提交*/
	function batchSet(){
		/* var deviceCount = "0";
		var queryTypeNew = "";
		var device_id = ""; */
	    var templateId = $("#template").val();
	    if(templateId=="-1"){
	    	alert("请选择模板");
	    	return false;
	    }
	    if('' == $("#task_name").val()){
	    	alert("任务名称不可为空");
	    	return false;
	    }
	    if('0' == deviceCount){
	    	alert("请选择设备");
	    	return false;
	    }
	    
	    var starttime = $("input[@name='expire_time_start']").val();
	    var endtime = $("input[@name='expire_time_end']").val();
	    var startdate = $("input[@name='expire_date_start']").val();
	    var enddate = $("input[@name='expire_date_end']").val();
	    
	    if(enddate<startdate || starttime>endtime){
	    	alert("触发开始日期/时间小于触发结束日期/时间");
	    	return false;
	    }
	    	
	    	
	    var obj = $("input[@name='donow']");
        var donow = '0';
        for(k in obj){
            if(obj[k].checked)
            	donow = "1";
        }
        
	    obj = $("input[@name='dotype']");
        var check_val = '';
        for(k in obj){
            if(obj[k].checked)
            	check_val = check_val + obj[k].value;
        }
        
	    if(check_val == '')
	    {
	    	alert("请选择策略触发方式");
	    	return false;
	    }
	    else{
	    	var url = "<s:url value='/gtms/config/paramNodeBatchConfigAction!addTemplateTaskStb.action' />?task_name="+$("#task_name").val()+"&template_id="+templateId+"&dotype="+check_val+"&starttime="+startdate+" "+starttime+"&endtime="+enddate+" "+endtime+"&donow="+donow;
	    	if("1"==queryTypeNew){
	    		var gwShare_queryField = $("input[@name='gwShare_queryField'][@checked]").val();
	    		var gwShare_queryParam = $.trim($("input[@name='gwShare_queryParam']").val());
	    		url = url + "&gwShare_queryType=1";
	    		url = url + "&device_id=" + device_id;
	    	}else if("2"==queryTypeNew){
	    		var gwShare_nextCityId = $.trim($("select[@name='gwShare_nextCityId']").val());
	    	 	var gwShare_cityId = $.trim($("select[@name='gwShare_cityId']").val());
	            var gwShare_vendorId = $.trim($("select[@name='gwShare_vendorId']").val());
	            var gwShare_deviceModelId = $.trim($("select[@name='gwShare_deviceModelId']").val());
	            var gwShare_devicetypeId = $.trim($("select[@name='gwShare_devicetypeId']").val());
	            var gwShare_bindType = $.trim($("select[@name='gwShare_bindType']").val());
	            url = url + "&gwShare_cityId=" + gwShare_cityId;
	            url = url + "&gwShare_nextCityId=" + gwShare_nextCityId;
	            url = url + "&gwShare_vendorId=" + gwShare_vendorId;
	            url = url + "&gwShare_deviceModelId=" + gwShare_deviceModelId;
	            url = url + "&gwShare_devicetypeId=" + gwShare_devicetypeId;
	            url = url + "&gwShare_bindType=" + gwShare_bindType;
	            url = url + "&gwShare_queryType=2";
	    	}else {
	    		var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
	    		if(""==gwShare_fileName){
	    			alert("请先上传文件！");
	    			return;
	    		}
	    		url = url + "&gwShare_fileName=" + gwShare_fileName;
	    		url = url + "&gwShare_queryType=3";
	    	}
	    	
	    	$.ajax({
	    	    type:"POST",
	    	    async:false,
	    	    url:url,
	    	    dataType:"text",
	    	    data: {},
	    	    success:function (data) {
	    	    	if(data==1){
	    	    		alert("定制成功");
	    	    		window.location.reload();
	    	    	}
	    	    	else
	    	 		{
	    	 			alert("定制失败");
	    	 		}
	    	    }
	    	});
	    }
	}
</script>
</html>
<%@ include file="../../foot.jsp"%>