package net.sblundy.plugins.webpack.devServer.server

internal data class Message(
        val type: String,
        val status: String,
        val progress: Double?,
        val operations: String?,
        val errors: List<String>?,
        val assets: List<Asset>?)

internal data class Asset(
        val name: String,
        val size: Long,
        val chunks: List<Int>,
        val chunkNames: List<String>,
        val emitted: Boolean)