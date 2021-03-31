var head_h = 30;
var indicator = 8;
var left_w = 205;
var oInterval="";
var voInterval2="";
//告警声音文件名
var VoiceName="null";
//『loop：0 只播放一次 1 一直播放 相当于false和true』
var VoiceLoop="0";
//是否自动播放
var VoiceAuto = "true";
var isSendVoice = false;
//是否刷新告警信息,默认为false,即没有告警信息. Add by Hemc 2006-10-25
var warnFlag = true;
//告警rt_refresh.jsp/rt_refreshAll.jsp页面中JS赋值
var idRefreshAlarmNumMain = "";//参考方法本JS中refreshAlarmNumMain方法
var idRefreshAlarmNumLink = "";
var gatherIdStr="";             // gather_id纪录 告警信息中的 属地 信息，在 rightclick()方法中获得；
//Add by HEMC 2006-12-19 用于XMLHttp方法 见function webtopoSearchDevice
//js文件XMLHttp.js
var _xmlHttp = null;
function initialize(warn_num){
	//画拓扑图
	drawTopo();
	//设置初始化的一些环境
	reset();
	//初始化网络视图的快捷菜单
	InitNetRapidMenu();
	if(!ISVIP){
		//初始化告警的一些具体信息
		initLV(warn_num);
	}
	//初始化告警的相关信息
	initParamToSession();
}
//告警手动刷新 Add by Hemc
function RefreshByHand(){
	 //refreshAlarm();
	 refreshTopo();
}
//告警自动刷新 Add by Hemc
function setAutoRefresh(obj){
	var _checked = obj.checked;
	//设置标志位
	warnFlag = _checked;
	if(warnFlag){
		//并且设置刷新频率
		fnStartInterval();
	}else{
		fnStopInterval();
	}
}
function initImageSize(){
	document.all("childFrm").src = "getTopoImageSize.jsp?refresh="+(new Date()).getTime();
}

function initParamToSession(){
	//停止告警的相关处理循环
	//fnStopInterval();
	if(allObjIds==null) allObjIds="";
	//alert(allObjIds);
	document.initForm.curLayerObjList.value = allObjIds;
	//alert(document.initForm.curLayerObjList.value);
	document.initForm.action="initStaticParam.jsp?refresh="+(new Date()).getTime();
	document.initForm.submit();

	//refreshAlarm();
}

//设备关联链路数据显示
function ConfigLinkData(type,oIndex)
{
   ip=arrDev[oIndex]._ip;
   page = "web_deviceRelateLink.jsp?device_id="+ip;
   var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=1,toolbar=0,height=500,width=700,top=200,left=365";
   window.open(page,"relateLink",otherpra);
}

//显示实时配置信息
function showRTConfigInfo(type,oIndex)
{
	var tmp_id=arrDev[oIndex]._id.split("/");
	var tmp_title=arrDev[oIndex]._title;
	page = "vpn_realtime_config_info.jsp?device_id="+tmp_id[2]+"&refresh="+(new Date()).getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,800,600);
}

//设备登陆信息配置
function DeviceLoginConfig(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
	page = "vpn_config_name_and_pw.jsp?device_id="+ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,height=500,width=700,top=200,left=365";
	window.open(page,"设备登陆信息配置",otherpra);
}

//VPN工单配置
function ConfigVPNBill(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
	page = "vpn_bill_Config.jsp?device_id="+ip;

	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=1,toolbar=0,height=500,width=700,top=200,left=365";
	window.open(page,"VPN工单配置",otherpra);
}

//VPNPing配置
function ConfigVPNPing(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
	page = "web_pingtask.jsp?device_id="+ip;

	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=1,toolbar=0,height=768,width=1024,top=200,left=365";
	window.open(page,"VPNPing配置",otherpra);
}

function fnStartRefresh(){

	refreshAlarm();
	//如果已经设置了为手动刷新,则直接返回.
	if(warnFlag == false) return ;
	//refreshAlarm();
	//refreshVoice();
	//fnStartInterval();
}

function refreshVoice(){
	var page = "ManagerWarnVoice.jsp?name="+VoiceName+"&loop="+VoiceLoop+"&autostart="+VoiceAuto;
	//if(isSendVoice && VoiceName!="null"){
		//document.all("childFrm2").src = page;
	//}
}

function refreshAlarm(){
	document.all("childFrm").src = "rt_refresh.jsp?updateTime="+updateTime+"&isvip="+ISVIP+"&maxid="+ oList.getMaxId() +"&refresh="+(new Date()).getTime();
	document.all("childFrm2").src = "refreshDeviceStatus.jsp?tt=" + new Date().getTime();
	//getWarninit();
}
//配置告警声音
function ConfigWarnVoice(){
	var page = "config_warn_voice.jsp";
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,800,600);
}
//配置告警过滤规则
function ConfigWarnRule(){
	var page = "warnRuleDef.action";//"webtop_RuleListForm.jsp";
	window.open(page);
}

//IPBrowser发现
function IPBrowser()
{
   var page="IP_Browser.jsp?pid="+arrPObjectID;
   var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=no,status=0,toolbar=0";
   newWin(page,otherpra,700,400);
}

//查看所有告警信息
function ViewAllWarn(){
	var page = "RealTimeWarn.action";//"webtop_AllWarn.jsp";
	window.open(page);
}
//导出拓扑图-add by shenkejian 2007-2-7
function SaveImg(){
	var page="download_webtopo_img.jsp";
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,500,200);
}

//自动刷新topo
function fnStartInterval(){
	//if(oInterval==""){
	//	oInterval=window.setInterval("refreshAlarm()",60000);
	//}
	//else{
	//	fnStopInterval();
	//}

	//if(voInterval2=="")
	//{
	//	voInterval2 =window.setInterval("refreshVoice()",10000);
	//}

	if(oInterval==""){
		oInterval=window.setInterval("refreshTopo()",60000);
	}
	else{
		fnStopInterval();
	}
}

//停止刷新topo
function fnStopInterval(){
	if(oInterval!=""){
		window.clearInterval(oInterval);
		oInterval="";
	}
}

function reset(){
	var temp_w = (idLeftView.style.display == "")?left_w:0;

	if(ISVIP)
	{
		head_h=eval(document.all("overMenu").height);
		indicator =0;
		idWebTopo.style.height = document.body.clientHeight-(head_h+eval(oList.height)+indicator+25);
		//alert(document.body.clientWidth);
		idWebTopo.style.width = document.body.clientWidth-(indicator+temp_w+0.001);

	}
	else{
		idWebTopo.style.height = document.body.clientHeight-(head_h+eval(oList.height)+indicator);
		idWebTopo.style.width = document.body.clientWidth-(indicator+temp_w);
	}

	idNavigation.style.height = document.body.clientHeight-head_h;
	oList.width = document.body.clientWidth-(indicator+temp_w);
	oList.setSize();
	//alert(eval(oList.height));
	idLeftView.style.display = "none";
	//idBottomView.style.display = "none";
	setTopoDisplacement(indicator+temp_w,head_h);
	resizeView();
}

function resizeView(){
	idNavigation.style.height = document.body.clientHeight-head_h;
	//alert(idNavigation.style.height);
	var h = (idBottomView.style.display=="")?eval(oList.height):0;
	var w = (idLeftView.style.display == "")?left_w:0;
	idWebTopo.style.height = document.body.clientHeight-(head_h+h+indicator);
	//alert(idWebTopo.style.height);
	idWebTopo.style.width = document.body.clientWidth-(indicator+w);
	//alert(idWebTopo.style.width);
	oList.width = document.body.clientWidth-(indicator+w);
	oList.setSize();
	setTopoDisplacement(indicator+w,head_h);
}

function closeView(objid,type){
	document.all(objid).style.display="none";
	var o = event.srcElement;
	var s = o.src;

	if(s!=null && s!=""){
		o.src = "images/"+type+"_show.jpg";
		o.onmouseover = function (){
			this.src="images/"+type+"_show_over.jpg"
		}
		o.onmouseout = function (){
			this.src="images/"+type+"_show.jpg"
		}
		o.onclick=function anonymous(){
			showView(objid,type);
		}
	}

	resizeView();
}

function showView(objid,type){
	document.all(objid).style.display="";
	var o = event.srcElement;
	var s = o.src;

	if(s!=null && s!=""){
		o.src = "images/"+type+"_close.jpg";
		o.onmouseover = function (){
			this.src="images/"+type+"_close_over.jpg"
		}
		o.onmouseout = function (){
			this.src="images/"+type+"_close.jpg"
		}
		o.onclick = function anonymous(){
			closeView(objid,type);
		}
	}

	resizeView();
}

var userPremession="";

//验证用户权限
function chkPremession(oname){
	//return true;

	//alert(userPremession)
	if(oname=="") return true;
	var tmp = "#"+userPremession+"#"
	//alert(oname);
	if(tmp.indexOf("#"+oname+"#") == -1)
		return false;
	else
		return true;

}


//验证设备型号
function chkDeviceType(arrType,type,pid){
	if(arrType=="" && type!="-3" && pid!="null")
	{
		return true;
	}
	var tmp=arrType;
	//针对单个网元的 包括所有类型设备和所有类型网段
	if(tmp=="*" && (new String(type).length>0) && pid!="null" )
	{
		var tmp_id=new String(arrObjectID);
		if(tmp_id.indexOf(",")>0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	//针对多个网元对象
	else if(tmp=="***" && ((new String(type)).length==0 || parseInt(type,10)>=0 || parseInt(type,10)==-2 || parseInt(type,10)==-1) && pid!="null" )
	{
		return true;
	}
//	//针对网元管理菜单做特殊处理
//	else if(tmp.indexOf("netmanager")>=0 && (new String(type)).length>0)
//	{
//		if(arrObjectID==null)
//		{
//			return false;
//		}
//		var tmp_id=new String(arrObjectID);
//		//如果有很多个对象，则即显示管理设备，又显示不管理设备
//		if(tmp_id.indexOf(",")>0)
//		{
//			return true;
//		}
//		//如果只有一个对象，则首先判断该设备的状态来决定显示哪个菜单
//		else
//		{
//			o=arrDev[arrObjectID];
//			if(o!=null)
//			{
//				if(parseInt(o._state,10)==0 && tmp=="netmanager1")
//				{
//					o=null;
//					return true;
//				}
//				else if(parseInt(o._state,10)==0 && tmp=="netmanager2")
//				{
//					o=null;
//					return false;
//				}
//				else if(parseInt(o._state,10)==1 && tmp=="netmanager1")
//				{
//					o=null;
//					return false;
//				}
//				else if(parseInt(o._state,10)==1 && tmp=="netmanager2")
//				{
//					o=null;
//					return true;
//				}
//			}
//		}
//	}
	//单个设备菜单
	else if(tmp=="device" && (new String(type)).length>0 &&
		(parseInt(type,10)>0))
	{
		var tmp_id=new String(arrObjectID);
		if(tmp_id.indexOf(",")>0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	//针对剪切、粘贴、取消剪切做特殊处理
//	else if(tmp=="node" && (new String(type)).length>0 && parseInt(type,10)>1)
//	{
//		var tmp_id=new String(arrObjectID);
//		if(tmp_id.indexOf(",")>0)
//		{
//			return false;
//		}
//		else
//		{
//			return true;
//		}
//	}
	//针对cpe设备做特殊处理
//	else if(tmp=="device_cpe"  && (new String(type)).length>0 &&
//		(parseInt(type,10)>0 || parseInt(type,10)==-1 || parseInt(type,10)==-2))
//	{
//		var tmp_id=new String(arrObjectID);
//		if(tmp_id.indexOf(",")>0)
//		{
//			return false;
//		}
//		if(parseInt(arrDev[tmp_id]._iscpe,10)==0)
//		{
//			return false;
//		}
//		else
//		{
//			return true;
//		}
//	}
//	else if(tmp=="device_host"  && (new String(type)).length>0 &&
//		(parseInt(type,10)>0 || parseInt(type,10)==-1 || parseInt(type,10)==-2))
//	{ //主机设备菜单
//		var tmp_id=new String(arrObjectID);
//		if(tmp_id.indexOf(",")>0)
//		{
//
//			return false;
//		}
//		else
//		{
//			//网络设备 1/dev/  主机设备 2/
//			if(arrDev[tmp_id] != null){
//				var tmpIP = arrDev[tmp_id]._id;
//				//alert(checkDeviceViewType(tmpIP));
//				if(checkDeviceViewType(tmpIP) == 2){
//					return true;
//				}
//				else
//					return false;
//			}else
//				return false;
//		}
//	}
//	else if(tmp=="device_net"  && (new String(type)).length>0 &&
//		(parseInt(type,10)>0 || parseInt(type,10)==-1 || parseInt(type,10)==-2))
//	{ //网络设备菜单
//		var tmp_id=new String(arrObjectID);
//		if(tmp_id.indexOf(",")>0)
//		{
//			return false;
//		}
//		else
//		{
//			//网络设备 1/dev/  主机设备 2/
//			if(arrDev[tmp_id] != null){
//				var tmpIP = arrDev[tmp_id]._id;
//				if(checkDeviceViewType(tmpIP) == 1){
//					return true;
//				}
//				else
//					return false;
//			}else
//				return false;
//		}
//	}
	//针对链路做的判断
	else if(tmp=="link" && (new String(type)).length==0)
	{
		return true;
	}
	//剪切操作
	//不可对待确认设备或云团进行剪切操作
	else if((tmp=="netCut") && (new String(type)).length>0 && pid !="null")
	{
		//选中了多个设备时
		var tmp_id=new String(arrObjectID);
		if(tmp_id.indexOf(",")>0)
		{
			var o_array=arrObjectID.split(",");
			for(var i=0;i<o_array.length;i++)
			{
				//是否为待确认设备或待确认云团
				//_isconfirm 待确认为0 已确认为1
				if(arrDev[o_array[i]]._isconfirm == '0' || arrDev[o_array[i]]._isConfirmSeg == 'unkdev')
					return false;
			}
			return true;
		}
		//选中了一个设备时
		else{
			if(arrDev[arrObjectID]._isconfirm == '0' || arrDev[arrObjectID]._isConfirmSeg == 'unkdev')
				return false;
			return true;
		}
	}
	//删除操作
	//不可删除待确认云团
	else if((tmp=="netDelete") && (new String(type)).length>0 && pid !="null")
	{
		//选中了多个设备时
		var tmp_id=new String(arrObjectID);
		if(tmp_id.indexOf(",")>0)
		{
			var o_array=arrObjectID.split(",");
			for(var i=0;i<o_array.length;i++)
			{
				//是否为待确认云团
				//_isConfirmSeg 待确认云团为unkdev  普通云团为0
				if(arrDev[o_array[i]]._isConfirmSeg == 'unkdev')
					return false;
			}
			return true;
		}
		//选中了一个设备时
		else{
			if(arrDev[arrObjectID]._isConfirmSeg == 'unkdev')
				return false;
			return true;
		}
	}
	//粘帖 取消剪切操作
	//不可在待确认云团上进行粘帖操作
	else if(tmp=="netPase" && pid!="null" && parseInt(type,10)==0)
	{
		if(curPasteObj==null)
		{
			return false;
		}

		var tmp_id=new String(curPasteObj);
		//arrDev[arrObjectID]._isConfirmSeg == '0' 表明为普通云团
		if(tmp_id.length>0&&arrDev[arrObjectID]._isConfirmSeg == '0')
		{
			return true;
		}
		else
		{
			return false;
		}

	}
	//该菜单只能选择一台设备或者一个网段
//	else if(tmp=="singlenet" && pid!="null")
//	{
//		var tmp_id=new String(arrObjectID);
//		if(tmp_id.indexOf(",")>0)
//		{
//			return false;
//		}
//		else
//		{
//			return true;
//		}
//	}
	//针对网段类型的菜单
	else if(tmp=="0"&&parseInt(type,10)==0)
	{
		//暂时只有针对网段的“进入下一层”菜单
		return true;
	}
	//针对待确认云团特有菜单－－确认所有待确认设备
	else if(tmp=="segment_confirm"&&parseInt(type,10)==0)
	{
		//alert("segment_confirm "+arrObjectID);
		//是否为待确认设备云团
		//待确认云团的_isConfirmSeg为 unkdev 普通云团为0
		if(arrDev[arrObjectID]._isConfirmSeg != '0')
			return true;
		else
			return false;
	}
	//针对待确认设备特有菜单－－支持多个
	//如果选中设备中有已确认设备 则此菜单不会出现
	else if(tmp=="device_confirm"&&parseInt(type,10)>0)
	{
		//选中了多个设备时
		var tmp_id=new String(arrObjectID);
		if(tmp_id.indexOf(",")>0)
		{
			var o_array=arrObjectID.split(",");
			for(var i=0;i<o_array.length;i++)
			{
				//是否为待确认设备
				//待确认为0 已确认为1
				if(arrDev[o_array[i]]._isconfirm == '1')
					return false;
			}
			return true;
		}
		//选中了一个设备时
		else{
			if(arrDev[arrObjectID]._isconfirm == '0')
				return true;
			return false;
		}
	}
	//针对待确认设备特有菜单－－单个
	else if(tmp=="device_confirm_single"&&parseInt(type,10)>0)
	{
		var tmp_id=new String(arrObjectID);
		if(tmp_id.indexOf(",")>0)
		{
			return false;
		}
		//待确认为0 已确认为1
		if(arrDev[arrObjectID]._isconfirm == '0')
				return true;
		return false;
	}
	//针对已确认设备特有菜单
	//如果选中设备中有待确认设备 则此菜单不会出现
	else if(tmp=="device_confirmed"&&parseInt(type,10)>0)
	{
		//选中了多个设备时
		var tmp_id=new String(arrObjectID);
		if(tmp_id.indexOf(",")>0)
		{
			var o_array=arrObjectID.split(",");
			for(var i=0;i<o_array.length;i++)
			{
				//是否为待确认设备
				//待确认为0 已确认为1
				if(arrDev[o_array[i]]._isconfirm == '0')
					return false;
			}
			return true;
		}
		//选中了一个设备时
		else{
			if(arrDev[arrObjectID]._isconfirm == '1')
				return true;
			return false;
		}
	}
	//针对已确认设备特有菜单－－单个
	else if(tmp=="device_confirmed_single"&&parseInt(type,10)>0)
	{
		var tmp_id=new String(arrObjectID);
		if(tmp_id.indexOf(",")>0)
		{
			return false;
		}
		//待确认为0 已确认为1
		if(arrDev[arrObjectID]._isconfirm == '1')
				return true;
		return false;
	}
	else
	{
		//针对网段或者特定设备类型的菜单
		tmp = ","+ arrType +",";
		if(tmp.indexOf(","+type+",") == -1)
			return false;
		else
			return true;
	}
}

/*
 *根据设备id字符串判断用户视图中设备类型
 *网络设备：4/1/dev/1317/0  4/1/dev/开头
 *主机设备：4/2/10/0        4/2/开头
 *return 1:网络视图设备 2：主机视图设备 0：未知
 */
function checkDeviceViewType(device_id){
	if(device_id == null || device_id == "") return 0;
	if(device_id.indexOf("4/1/gw/") != -1)
		return 1;
	else if(device_id.indexOf("4/2/") != -1)
		return 2;

	return 0;
}
//创建右健菜单
var RM_Menu = new CMenu("RM_Menu");
function createRMenu(type,pid){
	var n=0;
	RM_Menu.menu.length=0;
	//alert(arrRMenu);
	for(var i=0;i<arrRMenu.length;i++){
		if(arrRMenu[i] == null) continue;
		if(chkPremession(arrRMenu[i]._keyword)){
			if(chkDeviceType(arrRMenu[i]._type,type,pid)){
				//alert("sssssssss"+type);
				if(typeof(arrRMenu[i]._submenu) == "string")
				{
					RM_Menu.menu[n] = new menu("RM_Menu"+n,
						arrRMenu[i]._name,
						"","",
						arrRMenu[i]._target,
						"",0,
						arrRMenu[i]._submenu);
					//alert("Main:"+arrRMenu[i]._submenu+"\n"+i)
					createRSMenu(arrRMenu[i]._submenu,type,pid);
				}
				else
					RM_Menu.menu[n] = new menu("RM_Menu"+n,
						arrRMenu[i]._name,
						"","",
						arrRMenu[i]._target,
						"",1,
						arrRMenu[i]._link);
				n++;
			}
		}
	}
}

function createRSMenu(submenu,type,pid){
	//alert("menu name is "+submenu);
	var RS_Menu = new CMenu(submenu);
	var n=0;
	obj = eval(submenu);
	if(typeof(obj)!="object")
	{
		return;
	}

	for(var i=0;i<obj.length;i++){
		//alert(obj[i]._oper);
		if(chkPremession(obj[i]._keyword)){
			if(chkDeviceType(obj[i]._type,type,pid)){
				if(typeof(obj[i]._submenu) == "string")
				{
					RS_Menu.menu[n] = new menu(submenu+n,
						obj[i]._name,
						"","",
						obj[i]._target,
						"",0,
						obj[i]._submenu);
					//alert("Sub:"+obj[i]._submenu)
					createRSMenu(obj[i]._submenu,type,pid);
				}
				else
				{
					RS_Menu.menu[n] = new menu(submenu+n,
						obj[i]._name,
						"","",
						obj[i]._target,
						"",1,
						obj[i]._link);
					//alert("Sub:"+obj[i]._submenu)
				}
				n++;
			}
		}
	}

	drawSubMenu(RS_Menu);
}

function clickRow(){
	clearRightMenu();
	return;
}

function rightclick(){
	if(event.button==1) return;
	oList.selectRow();
	var index = oList.currentRow.rowIndex;
	//传当前告警ID给菜单系统,arrObjectID 是在coolmenu.js定义的,smf
	arrObjectID = oList.lvdata[index-1][0]._value;
	//alert("rightclick中创建菜单");

	gatherIdStr= oList.lvdata[index-1][13]._value ; //获得发出告警的属地信息, 赋值给 全局变量 gatherIdStr
	//alert("告警gather_id 是"+gatherIdStr);
	//alert("ttttt"+RM_Menu);
	//if(RM_Menu.length=="undefined")

  //alert("index is: "+index);                 //index代表 告警信息在 当前oList列表中的 索引
 // alert("arrObjectID is: "+ arrObjectID);      //arrObjectID 代表 告警ID serialno@1/dev/device_Coding
	createRMenu("-3",-1);
	showRightMenu();

	return;
}

function showRightMenu(){
	//显示之前，首先将原有的层删除掉
	clearRightMenu();
	//画图
	drawSubMenu(RM_Menu);
	//alert(RM_Menu.menu.length);
	//显示菜单
	showRMenu("RM_Menu");



	g_blnRMShow = true;
	return;
}

function clearRightMenu(){

	document_click();

	/*
	if(document.all("RM_Menu"))
	{
		document.body.removeChild(document.all("RM_Menu"));

	}
	*/
	return;
}

var alarmLevel = new Array(6);
alarmLevel[0] = new Array("0","清除告警");
alarmLevel[1] = new Array("1","事件告警");
alarmLevel[2] = new Array("2","警告告警");
alarmLevel[3] = new Array("3","次要告警");
alarmLevel[4] = new Array("4","主要告警");
alarmLevel[5] = new Array("5","严重告警");



var alarmFlag = new Array(2);
alarmFlag[0] = new Array("false","&nbsp;");
alarmFlag[1] = new Array("true","<img src='images/check.png'>");


function initLV(warn_num){
	oList.setColumnProperty('Severity','backgroundColor:#FF0000;color:#000000;cursor:default','5',true);
	oList.setColumnProperty('Severity','backgroundColor:#FFB4B2;color:#000000;cursor:default','4',true);
	oList.setColumnProperty('Severity','backgroundColor:#FFC351;color:#000000;cursor:default','3',true);
	oList.setColumnProperty('Severity','backgroundColor:#FFEBB5;color:#000000;cursor:default','2',true);
	oList.setColumnProperty('Severity','backgroundColor:#E1ECFB;color:#000000;cursor:default','1',true);
	oList.setColumnProperty('Severity','backgroundColor:#FFFFFF;color:#000000;cursor:default','0',true);

	oList.formatColumnValue('Severity',alarmLevel);
	oList.formatColumnValue('acknowledged',alarmFlag);
	oList.maxRowCount = warn_num;
	oList.pageSize = "200";
	oList.drawData();
}


function showMsgDlg(){
	//w = document.body.clientWidth;
	//h = document.body.clientHeight;

	//l = (w-250)/2;
	//t = (h-60)/2;
	//PendingMessage.style.left = l;
	//PendingMessage.style.top  = t;
	//PendingMessage.style.display="";
}

function closeMsgDlg(){
	PendingMessage.style.display="none";
}

function showPageFrm(type,oIndex){
	if(type==1||type==2||type==3){
		ip =  arrDev[oIndex]._ip;
		page = "DevicePageFrm.jsp?ip="+ip+"&refresh="+(new Date()).getTime();
		var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=1,scrollbars=0,status=0,toolbar=0";
		newWin(page,otherpra);
	}
	else if(type==0){

	}
}

//显示流量
function showFlux(type,oIndex)
{
 ip=arrDev[oIndex]._ip;
 page = "webtop_liuliang.jsp?device_id="+ip+"&refresh="+(new Date()).getTime();
 //page = "webtop_liuliang_jsRefresh.jsp?device_id="+ip+"&refresh="+(new Date()).getTime();
 var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
 newWin(page,otherpra,1000,400);
}

//显示性能
function showPmee(type,oIndex)
{
 ip=arrDev[oIndex]._ip;
 page = "webtop_xinnen.jsp?device_id="+ip+"&refresh="+(new Date()).getTime();
 var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
 newWin(page,otherpra,1000,400);
}

//显示设备性能和流量
function showFluxAndPmee(type,oIndex)
{
 ip=arrDev[oIndex]._ip;
 page = "webtop_FluxAndPmee.jsp?device_id="+ip+"&refresh="+(new Date()).getTime();
 //page = "webtop_liuliang_jsRefresh.jsp?device_id="+ip+"&refresh="+(new Date()).getTime();
 var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
 newWin(page,otherpra,1000,680,5,5);
}

//显示系统信息
function showSysInfo(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
	page = "webtop_SysInfo.jsp?title=系统信息&className=ReadDeviceInfo&device_id="+ip+"&refresh="+(new Date()).getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,850,680);
}

//显示EVDO信息
function showEVDOInfo(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
	page = "../bbms/resource/showCardInfo.action?deviceId="+ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	newWin(page,otherpra,850,450,50,50);
}

//显示端口信息
function showPortInfo(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
	page = "webtop_SysInfo.jsp?title=端口信息&className=ReadDevicePort&device_id="+ip+"&refresh="+(new Date()).getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,1000,750);
}

//显示当前设备的端口,速率,状态等信息
function showDeviceStatus(type,oIndex) {
	ip=arrDev[oIndex]._ip;
	
	page = "webtop_DeviceStatus2.jsp?title=设备状态信息&className=ReadDeviceStatus&device_id="+ip+"&refresh="+(new Date()).getTime();
	//page = "deviceStatusFrame.jsp?device_id="+ip+"&refresh="+(new Date()).getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=1,toolbar=0";
	newWin(page,otherpra,1000,650,5,5);
}
// -----------------add by lizhaojun 2007-01-12---------webtopo增加ping和telnet菜单------->start
//ping 服务器地址
function pingIp(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
	page = "webtop_ping.jsp?&device_id="+ ip +"&action=ping" ;
	document.all("childFrm").src = page;
}

//telnet 服务器地址
function telnetLink(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
	page = "webtop_ping.jsp?&device_id="+ip+"&action=telnet" ;
	document.all("childFrm").src = page;
}
//tracert 服务器地址
function tracertLink(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
	page = "webtop_ping.jsp?&device_id="+ip+"&action=tracert" ;
	document.all("childFrm").src = page;
}
// -----------------add by lizhaojun 2007-01-12---------webtopo增加ping和telnet菜单------>-end
//显示设备告警
function showDeviceWarn(type,oIndex)
{
	var ip=arrDev[oIndex]._ip;
	var page = "webtop_viewwarn.jsp?type=device&device_id="+ip+"&refresh="+(new Date()).getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,800,400);
}

//显示所有告警
function showAllDeviceWarn()
{
	page = "webtop_viewwarn.jsp?type=all&refresh="+(new Date()).getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,800,600);

}


//新开窗口
function newWin(page,otherpra){
	SynOperator(function(){
	var w		= 750;
	var h		= 600;
	var l		= (document.body.clientWidth-w)/2;
	var t		= (document.body.clientHeight-h)/2;

	window.open(page,"","left="+l+",top="+t+",width="+w+",height="+h+","+otherpra);
	});
}

function newWin(page,otherpra,w,h)
{
	SynOperator(function(){
	var l		= (document.body.clientWidth-w)/2;
	var t		= (document.body.clientHeight-h)/2;
	window.open(page,"","left="+l+",top="+t+",width="+w+",height="+h+","+otherpra);
	});
}

function newWin(page,otherpra,w,h,l,t)
{
	SynOperator(function(){
	window.open(page,"","left="+l+",top="+t+",width="+w+",height="+h+","+otherpra);
	});
}

//显示所有设备
function ViewDevice(type)
{
	isDisplayAll=type;
	viewTopo();
}


//管理设备
function ManagerDeviceWin(type,oIndex)
{
	var tmp=new String(oIndex);
	var o_array=tmp.split(",");
	var ids="";
	for(var i=0;i<o_array.length;i++)
	{
		if(i==0)
		{
			ids=arrDev[o_array[i]]._id;
		}
		else
		{
			ids +=","+arrDev[o_array[i]]._id;
		}

	}
	var page="ManagerDevice.jsp?type=1&objs="+ids;
	document.all("childFrm").src=page;
}

//取消管理设备
function UnManagerDeviceWin(type,oIndex)
{
	var tmp=new String(oIndex);
	var o_array=tmp.split(",");
	var ids="";
	for(var i=0;i<o_array.length;i++)
	{
		if(i==0)
		{
			ids=arrDev[o_array[i]]._id;
		}
		else
		{
			ids +=","+arrDev[o_array[i]]._id;
		}

	}
	var page="ManagerDevice.jsp?type=0&objs="+ids;
	document.all("childFrm").src=page;
}

function showLinkInfo(type,oIndex)
{
	var LinkObj = arrLink[oIndex];
	var linkid  = LinkObj._id;
	_from_obj	= arrDev[findDevObjByID(LinkObj._from)];
	_to_obj     = arrDev[findDevObjByID(LinkObj._to)];
	var _from_title = _from_obj._title;
	var _to_title   = _to_obj._title;
	var _from_ip    = _from_obj._ip;
	var _to_ip      = _to_obj._ip;
	var page        = "../Visualman/now_main.jsp?linkid=" + linkid + "&from=" + _from_title + "&to=" + _to_title + "&from_ip=" + _from_ip + "&to_ip=" + _to_ip;
	window.open(page,"","left=70,top=130,width=800,height=800,resizable=yes,scrollbars=yes");
}

//modified by ll
function editLinkInfo(type,oIndex)
{
	//alert(oIndex);

	////////////////////////////webtopo报表/////////////////////////////
	var LinkObj = arrLink[oIndex];
	var linkid = LinkObj._id;
	_from_obj	= arrDev[findDevObjByID(LinkObj._from)];
	_to_obj     = arrDev[findDevObjByID(LinkObj._to)];
	var _from_title = _from_obj._title;
	var _to_title = _to_obj._title;
	//var _to_id   = _to_obj._id;
	//var _from_type = _from_id._type;
	//var _to_type =_to_obj._type;
	//var param = "";
	//判断是否为网元对象  _type如果是0的话就是网段，其余的都是网元
	//设备from 和 to 使用“,”隔开。如果有多个from 和 to，则使用“;”隔开
	//if(_from_type == 0){ // from端为网段

	//}else if(_to_type == 0){//to端为网段

	//}else{//两端都不是网段  设备
	//	param = _from_id + "," + _to_id;
	//}
	var page = "../Visualman/EditLinkInfo.jsp?linkid=" + linkid + "&from=" + _from_title + "&to=" + _to_title;
	window.open(page);
}

//link css.
//modified by yanhj,2006-10-13.
function editLinkCss(type,oIndex) {
// alert(oIndex);
 var LinkObj = arrLink[oIndex];
 var link_id = LinkObj._id;
 _from_obj = arrDev[findDevObjByID(LinkObj._from)];
 _to_obj     = arrDev[findDevObjByID(LinkObj._to)];
 var _from_title = _from_obj._title;
 var _to_title = _to_obj._title;
 var page = "./link_css_edit_form.jsp?link_id=" + link_id + "&from=" + _from_title + "&oIndex=" + oIndex+"&to=" + _to_title;
 window.open(page,"编辑链路样式","left=20,top=20,width=450,height=250,resizable=no,scrollbars=yes");
}

//剪切设备
function CutNet(type,oIndex)
{
	var tmp=new String(oIndex);
	var o_array=tmp.split(",");
	var ids="";
	curPasteObj=new Array(o_array.length);
	for(var i=0;i<o_array.length;i++)
	{
		curPasteObj[i]=arrDev[o_array[i]];
	}
}

//粘贴设备
function PasteNet(type,oIndex)
{
	if(curPasteObj==null || curPasteObj.length==0)
	{
		window.alert("请选剪切设备");
		return;
	}

	if(arrDev[oIndex]._id==curPasteObj[0]._pid)
	{
		window.alert("您要粘贴的对象已经在您选择的网段内！");
		return;
	}

	var ids="";
	for(var i=0;i<curPasteObj.length;i++)
	{
		if(i==0)
		{
			ids=curPasteObj[i]._id;
		}
		else
		{
			ids +=","+curPasteObj[i]._id;
		}

	}

	var page="DeviceOperation.jsp?action=paste&objs="+ids+"&refresh_id="+arrPObjectID+"&parent_id="+arrDev[oIndex]._id+"";
	//alert(page);
	document.all("childFrm").src=page;

}

//取消剪切
function CancelCut(type,oIndex)
{
	curPasteObj=null;

}

//删除设备
function DeleteObj(type,oIndex)
{

	if(!window.confirm("您确认要删除您选择的对象？"))
	{
		return;
	}

	var tmp=new String(oIndex);
	var o_array=tmp.split(",");
	var ids="";
	for(var i=0;i<o_array.length;i++)
	{
		if(i==0)
		{
			ids=arrDev[o_array[i]]._id;
		}
		else
		{
			ids +=","+arrDev[o_array[i]]._id;
		}

	}



	var page="DeviceOperation.jsp?action=delete&objs="+ids+"&refresh_id="+arrPObjectID+"";

	document.all("childFrm").src=page;

}


//删除设备
function DeleteLinkObj(type,oIndex)
{

	if(!window.confirm("您确认要删除您选择的链路？"))
	{
		return;
	}

	var tmp=new String(oIndex);
	var o_array=tmp.split(",");
	var ids="";
	for(var i=0;i<o_array.length;i++)
	{
		if(i==0)
		{
			ids=arrLink[o_array[i]]._id;
		}
		else
		{
			ids +=","+arrLink[o_array[i]]._id;
		}

	}


	var page="DeviceOperation.jsp?action=delete&objs="+ids+"&refresh_id="+arrPObjectID+"";

	document.all("childFrm").src=page;

}


//流量配置
function ConfigFlux(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
	var serial=arrDev[oIndex]._type;
	//page="webtop_ConfigFlux.jsp?device_id="+ip+"&serial="+serial;
	page = "../performance/configFlux.action?isbatch=false&device_id="+ip;
	var otherpra="channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	newWin(page,otherpra,750,500);
}
//QOS配置
function ConfigQOS(type,oIndex){
	ip=arrDev[oIndex]._ip;
	var serial=arrDev[oIndex]._type;
	page="webtop_ConfigQOS.jsp?device_id="+ip+"&serial="+serial;
	var otherpra="channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	newWin(page,otherpra,750,500);
}

//性能配置
function ConfigPerf(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
	var serial=arrDev[oIndex]._type;
	if (serial<0)
	{
		alert("此设备暂时无法配置性能！");
	}
	else {
		//page = "pm_Config.jsp?device_id="+ip+"&dev_serial="+serial;
		page = "../performance/configPmee.action?isbatch=false&device_id="+ip;
		var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,height=500,width=700,top=200,left=365";
		window.open(page,"设备性能配置",otherpra);
	}
}

//IPCHECK配置
function ConfigIpCheck(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
	page = "single_DevIPcheck.jsp?device_id="+ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,height=160,width=300,top=200,left=365";
	window.open(page,"IP状态配置",otherpra);

}

//对业务电路的配置
function ConfigPathID(type,oIndex)
{
	//alert(arrDev[oIndex]._iscpe);
	ip=arrDev[oIndex]._ip;
	var objid=arrDev[oIndex]._id;
	page="config_cpe.jsp?objid="+objid+"&device_id="+ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,600,320);
}

//查看当前设备的电路图
function showCircuit(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
	page="view_cpe.jsp?device_id="+ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,600,200);

}

//显示一台设备的详细信息
function showDevDetail(type,oIndex)
{
	//增加了用户视图.用户视图中包括网络设备(ip=1/dev/device_id)和主机设备(2/dxbh)
	ip=arrDev[oIndex]._ip;
	//用户视图 需要将ip进行处理 ip=1/dev/2222-->ip=2222
	//if(ViewID == 4 && ip.indexOf("1/dev/") == 0 && ip.length > 6){
	//	ip = ip.substr(6);
	//}
	var page="../Resource/DeviceShow.jsp?device_id="+ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,800,580);
}

function swapNewObj()
{
	isCreateLink=false;
	//判断如果当前状态是不锁定状态
	if(!isCreateNet)
	{
		document.all("Image11").src="./images/topban_1_lock.gif";
		document.all("Image11").title="解除锁定";
		isCreateNet=true;

	}
	else
	{
		document.all("Image11").src="./images/topban_1.gif";
		document.all("Image11").title="新增对象";
		isCreateNet=false
	}
}

function swapNewLink(){
	isCreateNet=false;

	if(!isCreateLink)	{
		document.all("Image12").src="./images/topban_2_lock.gif";
		document.all("Image12").title="解除锁定";
		isCreateLink=true;
	}
	else
	{
		document.all("Image12").src="./images/topban_2.gif";
		document.all("Image12").title="新增链路";
		isCreateLink=false;
	}
}

//删除告警
function remove_Warn(type,oIndex){
	if(!window.confirm("您确认要删除您选择的告警？")){
		return;
	}
	var page="./removeAlarm.jsp?alarmStr="+oIndex;
	document.all("childFrm").src=page;

}

//确认告警
function ack_Warn(type,oIndex)
{
	if(isActivestatus()){
		alert("此告警已经确认");
		return ;
	}
	if(!window.confirm("确认告警吗？")){
		return;
	}
/**
	var page="./ackAlarm.jsp?alarmStr=" + oIndex + "&gatherId="+gatherIdStr +"&tt="+ new Date().getTime();
	if(_xmlHttp == null) _xmlHttp = new XmlHRequest();
	_xmlHttp.setURL(page);
	//将XMLHttp的setReadyChange触发事件传入
	_xmlHttp.doPost(ack_Warn_Result);
	//document.all("childFrm").src=page;
*/
    var tmp=oIndex.split("@"); 
	var alarmid=tmp[0]+"-"+gatherIdStr+"-"+tmp[1]+"-"+Math.round(new Date().getTime()/1000)+"-/-";
    var page="./RealTimeWarn!ConfigWarn.action?alarmid=" + alarmid;
	//if(_xmlHttp == null) _xmlHttp = new XmlHRequest();
	//_xmlHttp.setURL(page);
	//将XMLHttp的setReadyChange触发事件传入
	//_xmlHttp.doPost(ack_Warn_Result);
	document.all("childFrm").src=page;
    window.location.reload();
}
function ack_Warn_Result(){
	if(_xmlHttp != null && _xmlHttp.getReady()){
		var result = _xmlHttp.getResult().toString();
		var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
        xmlDoc.loadXML(result);
        var node = xmlDoc.documentElement.selectSingleNode("//result");
		if(node!=null && node.text == 1){
			document.all("oList").deleteRow();
			alert("告警确认成功");
		}else{
			alert("告警确认失败，请重试！");
		}
		xmlDoc = null;
	}
}
//告警清除
function clear_Warn(type,oIndex){
	if(!window.confirm("清除告警吗？"))
	{
		return;
	}
	var page="./clearAlarm.jsp?alarmStr=" + oIndex + "&gatherId="+gatherIdStr +"&tt="+ new Date().getTime();
	//alert(page);

	if(_xmlHttp == null) _xmlHttp = new XmlHRequest();
	_xmlHttp.setURL(page);
	//将XMLHttp的setReadyChange触发事件传入
	_xmlHttp.doPost(clear_Warn_Result);
}
function clear_Warn_Result(){
	if(_xmlHttp != null && _xmlHttp.getReady()){
		var result = _xmlHttp.getResult().toString();
		var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
        xmlDoc.loadXML(result);
        var node = xmlDoc.documentElement.selectSingleNode("//result");
		if(node!=null && node.text == 1){
			document.all("oList").deleteRow();
			alert("告警清除成功");
		}else{
			alert("告警清除失败，请重试！");
		}
		xmlDoc = null;
	}
}
//查询告警
function query_Warn(type,oIndex)
{
	var page="./query_Warn.jsp?warnIndex="+oIndex+"&type="+type;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,height=310,width=640,top=200,left=220";
		window.open(page,"告警查询",otherpra);
}

// 确认告警并入库
function ack_Store_Warn(type,oIndex) {
	if(isActivestatus()){
		alert("此告警已经确认");
		return ;
	}
	var page="./ack_Store_Alarm.jsp?alarmStr="+oIndex+"&gatherId="+gatherIdStr;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=no,status=0,toolbar=0";
	newWin(page,otherpra,800,700);
}
//检查当前告警
//true:告警已经确认 false:还没有确认
function isActivestatus(){
	if(oList.currentRow == null)
		return false
	else{
		var index = oList.currentRow.rowIndex;
		var activestatus = oList.lvdata[index-1][4]._value;
		if(activestatus.indexOf("已确认") != -1){
			return true;
		}
	}
	return false;
}
//Add By Hemc 2006-12-11
//告警列表－定位设备对象
//参照topo.js中的Dev_MouseDown方法
//定位到设备函数
function findDevObjLocation(dev,srcpid){
	if(srcpid == null || typeof(srcpid) == "undefined") srcpid = (arrPObjectID == null || arrPObjectID == "" || typeof(arrPObjectID) == "undefined") ? "1/0" : arrPObjectID;
	if(dev == "-1") return false;
	var ln = -1;
	ln = findDevObjByID(dev);
	//如果设备在拖放列表里面,直接返回.
	//if(findDragObjListByID(ln)) return;
	if(arrDev != null && arrDev[ln] != null){
		arrObjectID = ln;
		arrPObjectID = arrDev[ln]._pid;
		//查找与选中设备的链路
		findLinkObjByID(dev);
		//清除链路选中状态
		clearLinkBorder();
		arrCurDragLink = null;
		curDragLinkObj=null;
		//删除当前被选中的对象边框
		clearDevBorder();
		arrDev[ln].drawBorder();
		focusDevObj(arrDev[ln]);
		//将画边框的设备放入『被拖动网元对象数组』中
		arrCurDrag = null;
		arrCurDrag = new Array();
		arrCurDrag[0] = new DragObj(ln,curDragLinkFrom,curDragLinkTo);
	}
	else{
		//此种情况是未知设备无法定位
		if(dev == "1/gw/"){
			alert("未知设备id无法定位(" + dev + ")");
		}else if(confirm("设备不在当前层，需要进入此设备的Topo图吗？(" + dev + ")")){
			GoTopoByDevID(dev,srcpid);
		}else{
			if(srcpid == null || typeof(srcpid) == "undefined") srcpid = "1/0";
			arrPObjectID = srcpid;
			refreshTopo();
		}
	}
	return ;
}
//告警列表中设备定位js函数
function findDevObj(type,oIndex){
	var $ = null;
	try{
		$ = window.opener == null ? this : window.opener;
	}catch(e){
		alert("WebTopo窗口已被关闭，无法定位到设备！(" + e.description + ")");
		return false;
	}
	try{
		var _index = oIndex.indexOf("@");
		var dev = "1/gw/";
		if(_index != -1){
			dev = oIndex.substr(_index + 1);
		}
		//alert("arrPObjectID:" + arrPObjectID);
		$.findDevObjLocation(dev,arrPObjectID);
		$.focus();
		return true;
	}catch(e){
		alert("无法设备定位!\n错误描述为：" + e.description);
	}
	return false;
}
//将设备聚焦 Add By Hemc
function focusDevObj(devObj){
	try{
		var _$ = document.all;
		if(_$(devObj._id) != null){
			//_$(devObj._id).focus();
			idWebTopo.focus();
			//让层idWebTopo滚动，定位到设备
			idWebTopo.scrollTop = devObj._y;
			idWebTopo.scrollLeft = devObj._x;
		}
	}catch(e){
		window.alert("无法设备聚焦(focusDevObj" + e.description + ")");
	}
}

//根据传入的设备id(1/dev/222)得到topo图 Add by HEMC 2006-12-15
function GoTopoByDevID(pid,srcpid){
	var oldViewID = ViewID;
	//暂时保存当前Topo图pid
	idWebTopo.src = "getTopoByIdXML.jsp?pid="+ pid;
	dispTopo();
	idWebTopo.innerHTML = "<v:rect id=selRect  style=\"DISPLAY: none; Z-INDEX:  255; POSITION:  absolute\" coordsize = \"21600,21600\" strokecolor = \"#10fc18\"><v:fill opacity = \"0\" ></v:fill></v:rect>";
	drawTopo();
	initParamToSession();
	findDevObjLocation(pid,srcpid);
}

//清除所有告警标记
//Add by HEMC 2006-12-15
function removeAllWarnMain(){
	if(arrDev != null){
		for(var i=0;i<arrDev.length;i++){
			arrDev[i].removeWarn();
		}
	}
}
//给多台设备设备画告警 Add by HEMC 2006-12-15
//idRefreshAlarmNum是需要给设备画告警字符串
//如：1/dev/222,15,4;1/dev/333,16,3(设备id,告警数量,告警级别)
function refreshAlarmNumMain(){
	var s = idRefreshAlarmNumMain;
	if(s=="") return;
	var list = s.split(";");
	//devAlarm = new Array();
	for(var i=0;i<list.length;i++){
		arr = list[i].split(",");
		ln = findDevObjByID(arr[0]);
		//devAlarm[devAlarm.length] = arr[0];
		num = arr[1];
		level = arr[2];
		//Topo图中存在设备对象
		if(typeof(arrDev[ln]) != "undefined"){
			arrDev[ln].drawWarn(num,level);
		}
	}
	refreashLinkColor();
}
//链路状态改变
function refreashLinkColor(){
	var s = idRefreshAlarmNumLink;
	if(s=="") return;
	var list = s.split(",");
	for(var i=0;i<list.length;i++){
		ln = findDevObjByID(list[i]);
		if(typeof(arrLink[ln]) != "undefined"){
			arrLink[ln]._color = "red";
			arrLink[ln]._weight = "3";
			arrLink[ln].view();
		}
	}
}
//JS遍历告警状态实现链路变红
function refreshLinkStatus(){
	var s = idRefreshAlarmNumMain;
	if(s=="") return;
	var list = s.split(";");
	var devAlarm = new Array();
	for(var i=0;i<list.length;i++){
		arr = list[i].split(",");
		//devAlarm[devAlarm.length] = arr[0];
		findLinkObjByID(arr[0]);
	}
}
//刷新告警的状态
function refreshDeviceStatus(result){
	if(result == "") return;
	var list = result.split(";");
	var arr = null;
	var ln = 0;
	var level = 0;
	for(var i=0;i<list.length;i++){
		arr = list[i].split(",");
		ln = findDevObjByID(arr[0]);
		level = arr[1];
		//Topo图中存在设备对象
		if(typeof(arrDev[ln]) != "undefined"){
			arrDev[ln].drawStatus(level);
		}else{
			//alert("no device " + arr[0]);
		}
	}
}
function resetAllDeviceStatus(){
	if(arrDev != null)
		for(var i=0;i<arrDev.length;i++){
			arrDev[i].resetStatus();
		}
}
//得到web上控件的值，然后再调用webtopoSearchDevice方法
function webtopoSearch(){
	if(_searchValue.value == "")
		alert("请输入查询数据!");
	else{
		webtopoSearchDevice(_searchValue.value,_typeSelect.value);
	}
}
/*
 *使用XMLHttp来获取数据(webtopo设备查询)
 *_type  1:device_ip 2:device_name 3:device_model
 *_value  需要查询的值
 */
function webtopoSearchDevice(_value,_type){
	divSelectDevice.innerHTML = "正在加载设备....";
	var _url = "webtopo_SearchDevice.jsp?_value=" + _value + "&_type=" + _type;
	if(_xmlHttp == null) _xmlHttp = new XmlHRequest();
	_xmlHttp.setURL(_url);
	//将XMLHttp的setReadyChange触发事件传入
	_xmlHttp.doPost(setDeviceData);
}
//将已生成设备下拉框的数据加载到div中
function setDeviceData(){
	if(_xmlHttp != null && _xmlHttp.getReady()){
		divSelectDevice.innerHTML = _xmlHttp.getResult().toString();
	}
}
//将已生成设备类型下拉框的数据加载到div中
function setDeviceTypeData(){
	if(_xmlHttp != null && _xmlHttp.getReady()){
		divSelectDevSerial.innerHTML = _xmlHttp.getResult().toString();
	}
}
//将已生成设备厂商下拉框的数据加载到div中
function setVenderData(){
	if(_xmlHttp != null && _xmlHttp.getReady()){
		divSelectVendor.innerHTML = _xmlHttp.getResult().toString();
	}
}
//webtopo查找设备 按类型来查找设备
function changeSelectType(){
	divSelectDevice.innerHTML = "";
	//alert("_typeSelect.value:"+_typeSelect.value)
	//查找类型 OUI+Serial
	if(_typeSelect.value == "3"){
		//选择--"厂商|设备序列号"
		//当按设备类型查找时，文本框和按钮隐藏
		_searchValue.style.display = "";
		btn_QueryDev.style.display = "";

		//让厂商、设备型号div隐藏
		divSelectVendor.style.display = "none";
		divSelectDevSerial.style.display = "none";

		//divSelectDevSerial.innerHTML = "";
		//divSelectVendor.innerHTML = "加载厂商数据...";
		//var _url = "webtopo_SearchDeviceType.jsp?type=0";
		//if(_xmlHttp == null) _xmlHttp = new XmlHRequest();
		//_xmlHttp.setURL(_url);
		//将XMLHttp的setReadyChange触发事件传入
		//_xmlHttp.doPost(setVenderData);
	}
	//显示厂商，并能关联到型号
	else if(_typeSelect.value == "5") {
		//选择--"厂商|设备型号"
		//当按设备类型查找时，文本框和按钮隐藏
		_searchValue.style.display = "";
		btn_QueryDev.style.display = "";

		//让厂商、设备型号div显示
		divSelectVendor.style.display = "";
		divSelectDevSerial.style.display = "";

		divSelectDevSerial.innerHTML = "";
		divSelectVendor.innerHTML = "加载厂商数据...";
		var _url = "webtopo_SearchDeviceType.jsp?type=3";
		if(_xmlHttp == null) _xmlHttp = new XmlHRequest();
		_xmlHttp.setURL(_url);
		//将XMLHttp的setReadyChange触发事件传入
		_xmlHttp.doPost(setVenderData);
	}
	else{
	    _searchValue.value="";
		_searchValue.style.display = "";
		btn_QueryDev.style.display = "";
		divSelectVendor.style.display = "none";
		divSelectDevSerial.style.display = "none";
	}
}
//选择厂商，加载该厂商的设备序列号
function changeVendorType(_value){
	divSelectDevice.innerHTML = "";
	if(_value != "-1"){
		var _url = "webtopo_SearchDeviceType.jsp?type=1&vendor_id=" + _value;
		divSelectDevSerial.innerHTML = "加载设备型号...";
		if(_xmlHttp == null) _xmlHttp = new XmlHRequest();
		_xmlHttp.setURL(_url);
		//将XMLHttp的setReadyChange触发事件传入
		_xmlHttp.doPost(setDeviceTypeData);
	}else{
		divSelectDevSerial.innerHTML = "";
	}
}

//选择厂商，加载该厂商的设备型号
function relateDeviceModel(_value)
{
	divSelectDevice.innerHTML = "";
	if(_value != "-1"){
		var _url = "webtopo_SearchDeviceType.jsp?type=4&vendor_id=" + _value;
		divSelectDevSerial.innerHTML = "加载设备型号...";
		if(_xmlHttp == null) _xmlHttp = new XmlHRequest();
		_xmlHttp.setURL(_url);
		//将XMLHttp的setReadyChange触发事件传入
		_xmlHttp.doPost(setDeviceTypeData);
	}else{
		divSelectDevSerial.innerHTML = "";
	}
}
//选择厂商+设备序列号，加载对应的设备
function changeDeviceType(_value){
	if(_value != "-1"){
		_value =_VendorType.value+"|||"+_value;
		webtopoSearchDevice(_value,3);
	}else{
		divSelectDevice.innerHTML = "";
		//divSelectDevice.style.display = "";
	}
}

//选择厂商+设备型号，加载对应的设备
function relateDevice(_value)
{
	if(_value != "-1"){
		_value =_VendorType.value+"|||"+_value;
		webtopoSearchDevice(_value,5);
	}else{
		divSelectDevice.innerHTML = "";
		//divSelectDevice.style.display = "";
	}
}

//用户进入到topo层设置参数
function setTopoLayer(_boolean){
	var parentid = "";
	//true:设置默认topo层，false:撤销设置默认topo层。
	if(_boolean){
		parentid = arrPObjectID;
	}

	if(parentid != null && parentid != "null"){
		var url = "setTopoLayer.jsp?parentid=" + parentid;
		if(_xmlHttp == null) _xmlHttp = new XmlHRequest();
		_xmlHttp.setURL(url);
		_xmlHttp.doPost(setParentResult);
	}else{
		alert("当前层父对象为空，设置失败！");
	}
}
//设置后 给用户提示时候设置成功
function setParentResult(){
	if(_xmlHttp != null && _xmlHttp.getReady()){
		var result = _xmlHttp.getResult().toString();
		//result = result == "true" ? "设置成功!":"设置失败，请重试!";
		alert("设置成功!");
	}
}

//测试设备连接
function TestConnection(type, oIndex){
	ip = arrDev[oIndex]._ip;
	var serial = arrDev[oIndex]._type;
	var id = arrDev[oIndex]._id;
	var page = "./webtopo_TestConnection.jsp?oIndex=" + oIndex + "&device_id=" + ip + "&type=" + serial + "&id=" + id;
	document.all("childFrm").src = page;
}

//获取RPC方法
function RPCMethod(type, oIndex){
	ip = arrDev[oIndex]._ip;
	var serial = arrDev[oIndex]._type;
	var id = arrDev[oIndex]._id;
	var page = "./webtopo_RPCMethod.jsp?oIndex=" + oIndex + "&device_id=" + ip + "&type=" + serial + "&id=" + id;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra, 600, 300);
}

//修改设备类型
function ModifyDeviceType(type, oIndex){
	ip = arrDev[oIndex]._ip;
	var serial = arrDev[oIndex]._type;
	var id = arrDev[oIndex]._id;
	var page = "./webtopo_ModifyDeviceType.jsp?oIndex=" + oIndex + "&device_id=" + ip + "&type=" + serial + "&id=" + id;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra, 400, 200);
}
function ModifyDeviceIcon(type, oIndex){
	var serial = arrDev[oIndex]._type;
	var icon = arrDev[oIndex]._icon;
	var page = "./webtopo_ChangeIcon.jsp?serial=" + serial + "&icon=" + icon;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra, 400, 200);
}
 //修改设备属性
function ModifyDeviceAttribute(type, oIndex)
{
	ip = arrDev[oIndex]._ip;
	var serial = arrDev[oIndex]._type;
	var id = arrDev[oIndex]._id;
	var title=arrDev[oIndex]._title;
	var page = "./webtopo_ModifyDeviceAttribute.jsp?oIndex=" + oIndex + "&title="+title+"&device_id=" + ip + "&id=" + id + "&type=" + serial;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra, 300, 200);
}

//常见参数的获取
function getFamiliarPara(type, oIndex)
{
	ip = arrDev[oIndex]._ip;
	var page = "./paramAllList.jsp?device_id=" + ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra,1024, 500);
}

//参数模型获取
function getParaModule(type, oIndex)
{
	ip = arrDev[oIndex]._ip;
	var page = "./HGWGetParaModel.jsp?device_id=" + ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra,500, 400);
}

//确认设备参数查询
function paraQuery(type, oIndex)
{
	ip = arrDev[oIndex]._ip;
	var page = "./HGWGetMoreParaModel.jsp?device_id=" + ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra, 500,400);
}

//确认设备参数变化上报
function paraChangCall(type, oIndex)
{
	ip = arrDev[oIndex]._ip;
	var page = "./ParamChangeConfig.jsp?device_id=" + ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra,500, 400);
}

//设备ping检测
function pingTest(type, oIndex)
{
	ip = arrDev[oIndex]._ip;
	var page = "./jt_device_webtopo_from1.jsp?device_id=" + ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra,600, 400);
}

//ATMF5LOOP检测
function ATMTest(type, oIndex)
{
	ip = arrDev[oIndex]._ip;
	var page = "./jt_device_webtopo_from2.jsp?device_id=" + ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra,600, 400);
}

//重启设备
function deviceReboot(type, oIndex)
{
	ip = arrDev[oIndex]._ip;
	var page = "./jt_device_webtopo_from3.jsp?device_id=" + ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra,600, 400);
}

//恢复出厂设置
function deviceReset(type, oIndex)
{
	ip = arrDev[oIndex]._ip;
	var page = "./jt_device_webtopo_from4.jsp?device_id=" + ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra,600, 400);
}

//DSL检测
function DSLTest(type, oIndex)
{
	ip = arrDev[oIndex]._ip;
	var page = "./jt_device_webtopo_from5.jsp?device_id=" + ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra,600, 400);
}

function InitNetRapidMenu()
{

	var str="";
	var menuid="";


	for(var i=0;i<netMenu.length;i++)
	{
		if(chkPremession(netMenu[i]))
		{

			menuid=netMenu[i].replace("HOST_","");
			menuid=menuid.replace("USER_","");
			menuid=menuid.replace("SERVICE_","");
			str="document.all(\""+menuid+"\")";
			str = eval(str);
			if(str != null)	str.style.display="";

		}
	}
}

//显示主机对象的属性
function HOST_ShowObjectAttr(type,oIndex)
{
	var dxbh=arrDev[oIndex]._ip;
	//增加了用户视图.用户视图中包括网络设备(ip=1/dev/device_id)和主机设备(2/dxbh)
	//用户视图 需要将dxbh进行处理 dxbh=2/1422-->dxbh=1422
	//if(ViewID == 4 && dxbh.indexOf("2/") == 0 && dxbh.length > 2){
	//	dxbh = dxbh.substr(2);
	//}
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	page="HOST_ManagerHostObject.jsp?action=view&dxbh="+dxbh+"";
	newWin(page,otherpra,550,280);

}


//编辑主机对象的属性
function HOST_EditObjectAttr(type,oIndex)
{
	var dxbh=arrDev[oIndex]._ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	page="HOST_ManagerHostObject.jsp?action=edit&dxbh="+dxbh+"";
	newWin(page,otherpra,550,280);


}

//删除主机对象
function HOST_DelObject(type,oIndex)
{
	if(!window.confirm("您确认要删除您选择的主机？"))
	{
		return;
	}

	var tmp=new String(oIndex);
	var o_array=tmp.split(",");
	var ids="";
	for(var i=0;i<o_array.length;i++)
	{
		if(i==0)
		{
			ids=arrDev[o_array[i]]._ip;
		}
		else
		{
			ids +=","+arrDev[o_array[i]]._ip;
		}
	}
	var page="HOST_Operation.jsp?action=delete&objs="+ids+"&refresh_id="+arrPObjectID+"";
	document.all("childFrm").src=page;
}

//主机视图--主机性能配置
function HOST_perfConfig(type, oIndex)
{
	var dxbh = arrDev[oIndex]._ip;
	var title = arrDev[oIndex]._title;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	page = "HOST_PerfConfig.jsp?dxbh=" + dxbh + "&title=" + title;
	newWin(page,otherpra,600,550);
}

//主机视图--主机性能查看
function HOST_perfView(type, oIndex)
{
	var dxbh = arrDev[oIndex]._ip;
	var title = arrDev[oIndex]._title;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	var page = "HOST_PerfView.jsp?dxbh=" + dxbh + "&title=" + title;
	newWin(page,otherpra,500,310);
}

//主机视图－告警配置
function HOST_warnConfig(type,oIndex)
{
	var dxbh=arrDev[oIndex]._ip;
	var title= arrDev[oIndex]._title;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	page = "HOST_HostObjectWarn.jsp?dxbh=" + dxbh + "&dxmc=" + title;
	newWin(page,otherpra,600,380);
}

//打开拓扑图
function openTopo(type,id){
	var page = "./openTopoView.jsp?id="+ id +"&oper=open&type="+ type +"&tt="+ new Date().getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	newWin(page,otherpra,300,130);
}

//重新发现拓扑图
function NewAllTopo(type)
{
	var page = "./SelectGathers.jsp?oper=reset&type="+ type +"&tt="+ new Date().getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	newWin(page,otherpra,400,130);
}


//增量发现拓扑图
function AddTopo(type)
{
	var page = "./SelectGathers.jsp?oper=add&type="+ type +"&tt="+ new Date().getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	newWin(page,otherpra,400,130);
}


//关联客户信息
function SetCustomInfo(type,oIndex)
{
	var obj_id=arrDev[oIndex]._id;
	var name=arrDev[oIndex]._title;
	var page = "./webtopo_setCustomID.jsp?obj_id=" + obj_id + "&title=" + name;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	newWin(page,otherpra,400,130);
}


//拓扑结构保存
function SaveTopo(type)
{
	my_movedObjs();
	var page="webtopo_TopOper.jsp?type="+type;
	document.all("childFrm").src=page;
}


//移动webtopo网元
function my_movedObjs()
{
	//通知MasterControl 本层移动对象
	var param = getMovedObjs();
	//alert("param :" + param);
	if(param != null && param != ""){
		var XMLSender = new ActiveXObject("Microsoft.XMLHTTP" );
		XMLSender.Open("POST",'modifyObjectsPosition.jsp',false);
		//var param = parent.getMovedObjs();
		XMLSender.send(param);
		XMLSender = null;
	}
}

//导出拓扑图-add by shenkejian 2007-2-7
function SaveImg(topoType){
	//移动网元到新位置
	movedObjs(getMovedObjs(),topoType);
	var pid = arrPObjectID;
	outTopoImage(pid,topoType);
}

//导出选择的拓扑设备信息
function SaveImgSelected(topoType){
	//移动网元到新位置
	movedObjs(getMovedObjs(),topoType);
	var pid = arrPObjectID;
	if (arrCurDrag != null && arrCurDrag.length > 0){
		var selDev = "";
		for (var i=0;i<arrCurDrag.length;i++){
			selDev += arrDev[arrCurDrag[i]._curDragObjID]._id + "-";
		}
		var page = "download_img.jsp?pid="+ pid +"&topoType="+ topoType + "&selDev=" + selDev;
		document.all("childFrm").src = page;
		//var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
		//newWin(page,otherpra,0,0);
	}
	else{
		alert('请选择需要导出的设备！');
		return false;
	}
}

//导出拓扑图
function outTopoImage(pid,topoType)
{
	var page = "download_img.jsp?pid="+ pid +"&topoType="+ topoType;
	document.all("childFrm").src = page;
	//var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	//alert(page);
	//newWin(page,otherpra,0,0);
}

//导入拓扑图
function importTopo(type,oIndex)
{
	var x = event.clientX + document.body.scrollLeft-curDisX;
	var y = event.clientY+document.body.scrollTop-curDisY;
	SynOperator(function(){
		var page="importTopo!importForm.action?pid="+arrPObjectID+"&x="+x+"&y="+y;
		var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,height=310,width=640,top=200,left=220";
		window.open(page,"",otherpra);
	});
}
function SynOperator(fun){
	setTimeout(fun,1);
}
//移动webtopo网元
function movedObjs(param,topoType)
{
	//通知MasterControl 本层移动对象
	//var param = parent.getMovedObjs();
	//alert(param);
	if(param != null && param != ""){
		var XMLSender = new ActiveXObject("Microsoft.XMLHTTP" );
		//topoType==5 vpn视图
		if(topoType == "5"){
			XMLSender.Open("POST",'vpn_modifyObjectsPosition.jsp',false);
		}else{
			XMLSender.Open("POST",'modifyObjectsPosition.jsp',false);
		}

		//var param = parent.getMovedObjs();
		XMLSender.send(param);
		XMLSender = null;
	}
}

function CutNet(type,oIndex)
{
	var tmp=new String(oIndex);
	var o_array=tmp.split(",");
	var ids="";
	curPasteObj=new Array(o_array.length);
	for(var i=0;i<o_array.length;i++)
	{
		curPasteObj[i]=arrDev[o_array[i]];
	}
}

//确认设备
function ConfirmDevice(type,oIndex)
{
	var tmp=new String(oIndex);
	var o_array=tmp.split(",");

	var ids="";
	for(var i=0;i<o_array.length;i++)
	{
		if(i==0)
		{
			ids=arrDev[o_array[i]]._id;
		}
		else
		{
			ids +=","+arrDev[o_array[i]]._id;
		}
	}

	var page="confirmDevice.jsp?id="+ids+"&refresh_id="+arrPObjectID;
	//alert(page);
	document.all("childFrm").src=page;

}

function changeTitle(){

	var idList = getAllDeviceID();

	//var page="getDeviceTitle.jsp?idList="+idList+"&titleStyle="+document.all('titleStyle').value+"&refresh="+ new Date().getTime();
	//document.all("getTitle").src=page;

	var xmlHttp=new ActiveXObject("Microsoft.XMLHttp");
	xmlHttp.open("POST","./getDeviceTitle.jsp",false);
	xmlHttp.setRequestHeader("content-type","application/x-www-form-urlencoded");
	xmlHttp.send("idList="+idList+"&titleStyle="+document.all('titleStyle').value+"&refresh="+ new Date().getTime());
	var ret = xmlHttp.responseText;
	setTitle(ret);
}

function setTitle(deviceStr){
	setAllDeviceID(deviceStr);
}

//刷新topo图
function refreshTopo(){
	SynOperator(function(){
		document.all("childFrm").src="getChildTopo.jsp?pid="+arrPObjectID + "&refresh=" + new Date().getTime();
		//document.all("childFrm2").src = "refreshDeviceStatus.jsp?tt=" + new Date().getTime();
	});
}

function showDeviceRoute(type,oIndex){
	var ip=arrDev[oIndex]._ip;
	var page="getDeviceRounteInfo.jsp?device_id="+ip;
	var otherpra="channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,750,500);
}

//获取vlan信息
function getVlanInfo(type,oIndex){
	var id=arrDev[oIndex]._ip;
	var page = "../bbms/GetSnmpInfo!getVlanInfo.action?device_id="+ id +"&tt="+ new Date().getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,400,230);
}

//获取DHCP信息
function getDhcpInfo(type,oIndex){
	var id=arrDev[oIndex]._ip;
	var page = "../bbms/GetSnmpInfo.action?device_id="+ id +"&tt="+ new Date().getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,400,230);
}

//获取wlan信息
function getwlanInfo(type,oIndex){
	var id=arrDev[oIndex]._ip;
	var page = "webtopo_wlanInfo.jsp?device_id="+ id +"&tt="+ new Date().getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,400,230);
}
