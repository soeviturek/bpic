cordova.define("cordova-plugin-sharedPref.SharedPref", function(require, exports, module) {
/*global cordova,window,console*/
/**
 * An Image Picker plugin for Cordova
 *
 * Developed by Wymsee for Sync OnSet
 */
var SharedPref = function () {
}
/*
*  success - success callback
*  fail - error callback
*  options
*    .maximumImagesCount - max images to be selected, defaults to 15. If this is set to 1,
*                          upon selection of a single image, the plugin will return it.
*    .width - width to resize image to (if one of height/width is 0, will resize to fit the
*             other while keeping aspect ratio, if both height and width are 0, the full size
*             image will be returned)
*    .height - height to resize image to
*    .quality - quality of resized image, defaults to 100
*/
SharedPref.prototype.save = function (success, fail, options) {
  options = options || {}

  options.key = options.key || ''
  options.str = options.str || ''

  return cordova.exec(success, fail, 'SharedPref', 'save', [options])
}

SharedPref.prototype.get = function (success, fail, options) {
 			    options = options || {};

 			    options.key = options.key || '';

 			    return cordova.exec(success, fail, "SharedPref", "get", [options]);
}

SharedPref.prototype.clear = function (success, fail, options) {
      options = options || {}

      options.key = options.key || ''

      return cordova.exec(success, fail, 'SharedPref', 'clear', [options])
}
module.exports = new SharedPref()

});