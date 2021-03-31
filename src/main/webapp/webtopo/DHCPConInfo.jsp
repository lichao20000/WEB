<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%--
/**
 * ��jsp��������Ϣ
 * REQ:XXXX-XXXX-XXXX
 * BUG:XXXX-XXXX-XXXX
 * @author wangzhimeng(����) tel��1234567890123
 * @version 1.0
 * @since 2008-6-5 ����02:28:24
 *
 * ��Ȩ���Ͼ������Ƽ� ���ܿƼ���
 *
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>DHCP������Ϣ</title>
<script type="text/javascript" src="<s:url value="/Js/edittable.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryCheckForm-linkage.js"/>"></script>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css"></link>
<script type="text/javascript">
var paramsSnmp=[{oid_type:313,type:0},
			{oid_type:339,type:1},
			{oid_type:340,type:1},
			{oid_type:341,type:1},
			{oid_type:344,type:1},
			{oid_type:345,type:1},
			{oid_type:342,type:1},
			{oid_type:343,type:1}];
var paramsTR069=[{oid_type:377,type:1},
				 {oid_type:385,type:1},
				 {oid_type:378,type:1},
				 {oid_type:379,type:1},
				 {oid_type:382,type:1},
				 {oid_type:385,type:1},
				 {oid_type:380,type:1},
				 {oid_type:381,type:1}];
var gatherType=0;//�ɼ���ʽĬ����snmp��ʽ
var index=0;
var firstinit=0;//����0�t���ʼ����B

$(function(){
	getSnmpInfo();
	$("a").click(function(){
		//onclick="initialize('outTable',0,0)"
		$("td[@name='c_type']").html($("option[@selected]").html());
		$("td[@name='dhcp_td_g']").html($("td[@name='dhcp_td']").html());
		$("td[@name='add_td_g']").html($("td[@name='add_td']").html());
		$("#outTable").html($("#cp").html());
		var tmp="";
		$("table[@name='table_cp']").each(function(){
			tmp+=$(this).html();
		});
		$("#outTable").append(tmp);
		initialize('outTable',0,0);
	});

});
//��ȡsnmp���豸��Ϣ
function getSnmpInfo()
{
		var params=((gatherType==0)?paramsSnmp:paramsTR069);
		if(index>=params.length)
		{
		//initStaAndEndAddr();
		return;
		}
		var oid_type=params[index].oid_type;
		var type=params[index].type;
		$.ajax({url:"<s:url value="/bbms/GetSnmpInfo!getSnmpInfo.action"/>",type:"POST",data:{device_id:<s:property value="device_id"/>,oid_type:oid_type,type:type,gatherType:gatherType},success:function(data){
			if(oid_type==313||oid_type==377)
			{
				$("td[@snmp='313']").html("1"==data?"����":"0"==data?"ͣ��":data);
			}
			else if(oid_type==344||oid_type==382)
			{
				$("td[@snmp='344']").html(data);
			}
			else
			{
				var tmp=data.split("<br>");
				//���ָ��ɼ�����������и���
				if((data!="�ݲ�֧�ָ����Բɼ�")&&(data!="û�����òɼ�oid")&&(data!="�޷��ɼ�������")&&(data!=null)&&(data!=""))
				{
				if(firstinit==0)
				{
					for(var i=1;i<tmp.length;i++)
						{
							var con=$("div[@name='conBlock']:first").clone();
							$("#content").append(con);
						}
						firstinit=1;
				}
					if(gatherType==0)
					{
						$("td[@snmp='"+oid_type+"']").each(function(i){
						if(oid_type==345||oid_type==385)
						{
							$(this).html(tmp[i]=='1'?"����":tmp[i]=='0'?"ֹͣ":tmp[i]);
						}
						else
						{
							$(this).html(tmp[i])
						 }
					});
					}
					else
					{
						$("td[@tr069='"+oid_type+"']").each(function(i){
						if(oid_type==345||oid_type==385)
						{
							$(this).html(tmp[i]=='1'?"����":tmp[i]=='0'?"ֹͣ":tmp[i]);
						}
						else
						{
							$(this).html(tmp[i]);
						}
					});
					}
					}
				else
				{
				if(gatherType==0)
					{
						$("td[@snmp='"+oid_type+"']").each(function(i){
							$(this).html(data)
						});
					}
					else
					{
						$("td[@tr069='"+oid_type+"']").each(function(i){
							$(this).html(data)
						});
					}
				}

			}
		++index;
		getSnmpInfo();
	},error:function(){
		if(gatherType==0)
		{
			$("td[@snmp='"+oid_type+"']").each(function(i){
						$(this).text("�޷��ɼ�������");
					});
		}
		else
		{
				$("td[@tr069='"+oid_type+"']").each(function(i){
					$(this).text("�޷��ɼ�������");
				});
		}
		++index;
		getSnmpInfo();
	}});
}
//��ʼ����ʼ�ͽ�����ַ
function initStaAndEndAddr()
{
	$("div[@name='conBlock']").each(function(i){
		var netAddr = $("td[@snmp='340']",$(this)).text();
		var netMask=$("td[@snmp='341']",$(this)).text();
		if($.checkIP(netAddr)&&$.checkIP(netMask))
		{
			$.ajax({url:"<s:url value="/bbms/GetSnmpInfo!getHLAddress.action"/>",
		type:"POST",data:{netAddr:netAddr,netMask:netMask},success:function(data){
		var res = data.split("-");
		$("td[@snmp='addrs']:eq("+i+")").html(res[1]);
		$("td[@snmp='addre']:eq("+i+")").html(res[0]);
	},error:function(){alert("error");}});
		}
		else
		{
			$("td[@snmp='addrs']:eq("+i+")").html("�޷���ȡ��ʼ��ַ");
			$("td[@snmp='addre']:eq("+i+")").html("�޷���ȡ������ַ");
		}

	});
}
//���Ĳɼ���ʽ
function changeGtType()
{
	index=0;
	gatherType=$("select[@name='gatherType']").val();
	$("td[@snmp]").text("���ڼ�����......");
	getSnmpInfo();
}
</script>
<style type="text/css">
/*���ڵ���excel*/
td.column {
	background-color: #F4F4FF;
	color: #000000;
}

td.head {
	background-color: buttonface;
	border-left: solid #ffffff 1.5px;
	border-top: solid #ffffff 1.5px;
	border-right: solid #808080 1.8px;
	border-bottom: solid #808080 1.8px;
	color: #000000;
}
/*****************/
	table,tr,td
	{
		border-style: solid;
		border-width: 1px;
		border-color: black;
		border-collapse: collapse;
		border-spacing: 0px;
	}
	table
	{
		width: 100%;
	}
</style>
</head>
<body>
<div width="98%" align="right"
		style="margin-right: 10px; margin-top: 2px;"><a
		href="javascript://">������Excel</a>&nbsp;&nbsp;</div>
<table style="width: 100%;">
	<tr>
		<th>DHCP������Ϣ</th>
	</tr>
	<tr><td>
	<table>
		<tr><td>�ɼ���ʽ</td>
		<td>
		<select name="gatherType" onchange="changeGtType();">
		<option value="0">SNMP</option>
		<option value="1">TR069</option>
		</select>
		</td>
		</tr>
		<tr>
			<td class="column" width="30%">DHCP����״̬</td>
			<td snmp="313" tr069="377" name="dhcp_td">���ڼ�����......</td>
		</tr>
		<tr>
			<td class="column">��ַ��Ԥ����ַ�б�</td>
			<td snmp="344" tr069="382"  name="add_td">���ڼ�����......</td>
		</tr>
	</table>
	<div style="width:100%;height:220px;" id="content">
	<div name="conBlock" style="margin-top:10px;">
	<table name="table_cp">
		<tr>
			<td class="column" width="30%">��ַ���Ƿ�����</td>
			<td snmp="345" tr069="385">���ڼ�����......</td>
		</tr>
		<tr>
			<td class="column" >��ַ���б���Ϣ</td>
			<td snmp="339" tr069="386">���ڼ�����......</td>
		</tr>
		<tr>
			<td class="column">��ַ��������Ϣ</td>
			<td snmp="340" tr069="378">���ڼ�����......</td>
		</tr>
		<tr>
			<td class="column">��ַ��������Ϣ</td>
			<td snmp="341" tr069="379">���ڼ�����......</td>
		</tr>
		<tr>
			<td class="column" >��ַ����ʼ��ַ</td>
			<td snmp="342" tr069="380">���ڼ�����......</td>
		</tr>
		<tr>
			<td class="column" >��ַ�ؽ�����ַ</td>
			<td snmp="343" tr069="381">���ڼ�����......</td>
		</tr>
	</table>
	</div>
	</div>
	</td>
	</tr>
</table>
<div style="display:none;">
	<table id="outTable">
		
	</table>
	<table id="cp">
		<tr>
			<th>DHCP������Ϣ</th>
			<th>&nbsp;</th>
		</tr>
		<tr>
			<td>�ɼ���ʽ</td>
			<td name="c_type"></td>
		</tr>
		<tr>
			<td class="column" width="30%">DHCP����״̬</td>
			<td name="dhcp_td_g"></td>
		</tr>
		<tr>
			<td class="column">��ַ��Ԥ����ַ�б�</td>
			<td name="add_td_g"></td>
		</tr>
	</table>
</div>
</body>
</html>