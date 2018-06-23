import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class findFileGui extends JFrame{

    private long filesCheckedNumber = 0;
    private long filesFoundNumber = 0;
    private long dirsFoundNumner = 0;
    private int xSize = 800;
    private int ySize = 500;

    private JLabel filesChecked;
    private JLabel filesFound;
    private JLabel dirsFound;
    private JTextField tf;
    private JTextArea display;

    private String [] drives;

    private ArrayList<String> searchTerms = new ArrayList<>();

    private Thread worker;
    private mySwingWorker swingWorker;

    public static void main(String[] args) {

        new findFileGui();
    }

    private findFileGui(){

        JFrame f = new JFrame("File Finder");

        JPanel topPanel = new JPanel();
        topPanel.setBorder ( new TitledBorder ( new EtchedBorder (), "Parameters" ) );
        topPanel.setBounds(8,0, xSize-50, ySize-400);
        topPanel.setLayout(null);

        final JLabel enterName = new JLabel("Search Terms:");
        enterName.setBounds(30,30, 300, 30);
        topPanel.add(enterName);

        tf = new JTextField();
        tf.setBounds(125, 35,150,25);
        topPanel.add(tf);

        final JButton addTerms = new JButton("Add");
        addTerms.setBounds(280, 35,56,25);
        topPanel.add(addTerms);

        JLabel terms = new JLabel("");
        terms.setBounds(125,10, 300, 30);
        topPanel.add(terms);

        final JLabel chooseDrive = new JLabel("Choose drive: ");
        chooseDrive.setBounds(360, 30, 300, 30);
        topPanel.add(chooseDrive);

        File [] roots = File.listRoots();
        FileSystemView fsv = FileSystemView.getFileSystemView();

        ArrayList<String> options = new ArrayList<>();

        options.add("All");

        for(File root : roots){

            String isItLocal = fsv.getSystemTypeDescription(root);
            if(isItLocal.equals("Local Disk")){

                options.add(root.toString());
            }
        }

        drives = new String[options.size()];

        for(int i = 0 ; i < options.size(); i++){

            drives[i] = options.get(i);
        }

        final JComboBox driveList = new JComboBox(drives);
        driveList.setBounds(450, 30, 80, 30);
        topPanel.add(driveList);

        final JButton search = new JButton("Search");
        search.setBounds(xSize-150,60,80,30);
        topPanel.add(search);

        final JButton clear = new JButton("Clear");
        clear.setBounds(xSize-235,60,80,30);
        topPanel.add(clear);

        filesChecked = new JLabel();
        filesChecked.setBounds(30, 60, 300, 30);
        filesChecked.setVisible(false);
        topPanel.add(filesChecked);

        filesFound = new JLabel();
        filesFound.setBounds(230, 60, 300, 30);
        filesFound.setVisible(false);
        topPanel.add(filesFound);

        dirsFound = new JLabel();
        dirsFound.setBounds(400, 60, 300, 30);
        dirsFound.setVisible(false);
        topPanel.add(dirsFound);

        /**********************/

        final JLabel credit = new JLabel("<html><div align=right width=100px>Version 0.4<br/>by Eoin Gaughran</div></html>");
        credit.setBounds(xSize-190, -25, 150, 100);
        topPanel.add(credit);

        JPanel middlePanel = new JPanel ();
        middlePanel.setBorder ( new TitledBorder ( new EtchedBorder (), "Results" ) );
        middlePanel.setBounds(8, ySize-400, xSize-50, 350);

        final JButton pause = new JButton("Pause");
        pause.setEnabled(false);
        //middlePanel.add(pause);

        final JButton stop = new JButton("Stop");
        stop.setEnabled(false);
        middlePanel.add(stop);

        final JButton restart = new JButton("Restart");
        restart.setEnabled(false);
        middlePanel.add(restart);

        //middlePanel.setLayout(null);
        // create the middle panel components

        display = new JTextArea ( 16, 58 );
        display.setEditable ( false ); // set textArea non-editable
        //display.setLocation(50, ySize/2);
        JScrollPane scroll = new JScrollPane ( display );
        scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

        //Add Textarea in to middle panel
        middlePanel.add ( scroll );

        addTerms.addActionListener(e -> {

            if(!tf.getText().equals("")) {
                searchTerms.add(tf.getText());

                StringBuilder s = new StringBuilder();

                for(int i = 0; i < searchTerms.size(); i++){

                    s.append(searchTerms.get(i));
                    if(i != searchTerms.size()-1)
                        s.append("; ");
                }

                terms.setText(s.toString());
            }
            tf.setText("");

        });

        clear.addActionListener(e -> {

            tf.setText("");
            terms.setText("");
            searchTerms.clear();
            display.setText("");
        });

        search.addActionListener(e -> {

            String currentTerms = terms.getText();
            if(!(tf.getText().equals(""))) {

                searchTerms.add(tf.getText());

                if (!currentTerms.equals(""))
                    terms.setText(currentTerms + "; " + tf.getText());
                else
                    terms.setText(tf.getText());

            }

            if(!terms.getText().equals("")) {

                filesChecked.setVisible(true);
                filesFound.setVisible(true);
                dirsFound.setVisible(true);
                pause.setEnabled(true);
                stop.setEnabled(true);
                restart.setEnabled(true);
                search.setEnabled(false);
                addTerms.setEnabled(false);
                driveList.setEnabled(false);
                clear.setEnabled(false);

                filesChecked.setText("Files Checked: 0");
                filesFound.setText("Files Found: 0");
                dirsFound.setText("Folders Found: 0");

                /*worker = new Thread(() -> {

                    //while (!worker.isInterrupted()) {

                    findFile(driveList.getItemAt(driveList.getSelectedIndex()).toString());
                    //}

                    SwingUtilities.invokeLater(() -> {

                        display.append("\nDone!");
                        searchTerms.clear();
                    });

                });
                worker.start();*/

                swingWorker = new mySwingWorker(driveList.getItemAt(driveList.getSelectedIndex()).toString());
                swingWorker.execute();
            }
            else
                display.setText("Enter a Search term");

            tf.setText("");
        });

        stop.addActionListener(e -> swingWorker.cancel(true));

        pause.addActionListener(e -> swingWorker.cancel(false));

        restart.addActionListener(e -> {

            swingWorker.cancel(true);
            filesChecked.setVisible(false);
            filesFound.setVisible(false);
            dirsFound.setVisible(false);
            pause.setEnabled(false);
            stop.setEnabled(false);
            restart.setEnabled(false);
            search.setEnabled(true);
            addTerms.setEnabled(true);
            driveList.setEnabled(true);
            clear.setEnabled(true);

            searchTerms.clear();
            display.setText("");
            terms.setText("");

            filesFoundNumber = 0;
            filesCheckedNumber = 0;
            dirsFoundNumner = 0;

        });

        f.add(middlePanel);
        f.add(topPanel);
        f.setSize(xSize,ySize);
        f.setLocationRelativeTo(null);
        f.setLayout(null);
        f.setVisible(true);
        f.setResizable(false);

        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    private class mySwingWorker extends javax.swing.SwingWorker<Void, Void> {

        String drive;

        private mySwingWorker(String drive){

            this.drive = drive;
        }

        @Override
        protected Void doInBackground() {
            if (javax.swing.SwingUtilities.isEventDispatchThread()) {
                System.out.println("javax.swing.SwingUtilities.isEventDispatchThread() returned true.");
            }

            List<File> directories = new ArrayList<>();
            File[] listOfFiles;

            if(drive.equals("All")) for(String mDrive : drives) directories.add(new File(mDrive));

            else directories.add(new File(drive));

            if (searchTerms.get(0).equals("")) display.setText("Enter a filename");
            else {
                try {

                    for (int i = 0; i < directories.size(); i++) {

                        listOfFiles = directories.get(i).listFiles();

                        if (listOfFiles != null) {
                            for (File listOfFile : listOfFiles) {

                                if(isCancelled()) {
                                    directories.clear();
                                    return null;
                                }

                                if (listOfFile.isFile()) {

                                    String fileName = listOfFile.getName();
                                    if (searchTermMatchesFile(fileName.toLowerCase())) {

                                        display.append("\r\n" + listOfFile);
                                        filesFoundNumber++;
                                        filesFound.setText("Files Found: " + filesFoundNumber);
                                    }

                                    filesCheckedNumber++;
                                    //ta.append("\r\nFiles Checked: " + filesChecked + " Files Found: " + filesFound);
                                    filesChecked.setText("Files Checked: " + filesCheckedNumber);

                                } else if (listOfFile.isDirectory()) {
                                    //System.out.println("Found Directory");
                                    directories.add(listOfFile);
                                    if(searchTermMatchesFile(listOfFile.toString())) {
                                        display.append("\r\nFOLDER: " + listOfFile);
                                        dirsFoundNumner++;
                                        dirsFound.setText("Folders Found: " + dirsFoundNumner);
                                    }
                                }
                            }
                        }
                    }
                } catch (NullPointerException e) {

                    display.append("Error: " + e);
                }
            }
            return null;
        }

        @Override
        protected void done() {
            if (!javax.swing.SwingUtilities.isEventDispatchThread()) {
                System.out.println("javax.swing.SwingUtilities.isEventDispatchThread() + returned false.");
            }
        }

    }

    private boolean searchTermMatchesFile(String mFilename){

        for(String searchTerm : searchTerms) {

            if (!mFilename.contains(searchTerm))
                return false;

        }

        return true;
    }
}