package filesprocessing.secssionsprocessor;

import java.io.File;
import java.io.FileFilter;

/*
 * singleton of filterFactory, generate an FileFilter for sorting file.
 */
class FilterFactory {
    /* constants*/
    private static final String ALL_FILTER_KEY_WORD = "all";
    /*
    * instance.
     */
    private static FilterFactory instance = new FilterFactory();

    /*
    * instance private constructor.
     */
    private FilterFactory() {
    }

    /*
     * @return factory instance
     */
    static FilterFactory getInstance() {
        return instance;
    }

    /*
    * enum which its values check filter using a single double argument.
     */
    private enum MathComparison {
        GREATER_THAN() {
            boolean filter(double fileSize, double threshold) {
                return fileSize > threshold;
            }
        },
        SMALLER_THAN() {
            boolean filter(double fileSize, double threshold) {
                return fileSize < threshold;
            }
        };
        /*--constants--*/
        private static final int BYTE_IT_KB = 1024;

        /*
         * abstract methode which determine the filter behavior.
         */
        abstract boolean filter(double fileSize, double threshold);

        /*
         * cheack a file and a threshold and
         */
        boolean isFiltered(File file, double threshold) {
            return filter(file.length() / BYTE_IT_KB, threshold);
        }

        /**
         * override the enum metod return the enum name with lowercase.
         *
         * @return enum name with lower case .
         */
        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }

    }
    /*
    * enum which its values check filter using a two double argument.
     */
    private enum TwoFactorMathComparison {
        BETWEEN() {
            boolean isFiltered(File file, double maxBar, double minBar) {
                return MathComparison.GREATER_THAN.isFiltered(file, minBar) &&
                        MathComparison.SMALLER_THAN.isFiltered(file, maxBar);
            }
        };
        /*
         * abstract method which determine the filter behavior.
         */
        abstract boolean isFiltered(File file, double maxBar, double minBar);

        /**
         * override the enum metod return the enum name with lowercase.
         *
         * @return enum name with lower case .
         */
        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
    /*
    * enum which its values check filter without parameters
     */
    private enum NonFactorComparison {
        ALL() {
            boolean isFiltered(File file) {
                return true;
            }
        };

        abstract boolean isFiltered(File file);

        /**
         * override the enum metod return the enum name with lowercase.
         *
         * @return enum name with lower case .
         */
        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
    /*
     * enum which its values check filter with string parameter.
     */
    private enum NameComparison {
        FILE() {
            boolean isFiltered(File file, String parameter) {
                return file.getName().equals(parameter);
            }
        },
        CONTAINS() {
            boolean isFiltered(File file, String parameter) {
                return file.getName().contains(parameter);
            }
        },
        PREFIX() {
            boolean isFiltered(File file, String parameter) {
                return file.getName().startsWith(parameter);
            }
        },
        SUFFIX() {
            boolean isFiltered(File file, String parameter) {
                return file.getName().endsWith(parameter);
            }
        };

        abstract boolean isFiltered(File file, String parameter);

        /**
         * override the enum metod return the enum name with lowercase.
         *
         * @return enum name with lower case .
         */
        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }

    }
    /*
     * enum which its values check filter with boolean parameter.
     */
    private enum FileStateComparison {
        WRITABLE() {
            boolean isFiltered(File file) {
                return file.canWrite();
            }
        },
        EXECUTABLE() {
            boolean isFiltered(File file) {
                return file.canExecute();
            }
        },
        HIDDEN() {
            boolean isFiltered(File file) {
                return file.isHidden();
            }
        };

        abstract boolean isFiltered(File file);

        /**
         * override the enum metod return the enum name with lowercase.
         *
         * @return enum name with lower case .
         */
        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }


    /*
     * get filter using a threshold.
     * @param filterName name of the filter.
     * @param threshold the threshold.
     * @param isNot if not parameter has been insert.
     * @return FileFilter instance.
     */
    FileFilter getFilter(String filterName, double threshold, boolean isNot) {
        MathComparison mathFilter = null;
        //search the value in the proper enum
        for (MathComparison filter : MathComparison.values()) {
            if (filterName.equals(filter.toString())) {
                mathFilter = filter;
            }
        }
        if (mathFilter == null) {
            return null;
        } else {
            final MathComparison mathComparisonFilter = mathFilter;
            return file -> mathComparisonFilter.isFiltered(file, threshold) != isNot && file.isFile();
        }
    }

    /*
     * get filter from between filter.
     * @param filterName name of the filter.
     * @param maxBar the max maxBar.
     * @param minBar the minimum maxBar.
     * @param isNot if not parameter has been insert.
     * @return FileFilter instance.
     */
    FileFilter getFilter(String filterName, double maxBar, double minBar, boolean isNot) {
        TwoFactorMathComparison twoFactorMathComparison = null;
        //search the value in the proper enum
        for (TwoFactorMathComparison filter : TwoFactorMathComparison.values()) {
            if (filterName.equals(filter.toString())) {
                twoFactorMathComparison = filter;
            }
        }
        if (twoFactorMathComparison == null || maxBar <= minBar) {
            return null;
        }
        final TwoFactorMathComparison currentFactor = twoFactorMathComparison;
        return file -> (MathComparison.GREATER_THAN.isFiltered(file, minBar) &&
                currentFactor.isFiltered(file, maxBar, minBar)) != isNot && file.isFile();
    }

    /*
     * get filter using search key .
     * @param filterName the filter name.
     * @param searchValue a String key for searching in the file name.
     * @param isNot if not parameter has been insert.
     * @return FileFilter instance.
     */
    FileFilter getFilter(String filterName, String searchValue, boolean isNot) {
        NameComparison nameComparisonFilter = null;
        //search the value in the proper enum
        for (NameComparison currentValue : NameComparison.values()) {
            if (filterName.equals(currentValue.toString())) {
                nameComparisonFilter = currentValue;
            }
        }
        if (nameComparisonFilter == null) {
            return null;
        } else {
            final NameComparison nameComparison = nameComparisonFilter;
            return file -> nameComparison.isFiltered(file, searchValue) != isNot && file.isFile();
        }
    }

    /*
     * get filter based on file State.
     * @param filterName the filter name.
     * @param isUphold is the uphold the current demand.
     * @param isNot if not parameter has been insert.
     * @return FileFilter instance.
     */
    FileFilter getFilter(String filterName, boolean isUphold, boolean isNot) {
        FileStateComparison fileStateComparisonFilter = null;
        //search the value in the proper enum
        for (FileStateComparison currantFilter : FileStateComparison.values()) {
            if (filterName.equals(currantFilter.toString())) {
                fileStateComparisonFilter = currantFilter;
            }
        }
        if (fileStateComparisonFilter == null) {
            return null;
        } else {
            final FileStateComparison fileStateComparison = fileStateComparisonFilter;
            return file -> {
                boolean answer = fileStateComparison.isFiltered(file);
                return answer == isUphold && answer != isNot && file.isFile();
            };
        }
    }

    /*
     *get filter based on none criterion (all filter).
     * @param filterName the filter name.
     * @param isNot if not parameter has been insert.
     * @return FileFilter instance.
     */
    FileFilter getFilter(String filterName, boolean isNot) {
        NonFactorComparison nonFactorComparison = null;
        //search the value in the proper enum
        for (NonFactorComparison currantFilter : NonFactorComparison.values()) {
            if (filterName.equals(currantFilter.toString())) {
                nonFactorComparison = currantFilter;
            }
        }
        if (nonFactorComparison == null) {
            return null;
        }
        final NonFactorComparison currentFilter = nonFactorComparison;
        return file -> currentFilter.isFiltered(file) != isNot && file.isFile();
    }
    /*
     * get the "all" filter.
     */
    FileFilter getAllFilter() {
        return getFilter(ALL_FILTER_KEY_WORD, false);
    }
}
