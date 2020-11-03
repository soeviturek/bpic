/*(function(){
    function w() {
        var r = document.documentElement;
        var a = parseInt(r.getBoundingClientRect().width);
		console.log(a);
        if (!a) return;
        rem = a / 10.24;
		if(rem >= 100) rem = 100;
		r.style.fontSize = rem + "px"
    }
    var t;
    w();
    window.addEventListener("resize", function() {
        clearTimeout(t);
        t = setTimeout(w, 300)
    }, false);
})();*/

//获取路径参数
$.getHash = function(key, url) {
	var hash;
	if (!!url) {
		hash = url.replace(/^.*?[#](.+?)(?:\?.+)?$/, "$1");
		hash = (hash == url) ? "" : hash;
	} else {
		hash = self.location.hash;
	}

	hash = "" + hash;
	hash = hash.replace(/^[?#]/, '');
	hash = "&" + hash;
	var val = hash.match(new RegExp("[\&]" + key + "=([^\&]+)", "i"));
	if (val == null || val.length < 1) {
		return null;
	} else {
		return decodeURIComponent(val[1]);
	}
}

//提示气泡(zwt)
var isTip = false;
function showTips(tips, callback) {
	if(isTip) {
		return;
	}
	isTip = true;
	var tipsDiv = '<div id="_tips"><div id="tips-box">' + tips + '</div></div>';
	$('body').append(tipsDiv);
	$('#_tips').css('width', '100%');
	$('#_tips').css('height', '100%');
	$('#_tips').css('position', 'absolute');
	$('#_tips').css('top', '0');
	$('#_tips').css('left', '0');
	$('#tips-box').css('width', '64%');
	$('#tips-box').css('line-height', '.5rem');
	$('#tips-box').css('padding', '.15rem .2rem');
	$('#tips-box').css('background-color', '#000000');
	$('#tips-box').css('border-radius', '.8rem');
	$('#tips-box').css('text-align', 'center');
	$('#tips-box').css('font-size', '0.3rem');
	$('#tips-box').css('color', '#FFFFFF');
	$('#tips-box').css('position', 'absolute');
	$('#tips-box').css('top', '50%');
	$('#tips-box').css('right', '18%');
	$('#tips-box').css('opacity', '0.8');
	$('#tips-box').css('filter', 'alpha(opacity=80)');
	$('#tips-box').css('z-index', '9999');
	setTimeout(function() {
		$("#_tips").remove();
		$("#tips-box").remove();
		isTip = false;
		if(callback){
			callback();
		}
	}, (1500));
}

$.popup = function(obj, info){
	var init = {
		width : 200,
		zIndex : 0, //层级
		btnConfirm : function(){ return true; }, //确定按钮事件
		btnCancel : function(){ return true; } //取消按钮事件
	}
	var $obj = typeof obj == 'string' ? $(obj) : '',
		info = info || init;
		width = info.width || init.width,
		zIndex = info.zIndex || init.zIndex,
		btnConfirm = info.btnConfirm || init.btnConfirm,
		btnCancel = info.btnCancel || init.btnCancel;
		
	if($obj.size() == 1){
		if($('.popup_bg').size() <= 0){
			$('body').append('<div class="popup_bg"></div>');
			$('.popup_bg').bind('click',function(){
				$(this).hide();
				$obj.hide();
			});
		}
		var $popup_bg = $('.popup_bg');
		if(width != 200){
			$obj.width(width);
		}
		$obj.show();
		$popup_bg.each(function(index, element) {
			zIndex == 0 ? $(this).show() : '';
		});
		$obj.find('.popup_submit').bind('click', function(){
			if(btnConfirm()){
				$obj.hide();
				zIndex == 0 ? $popup_bg.hide() : $popup_bg.removeAttr('style').show();
			}
		});
		$obj.find('.popup_close').bind('click', function(){
			if(btnCancel()){
				$obj.hide();
				zIndex == 0 ? $popup_bg.hide() : $popup_bg.removeAttr('style').show();
			}
		});
	}else{
		return false;
	}
};
$.popdown = function(obj, fun){
	var $obj = typeof obj == 'string' ? $(obj) : '',
		$popup_bg = $('.popup_bg'),
		fun = fun || function(){};
	
	if($obj.size() == 1){
		$popup_bg.hide();
		$obj.hide();
		fun();
	}
}

$.fn.prevAll = function(selector){
	var prevEls = [];
	var el = this[0];
	if(!el) return $([]);
	while (el.previousElementSibling) {
		var prev = el.previousElementSibling;
		if (selector) {
		if($(prev).is(selector)) prevEls.push(prev);
	}
	else prevEls.push(prev);
		el = prev;
	}
	return $(prevEls);
};
 
$.fn.nextAll = function (selector) {
	var nextEls = [];
	var el = this[0];
	if (!el) return $([]);
		while (el.nextElementSibling) {
		var next = el.nextElementSibling;
		if (selector) {
		if($(next).is(selector)) nextEls.push(next);
	}
	else nextEls.push(next);
		el = next;
	}
	return $(nextEls);
};

Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

$.getDates = function(date, type){
	var _date = new Date(date.replace(/\-/g,'/'));
	return _date.Format(type);
};