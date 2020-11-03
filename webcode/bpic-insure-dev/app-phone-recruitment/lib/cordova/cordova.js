// JavaScript Document
//文件开始包含类似以下注释说明
/********************************************
// 文件名：cordova.js
// 文件功能描述：cordova初始化载入脚本
// 创建人：Lincoln
// 创建时间：2014-11-07
// 描述：
// 修改人：
// 修改时间：
// 修改描述： 
//******************************************/
//文件功能描述着重于描述文件功能与说明，详情应在类的注释中描述。
//一天内有多个修改只需做做一个修改标识，在所有的代码修改处加上

var Cordova= Cordova || {};

;(function($){
	var _this=this;
	
	//标示cordova准备是否完成
	Cordova.cordovaComplete=false;
	
    if(typeof $ =='undefined'){
        throw "Zepto is not function";
        return;
    }
    
    function getCordovaPath() {
        var path = null;
        var scripts = document.getElementsByTagName('script');
        var term = 'cordova.js';
        for (var n = scripts.length-1; n>-1; n--) {
            var src = scripts[n].src;
            if (src.indexOf(term) == (src.length - term.length)) {
                path = src.substring(0, src.length - term.length);
                break;
            }
        }
        return path;
    }
    
    function injectScript(url, onload, onerror) {
        var script = document.createElement("script");
        // onload fires even when script fails loads with an error.
        script.onload = onload;
        script.onerror = onerror || onload;
        script.src = url;
        document.head.appendChild(script);
    }



    /**
     * 初始化方法
     */
    this._init=function(callback){
        var cordovaPath = getCordovaPath();
		if(window.Cordova.isAndroidApp || window.Cordova.isiPhoneApp){
			//如果是在壳中运行，需要加载cordova
			if(window.Cordova&& (window.Cordova.isAndroidApp)){
				injectScript(cordovaPath+'cordova_android.js');
			}else{
				injectScript(cordovaPath+'cordova_ios.js');
			}
		}
    }
    
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
        for (var key in platforms) {
            if (platforms[key].exec(navigator.userAgent)) {
                return key;
            }
        }
        return "";
    };

    /**
	 * 当前是否在Android壳子中运行
	 */
	this.isAndroidApp = function() {
		var result = false;
		if(window.javaBridgeInterface && window.javaBridgeInterface.getClientOS("android").toLocaleLowerCase() == 'android') {
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
    
    /**
     * 当前是否在iPhone壳子中运行
     */
    this.isiPhoneApp=function(){
        var result=false;
        if(getPlatform() == "ios"&&(!/Safari/.test(navigator.userAgent))){
            result=true;
        }
    	return result;
    }();
    
    /**
     * 当前是否在壳子中运行
     */
    this.isRunByApp=function(){
        return _this.isAndroidApp||_this.isiPhoneApp;
    }();

   
    
    $(function(){
        /**
         * 执行初始化方法
         */
        _this._init();
    });

}).call(Cordova,Zepto);
/**
 * 捕获JS异常信息
 */
window.onerror=function(sMessage,sUrl,sLine){
	console.log("js异常消息:"+sMessage+",来自:"+sUrl+"第"+sLine+"行");
}

/**
 * cordova加载完成事件捕获
 */
window.addEventListener("deviceready",function(){
	Cordova.cordovaComplete=true;
	navigator.splashscreen.hide();
},false);
