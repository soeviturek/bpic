define(function (require, exports) {
    require('../../css/recruit-details.css');
    var template = require('./recrite-details.html');
    template = doT.template(template);
    exports.back = function () {
        return '';
    };

    exports.show = function (opt) {
        var recheck = {
            init: function (obj) {
                this.obj = obj;
                this.obj.html(template(''));
                this.tabFlag = true;
                this.infoFlag = true;
                this.post = [];
                this.isClose=null;
                this.personnelId = common.getHash('personnelId') || '';
                this.agentCode = common.getHash("token") ||"";
                this.platform=common.getLocalStorage('platform', true)||'';
                this.obj.off('tap', '.tab-slide');
                this.obj.on('tap', '.tab-slide', {fun: this}, this.tabDetail);
                this.obj.off('tap', '.arrow');
                this.obj.on('tap', '.arrow', {fun: this}, this.tabInfo);
                /*  this.getFlowNode();*/
                if(this.agentCode){
                    this.rescriteLogin();
                }else{
                    this.initPersonInfo();
                   this.getInterview();
                    this.getDict(); 
                }
                

            },
            rescriteLogin: function () {
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
                        _this.getInterview();
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
            getInterview: function () {
                var _this=this;
                var data = {
                    personnelId: this.personnelId
                };
                var params = {
                    url: '/personnel/customer/app-personnel/getAllInterviewAction',
                    type: 'POST',
                    ifOpenLoading: true,
                    data: JSON.stringify(data)

                };
                Interface.getAsynData(params, function (response) {
                    if (response.code == "200") {
                        var res = response.data;
                        res.forEach(function (item, index) {
                            /* $('.details li').eq(index).addClass('on');
                             $('.details li').eq(index).find('.right p').eq(0).find('span').eq(2).addClass('pass').text('通过');
                             $('.details li').eq(index).find('.right p').eq(0).find('span').eq(1).text(item.processingName);
                             $('.details li').eq(index).find('.right p').eq(1).find('span').eq(0).text(item.processingDesc);
                             $('.details li').eq(index).find('.right p').eq(1).find('span').eq(0).text(item.processingDesc);
                             $('.details li').eq(index).find('.time em').text(item.createTime ? item.createTime.substr(5) : '');*/
                            var className = ".step" + item.flowItemId;
                            $(className).find(".right p span").eq(1).text(item.processingName);
                            $(className).find(".over-flow span").text(item.processingDesc);
                            $(className).find(".time span").text(common.getCurTime(item.startTime));
                            $(className).find(".time em").text(common.getCurDate(item.startTime));
                            /*   $(className).find(".time span").text(item.startTime)
                                 $(className).find(".time em").text(item.endTime)*/

                                 $(className).removeClass('no-pass')
							
                                $(className).removeClass('no-pass');
                                if (res.length == index + 1) {
                                    $(className).prevAll().addClass('pass').find("span.fr").text('通过');
                                    $(className).prevAll().find(".left-fathter").addClass("current-node");
                                    // $(className).find(".left img").attr('src',imgsrc);
                                    if (item.processingStatus == "2") {
                                        $(className).addClass('no-pass').find("span.fr").text("不通过");
                                        var imgsrc = $(className).find('.left').attr('img-fail');
                                        $(className).find(".left img").attr('src', imgsrc);
                                    } else {
                                        var imgsrc = $(className).find('.left').attr('img-pending');
                                        $(className).find(".left img").attr('src', imgsrc);
                                        $(className).addClass('under-way').find("span.fr").text("进行中");
                                        $(className).find(".over-flow i").hide();
                                    }
                                    $(className).nextAll().find(".over-flow i").hide();
                                    if(_this.isClose){
                                        $(className).addClass('no-pass').find("span.fr").text("已关闭");
                                    }
                                }
                            
                            
                            if (res.length > 1) {
                                $(className).prevAll().each(function (index, item) {
                                    var imgsrc = $(item).find('.left').attr('img-pass');
                                    $(item).find('.left img').attr('src', imgsrc);
                                    $(item).addClass('pass').find("span.fr").text('通过');
                                });
                            }

                        });

                        /*if (res[res.length - 1].startTime && !res[res.length - 1].processingStatus) {
                            $('.details li').eq(res.length - 1).addClass('current-node').find('.right p').eq(0).find('span').eq(2).text('进行中');
                        } else if (res[res.length - 1].startTime && res[res.length - 1].processingStatus == '2') {
                            $('.details li').eq(res.length - 1).addClass('current-node').find('.right p').eq(0).find('span').eq(2).text('未通过');
                        }*/

                    }

                }, function (error) {

                });
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
                      var temp='<ul>\
                    <li class="clearfix">\
                        <strong class="time">学历信息</strong>\
                        <img class="right_icon" src="../../images/right_icon3.png" alt="">\
                        <div class="work_msg">\
                            <p><span>最高学历</span><span>{{=it.educationalList.length?it.educationalList[0].education:""}}</span></p>\
                        </div>\
                    </li>\
                    <li class="clearfix">\
                        <strong class="time">工作经历</strong>\
                        <img class="right_icon" src="../../images/right_icon2.png" alt="">\
                        <div class="work_msg">\
                            <p><span>工作总年限</span><span>{{=it.totalWorkTime||"0"}}年</span></p>\
                            <p><span>是否有保险公司工作经验</span><span>{{?it.insuranceWorkExperience.length>0&&it.insuranceWorkExperience[0].isInsuranceWork}}是{{??}}否{{?}}</span></p>\
                            {{?it.insuranceWorkExperience.length>0&&it.insuranceWorkExperience[0].isInsuranceWork}}\
                            <p><span>保险从业年限</span><span>{{=it.nativeWorkTime||"0"}}</span></p>\
                            <p><span>累计签寿险单的客户量（人）</span><span>{{=it.totalPortfolio||"0"}}人</span></p>\
                            <p><span>最多同时辅导/管理人力（人）</span><span>{{=it.maxCoacher||"0"}}人</span></p>\
                            {{~it.insuranceWorkExperience:v:i}}\
                            <p><span>起止时间</span><span>{{=v.startTime.replace("-","年").replace("-01","月")}}-{{=v.endTime.replace("-","年").replace("-01","月")}}</span></p>\
                            <p><span>公司</span><span>{{=v.company||""}}</span></p>\
                            <p><span>部门</span><span>{{=v.occupation||""}}</span></p>\
                            <p><span>职位</span><span>{{=v.post||""}}</span></p>\
                            {{~}}\
                            {{?}}\
                        </div>\
                        \
                    </li>\
                    {{?it.workExperienceList.length&&(!it.insuranceWorkExperience.length||!it.insuranceWorkExperience[0].isInsuranceWork)}}\
                    <li class="clearfix">\
                        <strong class="time">\
                            最近一份工作经历\
                        </strong>\
                        <img class="right_icon" src="../../images/right_icon1.png" alt="">\
                        <div class="work_msg">\
                            {{~it.workExperienceList:v:i}}\
                            <p><span>起止时间</span><span>{{=v.startTime.replace("-","年").replace("-01","月")}}-{{=v.endTime.replace("-","年").replace("-01","月")}}</span></p>\
                            <p><span>公司</span><span>{{=v.company||""}}</span></p>\
                            <p><span>部门</span><span>{{=v.occupation||""}}</span></p>\
                            <p><span>职位</span><span>{{=v.post||""}}</span></p>\
                            {{~}}\
                        </div>\
                    </li>\
                    {{?}}\
                </ul>'
                      _this.isClose=response.data.isClose;
                      _this.getInterview();
                      temp = doT.template(temp);
                      $('.work-details').append(temp(response.data));
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
            getUserInfo: function () {
                var _this = this;
                var data = {
                    id: this.personnelId
                };
                var params = {
                    url: '/personnel/customer/app-personnel/getPersonnel',
                    type: 'POST',
                    ifOpenLoading: true,
                    data: JSON.stringify(data)

                };
                Interface.getAsynData(params, function (response) {
                    if (response.code == "200") {
                        var res = response.data;
                        var curPost;
                        if (res.personnel.confirmPosition) {
                            curPost = res.personnel.confirmPosition;
                            $(".postType").text("核定");
                            $(".postType").parent('.right').find('.iconfont').addClass('icon-character-success')
                        } else {
                            curPost = res.personnel.protocolPosition;
                            $(".postType").text("拟定");
                            $(".postType").parent('.right').find('.iconfont').addClass('icon-renyuanzhunbei')
                        }
                        $('.rank p').eq(0).text(res.personnel.name);
                        //$('.rank p').eq(1).text(res.personnel.cellphone);
                        $('.rank p').eq(2).find('span').text(res.createName);
                        $('.right span').eq(0).text(common.getPostName(_this.post, curPost));
                        $('.we-chat').find('span').text(res.personnel.wechat);
                        $('.phone').find('span').text(res.personnel.cellphone);
                        // $('.content-work').attr('data-tempname',res.personnel.name);
                        // _this.rescriteInitWorkExperience();
                        //$('.email-message').find('span').text(res.personnel.email);
                        // if(res.personnel.sex == 'M'){
                        //
                        //     $('.portrait .iconfont').addClass('icon-male')
                        // }else if(res.personnel.sex == 'F'){
                        //     $('.portrait .iconfont').addClass('icon-female')
                        // }

                        if (res.personnel.sex == 'F') {
                            $('.portrait').find('i').addClass('icon-female');
                            $('.portrait').find('img').attr('src', '../../images/feman-top.jpg')
                        } else if (res.personnel.sex == 'M') {
                            $('.portrait').find('img').attr('src', '../../images/top-pic.jpg');
                            $('.portrait').find('i').addClass('icon-male')
                        }

                        if (res.personnel.personnelStatus == "7" && res.personnel.checkResult == "1") {
                            $(".bottom-content li:last-child").addClass('pass').find("span.fr").text('已签约');
                            var imgSrc= $(".bottom-content li:last-child").find('.left').attr('img-pass');
                            $(".bottom-content li:last-child").find('.left img').attr('src',imgSrc);

                        } else if (res.personnel.personnelStatus == "7" && res.personnel.checkResult == "2") {
                            var imgSrc= $(".bottom-content li:last-child").find('.left').attr('img-fail');
                            $(".bottom-content li:last-child").find('.left img').attr('src',imgSrc);
                            $(".bottom-content li:last-child").addClass('no-pass').find("span.fr").text("不通过");
                        }
                    }

                }, function (error) {

                });
            },
            // rescriteInitWorkExperience: function () {
            //     var _this = this;
            //     var param = {
            //         url: '/personnel/customer/app-personnel/getInsuranceWorkExperienceByPersonnelId',
            //         type: 'POST',
            //         contentType: 'application/x-www-form-urlencoded',
            //         data: {
            //             personnelId: _this.personnelId
            //         }
            //     };

            //     Interface.getAsynData(param, function (response) {
            //         if (response.code == "200") {
            //             var data=response.data;
            //             if(data){
            //                 $(".content-work").children('span').html($(".content-work").data("tempname") +'于'+data.startTime.replace('-','年').replace('-','月')+'-'+data.endTime.replace('-','年').replace('-','月')+'于'+data.company+'保险公司就职，职位为'+data.post)
            //             }else{
            //                 $(".content-work").children('span').html('无')
            //             }
            //         }
            //     }, function (error) {

            //     });
            // },
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
                    _this.getUserInfo();
                });
            },
            getFlowNode: function () {
                var _this = this;
                var dictParam = {
                    url: '/flow/customer/interviewFlow/getFlowItemList',
                    type: 'GET',
                    contentType: 'application/x-www-form-urlencoded',
                    data: {}
                };
                Interface.getAsynData(dictParam, function (response) {
                    if (response.code == "200") {
                        var flowNode = '{{~it:value:index}}\
                          <li {{=value.moudleName==1?"hidden":"" }} class="clearfix step1 flowNode{{=value.id}}">\
		                       <div class="time fl">\
		                            <span>08:00</span><br><em></em>\
		                        </div>\
		                        <div class="left-fathter fl">\
		                            <div class="left bor_1FD09B">\
		                                <i class="iconfont icon-jihuashucur col_1FD09B"></i>\
		                            </div>\
		                        </div>\
		                        <div class="right fl col_1FD09B">\
		                            <p class="clearfix"><span>{{=value.flowItemName||""}}</span><span></span><span class="fr"></span></p>\
		                            <p class="over-flow"><span></span><i class="iconfont icon-jiantouarrow483 fr tab-slide"></i></p>\
		                        </div>\
		                    </li>\
		                 {{~}}';
                        flowNode = doT.template(flowNode);
                        $('.details ul').append(flowNode(response.data));
                    }
                });
            },
            tabInfo: function (btn) {
                var _this = btn.data.fun;
                if (_this.infoFlag) {
                    $(this).css('transform', 'rotate(0deg)');

                    $('.content-details').hide();
                    // $('.content-work').hide();
                    _this.infoFlag = false
                } else {
                    $(this).css('transform', 'rotate(180deg)');

                    $('.content-details').show();
                    // $('.content-work').show();
                    _this.infoFlag = true;
                }
            }
        };

        recheck.init(opt.body);
    }
});