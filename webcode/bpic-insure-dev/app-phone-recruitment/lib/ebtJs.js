;(function (root, factory) {
    if (typeof exports === 'object') {
        module.exports = exports = factory()
    } else if (typeof define === 'function' && define.amd) {
        define([], factory)
    } else {
        root.ebtJs = factory()
    }
}(this, function () {
    var system = function () {
        var u = navigator.userAgent,
            isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1,
            isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/),
            system = void 0;
        if (isAndroid) {
            system = 'Android';
        } else if (isIOS) {
            system = 'IOS';
        }
        return system;
    }();

    if (system === 'IOS') {
        // 展业家app
        window.isWebViewBridge && sessionStorage.setItem('isWebViewBridge', window.isWebViewBridge);
    }

    var setupWebViewJavascriptBridge = function setupWebViewJavascriptBridge(callback) {
        if (!!window.WebViewJavascriptBridge) {
            return callback();
        }
        if (!!window.WVJBCallbacks) {
            return window.WVJBCallbacks.push(callback);
        }
        window.WVJBCallbacks = [callback];

        var WVJBIframe = document.createElement('iframe');

        WVJBIframe.style.display = 'none';
        WVJBIframe.src = 'https://__bridge_loaded__';

        document.documentElement.appendChild(WVJBIframe);
        setTimeout(function () {
            document.documentElement.removeChild(WVJBIframe);
        }, 0);
    };

    var inAndroid = function inAndroid() {
        return typeof EbtJsBridge !== 'undefined';
    };

    var inIos = function inIos() {
        return window.isWebViewBridge || !!sessionStorage.getItem('isWebViewBridge');
    };

    var inApp = function inApp() {
        return inAndroid() || inIos();
    };

    var bridgeHandler = function bridgeHandler(functionName, nativeWillResponse) {
        return function () {
            for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
                args[_key] = arguments[_key];
            }

            return new Promise(function (resolve, reject) {
                if (inApp()) {
                    if (inAndroid()) {
                        if (EbtJsBridge[functionName]) {
                            var _EbtJsBridge;
                            resolve((_EbtJsBridge = EbtJsBridge)[functionName].apply(_EbtJsBridge, args));
                        } else reject();
                    } else {
                        setupWebViewJavascriptBridge(function () {
                            var params = !args.length ? ['bridge'] : args;
                            window.WebViewJavascriptBridge.callHandler(functionName, { params: params }, resolve);
                            !nativeWillResponse && resolve();
                        });
                    }
                } else reject();
            });
        };
    };

    var catchHandler = function catchHandler() {};

    //获取sysTime
    var getSysTime = function getSysTime(cb) {
        return bridgeHandler('getSysTime', true)().then(function (res) {
            return cb(JSON.parse(res).systime);
        }).catch(cb);
    };

    //关闭webView，回退到webView开启前的native页面
    var notifyBack = function notifyBack() {
        return bridgeHandler('notifyBack')().catch(function () {
            return history.go(-1);
        });
    };
    //设置原生头部标题名称
    var setNativeTitle = function setNativeTitle(name, url) {
        return bridgeHandler('setNativeTitle')(name, url).catch(catchHandler);
    };
    //关闭所在webView
    var closeWebview = function closeWebview(name) {
        return bridgeHandler('closeWebview')(name).catch(catchHandler);
    };
    //分享
    var share = function share(url, title, subTitle, imgUrl) {
        return bridgeHandler('share')(url, title, subTitle, imgUrl).catch(catchHandler);
    };
    //打开自定义原生回退
    var callNativeReturn = function callNativeReturn(cb) {
        return bridgeHandler('callNativeReturn')(cb).catch(catchHandler);
    };
    //取消自定义原生回退
    var cancelNativeReturn = function cancelNativeReturn() {
        return bridgeHandler('cancelNativeReturn')().catch(catchHandler);
    };
    return {
        inApp: inApp,
        getSysTime: getSysTime,
        notifyBack: notifyBack,
        setNativeTitle: setNativeTitle,
        closeWebview: closeWebview,
        share: share,
        callNativeReturn: callNativeReturn,
        cancelNativeReturn: cancelNativeReturn
    }
}));