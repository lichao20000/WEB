var deviceId;
var date;


function fluxConfig()
{    
    window.open("../performance/configFlux.action?isbatch=false&device_id="+deviceId);
}

function perfConfig()
{    
    window.open("../performance/configPmee.action?isbatch=false&device_id="+deviceId);
}
function fluxAndPme()
{  
	window.open("../webtopo/webtop_FluxAndPmee.jsp?device_id="+deviceId+"&refresh="+date);
}
function sysInfo()
{ 
	window.open("../webtopo/webtop_SysInfo.jsp?title=�˿���Ϣ&className=ReadDevicePort&device_id="+deviceId+"&refresh="+date);
}
function xinnen()
{
	window.open("../webtopo/webtop_xinnen.jsp?device_id="+deviceId+"&refresh="+date);
}
function liuliang()
{
	window.open("../webtopo/webtop_liuliang.jsp?device_id="+deviceId+"&refresh="+date);
}


function evtMenuOnmouseMove()
{
    this.style.backgroundColor='#8AAD77';
    this.style.paddingLeft='20px';
}

function evtOnMouseOut()
{
    this.style.backgroundColor='#FAFFF8';
}

function CreateMenu()
{    
        var div_Menu          = document.createElement("Div");
        div_Menu.id           = "div_RightMenu";
        div_Menu.className    = "div_RightMenu";
        
        /**
        var div_Menu1          = document.createElement("Div");
        div_Menu1.className   = "divMenuItem";
        div_Menu1.onclick     = evtMenu1;
        div_Menu1.onmousemove = evtMenuOnmouseMove;
        div_Menu1.onmouseout  = evtOnMouseOut;
        div_Menu1.innerHTML   = "�ҵ���ҳ";
        
        var div_Menu2          = document.createElement("Div");
        div_Menu2.className   = "divMenuItem";
        div_Menu2.onclick     = evtMenu2;
        div_Menu2.onmousemove = evtMenuOnmouseMove
        div_Menu2.onmouseout  = evtOnMouseOut
        div_Menu2.innerHTML   = "ɾ����¼";
        
        var div_Menu3          = document.createElement("Div");
        div_Menu3.className   = "divMenuItem";
        div_Menu3.onmousemove = evtMenuOnmouseMove;
        div_Menu3.onmouseout  = evtOnMouseOut;
        div_Menu3.innerHTML   = "��ϸ��Ϣ";
        
        var div_Menu4          = document.createElement("Div");
        div_Menu4.className   = "divMenuItem";
        div_Menu4.onmousemove = evtMenuOnmouseMove;
        div_Menu4.onmouseout  = evtOnMouseOut;
        div_Menu4.innerHTML   = "ˢ��";
        
        var Hr1        = document.createElement("Hr");
        **/
        var div_Menu5          = document.createElement("Div");
        div_Menu5.className   = "divMenuItem";
        div_Menu5.onclick     = fluxConfig;
        div_Menu5.onmousemove = evtMenuOnmouseMove;
        div_Menu5.onmouseout  = evtOnMouseOut;
        div_Menu5.innerHTML   = "��������";
        
        var div_Menu6          = document.createElement("Div");
        div_Menu6.className   = "divMenuItem";
        div_Menu6.onclick     = perfConfig;
        div_Menu6.onmousemove = evtMenuOnmouseMove;
        div_Menu6.onmouseout  = evtOnMouseOut;
        div_Menu6.innerHTML   = "��������";
        
        var Hr2        = document.createElement("Hr");
                
        var div_Menu7          = document.createElement("Div");
        div_Menu7.className   = "divMenuItem";
        div_Menu7.onclick     = fluxAndPme;
        div_Menu7.onmousemove = evtMenuOnmouseMove;
        div_Menu7.onmouseout  = evtOnMouseOut;
        div_Menu7.innerHTML   = "�豸����+����";
        
        var div_Menu8          = document.createElement("Div");
        div_Menu8.className   = "divMenuItem";
        div_Menu8.onclick     = sysInfo;
        div_Menu8.onmousemove = evtMenuOnmouseMove;
        div_Menu8.onmouseout  = evtOnMouseOut;
        div_Menu8.innerHTML   = "�˿���Ϣ";       
        
        var div_Menu9          = document.createElement("Div");
        div_Menu9.className   = "divMenuItem";
        div_Menu9.onclick     = xinnen;
        div_Menu9.onmousemove = evtMenuOnmouseMove;
        div_Menu9.onmouseout  = evtOnMouseOut;
        div_Menu9.innerHTML   = "�豸����";
        
        var div_Menu10           = document.createElement("Div");
        div_Menu10.className   = "divMenuItem";
        div_Menu10.onclick     = liuliang;
        div_Menu10.style.marginBottom =  0;
        div_Menu10.onmousemove = evtMenuOnmouseMove;
        div_Menu10.onmouseout  = evtOnMouseOut;
        div_Menu10.innerHTML   = "�豸����";
        
        /**
        div_Menu.appendChild(div_Menu1);
        div_Menu.appendChild(div_Menu2);
        div_Menu.appendChild(div_Menu3);
        div_Menu.appendChild(div_Menu4);
        div_Menu.appendChild(Hr1);
        **/
        div_Menu.appendChild(div_Menu5);
        div_Menu.appendChild(div_Menu6);
        div_Menu.appendChild(Hr2);

        div_Menu.appendChild(div_Menu10);
        div_Menu.appendChild(div_Menu9);
        div_Menu.appendChild(div_Menu8);
        div_Menu.appendChild(div_Menu7);

        document.body.appendChild(div_Menu);
}
    
    function ShowRightMenu(devId)
    {
		date = new Date().getTime();
		deviceId = devId;
		
        if(document.getElementById("div_RightMenu") == null)
        {    
            //alert("create menu");
        	CreateMenu();
        }
        
        //document.oncontextmenu = ShowMyMenu;
        //alert("show");
        ShowMyMenu();
        document.body.onclick  = HideMyMenu;
    }
    
    // �жϿͻ��������
    function IsIE() 
    {
        if (navigator.appName=="Microsoft Internet Explorer") 
        {
            return true;
        } 
        else 
        {
            return false;
        }
    }
    
    function ShowMyMenu()
    {
        if (IsIE())
        {
            //document.body.onclick  = HideMyMenu;
            
            var redge=document.body.clientWidth-event.clientX;
            var bedge=document.body.clientHeight-event.clientY;
            var menu = document.getElementById("div_RightMenu");
            
            if (redge<menu.offsetWidth)
            {
                menu.style.left=document.body.scrollLeft + event.clientX-menu.offsetWidth
            }
            else
            {
                menu.style.left=document.body.scrollLeft + event.clientX
                //�����иĶ�
        		//menu.style.visibility = "visible";//ҳ��׶�ͻ��
                menu.style.display = "block";
            }
            if (bedge<menu.offsetHeight)
            {
                menu.style.top=document.body.scrollTop + event.clientY - menu.offsetHeight
            }
            else
            {
                menu.style.top = document.body.scrollTop + event.clientY
                menu.style.display = "block";
            }
           
        }
        return false;
    }
    
    
    function HideMyMenu()
    {
        if (IsIE()){
        	document.getElementById("div_RightMenu").style.display="none";
        }
    }