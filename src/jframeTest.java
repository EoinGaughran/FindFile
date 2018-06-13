import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class jframeTest extends JFrame{

    private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    private int xSize = 800;
    private int ySize = 500;

    JTextArea display;

    jframeTest(){

        JPanel middlePanel = new JPanel ();
        middlePanel.setBorder ( new TitledBorder( new EtchedBorder(), "Display Area" ) );

        // create the middle panel components

        display = new JTextArea ( 16, 58 );
        display.setEditable ( false ); // set textArea non-editable
        JScrollPane scroll = new JScrollPane ( display );
        scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

        //Add Textarea in to middle panel
        middlePanel.add ( scroll );
        FileSystemView fsv = FileSystemView.getFileSystemView();

        File [] salad = File.listRoots();
        for(File salads : salad){
            String skm = fsv.getSystemTypeDescription(salads);
            if(skm.equals("Local Disk")) display.append("\r\n"+ salads.toString() + " Skum: " + fsv.getSystemTypeDescription(salads));
        }

        // My code
        JFrame frame = new JFrame ();
        frame.add ( middlePanel );
        frame.pack ();
        frame.setLocationRelativeTo ( null );
        frame.setVisible ( true );
    }

    public static void main(String[] args) {

        new jframeTest();

    }

}
