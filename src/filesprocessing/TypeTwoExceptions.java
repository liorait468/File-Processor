package filesprocessing;

/**
 * The class represents
 */
abstract class TypeTwoExceptions extends Exception {
    private static final long serialVersionUID=1L;
    private static final String TYPE_2_ERROR_PREFIX = "ERROR: %s\n";
    private static final String FILTER_SUB_SECTION_MISSING = "FILTER sub-section missing";
    private static final String ORDER_SUB_SECTION_MISSING = "ORDER sub-section missing";
    private static final String BAD_FORMAT = "Bad format";
    private static final String FILE_NOT_FOUND = "file not found";
    private static final String WRONG_USAGE_SHOULD_RECEIVE_2_ARGUMENTS = "Wrong usage. Should receive 2 arguments";
    private static final String NO_FILES_IN_SOURCEDIR = "No files in sourcedir";

    TypeTwoExceptions(String msg){
        super(String.format(TYPE_2_ERROR_PREFIX, msg));
    }

    static class BadFilterSectionName extends TypeTwoExceptions {
        BadFilterSectionName() {
            super(FILTER_SUB_SECTION_MISSING);
        }
    }

    static class BadOrderSectionName extends TypeTwoExceptions{
        BadOrderSectionName() {
            super(ORDER_SUB_SECTION_MISSING);
        }
    }

    static class BadFormatFile extends TypeTwoExceptions{
        BadFormatFile(){
            super(BAD_FORMAT);
        }
    }

    static class FileNotFoundException extends TypeTwoExceptions{
        FileNotFoundException(){
            super(FILE_NOT_FOUND);
        }
    }
    static class IncorrentAmountOfArguments extends TypeTwoExceptions{
        IncorrentAmountOfArguments(){
            super(WRONG_USAGE_SHOULD_RECEIVE_2_ARGUMENTS);
        }
    }

    static class NoFilesInSourceDir extends TypeTwoExceptions{
        NoFilesInSourceDir(){
            super(NO_FILES_IN_SOURCEDIR);
        }
    }
}
