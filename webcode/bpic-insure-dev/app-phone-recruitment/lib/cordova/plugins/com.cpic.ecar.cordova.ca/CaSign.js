cordova.define("com.cpic.ecar.cordova.ca", function(require, exports, module) {
	
	var exec = require('cordova/exec');
	var _this = {};
	
	_this.init = function (successCallback, errorCallback,params) {
	    exec(successCallback, errorCallback, "CaSign", "initCA", params);
	};
	
	_this.sign = function (successCallback, errorCallback,params) {
	    exec(successCallback, errorCallback, "CaSign", "sign", params);
	};
	
	_this.getEncrypt = function (successCallback, errorCallback,params) {
	    exec(successCallback, errorCallback, "CaSign", "getEncrypt", params);
	};
	
	_this.deleteTempImg = function (successCallback, errorCallback,params) {
	    exec(successCallback, errorCallback, "CaSign", "deleteTempImg", params);
	};
	
	module.exports = _this;
	
});