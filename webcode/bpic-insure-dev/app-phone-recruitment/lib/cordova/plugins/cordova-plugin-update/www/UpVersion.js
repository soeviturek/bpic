cordova.define("cordova-plugin-update.UpVersion", function(require, exports, module) {
var exec = require('cordova/exec');

module.exports = {
  /**
   * Exits the PhoneGap application with no questions asked.
   */
  update: function(successCallback, errorCallback,options) {
	  exec(successCallback, errorCallback, "UpVersion", "update", [options]);
	}
};


});
