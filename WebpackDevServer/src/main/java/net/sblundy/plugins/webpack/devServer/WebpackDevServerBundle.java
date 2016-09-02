package net.sblundy.plugins.webpack.devServer;

import com.intellij.CommonBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.util.ResourceBundle;

/**
 */
public class WebpackDevServerBundle {
    private static final String BUNDLE_NAME = "net.sblundy.plugins.webpack.devServer.WebpackDevServer";

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    public static String message(@NotNull @PropertyKey(resourceBundle = BUNDLE_NAME) String key, @NotNull Object... params) {
        return CommonBundle.message(getBundle(), key, params);
    }

    @NotNull
    private static ResourceBundle getBundle() {
        return BUNDLE;
    }
}
