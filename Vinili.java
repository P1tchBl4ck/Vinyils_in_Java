import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * Created by De Angelis Alberto on 29/03/2017.
 * This was a school test in Java Swing where in two hours
 * I had to create a software that could be able to read and save
 * information on vinyls in a Comma Separated Value file.
 * The vinyls read could be 33 rpm, 45 rpm or 78 rpm, each of them
 * shown on a different table. The software should also feature an
 * "Add" button that gives possibility to add new vynils.
 */

public class Vinili extends JFrame {
    //Finals
    private static final char   HOTKEY_O            = 'O';
    private static final char   HOTKEY_S            = 'S';
    private static final char   HOTKEY_X            = 'X';
    private static final int    THIRTYTHREE         = "33";
    private static final int    FOURTYFIVE          = "45";
    private static final int    SEVENTYEIGHT        = "78";
    private static final String SPLITTER            = ";";
    private static final String RPM                 = " rpm";
    private static final String FILE                = "File";
    private static final String OPEN                = "Open...";
    private static final String SAVE                = "Save...";
    private static final String EXIT                = "Exit";
    private static final String AUTHOR              = "Author";
    private static final String TITLE               = "Title";
    private static final String ADD                 = "Add";
    private static final String RPM_33              = "33 rpm";
    private static final String RPM_45              = "45 rpm";
    private static final String RPM_78              = "78 rpm";
    private static final String DLG_CONFIRM_TITLE   = "File was not saved";
    private static final String DLG_CONFIRM_MESSAGE = "Save before exit?";
    //boolean needed to know whether the collection was saved or not
    private boolean             saved       = true;
    //JComponents
    private JTable              tblCenter   = null;
    private JTable              tblLeft     = null;
    private JTable              tblRight    = null;
    private JTextField          txtAuthor   = null;
    private JTextField          txtTitle    = null;
    private JComboBox<String>   cbbRounds   = null;
    //Table models (used to load data inside JTables)
    private MyTableModel        mdlCenter   = null;
    private MyTableModel        mdlLeft     = null;
    private MyTableModel        mdlRight    = null;

    private class MyTableModel extends DefaultTableModel{
    /**
     * Extended the DefaultTableModel class in order to have an
     * easy function to add the rpm wording when adding an RPM data.
     */
        public void addColumnRounds(int nRounds){
            this.addColumn("");
            this.addColumn(nRounds + RPM);
        }
    }

    public static void main(String[] args){
        //Main procedure to start a form in JSwing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Vinili();
            }
        });
    }

    private Vinili(){
        initUI();
    }

    private void initUI(){
        setDefaults();

    //Creating components

        //Building tables
        tblLeft     = new JTable();
        tblCenter   = new JTable();
        tblRight    = new JTable();
        mdlLeft     = new MyTableModel();
        mdlCenter   = new MyTableModel();
        mdlRight    = new MyTableModel();
        //Scroll panes are needed to show table column title.
        JScrollPane scrLeft     = new JScrollPane(tblLeft);
        JScrollPane scrCenter   = new JScrollPane(tblCenter);
        JScrollPane scrRight    = new JScrollPane(tblRight);
        //Creating menu bar and its items
        JMenuBar  mnbNorth  = new JMenuBar();
        JMenu     mnuFile   = new JMenu(FILE);
        JMenuItem mniOpen   = new JMenuItem(OPEN);
        JMenuItem mniSave   = new JMenuItem(SAVE);
        JMenuItem mniExit   = new JMenuItem(EXIT);
        //Building input components
        cbbRounds   = new JComboBox<>();
        txtAuthor   = new JTextField(AUTHOR);
        txtTitle    = new JTextField(TITLE);
        JButton btnAdd = new JButton(ADD);
        //JPanels are needed to set up the layout of the form
        JPanel pnlCenter    = new JPanel(new FlowLayout());
        JPanel pnlSouth     = new JPanel(new FlowLayout());
        //Creating labels
        JLabel lblAuthor    = new JLabel(AUTHOR);
        JLabel lblTitle     = new JLabel(TITLE);

    //Setting up the form

        //Adding columns to models
        mdlLeft.addColumnRounds(THIRTYTHREE);
        mdlCenter.addColumnRounds(FOURTYFIVE);
        mdlRight.addColumnRounds(SEVENTYEIGHT);
        //Setting models to tables
        tblLeft.setModel(mdlLeft);
        tblCenter.setModel(mdlCenter);
        tblRight.setModel(mdlRight);
        //Setting hotkeys for menu
        mniOpen.setMnemonic(HOTKEY_O);
        mniSave.setMnemonic(HOTKEY_S);
        mniExit.setMnemonic(HOTKEY_X);
        //Adding menu items to menu bar
        mnuFile.add(mniOpen);
        mnuFile.add(mniSave);
        mnuFile.addSeparator();
        mnuFile.add(mniExit);
        mnbNorth.add(mnuFile);
        //Adding items to combobox
        cbbRounds.addItem(RPM_33);
        cbbRounds.addItem(RPM_45);
        cbbRounds.addItem(RPM_78);
        //Adding components to panels
        pnlCenter.add(scrLeft);
        pnlCenter.add(scrCenter);
        pnlCenter.add(scrRight);
        pnlSouth.add(lblAuthor);
        pnlSouth.add(txtAuthor);
        pnlSouth.add(lblTitle);
        pnlSouth.add(txtTitle);
        pnlSouth.add(cbbRounds);
        pnlSouth.add(btnAdd);
        //Adding components to main frame
        add(mnbNorth,   BorderLayout.NORTH);
        add(pnlCenter,  BorderLayout.CENTER);
        add(pnlSouth,   BorderLayout.SOUTH);

    //Adding on click listener to buttons
        mniOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openVinyls();
            }
        });

        mniSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveVinyls();
            }
        });

        mniExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmExit();
            }
        });

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRow();
            }
        });
    }

    private void confirmExit(){
        /**
         * This function is called when the user
         * attempts to close the program.
         * Checks if the collection was saved and,
         * if not, shows a confirm dialog.
         */
        if(saved || confirmDialog())
            System.exit(0);
    }

    private boolean confirmDialog(){
        /**
         *  This function is called when the user attempted
         *  to exit and the collection was not saved.
         */ 
        JOptionPane dlgConfirm = new JOptionPane();
        //Asking user if he wants to save before exiting. 
        int ret = dlgConfirm.showConfirmDialog(this,
                                        DLG_CONFIRM_MESSAGE,
                                        DLG_CONFIRM_TITLE,
                                        JOptionPane.YES_NO_CANCEL_OPTION,
                                        JOptionPane.QUESTION_MESSAGE);
        //If he doesn't, close the program.
        if(ret == JOptionPane.NO_OPTION)
            return true;
        //If he does, show a save dialog.
        if(ret == JOptionPane.YES_OPTION)
            return saveVinyls();
        //If he cancels closing, keep the program opened.
        return false;
    }

    private void addRow(){
        /**
         * This function is called when the "Add" button is pressed.
         */
        //Claiming values from textboxes.
        String values = txtAuthor.getText() + SPLITTER + txtTitle.getText();
        //Putting values in their table basing on RPM.
        if(cbbRounds.getSelectedItem() == RPM_33)
            mdlLeft.addRow(values.split(SPLITTER));
        if(cbbRounds.getSelectedItem() == RPM_45)
            mdlCenter.addRow(values.split(SPLITTER));
        if(cbbRounds.getSelectedItem() == RPM_78)
            mdlRight.addRow(values.split(SPLITTER));
        //Informing the program that there are some unsaved changes.
        saved = false;
    }

    private boolean saveVinyls(){
        /**
         * This function is called when the "Save" button is pressed.
         */
        //Showing user a save dialog.
        JFileChooser jfc = new JFileChooser();
        int ret = jfc.showSaveDialog(this);
        if(ret == JFileChooser.CANCEL_OPTION) return false;
        File file = jfc.getSelectedFile();
        //When user select a file path, go forward to write values in it
        try {
            int y;
            PrintWriter pw = new PrintWriter(new FileWriter(file));
            //Taking values from each table and writing them,
            //separated by the splitter, in a CSV file
            for(int x = 0; x < mdlLeft.getRowCount(); x++){
                pw.print(THIRTYTHREE + SPLITTER);
                for(y = 0; y < mdlLeft.getColumnCount(); y++)
                    pw.print(mdlLeft.getValueAt(x,y) + SPLITTER);
                pw.println();
            }
            for(int x = 0; x < mdlCenter.getRowCount(); x++){
                pw.print(FOURTYFIVE + SPLITTER);
                for(y = 0; y < mdlCenter.getColumnCount(); y++)
                    pw.print(mdlCenter.getValueAt(x,y) + SPLITTER);
                pw.println();
            }
            for(int x = 0; x < mdlRight.getRowCount(); x++){
                pw.print(SEVENTYEIGHT + SPLITTER);
                for(y = 0; y < mdlRight.getColumnCount(); y++)
                    pw.print(mdlRight.getValueAt(x,y) + SPLITTER);
                pw.println();
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Informing the program that the collection was saved.
        saved = true;
        return true;
    }

    private void openVinyls(){
        /**
         * This function is called when the "Open" button is pressed.
         */
        String[] splitted;
        String[] params;
        //Showing user a file chooser to let us know which file he wants to be opened
        JFileChooser jfc = new JFileChooser();
        int ret = jfc.showOpenDialog(this);
        if(ret == JFileChooser.CANCEL_OPTION)
            return;
        File file = jfc.getSelectedFile();
        //Once we know which file it is, proceed to open it.
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while(line != null){
                splitted = line.split(SPLITTER);
                //Reading values
                params = (splitted[1] + SPLITTER + splitted[2]).split(SPLITTER);
                //Putting values inside tables basing on their RPM.
                if(Integer.parseInt(splitted[0]) == THIRTYTHREE)
                    mdlLeft.addRow(params);
                if(Integer.parseInt(splitted[0]) == FOURTYFIVE)
                    mdlCenter.addRow(params);
                if(Integer.parseInt(splitted[0]) == SEVENTYEIGHT)
                    mdlRight.addRow(params);
                line = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Informing the program that there are no unsaved changes.
        saved = true;
    }

    private void setDefaults(){
        //Setting form default values
        setTitle("Vinyls collection");
        setSize(1500, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        //Setting confirm dialog on close
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                confirmExit();   
            }
        });
    }
}
