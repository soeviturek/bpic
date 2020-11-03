define(function (require, exports) {
    require('datepicker');
    require('moment');
    require('BScroll')
    var template = '';
    exports.back = function () {
        return '';
    };

    exports.show = function (opt) {
        var recheck = {
            init: function (obj) {
                this.obj = obj.body;
                this.personnelId = common.getHash('personnelId') || '';
                this.shareInfoId = common.getHash('shareInfoId')||'';


                if (this.personnelId) {
                    this.getStatu();
                } else {
                    common.alertCreate({
                        html: "无效链接"
                    });
                }
                this.scroll = null;
                this.pageNo = 1;
                this.pageSize = 5;
                this.checkMore = true;//判断是否还有数据
                this.isFirstComing = true;//第一次初始化滚动
                this.obj.off('tap');
                this.obj.on('tap', {fun: this}, this.hideBankSearch);

                // 通过
                this.obj.off('tap', '.fircheck-view .bottom button:first-child');
                this.obj.on('tap', '.fircheck-view .bottom button:first-child', {fun: this}, this.subInterResult);

                //添加信息
                this.obj.off('tap', '.add-panel');
                this.obj.on('tap', '.add-panel', {fun: this}, this.addPanel);

                this.obj.off('tap', '.bank-box li:not(:nth-of-type(1))');
                this.obj.on('tap', '.bank-box  li:not(:nth-of-type(1))', {fun: this}, this.setBankName);

                this.obj.off('tap', '.select-bank');
                this.obj.on('tap', '.select-bank', {fun: this}, this.selectBank);

                this.obj.off('input propertychange', '.bank-branch');
                this.obj.on('input propertychange', '.bank-branch', {fun: this}, this.searchBank);

                this.obj.off('blur', '.base-info .bank-branch');
                this.obj.on('blur', '.base-info .bank-branch', {fun: this}, this.clearBank);

                //删除信息
                this.obj.off('tap', '.del-panel');
                this.obj.on('tap', '.del-panel', {fun: this}, this.delPanel);

                // 单选择
                this.obj.off('tap', '.sex-common,.ok-common');
                this.obj.on('tap', '.sex-common,.ok-common', {fun: this}, this.radioDate);

                //
                this.obj.off('tap', '.ok-common');
                this.obj.on('tap', '.ok-common', {fun: this}, this.checkZs);

                /* 开始时间校验 */
                this.obj.off('blur', '[name=startTime]');
                this.obj.on('blur', '[name=startTime]', {fun: this}, this.verifyTime);

                /* 结束时间校验 */
                this.obj.off('blur', '[name=endTime]');
                this.obj.on('blur', '[name=endTime]', {fun: this}, this.verifyTime);

                // 下一步
                this.obj.off('tap', '#next-page');
                this.obj.on('tap', '#next-page', {fun: this}, this.bascInfoGoNext);

            },
            getStatu:function(){
                var _this = this;
                var data = {
                    personnelId: _this.personnelId,
                    id:_this.shareInfoId
                };
                var params = {
                    url: '/personnel/customer/app-personnel/exclusions/checkUrl',
                    type: 'POST',
                    ifOpenLoading: true,
                    data: JSON.stringify(data)
                };
                Interface.getAsynData(params,function (respData) {

                    if (respData.code == "200" ) {
                        template = require('./base-info.html');
    					template = doT.template(template);
                        _this.initBascInfo();
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
            },
            verifyTime: function (btn) {
                var _parent = $(this).parent();
                if (_parent.find('[name=startTime]').val() != '' && _parent.find('[name=endTime]').val() != '') {
                    if (new Date(_parent.find('[name=endTime]').val()) < new Date(_parent.find('[name=startTime]').val())) {
                        common.alertCreate({
                            html: '结束时间不能小于开始时间',
                            callback: function () {
                                _parent.find('[name=endTime]').val('')
                            }
                        });
                        return false;
                    }
                }
                return true;
            },
            initSelectData: function () {
                var parmaData = {
                    url: '/web/system/dict/data/type/findByCodes',
                    type: 'POST',
                    contentType: 'application/x-www-form-urlencoded',
                    ifOpenLoading: true,
                    data: {
                        codes: 'nation,political,education,relationship'
                    }
                };
                Interface.getAsynData(parmaData, function (data) {
                    var res = data.data;
                    for (var val in res) {
                        var temp = '<option disabled value="" selected>请选择</option>\
                                    {{~it.' + val + ':value:index}}\
                                    <option value="{{=value.code || ""}}">{{=value.code}}</option>\
                                {{~}}';
                        temp = doT.template(temp);
                        $('.' + val).html(temp(res));
                        if ($('.contacts-info').length >= 2) {
                            $('.contacts-info').eq(1).find('.add-panel').hide();
                            $('.contacts-info').eq(0).find('.add-panel').hide();
                            $('.contacts-info').eq(1).find('.del-panel').show();
                        }
                    }
                }, function (error) {
                    console.log(error)
                })
            },
            initTime: function () {
                $('[name=startTime]').each(function (index, elem) {
                    if ($(elem).data('time') == 'off') {
                        $(elem).attr('data-time', 'on');
                        /* 选择日期 */
                        var calendar = new LCalendar();
                        calendar.init({
                            'trigger': elem,
                            'type': 'ym',//date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择范围
                            'initTime': moment(new Date()).format('YYYY/MM'),
                        });
                    }

                });
                $('[name=endTime]').each(function (index, elem) {
                    if ($(elem).data('time') == 'off') {
                        $(elem).attr('data-time', 'on');
                        /* 选择日期 */
                        var calendar = new LCalendar();
                        calendar.init({
                            'trigger': elem,
                            'type': 'ym',//date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择范围
                            'initTime': moment(new Date()).format('YYYY/MM'),
                        });
                    }
                })
            },
            addPanel: function (btn) {
                var $this = $(this);
                var _this = btn.data.fun;
                var type = $this.data('type');
                if ($('.contacts-info').length >= 2) {
                    $('.contacts-info').eq(1).find('.add-panel').hide();
                }
                var html = '';
                $this.hide();
                switch (type) {
                    // case 'educa':
                    //     html = '<div class="info-list educa-info">\
                    //                 <h3>学历信息</h3>\
                    //                 <div class="info-text time-text">\
                    //                     <label>时间</label>\
                    //                     <input type="text" value="" name="endTime" class="datepicker" placeholder="结束时间" readonly>\
                    //                     <input type="text" value="" name="startTime" class="datepicker" placeholder="开始时间" readonly>\
                    //                 </div>\
                    //                 <div class="info-text">\
                    //                     <label>学校名称</label>\
                    //                     <input type="text" value="" name="shooclName" class="">\
                    //                 </div>\
                    //                 <div class="info-text">\
                    //                     <label>学历</label>\
                    //                     <div class="ipt-select">\
                    //                         <input type="text" class="ipt-text" value="" name="education" placeholder="请选择学历" readonly>\
                    //                         <select class="education">\
                    //                             <option disabled value="" selected>请选择学历</option>\
                    //                         </select>\
                    //                     </div>\
                    //                     <i class="iconfont icon-jiantou narrow"></i>\
                    //                 </div>\
                    //                 <div class="info-text">\
                    //                     <label>专业</label>\
                    //                     <input type="text" value="" name="specialty" class="">\
                    //                 </div>\
                    //                 <div class="info-text">\
                    //                     <label>学位</label>\
                    //                     <input type="text" value="" name="degree" class="">\
                    //                 </div>\
                    //                 <div class="add-del clearfix">\
                    //                     <div class="add-panel" data-type="educa">+&nbsp;继续添加学历信息</div>\
                    //                     <div class="del-panel" data-type="educa">X&nbsp;删除学历信息</div>\
                    //                 </div>\
                    //             </div>'
                    //     $('.'+ type +'-box').append(html);
                    // break;
                    case 'job':
                        html = '<div class="info-list job-info">\
                                        <h3>工作经历</h3>\
                                        <div class="info-text time-text must-sign">\
                                            <label>时间</label>\
                                            <input type="text" data-time="off" value="" name="endTime" class="" placeholder="结束时间" readonly>\
                                            <input type="text" data-time="off" value="" name="startTime" class="" placeholder="开始时间" readonly>\
                                        </div>\
                                        <div class="info-text must-sign">\
                                            <label>公司</label>\
                                            <input type="text" value="" name="company" class="" placeholder="请填写最近工作经历">\
                                        </div>\
                                        <div class="info-text must-sign">\
                                            <label>部门</label>\
                                            <input type="text" value="" name="department" class="">\
                                        </div>\
                                        <div class="info-text must-sign">\
                                            <label>职位</label>\
                                            <input type="text" value="" name="position" class="">\
                                        </div>\
                                        <div class="add-del clearfix">\
                                            <div class="add-panel" data-type="job">+&nbsp;继续添加工作经历</div>\
                                            <div class="del-panel" data-type="job">X&nbsp;删除工作经历</div>\
                                        </div>\
                                    </div>'

                        $('.' + type + '-box').append(html);
                        if ($('.job-info').size() == 3) {//最多三个工作经历
                            $('.job-info').eq(2).find('.add-panel').hide();
                        }

                        break;
                    case 'contacts':
                        html = '<div class="info-list contacts-info">\
                                        <h3>紧急事件联系人<span>(必须填写至少1名至多2名紧急联络人)</span></h3>\
                                        <div class="info-text must-sign">\
                                            <label>姓名</label>\
                                            <input type="text" value="" name="name" class="">\
                                        </div>\
                                        <div class="info-text must-sign">\
                                            <label>关系</label>\
                                            <div class="ipt-select">\
                                                <input type="text" class="ipt-text" value="" name="relation" placeholder="请选择关系" readonly>\
                                                <select class="relationship">\
                                                    <option disabled value="" selected>请选择关系</option>\
                                                </select>\
                                            </div>\
                                            <i class="iconfont icon-jiantou narrow"></i>\
                                        </div>\
                                        <div class="info-text must-sign">\
                                            <label>手机号</label>\
                                            <input type="text" maxlength="11" value="" name="phone" class="">\
                                        </div>\
                                        <div class="add-del clearfix">\
                                            <div class="add-panel" data-type="contacts">+&nbsp;继续添加紧急联系人</div>\
                                            <div class="del-panel" data-type="contacts">X&nbsp;删除紧急联系人</div>\
                                        </div>\
                                    </div>';
                        $('.' + type + '-box').append(html);
                        break;
                }

                var size = $('.' + type + '-box').find('.' + type + '-info').size();
                if (type == 'job' || type == 'educa') {
                    if (size > 1) {
                        $('.' + type + '-info').find('.del-panel').show();
                        if(type == 'job'){
                            $('.' + type + '-info').find('.del-panel').eq(0).hide();
                        }
                    } else {
                        $('.' + type + '-info').find('.del-panel').hide();
                    }
                } else if (type == 'contacts') {
                    if (size >= 2) {
                        $('.' + type + '-info').find('.del-panel').show();
                    } else {
                        $('.' + type + '-info').find('.del-panel').hide();
                    }
                }
                _this.initTime();
                _this.initSelectData();
            },
            delPanel: function (btn) {
                var $this = $(this);
                var type = $this.data('type');
                $this.parents('.info-list').remove();
                var size = $('.' + type + '-box').find('.' + type + '-info').size();
                if (type == 'job' || type == 'educa') {
                    if (size > 1) {
                        $('.' + type + '-info').find('.del-panel').show();
                        $('.' + type + '-info').eq(size - 1).find('.add-panel').show();
                        if(type == 'job'){
                            $('.' + type + '-info').find('.del-panel').eq(0).hide();
                        }
                    } else {
                        $('.' + type + '-info').find('.del-panel').hide();
                        $('.' + type + '-info').find('.add-panel').show()
                    }
                } else if (type == 'contacts') {
                    if (size >= 1) {
                        $('.' + type + '-info').find('.del-panel').hide();
                        $('.' + type + '-info').eq(size - 1).find('.add-panel').show();
                    } else {
                        $('.' + type + '-info').find('.del-panel').hide();
                        $('.' + type + '-info').eq(1).find('.add-panel').show();
                    }
                }
            },
            initBascInfo: function () {
                var _this = this;
                $(".icon-back").css("display", "none");
                var parma = {
                    url: '/personnel/customer/app-personnel/exclusions/getPersonnelBaseInfo',
                    type: 'POST',
                    ifOpenLoading: true,
                    contentType: 'application/json',
                    data: JSON.stringify({
                        personnelId: _this.personnelId
                    })
                };

                Interface.getAsynData(parma, function (response) {
                    if (response.code == "200") {
                       if(response.data.educationalList.length) {
                        response.data.educationalList.forEach(function(v){
                            v.endTime = v.endTime && v.endTime.substr(0,7);
                            v.startTime = v.startTime && v.startTime.substr(0,7)
                        })
                       }
                       if(response.data.workExperienceList.length) {
                        response.data.workExperienceList.forEach(function(v){
                            v.endTime = v.endTime && v.endTime.substr(0,7);
                            v.startTime = v.startTime && v.startTime.substr(0,7)
                        })
                       }
                       if(response.data.insuranceWorkExperience.length) {
                        response.data.insuranceWorkExperience.forEach(function(v){
                            v.endTime = v.endTime && v.endTime.substr(0,7);
                            v.startTime = v.startTime && v.startTime.substr(0,7)
                        })
                       }

                       _this.obj.html(template(response.data));
                       if(response.data.personnel.isClose){
                            $('#next-page').hide()
                        }
                        _this.getBankName(response);
                        //_this.initTime();
                        _this.initSelectData();
                        var birthday = new LCalendar();
                        _this.initScroll();
                        birthday.init({
                            'trigger': $('[name=birthday]'),//标签class
                            'type': 'date',//date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择范围
                            'initTime': moment(new Date()).format('YYYY/MM/DD'),
                        });

                        $('[name=startTime]').each(function (index, elem) {
                            /* 选择日期 */
                            var calendar = new LCalendar();
                            calendar.init({
                                'trigger': elem,
                                'type': 'ym',//date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择范围
                                'initTime': moment(new Date()).format('YYYY/MM'),
                            });
                        });
                        $('[name=endTime]').each(function (index, elem) {
                            /* 选择日期 */
                            var calendar = new LCalendar();
                            calendar.init({
                                'trigger': elem,
                                'type': 'ym',//date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择范围
                                'initTime': moment(new Date()).format('YYYY/MM'),
                            });
                        });
                        if(response.data.insuranceWorkExperience.length&&response.data.insuranceWorkExperience[0].isInsuranceWork){
                            $('.insurance_work').find('.info-text').addClass('must-sign');
                        }else if(!response.data.insuranceWorkExperience.length||!response.data.insuranceWorkExperience[0].isInsuranceWork){
                            $('.insurance_work').find('.info-text').removeClass('must-sign');
                        }
                    }
                });
            },
            getWorkList:function(){
                var _this = this;
                $(".icon-back").css("display", "none");
                var parma = {
                    url: '/personnel/customer/',
                    type: 'POST',
                    ifOpenLoading: true,
                    contentType: 'application/x-www-form-urlencoded',
                    data: {
                        personnelId: _this.personnelId
                    }
                };

                Interface.getAsynData(parma, function (response) {
                    if (response.code == "200") {
                       if(response.data.educationalList.length) {
                        response.data.educationalList.forEach(function(v){
                            v.endTime = v.endTime && v.endTime.substr(0,7);
                            v.startTime = v.startTime && v.startTime.substr(0,7)
                        })
                       }
                       if(response.data.workExperienceList.length) {
                        response.data.workExperienceList.forEach(function(v){
                            v.endTime = v.endTime && v.endTime.substr(0,7);
                            v.startTime = v.startTime && v.startTime.substr(0,7)
                        })
                       }
                       var temp = '{{~it:v:i}}\
							\
						{{~}}'
							temp = doT.template(temp);

                       $('.job-box').html(template(response.data));


                    }
                });
            },
            radioDate: function () {
                $(this).addClass('active').siblings().removeClass('active');
            },
            checkZs: function (btn) {
                var _obj = $(this);
                if (_obj.data('val') == '1') {
                    $('.zs-check').show()
                } else {
                    $('.zs-check').find('input[type=checkbox]').prop('checked', false);
                    $('.zs-check').find('input[type=text]').val('');
                    $('.zs-check').hide()
                }
            },
            bascInfoGoNext: function (btn) {
                var _this = btn.data.fun;
                var encArr = [];//学历
                var jobArr = [];//工作
                var conArr = [];//联系人
                var insurance={} //保险经历后面四个参数
                var educaOk = true;
                var jobOk = true;
                var insuranceOk=true;
                var contactsOk = true;
                var contactsPhone = true;
                if (!_this.personnelId) {
                    common.alertCreate({
                        html: "无效链接"
                    });
                }
                // if($('.ok-text').find('.active').size() == 0){
                //     common.alertCreate({
                //         html: '请选择是否持有相关行业从业证书！'
                //     });
                //     return false;
                // }


                if ($('.base-info select[name=identityType]').val() == '111') {
                    if (!validation.idcard($('[name=identityCode]').val())) {
                        common.alertCreate({
                            html: '请输入正确的身份证号码（18位居民身份证号码）！'
                        });
                        return false;
                    }
                }
                if (!validation.checkValiMust($('#formData'))) {
                    return false;
                }

                if ($('.sex-text').find('.active').size() == 0) {
                    common.alertCreate({
                        html: '请选择性别！'
                    });
                    return false;
                }
                $('.educa-info').each(function (index, elem) {
                    $(elem).find('.must-sign input').each(function (i, e) {
                        if (!$.trim($(e).val())) {
                            educaOk = false;
                        }
                    });
                    encArr.push({
                        startTime: $(elem).find('[name=startTime]').val()? $(elem).find('[name=startTime]').val()+ '-01':'',
                        endTime: $(elem).find('[name=endTime]').val()?$(elem).find('[name=endTime]').val() + '-01':'',
                        school: $(elem).find('[name=shooclName]').val(),
                        education: $(elem).find('[name=education]').val(),
                        specialty: $(elem).find('[name=specialty]').val(),
                        degree: $(elem).find('[name=degree]').val()
                    })
                });

                $('.job-info').each(function (index, elem) {
                    $(elem).find('input').each(function (i, e) {
                        if (!$.trim($(e).val())) {
                            jobOk = true;
                        }
                    });

                    var totalYears = $('[name=totalyears]').val()||'';
                    var companyName = $.trim($(elem).find('[name=company]').val());
                    var startTime = $(elem).find('[name=startTime]').val();
                    var endTime = $(elem).find('[name=endTime]').val();
                    var occupation = $(elem).find('[name=department]').val();
                    var post = $(elem).find('[name=position]').val();
                    // if (companyName) {
                        if (!startTime || !endTime || !occupation || !post||!totalYears||!companyName) {
                            jobOk = false;
                        }
                    // }
                    jobArr.push({
                        startTime: $(elem).find('[name=startTime]').val()? $(elem).find('[name=startTime]').val()+ '-01':'',
                        endTime: $(elem).find('[name=endTime]').val()? $(elem).find('[name=endTime]').val()+ '-01':'',
                        company: $(elem).find('[name=company]').val(),
                        occupation: $(elem).find('[name=department]').val(),
                        post: $(elem).find('[name=position]').val()
                    })
                });

                for(var j=0;j<$('.contacts-info').length;j++){
                    $('.contacts-info').eq(j).find('input').each(function (i, e) {
                        if (!$.trim($(e).val())) {
                            contactsOk = false;
                        }
                    });
                }

                // if (!jobOk) {
                //     common.alertCreate({
                //         html: '请填写完整工作经历信息！'
                //     });
                //     return false;
                // }
                var workYears = $('.work-years select').val();
                // if (!workYears) {
                //     common.alertCreate({
                //         html: '请选择保险工作年限！'
                //     });
                //     return false;
                // }
                // if (!contactsOk) {
                //     common.alertCreate({
                //         html: '请填写完整紧急人信息！'
                //     });
                //     return false;
                // }

                $('.contacts-info').each(function (index, elem) {

                    // $(elem).find('input').each(function(i,e){
                    //     if(!$.trim($(e).val())){
                    //         contactsOk = false;
                    //     }
                    // });

                    conArr.push({
                        name: $(elem).find('[name=name]').val(),
                        relationship: $(elem).find('[name=relation]').val(),
                        telephone: $(elem).find('[name=phone]').val(),
                    })
                });

                if ($('[name=identityType] option:checked').val() == '111') {
                    var cardCode = $('[name=identityCode]').val();
                    var Y = cardCode.substring(6, 10);
                    var M = cardCode.substring(10, 12);
                    var D = cardCode.substring(12, 14);
                    var birthday = Y + "-" + M + "-" + D;
                    $("[name=birthday]").val(Y + "-" + M + "-" + D).prop('disabled', true);
                }


                // if (!educaOk) {
                //     common.alertCreate({
                //         html: '请填写完整学历信息！'
                //     });
                //     return false;
                // }

                // if(!contactsOk){
                //     common.alertCreate({
                //         html: '请填写完整紧急人信息！'
                //     });
                //     return false;
                // }

                // $('[name=phone]').each(function (index, elem) {
                //     if (!validation.checkMobiePho($(elem).val())) {
                //         common.alertCreate({
                //             html: '紧急联系人' + (index + 1) + '手机号不符合要求'
                //         });
                //         contactsPhone = false;
                //         return false
                //     }
                // });

                // if (!contactsPhone) {
                //     return false;
                // }
                $('.insurance_work').find('input').each(function(i,ele){
                    if($(ele).val()||$('.work-years select').val()){
                        $('.insurance_work').attr("data-isinsurancework",'1');
                    }else{
                        $('.insurance_work').attr("data-isinsurancework",'0');
                    }
                })
                insurance={
                    company: $('.insurance_work .company').find('input').val(),
                    endTime: $('.insurance_work').find('[name=endTime]').val()?$('.insurance_work').find('[name=endTime]').val()+ '-01':'',
                    occupation: $('.insurance_work .department').find('input').val(),
                    post: $('.insurance_work .job').find('input').val(),
                    startTime:$('.insurance_work').find('[name=startTime]').val()? $('.insurance_work').find('[name=startTime]').val()+'-01':'',
                    isInsuranceWork: $('.insurance_work').data('isinsurancework')
                }
                jobArr.push(insurance)
                // 保险从业是否必填校验 
                if($('.insurance_work').data('isinsurancework')){
                    $('.insurance_work').find('input').each(function(i,ele){
                        if(!$(ele).val()||!$('.work-years select').val()){
                            insuranceOk=false;
                            return;
                        }
                    })
                }
                // if(!insuranceOk){
                //     common.alertCreate({
                //         html: '请填写完整保险从业经历经历信息！'
                //     });
                //     return false;
                // }

                // if(!$('.select-bank').val()||!$('.select-bank').attr('tittle')){
                //     common.alertCreate({
                //         html: "请选择银行卡支行"
                //     });
                //     return
                // }
                // 银行支行和卡号有一个则另一个也要校验
                if((!$('.select-bank').val()||!$('.select-bank').attr('tittle'))&&$('.bank-cardno').val()){
                    common.alertCreate({
                        html: "请选择银行卡支行"
                    });
                    return
                }
                if($('.select-bank').val()&&$('.select-bank').attr('tittle')&&!$('.bank-cardno').val()){
                    common.alertCreate({
                        html: "请输入银行卡卡号"
                    });
                    return
                }
                // var data = {
                //     name: $('.base-info input[name=name]').val(),
                //     sex: $('.sex-text .sex-active').data('val'),
                //     onceName: $('.base-info input[name=onceName]').val(),
                //     cellphone: $('.base-info input[name=cellphone]').val(),
                //     telephone: $('.base-info input[name=telephone]').val(),
                //     identityType: $('.base-info select[name=identityType]').val(),
                //     identityCode: $('.base-info input[name=identityCode]').val(),
                //     familyAddrDetail: $('.base-info input[name=familyAddrDetail]').val(),
                //     postcode: $('.base-info input[name=postcode]').val(),
                //     contactAddress: $('.base-info input[name=contactAddress]').val(),
                //     id: _this.personnelId
                // };

                var data = {
                    personnel: {
                        name: $('.base-info input[name=name]').val(),
                        sex: $('.sex-text .sex-active').data('val'),
                        onceName: $('.base-info input[name=onceName]').val(),
                        birthday: $('.base-info input[name=birthday]').val(),
                        political: $('.base-info input[name=political]').val(),
                        email: $('.base-info input[name=email]').val(),
                        bankBranch: $('.select-bank').attr('tittle'),
                        chinaCiticBank: $('.bank-cardno').val(),
                        wechat: $('.base-info input[name=wechat]').val(),
                        nation: $('.base-info input[name=nation]').val(),
                        cellphone: $('.base-info input[name=cellphone]').val(),
                        telephone: $('.base-info input[name=telephone]').val(),
                        identityType: $('.base-info select[name=identityType]').val(),
                        identityCode: $('.base-info input[name=identityCode]').val(),
                        familyAddrDetail: $('.base-info input[name=familyAddrDetail]').val(),
                        postcode: $('.base-info input[name=postcode]').val(),
                        domicilePlace: $('.base-info input[name=domicilePlace]').val(),
                        contactAddress: $('.base-info input[name=contactAddress]').val(),
                        jobCertificate: $('.base-info .ok-common.active').data('val'),
                        totalPortfolio: $('.base-info input[name=totalPortfolio]').val(), //累计签寿险单的客户量(人)
                        maxCoacher: $('.base-info input[name=maxCoacher]').val(), //最多同时辅导/管理人力(人）
                        certificate1: $('[name=certificate1]:checked').val() || '0',
                        certificate2: $('[name=certificate2]:checked').val() || '0',
                        certificate3: $('[name=certificate3]:checked').val() || '0',
                        certificate4: $('[name=certificate4]:checked').val() || '0',
                        certificateOther: $('[name=certificate_other]').val(),
                        id: _this.personnelId,
                        nativeWorkTime: $('.work-years select').val() || '', //保险从业年限
                        bankName: $('.bank-select select').val() || '',
                        totalWorkTime:$('[name=totalyears]').val()||'',
                    },
                    familyMemberList: conArr,
                    educationalList: encArr,
                    workExperienceList: jobArr
                };
                console.log(data);
                var personnelData = {
                    id: _this.personnelId
                }
                var personnelParam = {
                    url: '/personnel/customer/app-personnel/exclusions/getPersonnel',
                    type: 'POST',
                    ifOpenLoading: true,
                    data: JSON.stringify(personnelData)
                };

                Interface.getAsynData(personnelParam, function (response) {
                    if (response.code == "200") {
                        console.log(data)
                        var personnel = response.data.personnel;
                        if (personnel && (personnel.personnelStatus == "6" || personnel.personnelStatus == "7") && (personnel.checkResult == null || personnel.checkResult == "2")) {
                            var parma = {
                                url: '/personnel/customer/app-personnel/exclusions/updatePersonnelBaseInfo',
                                type: 'POST',
                                ifOpenLoading: true,
                                data: JSON.stringify(data)
                            };

                            Interface.getAsynData(parma, function (response) {
                                if (response.code == "200") {
                                    return window.location.href = '#page=inform&personnelId=' + _this.personnelId + '&shareInfoId='+_this.shareInfoId;
                                } else {
                                    common.alertCreate({
                                        html: response.msg
                                    });
                                }
                            });
                        } else {
                            return window.location.href = '#page=inform&personnelId=' + _this.personnelId + '&shareInfoId='+_this.shareInfoId;
                        }
                    }
                });
            },
            searchBank: function (btn) {
                var _this = this;
                if (btn) {
                    var that = btn.data.fun;
                } else {
                    var that = this;
                }
                $('.select-bank').attr('tittle','');
                var bankCode = $('.bank-select select').val();
                $('.select-bank').val($('.bank-branch').val());
                setTimeout(function () {
                    // var bankName=$(_this).val();
                    var bankName = $('.bank-branch').val();
                    var data = {
                        cnName: bankName,
                        codes: bankCode,
                        pageNo: that.pageNo,
                        pageSize: that.pageSize,
                        pageSortFiled: 'sort',
                        pageSortType: 'asc'
                    };
                    var parma = {
                        url: '/web/system/dict/data/type/findByCodes',
                        type: 'POST',
                        contentType: 'application/x-www-form-urlencoded',
                        ifOpenLoading: false,
                        data: data
                    };

                    Interface.getAsynData(parma, function (response) {
                        if (response.code == "200") {
                            console.log(response.data[bankCode]);
                            if (btn) {
                                that.pageNo = 1;
                                $('.bank-box li:not(:nth-of-type(1))').remove();
                            }
                            if (response.data[bankCode]) {
                                var html = '';
                                $('.bank-content').show();
                               response.data[bankCode].forEach(function (item, index) {
                                    html += '<li tittle='+item.value+'>'+item.code + '</li>'
                                });
                                $('.bank-box').append(html);

                            }

                            if (that.isFirstComing) {
                                that.isFirstComing = false;
                            } else {
                                that.scroll && that.scroll.refresh();
                            }
                            // if (response.data.records.length < that.pageSize) {
                            //     that.checkMore = false;
                            // }

                        }
                    });
                }, 300)
            },
            clearBank:function(){
                $('.select-bank').val('');
                $('.bank-branch').val('');
            },
            setBankName: function () {
                var val = $(this).text();
                var code=$(this).attr('tittle')
                $('.select-bank').val(val);
                $('.select-bank').attr('tittle',code);
                $('.bank-branch').val('');
                $('.bank-box li:not(:nth-of-type(1))').remove();
                setTimeout(function () {
                    $('.bank-content').hide();
                }, 350)
            },
            selectBank: function () {
                var bankCode = $('.bank-select select').val();
                $('.select-bank').val('');
                if (!bankCode) {
                    common.alertCreate({
                        html: '请选择银行卡',
                        callback: function () {
                        }
                    });
                    return false;
                }

                $('.bank-content').show()
            },
            hideBankSearch: function (btn) {
                var target = btn.target;
                var _this = btn.data.fun;
                _this.pageNo = 1;
                if ($(target).hasClass('select-bank') || $(target).hasClass('bank-content') || $(target).hasClass('bank-branch')) {
                    $('.bank-content').show();
                    $('.bank-branch').focus();

                } else {
                    setTimeout(function () {
                        $('.bank-content').hide();
                    }, 350)
                }
            },
            getBankName: function (res) {
                var data = {
                    codes: 'BANK'
                };
                var parma = {
                    url: '/web/system/dict/data/type/findByCodes',
                       contentType: 'application/x-www-form-urlencoded',
                    type: 'POST',
                    ifOpenLoading: false,
                    data: data
                };

                Interface.getAsynData(parma, function (response) {
                    if (response.code == "200") {

                        if (response.data ) {
                            var html = '';

                            response.data.BANK.forEach(function (item, index) {
                                html += '<option value="' + item.value + '">' + item.code + '</option>'
                            });
                            $('.bank-select select').append(html);
                            // $('.bank-select select').find('option[value=' + res.data.personnel.bankName + ']').attr('selected', true);
                            $('.bank-select select').val(res.data.personnel.bankName)
                            // $('.bank-select select').val(res.data.personnel.bankName)

                        }
                    }
                });
            },
            initScroll: function () {
                var _this = this;
                this.scroll = new BScroll($('.bank-content')[0], {
                    scrollX: false,
                    scrollY: true,
                    probeType: 1
                });
                this.scroll.off('scroll');
                // this.scroll.on('scroll', function (pos) {
                //     this.scrollDisct = pos.y;
                //     // if(pos.y>10){
                //     //     $('.drop-tips').find('img').css('transform','rotate(0deg)');
                //     //     $('.drop-tips').show().find('span').text('下拉刷新');
                //     //
                //     // }
                //     if(pos.y>50){
                //         $('.drop-tips').find('img').css('transform','rotate(180deg)');
                //         $('.drop-tips').show().find('span').text('释放立即刷新');
                //     }
                // });
                this.scroll.off('scrollEnd');
                this.scroll.on('scrollEnd', function (pos) {
                    console.log(_this.scroll.y);
                    if ((_this.scroll.y <= (_this.scroll.maxScrollY + 50))) {
                        _this.pageNo++;
                        _this.searchBank()
                    }

                    if (this.scrollDisct > 50) {
                        _this.dropDown = true;
                        _this.ifOpenLoading = true;
                        _this.pageNo = 1;
                        $('.se-con').empty();
                        _this.getTabList();
                        this.scrollDisct = 0;
                    }
                })
            }
        };
        recheck.init(opt);
    }

});