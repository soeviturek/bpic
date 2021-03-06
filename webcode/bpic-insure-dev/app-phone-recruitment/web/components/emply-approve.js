define(function (require, exports) {
    require('datepicker');
    require('moment');
    require('../../lib/mobileSelect/mpicker.css');
    var template = require('./emply-approve.html');
    template = doT.template(template);
    exports.back = function () {
        return '';
    };
    exports.show = function (opt) {
        var emplyProcess = {
            init: function (obj) {
                this.obj = obj;
                this.obj.html(template(''));
                // this.type = common.getHash('type') || '1';
                this.type = '1';
                this.scroll = null;
                this.pageNo = 1;
                this.pageSize = 6;
                this.ifOpenLoading = true;
                this.isOnceLoading = true;
                this.checkMore = true;
                this.isFirstComing = true;
                this.scrollDisct = 0;
                this.dropDown = false;
                this.name = '';
                this.status = [];
                this.position = [];
                this.post = [];
                this.agentCode = common.getHash("token")|| "";
                this.platform=common.getLocalStorage('platform', true)||'';

                this.startTime='';
                this.endTime='';
                if(this.type){
                    $('.tabs li').removeClass('active').eq(this.type-1).addClass('active')
                }

                this.initScroll();
                // this.getTabList(this.type);
                // this.getPost();
                if(this.agentCode){
                    this.approveLogin()
                }else{
                    this.getTabList(this.type);
                    this.getPost();
                }
                // 筛选点击
                this.obj.off('tap', '.process-select');
                this.obj.on('tap', '.process-select', {fun: this}, this.popupFilter);

                // 确定筛选
                this.obj.off('tap', '.filter-confirm');
                this.obj.on('tap', '.filter-confirm', {fun: this}, this.confirmFilter);

                /*重置筛选*/
                this.obj.off('tap','.filter-reset');
                this.obj.on('tap','.filter-reset', {fun: this}, this.resetFilter);
                 // 时间控件
                 this.initTime({
                    fun: this
                })
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
                // 选择状态
                this.obj.off('tap', '.status-item');
                this.obj.on('tap', '.status-item', {fun: this}, this.toggleStatus);

                // 选择职位
                this.obj.off('tap', '.post-item');
                this.obj.on('tap', '.post-item', {fun: this}, this.toggleStatus);

                // 搜索
                this.obj.off('tap', '.search-input i');
                this.obj.on('tap', '.search-input i', {fun: this}, this.searchData);

                // 跳转详情
                this.obj.off('tap', '.select-item');
                this.obj.on('tap', '.select-item', {fun: this}, this.goToInterview);

                // tabs切换
                this.obj.off('tap', '.tabs li');
                this.obj.on('tap', '.tabs li', {fun: this}, this.tabAnimation);
                var height=$('.select-box').height();
                // $('.slider-box').css('height',(height+1)+'px');
            },
            approveLogin: function () {
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
                    url: '/personnel/JTERLogin',
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
                        _this.getTabList(_this.type);
                        _this.getPost();
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
            tabAnimation: function(btn){
                var _this = btn.data.fun;
                _this.type = $(this).data('type');
                _this.ifOpenLoading = true;
                _this.checkMore = true;
                _this.pageNo = 1;
                $(this).addClass('active').siblings().removeClass('active');
                $('.select-box .se-con').html('');
                //return window.location.href = "#page=emplyApprove&type=" + _this.type;
                _this.getTabList(_this.type);

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
            getPost: function () {
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
                        $('[name=startTime]').each(function (index, elem) {
                            /* 选择日期 */
                            var calendar = new LCalendar();
                            calendar.init({
                                'trigger': elem,
                                'type': 'date', //date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择范围
                                'initTime': moment(new Date()).format('YYYY/MM'),
                            });
                        });
                        $('[name=endTime]').each(function (index, elem) {
                            /* 选择日期 */
                            var calendar = new LCalendar();
                            calendar.init({
                                'trigger': elem,
                                'type': 'date', //date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择范围
                                'initTime': moment(new Date()).format('YYYY/MM'),
                            });
                        });
                    }
                });
            },

            resetFilter: function(btn){
                var _this = btn.data.fun;
                $('.checkbox-wrapper .checkbox-item').removeClass('checkbox-active');
                $('[name=name]').val('');
                _this.status = [];
                _this.position = [];
            },

            goToInterview: function (btn) {
                var flowId = $(this).data('flowid');
                var _this = btn.data.fun;
                if(_this.type == 1){
                    if (flowId === 2) {
                        return window.location.href = '#page=fircheck&personnelId=' + $(this).data('id');
                    } else if (flowId === 3) {
                        return window.location.href = '#page=recheck&personnelId=' + $(this).data('id');
                    } else if (flowId === 4) {
                        return window.location.href = '#page=recheck&personnelId=' + $(this).data('id') + '&type=1';
                    } else if (flowId === 5) {
                        return window.location.href = '#page=signCheck&personnelId=' + $(this).data('id');
                    }
                }else if(_this.type == 2){
                    return window.location.href = '#page=rescrite&hrefType=1&personnelId=' + $(this).data('id');
                }
            },

            searchData: function (btn) {
                var _this = btn.data.fun;
                $('.se-con').html('');
                _this.ifOpenLoading = true;
                _this.checkMore = true;
                _this.pageNo = 1;
                _this.getTabList(_this.type);
            },

            toggleStatus: function () {
                $(this).toggleClass('checkbox-active');
            },

            setFilterData: function () {
                var _this = this;
                _this.status = [];
                _this.position = [];
                this.name = $('input[name=name]').val();
                this.phone = $('input[name=phone]').val();
                $('.status-item').each(function () {
                    if ($(this).hasClass('checkbox-active')) {
                        _this.status.push($(this).text());
                    }
                });

                $('.post-item').each(function () {
                    if ($(this).hasClass('checkbox-active')) {
                        _this.position.push($(this).attr("data-type"));
                    }
                });
                _this.startTime=$('.filter-item-content [name=startTime]').val();
                _this.endTime=$('.filter-item-content [name=endTime]').val();
            },

            confirmFilter: function (btn) {
                var _this = btn.data.fun;
                $('.filter-box').removeClass("isBlock");
                _this.ifOpenLoading = true;
                _this.checkMore = true;
                _this.pageNo = 1;
                _this.setFilterData();
                $('.se-con').html('');
                _this.getTabList(_this.type);
            },

            popupFilter: function () {
              $('.filter-box').addClass("isBlock");
            },

            getTabList: function (_type) {
                var _this = this;
                var _url = ''
                _this.isOnceLoading = false;
                if(_type == 1){
                    _url = '/personnel/customer/app-personnel/getAgentPersonnelByViewer'
                }else if(_type == 2){
                    _url = '/personnel/customer/app-personnel/getApprovedAgentPersonnelByViewer'
                }
                var data = {
                    url: _url,
                    type: 'POST',
                    ifOpenLoading: _this.ifOpenLoading,
                    data: JSON.stringify({
                        pageNo: _this.pageNo,
                        pageSize: _this.pageSize,
                        //accountId: '953555301552887890',
                        keyName: $('#search-text').val(),
                        name: _this.name,
                        phone: _this.phone,
                        status: _this.status,
                        position: _this.position,
                        startTime:_this.startTime,
                        endTime:_this.endTime,
                    })
                };

                Interface.getAsynData(data, function (response) {
                    if (response.code == "200") {
                        if (_this.ifOpenLoading) {
                            _this.ifOpenLoading = false;
                        }
                        var temp = '{{~it:item:index}}\
                            <div class="select-item" data-flowId="{{=item.flowId}}" data-id="{{=item.id}}">\
                                <h2>\
                                    <span class="sex-name">{{=item.name && item.name.slice(0, 1)}}</span>\
                                    <i class="iconfont {{=item.sex=="M"?"icon-male":"icon-female"}}"></i>\
                                </h2>\
                                <h3>\
                                    <p class="user-name">{{=item.name}}</p>\
                                    <p class="user-tel">{{=item.cellphone || ""}}</p>\
                                </h3>\
                                <h4>\
                                    <p class="{{=item.sort==1?"bus-contoller":item.sort==2?"bus-manager":item.sort==3?"high-bus-manager":item.sort==4?"bus-major":item.sort==5?"high-bus-major":item.sort==6?"bus-deputy-general-manager":item.sort==7?"high-sell-major":item.sort==8?"sell-deputy-general-manager":"bus-contoller"}}">{{=item.position}}</p>\
                                    <p>\
                                          <span class="sh-status">{{=item.flow!=null?item.flow:item.personnelStatus=="7" ?"资料审核":""}}</span>\
                                        <span class="select-status {{=item.isClose?"no-pass":item.status=="进行中"?"under-way":item.status=="通过"?"pass":item.status=="已签约"?"pass":"no-pass"}}">{{=item.isClose?"已关闭":item.status!=null?item.status:"" }}</span>\
                                    </p>\
                                </h4>\
                            </div>\
                        {{~}}';
                        //item.personnelStatus=="7"&&item.checkResult=="1"?"pass":item.personnelStatus=="7"&&item.checkResult=="2"?"no-pass":   item.personnelStatus=="7"&&item.checkResult=="1" ?"已签约":item.personnelStatus=="7"&&item.checkResult=="2"?"不通过":
                        temp = doT.template(temp);
                        if (_this.post.length) {   
                            $('.select-box .se-con').append(temp(_this.getPostName(response.data.records)));
                        } else {
                            var timer = setInterval(function () {
                                if (_this.post.length) {
                                    var data = _this.getPostName(response.data.records);
                                    $('.select-box .se-con').append(temp(_this.getPostName(response.data.records)));
                                    clearInterval(timer);
                                }
                            }, 20);
                        }
                        _this.isOnceLoading = true;
                        _this.pageNo++;
                        if (_this.isFirstComing) {
                            _this.isFirstComing = false;
                           // _this.initScroll();
                        } else {
                            _this.scroll && _this.scroll.refresh();
                        }
                        if (response.data.records.length < _this.pageSize) {
                            _this.checkMore = false;
                            $('.loading-img').hide();
                            $('.loading-text').text('暂无更多数据');
                        }
                    }
                }, function (error) {

                });
            },

            initScroll: function () {
                var _this = this;
                this.scroll = new BScroll($('.select-box')[0], {
                    scrollX: false,
                    scrollY: true,
                    probeType: 1
                });
                this.scroll.off('scroll');
                this.scroll.on('scroll', function (pos) {
                    this.scrollDisct = pos.y;
                    if(pos.y>10){
                        $('.drop-tips').find('img').css('transform','rotate(0deg)');
                        $('.drop-tips').show().find('span').text('下拉刷新');

                    }
                    if(pos.y>50){
                        $('.drop-tips').find('img').css('transform','rotate(180deg)');
                        $('.drop-tips').show().find('span').text('释放立即刷新');
                    }

                });

                this.scroll.on('scrollEnd', function () {
                    if ((_this.scroll.y <= (_this.scroll.maxScrollY + 50)) && _this.checkMore && _this.isOnceLoading) {
                        _this.getTabList(_this.type);
                    }

                    $('.drop-tips').hide();
                    if (this.scrollDisct > 50) {
                        _this.dropDown = true;
                        _this.ifOpenLoading=true;
                        _this.pageNo = 1;
                        $('.se-con').empty();
                        _this.getTabList(_this.type);
                        this.scrollDisct = 0;
                    }
                })

            },

            getPostName: function (list) {
                var optionList = list.slice(0);
                for (var i = 0; i <optionList.length; i++) {
                    for (var j = 0; j < this.post.length; j++) {
                        if (optionList[i].position === this.post[j].code) {
                            optionList[i].position = this.post[j].value;
                            optionList[i].sort = this.post[j].sort;
                            break;
                        }
                    }
                }
                return optionList;
            }

        };

        emplyProcess.init(opt.body);
    }
});