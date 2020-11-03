cordova.define("cordova-plugin-pdf.www.PDF",
    function(require, exports, module) {
        var exec = require("cordova/exec");
        module.exports = {
            openPDF: function(successCallback, errorCallback, content){
                exec(
                successCallback,
                errorCallback,
                "PDF",//feature name
                "openPDF",//action
                [content]//要传递的参数，json格式
                );
            }
        }
});