package net.sblundy.plugins.webpack.devServer.server;

/**
 */
public interface CompileStatusListener {
    void onInvalidated();
    void onStart();
    void onProgress(double progress, String message);
    void onComplete(Boolean errors);
    void onFailure();
}
