<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ page import= "java.util.*"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>文件导入查询机顶盒信息</title>
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
			type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript"
			src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
		<script type="text/javascript">
function query(){
    $("#querybtn").attr("disabled","disabled");
    document.selectForm.action = "<s:url value='/gtms/stb/report/batchImpQryStbInfo!impStb.action'/>";
    document.selectForm.target = "dataForm";
	document.selectForm.submit();
}

//根据需要选择高级查询选项
function ShowDialog(leaf){
	//pobj = obj.offsetParent;
	oTRs = document.getElementsByTagName("TR");
	var m_bShow;
	var setvalueTemp = 0;
	for(var i=0; i<oTRs.length; i++){
		if(oTRs[i].leaf == leaf){
			m_bShow = (oTRs[i].style.display=="none");
			oTRs[i].style.display = m_bShow?"":"none";
		}
	}
	if(m_bShow){
		setvalueTemp = "1";
	}
	setValue(setvalueTemp);
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

</script>
	</head>

	<body>
		<form name="selectForm" action="<s:url value='/gtms/stb/report/batchImpQryStbInfo!impStb.action'/>"
			target="dataForm">
			<input type="hidden" name="selectType" value="0" />
			<table>
				<tr>
					<td HEIGHT=20>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite">
									文件导入查询机顶盒信息
								</td>
								<td>
									<img src="<s:url value="/images/attention_2.gif"/>" width="15"
										height="12" />
									文件导入查询机顶盒信息
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table class="querytable">

							<TR>
								<th colspan="4">
									文件导入查询机顶盒信息
								</th>
							</tr>
							
							<tr id="gwShare_tr31" bgcolor="#FFFFFF" >
								<td align="right" width="15%">提交文件</td>
								<td colspan="3" width="85%">
									<div id="importUsername">
										<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="<s:url value="/gwms/share/FileUpload.jsp"/>" height="20" width="100%">
										</iframe>
										<input type="hidden" name=gwShare_fileName value=""/>
									</div>
								</td>
							</tr>
							<tr id="gwShare_tr32" >
								<td CLASS="green_foot" align="right">注意事项</td>
								<td colspan="3" CLASS="green_foot">
								1、需要导入的文件格式限于Excel、文本文件，即xls、txt格式 。
								 <br>
								2、文件的第一行为标题行，即【IPTV账号】、或者【设备序列号】。
								 <br>
								3、文件只有一列。
								 <br>
								4、文件行数不要超过2000行，以免影响性能。
								</td>
							</tr>
							<TR>
								<td colspan="4" align="right" class=foot>
								<button onclick="query()">&nbsp;导入查询&nbsp;</button>
								</td>
							</TR>
						</table>
					</td>
				</tr>
				<tr>
					<td height="25" id="resultStr">

					</td>
				</tr>
				<tr>
					<td>
						<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
							scrolling="no" width="100%" src=""></iframe>
					</td>
				</tr>
			</table>
			<br>
		</form>
	</body>
</html>