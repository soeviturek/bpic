//文件开始包含类似以下注释说明
/*******************************************************************************
 * // 文件名：interface.js // 文件功能描述：存放与数据交互的脚本文件 // 创建人：Lincoln // 创建时间：2014-10-14 //
 * 描述： // 修改人： // 修改时间： // 修改描述： //
 ******************************************************************************/
// 文件功能描述着重于描述文件功能与说明，详情应在类的注释中描述。
// 一天内有多个修改只需做做一个修改标识，在所有的代码修改处加上
var Interface = Interface || {};

;
(function (Interface) {
    // localStorage的key值定义------------------------------start
    Interface.ss = {};
    //Interface.ss.token = 'ACCESS_TOKEN'; //token

    //用户平台信息
    var userPlatform = getPlatform() || '';


    //统一网络请异步求接口
    Interface.getAsynData = function (paramData, successCallbackFunc, errorCallbackFunc) {
    	//用户登录token
    var _token = common.getLocalStorage('token') || '';
    	  //  console.log(_token)
        if (paramData.ifOpenLoading) {
            common.loading.open();
        }

        var _randomcode = common.getRandomCode();  //随机码
        //var _signature = common.getSignature(_randomcode);//签名
        var platform=common.getLocalStorage('platform', true)||'';
        var param = paramData.data;
        var source="/recruit";
        //异步处理
        var asyncMethod = true;
        var successCallbackFunc = successCallbackFunc || function (data) {
                return data;
            };
        var errorCallbackFunc = errorCallbackFunc || function (data) {
                return data;
            };
        var timeoutCallbackFunc = timeoutCallbackFunc || function (data) {
                return data;
            };
        var returnData;

        var _options = {
            async: asyncMethod,
            url: source+paramData.url,
            //url: paramData.url,
            type: paramData.type || 'GET',
            headers: {
                'system': userPlatform,
                'randomcode': _randomcode,
                //'signature': _signature,
                "token": _token
            },
            timeout: '50000',
            contentType: param.contentType || 'application/json',
            dataType: 'json',
            data: param,
            success: function (data) {
                if(paramData.ifOpenLoading) {
                    common.loading.close();
                }
                returnData = data;
                if (returnData && returnData.code && returnData.code == '200') {
                    return successCallbackFunc(returnData);
                } else {
                    switch (returnData.code) {
                        case '004':  //非法请求
                            common.alertCreate({
                                html: '<p>非法请求，请登录后再尝试！</p>',
                                callback: function (status) {
                                    window.location.href = '../login/login.html';
                                }
                            });
                            break;
                        case '005':  //未登录
                            common.alertCreate({
                                html: '<p>您尚未登录，请登录后再进行此操作！</p>',
                                callback: function (status) {
                                    window.location.href = '../login/login.html';
                                }
                            });
                            break;
                        case '006':  //登录超时
                            common.alertCreate({
                                html: '<p>登录超时，请重新登录！</p>',
                                callback: function (status) {
                                    window.location.href = '../login/login.html';
                                }
                            });
                            break;
                        default:
                            return errorCallbackFunc(returnData);
                            break;
                    }
                }
            },
            error: function (data) {
                if(paramData.ifOpenLoading) {
                    common.loading.close();
                }
                var msg = '';
                if (data.status == 500) {
                    msg = '服务器产生内部错误';
                } else if (data.status == 403) {
                    msg = '请求不允许';
                } else if (data.status == 404) {
                    msg = '没有发现文件、查询或URl';
                } else if (data.status == 502 || data.status == 503) {
                    msg = '服务暂时不可用，请重试！';
                } else if (data.status == 200) {
                    var a = eval("(" + data.responseText + ")");
                    msg = a.error;
                } else {
                    msg = '访问错误！';
                }
                //return errorCallbackFunc();
                common.Prompt(msg);
                //showTips(msg);
            }
        };

        if (paramData.hasOwnProperty('cache')) {
            _options.cache = paramData.cache;
        };

        if (paramData.hasOwnProperty('processData')) {
            _options.processData = paramData.processData;
        };
        if (paramData.hasOwnProperty('contentType')) {
            _options.contentType = paramData.contentType;
        };

        Interface.getAjax=$.ajax(_options);
        return returnData;
    };

})(Interface);