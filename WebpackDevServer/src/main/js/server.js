var webpackConfigFileName = process.argv[2];
var port = Number.parseInt(process.argv[3]);

var express = require('express');
var webpack = require('webpack');
var webpackMiddleware = require('webpack-dev-middleware');
var config = require(webpackConfigFileName);

const app = express();

app.use(webpackMiddleware(webpack(config), {
    publicPath: "/assets/"
}));

app.listen(port, '0.0.0.0', function(err) {
    if (err) {
        console.log(err);
        return;
    }

    console.log('Listening at http://0.0.0.0:' + port);
});
