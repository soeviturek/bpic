cordova.define("cordova-plugin-clearcache.ClearCache", function(require, exports, module) {
var exec = require('cordova/exec');

module.exports = {
  /**
   * Exits the PhoneGap application with no questions asked.
   */
  clearCache: function(success, error) {
    exec(success, error, 'ClearCache', 'clearCache', []);
  }
};

});