import javax.swing.*;
import java.awt.*;


public class test extends JFrame {
    public test() {

        this.setTitle("계산기");
        this.setSize(300, 400);

        this.setLayout(new BorderLayout());
        JPanel p1 = new JPanel();

        JTextField result_show = new JTextField("1");
        JTextField result = new JTextField("0");



        add(p1, BorderLayout.NORTH);

        p1.setLayout(new BorderLayout());

        result_show.setEditable(false);
        result.setEditable(false);

        result_show.setFont(new Font("Arial", Font.PLAIN, 25));
        result.setFont(new Font("Arial", Font.PLAIN, 30));
        result_show.setHorizontalAlignment(JTextField.RIGHT);
        result.setHorizontalAlignment(JTextField.RIGHT);

        p1.add(result_show,BorderLayout.NORTH);
        p1.add(result,BorderLayout.CENTER);



        JPanel p2 = new JPanel();
        p2.setLayout(new GridLayout(6 ,4 ,2 ,2));

        String[] a={"%","CE","C","←"
                ,"1/x","x²","√x","÷"
                ,"7","8","9","x"
                ,"4","5","6","-"
                ,"1","2","3","+"
                ,"( )","0",".","="};
        JButton[] b= new JButton[24];

        for(int i=0; i<24; i++) {
            b[i]=new JButton(a[i]);
            b[i].setSize(3,3);


            p2.add(b[i]);

        }
        add(p2, BorderLayout.CENTER);


        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] argv) {
        new test();
    }
}
