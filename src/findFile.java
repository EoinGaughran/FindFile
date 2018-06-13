import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class findFile {

    File file;

    public static void main(String[] args) {

        String userHome = System.getProperty("user.home");
        String desktop = userHome + "\\Desktop";
        String pictures = userHome + "\\Pictures";
        String salad = userHome + "\\saladoats";

        int filesChecked = 0;
        int filesFound = 0;

        File cDirectory = new File("C:\\");
        File dDirectory = new File("D:\\");
        File eDirectory = new File("E:\\");
        File fDirectory = new File("F:\\");

        //System.out.println(pictures);
        File folder = new File(pictures);
        File saladFolder = new File(salad);
        File[] listOfFiles;
        List<File> directories = new ArrayList<>();
        //directories.add(folder);
        //directories.add(saladFolder);
        directories.add(cDirectory);
        directories.add(dDirectory);
        directories.add(eDirectory);
        directories.add(fDirectory);

        try{

            for(int i = 0; i < directories.size(); i++) {

                listOfFiles = directories.get(i).listFiles();

                if(listOfFiles == null); //System.out.println(directories.get(i) + ": Empty list");

                else {
                    for (File listOfFile : listOfFiles) {

                        String fileName = listOfFile.getName();
                        if (listOfFile.isFile()) {

                            if (fileName.toLowerCase().contains("pawg")) {

                                System.out.println("\r\n"+listOfFile);
                                filesFound++;
                            }

                            filesChecked++;
                            System.out.print("\rFiles Checked: " + filesChecked + " Files Found: " + filesFound);

                        } else if (listOfFile.isDirectory()) {
                            //System.out.println("Found Directory");
                            directories.add(listOfFile);
                            if (fileName.toLowerCase().contains("pawg")) {

                                System.out.println("\r\nDirectory Found: "+listOfFile);
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException e){

            System.out.println("error: " + e);
        }
    }

    private File createSampleFile() throws IOException {
        file = File.createTempFile("aws-java-sdk-", ".txt");
        file.deleteOnExit();

        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write("abcdefghijklmnopqrstuvwxyz\n");
        writer.write("01234567890112345678901234\n");
        writer.write("!@#$%^&*()-=[]{};':',.<>/?\n");
        writer.write("01234567890112345678901234\n");
        writer.write("abcdefghijklmnopqrstuvwxyz\n");
        writer.close();

        return file;
    }
}
