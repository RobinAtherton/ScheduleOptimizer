package frontend;

import com.sun.scenario.effect.impl.sw.java.JSWBlend_COLOR_BURNPeer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Screen extends JFrame{

    JTextArea textArea;
    private int rows = 0;
    private int columns = 0;
    private JButton buttonStart = new JButton("Start");
    private JButton buttonClear = new JButton("Clear");
    private InputStream in;



    public Screen(int rows, int columns) throws IOException {
        setRows(rows);
        setColumns(columns);
        init();
    }


    private void setColumns(int columns) {
        this.columns = columns;
    }

    private void setRows(int rows) {
        this.rows = rows;
    }

    private void init() throws IOException {

        textArea = new JTextArea(rows, columns);
        textArea.setEditable(false);

        // keeps reference of standard output stream

        // re-assigns standard output stream and error output stream

        // creates the GUI
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.anchor = GridBagConstraints.WEST;

        add(buttonStart, constraints);


        constraints.gridx = 1;
        add(buttonClear, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;


        add(new JScrollPane(textArea), constraints);
        setVisible(true);
    }
    
}
