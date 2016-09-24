package net.sblundy.plugins.webpack.devServer.server

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket

internal class ServerStatusMonitorImpl(val mapper: ObjectMapper) : ServerStatusMonitor, Disposable {
    private val LOGGER = Logger.getInstance(ServerStatusMonitorImpl::class.java)

    private val connection = ServerSocket(0)
    private var compileListeners: Collection<CompileStatusListener> = arrayListOf()
    private var currentServerStatus: ServerStatusMonitor.ServerStatus = ServerStatusMonitor.ServerStatus.NONE
    private var currentConnectionStatus: ServerStatusMonitor.ConnectionStatus = ServerStatusMonitor.ConnectionStatus.NONE

    init {
        ApplicationManager.getApplication().executeOnPooledThread({ receiveMessages() })
    }

    private fun receiveMessages() {
        val socket: Socket = connection.accept()
        try {
            LOGGER.debug("Connected")
            val reader = BufferedReader(InputStreamReader(socket.inputStream, "UTF-8"))
            var line: String? = reader.readLine()
            while (line != null) {
                LOGGER.debug("Received messages:" + line)
                val msg = mapper.readValue<Message>(line)

                if ("status".equals(msg.type)) {
                    handleStatus(msg)
                } else if ("connection".equals(msg.type)) {
                    currentConnectionStatus = ServerStatusMonitor.ConnectionStatus.valueOf(msg.status)
                } else {
                    LOGGER.warn("Message type not recognized:" + line)
                }

                line = reader.readLine()
            }
        } catch (e: IOException) {
            LOGGER.error("Error in socket", e)
        } finally {
            socket.close()
        }
    }

    private fun handleStatus(msg: Message) {
        currentServerStatus = ServerStatusMonitor.ServerStatus.valueOf(msg.status)
        when (currentServerStatus) {
            ServerStatusMonitor.ServerStatus.INVALIDATED -> this.compileListeners.forEach { it.onInvalidated() }
            ServerStatusMonitor.ServerStatus.COMPILING -> this.compileListeners.forEach {
                if (msg.progress == null)
                    it.onStart()
                else {
                    it.onProgress(msg.progress, msg.operations)
                }
            }
            ServerStatusMonitor.ServerStatus.COMPLETE -> this.compileListeners.forEach { it.onComplete(msg.assets, msg.errors) }
            ServerStatusMonitor.ServerStatus.FAILED -> this.compileListeners.forEach { it.onFailure() }
            ServerStatusMonitor.ServerStatus.NONE -> {}
        }
    }

    override fun getPort(): Int {
        return connection.localPort
    }

    override fun addListener(listener: CompileStatusListener) {
        this.compileListeners += listener

    }

    override fun dispose() {
        try {
            this.connection.close()
        } catch (e: IOException) {
            LOGGER.warn("Error closing", e)
        }
    }
}