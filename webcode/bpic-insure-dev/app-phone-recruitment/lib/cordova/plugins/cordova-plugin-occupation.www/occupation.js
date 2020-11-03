cordova.define("cordova-plugin-occupation.www.Occupation",
    function(require, exports, module) {
        var exec = require("cordova/exec");
        module.exports = {
            getStage: function(successCallback, errorCallback
             ,url){
                exec(
                successCallback,
                errorCallback,
                "Occupation",//feature name
                "getStage",//action
                [url]//要传递的参数，json格式
                );
            }
        }
});