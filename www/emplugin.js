var argscheck = require('cordova/argscheck');
var channel = require('cordova/channel');
var exec = require('cordova/exec');
var cordova = require('cordova');

channel.createSticky('onCordovaInfoReady');
// Tell cordova channel to wait on the CordovaInfoReady event
channel.waitForInitialization('onCordovaInfoReady');

/**
 * @constructor
 */
function EMPlugin () {
    this.aviable	= null;
    this.is_virtual	= null;
    this.serial		= null;
    this.info		= null;
	
    var t = this;

    channel.onCordovaReady.subscribe(function () {
        t.getDeviceInfo(
            function (info) {
				t.aviable = true;
				t.is_virtual	= info.isVirtual || 'unknown';
				t.serial		= info.serial || 'unknown';
				t.info			= info.info || 'unknown';
                channel.onCordovaInfoReady.fire();
            },
            function (e) {
				t.aviable = false;
                console.error('[ERROR] Error initializing cordova-plugin-device: ' + e);
            }
        );
    });
}


EMPlugin.prototype.getDeviceInfo = function (successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'EMPlugin.getDeviceInfo', arguments);
    exec(successCallback, errorCallback, 'EMPlugin', 'getDeviceInfo', []);
};

module.exports = new EMPlugin();
