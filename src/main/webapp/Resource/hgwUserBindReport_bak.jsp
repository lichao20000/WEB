<html>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />
<%@ include file="../head.jsp"%>
<%request.setCharacterEncoding("GBK");
            String strCityList = DeviceAct.getCityListAll(false, curUser
                    .getCityId(), "", request);
            DateTimeUtil dateTimeUtil = new DateTimeUtil();
            String end_time = dateTimeUtil.getDate();
            dateTimeUtil.getNextDate(-1);
            String start_time = dateTimeUtil.getDate();
			String gw_type = request.getParameter("gw_type");
            %>
<style>
caption {font-size:12px;}
.errmsg {background:#ef8f00;}
#divDetail{
  position:absolute;z-index:255;top:200px;left:100px;width:70%;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;
}
</style>
<%@ include file="../toolbar.jsp"%>
<head>
<script type="text/javascript" src="../Js/My97DatePicker/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/jquery.js"></SCRIPT>

<script language="JavaScript">


function CheckForm(){   
  temp =document.all("start_time").value;
      if(temp=="-1" || temp=="")
	   {
	     alert("查询时间不能为空");
	     return false;
	   }
	   temp =document.all("end_time").value;
	   if(temp=="-1" || temp=="")
	   {
	     alert("查询时间不能为空");
	     return false;
	   }



return true;




}



var mouse_y = 200;

function doQuery(){
	CheckForm();
    var city_id = "";
	//if(document.all("city_id") == null || document.all("city_id").value == "" || document.all("city_id").value < 0){
    if($(frm.city_id)==null){
		alert("请选择属地");
		$(frm.city_id).focus();
		return false;
	}else{
		city_id = $(frm.city_id).val();
	}
    queryForCity(city_id);
}

function str2time(sdate) {
  var dt = sdate.split(" ");
  var ymd = dt[0].split("-");
  var hms = dt[1].split(":");
  var dat = new Date(ymd[0],ymd[1]-1,ymd[2],hms[0],hms[1],hms[2]);
  return dat.getTime()/1000;
}

function queryForPrint(){
	var cityId = document.frm2.importCityId.value;
    var stime = document.frm2.importStartTime.value;
    var etime =  document.frm2.importEndTime.value;
    var usertype= document.frm2.importUsertype.value;
    
    var url = "../BindRate/getBindRateByCityid!getBindRateByCityidPrint.action";	
	
	url = url + "?cityId="+cityId+"&startTime="+stime+"&endTime="+etime+"&usertype="+usertype;
	window.open(url,"","left=80,top=80,width=800,height=400,resizable=yes,scrollbars=yes");
}

function queryForExcel(){
	var cityId = document.frm2.importCityId.value;
    var stime = document.frm2.importStartTime.value;
    var etime =  document.frm2.importEndTime.value;
    var usertype= document.frm2.importUsertype.value;
   
    var url = "../BindRate/getBindRateByCityid!getBindRateByCityidExcel.action";	
	
	url = url + "?cityId="+cityId+"&startTime="+stime+"&endTime="+etime+"&usertype="+usertype;
	
	document.frm2.action = url;
	document.frm2.method = "post";
	document.frm2.submit();
}

function queryForPdf(){
	var cityId = document.frm2.importCityId.value;
    var stime = document.frm2.importStartTime.value;
    var etime =  document.frm2.importEndTime.value;
    var usertype= document.frm2.importUsertype.value;
    
    var url = "../BindRate/getBindRateByCityid!getBindRateByCityidPdf.action";	
	
	url = url + "?cityId="+cityId+"&startTime="+stime+"&endTime="+etime+"&usertype="+usertype;
	
	document.frm2.action = url;
	document.frm2.method = "post";
	document.frm2.submit();
}

var currentCityId="00";
function queryForCity(cityId){
    
    currentCityId = cityId;
    //PageLog("cityId="+cityId);
    $(frm.city_id).val(cityId);
    var url = "../BindRate/getBindRateByCityid!getBindRateByCityid.action";
    var stime = str2time($(frm.start_time).val());
    var etime = str2time($(frm.end_time).val());  
    var usertype=$(frm.usertype).val()
   
    
    
    //报表所加
    document.frm2.importCityId.value = cityId;
    document.frm2.importStartTime.value = stime;
    document.frm2.importEndTime.value = etime;
    document.frm2.importUsertype.value = usertype;
     
    $(msg).show();
    $(divBindRates).hide();
    $(divBindUsers).hide();
    //PageLog("<a target=_blank href='"+url+"?startTime="+stime+"&endTime="+etime+"&cityId="+cityId+"'>go</a>"); 
    $.post(url,{
      startTime:stime,
      endTime:etime,
      cityId:cityId,
      usertype:usertype
    },function(mesg){
      var retstr = $.trim(mesg);
      //PageLog("retstr="+retstr);
      if(retstr=="FAILURE"){
        alert("查询出错");
        $(divBindRates).hide();
      }else if(retstr==""){
        alert("查询结果为空");
        $(divBindRates).hide();
      }else{        
        $(divBindRates).show();
        var lines = retstr.split("\n");
        shtm  = "<table width=\"95%\" id=\"tblBindRates\" align=\"left\" border=\"0\" cellspacing=\"1\" cellpadding=\"2\">";
        
        
        //添加表格标题 add by zhangcong@ 2011-06-09
        shtm += "<tr bgcolor=\"#FFFFFF\">";
        shtm += "<td class=column1 height=\"25\">";
        shtm += "<strong>";
        shtm += "绑定率统计";
        shtm += "</strong>";
        shtm += "<td nowrap>";
        shtm += "</td>";
        shtm += "</td>";
        shtm += "</tr>";
        
        shtm += "<tr><td><table border=0 cellspacing=1 cellpadding=2 width=\"100%\" align=\"center\" bgcolor=\"#000000\">";
        shtm += "<tr bgcolor=\"#FFFFFF\"><th><strong>属地</strong><"+
                "/th><th><strong>已绑定数</strong><"+
                "/th><th><strong>未绑定数</strong><"+
                "/th><th><strong>绑定率</strong></th></tr>";
        
        //PageLog("["+lines[0]+"]");
        for(var i=0;i<lines.length;i++){
          var cells = lines[i].split("|");
          var cid = cells[0]; 
          if(cid==currentCityId || cid=="total"){
            shtm += "<tr bgcolor=\"#FFFFFF\"><td class=column><b>"+cells[1]+"</b></td>";
          }else{
            shtm += "<tr bgcolor=\"#FFFFFF\"><td class=column><a href=javascript:queryForCity('"+cid+"')>&nbsp;"+cells[1]+"</a></td>";
          }
          if(cid=="total"){
        	  shtm += "<td><b>"+cells[2]+"</b></td>";//
              var unopok = parseInt(cells[3])-parseInt(cells[2]);
              shtm += "<td><b>"+unopok+"</b></td>";//
          } else {
        	  	
        	    //数值为0的时候，则不要超链接 modify by zhangcong@ 2011-06-09
        	    if(cells[2] == 0)
				{
        	//		shtm += "<td>"+cells[2]+"</td>";
        	    	shtm += "<td><a href=javascript:loadOkUsers('"+cid+"','"+cells[1]+"')>"+cells[2]+"</a></td>";
        		}else
        		{
        	    	shtm += "<td><a href=javascript:loadOkUsers('"+cid+"','"+cells[1]+"')>"+cells[2]+"</a></td>";
        		}
                var unopok = parseInt(cells[3])-parseInt(cells[2]);
        	  
            	//数值为0的时候，则不要超链接 modify by zhangcong@ 2011-06-09
            	if(unopok == 0)
            	{
            	//	shtm += "<td>"+unopok+"</td>";
            		shtm += "<td><a href=javascript:loadUnokUsers('"+cid+"','"+cells[1]+"')>"+unopok+"</a></td>";
            	}else
            	{
            		shtm += "<td><a href=javascript:loadUnokUsers('"+cid+"','"+cells[1]+"')>"+unopok+"</a></td>";
            	}
          }
          //shtm += "<td class=column><a href=javascript:loadOkUsers('"+cid+"','"+cells[1]+"')>"+cells[2]+"</a></td>";//
          //var unopok = parseInt(cells[3])-parseInt(cells[2]);
          //shtm += "<td class=column><a href=javascript:loadUnokUsers('"+cid+"','"+cells[1]+"')>"+unopok+"</a></td>";//
          var bndrt = cells[4];
          if(escape(cells[4])=="%26amp%3Bnbsp%3B"){
            //PageLog("*");
            bndrt="N/A";
          }
          shtm += "<td>"+bndrt+"</td></tr>";
        }
        shtm += "</table></td></tr>";

        //将3个图标移到下面来 modiby by zhangcong 2011-06-09
        shtm += "<tr><td><table border=0 cellspacing=1 cellpadding=2 width=\"100%\" align=\"right\" bgcolor=\"#000000\">";
        shtm += "<tr bgcolor=\"#FFFFFF\"><td class=column1 align=\"right\" width=\"100\">";
        shtm += "&nbsp;<a href=javascript:queryForPrint()><img src=\"../images/print.gif\" border=\"0\" width=\"16\" height=\"16\"></img></a>&nbsp;";
        shtm += "&nbsp;<a href=javascript:queryForExcel()><img src=\"../images/excel.gif\" border=\"0\" width=\"16\" height=\"16\"></img></a>&nbsp;";
        shtm += "&nbsp;<a href=javascript:queryForPdf()><img src=\"../images/pdf.gif\" border=\"0\" width=\"16\" height=\"16\"></img>"+"</a>";
        shtm += "</td></tr></table></td></tr></table>";
        
        //shtm += "";
        $(divBindRates).html(shtm);
      }
      $(msg).hide();
    });
}


var userListCaption="用户列表";
function loadUserList(retstr) {
	  var gwType = '<%=gw_type%>';
      var mesg = $.trim(retstr);
      //PageLog("mesg="+mesg);
      if(mesg=="FAILURE"){
        alert("查询出错");
      }else if(mesg==""){
        alert("查询结果为空");
        $(divBindUsers).hide();     
      }else{
        var lines = mesg.split("\n");
        shtm  = "<table width=\"100%\" align=\"center\" border=\"0\" cellspacing=\"1\" cellpadding=\"2\" bgcolor=\"#999999\">";
        shtm += "<caption>"+userListCaption+"<"+"/caption>";
        if("2" == gwType){
        	shtm += "<tr><th>用户账号/专线号<"+
	               "/th><th>开户时间<"+
	               "/th><th>竣工时间<"+
	               "/th><th>客户名称<"+"/th><"+"/tr>";
	       for(var i=0;i<lines.length;i++){
	         var cells = lines[i].split("|");
	         if(cells.length<5)continue;
	         shtm += "<tr><td class=column>"+cells[1]+"<";
	         shtm += "/td><td class=column>"+cells[2]+"<";
	         shtm += "/td><td class=column>"+cells[3]+"<";
	         shtm += "/td><td class=column>"+cells[4]+"<"+"/tr>";
	        }
        }else{
	       shtm += "<tr><th>用户账号<"+
	               "/th><th>开户时间<"+
	               "/th><th>竣工时间<"+"/th><"+"/tr>";
	       for(var i=0;i<lines.length;i++){
	         var cells = lines[i].split("|");
	         if(cells.length<4)continue;
	         shtm += "<tr><td class=column>"+cells[1]+"<";
	         shtm += "/td><td class=column>"+cells[2]+"<";
	         shtm += "/td><td class=column>"+cells[3]+"<"+"/td><"+"/tr>";
	       }
	    }
        shtm += "<"+"/table>";
        $(divBindUsers).html(shtm);
        $(divBindUsers).show();
      }
      $(msg).hide();
}

function loadOkUsers(cityId,cityName) {
    var stime = str2time($(frm.start_time).val());
    var etime = str2time($(frm.end_time).val());
   var usertype=  document.frm2.importUsertype.value;
    $(divBindUsers).hide();
    userListCaption = "["+cityName+"]已绑定用户列表";
    var url = "../BindRate/getOkUsers!getOkUsers.action";
    //PageLog("<a href='"+url+"?startTime="+stime+"&endTime="+etime+"&cityId="+cityId+"' target=_blank>go</a>");
    $(msg).show();
    $.post(url,{
      startTime:stime,
      endTime:etime,
      cityId:cityId,
      usertype:usertype
    },loadUserList);
}

function loadUnokUsers(cityId,cityName) {    
    var stime = str2time($(frm.start_time).val());
    var etime = str2time($(frm.end_time).val());
    var usertype=  document.frm2.importUsertype.value;
    $(divBindUsers).hide();
    userListCaption = "["+cityName+"]未绑定用户列表";
    var url = "../BindRate/getUnOkUsers!getUnOkUsers.action";
    //PageLog("<a href='"+url+"?startTime="+stime+"&endTime="+etime+"&cityId="+cityId+"' target=_blank>go</a>");
    $(msg).show();
    $.post(url,{
      startTime:stime,
      endTime:etime,
      cityId:cityId,
      usertype:usertype
    },loadUserList);
}


function showResult(response){
	$("QueryData").innerHTML = response.responseText;
	showProcess(false);
	InitDetail();
}

function showError(response){
	_debug.innerHTML = response.responseText;
}

function InitDetail(){
	var spanArr = $$('span[_type="detail"]');
	var shDetail = "showDetail";
	var js = null;
	spanArr.each(function(item){
		js = shDetail + "('" + item._dealstaff + "','" + item._city_id + "')";
		item.innerHTML = "<a href=javascript:// onclick=\"" + js + "\">详细数据</a>";
		js = null;
	});
	js = null;
	shDetail = null;
	spanArr = null;
}
function showDetail(staff,city_id){
	mouse_y = event.clientY;
	var starttime = $("start_time").value;
	var endtime = $("end_time").value;
	var param = "city_id=" + city_id;
	param += "&dealstaff=" + staff;
	param += "&start_time=" + starttime;
	param += "&end_time=" + endtime;
	param += "&type=detail";
	param += "&tt=" + new Date().getTime();

    CreateAjaxReq("UserInstReportData.jsp",param,showDetailResult,showError);
	
	//_divDetail.innerHTML = "正在生成详细资料...";
}

function showDetailResult(response){
	showProcess(false);
	var _divDetail = $("divDetail");
	_divDetail.show();
	//_divDetail.style.left = event.X;
	_divDetail.style.top = mouse_y + document.body.scrollTop;
	_divDetail.innerHTML = response.responseText;
}
function showProcess(flag){
	flag ? $("msg").show() : $("msg").hide();
}
function CloseDetail(){
	$("divDetail").hide();
	showProcess(false);
}	
function goPage(offset){
	
	var city_id = "";
	if(document.all("city_id") == null || document.all("city_id").value == "" || document.all("city_id").value < 0){
		alert("请选择属地");
		document.all("city_id").focus();
		return false;
	}else{
		city_id = document.all("city_id").value;
	}
	
	var starttime = $("start_time").value;
	var endtime = $("end_time").value;
	var param = "city_id=" + city_id;
	param +="&offset=" + offset;
	param += "&start_time=" + starttime;
	param += "&end_time=" + endtime;
	param += "&type=state";
	param += "&tt=" + new Date().getTime();
	
	CreateAjaxReq("UserInstReportData.jsp",param,showResult,showError);
	
}
//-->
</script>
</head>
<br>
<body>
<!-- 报表所加 -->
<form name="frm2">
<input type="hidden" name="importCityId" value=""/>
<input type="hidden" name="importStartTime" value=""/>
<input type="hidden" name="importEndTime" value=""/>

<input type="hidden" name="importUsertype" value=""/>

</form>

<TABLE border=0 cellspacing=0 cellpadding=0 width="90%" align=center>
 <tr>
  <td>
  <table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
   <tr>
    <td width="162" align="center" class="title_bigwhite" ondblclick="ShowHideLog()">绑定率统计</td>
    <td nowrap>
		<img src="../images/attention_2.gif" width="15" height="12">时间为用户受理时间
	</td>
   </tr>
  </table>
  </td>
 </tr>
 <tr>
  <td>
  <form name=frm>
  <table width="100%" border="0" cellspacing="1" cellpadding="2" bgcolor="#999999">
  <TR>
					<TH colspan="4" align="center">
						绑定率统计
					</TH>
				</TR>
   <tr bgcolor=#ffffff>
    <td class=column align=center>开始时间</td>
    <!--
    <td><input type="text" id="start_time" name="start_time" class=bk value="<%=start_time%> 00:00:00"> <input
     type="button" value="▼" class=btn onClick="showCalendar('all')" name="button1">
    </td>
     -->
    <td>
    	<input type="text" name="start_time" id="start_time" value="<%=start_time%> 00:00:00" readonly class=bk>
		<img name="endimg"
					onClick="WdatePicker({el:document.frm.start_time,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
					src="../images/dateButton.png" width="15" height="12"
					border="0" alt="选择">&nbsp;
		<font color="red"> *</font>
	</td>
     
    <td class=column align=center>结束时间</td>
    
    <td>
    	<input type="text" name="end_time" id="end_time" value="<%=end_time%> 00:00:00" readonly class=bk>
		<img name="endimg"
					onClick="WdatePicker({el:document.frm.end_time,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
					src="../images/dateButton.png" width="15" height="12"
					border="0" alt="选择">&nbsp;
		<font color="red"> *</font>
	</td>
	<!-- 
    <td><input type="text" id="end_time" name="end_time" class=bk value="<%=end_time%> 23:59:59"> <input
     type="button" value="▼" class=btn onClick="showCalendar('all')" name="button2">
    </td>
     -->
    
   </tr>
   <tr bgcolor=#ffffff>
    <td class=column align=center>属 地</td>
    <td><%=strCityList%></td>
    
    <td class=column width="15%" align=center>
								用户终端类型
						</td>
						<td width="35%">
							<SELECT name="usertype">
								<option selected value="2">E8-C</option>
								<option value="1">E8-B</option>
								<option value="0">全部</option>
							</SELECT>
						</td>
   
   </tr>
   <tr bgcolor=#ffffff>
    <td class=column colspan=4 align=right><input type="button" class=jianbian value=" 统 计 " onclick="doQuery()"></td>
   </tr>
  </table>
  </form>
  </td>
 </tr>
 <tr>
  <td>&nbsp;</td>
 </tr>
 <tr>
  <td>
  <div id="QueryData" align=center>
  
  </div>
  </td>
 </tr>
 <tr>
  <td>
  <div id="msg" style="display:none">正在统计，请稍等....</div>
  <div id="divNavBar" style="display:none"></div>
  <div id="divBindRates" style="display:none"></div>
  </td>
 </tr>
 <tr>
  <td><br>
  <div id="divBindUsers" style="display:none"></div>
  <div id=_debug></div>
  </td>
 </tr>
</TABLE>
</body>
<%@ include file="../foot.jsp"%>
</html>
