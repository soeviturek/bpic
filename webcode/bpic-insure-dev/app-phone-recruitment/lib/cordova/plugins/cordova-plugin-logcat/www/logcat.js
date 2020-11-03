cordova.define("cordova-plugin-logcat.LogCat", function(require, exports, module) {
module.exports = {
	sendLogs:function(successCB,failureCB){
		cordova.exec(successCB, failureCB, "LogCat","sendLogs", []);
	}
};


});
