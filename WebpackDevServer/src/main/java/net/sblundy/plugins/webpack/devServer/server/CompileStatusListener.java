package net.sblundy.plugins.webpack.devServer.server;

import java.util.List;

/**
 */
public interface CompileStatusListener {
    void onInvalidated();
    void onStart();
    void onProgress(double progress, String message);
    void onComplete(List<Asset> assets, List<String> errorMessages);
    void onFailure();
}
