<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>错误BSS工单</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	function query() {
		var _reciveDateStart = $("input[@name='reciveDateStart']").val();
		var _reciveDateEnd = $("input[@name='reciveDateEnd']").val();
		//var _cityId = $("select[@name='cityId']").val();
		var _username = $("input[@name='username']").val();
		var _resultSheet = $("select[@name='resultSheet']").val();
		var _sheetType = $("select[@name='sheetType']").val();
		$("td[@id='sheetContent']").hide();
		var cityId = $.trim($("select[@name='cityId']").val());
		if (cityId == "-1") {
			alert("请选择属地");
			return false;
		}
		document.selectForm.submit();
		/**var url = "<s:url value='/itms/service/errorSheet!query.action'/>";
		$.post(url, {
			reciveDateStart : _reciveDateStart,
			reciveDateEnd : _reciveDateEnd,
			username : _username,
			resultSheet : _resultSheet,
			sheetType : _sheetType,
			cityId : cityId
		}, function(mesg) {
			$("td[@id='sheetList']").html(mesg);
		}); **/
	}

	//** iframe自动适应页面 **//
	//输入你希望根据页面高度自动调整高度的iframe的名称的列表
	//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
	//定义iframe的ID
	var iframeids = [ "dataForm" ]

	//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
	var iframehide = "yes"

	function dyniframesize() {
		var dyniframe = new Array()
		for (i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//自动调整iframe高度
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block"
					//如果用户的浏览器是NetScape
					if (dyniframe[i].contentDocument
							&& dyniframe[i].contentDocument.body.offsetHeight)
						dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
					//如果用户的浏览器是IE
					else if (dyniframe[i].Document
							&& dyniframe[i].Document.body.scrollHeight)
						
						{
							dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
						}

				}
			}
			//根据设定的参数来处理不支持iframe的浏览器的显示问题
			if ((document.all || document.getElementById) && iframehide == "no") {
				var tempobj = document.all ? document.all[iframeids[i]]
						: document.getElementById(iframeids[i])
				tempobj.style.display = "block"
			}
		}
	}

	$(function() {
		//setValue();
		dyniframesize();
	});

	$(window).resize(function() {
		dyniframesize();
	});
</script>
</head>

<body>
<form name="selectForm"
	action="<s:url value='/itms/service/errorSheet!query.action'/>"
	target="dataForm"><input type="hidden" name="selectType"
	value="0" />
<table>
	<tr>
		<td HEIGHT=20>&nbsp;</td>
	</tr>
	<tr>
		<td>
		<table class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">
				错误BSS工单查询</td>
				<td><img src="<s:url value="/images/attention_2.gif"/>"
					width="15" height="12" /> 查询错误BSS工单情况</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table class="querytable">

			<TR>
				<th colspan="4">错误BSS工单</th>
			</tr>

			<TR bgcolor="#FFFFFF">
				<TD class=column align="right" width="20%">接受开始时间</TD>
				<TD width="30%"><input type="text" name="reciveDateStart"
					value='<s:property value="reciveDateStart" />' readonly class=bk>
				<img name="shortDateimg"
					onClick="WdatePicker({el:document.selectForm.reciveDateStart,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
					src="../../images/dateButton.png" width="15" height="12" border="0"
					alt="选择"></TD>

				<TD class=column align="right" width="20%">接受结束时间</TD>
				<TD width="30%"><input type="text" name="reciveDateEnd"
					value='<s:property value="reciveDateEnd" />' readonly class=bk>
				<img name="shortDateimg"
					onClick="WdatePicker({el:document.selectForm.reciveDateEnd,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
					src="../../images/dateButton.png" width="15" height="12" border="0"
					alt="选择"></TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD class=column align="right" width="20%">属地</TD>
				<TD width="30%"><s:select list="cityList" name="cityId"
					headerKey="-1" headerValue="请选择属地" listKey="city_id"
					listValue="city_name" value="cityId" cssClass="bk"></s:select></TD>
				<TD class=column align="right" nowrap>LOID</TD>
				<TD><INPUT TYPE="text" NAME="username" maxlength=50 class=bk
					value="">
				</div>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD class=column align="right" width="20%">工单类型</TD>
				<TD width="30%"><select name="sheetType" class=bk>
					<option value="-1">==请选择业务类型==</option>
					<option value="20">建设流程</option>
					<option value="22">上网业务</option>
					<option value="21">IPTV业务</option>
					<option value="14">VOIP业务</option>
				</select></TD>
				<TD class=column align="right" nowrap>回单结果</TD>
				<TD><select name="resultSheet" class=bk>
					<option value="-1">==请选择回单结果==</option>
					<option value="1">无此业务类型</option>
					<option value="2">无此操作类型</option>
					<option value="3">字段数目不对</option>
					<option value="4">业务受理时间不对</option>
					<option value="5">无此设备</option>
					<option value="6">无此用户</option>
					<option value="7">属地不对</option>
					<option value="8">帐号重复</option>
					<option value="9">最后缺少LINKGE</option>
					<option value="10">局向不对</option>
					<option value="11">接入方式不对</option>
					<option value="12">VlanID号不对</option>
					<option value="13">终端类型不对</option>
					<option value="31">IPTV终端个数不正确</option>
					<option value="32">开通端口不正确</option>
					<option value="33">IPTV宽带接入账号不合法</option>
					<option value="41">Voip认证账户不合法</option>
					<option value="42">Voip认证密码不合法</option>
					<option value="43">Sip服务器地址不合法</option>
					<option value="44">Sip服务器端口不合法</option>
					<option value="45">备用Sip服务器地址不合法</option>
					<option value="46">备用Sip服务器端口不合法</option>
					<option value="47">Voip线路端口不合法</option>
					<option value="48">VOIP逻辑电话号码不合法</option>
					<option value="51">LOID不合法</option>
					<option value="52">LOID已经存在</option>
					<option value="53">LOID不存在，请先走建设流程</option>
				</select></TD>
			</TR>
			<TR>
				<td colspan="4" align="right" class=foot>
				<button onclick="query()">&nbsp;查 询&nbsp;</button>
				</td>
			</TR>
		</table>
		</td>
	</tr>
	<tr>
		<td height="25" id="resultStr"></td>
	</tr>
	<tr>
		<td><iframe id="dataForm" name="dataForm" height="0"
			frameborder="0" scrolling="no" width="100%" src=""></iframe></td>

		<!-- <td id="sheetList"></td> -->
	</tr>
	<tr>
		<td height="25"></td>
	</tr>
	<tr>
		<td id="sheetContent"></td>
	</tr>
</table>
<br>
</form>
</body>
</html>