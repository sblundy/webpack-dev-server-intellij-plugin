package net.sblundy.plugins.webpack.devServer.run;

import com.intellij.openapi.ui.ComponentContainer;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import net.sblundy.plugins.webpack.devServer.WebpackDevServerBundle;
import net.sblundy.plugins.webpack.devServer.server.Asset;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 */
public class AssetsPanel implements ComponentContainer {
    private final JPanel outerView = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private final JBScrollPane myComponent = new JBScrollPane(outerView);

    @Override
    public JComponent getComponent() {
        return myComponent;
    }

    @Override
    public JComponent getPreferredFocusableComponent() {
        return myComponent;
    }

    @Override
    public void dispose() {

    }

    void setAssets(java.util.List<Asset> assets) {
        SwingUtilities.invokeLater(() -> {
            outerView.removeAll();

            JPanel view = new JPanel(new GridLayout(assets.size() + 1, 4, 3, 1));
            Font bold = myComponent.getFont().deriveFont(Font.BOLD);
            view.add(getTitleLabel(WebpackDevServerBundle.message("view.assets.headers.name"), bold));
            view.add(getTitleLabel(WebpackDevServerBundle.message("view.assets.headers.size"), bold));
            view.add(getTitleLabel(WebpackDevServerBundle.message("view.assets.headers.emitted"), bold));
            view.add(getTitleLabel(WebpackDevServerBundle.message("view.assets.headers.chunks"), bold));
            for (Asset asset : assets) {

                view.add(new JBLabel(asset.getName()));
                view.add(new JBLabel(WebpackDevServerBundle.message("view.assets.format.size", asset.getSize())));
                view.add(new JBLabel(asset.getEmitted() ? WebpackDevServerBundle.message("view.assets.emitted.true") : WebpackDevServerBundle.message("view.assets.emitted.false")));
                view.add(new JBLabel(StringUtils.join(asset.getChunkNames(), ", ")));
            }
            outerView.add(view);
        });
    }

    @NotNull
    private JBLabel getTitleLabel(String title, Font font) {
        JBLabel label = new JBLabel(title);
        label.setFont(font);
        return label;
    }
}
