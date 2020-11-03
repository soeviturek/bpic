cordova.define("cordova-plugin-ppt.www.PPT",
    function(require, exports, module) {
        var exec = require("cordova/exec");
        module.exports = {
            openPPT: function(successCallback, errorCallback, content){
                exec(
                successCallback,
                errorCallback,
                "PPT",//feature name
                "openPPT",//action
                [content]//要传递的参数，json格式
                );
            }
        }
});