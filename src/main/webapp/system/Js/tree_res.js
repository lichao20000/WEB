var tree = null;

var oList = null;

var isManage = false;

var curViewName = "根目录";

var curNodeObj = null;

var urlsep = "%%";

var curViewID = 0;



function TreeNode(id,pid,name,href,layer,ishas,leaf,target,remark){

	this._id = id;

	this._pid = pid;

	this._name = name;

	this._href = href;

	this._layer = layer;

	this._ishas = ishas;

	this._leaf = leaf;

	this._target = target;

	this._remark = remark;

}





function init_tree(){

	tree=new Tree_treeView();

	tree.lineFolder = "./treeimg/";

	tree.showLine=true; //显示连线

	tree.fileImg="./treeimg/persoinfo.gif"; // 设置默认图片

	tree.folderImg1="./treeimg/clsfld.gif";

	tree.folderImg2="./treeimg/openfld.gif";



	tree.folderClass1="FOLDER_1";//文件夹样式(正常状态)

	tree.folderClass2="FOLDER_2";//文件夹样式(鼠标位于文件夹上时)

	tree.folderClass3="FOLDER_3";//文件夹样式(选择状态)

	tree.fileClass1="FILE_1";//文件样式(正常状态)

	tree.fileClass2="FILE_2";//文件样式(鼠标位于文件上时)

	tree.fileClass3="FILE_3";//文件样式(选择状态)



	//生成CSS样式，注意，格式是TD.XXX{...},(XXX是样式名,如folderClass1,selectClass)

	var css=

	"<style>"+

	"TD.FOLDER_1{padding:1pt 5pt  }"+

	"TD.FOLDER_2{color:red;padding:1pt 5pt}"+

	"TD.FOLDER_3{text-decoration:underline;color:brown;padding:1pt 5pt}"+

	"TD.FILE_1{padding:1pt 5pt}"+

	"TD.FILE_2{color:blue;padding:1pt 5pt}"+

	"TD.FILE_3{text-decoration:underline;color:green;padding:1pt 5pt}"+

	"</style>";

	document.write(css);//tree.refresh();



	loadTreeXML();

	loadTreeHTML();



	tree.callback_expanding=function my_expand(nodeID){

		node = tree.getNode(nodeID);

		text = node.child[0].text;

		if(text == "Temp" && node.childCount==1){

			value = node.value;

			arr = value.split(",");

			node.delChild(0);

			addTreeNode(node,arr[0],arr[1]*1+1,arr[2]);

		}



		return true;

	};



	/*tree.callback_click = function my_click(nodeID){

		curNodeObj = tree.getNode(nodeID);

		return true;

	};*/

}



function loadTreeXML(){

	try{
		var XMLDoc;
		//XMLDoc.async = false;
		//XMLDoc.load(idTreeView.XMLSrc);
		
		var xmlSrc = document.getElementById("idTreeView").getAttribute("XMLSrc");
		if (window.ActiveXObject){  
			  XMLDoc= new ActiveXObject("Microsoft.XMLDOM");  
			  XMLDoc.async = false;    
			  XMLDoc.load(xmlSrc);   
			  //alert("11111===="+xmlSrc +"------"+XMLDoc);
		  }   
		  else if  
		     (document.implementation&& document.implementation.createDocument){
			 // alert("222222===="+xmlSrc);
		        try{    
		            XMLDoc = document.implementation.createDocument('', '', null);    
		            XMLDoc.async = false;    
		            XMLDoc.load(xmlSrc);    
		        } catch(e){    
		        	 //alert("3333333===="+xmlSrc);
		            var xmlhttp = new window.XMLHttpRequest();    
		            xmlhttp.open("GET","../Resource/area_xml.jsp",false);    
		            xmlhttp.send(null);    
		            XMLDoc = xmlhttp.responseXML;    
		        }
		        
	              
		  }  
		  else{  
		      alert("load data error");  
		  } 
		 //alert("444444===="+xmlSrc);
		 if (!window.ActiveXObject){  
	    //firefox并不支持selectSingleNode和selectNodes方法;下面两段是用XPath来解决firefox模拟selectSingleNode和selectNodes方法,正确性有待解决  
	        XMLDocument.prototype.selectSingleNode = Element.prototype.selectSingleNode = function (xpath){  
	             var  x = this .selectNodes(xpath)  
	             if ( ! x || x.length < 1 ) return   null ;  
	             return  x[ 0 ];  
	        }  
	        XMLDocument.prototype.selectNodes = Element.prototype.selectNodes = function (xpath){  
	             var  xpe  =   new  XPathEvaluator();  
	             var  nsResolver  =  xpe.createNSResolver( this .ownerDocument  ==   null   ?  
	                 this .documentElement :  this .ownerDocument.documentElement);  
	             var  result  =  xpe.evaluate(xpath,  this , nsResolver,  0 ,  null );  
	             var  found  =  [];  
	             var  res;  
	             while  (res  =  result.iterateNext())  
	                found.push(res);  
	             return  found;  
	        }  
		 }
		
		try{
			var node = XMLDoc.selectNodes("//TreeView").item(0);
		}catch(e){
			var node = XMLDoc.selectNodes("//TreeView")[0];
		}
		var tmp = node.getAttribute("isManage");

		isManage = (tmp=="true")?true:false;

		curViewName = node.getAttribute("title");

		curViewID = node.getAttribute("id");

		//alert(curViewID);

		Nodes = XMLDoc.selectNodes("//TreeNode");

		var node;

		oList = new Array(Nodes.length);



		for(var i=0;i<Nodes.length;i++){
			try{
				node = Nodes.item(i);
			}catch(e){
				node = Nodes[i];
			}
			
			oList[i] = new TreeNode(node.getAttribute("id"),

				node.getAttribute("pid"),

				node.getAttribute("title"),

				node.getAttribute("href"),

				node.getAttribute("layer"),

				node.getAttribute("ishas"),

				node.getAttribute("type"),

				node.getAttribute("target"),

				node.getAttribute("remark"));

		}

	}

	catch(e){

		alert(e);

	}

}



function loadTreeHTML(){

	if(oList == null) return;

	var RootNode = tree.add(0,Tree_ROOT,0,curViewName);

	if(isManage){

		addTreeNode(RootNode,0,1,null);

	}

	else{

		var node1 = RootNode.addChild(Tree_LAST,"公共报表");

		var node2 = RootNode.addChild(Tree_LAST,"私有报表");

		RootNode.expand(true);

		addTreeNode(node1,curViewID,2,"public");

		addTreeNode(node2,-1,-1,"private");

		var node3 = RootNode.addChild(Tree_LAST,"报表公告栏");

		var subnode = node3.addChild(Tree_LAST,"最新发布报表(Top10)");

		subnode.setLink('rp_topList.jsp','top.rightPage.viewPage');

		subnode = node3.addChild(Tree_LAST,"您发布的公共报表");

		subnode.setLink('rp_selfList.jsp','top.rightPage.viewPage');

		subnode = node3.addChild(Tree_LAST,"您发布的私有报表");

		subnode.setLink('rp_selfPrivateList.jsp','top.rightPage.viewPage');

		node3.expand(true);

	}

}



function addTreeNode(node,pid,layer,leaf){

	var curNode;

	var tmp;

	//alert(pid+"\n"+layer+"\n"+leaf);

	for(var i=0;i<oList.length;i++){

		if(oList[i]._layer == layer && oList[i]._pid == pid){

			if(leaf != null && leaf != "null" && oList[i]._leaf != leaf) continue;



			curNode = node.addChild(Tree_LAST,oList[i]._name);

			

			curNode.hint = oList[i]._remark;

			curNode.status = oList[i]._remark;

			tmp = formatURL(oList[i]._href,oList[i]._id);

			if(tmp!=null){

				if(oList[i]._target=="" || oList[i]._target==null)

					curNode.setLink(tmp,"_blank");

				else{

					curNode.setLink(tmp,oList[i]._target);

				}

			}

			

			if(oList[i]._ishas*1>0){

				curNode.addChild(Tree_LAST,"Temp");

				curNode.setValue(oList[i]._id+","+layer+","+leaf);

			}

		}

	}

	if(layer==1 || layer==2 || layer==-1) node.expand(true);

}



function formatURL(href,id){

	if(href == "" || href==null) return null;

	

	if(href.indexOf(urlsep) != -1){

		arr = href.split(urlsep);

		if(arr[1].indexOf("?") != -1) return arr[1]+"&id="+id;

		return arr[1]+"?id="+id;

	}

	else{

		if(href.indexOf("?") != -1) return href+"&id="+id;

		else return href+"?id="+id;

	}

}



function getCurrentPath(){

	o = tree.getSelect();

	if(o==null) {return "";}

	s = o.text;

	pobj = o.parent;

	while(pobj && pobj.id>0){

		s = pobj.text + " >> " + s;

		pobj = pobj.parent;

	}



	return s;

}



function getCurrentLayer(){

	o = tree.getSelect();

	if(o==null) {return -1;}

	v = o.value.toString();

	if(v.indexOf(",") == -1) return 0;

	arr = v.split(",");

	

	return arr[1]*1;

}



function getCurrentPathID(){

	o = tree.getSelect();

	if(o==null) {return "";}

	v = o.value.toString();

	if(v.indexOf(",") == -1) return "";

	arr = v.split(",");

	s = arr[0];

	pobj = o.parent;

	while(pobj && pobj.id>0){

		//alert(pobj.id+"\n"+pobj.text+"\n"+pobj.value)

		v = pobj.value.toString();

		if(v.indexOf(",") != -1){

			arr = v.split(",");

			s += ","+ arr[0];

		}

		pobj = pobj.parent;

	}

	return s;

}