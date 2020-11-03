define(function (require, exports) {
    var template = require('./index-page.html');
    require('../../css/index-page.css');
    exports.back = function () {
        return '';
    };

    exports.show = function (opt) {
        var recheck = {
            init: function (obj) {
                this.obj = obj;
                this.obj.html(template);
                this.platform=common.getLocalStorage('platform', true)||'';
                this.initSwiper();
                this.obj.off('tap', '.review-invite');
                this.obj.on('tap', '.review-invite', {fun: this}, this.goInvite);
                this.obj.off('tap', '.emply-process');
                this.obj.on('tap', '.emply-process', {fun: this}, this.goProcess);
                this.obj.off('tap', '.emply-sp');
                this.obj.on('tap', '.emply-sp', {fun: this}, this.goSp);
                this.obj.off('tap', '.person-doc');
                this.obj.on('tap', '.person-doc', {fun: this}, this.goPersonDoc);
            },
            initSwiper:function () {
                var mySwiper = new Swiper ('.swiper-container', {
                    loop: true,
                    autoplay:true,
                    // 如果需要分页器
                    pagination: {
                        el: '.swiper-pagination',
                    },

                });
            },
            goInvite:function () {
                var _this=this;
                if(_this.platform=='MER'){
                    window.postMessage(JSON.stringify({
                        action:'openShare',
                        url:location.href,
                        title:'haha',
                        text:'11111',
                        img: 'http://nddnk.jpg'
                    }));
                }else{
                    ebtJs.share(location.href,'haha','11111','http://nddnk.jpg');
                }
            },
            goProcess:function () {
                window.location.href='#page=emplyProcess';
            },
            goSp:function () {
                window.location.href='#page=emplyApprove';
            },
            goPersonDoc:function () {
                window.location.href='#page=personnelDoc';
            }
        };

        recheck.init(opt.body);
    }
});