//�������
var refnum=5;//���ð�ťˢ��ʱ��Ϊ10��
var refnum_total=0;//����ˢ��ʱ��
var time_out=null;//��ʱ��
var ref_interval=null;//�ָ��澯��ʱ��
var rid;//����ID
var RM_Menu = new CMenu("RM_Menu");
var timeout=0;//��ʱ�Ĵ���
//**********************For Ctrl & Shift Click*************
var startindex,endindex;//��ʼλ�á�����λ��
var sel_json={};//ѡ�е�������ɵ�json���ṹ��<id>:{id:<id>,css:<css>}   <id>:Ϊ��¼��id
var sel_id="";//ѡ�е�tr��id����-/-�ָ�
var sel_devid="";//ɾ����ȷ�ϸ澯ʱʹ�õĲ���Numer+"-"+gatherid+"-"+deviceid
//*************************End Right Menu******************

//�л��ֶ����Զ�ˢ��
function ChangeAutoOrHandRef(val,intevel){
	$.showRef(val);
	if(time_out!=null)
		window.clearTimeout(time_out);
		time_out=null;
	if(val==0){//�Զ�ˢ��
		SetRefTime(intevel);
		ShowTimeRef();
	}else{//�ֶ�ˢ��
		SetRefTime(5);
		RefBtn();
	}
}
//����ˢ��ʱ��
 function SetRefTime(t){
	if(time_out!=null)
		window.clearTimeout(time_out);
	time_out=null;
	refnum=eval(t);
	refnum_total=refnum;
 }
 //��������ˢ��ʱ��
 function ReSetRefTime(){
	if(time_out!=null)
		window.clearTimeout(time_out);
	time_out=null;
	refnum=refnum_total;
 }
//��ʾ���ж���ʱ��ˢ��
function ShowTimeRef(){
	document.getElementById("next_reftime").innerHTML="�����Զ�ˢ��ʱ�仹��"+refnum+"��";
	if(refnum==0){
		window.clearTimeout(time_out);
		time_out=null;
		$.RefData();
	}else{
		refnum--;
        time_out=window.setTimeout("ShowTimeRef()",1000);
	}	
}
//��ťˢ��
function RefBtn(){
	document.getElementById("next_reftime").innerHTML="�����ֶ�ˢ��ʱ�仹��"+refnum+"��";
    if(refnum==0){
        window.clearTimeout(time_out);
		time_out=null;
        document.getElementById("refbtn").value="ˢ��";
        document.getElementById("refbtn").disabled=false;
    }else{
        document.getElementById("refbtn").value="ˢ��("+refnum+")";
		document.getElementById("refbtn").disabled="true";
        refnum--;
        time_out=window.setTimeout("RefBtn()",1000);
    }
}
//��ʱ��ȡ�ָ��澯
function getRefWarn(){
	//BBMS��ʱû���ṩ����澯�Ĺ���,�ʴ˹�����ʱ����
	/**
	if(ref_interval!=null){
		window.clearInterval(ref_interval);
		ref_interval=null;
	}
	ref_interval=window.setInterval("$.getRecWarn()",(1000*10+10));
	*/
}
//�رջָ��澯��ʱ��
function ClearWarnInterval(){
	if(ref_interval!=null){
		window.clearInterval(ref_interval);
		ref_interval=null;
	}
}

//�����ҽ��˵�
function createRMenu(type,pid){
	if(RM_Menu.menu.length>0){
		return;
	}
	var n=0;
	RM_Menu.menu.length=0;
	for(var i=0;i<arrRMenu.length;i++){
		if(arrRMenu[i] == null) continue;
				if(typeof(arrRMenu[i]._submenu) == "string")
				{
					RM_Menu.menu[n] = new menu("RM_Menu"+n,
						arrRMenu[i]._name,
						"","",
						arrRMenu[i]._target,
						"",0,
						arrRMenu[i]._submenu);
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

/**
 * �˵����ݽṹ
 * keyword���˵������ؼ���
 * name���˵�����
 * link���˵�����
 * desc���˵�����
*/
function RMenuRes(type,keyword, name, link,target, icon,desc, oper,smenu){
	this._type = type;
	this._keyword = keyword;
	this._name    = name;
	this._link	  = link;
	this._target = target;
	this._icon = icon;
	this._desc	  = desc;
	this._oper   = oper;
	if(smenu != null){
		this._submenu = smenu;
	}
}
var arrRMenu=[];
arrRMenu[arrRMenu.length] = new RMenuRes("-3","CHECKSTOREALARM","ȷ�ϸ澯","$.ConfigAlarm()","javascript","","ȷ�ϸ澯�����","");
arrRMenu[arrRMenu.length] = new RMenuRes("-3","DELALARM","����澯","$.DelAlarm()","javascript","","����澯","");

arrRMenu[arrRMenu.length] = new RMenuRes("*","","-","","","","","");
arrRMenu[arrRMenu.length] = new RMenuRes("-3","DELALARM","�Ƴ��澯","$.RemoveWarn(-2)","javascript","","�Ƴ��澯","");
arrRMenu[arrRMenu.length] = new RMenuRes("-3","EXPORTEXCEL","����EXCEL","$.Export()","javascript","","����Excel","");
arrRMenu[arrRMenu.length] = new RMenuRes("-3","FINDDEVICEOBJ","��λ�豸����","$.findDevObj()","javascript","","��λ�豸����","");



function showRightMenu(){
	//��ʾ֮ǰ�����Ƚ�ԭ�еĲ�ɾ����
	document_click();
	//��ͼ
	drawSubMenu(RM_Menu);
	//alert(RM_Menu.menu.length);
	//��ʾ�˵�
	showRMenu("RM_Menu");
	g_blnRMShow = true;
	return;
}
