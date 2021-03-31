<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="../../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
function doQuery(){
	var con = $.trim($("select[@name='con']").val());
    var condition = $.trim($("input[@name='condition']").val());
    var openState = $.trim($("select[@name='openState']").val());
    var starttime = $.trim($("input[@name='starttime']").val());
	var endtime = $.trim($("input[@name='endtime']").val()); 
	if(condition == null || condition == ""){
    	alert("���Ǻŵ�Ϊ�����");
   	    return false;
	}
	if(con=="1" && condition.length < 6){
		 alert("�豸���кŵ���Ч�ַ���������Ϊ6��");
    	 return false;
	}
	$("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
    $("button[@name='button']").attr("disabled", true);
    var operType = "<s:property value = 'operType' />";
    var url="<s:url value='/gtms/config/netByDHCPStop!queryNetByDHCPList.action'/>";
	$.post(url,{
		con:con,
		condition:condition,
		openState:openState,
		starttime:starttime,
		endtime:endtime
	},function(ajax){	
		if(ajax > 1){
			alert("�ÿ���ʺŶ�Ӧ����û�����ʹ���߼�sn�����豸���кŲ�ѯ!");
			$("div[@id='QueryData']").html("");
		} else{
			$("div[@id='QueryData']").html("");
			$("div[@id='QueryData']").append(ajax);
		}
		$("button[@name='button']").attr("disabled", false);
	});
	
}

</script>

<br>
<table>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>���DHCP�رղ��Բ�ѯ</th>
					<td><img src="<s:url value="/images/attention_2.gif"/>"
						width="15" height="12"></td>
					<td>��ѯ���DHCP�رղ����������ʼʱ�䡢����ʱ��Ϊ����ʱ�䡣</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name="frm">
				<table class="querytable">
					<tr>
						<th colspan=4>���DHCP�رղ��Բ�ѯ</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="25%">
							<select name="con" class=column>
								<option value="1">�豸���к�</option>
								<option value="0">LOID</option>
								<option value="-1">����˺�</option>
							</select>
						</td>
						<td align=center width="25%">
							<input type="text" name="condition" class='bk' /> 
							<font color="red">*</font>
						</td>
						<td class=column  width="25%">
							��ͨ״̬
						</td>
						<td bgcolor=#eeeeee>
							<select name="openState" class=column>
								<option value="2">��ѡ��</option>
								<option value="1">�ɹ�</option>
								<option value="0">ʧ��</option>
								<option value="3">δ��</option>
							</select>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="25%">
							��ͨʱ��
						</td>
						<td>
							<input type="text" name="starttime" class='bk' readonly value="<s:property value='starttime'/>">
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" /> 
							<font color="red">*</font>
						</td>
						<td class=column align=center width="15%">
							����ʱ��
						</td>
						<td><input type="text" name="endtime" class='bk' readonly value="<s:property value='endtime'/>"> 
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" /> 
							<font color="red">*</font>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4>
							<button onclick="doQuery()" name="button" id="button">&nbsp;��ѯ&nbsp;</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>
	
	<tr id="trData" style="display: none">
		<td class="colum">
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				����ͳ�ƣ����Ե�....
			</div>
		</td>
	</tr>
	<tr>
		<td height="20">
		</td>
	</tr>
</table>
<%@ include file="/foot.jsp"%>

