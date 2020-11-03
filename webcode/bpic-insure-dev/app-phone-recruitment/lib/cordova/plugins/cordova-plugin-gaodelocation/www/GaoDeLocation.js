cordova.define("cordova-plugin-gaodelocation.GaoDeLocation", function(require, exports, module) {


var exec = require('cordova/exec');

module.exports = {
      /**
       * Exits the PhoneGap application with no questions asked.
       */
      getCurrentPosition:function(successCallback, errorCallback) {
          exec(successCallback,errorCallback,'GaoDeLocation','getCurrentPosition',[]);
      }
  }
});
