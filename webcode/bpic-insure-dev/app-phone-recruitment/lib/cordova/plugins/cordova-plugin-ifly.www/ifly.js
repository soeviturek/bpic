cordova.define("cordova-plugin-ifly.www.FULAN_IFLY",
    function(require, exports, module) {
        var exec = require("cordova/exec");
        module.exports = {
            getIfly: function(successCallback, errorCallback){
                exec(
                successCallback,
                errorCallback,
                "FULAN_IFLY",//feature name
                "getIfly",//action
                []//要传递的参数，json格式
                );
            }
        }
});