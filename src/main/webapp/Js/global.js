//从cookie中读取用户权限

function GetCookie(sName){

	var aCookie = document.cookie.split("; ");

	for (var i=0; i < aCookie.length; i++){

		var aCrumb = aCookie[i].split("=");

		if (sName == aCrumb[0]) 

			return unescape(aCrumb[1]);

	}

	return "";	

}



//验证用户权限

function chkPremession(oname){

	if(oname=="") return true;

	var tmp = "#"+userPremession+"#"

	if(tmp.indexOf("#"+oname+"#") == -1)

		return false;

	else

		return true;

}



var showICPNum=0;	//ICPMenu 显示的个数

var showLSNum =0;

//var userPremession = GetCookie("ldims");	//用户权限





//设置通用菜单



for(var i=0;i<LSMenuRes.length;i++){

	if(chkPremession(LSMenuRes[i]._oname)){

		AddLSMenu(LSMenuRes[i]._keyword,

				  LSMenuRes[i]._name,

				  LSMenuRes[i]._link,

				  LSMenuRes[i]._desc

		);

		if(typeof(LSMenuRes[i]._submenu) == "object" && isFrameWork){

			for(var m=0; m<LSMenuRes[i]._submenu.length; m++){

				if(chkPremession(LSMenuRes[i]._submenu[m]._oname))

					AddSubLSMenu(LSMenuRes[i]._submenu[m]._keyword,

								 LSMenuRes[i]._submenu[m]._name,

								 LSMenuRes[i]._submenu[m]._link,

								 LSMenuRes[i]._submenu[m]._desc

					);

			}

		}

		showLSNum += 1;

	}

}



//设置主题菜单

for(var j=0;j<ICPMenuRes.length;j++){

	if(chkPremession(ICPMenuRes[j]._oname)){

		AddICPMenu(ICPMenuRes[j]._keyword,

				   ICPMenuRes[j]._name,

				   ICPMenuRes[j]._link,

				   ICPMenuRes[j]._desc

		);

		if(typeof(ICPMenuRes[j]._submenu) == "object"){

			for(var k=0; k<ICPMenuRes[j]._submenu.length; k++){

				if(ICPMenuRes[j]._submenu[k]._name == "-"){

					if(chkPremession(ICPMenuRes[j]._submenu[k]._oname))

						AddHR(ICPMenuRes[j]._submenu[k]._keyword,true);

				}else{

					if(chkPremession(ICPMenuRes[j]._submenu[k]._oname))

						AddSubICPMenu(ICPMenuRes[j]._submenu[k]._keyword,

									  ICPMenuRes[j]._submenu[k]._name,

									  ICPMenuRes[j]._submenu[k]._link,

									  ICPMenuRes[j]._submenu[k]._desc

						);

				}

			}

		}

		showICPNum += 1;

	}

}



//设置菜单长度

//setMenuWidth(showLSNum*70,showICPNum*100);

setMenuWidth(showLSNum*70,showICPNum*120);



//画工具栏

//alert(HTMLStr)

drawToolbar();