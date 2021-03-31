/*
 * @author:Hemc
 * @date:2006-12-5
 * @descr:????MRTG??????????
 */
document.write("<style>.tipdiv {font-family: verdana, arial, helvetica; font-size:11px;font-style:normal;font-weight:normal;color:black;background-color:lightyellow;border:black 1px solid;padding:1px;}</style>");
document.write("<div id=popTip class=tipdiv style='position:absolute;display:none;' onmouseout='hideSelf(this, event)'></div>");

var popup_tip = null;

/*????????????title
 *m:popTip
 *tipstr:??????????title
 */
function showTip(m,tipstr) {	
	if (tipstr.length > 1) {
		if (m) {
			m.style.display='';
			m.style.left = event.clientX + document.body.scrollLeft;
			m.style.top = event.clientY + document.body.scrollTop;
			if (m.innerHTML.length<1) {
				m.innerHTML = tipstr;
			}
		}
	}
	if ((m!=popup_tip) && (popup_tip)) popup_tip.style.display = 'none';
	popup_tip = m;
}
/*????title
 *m:popTip
 */
function hideTip(m) {
	m.style.display = 'none';
	m.innerHTML = "";
}
/*
 *
 *m:this
 *e:event
 */
function hideSelf(m, e) {	
	var cx, cy;
	if (!e) {
		cx = window.event.x;
		cy = window.event.y;
	} else {
		cx = e.clientX;
		cy = e.clientY;
	}

	if (m.style.display=='') {
		if ((document.body.scrollLeft + cx >= m.offsetLeft)                
		&& (document.body.scrollLeft + cx <= m.offsetLeft + m.offsetWidth) 
		&& (document.body.scrollTop + cy >= m.offsetTop)
		&& (document.body.scrollTop + cy <= m.offsetTop + m.offsetHeight)) {
		} else {
			m.style.display = 'none';
			m.innerHTML = "";
		}
	}
}
/**
 *
*/
function overlib(str){
	showTip(popTip,str);		
}
function nd(){
	hideTip(popTip);
}