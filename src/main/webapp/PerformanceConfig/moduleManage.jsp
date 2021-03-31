<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%--
/**
 * 【北京酒店网管模板配置】模板配置管理页面;
 * REQ:XXXX-XXXX-XXXX
 * BUG:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-Mail:benyp@lianchuang.com
 * @version 1.0
 * @since 2009-1-12 下午05:07:32
 *
 * 版权：南京联创科技 网管科技部;
 *
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>模板配置管理</title>
<lk:res/>
<script type="text/javascript">
	$(function(){
		$("select[@name='vendor_id']").change(function(){
			if($(this).val()==""){
				alert("请选择厂商！");
				return false;
			}
			getInitData($(this).val(),null,null);
		});
		//点击配置指标
		$("input[@name='configtype']").click(function(){
			if($("select[@name='vendor_id']").val()==""){
				alert("请选择设备厂商！");
				$("select[@name='vendor_id']").focus();
				$("select[@name='vendor_id']").select();
				$(this).attr("checked",false);
				return false;
			}
			$("tr[@name='tr_pm_exp"+$(this).val().split("-/-")[0]+"']").toggle();
		});
	});

	//添加
	function Add(configtype){
		$("#use_attr_"+configtype).append($("#can_attr_"+configtype+" option:selected"));
	}
	//删除
	function Del(configtype){
		$("#can_attr_"+configtype).append($("#use_attr_"+configtype+" option:selected"));
	}
	//Check Form
	function CheckForm(){
		if($.trim($("input[@name='template_name']").val())==""){
			alert("请输入模板名称！");
			$("input[@name='template_name']").focus();
			$("input[@name='template_name']").select();
			return false;
		}else if($("select[@name='vendor_id']").val()==""){
			alert("请选择设备厂商！");
			$("select[@name='vendor_id']").focus();
			$("select[@name='vendor_id']").select();
			return false;
		}else if($("select[@name='serial']").val()==""){
			alert("请选择设备型号!");
			$("select[@name='serial']").focus();
			$("select[@name='serial']").select();
			return false;
		}else if($("input[@name='configtype'][@checked]").length<1){
			alert("请至少选择一个配置指标!");
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
						alert("请选择表达式！");
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
	//获取初始化数据
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
	//编辑模板
	function Edit(serial,configtype,vendor_id,template_name){
		$("#m_id").html("编辑【"+template_name+"】模板");
		$("input[@name='template_name']").val(template_name);
		$("select[@name='vendor_id'] option[value="+vendor_id+"]").attr("selected",true);
		getInitData(vendor_id,serial,configtype);
	}

	//删除模板
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
	//初始化
	function init(){
		var ajax="<s:property value="ajax"/>";
		if(ajax!=null && ajax!=""){
			alert(ajax);
			window.location="<s:url value="/performance/moduleManage.action"/>";
		}
	}
	//添加模板
	function showAdd(){
		$("#m_id").html("模板添加");
		$("input[@name='template_name']").val("");
		$("select[@name='vendor_id']").attr("selectedIndex",0);
		$("input[@name='configtype']").attr("checked",false);
		$("select[@name='serial']").html("<option value=''>请先选择厂商</option>");
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
				<label id="m_id">模板添加</label>
			</th>
		</tr>
		<tr>
			<td class="title_2" width="10%">模板名称:</td>
			<td colspan="5">
				<input type="text" name="template_name">
			</td>
		</tr>
		<tr>
			<td class="title_2" width="10%">设备厂商</td>
			<td colspan="2" width="40%">
				<select name="vendor_id">
					<s:property escapeHtml="false" value="vendor"/>
				</select>
			</td>
			<td class="title_2" width="10%">设备型号</td>
			<td colspan="2" width="40%">
				<select name="serial">
					<option value="">请先选择厂商</option>
				</select>
			</td>
		</tr>
		<tr>
			<td class="title_2" width="10%">配置指标</td>
			<td colspan="5">
				<s:property value="configtypestr" escapeHtml="false"/>
				<input type="hidden" name="configtype_hidden" value="<s:property value="configtype_hidden"/>">
			</td>
		</tr>
		<tbody id="tbody">
		</tbody>
		<tr>
			<td class="foot" colspan="6" align="right">
				<button name="save" onclick="CheckForm();">&nbsp;添&nbsp;加&nbsp;</button>
			</td>
		</tr>
	</table>
	<br>
	<table align="center" width="94%">
		<tr>
			<td align="right"><a href="#" onclick="showAdd();">添加模板</a></td>
		</tr>
	</table>
	<table width="94%" align="center" class="listtable">
		<thead>
			<tr>
				<th colspan="5">模板列表</th>
			</tr>
			<tr>
				<th width="20%">设备厂商</th>
				<th width="20%">设备型号</th>
				<th width="20%">模板名称</th>
				<th width="20%">配置指标</th>
				<th width="20%">操作</th>
			</tr>
		</thead>
		<tbody id="datatbody">
			<s:property escapeHtml="false" value="configStr"/>
		</tbody>
	</table>
	</form>
</body>
</html>
