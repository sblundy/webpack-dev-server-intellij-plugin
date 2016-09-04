var webpackConfigFileName = process.argv[2];
var port = Number.parseInt(process.argv[3]);
var comPort = Number.parseInt(process.argv[4]);

var express = require('express');
var webpack = require('webpack');
var webpackMiddleware = require('webpack-dev-middleware');
var DashboardPlugin = require('webpack-dashboard/plugin');
var IdeaPluginConnection = require('./IdeaPluginConnection');

var config = require(webpackConfigFileName);
var connection = new IdeaPluginConnection({port: comPort});
var compiler = webpack(config);

const app = express();
compiler.apply(new DashboardPlugin({handler: connection.handle.bind(connection)}));

app.use(webpackMiddleware(compiler, {
    publicPath: "/assets/"
}));

app.listen(port, '0.0.0.0', function(err) {
    if (err) {
        console.log(err);
        connection.sendConnection("ERROR");
        return;
    }

    connection.sendConnection("LISTENING");

    console.log('Listening at http://0.0.0.0:' + port);
});
