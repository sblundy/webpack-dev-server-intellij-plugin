package net.sblundy.plugins.webpack.devServer.server;

import com.intellij.openapi.Disposable;

/**
 */
public interface ServerStatusMonitor extends Disposable {
    int getPort();
    void addListener(CompileStatusListener listener);

    enum ServerStatus {
        NONE,
        COMPLETE,
        INVALIDATED,
        FAILED,
        COMPILING
    }

    enum ConnectionStatus {
        NONE,
        LISTENING,
        ERROR
    }
}
