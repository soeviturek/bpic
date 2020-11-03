// JavaScript Document
//文件开始包含类似以下注释说明
//文件功能描述着重于描述文件功能与说明，详情应在类的注释中描述。
//一天内有多个修改只需做做一个修改标识，在所有的代码修改处加上
'use strict';
! function($, e) {
    var url = e.url = e.url || {};
    
    url.getPageName = function() {
        var u = window.location.pathname;
        var a = u.split(/\//);
        var m = a.pop().match(/(?:^|\/)($|[^\.]+)/);
        return m[1] ? m[1] : 'index';
    };

    url.getQuery = function(name) {
        var u = window.location.search.slice(1);
        var re = new RegExp(name + '=([^&\\s+]+)');
        var m = u.match(re);
        return m ? m[1] : '';
    };

    url.getHash = function(name) {
        var u = window.location.hash.slice(1);
        var re = new RegExp(name + '=([^&\\s+]+)');
        var m = u.match(re);
        var v = m ? m[1] : '';

        return (v === '' || isNaN(v)) ? v : v - 0;
    };

    url.parseUrl = function(url) {
        var a = document.createElement('a');
        a.href = (url || '404.html');
        return {
            host: a.host,
            protocol: a.protocol
        };
    };

    url.serialize = function(obj) {
        var s = [];
        $.each(obj, function(k, v) {
            s.push(k + '=' + encodeURIComponent(v));
        });
        return s.join('&');
    };

}(window.Zepto, window.url);