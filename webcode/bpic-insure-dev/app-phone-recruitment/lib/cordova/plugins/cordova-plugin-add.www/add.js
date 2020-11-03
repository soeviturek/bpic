cordova.define("cordova-plugin-add.www.Add",
    function(require, exports, module) {
        var exec = require("cordova/exec");
        module.exports = {
            chooseAddress: function(successCallback, errorCallback){
                exec(
                successCallback,
                errorCallback,
                "Add",//feature name
                "chooseAddress",//action
                []//要传递的参数，json格式
                );
            }
        }
});