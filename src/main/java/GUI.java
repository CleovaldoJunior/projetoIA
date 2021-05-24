import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class GUI {
    private JPanel panel1;
    private JTextField entradaTextField;
    private JButton button1;
    private JLabel resultLabel;
    private JLabel label1;

    public GUI() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = entradaTextField.getText();
                try {
                    Database.run(text);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                resultLabel.setText(text);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GUI");
        frame.setPreferredSize(new Dimension(400, 300));
        frame.setContentPane(new GUI().panel1);
        frame.pack();
        frame.setVisible(true);
    }
}
