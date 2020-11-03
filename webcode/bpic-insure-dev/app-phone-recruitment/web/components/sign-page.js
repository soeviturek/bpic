define(function (require, exports) {
    require('../../css/sign-page.css');
    require('signJs');
    var template = require('./sign-page.html');
    template = doT.template(template);
    exports.back = function () {
        return '';
    };
    exports.show = function (opt) {
        var recheck = {
            init: function (obj) {
                this.obj = obj;
                this.obj.html(template(''));
                this.type = common.getQueryString('type') || '';
                this.personnelId = common.getHash('personnelId') || '';
                this.shareInfoId = common.getHash('shareInfoId');
                this.reEdit = false;
                var userName = '';
                var contact = '';
                this.title = '';
                this.userInfo = {};
                this.companyInfo = {};
                this.wordShow(this.type);
                this.imgEcho();
                this.tempId1 = '';
                this.tempId2 = '';
                this.tempId3 = '';
                this.tempId4 = '';
                this.getUserInfo();
                this.obj.off('tap', '.sign-img');
                this.obj.on('tap', '.sign-img', {fun: this}, this.signPop);
                this.obj.off('tap', '.sign-btn-save');
                this.obj.on('tap', '.sign-btn-save', {fun: this}, this.saveInfo);
               
            },
            saveInfo: function (btn) {
                var _this = btn.data.fun;
                if (!_this.reEdit && $('.sign-img img').attr('src')) {
                    window.location.href = '#page=signDatum&personnelId=' + _this.personnelId + '&shareInfoId='+_this.shareInfoId;
                    return;
                }
                if (!_this.reEdit && !$('.sign-img img').attr('src')) {
                    common.alertCreate({
                        html: '请先签署您的姓名再保存!',
                        callback: function () {
                        }
                    });
                    return
                }
                var imgSrc = $('.sign-img img').attr('src');
                // console.log(imgSrc,_this.type,_this.personnelId)
                var obj = {
                    width: 400,
                    height: 200,
                    quality: 1
                };

                _this.zipImg(imgSrc, obj, function (imgSrc) {
                    _this.uploadImg(1+_this.type, imgSrc, _this.personnelId);
                   window.location.href = '#page=signDatum&personnelId=' + _this.personnelId + '&shareInfoId='+_this.shareInfoId;
                    
				})

            },
            wordShow: function (t) {
                $('.word-' + t).show()
            },
            signPop: function (e) {
                var _this = e.data.fun;
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
                        if (personnel && (personnel.personnelStatus == "6" || personnel.personnelStatus == "7") && (personnel.checkResult == null || personnel.checkResult == "2")) {
                            $('.sign-popup').show('fast', function () {
                                $('#tools').html('');
                                _this.signPage(_this.type, _this.personnelId);
                            });
                        } else {
                            common.alertCreate({
                                html: "操作已失效"
                            });
                        }
                    }
                });
            },
            imgEcho: function () {
                var _this = this;
                var paramData = {
                    url: '/attachment/file/findAttachment',
                    type: 'get',
                    data: {
                        hostId: _this.personnelId,
                        category: _this.type
                    }
                };
                Interface.getAsynData(paramData, function (response) {
                    if (response.msg === "无此图片") {

                    } else {
                        $('.sign-img p').hide();
                        $('.sign-img img').show().attr('src', response.data[0].path);
                        $('.time span').text(response.data[0].gmtCreate);
                    }

                }, function (error) {

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
                        $('.time span').text(reponse.data[0].gmtCreate);
                    }

                }, function (error) {
                    console.log(error)
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
                        _this.userName = response.data.personnel.name;
                        _this.contact = response.data.personnel.cellphone;
                        if (_this.type == "44") {
                            $(".personnelName").text(response.data.personnel.name)
                        } else if (_this.type == "47") {
                            if (response.data.personnel) {
                                $(".name").text(response.data.personnel.name)
                                $(".sex").text(response.data.personnel.sex == "M" ? "男" : "女")
                                $(".onceName").text(response.data.personnel.onceName)

                                var identityType = response.data.personnel.identityType == "111" ? "身份证" : response.data.personnel.identityType == "414" ? "护照" : response.data.personnel.identityType == "114" ? "军官证" : response.data.personnel.identityType == "516" ? "港澳回乡证" : response.data.personnel.identityType == "616" ? "士兵证" :response.data.personnel.identityType == "717" ? "警官证" :response.data.personnel.identityType == "818" ? "台胞证" :response.data.personnel.identityType == "919" ? "香港身份证" :"其他证件";
                                $(".identityType").text(identityType)
                                $(".identityCode").text(response.data.personnel.identityCode)
                                $(".domicilePlace").text(response.data.personnel.domicilePlace)
                    $(".contactAddress").text(response.data.personnel.familyAddrDetail);
                    $(".postcode").text(response.data.personnel.postcode)
                    $(".telephone").text(response.data.personnel.telephone)
                    $(".cellphone").text(response.data.personnel.cellphone)
                }
            }
            //_this.provinceAndCity(response.data.personnel.companyId);

        }

        }, function (error) {

                    });
                },
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
                        $.publish(pubsubprefix + 'formatchanged');
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
                    // console.log(data);

                    //  _this.uploadImg(type,data,personnelId);
                    _this.reEdit = true;
                    $('.sign-popup').hide();
                    $('.sign-img p').hide();
                    $('.sign-img img').show().attr('src', data)
                    //signSubmit(data);
                }).appendTo($tools);
                $('<div class="ToResetCode"><i class="icon_revoke"></i>撤销</div>').bind('click', function (e) {
                    $sigdiv.jSignature('reset')
                }).appendTo($tools);
                $('<div><textarea style="width:100%;height:7em;display:none" id="createCode"></textarea></div>').appendTo($tools);
                $('<i class="iconfont icon-back sigClose"></i>').bind('tap', function (e) {
                    $sigdiv.jSignature('reset');
                    $('.sign-popup').hide();
                }).appendTo($tools)
            },
            zipImg: function (url, obj, callback) {
                var img = new Image();
                img.src = url;
                img.onload = function () {
                    var that = this;
                    // 默认按比例压缩
                    var w = that.width,
                        h = that.height,
                        scale = w / h;
                    w = obj.width || w;
                    h = obj.height || (w / scale);
                    var quality = 0.7;  // 默认图片质量为0.7
                    //生成canvas
                    var canvas = document.createElement('canvas');
                    var ctx = canvas.getContext('2d');
                    // 创建属性节点
                    var anw = document.createAttribute("width");
                    anw.nodeValue = w;
                    var anh = document.createAttribute("height");
                    anh.nodeValue = h;
                    canvas.setAttributeNode(anw);
                    canvas.setAttributeNode(anh);
                    ctx.drawImage(that, 0, 0, w, h);
                    // 图像质量
                    if (obj.quality && obj.quality <= 1 && obj.quality > 0) {
                        quality = obj.quality;
                    }
                    // quality值越小，所绘制出的图像越模糊
                    var base64 = canvas.toDataURL('image/png', quality);

                    callback(base64)

                }
            },
            provinceAndCity:function (companyId) {
                var _this = this;
                var postData = {
                    code:companyId
                }
                var params = {
                    url: '/system/organization/code/exclusions/findByCode',
                    data:postData,
                    type: 'POST',
                    contentType:"application/x-www-form-urlencoded",
                    ifOpenLoading: true
                };
                Interface.getAsynData(params, function (response) {
                    if (response.code == "200") {
                         var province = response.data.province;
                         var city = response.data.city;
                        province = province.substring(0, province.length-1);
                        city = city.substring(0, city.length-1);
                        response.data.province = province;
                        response.data.city = city;
                        _this.companyInfo = response.data;
                        $(".province").text(province);
                        $(".city").text(city);
                    }
                },function (error) {

                });
            }

        };
        recheck.init(opt.body);
    }
});