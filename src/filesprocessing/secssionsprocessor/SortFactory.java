package filesprocessing.secssionsprocessor;

import filesprocessing.FileFacade;

import java.util.Comparator;

/*
 * an singleton class which generate FileFilter according to the given parameters.
 */
class SortFactory {
    /*--class singleton instance--*/
    private static SortFactory instance = new SortFactory();
    /*
     * private default constructor.
     */
    private SortFactory() {
    }
    /*
    * return the class single instance.
     */
    static SortFactory getInstance() {
        return instance;
    }
    /*
     *  enum of all kind of sort.
     */
    private enum Sort {
        ABS() {
            int compareIt(FileFacade currentFile, FileFacade fileToComper) {
                return currentFile.getAbsolutePath().compareTo(fileToComper.getAbsolutePath());
            }
        },
        TYPE() {
            int compareIt(FileFacade currentFile, FileFacade fileToComper) {
                int toReturn= currentFile.getType().compareTo(currentFile.getType());
                if (toReturn== EQUAL_FACTOR) {
                    toReturn=ABS.compareIt(currentFile,fileToComper);
                }
                    return toReturn;
            }
        },
        SIZE() {
            int compareIt(FileFacade currentFile, FileFacade fileToComper) {
                int toReturn=Double.compare(currentFile.length(),fileToComper.length());
                if (toReturn==EQUAL_FACTOR) {
                    toReturn=ABS.compareIt(currentFile,fileToComper);
                }
                return toReturn;
            }
        };
        /*--constant--*/
        private static final int EQUAL_FACTOR = 0;

        /*
         * abstract method for compering 2 files the fires one is the current file and
         * the outher one is the one to compere with
         */
        abstract int compareIt(FileFacade currentFile, FileFacade fileToComper);

        /**
         * override the enum metod return the enum name with lowercase.
         * @return enum name with lower case .
         */
        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    /*
     * generate comparator object.
     * @param sortName the name of the sort operation.
     * @param isReversed is the sort operation is reversed.
     * @return comparator for the designed properties.
     */
    Comparator<FileFacade> getComparator(String sortName, boolean isReversed) {
        int reversFactor = 1;
        if (isReversed) {
            reversFactor = -reversFactor;
        }
        Sort currantSorter = null;
        //search the value in the proper enum
        for (Sort sort : Sort.values()) {
            if (sortName.equals(sort.toString())) {
                currantSorter = sort;
            }
        }
        if (currantSorter == null) {
            return null;
        }
        final Sort sorter = currantSorter;
        final int revers = reversFactor;
        return (fileFacade, t1) -> sorter.compareIt(fileFacade, t1) * revers;
    }
    /*
     * get the "abs" comparator
     */
    Comparator<FileFacade> getAbsComparator(){
        return getComparator(Sort.ABS.toString(),true);
    }
}

