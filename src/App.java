import javax.swing.*;

public class App extends JFrame {

    private JPanel MainPanel;

    public App(String title) {
        super(title);

        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Shed");
        frame.setLocationRelativeTo(null); // Makes the panel centered
        frame.setVisible(true);
    }
}
