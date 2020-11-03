cordova.define("cordova-plugin-exitapp.exitApp", function(require, exports, module) {
var exec = require('cordova/exec');

module.exports = {
  /**
   * Exits the PhoneGap application with no questions asked.
   */
  exitApp: function() {
    exec(null, null, 'ExitApp', 'exitApp', []);
  },

  startOtherApp: function(success, fail, options) {
      options = options || {};

       options.action = options.action;
              options.receiveKey = options.receiveKey ;
              options.params = options.params ;
      exec(success, fail, 'ExitApp', 'startOtherApp', [options]);
    },

  backThisApp: function(success, fail, options) {
        options = options || {};

        options.setKey = options.setKey ;
        options.params = options.params ;
       exec(success, fail, 'ExitApp', 'backThisApp', [options]);
   }
};

});
