import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class test extends JFrame {
    JTextField result_show;    // 결과 표시
    JTextField result_process; // 계산 과정
    JTextField result;         // 입력 필드

    public test() {
        this.setTitle("계산기");
        this.setSize(300, 400);
        this.setLayout(new BorderLayout());

        // 패널 및 텍스트 필드 설정
        JPanel p1 = new JPanel();
        result_show = new JTextField("");
        result_process = new JTextField("");
        result = new JTextField("");

        p1.setLayout(new BorderLayout());
        result_show.setEditable(false);
        result_process.setEditable(false);
        result_show.setFont(new Font("Arial", Font.PLAIN, 25));
        result_process.setFont(new Font("Arial", Font.PLAIN, 20));
        result.setFont(new Font("Arial", Font.PLAIN, 45));

        result_show.setHorizontalAlignment(JTextField.RIGHT);
        result_process.setHorizontalAlignment(JTextField.RIGHT);
        result.setHorizontalAlignment(JTextField.RIGHT);

        p1.add(result_show, BorderLayout.NORTH);
        p1.add(result_process, BorderLayout.CENTER);
        p1.add(result, BorderLayout.SOUTH);
        add(p1, BorderLayout.NORTH);

        // 버튼 패널 설정
        JPanel p2 = new JPanel();
        p2.setLayout(new GridLayout(6, 4, 2, 2));

        String[] buttons = {
                "%", "CE", "C", "←",
                "1/x", "x²", "√x", "/",
                "7", "8", "9", "*",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "+/-", "0", ".", "="
        };

        JButton[] b = new JButton[buttons.length];

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
        String currentText = result.getText();

        switch (buttonText) {
            case "+/-":
                toggleLastNumberSign();
                break;
            case "CE":
                result.setText(""); // 입력 필드 지우기
                break;
            case "C":
                result.setText(""); // 모든 필드 초기화
                result_show.setText("");
                result_process.setText("");
                break;
            case "←":
                if (!currentText.isEmpty()) {
                    result.setText(currentText.substring(0, currentText.length() - 1));
                }
                break;
            case "=":
                String expression = result_process.getText() + result.getText();
                double resultValue = evaluateExpression(expression);
                if (Double.isNaN(resultValue) || Double.isInfinite(resultValue)) {
                    result_show.setText("Error"); // 오류 메시지 표시
                } else {
                    result_show.setText(String.valueOf(resultValue));
                    result_process.setText(expression + " = " + resultValue);
                }
                result.setText(""); // 입력 필드 비우기
                break;
            default:
                if (isOperator(buttonText) && isOperator(currentText)) {
                    return; // 연산자 중복 방지
                }
                if (isOperator(buttonText)) {
                    String expression1 = result_process.getText() + result.getText();
                    double resultValue2 = evaluateExpression(expression1);
                    result_process.setText(result_process.getText() + currentText + buttonText);
                    result_show.setText(String.valueOf(resultValue2));
                    result.setText("");
                } else {
                    result.setText(currentText + buttonText);
                }
                break;
        }
    }

    private void toggleLastNumberSign() {
        String currentText = result.getText();
        if (!currentText.isEmpty()) {
            try {
                double value = Double.parseDouble(currentText);
                value = -value;
                result.setText(String.valueOf(value)); // 부호 반전된 값으로 설정
            } catch (NumberFormatException e) {
                // 숫자가 아닌 경우 무시
            }
        }
    }

    private double evaluateExpression(String expression) {
        try {
            List<Double> values = new ArrayList<>();
            List<String> operators = new ArrayList<>();

            StringBuilder currentNumber = new StringBuilder();
            boolean expectNumber = true;  // 다음에 숫자가 나와야 하는지 여부

            for (int i = 0; i < expression.length(); i++) {
                char c = expression.charAt(i);

                if (c == '-' && expectNumber) {
                    // 숫자 앞의 '-' 기호를 음수로 처리
                    currentNumber.append(c);
                    expectNumber = false;
                } else if (Character.isDigit(c) || c == '.') {
                    currentNumber.append(c);
                    expectNumber = false;
                } else if (isOperator(String.valueOf(c))) {
                    values.add(Double.parseDouble(currentNumber.toString()));
                    operators.add(String.valueOf(c));
                    currentNumber.setLength(0);  // 현재 숫자 초기화
                    expectNumber = true;  // 다음에 숫자가 오기를 기대
                }
            }

            if (currentNumber.length() > 0) {
                values.add(Double.parseDouble(currentNumber.toString()));
            }

            if (values.isEmpty() || operators.size() != values.size() - 1) {
                return Double.NaN;
            }

            for (int i = 0; i < operators.size(); i++) {
                String operator = operators.get(i);
                if (operator.equals("*") || operator.equals("/")) {
                    double leftValue = values.get(i);
                    double rightValue = values.get(i + 1);
                    double result = 0;

                    switch (operator) {
                        case "*":
                            result = leftValue * rightValue;
                            break;
                        case "/":
                            if (rightValue != 0) {
                                result = leftValue / rightValue;
                            } else {
                                return Double.NaN;
                            }
                            break;
                    }

                    values.set(i, result);
                    values.remove(i + 1);
                    operators.remove(i);
                    i--;
                }
            }

            double finalResult = values.get(0);
            for (int i = 0; i < operators.size(); i++) {
                String operator = operators.get(i);
                double nextValue = values.get(i + 1);

                switch (operator) {
                    case "+":
                        finalResult += nextValue;
                        break;
                    case "-":
                        finalResult -= nextValue;
                        break;
                }
            }
            return finalResult;
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    private boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("%");
    }

    public static void main(String[] argv) {
        new test(); // 계산기 GUI 실행
    }
}