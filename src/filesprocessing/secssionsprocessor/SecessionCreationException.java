package filesprocessing.secssionsprocessor;


/**
 * an abstract exception class that regard CurrentSession couldent constract a filter or an object.
 */
public abstract class SecessionCreationException extends Exception {
    /*--constance--*/
    private static final long serialVersionUID = 1L;
    private static final String SORTER_EXCEPTION_MSG = "cannot create sorter.";
    private static final String FILTER_EXCEPTION_MSG = "cannot create filter.";

    /*
     * constructor which insert the proper message.
     * @param msg the message to be sent.
     */
    private SecessionCreationException(String msg) {
        super(msg);
    }
    /**
     * an exception class which indicate a that the proper Filter(FileFilter) which met to be build hasn't
     * manged to be form.
     */
    public static class FilterCreationException extends SecessionCreationException {
        /*
         *default constructor, which sent the proper message.
         */
        FilterCreationException() {
            super(FILTER_EXCEPTION_MSG);
        }
    }

    /**
     * an exception class which indicate a that the proper sort(Comparable) which met to be build hasn't
     * manged to be form.
     */
    public static class SorterCreationException extends SecessionCreationException {
        /*
         *default constructor, which sent the proper message.
         */
        SorterCreationException() {
            super(SORTER_EXCEPTION_MSG);
        }
    }
}
