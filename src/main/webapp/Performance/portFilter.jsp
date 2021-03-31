<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<html>
	<head>
		<title>过滤规则配置</title>
		<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
		type="text/css">
		<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
		type="text/css">
		<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
		type="text/css">
		<link href="<s:url value="/css2/tab.css"/>" rel="stylesheet"
		type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/CheckForm.js"/>"></script>
		<style type="text/css">
		</style>
		<script type="text/javascript">

		var msg = '<s:property value="messageStr"/>'
		if(msg!=''&&msg!=null)
		alert(msg)//提示操作结果
		//选择设备厂商，根据设备厂商获得设备名称
		function chooseCompany(){
			var form1 = document.forms['form1'];
			form1.action = "<s:url value="/performance/portFilterAction!chooseCompany.action"/>";
			form1.submit();

		}

		//创建过滤规则
		function submit1(){
			var form1 = document.forms['form1'];
			var companyName = document.getElementById("companyName").value;
			var deviceModel = document.getElementById("deviceModel").value;
			var filterType = document.getElementById("filterType").value;
			var filterValue = document.getElementById("filterValue").value;
			if(Trim(companyName)=="None"){//判断厂商名是否为空
				alert("请选择设备厂商！");
				return;
			}
			if(Trim(deviceModel)=="None"){//判断设备型号是否为空
				alert("请选择设备型号！");
				return;
			}
			if(Trim(filterType)=="None"){//判断过滤类型是否为空
				alert("请选择过滤类型！");
				return;
			}
			if(Trim(filterValue)==""){//判断过滤信息是否为空
				alert("请输入过滤信息！");
				return;
			}
			if(Trim(filterType)=="4"){//判断输入的端口索引过滤信息是否合法
				if(!isIndex(filterValue)){
					alert("端口索引过滤信息不合法必须是数字并且类似 123，456！");
					return ;
				}
			}
			if(Trim(filterType)=="5"){//判断输入的端口类型过滤信息是否合法
				if(!IsNum(filterValue)){//判断是否是数字
					alert("端口类型必须是数字！")
					return;
				}
			}

			function isIndex(str){//判断是否是有效的Index区间
				var bz=true;
				var pos = str.indexOf(",");
				if(pos==-1){
					bz=false;
				}else{
					var arr = str.split(",");
					if(arr.length>2){
						bz=false;
					}else{
						if(!IsNum(arr[0])){
							bz=false;
						}
						if(!IsNum(arr[1])){
							bz=false;
						}
					}
				}
				return bz;
			}

			form1.action = "<s:url value="/performance/portFilterAction!savePortFilterRule.action"/>";
			form1.submit();

		}
		//查询规则
		function search(){
			var form1 = document.forms['form1'];
			var companyName = document.getElementById("companyName").value;
			var deviceModel = document.getElementById("deviceModel").value;
			if(Trim(companyName)=="None"){//判断厂商名是否为空
				alert("请选择设备厂商！");
				return;
			}
			if(Trim(deviceModel)=="None"){//判断设备型号是否为空
				alert("请选择设备型号！");
				return;
			}
			form1.action = "<s:url value="/performance/portFilterAction!searchPortFilterRule.action"/>";
			form1.submit();
		}
		//删除规则
		function delRules(deviceModel,filterType){
			if(confirm("确定要删除规则?")==false){
	  			return;
	  		}else{
				document.getElementById("delDeviceModel").value=deviceModel
				document.getElementById("filterType").value=filterType
				var form11 = document.forms['form1'];
				form11.action = "<s:url value="/performance/portFilterAction!removePortFilterRule.action"/>";
				form11.submit();
			}
		}
		</script>
	</head>
	<body>
	<form name="form1" id="form1" align="center">
	<input type="hidden" id="delDeviceModel" name="delDeviceModel"/>
		<br/>
		<br/>
			<table width="95%" class="tab" align="center">
			  <tr>
			    <td class="mouseon"
			                      onClick="location.href='#'"
			                      onMouseOver="this.className='mouseon';"
			                      onMouseOut="this.className='mouseon';">规则配置</td>
			    <td>&nbsp;</td>
			  </tr>
			  <tr>
			    <td colspan="2" class="tag_line"></td>
			  </tr>
			</table>
			<table width="95%" class="querytable" align="center">
			<tr>
					<td class="title_2">选择设备厂商：</td>
					<td>
					<select id="companyName" name="companyID" onchange="chooseCompany()">
						<option value="None" selected="selected">--请选择设备厂商--</option>
						<s:iterator var="company" value="companyList" status="row">
							<s:if test="companyID==#company.vendor_id">
								<option value="<s:property value="vendor_id"/>" selected="selected"><s:property value="vendor_name"/></option>
							</s:if>
							<s:else>
								<option value="<s:property value="vendor_id"/>"><s:property value="vendor_name"/></option>
							</s:else>
						</s:iterator>
					</select>
					</td>

			</tr>

			<tr>
					<td class="title_2">选择设备型号：</td>
					<td>
					<select name="deviceModel" id="deviceModel">
						<option value="None" selected="selected">--请选择设备型号--</option>
						<s:iterator var="modelMap" value="deviceModelList" status="row">
							<s:if test="deviceModel==#modelMap.device_name">
								<option value="<s:property value="device_model_id"/>" selected="selected"><s:property value="device_model"/></option>
							</s:if>
							<s:else>
								<option value="<s:property value="device_model_id"/>"><s:property value="device_model"/></option>
							</s:else>
						</s:iterator>
					</select>
					</td>

			</tr>
			<tr>
					<td class="title_2">选择过滤类型：</td>
					<td >
					<select name="filterType" id="filterType">
						<option value="None" selected="selected">--请选择过滤类型--</option>
						<option value="1" >端口名称</option>
						<option value="2" >端口描述</option>
						<option value="3" >端口别名</option>
						<option value="4" >端口索引</option>
						<option value="5" >端口类型</option>
					</select>
					</td>
			</tr>
			<tr>
					<td class="title_2">输入过滤信息：</td>
					<td ><input name="filterValue" id ="filterValue" type="text" /></td>
			</tr>
			<tr>
				<td colspan="2" class="foot"><div align="right"><button onclick="submit1()"/>新  增</button><button onclick="search()"/>查  询</button></div></td>
			</tr>
			</table>


		<br/>
		<br/>
		<table width="95%" class="tab" align="center">
		  <tr>
		    <td class="mouseon"
		                      onClick="location.href='#'"
		                      onMouseOver="this.className='mouseon';"
		                      onMouseOut="this.className='mouseon';">规则列表</td>
		    <td>&nbsp;</td>
		  </tr>
		  <tr>
		    <td colspan="2" class="tag_line"></td>
		  </tr>
		</table>
		<table width="95%" align="center" class="listtable">
		<thead>
	    <tr align="center">
        <th>设备型号</th>
        <th>过滤类型</th>
        <th> 过滤信息</th>
        <th>操作</th>

	    </tr>
	    <tbody>
	    <s:if test="filterRulesList.size!=0">
	    <s:iterator var="filter"value="filterRulesList" status="row">
	    	<tr>
	    	<td><s:property value="device_model"/></td>
	    	<td><s:if test="#filter.type==1">端口名称</s:if>
	    		<s:elseif test="#filter.type==2">端口描述</s:elseif>
	    		<s:elseif test="#filter.type==3">端口别名</s:elseif>
	    		<s:elseif test="#filter.type==4">端口索引</s:elseif>
	    		<s:elseif test="#filter.type==5">端口类型</s:elseif>
	    	</td>
	    	<td><s:property value="value"/></td>
	    	<td>
		    	<a href="javascript:delRules('<s:property value="device_model_id"/>','<s:property value="type"/>');" title="点击删除规则">删除</a>
	    	</td>
	    	</tr>
	    </s:iterator>
	    <tr><td colspan="4"><div align="right"><lk:pages url="/performance/portFilterAction!toChooseCompany.action" isGoTo="true"/></div></td></tr>
		</s:if>
		<s:else>
		<td colspan="4">无数据！</td>
		</s:else>

		</table>
	</form>
	</body>
</html>
