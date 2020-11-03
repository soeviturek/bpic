define(function (require, exports) {
    var template = require('./personnel-file.html');
    require('../../css/personnel-file.css');
    require('../../css/recheck-interview.css');
    template = doT.template(template);
    exports.back = function () {
        return '';
    };

    exports.show = function (opt) {
        var main = {
            init: function (obj) {
                this.obj = obj;
                this.obj.html(template(''));
                this.post = [];
                this.getDict();
              
                this.result = '';
                // this.personnelId = '1029188258464006144';
                this.personnelId = common.getHash('personnelId')||'';
                this.agentCode = common.getHash("token")|| "";
                this.platform=common.getLocalStorage('platform', true)||'';
                this.infoFlag = true;
                this.userInfo={};
                this.obj.off('tap', '.arrow');
                this.obj.on('tap', '.arrow', {fun: this}, this.tabInfo);
                //点击查看大图
                this.obj.off('tap', '.photo-item img');
                this.obj.on('tap', '.photo-item img', {fun: this}, this.lookBigPic);
                
                //查看pdf
                this.obj.off('tap', '.sign-item');
                this.obj.on('tap', '.sign-item', {fun: this}, this.lookSign);
                //跳转银行卡补录链接
                this.obj.off('tap', '.bank-sign');
                this.obj.on('tap', '.bank-sign', {fun: this}, this.goBankSign);
                
                if(this.agentCode){
                    this.personFileLogin()
                }else{
                    this.getNumber();
                    this.initBascInfo();
                    this.getUserInfo();
                }
            },
            personFileLogin: function () {
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
                        _this.toPersonDoc();
                        
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
            toPersonDoc: function () {
                var personInfor = common.getLocalStorage("currentUser", true);
                var _this = this;
                console.log(personInfor.presentCode);
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
                            _this.personnelId=data.id;
                            _this.getNumber();
                            _this.initBascInfo();
                            _this.getUserInfo();
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
            },
            initBascInfo: function () {
                var _this = this;
                _this.imgEcho("1", ".up-card");
                _this.imgEcho("2", ".back-card");
                _this.imgEcho("4", ".school-card");
                _this.imgEcho("42", ".bank-card");
                _this.imgEcho("43", ".ry-card");
                _this.imgEcho("49", ".user-card");
            },
            getNumber: function () {
                var _this = this;
                var data = {
                    // id: "1029188258464006144"
                    id: _this.personnelId
                };
                var params = {
                    url: '/personnel/customer/app-personnel/getPersonnel',
                    type: 'POST',
                    data: JSON.stringify(data)
                };
                Interface.getAsynData(params, function (response) {
                    if (response.code == 1) {
                        var res = response.data;
                        var curPost;
                        if (res.personnel.confirmPosition) {
                            curPost = res.personnel.confirmPosition;
                            $(".postType").text("核定")
                        } else {
                            curPost = res.personnel.protocolPosition;
                            $(".postType").text("核定")
                        }
                        if (res.personnel.sex == 'F') {
                            $('.portrait').find('i').addClass('icon-female');
                            $('.portrait').find('img').attr('src', '../../images/feman-top.jpg')
                        } else if (res.personnel.sex == 'M') {
                            $('.portrait').find('img').attr('src', '../../images/top-pic.jpg');
                            $('.portrait').find('i').addClass('icon-male')
                        }
                        $('.rank p').eq(0).text(res.personnel.name);
                        //$('.rank p').eq(1).text(res.personnel.cellphone);
                        $('.rank p').eq(2).find('span').text(res.createName);
                        $('.right span').eq(0).text(common.getPostName(_this.post, curPost));
                        $('.we-chat').find('span').text(res.personnel.wechat);
                        $('.phone').find('span').text(res.personnel.cellphone);
                        $('.content-work').attr('data-tempname',res.personnel.name);
                        _this.personnelInitWorkExperience();
                        // if (res.workExperiences) {
                        //     $('.content-work').find('span').text(res.workExperiences);
                        // }
                        //$('.email-message').find('span').text(res.personnel.email);
                        // if(res.personnel.sex == 'M'){
                        //     $('.portrait .iconfont').addClass('icon-male')
                        // }else if(res.personnel.sex == 'F'){
                        //     $('.portrait .iconfont').addClass('icon-female')
                        // }
                    }
                }, function (error) {
                });
            },
            personnelInitWorkExperience: function () {
                var _this = this;
                var param = {
                    url: '/personnel/customer/app-personnel/getInsuranceWorkExperienceByPersonnelId',
                    type: 'POST',
                    contentType: 'application/x-www-form-urlencoded',
                    data: {
                        personnelId: _this.personnelId
                    }
                };

                Interface.getAsynData(param, function (response) {
                    if (response.code == "200") {
                        var data=response.data;
                        if(data){
                            $(".content-work").children('span').html($(".content-work").data("tempname") +'于'+data.startTime.replace('-','年').replace('-','月')+'-'+data.endTime.replace('-','年').replace('-','月')+'于'+data.company+'保险公司就职，职位为'+data.post)
                        }else{
                            $(".content-work").children('span').html('无')
                        }
                       
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
            imgEcho: function (imgType, _class) {
                var _this = this;
                var paramData = {
                    url: '/attachment/file/find',
                    type: 'get',
                    data: {
                        hostId: _this.personnelId,
                        category: imgType
                    }
                };
                Interface.getAsynData(paramData, function (response) {
                    if (response.msg === "无此图片") {

                    } else {
                        if (imgType == '42') {
                            $(_class).find('.fileImg').hide();
                        }
                        $(_class).find('img').show().attr('src', response.data[0]);

                    }

                }, function (error) {

                });
            },
            lookBigPic: function (btn) {
                var _this = btn.data.fun;
                var picSrc = $(this).attr('src');
                if (picSrc) {
                    // common.loading.open();
                    $('.look-big').find('img').attr('src', picSrc);
                    // $('.look-big').find('img')[0].onload = function () {
                    //     common.loading.close();
                    // };
                    // $('.look-big').find('img').error(function () {
                    //     common.loading.close();
                    // });
                    $('.look-big').show();
                }
                $('.look-big').off('tap');
                $('.look-big').on('tap', function () {
                    $(this).find('img').attr('src', '');
                    $(this).hide()
                });


            },
            lookSign: function (btn) {
                var _this = btn.data.fun;
                var pdfName = $(this).attr('pdf-name');
                if (pdfName == '146'&&_this.userInfo.familyCity!='上海市') {
                    common.alertCreate({
                        html: '非上海地区无需签署此合同！',
                        callback: function () {
                        }
                    });
                    return;
                }
                var paramData = {
                    url: '/attachment/file/findAttachment',
                    type: 'get',
                    data: {
                        hostId: _this.personnelId,
                        // hostId:'1029188258464006144',
                        // category: imgType
                        category: pdfName
                    }
                };
                Interface.getAsynData(paramData, function (response) {
                    if (response.msg === "无此图片") {

                    } else {
                        console.log(response);
                        var url = response.data[0].path;
                        common.setSessionStorage('pdfUrl', url, false);
                        return window.location.href = '#page=lookPdf';
                        // $(_class).find('.card-on p,i').hide();
                        // $(_class).find('img').show().attr('src', response.data[0]);
                    }

                }, function (error) {

                });
            },
            goBankSign: function (btn) {
            	 var _this = btn.data.fun;
                return window.location.href = '#page=bankSign&personnelId='+_this.personnelId;
            },
            getUserInfo: function () {
                var _this = this;
                var data = {
                    id: this.personnelId
                };
                var params = {
                    url: '/personnel/customer/app-personnel/exclusions/getPersonnel',
                    type: 'POST',
                    ifOpenLoading: true,
                    data: JSON.stringify(data)
                };
                Interface.getAsynData(params, function (response) {
                    if (response.code == "200") {
                        console.log(response);
                        _this.userInfo = response.data.personnel;
                        console.log(_this.userInfo)

                    }

                }, function (error) {

                });
            },
        };
        main.init(opt.body);
    }
});