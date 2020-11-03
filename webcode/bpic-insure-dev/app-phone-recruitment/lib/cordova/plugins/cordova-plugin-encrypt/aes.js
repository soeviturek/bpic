cordova.define("cordova-plugin-encrypt.AesCrypto", function(require, exports, module) {
var exec = require('cordova/exec');
var catfaceFunc = function(){};

// arg1：成功回调
// arg2：失败回调
// arg3：将要调用类配置的标识
// arg4：调用的原生代码中的方法名
// arg5：参数，json格式
catfaceFunc.prototype.str2Aes=function(str, success, error) {
    exec(success, error, "AesCrypto", "str2Aes", [str]);
};

catfaceFunc.prototype.aes2Str=function(str, success, error) {
    exec(success, error, "AesCrypto", "aes2Str", [str]);
};
var catfaceFunc = new catfaceFunc();
module.exports = catfaceFunc;
});