package net.sblundy.plugins.webpack.devServer.server;

import com.intellij.execution.ExecutionException;
import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreterRef;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 */
public interface ServerMonitorFactory {
    @NotNull
    File getScriptFile();

    File getLibraryCache(NodeJsInterpreterRef nodeInterpreterRef) throws ExecutionException;

    ServerStatusMonitor createStatusMonitor() throws ExecutionException;
}
