cordova.define("cordova-plugin-share.SocialShare", function(require, exports, module) {
var exec = require('cordova/exec');
var catfaceFunc = function(){};

  catfaceFunc.prototype.socialShare=function(success, error) {
    exec(success, error, 'SocialShare', 'socialShare', []);
  };

  catfaceFunc.prototype.sinaLogin=function(success, error) {
      exec(success, error, 'SocialShare', 'sinaLogin', []);
    };

  catfaceFunc.prototype.qqLogin=function(success, error) {
      exec(success, error, 'SocialShare', 'qqLogin', []);
    };

  catfaceFunc.prototype.weixinLogin=function(success, error) {
      exec(success, error, 'SocialShare', 'weixinLogin', []);
    };

 catfaceFunc.prototype.weixinShare=function(success, error,options) {
      exec(success, error, 'SocialShare', 'weixinShare',[options]);
    };

 catfaceFunc.prototype.weixinFriend=function(success, error,options) {
      exec(success, error, 'SocialShare', 'weixinFriend',[options]);
    };

var catfaceFunc = new catfaceFunc();
module.exports = catfaceFunc;
});