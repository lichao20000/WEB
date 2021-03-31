<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import ="com.linkage.litms.tree.Item"%>
<%@ page import ="com.linkage.litms.tree.Tree"%>
<%@ page import ="com.linkage.litms.tree.Module"%>
<%@ page import ="com.linkage.litms.common.database.DataSetBean" %>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Vector"%>
<%@ page import="com.linkage.litms.common.database.Cursor"%>
<%@ page import ="com.linkage.litms.common.database.DatabaseUtil"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%
request.setCharacterEncoding("GBK");
try{
String id = request.getParameter("id");	// 报表ID
String report_name = request.getParameter("report_name"); //报表名称
String report_path = request.getParameter("hidpath");	//报表路径
String pid = request.getParameter("hidparentid");	//报表父ID
String layer = request.getParameter("hidlayer");	//报表深度
String[] arr_role = request.getParameterValues("role_id");	//报表浏览权限
String report_stat = request.getParameter("report_stat");	//操作类型
String hidpathid = request.getParameter("hidpathid");	//报表路径ID
String tree_id = request.getParameter("tree_id");//treeID
String item_id = request.getParameter("item_id");//itemID
String url = request.getParameter("url");
String public_stat  = request.getParameter("public_stat");
String rp_stat = "1";
long iid = DataSetBean.getMaxId("tab_report_manager","id");
if(null!=id&&(null==item_id||"null".equalsIgnoreCase(item_id)||"".equals(item_id)))
{	
//	syabse数据库
String itemSQL="select b.item_id from tab_report_manager a,tab_item b where convert(varchar(255),a.sqlvalue)=b.item_url and a.report_name=b.item_name and a.id="+id;

//add by zhangcong@ 2011-06-21
if(LipossGlobals.isOracle())
{
	itemSQL="select b.item_id from tab_report_manager a,tab_item b where to_char(a.sqlvalue)=b.item_url and a.report_name=b.item_name and a.id="+id;
}
// teledb
if (DBUtil.GetDB() == 3) {
	itemSQL="select b.item_id from tab_report_manager a,tab_item b where CAST(a.sqlvalue AS CHAR)=b.item_url and a.report_name=b.item_name and a.id="+id;
}
com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(itemSQL);
psql.getSQL();
Map map =DataSetBean.getRecord(itemSQL);
if(null!=map)
{
    item_id= (String)map.get("item_id");
}
}

//加上报表自身节点id
hidpathid = id +","+ hidpathid;
String[] arr_node = hidpathid.split(",");
String[] strSQL = null;
Vector list = new Vector();


String sql;
Cursor cursor;
Map fields;
DateTimeUtil dateUtil = null;
int flag = -1;
int iAudit = Integer.parseInt(report_stat);
if(iAudit == 1){	//报表订阅审核通过
	if(arr_role!=null)
		for(int i=0;i<arr_role.length;i++){
			list.add("insert into tab_report_role (role_id,report_id) values ("+ arr_role[i] +","+ id +")");
			if(!"".equals(item_id))
			{
				//list.add("insert into tab_item_role(item_id,role_id) values('"+item_id+"',"+arr_role[i]+")");
				list.add("insert into " + LipossGlobals.getLipossProperty("Systype") + "(item_id,role_id) values('"+item_id+"',"+arr_role[i]+")");
			}
		}

	  String rootid = request.getParameter("rootid");

	  list.add("update tab_report_manager set report_stat=1 where id="+ id);
}
else if(iAudit == -1){		//报表订阅审核不通过
//	list.add("delete from tab_item_role where item_id ='"+item_id+"'");
	list.add("delete from " + LipossGlobals.getLipossProperty("Systype") + " where item_id ='"+item_id+"'");
	list.add("delete from tab_tree_item where item_id ='"+item_id+"'");
	list.add("delete from tab_item where item_id ='"+item_id+"'");
	list.add("update tab_report_manager set report_stat=-1 where id="+ id);
}
else if(iAudit == -2){		//删除已经审核通过的报表订阅
//	list.add("delete from tab_item_role where item_id ='"+item_id+"'");
	list.add("delete from " + LipossGlobals.getLipossProperty("Systype") + " where item_id ='"+item_id+"'");
	list.add("delete from tab_tree_item where item_id ='"+item_id+"'");
	list.add("delete from tab_item where item_id ='"+item_id+"'");
	list.add("delete from tab_report_manager where id="+ id);	
	list.add("delete from tab_report_role where report_id="+id);
}
else if(iAudit == -3){//删除未审核通过的报表订阅
//	list.add("delete from tab_item_role where item_id ='"+item_id+"'");
	list.add("delete from " + LipossGlobals.getLipossProperty("Systype") + " where item_id ='"+item_id+"'");
	list.add("delete from tab_tree_item where item_id ='"+item_id+"'");
	list.add("delete from tab_item where item_id ='"+item_id+"'");
	list.add("delete from tab_report_manager where id="+ id);	
	list.add("delete from tab_report_role where report_id="+id);
}
else if(iAudit == 2){		//编辑这项报表订阅
	String sqlvalue = request.getParameter("sqlvalue");
	list.add("delete from tab_report_role where report_id="+ id);
//	list.add("delete from tab_item_role where item_id='"+item_id+"'");
	list.add("delete from " + LipossGlobals.getLipossProperty("Systype") + " where item_id='"+item_id+"'");
	if(arr_role!=null)
		for(int i=0;i<arr_role.length;i++){
			list.add("insert into tab_report_role (role_id,report_id) values ("+ arr_role[i] +","+ id +")");
			//list.add("insert into tab_item_role(item_id,role_id) values('"+item_id+"',"+arr_role[i]+")");
			list.add("insert into " + LipossGlobals.getLipossProperty("Systype") + "(item_id,role_id) values('"+item_id+"',"+arr_role[i]+")");
		}	
	sqlvalue = sqlvalue.replaceAll("'","''");
	list.add("update tab_report_manager set report_name='"+report_name+"',sqlvalue='"+sqlvalue+"' where id="+ id);	
	list.add("update tab_item set item_name='"+report_name+"',item_url='"+sqlvalue+"' where item_id='"+item_id+"'");
}
else if(iAudit == 3){		//增加静态报表订阅
	String sqlvalue = request.getParameter("sqlvalue");
	String rootid = request.getParameter("rootid");
	
	sqlvalue = sqlvalue.replaceAll("'","''");
	fields = DataSetBean.getRecord("select id from tab_report_manager where sqlvalue like '"+ sqlvalue +"'");
	if(fields == null){
		if(arr_role!=null)
			for(int i=0;i<arr_role.length;i++){
				list.add("insert into tab_report_role (role_id,report_id) values ("+ arr_role[i] +","+ iid +")");
			}
	dateUtil = new DateTimeUtil();
		list.add("insert into tab_report_manager (id,pid,rootid,layer,ishas,report_name,report_path,sqlvalue,public_stat,report_stat,reg_time,user_id,tree_id) values ("+ iid+","+ pid +","+ rootid +","+ layer +",0,'"+ report_name +"','"+ report_path +"','"+ sqlvalue +"',0,1,'"+ dateUtil.getDate() +"','"+user.getAccount()+"','"+tree_id+"')");
		dateUtil = null;
	}
	else{
		flag = 0;
	}
}
else if(iAudit == 4){	//增加动态报表订阅
	//String sqlvalue1 = (String)session.getAttribute("parentSQL");
	//String sqlvalue2 = (String)session.getAttribute("childSQL");
	//String sqlvalue = sqlvalue1+";"+sqlvalue2;
	//sqlvalue = sqlvalue.replaceAll("'","''");

	if(url != null)
		//sqlvalue += "%%"+ url;
		url = url.replaceAll("'","''");
	
	if(public_stat.equals("1"))
		rp_stat = "0";
	
	dateUtil = new DateTimeUtil();
		String item_name = report_name;
		String item_url = url;
		String item_desc = report_name;
		String item_visual = "1";
		Item item = new Item();
		Tree tree = new Tree();
		String reportPath = tree.getFullParentPathByTreeId(item_id);
		//增加功能点报表 tab_item
		String re_Item_Id = item.reportItemAdd(item_name,item_url,item_desc,item_visual,user.getRoleId());
		
	  list.add("insert into tab_tree_item(sequence,tree_id,item_id) values(0,'"+item_id+"','"+re_Item_Id+"')");
	  list.add("insert into tab_report_manager (id,pid,rootid,layer,ishas,report_name,report_path,sqlvalue,public_stat,report_stat,reg_time,user_id,tree_id) values ("+ iid+",-1,-1,-1,0,'"+ report_name +"','"+reportPath+"','"+ url +"',"+public_stat+","+rp_stat+",'"+ dateUtil.getDate() +"','"+ user.getAccount() +"','"+re_Item_Id+"')");
	  list.add("insert into tab_report_role values("+ iid +","+user.getRoleId()+")");
}

	strSQL = new String[list.size()];
	for(int ln=0;ln<list.size();ln++){
		out.println(list.get(ln)+"<br>");
		strSQL[ln] = (String)list.get(ln);
	}

	if(strSQL.length>0){
	int[] iCode = DataSetBean.doBatch(strSQL);
	flag = (iCode != null && iCode.length > 0)?1:-1;
	if(flag==1)
	{
		Module.clearModuleRoleMap();
	}
}
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
var flag   = "<%=flag%>";
parent.doString(parseInt(flag));

//-->
</SCRIPT>
<%}catch(Exception e)
{
	e.printStackTrace();
}%>