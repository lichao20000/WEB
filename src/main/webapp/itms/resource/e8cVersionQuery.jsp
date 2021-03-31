<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<link href="<s:url value="/css3/css_s.css"/>" rel="stylesheet"
	type="text/css" />
<meta http-equiv="x-ua-compatible" content="IE=7" >
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/slide.js"/>"></script>
<script type="text/javascript"
	src="../../Js/My97DatePicker/WdatePicker.js"></script>
<title>e8-c�淶�汾��ѯ</title>
<script language="JavaScript">
var operType = <%=request.getParameter("operType")%>;
//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=["dataForm"]

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
   			dyniframe[i].style.display="block"
   			//����û����������NetScape
   			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
    				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
    			//����û����������IE
   			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
    				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
 			 }
 		}
		//�����趨�Ĳ���������֧��iframe�����������ʾ����
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
  		tempobj.style.display="block"
		}
	}
}

$(function(){
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

function getDeviceModel(oselect){
	var url = "<s:url value='/itms/resource/E8cVersionQuery!getDevModelSelectList.action'/>";
	var vendor_id = oselect.value;
	$.post(url,{
		vendor_id:vendor_id
	},function(ajax){
		parseMessageModel(ajax);
	});
	
	//������ѯ�豸�ͺ�
	function parseMessageModel(ajax){
		var devModelObj = $("#selectDevModel");
		var str = '';
		devModelObj.html("");
		str = "<option value=\"-1\" selected>==��ѡ���ͺ�==</option>";
		str += ajax;
		devModelObj.append(str);
	}
}

function doQuery(){
    $("#data").show();
    $("#QueryData").html("���ڲ�ѯ�����Ե�....");
	$("input[@name='button']").attr("disabled", true);
	$("input[@name='button']").css("backgroundColor","#AAAAAA");
	var form = document.getElementById("frm");
	form.action ="<s:url value='/itms/resource/E8cVersionQuery!getE8cVersionList.action'/>?operType="+operType;
	form.submit();
}

function closeDialog(){
	$("#divData").hide();
	$("#QueryData").hide();
	$("#wall").hide();
}
</script>
</head>
<body>
	<form name="frm" id="frm" method="post" target="dataForm">
		<div class="it_main">
			<table width="100%" border="0" cellspacing="10" cellpadding="0" class="mainSearch" bgcolor="#f2f5ff">
				<tr>
				    <td class="tit">�豸����:</td>
				    <td>
			      		<s:select list="vendorList" name="vendor_id" headerKey="-1" 
								headerValue="��ѡ����" listKey="vendor_id" listValue="vendorName" 
								value="vendor_id" onchange="getDeviceModel(this);" theme="simple" cssClass="gj_select"/>
				    </td>
				    <td class="tit">�豸�ͺ�:</td>
				    <td>
				    	<select id="selectDevModel" name="device_model" class="gj_select">
							<option value="-1">==��ѡ����==</option>
						</select>
				    </td>
				    <td class="tit">�豸����:</td>
				    <td>
				    	<s:select list="devTypeMap" name="device_type"
							headerKey="-1" headerValue="��ѡ���豸����" listKey="type_id"
							listValue="type_name" cssClass="gj_select"></s:select>
				    </td>
				  </tr>
				  <tr>
				    <td class="tit">�ն˹��:</td>
				    <td>
				    	<s:select list="specList" name="spec_id" headerKey="-1"
							headerValue="��ѡ���ն˹��" listKey="id" listValue="spec_name"
							value="spec_id" cssClass="bk"></s:select>
					</td>
				    <td class="tit">��ʼʱ��:</td>
				    <td>
				    	<input type="text" name="starttime" readonly value="<s:property value='starttime'/>">
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" /> 
				    </td>
				    <td class="tit">����ʱ��:</td>
				    <td>
				    	<input type="text" name="endtime" readonly value="<s:property value='endtime'/>"> 
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" /> 
				    </td>
				 </tr>
				 <tr>
			</table>
			 <div class="mainSearch_btn" style="background-color: #d2dff8">
				 <input name="button" type="button" onclick="doQuery();" class="it_btn" value="��ѯ" />
			</div>
			<div id="data" style="display: none">
				<div id="QueryData">
					���ڲ�ѯ�����Ե�....
				</div>
			</div>
			<div style="height: 20px;">
			</div>
			<div class="it_table">
				<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe>
			</div>
			<div id="divData" style="display: none;position: absolute;z-index: 1200">
				<div id="QueryData">
				</div>
			</div>
		</div>
		<div id="wall" class="wall"></div>
	</form>
</body>
</html>