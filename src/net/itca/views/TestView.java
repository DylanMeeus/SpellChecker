package net.itca.views;

import net.itca.checker.SpellService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dylan on 19.01.18.
 */
public class TestView {

    private JPanel mainPanel;
    private JList spellingMistakeList;
    private SpellService service = SpellService.getSpellService();
    public TestView(){
        setupUI();
    }

    private void setupUI(){
        JFrame frame = new JFrame("Spelling tester");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        mainPanel = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea(20, 10);
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            private int prevSpaceIndex = 0;
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                checkSpelling();
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                checkSpelling();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                checkSpelling();
            }

            private void checkSpelling(){
                // this should be a regex
                // actually determining when a user stopped typing should be done better..
                String[] words = textArea.getText().split(" ");
                int lastSpaceIndex = textArea.getText().lastIndexOf(" ");
                if (lastSpaceIndex == prevSpaceIndex) {
                    return; // save a bit of performance, don't check every character typed if there's no new word..
                }
                System.out.println("checking the spelling!");
                prevSpaceIndex = words.length;
                java.util.List<String> mistakes = Arrays.stream(words)
                        .filter(w -> !service.correctSpelling(w.toLowerCase()))

                        .collect(Collectors.toList());
                updateMistakeList(mistakes);
            }
        });
        mainPanel.add(textArea,BorderLayout.CENTER);

        JPanel mistakesPanel = new JPanel(new BorderLayout());
        mistakesPanel.add(new JLabel("Mistakes"), BorderLayout.NORTH);
        spellingMistakeList = new JList();
        mistakesPanel.add(spellingMistakeList, BorderLayout.CENTER);
        mainPanel.add(mistakesPanel, BorderLayout.EAST);
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    private void updateMistakeList(List<String> mistakes) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String mistake : mistakes) {
            listModel.addElement(mistake);
        }
        spellingMistakeList.setModel(listModel);
        mainPanel.revalidate();
    }
}
