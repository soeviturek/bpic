cordova.define("com.cpic.ecar.cordova.authentication", function(require, exports, module) {
	
	var exec = require('cordova/exec');
	var _this = {};
	
	_this.getAuthenticationInfo = function (successCallback, errorCallback, params) {
	    exec(successCallback, errorCallback, "Authentication", "getAuthenticationInfo", params);
	};
	
	_this.logout = function (successCallback, errorCallback, params) {
	    exec(successCallback, errorCallback, "Authentication", "logout", params);
	};
	
	_this.exit = function (successCallback, errorCallback, params) {
	    exec(successCallback, errorCallback, "Authentication", "exit", params);
	};
	
	_this.error = function (successCallback, errorCallback, params) {
	    exec(successCallback, errorCallback, "Authentication", "error", params);
	};
	
	_this.timeout = function(sucess, error, params){
		exec(sucess, error, "Authentication", "timeout", params);
	};
	//产险系统版本更新
	_this.upgrade = function(sucess, error, params){
		exec(sucess, error, "Authentication", "upgrade", params);
	};
	//CRM拍照报价图片上传参数
	_this.crmparams = function(sucess, error, params){
		exec(sucess, error, "Authentication", "crmparams", params);
	};
	
	module.exports = _this;
	
});