package filesprocessing;

import filesprocessing.secssionsprocessor.CurrentSecession;
import filesprocessing.secssionsprocessor.SecessionCreationException;

import java.util.ArrayList;
import java.lang.*;

/**
 * Represents a FileAnalyzer object
 */
class FileAnalyzer {
    private static FileAnalyzer instance=new FileAnalyzer();
    private FileAnalyzer(){}
    static FileAnalyzer getInstance() {
        return instance;
    }

    /*--constants--*/
    private static final String FILTER_HEADLINE = "FILTER";
    private static final String ORDER  = "ORDER";
    private static final String TYPE_I_ERROR_MSG_STR_FORMAT = "Warning in line %d";
    private static final int JUMP_TO_ORDER = 2;
    private static final String DEFAULT_SORTER = "abs";

    /* This method goes through the array list that contains the file data, First,
    * it looks for type 2 exceptions, if found,
    * it throws exceptions. If there are no type 2 exceptions, it goes over the file, and for each section
    *it filters and orders the files
    */
    void analyzeStringList(ArrayList<String> fileData) throws TypeTwoExceptions.BadFilterSectionName,
            TypeTwoExceptions.BadOrderSectionName, TypeTwoExceptions.BadFormatFile {
        checkTypeTwoErrors(fileData); // Checks if there are type 2 errors in the file
        String filterValue ;
        String orderValue ;
        int filterLine=0;
        int orderLine=0;
        // This loop goes over the array list and filters and sorters files by each section
        for (int i = 0; i < fileData.size();) {
            i++;
            if (i>=fileData.size()){
                break;
            }
            filterValue=fileData.get(i);
            i+= JUMP_TO_ORDER;
            // Checks if the index is larger of the array list size
            if (i>=fileData.size()){
                orderValue=DEFAULT_SORTER;
            }else {
                if (fileData.get(i).equals(FILTER_HEADLINE)){
                    orderValue=DEFAULT_SORTER;
                }else {
                    orderValue=fileData.get(i);
                    i++;
                }
            }
            try {
                CurrentSecession.getInstance().setFilterAndSorter(filterValue, orderValue);
            } catch (SecessionCreationException.FilterCreationException e) {
                System.err.printf(TYPE_I_ERROR_MSG_STR_FORMAT, filterLine);
            }catch (SecessionCreationException.SorterCreationException e) {
                System.err.printf(TYPE_I_ERROR_MSG_STR_FORMAT, orderLine);
            }
            String[] outPutData = CurrentSecession.getInstance().getCurrentSessionOutput();
            printFiles(outPutData);
        }
    }

    /* private method to print the names of the filtered and sorted files*/
    private void printFiles(String[] filesToPrint){
        for (int i = 0; i< filesToPrint.length; i++){
            System.out.println(filesToPrint[i]);
        }
    }

    /* private method to check type 2 errors */
    private void checkTypeTwoErrors(ArrayList<String> fileData) throws TypeTwoExceptions.BadFilterSectionName,
             TypeTwoExceptions.BadOrderSectionName, TypeTwoExceptions.BadFormatFile {
            // This loop iterates through the array, for each section checks if in the section exists type 2 errors
            for (int i = 0; i < fileData.size(); i++) {
                i = checkFilter(fileData, i);
                i = checkOrder(fileData, i);
            }
    } // end of checkTypeTwoErrors method

    /* private method to chech filter section in the command file*/
    private int checkFilter(ArrayList<String> fileData, int lineNumber) throws TypeTwoExceptions.BadFilterSectionName,
            TypeTwoExceptions.BadOrderSectionName, TypeTwoExceptions.BadFormatFile {
        boolean isFilterHeadLine=false;
        boolean isFilterValue=false;
        for (int i = 0; i <3; i++) {
            if(fileData.get(lineNumber).equals(FILTER_HEADLINE) && !isFilterHeadLine){
                isFilterHeadLine=true;
                lineNumber++;
            }else if (isFilterHeadLine&&!isFilterValue&&!(fileData.get(lineNumber).equals(ORDER))){
                //current filterValue;
                isFilterValue=true;
                lineNumber++;
            }else if (isFilterHeadLine&&isFilterValue&&fileData.get(lineNumber).equals(ORDER)){
                return lineNumber;
            }
        }//if the
        if (fileData.get(lineNumber).toUpperCase().equals(ORDER)||fileData.get(lineNumber).
                toUpperCase().equals(FILTER_HEADLINE)){
            throw new  TypeTwoExceptions.BadFilterSectionName();
        }else if (fileData.get(lineNumber).toUpperCase().equals(FILTER_HEADLINE)){
        throw new TypeTwoExceptions.BadOrderSectionName();
    }else {
        throw new TypeTwoExceptions.BadFormatFile();
    }
    } // end of checkFilter method

    private int checkOrder(ArrayList<String> fileData, int lineNumber) throws TypeTwoExceptions.BadOrderSectionName,
            TypeTwoExceptions.BadFilterSectionName, TypeTwoExceptions.BadFormatFile {

        boolean isSortHeadLine=false;
        boolean isSortValue=false;
        for (int i = 0; i <3; i++) {
            if (lineNumber>=fileData.size()){
                return fileData.size()-1;
            }
            if (!isSortHeadLine&&fileData.get(lineNumber).equals(ORDER)){
                isSortHeadLine=true;
                lineNumber++;
            }else if (!fileData.get(lineNumber).equals(ORDER)){
                if (fileData.get(lineNumber).equals(FILTER_HEADLINE)){
                    return lineNumber;
                }else if (!isSortValue){
                    lineNumber++;
                    isSortValue=true;
                }
            }
        }

        if (fileData.get(lineNumber).toUpperCase().equals(ORDER)){
            throw new  TypeTwoExceptions.BadFilterSectionName();
        }else if (fileData.get(lineNumber).
                toUpperCase().equals(FILTER_HEADLINE)){
            throw new TypeTwoExceptions.BadOrderSectionName();
        }else {
            throw new TypeTwoExceptions.BadFormatFile();
        }
        }
}
