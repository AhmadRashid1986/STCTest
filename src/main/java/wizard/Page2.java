package wizard;

import main.MainClass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Page2 extends MainClass {
    public JPanel page2MainPanel;
    public JButton nextButton;
    public JButton quitButton;
    public JComboBox browserList;
    public JComboBox countryLists;
    public JLabel message1;
    private JLabel imageLabel;

    public static JFrame frame = new JFrame("Automation testing");

    public Page2() {

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                frame.setVisible(false);
                browserType = browserList.getSelectedItem().toString();
                selectedCountry = countryLists.getSelectedItem().toString();

                String[] listOfVariables = {browserType, selectedCountry};

                Boolean isValidArray = true;

                for (int i = 0; i < listOfVariables.length; i++) {

                    isValidArray = isValidArray && !listOfVariables[i].isEmpty();

                }

                if (!isValidArray) {

                    message1.setForeground(Color.RED);

                } else {

                    frame.setVisible(false);
                    synchronized (currentThread) {

                        currentThread.notify();

                    }

                    try {
                        wizardFinished = true;
                        wizardThread.interrupt();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }

                }

            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.exit(0);

            }
        });

    }

    private void createUIComponents() {

        browserList = new JComboBox(browserSelection);
        countryLists = new JComboBox(countryList);
        ImageIcon imgThisImg = new ImageIcon(imageWizardPath);
        imageLabel = new JLabel(imgThisImg);

    }

    public void page2View() {

        frame.setContentPane(new Page2().page2MainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
