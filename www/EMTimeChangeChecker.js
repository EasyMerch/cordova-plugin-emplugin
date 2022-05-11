var argscheck = require('cordova/argscheck');
var channel = require('cordova/channel');
var exec = require('cordova/exec');
var cordova = require('cordova');

/**
 * @constructor
 */
function EMTimeChangeChecker () {
	this._watch_id = 1;
	this._watch_callbacks = {};
}

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

EMTimeChangeChecker.prototype.watch=function(successCallback, options){
	var errorCallback = options.error;
	argscheck.checkArgs('fFO', 'EMTimeChangeChecker.watchTimeChanges', [successCallback, errorCallback, options], arguments.callee);

	if(!this.watch_started){
		exec(this._onTimeChanged.bind(this), errorCallback, 'EMTimeChangeChecker', 'watchTimeChanges', []);
	}

	var watch_id = this.watch_id++;

	this._watch_callbacks[watch_id] = successCallback;

	return watch_id;
};

EMTimeChangeChecker.prototype.clearWatch=function(id){
	delete this._watch_callbacks[id];
};

EMTimeChangeChecker.prototype._onTimeChanged=function(changeObj){
	for(var k in this._watch_callbacks){
		this._watch_callbacks(changeObj);
	}
};

module.exports = new EMTimeChangeChecker();