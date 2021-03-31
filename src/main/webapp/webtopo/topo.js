//------------------------------------------------------------
// Copyright (c) 2004-2005 linkage. All rights reserved.
// Version 1.0.03
// Ahthor Haiteng.Yu
//------------------------------------------------------------
//==============================通用函数=========================
var deviceNumber = 0x3116;

//重载Error对象的toString方法
Error.prototype.toString = function() {
	return "Error / Code: 0x"
		 + this.number.toString(16)
		 + "\nMessage: "
		 + this.description;
};
Error.prototype.printStackTrace = function() {print(this);}

//触发异常函数
function _throw(ex, dspt) {
	if (ex instanceof Error)
		print(ex);
	if (typeof(ex) == "number") {
		if (ex < 0xFFFF) ex = deviceNumber * 0x10000 + ex;
		ex = new Error(ex, dspt);
	}

	print(ex);
};

//打印对象
function print(_obj){
	//alert(_obj);
};
//================================================================
//==============================系统函数和变量======================
var DEBUG = true;
var NETWORK_DEVICE_LAYER = 4;	//显示设备层次
var NETWORK_LINK_LAYER = 3;		//显示链路层次

//判断是否大客户系统，如果是的话，则做一些特殊处理
var ISVIP=false;

var idStr = "idWebTopo";
var oWT = null;
var xmlSrc = "";

var arrDev	= null;						//设备数组
var arrLink	= null;						//链路数组
var IsLoadXML = false;					//是否已经载入XML数据
var IsDevDrag=false;						//是否是网元拖动
var IsDrag=false;							//是否是拖动

var x=y=0;									//当前鼠标坐标
var arrCurDrag = null;					//当前被拖动网元对象数组

var arrCurDragLink=null;               //当前被托动的链路对象数组
var curDragLinkObj=null;			   //当前被拖动的链路

//var rx=ry=0;						//当前被拖动网元中心点
var curDragObj = null;				//当前被拖动网元
var curDragLinkFrom = null;			//当前被拖动网元相关链路对象（FROM 端在网元）
var curDragLinkTo = null;				//当前被拖动网元相关链路对象（TO 端在网元）

var arrImgSize = null;		//根据资源类型存放其ICON大小
var arrObjIdAndIndx = null;	//存放对象ID和arrDev索引对应关系
var arrWarnClr = new Array(6);		//告警颜色
var levelWarn = 3;						//Topo上显示告警最低等级
var ViewID	 = null;						//当前视图ID  1:网络视图 2:主机视图 5:vpn视图
var arrPObjectID = null;					//当前网元父对象ID
var allObjIds = null;
var iMinX = 0;
var iMinY = 0;

var coordOffset = 15;
var curDisX=curDisY=0;

//判断是否处于画链路的模式
var isCreateLink=false;

//判断是否处于新增对象的模式
var isCreateNet=false;

//保存的放在粘贴板里面的对象ID
var curPasteObj=null;

//判断当前页是否要显示所有设备的标识
var isDisplayAll="false";		//如果为true的话，则显示所有的设备；false的话只显示管理状态为1的设备和链路

var Link_managed_Color="green";
var Link_unmanaged_Color="glory";

var Link_managed_Weight = "1";

//记录告警的最近更新时间
var updateTime="0";

//当前层所属的最近的用户id
var topouser_id = [-1];

//0:不在线 蓝色 1:在线 绿色 2:暂停 黄色 3:停机 灰色
var STATUS_MAP = [];
STATUS_MAP[0] = "_blue";
STATUS_MAP[1] = "";
STATUS_MAP[2] = "_yellow";
STATUS_MAP[3] = "_glory";

//初始化可变变量
function initTopo(){
	oWT = document.all(idStr);
	this.xmlSrc = oWT.src;
	//注释by smf
	//arrImgSize = new Object();
	arrObjIdAndIndx = new Object();
	//初始化窗口尺寸
	g_scrHeight = window.document.body.offsetHeight;
	g_scrWidth  = window.document.body.offsetWidth;

	arrWarnClr[0] ="#FFFFFF";
	arrWarnClr[1] ="#D5D5D5";
	arrWarnClr[2] ="#FFEBB5";
	arrWarnClr[3] ="#FFC351";
	arrWarnClr[4] ="#FF5459";
	arrWarnClr[5] ="#FF0000";
	loadData();
}

function dispTopo(){
	arrDev	= null;						//设备数组
	arrLink	= null;						//链路数组
	IsLoadXML = false;					//是否已经载入XML数据
	IsDevDrag=false;						//是否是网元拖动
	IsDrag=false;							//是否是拖动

	x=y=0;									//当前鼠标坐标
	arrCurDrag = null;						//当前被拖动网元对象数组

	arrCurDragLink=null;               //当前被托动的链路对象数组
	curDragLinkObj=null;			   //当前被拖动的链路

	curDragObj = null;					//当前被拖动网元
	curDragLinkFrom = null;				//当前被拖动网元相关链路对象（FROM 端在网元）
	curDragLinkTo = null;				//当前被拖动网元相关链路对象（TO 端在网元）

	ViewID	 = null;						//当前视图ID
	arrPObjectID = null;	

	//curDisX=curDisY=0;	
	//arrImgSize = null;
	arrObjIdAndIndx = null;

	
	

	allObjIds = null;
}

//重新显示topo
function viewTopo(){
	//画设备
	for(var i=0;i<arrDev.length;i++){
		arrDev[i].view();
	}
	//画链路
	for(var i=0;i<arrLink.length;i++){
		arrLink[i].view();
	}


}


//画Topo
function drawTopo(){
	if(!IsLoadXML){
		initTopo();
	}

	//alert(arrDev.length);
	//画设备
	if(arrDev!=null)
	{
		for(var i=0;i<arrDev.length;i++){
			arrDev[i].draw();
		}
	}
	//画链路
	if(arrLink!=null)
	{
		for(var i=0;i<arrLink.length;i++){
			arrLink[i].draw();
		}
	}
}

//managed:1 将原来是未管理的改成管理状态；0:将原来是管理状态的改为未管理的
function modifyIconName(icon,managed)
{
	var temp;
	if(managed==0)
	{
		var len=icon.length;
		var index_size=icon.indexOf(".");
		temp=icon.substring(0,index_size)+"_glory."+icon.substring(index_size+1,len);
	}
	else
	{
		re = /_glory/g;			
		temp=icon.replace(re,"");
	
	}
	return temp;
}

//载入Topo数据
function loadData(){
	if(xmlSrc == ""){
		_throw(0x0002,"Web 拓扑没有初始化，找不到数据源");
		return;
	}
	//读取Topo XML数据
	var XMLDoc;
	try{
		XMLDoc = new ActiveXObject("Microsoft.XMLDOM");
		XMLDoc.async = false;
		XMLDoc.load(xmlSrc);
	} catch(e){
		e.printStackTrace();
		return;
	}

	var nodes,node,ln,pid;
	//初始化设备对象-------------------------------
	

	nodes = XMLDoc.selectNodes("//NetView");
	if(nodes.length>0){		
		pid = nodes.item(0).getAttribute("pid");
		arrPObjectID = pid;
		ViewID = nodes.item(0).getAttribute("id");		
	}
	else
	{
		return;
	}
 	switch(parseInt(ViewID,10))
	{
		case 1:
		{
			nodes = XMLDoc.selectNodes("//Nodes/Device");
			ln = nodes.length;
			arrDev = new Array(ln);

			var tmp_icon;
			for(var i=0;i<ln;i++){
				 node = nodes.item(i);
				 //构造一个设备对象
				 arrDev[i] = new NetGraph();
				 //初始化设备对象属性
				 tmpx = parseInt(node.getAttribute("x"),10) + coordOffset;
				 tmpy = parseInt(node.getAttribute("y"),10) + coordOffset;

				 //判断如果如果是管理状态的，则赋于原先本来的图标，如果是不管理状态，则赋不管理状态的图标
				 tmp_icon=node.getAttribute("icon");
				 if(parseInt(node.getAttribute("state"),10)==0)
				 {
					tmp_icon=modifyIconName(node.getAttribute("icon"),0);	 
				 
				 }

				 arrDev[i].init(node.getAttribute("id"),
					 node.getAttribute("title"),
					 tmpx,
					 tmpy,
					 node.getAttribute("ip"),
					 tmp_icon,
					 node.getAttribute("type"),
					 pid,
					 node.getAttribute("state"),
					 node.getAttribute("isCPE"),
					 node.getAttribute("confirm"),
					 node.getAttribute("isConfirmSeg")
					 );
				
				 //存放所有对象ID字符串
				 if(allObjIds == null) allObjIds=node.getAttribute("id");
				 else allObjIds += ","+node.getAttribute("id");
				 //保存对象ID和arrDev索引对应关系
				 arrObjIdAndIndx[node.getAttribute("id")] = i;
				 
			}

			//初始化链路对象---------------------------------
			nodes = XMLDoc.selectNodes("//Nodes/Link");
			ln = nodes.length;
			arrLink = new Array(ln);
			for(var i=0;i<ln;i++){
				 node = nodes.item(i);
				 //构造一个链路对象
				 arrLink[i] = new LinkGraph();
				 //初始化链路对象属性
				 arrLink[i].init(node.getAttribute("id"),
					 node.getAttribute("title"),
					 node.getAttribute("from"),
					 node.getAttribute("to"),
					 node.getAttribute("weight"),
					 node.getAttribute("color"),
					 pid);
				 arrObjIdAndIndx[node.getAttribute("id")] = i ;
			}
			//读取最小XY坐标
			try{
				nodes = XMLDoc.selectNodes("//Nodes/XY");
				if(nodes!= null && nodes.length > 0){ 
					node = nodes.item(0);
					iMinX = parseInt(node.getAttribute("iMinX"),10);
					iMinY = parseInt(node.getAttribute("iMinY"),10);
				}
			}catch(e){
				//alert(e.toString());
			}
			break;
		}
		case 2:
		{
			nodes = XMLDoc.selectNodes("//Nodes/Device");
			ln = nodes.length;
			arrDev = new Array(ln);			
			var tmp_icon;
			for(var i=0;i<ln;i++){
				 node = nodes.item(i);
				 //构造一个设备对象
				 arrDev[i] = new NetGraph();
				 //初始化设备对象属性
				 tmpx = parseInt(node.getAttribute("x"),10) + coordOffset;
				 tmpy = parseInt(node.getAttribute("y"),10) + coordOffset;
				 
				 //判断如果如果是管理状态的，则赋于原先本来的图标，如果是不管理状态，则赋不管理状态的图标
				 tmp_icon=node.getAttribute("icon");
				 /*
				 if(parseInt(node.getAttribute("state"),10)==0)
				 {
					tmp_icon=modifyIconName(node.getAttribute("icon"),0);	 
				 
				 }
				 */
				 arrDev[i].init(node.getAttribute("id"),
					 node.getAttribute("title"),
					 tmpx,
					 tmpy,
					 node.getAttribute("dxbh"),
					 tmp_icon,
					 node.getAttribute("type"),
					 pid,
					 node.getAttribute("state"),
					 "0"
					 );
				
				 //存放所有对象ID字符串
				 if(allObjIds == null) allObjIds=node.getAttribute("id");
				 else allObjIds += ","+node.getAttribute("id");
				 //保存对象ID和arrDev索引对应关系
				 arrObjIdAndIndx[node.getAttribute("id")] = i;
				 
			}


			arrLink = new Array(0);
			
			break;
		}
		case 3:
		{
			
			break;
		}
		case 4:
		{
			nodes = XMLDoc.selectNodes("//Nodes/Device");
			ln = nodes.length;
			arrDev = new Array(ln);

			var tmp_icon;
			for(var i=0;i<ln;i++){
				 node = nodes.item(i);
				 //构造一个设备对象
				 arrDev[i] = new NetGraph();
				 //初始化设备对象属性
				 tmpx = parseInt(node.getAttribute("x"),10) + coordOffset;
				 tmpy = parseInt(node.getAttribute("y"),10) + coordOffset;

				 //判断如果如果是管理状态的，则赋于原先本来的图标，如果是不管理状态，则赋不管理状态的图标
				 tmp_icon=node.getAttribute("icon");
				 if(parseInt(node.getAttribute("state"),10)==0)
				 {
					tmp_icon=modifyIconName(node.getAttribute("icon"),0);	 
				 
				 }
				 arrDev[i].init(node.getAttribute("id"),
					 node.getAttribute("title"),
					 tmpx,
					 tmpy,
					 node.getAttribute("ip"),
					 tmp_icon,
					 node.getAttribute("type"),
					 pid,
					 node.getAttribute("state"),
					 node.getAttribute("isCPE")
					 );
				
				 //存放所有对象ID字符串
				 if(allObjIds == null) allObjIds=node.getAttribute("id");
				 else allObjIds += ","+node.getAttribute("id");
				 //保存对象ID和arrDev索引对应关系
				 arrObjIdAndIndx[node.getAttribute("id")] = i;
				 
			}

			//初始化链路对象---------------------------------
			nodes = XMLDoc.selectNodes("//Nodes/Link");
			ln = nodes.length;
			arrLink = new Array(ln);
			for(var i=0;i<ln;i++){
				 node = nodes.item(i);
				 //构造一个链路对象
				 arrLink[i] = new LinkGraph();
				 //初始化链路对象属性
				 arrLink[i].init(node.getAttribute("id"),
					 node.getAttribute("title"),
					 node.getAttribute("from"),
					 node.getAttribute("to"),
					 node.getAttribute("weight"),
					 node.getAttribute("color"),
					 pid);
				 arrObjIdAndIndx[node.getAttribute("id")] = i ;
			}
			break;
		}
		case 5:
		{
			//记录下当前层拓扑图的
			var temp_pid = pid.split("/");
			//如果父节点为用户对象 (编码格式为5/u/int型) 则记录此用户编码
			if(temp_pid[1]=="u"){
				//追加用户编号列表
				if(topouser_id[topouser_id.length-1]!=pid)
					topouser_id[topouser_id.length] =pid;
//				for(var x in topouser_id){
//					alert("当前拓扑图所属的最近用户编号为： "+topouser_id[x]);
//				}
				//alert("当前拓扑图所属的最近用户编号为： "+topouser_id[topouser_id.length-1]);
			}
			
			nodes = XMLDoc.selectNodes("//Nodes/Device");
			ln = nodes.length;
			arrDev = new Array(ln);

			var tmp_icon;
			for(var i=0;i<ln;i++){
				 node = nodes.item(i);
				 //构造一个设备对象
				 arrDev[i] = new NetGraph();
				 //初始化设备对象属性
				 tmpx = parseInt(node.getAttribute("x"),10) + coordOffset;
				 tmpy = parseInt(node.getAttribute("y"),10) + coordOffset;

				 //判断如果如果是管理状态的，则赋于原先本来的图标，如果是不管理状态，则赋不管理状态的图标
				 tmp_icon=node.getAttribute("icon");
				 if(parseInt(node.getAttribute("state"),10)==0)
				 {
					tmp_icon=modifyIconName(node.getAttribute("icon"),0);
				 }
				 
				 //若type类型不为1（设备），则ip为网元ID
				arrDev[i].init(node.getAttribute("id"),
					 node.getAttribute("title"),
					 tmpx,
					 tmpy,
					 node.getAttribute("ip"),
					 tmp_icon,
					 node.getAttribute("type"),
					 pid,
					 //node.getAttribute("state"),
					 1,
					 //node.getAttribute("isCPE")
					 0
					 );
				
				 //存放所有对象ID字符串
				 if(allObjIds == null) allObjIds=node.getAttribute("id");
				 else allObjIds += ","+node.getAttribute("id");
				 //保存对象ID和arrDev索引对应关系
				 arrObjIdAndIndx[node.getAttribute("id")] = i;
				 
			}

			//初始化链路对象---------------------------------
			nodes = XMLDoc.selectNodes("//Nodes/Link");
			ln = nodes.length;
			arrLink = new Array(ln);
			for(var i=0;i<ln;i++){
				 node = nodes.item(i);
				 //构造一个链路对象
				 arrLink[i] = new LinkGraph();
				 //初始化链路对象属性
				 arrLink[i].init(node.getAttribute("id"),
					 node.getAttribute("title"),
					 node.getAttribute("from"),
					 node.getAttribute("to"),
					 "null",//补齐链路的weigth和color属性
					 node.getAttribute("color"),
					 pid);
				 arrObjIdAndIndx[node.getAttribute("id")] = i ;
			}
			break;			
			
		}
	}

	//释放变量
	ln = null;
	node = null;
	nodes = null;
	IsLoadXML = true;
}

//根据网元ID查找网元对象索引
function findDevObjByID(id){
	/*for(var i=0;i<arrDev.length;i++){
		if(arrDev[i]._id == id) return i;
	}*/

	if(typeof(arrObjIdAndIndx[id]) != "undefined"){
		return arrObjIdAndIndx[id];
	}

	return -1;
}

//根据网元ID查找相关链路对象索引
function findLinkObjByID(id){
	curDragLinkFrom = new Array();
	curDragLinkTo = new Array();
	
	for(var i=0;i<arrLink.length;i++){
		if(arrLink[i]._from == id)
			curDragLinkFrom[curDragLinkFrom.length]=i;
		if(arrLink[i]._to == id)
			curDragLinkTo[curDragLinkTo.length]=i;
	}
}


function DragLinkObj(curDragObjID)
{
	this._curDragObjID=curDragObjID;//当前被拖动链路索引
}

function DragObj(curDragObjID,curDragLinkFrom,curDragLinkTo){
	this._curDragObjID = curDragObjID;					//当前被拖动网元索引
	this._curDragLinkFrom = curDragLinkFrom;			//当前被拖动网元相关链路对象索引（FROM 端在网元）
	this._curDragLinkTo = curDragLinkTo;				//当前被拖动网元相关链路对象索引（TO 端在网元）
}



//计算字符串长度（字体为12px）
function getStrLn(s){
	var ln=0;
	for(var i=0;i<s.length;i++){
		if(s.charCodeAt(i)>256)
			ln+=2;
		else 
			ln++;
	}
	return ln*6;
}

//设置Topo Panel在页面上的位移
function setTopoDisplacement(x,y){
	curDisX = x;
	curDisY = y;
}



//获取图像大小，并保存在arrImgSize对象中
function getIconSize(icon){
	//获取原始图片的名称
	//icon=modifyIconName(icon,1);

	var arr = new Array(2);	
	if(typeof(arrImgSize[icon]) != "object"){
		var iTimerID;
		s = "images/"+icon;
		idTmpImg.removeAttribute("src");
		idTmpImg.setAttribute("src",s);		
		start = (new Date()).getTime();
		end = (new Date()).getTime();		
		while(idTmpImg.readyState !="complete" && (end-start)<800){			
			end = (new Date()).getTime();		
		}		
		arr[0] = idTmpImg.width;
		arr[1] = idTmpImg.height;
		arrImgSize[icon] = arr;
	}
	else{
		arr = arrImgSize[icon];
	}
	return arr;
}


//================================================================
//==========================定义网元抽象类=========================
function Graph(){
	this._id		= null;			//网元ID
	this._pid		= null;
	this._title	= null;			//网元名称
	this._transform = 0;			//是否被改变坐标
};
//获取Graph类的一个引用
var _p = Graph.prototype;

//初试化参数
_p.init = function(){
	_throw(0x0001,"抽象类不能够实例化具体数据");
};

//画图形
_p.draw = function(){
	_throw(0x0001,"抽象类不能够实例化具体数据");
};

//删除图形
_p.remove = function(){
	_throw(0x0001,"抽象类不能够实例化具体数据");
};

//设置改变状态
_p.setTransform = function(transform){
	this._transform = transform;
};

//重载Object的toString方法
_p.toString = function(){
	return "oGraph";
};
//================================================================

//=============================设备对象============================
function NetGraph(){
	Graph.call(this);
	this._x		= 0;		//X轴坐标(left)
	this._y		= 0;		//Y轴坐标(top)
	this._ip		= null;	//IP地址
	this._w		= 0;		//图像的宽
	this._h		= 0;		//图像的高
	this._icon_ch   = null;//原始图标名称
	this._icon	= null;	//图像图标
	this._type	= -1;		//设备型号 0为网段
	this._state =0;         //设备是否管理
	this._iscpe =0;         //是否cpe设备,0为非cpe设备；1为cpe设备

	this._isconfirm =0;         //是否为确认设备,0非；1是
	this._isConfirmSeg =0;         //是否为待确认设备网段,0非；unkdev是

};

//获取NetGraph类的一个引用
var _p = NetGraph.prototype = new Graph;

//实现Graph类的init方法
_p.init = function(id,title,x,y,ip,icon,type,pid,state,iscpe,isconfirm,isConfirmSeg){
	this._id	= id;
	this._title = title;
	this._x		= eval(x)*1;
	this._y		= eval(y)*1;
	this._ip		= ip;
	this._icon_ch = this._icon	= icon;
	this._type	= eval(type)*1;
	this._pid		= pid;
	//增加显示告警的级别
	this._level       = -1;
	this._state=eval(state)*1;
	this._iscpe=eval(iscpe)*1;

	this._isconfirm = isconfirm;
	this._isConfirmSeg = isConfirmSeg; 
};

//实现Graph类的draw方法
_p.draw = function(){
	//如果不是显示所有的设备，并且设备的状态是不管理设备的状态，则直接返回
	tmp_display="";
	switch(parseInt(ViewID,10))
	{
		case 1:
		{
			if(isDisplayAll=="false" && this._state==0)
			{
				tmp_display="none";
			}
			break;
		}
		case 2:
		{
		
			break;
		}
		case 3:
		{
		
			break;
		}
		case 4:
		{
		
			break;
		}
	}
	arr = getIconSize(this._icon);
	w	= arr[0];
	h	= arr[1];

	t_w = getStrLn(this._title);
	t_left = (w-t_w)/2;

	//注册网段双击事件和设备的双击事件
	eventStr = "";
	/*
	if(this._type==0){
		eventStr = " ondblclick=\"Dev_DblClick();\"";
	}
	*/
	eventStr = " ondblclick=\"Dev_DblClick('"+this._type+"');\"";

	s = "<v:image id='"+ this._id +"' src='images/"+ this._icon +"' style='position:absolute;top:"+ this._y +";left:"+ this._x+";width:"+ w +";height:"+ h +";z-index:"+ NETWORK_DEVICE_LAYER +";display:"+tmp_display+"' onmousedown=\"Dev_MouseDown();\""+ eventStr +">";
	s += "<span nowrap style=\"position:relative;top:"+h+";left:"+t_left+";font-size:12px;z-index:6;width:"+t_w+";cursor:default\">"+ this._title +"</span>"
	s += "</v:image>";

	//document.write(s);
	oWT.insertAdjacentHTML("BeforeEnd",s);
	s = null;
};

//实现Graph类的remove方法
_p.remove = function(){
	try{		
		o = document.all(this._id);		
		document.body.removeChild(o);		
		o = null;
	}
	catch(e){
		e.printStackTrace();
	}
};

//实现Graph类的removeNode方法
_p.removeNode = function(){
	try{		
		o = document.all(this._id);
		o.removeNode(true);
		o = null;
	}
	catch(e){
		e.printStackTrace();
	}
};

//绘画对象外边框
_p.drawBorder = function(){
	o = document.all("B_"+this._id);
	if(o != null) return;
	w	= arrImgSize[this._icon][0];
	h	= arrImgSize[this._icon][1];

	s = "<v:rect id=\"B_"+ this._id+"\" style=\"position:absolute;top:"+ (this._y-1)+";left:"+(this._x-1)+";width:"+ (w+3) +";height:"+ (h+3) +";z-index:2\" strokecolor = \"blue\"  coordsize = \"21600,21600\" strokeweight=\"3px\"  ><v:fill opacity = \"0\" ></v:fill></v:rect>";	
	oWT.insertAdjacentHTML("BeforeEnd",s);
}

//删除对象外边框
_p.removeBorder = function(){
	try{
		o = document.all("B_"+this._id);
		o.removeNode(true);
		o = null;
	}
	catch(e){
		e.printStackTrace();
	}finally{
		o = null;
	}
}

//重新设置坐标
_p.setXY = function(x,y){
	this._x = x;
	this._y = y;
};

//对象移动
_p.move = function(){
	try{
		o = document.all(this._id);
		o.style.posLeft = this._x;
		o.style.posTop = this._y;
		o = document.all("B_"+this._id);
		if(typeof(o) == "object"){
			o.style.posLeft = this._x-2;
			o.style.posTop = this._y-2;
		}
		o = null;
	}
	catch(e){
		e.printStackTrace();
	}
};
//对象是否显示的方法
_p.view=function(){
	try{
		o = document.all(this._id);
		if(isDisplayAll=="false" && this._state==0)
		{
			o.style.display="none";	
		}
		else
		{
			o.style.display="";
		}
		o=null;
	}
	catch(e)
	{
		e.printStackTrace();
	}
};
//企业网关需要获取MC缓存中获取状态值，并修改网元图片名称
//0:不在线 蓝色 1:在线 绿色 2:暂停 黄色 3:停机 灰色
_p.drawStatus=function(type){
	if(type < 0 || type > 3) return;
	var icon = this._icon;
	var len=icon.length;
	var index=icon.indexOf(".");
	var iconname = icon.substring(0,index);
	var iconsuffix = icon.substring(index+1,len);
	
	var _type = STATUS_MAP[type];
	if(_type == null) _type = "";
	this._icon_ch = iconname + _type + "." + iconsuffix;
	document.all(this._id).src="images/" + this._icon_ch;
	this.view();
};
//恢复所有设备的状态值
_p.resetStatus=function(){
	document.all(this._id).src="images/" + this._icon;
	this.view();
};

//画告警数量
_p.drawWarn = function(num,level){
	//如果level的告警级别小于设备当前的告警级别，则不需要画告警
	if(level < this._level) return;
	this.removeWarn();
	//modify by hemc 2006-11-20
	if(level<=levelWarn) return;
	try{
		o = document.all(this._id);
		s = document.all("idWarn_"+level).innerHTML;
		var unit="M";
		switch(parseInt(level,10))
		{
			case 5:
			{
				unit="C";//clamant
				break;
			}
			case 4:
			{
				unit="S";//serious
				break;
			}
			case 3:
			{
				unit="M";//Message
				break;
			}	
		
		}

		tmp = num+unit;

		s = "<div style=\"position:relative;left:"+(o.style.posWidth/2)+";top:-27;border:#000000 1px solid;background-color: "+ arrWarnClr[level] +";width:"+tmp.length*10+";\">"+s.replace("<!--Warn-->",tmp)+"</div>";
		o.insertAdjacentHTML("beforeEnd",s);
		o = null;
		this._level=level;
	}
	catch(e){
		e.printStackTrace();
	}
	

};
//删除告警数量
_p.removeWarn = function(){
	try{
		o = document.all(this._id);
		sub = o.getElementsByTagName("DIV");
		if(sub.length==1) o.removeChild(sub[0]);
		sub = null;
		o = null;
		this._level=-1;
	}
	catch(e){
		e.printStackTrace();
	}
};
//重载Object的toString方法
_p.toString = function(){
	return "oDevGraph";
};
//=========================链路对象====================================
function LinkGraph(){
	Graph.call(this);
	this._form	= null;	//链路起点对象ID
	this._to		= null;	//链路终点对象ID
	this._weight	= null;	//链路粗细
	this._color		= null;	//链路颜色
};

//获取LinkGraph类的一个引用
var _p = LinkGraph.prototype = new Graph;

//实现Graph类的init方法
_p.init = function(id,title,from,to,weight,color,pid){
	this._id	= id;
	this._title	= title;
	this._from	= from;
	this._to	= to;
	this._weight= weight;
	this._color	= color;
	this._pid = pid;
	
};

//实现Graph类的draw方法
_p.draw = function(){

	_from_obj	= arrDev[findDevObjByID(this._from)];
	if(_from_obj == null){
		_throw(0x0004,"系统无法找到ID号为："+ this._from +" 的网元");
		return;
	}
	
	_to_obj		= arrDev[findDevObjByID(this._to)];
	if(_to_obj == null){
		_throw(0x0004,"系统无法找到ID号为："+ this._to +" 的网元");
		return;
	}
	
	

	tmp_display="";

	s_from_state=_from_obj._state;
	s_to_state=_to_obj._state;
	
	link_state=1;
	
	if(this._color.length > 0 && this._color != "null") {
		tmp_Color=this._color;
	} else {
		tmp_Color=Link_managed_Color;
	}

	if(this._weight.length > 0 && !this._weight != "null") {
		tmp_Weight = this._weight;
	} else {
		tmp_Weight = Link_managed_Weight;
	}

	if(parseInt(s_from_state,10)==0 || parseInt(s_to_state,10)==0)
	{
		tmp_Color=Link_unmanaged_Color;
		link_state=0;
	}
	
	
	if(isDisplayAll=="false" && link_state==0)
	{
		tmp_display="none";
		
	}

	s_from_x = _from_obj._x + arrImgSize[_from_obj._icon][0]/2;
	s_from_y = _from_obj._y + arrImgSize[_from_obj._icon][1]/2;
	s_to_x   = _to_obj._x + arrImgSize[_to_obj._icon][0]/2;
	s_to_y   = _to_obj._y +arrImgSize[_to_obj._icon][1]/2;
	
	//alert("tmp_Weight = "+tmp_Weight);
	s = "<v:line id='"+ this._id +"' strokeColor='"+tmp_Color+"' ";
	s += "strokeweight='" + tmp_Weight + "' "
	s += "from='"+ s_from_x +","+ s_from_y +"' ";
	s += "to='"+ s_to_x +","+ s_to_y +"' ";
	s += "style='position:absolute;z-index:"+NETWORK_LINK_LAYER +";display:"+tmp_display+"' ";
	s +=" onmousedown=\"Link_MouseDown();\" />";

	//document.write(s);
	oWT.insertAdjacentHTML("BeforeEnd",s);
	s = null;
	_from_obj = null;
	_to_obj = null;
};

//实现Graph类的remove方法
_p.remove = function(){
	try{
		o = document.all(this._id);
		document.body.removeChild(o);
		o = null;
	}
	catch(e){
		e.printStackTrace();
	}
};

//对象移动
_p.move = function(way,fx,fy){
	try{
		o = document.all(this._id);
		if(way == "from"){
			o.from = fx+","+fy;
		}else{
			o.to = fx+","+fy;	
		}
		o = null;
	}
	catch(e){
		e.printStackTrace();
	}
};

//实现是否显示链路
_p.view=function()
{
	try{
		o = document.all(this._id);
		_from_obj	= arrDev[findDevObjByID(this._from)];
		if(_from_obj == null){
			_throw(0x0004,"系统无法找到ID号为："+ this._from +" 的网元");
			return;
		}
		
		_to_obj		= arrDev[findDevObjByID(this._to)];
		if(_to_obj == null){
			_throw(0x0004,"系统无法找到ID号为："+ this._to +" 的网元");
			return;
		}
		s_from_state = _from_obj._state;
		s_to_state = _to_obj._state;		
		link_state = 1;
		//alert(this._weight);
		if(this._weight.length > 0 && this._weight != "null"){
			tmp_Weight = this._weight;
		}else{
			tmp_Weight = Link_managed_Weight;
		}
		//alert(tmp_Weight);
		//tmp_Color=Link_managed_Color;
		if(this._color.length > 0 && this._color != "null") {
			tmp_Color = this._color;
		} else {
			tmp_Color = Link_managed_Color;
		}

		if(parseInt(s_from_state,10) == 0 || parseInt(s_to_state,10)==0)
		{
			tmp_Color = Link_unmanaged_Color;
			link_state=0;
		}
		
		if(isDisplayAll=="false" && link_state==0)
		{
			o.style.display="none";
			
		}
		else
		{
			o.style.display="";
		}
		o.strokeColor = tmp_Color;
		o.strokeweight = tmp_Weight;
		o = null;
	}
	catch(e){
		e.printStackTrace();
	}


};

_p.drawBorder=function(){
	o = document.all(this._id);
	o.strokeColor="blue";
	o=null;

};

_p.removeBorder=function(){

	try{
		o = document.all(this._id);
		_from_obj	= arrDev[findDevObjByID(this._from)];
		if(_from_obj == null){
			_throw(0x0004,"系统无法找到ID号为："+ this._from +" 的网元");
			return;
		}
		
		_to_obj		= arrDev[findDevObjByID(this._to)];
		if(_to_obj == null){
			_throw(0x0004,"系统无法找到ID号为："+ this._to +" 的网元");
			return;
		}
		s_from_state=_from_obj._state;
		s_to_state=_to_obj._state;		
		link_state=1;
		//tmp_Color=Link_managed_Color;
		if(this._color.length > 0 && this._color != "null") {
			tmp_Color=this._color;
		} else {
			tmp_Color = Link_managed_Color;
		}

		if(parseInt(s_from_state,10)==0 || parseInt(s_to_state,10)==0)
		{
			tmp_Color=Link_unmanaged_Color;
			link_state=0;
		}
		
		if(isDisplayAll=="false" && link_state==0)
		{
			o.style.display="none";
			
		}
		else
		{
			o.style.display="";
		}
		o.strokeColor=tmp_Color;
		o = null;
	}
	catch(e){
		e.printStackTrace();
	}

};




//重载Object的toString方法
_p.toString = function(){
	return "oLinkGraph";
};
//======================================================================
//===================================事件驱动============================
//查找网元是否在拖放列表中
function findDragObjListByID(id){
	if(arrCurDrag == null) return false;
	for(var i=0;i<arrCurDrag.length;i++){
		if(arrCurDrag[i]._curDragObjID == id)
			return true;
	}
	return false;
}

//查找链路是否在拖放列表中
function findDragLinkObjListByID(id){
	if(arrCurDragLink == null) return false;
	for(var i=0;i<arrCurDragLink.length;i++){
		if(arrCurDragLink[i]._curDragObjID == id)
			return true;
	}
	return false;
}

function Link_MouseDown(){
	if(isCreateLink || isCreateNet)
	{
		return;
	}
	//获取当前事件触发对象
	curDragObj = event.srcElement;
	oIndex = findDevObjByID(curDragObj.id);
	arrObjectID =  oIndex;
	arrPObjectID = arrLink[oIndex]._pid;
	if(!findDragLinkObjListByID(oIndex)){
		clearLinkBorder();
		arrLink[oIndex].drawBorder();	

		clearDevBorder();
		arrCurDrag = null;

		arrCurDragLink = null;
		arrCurDragLink = new Array();
		arrCurDragLink[0] = new DragLinkObj(oIndex);
	}

	runLink();

}

function Dev_DblClick(dbl_type){
	//dbl_type 被双击对象的type值
	
	switch(parseInt(ViewID,10))
	{
		case 1:
		{
			if(dbl_type==0)
			{
				CMS_Click(null,1,"getChildTopo.jsp","childFrm");
			}
			else
			{
				//alert(dbl_type);
				CMS_Click(null,1,"showDevDetail","javascript");
			}
			break;
		}
		case 2:
		{
			if(dbl_type==1)
			{
				CMS_Click(null,1,"getChildTopo.jsp","childFrm");
			}
			else
			{
				CMS_Click(null,1,"HOST_ShowObjectAttr","javascript");
				return;
			}

			break;
		}
		case 3:
		{
		
			break;
		}
		case 4:
		{
			//dbl_type == 1 主机视图中  dbl_type == 0 网络视图中
			if(dbl_type == 1 || dbl_type == 0)
			{
				CMS_Click(null,1,"getChildTopo.jsp","childFrm");
			}
			else
			{
				if(arrDev[arrObjectID] != null){
					//网络设备 1/dev/  主机设备 2/
					var tmp_id = arrDev[arrObjectID]._id;
					if(checkDeviceViewType(tmp_id) == 2){
						CMS_Click(null,1,"HOST_ShowObjectAttr","javascript");		
					}else if(checkDeviceViewType(tmp_id) == 1){
						CMS_Click(null,1,"showDevDetail","javascript");
					}else{
					}
				}
			}
			break;
		}
		case 5:
		{
			//alert("被双击对象类型编号为："+dbl_type);
			//0:网段,2:用户组,3:用户,5:整体拓扑节点,6 VPN视图
			if(dbl_type==0||dbl_type==2||dbl_type==3||dbl_type==5||dbl_type==6)
			{
				CMS_Click(null,1,"getChildTopo.jsp?topoType=5","childFrm");
			}
			//1 设备
			else if(dbl_type==1)
			{
				CMS_Click(null,1,"showDevDetail","javascript");
			}
			//接入点
			else if(dbl_type==4)
			{
				//CMS_Click(null,1,"showDevDetail","javascript");
			}
			break;
		}
	}
}

//网元Down事件
function Dev_MouseDown(){
	//如果是新增链路或者新增对象的时候返回
	if(isCreateNet)
	{
		return;
	}
	//获取当前事件触发对象
	curDragObj = event.srcElement;
	if(event.srcElement.tagName != "image")  return;
	//传当前网元ID给菜单系统,arrObjectID 是在coolmenu.js定义的
	//arrObjectID = curDragObj.id;
	//alert(curDragObj.id);
	oIndex = findDevObjByID(curDragObj.id);
	arrObjectID = oIndex;
	arrPObjectID = arrDev[oIndex]._pid;

	//没有按ctrl键而且不在拖放列表中时，点击设备直接选中
	if(!event.ctrlKey && !findDragObjListByID(oIndex)){
		//查找网元相关链路列表
		findLinkObjByID(curDragObj.id);
		
		clearLinkBorder();
		arrCurDragLink = null;
		curDragLinkObj=null;

		//删除当前被选中的对象边框
		clearDevBorder();
		//加边框
		arrDev[oIndex].drawBorder();
		//初始化拖动对象列表

		arrCurDrag = null;
		arrCurDrag = new Array();
		arrCurDrag[0] = new DragObj(oIndex,curDragLinkFrom,curDragLinkTo);
	}
	//如果设备没有被选中，但按ctrl键，则在已选择列表中增加
	else if(!findDragObjListByID(oIndex) && event.ctrlKey){
		//加边框
		arrDev[oIndex].drawBorder();
		//在已选择列表中增加
		if (arrCurDrag == null){
			arrCurDrag = new Array();
			arrCurDrag[0] = new DragObj(oIndex,curDragLinkFrom,curDragLinkTo);
		}
		else{
			arrCurDrag[arrCurDrag.length] = new DragObj(oIndex,curDragLinkFrom,curDragLinkTo);
		}
	}
	//如果设备已被选中，但按ctrl键，则在已选择列表中删除
	else if(findDragObjListByID(oIndex) && event.ctrlKey){
		var tmpArr = arrCurDrag;
		var j = 0;
		arrCurDrag = new Array();
		for (var i=0;i<tmpArr.length;i++){
			if (arrDev[tmpArr[i]._curDragObjID]._id != curDragObj.id){
				arrCurDrag[j] = tmpArr[i];
				j++;
			}
			else{
				o = arrDev[tmpArr[i]._curDragObjID];
				o.removeBorder();
				o = null;
			}
		}
	}

	if(!isCreateLink && !isCreateNet)
	{
		run();
	}
}

function clearDevBorder(){
	if(arrCurDrag==null) return;
	for(var i=0;i<arrCurDrag.length;i++){
		o = arrDev[arrCurDrag[i]._curDragObjID];
		o.removeBorder();
		o = null;
	}
}

function clearLinkBorder(){
	if(arrCurDragLink==null) return;
	for(var i=0;i<arrCurDragLink.length;i++){
		o = arrLink[arrCurDragLink[i]._curDragObjID];
		o.removeBorder();
		o = null;
	}
	

}

//网元拖动事件触发器
function runLink(){

	//判断鼠标左键是否按下,如果右健则显示右健菜单	
	if(event.button!=1){
		if(arrCurDragLink.length == 1){
			//alert(arrCurDragLink[0]._curDragObjID);
			//获取网元对象
			o = arrLink[arrCurDragLink[0]._curDragObjID];
			//alert("run 过程中触发"+o._type)
			//初始化右健菜单
			//if(RM_Menu.length==0) 
			createRMenu("",o._pid);
			//显示右健菜单
			showRightMenu();
		}
		else if(arrCurDragLink !=null && arrCurDragLink.length>1)
		{
			//alert(arrCurDrag.length);
			/*
			var tmp="";
			var type="";
			for(var i=0;i<arrCurDrag.length;i++)
			{
				if(i==0)
				{
					tmp=arrCurDrag[i]._curDragObjID;
					arrPObjectID = arrDev[arrCurDrag[i]._curDragObjID]._pid;
					type=arrDev[arrCurDrag[i]._curDragObjID]._type;
				}
				else
				{
					tmp +=","+arrCurDrag[i]._curDragObjID;
				}					
			}
			arrObjectID=tmp;

			createRMenu(type,arrPObjectID);
			*/
		}
		else{
			_throw(0x0010,"目前不支持多个设备选中的右健菜单");
		}
		return;
	}
	
	IsDevDrag=false;

	x=event.clientX;
	y=event.clientY;
	
	
	//设置TOPO层的鼠标移动事件和鼠标UP事件
	//oWT.onmousemove=Dev_MouseMove;
	//oWT.onmouseup = Dev_MouseUp;
}


//网元拖动事件触发器
function run(){
	//判断鼠标左键是否按下,如果右健则显示右健菜单	
	if(event.button!=1){
		if(arrCurDrag.length == 1){
			//alert(arrCurDrag[0]);
			//获取网元对象
			o = arrDev[arrCurDrag[0]._curDragObjID];
			//alert("run 过程中触发"+o._type)
			//初始化右健菜单
			//if(RM_Menu.length==0) 
			createRMenu(o._type,o._pid);
			//显示右健菜单
			showRightMenu();
		}
		else if(arrCurDrag !=null && arrCurDrag.length>1)
		{
			//alert(arrCurDrag.length);
			var tmp="";
			var type="";
			for(var i=0;i<arrCurDrag.length;i++)
			{
				if(i==0)
				{
					tmp=arrCurDrag[i]._curDragObjID;
					arrPObjectID = arrDev[arrCurDrag[i]._curDragObjID]._pid;
					type=arrDev[arrCurDrag[i]._curDragObjID]._type;
				}
				else
				{
					tmp +=","+arrCurDrag[i]._curDragObjID;
				}					
			}
			arrObjectID=tmp;

			createRMenu(type,arrPObjectID);
			
		}
		else{
			_throw(0x0010,"目前不支持多个设备选中的右健菜单");
		}
		return;
	}
	
	IsDevDrag=true;

	x=event.clientX;
	y=event.clientY;
	
	
	//设置TOPO层的鼠标移动事件和鼠标UP事件
	oWT.onmousemove=Dev_MouseMove;
	oWT.onmouseup = Dev_MouseUp;
}

//网元选中触发的TOPO层的鼠标移动事件
function Dev_MouseMove(){
	//alert("IsDevDrag is"+IsDevDrag);
	//执行条件
	var rx=ry=0;
	
    if(event.button==1 && IsDevDrag && !isCreateLink && !isCreateNet){
		for(var i=0;i<arrCurDrag.length;i++){
			//获取网元对象
			o = arrDev[arrCurDrag[i]._curDragObjID];
			o.setTransform(1);
			po = document.all(o._id);
			rx = po.style.posLeft + po.style.posWidth/2;
			ry = po.style.posTop + po.style.posHeight/2;
			//重新设置网元坐标
			o.setXY(o._x+event.clientX-x,o._y+event.clientY-y);
			//网元移动
			o.move();
			//网元相关链路移动
			of = arrCurDrag[i]._curDragLinkFrom;
			if (of != null){
				for(var j=0;j<of.length;j++){
					if(typeof(of[j]) == "number")
						arrLink[of[j]].move("from",rx,ry);
				}
			}
			ot = arrCurDrag[i]._curDragLinkTo;
			if (ot != null){
				for(var j=0;j<ot.length;j++){
					if(typeof(ot[j]) == "number")
						arrLink[ot[j]].move("to",rx,ry);
				}
			}
		}
		//清除临时变量
		o = null;
		of = null;
		ot = null;
		//为下次移动记录当前鼠标坐标
		x=event.clientX;
		y=event.clientY;
    }
    return false;	
}

//网元UP事件
function Dev_MouseUp(){
	
	//没有按ctrl键但在拖放列表中时，点击设备直接选中
	if(!event.ctrlKey && findDragObjListByID(oIndex)){
		//查找网元相关链路列表
		findLinkObjByID(curDragObj.id);
		
		clearLinkBorder();
		arrCurDragLink = null;
		curDragLinkObj=null;

		//删除当前被选中的对象边框
		clearDevBorder();
		//加边框
		arrDev[oIndex].drawBorder();
		//初始化拖动对象列表

		arrCurDrag = null;
		arrCurDrag = new Array();
		arrCurDrag[0] = new DragObj(oIndex,curDragLinkFrom,curDragLinkTo);
	}
	
	if(!isCreateLink && !isCreateNet)
	{
		curDragObj = null;
		curDragLinkFrom = null;
		curDragLinkTo = null;
		IsDevDrag = false;	
		curDragLinkObj=null;	
		
		//rx=ry=0;
		x=y=0;
		oWT.onmousemove = Main_MouseMove;
		oWT.onmouseup = Main_MouseUp;
		oWT.onmousedown = Main_MouseDown;
	}
	else
	{
		
	}
	
}

//Topo层Down事件
function Main_MouseDown(){
	//alert(event.button);
	findWTPos();

	if(isCreateLink)
	{
		return;
	}
	
	if(isCreateNet)
	{
		return;
	}
	
	//判断鼠标是否为左键
	if(event.button!=1) { 
		
		if(arrPObjectID!="null" && arrPObjectID!=""){
			if(arrCurDrag !=null && arrCurDrag.length==1)
			{
				o = arrDev[arrCurDrag[0]._curDragObjID];
				createRMenu(o._type,o._pid);
			}
			else if(arrCurDrag !=null && arrCurDrag.length>1)
			{
				//alert(arrCurDrag.length);
				var tmp="";
				var type="";
				for(var i=0;i<arrCurDrag.length;i++)
				{
					if(i==0)
					{
						tmp=arrCurDrag[i]._curDragObjID;
						arrPObjectID = arrDev[arrCurDrag[i]._curDragObjID]._pid;
						type=arrDev[arrCurDrag[i]._curDragObjID]._type;
					}
					else
					{
						tmp +=","+arrCurDrag[i]._curDragObjID;
					}					
				}
				arrObjectID=tmp;
				createRMenu(type,arrPObjectID);
				
			}
			else if(arrCurDragLink !=null && arrCurDragLink.length==1)
			{
				o = arrLink[arrCurDragLink[0]._curDragObjID];
				createRMenu("",o._pid);
			}
			else
			{
				createRMenu("-200",arrPObjectID);
			}
			

			showRightMenu();
		}
		return;
	}
	if(event.srcElement.tagName == "image")  return;
	
	if(event.srcElement.tagName == "line")  return;
	//删除当前被选中的对象边框
	clearDevBorder();

	clearLinkBorder();

	
	x=event.clientX - curDisX;
	y=event.clientY - curDisY;
	//alert(x);
	//alert(y)
	//alert(oWT.offsetWidth);
	//alert(oWT.offsetHeight);

	//offsetWidth:代表的是层的宽
	//alert(oWT.offsetHeight);
	//alert(oWT.clientHeight);
	//alert(oWT.scrollHeight);
	//alert(oWT.scrollWidth);
	//if(x<(oWT.scrollWidth) && y<(oWT.scrollHeight)){
	if(x<(oWT.offsetWidth-20) && y<(oWT.offsetHeight-20)){	
		selRect.style.display = "";		
		selRect.style.posLeft=x;
		selRect.style.posTop=y;
		selRect.style.posWidth=selRect.style.posHeight=0;			
		IsDrag = true;
	}
	
	//IsDrag = true;
	arrCurDrag = null;
	arrCurDragLink = null;
}

//TOPO层的鼠标移动事件
function Main_MouseMove(){
	if(isCreateLink || isCreateNet)
	{
		return;
	}
	//alert("3333333333333333");
	if(selRect.style.display=="" && IsDrag){
		selRect.style.posWidth=(x)-selRect.style.posLeft;
		selRect.style.posHeight=(y)-selRect.style.posTop;
	}

	x=event.clientX - curDisX;
	y=event.clientY - curDisY;
}

//TOPO层的Up事件
function Main_MouseUp(){
	if(!isCreateLink && !isCreateNet)
	{
		if(event.button==2) return;	
		if(IsDrag)
			findRectInObj();
		selRect.style.posWidth=selRect.style.posHeight=0;
		selRect.style.display=="none";
		clearRightMenu();
		IsDrag = false;
		return;
		
	}
	else if(isCreateLink)
	{
		if(arrObjectID==null) return;
		if(event.srcElement.tagName != "image")  return;
		var from=arrDev[arrObjectID]._id;
		//alert(arrDev[arrObjectID]._id);
		var to=event.srcElement.id;
		//如果两次选择的是相同的设备
		if(from==to)
		{
			return;
		}

		//alert(event.srcElement.id);
		if(arrDev[arrObjectID]._id>event.srcElement.id)
		{
			from=event.srcElement.id;
			to=arrDev[arrObjectID]._id;
		}
		
		var id=from+to+"/0";
		//if(arrObjIdAndIndx)
		
		if(typeof(arrObjIdAndIndx[id])=="undefined")
		{	
			//VPN视图
			if(ViewID == "5"){
				var page="AddVpnDevice.jsp?obj_type=link&id="+id+"&from="+from+"&to="+to+"&parent_id="+arrPObjectID+"&ViewID="+ViewID+"&user_id="+topouser_id[topouser_id.length-1];
				//window.open(page);
				document.all("childFrm").src=page;
			}else{
				var page="AddDevice.jsp?obj_type=link&id="+id+"&from="+from+"&to="+to+"&parent_id="+arrPObjectID+"&ViewID="+ViewID;
				document.all("childFrm").src=page;
			}
		}
		else
		{
			//alert(arrObjIdAndIndx[id]);
		}
	}
	else if(isCreateNet)
	{
		//alert("当前topo图arrPObjectID："+arrPObjectID);
		if(arrPObjectID=="null"||(arrPObjectID.indexOf("unkdev")!=-1))
		{
			//alert(arrPObjectID);
			alert("对不起，你不能在当前层建对象！");
			isCreateNet=false;
			isCreateLink=false;
			return;
		}
		var x=event.clientX+document.body.scrollLeft-curDisX;
		var y=event.clientY+document.body.scrollTop-curDisY;		
		//alert(x+","+y);
		var page="";
		var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	
		switch(parseInt(ViewID,10))
		{
			case 1:
			{
				page="AddObjectView.jsp?x="+x+"&y="+y+"&parent_id="+arrPObjectID+"";
				newWin(page,otherpra,400,500);
				break;
			}
			case 2:
			{
				page="HOST_ManagerHostObject.jsp?action=add&x="+x+"&y="+y+"&parent_id="+arrPObjectID+"";
				newWin(page,otherpra,550,280);
				break;
			}
			case 3:
			{
				
				break;
			}
			case 4:
			{
				
				break;
			}
			case 5:
			{
				page="AddVpnObjectView.jsp?x="+x+"&y="+y+"&parent_id="+arrPObjectID+"&user_id="+topouser_id[topouser_id.length-1];
				newWin(page,otherpra,400,340);
				break;
			}
		}


			
	}
}

function createLink(id,from,to,pid)
{
	
	var tempLink=new Array(arrLink.length+1);
	//tempLink=arrLink;
	for(var i=0;i<arrLink.length;i++)
	{
		tempLink[i]=arrLink[i];
	}

	tempLink[arrLink.length]=new LinkGraph();
	tempLink[arrLink.length].init(id,
			 "",
			 from,
			 to,
			 "2",
			 "green",
			 pid);
	
	arrObjIdAndIndx[id] = arrLink.length ;
	tempLink[arrLink.length].draw();

	arrLink = tempLink;

	clearDevBorder();
	arrCurDrag = null;
	tempLink=null;
	//alert(id);
	//alert(arrLink.length);
}

function createNet(id,title,x,y,ip,icon,type,pid,state,iscpe)
{
	
	
	tmpx = parseInt(x,10) + coordOffset;
	tmpy = parseInt(y,10) + coordOffset;
	//判断如果如果是管理状态的，则赋于原先本来的图标，如果是不管理状态，则赋不管理状态的图标
	 tmp_icon=icon;
	 if(parseInt(state,10)==0)
	 {
		tmp_icon=modifyIconName(icon,0);	 	 
	 }
	//首先判断id是否存在
	if(typeof(arrObjIdAndIndx[id])=="undefined")
	{
		var tempDev=new Array(arrDev.length+1);
		for(var i=0;i<arrDev.length;i++)
		{
			tempDev[i]=arrDev[i];
		}
		
		tempDev[arrDev.length]=new NetGraph();

		tempDev[arrDev.length].init(id,title,tmpx,tmpy,ip,tmp_icon,type,pid,state,iscpe)
		tempDev[arrDev.length].draw();
		//存放所有对象ID字符串
		if(allObjIds == null) 
		 {	 
			allObjIds=id;
		 }

		 else 
		 {
			 allObjIds += ","+id;
		 }
		 //保存对象ID和arrDev索引对应关系
		 arrObjIdAndIndx[id] = arrDev.length;
		 arrDev=tempDev;
		 tempDev=null;
	}
	//如果对象已经存在
	else
	{
		arrDev[arrObjIdAndIndx[id]]._title=title;
		arrDev[arrObjIdAndIndx[id]]._x	  =tmpx;
		arrDev[arrObjIdAndIndx[id]]._y    =tmpy;
		arrDev[arrObjIdAndIndx[id]]._ip   =ip;
		arrDev[arrObjIdAndIndx[id]]._type =type;
		arrDev[arrObjIdAndIndx[id]]._pid  =pid;
		arrDev[arrObjIdAndIndx[id]]._state=state;
		arrDev[arrObjIdAndIndx[id]]._iscpe=iscpe;		
	}
}

function findRectInObj(){
	//获取选取框范围
	/**
	*最左上的距离
	* x:相当于鼠标第一次点下去的时候在界面的x坐标
	* Y:相当于鼠标第一次点下去的时候在界面的y坐标
	**/
	var x,y,e_x,e_y,x1,y1,x2,y2;
	
	
	x = selRect.style.posLeft;
	y = selRect.style.posTop;
	e_x=event.clientX - curDisX;
	e_y=event.clientY - curDisY;
	
	if((event.clientX-curDisX)<x){		
		x1 = event.clientX-curDisX;
		y1 = event.clientY-curDisY;
		x2 = x;
		y2 = y;
	}else{
		x1 = x;
		y1 = y;
		x2 = event.clientX-curDisX;
		y2 = event.clientY-curDisY;		
	}
	

	/*if(e_x<x){		
		x1 = e_x;		
		x2 = x;		
	}else{
		x1 = x;		
		x2 = e_x;			
	}

	if(e_y<y){			
		y1 = e_y;		
		y2 = y;
	}else{		
		y1 = y;		
		y2 = e_y;		
	}*/


	//初始化拖动对象列表
	if(arrCurDrag == null) arrCurDrag = new Array();	
	if(arrDev == null) arrDev = new Array(0);
	var d_x,d_y,k=0;
	for(var i=0;i<arrDev.length;i++){
		if(arrDev[i]._state == 0) continue;
		dx = arrDev[i]._x + arrImgSize[arrDev[i]._icon][0]/2;
		dy = arrDev[i]._y + arrImgSize[arrDev[i]._icon][1]/2;
		//判断网元是否在选取框内
		if(dx>x1 && dx<x2 && dy>y1 && dy<y2){
			//查找网元相关链路列表
			findLinkObjByID(arrDev[i]._id);
			arrDev[i].drawBorder();
			//加入被拖动选择列表
			arrCurDrag[k] = new DragObj(i,curDragLinkFrom,curDragLinkTo);
			k++;
		}
	}
}

//构造被移动的对象字符串
function getMovedObjs(){
	if(arrDev == null) return "";
	var s = null;
	var pid;
	var tmpx,temy;

	for(var i=0;i<arrDev.length;i++){
		if(eval(arrDev[i]._transform==1)){
			tmpx = parseInt(arrDev[i]._x,10) - coordOffset;
			tmpy = parseInt(arrDev[i]._y,10) - coordOffset;
			tmpx = tmpx - iMinX;
			tmpy = tmpy - iMinY;
			//if(tmpx<0) tmpx=0;
			//if(tmpy<0) tmpy=0;
			if(s==null){
				s = arrDev[i]._id+","+tmpx+","+tmpy;
				pid = arrDev[i]._pid;
			}
			else
				s += "@"+arrDev[i]._id+","+tmpx+","+tmpy;
		}
	}
	if(s!=null){
		//VPN视图追加当前层所属的user_id编号
		if(ViewID==5)
			s = ViewID+";"+pid+";"+topouser_id[topouser_id.length-1]+";"+s;
		else
			s = ViewID+";"+pid+";"+s;
	}
	//alert("getMovedObjs in "+s);
	return s;
}

/*
根据传入的对象id和要改变的对象的状态将对象进行处理
* @param objID 中间以","隔开
* @param type 0:代表将设备有管理转成不管理;1: 代表将设备有不管理转成管理
*/
function ManagerDevice(objID,type)
{
	var objs=objID.split(",");
	var obj;
	for(var i=0;i<objs.length;i++)
	{
		//针对设备的处理
		obj=arrDev[findDevObjByID(objs[i])];		
		
		if(parseInt(type,10)==0)
		{
			obj._state=0;
			document.all(obj._id).src="images/"+ modifyIconName(modifyIconName(obj._icon,1),0);		
		}
		else
		{
			obj._state=1;
			document.all(obj._id).src="images/"+ modifyIconName(obj._icon,1);
		}
		obj.view();
		
		//针对该设备连接的链路的处理
		findLinkObjByID(obj._id);
		for(var j=0;j<curDragLinkFrom.length;j++)
		{
			arrLink[curDragLinkFrom[j]].view();		
		}
		
		for(var j=0;j<curDragLinkTo.length;j++)
		{
			arrLink[curDragLinkTo[j]].view();		
		}
		
	
	}

	clearDevBorder();
	arrCurDrag = null;
}

/**
 * 根据输入网元两者之间连线对象ID，获得连线对象
 * 根据对象属性调整对象参数（strokecolor/颜色）
 */
function changeLinkLineColor(line_id,strokecolor){
	//var o = document.all(line_id);
	
	//根据输入参数不同决定是否进行处理
	if(strokecolor != null){
		//o.strokecolor.value = strokecolor;
		arrLink[line_id]._color = strokecolor;
	}
	
	//o = null;
}

/**
 * 根据输入网元两者之间连线对象ID，获得连线对象
 * 根据对象属性调整对象参数（strokeweight/粗细）
 */
function changeLinkLineWeight(line_id,strokeweight){
	//var o = document.all(line_id);
	//alert(strokeweight);
	//根据输入参数不同决定是否进行处理
	if(strokeweight != null){
		//o.strokeweight = strokeweight;
		arrLink[line_id]._weight = strokeweight +"px";
	}
	
	//o = null;
}

/**
 * 根据输入网元两者之间连线对象ID，获得连线对象
 * 根据对象属性调整对象参数（strokecolor/颜色；strokeweight/粗细）
 */
function changeLinkLineParam(line_id,strokecolor,strokeweight){
	changeLinkLineColor(line_id,strokecolor);
	changeLinkLineWeight(line_id,strokeweight);
}

/**
 * 根据输入网元两者之间连线对象ID，获得连线对象
 * 提升告警等级为红色
 */
function setLinkLineRed(line_id){
	changeLinkLineColor(line_id,"red");
}

function findWTPos(){
	l = oWT.offsetLeft;
	t = oWT.offsetTop;
	po = oWT.offsetParent;
	while(po.tagName != "BODY"){
		l += po.offsetLeft;
		t += po.offsetTop;

		po = po.offsetParent;
	}

	curDisX = l-(document.body.scrollLeft + oWT.scrollLeft);
	curDisY = t-(document.body.scrollTop + oWT.scrollTop);
}


/**
 * 获取当前topo图中的所有设备id
 */
function getAllDeviceID(){

	var idList = '';
	for (var i=0;i<arrDev.length ;i++ )
	{
		if (idList != '')
		{
			idList = idList + ',' + arrDev[i]._id;
		}
		else{
			idList = arrDev[i]._id;
		}
	}
	return idList;
}

/**
 * 根据传入的设备信息更新当前topo图中的设备描述
 */
function setAllDeviceID(deviceStr){
	var deviceArr = deviceStr.split(",");

	for (var i=0;i<deviceArr.length ;i++ )
	{
		var deviceInfo = deviceArr[i].split("|||");
		for (var j=0;j<arrDev.length ;j++ ){
			if (deviceInfo[0] == arrDev[j]._id && arrDev[j]._id.indexOf("1/gw/") != -1)
			{
				arrDev[j].removeNode();
				arrDev[j]._title = deviceInfo[1];
				arrDev[j].draw();
			}
		}
		
	}

}

