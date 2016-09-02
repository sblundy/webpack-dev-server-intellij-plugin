package net.sblundy.plugins.webpack.devServer;

import com.intellij.openapi.util.IconLoader;
import javax.swing.Icon;
/**
 */
public class Icons {
    public static final Icon WebPackIcon = load("/IconSmall.png");

    private static Icon load(String path) {
        return IconLoader.getIcon(path, Icons.class);
    }
}
