//------------------------------------------------------------
// Copyright (c) 2004-2005 linkage. All rights reserved.
// Version 1.0.03
// Ahthor Haiteng.Yu
//------------------------------------------------------------
//==============================ͨ�ú���=========================
var deviceNumber = 0x3116;

//����Error�����toString����
Error.prototype.toString = function() {
	return "Error / Code: 0x"
		 + this.number.toString(16)
		 + "\nMessage: "
		 + this.description;
};
Error.prototype.printStackTrace = function() {print(this);}

//�����쳣����
function _throw(ex, dspt) {
	if (ex instanceof Error)
		print(ex);
	if (typeof(ex) == "number") {
		if (ex < 0xFFFF) ex = deviceNumber * 0x10000 + ex;
		ex = new Error(ex, dspt);
	}

	print(ex);
};

//��ӡ����
function print(_obj){
	//alert(_obj);
};
//================================================================
//==============================ϵͳ�����ͱ���======================
var DEBUG = true;
var NETWORK_DEVICE_LAYER = 4;	//��ʾ�豸���
var NETWORK_LINK_LAYER = 3;		//��ʾ��·���

//�ж��Ƿ��ͻ�ϵͳ������ǵĻ�������һЩ���⴦��
var ISVIP=false;

var idStr = "idWebTopo";
var oWT = null;
var xmlSrc = "";

var arrDev	= null;						//�豸����
var arrLink	= null;						//��·����
var IsLoadXML = false;					//�Ƿ��Ѿ�����XML����
var IsDevDrag=false;						//�Ƿ�����Ԫ�϶�
var IsDrag=false;							//�Ƿ����϶�

var x=y=0;									//��ǰ�������
var arrCurDrag = null;					//��ǰ���϶���Ԫ��������

var arrCurDragLink=null;               //��ǰ���ж�����·��������
var curDragLinkObj=null;			   //��ǰ���϶�����·

//var rx=ry=0;						//��ǰ���϶���Ԫ���ĵ�
var curDragObj = null;				//��ǰ���϶���Ԫ
var curDragLinkFrom = null;			//��ǰ���϶���Ԫ�����·����FROM ������Ԫ��
var curDragLinkTo = null;				//��ǰ���϶���Ԫ�����·����TO ������Ԫ��

var arrImgSize = null;		//������Դ���ʹ����ICON��С
var arrObjIdAndIndx = null;	//��Ŷ���ID��arrDev������Ӧ��ϵ
var arrWarnClr = new Array(6);		//�澯��ɫ
var levelWarn = 3;						//Topo����ʾ�澯��͵ȼ�
var ViewID	 = null;						//��ǰ��ͼID  1:������ͼ 2:������ͼ 5:vpn��ͼ
var arrPObjectID = null;					//��ǰ��Ԫ������ID
var allObjIds = null;
var iMinX = 0;
var iMinY = 0;

var coordOffset = 15;
var curDisX=curDisY=0;

//�ж��Ƿ��ڻ���·��ģʽ
var isCreateLink=false;

//�ж��Ƿ������������ģʽ
var isCreateNet=false;

//����ķ���ճ��������Ķ���ID
var curPasteObj=null;

//�жϵ�ǰҳ�Ƿ�Ҫ��ʾ�����豸�ı�ʶ
var isDisplayAll="false";		//���Ϊtrue�Ļ�������ʾ���е��豸��false�Ļ�ֻ��ʾ����״̬Ϊ1���豸����·

var Link_managed_Color="green";
var Link_unmanaged_Color="glory";

var Link_managed_Weight = "1";

//��¼�澯���������ʱ��
var updateTime="0";

//��ǰ��������������û�id
var topouser_id = [-1];

//0:������ ��ɫ 1:���� ��ɫ 2:��ͣ ��ɫ 3:ͣ�� ��ɫ
var STATUS_MAP = [];
STATUS_MAP[0] = "_blue";
STATUS_MAP[1] = "";
STATUS_MAP[2] = "_yellow";
STATUS_MAP[3] = "_glory";

//��ʼ���ɱ����
function initTopo(){
	oWT = document.all(idStr);
	this.xmlSrc = oWT.src;
	//ע��by smf
	//arrImgSize = new Object();
	arrObjIdAndIndx = new Object();
	//��ʼ�����ڳߴ�
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
	arrDev	= null;						//�豸����
	arrLink	= null;						//��·����
	IsLoadXML = false;					//�Ƿ��Ѿ�����XML����
	IsDevDrag=false;						//�Ƿ�����Ԫ�϶�
	IsDrag=false;							//�Ƿ����϶�

	x=y=0;									//��ǰ�������
	arrCurDrag = null;						//��ǰ���϶���Ԫ��������

	arrCurDragLink=null;               //��ǰ���ж�����·��������
	curDragLinkObj=null;			   //��ǰ���϶�����·

	curDragObj = null;					//��ǰ���϶���Ԫ
	curDragLinkFrom = null;				//��ǰ���϶���Ԫ�����·����FROM ������Ԫ��
	curDragLinkTo = null;				//��ǰ���϶���Ԫ�����·����TO ������Ԫ��

	ViewID	 = null;						//��ǰ��ͼID
	arrPObjectID = null;	

	//curDisX=curDisY=0;	
	//arrImgSize = null;
	arrObjIdAndIndx = null;

	
	

	allObjIds = null;
}

//������ʾtopo
function viewTopo(){
	//���豸
	for(var i=0;i<arrDev.length;i++){
		arrDev[i].view();
	}
	//����·
	for(var i=0;i<arrLink.length;i++){
		arrLink[i].view();
	}


}


//��Topo
function drawTopo(){
	if(!IsLoadXML){
		initTopo();
	}

	//alert(arrDev.length);
	//���豸
	if(arrDev!=null)
	{
		for(var i=0;i<arrDev.length;i++){
			arrDev[i].draw();
		}
	}
	//����·
	if(arrLink!=null)
	{
		for(var i=0;i<arrLink.length;i++){
			arrLink[i].draw();
		}
	}
}

//managed:1 ��ԭ����δ����ĸĳɹ���״̬��0:��ԭ���ǹ���״̬�ĸ�Ϊδ�����
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

//����Topo����
function loadData(){
	if(xmlSrc == ""){
		_throw(0x0002,"Web ����û�г�ʼ�����Ҳ�������Դ");
		return;
	}
	//��ȡTopo XML����
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
	//��ʼ���豸����-------------------------------
	

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
				 //����һ���豸����
				 arrDev[i] = new NetGraph();
				 //��ʼ���豸��������
				 tmpx = parseInt(node.getAttribute("x"),10) + coordOffset;
				 tmpy = parseInt(node.getAttribute("y"),10) + coordOffset;

				 //�ж��������ǹ���״̬�ģ�����ԭ�ȱ�����ͼ�꣬����ǲ�����״̬���򸳲�����״̬��ͼ��
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
				
				 //������ж���ID�ַ���
				 if(allObjIds == null) allObjIds=node.getAttribute("id");
				 else allObjIds += ","+node.getAttribute("id");
				 //�������ID��arrDev������Ӧ��ϵ
				 arrObjIdAndIndx[node.getAttribute("id")] = i;
				 
			}

			//��ʼ����·����---------------------------------
			nodes = XMLDoc.selectNodes("//Nodes/Link");
			ln = nodes.length;
			arrLink = new Array(ln);
			for(var i=0;i<ln;i++){
				 node = nodes.item(i);
				 //����һ����·����
				 arrLink[i] = new LinkGraph();
				 //��ʼ����·��������
				 arrLink[i].init(node.getAttribute("id"),
					 node.getAttribute("title"),
					 node.getAttribute("from"),
					 node.getAttribute("to"),
					 node.getAttribute("weight"),
					 node.getAttribute("color"),
					 pid);
				 arrObjIdAndIndx[node.getAttribute("id")] = i ;
			}
			//��ȡ��СXY����
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
				 //����һ���豸����
				 arrDev[i] = new NetGraph();
				 //��ʼ���豸��������
				 tmpx = parseInt(node.getAttribute("x"),10) + coordOffset;
				 tmpy = parseInt(node.getAttribute("y"),10) + coordOffset;
				 
				 //�ж��������ǹ���״̬�ģ�����ԭ�ȱ�����ͼ�꣬����ǲ�����״̬���򸳲�����״̬��ͼ��
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
				
				 //������ж���ID�ַ���
				 if(allObjIds == null) allObjIds=node.getAttribute("id");
				 else allObjIds += ","+node.getAttribute("id");
				 //�������ID��arrDev������Ӧ��ϵ
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
				 //����һ���豸����
				 arrDev[i] = new NetGraph();
				 //��ʼ���豸��������
				 tmpx = parseInt(node.getAttribute("x"),10) + coordOffset;
				 tmpy = parseInt(node.getAttribute("y"),10) + coordOffset;

				 //�ж��������ǹ���״̬�ģ�����ԭ�ȱ�����ͼ�꣬����ǲ�����״̬���򸳲�����״̬��ͼ��
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
				
				 //������ж���ID�ַ���
				 if(allObjIds == null) allObjIds=node.getAttribute("id");
				 else allObjIds += ","+node.getAttribute("id");
				 //�������ID��arrDev������Ӧ��ϵ
				 arrObjIdAndIndx[node.getAttribute("id")] = i;
				 
			}

			//��ʼ����·����---------------------------------
			nodes = XMLDoc.selectNodes("//Nodes/Link");
			ln = nodes.length;
			arrLink = new Array(ln);
			for(var i=0;i<ln;i++){
				 node = nodes.item(i);
				 //����һ����·����
				 arrLink[i] = new LinkGraph();
				 //��ʼ����·��������
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
			//��¼�µ�ǰ������ͼ��
			var temp_pid = pid.split("/");
			//������ڵ�Ϊ�û����� (�����ʽΪ5/u/int��) ���¼���û�����
			if(temp_pid[1]=="u"){
				//׷���û�����б�
				if(topouser_id[topouser_id.length-1]!=pid)
					topouser_id[topouser_id.length] =pid;
//				for(var x in topouser_id){
//					alert("��ǰ����ͼ����������û����Ϊ�� "+topouser_id[x]);
//				}
				//alert("��ǰ����ͼ����������û����Ϊ�� "+topouser_id[topouser_id.length-1]);
			}
			
			nodes = XMLDoc.selectNodes("//Nodes/Device");
			ln = nodes.length;
			arrDev = new Array(ln);

			var tmp_icon;
			for(var i=0;i<ln;i++){
				 node = nodes.item(i);
				 //����һ���豸����
				 arrDev[i] = new NetGraph();
				 //��ʼ���豸��������
				 tmpx = parseInt(node.getAttribute("x"),10) + coordOffset;
				 tmpy = parseInt(node.getAttribute("y"),10) + coordOffset;

				 //�ж��������ǹ���״̬�ģ�����ԭ�ȱ�����ͼ�꣬����ǲ�����״̬���򸳲�����״̬��ͼ��
				 tmp_icon=node.getAttribute("icon");
				 if(parseInt(node.getAttribute("state"),10)==0)
				 {
					tmp_icon=modifyIconName(node.getAttribute("icon"),0);
				 }
				 
				 //��type���Ͳ�Ϊ1���豸������ipΪ��ԪID
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
				
				 //������ж���ID�ַ���
				 if(allObjIds == null) allObjIds=node.getAttribute("id");
				 else allObjIds += ","+node.getAttribute("id");
				 //�������ID��arrDev������Ӧ��ϵ
				 arrObjIdAndIndx[node.getAttribute("id")] = i;
				 
			}

			//��ʼ����·����---------------------------------
			nodes = XMLDoc.selectNodes("//Nodes/Link");
			ln = nodes.length;
			arrLink = new Array(ln);
			for(var i=0;i<ln;i++){
				 node = nodes.item(i);
				 //����һ����·����
				 arrLink[i] = new LinkGraph();
				 //��ʼ����·��������
				 arrLink[i].init(node.getAttribute("id"),
					 node.getAttribute("title"),
					 node.getAttribute("from"),
					 node.getAttribute("to"),
					 "null",//������·��weigth��color����
					 node.getAttribute("color"),
					 pid);
				 arrObjIdAndIndx[node.getAttribute("id")] = i ;
			}
			break;			
			
		}
	}

	//�ͷű���
	ln = null;
	node = null;
	nodes = null;
	IsLoadXML = true;
}

//������ԪID������Ԫ��������
function findDevObjByID(id){
	/*for(var i=0;i<arrDev.length;i++){
		if(arrDev[i]._id == id) return i;
	}*/

	if(typeof(arrObjIdAndIndx[id]) != "undefined"){
		return arrObjIdAndIndx[id];
	}

	return -1;
}

//������ԪID���������·��������
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
	this._curDragObjID=curDragObjID;//��ǰ���϶���·����
}

function DragObj(curDragObjID,curDragLinkFrom,curDragLinkTo){
	this._curDragObjID = curDragObjID;					//��ǰ���϶���Ԫ����
	this._curDragLinkFrom = curDragLinkFrom;			//��ǰ���϶���Ԫ�����·����������FROM ������Ԫ��
	this._curDragLinkTo = curDragLinkTo;				//��ǰ���϶���Ԫ�����·����������TO ������Ԫ��
}



//�����ַ������ȣ�����Ϊ12px��
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

//����Topo Panel��ҳ���ϵ�λ��
function setTopoDisplacement(x,y){
	curDisX = x;
	curDisY = y;
}



//��ȡͼ���С����������arrImgSize������
function getIconSize(icon){
	//��ȡԭʼͼƬ������
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
//==========================������Ԫ������=========================
function Graph(){
	this._id		= null;			//��ԪID
	this._pid		= null;
	this._title	= null;			//��Ԫ����
	this._transform = 0;			//�Ƿ񱻸ı�����
};
//��ȡGraph���һ������
var _p = Graph.prototype;

//���Ի�����
_p.init = function(){
	_throw(0x0001,"�����಻�ܹ�ʵ������������");
};

//��ͼ��
_p.draw = function(){
	_throw(0x0001,"�����಻�ܹ�ʵ������������");
};

//ɾ��ͼ��
_p.remove = function(){
	_throw(0x0001,"�����಻�ܹ�ʵ������������");
};

//���øı�״̬
_p.setTransform = function(transform){
	this._transform = transform;
};

//����Object��toString����
_p.toString = function(){
	return "oGraph";
};
//================================================================

//=============================�豸����============================
function NetGraph(){
	Graph.call(this);
	this._x		= 0;		//X������(left)
	this._y		= 0;		//Y������(top)
	this._ip		= null;	//IP��ַ
	this._w		= 0;		//ͼ��Ŀ�
	this._h		= 0;		//ͼ��ĸ�
	this._icon_ch   = null;//ԭʼͼ������
	this._icon	= null;	//ͼ��ͼ��
	this._type	= -1;		//�豸�ͺ� 0Ϊ����
	this._state =0;         //�豸�Ƿ����
	this._iscpe =0;         //�Ƿ�cpe�豸,0Ϊ��cpe�豸��1Ϊcpe�豸

	this._isconfirm =0;         //�Ƿ�Ϊȷ���豸,0�ǣ�1��
	this._isConfirmSeg =0;         //�Ƿ�Ϊ��ȷ���豸����,0�ǣ�unkdev��

};

//��ȡNetGraph���һ������
var _p = NetGraph.prototype = new Graph;

//ʵ��Graph���init����
_p.init = function(id,title,x,y,ip,icon,type,pid,state,iscpe,isconfirm,isConfirmSeg){
	this._id	= id;
	this._title = title;
	this._x		= eval(x)*1;
	this._y		= eval(y)*1;
	this._ip		= ip;
	this._icon_ch = this._icon	= icon;
	this._type	= eval(type)*1;
	this._pid		= pid;
	//������ʾ�澯�ļ���
	this._level       = -1;
	this._state=eval(state)*1;
	this._iscpe=eval(iscpe)*1;

	this._isconfirm = isconfirm;
	this._isConfirmSeg = isConfirmSeg; 
};

//ʵ��Graph���draw����
_p.draw = function(){
	//���������ʾ���е��豸�������豸��״̬�ǲ������豸��״̬����ֱ�ӷ���
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

	//ע������˫���¼����豸��˫���¼�
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

//ʵ��Graph���remove����
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

//ʵ��Graph���removeNode����
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

//�滭������߿�
_p.drawBorder = function(){
	o = document.all("B_"+this._id);
	if(o != null) return;
	w	= arrImgSize[this._icon][0];
	h	= arrImgSize[this._icon][1];

	s = "<v:rect id=\"B_"+ this._id+"\" style=\"position:absolute;top:"+ (this._y-1)+";left:"+(this._x-1)+";width:"+ (w+3) +";height:"+ (h+3) +";z-index:2\" strokecolor = \"blue\"  coordsize = \"21600,21600\" strokeweight=\"3px\"  ><v:fill opacity = \"0\" ></v:fill></v:rect>";	
	oWT.insertAdjacentHTML("BeforeEnd",s);
}

//ɾ��������߿�
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

//������������
_p.setXY = function(x,y){
	this._x = x;
	this._y = y;
};

//�����ƶ�
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
//�����Ƿ���ʾ�ķ���
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
//��ҵ������Ҫ��ȡMC�����л�ȡ״ֵ̬�����޸���ԪͼƬ����
//0:������ ��ɫ 1:���� ��ɫ 2:��ͣ ��ɫ 3:ͣ�� ��ɫ
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
//�ָ������豸��״ֵ̬
_p.resetStatus=function(){
	document.all(this._id).src="images/" + this._icon;
	this.view();
};

//���澯����
_p.drawWarn = function(num,level){
	//���level�ĸ澯����С���豸��ǰ�ĸ澯��������Ҫ���澯
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
//ɾ���澯����
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
//����Object��toString����
_p.toString = function(){
	return "oDevGraph";
};
//=========================��·����====================================
function LinkGraph(){
	Graph.call(this);
	this._form	= null;	//��·������ID
	this._to		= null;	//��·�յ����ID
	this._weight	= null;	//��·��ϸ
	this._color		= null;	//��·��ɫ
};

//��ȡLinkGraph���һ������
var _p = LinkGraph.prototype = new Graph;

//ʵ��Graph���init����
_p.init = function(id,title,from,to,weight,color,pid){
	this._id	= id;
	this._title	= title;
	this._from	= from;
	this._to	= to;
	this._weight= weight;
	this._color	= color;
	this._pid = pid;
	
};

//ʵ��Graph���draw����
_p.draw = function(){

	_from_obj	= arrDev[findDevObjByID(this._from)];
	if(_from_obj == null){
		_throw(0x0004,"ϵͳ�޷��ҵ�ID��Ϊ��"+ this._from +" ����Ԫ");
		return;
	}
	
	_to_obj		= arrDev[findDevObjByID(this._to)];
	if(_to_obj == null){
		_throw(0x0004,"ϵͳ�޷��ҵ�ID��Ϊ��"+ this._to +" ����Ԫ");
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

//ʵ��Graph���remove����
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

//�����ƶ�
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

//ʵ���Ƿ���ʾ��·
_p.view=function()
{
	try{
		o = document.all(this._id);
		_from_obj	= arrDev[findDevObjByID(this._from)];
		if(_from_obj == null){
			_throw(0x0004,"ϵͳ�޷��ҵ�ID��Ϊ��"+ this._from +" ����Ԫ");
			return;
		}
		
		_to_obj		= arrDev[findDevObjByID(this._to)];
		if(_to_obj == null){
			_throw(0x0004,"ϵͳ�޷��ҵ�ID��Ϊ��"+ this._to +" ����Ԫ");
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
			_throw(0x0004,"ϵͳ�޷��ҵ�ID��Ϊ��"+ this._from +" ����Ԫ");
			return;
		}
		
		_to_obj		= arrDev[findDevObjByID(this._to)];
		if(_to_obj == null){
			_throw(0x0004,"ϵͳ�޷��ҵ�ID��Ϊ��"+ this._to +" ����Ԫ");
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




//����Object��toString����
_p.toString = function(){
	return "oLinkGraph";
};
//======================================================================
//===================================�¼�����============================
//������Ԫ�Ƿ����Ϸ��б���
function findDragObjListByID(id){
	if(arrCurDrag == null) return false;
	for(var i=0;i<arrCurDrag.length;i++){
		if(arrCurDrag[i]._curDragObjID == id)
			return true;
	}
	return false;
}

//������·�Ƿ����Ϸ��б���
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
	//��ȡ��ǰ�¼���������
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
	//dbl_type ��˫�������typeֵ
	
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
			//dbl_type == 1 ������ͼ��  dbl_type == 0 ������ͼ��
			if(dbl_type == 1 || dbl_type == 0)
			{
				CMS_Click(null,1,"getChildTopo.jsp","childFrm");
			}
			else
			{
				if(arrDev[arrObjectID] != null){
					//�����豸 1/dev/  �����豸 2/
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
			//alert("��˫���������ͱ��Ϊ��"+dbl_type);
			//0:����,2:�û���,3:�û�,5:�������˽ڵ�,6 VPN��ͼ
			if(dbl_type==0||dbl_type==2||dbl_type==3||dbl_type==5||dbl_type==6)
			{
				CMS_Click(null,1,"getChildTopo.jsp?topoType=5","childFrm");
			}
			//1 �豸
			else if(dbl_type==1)
			{
				CMS_Click(null,1,"showDevDetail","javascript");
			}
			//�����
			else if(dbl_type==4)
			{
				//CMS_Click(null,1,"showDevDetail","javascript");
			}
			break;
		}
	}
}

//��ԪDown�¼�
function Dev_MouseDown(){
	//�����������·�������������ʱ�򷵻�
	if(isCreateNet)
	{
		return;
	}
	//��ȡ��ǰ�¼���������
	curDragObj = event.srcElement;
	if(event.srcElement.tagName != "image")  return;
	//����ǰ��ԪID���˵�ϵͳ,arrObjectID ����coolmenu.js�����
	//arrObjectID = curDragObj.id;
	//alert(curDragObj.id);
	oIndex = findDevObjByID(curDragObj.id);
	arrObjectID = oIndex;
	arrPObjectID = arrDev[oIndex]._pid;

	//û�а�ctrl�����Ҳ����Ϸ��б���ʱ������豸ֱ��ѡ��
	if(!event.ctrlKey && !findDragObjListByID(oIndex)){
		//������Ԫ�����·�б�
		findLinkObjByID(curDragObj.id);
		
		clearLinkBorder();
		arrCurDragLink = null;
		curDragLinkObj=null;

		//ɾ����ǰ��ѡ�еĶ���߿�
		clearDevBorder();
		//�ӱ߿�
		arrDev[oIndex].drawBorder();
		//��ʼ���϶������б�

		arrCurDrag = null;
		arrCurDrag = new Array();
		arrCurDrag[0] = new DragObj(oIndex,curDragLinkFrom,curDragLinkTo);
	}
	//����豸û�б�ѡ�У�����ctrl����������ѡ���б�������
	else if(!findDragObjListByID(oIndex) && event.ctrlKey){
		//�ӱ߿�
		arrDev[oIndex].drawBorder();
		//����ѡ���б�������
		if (arrCurDrag == null){
			arrCurDrag = new Array();
			arrCurDrag[0] = new DragObj(oIndex,curDragLinkFrom,curDragLinkTo);
		}
		else{
			arrCurDrag[arrCurDrag.length] = new DragObj(oIndex,curDragLinkFrom,curDragLinkTo);
		}
	}
	//����豸�ѱ�ѡ�У�����ctrl����������ѡ���б���ɾ��
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

//��Ԫ�϶��¼�������
function runLink(){

	//�ж��������Ƿ���,����ҽ�����ʾ�ҽ��˵�	
	if(event.button!=1){
		if(arrCurDragLink.length == 1){
			//alert(arrCurDragLink[0]._curDragObjID);
			//��ȡ��Ԫ����
			o = arrLink[arrCurDragLink[0]._curDragObjID];
			//alert("run �����д���"+o._type)
			//��ʼ���ҽ��˵�
			//if(RM_Menu.length==0) 
			createRMenu("",o._pid);
			//��ʾ�ҽ��˵�
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
			_throw(0x0010,"Ŀǰ��֧�ֶ���豸ѡ�е��ҽ��˵�");
		}
		return;
	}
	
	IsDevDrag=false;

	x=event.clientX;
	y=event.clientY;
	
	
	//����TOPO�������ƶ��¼������UP�¼�
	//oWT.onmousemove=Dev_MouseMove;
	//oWT.onmouseup = Dev_MouseUp;
}


//��Ԫ�϶��¼�������
function run(){
	//�ж��������Ƿ���,����ҽ�����ʾ�ҽ��˵�	
	if(event.button!=1){
		if(arrCurDrag.length == 1){
			//alert(arrCurDrag[0]);
			//��ȡ��Ԫ����
			o = arrDev[arrCurDrag[0]._curDragObjID];
			//alert("run �����д���"+o._type)
			//��ʼ���ҽ��˵�
			//if(RM_Menu.length==0) 
			createRMenu(o._type,o._pid);
			//��ʾ�ҽ��˵�
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
			_throw(0x0010,"Ŀǰ��֧�ֶ���豸ѡ�е��ҽ��˵�");
		}
		return;
	}
	
	IsDevDrag=true;

	x=event.clientX;
	y=event.clientY;
	
	
	//����TOPO�������ƶ��¼������UP�¼�
	oWT.onmousemove=Dev_MouseMove;
	oWT.onmouseup = Dev_MouseUp;
}

//��Ԫѡ�д�����TOPO�������ƶ��¼�
function Dev_MouseMove(){
	//alert("IsDevDrag is"+IsDevDrag);
	//ִ������
	var rx=ry=0;
	
    if(event.button==1 && IsDevDrag && !isCreateLink && !isCreateNet){
		for(var i=0;i<arrCurDrag.length;i++){
			//��ȡ��Ԫ����
			o = arrDev[arrCurDrag[i]._curDragObjID];
			o.setTransform(1);
			po = document.all(o._id);
			rx = po.style.posLeft + po.style.posWidth/2;
			ry = po.style.posTop + po.style.posHeight/2;
			//����������Ԫ����
			o.setXY(o._x+event.clientX-x,o._y+event.clientY-y);
			//��Ԫ�ƶ�
			o.move();
			//��Ԫ�����·�ƶ�
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
		//�����ʱ����
		o = null;
		of = null;
		ot = null;
		//Ϊ�´��ƶ���¼��ǰ�������
		x=event.clientX;
		y=event.clientY;
    }
    return false;	
}

//��ԪUP�¼�
function Dev_MouseUp(){
	
	//û�а�ctrl�������Ϸ��б���ʱ������豸ֱ��ѡ��
	if(!event.ctrlKey && findDragObjListByID(oIndex)){
		//������Ԫ�����·�б�
		findLinkObjByID(curDragObj.id);
		
		clearLinkBorder();
		arrCurDragLink = null;
		curDragLinkObj=null;

		//ɾ����ǰ��ѡ�еĶ���߿�
		clearDevBorder();
		//�ӱ߿�
		arrDev[oIndex].drawBorder();
		//��ʼ���϶������б�

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

//Topo��Down�¼�
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
	
	//�ж�����Ƿ�Ϊ���
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
	//ɾ����ǰ��ѡ�еĶ���߿�
	clearDevBorder();

	clearLinkBorder();

	
	x=event.clientX - curDisX;
	y=event.clientY - curDisY;
	//alert(x);
	//alert(y)
	//alert(oWT.offsetWidth);
	//alert(oWT.offsetHeight);

	//offsetWidth:������ǲ�Ŀ�
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

//TOPO�������ƶ��¼�
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

//TOPO���Up�¼�
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
		//�������ѡ�������ͬ���豸
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
			//VPN��ͼ
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
		//alert("��ǰtopoͼarrPObjectID��"+arrPObjectID);
		if(arrPObjectID=="null"||(arrPObjectID.indexOf("unkdev")!=-1))
		{
			//alert(arrPObjectID);
			alert("�Բ����㲻���ڵ�ǰ�㽨����");
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
	//�ж��������ǹ���״̬�ģ�����ԭ�ȱ�����ͼ�꣬����ǲ�����״̬���򸳲�����״̬��ͼ��
	 tmp_icon=icon;
	 if(parseInt(state,10)==0)
	 {
		tmp_icon=modifyIconName(icon,0);	 	 
	 }
	//�����ж�id�Ƿ����
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
		//������ж���ID�ַ���
		if(allObjIds == null) 
		 {	 
			allObjIds=id;
		 }

		 else 
		 {
			 allObjIds += ","+id;
		 }
		 //�������ID��arrDev������Ӧ��ϵ
		 arrObjIdAndIndx[id] = arrDev.length;
		 arrDev=tempDev;
		 tempDev=null;
	}
	//��������Ѿ�����
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
	//��ȡѡȡ��Χ
	/**
	*�����ϵľ���
	* x:�൱������һ�ε���ȥ��ʱ���ڽ����x����
	* Y:�൱������һ�ε���ȥ��ʱ���ڽ����y����
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


	//��ʼ���϶������б�
	if(arrCurDrag == null) arrCurDrag = new Array();	
	if(arrDev == null) arrDev = new Array(0);
	var d_x,d_y,k=0;
	for(var i=0;i<arrDev.length;i++){
		if(arrDev[i]._state == 0) continue;
		dx = arrDev[i]._x + arrImgSize[arrDev[i]._icon][0]/2;
		dy = arrDev[i]._y + arrImgSize[arrDev[i]._icon][1]/2;
		//�ж���Ԫ�Ƿ���ѡȡ����
		if(dx>x1 && dx<x2 && dy>y1 && dy<y2){
			//������Ԫ�����·�б�
			findLinkObjByID(arrDev[i]._id);
			arrDev[i].drawBorder();
			//���뱻�϶�ѡ���б�
			arrCurDrag[k] = new DragObj(i,curDragLinkFrom,curDragLinkTo);
			k++;
		}
	}
}

//���챻�ƶ��Ķ����ַ���
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
		//VPN��ͼ׷�ӵ�ǰ��������user_id���
		if(ViewID==5)
			s = ViewID+";"+pid+";"+topouser_id[topouser_id.length-1]+";"+s;
		else
			s = ViewID+";"+pid+";"+s;
	}
	//alert("getMovedObjs in "+s);
	return s;
}

/*
���ݴ���Ķ���id��Ҫ�ı�Ķ����״̬��������д���
* @param objID �м���","����
* @param type 0:�����豸�й���ת�ɲ�����;1: �����豸�в�����ת�ɹ���
*/
function ManagerDevice(objID,type)
{
	var objs=objID.split(",");
	var obj;
	for(var i=0;i<objs.length;i++)
	{
		//����豸�Ĵ���
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
		
		//��Ը��豸���ӵ���·�Ĵ���
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
 * ����������Ԫ����֮�����߶���ID��������߶���
 * ���ݶ������Ե������������strokecolor/��ɫ��
 */
function changeLinkLineColor(line_id,strokecolor){
	//var o = document.all(line_id);
	
	//�������������ͬ�����Ƿ���д���
	if(strokecolor != null){
		//o.strokecolor.value = strokecolor;
		arrLink[line_id]._color = strokecolor;
	}
	
	//o = null;
}

/**
 * ����������Ԫ����֮�����߶���ID��������߶���
 * ���ݶ������Ե������������strokeweight/��ϸ��
 */
function changeLinkLineWeight(line_id,strokeweight){
	//var o = document.all(line_id);
	//alert(strokeweight);
	//�������������ͬ�����Ƿ���д���
	if(strokeweight != null){
		//o.strokeweight = strokeweight;
		arrLink[line_id]._weight = strokeweight +"px";
	}
	
	//o = null;
}

/**
 * ����������Ԫ����֮�����߶���ID��������߶���
 * ���ݶ������Ե������������strokecolor/��ɫ��strokeweight/��ϸ��
 */
function changeLinkLineParam(line_id,strokecolor,strokeweight){
	changeLinkLineColor(line_id,strokecolor);
	changeLinkLineWeight(line_id,strokeweight);
}

/**
 * ����������Ԫ����֮�����߶���ID��������߶���
 * �����澯�ȼ�Ϊ��ɫ
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
 * ��ȡ��ǰtopoͼ�е������豸id
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
 * ���ݴ�����豸��Ϣ���µ�ǰtopoͼ�е��豸����
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

