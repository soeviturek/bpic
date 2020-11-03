define(function (require, exports) {
    var template = require('./sign-check.html');
    template = doT.template(template);
    exports.back = function () {
        return '';
    };

    exports.show = function (opt) {
        var recheck = {
            init: function (obj) {
                this.obj = obj;
                this.infoFlag = true;
                this.personnelId = common.getHash('personnelId') || '';
                this.type = Number(common.getHash('type')) || 0;
                this.post = [];

                // 通过
                this.obj.off('tap', '.sign-check-view .bottom button:first-child');
                this.obj.on('tap', '.sign-check-view .bottom button:first-child', { fun: this}, this.subInterResult);

                // 不通过
                this.obj.off('tap', '.sign-check-view .bottom button:last-child');
                this.obj.on('tap', '.sign-check-view .bottom button:last-child', { fun: this}, this.subInterResult);

                this.obj.off('tap', '.arrow');
                this.obj.on('tap', '.arrow', {fun: this}, this.tabInfo);
                
                this.getDict();

                var _this = this;
                // var timer = setInterval(function () {
                //     var hash = common.getHash('page');
                //     if (hash !== 'signCheck') {
                //         clearInterval(timer);
                //         return;
                //     }
                //     if ($('.system-loading').length <= 0) {
                //         common.loading.open();
                //     }
                //     if (_this.dict && _this.post.length) {
                //         var renderData = {
                //             dict: _this.dict,
                //             post: _this.post
                //         }
                //         var temp = '{{~it.post:value:index}}\
                //             <option value="{{=value.code}}" {{?value.code==it.dict}}selected{{?}}>{{=value.value}}</option>\
                //         {{~}}';
                //         temp = doT.template(temp);
                //         $('select[name=confirmPosition]').append(temp(renderData));
                //         var name = _this.findName(renderData.post);
                //         $('select[name=confirmPosition]').prev('input').val(name);
                //         $('.recheck-post span:nth-child(2)').text(name);
                //         common.loading.close();
                //         clearInterval(timer);
                //     }
                // }, 20);
            },

            tabInfo: function (btn) {
                var _this = btn.data.fun;
                if (_this.infoFlag) {
                    $(this).css('transform', 'rotate(0deg)');

                    $('.content-details').hide();
                    _this.infoFlag = false
                } else {
                    $(this).css('transform', 'rotate(180deg)');

                    $('.content-details').show();
                    _this.infoFlag = true;
                }
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
                    	  var curPost;
                  		  var res = response.data;
                    	 if(res.confirmPosition){
                        	curPost = res.confirmPosition;
                            $(".postType").text("核定");
                            $(".postType").parent('.right').find('.iconfont').addClass('icon-character-success')
                        }else{
                            curPost = res.protocolPosition;
                            $(".postType").text("拟定");
                            $(".postType").parent('.right').find('.iconfont').addClass('icon-renyuanzhunbei')
                        }
                    	response.data.position = common.getPostName(_this.post,curPost)
                    	response.data.position = common.getPostName(_this.post,curPost)
                        _this.obj.html(template(response.data));
                        
                         var renderData = {
                            dict: curPost,
                            post: _this.post
                        }
                         console.log(renderData)
                        var temp = '{{~it.post:value:index}}\
                            <option value="{{=value.code}}" data-mustname="职级" {{?value.code==it.dict}}selected{{?}}>{{=value.value}}</option>\
                         {{~}}';
                        temp = doT.template(temp);
                        $('select[name=confirmPosition]').append(temp(renderData));
                         
                    }
                }, function (error) {

                });
            },

            subInterResult: function (btn) {
                var _this = btn.data.fun;
                var data = {};

                if ($(this).data('result') === 'Y') {
                    data = {
                        comment: $('#comment').val(),
                        currentStatus: "5",
                        personnelId: _this.personnelId,
                        result: "1",
                        confirmPosition: $('select[name=confirmPosition]').val()
                    };
                } else {
                    if(!$.trim($('#comment').val())){
                        common.alertCreate({
                            html: '评语不能为空！'
                        })
                        return false;
                    }
                    data = {
                        comment: $('#comment').val(),
                        currentStatus: "5",
                        personnelId: _this.personnelId,
                        result: "2",
                        confirmPosition: $('select[name=confirmPosition]').val()
                    };
                }

                var param = {
                    url: '/personnel/customer/app-personnel/savePersonnelInfo',
                    type: 'POST',
                    ifOpenLoading: true,
                    data: JSON.stringify(data)
                };

                Interface.getAsynData(param, function (response) {
                    if (response.code == "200") {
                        return window.location.href = '#page=main';
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
                    _this.initPersonInfo()
                });
            },

            findName: function (list) {
                var length = list.length;
                for (var i = 0; i < length; i++) {
                    if (list[i].code === this.dict) {
                        return list[i].value;
                    }
                }
            }
        };

        recheck.init(opt.body);
    }
});