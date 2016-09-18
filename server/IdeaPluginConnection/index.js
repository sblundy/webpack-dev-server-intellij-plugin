var net = require('net');

function IdeaPluginConnection(options) {
    var port = options.port;

    this.socket = net.createConnection({port: port});
}

function findMessage(messages, name) {
    return messages.find(function (msg) {
        return msg.type === name;
    });
}

IdeaPluginConnection.prototype.handle = function (messages) {
    var status = findMessage(messages, 'status');
    if (!(status)) {
    } else if (status.value === 'Success') {
        var stats = findMessage(messages, 'stats');
        this.handleSuccess(stats);
    } else if (status.value === 'Invalidated') {
        this.handleInvalidated();
    } else if (status.value === 'Failed') {
        this.handleFailed();
    } else if (status.value === 'Compiling') {
        var progress = findMessage(messages, 'progress');
        var operations = findMessage(messages, 'operations');
        this.handleCompiling(progress, operations);
    }
};

IdeaPluginConnection.prototype.handleSuccess = function (stats) {
    this.send({
        type: 'status',
        status: 'COMPLETE',
        errors: stats.value.errors,
        assets: stats.value.data.assets
    });
};

IdeaPluginConnection.prototype.handleInvalidated = function () {
    var msg = {type: 'status', status: 'INVALIDATED'};
    this.send(msg);
};

IdeaPluginConnection.prototype.handleFailed = function () {
    var msg = {type: 'status', status: 'FAILED'};
    this.send(msg);
};

IdeaPluginConnection.prototype.handleCompiling = function (progress, operations) {
    var msg = {type: 'status', status: 'COMPILING'};
    if ((progress)) {
        msg.progress = progress.value;
    }
    if ((operations)) {
        msg.operations = operations.value;
    }
    this.send(msg);
};

IdeaPluginConnection.prototype.sendConnection = function (msg) {
    this.send({type: 'connection', status: msg});
};

IdeaPluginConnection.prototype.send = function (msg) {
    this.socket.write(JSON.stringify(msg));
    this.socket.write('\n');
};

module.exports = IdeaPluginConnection;