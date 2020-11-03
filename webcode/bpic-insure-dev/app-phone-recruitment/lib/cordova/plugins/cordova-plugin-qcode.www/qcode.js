cordova.define("cordova-plugin-qcode.www.QCode",
    function(require, exports, module) {
        var exec = require("cordova/exec");
        module.exports = {
            saveCode: function(successCallback, errorCallback, content){
                exec(
                successCallback,
                errorCallback,
                "QCode",//feature name
                "saveCode",//action
                [content]//要传递的参数，json格式
                );
            }
        }
});