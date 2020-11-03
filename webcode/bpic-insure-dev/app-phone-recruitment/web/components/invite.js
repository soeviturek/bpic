define(function (require, exports) {
    require('../../css/invite.css');
    var template = require('./invite.html');
    template = doT.template(template);
    exports.back = function () {
        return '';
    };

    exports.show = function (opt) {
        var recheck = {
            init: function (obj) {
                this.obj = obj;
                
                this.agentCode = common.getHash("token")|| "";
                this.userInfo =  common.getLocalStorage("currentUser") || '';
                this.platform=common.getLocalStorage('platform', true)||'';
                if(this.agentCode){
                    this.inviteLogin()
                }else{
                    this.getDict();
                }
                this.obj.off('tap', '.icon-share');
                this.obj.on('tap', '.icon-share', {fun: this}, this.sendData);
            },

            inviteLogin: function () {
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
                        _this.userInfo =  common.getLocalStorage("currentUser") || '';
           
                        common.setLocalStorage("token", data.token);
                        
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

            sendData: function (btn) {
                var _this = btn.data.fun;
                var _this2 = this;
                var userInfo = _this.userInfo==''?'':JSON.parse(_this.userInfo);
                var data = _this.getVal();
                var _val = $.trim($('[name=job]').val());
                data.id = $('input[name=isShare]').val();
                if(!_val){
                    common.alertCreate({
                        html: '请选择职位'
                    });
                    return false
                }


                var params = {
                    url: '/personnel/customer/app-personnel/addPersonnel',
                    type: 'POST',
                    ifOpenLoading: _this.ifOpenLoading,
                    data: JSON.stringify(data)
                };
                Interface.getAsynData(params, function (response) {
                    if (response.code == "200") {
                        $('input[name=isShare]').val(response.data);
                        var url = $.domainUrl+'/app/web/home/?personnelId='+ response.data.id+'&createUser='+response.data.createUser +'#page=inviteShare&channel=003';
                 
                        var title = '来自'+userInfo.accountName+'的招募邀请';

                        var params1 = {
                            url: '/personnel/customer/app-personnel/addShare',
                            type: 'POST',
                            contentType: 'application/json',
                            data: JSON.stringify({shareUrl:url})
                        };
                        Interface.getAsynData(params1,function (resp) {
                            if(response.code == "200"){
                                url = resp.data.shareUrl;
                                if(_this.platform=='MER'){
//                                  window.postMessage(JSON.stringify({
//                                      action:'openShare',
//                                      url:url,
//                                      title:title,
//                                      text:'复深蓝期待您的加入',
//                                      img: $.domainUrl+'/images/share-img.jpg'
//                                  }));
                                    
                                    // var autoMessage = {"name":"三三"};
                                    // window.parent.postMessage(autoMessage, '*');
                                    
                                    console.log(window.shareJob, '点击了分享')
                                    window.parent.shareJob(url) 
									console.log("调用分享url:"+url)
									// console.log("调用方法:parent.vm.shareJob(url) ;")
									// parent.vm.shareJob(url);
                                }else{
                                    ebtJs.share(url,title,"复深蓝期待您的加入", $.domainUrl+"/images/share-img.jpg");
                                }
                            }
                        });

                        // EbtJsBridge.share(url,title,"复深蓝保险经纪期待您的加入","https://erecruit-uat.juntianbroker.com/images/share-img.jpg");
                        /*ebtJs.share(url,title,"复深蓝保险经纪期待您的加入", $.domainUrl+"/images/share-img.jpg");*/
                       /*  common.alertCreate({
                     		   html: '分享成功',
                     		    callback: function () {
                 		    	 return window.location.href = '#page=main';
                 		       }
                 	     })*/
                    }

                }, function (error) {

                });
            },

            getDict: function () {
                var _this = this;
                var dictParam = {
                    url: '/web/system/dict/data/type/findByCodes',
                    type: 'POST',
                    contentType: 'application/x-www-form-urlencoded',
                    ifOpenLoading: true,
                    data: {
                        codes: 'rank'
                    }
                };

                Interface.getAsynData(dictParam, function (response) {
                    if (response.code == "200") {
                        _this.obj.html(template(response.data['rank']));
                        // 水星针对安卓单独做的样式
                        var isAndroidApp =  getPlatform()=='android'?true:false;
                        if(isAndroidApp){
                            $('.invite .ipt-select').css('margin-top','-15px')
                        }
                    }
                });
            },

            getVal: function () {
                var obj = {};
                obj.name = $('.name input').val();
                obj.sex = $('.sex input:checked').val();
                obj.familyCity = $('.city input').val();
                obj.cellphone  = $('.phone input').val();
                obj.email = $('.email input').val();
                obj.wechat = $('.wechat input').val();
                //obj.contactQQ = $('.qq input').val();
                //obj.company = $('.company input').val();
                //obj.post = $('.job input').val();
                obj.workIntroduction  = $('.job textarea').val();
                obj.protocolPosition = $('select[name=protocolposition]').val();
                return obj;
            }
        };

        recheck.init(opt.body);
    }
});