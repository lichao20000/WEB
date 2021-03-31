<%@ include file="../../timelater.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean,
	java.util.HashMap,java.util.Map,
	com.linkage.litms.common.database.Cursor"%>
<%

request.setCharacterEncoding("GBK");

UserRes userRes = (UserRes) request.getSession().getAttribute("curUser");
String cityId = userRes.getCityId();

String citySql = "select city_id,city_name from tab_city where city_id='"+cityId+"' or parent_id='"+cityId+"'";
Cursor cityCursor = DataSetBean.getCursor(citySql);
HashMap treeList = new HashMap();
String treeSQL="select tree_id,tree_name from tab_system_tree";
Cursor cursor = DataSetBean.getCursor(treeSQL);
Map treeMap = cursor.getNext();
if(treeMap != null)
{

%>

<SCRIPT LANGUAGE="JavaScript">
<!--//

function Map() {
 var struct = function(key, value) {
  this.key = key;
  this.value = value;
 }
 
 var put = function(key, value){
  for (var i = 0; i < this.arr.length; i++) {
   if ( this.arr[i].key === key ) {
    this.arr[i].value = value;
    return;
   }
  }
   this.arr[this.arr.length] = new struct(key, value);
 }
 
 var get = function(key) {
  for (var i = 0; i < this.arr.length; i++) {
   if ( this.arr[i].key === key ) {
     return this.arr[i].value;
   }
  }
  return null;
 }
 
 var remove = function(key) {
  var v;
  for (var i = 0; i < this.arr.length; i++) {
   v = this.arr.pop();
   if ( v.key === key ) {
    continue;
   }
   this.arr.unshift(v);
  }
 }
 
 var size = function() {
  return this.arr.length;
 }
 
 var isEmpty = function() {
  return this.arr.length <= 0;
 }

 this.arr = new Array();
 this.get = get;
 this.put = put;
 this.remove = remove;
 this.size = size;
 this.isEmpty = isEmpty;
}
	
	
var map = new Map();
<%
	while(treeMap != null)
	{
%>
 map.put("<%=treeMap.get("tree_id")%>","<%=treeMap.get("tree_name")%>");
<%
		treeList.put(treeMap.get("tree_id"),treeMap.get("tree_name"));
		treeMap = cursor.getNext();
	}
	
%>
//-->
</SCRIPT>
<%}%>
<html>
<head>
<title>报表订制页面</title>
<script type="text/javascript" src="../../Js/jsDate/WdatePicker.js"></script>
<link rel="stylesheet" href="../../css/css_green.css" type="text/css">
<link rel="stylesheet" href="../../css/liulu.css" type="text/css">
</head>
<body>
<form name=frm action="../../Report/frame/treeview/rp_nodesave.jsp" method="post" target="childFrm" onsubmit="return checkElmenets();">
<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<tr>
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
			<tr>
				<td>
				<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
					<TR>
						<TD>
						<table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="0">
							<tr>
								<td>
								<table width="100%" height="30" border="0" cellspacing="0"
									cellpadding="0" class="green_gargtd">
									<tr>
										<td width="162">
										<div align="center" class="title_bigwhite">报表订制</div>
										</td>
										<td><img src="../../images/attention_2.gif" width="15"
											height="12">
										</td>
									</tr>
								</table>
								</td>
							</tr>
							<tr>
								<td bgcolor="#FFFFFF">
								<table width="100%" border=0 align="center" cellpadding="1"
									cellspacing="1" bgcolor="#999999" class="text">
									<TR>
										<TH colspan=4>报表订制</TH>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD width="20%" align="right">报表名称</TD>
										<td colspan=3>
											<input type=text name="report_name" class="bk" value="" size="30">
											&nbsp;<font color=red>*</font>
										</td>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD align="right">报表菜单路径</TD>
										<td colspan=3>
											<input type=text name="save_dir" class="bk" size="30" value="" onClick=doSelect() readonly/>
											&nbsp;<font color=red>*</font>
                    						<input type=hidden name="item_id" class="bk" size="30" value="" />
                    						<input type=hidden name="tree_id" class="bk" size="30" value="" />
										</td>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD align="right">区域属地</TD>
										<td colspan=3>
											<select name="cityId" class="bk">
												<%for(int i=0;i<cityCursor.getRecordSize();i++) {
													Map cityMap = cityCursor.getNext();%>
													<OPTION value="<%=cityMap.get("city_id") %>">
														<%=cityMap.get("city_name") %>
													</OPTION>
												<%} %>
											</select>
											&nbsp;<font color=red>*</font>
										</td>
									</TR>
									<!-- 
									<TR bgcolor="#FFFFFF">
										<TD align="right">所属区域</TD>
										<td colspan=3>
											<input type="radio" name="public_stat" value="-1">私有区域 
						                    <input type="radio" name="public_stat" value="1" checked>公共区域
											&nbsp;<font color=red>*</font>
										</td>
									</TR>
									 -->
									<TR bgColor=#ffffff>
										<TD align="right" width="10%">报表类型<br>
										</TD>
										<TD colspan="3">
											<INPUT TYPE="radio" NAME="SearchType" CLASS="bk" VALUE="1"> 小时报表
											<INPUT TYPE="radio" NAME="SearchType" CLASS="bk" VALUE="2" checked> 日报表
											<INPUT TYPE="radio" NAME="SearchType" CLASS="bk" VALUE="3"> 周报表 
											<INPUT TYPE="radio" NAME="SearchType" CLASS="bk" VALUE="4"> 月报表 
											&nbsp;<font color="#FF0000">*</font>
											<INPUT TYPE="hidden" name="hidtype" value="2"></TD>
									</TR>
									<TR bgColor=#ffffff>
										<TD align=right>时间<br>
										</TD>
										<TD class="" colspan=3>
											<INPUT TYPE="text" NAME="start" class=bk readonly>
											<img name="shortDateimg"
											onclick="new WdatePicker(document.frm.start,'%Y-%M-%D %h:%m:%s',true,'whyGreen')"
											src="../../images/search.gif" width="15" height="12" border="0" alt="选择">
											&nbsp;<font color="#FF0000">*</font>
											<INPUT TYPE="hidden" name="hidtime">
										</TD>
									</TR>
									<TR bgColor=#ffffff>
										<TD align=right>定制字段<br>
										</TD>
										<TD class="" colspan=3>
											<div id="divParam"></div>
										</TD>
									</TR>
									<TR class=green_foot>
										<TD colspan=4 align=right>
											<input type="submit" value="订 阅" class="jianbian">&nbsp;
											<input type=button class="jianbian" value="取 消" onClick="window.close()" >
											<input type="hidden" name="url" value="">
											<input type="hidden" name="expression_Name">
											<input type="hidden" name="public_stat" value='1'>
											<input type=hidden name=report_stat value="4">
											<input type="hidden" name="timer" value="" />
										</TD>
									</TR>
								</table>
								</td>
							</tr>
						</table>
						</TD>
					</TR>
					<tr>
						<td height="15"></td>
					</tr>
				</TABLE>
			</tr>
		</table>
		<iframe id="childFrm" name = "childFrm" STYLE="display:none;width:500;height:200"></iframe>
		</td>
	</tr>
</table>
</form>
<%@ include file="../foot.jsp"%>
</body>
<script language="javascript">
//父页面url路径
var url;
function getURL(){
	url = opener.document.all("url").value;
	
	var html = "";
	var bookparam = opener.document.all("bookparamfirst").value;
	var temp1 = bookparam.split("$");
	for(var i=0;i<temp1.length;i++){
		var temp2 =temp1[i].split("|");
		html = html + "<input type='checkbox' name='bookparam' value='"+temp2[0]+"' checked=true/>"+temp2[1];
		if((i+1)%4==0){
			html = html+"<br>"
		}
	}
	
	document.all("divParam").innerHTML = html;
	
	//alert(document.all("url").value);
}

document.onload = getURL();

//提交检查
function checkElmenets(){

	var obj = document.frm;
	
	var bookparam = "";
	var inputs=document.all("bookparam");
    for(var i=0;i<inputs.length;i++)
    {
    	if(inputs[i].type=="checkbox")
    		if(inputs[i].checked){
    			if(""==bookparam){
    				bookparam = inputs[i].value;
    			}else{
    				bookparam = bookparam+"$"+inputs[i].value;
    			}
    		}
    }
	
	if(KillSpace(obj.report_name.value).length == 0){
		alert("报表名称不能为空");
		obj.report_name.focus();
		return false;
	}
   if(KillSpace(obj.save_dir.value).length == 0)
    {
  		alert("请选择保存路径");
		 // obj.report_name.focus();
		return false;
    }
  	//alert(document.all("save_dir").value+" "+document.all("item_id").value+" " +document.frm.tree_id.value);
	if(KillSpace(obj.start.value).length == 0){
		alert("请选择时间");
		return false;
	}
	if(""===bookparam){
		alert("请选择定制字段");
		return false;
	}
	
	searchType();
	obj.hidtime.value = DateToDes(obj.start.value);
	var param = "?reportType=" + obj.hidtype.value 
			+ "&longData=" + obj.hidtime.value
			+ "&booked=none" + "&cityId=" + obj.cityId.value
			+ "&bookparam=" + bookparam;
	document.all("url").value = url + param;
	//alert(obj.url.value);
	return true;
}
//判空
function KillSpace(x){
	while((x.length > 0) && (x.charAt(0) == ' '))
		x = x.substring(1,x.length)
	while((x.length>0) && (x.charAt(x.length-1) ==' '))
		x = x.substring(0,x.length-1)
	return x
}

//flag -1失败；+1成功；0有重复值
function doString(flag){
	if(flag == -1){
		alert("保存新节点操作失败");
		window.close();
	}else if(flag == 0){
		alert("新节点与原有节点内容重复，操作失败");
		window.close();
	}else if(flag == 1){
		alert("新节点创建成功");
		window.close();
	}
}
//选择路径
function doSelect(){
	document.frm.save_dir.value="";
	var s=window.showModalDialog("../../Report/frame/treeview/rp_tree_list.jsp");
	if('undefined' != typeof(s)){
		var sp_value = s.split(",");
		var s_value;
		var s_show="";
	
		for(var i=0;i<sp_value.length; i++){
			s_show += map.get(sp_value[i])+" >>";
		}
		document.frm.tree_id.value = sp_value[sp_value.length-2];
		document.frm.item_id.value = sp_value[sp_value.length-1];
		document.frm.save_dir.value = s_show;
	}
}
//报表类型
function searchType(){    
	var type=2;
	for(var i=0;i<document.frm.SearchType.length;i++){
		 if(document.frm.SearchType[i].checked){
				type=document.frm.SearchType[i].value;
				//alert("searchType    "+frm.hidtype.value);
				break;
		  }
	}
	document.frm.hidtype.value = type;
}
//时间转秒
function DateToDes(tm){
	if(tm != ""){
		//日期
		var date = tm.split(' ')[0].split('-');   
	    var time = tm.split(' ')[1].split(':'); 
		
		var reqReplyDate = new Date();
		reqReplyDate.setYear(date[0]);
	    reqReplyDate.setMonth(date[1]-1);
	    reqReplyDate.setDate(date[2]);
	    reqReplyDate.setHours(time[0]);
	    reqReplyDate.setMinutes(time[1]);
	    reqReplyDate.setSeconds(time[2]);

		return Math.floor(reqReplyDate.getTime()/1000);
	} else {
		return 0;
	}
}

</script>
</html>