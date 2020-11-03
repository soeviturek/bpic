var common = common || {};
common.loading = {};

common.getServer=function(){
    return window.location.protocol + '//' + window.location.host;
};

common.getMathRand = function(num){
	var _val = '';
	if(typeof num !== 'number'){
		return '00000';
	} 
	for(var i=0; i<num; i++){ 
		_val += Math.floor(Math.random()*10); 
	}
	return _val;
};

common.getRandomCode = function(){
	var date = new Date();
	return date.getTime() + common.getMathRand(5);
};

// common.getSignature = function(randomcode){
// 	var openname = 'fwd';
// 	var openkey = 'fairyland';
// 	return $.md5($.md5(openname + openkey) + randomcode);
// }

/*脱敏处理*/
common.emdr = function(idCard){
	var str='';
	if(idCard){
		idCard.replace(/^(\w{6})(\w*)(.{4})$/,function(all,u1,u2,u3){
  			str =	u1 + new Array(u2.length+1).join('*') + u3;
		});
	}
	return str;
}

common.tipSystem = function(params){
	var options = {
		html: params.html,
		timeout: params.timeout || 0,
		callback: function(data){}
	};
	var init = $.extend(options, params);
	var _html = '<div class="tip-system">\
		<p><i class="icon-tips"></i>'+ init.html +'</p>\
		<button type="button" class="btn-close"><i class="icon-close"></i></button>\
	</div>';
	$('body').append(_html);
	
	var _obj = $('.tip-system');
	_obj.animate({opacity:1}, 300);
	_obj.on('click', '.btn-close', function(){
		$.isFunction(init.callback) && init.callback(1);
		$(this).parents('.tip-system').remove();
	});
	if(init.timeout > 0){
		setTimeout(function(){
			$.isFunction(init.callback) && init.callback(1);
			_obj.animate({opacity:0}, 500, function(){
				$(this).remove();
			});
		}, init.timeout);
	}
	
};

/*
 *confirm提示框
 * common.confirmCreate({
 * 		html: '<p>是否需要电话咨询律师，<br>指导事故处理?</p>',
 * 		callback: function(status) {
 *				if(status == '1'){
 *					alert('是');
 *				}else{
 * 					alert('否');
 * 				}
 *		}
 * });
 * */
common.confirmCreate = function(params) {
	var options = {
			html: params.html,
			callback: function() {},
			cancelBack: function() {}
		};

	var pa = $.extend({}, options, params);
	var _popup = '\
		<div class="popup-box popup-confirm popup-confirm-mobile">\
			<div class="popup-header">\
				<h2>提示</h2>\
			</div>\
			<div class="popup-content">\
				<div class="text">'+ pa.html +'</div>\
				<div class="button">\
					<button type="button" class="btn btn-lg btn-default btn-confirm-cancel">否</button>\
					<button type="button" class="btn btn-lg btn-submit btn-confirm-confirm">是</button>\
				</div>\
			</div>\
		</div>';
	$("body").append($(_popup));
	common.popup('.popup-confirm');
	$('.btn-confirm-cancel').one('tap', function(){
		common.closePopup('.popup-confirm');
		$('.popup-confirm').remove();
		$.isFunction(pa.cancelBack) && pa.cancelBack();
	});
	$('.btn-confirm-confirm').one('tap', function(){
		common.closePopup('.popup-confirm');
		$('.popup-confirm').remove();
		$.isFunction(pa.callback) && pa.callback();
	});
};

common.confirmCallBackCreate = function(params) {
	var options = {
			html: params.html,
			callback: function() {},
			cancelBack: function() {}
		};

	var pa = $.extend({}, options, params);
	var _popup = '\
		<div class="popup-box popup-cb-confirm">\
			<div class="popup-header">\
				<h2>提示</h2>\
			</div>\
			<div class="popup-content">\
				<div class="text">'+ pa.html +'</div>\
				<div class="button">\
					<button type="button" class="btn btn-lg btn-default btn-cb-cancel">否</button>\
					<button type="button" class="btn btn-lg btn-submit btn-cb-confirm">是</button>\
				</div>\
			</div>\
		</div>';
	$("body").append($(_popup));
	common.popup('.popup-cb-confirm');
	$('.btn-cb-cancel').one('tap', function(){
		$.isFunction(pa.cancelBack) && pa.cancelBack();
		common.closePopup('.popup-cb-confirm');
		$('.popup-cb-confirm').remove();
	});
	$('.btn-cb-confirm').one('tap', function(){
		$.isFunction(pa.callback) && pa.callback();
		common.closePopup('.popup-cb-confirm');
		$('.popup-cb-confirm').remove();
	});
};

/*
 *alert提示框
 * common.alertCreate({
 * 		html: '<p>是否需要电话咨询律师，<br>指导事故处理?</p>',
 * 		callback: function(status) {
 *		}
 * });
 * */
common.alertCreate = function(params) {
	var options = {
			html: params.html,
			callback: function() {}
		};

	var pa = $.extend({}, options, params);

	// 防止弹框底部按钮消失
	// if($('.popup-confirm').size() > 0){
	// 	$('.popup-confirm').remove();
	// }
	
	var _popup = '\
		<div class="popup-box popup-confirm">\
			<div class="popup-content">\
				<div class="text">'+ pa.html +'</div>\
				<div class="button">\
					<button type="button" class="btn btn-lg btn-submit btn-confirm-confirm">确定</button>\
				</div>\
			</div>\
		</div>';
	$("body").append($(_popup));
	common.popup('.popup-confirm');
	
	$('.btn-confirm-confirm').one('tap', function(){
		common.closePopup('.popup-confirm');
		$('.popup-confirm').remove();
		$.isFunction(pa.callback) && pa.callback(1);
	});
	
}

/*
 *alert提示框(无确定按钮)
 * common.alertCreate({
 * 		html: '<p>是否需要电话咨询律师，<br>指导事故处理?</p>',
 * 		callback: function(status) {
 *		}
 * });
 * */
common.alertOnlyCreate = function(params) {
	var options = {
			html: params.html,
			callback: function() {}
		};

	var pa = $.extend({}, options, params);

	// 防止弹框底部按钮消失
	// if($('.popup-confirm').size() > 0){
	// 	$('.popup-confirm').remove();
	// }
	
	var _popup = '\
		<div class="popup-box popup-confirm">\
			<div class="popup-content">\
				<div class="text">'+ pa.html +'</div>\
			</div>\
		</div>';
	$("body").append($(_popup));
	common.popup('.popup-confirm');
	
	$('.btn-confirm-confirm').one('tap', function(){
		common.closePopup('.popup-confirm');
		$('.popup-confirm').remove();
		$.isFunction(pa.callback) && pa.callback(1);
	});
	
}

//加载框
common.loading.create = function(msg) {
	var h = new Array();
	h.push("<div id='cpic_ui_mask1' type='loading_type' class='cpic_ui_mask' style='display:none'></div>");
	h.push("<div id='cpic_ui_loading1' type='loading_type' style='display:none;position:fixed;z-index:6000;text-align:center;'><div class='cpic_ui_loading' style='left:50%;margin-left:-95px'></div><div id='loadingMessage' style='margin-top:5em;color:#bbb'>" + msg + "</div></div>");
	h = $(h.join(""));
	$("body").append(h);
};

common.isNull = function(value) {
	return typeof value == 'undefined' || value === "" || value == null || value == undefined;
};
common.isNotNull = function(value) {
	return !common.isNull(value);
};
common.loading.open = function(msg) {
	if(common.isNull(msg)) {
		msg = "";
	} else {
		msg += "...";
	}
	var _html = '<div class="system-loading"><p></p></div>';
	$('body').append(_html);
};
common.loading.refresh = function() {
	$win = $(window);
	var ret = {
		top: (($win.height() - 180) / 2 + 5) + "px",
		//left : (($win.width()-100)/2)+"px",
		display: 'block'
	};
};
common.loading.close = function() {
	$('.system-loading').animate({opacity:0}, 500, function(){
		$(this).remove();
	})
};

/**
 * 加密方法
 */
common.encrypt = function(key, value) {
	localStorage.setItem(key, (Base64.encode(value)));
};

/**
 * 解密方法
 */
common.decipher = function(key) {
	var rerunValue = localStorage.getItem(key);
	if(common.isNull(rerunValue)) {
		rerunValue = null;
	} else {
		rerunValue = Base64.decode(rerunValue);
	}

	return rerunValue;
};

common.isRunningInApp = function() {
	function getCookie(name) {
		var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
		if(arr = document.cookie.match(reg)) {
			var cookie = '' + arr[2] + '';
			if(cookie == 'null') {
				return 'false';
			}
			return unescape(arr[2]);
		} else {
			return 'false';
		}
	}

	//判断是否在androidApp
	var isAndroidApp = window.javaBridgeInterface &&
		window.javaBridgeInterface.getClientOS("android").toLocaleLowerCase() == 'android';

	var isiPhoneApp = getCookie('isiPhoneApp');
	if("false" == isiPhoneApp) {
		isiPhoneApp = false;
	}
	if('true' == isiPhoneApp) {
		isiPhoneApp = true;
	}

	if(isAndroidApp || isiPhoneApp) {
		//在APP中运行
		return true;
	}
	return false;
}

/**二秒后消失的弹出框
 *@param message:消息内容，只传字符串
 */
common.Prompt = function(message) {
	
	//创建prompt框
	var h = $('<div class="tip-short" id="e_ui_prompt">' + message + '</div>');
	$('body').append(h);

	//改变透明度
	var i = 0;
	var t = setInterval(function() {
		i = i + 0.1;
		if(i >= 1) {
			clearInterval(t);
			i = 0;
		} else {
			$(h).css('opacity', i);
		}
	}, 80);

	//2秒后消失
	setTimeout(function() {
		var j = $(h).css('opacity');
		var ti = setInterval(function() {
			j = j - 0.1;
			if(j <= 0) {
				clearInterval(ti);
				$(h).remove();
			} else {
				$(h).css('opacity', j);
			}
		}, 80);
	}, 4000);
};

common.forbidInputEmoji = function() {

    $('input').on('keyup', function() {
        if(isEmojiCharacter(this.value)) {
            this.value = '';
        }
    });

	$('input').on('paste', function() {
		if(isEmojiCharacter(this.value)) {
			this.value = '';
		}
	});

	$('input').on('contextmenu', function() {
		if(isEmojiCharacter(this.value)) {
			this.value = '';
		}
	});
};

//设置本地缓存
common.setLocalStorage = function(key, value, isJson) {
	if(window.localStorage) {
		if(isJson) {
			value = JSON.stringify(value);
		}
		window.localStorage[key] = value;
	} else {
		console.log("当前浏览器不支持localStorage");
	}
};

//获取本地缓存
common.getLocalStorage = function(key, isJson) {
	if(window.localStorage) {
		var value = window.localStorage[key] || "";
		if(isJson && value) {
			value = JSON.parse(value);
		}
		return value;
	} else {
		console.log("当前浏览器不支持localStorage");
	}
};

//清除本地缓存
common.removelocalStorage = function(key) {
	if(window.localStorage) {
		window.localStorage.removeItem(key);
	}
};

common.cleanLocalStorage = function () {
    if(window.localStorage) {
        window.localStorage.clear();
    }
};

//设置运行时缓存
common.setSessionStorage = function(key, content) {
	if(window.sessionStorage) {
		window.sessionStorage[key] = content;
	} else {
		console.log('您的浏览器不支持setSessionStorage方法');
	}
};

//获取运行时缓存
common.getSessionStorage = function(key) {
	if(window.sessionStorage) {
		return window.sessionStorage[key] || '';
	} else {
		console.log('您的浏览器不支持getSessionStorage方法');
	}
};

//清除运行时缓存
common.removeSessionStorage = function(key) {
	if(window.sessionStorage) {
		window.sessionStorage.removeItem(key);
	}
};

//获取某个cookie
common.getCookie = function(cookie_name, decode) {
	decode = decode || true;
	var allcookies = document.cookie;
	var cookie_pos = allcookies.indexOf(cookie_name);
	var value = '';
	if(cookie_pos > -1) {
		cookie_pos += cookie_name.length + 1;
		var cookie_end = allcookies.indexOf(";", cookie_pos);
		if(cookie_end == -1) {
			cookie_end = allcookies.length;
		}
		value = allcookies.substring(cookie_pos, cookie_end);
		if(decode) {
			try {
				value = decodeURIComponent(value);
			} catch(e) {
				console.log(e);
				return "";
			}
		}
	}
	return value;
};

//设置某个cookie
common.setCookie = function(cookie_name, cookie_value, domain, isencode, expTime) {
	var exp = new Date();
	var expires = "";
	if(expTime) {
		exp.setTime(exp.getTime() + expTime);
		expires = ";expires=" + exp.toGMTString();
	}
	if(!domain) {
		domain = document.domain;
		if(/^[a-z]/i.test(document.domain)) {
			domain = document.domain.substring(document.domain.indexOf("."));
		}
	}

	isencode = typeof isencode == 'undefined' ? true : isencode;
	if(isencode) {
		cookie_value = encodeURIComponent(cookie_value);
	}
	document.cookie = cookie_name + "=" + cookie_value + "; path=/; domain=" + domain + ";" + expires;
};

/**
 *得到地址栏参数
 *@param names 参数名称
 *@param urls 从指定的urls获取参数
 *@param cn 是否中文
 *@returns string
 **/
common.getQueryString = function(names, urls, cn) {
	if(urls) {
		if(urls.indexOf('?') > -1) {
			urls = urls.substring(urls.indexOf('?') + 1);
		} else {
			return '';
		}
	} else {
		urls = window.location.search.substr(1);
	}

	var reg = new RegExp("(^|&)" + names + "=([^&]*)(&|$)", "i");
	var r = urls.match(reg);
	if(r && r[2]) {
		var ms = r[2].match(/(\<)|(\>)|(%3C)|(%3E)/g);
		if(ms && ms.length >= 4) {
			//如果检测到有2对及以上开始和结束尖括号
			r[2] = r[2].replace(/(\<)|(%3C)/g, '');
		}
		if(cn) {
			return r[2];
		}
		return unescape(r[2]);
	}
	return '';
};

//获取路径参数 #
common.getHash = function(key, url){
	var hash;
	if(!!url) {
		hash = url.replace(/^.*?[#](.+?)(?:\?.+)?$/, "$1");
		hash = (hash == url) ? "" : hash;
	} else {
		hash = self.location.hash;
	}

	hash = "" + hash;
	hash = hash.replace(/^[?#]/, '');
	hash = "&" + hash;
	var val = hash.match(new RegExp("[\&]" + key + "=([^\&]+)", "i"));
	if(val == null || val.length < 1) {
		return null;
	} else {
		return decodeURIComponent(val[1]);
	}
}

//获取路径参数 ?
common.getSearch = function(key, url){
	var hash;
	if(!!url) {
		hash = url.replace(/^.*?[#](.+?)(?:\?.+)?$/, "$1");
		hash = (hash == url) ? "" : hash;
	} else {
		hash = self.location.search;
	}
	

	hash = "" + hash;
	hash = hash.replace(/^[?#]/, '');
	hash = "&" + hash;
	var val = hash.match(new RegExp("[\&]" + key + "=([^\&]+)", "i"));
	if(val == null || val.length < 1) {
		return null;
	} else {
		return decodeURIComponent(val[1]);
	}
}

//获取路径参数 ?
common.getQuery = function(name) {
	var u = window.location.search.slice(1);
	var re = new RegExp(name + '=([^&\\s+]+)');
	var m = u.match(re);
	return m ? m[1] : '';
};

//获取当前运行平台
function getPlatform() {

	var platforms = {
		amazon_fireos: /cordova-amazon-fireos/,
		android: /Android/,
		ios: /(iPad)|(iPhone)|(iPod)/,
		blackberry10: /(BB10)/,
		blackberry: /(PlayBook)|(BlackBerry)/,
		windows8: /MSAppHost/,
		windowsphone: /Windows Phone/
	};

	for(var key in platforms) {
		if(platforms[key].exec(navigator.userAgent)) {
			return key;
		}
	}
	return "";
};

//是否在Android壳子中运行
common.isAndroidApp = function() {
	var result = false;
	if(window.javaBridgeInterface && window.javaBridgeInterface.getClientOS("android").toLocaleLowerCase() === 'android') {
		console.log('当前运行环境是在Android壳子中运行');
		result = true;
	}

	//如果判断失败，再次判断所属平台
	if(!result) {
		if(getPlatform() == "android") {
			result = true;
		} else {
			result = false;
		}
	}
	return result;
}();

//是否在iPhone壳子中运行
common.isiPhoneApp = function() {
	var result = common.getCookie('isiPhoneApp', false) || "false";
	if(result != "true") {
		if(typeof(getClientOS) == 'function' && getClientOS().toLocaleLowerCase() == 'ios') {
			result = "true";
			console.log('当前运行环境是在iOS壳子中运行');
		} else if(window.navigator.userAgent.indexOf(",cpic_zt_ios") > -1) {
			//2015-10-21 目前已通过userAgent解决此问题，考虑到用户不会选择更新的问题此代码开放最少等到最新版ios上线appstore1个月后
			result = "true";
		} else {
			result = (common.getQueryString("e") == "1").toString(); //1==ios
		}
	}

	//如果判断失败，再次判断所属平台
	if(result != "true") {
		if(getPlatform() == "ios" && isApp()) {
			result = "true";
		} else {
			result = "false";
		}
	}

	//写入cookie
	common.setCookie('isiPhoneApp', result);
	return result == "true" ? true : false;
}();
//当前是否在壳子中运行
common.isRunByApp = function() {
	return common.isAndroidApp || common.isiPhoneApp;
}();

/**
 * 是否运行在APP中
 */
function isApp() {
	var result = true;

	var userAgent = window.navigator.userAgent;

	//浏览器
	if(userAgent.indexOf('Safari') >= 0) {
		result = false;
	}

	if(result) {
		//微信
		if(userAgent.indexOf('MicroMessenger') >= 0) {
			result = false;
		}
	}

	if(result) {
		//QQ
		if(userAgent.indexOf('QQ') >= 0) {
			result = false;
		}
	}

	if(result) {
		//支付宝
		if(userAgent.indexOf('AliApp') >= 0) {
			result = false;
		}
	}

	if(result) {
		//微博
		if(userAgent.indexOf("Weibo") >= 0 && userAgent.indexOf("_weibo_") >= 0) {
			result = false;
		}
	}

	return result;
}
//调用壳子缓存token
common.setTokenCookie = function(){
	if(common.isRunByApp){
			try{
				//清空token值
				sharedPref.save(function(){
				//成功回调
				}, function(error){
					showTips(error);
					console.log(error);
					//失败回调
				},{
					key:'ACCESS_TOKEN',
					str:''
				});
			
			}catch(e){
				alert(e);
				showTips(e);
				console.log(e);
			}
		}
}
//事故申请进度状态值
common.applicationProgress = {
	'0':'申请',
	'1':'律师电话指导',
	'2':'待完善资料',
	'3':'待匹配律师',
	'4':'律师受理',
	'5':'处理中',
	'6':'理赔',
	'7':'评价',
	'8':'完成'
};
common.changeApplicationProgress = function(status){
	return common.applicationProgress[status];
}

/* 上传图片 */
common.uploadPic = function(info, callback){
	info = info || {};
	var init = {
		width : 200,
		height : 200,
		quality : 50,
		type : 0, //0: 单张,  1: 多张
		maxImages : 1,
		allowEdit: 0 //0:不可编辑  ， 1： 可编辑
	};
	init.width = info.width || init.width;
	init.height = info.height || init.height;
	init.quality = info.quality || init.quality;
	init.type = info.type || init.type;
	init.maxImages = info.maxImages || init.maxImages;
	init.allowEdit = info.allowEdit || init.allowEdit;
	if(actionsheet){
		var options = {
			androidTheme: actionsheet.ANDROID_THEMES.THEME_DEVICE_DEFAULT_LIGHT, // default is THEME_TRADITIONAL
			title: '选择图片',
			subtitle: '', // supported on iOS only
			buttonLabels: ['从图库选择', '拍照'],
			androidEnableCancelButton: true, // default false
			winphoneEnableCancelButton: true, // default false
			addCancelButtonWithLabel: '取消',
			position: [20, 0], // for iPad pass in the [x, y] position of the popover
			destructiveButtonLast: true // you can choose where the destructive button is shown
		};
		actionsheet.show(options, function(bIndex){
			if(init.type == 0){  //单选
				if (bIndex != 3) {
					common.imagePick(bIndex, init, function(data){
						callback(data);
					});
				}
			} else {  //多选
				switch (bIndex){
					case 1:
						imagePicker.getPictures(function(result) {
							var data = {
								fileList: result,
								resultCode: 1
							};
							callback(data);
						}, function(error) {
							var data = {
								message: error,
								resultCode: 0
							};
							callback(data);
						}, init);
					break;
					case 2:
						common.imagePick(bIndex, init, function(data){
							var newFileList = {
								resultCode : data.resultCode,
								fileList : [
									{src: data.fileList.src, name:data.fileList.name}
								]	
							}
							callback(newFileList);
						});
					break;
				}
			};
		});
	} else {
		showTips('接口调用错误！Step: 1');
	}
};

common.imagePick = function(bIndex, init, callback) {
	camera.getPicture(function(data){
		var fileList = JSON.parse(data);
		callback({fileList: fileList, resultCode: 1});
	}, function(error){
		callback({message: error, resultCode: 0});
	}, {
		quality: init.quality,
		destinationType: Camera.DestinationType.DATA_URL,
		sourceType: bIndex - 1,
		targetWidth: init.width,
		targetHeight: init.height,
		encodingType: Camera.EncodingType.JPEG,
		allowEdit: init.allowEdit

	});
};

common.popup = function(obj){
	if($(obj).size() <= 0){
		return false;
	}
	if($('.popup-bg').size() <= 0){
		var popupBg = '<div class="popup-bg"></div>';
		$('body').append(popupBg);
	} else {
		common.deleteMask('.popup-bg');
	}

	//$('.popup-box').hide();
	
	$('.popup-bg').insertBefore($(obj));
	$('.popup-bg').eq(0).show();/*.one('tap', function(){
		common.closePopup(obj);
	});*/
	if($(obj).hasClass('popup-confirm')){
		$('.popup-bg').css('z-index', 1009).show();
	};
	
	$(obj).addClass('show').show();
	
	$(obj).off('click', '.popup-close');
	$(obj).on('click', '.popup-close', function(){
		common.closePopup(obj);
	});
};
common.closePopup = function(obj){
    common.deleteMask('.popup-bg');
	if($(obj).size() <= 0){
		return false;
	}
	$(obj).removeClass('show').animate({opacity:0}, 350, function(){
		$(this).hide().css('opacity', 1);
	});
	if($('.popup-box.show').size() > 0){
		$('.popup-bg').insertBefore($('.popup-box.show'));
		$('.popup-bg').css('z-index', 999);
	}else{
		$('.popup-bg').animate({opacity:0}, 350, function(){
			$(this).hide().css({
				'opacity': 1,
				'z-index': 999
			});
		});
	}
};

/* 计算年龄 */
common.getAge = function(strBirthday){
	if(!strBirthday){
		return '';
	}
	var returnAge;  
    var strBirthdayArr = strBirthday.split("-");  
    var birthYear = strBirthdayArr[0];  
    var birthMonth = strBirthdayArr[1];  
    var birthDay = strBirthdayArr[2];  
      
    d = new Date();  
    var nowYear = d.getFullYear();  
    var nowMonth = d.getMonth() + 1;  
    var nowDay = d.getDate();  
      
    if(nowYear == birthYear){  
        returnAge = 0;//同年 则为0岁  
    }  
    else{  
        var ageDiff = nowYear - birthYear ; //年之差  
        if(ageDiff > 0){  
            if(nowMonth == birthMonth) {  
                var dayDiff = nowDay - birthDay;//日之差  
                if(dayDiff < 0){  
                    returnAge = ageDiff - 1;  
                } else {  
                    returnAge = ageDiff ;  
                }  
            }  
            else  
            {  
                var monthDiff = nowMonth - birthMonth;//月之差  
                if(monthDiff < 0){  
                    returnAge = ageDiff - 1;  
                } else {  
                    returnAge = ageDiff ;  
                }  
            }  
        } else {  
            returnAge = -1;//返回-1 表示出生日期输入错误 晚于今天  
        }  
    }
    return returnAge;//返回周岁年龄  
}

/* 千位分隔符 */
common.separator = function(num){ 
  return num && num
        .toString()
        .replace(/(\d)(?=(\d{3})+\.)/g, function($0, $1) {
            return $1 + ",";
        });
};

/* 转换表单Array为JSON */
common.getFormJson = function(array){
	var _json = {};
	for(var i in array){
		_json[array[i].name] = array[i].value;
	};
	return _json;
}

/* 金额转化为汉字单位 */
common.SectionToChinese = function (num) {
	var _num = String(num);
	var _val = '';
	switch (_num.length){
		case null:
		case 'undefined':
		case 0:
			return '';
		break;
		case 1:
		case 2:
			return _num + '元';
		break;
		case 3:
			return _num/100 + '百';
		break;
		case 4:
			return _num/1000 + '千';
		break;
		default:
			return _num/10000 + '万';
		break;
	}
	return _val;
}

/* 格式化日期 */
common.getFormatDate = function (date) {
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate();

	return year + '-' + month + '-' + day;
};

/* 招募模态层bug */
common.deleteMask = function (className) {
	if($(className).length === 1) {
		return false;
	}
	while($(className).length > 1) {
        $(className).eq(0).remove();
	}
};

/* 判断滚动条是否存在 */
common.hasScrolled = function (element) {
	 element.scrollTop(10);//控制滚动条下移10px  
    if( element.scrollTop()>0 ){  
    	element.scrollTop(0);//滚动条返回顶部 
        return true;
    }else{  
        element.scrollTop(0);//滚动条返回顶部 
        return false;
    }



};

//补零
common.addZero=function(n){
	return n>=10?n:'0'+n;
};

//获取标准时间
common.getStaTime=function(timeStamp){
	var date= new Date(timeStamp);
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var day = date.getDate();
    return year + '-' + common.addZero(month) + '-' + common.addZero(day);
};

//获取标准时间 hh:dd
common.getCurTime=function(timeStamp){
	var str = "--";
	str = timeStamp.substring(11,16);
    return  str;
};

//获取标准时间 mm-dd
common.getCurDate=function(timeStamp){
	var str = "--";
	str = timeStamp.substring(5,10);
    return str;
};

common.cutDate = function (val) {
	if (!/-/g.test(val)) {
		return val;
	}
	var ret = val.split('-');
	return ret[0] + '-' + ret[1];
}

//获取职级value
common.getPostName=function(resultData,val){
	      var post = ''
          $.each(resultData,function(i,value){
　　				if(value.code==val){
					post = value.value;
				}
			});
          return post;
};


$(function () {
	String.prototype.replaceAll=function (older,newer) {
		return this.replace(new RegExp(older,'gm'),newer)
    };
    Array.prototype.indexOf = function (val) {
        for (var i = 0; i < this.length; i++) {
            if (this[i] == val) return i;
        }
        return -1;
    };

    Array.prototype.remove = function (val) {
        var index = this.indexOf(val);
        if (index > -1) {
            this.splice(index, 1);
        }
    };

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


    //阻止ios双指缩放
    document.addEventListener('touchstart',function (event) {
        if(event.touches.length>1){
            event.preventDefault();
        }
    })
    var lastTouchEnd=0;
    document.addEventListener('touchend',function (event) {
        var now=(new Date()).getTime();
        if(now-lastTouchEnd<=300){
            event.preventDefault();
        }
        lastTouchEnd=now;
    },false)

});



