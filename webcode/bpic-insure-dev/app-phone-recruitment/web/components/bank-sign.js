define(function (require, exports) {
    var template = require('./bank-sign.html');
    require('../../css/bank-sign.css');
    // template = doT.template(template);
    exports.back = function () {
        return '';
    };

    exports.show = function (opt) {
        var recheck = {
            init: function (obj) {
                this.obj = obj;
                this.obj.html(template);
               this.personnelId = common.getHash('personnelId')||'';
                this.justUseAgent();
                if (this.personnelId) {
                    this.initBascInfo();
                   this.imgEcho("42", ".bank");
                } else {
                    common.alertCreate({
                        html: "无效链接"
                    });
                }
                this.scroll = null;
                this.pageNo=1;
                this.pageSize=5;
                this.checkMore = true;//判断是否还有数据
                this.isFirstComing = true;//第一次初始化滚动
                this.obj.off('tap');
                this.obj.on('tap', {fun: this}, this.hideBankSearch);
                this.obj.off('tap', '.icon-share');
                this.obj.on('tap', '.icon-share', {fun: this}, this.sendData);
                //动态搜索
                // this.obj.off('keyup', '.bank-branch');
                // this.obj.on('keyup', '.bank-branch', {fun: this}, this.searchBank);
                this.obj.off('input propertychange', '.bank-branch');
                this.obj.on('input propertychange', '.bank-branch', {fun: this}, this.searchBank);
                // this.obj.off('focus', '.bank-branch');
                // this.obj.on('focus', '.bank-branch', {fun: this}, this.checkBankName);
                //上传银行卡
                this.obj.off('tap', '.fileImg');
                this.obj.on('tap', '.fileImg', {fun: this}, this.uploadImg);
                //点击确定
                this.obj.off('tap', '.submit-bank');
                this.obj.on('tap', '.submit-bank', {fun: this}, this.submitBank);
                //点击清空
                this.obj.off('tap', '.reset-bank');
                this.obj.on('tap', '.reset-bank', {fun: this}, this.resetBank);
                //点击给银行卡赋值
                this.obj.off('tap','.bank-con li');
                this.obj.on('tap','.bank-con li', {fun: this}, this.setBankName);
                //点击给银行卡赋值
                // this.obj.off('change','.bank-con li');
                // this.obj.on('change','.bank-con li', {fun: this}, this.setBankName);
                this.obj.off('blur', '.bank_sign .bank-branch');
                this.obj.on('blur', '.bank_sign .bank-branch', {fun: this}, this.clearBank);
                this.initScroll();
            },

            resetBank:function (e) {
                var _this=e.data.fun||e.fun;
                if($('.bank-branch').val()==''&&$('.bank-cardno').val()==''&&$('.card-on img').attr('src')==''&& $('.bank-select select').val()==''){
                    common.alertCreate({
                        html: '该页面数据为空!', 
                    })
                }else{
                    common.confirmCreate({
                        html: '是否清空银行卡信息？',
                        callback: function (status) {
                                _this.reSet();
                        }
                    });
                }
            },
            reSet:function(){
                var _this=this;
                
                // return;
                var parma = {
                    url: '/personnel/customer/app-personnel/exclusions/clearBankInfo',
                    type: 'POST',
                    ifOpenLoading: true,
                    data:JSON.stringify({id: _this.personnelId,}),
                };
                Interface.getAsynData(parma, function (response) {
                    if (response.code == "200") {
                        _this.initBascInfo();
                        _this.imgEcho("42", ".bank");
                        common.alertCreate({
                            html: '清除成功!', 
                        })
                    }

                }, function (error) {
                    console.log(error)
                });
            },
            searchBank: function (btn) {
                if(btn){
                    var _this=btn.data.fun;
                }else {
                    var _this=this;
                }
                $('.bank-branch').attr('tittle','');
                var bankCode=$('.bank-select select').val();
                $('.select-bank').val($('.bank-branch').val());
                if(!bankCode){
                    common.alertCreate({
                        html: '请选择银行卡',
                        callback: function() {}
                    });
                    return
                }
                setTimeout(function () {
                    // $('.bank-con').empty();
                    var bankName=$('.bank-branch').val();
                    var data={
                        cnName:bankName,
                        codes: bankCode,
                        pageNo:_this.pageNo,
                        pageSize:_this.pageSize,
                        pageSortFiled:'sort',
                        pageSortType:'asc'
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
                            console.log(response);
                            if(btn){
                                // _this.pageNo=1;
                                $('.bank-con li:not(:nth-of-type(1))').remove();
                            }
                            if(response.data[bankCode]){
                                var html='';
                                $('.bank-content').show();
                                response.data[bankCode].forEach(function (item,index) {
                                    html+='<li tittle='+item.value+'>'+item.code+'</li>'
                                });
                                $('.bank-con').append(html);
                            }
                            // if (_this.isFirstComing) {
                            //     _this.isFirstComing = false;
                            // } else {
                            //     _this.scroll && _this.scroll.refresh();
                            // }
                        }
                    });
                },300)
            },
            setBankName:function () {
                var val=$(this).text();
                var code=$(this).attr('tittle')
               /* console.log(val.length);
                if(val.length>=15)
                    val=val.substr(10,val.length);*/
                $('.bank-branch').val(val);
                $('.bank-branch').attr('tittle',code);
                $('.select-bank').val(val);
                $('.select-bank').attr('tittle',code);
                $('.bank-box li').remove();
                setTimeout(function () {
                    $('.bank-content').hide();
                },350)
            },
            clearBank:function(){
                $('.bank-branch').val('');
            },
            uploadImg: function (btn) {
                // $('.fileImg').off('tap');
                var _this = btn.data.fun;
                var _obj = $(this);
                var textfield = _obj[0];
                var cardType = _obj.attr("card-type");
                var personnelData = {
                    id: _this.personnelId
                };
                // textfield.removeEventListener('change', readFile, false);
                // textfield.addEventListener('change', readFile, false);
                $(textfield).off('change');
                $(textfield).on('change', readFile);

                function readFile() {
                    var file = this.files[0];
                    var reader = new FileReader();
                    reader.readAsDataURL(file);
                    reader.onload = function () {
                        _this.result = this.result;
                        _this.canvasDataURL(_obj, _this.personnelId, cardType, _this.result, {
                            width: 160,
                            height: 117,
                            quality: 1
                        })
                    }
                }

            },
            canvasDataURL: function (_class,personnelId, cardType, path, obj, callback) {//压缩图片
                var img = new Image();
                img.src = path;
                img.onload = function () {
                    var that = this;
                    // 默认按比例压缩
                    var w = that.width,
                        h = that.height,
                        scale = w / h;
                    w = obj.width || w;
                    h = obj.height || (w / scale);
                    var quality = 1;  // 默认图片质量为0.7
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
                    console.log(base64);

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
                    Interface.getAsynData(paramData, function (response) {
                        if (response.code == "200") {
                            console.log(response);
                             $('.bank img').attr('src',response.data[0].path).show();
                             $('.add').hide();
                        }

                    }, function (error) {
                        console.log(error)
                    });


                    // 回调函数返回base64的值
                    //callback(base64);
                }
            },
            submitBank:function (btn) {
                var _this=btn.data.fun;
                var bankBrach=$('.bank-branch').val();
                var code=$('.bank-branch').attr('tittle');
                var bankCode=$('.bank-cardno').val();
                var bankPic=$('.card-on img').attr('src');
                var bankName=$('.bank-select select').val();
                if(!bankName){
                    common.alertCreate({
                        html: "请选择开户银行卡"
                    });
                    return
                }
                if(!bankBrach||!code){
                    common.alertCreate({
                        html: "请选择银行卡支行"
                    });
                    return
                }
                if(!bankCode){
                    common.alertCreate({
                        html: "请输入银行卡卡号"
                    });
                    return
                }
                if(!bankPic){
                    common.alertCreate({
                        html: "请上传银行卡影像"
                    });
                    return
                }

                var data = {
                    id: _this.personnelId,
                    bankBranch:bankBrach,
                    chinaCiticBank:bankCode,
                    bankName:bankName
                };
                var parma = {
                    url: '/personnel/customer/app-personnel/exclusions/updatePersonnel',
                    type: 'POST',
                    ifOpenLoading: true,
                    data: JSON.stringify(data)
                };
                Interface.getAsynData(parma, function (response) {
                    if (response.code == "200") {
                        console.log(response);
                        return window.location.replace('#page=personnelFile&personnelId='+_this.personnelId);
                    }

                }, function (error) {
                    console.log(error)
                });

            },
            hideBankSearch:function (btn) {
                var target=btn.target;
                var _this=btn.data.fun;
                _this.pageNo=1;
                // console.log(target);
                if($(target).hasClass('select-bank')||$(target).hasClass('bank-con')){
                    $('.bank-content').show();
                    $('.bank-branch').focus();
                }else {
                    $('.bank-content').hide();
                }
            },
            initBascInfo: function () {
                var _this = this;
                $(".icon-back").css("display","none");
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
                        var inFo=response.data.personnel;
                        _this.getBankName(inFo);
                        if(inFo){
                            $('.bank-branch').val(inFo.bankBranch);
                            $('.bank-branch').attr('tittle',inFo.bankBranch);
                            $('.bank-cardno').val(inFo.chinaCiticBank);
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
                        category: imgType
                    }
                };
                Interface.getAsynData(paramData, function (response) {
                    if (response.msg === "无此图片") {
                        $(_class).find('img').hide().attr('src', '');
                        $(_class).find('.add').show();
                    } else {
                        $(_class).find('img').show().attr('src', response.data[0]);
                        $(_class).find('.add').hide();
                    }

                }, function (error) {

                });
            },
            justUseAgent:function () {
                function getIos() {
                    var ua=navigator.userAgent.toLowerCase();
                    if (ua.match(/iPhone\sOS/i) == "iphone os") {
                        return true;
                    } else {
                        return false;
                    }
                }
                if (getIos()) {
                    $('.fileImg')[0].removeAttribute('capture');
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
                            $('.bank-select select').val(res.bankName);
                            // $('.bank-select select').val(res.data.personnel.bankName)
                        }
                    };
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
                    console.log(_this.scroll.y)
                    if ((_this.scroll.y <= (_this.scroll.maxScrollY + 50)) ) {
                        _this.pageNo++;
                        $('.bank-con li:not(:nth-of-type(1))').remove();
                        _this.searchBank();
                    }


                })
            },
        };

        recheck.init(opt.body);
    }
});