cordova.define("cordova-plugin-alipay-v2.alipay", function(require, exports, module) {
var exec = require('cordova/exec');

exports.payment = function(payInfo, success, error) {
    exec(success, error, "alipay", "payment", [payInfo]);
};

});
