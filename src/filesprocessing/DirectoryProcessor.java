package filesprocessing;


import filesprocessing.secssionsprocessor.CurrentSecession;

import java.util.ArrayList;

/**
 * The main class. receives two arguments - the directory of files to filter&sort, and the command file.
 * If the arguments are valid, will filter and sort the files, for each section in the command file.
 */
public class DirectoryProcessor {

    private static final int MIN_NUM_OF_ARGUMENTS = 1;
    private static final int PATH_NAME_POSITION = 0;
    private static final int COMMAND_NAME_POSITION = 1;

    public static void main(String [] args) throws TypeTwoExceptions.BadFilterSectionName,
            TypeTwoExceptions.BadOrderSectionName,
            TypeTwoExceptions.IncorrentAmountOfArguments,
            TypeTwoExceptions.FileNotFoundException, TypeTwoExceptions.NoFilesInSourceDir,
            TypeTwoExceptions.BadFormatFile {
        checkArgs(args); // checks the validation of arguments, if valid, continues
        String commendFileName = args[COMMAND_NAME_POSITION];
        CommandFile commandFile = new CommandFile(commendFileName); // creates a new instance of CommandFile
        FileFacade path=new FileFacade(args[PATH_NAME_POSITION]);
        // Checks if the commend file is a valid file, if not, throws exception
        if (!(commandFile.isFile())){
            throw new TypeTwoExceptions.FileNotFoundException();
        }else if (!(path.isDirectory())){
            throw new TypeTwoExceptions.NoFilesInSourceDir();
        }
        else{
            ArrayList<String> commendFileData = commandFile.readFile();// saves the command file's data to
            // an array list of Strings
            CurrentSecession.getInstance().setPath(path); // sets the path of the files
            FileAnalyzer.getInstance().analyzeStringList(commendFileData);
        }
    }

    private static void checkArgs(String[]args) throws TypeTwoExceptions.IncorrentAmountOfArguments {
        // Checks if the number of arguments is smaller then 2 - in this case throws exception and stops
        if (args.length < MIN_NUM_OF_ARGUMENTS){
            throw new TypeTwoExceptions.IncorrentAmountOfArguments();
        }
    }

}
