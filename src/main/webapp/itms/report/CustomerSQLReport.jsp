<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script language="JavaScript">
var gw_type = '<s:property value="gw_type"/>';
function doQuery(){
	var custSQL = $.trim($("textarea[@name='custSQL']").val());            // 查询SQL
	custSQL = custSQL.replace("%", "%25").replace(new RegExp("'", 'g'), "[");
	custSQL = custSQL.replace(/\+/g, "]");
	$("textarea[@name='custSQL']").val(custSQL);
    if(custSQL == ""){
         alert("请输入查询SQL");
         return false;
    }
	var tmplSQL = custSQL.toLowerCase();
    if(tmplSQL.indexOf("select") == -1 || tmplSQL.indexOf("from") == -1 ||tmplSQL.indexOf("where") == -1){
        alert("定制的SQL必须包含'select'、'from'、'where'等关键字，请优化");
        return false;
    }
    if(tmplSQL.indexOf("*") > 0){
        alert("定制的SQL中包含了'*'，请指定明确的查询字段");
        return false;
    }
    if(tmplSQL.split("select").length-1 > 1){
        alert("定制的SQL中超过两层嵌套查询，请优化");
        return false;
    }
    /**$("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    $("button[@name='button']").attr("disabled", true);
    var url = '<s:url value='/itms/report/customerSQLReport!queryList.action'/>'; 
	$.post(url,{
		custSQL:custSQL
	},function(ajax){
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
		 $("button[@name='button']").attr("disabled", false);
	});**/
    document.selectForm.submit();
	
}
function ToExcel(){
	var custSQL = $.trim($("textarea[@name='custSQL']").val());            // 查询SQL
	custSQL = custSQL.replace("%", "%25").replace(new RegExp("'", 'g'), "[");
	custSQL = custSQL.replace(/\+/g, "]");
	var url = "<s:url value='/itms/report/customerSQLReport!queryListCount.action'/>";
	$.post(url, {
		custSQL:custSQL
	}, function(ajax) {
		var total=parseInt(ajax);
		if(ajax>100000){
	   alert("数据量太大不支持导出 ");
	   return;
		}
		else{

			var mainForm = document.getElementById("form");
			mainForm.action="<s:url value='/itms/report/customerSQLReport!getAllResultExcel.action'/>";
			mainForm.submit();
		    mainForm.action="<s:url value='/itms/report/customerSQLReport!queryList.action'/>";
		}
	});
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
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

</script>

<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>
						自定义查询报表
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
						定制查询SQL
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form id="form" name="selectForm" action="<s:url value='/itms/report/customerSQLReport!queryList.action'/>"
			target="dataForm">
				<table class="querytable">
					<tr>
						<th colspan=4>
							自定义查询报表
						</th>
						
					</tr>
					<TR bgcolor="#FFFFFF" id="" STYLE="">
						<TD align="right" class=column width="10%">
							SQL查询语句
						</TD>
						<TD align="left" width="60%" >
                               <textarea name="custSQL" cols="100" rows="4" class=bk></textarea>
						</TD>
						<TD align="left" width="30%" colspan=2>
                               <font color="red">1、定制的SQL必须包含'select'、'from'、'where'等关键字<br>2、定制的SQL中不能用'*'，需指定明确的查询字段<br>3、定制的SQL中不支持嵌套查询<br>4、不支持导出大于10W的数据量</font>
						</TD>
					</TR>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()" name="button">
								&nbsp;统 计&nbsp;
							</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>

	<!-- tr id="trData" style="display: none">
		<td class="colum">
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				正在统计，请稍等....
			</div>
		</td>
	</tr-->
	<tr>
		<td>
			<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
				scrolling="no" width="100%" src="">
				
				</iframe>
		</td>
	</tr>
	<tr>
		<td height="20">
		</td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>

