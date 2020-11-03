define(function (require, exports) {
    var template = require('./recheck.html');
    template = doT.template(template);
    exports.back = function () {
        return '';
    };

    exports.show = function (opt) {
        var recheck = {
            init: function (obj) {
                this.obj = obj;
                this.infoFlag = true;
                this.agentCode = common.getHash("token")|| "";
                this.platform=common.getLocalStorage('platform', true)||'';
                this.personnelId = common.getHash('personnelId') || '';
                this.type = Number(common.getHash('type')) || 0;
                this.post = [];

                // 通过
                this.obj.off('tap', '.recheck-view .bottom button:first-child');
                this.obj.on('tap', '.recheck-view .bottom button:first-child', { fun: this}, this.subInterResult);

                // 不通过
                this.obj.off('tap', '.recheck-view .bottom button:last-child');
                this.obj.on('tap', '.recheck-view .bottom button:last-child', { fun: this}, this.subInterResult);

                this.obj.off('tap', '.arrow');
                this.obj.on('tap', '.arrow', {fun: this}, this.tabInfo);
                if(this.agentCode){
                    this.recheckLogin()
                }else{
                    this.initPersonInfo();
                    this.getDict();
                }

                var _this = this;
                var timer = setInterval(function () {
                    var hash = common.getHash('page');
                    if (hash !== 'recheck') {
                        clearInterval(timer);
                        return;
                    }
                    if ($('.system-loading').length <= 0) {
                        common.loading.open();
                    }
                    if (_this.dict && _this.post.length) {
                        var renderData = {
                            dict: _this.dict,
                            post: _this.post
                        }
                        var temp = '{{~it.post:value:index}}\
                            <option value="{{=value.code}}" {{?value.code==it.dict}}selected{{?}}>{{=value.value}}</option>\
                        {{~}}';
                        temp = doT.template(temp);
                        $('select[name=confirmPosition]').append(temp(renderData));
                        var name = _this.findName(renderData.post);
                        $('select[name=confirmPosition]').prev('input').val(name);
                        $('.recheck-post span:nth-child(2)').text(name);
                        common.loading.close();
                        clearInterval(timer);
                    }
                }, 20);
            },
            recheckLogin: function () {
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
                }
                common.setLocalStorage("token", agentCode);
                var login = {
                    params: agentCode,
                    platform:_this.platform,
                };
                var paramData = {
                    ifOpenLoading: true,
                    url: '/security/JTERLogin',
                    type: 'POST',
                    contentType: 'application/x-www-form-urlencoded',
                    data: login
                };
                Interface.getAsynData(paramData, function (response) {      
                    if (response.code == "200") {
                        var data = JSON.parse(response.data); 
                        common.setLocalStorage("currentUser", JSON.stringify(data.account));
                      if(!data.isOpen){
                        if(_this.platform=='MER'){
                            window.postMessage(JSON.stringify({
                                action:'close'
                            }));
                        }else{
                        ebtJs.closeWebview('123'); 
                        }
                        }
                        common.setLocalStorage("token", data.token);
                        _this.initPersonInfo();
                        _this.getDict();
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
            recheckActionResult: function () {
                var _this = this;
                var param = {
                    url: '/personnel/customer/app-personnel/getInterviewActionResult',
                    type: 'POST',
                    data: JSON.stringify({
                        personnelId: _this.personnelId,
                        flowItemId:'3'
                    })
                };

                Interface.getAsynData(param, function (response) {
                    if (response.code == "200") {
                        response.data=''+response.data;
                     //  -1:没有当前流程，0：未进行，1：通过 2：不通过
                     switch(response.data){
                         case '-1':
                         $('.bottom').html('没有当前流程')
                         break;
                         case '0':
                         break;
                         case '1':
                         $('.bottom').html('通过')
                         break;
                         case '2':
                         $('.bottom').html('不通过')
                         break;
                         default:
                         $('.bottom').html('数据错误')
                         break;
                     }
                    }
                }, function (error) {

                });
            },
            tabInfo: function (btn) {
                var _this = btn.data.fun;
                if (_this.infoFlag) {
                    $(this).css('transform', 'rotate(0deg)');

                    $('.content-details').hide();
                    $('.content-work').hide();
                    _this.infoFlag = false
                } else {
                    $(this).css('transform', 'rotate(180deg)');

                    $('.content-details').show();
                    $('.content-work').show();
                    _this.infoFlag = true;
                }
            },

            initPersonInfo: function () {
                var _this = this;
                var param = {
                    url: '/personnel/customer/app-personnel/getPersonnelInfoForViewer',
                    type: 'POST',
                    contentType: 'application/x-www-form-urlencoded',
                    data: {
                        personnelId: _this.personnelId
                    }
                };

                Interface.getAsynData(param, function (response) {
                    if (response.code == "200") {
                        var curPost;
                        var res = response.data;
                    	if(res.confirmPosition){
                        	curPost = res.confirmPosition;
                        }else{
                            curPost = res.protocolPosition;
                        }
                        _this.dict = curPost;
                        _this.obj.html(template(response.data));
                        if(_this.agentCode){
                            _this.recheckActionResult();
                        }
                        if(res.isClose){
                            $('.bottom').hide();
                        }
                        if(res.confirmPosition){
                            $(".postType").text("核定");
                            $(".postType").parent('.right').find('.iconfont').addClass('icon-character-success')
                        }else{
                            $(".postType").text("拟定");
                            $(".postType").parent('.right').find('.iconfont').addClass('icon-renyuanzhunbei')
                        }
                    }
                }, function (error) {

                });
            },
            subInterResult: function (btn) {
                var _this = btn.data.fun;
                var data = {};
                if (_this.type === 1) { 
                    if ($(this).data('result') === 'Y') {
                        data = {
                            currentStatus: "4",
                            personnelId: _this.personnelId,
                            result: "1",
                        };
                    } else {
                        data = {
                            currentStatus: "4",
                            result: "2",
                            confirmPosition: $('select[name=confirmPosition]').val()
                        };
                    }
                } else {
                    if ($(this).data('result') === 'Y') {
                        data = {
                            comment: $('#comment').val(),
                            currentStatus: "3",
                            personnelId: _this.personnelId,
                            result: "1",
                            confirmPosition: $('select[name=confirmPosition]').val()
                        };
                    } else {
                        if(!$.trim($('#comment').val())){
                            common.alertCreate({
                                html: '评语不能为空！'
                            })
                            return false;
                        }
                        data = {
                            comment: $('#comment').val(),
                            currentStatus: "3",
                            personnelId: _this.personnelId,
                            result: "2",
                            confirmPosition: $('select[name=confirmPosition]').val()
                        };
                    }
                }

                var param = {
                    url: '/personnel/customer/app-personnel/savePersonnelInfo',
                    type: 'POST',
                    ifOpenLoading: true,
                    data: JSON.stringify(data)
                };

                Interface.getAsynData(param, function (response) {
                    if (response.code == "200") {
                        return window.location.href = '#page=main';
                    }
                }, function (error) {
                    common.alertCreate({
                        html: error.msg
                    });
                    return false;
                });
            },
            getDict: function () {
                var _this = this;
                var dictParam = {
                    url: '/web/system/dict/data/type/findByCodes',
                    type: 'POST',
                    contentType: 'application/x-www-form-urlencoded',
                    data: {
                        codes: 'rank'
                    }
                };

                Interface.getAsynData(dictParam, function (response) {
                    if (response.code == "200") {
                        _this.post = response.data['rank'];
                    }
                });
            },

            findName: function (list) {
                var length = list.length;
                for (var i = 0; i < length; i++) {
                    if (list[i].code === this.dict) {
                        return list[i].value;
                    }
                }
            }
        };
        recheck.init(opt.body);
    }

});