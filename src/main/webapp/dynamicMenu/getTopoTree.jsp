<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.system.UserRes"%>
<%@ page import="java.util.Map,com.linkage.litms.common.database.*"%>
<%
request.setCharacterEncoding("GBK");
UserRes curUser = (UserRes) request.getSession().getAttribute("curUser");
String strSQL = null;
if(curUser.getUser().isAdmin()){
	strSQL = "select a.* from tp_gw_segment a where a.managed = 1";

	// teledb
	if (DBUtil.GetDB() == 3) {
		strSQL = "select a.labelname, a.id, a.parentid from tp_gw_segment a where a.managed = 1";
	}
}else{
	strSQL = "select a.* from tp_gw_segment a, tab_gw_res_area b "
		+ " where a.managed = 1 and a.id=b.res_id and b.res_type=0 "
		+ " and b.area_id=" + curUser.getAreaId();

	// teledb
	if (DBUtil.GetDB() == 3) {
		strSQL = "select a.labelname, a.id, a.parentid from tp_gw_segment a, tab_gw_res_area b "
				+ " where a.managed = 1 and a.id=b.res_id and b.res_type=0 "
				+ " and b.area_id=" + curUser.getAreaId();
	}
}
com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(strSQL);
psql.getSQL();
Cursor cursor = DataSetBean.getCursor(strSQL);

String topoXml = "";
Map fields = cursor.getNext();

topoXml += "<Nodes>";

while(fields != null){
	topoXml += "<Device ";
	topoXml += "title='" + fields.get("labelname") + "' id='" + fields.get("id") + "' pid='" + fields.get("parentid") + "' ";
	topoXml += "/>";
	fields = cursor.getNext();
}

topoXml += "</Nodes>";

fields = null;
cursor = null;
%>
<%@page import="com.linkage.litms.tree.NodeData"%>
<%@page import="com.linkage.litms.tree.Node"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<HTML>
<HEAD>
<TITLE></TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<LINK REL="stylesheet" HREF="./css/css_blue.css" TYPE="text/css">
</HEAD>

<style>

    A:LINK, A:VISITED, A:ACTIVE, A:HOVER
    {
      color: #000000;
      font-size: 12px;
      padding-left: 5px;
      TEXT-DECORATION: NONE;
      font-family: Tahoma,Verdana,宋体,Fixedsys;
    }
    SPAN
    {
      font-family: Tahoma,Verdana,宋体,Fixedsys;
    }
A.MzTreeview
{
  font-size: 9pt;
  padding-left: 3px;
}
</style>

<BODY style="height:100%"  leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellpadding="0" cellspacing="0"  background="../../images/left_back.jpg">
	<tr>
		<td  width="220" height="19" align=left background="../images/left_top.jpg"></td>
	</tr>
</table>

<script language="JavaScript" src="./MzTreeView10/MzTreeView10.js"></script>
<script language="JavaScript">
  var tree = new MzTreeView("tree");

  tree.icons["property"] = "property.gif";
  tree.icons["css"] = "collection.gif";
  tree.icons["book"]  = "book.gif";
  tree.iconsExpand["book"] = "bookopen.gif"; //展开时对应的图片

  //tree.setIconPath("http://www.meizz.com/Icons/TreeView/"); //可用相对路径
  tree.setIconPath("./MzTreeView10/"); //可用相对路径
  

  tree.nodes["0_1"] = "text:拓扑管理; data:id=1/0";
  tree.nodes["1_1/0"] = "text:网络视图; data:id=1/0";
  
//获取列表数据
function getTopoInfo(){
	//读取Topo XML数据
	var XMLDoc;
	XMLDoc = new ActiveXObject("Microsoft.XMLDOM");
	XMLDoc.async = false;
	XMLDoc.loadXML("<%=topoXml%>");
	
	var nodes = XMLDoc.selectNodes("//Nodes/Device");
	var ln = nodes.length;

	var node = null;
	var type = null;
	var child = null;

	for(var i=0;i<ln;i++){
		node = nodes.item(i);
		
		var parentid = node.getAttribute("pid");
		var id = node.getAttribute("id");
		var title = node.getAttribute("title");
		
		//alert(parentid + "_" + id + "_" + title);
		
		tree.nodes[parentid + "_" + id] = "text:" + title + "; data:id=" + id; 
		
	}
	document.write(tree.toString());
}

//单击事件
MzTreeView.prototype.nodeClick = function(id)
{
	var source = this.nodes[this.node[id].sourceIndex];
	var temp_id = source.substring(source.lastIndexOf("id=")+3);
	
	if (id != 1){
		page = "getChildTopo.jsp?oid=0&pid=" + temp_id;
		parent.parent.window.frames[2].document.frames('viewPage').document.all("childFrm").src = page;
	}
	else{
		page = "../../webtopo/main_map.jsp?type=1&pid=1/0";
		parent.parent.window.frames[2].document.all('viewPage').src = page;
	}
	
}

getTopoInfo();
</script>
</body>
</html>

