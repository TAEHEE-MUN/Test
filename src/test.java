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

        Color color= new Color(222,251,222);
        Color color2= new Color(255,127,80);
        Color color3= new Color(255,255,224);
        for (int i = 0; i < buttons.length; i++) {
            b[i] = new JButton(buttons[i]);
            p2.add(b[i]);

            final String buttonText = buttons[i];
            b[i].addActionListener(e -> handleButtonClick(buttonText));

            b[i].setBackground(color);
        }
        for (int i = 8; i < 20; i++){
            if (i % 4 != 3) {
                b[i].setForeground(color2);
            }
        }
        b[21].setForeground(color2);
        p2.setBackground(color3);

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
                result_process.setText("");
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
                    result_process.setText(expression);
                }
                result.setText(""); // 입력 필드 비우기
                break;
            case "1/x":
                if (!currentText.isEmpty()) {
                    double value = 1.0 / Double.parseDouble(currentText);
                    result_show.setText(String.valueOf(value));
                    result_process.setText("1/" + currentText);
                    result.setText(""); // 입력 필드 비우기
                }
                break;
            case "x²":
                if (!currentText.isEmpty()) {

                    result_process.setText(result_process.getText() + "(" + currentText + "²)");
                    result_show.setText("");
                    result.setText(""); // 입력 필드 비우기
                }
                break;
            case "√x":
                if (!currentText.isEmpty()) {

                    double sqrtValue = Math.sqrt(Double.parseDouble(currentText));
                    result_show.setText("");
                    result_process.setText(result_process.getText() + sqrtValue);
                    result.setText(""); // 입력 필드 비우기

                }
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
                    if (isOperator("%")) {
                        result_show.setText("");
                    }
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
            boolean expectNumber = true;  // 다음 토큰은 숫자여야 함

            for (int i = 0; i < expression.length(); i++) {
                char c = expression.charAt(i);

                if (c == '-' && expectNumber) {
                    currentNumber.append(c);
                    expectNumber = false;
                } else if (Character.isDigit(c) || c == '.') {
                    currentNumber.append(c);
                    expectNumber = false;
                } else if (c == '²') {
                    // 제곱 처리
                    double base = Double.parseDouble(currentNumber.toString());
                    values.add(Math.pow(base, 2));
                    currentNumber.setLength(0); // 현재 숫자 초기화
                    expectNumber = true; // 다음에 숫자를 기대
                } else if (c == '√') {
                    if (currentNumber.length() > 0) {
                        double base = Double.parseDouble(currentNumber.toString());
                        values.add(Math.sqrt(base)); // 현재 숫자의 제곱근을 계산
                        result_process.setText(result_process.getText() + "√" + base); // 과정에 추가
                        currentNumber.setLength(0); // 현재 숫자 초기화
                    }
                    expectNumber = true; // 다음에 숫자를 기대
                } else if (isOperator(String.valueOf(c))) {
                    if (currentNumber.length() > 0) {
                        values.add(Double.parseDouble(currentNumber.toString()));
                        currentNumber.setLength(0);  // 현재 숫자 초기화
                    }
                    operators.add(String.valueOf(c));
                    expectNumber = true;  // 다음에 숫자를 기대
                }
            }

            if (currentNumber.length() > 0) {
                values.add(Double.parseDouble(currentNumber.toString()));
            }

            if (values.isEmpty() || operators.size() != values.size() - 1) {
                return Double.NaN;
            }

            // 곱셈과 나눗셈을 먼저 처리합니다.
            for (int i = 0; i < operators.size(); i++) {
                String operator = operators.get(i);
                if (operator.equals("*") || operator.equals("/") || operator.equals("%")) {
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
                                return Double.NaN; // 0으로 나누는 경우
                            }
                            break;
                        case "%":
                            result = leftValue % rightValue; // 나머지 연산
                            break;
                    }

                    values.set(i, result);
                    values.remove(i + 1);
                    operators.remove(i);
                    i--; // 제거 후 인덱스 조정
                }
            }

            // 이제 덧셈과 뺄셈을 처리합니다.
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
            return Double.NaN; // 오류 발생 시 NaN 반환
        }
    }


    private boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("%");
    }

    public static void main(String[] argv) {
        new test();
    }
}