package net.sblundy.plugins.webpack.devServer.server;

import com.intellij.execution.ExecutionException;
import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreterRef;
import net.sblundy.plugins.webpack.devServer.WebpackDevServerRunConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 */
public interface Server {
    @NotNull
    File getScriptFile();

    File getLibraryCache(NodeJsInterpreterRef nodeInterpreterRef) throws ExecutionException;
}
