import javax.swing.*;
import java.awt.*;

public class test extends JFrame {
    JTextField result_show;
    JTextField result;

    public test() {
        this.setTitle("계산기");
        this.setSize(300, 400);
        this.setLayout(new BorderLayout());

        JPanel p1 = new JPanel();
        result_show = new JTextField("");
        result = new JTextField("");

        p1.setLayout(new BorderLayout());
        result_show.setEditable(false);
        result_show.setFont(new Font("Arial", Font.PLAIN, 25));
        result.setFont(new Font("Arial", Font.PLAIN, 45));
        result_show.setForeground(Color.lightGray);
        result_show.setHorizontalAlignment(JTextField.RIGHT);
        result.setHorizontalAlignment(JTextField.RIGHT);

        p1.add(result_show, BorderLayout.NORTH);
        p1.add(result, BorderLayout.CENTER);
        add(p1, BorderLayout.NORTH);

        JPanel p2 = new JPanel();
        p2.setLayout(new GridLayout(6, 4, 2, 2));

        String[] buttons = {
                "%", "CE", "C", "←",
                "1/x", "x²", "√x", "÷",
                "7", "8", "9", "x",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "+/-", "0", ".", "="
        };

        JButton[] b = new JButton[24];

        for (int i = 0; i < buttons.length; i++) {
            b[i] = new JButton(buttons[i]);
            p2.add(b[i]);


                final String buttonText = buttons[i];
                 b[i].addActionListener(e -> handleButtonClick(buttonText));

            }

        add(p2, BorderLayout.CENTER);

        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void handleButtonClick(String buttonText) {
        switch (buttonText) {
            case "+/-":
                if (!result.getText().isEmpty()) {
                    double currentValue = Double.parseDouble(result.getText());
                    result.setText(String.valueOf(-currentValue).replace(".0", ""));
                }
                break;
            case "CE":
                result.setText("");
                break;
            case "C":
                result.setText("");
                result_show.setText("");
                break;
            case "←":
                String currentText = result.getText();
                if (!currentText.isEmpty()) {
                    result.setText(currentText.substring(0, currentText.length() - 1));
                }
                break;
            case "=":
                break;

            default:
                result.setText(result.getText() + buttonText);
                break;
        }
    }

    public static void main(String[] argv) {
        new test();
    }
}
