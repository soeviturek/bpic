define(function (require, exports) {
    require('../../css/sign-datum.css');
    var template = ''; 
    exports.back = function () {
        return '';
    };

    exports.show = function (opt) {
        var recheck = {
            init: function (obj) {
                this.obj = obj;
                this.userInfo = {};
                this.personnelId = common.getHash('personnelId') || '';
                this.shareInfoId = common.getHash('shareInfoId');
                this.orgSubCodes = '';
                this.isCity=0;
                if (this.personnelId) {
                    this.getStatu();
                } else {
                    this.obj.html(template({}));
                }

                this.obj.off('tap', '.sign-item');
                this.obj.on('tap', '.sign-item', {fun: this}, this.goSign);

                // 上一步
                this.obj.off('tap', '.prev-page');
                this.obj.on('tap', '.prev-page', {fun: this}, this.bascInfoGoPrev);

                //提交
                this.obj.off('tap', '.sign-submit');
                this.obj.on('tap', '.sign-submit', {fun: this}, this.signSubmit);
            },

            initBascInfo: function () {
                var _this = this;
                $(".icon-back").css("display", "block");
                $('.sign-item').each(function (index, elem) {
                    var _category = $(elem).data('type');
                    // console.log(_category)
                    var paramData = {
                        url: '/attachment/file/find',
                        type: 'get',
                        data: {
                            hostId: _this.personnelId,
                            category: "1"+_category
                        }
                    };
                    Interface.getAsynData(paramData, function (response) {
                        if (response.msg === "无此图片") {
                            $(elem).removeClass('signed');
                        } else {
                            $(elem).addClass('signed');
                        }

                    }, function (error) {

                    });
                })
            },
            goSign: function (btn) {
                var _this = btn.data.fun;
                var $this = $(this);
                var city = _this.userInfo.familyCity;
                var companyId = _this.userInfo.companyId;
                if ($this.attr('data-type') == 46 &&  !_this.isCity) {
                    common.alertCreate({
                        html: '非上海地区无需签署此合同！',
                        callback: function () {
                        }
                    });
                    return;
                }
                var type = $this.data('type');
                return window.location.href = '?type=' + type + '#page=signPage&personnelId=' + _this.personnelId + '&shareInfoId='+_this.shareInfoId;
            },
            bascInfoGoPrev: function (btn) {
                var _this = btn.data.fun;
                return window.location.href = '#page=cardInfo&personnelId=' + _this.personnelId + '&shareInfoId='+_this.shareInfoId;
            }
            ,
            signSubmit: function (btn) {
                var _this = btn.data.fun;
				var companyId = _this.userInfo.companyId;
                if ($('.signed').size() != 4 && _this.isCity) {
                    common.alertCreate({
                        html: '请全部阅读并签字！'
                    });
                    return false;
                } else {
                    var flag = false;
                    $('.sign-item').each(function (index, item) {
                        if (!$(item).hasClass('signed') && $(item).attr('data-type') != '46') {
                            flag = true;
                        }
                    });

                    if (flag) {
                        common.alertCreate({
                            html: '请全部阅读并签字！'
                        });
                        return false;
                    }

                }

                var data1 = {
                    personnelId: _this.personnelId,
                    id: _this.shareInfoId
                };
                var params1 = {
                    url: '/personnel/customer/app-personnel/exclusions/checkUrl',
                    type: 'POST',
                    ifOpenLoading: true,
                    data: JSON.stringify(data1)
                }

                var data = {
                    checkResult: "0",
                    id: _this.personnelId,
                    personnelStatus: 7
                };
                var parma = {
                    url: '/personnel/manage/personnel/exclusions/checkDataResult',
                    ifOpenLoading: true,
                    type: 'POST',
                    data: JSON.stringify(data)
                };

                var personnelData = {
                    id: _this.personnelId
                };
                var personnelParam = {
                    url: '/personnel/customer/app-personnel/exclusions/getPersonnel',
                    type: 'POST',
                    ifOpenLoading: true,
                    data: JSON.stringify(personnelData)
                };

                Interface.getAsynData(params1,function (respData) {
                    /**
                     *  data
                     *  1：被删除
                     *  2：超过六十
                     * */
                    if(respData.code == "200" ){
                        Interface.getAsynData(personnelParam, function (response) {
                            if (response.code == "200") {
                                var personnel = response.data.personnel;
                                if (personnel && (personnel.personnelStatus == "6" || personnel.personnelStatus == "7") && (personnel.checkResult == null || personnel.checkResult == "2")) {
                                    Interface.getAsynData(parma, function (response) {
                                        if (response.code == "200") {
                                            common.alertCreate({
                                                html: "信息提交完成"
                                            });
                                        } else {
                                            common.alertCreate({
                                                html: response.msg
                                            });
                                        }
                                    });
                                } else {
                                    common.alertCreate({
                                        html: "签约信息请勿重复提交"
                                    });
                                }
                            }
                        });
                    }
                }, function (error) {
                    common.alertCreate({
                        html: '' + error.msg,
                        callback: function () {
                        }
                    });
                });




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
                        if(_this.userInfo.isClose){
                            $('.click-button').hide()
                        }
                        _this.getOrganizationChart(response.data.personnel.companyId)
                    }

                }, function (error) {

                });
            },
            getOrgSubCodes: function(){
            	 	var _this = this;
            	 	var  data={
                           code:"861310000000"
                        }
                    var paramData = {
                        url: '/system/organization/code/exclusions/findByCode',
                        type: 'POST',
                        contentType:"application/x-www-form-urlencoded",
                        data: data
                    };
                    Interface.getAsynData(paramData, function (response) {
                       if(response.data){
                       	console.log(response.data.subCode)
                               _this.orgSubCodes = response.data.subCode;
                       }
                    }, function (error) {

                    });
            },
            getOrganizationChart: function(companyId){
                var _this = this;
                var  data={
                      code:companyId
                   }
               var paramData = {
                   url: '/system/organizationChart/getOrganizationChart',
                   type: 'POST',
                   contentType:"application/x-www-form-urlencoded",
                   data: data
               };
               Interface.getAsynData(paramData, function (response) {
                  if(response.data){
                      console.log(response.data)
                      var city=response.data.provinceName||response.data.cityName||'';
                      if(city.indexOf('上海')!='-1'){
                        _this.isCity=1;
                    }
                  }
               }, function (error) {

               });
       },
            getStatu:function () {
                var _this = this;
                var data = {
                    personnelId: _this.personnelId,
                    id: _this.shareInfoId
                };
                var params = {
                    url: '/personnel/customer/app-personnel/exclusions/checkUrl',
                    type: 'POST',
                    ifOpenLoading: true,
                    data: JSON.stringify(data)
                }
                Interface.getAsynData(params,function (respData) {
                    /**
                     *  data
                     *  1：被删除
                     *  2：超过六十
                     * */
                    if(respData.code == "200" ){
                        template = require('./sign-datum.html');
  					    template = doT.template(template);
  					    _this.obj.html(template(''));
                        _this.initBascInfo();
                        _this.getUserInfo()
                        _this.getOrgSubCodes();
                    }
                }, function (error) {
                    common.alertCreate({
                        html: '' + error.msg,
                        callback: function () {
                           template = require('./invalid-page.html');
  					       template = doT.template(template);
  					       _this.obj.html(template(''));
                        }
                    });
                });
            }
        };

        recheck.init(opt.body);
    }


});