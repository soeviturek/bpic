define(function (require, exports) {
    require('datepicker');
    require('moment');
    require('../../css/invite.css');
    require('../../lib/mobileSelect/mpicker.css');
    //require('../../lib/jquery.min');
    var template = '';
    exports.back = function () {
        return '';
    };

    exports.show = function (opt) {
        var recheck = {
            init: function (obj) {
                this.obj = obj;
                this.post = [];
                this.channel = common.getHash('channel') || '';
                this.personnelId = common.getQueryString('personnelId');
                this.createUser = common.getQueryString('createUser');
                this.shareInfoId = common.getHash('shareInfoId');
                if (this.personnelId) {
                    this.getStatu();
                } else {
                    $('.ipt-select select').removeAttr('disabled');
                    this.getDictList();
                }
                this.initTime({
                    fun: this
                })
                // 保存
                this.obj.off('tap', '#submit');
                this.obj.on('tap', '#submit', {
                    fun: this
                }, this.sendData);

                // 是否有经验
                this.obj.off('change', '.has_experience input[name=iswork]');
                this.obj.on('change', '.has_experience input[name=iswork]', {
                    fun: this
                }, this.isWork);

                /* 开始时间校验 */
                this.obj.off('blur', '[name=startTime]');
                this.obj.on('blur', '[name=startTime]', {
                    fun: this
                }, this.verifyTime);

                /* 结束时间校验 */
                this.obj.off('blur', '[name=endTime]');
                this.obj.on('blur', '[name=endTime]', {
                    fun: this
                }, this.verifyTime);
            },
            isWork: function () {
                if ($(this).val() == '0') {
                    $(this).parents('.has_experience').nextAll().addClass('hidden');
                    $('.recent_wort').removeClass('hidden');
                } else if ($(this).val() == '1') {
                    $(this).parents('.has_experience').nextAll().removeClass('hidden');
                    $('.recent_wort').addClass('hidden');
                }

            },
            initTime: function () {
                $('[name=startTime]').each(function (index, elem) {
                    if ($(elem).data('time') == 'off') {
                        $(elem).attr('data-time', 'on');
                        /* 选择日期 */
                        var calendar = new LCalendar();
                        calendar.init({
                            'trigger': elem,
                            'type': 'date', //date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择范围
                            'initTime': moment(new Date()).format('YYYY/MM/DD'),
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
                            'type': 'date', //date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择范围
                            'initTime': moment(new Date()).format('YYYY/MM/DD'),
                        });
                    }
                })
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
            /*  校验数据  */
            sendData: function (btn) {
                var _this = btn.data.fun;
                $('input').blur();
                $("html,body").animate({scrollTop: document.documentElement.clientHeight+666},500);
                var data = _this.getVal();
                /*   data.id = $('input[name=isShare]').val();*/
                data.id = _this.personnelId;
                var educational = {},
                    personnel = {},
                    workExperience = {};
                educational = {
                    education: data.educational
                };
                personnel = {
                    cellphone: data.cellphone,
                    channel: data.channel,
                    createUser: data.createUser,
                    id: data.id,
                    isInsuranceCompany: data.hasExperience,
                    name: data.name,
                    nativeWorkTime: data.insuranceYears,
                    protocolPosition: data.protocolPosition,
                    sex: data.sex,
                    totalWorkTime: data.workYears,
                    totalPortfolio: data.userNumber,
                    maxCoacher: data.workNumber,
                };
                workExperience = {
                    company: data.hasExperience == '1' ? data.company_1 : data.company_2,
                    endTime: data.hasExperience == '1' ? data.endTime_1 + '-01' : data.endTime_2 + '-01',
                    occupation: data.hasExperience == '1' ? data.department_1 : data.department_2,
                    post: data.hasExperience == '1' ? data.post_1 : data.post_2,
                    startTime: data.hasExperience == '1' ? data.startTime_1 + '-01' : data.startTime_2 + '-01',
                    isInsuranceWork: data.hasExperience
                };
                if (!data.name) {
                    common.alertCreate({
                        html: '请填写姓名',
                    });
                    return false
                }
                if (!data.sex) {
                    common.alertCreate({
                        html: '请选择性别',
                    });
                    return false
                }
                if (!data.cellphone) {
                    common.alertCreate({
                        html: '请输入手机号码',
                    });
                    return false
                }
                if (data.cellphone && !validation.checkMobiePho(data.cellphone)) {
                    common.alertCreate({
                        html: '该手机号不符合要求',
                    });
                    return false
                }
                if (!data.educational) {
                    common.alertCreate({
                        html: '请选择最高学历',
                    });
                    return false
                }
                if (!data.workYears) {
                    common.alertCreate({
                        html: '请填写工作总年限',
                    });
                    return false
                }
                if (data.hasExperience == '1') {
                    if (!data.insuranceYears) {
                        common.alertCreate({
                            html: '请选择保险从业年限',
                        });
                        return false
                    };
                    if (!data.userNumber) {
                        common.alertCreate({
                            html: '请填写累计签寿险单的客户量',
                        });
                        return false
                    };
                    if (!data.workNumber) {
                        common.alertCreate({
                            html: '请填写最多同时辅导/管理人力',
                        });
                        return false
                    }
                }
                if (workExperience.startTime=='-01') {
                    common.alertCreate({
                        html: '请选择开始时间',
                    });
                    return false
                }
                if (workExperience.endTime=='-01') {
                    common.alertCreate({
                        html: '请选择结束时间',
                    });
                    return false
                }
                if (!workExperience.company) {
                    common.alertCreate({
                        html: '请填写公司',
                    });
                    return false
                }
                if (!workExperience.occupation) {
                    common.alertCreate({
                        html: '请填写部门',
                    });
                    return false
                }
                if (!workExperience.post) {
                    common.alertCreate({
                        html: '请填写职位',
                    });
                    return false
                }
                
                
                var _data = {
                    educationalList: [educational],
                    personnel: personnel,
                    workExperienceList: [workExperience]
                }
                var params = {
                    url: '/personnel/customer/app-personnel/exclusions/savePersonnelExp',
                    type: 'POST',
                    ifOpenLoading: true,
                    data: JSON.stringify(_data)

                };
                Interface.getAsynData(params, function (response) {
                    if (response.code == "200") {
                        $('input[name=isShare]').val(response.data);
                        common.alertCreate({
                            html: '您已提交成功，报聘流程进行中，请敬待通知',
                            callback: function () {
                                /*  _this.obj.off('tap', '#submit');
                        	    $("#submit").addClass("invite-vain");*/
                                /* return window.location.href = '#page=main';*/
                            }
                        })
                    }
                }, function (error) {
                    common.alertCreate({
                        html: error.msg
                    })
                });
            },
            /*  字典  */
            getDict: function (data) {
                var _this = this;
                var dictParam = {
                    url: '/web/system/dict/data/type/findByCodes',
                    type: 'POST',
                    contentType: 'application/x-www-form-urlencoded',
                    ifOpenLoading: true,
                    data: {
                        codes: 'rank,education'
                    }
                };

                Interface.getAsynData(dictParam, function (response) {
                    if (response.code == "200") {
                        response.data.personnel = data;
                        var temp = '{{~it.rank:value:index}}\
                                   		 <option value="{{=value.code || ""}}" {{=value.code===it.personnel.protocolPosition?"selected":""}}>{{=value.value}}</option>\
                                    {{~}}';
                        temp = doT.template(temp)
                        $('select[name=protocolposition]').append(temp(response.data));
                        _this.post = response.data['rank'];
                        $('select[name=protocolposition]').prev().val(common.getPostName(_this.post, data.protocolPosition));

                        var tempOne = '{{~it.education:value:index}}\
                                   		 <option value="{{=value.code || ""}}">{{=value.value||""}}</option>\
                                    {{~}}';
                        tempOne = doT.template(tempOne)
                        $('.educational select').append(tempOne(response.data));
                    }
                });
            },
            /*  */
            getDictList: function () {
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
                        var temp = '{{~it.rank:value:index}}\
                                   		 <option value="{{=value.code || ""}}">{{=value.value}}</option>\
                                    {{~}}';
                        temp = doT.template(temp)
                        $('select[name=protocolposition]').append(temp(response.data));

                        $('[name=startTime]').each(function (index, elem) {
                            /* 选择日期 */
                            var calendar = new LCalendar();
                            calendar.init({
                                'trigger': elem,
                                'type': 'ym', //date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择范围
                                'initTime': moment(new Date()).format('YYYY/MM'),
                            });
                        });
                        $('[name=endTime]').each(function (index, elem) {
                            /* 选择日期 */
                            var calendar = new LCalendar();
                            calendar.init({
                                'trigger': elem,
                                'type': 'ym', //date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择范围
                                'initTime': moment(new Date()).format('YYYY/MM'),
                            });
                        });
                    }
                });
            },

            getVal: function () {
                var _this = this;
                var obj = {};
                // var provinceCode  =  $('.select-value1').data('value1');
                // var provinceName;
                // $.each(cityData,function(index,value) {
                // 	if(value.value == provinceCode){
                // 		provinceName = value.name;
                // 	}
                // });
                // obj.familyProvince = provinceName;
                // obj.familyCity = $('.city input').val();
                // obj.email = $('.email input').val();
                // obj.wechat = $('.wechat input').val();
                // obj.contactQQ = $('.qq input').val();
                // obj.workIntroduction  = $('.experience textarea').val();
                obj.protocolPosition = $('select[name=protocolposition]').val(); //拟申请职级
                obj.channel = _this.channel;
                obj.createUser = _this.createUser;
                obj.name = $('.name input').val(); //姓名
                obj.sex = $('.sex input:checked').val(); //性别
                obj.cellphone = $('.phone input').val(); //手机
                obj.educational = $('.educational select').val(); //最高学历
                obj.workYears = $('.work-years input').val(); //工作总年限
                obj.hasExperience = $('.has_experience input[name=iswork]:checked').val(); //是否有保险公司工作经验

                obj.insuranceYears = $('.insurance_years select').val(); //保险从业年限
                obj.userNumber = $('.user_number input').val(); //累计签寿险单的客户量（人）
                obj.workNumber = $('.work_number input').val(); //最多同时辅导/管理人力（人

                obj.startTime_1 = $('.ex_time input[name=startTime]').val();
                obj.endTime_1 = $('.ex_time input[name=endTime]').val();
                obj.company_1 = $('.experience .company input').val();
                obj.department_1 = $('.experience .department input').val();
                obj.post_1 = $('.experience .job input').val();

                obj.startTime_2 = $('.rw_time input[name=startTime]').val();
                obj.endTime_2 = $('.rw_time input[name=endTime]').val();
                obj.company_2 = $('.recent_wort .company input').val();
                obj.department_2 = $('.recent_wort .department input').val();
                obj.post_2 = $('.recent_wort .job input').val();
                return obj;
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
                        var res = response.data;
                        var curPost = res.personnel.protocolPosition;
                        /* $('.ipt-text[name=job]').val(common.getPostName(_this.post,curPost));*/
                        _this.getDict(res.personnel);
                        /*if(res.personnel.personnelStatus!="1"){
                        	  _this.obj.off('tap', '#submit');
                        	  $("#submit").addClass("invite-vain")
                        }*/
                    }

                }, function (error) {

                });
            },
            // initProvince:function () {
            //     $('.select-value1').mPicker({
            //         level: 2,
            //         dataJson: dataJson,
            //         Linkage: true,
            //         rows: 6,
            //         idDefault: true,
            //         splitStr: '-',
            //         header: '<div class="mPicker-header">请选择所在省市</div>',
            //         confirm: function (json) {
            //            // console.log(json);
            //            // console.info('当前选中json：', json);
            //             var _this= this;
            //           //  console.info('【json里有带value的情况】');
            //             //更新json
            //           //  console.info('3s后更新json...');
            //             setTimeout(function(){
            //                 //_this.container.data('mPicker').updateData.call(_this,level3);
            //                 var json = getVal2();
            //                 var valArr = json.value;
            //                // console.log(valArr)
            //                // console.info('更新成功!!');
            //                 //console.info('更新后的value为空', valArr[0], valArr[1]);
            //                // console.info('更新后的value拼接值为空', json.result);
            //             },0);

            //            // $('.select-value1').val(json.name.split('-')[1]);
            //         },
            //         cancel: function (json) {
            //             $('.select-value1').val('');
            //         }
            //     })
            //     //获取value
            //     function getVal2(){
            //         var value1 = $('.select-value1').data('value1');
            //         var value2 = $('.select-value1').data('value2');
            //         var result='';
            //         value1 = value1 || '';
            //         value2 = value2 || '';
            //         if(value1){
            //             result= value1;
            //         }
            //         if(value2){
            //             result = result+'-'+ value2;
            //         }
            //         console.log(result)
            //         return {
            //             value:[value1, value2],
            //             result: result
            //         };
            //     }
            // },
            // cityChange:function () {
            //     $('.select-value1').change(function () {
            //     })
            // },

            getStatu: function () {
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
                Interface.getAsynData(params, function (respData) {
                    /**
                     *  data
                     *  1：被删除
                     *  2：超过六十
                     * */
                    if (respData.code == "200") {
                        template = require('./invite-share.html');
                        template = doT.template(template);
                        _this.obj.html(template(''));
                        _this.getUserInfo();
                        // _this.initProvince();
                        // _this.cityChange();
                        $('.ipt-select select').attr('disabled', true);

                        $('[name=startTime]').each(function (index, elem) {
                            /* 选择日期 */
                            var calendar = new LCalendar();
                            calendar.init({
                                'trigger': elem,
                                'type': 'ym', //date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择范围
                                'initTime': moment(new Date()).format('YYYY/MM'),
                            });
                        });
                        $('[name=endTime]').each(function (index, elem) {
                            /* 选择日期 */
                            var calendar = new LCalendar();
                            calendar.init({
                                'trigger': elem,
                                'type': 'ym', //date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择范围
                                'initTime': moment(new Date()).format('YYYY/MM'),
                            });
                        });
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