define(function (require, exports) {
    require('../../css/invite.css');
    // require('pdfJs');
    var template = require('./look-pdf.html');
    // console.log(template);
    exports.back = function () {
        return '';
    };
    exports.show = function (opt) {
        var recheck = {
            init: function (obj) {
                this.obj = obj;
                this.url = common.getSessionStorage('pdfUrl', false);
                this.obj.html(template);
                console.log(common.getSessionStorage('pdfUrl', false));
                this.lookPdfL(this.url);
            },
            lookPdfL: function (url) {
                url=url.replace('https://share','https://er')
                this.url=this.url.replace('https://share','https://er')
            	var _this = this;
                if(!PDFJS){
                    this.lookPdfL(this.url);
                }
                console.log(PDFJS, 'pdfjs');
                console.log(url);
                common.loading.open();
                setTimeout(function () {
                    common.loading.close();
                }, 10000);
                console.log(location.href, 'href');
                PDFJS.workerSrc = $.domainUrl+'/lib/pdf.worker.js';
                // PDFJS.workerSrc ='../../lib/pdf.worker.js';
                var loadingTask = PDFJS.getDocument(url);
                loadingTask.promise.then(function (pdf) {
                    var pageNumber = pdf.numPages;
                    console.log(pageNumber, 'pages');
                    for (var i = 1; i <= pageNumber; i++) {
                        pdf.getPage(i).then(function (page) {
                            console.log('Page loaded');
                            var scale = 1.5;
                            var viewport = page.getViewport(scale);
                            // var canvas = document.getElementById('canvas');
                            var canvas = document.createElement('canvas');
                            var context = canvas.getContext('2d');
                            canvas.height = viewport.height;
                            canvas.width = viewport.width;
                            document.getElementById('canvas-box').appendChild(canvas);
                            var renderContext = {
                                canvasContext: context,
                                viewport: viewport
                            };
                            var renderTask = page.render(renderContext);
                            renderTask.then(function () {
                                common.loading.close();
                                console.log('Page rendered');
                            });
                        });
                    }
                }, function (reason) {
                    console.error(reason);
                });
            }
        };
        recheck.init(opt.body);
    }

});