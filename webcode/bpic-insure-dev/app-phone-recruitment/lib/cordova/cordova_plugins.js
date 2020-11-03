cordova.define('cordova/plugin_list', function (require, exports, module) {
    module.exports = [
        {
            "file": "plugins/cordova-plugin-upload.www/upload.js",//js文件路径
            "id": "cordova-plugin-upload.www.UpCardId",//js cordova.define的id
            "clobbers": [
                "UpCardId"//js 调用时的方法名
            ]
        },
        {
            "file": "plugins/cordova-plugin-baiduocr.www/baidu_ocr.js",//js文件路径
            "id": "cordova-plugin-baiduocr.www.FULAN_OCR",//js cordova.define的id
            "clobbers": [
                "FULAN_OCR"//js 调用时的方法名
            ]
        },
        {
            "file": "plugins/cordova-plugin-pen.www/pen.js",//js文件路径
            "id": "cordova-plugin-pen.www.Pen",//js cordova.define的id
            "clobbers": [
                "Pen"//js 调用时的方法名
            ]
        },
        {
            "id": "cordova-plugin-camera.Camera",
            "file": "plugins/cordova-plugin-camera/www/CameraConstants.js",
            "pluginId": "cordova-plugin-camera",
            "clobbers": [
                "Camera"
            ]
        },
        {
            "id": "cordova-plugin-camera.CameraPopoverOptions",
            "file": "plugins/cordova-plugin-camera/www/CameraPopoverOptions.js",
            "pluginId": "cordova-plugin-camera",
            "clobbers": [
                "CameraPopoverOptions"
            ]
        },
        {
            "id": "cordova-plugin-camera.camera",
            "file": "plugins/cordova-plugin-camera/www/Camera.js",
            "pluginId": "cordova-plugin-camera",
            "clobbers": [
                "navigator.camera"
            ]
        },
        {
            "id": "cordova-plugin-camera.CameraPopoverHandle",
            "file": "plugins/cordova-plugin-camera/www/CameraPopoverHandle.js",
            "pluginId": "cordova-plugin-camera",
            "clobbers": [
                "CameraPopoverHandle"
            ]
        },
        {
            "id": "cordova-plugin-device.device",
            "file": "plugins/cordova-plugin-device/www/device.js",
            "pluginId": "cordova-plugin-device",
            "clobbers": [
                "device"
            ]
        },
        {
            "id": "cordova-plugin-geolocation.geolocation",
            "file": "plugins/cordova-plugin-geolocation/www/android/geolocation.js",
            "pluginId": "cordova-plugin-geolocation",
            "clobbers": [
                "navigator.geolocation"
            ]
        },
        {
            "id": "cordova-plugin-geolocation.PositionError",
            "file": "plugins/cordova-plugin-geolocation/www/PositionError.js",
            "pluginId": "cordova-plugin-geolocation",
            "runs": true
        }
    ];
    module.exports.metadata =
// TOP OF METADATA
        {
            "cordova-plugin-whitelist": "1.3.3",
            "cordova-plugin-camera": "3.0.0",
            "cordova-plugin-device": "1.1.6",
            "cordova-plugin-geolocation": "3.0.0"
        };
// BOTTOM OF METADATA
});