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
    argscheck.checkArgs('fF', 'EMPlugin.getInfo', arguments);
    exec(successCallback, errorCallback, 'EMPlugin', 'getDeviceInfo', []);
};

EMPlugin.prototype.getMockPermissionApps=function(successCallback, errorCallback){
	argscheck.checkArgs('fF', 'EMPlugin.getInfo', arguments);
	exec(successCallback, errorCallback, 'EMPlugin', 'getMockPermissionApps', []);
};

EMPlugin.prototype.locationIsMock=function(successCallback, errorCallback){
	argscheck.checkArgs('fF', 'EMPlugin.getInfo', arguments);
	exec(successCallback, errorCallback, 'EMPlugin', 'locationIsMock', []);
};

module.exports = new EMPlugin();
