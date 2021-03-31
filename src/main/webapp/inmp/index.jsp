<%@ include file="../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>终端综合管理系统-ITMS</title>
<meta http-equiv="X-UA-Compatible" content="IE=EDGE">  
<link href="../css/inmp/ITMScss.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../Js/inmp/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../Js/inmp/jquery.tabso_yeso.js"></script>
<script type="text/jscript" src="../Js/inmp/itms.js"></script>
<script type="text/javascript" src="<s:url value="../Js/inmp/commFunction.js"/>"></script>
<script type="text/javascript" src="../Js/inmp/echarts-all.js"></script>
<script type="text/javascript">
var iframeSrc=["<s:url value='/inmp/resource/QueryDevice.jsp?gw_type=1'/>","<s:url value='/inmp/resource/itmsRelease.jsp?gw_type=1'/>","<s:url value='/inmp/resource/itmsInst!init.action?gw_type=1'/>",
			   "<s:url value='/inmp/bss/ChangeConnectionType.jsp?gw_type=1'/>","<s:url value='/inmp/bss/bssSheetServ!init.action?gw_type=1&netServUp=true'/>","<s:url value='/inmp/bss/ServiceDone.jsp?gw_type=1'/>",
			   "<s:url value='/inmp/diagnostics/jt_device_zendan_from3.jsp?gw_type=1'/>","<s:url value='/inmp/diagnostics/jt_device_zendan_from4.jsp?gw_type=1'/>","<s:url value='/inmp/diagnostics/diagnosticsProcess.jsp?gw_type=1'/>",
			   "<s:url value='/inmp/software/software!init.action?gw_type=1'/>","<s:url value='/inmp/softwareUp/softUpgrade.jsp?gw_type=1'/>"	   
		];
function changeNav(index){
	if(index == 11){
		this.div_pie.style.display="";
		this.div_bar.style.display="";
		this.myiframe.style.display = "none";
		window.location.href = "/itms/inmp/index.jsp";
	}else{
		this.div_pie.style.display="none";
		this.div_bar.style.display="none";
		this.myiframe.style.display = "";
		document.all("myiframes").src = iframeSrc[index];
		
	}
	//document.all("tr_other").style.display="";
}
//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["myiframes"];

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes";

function dyniframesize() 
{
	var dyniframe=new Array();
	for (var i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
     			dyniframe[i].style.display="block";
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
    		tempobj.style.display="block";
		}
	}
	
}

</script>
</head>

<body>
	<div class="itms_main1">
	  <!-- <div class="gross">
	    <div class="gross_num" id="totalPieTerminal">0</div>
	    <div class="gross_num" id="totalPieVersion" style="display: none;">0</div>
	    <div class="gross_num" id="totalUser" style="display: none;">0</div>
	    <div class="gross_num" id="totalPieMTerminal" style="display: none;">0</div>
	  </div> -->
	  <ul class="itms_main1tab" id="itmsMain1tab">
	  	<li class="itms_main1tab1"><a>终端</a></li>
	     <li class="itms_main1tab2"><a>版本</a></li>
	     <li class="itms_main1tab3"><a>用户</a></li>
	    <li class="itms_main1tab4"><a>管控终端</a></li>
	  </ul>
	  <ul class="itms_main1tab_content" id="itmsMain1tab_content">
	    <li id="itmsMain1tab_content1">
	      <table border="0" cellspacing="0" cellpadding="0" class="main1table">
	        <tr>
	        <td>
	        	<div class="gross">
			    <div class="gross_tit">总量</div>
			    <div class="gross_num" id="totalPieTerminal">0</div>
			    <div class="gross_dw">（单位:万）</div>
			  </div>
			</td>
	          <td><div class="main1chatBox">
	              <h2><span class="title">E8-C</span><span class="number" id="e8cnumgw">0</span></h2>
	              <div class="ITMSpiechat" id="gateway1"></div>
	            </div></td>
	          <td><div class="main1chatBox">
	              <h2><span class="title">E8-B</span><span class="number" id="e8bnumgw">0</span></h2>
	              <div class="ITMSpiechat" id="gateway2"></div>
	            </div></td>
	          <td><div class="main1chatBox">
	              <h2><span class="title">政企</span><span class="number" id="hwnumgw">0</span></h2>
	              <div class="ITMSpiechat" id="gateway3"></div>
	            </div></td>
	        </tr>
	      </table>
	    </li>
	    
	     <li id="itmsMain1tab_content2" style="display: none;">
	      <table border="0" cellspacing="0" cellpadding="0" class="main1table">
	        <tr>
	        <td><div class="gross">
			    <div class="gross_tit">总量</div>
			    <div class="gross_num" id="totalPieVersion">0</div>
			    <div class="gross_dw">（单位:万）</div>
			  	</div>
			 </td>
	          <td><div class="main1chatBox">
	              <h2><span class="title">E8-C</span><span class="number" id="e8cversion">0</span></h2>
	              <div class="ITMSpiechat" id="version1"></div>
	            </div></td>
	          <td><div class="main1chatBox">
	              <h2><span class="title">E8-B</span><span class="number" id="e8bversion">0</span></h2>
	              <div class="ITMSpiechat" id="version2"></div>
	            </div></td>
	          <td></td>
	        </tr>
	      </table>
	    </li>
	    <li id="itmsMain1tab_content3" style="display: none;">
	      <table border="0" cellspacing="0" cellpadding="0" class="main1table">
	        <tr>
	        <td>
	        <div class="gross">
			    <div class="gross_tit">总量</div>
			    <div class="gross_num" id="totalUser">0</div>
			    <div class="gross_dw">（单位:万）</div>
		  	</div>
		  	</td>
	          <td><div class="main1chatBox">
	              <h2><span class="title">E8-C</span><span class="number" id="e8cuser">0</span></h2>
	              <div class="ITMSpiechat" id="user1"></div>
	            </div></td>
	          <td><div class="main1chatBox">
	              <h2><span class="title">E8-B</span><span class="number" id="e8buser">0</span></h2>
	              <div class="ITMSpiechat" id="user2"></div>
	            </div></td>
	          <td><div class="main1chatBox">
	              <h2><span class="title">E8-B</span><span class="number" id="hwuser">0</span></h2>
	              <div class="ITMSpiechat" id="user3"></div>
	            </div></td>
	        </tr>
	      </table>
	    </li>
	    <li id="itmsMain1tab_content4" style="display: none;">
	      <table border="0" cellspacing="0" cellpadding="0" class="main1table">
	        <tr>
	        	<td>
	        		<div class="gross">
					    <div class="gross_tit">总量</div>
					    <div class="gross_num" id="totalPieMTerminal">0</div>
					    <div class="gross_dw">（单位:万）</div>
				  </div>
			  	</td>
	          <td><div class="main1chatBox">
	              <h2><span class="title">E8-C</span><span class="number" id="e8cMterminal">0</span></h2>
	              <div class="ITMSpiechat" id="managerGW1"></div>
	            </div></td>
	          <td><div class="main1chatBox">
	              <h2><span class="title">E8-B</span><span class="number" id="e8bMterminal">0</span></h2>
	              <div class="ITMSpiechat" id="managerGW2"></div>
	            </div></td>
	          <td><div class="main1chatBox">
	              <h2><span class="title">政企</span><span class="number" id="hwMterminal">0</span></h2>
	              <div class="ITMSpiechat" id="managerGW3"></div>
	            </div></td>
	        </tr>
	      </table>
	    </li>
	  </ul>
	</div>
	<!--终端、版本、用户、管控终端结束-->
	<div class="itms_main1">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td>
	      <div class="dash_wrap">
	        <div class="dash_tit">用户自动绑定率</div>
	        <div class="time_line"><a class="year_btn" id="preyear1">上一年</a><span class="dash_time" id="year1">
		</span><a class="year_btn" id="subyear1">下一年</a></div>
	        <div class="dash_box">
	          <div class="dash_chart" id="itmscolumechat1"></div>
	          <div class="zq_btnp"><a class="zq_btn" id="zq_btn1">钻取</a></div>
	        </div>
	        <div class="mouth_line">
	        <select id="month1">
	        	<option value="1">1月</option>
	        	<option value="2">2月</option>
	        	<option value="3">3月</option>
	        	<option value="4">4月</option>
	        	<option value="5">5月</option>
	        	<option value="6">6月</option>
	        	<option value="7">7月</option>
	        	<option value="8">8月</option>
	        	<option value="9">9月</option>
	        	<option value="10">10月</option>
	        	<option value="11">11月</option>
	        	<option value="12">12月</option>
	        </select><span class="mouth_num" id="userNum">0.00%</span>
	        <a class="zq_btn" id="zq_month1">钻取</a></div>
	      </div>
	    </td>
	    <td><div class="dash_wrap">
	        <div class="dash_tit">业务一次下发成功率</div>
	        <div class="time_line"><a class="year_btn" id="preyear2">上一年</a><span class="dash_time" id="year2">2014年</span><a class="year_btn" id="subyear2">下一年</a></div>
	        <div class="dash_box">
	          <div class="dash_chart" id="itmscolumechat2"></div>
	          <div class="zq_btnp"><a class="zq_btn" id="zq_btn2">钻取</a></div>
	        </div>
	        <div class="mouth_line">
	        <select id="month2">
	        	<option value="1">1月</option>
	        	<option value="2">2月</option>
	        	<option value="3">3月</option>
	        	<option value="4">4月</option>
	        	<option value="5">5月</option>
	        	<option value="6">6月</option>
	        	<option value="7">7月</option>
	        	<option value="8">8月</option>
	        	<option value="9">9月</option>
	        	<option value="10">10月</option>
	        	<option value="11">11月</option>
	        	<option value="12">12月</option>
	        </select>
	        <span class="mouth_num" id="bussNum">0.00%</span>
	        <a class="zq_btn" id="zq_month2">钻取</a></div>
	      </div></td>
	    <td><div class="dash_wrap">
	        <div class="dash_tit">新用户多PVC部署</div>
	        <div class="time_line"><a class="year_btn" id="preyear3">上一年</a><span class="dash_time" id="year3">2014年</span><a class="year_btn" id="subyear3">下一年</a></div>
	        <div class="dash_box">
	          <div class="dash_chart" id="itmscolumechat3"></div>
	          <div class="zq_btnp"><a class="zq_btn" id="zq_btn3">钻取</a></div>
	        </div>
	        <div class="mouth_line">
	        <select id="month3">
	        	<option value="1">1月</option>
	        	<option value="2">2月</option>
	        	<option value="3">3月</option>
	        	<option value="4">4月</option>
	        	<option value="5">5月</option>
	        	<option value="6">6月</option>
	        	<option value="7">7月</option>
	        	<option value="8">8月</option>
	        	<option value="9">9月</option>
	        	<option value="10">10月</option>
	        	<option value="11">11月</option>
	        	<option value="12">12月</option>
	        </select>
	        <span class="mouth_num" id="pvcNum">0.00%</span>
	        <a class="zq_btn" id="zq_month3">钻取</a></div>
	      </div></td>
	    <td><div class="dash_wrap">
	        <div class="dash_tit">终端版本规范率</div>
	        <div class="time_line"><a class="year_btn" id="preyear4">上一年</a><span class="dash_time" id="year4">2014年</span><a class="year_btn" id="subyear4">下一年</a></div>
	        <div class="dash_box">
	          <div class="dash_chart" id="itmscolumechat4"></div>
	          <div class="zq_btnp" id="zq_month1"><a class="zq_btn" id="zq_btn4">钻取</a></div>
	        </div>
	        <div class="mouth_line">
	        <select id="month4">
	        	<option value="1">1月</option>
	        	<option value="2">2月</option>
	        	<option value="3">3月</option>
	        	<option value="4">4月</option>
	        	<option value="5">5月</option>
	        	<option value="6">6月</option>
	        	<option value="7">7月</option>
	        	<option value="8">8月</option>
	        	<option value="9">9月</option>
	        	<option value="10">10月</option>
	        	<option value="11">11月</option>
	        	<option value="12">12月</option>
	        </select>
	        <span class="mouth_num" id="versionNum">0.00%</span>
	        <a class="zq_btn" id="zq_month4">钻取</a></div>
	      </div></td>
	  </tr>
	</table>
	</div>
	<div class="dialog_wall" id="autoBindWall"  style="display: none;"></div>
	<div class="zq_dialog" id="autoBind" style="display: none;">
	  <div class="dialogTitle"><h1>用户自动绑定率</h1>
	    <span class="close">×</span>
	  </div>
	  <div class="dialogContent" >
	    <div class="zq_chartstit"><h2>用户自动绑定率</h2>
	    <span class="zqchart_choose">我想选择:
	    		<input type="hidden" id="dateType" value="1">
	    		<select name="year1" id="yearSel1">
	    				<option value="2018">2018年</option>
	    				<option value="2017">2017年</option>
	    				<option value="2016">2016年</option>
	    				<option value="2015">2015年</option>
	    				<option value="2014">2014年</option>
	    				<option value="2013">2013年</option>
	    				<option value="2012">2012年</option>
	    				<option value="2011">2011年</option>
	    		 </select>
	    		 <select name="month1" id="monthSel1">
		        	<option value="1">1月</option>
		        	<option value="2">2月</option>
		        	<option value="3">3月</option>
		        	<option value="4">4月</option>
		        	<option value="5">5月</option>
		        	<option value="6">6月</option>
		        	<option value="7">7月</option>
		        	<option value="8">8月</option>
		        	<option value="9">9月</option>
		        	<option value="10">10月</option>
		        	<option value="11">11月</option>
		        	<option value="12">12月</option>
		        </select>
		    </span>
		</div>
	    <div class="zq_columnchart" >
	      	<span class="zq_cpm"><label for="">
	      	<input type="hidden" id="flag" value="desc">
	      	<input name="" type="checkbox" value="" id="cbx1" />&nbsp;按排名显示</label></span>
	    	<div style="height: 25px;"></div>
	    	<div class="itmscolumechat" id="columechat1" ></div>
	    </div> 
	  </div>
	</div>
	<div class="dialog_wall" id="bussWall"  style="display: none;"></div>
	<div class="zq_dialog" id="buss" style="display: none;">
	  <div class="dialogTitle"><h1>业务一次下发成功率</h1>
	    <span class="close">×</span>
	  </div>
	  <div class="dialogContent" id="bussContent" >
	    <div class="zq_chartstit"><h2>业务一次下发成功率</h2>
	    	<span class="zqchart_choose">我想选择:
	    	<input type="hidden" id="dateType" value="1">
	    		<select name="year2" id="yearSel2">
	    				<option value="2018">2018年</option>
	    				<option value="2017">2017年</option>
	    				<option value="2016">2016年</option>
	    				<option value="2015">2015年</option>
	    				<option value="2014">2014年</option>
	    				<option value="2013">2013年</option>
	    				<option value="2012">2012年</option>
	    				<option value="2011">2011年</option>
	    		 </select>
	    		 <select name="month2" id="monthSel2">
		        	<option value="1">1月</option>
		        	<option value="2">2月</option>
		        	<option value="3">3月</option>
		        	<option value="4">4月</option>
		        	<option value="5">5月</option>
		        	<option value="6">6月</option>
		        	<option value="7">7月</option>
		        	<option value="8">8月</option>
		        	<option value="9">9月</option>
		        	<option value="10">10月</option>
		        	<option value="11">11月</option>
		        	<option value="12">12月</option>
		        </select>
		    </span>
		</div>
	    <div class="zq_columnchart" >
	    	<input type="hidden" id="flag" value="desc">
	      	<span class="zq_cpm"><label for=""><input name="" type="checkbox" value="" id="cbx2" />&nbsp;按排名显示</label></span>
	    	<div style="height: 25px;"></div>
	    	<div class="itmscolumechat" id="columechat2" ></div>
	    </div>    
	  </div>
	</div>
	<div class="dialog_wall" id="pvcWall"  style="display: none;"></div>
	<div class="zq_dialog" id="pvc" style="display: none;">
	  <div class="dialogTitle"><h1>新用户多PVC部署</h1>
	    <span class="close">×</span>
	  </div>
	  <div class="dialogContent" id="pvcContent" >
	    <div class="zq_chartstit"><h2>新用户多PVC部署</h2>
		    <span class="zqchart_choose">我想选择:
		    <input type="hidden" id="dateType" value="1">
	    		<select name="year3" id="yearSel3">
	    				<option value="2018">2018年</option>
	    				<option value="2017">2017年</option>
	    				<option value="2016">2016年</option>
	    				<option value="2015">2015年</option>
	    				<option value="2014">2014年</option>
	    				<option value="2013">2013年</option>
	    				<option value="2012">2012年</option>
	    				<option value="2011">2011年</option>
	    		 </select>
	    		 <select name="month3" id="monthSel3">
		        	<option value="1">1月</option>
		        	<option value="2">2月</option>
		        	<option value="3">3月</option>
		        	<option value="4">4月</option>
		        	<option value="5">5月</option>
		        	<option value="6">6月</option>
		        	<option value="7">7月</option>
		        	<option value="8">8月</option>
		        	<option value="9">9月</option>
		        	<option value="10">10月</option>
		        	<option value="11">11月</option>
		        	<option value="12">12月</option>
		        </select>
		    </span>
	    </div>
	    <div class="zq_columnchart">
	    	<input type="hidden" id="flag" value="desc">
	      	<span class="zq_cpm"><label for=""><input name="" type="checkbox" value="" id="cbx3" />&nbsp;按排名显示</label></span>
	    	<div style="height: 25px;"></div>
	    	<div class="itmscolumechat" id="columechat3" ></div>
	    </div>    
	  </div>
	</div>
	<div class="dialog_wall" id="versionDevWall"  style="display: none;"></div>
	<div class="zq_dialog" id="versionDev" style="display: none;">
	  <div class="dialogTitle"><h1>终端版本规范率</h1>
	    <span class="close">×</span>
	  </div>
	  <div class="dialogContent" id="versionDevContent" >
	    <div class="zq_chartstit"><h2>终端版本规范率</h2>
	    	<span class="zqchart_choose">我想选择:
	    	<input type="hidden" id="dateType" value="1">
	    		<select name="year4" id="yearSel4">
	    				<option value="2018">2018年</option>
	    				<option value="2017">2017年</option>
	    				<option value="2016">2016年</option>
	    				<option value="2015">2015年</option>
	    				<option value="2014">2014年</option>
	    				<option value="2013">2013年</option>
	    				<option value="2012">2012年</option>
	    				<option value="2011">2011年</option>
	    		 </select>
	    		 <select name="month4" id="monthSel4">
		        	<option value="1">1月</option>
		        	<option value="2">2月</option>
		        	<option value="3">3月</option>
		        	<option value="4">4月</option>
		        	<option value="5">5月</option>
		        	<option value="6">6月</option>
		        	<option value="7">7月</option>
		        	<option value="8">8月</option>
		        	<option value="9">9月</option>
		        	<option value="10">10月</option>
		        	<option value="11">11月</option>
		        	<option value="12">12月</option>
		        </select>
		    </span>
		</div>
	    <div class="zq_columnchart" >
	   		<input type="hidden" id="flag" value="desc">
	      	<span class="zq_cpm"><label for=""><input name="" type="checkbox" value="" id="cbx4" />&nbsp;按排名显示</label></span>
	    	<div style="height: 25px;"></div>
	    	<div class="itmscolumechat" id="columechat4" ></div>
	    </div>   
	  </div>
	 </div>
</body>
<script type="text/javascript" src="../Js/inmp/echarts/pieTerminal.js"></script>
<script type="text/javascript" src="../Js/inmp/echarts/pieVersion.js"></script>
<script type="text/javascript" src="../Js/inmp/echarts/pieUser.js"></script>
<script type="text/javascript" src="../Js/inmp/echarts/pieMTerminal.js"></script>
<script type="text/javascript" src="../Js/inmp/echarts/autoBind.js"></script>
<script type="text/javascript" src="../Js/inmp/echarts/buss.js"></script>
<script type="text/javascript" src="../Js/inmp/echarts/pvc.js"></script>
<script type="text/javascript" src="../Js/inmp/echarts/versionDev.js"></script>
<script type="text/javascript" src="../Js/inmp/echarts/barAutoBind.js"></script> 
<script type="text/javascript" src="../Js/inmp/echarts/barBuss.js"></script>
<script type="text/javascript" src="../Js/inmp/echarts/barPvc.js"></script>
<script type="text/javascript" src="../Js/inmp/echarts/barVersionDev.js"></script>
<script type="text/javascript" SRC="../Js/inmp/jquery.blockUI.js"></SCRIPT>
</html>
