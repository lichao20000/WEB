<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String gwType = request.getParameter("gw_type");
 %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>批量配置修改参数节点</title>
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
											批量配置修改参数节点
										</td>
										<td align="right">
											<button type="button" id="doAddButton"
													onclick="doAdd();" class=btn>
															&nbsp;增&nbsp;加 &nbsp;
											</button>
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
												配置    策略
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
																	<a href="javascript:doDelete()">
																		删除    |
																	 </a>
																</span>
																参数节点路径
															</td>
															<td width="70%" colspan="3" >
																<input type="text" id="paramNodePath0" name="paramNodePath0" style="width:800px;"> 
															</td>
														</TR>
														<TR bgcolor="#FFFFFF">
															<td  align="right" class=column width="20%">
																参数值
															</td>
															<td  width="30%" >
																<input type="text" id="paramValue0" name="paramValue0" value=""/>
															</td>
															<td  align="right" class=column width="30%">
																参数类型
															</td>
															<td  width="70%" >
																<select name="paramType0" id="paramType0" cssClass="bk">
																 	<option value="-1">==请选择==</option>
																 	<option value="1">string</option>
																 	<option value="2">int</option>
																 	<option value="3">unsignedInt</option>
																 	<option value="4">boolean</option>
																 </select>
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
		gwShare_setImport();
		$("input[@name='gwShare_queryResultType']").val("checkbox");
	});
	
	function deviceResult(returnVal){

		$("#exeButton").attr("disabled",false);
		deviceIds ="";
		$("table[@id='table_showConfig']").css("display","");
		$("#resultDIV").html("");
		var totalNum = returnVal[0];
		if(returnVal[0]==0){
			totalNum = returnVal[2].length;
			$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[2].length+"</strong></font>");
			if(returnVal[2].length > 500)
			{
				alert("一次导入设备或者用户超过500，不予执行");
           		$("#exeButton").attr("disabled",true);
	            return;
			}
			for(var i=0;i<returnVal[2].length;i++){
				deviceIds = deviceIds + returnVal[2][i][0]+",";			
			}
		}else{
			$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[0]+"</strong></font>");
			deviceIds = "0";
			param=returnVal[1];
		}
		// 判断未作策略的数量
		/**var url = "<s:url value='/gtms/config/paramNodeBatchConfig4QAAction!queryUndoNum.action'/>"; 
		var maxNum = 50000;
		$.post(url,{} ,function(ajax){
	          var num = parseInt(ajax);
	           if(num > maxNum)
	           {
	           		alert("今天配置数已达到上限，请明日再配置！");
	           		$("#exeButton").attr("disabled",true);
		            return;
	           }
	    });**/
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
    function checkForm(){
       $("#fatherDiv div").each(function(i){
			//遍历配置	
			var tempPath = $("#paramNodePath"+i).val();
			var tempValue = $("#paramValue"+i).val();
			var tempType = $("#paramType"+i).val();
			if(tempPath==""){
				alert("第"+(i+1)+"个参数节点路径为空！");
				return false;
			}
			if(tempValue==""){
				alert("第"+(i+1)+"个参数值为空！")
				return false;
			}
			if(tempType==""){
				alert("第"+(i+1)+"个参数类型为空！")
				return false;
			}
        });
        return true;
    }
	//执行配置
	function doExecute(){
	
	   var url = "<s:url value='/gtms/config/paramNodeBatchConfig4QAAction!doConfigAll.action'/>"; 
	  // $("#doButton").attr("disabled",true);
	   var len = $("#fatherDiv div").size();
	   var gwType = <%=gwType%>;
	   
	   var paramNodePath = ""; 
	   var paramValue = "";
	   var paramType = "";
		$("#fatherDiv div").each(function(i){
			//遍历配置	
			paramNodePath += "," + $("#paramNodePath"+i).val();
			paramType     += "," + $("#paramType"+i).val();
			paramValue    += "," + $("#paramValue"+i).val();
        });
        if(checkForm()){
	        $.post(
	                url,{
	                deviceIds : deviceIds,
	                param:param,
	                paramNodePath : paramNodePath,
	                paramValue : paramValue,
	                paramType : paramType,
	                gw_type : gwType
	         } ,function(ajax){
	                $("#resultDIV").html("");
	                $("#doButton").attr("disabled",false);
	                if("1"==ajax){
	                      $("#resultDIV").append("后台执行成功");
	                      return;
	                 }
	                if ("-2"==ajax){
	                      $("#resultDIV").append("一次导入设备或者用户超过500，不予执行");
	                 }
	                else{
	                     $("#resultDIV").append("后台执行失败");
	                 }
	        });
        }
	}
	 	
	</script>
</html>
<%@ include file="../../foot.jsp"%>