var argscheck = require('cordova/argscheck');
var channel = require('cordova/channel');
var exec = require('cordova/exec');
var cordova = require('cordova');

/**
 * @constructor
 */
function EMTimeChangeTracker () {
	this._watch_id = 1;
	this._watch_callbacks = {};
}

EMTimeChangeTracker.prototype.getTimeChanges=function(successCallback, errorCallback){
	argscheck.checkArgs('fF', 'EMTimeChangeTracker.getTimeChanges', arguments);
	exec(successCallback, errorCallback, 'EMTimeChangeTracker', 'getTimeChanges', []);
};

EMTimeChangeTracker.prototype.clearTimeChanges=function(options){
	options = options || {};
	var successCallback = options.success || function(){};
	var errorCallback = options.error;

	argscheck.checkArgs('fFO', 'EMTimeChangeTracker.clearTimeChanges', [successCallback, errorCallback, options], arguments.callee);

	exec(successCallback, errorCallback, 'EMTimeChangeTracker', 'clearTimeChanges', []);
};

EMTimeChangeTracker.prototype.watch=function(successCallback, options){
	var errorCallback = options.error;
	argscheck.checkArgs('fFO', 'EMTimeChangeTracker.watchTimeChanges', [successCallback, errorCallback, options], arguments.callee);

	if(!this.watch_started){
		exec(this._onTimeChanged.bind(this), errorCallback, 'EMTimeChangeTracker', 'watchTimeChanges', []);
		this.watch_started = 1;
	}

	var watch_id = this.watch_id++;

	this._watch_callbacks[watch_id] = successCallback;

	return watch_id;
};

EMTimeChangeTracker.prototype.clearWatch=function(id){
	delete this._watch_callbacks[id];
};

EMTimeChangeTracker.prototype._onTimeChanged=function(changeObj){
	for(var k in this._watch_callbacks){
		this._watch_callbacks(changeObj);
	}
};

module.exports = new EMTimeChangeTracker();