package net.sblundy.plugins.webpack.devServer.server

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreterRef
import com.intellij.openapi.components.AbstractProjectComponent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.util.PathUtil
import net.sblundy.plugins.webpack.devServer.WebpackDevServerRunProfileState
import java.io.File
import java.io.IOException
import java.util.concurrent.ExecutionException

/**
 */
class ServerMonitorFactoryImpl(project: Project) : AbstractProjectComponent(project), ServerMonitorFactory {
    private val LOGGER = Logger.getInstance(ServerMonitorFactoryImpl::class.java)

    private var libCache: File? = null

    private val mapper = jacksonObjectMapper()

    override fun getScriptFile(): File {
        val jarPath = PathUtil.getJarPathForClass(WebpackDevServerRunProfileState::class.java)
        val pluginClassesDir = File(jarPath)

        val script = File(pluginClassesDir.parent, "server/server.js")

        LOGGER.assertTrue(script.exists(), "Script file not found:" + script.absolutePath)
        return script
    }

    override fun getLibraryCache(nodeInterpreterRef: NodeJsInterpreterRef?): File {
        return if (null != this.libCache) this.libCache as File else initLibCache(nodeInterpreterRef)
    }

    private fun initLibCache(nodeInterpreterRef: NodeJsInterpreterRef?): File {
        val initTask = ServerInitTask(super.myProject, nodeInterpreterRef)
        val temp = File(ProgressManager.getInstance().run(initTask), "node_modules")
        this.libCache = temp
        return temp
    }

    override fun createStatusMonitor(): ServerStatusMonitor {
        try {
            return ServerStatusMonitorImpl(mapper)
        } catch (e: IOException) {
            throw ExecutionException("Error opening status server", e)
        }
    }
}
