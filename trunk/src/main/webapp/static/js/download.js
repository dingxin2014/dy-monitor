var __down__target="downloadFormIframe";
// add iframe for ajax file upload and download
$(document).ready(function() {
	__down__target = __down__target+Math.round((Math.random()*100000000));
	$('<iframe src="javascript:\'\'" style="position:absolute; left:-10000px; top:-10000px; visibility:hidden;" id="'+__down__target+'" name="'+__down__target+'"></iframe>')
			.appendTo("body");
	// do not focus the frist input filed
	try {
		top.document.body.focus();
	} catch (e) {
		// no field need focus
	}
});

function ajaxDownload(url,params){
	var p = params;
	if(params){
		p = "";
		if(typeof(params) == "object"){
			for(var k in params){
				if(k){
					p = p+k+"="+encodeURIComponent(params[k])+"&";
				}
			}
		}
	}
	if(p){
		if(url.indexOf("?") > 0){
			url = url+"&"+p;
		}else{
			url = url+"?"+p;
		}
	}
	document.location.href.target=__down__target;
	document.location.href=url;
}