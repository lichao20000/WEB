<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%--
/**
 * ��������ҳ��;
 * BUG:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-Mail:benyp@lianchuang.com
 * @version 1.0
 * @since 2008-10-23 ??03:32:08
 *
 * @��Ȩ �Ͼ����� ���ܲ�Ʒ��;
 *
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��������</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryCheckForm-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/PerformanceConfig/jquery.configPmee.js"/>"></script>
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/tab.css"/>" rel="stylesheet" type="text/css">
<style type="text/css">
   td.no{
		color:red;
	}
</style>
<script type="text/javascript">
	$(function(){
		var ajax="<s:property value="ajax"/>";
		if(ajax=="true" || ajax=="false"){
			if(ajax=="true"){
				alert("�ѳɹ������߳����ã���ȴ�Ƭ�̲鿴���ý����������Ҫһ��ʱ�䣡");
			}else{
				alert("����ʧ�ܣ����������ã�");
			}
			window.close();
		}
		var isbatch="<s:property value="isbatch"/>";
		if(isbatch=='true'){
			window.setInterval("getConfigFlux();",1000*60*2);//ˢ�»�ȡ�Ѿ����õ����ܱ��ʽ
		}
		//�ɼ���ʽ�л�
		$("input[@name='auto']").click(function(){
			$("#tr_coltype").toggle();
		});
		//���������л�
		$("input[@name='total']").click(function(){
			$("#tr_total").toggle();
			getPort();
		});
		//��ʾ�������ø澯��
		$("td[@name='td_show']").toggle(function(){
			$(this).html("�������� <img src='<s:url value="/images/ico_9_up.gif"/>' width='10' hight='12' align='center' border='0' alt='����������ø澯' >");
			$("#tr_show").show();
			getWarn();
		},function(){
			$(this).html("���ø澯 <img src='<s:url value="/images/ico_9.gif"/>' width='10' hight='12' align='center' border='0' alt='������ø澯' >");
			$("#tr_show").hide();
		});
	});
	//��ȡ�˿�
	function getPort(){
		if($("#div_port").html()==""){
			$("#div_port").html("���ڻ�ȡ�˿���Ϣ����ȴ�........");
			$.post(
				"<s:url value="/performance/configFlux!getDevPortStr.action"/>",
				{
					device_id:"<s:property value="device_id"/>",
					coltype:$("input[@name='coltype'][@checked]").val(),
					auto:$("input[@name='auto'][@checked]").val(),
					serial:"<s:property value="serial"/>"
				},
				function(data){
					data="<table width='100%' class='listtable'>"
					     +"<thead><tr><th width='10%'>"
					     +"<input type='checkbox' onclick='chkAllPort()' id='sel_1'><label for='sel_1'>ȫѡ</label></th>"
					     +"<th width='45%'>�ɼ���ʽ</th><th width='45%'>�ɼ�����</th>"+data+"</table>";
					  $("#div_port").html($(data));
				}
			);
	    }
	}
	function chkAllPort(){
		var chk=$("#sel_1").attr("checked");
		chk=chk?chk:false;
		$("input[@name='chk']").attr("checked",chk);
	}
	function dualPort(){
		$("#_div_port_flg").hide();

	}
	//��ȡ�澯ҳ��
	function getWarn(){
		if($("#td_warn").html()==""){
			$("#td_warn").html("���ڻ�ȡ�澯�������ȴ�......");
			$.post(
				"<s:url value="/performance/configFlux!getWarnJSP.action"/>",
				function(data){
					$("#td_warn").html($(data));
				}
			);
		}
	}
	//��ȡ�豸����״̬
	function getConfigFlux(){
		$.post(
			"<s:url value="/performance/configFlux!getConfigResult.action"/>",
			{
				device_id:"<s:property value="device_id"/>"
			},
			function(data){
				$("#tbody").html(data);
			}
		);
	}
	//��ʾ�˿���ϸ��Ϣ
	function showPortDetail(device_name,loopback_ip,device_id){
		var url="<s:url value="/performance/configFlux!getConfigPortList.action"/>?device_id="+device_id
		+"&device_name="+device_name+"&loopback_ip="+loopback_ip+"&t="+new Date();
		window.open(url);
	}
	//ɾ������
	function DelConfig(device_id){
		if(window.confirm("ɾ���󽫲�������ɼ���ȷ��Ҫɾ�����ã�")){
			$.post(
				"<s:url value="/performance/configFlux!DelFlux.action"/>",
				{
					device_id:device_id
				},
				function(data){
					if(data=="0"){
						alert("ɾ���ɹ���");
					}else if(data=="-1"){
						alert("ɾ��ʧ�ܣ�");
					}else if(data=="-2"){
						alert("����ɾ���ɹ���֪ͨ��̨ʧ�ܣ�");
					}else{
						alert("��ʱ�������ԣ�");
					}
					window.location=window.location;
				}
			);
		}
	}
	//
	function SaveFlux(device_id){
		//�澯
		if($("#td_warn").html()!=""){
			if(!$.checkFluxWarn()){
				return false;
			}
		}
		//�˿�
		if($("input[@name='total'][@checked]").val()==0){
			if($("input[@name='chk'][@checked]").length<1){
				alert("������ѡ��һ���˿ڣ�");
				return false;
			}
			var chk="";
			var param="";
			var flg=true;
			$("input[@name='chk'][@checked]").each(function(){
				chk+="-/-"+$(this).val();
				if($(this).parent().parent().find("table input[@checked]").length<1){
					flg=false;
				}
				$(this).parent().parent().find("table input[@checked]").each(function(){
					chk+="|||"+$(this).val();
				});
			});
			if(!flg){
				alert("�ɼ���������Ϊ�գ�������ѡ��һ���ɼ�������");
				return false;
			}
			chk=chk.substring(3);
			$("input[@name='port_info']").val(chk);
		}
		$("input[@name='dev_id']").val(device_id);
		$("form[@name='frm']").submit();
		return false;
	}
</script>
</head>
<body>
	<form name="frm" action="<s:url value="/performance/configFlux!saveFlux.action"/>" method="post">
	<input type="hidden" name="dev_id">
	<input type="hidden" name="device_id" value="<s:property value="device_id"/>">
	<input type="hidden" name="port_info">
	<br>
	<table width="94%" align="center" class="querytable">
		<tr>
			<s:if test="isbatch">
				<th colspan="2" class="title_1">������������</th>
			</s:if>
			<s:else>
				<th colspan="2" class="title_1">��������(<s:property value="device_name"/>��<s:property value="loopback_ip"/>��)</th>
			</s:else>
		</tr>
		<tr style="display:none">
			<td width="20%" class="title_2">�Զ�����</td>
			<td width="80%">
				<input type="radio" name="auto" value="1" checked id="a_0"><label for="a_0">&nbsp;��&nbsp;</label>&nbsp;
				<input type="radio" name="auto" value="0" id="a_1"><label for="a_1">&nbsp;��&nbsp;</label>&nbsp;
				<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">ע���뾡��ѡ���Զ�����
			</td>
		</tr>
		<tr id="tr_coltype" style="display:none;">
			<td class="title_2">�ɼ���ʽ</td>
			<td>
				<input type="radio" name="coltype" value="2_64" checked id="c_0"><label for="c_0">V2�汾64λ������</label>&nbsp;
				<input type="radio" name="coltype" value="1_32" id="c_1"><label for="c_1">V1�汾32λ������</label>&nbsp;
				<input type="radio" name="coltype" value="2_32" id="c_2"><label for="c_2">V2�汾32λ������ </label>&nbsp;
			</td>
		</tr>
		<tr style="display:<s:property value="isbatch?'none;':''"/>">
			<td class="title_2">��������</td>
			<td>
				<input type="radio" name="total" value="1" checked id="t_0"><label for="t_0">&nbsp;��&nbsp;</label>&nbsp;
				<input type="radio" name="total" value="0" id="t_1"><label for="t_1">&nbsp;��&nbsp;</label>&nbsp;
				<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">ע����˿ڹ����뾡��ѡ����������
			</td>
		</tr>
		<tr>
			<td class="title_2">�ɼ����ʱ��</td>
			<td>
				<input type="text" name="polltime" value="900" size="10">&nbsp;&nbsp;
				<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">ע���ɼ����ʱ����900�����ұȽϺ���
			</td>
		</tr>
		<tr>
			<td class="title_2">ԭʼ�����Ƿ����</td>
			<td>
				<input type="radio" name="intodb" value="1" checked id="i_0"><label for="i_0">&nbsp;��&nbsp;</label>&nbsp;
				<input type="radio" name="intodb" value="0" id="i_1"><label for="i_1">&nbsp;��&nbsp;</label>&nbsp;
			</td>
		</tr>
		<tr>
			<td class="title_2">�Ƿ���ԭ������</td>
			<td>
				<select name="iskeep">
					<option value="true">����ԭ������</option>
					<option value="false">��������</option>
				</select>
			</td>
		</tr>
		<tr style="display:<s:property value="isbatch?'none;':''"/>">
			<td class="title_2">����״̬</td>
			<td class="<s:property value="configresult==true?'':'no'"/>">
				<s:property value="configresult==true?'������':'δ����'"/>
			</td>
		</tr>
		<tr>
			<td colspan="2" class="title_1" name="td_show">
				���ø澯
				<img src="<s:url value="/images/ico_9.gif"/>" width="10" hight="12" align="center" border="0" alt="������ø澯" >
			</td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr id="tr_show" style="display:none;">
			<td id="td_warn" colspan="2">

			</td>
		</tr>
		<tr id="tr_total" style="display:none;">
			<td colspan="2">
				<div id="div_port" style="width:100%;height:350px;overflow:auto;">

				</div>
			</td>
		</tr>
		<tr>
			<td class="foot" colspan="2" align="right">
				<s:if test="isbatch">
					<button name="save" onclick="SaveFlux('<s:property value="device_id"/>');">&nbsp;��&nbsp;��&nbsp;</button>
				</s:if>
				<s:elseif test="configresult">
					<button name="save" onclick="SaveFlux('<s:property value="device_id"/>');">&nbsp;ˢ������&nbsp;</button>
					<button name="b_list" onclick="showPortDetail('<s:property value="device_name"/>',
					                                              '<s:property value="loopback_ip"/>',
					                                              '<s:property value="device_id"/>')">&nbsp;�˿��б�&nbsp;</button>
					<button name="del" onclick="DelConfig('<s:property value="device_id"/>')">&nbsp;ɾ������&nbsp;</button>
				</s:elseif>
				<s:else>
					<button name="save" onclick="SaveFlux('<s:property value="device_id"/>');">&nbsp;��&nbsp;��&nbsp;</button>
				</s:else>
				<s:if test="!ismodule">
					<button onclick="window.close();">&nbsp;��&nbsp;��&nbsp;</button>
				</s:if>
			</td>
		</tr>
	</table>
	<br>
	<s:if test="isbatch">
		<table width="94%" align="center" class="listtable">
			<thead>
				<tr>
					<th width="20%">�豸����</th>
					<th width="20%">�豸IP</th>
					<th width="10%">�豸����</th>
					<th width="20%">�豸�ͺ�</th>
					<th width="10%">�������</th>
					<th width="20%">����</th>
				</tr>
			</thead>
			<tbody id="tbody">
				<s:iterator var="cl" value="configList" status="rowstatus">
					<tr class="<s:property value="#rowstatus.odd==true?'odd':'even'"/>"
						onmouseover="className='odd'"
						onmouseout="className='<s:property value="#rowstatus.odd==true?'odd':'even'"/>'"
					>
						<td><s:property value="#cl.device_name"/></td>
						<td><s:property value="#cl.loopback_ip"/></td>
						<td><s:property value="#cl.vendor_name"/></td>
						<td><s:property value="#cl.device_model"/></td>
						<td class="<s:property value="#cl.result=='true'?'':'no'"/>"><s:property value="#cl.result=='true'?'������':'δ����'"/></td>
						<s:if test="#cl.result">
							<td>
								<a href="#" onclick="SaveFlux('<s:property value="#cl.device_id"/>');">ˢ������</a>
								<a href="#" onclick="showPortDetail('<s:property value="#cl.device_name"/>',
								                                    '<s:property value="#cl.loopback_ip"/>',
								                                    '<s:property value="#cl.device_id"/>')">�˿��б�</a>
								<a href="#" onclick="DelConfig('<s:property value="#cl.device_id"/>')">ɾ������</a>
							</td>
						</s:if>
						<s:else>
							<td>
								<a href="#" onclick="SaveFlux('<s:property value="#cl.device_id"/>');">��������</a>
							</td>
						</s:else>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</s:if>
	</form>
</body>
</html>
