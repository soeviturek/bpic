cordova.define("cordova-plugin-signature.www.Signature",
    function(require, exports, module) {
        var exec = require("cordova/exec");
        module.exports = {
            getSignature: function(successCallback, errorCallback){
                exec(
                successCallback,
                errorCallback,
                "Signature",//feature name
                "getSignature",//action
                []//要传递的参数，json格式
                );
            }
        }
});