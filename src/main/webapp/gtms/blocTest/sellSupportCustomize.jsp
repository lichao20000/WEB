<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript">
function selectCust(_name){
	
	var strcustIDs = getDeviceIDByCheck(_name);
	var cust_id = strcustIDs.split(",");
	
	if(strcustIDs == ""){
		alert("请选择要确认的客户");
		return false;
	}
	document.addForm.customerId.value=strcustIDs;
	return true;
}
//根据复选框选中状态，获取设备id
function getDeviceIDByCheck(_name){
	var arrObj = document.all(_name);
	var strDeviceIDs = "";
	if(typeof(arrObj.length) == "undefined"){
		if(arrObj.checked){
			strDeviceIDs = arrObj.value + ",";
		}
	}else{
		for(var i=0;i<arrObj.length;i++){
			if(arrObj[i].checked){
				strDeviceIDs += arrObj[i].value + ",";
			}
		}
	}
	strDeviceIDs = strDeviceIDs.substring(0, strDeviceIDs.length-1);
	return strDeviceIDs;
}
	//查询
	function queryCustomer() {
		document.addForm.submit();
	}
	//** iframe自动适应页面 **//
	//输入你希望根据页面高度自动调整高度的iframe的名称的列表
	//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
	//定义iframe的ID
	var iframeids=["dataForm"]

	//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
	var iframehide="yes"

	function dyniframesize() 
	{
		var dyniframe=new Array()
		for (i=0; i<iframeids.length; i++)
		{
			if (document.getElementById)
			{
				//自动调整iframe高度
				dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera)
				{
	     			dyniframe[i].style.display="block"
	     			//如果用户的浏览器是NetScape
	     			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
	      				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
	      			//如果用户的浏览器是IE
	     			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
	      				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
	   			 }
	   		}
			//根据设定的参数来处理不支持iframe的浏览器的显示问题
			if ((document.all || document.getElementById) && iframehide=="no")
			{
				var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
	    		tempobj.style.display="block"
			}
		}
	}

	$(function(){
		//setValue();
		dyniframesize();
	});

	$(window).resize(function(){
		dyniframesize();
	}); 
</SCRIPT>
<%@ include file="/toolbar.jsp"%>
<body onload="init();">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM id="addForm" name="addForm" method="post"
			action="./gtms/blocTest/sellSupportCustomize!queryData.action" target="dataForm">
			<input type="hidden" name="customerId" value="">
						
		<!-- 添加和编辑part -->
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center"
			id="addTable">
<table  class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">营销支撑定制</td>
				<td><img src="<s:url value="/images/attention_2.gif"/>"
					width="15" height="12" /> 定制营销支撑报告</td>
			</tr>
		</table>
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">

					<tr>
						<th colspan="4" id="gwShare_thTitle">营销支撑定制</th>
					</tr>
					<!-- id="gwShare_tr21" -->
					<tr bgcolor="#FFFFFF" id="gwShare_tr21">
						<td align="right" class=column width="15%">流量上限</td>
						<td align="left" width="35%"><INPUT TYPE="text"
							NAME="flow_max" maxlength=20 class=bk size=20
							value="<s:property value="configMap.lan_can"/>">&nbsp;</td>
						<td align="right" class=column width="15%">流量下限</td>
						<td align="left" width="35%"><INPUT TYPE="text"
							NAME="flow_min" maxlength=20 class=bk size=20
							value="<s:property value="configMap.lan_can"/>">&nbsp;</td>
					</tr>
					<tr bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<td align="right" class=column width="15%">时长上限</td>
						<td align="left" width="35%"><INPUT TYPE="text"
							NAME="time_max" maxlength=20 class=bk size=20
							value="<s:property value="configMap.lan_can"/>">&nbsp;</td>
						<td align="right" class=column width="15%">时长下限</td>
						<td align="left" width="35%"><INPUT TYPE="text"
							NAME="time_min" maxlength=20 class=bk size=20
							value="<s:property value="configMap.lan_can"/>">&nbsp;</td>

					</tr>
					<tr bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<td align="right" class=column width="15%">客户经理</td>
						<td align="left" width="35%">
									<s:select list="custManagerList" name="custManagerId" headerKey="-1"
										headerValue="==请选择==" listKey="cust_manager_id" listValue="cust_manager_name"
										 cssClass="bk"></s:select>
						</td>

						<td align="right" class=column width="15%"></td>
						<td align="left" width="35%"><!-- input type="button"
							value="查询客户" onclick="queryCustomer()"--></td>

					</tr>
					<tr>
						<th colspan="4" id="gwShare_thTitle">选择客户</th>
					</tr>
					<table width="100%" border=0 cellspacing=1 cellpadding=2
					bgcolor=#999999 id=userTable>
					<tr>
					<th></th>
						<th>客户ID</th>
						<th>客户名</th>
						<th>联系人</th>
						<th>联系电话</th>
						<!--th>属地</th-->
					</tr>
					<s:if test="customerList.size()>0">
						<s:iterator value="customerList">
							<tr bgcolor="#FFFFFF">
								<td class=column1><input type=checkbox name=chkCheck onclick="selectCust('chkCheck')"
									value='<s:property value="customer_id"/>'></td>
								<td class=column1><s:property value="customer_id" /></td>
								<!--  <td class=column1><s:property value="customer_account"/></td>-->
								<td class=column1><s:property value="customer_name" /></td>
								<td class=column1><s:property value="linkman" /></td>
								<td class=column1><s:property value="linkphone" /></td>
								<!--td class=column1><s:property value="city_id" /></td-->
							</tr>
						</s:iterator>
					</s:if>
					</table>
					<tr>
						<td colspan="4" align="right" CLASS=green_foot><INPUT
							TYPE="button" onclick="javascript:abc();" class="jianbian"
							value="定 制"> <INPUT TYPE="hidden" name="action"
							value="add"> <input type="hidden" name="devicetype_id"
							value="<s:property value="devicetype_id"/>"></td>
					</tr>
					

				</TABLE>
				</TD>
				
			</TR>
		</TABLE>
		</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=15><IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>&nbsp;</TD>
	</TR>
</TABLE>
</body>
<SCRIPT LANGUAGE="JavaScript">

	function abc() {

		//trimAll();
		var url = "<s:url value='/gtms/blocTest/sellSupportCustomize!add.action'/>?customerId="+document.addForm.customerId.value;
		var cust_manager_id = $("select[@name='custManagerId']").val();
		var flow_min = $("input[@name='flow_min']").val();
		var flow_max = $("input[@name='flow_max']").val();
		var time_min = $("input[@name='time_min']").val();
		var time_max = $("input[@name='time_max']").val();
		if (!/^[\d]+$/gi.test(flow_min)) {
			alert("请输入数字");
			$("input[@name='flow_min']").focus();
			$("input[@name='flow_min']").select();
			return false;
		}
		if (!/^[\d]+$/gi.test(flow_max)) {
			alert("请输入数字");
			$("input[@name='flow_max']").focus();
			$("input[@name='flow_max']").select();
			return false;
		}
		if (!/^[\d]+$/gi.test(time_min)) {
			alert("请输入数字");
			$("input[@name='time_min']").focus();
			$("input[@name='time_min']").select();
			return false;
		}
		if (!/^[\d]+$/gi.test(time_max)) {
			alert("请输入数字");
			$("input[@name='time_max']").focus();
			$("input[@name='time_max']").select();
			return false;
		}
		$.post(url, {
			custManagerId : cust_manager_id,
			flowMin : flow_min,
			flowMax : flow_max,
			timeMin : time_min,
			timeMax : time_max
		}, function(ajax) {

				alert(ajax);

		});
	}

</SCRIPT>