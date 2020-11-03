cordova.define("cordova-plugin-baiduocr.www.FULAN_OCR",
    function(require, exports, module) {
        var exec = require("cordova/exec");
        module.exports = {
            getIdInfo: function(successCallback, errorCallback, card_type){
                exec(
                successCallback,
                errorCallback,
                "FULAN_OCR",//feature name
                "getIdInfo",//action
                [card_type]//要传递的参数，json格式
                );
            }
        }
});