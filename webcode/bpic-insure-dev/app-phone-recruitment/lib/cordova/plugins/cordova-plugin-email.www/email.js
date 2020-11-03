cordova.define("cordova-plugin-email.www.Email",
    function(require, exports, module) {
        var exec = require("cordova/exec");
        module.exports = {
            sendEmail: function(successCallback, errorCallback
            , receiver, emailtitle, emailcontent, emailfile){
                exec(
                successCallback,
                errorCallback,
                "Email",//feature name
                "sendEmail",//action
                [receiver, emailtitle, emailcontent, emailfile]//要传递的参数，json格式
                );
            }
        }
});