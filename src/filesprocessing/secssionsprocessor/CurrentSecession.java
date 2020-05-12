package filesprocessing.secssionsprocessor;


import filesprocessing.FileFacade;

import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;

/**
 * a singletone class which hold a one secssion every time , and process the section to the proper name of files.
 */
public class CurrentSecession {
    private static final String VALUES_SEPARATOR = "#";
    private static final String REVERSE_SORT_KEY_VALUE = "REVERSE";
    private static final String NOT_OPERATION_KEY_WORD = "NOT";
    private static final String VALUE_SEPARATOR_KEY_WORD = "#";
    private static final String NOT_UPHOLD_OPERATOR = "NO";
    private static final String UPHOLD_OPERATOR = "YES";
    private static CurrentSecession instance=new CurrentSecession();
    private FileFacade pathName=null;
    private FileFilter currentFileFilter;
    private Comparator<FileFacade> currentSort;
    private CurrentSecession(){
        setDefaultValus();
    }
    private void setDefaultValus(){
        currentSort= SortFactory.getInstance().getAbsComparator();
        currentFileFilter=FilterFactory.getInstance().getAllFilter();
    }
    public static CurrentSecession getInstance() {
        return instance;
    }

    /**
     * set the currnt path name for extraction.
     * @param path the path name.
     */
    public void setPath(FileFacade path) {
        this.pathName = path;
    }

    public void setFilterAndSorter(String filterName,String orderName) throws
            SecessionCreationException.SorterCreationException,
            SecessionCreationException.FilterCreationException {
        setFileFilter(filterName);
        setSorter(orderName);
    }
    /**
     * get currnt Session list of files.
     * @return orderd array of files names.
     */
    public String[] getCurrentSessionOutput(){
        FileFacade[] files=pathName.listFiles(currentFileFilter);
        Arrays.sort(files,currentSort);
        String[] secessionFilesOutputNames=new String[files.length];
        for (int i = 0; i <files.length ; i++) {
            secessionFilesOutputNames[i]=files[i].getName();
        }
        setDefaultValus();
        return secessionFilesOutputNames;

    }

    private void setFileFilter(String filterKey) throws SecessionCreationException.FilterCreationException {
        FileFilter currentFileFilter = readFilterKey(filterKey);
        if (currentFileFilter == null){
            throw new SecessionCreationException.FilterCreationException();
        }else {
                this.currentFileFilter=currentFileFilter;
            }
        }

    /*
     * convert filter string into a FileFilter Object.
      * @param filterKey the filter requst string.
     * @return FileFilter
     */
    private FileFilter readFilterKey(String filterKey) {
        String[] values = filterKey.split(VALUE_SEPARATOR_KEY_WORD);
        String filterName = values[0];
        //indicative variables( if varible does not exists then it is null).
        Double firstDouble = null;
        Double secondDouble = null;
        Boolean filterParameterBoolean = null;
        String stringToFilter = null;
        int notOperationInclude = 0;
        boolean notOperation = false;
        for (int i = 1; i < values.length; i++) {
            if (values.length - 1 == i) {
                if (values[i].equals(NOT_OPERATION_KEY_WORD)) {
                    notOperation = true;
                    notOperationInclude++;
                    break;
                }
            }
            Double doubleValue = getDouble(values[i]);
            if (doubleValue != null && doubleValue >= 0) {//non negativ number
                if (firstDouble == null) {
                    firstDouble = doubleValue;
                } else if (secondDouble == null) {
                    secondDouble = doubleValue;
                }
            } else {
                Boolean booleanValue = isBoolean(values[i]);
                if (booleanValue != null) {
                    filterParameterBoolean = booleanValue;
                } else {
                    stringToFilter = values[i];
                }
            }
        }
        return getFilter(values.length - notOperationInclude, filterName, firstDouble, secondDouble,
                stringToFilter, filterParameterBoolean, notOperation);
    }

    /*
     * this method return a filter by the veriabls that had been read from the filter string
     * @param currentSize the number of valus without the not operation.
     * @param filterName the filter key name
     * @param firstDouble the double arg(if exists)
     * @param secondDouble the second doubler arg(if exists , for between filter)
     * @param stringToFilter the String search key (if exists)
     * @param firstBoolean the isUphold boolean(if exists)
     * @param notOperation is the Not operation exists
     * @return FileFilter if args are valid.
     */
    private FileFilter getFilter(int currentSize,String filterName,Double firstDouble,Double secondDouble,
                                 String stringToFilter,Boolean firstBoolean,boolean notOperation){
        switch (currentSize){
            case 1:
                return FilterFactory.getInstance().getFilter(filterName,notOperation);
            case 2:
                if (firstBoolean!=null){
                    return FilterFactory.getInstance().getFilter(filterName,firstBoolean,notOperation);
                }else if (firstDouble!=null){
                    return FilterFactory.getInstance().getFilter(filterName,firstDouble,notOperation);
                }else if (stringToFilter!=null){
                    return FilterFactory.getInstance().getFilter(filterName,stringToFilter,notOperation);
                }break;
            case 3:
                if (firstDouble!=null&&secondDouble!=null){
                    return FilterFactory.getInstance().getFilter(filterName,secondDouble,firstDouble,notOperation);
                }
            default:
                break;
        }
        return null;
    }
    /*
    * return true if the string is boolean yes and false if it is no, return null if the string isnt yes/no
     */
    private Boolean isBoolean(String stringToCheck){
        if (stringToCheck.equals(NOT_UPHOLD_OPERATOR)){
            return false;
        }else if (stringToCheck.equals(UPHOLD_OPERATOR)){
            return true;
        }
        return null;
    }

    /*
     * determine if the corrnt string is double.
     * @param stringToCheck
     * @return double value if the string is a double , null otherwise.
     */
    private Double getDouble(String stringToCheck){
        try {
            return Double.parseDouble(stringToCheck);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    private void setSorter(String sorterKey) throws SecessionCreationException.SorterCreationException {
        Comparator<FileFacade> comparator=readSortKey(sorterKey);
        if (comparator==null){
            throw new SecessionCreationException.SorterCreationException();
        }else {
            currentSort=comparator;
        }
    }
    private Comparator<FileFacade> readSortKey(String sorterKey){
        String[] values=sorterKey.split(VALUES_SEPARATOR);
        if (values.length>2||values.length<1){
            return null;
        }
        boolean isRevers= false;
        String sorterName=values[0];
        if (values.length==2){
            if(!values[1].equals(REVERSE_SORT_KEY_VALUE)){
                return null;
            }else {
                isRevers=true;
            }
        }
        return SortFactory.getInstance().getComparator(sorterName,isRevers);
    }
}
