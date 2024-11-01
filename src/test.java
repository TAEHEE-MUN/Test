import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 계산기 클래스.
 * 이 클래스는 Java Swing 사용하여 간단한 계산기를 생성합니다.
 * 기본 사칙연산, 제곱, 제곱근 및 음수 변환 기능을 제공합니다.
 * /@see https://chatgpt.com/
 */
public class test extends JFrame {
    JTextField result_show;    // 결과 표시
    JTextField result_process; // 계산 과정
    JTextField result;         // 입력 필드

    /**
     * 계산기 생성자.
     * UI 구성 요소를 초기화하고 설정합니다.
     */
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
        result_show.setEditable(false); // 결과 필드는 읽기 전용
        result_process.setEditable(false); // 계산 과정 필드도 읽기 전용
        result_show.setFont(new Font("Arial", Font.PLAIN, 25));
        result_process.setFont(new Font("Arial", Font.PLAIN, 20));
        result.setFont(new Font("Arial", Font.PLAIN, 45));

        // 입력 필드 정렬 설정(오른쪽 정렬)
        result_show.setHorizontalAlignment(JTextField.RIGHT);
        result_process.setHorizontalAlignment(JTextField.RIGHT);
        result.setHorizontalAlignment(JTextField.RIGHT);

        // 패널에 필드 추가
        p1.add(result_show, BorderLayout.NORTH);
        p1.add(result_process, BorderLayout.CENTER);
        p1.add(result, BorderLayout.SOUTH);
        add(p1, BorderLayout.NORTH);

        // 버튼 패널 설정
        JPanel p2 = new JPanel();
        p2.setLayout(new GridLayout(6, 4, 2, 2));

        // 버튼 배열 정의
        String[] buttons = {
                "%", "CE", "C", "←",
                "1/x", "x²", "√x", "/",
                "7", "8", "9", "*",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "+/-", "0", ".", "="
        };

        JButton[] b = new JButton[buttons.length];

        // 버튼 색상 설정
        Color color = new Color(222, 251, 222);
        Color color2 = new Color(255, 127, 80);
        Color color3 = new Color(255, 255, 224);

        for (int i = 0; i < buttons.length; i++) {
            b[i] = new JButton(buttons[i]);
            p2.add(b[i]);

            final String buttonText = buttons[i];
            b[i].addActionListener(e -> handleButtonClick(buttonText)); // 버튼 클릭 시 이벤트 처리
            b[i].setBackground(color);
        }
            // 숫자 버튼 색상 설정
        for (int i = 8; i < 20; i++) {
            if (i % 4 != 3) {
                b[i].setForeground(color2);
            }
        }
        b[21].setForeground(color2);
        p2.setBackground(color3);

        add(p2, BorderLayout.CENTER);
        this.setVisible(true); // 프레임을 화면에 표시
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 프레임 종료 시 프로그램 종료
    }

    /**
     * 버튼 클릭 이벤트 핸들러.
     * 사용자가 클릭한 버튼에 따라 적절한 동작을 수행합니다.
     *
     * @param buttonText 클릭된 버튼의 텍스트
     */
    private void handleButtonClick(String buttonText) {
        String currentText = result.getText(); // 입력 필드의 텍스트

        switch (buttonText) {
            case "+/-":
                toggleLastNumberSign(); // 부호 반전
                break;
            case "CE":
                result_process.setText(""); // 계산 과정 초기화
                break;
            case "C":
                result.setText(""); // 모든 필드 초기화
                result_show.setText("");
                result_process.setText("");
                break;
            case "←":
                if (!currentText.isEmpty()) {
                    result.setText(currentText.substring(0, currentText.length() - 1)); // 마지막 문자 삭제(뒤로가기)
                }
                break;
            case "=":
                String expression = result_process.getText() + result.getText(); // 전체 수식
                double resultValue = evaluateExpression(expression); // 수식 평가
                if (Double.isNaN(resultValue) || Double.isInfinite(resultValue)) {
                    result_show.setText("Error"); // 오류 메시지 표시
                } else {
                    result_show.setText(String.valueOf(resultValue)); // 결과 표시
                    result_process.setText(expression); // 계산 과정 표시
                }
                result.setText("");
                break;
            case "1/x":
                // 역수 계산
                if (!currentText.isEmpty()) {
                    double value = 1.0 / Double.parseDouble(currentText); // 역수 계산
                    result_show.setText(String.valueOf(value));
                    result_process.setText("1/" + currentText);
                    result.setText("");
                }
                break;
            case "x²":
                // 제곱 처리
                if (!currentText.isEmpty()) {
                    result_process.setText(result_process.getText() + "(" + currentText + "²)");
                    result_show.setText("");
                    result.setText("");
                }
                break;
            case "√x":
                // 제곱근 계산
                if (!currentText.isEmpty()) {
                    double sqrtValue = Math.sqrt(Double.parseDouble(currentText)); // 제곱근 계산
                    result_show.setText(String.valueOf(sqrtValue));
                    result_process.setText(result_process.getText() + "√" + currentText);
                    result.setText("");
                }
                break;
            default:
                // 연산자 처리
                if (isOperator(buttonText) && isOperator(currentText)) {
                    return; // 연산자 중복 방지
                }
                if (isOperator(buttonText)) {
                    String expression1 = result_process.getText() + result.getText(); // 현재 수식
                    double resultValue2 = evaluateExpression(expression1); // 수식 평가
                    result_process.setText(result_process.getText() + currentText + buttonText);
                    result_show.setText(String.valueOf(resultValue2));
                    result.setText("");
                } else {
                    result.setText(currentText + buttonText); // 숫자 입력
                }
                break;
        }
    }

    /**
     * 입력 필드의 마지막 숫자의 부호를 반전시킵니다.
     */
    private void toggleLastNumberSign() {
        String currentText = result.getText(); // 현재 입력 필드의 텍스트
        if (!currentText.isEmpty()) {
            try {
                double value = Double.parseDouble(currentText); // 현재 숫자 가져오기
                value = -value; // 부호 반전
                result.setText(String.valueOf(value)); // 결과 설정
            } catch (NumberFormatException e) {
                // 숫자가 아닌 경우 무시
            }
        }
    }

    /**
     * 수식을 평가하여 결과를 반환합니다.
     *
     * @param expression 평가할 수식
     * @return 계산 결과
     */
    private double evaluateExpression(String expression) {
        try {
            List<Double> values = new ArrayList<>(); // 숫자 목록
            List<String> operators = new ArrayList<>(); // 연산자 목록

            StringBuilder currentNumber = new StringBuilder(); // 현재 숫자
            boolean expectNumber = true;  // 다음 토큰은 숫자

            for (int i = 0; i < expression.length(); i++) {
                char c = expression.charAt(i); // 현재 문자

                if (c == '-' && expectNumber) {
                    currentNumber.append(c); // 음수 처리
                    expectNumber = false;
                } else if (Character.isDigit(c) || c == '.') {
                    currentNumber.append(c); // 숫자 처리
                    expectNumber = false;
                } else if (c == '²') {
                    // 제곱 처리
                    double base = Double.parseDouble(currentNumber.toString()); // 현재 숫자
                    values.add(Math.pow(base, 2)); // values 추가 제곱 계산
                    currentNumber.setLength(0); // 현재 숫자 초기화
                    expectNumber = true; // 다음에 숫자를 기대
                } else if (c == '√') {
                    if (currentNumber.length() > 0) {
                        double base = Double.parseDouble(currentNumber.toString());
                        values.add(Math.sqrt(base)); // 현재 숫자의 제곱근을 계산
                        result_process.setText(result_process.getText() + "√" + base);
                        currentNumber.setLength(0);
                    }
                    expectNumber = true;
                } else if (isOperator(String.valueOf(c))) {
                    if (currentNumber.length() > 0) {
                        values.add(Double.parseDouble(currentNumber.toString()));
                        currentNumber.setLength(0);
                    }
                    operators.add(String.valueOf(c)); // 연산자 추가
                    expectNumber = true;
                }
            }

            if (currentNumber.length() > 0) {
                values.add(Double.parseDouble(currentNumber.toString())); // 마지막 숫자 추가
            }

            if (values.isEmpty() || operators.size() != values.size() - 1) {
                return Double.NaN; // 잘못된 수식 처리
            }

            // 곱셈과 나눗셈을 먼저 처리
            for (int i = 0; i < operators.size(); i++) {
                String operator = operators.get(i);
                if (operator.equals("*") || operator.equals("/") || operator.equals("%")) {
                    double leftValue = values.get(i); // 왼쪽 값
                    double rightValue = values.get(i + 1); // 오른쪽 값
                    double result = 0;

                    switch (operator) {
                        case "*":
                            result = leftValue * rightValue; // 곱셈
                            break;
                        case "/":
                            if (rightValue != 0) {
                                result = leftValue / rightValue; // 나눗셈
                            } else {
                                return Double.NaN; // 0으로 나누는 경우
                            }
                            break;
                        case "%":
                            result = leftValue % rightValue; // 나머지 연산
                            break;
                    }

                    values.set(i, result); // 결과 업데이트
                    values.remove(i + 1); // 오른쪽 값 제거
                    operators.remove(i); // 연산자 제거
                    i--; // 제거 후 인덱스 조정
                }
            }

            // 덧셈과 뺄셈을 처리
            double finalResult = values.get(0); // 초기값 설정
            for (int i = 0; i < operators.size(); i++) {
                String operator = operators.get(i);
                double nextValue = values.get(i + 1); // 다음 값

                switch (operator) {
                    case "+":
                        finalResult += nextValue; // 덧셈
                        break;
                    case "-":
                        finalResult -= nextValue; // 뺄셈
                        break;
                }
            }

            return finalResult; // 최종 결과 반환

        } catch (Exception e) {
            return Double.NaN; // 오류 발생 시 NaN 반환
        }
    }

    /**
     * 주어진 문자열이 연산자인지 확인합니다.
     *
     * @param token 확인할 문자열
     * @return 연산자일 경우 true, 아니면 false
     */
    private boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("%");
    }

    /**
     * 프로그램의 시작점.
     * 계산기 GUI 실행합니다.
     *
     * @param argv 명령줄 인수
     */
    public static void main(String[] argv) {
        new test(); // 계산기 인스턴스 생성 및 실행
    }
}

