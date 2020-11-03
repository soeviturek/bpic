define(function (require, exports) {
    var template = require('./transmit-sign.html');
    require('../../css/recruit-details.css');
    exports.back = function () {
        return '';
    };

    exports.show = function (opt) {
        var recheck = {
            init: function (obj) {
                this.obj = obj;
                this.obj.html(template);
                this.tabFlag = true;
                this.infoFlag = true;
                this.personnelId = common.getQuery('personnelId');
                this.obj.off('tap', '.tab-slide');
                this.obj.on('tap', '.tab-slide', {fun: this}, this.tabDetail);
                this.getInterview();
                this.getUserInfo();
                this.obj.off('tap', '.arrow');
                this.obj.on('tap', '.arrow', {fun: this}, this.tabInfo);

            },
            getInterview: function () {
                var data = {
                    personnelId: this.personnelId||'1015133468574613504'
                };
                var params = {
                    url: '/personnel/customer/app-personnel/getAllInterviewAction',
                    type: 'POST',
                    ifOpenLoading: true,
                    data: JSON.stringify(data)

                };
                Interface.getAsynData(params, function (response) {
                    if (response.code == "200") {
                        console.log(response);
                        var res = response.data;
                        res.forEach(function (item, index) {
                            $('.details li').eq(index).find('.right p').eq(0).find('span').eq(2).addClass('pass').text('通过');
                            $('.details li').eq(index).find('.right p').eq(0).find('span').eq(1).text(item.processingName);
                            $('.details li').eq(index).find('.right p').eq(1).find('span').eq(0).text('222');
                            $('.details li').eq(index).find('.right p').eq(1).find('span').eq(0).text(item.processingDesc);

                            $('.details li').eq(index).find('.time em').text(item.createTime?item.createTime.substr(5):'');

                        });
                        if(res[res.length-1].startTime&&!res[res.length-1].processingStatus){
                            $('.details li').eq(res.length-1).addClass('current-node').find('.right p').eq(0).find('span').eq(2).text('进行中');
                        }else if(es[res.length-1].startTime && res[res.length-1].processingStatus=='2'){
                            $('.details li').eq(res.length-1).addClass('current-node').find('.right p').eq(0).find('span').eq(2).text('未通过');
                        }

                    }

                }, function (error) {

                });
            },
            tabDetail: function (btn) {
                var _this = btn.data.fun;
                if (_this.tabFlag) {
                    $(this).parent().addClass('over-flow');
                    _this.tabFlag = false
                } else {
                    $(this).parent().removeClass('over-flow');
                    _this.tabFlag = true;

                }
                $(this).css('transform', 'rotate(180)')

            },
            getUserInfo:function () {
                var data = {
                    id: 1
                };
                var params = {
                    url: '/personnel/customer/app-personnel/getPersonnel',
                    type: 'POST',
                    ifOpenLoading: true,
                    data: JSON.stringify(data)

                };
                Interface.getAsynData(params, function (response) {
                    if (response.code == "200") {
                        console.log(response);
                        var res=response.data;
                        $('.rank p').eq(0).text(res.personnel.name);
                        //$('.rank p').eq(1).text(res.personnel.cellphone);
                        $('.rank p').eq(2).find('span').text(res.createName);
                        $('.right span').eq(0).text(res.personnel.protocolPosition);
                        $('.we-chat').find('span').text(res.personnel.wechat);
                        $('.phone').find('span').text(res.personnel.cellphone);
                        $('.content-work').find('span').text(res.workExperiences[0].workIntroduction);
                        //$('.email-message').find('span').text(res.personnel.email);
                    }

                }, function (error) {

                });
            },
            tabInfo:function (btn) {
                var _this=btn.data.fun;
                if(_this.infoFlag){
                    $('.content-details').hide();
                    $('.content-work').hide();
                    _this.infoFlag=false
                    $(this).css('transform','rotate(0deg)')
                }else {
                    $('.content-details').show();
                    $('.content-work').show();
                    _this.infoFlag=true;
                    $(this).css('transform','rotate(180deg)')
                }
            }
        };

        recheck.init(opt.body);
    }
});