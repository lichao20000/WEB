<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.linkage.litms.common.database.Cursor"%>
<%
request.setCharacterEncoding("GBK");
HashMap treeList = new HashMap();
String treeSQL="select tree_id,tree_name from tab_system_tree";
Cursor cursor = DataSetBean.getCursor(treeSQL);
Map treeMap = cursor.getNext();
if(treeMap != null)
{

%>
<SCRIPT LANGUAGE="JavaScript">
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
	
%></SCRIPT><%

}

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<SCRIPT LANGUAGE="JavaScript" SRC="../../../Js/CheckForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../../Js/Calendar.js"></SCRIPT>
<link rel="stylesheet" href="../../../css/css_blue.css" type="text/css">
<link rel="stylesheet" href="../../../css/tree.css" type="text/css">
<link rel="stylesheet" href="../../../css/tab.css" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../../Js/toolbars.js"></SCRIPT>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<!-- 主程序 start -->
	<form name=frm action="rp_nodesave.jsp"  method="post" target="childFrm" onsubmit="return checkElmenets();">
	    <TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
          <TR><TD>
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
		  <TR height=30>
			<TD class="curstarttab"><div align=center><font color=blue>新增报表节点</font></div>
                  </TD>
		  </TR>
		</TABLE>
	  </TD></TR>
	  <TR><TD bgcolor=#999999>
	          <TABLE border=0 cellspacing=1 cellpadding=6 width="100%" id="idTable">
                <TR> 
                  <TD style='background:#F4F4FF;color:#000000' onmouseover="this.style.background='#CECECE';this.style.color='#006790';" onmouseout="this.style.background='#F4F4FF';this.style.color='#000000';" width="20%" class=column3> 
                    <div align="right">报表名称</div>
                  </TD>
                  <TD style='background:#F4F4FF;color:#000000' onmouseover="this.style.background='#CECECE';this.style.color='#006790';" onmouseout="this.style.background='#F4F4FF';this.style.color='#000000';" class=column3 width="80%"> 
                    <input type=text name="report_name" class="bk" value="" size="30">
                    &nbsp;<font color="#FF0000">*</font> </TD>
                </TR>
                <TR> 
                  <TD style='background:#F4F4FF;color:#000000' onmouseover="this.style.background='#CECECE';this.style.color='#006790';" onmouseout="this.style.background='#F4F4FF';this.style.color='#000000';" width="20%" class=column3> 
                    <div align="right">保存目录</div>
                  </TD>
                  <TD style='background:#F4F4FF;color:#000000' onmouseover="this.style.background='#CECECE';this.style.color='#006790';" onmouseout="this.style.background='#F4F4FF';this.style.color='#000000';" class=column3 width="80%"> 
                    <input type=text name="save_dir" class="bk" size="30" value="" onClick=doSelect() readonly />
                    <input type=hidden name="item_id" class="bk" size="30" value="" /><input type=hidden name="tree_id" class="bk" size="30" value="" />
                    &nbsp;<font color="#FF0000">*</font> </TD>
                </TR>
                <TR> 
                  <TD style='background:#F4F4FF;color:#000000' onmouseover="this.style.background='#CECECE';this.style.color='#006790';" onmouseout="this.style.background='#F4F4FF';this.style.color='#000000';" class=column3 width="20%"> 
                    <div align="right">所属区域</div>
                  </TD>
                  <TD style='background:#F4F4FF;color:#000000' onmouseover="this.style.background='#CECECE';this.style.color='#006790';" onmouseout="this.style.background='#F4F4FF';this.style.color='#000000';" class=column3 width="80%"> 
                    <input type="radio" name="public_stat" checked value="-1">
                    私有区域 
                    <input type="radio" name="public_stat" value="1">
                    公共区域</TD>
                </TR>
                <TR> 
                  <TD style='background:#F4F4FF;color:#000000' onmouseover="this.style.background='#CECECE';this.style.color='#006790';" onmouseout="this.style.background='#F4F4FF';this.style.color='#000000';" class=column3  colspan=2> 
                    <div align="center"> 
                      <input type=submit name=add class="btn" value="新 增">
                      &nbsp;&nbsp;&nbsp;&nbsp; 
                      <input type=button name=cancel class="btn" value="取 消" onClick="window.close()" >
                    </div>
                  </TD>
                </TR>
              </TABLE>
	  </TD></TR>
	  <TR><TD HEIGHT=10>&nbsp;</TD></TR>

	</TABLE>
	<input type=hidden name=report_stat value="4">
	<input type=hidden name=url value="">
	</form>
	<!-- 主程序 end -->
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
<iframe id="childFrm" name = "childFrm" STYLE="display:none;width:500;height:200"></iframe>
<script language="javascript">
function getURL(){
	document.all("url").value = opener.document.all("url").value;
	//alert(document.all("url").value);
}

document.onload = getURL();

function checkElmenets(){
	var obj = document.frm;
	
	if(KillSpace(obj.report_name.value).length == 0){
		alert("报表名称不能为空");
		obj.report_name.focus();
		return false;
	}
   if(KillSpace(obj.save_dir.value).length == 0)
  {
  		alert("请选择定制位置");
		 // obj.report_name.focus();
		  return false;
  }
  //alert(document.all("save_dir").value+"     "+document.all("item_id").value+"   " +document.frm.tree_id.value);
	
	return true;
}

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
function doSelect(){
  document.frm.save_dir.value="";
  var s=window.showModalDialog("rp_tree_list.jsp");
  var sp_value = s.split(",");
  var s_value;
  var s_show="";

  for(var i=0;i<sp_value.length;i++){
  	s_show += map.get(sp_value[i])+">>";
  }
	
	document.frm.tree_id.value = sp_value[sp_value.length-2];
	document.frm.item_id.value = sp_value[sp_value.length-1];
	document.frm.save_dir.value = s_show;
}

</script>
</html>