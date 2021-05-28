import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class GUITOTAL {
    private JPanel panel1;
    private JPanel PanelQuery;
    private JPanel PanelBanco;
    private JLabel LebelTitle;
    private JTextField NomeTextField;
    private JTextField UserTextField;
    private JPasswordField passwordField1;
    private JTextField PortaTextField;
    private JTextField ServidorTextField;
    private JButton buttonBanco;
    private JLabel NomeLabel;
    private JLabel UserLabel;
    private JLabel SenhaLabel;
    private JLabel ServidorLabel;
    private JLabel PortaLabel;
    private JLabel TitleLabel2;
    private JTextField textField1;
    private JButton OKButton;
    private JLabel ResultLabel;

    public GUITOTAL() {
        buttonBanco.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Database.connect(NomeTextField.getText(), UserTextField.getText(), passwordField1.getText(), ServidorTextField.getText(), PortaTextField.getText());
                    buttonBanco.setText("Recarregar Banco");
                } catch (SQLException throwables) {
                    buttonBanco.setText("ERRO DOENTE BURRO SABE NEM COLOCAR OS DADO DEMENTE");
                }
            }
        });
        OKButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ResultLabel.setText("Criando tabelas...");
                String text = textField1.getText();
                try {
                    ArrayList<String> entidades = Database.run(text);
                    ResultLabel.setText("Tabela: "+entidades.toString()+" Criada");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GUITOTAL");
        frame.setPreferredSize(new Dimension(500, 300));
        frame.setContentPane(new GUITOTAL().panel1);
        frame.pack();
        frame.setVisible(true);
    }
}


