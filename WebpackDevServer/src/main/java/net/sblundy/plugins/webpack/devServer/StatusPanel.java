package net.sblundy.plugins.webpack.devServer;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.JBColor;
import com.intellij.ui.JBProgressBar;
import com.intellij.ui.components.JBLabel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 */
public class StatusPanel extends JPanel {
    private static final Logger LOGGER = Logger.getInstance(StatusPanel.class);

    private static final JBColor GREEN = new JBColor( new Color(89, 161, 95), new Color(58, 125, 63));

    private final JBLabel statusLabel;
    private final JProgressBar progressBar;
    private final JPanel statusPanel;

    public StatusPanel() {
        super(new VerticalFlowLayout());
        this.progressBar = new JBProgressBar();
        this.progressBar.setMinimum(0);
        this.progressBar.setMaximum(100);

        this.statusLabel = new JBLabel();

        this.statusPanel = new JPanel();
        this.statusPanel.setBorder(new LineBorder(JBColor.foreground(), 1));
        this.statusPanel.add(this.statusLabel);
        add(new JBLabel(WebpackDevServerBundle.message("status.title")));
        add(this.progressBar);
        add(this.statusPanel);
    }

    public void setProgress(String message, double progress) {
        SwingUtilities.invokeLater(() -> {
            setStatus(message, MessageType.INFO.getPopupBackground(), MessageType.INFO.getTitleForeground());
            progressBar.setValue((int)(100 * progress));
        });
    }

    private void setStatus(String message, Color background, Color foreground) {
        statusLabel.setText(message);
        statusPanel.setBackground(background);
        statusPanel.setBorder(new LineBorder(foreground, 1));
        statusLabel.setForeground(foreground);
    }

    public void setErrorMessage(String message) {
        SwingUtilities.invokeLater(() -> setStatus(message, MessageType.ERROR.getPopupBackground(), MessageType.ERROR.getTitleForeground()));
    }

    public void setWarnMessage(String message) {
        SwingUtilities.invokeLater(() -> setStatus(message, MessageType.WARNING.getPopupBackground(), MessageType.WARNING.getTitleForeground()));
    }

    public void setSuccessMessage(String message) {// 46, 132, 34
        SwingUtilities.invokeLater(() -> setStatus(message, GREEN, JBColor.black));
    }

    public void setMessage(String message) {
        SwingUtilities.invokeLater(() -> setStatus(message, JBColor.background(), JBColor.foreground()));
    }
}
