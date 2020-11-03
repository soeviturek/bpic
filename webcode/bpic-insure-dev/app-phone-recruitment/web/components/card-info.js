define(function (require, exports) {
    require('../../css/card-info.css');
    require('conversion')
    var template = '';
    exports.back = function () {
        return '';
    };
    exports.show = function (opt) {
        var cardPage = {
            init: function (obj) {
                this.obj = obj;

                this.result = '';


                this.personnelId = common.getHash('personnelId') || '';
                this.shareInfoId = common.getHash('shareInfoId');
                if (this.personnelId) {
                    this.getStatu();
                } else {
                    this.obj.html(template({}));
                }


                this.obj.off('tap', '.fileImg');
                this.obj.on('tap', '.fileImg', {fun: this}, this.uploadImg);

                this.obj.off('change', '#check-honver');
                this.obj.on('change', '#check-honver', {fun: this}, this.honver);

                // 下一步
                this.obj.off('tap', '.next-page');
                this.obj.on('tap', '.next-page', {fun: this}, this.bascInfoGoNext);

                // 上一步
                this.obj.off('tap', '.prev-page');
                this.obj.on('tap', '.prev-page', {fun: this}, this.bascInfoGoPrev);

            },
            initBascInfo: function () {
                var _this = this;
                $(".icon-back").css("display", "block");
                _this.imgEcho("1", ".up-card");
                _this.imgEcho("2", ".back-card");
                _this.imgEcho("4", ".school-card");
                _this.imgEcho("42", ".bank-card");
                _this.imgEcho("43", ".ry-card");
                _this.imgEcho("49", ".user-card");

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
                        if (response.data && response.data.personnel.isMdrt == "1") {
                            $('.card-honver').removeClass("card-mdrt-hidden");
                            $("#check-honver").attr("checked", "checked")
                        }

                        if (response.data && response.data.personnel.identityType != "111") {
                            $('.back-card').find('.card-on').removeClass('must-up');
                            $('.back-card').hide();
                            $('.up_tip').text('请上传证件照')
                        }
                        if( response.data.personnel.isClose){
                            $('.click-button').hide()
                        }
                    }
                });
            },
            imgEcho: function (imgType, _class) {
                var _this = this;
                var paramData = {
                    url: '/attachment/file/find',
                    type: 'get',
                    data: {
                        hostId: _this.personnelId,
                        // hostId: 1020204801218052096,
                        category: imgType
                        // category: 144
                    }
                };
                Interface.getAsynData(paramData, function (response) {
                    if (response.msg === "无此图片") {

                    } else {
                        /*if (imgType == '43') {
                            $('#check-honver').prop('checked', true);
                            $('.card-honver').removeClass("card-mdrt-hidden")
                        }*/

                        $(_class).find('.card-on p,i').hide();
                        $(_class).find('img').show().attr('src', response.data[0]);
                    }

                }, function (error) {

                });
            },
            honver: function () {
                var $this = $("#check-honver");
                console.log($this.is(':checked'));
                if ($this.is(':checked')) {
                    $('.card-honver').removeClass("card-mdrt-hidden");
                } else {
                    $('.card-honver').addClass("card-mdrt-hidden");
                }
            },
            uploadImg: function (btn) {

                var _this = btn.data.fun;
                var _obj = $(this);
                var textfield = _obj[0];
                var cardType = _obj.attr("card-type");
                var personnelData = {
                    id: _this.personnelId
                }

                // textfield.addEventListener('change', readFile, false);
                $(textfield).off('change');
                $(textfield).on('change',readFile);

                function readFile() {
                    var file = this.files[0];
                    if(cardType=="49"){
                        if(file.type!='image/jpeg') {
                            return common.Prompt('请上传jpg格式图片');
                        }
                        _this.upDataURL(_obj, _this.personnelId, cardType,file)
                    }else{
                        var reader = new FileReader();
                        reader.readAsDataURL(file);
                        reader.onload = function () {
                            _this.result = this.result;
                            _this.canvasDataURL(_obj, _this.personnelId, cardType, _this.result, {
                                width: 160,
                                height: 117,
                                quality: 0.7
                            })
                        }
                    }
                }
            },
            bascInfoGoNext: function (btn) {
                var _this = btn.data.fun;
                var isMdrt = "0";
                var upImg = true;
                $('.must-up').each(function (index, elem) {
                    console.log(elem);
                    var card=$(elem).attr('card-type');
                    if (!$(elem).find('img').attr('src')&&!card) {
                        common.alertCreate({
                            html: '请上传完成所有的图片！'
                        });
                        upImg = false;
                        return false;
                    }
                });

                if (!upImg) {
                    return false;
                }


                if ($("#check-honver").is(':checked')) {
                    if (!$('.ry-card').find('img').attr('src')) {
                        common.alertCreate({
                            html: '请上传荣誉资格证书！'
                        });
                        return false;
                    }
                    isMdrt = "1"
                }
                var data = {
                    isMdrt: isMdrt,
                    id: _this.personnelId
                };

                var parma = {
                    url: '/personnel/customer/app-personnel/exclusions/updatePersonnel',
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
                        if (personnel && (personnel.personnelStatus == "6" || personnel.personnelStatus == "7") && (personnel.checkResult == null || personnel.checkResult == "2")) {
                            Interface.getAsynData(parma, function (response) {
                                if (response.code == "200") {
                                    return window.location.href = '#page=signDatum&personnelId=' + _this.personnelId + '&shareInfoId='+_this.shareInfoId;
                                } else {
                                    common.alertCreate({
                                        html: response.msg
                                    });
                                }
                            });
                        } else {
                            return window.location.href = '#page=signDatum&personnelId=' + _this.personnelId + '&shareInfoId='+_this.shareInfoId;
                        }
                    }
                });

            },
            bascInfoGoPrev: function (btn) {
                var _this = btn.data.fun;
                var isMdrt = "0";
                if ($("#check-honver").is(':checked')) {
                    isMdrt = "1"
                }
                var data = {
                    isMdrt: isMdrt,
                    id: _this.personnelId
                };
                var parma = {
                    url: '/personnel/customer/app-personnel/exclusions/updatePersonnel',
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
                        if (personnel && (personnel.personnelStatus == "6" || personnel.personnelStatus == "7") && (personnel.checkResult == null || personnel.checkResult == "2")) {
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
            updatePersonnel: function (personnelId) {

            },
            imgSubmit(_class, personnelId, cardType, base64){
                var postData = {
                    keyword: '1111',
                    hostId: personnelId,
                    category: cardType,
                    base64StrImg: base64
                };
                var paramData = {
                    ifOpenLoading: true,
                    type: 'POST',
                    url: '/attachment/uploadImage',
                    data: JSON.stringify(postData)
                };

                var personnelData = {
                    id: personnelId
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
                            Interface.getAsynData(paramData, function (reponse) {
                                if (reponse.code == "200") {
                                    _class.parent('.card-on').find('p,i').hide();
                                    _class.parent('.card-on').find('img').show().attr('src', reponse.data[0].path);
                                }

                            }, function (error) {
                                console.log(error)
                            });
                        } else {
                            common.alertCreate({
                                html: "操作已失效"
                            });
                        }
                    }
                });
            },
            upDataURL: function (_class, personnelId, cardType, path, obj, callback) {//压缩图片
                var _this=this;
                    var that = this;
                    var file=path;
                    async function filetoImage(file) {
                        var dataURL = await imageConversion.filetoDataURL(file);
                        return dataURL;
                      }
                    async function compress(file) {
                        var compress_file = await imageConversion.compressAccurately(file, {
                          size:180,
                          accuracy:0.9,
                        });
                        var compress_image = await filetoImage(compress_file);
                        return compress_image;
                      }
                     compress(file).then((base64)=>{
                        _this.imgSubmit(_class, personnelId, cardType, base64)
                     });
                    
                    // 回调函数返回base64的值
                    //callback(base64);
            },
            canvasDataURL: function (_class, personnelId, cardType, path, obj, callback) {//压缩图片
                var _this=this;
                var img = new Image();
                img.src = path;
                img.onload = function () {
                    var that = this;
                    // 默认按比例压缩
                    var w = that.width,
                        h = that.height,
                        scale = w / h;
                    // w = obj.width || w;
                    // h = obj.height || (w / scale);
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
                    var base64 = canvas.toDataURL('image/jpeg', quality);
                //     // console.log(base64);
                _this.imgSubmit(_class, personnelId, cardType, base64)
                    // 回调函数返回base64的值
                    //callback(base64);
                }
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
                        template = require('./card-info.html');
    					template = doT.template(template);
                        _this.initBascInfo();
                        _this.obj.html(template(''));
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
        cardPage.init(opt.body);
    }
});