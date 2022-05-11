var argscheck = require('cordova/argscheck');
var channel = require('cordova/channel');
var exec = require('cordova/exec');
var cordova = require('cordova');

/**
 * @constructor
 */
function EMTimeChangeChecker () {}

EMTimeChangeChecker.prototype.getTimeChanges=function(successCallback, errorCallback){
	argscheck.checkArgs('fF', 'EMTimeChangeChecker.getTimeChanges', arguments);
	exec(successCallback, errorCallback, 'EMTimeChangeChecker', 'getTimeChanges', []);
};

EMTimeChangeChecker.prototype.clearTimeChanges=function(options){
	options = options || {};
	var successCallback = options.success || function(){};
	var errorCallback = options.error;

	argscheck.checkArgs('fFO', 'EMTimeChangeChecker.clearTimeChanges', [successCallback, errorCallback, options], arguments.callee);

	exec(successCallback, errorCallback, 'EMTimeChangeChecker', 'clearTimeChanges', []);
};

EMTimeChangeChecker.prototype.watchTimeChanges=function(successCallback, options){
	var errorCallback = options.error;

	argscheck.checkArgs('fFO', 'EMTimeChangeChecker.watchTimeChanges', [successCallback, errorCallback, options], arguments.callee);

	exec(successCallback, errorCallback, 'EMTimeChangeChecker', 'watchTimeChanges', []);
};

module.exports = new EMTimeChangeChecker();