cordova.define("cordova-plugin-encrypt.MD5Tool", function(require, exports, module) {
var exec = require('cordova/exec');
var catfaceFunc = function(){};

// arg1：成功回调
// arg2：失败回调
// arg3：将要调用类配置的标识
// arg4：调用的原生代码中的方法名
// arg5：参数，json格式
catfaceFunc.prototype.str2MD5=function(str, success, error) {
    exec(success, error, "MD5Tool", "str2MD5", [str]);
};

catfaceFunc.prototype.md52Str=function(str, success, error) {
    exec(success, error, "MD5Tool", "md52Str", [str]);
};
var catfaceFunc = new catfaceFunc();
module.exports = catfaceFunc;
});