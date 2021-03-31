<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%--
/**
 * �������Ƶ�����ģ�����á�ģ�����ù���ҳ��;
 * REQ:XXXX-XXXX-XXXX
 * BUG:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-Mail:benyp@lianchuang.com
 * @version 1.0
 * @since 2009-1-12 ����05:07:32
 *
 * ��Ȩ���Ͼ������Ƽ� ���ܿƼ���;
 *
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ģ�����ù���</title>
<lk:res/>
<script type="text/javascript">
	$(function(){
		$("select[@name='vendor_id']").change(function(){
			if($(this).val()==""){
				alert("��ѡ���̣�");
				return false;
			}
			getInitData($(this).val(),null,null);
		});
		//�������ָ��
		$("input[@name='configtype']").click(function(){
			if($("select[@name='vendor_id']").val()==""){
				alert("��ѡ���豸���̣�");
				$("select[@name='vendor_id']").focus();
				$("select[@name='vendor_id']").select();
				$(this).attr("checked",false);
				return false;
			}
			$("tr[@name='tr_pm_exp"+$(this).val().split("-/-")[0]+"']").toggle();
		});
	});

	//���
	function Add(configtype){
		$("#use_attr_"+configtype).append($("#can_attr_"+configtype+" option:selected"));
	}
	//ɾ��
	function Del(configtype){
		$("#can_attr_"+configtype).append($("#use_attr_"+configtype+" option:selected"));
	}
	//Check Form
	function CheckForm(){
		if($.trim($("input[@name='template_name']").val())==""){
			alert("������ģ�����ƣ�");
			$("input[@name='template_name']").focus();
			$("input[@name='template_name']").select();
			return false;
		}else if($("select[@name='vendor_id']").val()==""){
			alert("��ѡ���豸���̣�");
			$("select[@name='vendor_id']").focus();
			$("select[@name='vendor_id']").select();
			return false;
		}else if($("select[@name='serial']").val()==""){
			alert("��ѡ���豸�ͺ�!");
			$("select[@name='serial']").focus();
			$("select[@name='serial']").select();
			return false;
		}else if($("input[@name='configtype'][@checked]").length<1){
			alert("������ѡ��һ������ָ��!");
			$("input[@name='configtype']").focus();
			$("input[@name='configtype']").select();
			return false;
		}else{
			var target;
			var flg=true;
			var atrrvalue="";
			$("input[@name='configtype'][@checked]").each(function(){
				target="#use_attr_"+$(this).val().split("-/-")[0];
				if($(target).length==1){
					if($(target).html()==""){
						alert("��ѡ����ʽ��");
						$(target).focus();
						$(target).select();
						flg=false;
						return false;
					}else{
						$(target+" option").each(function(){
							atrrvalue+="/"+$(this).val();
						});
					}
				}
			});
			if(flg){
				$("input[@name='atrrvalue']").val(atrrvalue.substring(1));
				$.post(
					"<s:url value="/performance/moduleManage!checkNameExit.action"/>",
					{
						serial:$("select[@name='serial']").val()
					},
					function(data){
						if(data==""){
							$("form").submit();
						}else{
							if(window.confirm(data)){
								$("form").submit();
							}
						}
					}
				);
			}
			return false;
		}
		return false;
	}
	//��ȡ��ʼ������
	function getInitData(vendor_id,serial,configtype){
		$.post(
			"<s:url value="/performance/moduleManage!getSerialByVendor.action"/>",
			{
				vendor_id:vendor_id
			},
			function(data){
				$("select[@name='serial']").html(data);
				if(serial!=null && serial!=""){
					try{
						$("select[@name='serial'] option[value="+serial+"]").attr("selected",true);
					}catch(e){
						//alert(e);
					}
				}
			}
		);

		if($("input[@name='configtype_hidden']").val()==""){
			return;
		}
		$("input[@name='configtype']").attr("checked",false);
		if(configtype!=null && configtype!=""){
			$.post(
				"<s:url value="/performance/moduleManage!getConfigRelateTabColumn.action"/>",
				{
					configtype_hidden:$.cc($("input[@name='configtype_hidden']")),
					vendor_id:vendor_id,
					configtypestr:configtype,
					serial:serial
				},
				function(data){
					$("#tbody").html(data);
					var tmp=configtype.split(",");
					for(var i=0;i<tmp.length;i++){
						$("#ct_"+tmp[i]).attr("checked",true);
						$("tr[@name='tr_pm_exp"+tmp[i]+"']").show();
					}
				}
			);
		}else{
			$.post(
				"<s:url value="/performance/moduleManage!getRelateTabColumn.action"/>",
				{
					configtype_hidden:$.cc($("input[@name='configtype_hidden']")),
					vendor_id:vendor_id
				},
				function(data){
					$("#tbody").html(data);
				}
			);
		}
	}
	//�༭ģ��
	function Edit(serial,configtype,vendor_id,template_name){
		$("#m_id").html("�༭��"+template_name+"��ģ��");
		$("input[@name='template_name']").val(template_name);
		$("select[@name='vendor_id'] option[value="+vendor_id+"]").attr("selected",true);
		getInitData(vendor_id,serial,configtype);
	}

	//ɾ��ģ��
	function DelTemp(serial,target){
		$.post(
			"<s:url value="/performance/moduleManage!del.action"/>",
			{
				serial:serial
			},
			function(data){
				alert(data);
				window.location="<s:url value="/performance/moduleManage.action"/>";
			}
		);
	}
	//��ʼ��
	function init(){
		var ajax="<s:property value="ajax"/>";
		if(ajax!=null && ajax!=""){
			alert(ajax);
			window.location="<s:url value="/performance/moduleManage.action"/>";
		}
	}
	//���ģ��
	function showAdd(){
		$("#m_id").html("ģ�����");
		$("input[@name='template_name']").val("");
		$("select[@name='vendor_id']").attr("selectedIndex",0);
		$("input[@name='configtype']").attr("checked",false);
		$("select[@name='serial']").html("<option value=''>����ѡ����</option>");
		$("input[@name='atrrvalue']").val("");
		$("#tbody").html("");
	}
</script>
</head>
<body onload="init();">
	<form action="<s:url value="/performance/moduleManage!save.action"/>">
		<input type="hidden" name="atrrvalue">
	<br>
	<table width="94%" class="querytable" align="center">
		<tr>
			<th class="title_1" colspan="6">
				<label id="m_id">ģ�����</label>
			</th>
		</tr>
		<tr>
			<td class="title_2" width="10%">ģ������:</td>
			<td colspan="5">
				<input type="text" name="template_name">
			</td>
		</tr>
		<tr>
			<td class="title_2" width="10%">�豸����</td>
			<td colspan="2" width="40%">
				<select name="vendor_id">
					<s:property escapeHtml="false" value="vendor"/>
				</select>
			</td>
			<td class="title_2" width="10%">�豸�ͺ�</td>
			<td colspan="2" width="40%">
				<select name="serial">
					<option value="">����ѡ����</option>
				</select>
			</td>
		</tr>
		<tr>
			<td class="title_2" width="10%">����ָ��</td>
			<td colspan="5">
				<s:property value="configtypestr" escapeHtml="false"/>
				<input type="hidden" name="configtype_hidden" value="<s:property value="configtype_hidden"/>">
			</td>
		</tr>
		<tbody id="tbody">
		</tbody>
		<tr>
			<td class="foot" colspan="6" align="right">
				<button name="save" onclick="CheckForm();">&nbsp;��&nbsp;��&nbsp;</button>
			</td>
		</tr>
	</table>
	<br>
	<table align="center" width="94%">
		<tr>
			<td align="right"><a href="#" onclick="showAdd();">���ģ��</a></td>
		</tr>
	</table>
	<table width="94%" align="center" class="listtable">
		<thead>
			<tr>
				<th colspan="5">ģ���б�</th>
			</tr>
			<tr>
				<th width="20%">�豸����</th>
				<th width="20%">�豸�ͺ�</th>
				<th width="20%">ģ������</th>
				<th width="20%">����ָ��</th>
				<th width="20%">����</th>
			</tr>
		</thead>
		<tbody id="datatbody">
			<s:property escapeHtml="false" value="configStr"/>
		</tbody>
	</table>
	</form>
</body>
</html>
