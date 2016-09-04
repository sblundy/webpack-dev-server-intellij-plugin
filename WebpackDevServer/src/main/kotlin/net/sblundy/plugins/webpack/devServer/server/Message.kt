package net.sblundy.plugins.webpack.devServer.server

internal data class Message(val type: String, val status: String, val progress: Double?, val operations: String?, val errors: Boolean?)