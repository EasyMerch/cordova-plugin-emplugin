var argscheck = require('cordova/argscheck');
var channel = require('cordova/channel');
var exec = require('cordova/exec');
var cordova = require('cordova');

/**
 * @constructor
 */
function EMPlugin () {
	this.device = {
		aviable: null,
		isVirtual: null,
		serial: null,
		info: null
	};
	
	var t = this;
	channel.onCordovaReady.subscribe(function () {
		t._getDeviceInfo(
			function (info) {
				t.device.aviable	= true;
				t.device.isVirtual	= info.isVirtual || 'unknown';
				t.device.serial		= info.serial || 'unknown';
				t.device.info		= info.info || 'unknown';
			},
			function (e) {
				t.device.aviable = false;
				console.error('[ERROR] Error initializing emplugin: ' + e);
			}
		);
	});
}


EMPlugin.prototype._getDeviceInfo=function (successCallback, errorCallback) {
	argscheck.checkArgs('fF', 'EMPlugin._getDeviceInfo', arguments);
	exec(successCallback, errorCallback, 'EMPlugin', 'getDeviceInfo', []);
};

EMPlugin.prototype.getMockPermissionApps=function(successCallback, errorCallback){
	argscheck.checkArgs('fF', 'EMPlugin.getMockPermissionApps', arguments);
	exec(successCallback, errorCallback, 'EMPlugin', 'getMockPermissionApps', []);
};

EMPlugin.prototype.locationIsMock=function(successCallback, errorCallback){
	argscheck.checkArgs('fF', 'EMPlugin.locationIsMock', arguments);
	exec(successCallback, errorCallback, 'EMPlugin', 'locationIsMock', []);
};

EMPlugin.prototype.saveImageToGallery=function(path, options){
	options = options || {};

	var errorCallback = options.error;

	function successCallback(){
		if(options.success) options.success();
	}

	argscheck.checkArgs('fFsO', 'EMPlugin.saveImageToGallery', [successCallback, errorCallback, path, options], arguments.callee);
	
	exec(successCallback, errorCallback, 'EMPlugin', 'saveImageToGallery', [path, options]);
};

module.exports = new EMPlugin();