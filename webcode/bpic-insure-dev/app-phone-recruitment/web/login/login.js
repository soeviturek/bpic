// JavaScript Document
$(function(){
    $('body').on('change', '.ipt-select select', function () {
        $(this).siblings('.ipt-text').val($(this).children(':checked').text());
    });

	$('#login-form li:not(active)').on('tap', function () {
		$(this).addClass('active').siblings().removeClass('active');
		$(this).data('type') === 'face' ? ($('.face-recognition').removeClass('go-left'),
			$('.user-password').removeClass('go-right')) : ($('.face-recognition').addClass('go-left'),
			$('.user-password').addClass('go-right'))
    });
	
	$('.login-to-platform').on('tap', function(){
		var accountName = $(".user-account input").val();
		var password = $(".user-pass input").val();
		var systemType = $('[name=sys-type]').val();
		if(!systemType) {
			common.alertCreate({
				html: '请选择系统类型'
			});
			return false;
		}
		if(systemType=="JT_SYSTEM"){
				var login = {
					 params: accountName,
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
					return  window.location.href='../home/#page=main';
				}
			}, function (error) {
				common.alertCreate({
					html: error.msg
				});
	            return false;
			});
		}else{
			var login = {
	            accountName: accountName,
	            password: password,
	            accountType: 1,
	            systemType: systemType
	        };
			var paramData = {
	            ifOpenLoading: true,
				url: '/security/login',
				type: 'POST',
				data: JSON.stringify(login)
			};
			Interface.getAsynData(paramData,function (response) {
				if(response.code == "200") {
					var data=JSON.parse(response.data);
					common.setLocalStorage("currentUser",JSON.stringify(data.account));
					common.setLocalStorage("token",data.token);
					return  window.location.href='../home/#page=main';
				}
			}, function (error) {
				common.alertCreate({
					html: error.msg
				});
	            return false;
			});
		}
	});

    $('.face-login').on('tap', function(){
        return  window.location.href='../home/#page=main';
    });
});
