import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class findFileGui extends JFrame{

    private int filesCheckedNumber = 0;
    private int filesFoundNumber = 0;
    private JLabel filesChecked;
    private JLabel filesFound;

    private String [] drives;

    private Thread worker;

    public static void main(String[] args) {

        new findFileGui();
    }

    private int xSize = 800;
    private int ySize = 500;

    private JTextField tf;
    private JTextArea display;

    private findFileGui(){

        JFrame f = new JFrame("File Finder");

        JPanel topPanel = new JPanel();
        topPanel.setBorder ( new TitledBorder ( new EtchedBorder (), "Parameters" ) );
        topPanel.setBounds(8,0, xSize-50, ySize-400);
        topPanel.setLayout(null);

        final JLabel enterName = new JLabel("Enter file name:");
        enterName.setBounds(30,30, 300, 30);
        topPanel.add(enterName);

        tf = new JTextField();
        tf.setBounds(125, 35,150,25);
        topPanel.add(tf);

        final JLabel chooseDrive = new JLabel("Choose drive: ");
        chooseDrive.setBounds(300, 30, 300, 30);
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
        driveList.setBounds(390, 30, 80, 30);
        topPanel.add(driveList);

        final JButton b = new JButton("Search");
        b.setBounds(xSize-170,60,100,30);
        topPanel.add(b);

        filesChecked = new JLabel();
        filesChecked.setBounds(30, 60, 300, 30);
        filesChecked.setVisible(false);
        topPanel.add(filesChecked);

        filesFound = new JLabel();
        filesFound.setBounds(300, 60, 300, 30);
        filesFound.setVisible(false);
        topPanel.add(filesFound);

        /**********************/

        final JLabel credit = new JLabel("<html><div align=right width=100px>Version 0.1<br/>by Oats Gdankie</div></html>");
        credit.setBounds(xSize-190, -25, 150, 100);
        topPanel.add(credit);

        JPanel middlePanel = new JPanel ();
        middlePanel.setBorder ( new TitledBorder ( new EtchedBorder (), "Results" ) );
        middlePanel.setBounds(8, ySize-400, xSize-50, 350);

        final JButton pause = new JButton("Pause");
        pause.setEnabled(false);
        middlePanel.add(pause);

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

        b.addActionListener(e -> {

            filesChecked.setVisible(true);
            filesFound.setVisible(true);
            pause.setEnabled(true);
            stop.setEnabled(true);
            restart.setEnabled(true);
            b.setEnabled(false);

            worker = new Thread(() -> {

                while(!worker.isInterrupted()) {

                    findFile(tf.getText(), driveList.getItemAt(driveList.getSelectedIndex()).toString());
                }

                    SwingUtilities.invokeLater(() -> display.append("\nDone!"));

            });
            worker.start();
        });

        stop.addActionListener(e -> worker.interrupt());

        f.add(middlePanel);
        f.add(topPanel);
        f.setSize(xSize,ySize);
        f.setLocationRelativeTo(null);
        f.setLayout(null);
        f.setVisible(true);
        f.setResizable(false);

        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void findFile(String param, String drive){

        List<File> directories = new ArrayList<>();
        File[] listOfFiles;

        if(drive.equals("All")) for(String mDrive : drives) directories.add(new File(mDrive));

        else directories.add(new File(drive));

        if (param.equals("")) display.setText("Enter a filename");
        else {
            try {

                for (int i = 0; i < directories.size(); i++) {

                    listOfFiles = directories.get(i).listFiles();

                    if (listOfFiles != null) {
                        for (File listOfFile : listOfFiles) {
                            if (listOfFile.isFile()) {

                                String fileName = listOfFile.getName();
                                if (fileName.toLowerCase().contains(param)) {

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
                            }
                        }
                    }
                }
            } catch (NullPointerException e) {

                display.append("Error: " + e);
            }
        }
    }
}