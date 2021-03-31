/**

 * 菜单数据结构

 * keyword：菜单所属关键字

 * name：菜单名称

 * link：菜单连接

 * desc：菜单描述

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



//定义公共菜单

var LSMenuRes = new Array(5);

var TmpSub = new Array(10);



TmpSub[0]  = new Menu('Product','系统管理','./system/','系统管理','');

TmpSub[1]  = new Menu('Product','资源管理系统','./Resource/','资源管理系统','RESOURCE_READ');

TmpSub[2]  = new Menu('Product','综合性能管理','./Performance/','综合性能管理','PERFORMANCE_READ');

TmpSub[3]  = new Menu('Product',strFlautName,'./10000Flaut/',strFlautName,'10000WARN_READ');

TmpSub[4]  = new Menu('Product','综合故障管理','./Warn/','综合告警管理','WARN_READ');

TmpSub[5]  = new Menu('Product','业务/服务质量管理','./Operation/','业务/服务质量管理','OPERATION_READ');

TmpSub[6]  = new Menu('Product','网络开通系统','./NetCutover/','网络开通系统','NETCUTOVER_READ');

TmpSub[7]  = new Menu('Product','ADSL监控分析系统','./AdslCheck/','ADSL监控分析系统','ADSLCHECK_READ');

TmpSub[8]  = new Menu('Product','报表系统','./Report/','报表系统','REPORT_READ');

TmpSub[9]  = new Menu('Product','用户安装现场管理','./UserIsnt/','安装现场管理','USER_INST_READ');





LSMenuRes[0]  = new Menu('Product','综合网管','','综合网管--产品目录','',TmpSub);

LSMenuRes[1]  = new Menu('WebTopo','WEB Topo','./webtopo.jsp','Web Topo','WEB_TOP_READ');

LSMenuRes[2]  = new Menu('Refresh','刷新','javascript:this.location.reload();','刷新本页面','');

LSMenuRes[3]  = new Menu('ReLogin','重新登录','login.jsp','重新进行登录','');

LSMenuRes[4]  = new Menu('Close','退出','javascript:window.close();','退出ADSL系统','');



var isFrameWork = false;