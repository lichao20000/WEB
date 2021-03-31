<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�ն˹�����û����һ��ͳ�����</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	
	function query() {
		var spec_id = $("select[@name='spec_id']").val();
		var startOpenDate = $("input[@name='startOpenDate']").val();
		var endOpenDate = $("input[@name='endOpenDate']").val();
		var city_id = $("select[@name='city_id']").val();
		var _startOpenDate = $("input[@name='startOpenDate']");
		var _endOpenDate = $("input[@name='endOpenDate']");
		var _city_Id = $("select[@name='city_id']");
		var _spec_id = $("select[@name='spec_id']");
		if ('' == _city_Id.val() || '-1' == _city_Id.val()) {
			alert("��ѡ������");
			_city_Id.focus();
			return false;
		}
		if ('' == _spec_id.val() || '0' == _spec_id.val()) {
			alert("��ѡ��ƥ�䷽ʽ");
			_spec_id.focus();
			return false;
		}
		$("tr[@id='trData']").show();
		$("div[@id='QueryData']").html("���ڲ�ѯ�����Ե�....");
		
		var url = '<s:url value='/itms/resource/UserSpecTerminals!getUserSpecTerminals.action'/>'; 
		$.post(url,{
			spec_id:spec_id,
			city_id:city_id,
			startOpenDate:startOpenDate,
			endOpenDate:endOpenDate
		},function(ajax){	
		    $("div[@id='QueryData']").html("");
			$("div[@id='QueryData']").append(ajax);
		});
	}
	function countBycityId(cityId){
		var spec_id = $("select[@name='spec_id']").val();
		var startOpenDate = $("input[@name='startOpenDate']").val();
		var endOpenDate = $("input[@name='endOpenDate']").val();
		var city_id = $("select[@name='city_id']").val();
		$("tr[@id='trData']").show();
	    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
		var url = '<s:url value='/itms/resource/UserSpecTerminals!getUserSpecTerminals.action'/>'; 
		$.post(url,{
			spec_id:spec_id,
			city_id:cityId,
			startOpenDate:startOpenDate,
			endOpenDate:endOpenDate
		},function(ajax){
			$("div[@id='QueryData']").html("");
			$("div[@id='QueryData']").append(ajax);
		});
	}
	function doQuery(spec_id,cityId,startOpenDate,endOpenDate) {
		var page = "<s:url value='/itms/resource/UserSpecTerminals!getTerminalSpecList.action'/>?"
				+ "city_id="
				+ cityId
				+ "&startOpenDate="
				+ startOpenDate
				+ "&endOpenDate=" + endOpenDate + "&spec_id=" + spec_id;
		window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}
	function ToCityExcel(spec_id,cityId,startOpenDate,endOpenDate){
		var mainForm = document.getElementById("selectForm");
		mainForm.action = "<s:url value='/itms/resource/UserSpecTerminals!getCityExcel.action' />";
		mainForm.submit();
		mainForm.action = "<s:url value='/itms/resource/UserSpecTerminals!getUserSpecTerminals.action' />";
	}
</script>
</head>

<body>
	<form id="form" name="selectForm" id="selectForm"
		action="<s:url value='/itms/resource/UserSpecTerminals!getUserSpecTerminals.action'/>">
		<input type="hidden" name="selectType" value="0" /> <input
			type="hidden" name="gw_type" value='<s:property value="gw_type" />' />
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">���ͳ��</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /> ��ʼʱ�䡢����ʱ�䣬Ϊ�û�����ʱ�䡣�밴��ͳ�ƹ��ƥ�������<font
								color="red">*</font></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">�ն˹�����û����ƥ��ͳ��</th>
						</tr>
						<TR>
							<TD class=column width="15%" align='right'>��ʼʱ��</TD>
							<TD width="35%"><input type="text" name="startOpenDate"
								readonly class=bk value="<s:property value="startOpenDate" />">
								<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��"> &nbsp; <font color="red"> *</font></TD>
							<TD class=column width="15%" align='right'>����ʱ��</TD>
							<TD width="35%"><input type="text" name="endOpenDate"
								readonly class=bk value="<s:property value="endOpenDate" />">
								<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��"> &nbsp; <font color="red"> *</font></TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>����</TD>
							<TD width="35%"><s:select list="cityList" name="city_id"
									headerKey="0" headerValue="��ѡ������" listKey="city_id"
									listValue="city_name" cssClass="bk"></s:select> &nbsp; <font
								color="red"> *</font></TD>
							<TD class=column width="15%" align='right'>��ƥ�䷽ʽ</TD>
							<TD width="35%"><select name="spec_id">
									<option value="0">=====��ѡ��=====</option>
									<option value="4221">�û����4+2���ն˹��2+1</option>
									<option value="4220">�û����4+2���ն˹��2+0</option>
									<option value="2142">�û����2+1���ն˹��4+2</option>
									<option value="2042">�û����2+0���ն˹��4+2</option>
							</select> <font color="red"> *</font></TD>
						</TR>

						<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="query()">&nbsp;�� ѯ&nbsp;</button>
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr style="heigth: 100px"></tr>
			<tr id="trData" style="display: none">
				<td class="colum">
					<div id="QueryData" style="width: 100%; z-index: 1; margin-top: 50px">
						���ڲ�ѯ�����Ե�....</div>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
			<tr>
				<td id="dataTd"><iframe id="dataForm" name="dataForm"
						height="1000" frameborder="0" scrolling="no" width="100%" src=""></iframe>
				</td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>