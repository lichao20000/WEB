var head_h = 30;
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
var idRefreshAlarmNumLink = "";
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
	 //refreshAlarm();
	 refreshTopo();
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
	//fnStopInterval();
	if(allObjIds==null) allObjIds="";
	//alert(allObjIds);
	document.initForm.curLayerObjList.value = allObjIds;
	//alert(document.initForm.curLayerObjList.value);
	document.initForm.action="initStaticParam.jsp?refresh="+(new Date()).getTime();
	document.initForm.submit();

	//refreshAlarm();
}

//�豸������·������ʾ
function ConfigLinkData(type,oIndex)
{
   ip=arrDev[oIndex]._ip;
   page = "web_deviceRelateLink.jsp?device_id="+ip;
   var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=1,toolbar=0,height=500,width=700,top=200,left=365";
   window.open(page,"relateLink",otherpra);
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

//�豸��½��Ϣ����
function DeviceLoginConfig(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
	page = "vpn_config_name_and_pw.jsp?device_id="+ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,height=500,width=700,top=200,left=365";
	window.open(page,"�豸��½��Ϣ����",otherpra);
}

//VPN��������
function ConfigVPNBill(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
	page = "vpn_bill_Config.jsp?device_id="+ip;

	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=1,toolbar=0,height=500,width=700,top=200,left=365";
	window.open(page,"VPN��������",otherpra);
}

//VPNPing����
function ConfigVPNPing(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
	page = "web_pingtask.jsp?device_id="+ip;

	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=1,toolbar=0,height=768,width=1024,top=200,left=365";
	window.open(page,"VPNPing����",otherpra);
}

function fnStartRefresh(){

	refreshAlarm();
	//����Ѿ�������Ϊ�ֶ�ˢ��,��ֱ�ӷ���.
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
//���ø澯����
function ConfigWarnVoice(){
	var page = "config_warn_voice.jsp";
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,800,600);
}
//���ø澯���˹���
function ConfigWarnRule(){
	var page = "warnRuleDef.action";//"webtop_RuleListForm.jsp";
	window.open(page);
}

//IPBrowser����
function IPBrowser()
{
   var page="IP_Browser.jsp?pid="+arrPObjectID;
   var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=no,status=0,toolbar=0";
   newWin(page,otherpra,700,400);
}

//�鿴���и澯��Ϣ
function ViewAllWarn(){
	var page = "RealTimeWarn.action";//"webtop_AllWarn.jsp";
	window.open(page);
}
//��������ͼ-add by shenkejian 2007-2-7
function SaveImg(){
	var page="download_webtopo_img.jsp";
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,500,200);
}

//�Զ�ˢ��topo
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

//ֹͣˢ��topo
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
	if(arrType=="" && type!="-3" && pid!="null")
	{
		return true;
	}
	var tmp=arrType;
	//��Ե�����Ԫ�� �������������豸��������������
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
	//��Զ����Ԫ����
	else if(tmp=="***" && ((new String(type)).length==0 || parseInt(type,10)>=0 || parseInt(type,10)==-2 || parseInt(type,10)==-1) && pid!="null" )
	{
		return true;
	}
//	//�����Ԫ����˵������⴦��
//	else if(tmp.indexOf("netmanager")>=0 && (new String(type)).length>0)
//	{
//		if(arrObjectID==null)
//		{
//			return false;
//		}
//		var tmp_id=new String(arrObjectID);
//		//����кܶ����������ʾ�����豸������ʾ�������豸
//		if(tmp_id.indexOf(",")>0)
//		{
//			return true;
//		}
//		//���ֻ��һ�������������жϸ��豸��״̬��������ʾ�ĸ��˵�
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
	//�����豸�˵�
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
	//��Լ��С�ճ����ȡ�����������⴦��
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
	//���cpe�豸�����⴦��
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
//	{ //�����豸�˵�
//		var tmp_id=new String(arrObjectID);
//		if(tmp_id.indexOf(",")>0)
//		{
//
//			return false;
//		}
//		else
//		{
//			//�����豸 1/dev/  �����豸 2/
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
//	{ //�����豸�˵�
//		var tmp_id=new String(arrObjectID);
//		if(tmp_id.indexOf(",")>0)
//		{
//			return false;
//		}
//		else
//		{
//			//�����豸 1/dev/  �����豸 2/
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
	//�����·�����ж�
	else if(tmp=="link" && (new String(type)).length==0)
	{
		return true;
	}
	//���в���
	//���ɶԴ�ȷ���豸�����Ž��м��в���
	else if((tmp=="netCut") && (new String(type)).length>0 && pid !="null")
	{
		//ѡ���˶���豸ʱ
		var tmp_id=new String(arrObjectID);
		if(tmp_id.indexOf(",")>0)
		{
			var o_array=arrObjectID.split(",");
			for(var i=0;i<o_array.length;i++)
			{
				//�Ƿ�Ϊ��ȷ���豸���ȷ������
				//_isconfirm ��ȷ��Ϊ0 ��ȷ��Ϊ1
				if(arrDev[o_array[i]]._isconfirm == '0' || arrDev[o_array[i]]._isConfirmSeg == 'unkdev')
					return false;
			}
			return true;
		}
		//ѡ����һ���豸ʱ
		else{
			if(arrDev[arrObjectID]._isconfirm == '0' || arrDev[arrObjectID]._isConfirmSeg == 'unkdev')
				return false;
			return true;
		}
	}
	//ɾ������
	//����ɾ����ȷ������
	else if((tmp=="netDelete") && (new String(type)).length>0 && pid !="null")
	{
		//ѡ���˶���豸ʱ
		var tmp_id=new String(arrObjectID);
		if(tmp_id.indexOf(",")>0)
		{
			var o_array=arrObjectID.split(",");
			for(var i=0;i<o_array.length;i++)
			{
				//�Ƿ�Ϊ��ȷ������
				//_isConfirmSeg ��ȷ������Ϊunkdev  ��ͨ����Ϊ0
				if(arrDev[o_array[i]]._isConfirmSeg == 'unkdev')
					return false;
			}
			return true;
		}
		//ѡ����һ���豸ʱ
		else{
			if(arrDev[arrObjectID]._isConfirmSeg == 'unkdev')
				return false;
			return true;
		}
	}
	//ճ�� ȡ�����в���
	//�����ڴ�ȷ�������Ͻ���ճ������
	else if(tmp=="netPase" && pid!="null" && parseInt(type,10)==0)
	{
		if(curPasteObj==null)
		{
			return false;
		}

		var tmp_id=new String(curPasteObj);
		//arrDev[arrObjectID]._isConfirmSeg == '0' ����Ϊ��ͨ����
		if(tmp_id.length>0&&arrDev[arrObjectID]._isConfirmSeg == '0')
		{
			return true;
		}
		else
		{
			return false;
		}

	}
	//�ò˵�ֻ��ѡ��һ̨�豸����һ������
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
	//����������͵Ĳ˵�
	else if(tmp=="0"&&parseInt(type,10)==0)
	{
		//��ʱֻ��������εġ�������һ�㡱�˵�
		return true;
	}
	//��Դ�ȷ���������в˵�����ȷ�����д�ȷ���豸
	else if(tmp=="segment_confirm"&&parseInt(type,10)==0)
	{
		//alert("segment_confirm "+arrObjectID);
		//�Ƿ�Ϊ��ȷ���豸����
		//��ȷ�����ŵ�_isConfirmSegΪ unkdev ��ͨ����Ϊ0
		if(arrDev[arrObjectID]._isConfirmSeg != '0')
			return true;
		else
			return false;
	}
	//��Դ�ȷ���豸���в˵�����֧�ֶ��
	//���ѡ���豸������ȷ���豸 ��˲˵��������
	else if(tmp=="device_confirm"&&parseInt(type,10)>0)
	{
		//ѡ���˶���豸ʱ
		var tmp_id=new String(arrObjectID);
		if(tmp_id.indexOf(",")>0)
		{
			var o_array=arrObjectID.split(",");
			for(var i=0;i<o_array.length;i++)
			{
				//�Ƿ�Ϊ��ȷ���豸
				//��ȷ��Ϊ0 ��ȷ��Ϊ1
				if(arrDev[o_array[i]]._isconfirm == '1')
					return false;
			}
			return true;
		}
		//ѡ����һ���豸ʱ
		else{
			if(arrDev[arrObjectID]._isconfirm == '0')
				return true;
			return false;
		}
	}
	//��Դ�ȷ���豸���в˵���������
	else if(tmp=="device_confirm_single"&&parseInt(type,10)>0)
	{
		var tmp_id=new String(arrObjectID);
		if(tmp_id.indexOf(",")>0)
		{
			return false;
		}
		//��ȷ��Ϊ0 ��ȷ��Ϊ1
		if(arrDev[arrObjectID]._isconfirm == '0')
				return true;
		return false;
	}
	//�����ȷ���豸���в˵�
	//���ѡ���豸���д�ȷ���豸 ��˲˵��������
	else if(tmp=="device_confirmed"&&parseInt(type,10)>0)
	{
		//ѡ���˶���豸ʱ
		var tmp_id=new String(arrObjectID);
		if(tmp_id.indexOf(",")>0)
		{
			var o_array=arrObjectID.split(",");
			for(var i=0;i<o_array.length;i++)
			{
				//�Ƿ�Ϊ��ȷ���豸
				//��ȷ��Ϊ0 ��ȷ��Ϊ1
				if(arrDev[o_array[i]]._isconfirm == '0')
					return false;
			}
			return true;
		}
		//ѡ����һ���豸ʱ
		else{
			if(arrDev[arrObjectID]._isconfirm == '1')
				return true;
			return false;
		}
	}
	//�����ȷ���豸���в˵���������
	else if(tmp=="device_confirmed_single"&&parseInt(type,10)>0)
	{
		var tmp_id=new String(arrObjectID);
		if(tmp_id.indexOf(",")>0)
		{
			return false;
		}
		//��ȷ��Ϊ0 ��ȷ��Ϊ1
		if(arrDev[arrObjectID]._isconfirm == '1')
				return true;
		return false;
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
	if(device_id.indexOf("4/1/gw/") != -1)
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
	//alert(RM_Menu.menu.length);
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
 ip=arrDev[oIndex]._ip;
 page = "webtop_liuliang.jsp?device_id="+ip+"&refresh="+(new Date()).getTime();
 //page = "webtop_liuliang_jsRefresh.jsp?device_id="+ip+"&refresh="+(new Date()).getTime();
 var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
 newWin(page,otherpra,1000,400);
}

//��ʾ����
function showPmee(type,oIndex)
{
 ip=arrDev[oIndex]._ip;
 page = "webtop_xinnen.jsp?device_id="+ip+"&refresh="+(new Date()).getTime();
 var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
 newWin(page,otherpra,1000,400);
}

//��ʾ�豸���ܺ�����
function showFluxAndPmee(type,oIndex)
{
 ip=arrDev[oIndex]._ip;
 page = "webtop_FluxAndPmee.jsp?device_id="+ip+"&refresh="+(new Date()).getTime();
 //page = "webtop_liuliang_jsRefresh.jsp?device_id="+ip+"&refresh="+(new Date()).getTime();
 var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
 newWin(page,otherpra,1000,680,5,5);
}

//��ʾϵͳ��Ϣ
function showSysInfo(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
	page = "webtop_SysInfo.jsp?title=ϵͳ��Ϣ&className=ReadDeviceInfo&device_id="+ip+"&refresh="+(new Date()).getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,850,680);
}

//��ʾEVDO��Ϣ
function showEVDOInfo(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
	page = "../bbms/resource/showCardInfo.action?deviceId="+ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	newWin(page,otherpra,850,450,50,50);
}

//��ʾ�˿���Ϣ
function showPortInfo(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
	page = "webtop_SysInfo.jsp?title=�˿���Ϣ&className=ReadDevicePort&device_id="+ip+"&refresh="+(new Date()).getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,1000,750);
}

//��ʾ��ǰ�豸�Ķ˿�,����,״̬����Ϣ
function showDeviceStatus(type,oIndex) {
	ip=arrDev[oIndex]._ip;
	
	page = "webtop_DeviceStatus2.jsp?title=�豸״̬��Ϣ&className=ReadDeviceStatus&device_id="+ip+"&refresh="+(new Date()).getTime();
	//page = "deviceStatusFrame.jsp?device_id="+ip+"&refresh="+(new Date()).getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=1,toolbar=0";
	newWin(page,otherpra,1000,650,5,5);
}
// -----------------add by lizhaojun 2007-01-12---------webtopo����ping��telnet�˵�------->start
//ping ��������ַ
function pingIp(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
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
//tracert ��������ַ
function tracertLink(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
	page = "webtop_ping.jsp?&device_id="+ip+"&action=tracert" ;
	document.all("childFrm").src = page;
}
// -----------------add by lizhaojun 2007-01-12---------webtopo����ping��telnet�˵�------>-end
//��ʾ�豸�澯
function showDeviceWarn(type,oIndex)
{
	var ip=arrDev[oIndex]._ip;
	var page = "webtop_viewwarn.jsp?type=device&device_id="+ip+"&refresh="+(new Date()).getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,800,400);
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
// alert(oIndex);
 var LinkObj = arrLink[oIndex];
 var link_id = LinkObj._id;
 _from_obj = arrDev[findDevObjByID(LinkObj._from)];
 _to_obj     = arrDev[findDevObjByID(LinkObj._to)];
 var _from_title = _from_obj._title;
 var _to_title = _to_obj._title;
 var page = "./link_css_edit_form.jsp?link_id=" + link_id + "&from=" + _from_title + "&oIndex=" + oIndex+"&to=" + _to_title;
 window.open(page,"�༭��·��ʽ","left=20,top=20,width=450,height=250,resizable=no,scrollbars=yes");
}

//�����豸
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

//ճ���豸
function PasteNet(type,oIndex)
{
	if(curPasteObj==null || curPasteObj.length==0)
	{
		window.alert("��ѡ�����豸");
		return;
	}

	if(arrDev[oIndex]._id==curPasteObj[0]._pid)
	{
		window.alert("��Ҫճ���Ķ����Ѿ�����ѡ��������ڣ�");
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

//ȡ������
function CancelCut(type,oIndex)
{
	curPasteObj=null;

}

//ɾ���豸
function DeleteObj(type,oIndex)
{

	if(!window.confirm("��ȷ��Ҫɾ����ѡ��Ķ���"))
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


//ɾ���豸
function DeleteLinkObj(type,oIndex)
{

	if(!window.confirm("��ȷ��Ҫɾ����ѡ�����·��"))
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


//��������
function ConfigFlux(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
	var serial=arrDev[oIndex]._type;
	//page="webtop_ConfigFlux.jsp?device_id="+ip+"&serial="+serial;
	page = "../performance/configFlux.action?isbatch=false&device_id="+ip;
	var otherpra="channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	newWin(page,otherpra,750,500);
}
//QOS����
function ConfigQOS(type,oIndex){
	ip=arrDev[oIndex]._ip;
	var serial=arrDev[oIndex]._type;
	page="webtop_ConfigQOS.jsp?device_id="+ip+"&serial="+serial;
	var otherpra="channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	newWin(page,otherpra,750,500);
}

//��������
function ConfigPerf(type,oIndex)
{
	ip=arrDev[oIndex]._ip;
	var serial=arrDev[oIndex]._type;
	if (serial<0)
	{
		alert("���豸��ʱ�޷��������ܣ�");
	}
	else {
		//page = "pm_Config.jsp?device_id="+ip+"&dev_serial="+serial;
		page = "../performance/configPmee.action?isbatch=false&device_id="+ip;
		var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0,height=500,width=700,top=200,left=365";
		window.open(page,"�豸��������",otherpra);
	}
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
	//�������û���ͼ.�û���ͼ�а��������豸(ip=1/dev/device_id)�������豸(2/dxbh)
	ip=arrDev[oIndex]._ip;
	//�û���ͼ ��Ҫ��ip���д��� ip=1/dev/2222-->ip=2222
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
	var page="./removeAlarm.jsp?alarmStr="+oIndex;
	document.all("childFrm").src=page;

}

//ȷ�ϸ澯
function ack_Warn(type,oIndex)
{
	if(isActivestatus()){
		alert("�˸澯�Ѿ�ȷ��");
		return ;
	}
	if(!window.confirm("ȷ�ϸ澯��")){
		return;
	}
/**
	var page="./ackAlarm.jsp?alarmStr=" + oIndex + "&gatherId="+gatherIdStr +"&tt="+ new Date().getTime();
	if(_xmlHttp == null) _xmlHttp = new XmlHRequest();
	_xmlHttp.setURL(page);
	//��XMLHttp��setReadyChange�����¼�����
	_xmlHttp.doPost(ack_Warn_Result);
	//document.all("childFrm").src=page;
*/
    var tmp=oIndex.split("@"); 
	var alarmid=tmp[0]+"-"+gatherIdStr+"-"+tmp[1]+"-"+Math.round(new Date().getTime()/1000)+"-/-";
    var page="./RealTimeWarn!ConfigWarn.action?alarmid=" + alarmid;
	//if(_xmlHttp == null) _xmlHttp = new XmlHRequest();
	//_xmlHttp.setURL(page);
	//��XMLHttp��setReadyChange�����¼�����
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
			alert("�澯ȷ�ϳɹ�");
		}else{
			alert("�澯ȷ��ʧ�ܣ������ԣ�");
		}
		xmlDoc = null;
	}
}
//�澯���
function clear_Warn(type,oIndex){
	if(!window.confirm("����澯��"))
	{
		return;
	}
	var page="./clearAlarm.jsp?alarmStr=" + oIndex + "&gatherId="+gatherIdStr +"&tt="+ new Date().getTime();
	//alert(page);

	if(_xmlHttp == null) _xmlHttp = new XmlHRequest();
	_xmlHttp.setURL(page);
	//��XMLHttp��setReadyChange�����¼�����
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
			alert("�澯����ɹ�");
		}else{
			alert("�澯���ʧ�ܣ������ԣ�");
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
	if(isActivestatus()){
		alert("�˸澯�Ѿ�ȷ��");
		return ;
	}
	var page="./ack_Store_Alarm.jsp?alarmStr="+oIndex+"&gatherId="+gatherIdStr;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=no,status=0,toolbar=0";
	newWin(page,otherpra,800,700);
}
//��鵱ǰ�澯
//true:�澯�Ѿ�ȷ�� false:��û��ȷ��
function isActivestatus(){
	if(oList.currentRow == null)
		return false
	else{
		var index = oList.currentRow.rowIndex;
		var activestatus = oList.lvdata[index-1][4]._value;
		if(activestatus.indexOf("��ȷ��") != -1){
			return true;
		}
	}
	return false;
}
//Add By Hemc 2006-12-11
//�澯�б���λ�豸����
//����topo.js�е�Dev_MouseDown����
//��λ���豸����
function findDevObjLocation(dev,srcpid){
	if(srcpid == null || typeof(srcpid) == "undefined") srcpid = (arrPObjectID == null || arrPObjectID == "" || typeof(arrPObjectID) == "undefined") ? "1/0" : arrPObjectID;
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
		if(dev == "1/gw/"){
			alert("δ֪�豸id�޷���λ(" + dev + ")");
		}else if(confirm("�豸���ڵ�ǰ�㣬��Ҫ������豸��Topoͼ��(" + dev + ")")){
			GoTopoByDevID(dev,srcpid);
		}else{
			if(srcpid == null || typeof(srcpid) == "undefined") srcpid = "1/0";
			arrPObjectID = srcpid;
			refreshTopo();
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
		var dev = "1/gw/";
		if(_index != -1){
			dev = oIndex.substr(_index + 1);
		}
		//alert("arrPObjectID:" + arrPObjectID);
		$.findDevObjLocation(dev,arrPObjectID);
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
function GoTopoByDevID(pid,srcpid){
	var oldViewID = ViewID;
	//��ʱ���浱ǰTopoͼpid
	idWebTopo.src = "getTopoByIdXML.jsp?pid="+ pid;
	dispTopo();
	idWebTopo.innerHTML = "<v:rect id=selRect  style=\"DISPLAY: none; Z-INDEX:  255; POSITION:  absolute\" coordsize = \"21600,21600\" strokecolor = \"#10fc18\"><v:fill opacity = \"0\" ></v:fill></v:rect>";
	drawTopo();
	initParamToSession();
	findDevObjLocation(pid,srcpid);
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
	//devAlarm = new Array();
	for(var i=0;i<list.length;i++){
		arr = list[i].split(",");
		ln = findDevObjByID(arr[0]);
		//devAlarm[devAlarm.length] = arr[0];
		num = arr[1];
		level = arr[2];
		//Topoͼ�д����豸����
		if(typeof(arrDev[ln]) != "undefined"){
			arrDev[ln].drawWarn(num,level);
		}
	}
	refreashLinkColor();
}
//��·״̬�ı�
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
//JS�����澯״̬ʵ����·���
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
//ˢ�¸澯��״̬
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
		//Topoͼ�д����豸����
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
		divSelectDevSerial.innerHTML = _xmlHttp.getResult().toString();
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
	//alert("_typeSelect.value:"+_typeSelect.value)
	//�������� OUI+Serial
	if(_typeSelect.value == "3"){
		//ѡ��--"����|�豸���к�"
		//�����豸���Ͳ���ʱ���ı���Ͱ�ť����
		_searchValue.style.display = "";
		btn_QueryDev.style.display = "";

		//�ó��̡��豸�ͺ�div����
		divSelectVendor.style.display = "none";
		divSelectDevSerial.style.display = "none";

		//divSelectDevSerial.innerHTML = "";
		//divSelectVendor.innerHTML = "���س�������...";
		//var _url = "webtopo_SearchDeviceType.jsp?type=0";
		//if(_xmlHttp == null) _xmlHttp = new XmlHRequest();
		//_xmlHttp.setURL(_url);
		//��XMLHttp��setReadyChange�����¼�����
		//_xmlHttp.doPost(setVenderData);
	}
	//��ʾ���̣����ܹ������ͺ�
	else if(_typeSelect.value == "5") {
		//ѡ��--"����|�豸�ͺ�"
		//�����豸���Ͳ���ʱ���ı���Ͱ�ť����
		_searchValue.style.display = "";
		btn_QueryDev.style.display = "";

		//�ó��̡��豸�ͺ�div��ʾ
		divSelectVendor.style.display = "";
		divSelectDevSerial.style.display = "";

		divSelectDevSerial.innerHTML = "";
		divSelectVendor.innerHTML = "���س�������...";
		var _url = "webtopo_SearchDeviceType.jsp?type=3";
		if(_xmlHttp == null) _xmlHttp = new XmlHRequest();
		_xmlHttp.setURL(_url);
		//��XMLHttp��setReadyChange�����¼�����
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
//ѡ���̣����ظó��̵��豸���к�
function changeVendorType(_value){
	divSelectDevice.innerHTML = "";
	if(_value != "-1"){
		var _url = "webtopo_SearchDeviceType.jsp?type=1&vendor_id=" + _value;
		divSelectDevSerial.innerHTML = "�����豸�ͺ�...";
		if(_xmlHttp == null) _xmlHttp = new XmlHRequest();
		_xmlHttp.setURL(_url);
		//��XMLHttp��setReadyChange�����¼�����
		_xmlHttp.doPost(setDeviceTypeData);
	}else{
		divSelectDevSerial.innerHTML = "";
	}
}

//ѡ���̣����ظó��̵��豸�ͺ�
function relateDeviceModel(_value)
{
	divSelectDevice.innerHTML = "";
	if(_value != "-1"){
		var _url = "webtopo_SearchDeviceType.jsp?type=4&vendor_id=" + _value;
		divSelectDevSerial.innerHTML = "�����豸�ͺ�...";
		if(_xmlHttp == null) _xmlHttp = new XmlHRequest();
		_xmlHttp.setURL(_url);
		//��XMLHttp��setReadyChange�����¼�����
		_xmlHttp.doPost(setDeviceTypeData);
	}else{
		divSelectDevSerial.innerHTML = "";
	}
}
//ѡ����+�豸���кţ����ض�Ӧ���豸
function changeDeviceType(_value){
	if(_value != "-1"){
		_value =_VendorType.value+"|||"+_value;
		webtopoSearchDevice(_value,3);
	}else{
		divSelectDevice.innerHTML = "";
		//divSelectDevice.style.display = "";
	}
}

//ѡ����+�豸�ͺţ����ض�Ӧ���豸
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

//�����豸����
function TestConnection(type, oIndex){
	ip = arrDev[oIndex]._ip;
	var serial = arrDev[oIndex]._type;
	var id = arrDev[oIndex]._id;
	var page = "./webtopo_TestConnection.jsp?oIndex=" + oIndex + "&device_id=" + ip + "&type=" + serial + "&id=" + id;
	document.all("childFrm").src = page;
}

//��ȡRPC����
function RPCMethod(type, oIndex){
	ip = arrDev[oIndex]._ip;
	var serial = arrDev[oIndex]._type;
	var id = arrDev[oIndex]._id;
	var page = "./webtopo_RPCMethod.jsp?oIndex=" + oIndex + "&device_id=" + ip + "&type=" + serial + "&id=" + id;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra, 600, 300);
}

//�޸��豸����
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
 //�޸��豸����
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

//���������Ļ�ȡ
function getFamiliarPara(type, oIndex)
{
	ip = arrDev[oIndex]._ip;
	var page = "./paramAllList.jsp?device_id=" + ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra,1024, 500);
}

//����ģ�ͻ�ȡ
function getParaModule(type, oIndex)
{
	ip = arrDev[oIndex]._ip;
	var page = "./HGWGetParaModel.jsp?device_id=" + ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra,500, 400);
}

//ȷ���豸������ѯ
function paraQuery(type, oIndex)
{
	ip = arrDev[oIndex]._ip;
	var page = "./HGWGetMoreParaModel.jsp?device_id=" + ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra, 500,400);
}

//ȷ���豸�����仯�ϱ�
function paraChangCall(type, oIndex)
{
	ip = arrDev[oIndex]._ip;
	var page = "./ParamChangeConfig.jsp?device_id=" + ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=yes,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra,500, 400);
}

//�豸ping���
function pingTest(type, oIndex)
{
	ip = arrDev[oIndex]._ip;
	var page = "./jt_device_webtopo_from1.jsp?device_id=" + ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra,600, 400);
}

//ATMF5LOOP���
function ATMTest(type, oIndex)
{
	ip = arrDev[oIndex]._ip;
	var page = "./jt_device_webtopo_from2.jsp?device_id=" + ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra,600, 400);
}

//�����豸
function deviceReboot(type, oIndex)
{
	ip = arrDev[oIndex]._ip;
	var page = "./jt_device_webtopo_from3.jsp?device_id=" + ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra,600, 400);
}

//�ָ���������
function deviceReset(type, oIndex)
{
	ip = arrDev[oIndex]._ip;
	var page = "./jt_device_webtopo_from4.jsp?device_id=" + ip;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page, otherpra,600, 400);
}

//DSL���
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

//������ͼ
function openTopo(type,id){
	var page = "./openTopoView.jsp?id="+ id +"&oper=open&type="+ type +"&tt="+ new Date().getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	newWin(page,otherpra,300,130);
}

//���·�������ͼ
function NewAllTopo(type)
{
	var page = "./SelectGathers.jsp?oper=reset&type="+ type +"&tt="+ new Date().getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	newWin(page,otherpra,400,130);
}


//������������ͼ
function AddTopo(type)
{
	var page = "./SelectGathers.jsp?oper=add&type="+ type +"&tt="+ new Date().getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	newWin(page,otherpra,400,130);
}


//�����ͻ���Ϣ
function SetCustomInfo(type,oIndex)
{
	var obj_id=arrDev[oIndex]._id;
	var name=arrDev[oIndex]._title;
	var page = "./webtopo_setCustomID.jsp?obj_id=" + obj_id + "&title=" + name;
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=no,status=0,toolbar=0";
	newWin(page,otherpra,400,130);
}


//���˽ṹ����
function SaveTopo(type)
{
	my_movedObjs();
	var page="webtopo_TopOper.jsp?type="+type;
	document.all("childFrm").src=page;
}


//�ƶ�webtopo��Ԫ
function my_movedObjs()
{
	//֪ͨMasterControl �����ƶ�����
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

//��������ͼ-add by shenkejian 2007-2-7
function SaveImg(topoType){
	//�ƶ���Ԫ����λ��
	movedObjs(getMovedObjs(),topoType);
	var pid = arrPObjectID;
	outTopoImage(pid,topoType);
}

//����ѡ��������豸��Ϣ
function SaveImgSelected(topoType){
	//�ƶ���Ԫ����λ��
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
		alert('��ѡ����Ҫ�������豸��');
		return false;
	}
}

//��������ͼ
function outTopoImage(pid,topoType)
{
	var page = "download_img.jsp?pid="+ pid +"&topoType="+ topoType;
	document.all("childFrm").src = page;
	//var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	//alert(page);
	//newWin(page,otherpra,0,0);
}

//��������ͼ
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

//ȷ���豸
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

//ˢ��topoͼ
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

//��ȡvlan��Ϣ
function getVlanInfo(type,oIndex){
	var id=arrDev[oIndex]._ip;
	var page = "../bbms/GetSnmpInfo!getVlanInfo.action?device_id="+ id +"&tt="+ new Date().getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,400,230);
}

//��ȡDHCP��Ϣ
function getDhcpInfo(type,oIndex){
	var id=arrDev[oIndex]._ip;
	var page = "../bbms/GetSnmpInfo.action?device_id="+ id +"&tt="+ new Date().getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,400,230);
}

//��ȡwlan��Ϣ
function getwlanInfo(type,oIndex){
	var id=arrDev[oIndex]._ip;
	var page = "webtopo_wlanInfo.jsp?device_id="+ id +"&tt="+ new Date().getTime();
	var otherpra = "channelmode=0,directories=0,location=0,menubar=0,resizable=no,scrollbars=yes,status=0,toolbar=0";
	newWin(page,otherpra,400,230);
}
