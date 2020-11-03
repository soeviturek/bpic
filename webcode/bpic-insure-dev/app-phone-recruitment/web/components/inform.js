define(function (require, exports) {
    require('../../css/card-info.css');
    require('signJs');
    var template = ''; 
    exports.back = function () {
        return '';
    };

    exports.show = function (opt) {
        var recheck = {
            init: function (obj) {
                this.obj = obj;
                this.personnelId = common.getHash('personnelId') || '';
                this.shareInfoId = common.getHash('shareInfoId');

                if (this.personnelId) {
                    this.getStatu();
                } else {
                    common.alertCreate({
                        html: "无效链接"
                    });
                }
                this.obj.off('tap', '.signImg');
                this.obj.on('tap', '.signImg', {fun: this}, this.signPop);

                // 下一步
                this.obj.off('tap', '.next-page');
                this.obj.on('tap', '.next-page', {fun: this}, this.bascInfoGoNext);

                // 上一步
                this.obj.off('tap', '.prev-page');
                this.obj.on('tap', '.prev-page', {fun: this}, this.bascInfoGoPrev);
            },
            initBascInfo: function () {
                var _this = this;
                var postData = {
                    id: _this.personnelId
                };
                var parma = {
                    url: '/personnel/customer/app-personnel/exclusions/getInform',
                    type: 'POST',
                    ifOpenLoading: true,
                    data: JSON.stringify(postData)
                };
                Interface.getAsynData(parma, function (response) {
                    if (response.code == "200") {
                        if (response.data) {
                            _this.obj.html(template(response.data));
                            _this.imgEcho("48", ".signImg");
                        }
                    }
                });
            },
            bascInfoGoNext: function (btn) {
                var _this = btn.data.fun;
                var flag = true;
                var idx;
                $(".inform-paper").each(function (index, elem) {
                    if ($(elem).find('input[type=radio]:checked').size() == 0) {
                        idx = index + 1;
                        flag = false;
                    }
                })
                if (flag == false) {
                    common.alertCreate({
                        html: '请回答' + (idx) + '题！'
                    })
                    return false;
                }
                if (!$('.signImg').find('img').attr('src')) {
                    common.alertCreate({
                        html: '请完成签字！'
                    });
                    return false;
                }


                var personnelInform = {
                    personnelId: _this.personnelId,
                    informDetail: $("#informDetail").val()
                }
                var personnelInformInfoList = [];
                $(".inform-paper").each(function () {
                    var obj = {};
                    obj["personnelId"] = _this.personnelId;
                    obj["informId"] = $(this).attr("id");
                    obj["informOption"] = $(this).find("input[type=radio]:checked").val();
                    personnelInformInfoList.push(obj);
                })
                var data = {
                    personnelInform: personnelInform,
                    personnelInformInfoList: personnelInformInfoList
                };

                var parma = {
                    url: '/personnel/customer/app-personnel/exclusions/updateInform',
                    type: 'POST',
                    ifOpenLoading: true,
                    data: JSON.stringify(data)
                };

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
                        var personnel = response.data.personnel;
                        if (personnel && (personnel.personnelStatus=="6"||personnel.personnelStatus=="7")&&(personnel.checkResult==null||personnel.checkResult=="2")) {
                            Interface.getAsynData(parma, function (response) {
                                if (response.code == "200") {
                                    return window.location.href = '#page=cardInfo&personnelId=' + _this.personnelId + '&shareInfoId='+_this.shareInfoId;
                                } else {
                                    common.alertCreate({
                                        html: response.msg
                                    });
                                }
                            });

                        } else {
                            return window.location.href = '#page=cardInfo&personnelId=' + _this.personnelId + '&shareInfoId='+_this.shareInfoId;
                        }
                    }
                });
            },
            bascInfoGoPrev: function (btn) {
                var _this = btn.data.fun;
                var personnelInform = {
                    personnelId: _this.personnelId,
                    informDetail: $("#informDetail").val()
                }
                var personnelInformInfoList = [];
                $(".inform-paper").each(function () {
                    var obj = {};
                    obj["personnelId"] = _this.personnelId;
                    obj["informId"] = $(this).attr("id");
                    obj["informOption"] = $(this).find("input[type=radio]:checked").val();
                    personnelInformInfoList.push(obj);
                })
                var data = {
                    personnelInform: personnelInform,
                    personnelInformInfoList: personnelInformInfoList
                };
                var parma = {
                    url: '/personnel/customer/app-personnel/exclusions/updateInform',
                    type: 'POST',
                    ifOpenLoading: true,
                    data: JSON.stringify(data)
                };
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
                        var personnel = response.data.personnel;
                        if (personnel && (personnel.personnelStatus=="6"||personnel.personnelStatus=="7") && (personnel.checkResult == null || personnel.checkResult == "2")) {
                            Interface.getAsynData(parma, function (response) {
                                if (response.code == "200") {
                                    return window.location.href = '#page=baseInfo&personnelId=' + _this.personnelId + '&shareInfoId='+_this.shareInfoId;
                                } else {
                                    common.alertCreate({
                                        html: response.msg
                                    });
                                }
                            });

                        } else {
                            return window.location.href = '#page=baseInfo&personnelId=' + _this.personnelId + '&shareInfoId='+_this.shareInfoId;
                        }
                    }
                });
            },
            imgEcho: function (imgType, _class) {
                var _this = this;
                var paramData = {
                    url: '/attachment/file/findAttachment',
                    type: 'get',
                    data: {
                        hostId: _this.personnelId,
                        category: imgType
                    }
                };
                Interface.getAsynData(paramData, function (response) {
                    if (response.msg === "无此图片") {

                    } else {
                        $(_class).find('p').hide();
                        $(".signImg").find('img').show().attr('src', response.data[0].path);
                        $(".time span").text(response.data[0].gmtCreate)
                    }

                }, function (error) {

                });
            },
            signPop: function (e) {
                var _this = e.data.fun;
                var personnelData = {
                    id: _this.personnelId
                };
                var personnelParam = {
                    url: '/personnel/customer/app-personnel/exclusions/getPersonnel',
                    type: 'POST',
                    ifOpenLoading: true,
                    data: JSON.stringify(personnelData)
                };

                Interface.getAsynData(personnelParam, function (response) {
                    if (response.code == "200") {
                        var personnel = response.data.personnel;
                        if (personnel &&(personnel.personnelStatus=="6"||personnel.personnelStatus=="7")&& (personnel.checkResult == null || personnel.checkResult == "2")) {
                            $('.sign-popup').show('fast', function () {
                                $('#tools').html('');
                                _this.signPage("48", _this.personnelId);
                            });

                        } else {
                            common.alertCreate({
                                html: "操作已失效"
                            });
                        }
                    }
                });
            },
            uploadImg: function (type, imgStr, personnelId) {
                var postData = {
                    keyword: '1111',
                    hostId: personnelId,
                    category: type,
                    base64StrImg: imgStr
                };
                var paramData = {
                    ifOpenLoading: true,
                    type: 'POST',
                    url: '/attachment/uploadImage',
                    data: JSON.stringify(postData)
                };
                Interface.getAsynData(paramData, function (reponse) {
                    if (reponse.code == "1") {
                        //_obj.parent('.card-on').find('p,i').hide();
                        //_obj.parent('.card-on').find('img').show().attr('src',reponse.data[0].path);
                        $(".time span").text(reponse.data[0].gmtCreate)
                    }

                }, function (error) {
                    console.log(error)
                });
            }
            ,
            signPage: function (type, personnelId) {
                var _this = this;
                var topics = {};
                $.publish = function (topic, args) {
                    if (topics[topic]) {
                        var currentTopic = topics[topic],
                            args = args || {};
                        for (var i = 0, j = currentTopic.length; i < j; i++) {
                            currentTopic[i].call($, args);
                        }
                    }
                };
                $.subscribe = function (topic, callback) {
                    if (!topics[topic]) {
                        topics[topic] = [];
                    }
                    topics[topic].push(callback);
                    return {
                        "topic": topic,
                        "callback": callback
                    };
                };
                $.unsubscribe = function (handle) {
                    var topic = handle.topic;
                    if (topics[topic]) {
                        var currentTopic = topics[topic];

                        for (var i = 0, j = currentTopic.length; i < j; i++) {
                            if (currentTopic[i] === handle.callback) {
                                currentTopic.splice(i, 1);
                            }
                        }
                    }
                };
                var sigdiv = $("#signature");
                //	debugger;
                var _sigH = $(window).height() - 55;

                sigdiv.jSignature('init', {
                    height: _sigH,
                    width: '100%'
                });
                // This is the part where jSignature is initialized.
                var $sigdiv = $("#signature").jSignature({
                        'UndoButton': true
                    }),

                    // All the code below is just code driving the demo. 
                    $tools = $('#tools'),
                    pubsubprefix = 'jSignature.demo.'

                var export_plugins = $sigdiv.jSignature('listPlugins', 'export'),
                    chops = ['<div id="select">', ''],
                    name;

                name = export_plugins[0]
                chops.push('<div class="toCreatCode"><i class="icon_confirm"></i>确认</div>')
                chops.push('</div>')

                $(chops.join('')).on('tap', function (e) {

                    var options = document.getElementById('select').children;
                    if (e.target.value !== '') {
                        var data = $sigdiv.jSignature('getData', e.target.value);
                        // if(data.match(/^.*\=$/)){
                        //     common.Prompt('签名不能为空');
                        //     return
                        // };            
                        $.publish(pubsubprefix + 'formatchanged')
                        if (typeof data === 'string') {
                            $('textarea', $tools).val(data)
                        } else if ($.isArray(data) && data.length === 2) {
                            $('textarea', $tools).val(data.join(','))
                            $.publish(pubsubprefix + data[0], data);
                        } else {
                            try {
                                $('textarea', $tools).val(JSON.stringify(data))
                            } catch (ex) {
                                $('textarea', $tools).val('Not sure how to stringify this, likely binary, format.')
                            }
                        }
                    }
                    if ($sigdiv.jSignature('getData', 'native').length == '0') {
                        common.Prompt('签名不能为空');
                        return
                    }

                    _this.uploadImg(type, data, personnelId)

                    $('.sign-popup').hide();
                    $('.signImg p').hide();
                    $('.signImg img').show().attr('src', data)

                    //signSubmit(data);
                }).appendTo($tools)
                $('<div class="ToResetCode"><i class="icon_revoke"></i>撤销</div>').bind('click', function (e) {
                    $sigdiv.jSignature('reset')
                }).appendTo($tools)
                $('<div><textarea style="width:100%;height:7em;display:none" id="createCode"></textarea></div>').appendTo($tools);
                $('<i class="iconfont icon-back sigClose"></i>').bind('tap', function (e) {
                    $sigdiv.jSignature('reset');
                    $('.sign-popup').hide();
                }).appendTo($tools)
            },
            getPersonnelBaseInfo: function () {
                var _this = this;
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
                        if( response.data.personnel.isClose){
                            $('.click-button').hide()
                        }
                    }
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
                        template = require('./inform.html');
    					template = doT.template(template);
    					_this.obj.html(template(''));
                        _this.initBascInfo();
                        _this.getPersonnelBaseInfo()
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