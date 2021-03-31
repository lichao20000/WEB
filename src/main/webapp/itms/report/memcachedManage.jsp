<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��������</title>
<link rel="stylesheet" href="../../css/css_green.css" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	var ipaddress = "";
	var port = "";

	function doOperate(oipaddress,oport,mname,operateId){
		ipaddress = oipaddress;
		port = oport;
		$("#div_add").hide();
		$("#div_upd").hide();
		$("#div_del").hide();
		$("#div_stats").hide();
		$("#div_msg").hide();
		//��ѯ
		if(1==operateId){
			$("#div_upd").show();
			$("#title_upd").html("�����("+mname+")����-��ѯ/�޸�");
			$("#key_upd").val("");
			$("#value_upd").val("");
		}
		//����
		else if(2==operateId){
			$("#div_add").show();
			$("#title_add").html("�����("+mname+")����-����");
			$("#key_add").val("");
			$("#value_add").val("");
		}
		//ɾ��
		else if(3==operateId){
			$("#div_del").show();
			$("#title_del").html("�����("+mname+")����-ɾ��");
			$("#key_del").val("");
		}
		//����״̬
		else if(4==operateId){
			$("#div_msg").show();
			$("#div_msg").html("���ڲ�ѯ�����Ժ�...");
			var url = "<s:url value='/itms/report/memcachedManageACT!stats.action'/>";
			$.post(url, {
				ipaddress : ipaddress,
				port : port,
				timestamp : new Date().getTime()
			}, function(ajax) {
				if (ajax == "err") {
					$("#div_msg").html("ϵͳ��æ�����Ժ�����!");
				} else if (ajax == "off") {
					$("#div_msg").html("�����("+ipaddress+" "+port+")δ���������Ժ�����!");
				} else {
					$("#title_stats").html("�����("+mname+")����-����״̬");
					$("#text_stats").html(ajax);
					$("#div_stats").show();
					$("#div_msg").hide();
				}
			});
		}
		
	}
	
	//����
	function add(){
		$("#div_msg").show();
		$("#div_msg").html("���ڴ������Ժ�...");
		var key = $("#key_add").val();
		var value = $("#value_add").val();
		if(""==key||""==value){
			alert("Key��Value����Ϊ��!");
		}else{
			var url = "<s:url value='/itms/report/memcachedManageACT!add.action'/>";
			$.post(url, {
				key : key,
				value : value,
				ipaddress : ipaddress,
				port : port,
				timestamp : new Date().getTime()
			}, function(ajax) {
				if (ajax == "err") {
					$("#div_msg").html("ϵͳ��æ�����Ժ�����!");
				} else if (ajax == "off") {
					$("#div_msg").html("�����("+ipaddress+" "+port+")δ���������Ժ�����!");
				} else if (ajax == "ok") {
					$("#div_msg").html("���������("+ipaddress+" "+port+")�ɹ�!");
				}
			});
		}
	}
	
	//�޸�
	function upd(){
		$("#div_msg").show();
		$("#div_msg").html("���ڴ������Ժ�...");
		var key = $("#key_upd").val();
		var value = $("#value_upd").val();
		if(""==key||""==value){
			alert("Key��Value����Ϊ��!");
		}else{
			var url = "<s:url value='/itms/report/memcachedManageACT!upd.action'/>";
			$.post(url, {
				key : key,
				value : value,
				ipaddress : ipaddress,
				port : port,
				timestamp : new Date().getTime()
			}, function(ajax) {
				if (ajax == "err") {
					$("#div_msg").html("ϵͳ��æ�����Ժ�����!");
				} else if (ajax == "off") {
					$("#div_msg").html("�����("+ipaddress+" "+port+")δ���������Ժ�����!");
				} else if (ajax == "ok") {
					$("#div_msg").html("���������("+ipaddress+" "+port+")�ɹ�!");
				}
			});
		}
	}
	
	//��ѯ
	function query(){
		$("#div_msg").show();
		$("#div_msg").html("���ڲ�ѯ�����Ժ�...");
		var key = $("#key_upd").val();
		if(""==key){
			alert("Key����Ϊ��!");
		}else{
			var url = "<s:url value='/itms/report/memcachedManageACT!query.action'/>";
			$.post(url, {
				key : key,
				ipaddress : ipaddress,
				port : port,
				timestamp : new Date().getTime()
			}, function(ajax) {
				if (ajax == "err") {
					$("#div_msg").html("ϵͳ��æ�����Ժ�����!");
				} else if (ajax == "off") {
					$("#div_msg").html("�����("+ipaddress+" "+port+")δ���������Ժ�����!");
				} else if (ajax == "no"){
					$("#div_msg").html("��ѯʧ��!�����("+ipaddress+" "+port+")�в�����"+key+"!");
				}else {
					$("#div_msg").hide();
					$("#value_upd").val(ajax);
				}
			});
		}
	}
	
	
	//ɾ��
	function del(){
		$("#div_msg").show();
		$("#div_msg").html("���ڴ������Ժ�...");
		var key = $("#key_del").val();
		if(""==key){
			alert("Key����Ϊ��!");
		}else{
			var url = "<s:url value='/itms/report/memcachedManageACT!del.action'/>";
			$.post(url, {
				key : key,
				ipaddress : ipaddress,
				port : port,
				timestamp : new Date().getTime()
			}, function(ajax) {
				if (ajax == "err") {
					$("#div_msg").html("ϵͳ��æ�����Ժ�����!");
				} else if (ajax == "off") {
					$("#div_msg").html("�����("+ipaddress+" "+port+")δ���������Ժ�����!");
				} else if (ajax == "ok"){
					$("#div_msg").html("���������("+ipaddress+" "+port+")�ɹ�!");
				} else if (ajax == "no"){
					$("#div_msg").html("ɾ��ʧ��!�����("+ipaddress+" "+port+")�в�����"+key+"!");
				}
			});
		}
	}
	
	//�ϲ���Ԫ��
	function mergeCell(tableObj, col) {
        var $tab = $(tableObj);
        var $trs = $tab.find("tr");
        
        var oldval;
        var firstTD;
        var counter = 0;
        $trs.each(function(index) {
            
            if (!oldval && !firstTD) {
                oldval = $(this).find("td:eq(" + col + ")").text();
                firstTD = $(this).find("td:eq(" + col + ")").get(0);
                counter = 0;
                counter++;
            } else {
                if ($(this).find("td:eq(" + col + ")").text() == oldval) {
                    $(this).find("td:eq(" + col + ")").remove();
                    counter++;
                } else {
                    $(firstTD).attr("rowSpan", counter);
                    oldval = $(this).find("td:eq(" + col + ")").text();
                    firstTD = $(this).find("td:eq(" + col + ")").get(0);
                    counter = 0;
                    counter++;
                }
            }
            
            if (index >= $trs.length - 1) {
                $(firstTD).attr("rowSpan", counter);
            }
            
        });
    }

	$().ready(function() {
        mergeCell($("#dataTable").get(0), 0);
    });


</script>
</head>
<body>
<table boder=0 cellspacing=0 cellpadding=0 width="100%">
		<tr>
			<td height=20>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">��������</td>
						<td><img src="<s:url value="/images/attention_2.gif"/>"
							width="15" height="12" /> ��������</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td >
				<table border=1 width="100%" class="querytable" id="dataTable">
					<tr>
						<th>����</th>
						<th>IP��ַ</th>
						<th>�˿ں�</th>
						<th>״̬</th>
						<th>��������</th>
					</tr>
					<s:if test="resultList!=null">
						<s:if test="resultList.size()>0">
							<s:iterator value="resultList">
								<tr>
									<td><s:property value="mname" /></td>
									<td><s:property value="ipaddress" /></td>
									<td><s:property value="port" /></td>
									<td align="center"><s:if test='status=="1"'>
											UP
										</s:if>
										<s:else>
											Down
										</s:else>
										</td>
									<td align="center">
										<s:if test='status=="1"'>
											<a href="javascript:void(0);" onclick="doOperate('<s:property value="ipaddress" />','<s:property value="port" />','<s:property value="mname" />',1);">��ѯ</a>&nbsp;&nbsp;|&nbsp;&nbsp;
											<a href="javascript:void(0);" onclick="doOperate('<s:property value="ipaddress" />','<s:property value="port" />','<s:property value="mname" />',2);">����</a>&nbsp;&nbsp;|&nbsp;&nbsp;
											<a href="javascript:void(0);" onclick="doOperate('<s:property value="ipaddress" />','<s:property value="port" />','<s:property value="mname" />',3);">ɾ��</a>&nbsp;&nbsp;|&nbsp;&nbsp;
											<a href="javascript:void(0);" onclick="doOperate('<s:property value="ipaddress" />','<s:property value="port" />','<s:property value="mname" />',4);">����״̬</a>
										</s:if>
										<s:else>
											-
										</s:else>
									</td>
								</tr>
							</s:iterator>
						</s:if>
						<s:else>
						<tr>
							<td colspan=5>ϵͳû�����û������Ϣ!</td>
						</tr>
						</s:else>
					</s:if>
					<s:else>
					<tr>
						<td colspan=5>ϵͳû�����û������Ϣ!</td>
					</tr>
					</s:else>
				</table>
			</td>
		</tr>
		<tr>
			<td height="20">&nbsp;</td>
		</tr>
		<tr>
			<td>
				<div id="div_add" style="display: none;">
					<table border=1 width="100%" class="querytable">
						<tr>
							<th colspan="4"><span id="title_add">�����()����-����</span></th>
						</tr>
						<tr>
							<td class=column width="15%" align='right'>
								Key:
							</td>
							<td width="35%">
								<input type="text" id="key_add" size="20" maxlength="30" class=bk />
								<font color="red"> *</font>
							</td>
							<td class=column width="15%" align='right'>
								Value:
							</td>
							<td width="35%">
								<input type="text" id="value_add" size="20" maxlength="30" class=bk />
								<font color="red"> *</font>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="right" class=foot>
								<button onclick="add()">&nbsp;�� ��&nbsp;</button>
							</td> 
						</tr>
					</table>
				</div>
				<div id="div_upd" style="display: none;">
					<table border=1 width="100%" class="querytable">
						<tr>
							<th colspan="3"><span id="title_upd">�����()����-��ѯ/�޸�</span></th>
						</tr>
						<tr>
							<td class=column width="25%" align='right'>
								Key:
							</td>
							<td>
								<input type="text" id="key_upd" size="20" maxlength="30" class=bk />
								<font color="red">&nbsp;*</font>
							</td>
							<td width="25%">
								<button onclick="query()">&nbsp;�� ѯ&nbsp;</button>
							</td>
						</tr>
						<tr>
							<td class=column width="25%" align='right'>
								Value:
							</td>
							<td width="25%">
								<input type="text" id="value_upd" size="20" maxlength="30" class=bk />
							</td>
							<td>
								<button onclick="upd()">&nbsp;�� ��&nbsp;</button>
							</td>
						</tr>
					</table>
				</div>
				<div id="div_del" style="display: none;">
					<table border=1 width="100%" class="querytable">
						<tr>
							<th colspan="2"><span id="title_del">�����()����-ɾ��</span></th>
						</tr>
						<tr>
							<td class=column width="15%" align='right'>
								Key:
							</td>
							<td width="35%">
								<input type="text" id="key_del" size="20" maxlength="30" class=bk />
								<font color="red">&nbsp;*</font>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="right" class=foot>
								<button onclick="del()">&nbsp;ɾ ��&nbsp;</button>
							</td> 
						</tr>
					</table>
				</div>
				<div id="div_stats" border=1 width="100%" style="display: none;">
					<table class="querytable">
						<tr>
							<th><span id="title_stats">�����()����-����״̬</span></th>
						</tr>
						<tr>
							<td class=column >
								<div id="text_stats" style="text-align: left;"></div>
							</td>
						</tr>
					</table>
				</div>
				<div id="div_msg">
				</div>
			</td>
		</tr>
	</table>
	<br>
</body>
</html>
