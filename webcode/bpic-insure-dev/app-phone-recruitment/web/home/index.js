// var modelPage = null;
// var jobData = new Array();
   var vConsole = new VConsole();
// var justify = false;

 $.domainUrl = '' ; 
//  var listUrl= common.getLocalStorage('listUrl', true) || [];
define(['seajsText', 'seajsCss', 'zeptoTouch', 'zeptoMd5', 'doT', 'interface', 'validation', 'BScroll','conversion'], function (require, exports) {
    seajs.use(['../../css/components.css','../../css/common.css']);

    $(window).bind("hashchange", $.loadPanel).trigger("hashchange");

    var body = $('body');

    /* 头部页面返回入口 */
    // 单选框功能
    body.on('change', '.ipt-radio input', function () {
        $(this).parent().addClass('checked');
        $(this).parent().siblings('.checked').removeClass('checked');
    });

    /// 复选框功能
    body.on('change', '.ipt-checkbox input', function () {
        $(this).is(':checked') ? $(this).parent().addClass('checked') : $(this).parent().removeClass('checked');
    });

    // 下拉框事件
    body.on('change', '.ipt-select select', function () {
        $(this).siblings('.ipt-text').val($(this).children(':checked').text());
    });

    // 遮罩层禁止滑动
    body.on('touchmove', '.popup-bg', function (e) {
        e.preventDefault();
    });

    /* 表单失去焦点监控验证 */
    body.on('blur', '.check-vali', function(){
        var _this = $(this);
        var _ipt = validation.checkVali($(this));
        if(!_ipt.status){
            common.alertCreate({
                html : _ipt.message,
                callback : function(){
                    _this.val('');
                }
            });
        }
    });
  /*$.automaticLogin();*/
 
    // 获取上传路径
    var platform=common.getLocalStorage('platform', true)||''
    if(!platform){
        platform = common.getHash('platform')||'';
    }
    var uploadParam = {
        url: '/web/system/dict/data/type/findByCode',
        data: {
            code:'JTER_URL1'
        }
    };

    Interface.getAsynData(uploadParam, function (response) {
        if(response.code == "200") {
            $.domainUrl = response.data.value;
        }
    }, function (error) {
        common.alertCreate({
            html: '未获取到上传路径！'
        })
    });
});

$.automaticLogin = function(){
    var platform=common.getLocalStorage('platform', true)||''
    if(!platform){
        platform = common.getHash('platform')||'';
    }
	 var now=common.getHash("page");

	if(now=="main"){
		var user = common.getHash("token");
		common.setLocalStorage('user', user);
	}
	if(now=="main"){
		      var user = common.getHash("token");
		      var login = {
                         params:user,
                         platform:platform,
		       		};
		            var paramData = {
			            ifOpenLoading: true,
						url: '/security/JTERLogin',
						type: 'POST',
						contentType: 'application/x-www-form-urlencoded',
						data:login
					};
				Interface.getAsynData(paramData,function (response) {
					if(response.code == "200") {
						var data=JSON.parse(response.data);
						common.setLocalStorage("currentUser",JSON.stringify(data.account));
						common.setLocalStorage("token",data.token);
					}
				}, function (error) {
					common.alertCreate({
						html: error.msg
					});
		            return false;
				});
		}
};

$.loadPanel = function () {
	common.setLocalStorage('platform', "MER",true);
    var platform = common.getHash('platform')||'';
    if(!platform){
        platform=common.getLocalStorage('platform', true)||'';
    }
    common.setLocalStorage('platform', platform, true);
    var page = common.getHash('page');
    hrefType=common.getHash('hrefType')||''; //由招募审批页面的已审批tab跳到rescrite而加的返回区别标识
   // 缓存跳转路由 childrenUrl，listUrl为以后全做缓存地址跳转的预留
   var Url = window.location.href.split("#")[1];
   if(page=='main'){
    console.log(Url);
    common.removelocalStorage('mainUrl')
    //  common.setLocalStorage('mainUrl', Url, true);  //有问题，注释掉
   }
  	 
    // 跳转部分 因为推送问题，使用定向跳转，缓存记录地址，history
    if(page=='emplyApprove'||page=='emplyProcess'){

       $('.header').off('tap','.back');
        $('.header').on('tap', '.back', function(){
        	 var mainUrl=common.getLocalStorage('mainUrl', true) || 'page=main';
                if($(".filter-box").hasClass("isBlock")){
                	 $(".filter-box").removeClass("isBlock")
                	 return;
                }
            return window.location.href='#'+mainUrl;
        });
    }else if(page=='main'){
        $('.header').off('tap','.back');
        $('.header').on('tap', '.back', function(){
          try {
            if(platform=='MER'){

                console.log('$.domainUrl+"/me1111"')
                var autoMessage = {"name":"toMe"};
                window.parent.postMessage(autoMessage, '*');
         
			 	// window.location.href =  $.domainUrl+"/me";
//              window.postMessage(JSON.stringify({
//                  action:'close'
//              }));
            }else{
               ebtJs.closeWebview('123'); 
            }
          }catch (e) {
            console.log('EbtJsBridge run error!')
          }
    });
    }else {
        $('.header').off('tap','.back');
        $('.header').on('tap', '.back', function(){
            var mainUrl=common.getLocalStorage('mainUrl', true) || 'page=main';
            if(page=='wechatShare'||page=='personnelFile'){
                return window.location.href='#'+mainUrl;
            }
            if(page=='rescrite'&&!hrefType){
                return window.location.href='#page=emplyProcess';
            }
            if(page=='recheck'||page=='fircheck'||(hrefType&&page=='rescrite')){
                return window.location.href='#page=emplyApprove';
            }
            console.log('window.history.go2222222')
            var autoMessage = {"name":"toMe"};
            window.parent.postMessage(autoMessage, '*');

            // return window.history.go(-1);
        });

    }
    switch (page) {
        case '':
        case 'main':
        case null:
        case undefined:
            $('.title').text('线上报聘');
            try {
                // EbtJsBridge.setNativeTitle();
                ebtJs.setNativeTitle(null)

            }catch (e) {
                console.log('EbtJsBridge run error!')
            }
            $.showPanel('../home/main.js');
            break;
        case 'fircheck':
            $('.title').text('初审');
            try {
                ebtJs.setNativeTitle(null)

            }catch (e) {
                console.log('EbtJsBridge run error!')
            }
            $.showPanel('../components/fir-check.js');
            break;
        case 'recheck':
            try {
                ebtJs.setNativeTitle(null);

            }catch (e) {
                console.log('EbtJsBridge run error!')
            }
            $('.title').text('复审');
            $.showPanel('../components/recheck.js');
            break;
        case 'emplyProcess':
            try {
            	
                ebtJs.setNativeTitle(null);
            }catch (e) {
                console.log('EbtJsBridge run error!')
            }
            $('.title').text('我的招募');
            $.showPanel('../components/emply-process.js');
            break;
        case 'rescrite':
            try {
                ebtJs.setNativeTitle(null);

            }catch (e) {
                console.log('EbtJsBridge run error!')
            }
            $('.title').text('招募详情');
            $.showPanel('../components/rescrite-details.js');
            break;
        case 'signDatum':
            $('.title').text('签约资料');
            $.showPanel('../components/sign-datum.js');
            break;
        case 'transmit':
            try {
                ebtJs.setNativeTitle(null);

            }catch (e) {
                console.log('EbtJsBridge run error!')
            }

            $('.title').text('签约转发');
            $.showPanel('../components/transmit-sign.js');
            break;
        case 'emplyApprove':
            try {
                ebtJs.setNativeTitle(null);

            }catch (e) {
                console.log('EbtJsBridge run error!')
            }
            $('.title').text('招募审批');
            $.showPanel('../components/emply-approve.js');
            break;
        case 'baseInfo':
            $('.title').text('签约明细');
            $.showPanel('../components/base-info.js');
            break;
        case 'cardInfo':
            $('.title').text('签约明细');
            $.showPanel('../components/card-info.js');
            break;
        case 'personnelFile':
            try {
                ebtJs.setNativeTitle(null);

            }catch (e) {
                console.log('EbtJsBridge run error!')
            }
            $('.title').text('个人档案');
            $.showPanel('../components/personnel-file.js');
            break;
        case 'wechatShare':
            try {
                ebtJs.setNativeTitle(null);
            }catch (e) {
                console.log('EbtJsBridge run error!')
            }
            $('.title').text('招募邀请');
            $.showPanel('../components/invite.js');
            break;
        case 'signCheck':
            $('.title').text('签约审核');
            try {
                ebtJs.setNativeTitle(null);
            }catch (e) {
                console.log('EbtJsBridge run error!')
            }
            $.showPanel('../components/sign-check.js');
            break;
        case 'signPage':
            $.showPanel('../components/sign-page.js');
            break;
        case 'inviteShare':
            try {
                ebtJs.setNativeTitle(null);

            }catch (e) {
                console.log('EbtJsBridge run error!')
            }
            $('.title').text('招募邀请');
            $.showPanel('../components/invite-share.js');
            break;
        case 'inform':
            $('.title').text('签约明细');
            $.showPanel('../components/inform.js');
            break;
        case 'lookPdf':
            $('.title').text('PDF');
            $.showPanel('../components/look-pdf.js');
            break;
        case 'bankSign':
            $('.title').text('银行卡资料补录');
            $.showPanel('../components/bank-sign.js');
            break;
    }

    // if (page == '' || page == null || page == undefined || page == 'main') {
    //     $('.back').hide();
    // } else {
    //     $('.back').show();
    // }

};

$.showPanel = function (file) {
    var mainBody = $('.main');
    seajs.use(file, function (account) {
        modelPage = account;
        modelPage.show({
            body: mainBody
        });
        window.scrollTo(0, 0);
    });

};

