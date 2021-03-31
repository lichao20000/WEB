<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="com.linkage.litms.system.*"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>采集LAN1任务定制</title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/JavaScript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/JavaScript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/JavaScript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<script type="text/JavaScript" src="<s:url value="/Js/commonUtil.js"/>"></script>
</head>
<script language="JavaScript">

	function queryChange(change){
		switch (change){
			case "2":
				$("input[@name='queryType']").val("2");
				$("input[@name='jiandan']").css("display","");
				$("input[@name='gaoji']").css("display","none");
				$("input[@name='import']").css("display","");
				
				$("th[@id='thTitle']").html("高 级 定 制");
				$("tr[@id='tr11']").css("display","none");
				$("tr[@id='tr12']").css("display","none");
				$("tr[@id='tr21']").css("display","");
				$("tr[@id='tr22']").css("display","");
				$("tr[@id='tr23']").css("display","");
				$("tr[@id='tr31']").css("display","none");
				$("tr[@id='tr32']").css("display","none");
				$("input[@name='queryButton']").val(" 定 制 ");
				
				//加载相关项
				change_select("city","-1");
				change_select("vendor","-1");
				
				break;
			case "3":
				$("input[@name='queryType']").val("3");
				$("input[@name='jiandan']").css("display","");
				$("input[@name='gaoji']").css("display","");
				$("input[@name='import']").css("display","none");
				
				$("th[@id='thTitle']").html("导 入 定 制");
				$("tr[@id='tr11']").css("display","none");
				$("tr[@id='tr12']").css("display","none");
				$("tr[@id='tr21']").css("display","none");
				$("tr[@id='tr22']").css("display","none");
				$("tr[@id='tr23']").css("display","none");
				$("tr[@id='tr31']").css("display","");
				$("tr[@id='tr32']").css("display","");
				$("input[@name='queryButton']").val("定 制");
				break;
			case "1":
				$("input[@name='queryType']").val("1");
				$("input[@name='jiandan']").css("display","none");
				$("input[@name='gaoji']").css("display","");
				$("input[@name='import']").css("display","");
				
				$("th[@id='thTitle']").html("单 台 查 询");
				$("tr[@id='tr11']").css("display","");
				$("tr[@id='tr12']").css("display","");
				$("tr[@id='tr21']").css("display","none");
				$("tr[@id='tr22']").css("display","none");
				$("tr[@id='tr23']").css("display","none");
				$("tr[@id='tr31']").css("display","none");
				$("tr[@id='tr32']").css("display","none");
				$("input[@name='queryButton']").val(" 采 集 ");
				break;
			default:
				break;
		}
	}
	
	/*------------------------------------------------------------------------------
	//函数名:		change_select
	//参数  :	type 
		            vendor      加载设备厂商
		            deviceModel 加载设备型号
		            deviceType  加载设备版本
	//功能  :	加载页面项（厂商、型号级联）
	//返回值:		
	//说明  :	
	//描述  :	Create 2009-12-25 of By qxq
	------------------------------------------------------------------------------*/
	function change_select(type,selectvalue){
		switch (type){
			case "city":
				var url = "<s:url value="/gwms/share/gwDeviceQuery!getCityNextChild.action"/>";
				$.post(url,{
				},function(ajax){
					parseMessage(type,ajax,$("select[@name='cityId']"),selectvalue);
				});
				break;
			case "nextCity":
				var city_id = $("select[@name='cityId']").val();
				if("-1"==city_id){
					$("select[@name='nextCityId']").html("<option value='-1'>==全部==</option>");
					break;
				}
				var url = "<s:url value="/gwms/share/gwDeviceQuery!getCityNextChildCore.action"/>";
				$.post(url,{
					gwShare_cityId:city_id
				},function(ajax){
					parseMessage(type,ajax,$("select[@name='nextCityId']"),selectvalue);
				});
				break;
			case "vendor":
				var url = "<s:url value="/gwms/share/gwDeviceQuery!getVendor.action"/>";
				$.post(url,{
				},function(ajax){
					parseMessage(type,ajax,$("select[@name='vendorId']"),selectvalue);
					$("select[@name='deviceModelId']").html("<option value='-1'>==请先选择设备厂商==</option>");
					$("select[@name='deviceTypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				});
				break;
			case "deviceModel":
				var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
				var vendorId = $("select[@name='vendorId']").val();
				if("-1"==vendorId){
					$("select[@name='deviceModelId']").html("<option value='-1'>==请先选择厂商==</option>");
					$("select[@name='deviceTypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
					break;
				}
				$.post(url,{
					gwShare_vendorId:vendorId
				},function(ajax){
					parseMessage(type,ajax,$("select[@name='deviceModelId']"),selectvalue);
					$("select[@name='deviceTypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				});
				break;
			case "deviceType":
				var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
				var vendorId = $("select[@name='vendorId']").val();
				var deviceModelId = $("select[@name='deviceModelId']").val();
				if("-1"==deviceModelId){
					$("select[@name='deviceTypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
					break;
				}
				$.post(url,{
					gwShare_vendorId:vendorId,
					gwShare_deviceModelId:deviceModelId
				},function(ajax){
					parseMessage(type,ajax,$("select[@name='deviceTypeId']"),selectvalue);
				});
				break;	
			default:
				alert("未知查询选项！");
				break;
		}	
	}
	
	/*------------------------------------------------------------------------------
	//函数名:		
	//参数  :	ajax 
	            	类似于XXX$XXX#XXX$XXX
	            field
	            	需要加载的jquery对象
	//功能  :	解析ajax返回参数
	//返回值:		
	//说明  :	
	//描述  :	Create 2009-12-25 of By qxq
	------------------------------------------------------------------------------*/
	//解析查询设备型号返回值的方法
	function parseMessage(type,ajax,field,selectvalue){
		var flag = true;
		if(""==ajax){
			return;
		}
		var lineData = ajax.split("#");
		if(!typeof(lineData) || !typeof(lineData.length)){
			return false;
		}
		field.html("");
		option = "<option value='-1' selected>==请选择==</option>";
		
		field.append(option);
		
		for(var i=0;i<lineData.length;i++){
			var oneElement = lineData[i].split("$");
			var xValue = oneElement[0];
			var xText = oneElement[1];
			if(selectvalue==xValue){
				flag = false;
				//根据每组value和text标记的值创建一个option对象
				option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
			}else{
				//根据每组value和text标记的值创建一个option对象
				option = "<option value='"+xValue+"'>=="+xText+"==</option>";
			}
			try{
				field.append(option);
			}catch(e){
				alert("设备型号检索失败！");
			}
		}
		if(flag){
			field.attr("value","-1");
		}
	}
	
	/*------------------------------------------------------------------------------
	//函数名:		重写reset
	//参数  :	change 1:简单查询、2:高级查询、3、导入查询
	//功能  :	对页面进行重置
	//返回值:		页面重置
	//说明  :	
	//描述  :	Create 2010-4-26 of By qxq
	------------------------------------------------------------------------------*/
	function revalue(){
		var queryType = $("input[@name='queryType']").val();
		if("1"==queryType){
			$("input[@name='queryField']").get(0).checked = true;
			$("input[@name='queryParam']").val("");
		}else if("2"==queryType){
			$("select[@name='cityId']").attr("value",'-1')
			$("select[@name='onlineStatus']").attr("value",'-1')
			$("select[@name='vendorId']").attr("value",'-1')
			change_select('deviceModel','-1');
		}
	}
	
	function do_work(){
		// 按钮灰化
		$("input[@name='queryButton']").attr("disabled", true);
		
		if(!do_test()){
			$("input[@name='queryButton']").attr("disabled", false);
			return;
		}
		setTimeout("queryDevice()", 2000);
	}
	
	//验证输入参数的长度是否合法
	function do_test()
	{
		var queryType = $("input[@name='queryType']").val();
		// 单台采集
		if(queryType == "1"){
			
			//获取输入框内容，trim一下
			var queryParam = $.trim($("input[@name='queryParam']").val());		
			if(0 == queryParam.length){
				alert("请输入采集参数！");
				$("input[@name='queryParam']").focus();
				return false;
			}
			
			//获取选择的类型
			var queryFields = document.getElementsByName("queryField");
			
			//"设备序列号"被选中
			if(queryFields[0].checked)
			{
				if(queryParam.length<6 && queryParam.length>0){
					alert("请至少输入最后6位设备序列号进行查询！");
					$("input[@name='queryParam']").focus();
					return false;
				}
			}//"设备IP"被选中
			else if(queryFields[1].checked)
			{
				if(!reg_verify(queryParam))
				{
					alert("请输入合法的IP地址！");
					$("input[@name='queryParam']").focus();
					return false;
				}
			}//"用户帐号"被选中   
			else if(queryFields[2].checked)
			{
				if(queryParam.length<6 && queryParam.length>0){
					alert("请至少输入最后6位LOID进行查询！");
					$("input[@name='queryParam']").focus();
					return false;
				}
			}
		}
		else if(queryType == "2"){
			
			var cityId = $("select[@name='cityId']").val();
			var nextCityId = $("select[@name='nextCityId']").val();
			if("-1" == nextCityId && ("-1" == cityId || "00" == cityId)){
				alert("至少选择一个具体属地！");
				return false;
			}
		}
		else if(queryType == "2"){
			var fileName = $("input[@name='gwShare_fileName']").val();
			if("" == fileName){
				alert("请先上传文件！");
				return false;
			}
			if ("xls" != fileName[fileName.length - 1] && "XLS" != fileName[fileName.length-1] && 
					"txt" != fileName[fileName.length-1] && "TXT" != fileName[fileName.length-1]) {
				valert("仅支持后缀为xls或txt的文件");
				return false;
			}
		}
		return true;
	}
	
	/* reg_verify - 完全用正则表达式来判断一个字符串是否是合法的IP地址，
	如果是则返回true，否则，返回false。*/
	function reg_verify(addr)
	{
		//正则表达式
	    var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/;

	    if(addr.match(reg)){
	    	//IP地址正确
	        return true;
	    }else{
	    	//IP地址校验失败
	         return false;
	    }
	}
	
	/*------------------------------------------------------------------------------
	//函数名:		queryChange
	//参数  :	change 1:单台采集、2:高级定制
	//功能  :	根据传入的参数调整显示的界面
	//返回值:		调整界面
	//说明  :	
	------------------------------------------------------------------------------*/
	function queryDevice(){
		var	width=800;    
		var height=450; 
		var queryType = $("input[@name='queryType']").val();
		
		// 单台采集
		if("1" == queryType){
			
			// 校验设备是否存在
			var url="<s:url value='/gwms/resource/superGatherLanTask!validateSingleGather.action'/>";
			
			$.post(url,{
				queryField:$("input[@name='queryField'][@checked]").val(),
				queryParam:$.trim($("input[@name='queryParam']").val())
			},function(ajax){
				if("empty" == ajax){
					$("input[@name='queryButton']").attr("disabled", false);
					alert("查询不到设备信息，请重新输入！");
					return;
				}
				
				if("many" == ajax){
					$("input[@name='queryButton']").attr("disabled", false);
					alert("根据条件可以查询到多条设备信息，请重新输入！");
					return;
				}
				
				var url1="<s:url value='/gwms/resource/superGatherLanTask!singleSuperGather.action'/>?deviceId=" + ajax;
				window.showModalDialog(url1 ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
				$("input[@name='queryButton']").attr("disabled", false);
			});
		}
		
		// 高级定制
		if("2" == queryType){
			var url="<s:url value='/gwms/resource/superGatherLanTask!createTask.action'/>";
			var cityId = "";
			var preCityId = $("select[@name='cityId']").val();
			var nextCityId = $("select[@name='nextCityId']").val();
			
			cityId = nextCityId;
			if("-1" == nextCityId){
				cityId = preCityId;
			}
			
			var vendorId = $("select[@name='vendorId']").val();
			var deviceModelId = $("select[@name='deviceModelId']").val();
			var deviceTypeId = $("select[@name='deviceTypeId']").val();
			var onlineStatus = $("select[@name='onlineStatus']").val();
			$.post(url,{
				cityId:cityId,
				vendorId:vendorId,
				deviceModelId:deviceModelId,
				deviceTypeId:deviceTypeId,
				onlineStatus:onlineStatus
			},function(ajax){
				// 定制失败
				if("" != ajax){
					$("input[@name='queryButton']").attr("disabled", false);
					alert(ajax);
					return;
				}				
				alert("任务定制成功！");
				// 定制成功，转向结果页面
				var url1 = "<s:url value='/gwms/resource/SuperGatherLanTaskQuery.jsp'/>";
				window.open(url1,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
				$("input[@name='queryButton']").attr("disabled", false);
				//window.showModalDialog(url1 ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
			});
			
		}
		
		// 导入定制
		if("3" == queryType){
			var url="<s:url value='/gwms/resource/superGatherLanTask!createFileTask.action'/>";
			var fileName = $("input[@name='gwShare_fileName']").val();
			$.post(url,{
				fileName:fileName
			},function(ajax){
				// 定制失败
				if("" != ajax){
					$("input[@name='queryButton']").attr("disabled", false);
					alert(ajax);
					return;
				}
				alert("任务定制成功！");
				// 定制成功，转向结果页面
				var url1 = "<s:url value='/gwms/resource/SuperGatherLanTaskQuery.jsp'/>";
				window.open(url1,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
				$("input[@name='queryButton']").attr("disabled", false);
				//window.showModalDialog(url1 ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
			});
		}
	}
</script>

<form name="selectForm" action="" target="dataForm">

<input type="hidden" name="queryType" value="1" />
<table border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
	<tr>
		<td bgcolor=#999999>
			<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center">
				<tr><th colspan="6" id="thTitle" style="display:">单 台 采 集</th></tr>
				<tr bgcolor="#FFFFFF" id="tr11" style="display:">
					<td colspan="6" align="center" width="100%">
						<div>
							<input class="bk" name="queryParam" size="64" maxlength="64"/>
						</div>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF" id="tr12" style="display:">
					<td colspan="6" align="center" width="100%">
						<input type="radio" name="queryField" value="deviceSn" checked /> 设备序列号 &nbsp;&nbsp;
						<input type="radio" name="queryField" value="deviceIp"/> 设备IP &nbsp;&nbsp;
						<input type="radio" name="queryField" value="username"/> LOID &nbsp;&nbsp;
						<input type="radio" name="queryField" value="kdname"/> 宽带账号 &nbsp;&nbsp;
						<input type="radio" name="queryField" value="voipPhoneNum"/> VOIP电话号码 &nbsp;&nbsp;
					</td>
				</tr>
				<tr bgcolor="#ffffff" id="tr21" style="display:none">
					<td align="right" class=column width="15%">属    地</td>
					<td align="left" width="35%">
						<div style="float: left;">
							<select name="cityId" class="bk" onchange="change_select('nextCity','-1')">
								<option value="-1">==全部==</option>
							</select>
						</div>
						
					</td>
					<td align="right" class=column width="15%" >下级属地</td>
					<td align="left" width="35%"  colspan="3">
						<div style="float: left;" id="nextCityDiv">
							<select name="nextCityId" class="bk" >
								<option value="-1">==全部==</option>
							</select>
						</div>
					</td>
				</tr>
				<tr bgcolor="#ffffff" id="tr22" style="display:none">
					<td align="right" class=column width="15%">厂    商</td>
					<td width="35%">
						<select name="vendorId" class="bk" onchange="change_select('deviceModel','-1')">
							<option value="-1">==请选择==</option>
						</select>
					</td>
					<td align="right" class=column width="15%" >设备型号</td>
					<td align="left" width="35%"  colspan="3">
						<select name="deviceModelId" class="bk" onchange="change_select('deviceType','-1')">
							<option value="-1">请先选择厂商</option>
						</select>
					</td>
				</tr>
				<tr bgcolor="#ffffff" id="tr23"  style="display:none">	
					<td align="right" class=column width="15%">设备版本</td>
					<td width="35%">
						<select name="deviceTypeId" class="bk"">
							<option value="-1">请先选择设备型号</option>
						</select>
					</td>	
					<td align="right" class=column width="15%" >上线状态</td>
					<td width="35%"  colspan="3">
						<select name="onlineStatus" class="bk">
							<option value="-1">==全部==</option>
							<option value="0">下线</option>
							<option value="1">在线</option>
						</select>
					</td>
				</tr>
				<tr id="tr31" bgcolor="#ffffff"  style="display:none">
					<td align="right" width="15%">提交文件</td>
					<td colspan="5" width="85%">
						<div id="importusername">
							<iframe name="loadform" frameborder=0 scrolling=no src="<s:url value="/gwms/share/FileUpload.jsp"/>" height="30" width="100%">
							</iframe>
							<input type="hidden" name="gwShare_fileName" value=""/>
						</div>
					</td>
				</tr>
				<tr id="tr32" style="display:none">
					<td class="green_foot" align="right">注意事项</td>
					<td colspan="5" class="green_foot">
						1、需要导入的文件格式限于xls、文本文件，即txt格式 。
						 <br>
						2、文件的第一行为标题行，即【用户账号】、或者【设备序列号】。
						 <br>
						3、文件只有一列。
						 <br>
						4、文件行数不要太多，以免影响性能。
					</td>
				</tr>
				<tr bgcolor="#ffffff">
					<td colspan="6" align="right" class="green_foot" width="100%">
						<input type="button" style="cursor:pointer;display: none" onclick="javascript:queryChange('1');" name="jiandan" value="单台采集" />
						<input type="button" style="cursor:pointer;" onclick="javascript:queryChange('2');" name="gaoji" value="高级定制" />						
						<input type="button" style="cursor:pointer;" onclick="javascript:queryChange('3');" name="import" value="导入定制" />
						<input type="button" onclick="javascript:do_work()" name="queryButton" value=" 采 集 " />
						<input type="button" onclick="javascript:revalue()"name="rebutto" value=" 重 置 " />
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</form>