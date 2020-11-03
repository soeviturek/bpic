cordova.define("cordova-plugin-pen.www.Pen",
    function(require, exports, module) {
        var exec = require("cordova/exec");
        module.exports = {
            doSign: function(successCallback, errorCallback
             , customerId, plolicyId, seqNum, type, customerType, url){
                exec(
                successCallback,
                errorCallback,
                "Pen",//feature name
                "doSign",//action
                [customerId, plolicyId, seqNum, type, customerType, url]//要传递的参数，json格式
                );
            }
        }
});