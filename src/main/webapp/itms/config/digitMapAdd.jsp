<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%
	String action = request.getParameter("action");
	String map_id = request.getParameter("map_id");
	String vendor_id = request.getParameter("vendor_id");
	String device_model_id = request.getParameter("device_model_id");
	String devicetype_id = request.getParameter("devicetype_id");
%>
<html>
	<head>
	    <title>新增数图配置模板</title>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
	</head>
	
	<script type="text/javascript">
		var add_upd = "";
		function Init(action){
			add_upd = action;
			// 初始化设备厂商
			if(action=="add"){// 添加
				gwShare_change_select("vendor","-1","");
			}else{
				gwShare_change_select("vendor","<%=vendor_id%>","");
				gwShare_change_select("deviceModel","<%=vendor_id%>","<%=device_model_id%>");
				gwShare_change_select("devicetype","<%=device_model_id%>","<%=devicetype_id%>");
			}
		}

		/*------------------------------------------------------------------------------
		//函数名:		deviceSelect_change_select
		//参数  :	type 
			            vendor      加载设备厂商
			            deviceModel 加载设备型号
			            devicetype  加载设备版本
		//功能  :	加载页面项（厂商、型号级联）
		//返回值:		
		//说明  :	
		//描述  :	Create 2009-12-25 of By qxq
		------------------------------------------------------------------------------*/
		function gwShare_change_select(type,selectvalue,selectvalue_upd){
			switch (type){
				case "city":
					var url = "<s:url value="/gwms/share/gwDeviceQuery!getCityNextChild.action"/>";
					$.post(url,{
					},function(ajax){
						gwShare_parseMessage(ajax,$("select[@name='gwShare_cityId']"),selectvalue);
					});
					break;
				case "vendor":
					var url = "<s:url value="/gwms/share/gwDeviceQuery!getVendor.action"/>";
					$.post(url,{
					},function(ajax){
						gwShare_parseMessage(ajax,$("select[@name='gwShare_vendorId']"),selectvalue);
						$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==请先选择设备厂商==</option>");
						$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
					});
					break;
				case "deviceModel":
					var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
					var vendorId = $("select[@name='gwShare_vendorId']").val();
					if(add_upd == "update" && selectvalue_upd != ''){  // 更新
						vendorId = selectvalue;
					}
					if("-1"==vendorId){
						$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==请先选择厂商==</option>");
						$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
						break;
					}
					$.post(url,{
						gwShare_vendorId:vendorId
					},function(ajax){
						if(add_upd == "update"){  // 更新
							gwShare_parseMessage(ajax,$("select[@name='gwShare_deviceModelId']"),selectvalue_upd);
						}else{ // 新增
							gwShare_parseMessage(ajax,$("select[@name='gwShare_deviceModelId']"),selectvalue);
							$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
						}
						if(selectvalue_upd == ''){
							$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
						}
					});
					break;
				case "devicetype":
					var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
					var vendorId = $("select[@name='gwShare_vendorId']").val();
					var deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
					if(add_upd == "update" && selectvalue_upd != ''){  // 更新
						deviceModelId = selectvalue;
					}
					if("-1"==deviceModelId){
						$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
						break;
					}
					$.post(url,{
						gwShare_vendorId:vendorId,
						gwShare_deviceModelId:deviceModelId
					},function(ajax){
						if(add_upd == "update"){  // 更新
							gwShare_parseMessage(ajax,$("select[@name='gwShare_devicetypeId']"),selectvalue_upd);
						}else{  // 新增
							gwShare_parseMessage(ajax,$("select[@name='gwShare_devicetypeId']"),selectvalue);
						}
					});
					break;	
				default:
					alert("未知查询选项！");
					break;
			}	
		}
		
		/*------------------------------------------------------------------------------
		//函数名:		deviceSelect_parseMessage
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
		function gwShare_parseMessage(ajax,field,selectvalue){
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
	</script>

	<body onLoad="init()">
		<TABLE id="addTable">

			<tr>
				<td HEIGHT=20>&nbsp;
					
				</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								数图配置模板管理
							</td>
							<td>
								<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12" />
								<font color="#FF0000">注：模板名称、设备属地、设备厂商、设备型号为必填项</font>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
			<TR>
				<TD>
					<TABLE class="querytable" width="100%">
						<TR>
							<th colspan="4" id="page_title">
							</th>
						</TR>
						<TR>
							<TD class=column width="15%">
								模板名称：
							</TD>
							<TD width="35%">
								<!--  xxx 可以使用struts标签 -->
								<input type="text" name="map_name" id="map_name" value='<s:property value="digitMap.map_name" />' maxlength="100" />
							</TD>
							<TD class=column width="15%">
								设备属地：
							</TD>
							<TD width="35%">
								<s:select list="cityList" name="cityId" headerKey="-1"
									headerValue="请选择属地" listKey="city_id" listValue="city_name"
									value="digitMap.city_id" cssClass="bk"></s:select>
							</TD>
						</TR>
						<TR>
							<TD class=column>
								设备厂商：
							</TD>
							<TD>
								<select name="gwShare_vendorId" class="bk" onchange="gwShare_change_select('deviceModel','-1','')">
									<option value="-1">==请选择==</option>
								</select>&nbsp;
							</TD>
							<TD class=column>
								设备型号：
							</TD>
							<TD>
								<select name="gwShare_deviceModelId" class="bk" onchange="gwShare_change_select('devicetype','-1','')">
									<option value="-1">请先选择厂商</option>
								</select>&nbsp;
							</TD>
						</TR>
						<TR>
							<TD class=column>
								软件版本：
							</TD>
							<TD >
								<select name="gwShare_devicetypeId" class="bk"">
									<option value="-1">请先选择设备型号</option>
								</select>&nbsp;
							</TD>
							<TD class=column>
								是否默认：
							</TD>
							<TD >
								<select name="is_default" class="bk"" value ="1">
									<option value="0">是</option>
									<option value="1">否</option>
								</select>&nbsp;
							</TD>
						</TR>
						<TR>
							<TD class=column>
								模板参数内容
							</TD>
							<TD colspan="3">
								<textarea name="map_content" id="map_content" rows="10" cols="100"><s:property value='digitMap.map_content' /></textarea>
								<font color="#FF0000">* 最多不能超过1024个字符，且不可以输入中文字符。</font>
							</TD>
						</TR>
						<tr>
							<td colspan=4 class=foot align="right">
								<button onClick="save()" id="save_button"></button>
							</td>
						</tr>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
	</body>

	<script>
		
		var action = "<%=action%>";
		var map_id = "<%=map_id%>";
		
		Init(action);
		
		function init(){
			if(action == "add"){
				page_title.innerHTML = "新增模板";
				save_button.innerHTML = "新增保存";
			}else{
				page_title.innerHTML = "编辑模板";
				save_button.innerHTML = "编辑保存";
			}
		}
		
		function save(){
			var map_name = $.trim($("input[@id='map_name']").val());
			var map_content = document.getElementById("map_content");
			
			if(map_name ==""){		
				alert("模板名称不能为空！");
				$("input[@id='map_name']").focus();
				$("input[@id='map_name']").select();
				return;
			}
			if($.trim(map_content.value)==""){
				alert("模板内容不能为空！");
				map_content.focus();
				map_content.select();
				return;
			}
			var cityId = $("select[@name='cityId']").val();
			if(cityId==""||cityId=="-1"){
				alert("请选择属地!");
				return false;
			}
			
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			if(vendorId == "" || vendorId == "-1"){
				alert("请选择设备厂商！");
				return false;
			}
			
			var deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
			if(deviceModelId == "" || deviceModelId == "-1"){
				alert("请选择设备型号！");
				return false;
			}
			
			var deviceTypeId = $("select[@name='gwShare_devicetypeId']").val();
			//if(deviceTypeId == "" || deviceTypeId == "-1"){
			//	alert("请选择软件版本！");
			//	return false;
			//}
			var is_default = $("select[@name='is_default']").val();

			// 数据提交
			if(action=="add"){// 添加
				var url = "<s:url value='/itms/config/digitMapConfig!add.action'/>";
				$.post(url,{
					map_name:encodeURIComponent(map_name) , 
					map_content:$.trim(map_content.value),
					cityId:cityId,
					vendorId:vendorId,
					deviceModelId:deviceModelId,
					deviceTypeId:deviceTypeId,
					is_default:is_default
				},function(ajax){
					var result = parseInt(ajax);
					if (result == -2)
					{
						alert("已存在默认模板！");
						return;
					}
					if(result == -1){
						alert("添加失败！");
						return;
					}
					if(result > 0){
						alert("添加成功！");
						
						var form = window.opener.parent.document.getElementById("mainForm");;
						form.action = "<s:url value='/itms/config/digitMapConfig!queryList.action'/>";
						form.submit();
						
						window.close();
					}
				});
			}else{ // 修改
				var url = "<s:url value='/itms/config/digitMapConfig!update.action'/>";
				$.post(url,{
					map_name:encodeURIComponent(map_name), 
					map_content:$.trim(map_content.value), 
					map_id:map_id,
					cityId:cityId,
					vendorId:vendorId,
					deviceModelId:deviceModelId,
					deviceTypeId:deviceTypeId,
					is_default:is_default
				},function(ajax){
					var result = parseInt(ajax);
					if (result == -2)
					{
						alert("已存在默认模板！");
						return;
					}
					if(result == -1){
						alert("编辑失败！");
						return;
					}
					if(result > 0){
						alert("编辑成功！");
						
						var form = window.opener.parent.document.getElementById("mainForm");;
						form.action = "<s:url value='/itms/config/digitMapConfig!queryList.action'/>";
						form.submit();
						
						window.close();
					}
				});
			}
			
		}
		
		
		/** 工具方法 **/
		/*LTrim(string):去除左边的空格*/
		function LTrim(str){
		    var whitespace = new String("　 \t\n\r");
		    var s = new String(str);   
		
		    if (whitespace.indexOf(s.charAt(0)) != -1){
		        var j=0, i = s.length;
		        while (j < i && whitespace.indexOf(s.charAt(j)) != -1){
		            j++;
		        }
		        s = s.substring(j, i);
		    }
		    return s;
		}
		/*RTrim(string):去除右边的空格*/
		function RTrim(str){
		    var whitespace = new String("　 \t\n\r");
		    var s = new String(str);
		 
		    if (whitespace.indexOf(s.charAt(s.length-1)) != -1){
		        var i = s.length - 1;
		        while (i >= 0 && whitespace.indexOf(s.charAt(i)) != -1){
		            i--;
		        }
		        s = s.substring(0, i+1);
		    }
		    return s;
		}
		/*Trim(string):去除字符串两边的空格*/
		function trim(str){
		    return RTrim(LTrim(str)).toString();
		}
</script>
</html>