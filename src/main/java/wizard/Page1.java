package wizard;

import main.MainClass;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Page1 extends MainClass {
    public JButton nextButton;
    public JPanel page1MainPanel;
    public JButton quitButton;
    private JLabel imageLabel;

    public static JFrame frame = new JFrame("Automation testing");

    public Page1() {

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Page2 page2OBJ = new Page2();
                page2OBJ.page2View();
                frame.setVisible(false);

            }
        });
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.exit(0);

            }
        });

    }

    public void page1View() {

        frame.setContentPane(new Page1().page1MainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    private void createUIComponents() {

        ImageIcon imgThisImg = new ImageIcon(imageWizardPath);
        imageLabel = new JLabel(imgThisImg);

    }
}
