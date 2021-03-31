<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%request.setCharacterEncoding("GBK");%>
<%@ page import ="com.linkage.litms.tree.Droit"%>
<%@ page import ="com.linkage.litms.tree.Tree"%>
<%@ page import ="com.linkage.litms.tree.Module"%>
<%@ page import ="java.util.List"%> 
<%@ page import ="java.util.ArrayList"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%--
	zhaixf(3412) 2008-05-14
	req:NJLC_HG-BUG-ZHOUMF-20080508-001
--%>

<script type="text/javascript">

<%
String role_id = request.getParameter("role_id");
String role_name = request.getParameter("role_name");
String old_role_name = request.getParameter("old_role_name");
String role_desc = request.getParameter("role_desc");
String tree_id_query = request.getParameter("tree_id_query");

String action = request.getParameter("action");

Droit Droit = new Droit();
Tree Tree = new Tree();

//若是add，则为添加角色权限
if(action.equals("add")){
    
    //首先判断角色表中是否已经存在此角色名称
	boolean flag = Droit.isExistRoleName(role_name.trim());
	
	//若是存在，则直接返回
	if(flag){	    
		//返回原位
		out.println("window.alert('您输入角色名称已经存在，请重新编辑角色名称');");
		//选中角色名称输入框内容
		out.println("parent.document.all('role_name').focus();");
		out.println("parent.document.all('role_name').select();");
		
		Droit = null;
	}else{

		String[] tree_id = tree_id_query.split(",");
	
		List ItemList = new ArrayList();
		List TreeList = new ArrayList();
	
		//获取所有系统目录
		List TreeIdList = Tree.getTreeAll();
		//模块系统
		List ModuleList = Tree.getRootMenu();
	
	//	判断哪些是系统目录节点,哪些是功能节点
		for(int k=0;k<tree_id.length;k++){
			if(TreeIdList != null && TreeIdList.contains(tree_id[k])){
				TreeList.add(tree_id[k]);
			}else if(ModuleList!= null && ModuleList.contains(tree_id[k])){
				TreeList.add(tree_id[k]);
			}else{
				if(!"".equals(tree_id[k].trim()))
					ItemList.add(tree_id[k]);
			}
		}
	
		//clear
		tree_id = null;
		tree_id_query = null;
		TreeIdList.clear();
		TreeIdList = null;
	
		
		//delete tree_id from role_id
		//Droit.delItemDroitByRoleId(role_id);
		//Droit.delTreeDroitByRoleId(role_id);
		
		//保存角色名称到tab_role
		//UserRes userRes = (UserRes)session.getAttribute("")
		long lRoleId = user.getRoleId();
		long lUserId = user.getId(); 
		long lMaxId = DataSetBean.getMaxId("tab_role","role_id");
		
		boolean b = Droit.createNewRole(String.valueOf(lMaxId),role_name,role_desc,String.valueOf(lRoleId),String.valueOf(lUserId));
		//Droit.createNewRole("0"+(lMaxId),role_name,role_desc,"01"+(lRoleId),"01"+(lUserId));
		
		//save 
		//delete tree_id from role_id
		Droit.delItemDroitByRoleId("" + lMaxId);
		Droit.delTreeDroitByRoleId("" + lMaxId);
		boolean b1 = Droit.saveItemDroit(ItemList,String.valueOf(lMaxId));
		boolean b2 = Droit.saveTreeDroit(TreeList,String.valueOf(lMaxId));
		//boolean b1 = false;
		//boolean b2 = false;
		//clear
		ItemList.clear();
		ItemList = null;
		
		ModuleList.clear();
		ModuleList = null;
	
		TreeList.clear();
		TreeList = null;
		
		if(b && b1 && b2)
		    out.print("parent.DroitChange('true','"+ role_name +"');");
		else
			out.print("parent.DroitChange('false','"+ role_name +"');");	
	
	}
	
}else if(action.equals("update")){//更新角色信息和权限
	
	//首先判断角色表中是否已经存在此角色名称
	boolean flag = Droit.isExistOtherRoleName(role_name,old_role_name);
	//若是存在，则直接返回
	if(flag){
		//返回原位
		out.println("parent.window.alert('您输入角色名称已经存在，请重新编辑角色名称');history.go(-1);");
		//选中角色名称输入框内容
		out.println("parent.document.all('role_name').focus();");
		out.println("parent.document.all('role_name').select();");
		
		Droit = null;
		return;
	}

	String[] tree_id = tree_id_query.split(",");
	List ItemList = new ArrayList();
	List TreeList = new ArrayList();

	//获取所有系统目录
	List TreeIdList = Tree.getTreeAll();
	//模块系统
	List ModuleList = Tree.getRootMenu();

//	判断哪些是系统目录节点,哪些是功能节点
	for(int k=0;k<tree_id.length;k++){ 
		if(TreeIdList != null && TreeIdList.contains(tree_id[k])){
			TreeList.add(tree_id[k]);
		}else if(ModuleList!= null && ModuleList.contains(tree_id[k])){
			TreeList.add(tree_id[k]);
		}else{
			ItemList.add(tree_id[k]);
		}
	}

	//clear
	tree_id = null;
	tree_id_query = null;
	TreeIdList.clear();
	TreeIdList = null;
	ModuleList.clear();
	ModuleList = null;

	
	//delete tree_id from role_id
	//Droit.delItemDroitByRoleId(role_id);
	//Droit.delTreeDroitByRoleId(role_id);
	
	//保存角色名称到tab_role
	/* 2008-3-4 Zhaof
	 * 修改角色时不更新role_pid和acc_oid
	 * 修改Droit.updateRoleInfo方法体，参数不变
	 */
	long lUserId = user.getId();
	long lRoleId = user.getRoleId();
	Droit.updateRoleInfo(role_name,role_desc,String.valueOf(lRoleId),String.valueOf(lUserId),role_id);
	
	//save 
	//Droit.saveItemDroit(ItemList,role_id);
	//Droit.saveTreeDroit(TreeList,role_id);
	Droit.updateRoleItemAndTree(role_id, ItemList, TreeList);
	
	//clear
	ItemList.clear();
	ItemList = null;

	TreeList.clear();
	TreeList = null;
	
	out.print("parent.DroitChange('true','"+ role_name +"');");		
}

Module.clearModuleRoleMap();
Tree = null;
Droit = null;

%>

</script>