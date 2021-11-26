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
    this.info = null;

    var t = this;

    channel.onCordovaReady.subscribe(function () {
        t.getInfo(
            function (info) {
				t.aviable = true;
				t.is_virtual = info.isVirtual;
				t.serial = info.serial;
				t.info = info.info || 'unknown';
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
    argscheck.checkArgs('fF', 'EMPlugin.getInfo', arguments);
    exec(successCallback, errorCallback, 'EMPlugin', 'getDeviceInfo', []);
};

module.exports = new EMPlugin();
