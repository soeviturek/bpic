define(function (require, exports) {
    var template = require('./main.html');
    template = doT.template(template);
    exports.back = function () {
        return '';
    };

    exports.show = function (opt) {
        var main = {
            init: function (obj) {
                this.obj = obj;
                this.obj.html(template(''));
                this.agentCode = common.getSearch("token") != null ? common.getSearch("token").split("?")[0] : "";
                this.platform=common.getLocalStorage('platform', true)||'';
                this.automaticLogin();
                this.obj.off('tap', '.person-doc');
                this.obj.on('tap', '.person-doc', {fun: this}, this.toPersonDoc);
            },
            automaticLogin: function () {
                var _this = this;
                var agentCode = _this.agentCode;
     
                if (!agentCode) {
                    agentCode = common.getLocalStorage("token");
                    if(!agentCode){
                    	 common.alertCreate({
                                html: '认证超时',
                                callback: function(){
                                    if(_this.platform=='MER'){
                                        window.postMessage(JSON.stringify({
                                            action:'close'
                                        }));
                                    }else{
                                       ebtJs.closeWebview('123'); 
                                    }
                                }
                         })
                    	 return false;
                    }
                    _this.getNumber();
                }
                      common.setLocalStorage("token", agentCode);
               
                var login = {
                    platform:_this.platform
                };
                var paramData = {
                    ifOpenLoading: true,
                    url: '/personnel/customer/app-personnel/getUserInfo',
                    type: 'POST',
                    contentType: 'application/x-www-form-urlencoded',
                    data: login
                };
                Interface.getAsynData(paramData, function (response) {      
                    if (response.code == "200") {
                    	var userInfo = {
                    		presentCode:response.data.c_emp_cde,
                    		accountName:response.data.c_emp_cnm
                    	}
                        
                        common.setLocalStorage("currentUser", JSON.stringify(userInfo));
                     
                        _this.getNumber();
                    }else if (response.code === "0"){
                        if(_this.platform=='MER'){
                            window.postMessage(JSON.stringify({
                                action:'close'
                            }));
                        }else{
                           ebtJs.closeWebview('123'); 
                        }
                    }
                }, function (error) {
                    common.alertCreate({
                        html: error.msg
                    });
                    return false;
                });
            },
            getNumber: function () {
                var data = {
                    url: '/personnel/customer/app-personnel/getAgentPersonnelCount',
                    contentType: '',
                    type: 'POST',
                    ifOpenLoading: true,
                    data: {}
                };
                Interface.getAsynData(data, function (response) {
                    if (response.code == "200") {
                        var data = response.data;
                        //$('.enterCount').text(data.enterCount || '0');
                        //$('.vieweeCount').text(data.vieweeCount || '0');
                        if (parseInt(data.enterCount) == 0) {
                            $('.enterCount').hide()
                        } else if (parseInt(data.enterCount) < 99) {
                            $('.enterCount').text(data.enterCount);
                        } else {
                            $('.enterCount').text('99+');
                        }

                        if (parseInt(data.vieweeCount) == 0) {
                            $('.vieweeCount').hide()
                        } else if (parseInt(data.vieweeCount) < 99) {
                            $('.vieweeCount').text(data.vieweeCount);
                        } else {
                            $('.vieweeCount').text('99+');
                        }
                    }
                }, function (error) {

                });
            },
            toPersonDoc: function () {
                var personInfor = common.getLocalStorage("currentUser", true);
                // console.log(personInfor);
                // presentCode
                var data =
                    {
                        "token": personInfor.presentCode
                    };

                var data = {
                    url: '/personnel/customer/app-personnel/getPersonnelByPresonnel',
                    // contentType: '',
                    type: 'POST',
                    ifOpenLoading: false,
                    data: JSON.stringify(data)
                };
                Interface.getAsynData(data, function (response) {
                    if (response.code == "200") {
                        var data = response.data;
                        if (data && data.id) {
                            return window.location.href = '#page=personnelFile&personnelId='+data.id;
                        } else {
                            common.alertCreate({
                                html: '暂无个人档案!',
                                callback: function () {

                                }
                            })
                        }

                    }
                }, function (error) {

                });
            }

        };

        main.init(opt.body);
    }
});