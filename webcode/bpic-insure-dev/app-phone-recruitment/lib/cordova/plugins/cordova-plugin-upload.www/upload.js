cordova.define("cordova-plugin-upload.www.UpCardId", function(require, exports, module) {
	var exec = require("cordova/exec");
	module.exports = {
		upCardId: function(successCallback, errorCallback, customerId, plolicyId, seqNum, type, customerType, card_type, url){
			exec(successCallback, errorCallback, "UpCardId",  "upCardId", [customerId, plolicyId, seqNum, type, customerType, card_type, url]);
		}
	}
});