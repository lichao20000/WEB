var head_h = 79;
var indicator = 8;
var left_w = 205;
var oInterval="";
var voInterval2="";
//�澯�����ļ���
var VoiceName="null";
//��loop��0 ֻ����һ�� 1 һֱ���� �൱��false��true��
var VoiceLoop="0";
//�Ƿ��Զ�����
var VoiceAuto = "true";
var isSendVoice = false;
//�Ƿ�ˢ�¸澯��Ϣ,Ĭ��Ϊfalse,��û�и澯��Ϣ. Add by Hemc 2006-10-25
var warnFlag = true;
//�澯rt_refresh.jsp/rt_refreshAll.jspҳ����JS��ֵ
var idRefreshAlarmNumMain = "";//�ο�������JS��refreshAlarmNumMain����
var gatherIdStr="";             // gather_id��¼ �澯��Ϣ�е� ���� ��Ϣ���� rightclick()�����л�ã�
//Add by HEMC 2006-12-19 ����XMLHttp���� ��function webtopoSearchDevice
//js�ļ�XMLHttp.js
var _xmlHttp = null;
function initialize(warn_num){
	//������ͼ
	drawTopo();
	//���ó�ʼ����һЩ����
	reset();
	//��ʼ��������ͼ�Ŀ�ݲ˵�
	InitNetRapidMenu();
	if(!ISVIP){
		//��ʼ���澯��һЩ������Ϣ
		initLV(warn_num);
	}
	//��ʼ���澯�������Ϣ
	initParamToSession();
}
//�澯�ֶ�ˢ�� Add by Hemc
function RefreshByHand(){
	var url = "vpn_rt_refresh.jsp?updateTime="+updateTime+"&isvip="+ISVIP+"&maxid="+ oList.getMaxId() + "&user_id=" + topouser_id[topouser_id.length-1] +"&refresh="+(new Date()).getTime();
	document.all("childFrm").src = url;
}
//�澯�Զ�ˢ�� Add by Hemc
function setAutoRefresh(obj){
	var _checked = obj.checked;
	//���ñ�־λ
	warnFlag = _checked;
	if(warnFlag){
		//��������ˢ��Ƶ��
		fnStartInterval();
	}else{
		fnStopInterval();
	}
}
function initImageSize(){
	document.all("childFrm").src = "getTopoImageSize.jsp?refresh="+(new Date()).getTime();
}

function initParamToSession(){
	//ֹͣ�澯����ش���ѭ��
	fnStopInterval();
	if(allObjIds==null) allObjIds="";
	document.initForm.curLayerObjList.value = allObjIds;
	document.initForm.submit();

	//ÿ�ν�webtopoʱ�����豸���澯
	removeAllWarnMain();
	refreshAlarmNumMain();
}

function fnStartRefresh(){
	//����Ѿ�������Ϊ�ֶ�ˢ��,��ֱ�ӷ���.
	if(warnFlag == false) return ;
	refreshAlarm();
	//refreshVoice();
	//fnStartInterval();
}

function refreshVoice(){
	var page = "ManagerWarnVoice.jsp?name="+VoiceName+"&loop="+VoiceLoop+"&autostart="+VoiceAuto;
	if(isSendVoice && VoiceName!="null"){
		document.all("childFrm2").src = page;
	}
}

function refreshAlarm(){
	var url = "vpn_rt_refresh.jsp?updateTime="+updateTime+"&isvip="+ISVIP+"&maxid="+ oList.getMaxId() + "&user_id=" + topouser_id[topouser_id.length-1] +"&refresh="+(new Date()).getTime();
	document.all("childFrm").src = url;
}
//���ø澯����
function ConfigWarnVoice(){
	var page = "config_warn_voice.jsp";
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,800,600);
}

//���ø澯���˹���
function ConfigWarnRule(){
	var page = "webtop_RuleListForm.jsp";
	window.open(page);
}

//������ӽڵ�
function AddSeeds(){
	var page = "vpn_topoSeeds_add.jsp";
	window.open(page);
}

//�鿴���и澯��Ϣ
function ViewAllWarn(){
	var page = "vpn_webtop_AllWarn.jsp";
	window.open(page);
}
function fnStartInterval(){
	if(oInterval==""){
		oInterval=window.setInterval("refreshAlarm()",60000);
	}
	else{
		fnStopInterval();
	}

	if(voInterval2=="")
	{
		voInterval2 =window.setInterval("refreshVoice()",10000);
	}

}

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

//��֤�û�Ȩ��
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


//��֤�豸�ͺ�
function chkDeviceType(arrType,type,pid){
	//���Ϊȫ�ֲ˵��Ҳ�Ϊ�澯�˵�
	if(arrType=="" && type != "-3" && pid != "null")
	{
		return true;
	}
	var tmp=arrType;
	//��� 0:����,2:�û���,3:�û�,5:�������˽ڵ�,6 VPN��ͼ
	if(tmp=="0"&&(parseInt(type,10)==0||parseInt(type,10)==2||parseInt(type,10)==3||parseInt(type,10)==5||parseInt(type,10)==6)){
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
	//��Զ����Ԫ����
	else if(tmp=="***" && ((new String(type)).length>0 && parseInt(type,10)>=0))
	{
		return true;
	}
	else if(tmp=="***1" && ((new String(type)).length>0 && parseInt(type,10)>=0))
	{
		if(curPasteObj==null)
		{
			return false;
		}
		else
			return true;
	}
	//��Ե�����Ԫ����
	else if(tmp=="*" && ((new String(type)).length==0 || parseInt(type,10)>=0))
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
	//����豸�Ĳ���
	else if(tmp=="device" && parseInt(type,10)==1)
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
	//�����û���
	else if(tmp=="device2" && parseInt(type,10)==2)
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
	//�����û�
	else if(tmp=="device3" && parseInt(type,10)==3)
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
	//���������
	else if(tmp=="device4" && parseInt(type,10)==4)
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
	//��������ڵ�
	else if(tmp=="device5" && parseInt(type,10)==5)
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
	//��·
	else if(tmp=="link" && (new String(type)).length==0)
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
	else
	{

		//������λ����ض��豸���͵Ĳ˵�
		tmp = ","+ arrType +",";

		if(tmp.indexOf(","+type+",") == -1)
			return false;
		else
			return true;
	}
}

/*
 *�����豸id�ַ����ж��û���ͼ���豸����
 *�����豸��4/1/dev/1317/0  4/1/dev/��ͷ
 *�����豸��4/2/10/0        4/2/��ͷ
 *return 1:������ͼ�豸 2��������ͼ�豸 0��δ֪
 */
function checkDeviceViewType(device_id){
	if(device_id == null || device_id == "") return 0;
	if(device_id.indexOf("4/1/dev/") != -1)
		return 1;
	else if(device_id.indexOf("4/2/") != -1)
		return 2;

	return 0;
}
//�����ҽ��˵�
var RM_Menu = new CMenu("RM_Menu");
function createRMenu(type,pid){
	var n=0;
	RM_Menu.menu.length=0;
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
	//����ǰ�澯ID���˵�ϵͳ,arrObjectID ����coolmenu.js�����,smf
	arrObjectID = oList.lvdata[index-1][0]._value;
	//alert("rightclick�д����˵�");

	gatherIdStr= oList.lvdata[index-1][13]._value ; //��÷����澯��������Ϣ, ��ֵ�� ȫ�ֱ��� gatherIdStr
	//alert("�澯gather_id ��"+gatherIdStr);
	//alert("ttttt"+RM_Menu);
	//if(RM_Menu.length=="undefined")

  //alert("index is: "+index);                 //index���� �澯��Ϣ�� ��ǰoList�б��е� ����
 // alert("arrObjectID is: "+ arrObjectID);      //arrObjectID ���� �澯ID serialno@1/dev/device_Coding
	createRMenu("-3",-1);
	showRightMenu();

	return;
}

function showRightMenu(){
	//��ʾ֮ǰ�����Ƚ�ԭ�еĲ�ɾ����
	clearRightMenu();
	//��ͼ
	drawSubMenu(RM_Menu);
	//��ʾ�˵�
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
alarmLevel[0] = new Array("0","����澯");
alarmLevel[1] = new Array("1","�¼��澯");
alarmLevel[2] = new Array("2","����澯");
alarmLevel[3] = new Array("3","��Ҫ�澯");
alarmLevel[4] = new Array("4","��Ҫ�澯");
alarmLevel[5] = new Array("5","���ظ澯");


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

//��ʾ����
function showFlux(type,oIndex)
{
	//ip=arrDev[oIndex]._ip;
	var device_id=arrDev[oIndex]._id;
  device_id = device_id.split("/");
	page = "webtop_liuliang.jsp?device_id="+device_id[2]+"&refresh="+(new Date()).getTime()+"&type="+2;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,800,600);
}
//��ʾQOS����
function showQos(type,oIndex)
{
	var device_id=arrDev[oIndex]._id;
    device_id = device_id.split("/");
	page = "webtop_QosLiuliang.jsp?device_id="+device_id[2]+"&refresh="+(new Date()).getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,800,600);
}
//��ʾQOS��������
function showQosQueue(type,oIndex)
{
	var device_id=arrDev[oIndex]._id;
    device_id = device_id.split("/");
	page = "webtop_QosQueueLiuliang.jsp?device_id="+device_id[2]+"&refresh="+(new Date()).getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,800,600);
}

//��ʾQOS������Ϣ
function showQosConfigInfo(type,oIndex)
{
	var tmp_id=arrDev[oIndex]._id.split("/");
	var tmp_title=arrDev[oIndex]._title;
	page = "showDevQOSConfigInfo.jsp?device_id="+tmp_id[2]+"&title="+tmp_title+"&refresh="+(new Date()).getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,800,600);
}

//��ʾʵʱ������Ϣ
function showRTConfigInfo(type,oIndex)
{
	var tmp_id=arrDev[oIndex]._id.split("/");
	var tmp_title=arrDev[oIndex]._title;
	page = "vpn_realtime_config_info.jsp?device_id="+tmp_id[2]+"&refresh="+(new Date()).getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,800,600);
}

//��ʾ������Ϣ
function showQueueConfigInfo(type,oIndex)
{
	var tmp_id=arrDev[oIndex]._id.split("/");
	var tmp_title=arrDev[oIndex]._title;
	page = "queue_check.jsp?device_id="+tmp_id[2]+"&title="+tmp_title+"&refresh="+(new Date()).getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,800,600);
}

//PowerPing����
function PowerPingConfig(type,oIndex)
{
	page = "PowerPingConfig.jsp";
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,800,600);
}

//��ʾ����
function showPmee(type,oIndex)
{
	//ip=arrDev[oIndex]._ip;
	var device_id=arrDev[oIndex]._id;
  device_id = device_id.split("/");
	page = "webtop_xinnen.jsp?device_id="+device_id[2]+"&refresh="+(new Date()).getTime()+"&type="+2;;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,600,300);
}

//�豸������·������ʾ
function ConfigLinkData(type,oIndex)
{
   var device_id=arrDev[oIndex]._id;
   device_id = device_id.split("/");
   var tmp_uid = topouser_id[topouser_id.length-1].split("/");
   page = "vpn_deviceRelateLink.jsp?user_id="+tmp_uid[2]+"&device_id="+device_id[2];
   var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=1,toolbar=0,height=500,width=700,top=200,left=365";
   window.open(page,"relateLink",otherpra);   
}

//��ʾϵͳ��Ϣ
function showSysInfo(type,oIndex)
{
	//ip=arrDev[oIndex]._ip;
	var device_id=arrDev[oIndex]._id;
  device_id = device_id.split("/");
	page = "webtop_SysInfo.jsp?title=ϵͳ��Ϣ&className=VpnReadDeviceInfo&device_id="+device_id[2]+"&refresh="+(new Date()).getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	newWin(page,otherpra,850,680);
}

//��ʾ�˿���Ϣ
function showPortInfo(type,oIndex)
{
	//ip=arrDev[oIndex]._ip;
	var device_id=arrDev[oIndex]._id;
  device_id = device_id.split("/");
	page = "webtop_SysInfo.jsp?title=�˿���Ϣ&className=VpnReadDevicePort&device_id="+device_id[2]+"&refresh="+(new Date()).getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,1000,750);
}
// -----------------add by lizhaojun 2007-01-12---------webtopo����ping��telnet�˵�------->start
//ping ��������ַ
function pingIp(type,oIndex)
{
		//vpn��ͼ�豸����id�����ʽΪ��5/dev/device_id(��Ӧtab_gw_device��)
	var device_id=arrDev[oIndex]._id.split("/");
	ip=device_id[2];
	page = "webtop_ping.jsp?&device_id="+ ip +"&action=ping" ;
	document.all("childFrm").src = page;
}

//telnet ��������ַ
function telnetLink(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
	page = "webtop_ping.jsp?&device_id="+ip+"&action=telnet" ;
	document.all("childFrm").src = page;
}
// -----------------add by lizhaojun 2007-01-12---------webtopo����ping��telnet�˵�------>-end
//��ʾ�豸�澯
function showDeviceWarn(type,oIndex)
{
	//vpn��ͼ�豸����id�����ʽΪ��5/dev/device_id(��Ӧtab_gw_device��)
	var device_id=arrDev[oIndex]._id.split("/");
	ip=device_id[2];

	var level=arrDev[oIndex]._level;
	if(false)//(level=="-1")
	{
		alert("���豸��ʱû��һ�㼶�����ϸ澯!");
	}
	else
	{
		page = "webtop_viewwarn.jsp?isvpn=true&type=device&device_id="+ip+"&level="+level+"&refresh="+(new Date()).getTime();
		var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
		newWin(page,otherpra,800,400);
	}
}

//��ʾ���и澯
function showAllDeviceWarn()
{
	page = "webtop_viewwarn.jsp?type=all&refresh="+(new Date()).getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,800,600);

}


//�¿�����
function newWin(page,otherpra){
	var w		= 750;
	var h		= 600;
	var l		= (document.body.clientWidth-w)/2;
	var t		= (document.body.clientHeight-h)/2;

	window.open(page,"","left="+l+",top="+t+",width="+w+",height="+h+","+otherpra);
}

function newWin(page,otherpra,w,h)
{
	var l		= (document.body.clientWidth-w)/2;
	var t		= (document.body.clientHeight-h)/2;
	window.open(page,"","left="+l+",top="+t+",width="+w+",height="+h+","+otherpra);
}


//��ʾ�����豸
function ViewDevice(type)
{
	isDisplayAll=type;
	viewTopo();
}


//�����豸
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

//ȡ�������豸
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

function showLinkInfoVpn(type,oIndex)
{
	var LinkObj		= arrLink[oIndex];
	var linkid		= LinkObj._id;
	_from_obj		= arrDev[findDevObjByID(LinkObj._from)];
	_to_obj			= arrDev[findDevObjByID(LinkObj._to)];
	var _from_title = _from_obj._title;
	var _to_title   = _to_obj._title;

	//vpn��ͼ�豸����id�����ʽΪ��5/dev/device_id(��Ӧtab_gw_device��)
	//var device_id	=_from_obj._id.split("/");
	//device_id		= _to_obj._id.split("/");

	var page        = "./webtop_LinkInfo.jsp?user_id="+ topouser_id[topouser_id.length-1] + "&linkid=" + linkid + "&from=" + _from_title + "&to=" + _to_title;
	window.open(page,"","left=120,top=130,width=500,height=400,resizable=yes,scrollbars=yes");
}

function showLinkInfo(type,oIndex)
{
	var LinkObj = arrLink[oIndex];
	var linkid  = LinkObj._id;
	_from_obj	= arrDev[findDevObjByID(LinkObj._from)];
	_to_obj     = arrDev[findDevObjByID(LinkObj._to)];
	var _from_title = _from_obj._title;
	var _to_title   = _to_obj._title;
	var _from_id    = _from_obj._id;
	var _to_id      = _to_obj._id;	
	var page        = "../Visualman/vpn_flux_list.jsp?user_id="+ topouser_id[topouser_id.length-1] + "&linkid=" + linkid + "&from=" + _from_title + "&to=" + _to_title + "&from_ip=" + _from_id + "&to_ip=" + _to_id;
	window.open(page,"","left=70,top=130,width=800,height=800,resizable=yes,scrollbars=yes");
}


//modified by ll
function editLinkInfo(type,oIndex)
{
	//alert(oIndex);

	////////////////////////////webtopo����/////////////////////////////
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
	//�ж��Ƿ�Ϊ��Ԫ����  _type�����0�Ļ��������Σ�����Ķ�����Ԫ
	//�豸from �� to ʹ�á�,������������ж��from �� to����ʹ�á�;������
	//if(_from_type == 0){ // from��Ϊ����

	//}else if(_to_type == 0){//to��Ϊ����

	//}else{//���˶���������  �豸
	//	param = _from_id + "," + _to_id;
	//}
	var page = "../Visualman/EditLinkInfo.jsp?linkid=" + linkid + "&from=" + _from_title + "&to=" + _to_title;
	window.open(page);
}

//link css.
//modified by yanhj,2006-10-13.
function editLinkCss(type,oIndex) {
//	alert(oIndex);
	var LinkObj = arrLink[oIndex];
	var link_id = LinkObj._id;
	_from_obj	= arrDev[findDevObjByID(LinkObj._from)];
	_to_obj     = arrDev[findDevObjByID(LinkObj._to)];
	var _from_title = _from_obj._title;
	var _to_title = _to_obj._title;
	var page = "./link_css_edit_form.jsp?link_id=" + link_id + "&from=" + _from_title + "&to=" + _to_title;
	window.open(page,"�༭��·��ʽ","left=20,top=20,width=450,height=250,resizable=no,scrollbars=yes");
}

//�����豸
function CutNet(type,oIndex)
{
	var tmp = new String(oIndex);
	var o_array = tmp.split(",");
	var ids="";

	curPasteObj = new Array(o_array.length);
	for(var i = 0;i < o_array.length;i++)
	{
		curPasteObj[i] = arrDev[o_array[i]];
	}
}

//ճ���豸
function PasteNet(type,oIndex)
{
	if(curPasteObj == null || curPasteObj.length == 0)
	{
		window.alert("��ѡ�����豸");
		return;
	}

	if(arrDev[oIndex]._id == curPasteObj[0]._pid)
	{
		window.alert("��Ҫճ���Ķ����Ѿ�����ѡ��������ڣ�");
		return;
	}

	var ids="";
	for(var i = 0;i < curPasteObj.length;i++)
	{
		if(i == 0)
		{
			ids = curPasteObj[i]._id;
		}
		else
		{
			ids += ","+curPasteObj[i]._id;
		}

	}

	var topo_user_id = topouser_id[topouser_id.length-1];
	var page = "vpn_DeviceOperation.jsp?action=paste&objs="+ ids +"&refresh_id="+ arrPObjectID +"&parent_id="+
		arrDev[oIndex]._id +"&topouser_id="+ topo_user_id +"&topoType="+ type;
	//alert(page);
	document.all("childFrm").src = page;

}

//ȡ������
function CancelCut(type,oIndex)
{
	curPasteObj=null;
}

/*
	ɾ����Ԫ����
	type---topo��ͼ����
	oIndex----��ԪID
*/
function DeleteObj(type,oIndex)
{
	//alert(arrObjectID);
	if(!window.confirm("��ȷ��Ҫɾ����ѡ��Ķ���"))
	{
		return;
	}

	var tmp=new String(oIndex);
	var o_array=tmp.split(",");
	var ids="";
	var topo_user_id = topouser_id[topouser_id.length-1];

	//�������/����ֻȡ/�����ֵ
	//alert(topo_user_id);
	if(topo_user_id.toString().indexOf("/")>0){
		var id_array = topo_user_id.split("/");
		topo_user_id = id_array[id_array.length-1];
	}

	for(var i = 0;i < o_array.length;i++)
	{
		if(i == 0)
		{
			//alert(o_array[i]+" : "+arrDev.length);
			ids = topo_user_id +"|"+ arrDev[o_array[i]]._id;
		}
		else
		{
			ids += ","+ topo_user_id +"|"+ arrDev[o_array[i]]._id;
		}
	}

	var page="vpn_DeviceOperation.jsp?action=delete&objs="+ ids +"&refresh_id="+ arrPObjectID +"&topoType="+ type;
	document.all("childFrm").src=page;
}


/**
	ɾ����·����
	type---topo��ͼ����
	oIndex----��·ID
*/
function DeleteLinkObj(type,oIndex)
{

	if(!window.confirm("��ȷ��Ҫɾ����ѡ�����·��"))
	{
		return;
	}

	var tmp = new String(oIndex);
	var o_array = tmp.split(",");
	var ids = "";

	var topo_user_id = topouser_id[topouser_id.length-1];

	//�������/����ֻȡ/�����ֵ
	//alert(topo_user_id);
	if(topo_user_id.toString().indexOf("/")>0){
		var id_array = topo_user_id.split("/");
		topo_user_id = id_array[id_array.length-1];
	}

	for(var i = 0;i < o_array.length;i++)
	{
		if(i == 0)
		{
			ids = topo_user_id +"|"+ arrLink[o_array[i]]._id;
		}
		else
		{
			ids += ","+ topo_user_id +"|"+ arrLink[o_array[i]]._id;
		}
	}

	var page = "vpn_DeviceOperation.jsp?action=delete&objs="+ids+"&refresh_id="+arrPObjectID+"&topoType="+ type;
	document.all("childFrm").src = page;
}


//��������
function ConfigFlux(type,oIndex)
{
	var device_id=arrDev[oIndex]._id;
  device_id = device_id.split("/");
	page="webtop_ConfigFlux.jsp?device_id="+device_id[2]+"&type="+2;
	var otherpra="channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	newWin(page,otherpra,750,500);
}
//QOS����
function ConfigQos(type,oIndex)
{
	var device_id=arrDev[oIndex]._id;
    device_id = device_id.split("/");
	page="webtop_ConfigQOS.jsp?device_id="+device_id[2];
	var otherpra="channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	newWin(page,otherpra,750,500);
}
//QOS��������
function ConfigQosQueue(type,oIndex)
{
	var device_id=arrDev[oIndex]._id;
    device_id = device_id.split("/");
	page="webtop_ConfigQOSQueueing.jsp?device_id="+device_id[2];
	var otherpra="channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	newWin(page,otherpra,750,500);
}
//��������
function ConfigPerf(type,oIndex)
{
	var device_id=arrDev[oIndex]._id;
	//vpn��ͼ�豸����id�����ʽΪ��5/dev/device_id(��Ӧtab_gw_device��)
	device_id = device_id.split("/");

	page = "pm_Config.jsp?device_id="+device_id[2]+"&type="+2;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,height=500,width=700,top=200,left=365";
	window.open(page,"�豸��������",otherpra);
}

//�豸��½��Ϣ����
function DeviceLoginConfig(type,oIndex)
{
	var device_id=arrDev[oIndex]._id;
	//vpn��ͼ�豸����id�����ʽΪ��5/dev/device_id(��Ӧtab_gw_device��)
	device_id = device_id.split("/");

	page = "vpn_config_name_and_pw.jsp?device_id="+device_id[2]+"&type="+2;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,height=500,width=700,top=200,left=365";
	window.open(page,"�豸��������",otherpra);
}

//IPCHECK����
function ConfigIpCheck(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
	page = "single_DevIPcheck.jsp?device_id="+ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,height=160,width=300,top=200,left=365";
	window.open(page,"IP״̬����",otherpra);

}

//��ҵ���·������
function ConfigPathID(type,oIndex)
{
	//alert(arrDev[oIndex]._iscpe);
	ip=arrDev[oIndex]._ip;
	var objid=arrDev[oIndex]._id;
	page="config_cpe.jsp?objid="+objid+"&device_id="+ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,600,320);
}

//�鿴��ǰ�豸�ĵ�·ͼ
function showCircuit(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
	page="view_cpe.jsp?device_id="+ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,600,200);

}

//��ʾһ̨�豸����ϸ��Ϣ
function showDevDetail(type,oIndex)
{
	var device_id=arrDev[oIndex]._id;
	//vpn��ͼ�豸����id�����ʽΪ��5/dev/device_id(��Ӧtab_gw_device��)
	device_id = device_id.split("/");
	var page="../Resource/DeviceShow.jsp?device_id="+device_id[2];
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,600,580);
}

//VPN��������
function ConfigVPNBill(type,oIndex)
{
	//alert("ConfigBill in");
	var device_id=arrDev[oIndex]._id;
	//vpn��ͼ�豸����id�����ʽΪ��5/dev/device_id(��Ӧtab_gw_device��)
	device_id = device_id.split("/");
//	for(var x in device_id){
//		alert(device_id[x]);
//	}
	page = "vpn_bill_Config.jsp?device_id="+device_id[2];

	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=1,toolbar=0,height=500,width=700,top=200,left=365";
	window.open(page,"VPN��������",otherpra);
}

//QOS��������
function ConfigQOSBill(type,oIndex)
{
	//alert("ConfigBill in");
	var device_id=arrDev[oIndex]._id;
	//vpn��ͼ�豸����id�����ʽΪ��5/dev/device_id(��Ӧtab_gw_device��)
	device_id = device_id.split("/");
//	for(var x in device_id){
//		alert(device_id[x]);
//	}
	page = "vpn_Qos_Config.jsp?device_id="+device_id[2];

	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=1,toolbar=0,height=500,width=700,top=200,left=365";
	window.open(page,"QOS��������",otherpra);
}

//QOS����ִ��״̬
function ConfigQOSBillState(type,oIndex)
{
	//alert("ConfigBill in");
	var device_id=arrDev[oIndex]._id;
	//vpn��ͼ�豸����id�����ʽΪ��5/dev/device_id(��Ӧtab_gw_device��)
	device_id = device_id.split("/");
//	for(var x in device_id){
//		alert(device_id[x]);
//	}
	page = "vpn_Qos_Config_state.jsp?device_id="+device_id[2] +"&tt="+ new Date().getTime();

	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=1,toolbar=0,height=500,width=700,top=200,left=365";
	window.open(page,"QOS����ִ��״̬",otherpra);
}

//VPNPing����
function ConfigVPNPing(type,oIndex)
{
	//��õ�ǰ���������û����
	//��Ÿ�ʽΪ 5/u/int
	var tmp_uid = topouser_id[topouser_id.length-1].split("/");

	page = "vpn_pingtask.jsp?user_id="+tmp_uid[2];

	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=1,toolbar=0,height=500,width=700,top=200,left=365";
	window.open(page,"QOS��������",otherpra);
}

function swapNewObj()
{
	isCreateLink=false;
	//�ж������ǰ״̬�ǲ�����״̬
	if(!isCreateNet)
	{
		document.all("Image11").src="./images/topban_1_lock.gif";
		document.all("Image11").title="�������";
		isCreateNet=true;

	}
	else
	{
		document.all("Image11").src="./images/topban_1.gif";
		document.all("Image11").title="��������";
		isCreateNet=false
	}
}

function swapNewLink(){
	isCreateNet=false;

	if(!isCreateLink)	{
		document.all("Image12").src="./images/topban_2_lock.gif";
		document.all("Image12").title="�������";
		isCreateLink=true;
	}
	else
	{
		document.all("Image12").src="./images/topban_2.gif";
		document.all("Image12").title="������·";
		isCreateLink=false;
	}
}

//ɾ���澯
function remove_Warn(type,oIndex){
	if(!window.confirm("��ȷ��Ҫɾ����ѡ��ĸ澯��")){
		return;
	}
	var page="./removeAlarmVpn.jsp?alarmStr="+oIndex;
	document.all("childFrm").src=page;
	document.all("oList").deleteRow();
}

//ȷ�ϸ澯
function ack_Warn(type,oIndex)
{
	if(!window.confirm("ȷ�ϸ澯��"))
	{
		return;
	}
	var page="./ackAlarmVpn.jsp?alarmStr="+oIndex;
	if(_xmlHttp == null) _xmlHttp = new XmlHRequest();
	_xmlHttp.setURL(page);
	//��XMLHttp��setReadyChange�����¼�����
	_xmlHttp.doPost(ack_Warn_Result);
	//document.all("childFrm").src=page;
}
function ack_Warn_Result(){
	if(_xmlHttp != null && _xmlHttp.getReady()){
		var result = _xmlHttp.getResult().toString();
		var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
        xmlDoc.loadXML(result);
        var node = xmlDoc.documentElement.selectSingleNode("//result");
		if(node!=null && node.text == 1){
			document.all("oList").deleteRow();
			alert("�澯ȷ�ϳɹ�");
		}else{
			alert("�澯ȷ��ʧ�ܣ������ԣ�");
		}
		xmlDoc = null;
	}
}
//��ѯ�澯
function query_Warn(type,oIndex)
{
	var page="./query_Warn.jsp?warnIndex="+oIndex+"&type="+type;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,height=310,width=640,top=200,left=220";
		window.open(page,"�澯��ѯ",otherpra);
}

// ȷ�ϸ澯�����
function ack_Store_Warn(type,oIndex) {
   // alert(" �� main_control : oIndex :"+oIndex);    //gather_id Ϊmain_control.js ��ȫ�ֱ�������rightclick( ) �л��
   // alert("gatherIdStr is :"+gatherIdStr);
	  var page="./ack_Store_Alarm.jsp?alarmStr="+oIndex+"&gatherId="+gatherIdStr;
	 document.all("childFrm").src=page;
}
//Add By Hemc 2006-12-11
//�澯�б���λ�豸����
//����topo.js�е�Dev_MouseDown����
//��λ���豸����
function findDevObjLocation(dev){
	if(dev == "-1") return false;
	var ln = -1;
	ln = findDevObjByID(dev);
	//����豸���Ϸ��б�����,ֱ�ӷ���.
	//if(findDragObjListByID(ln)) return;
	if(arrDev != null && arrDev[ln] != null){
		arrObjectID = ln;
		arrPObjectID = arrDev[ln]._pid;
		//������ѡ���豸����·
		findLinkObjByID(dev);
		//�����·ѡ��״̬
		clearLinkBorder();
		arrCurDragLink = null;
		curDragLinkObj=null;
		//ɾ����ǰ��ѡ�еĶ���߿�
		clearDevBorder();
		arrDev[ln].drawBorder();
		focusDevObj(arrDev[ln]);
		//�����߿���豸���롺���϶���Ԫ�������须��
		arrCurDrag = null;
		arrCurDrag = new Array();
		arrCurDrag[0] = new DragObj(ln,curDragLinkFrom,curDragLinkTo);
	}
	else{
		//���������δ֪�豸�޷���λ
		if(dev == "5/dev/"){
			alert("δ֪�豸id�޷���λ(" + dev + ")");
		}else if(confirm("�澯Դ���ڵ�ǰ�㣬��Ҫ������豸��Topoͼ��(" + dev + ")")){
			GoTopoByDevID(dev,"5/u/0");
		}
	}
	return ;
}
//�澯�б����豸��λjs����
function findDevObj(type,oIndex){
	var $ = null;
	try{
		$ = window.opener == null ? this : window.opener;
	}catch(e){
		alert("WebTopo�����ѱ��رգ��޷���λ���豸��(" + e.description + ")");
		return false;
	}
	try{
		var _index = oIndex.indexOf("@");
		var dev = "5/dev/";
		if(_index != -1){
			dev = oIndex.substr(_index + 1);
		}
		$.findDevObjLocation(dev);
		$.focus();
		return true;
	}catch(e){
		alert("�޷��豸��λ!\n��������Ϊ��" + e.description);
	}
	return false;
}
//���豸�۽� Add By Hemc
function focusDevObj(devObj){
	try{
		var _$ = document.all;
		if(_$(devObj._id) != null){
			//_$(devObj._id).focus();
			idWebTopo.focus();
			//�ò�idWebTopo��������λ���豸
			idWebTopo.scrollTop = devObj._y;
			idWebTopo.scrollLeft = devObj._x;
		}
	}catch(e){
		window.alert("�޷��豸�۽�(focusDevObj" + e.description + ")");
	}
}

//���ݴ�����豸id(1/dev/222)�õ�topoͼ Add by HEMC 2006-12-15
function GoTopoByDevID(pid,topouser_id){
	//��ʱ���浱ǰTopoͼpid
	var tempArrPObjectID = arrPObjectID;
	//idWebTopo.src = "getTopoByIdXML.jsp?pid="+ pid;
	idWebTopo.src = "getTopoByIdXML.jsp?pid="+ pid+"&topoType=5&topouser_id=" + topouser_id;
	dispTopo();
	idWebTopo.innerHTML = "<v:rect id=selRect  style=\"DISPLAY: none; Z-INDEX:  255; POSITION:  absolute\" coordsize = \"21600,21600\" strokecolor = \"#10fc18\"><v:fill opacity = \"0\" ></v:fill></v:rect>";
	drawTopo();
	initParamToSession();
	//��ĳ�豸����topoͼʱ�����ܳ������κ��豸.
	//���ϴ�topoͼ��pid��ֵ��arrPObjectID���Ա��ܷ���topo
	/*
	if(arrDev == null){
		arrPObjectID = tempArrPObjectID;
		tempArrPObjectID = null;
	}
	*/
	//���豸���澯
	//removeAllWarnMain();
	//refreshAlarmNumMain();
	//��λ�豸
	findDevObjLocation(pid);
}

//������и澯���
//Add by HEMC 2006-12-15
function removeAllWarnMain(){
	if(arrDev != null){
		for(var i=0;i<arrDev.length;i++){
			arrDev[i].removeWarn();
		}
	}
}
//����̨�豸�豸���澯 Add by HEMC 2006-12-15
//idRefreshAlarmNum����Ҫ���豸���澯�ַ���
//�磺1/dev/222,15,4;1/dev/333,16,3(�豸id,�澯����,�澯����)
function refreshAlarmNumMain(){
	var s = idRefreshAlarmNumMain;
	if(s=="") return;
	var list = s.split(";");
	for(var i=0;i<list.length;i++){
		arr = list[i].split(",");
		ln = findDevObjByID(arr[0]);
		num = arr[1];
		level = arr[2];
		//Topoͼ�д����豸����
		if(typeof(arrDev[ln]) != "undefined"){
			arrDev[ln].drawWarn(num,level);
		}
	}
}
//�õ�web�Ͽؼ���ֵ��Ȼ���ٵ���webtopoSearchDevice����
function webtopoSearch(){
	if(_searchValue.value == "")
		alert("�������ѯ����!");
	else{
		webtopoSearchDevice(_searchValue.value,_typeSelect.value);
	}
}
/*
 *ʹ��XMLHttp����ȡ����(webtopo�豸��ѯ)
 *_type  1:device_ip 2:device_name 3:device_model
 *_value  ��Ҫ��ѯ��ֵ
 */
function webtopoSearchDevice(_value,_type){
	divSelectDevice.innerHTML = "���ڼ����豸....";
	var _url = "webtopo_SearchDevice.jsp?_value=" + _value + "&_type=" + _type;
	if(_xmlHttp == null) _xmlHttp = new XmlHRequest();
	_xmlHttp.setURL(_url);
	//��XMLHttp��setReadyChange�����¼�����
	_xmlHttp.doPost(setDeviceData);
}
//���������豸����������ݼ��ص�div��
function setDeviceData(){
	if(_xmlHttp != null && _xmlHttp.getReady()){
		divSelectDevice.innerHTML = _xmlHttp.getResult().toString();
	}
}
//���������豸��������������ݼ��ص�div��
function setDeviceTypeData(){
	if(_xmlHttp != null && _xmlHttp.getReady()){
		divSelectDevType.innerHTML = _xmlHttp.getResult().toString();
	}
}
//���������豸��������������ݼ��ص�div��
function setVenderData(){
	if(_xmlHttp != null && _xmlHttp.getReady()){
		divSelectVendor.innerHTML = _xmlHttp.getResult().toString();
	}
}
//webtopo�����豸 �������������豸
function changeSelectType(){
	divSelectDevice.innerHTML = "";
	//�������� �豸�ͺ�
	if(_typeSelect.value == "3"){
		//�ó��̡��豸�ͺ�div��ʾ
		divSelectVendor.style.display = "";
		divSelectDevType.style.display = "";
		//�����豸���Ͳ���ʱ���ı���Ͱ�ť����
		_searchValue.style.display = "none";
		btn_QueryDev.style.display = "none";
		divSelectDevType.innerHTML = "";
		divSelectVendor.innerHTML = "���س�������...";
		var _url = "webtopo_SearchDeviceType.jsp?type=0";
		if(_xmlHttp == null) _xmlHttp = new XmlHRequest();
		_xmlHttp.setURL(_url);
		//��XMLHttp��setReadyChange�����¼�����
		_xmlHttp.doPost(setVenderData);
	}else{
		_searchValue.style.display = "";
		btn_QueryDev.style.display = "";
		divSelectVendor.style.display = "none";
		divSelectDevType.style.display = "none";
	}
}
//ѡ���̣����ظó��̵��豸����
function changeVendorType(_value){
	divSelectDevice.innerHTML = "";
	if(_value != "-1"){
		var _url = "webtopo_SearchDeviceType.jsp?type=1&vendor_id=" + _value;
		divSelectDevType.innerHTML = "�����豸�ͺ�...";
		if(_xmlHttp == null) _xmlHttp = new XmlHRequest();
		_xmlHttp.setURL(_url);
		//��XMLHttp��setReadyChange�����¼�����
		_xmlHttp.doPost(setDeviceTypeData);
	}else{
		divSelectDevType.innerHTML = "";
	}
}
//ѡ���豸�ͺţ����ظ��豸���͵��豸
function changeDeviceType(_value){
	if(_value != "-1"){
		/*
		var _url = "webtopo_SearchDeviceType.jsp?type=2&device_model=" + _value;
		divSelectDevice.innerHTML = "���ڼ����豸....";
		if(_xmlHttp == null) _xmlHttp = new XmlHRequest();
		_xmlHttp.setURL(_url);
		//��XMLHttp��setReadyChange�����¼�����
		_xmlHttp.doPost(setDeviceData);
		*/
		webtopoSearchDevice(_value,3);
	}else{
		divSelectDevice.innerHTML = "";
		//divSelectDevice.style.display = "";
	}
}
//�û����뵽topo�����ò���
function setTopoLayer(_boolean){
	var parentid = "";
	//true:����Ĭ��topo�㣬false:��������Ĭ��topo�㡣
	if(_boolean){
		parentid = arrPObjectID;
	}

	if(parentid != null && parentid != "null"){
		var url = "setTopoLayer.jsp?parentid=" + parentid;
		if(_xmlHttp == null) _xmlHttp = new XmlHRequest();
		_xmlHttp.setURL(url);
		_xmlHttp.doPost(setParentResult);
	}else{
		alert("��ǰ�㸸����Ϊ�գ�����ʧ�ܣ�");
	}
}
//���ú� ���û���ʾʱ�����óɹ�
function setParentResult(){
	if(_xmlHttp != null && _xmlHttp.getReady()){
		var result = _xmlHttp.getResult().toString();
		//result = result == "true" ? "���óɹ�!":"����ʧ�ܣ�������!";
		alert("���óɹ�!");
	}
}

//�޸��豸����
function ModifyDeviceType(type, oIndex){
	//vpn��ͼ�豸����id�����ʽΪ��5/dev/device_id(��Ӧtab_gw_device��)
	var device_id=arrDev[oIndex]._id.split("/");
	ip=device_id[2];
	var serial = arrDev[oIndex]._type;
	var id = arrDev[oIndex]._id;
	var page = "./vpn_ModifyDeviceType.jsp?oIndex=" + oIndex + "&device_id=" + ip + "&type=" + serial + "&id=" + id;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra, 400, 200);
}

 //�޸��豸����
function ModifyDeviceAttribute(type, oIndex)
{
	//vpn��ͼ�豸����id�����ʽΪ��5/dev/device_id(��Ӧtab_gw_device��)
	var device_id=arrDev[oIndex]._id.split("/");
	ip=device_id[2];
	var serial = arrDev[oIndex]._type;
	var id = arrDev[oIndex]._id;
	var title=arrDev[oIndex]._title;
	var page = "./vpn_ModifyDeviceAttribute.jsp?oIndex=" + oIndex + "&title="+title+"&device_id=" + ip + "&id=" + id + "&type=" + serial;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra, 300, 200);
}


function InitNetRapidMenu()
{

	var str="";
	var menuid="";


	for(var i=0;i<netMenu.length;i++)
	{
		if(chkPremession(netMenu[i]))
		{
			//alert(netMenu[i]+" yes");
			menuid=netMenu[i].replace("HOST_","");
			menuid=menuid.replace("USER_","");
			menuid=menuid.replace("SERVICE_","");
			str="document.all(\""+menuid+"\")";
			eval(str).style.display="";

		}
	}
}

//��ʾ�������������
function HOST_ShowObjectAttr(type,oIndex)
{
	var dxbh=arrDev[oIndex]._ip;
	//�������û���ͼ.�û���ͼ�а��������豸(ip=1/dev/device_id)�������豸(2/dxbh)
	//�û���ͼ ��Ҫ��dxbh���д��� dxbh=2/1422-->dxbh=1422
	//if(ViewID == 4 && dxbh.indexOf("2/") == 0 && dxbh.length > 2){
	//	dxbh = dxbh.substr(2);
	//}
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	page="HOST_ManagerHostObject.jsp?action=view&dxbh="+dxbh+"";
	newWin(page,otherpra,550,280);

}

//��ʾ�û���Ϣ
function showUserInfo(type, oIndex){
	//vpn��ͼ�û�����id�����ʽΪ��5/u/Vpn_auto_id
	var tmp_id=arrDev[oIndex]._id.split("/");
	var tmp_title=arrDev[oIndex]._title;
	var page = "./showUserInfo.jsp?vpn_auto_id=" + tmp_id[2]+"&title="+tmp_title;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra, 800, 400);
}

//��ʾ�������Ϣ
function showAccessPointInfo(type, oIndex){
	//vpn��ͼ�û�����id�����ʽΪ��5/ap/link_auto_id
	var tmp_id=arrDev[oIndex]._id.split("/");
	var tmp_title=arrDev[oIndex]._title;
	var page = "./AccessPointInfo.jsp?link_auto_id=" + tmp_id[2]+"&title="+tmp_title;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra, 800, 400);
}

//�༭�������������
function HOST_EditObjectAttr(type,oIndex)
{
	var dxbh=arrDev[oIndex]._ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	page="HOST_ManagerHostObject.jsp?action=edit&dxbh="+dxbh+"";
	newWin(page,otherpra,550,280);


}

//ɾ����������
function HOST_DelObject(type,oIndex)
{
	if(!window.confirm("��ȷ��Ҫɾ����ѡ���������"))
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

//������ͼ--������������
function HOST_perfConfig(type, oIndex)
{
	var dxbh = arrDev[oIndex]._ip;
	var title = arrDev[oIndex]._title;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	page = "HOST_PerfConfig.jsp?dxbh=" + dxbh + "&title=" + title;
	newWin(page,otherpra,600,550);
}

//������ͼ--�������ܲ鿴
function HOST_perfView(type, oIndex)
{
	var dxbh = arrDev[oIndex]._ip;
	var title = arrDev[oIndex]._title;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	var page = "HOST_PerfView.jsp?dxbh=" + dxbh + "&title=" + title;
	newWin(page,otherpra,500,310);
}

//������ͼ���澯����
function HOST_warnConfig(type,oIndex)
{
	var dxbh=arrDev[oIndex]._ip;
	var title= arrDev[oIndex]._title;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	page = "HOST_HostObjectWarn.jsp?dxbh=" + dxbh + "&dxmc=" + title;
	newWin(page,otherpra,600,380);
}

//����ͼ����
function SaveTopo(type)
{
	var page="webtopo_TopOper.jsp?type="+type;
	document.all("childFrm").src=page;
}


//���·�������ͼ
function NewAllTopo(type)
{
	page = "./SelectGathers.jsp?oper=reset&type="+ type +"&tt="+ new Date().getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	newWin(page,otherpra,400,130);
}


//������������ͼ
function AddTopo(type)
{
	page = "./SelectGathers.jsp?oper=add&type="+ type +"&tt="+ new Date().getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	newWin(page,otherpra,400,130);
}


//�����ͻ���Ϣ
function SetCustomInfo(type,oIndex)
{
	var obj_id=arrDev[oIndex]._id;
	var name=arrDev[oIndex]._title;
	page = "./webtopo_setCustomID.jsp?obj_id=" + obj_id + "&title=" + name;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	newWin(page,otherpra,400,130);
}

//��õ�ǰPE�豸��Ϣ�ַ���
//�ַ�����ʽΪ device_id,title;device_id,title;device_id,title;.........................
function getTopoPEDev()
{
	var s = null;
	if(arrDev!=null){
		for(var x in arrDev){
			//��ԪΪ�豸ʱ
			if(arrDev[x]._type==1){
				if(s==null)
					s=arrDev[x]._ip+","+arrDev[x]._title;
				else
					s+=";"+arrDev[x]._ip+","+arrDev[x]._title;
			}
		}
		return s;
	}

	return null;
}

//��������ͼ-add by shenkejian 2007-2-7
function SaveImg(topoType){
	//�ƶ���Ԫ����λ��
	movedObjs(getMovedObjs(),topoType);

	//vpn������ͼ
	if(topoType == "5"){
		//����vpn������ͼ��ͼƬ
		var pid = arrPObjectID;
		//alert(pid);
		outTopoImage(topouser_id[topouser_id.length-1],pid,topoType);
	}else{
		//var page = "download_webtopo_img.jsp";
		//var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
		//newWin(page,otherpra,500,200);
		
	}
}

//����vpn����ͼ
function outTopoImage(topouser_id,pid,topoType)
{
	var page = "download_vpn_img.jsp?pid="+ pid +"&topouser_id="+ topouser_id +"&topoType="+ topoType;
	//document.all("childFrm").src = page;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	//alert(page);
	newWin(page,otherpra,0,0);
}

//�ƶ�webtopo��Ԫ
function movedObjs(param,topoType)
{
	//֪ͨMasterControl �����ƶ�����
	//var param = parent.getMovedObjs();
	//alert(param);
	if(param != null && param != ""){
		var XMLSender = new ActiveXObject("Microsoft.XMLHTTP" );
		//topoType==5 vpn��ͼ
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

//ˢ��VPNTOPOͼ
function refreshVPNTopo(){
	try{
		if(arrPObjectID != null && arrPObjectID != ""){
			idWebTopo.src = "getTopoByPidXML.jsp?pid="+ arrPObjectID +"&topouser_id=" + topouser_id[topouser_id.length-1] + "&topoType=5&tt="+ new Date().getTime();
			dispTopo();
			idWebTopo.innerHTML = "<v:rect id=selRect  style=\"DISPLAY: none; Z-INDEX:  255; POSITION:  absolute\" coordsize = \"21600,21600\" strokecolor = \"#10fc18\"><v:fill opacity = \"0\" ></v:fill></v:rect>";
			drawTopo();
			initParamToSession();
		}
	}catch(e){
		e.printStackTrace();
	}

}