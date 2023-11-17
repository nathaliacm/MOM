package View;

import javax.swing.*;
import java.awt.*;

public class ClientView extends JFrame {

    public ClientView() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientView());
    }
}

