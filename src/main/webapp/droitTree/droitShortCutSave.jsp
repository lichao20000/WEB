<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%request.setCharacterEncoding("GBK");%>
<%@ page import ="com.linkage.litms.tree.Droit"%>
<%@ page import ="com.linkage.litms.tree.Tree"%>
<%@ page import ="com.linkage.litms.tree.Module"%>
<%@ page import ="java.util.List"%> 
<%@ page import ="java.util.ArrayList"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<script language=javascript> 
<%
String acc_oid = request.getParameter("acc_oid");
String tree_id_query = request.getParameter("tree_id_query");
String login_name = user.getAccount();
Droit Droit = new Droit();
Tree Tree = new Tree();

//����add����Ϊ��ӽ�ɫȨ��
if(true){
	String[] tree_id = tree_id_query.split(",");

	List ItemList = new ArrayList();
	//List TreeList = new ArrayList();

	//��ȡ����ϵͳĿ¼
	List TreeIdList = Tree.getTreeAll();
	//ģ��ϵͳ
	List ModuleList = Tree.getRootMenu();

//	�ж���Щ��ϵͳĿ¼�ڵ�,��Щ�ǹ��ܽڵ�
	for(int k=0;k<tree_id.length;k++){
		if(TreeIdList != null && TreeIdList.contains(tree_id[k])){
			//TreeList.add(tree_id[k]);
		}else if(ModuleList!= null && ModuleList.contains(tree_id[k])){
			//TreeList.add(tree_id[k]);
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

	//save 
	Droit.saveShortCut(ItemList,acc_oid);

	//clear
	ItemList.clear();
	ItemList = null;
	out.print("parent.DroitChange('true','"+ login_name +"');");	
}
Tree = null;
Droit = null;
%>
</script>