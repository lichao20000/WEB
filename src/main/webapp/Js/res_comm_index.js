/**

 * �˵����ݽṹ

 * keyword���˵������ؼ���

 * name���˵�����

 * link���˵�����

 * desc���˵�����

*/

function Menu(keyword, name, link, desc, oname){

	this._keyword = keyword;

	this._name    = name;

	this._link	  = link;

	this._desc	  = desc;

	this._oname   = oname;

	if(arguments.length>5){

		this._submenu = arguments[5];

	}

}



//���幫���˵�

var LSMenuRes = new Array(5);

var TmpSub = new Array(10);



TmpSub[0]  = new Menu('Product','ϵͳ����','./system/','ϵͳ����','');

TmpSub[1]  = new Menu('Product','��Դ����ϵͳ','./Resource/','��Դ����ϵͳ','RESOURCE_READ');

TmpSub[2]  = new Menu('Product','�ۺ����ܹ���','./Performance/','�ۺ����ܹ���','PERFORMANCE_READ');

TmpSub[3]  = new Menu('Product',strFlautName,'./10000Flaut/',strFlautName,'10000WARN_READ');

TmpSub[4]  = new Menu('Product','�ۺϹ��Ϲ���','./Warn/','�ۺϸ澯����','WARN_READ');

TmpSub[5]  = new Menu('Product','ҵ��/������������','./Operation/','ҵ��/������������','OPERATION_READ');

TmpSub[6]  = new Menu('Product','���翪ͨϵͳ','./NetCutover/','���翪ͨϵͳ','NETCUTOVER_READ');

TmpSub[7]  = new Menu('Product','ADSL��ط���ϵͳ','./AdslCheck/','ADSL��ط���ϵͳ','ADSLCHECK_READ');

TmpSub[8]  = new Menu('Product','����ϵͳ','./Report/','����ϵͳ','REPORT_READ');

TmpSub[9]  = new Menu('Product','�û���װ�ֳ�����','./UserIsnt/','��װ�ֳ�����','USER_INST_READ');





LSMenuRes[0]  = new Menu('Product','�ۺ�����','','�ۺ�����--��ƷĿ¼','',TmpSub);

LSMenuRes[1]  = new Menu('WebTopo','WEB Topo','./webtopo.jsp','Web Topo','WEB_TOP_READ');

LSMenuRes[2]  = new Menu('Refresh','ˢ��','javascript:this.location.reload();','ˢ�±�ҳ��','');

LSMenuRes[3]  = new Menu('ReLogin','���µ�¼','login.jsp','���½��е�¼','');

LSMenuRes[4]  = new Menu('Close','�˳�','javascript:window.close();','�˳�ADSLϵͳ','');



var isFrameWork = false;