package filesprocessing;
import java.io.File;
import java.io.FileFilter;

/**
 * an abstract class which is a facade of java.io.File.
 */
public class FileFacade  {

    private static final int NO_DOT_IN_FILE = -1;
    private static final String SUFFIX_INDICATORS = ".";
    private File file;

    /**
     * creates a new FileFacade instance
     * @param s - the path of the file to create
     */
    public FileFacade(String s) {
        file=new File(s);
    }

    /*
     * creates a new FileFacade instance by a File object that it gets
     * a private method that is used only in this class.
     */
    private FileFacade(File file){
        this.file=file;
    }

    /**
     * Returns the name of the file
     * @return the name of the file
     */
    public String getName() {
        return file.getName();
    }

    /**
     * Returns the length of the file
     * @return the length of the file
     */
    public long length() {
        return file.length();
    }

    /**
     * Checks whether the path represents a valid file
     * @return true if the file is a valid file, false otherwise
     */
    public boolean isFile() {
        return file.isFile();
    }

    /**
     * Checks whether the path represents a valis directory
     * @return true if the directory is valid, false otherwise
     */
    boolean isDirectory(){
        return file.isDirectory();
    }

    /**
     * Filters the files by the FileFilter
     * @param fileFilter - The filter object to filter the files
     * @return an array of filtered files
     */
    public FileFacade[] listFiles(FileFilter fileFilter){
        File[] listFiles=file.listFiles(fileFilter);
        FileFacade[] listFilesFacade;
        if (listFiles==null){//if this abstract pathname does not denote a directory, or if an I/O error occurs.
            return null;
        }
        listFilesFacade=new FileFacade[listFiles.length];
        for (int i = 0; i <listFiles.length ; i++) {
            listFilesFacade[i]=new FileFacade(listFiles[i]);
            }
        return listFilesFacade;
    }

    /**
     * Returns the absolute path of the file
     * @return - the absolute path of the file
     */
    public String getAbsolutePath(){
        return file.getAbsolutePath();
    }

    /**
     * get the fileType by last dot.
     * @return fileType
     */
    public String getType(){
       String fileType=null;
        int lastDot=file.getName().lastIndexOf(SUFFIX_INDICATORS);
        String fileName=file.getName();
        if (lastDot!= NO_DOT_IN_FILE){
        fileType=fileName.substring(++lastDot);
       }
        return fileType;
    }
}
