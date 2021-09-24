package com.test.viewpagerfun.constants;

/**
 * This class holds constants that otherwise would be defined in many other places.
 * It's a public final class, which a final class cannot be extended by any other class.
 *
 * USAGE:   when using the constants in another class, it could be useful to omit the
 *          class prefix 'ConstantsHolder' prefix by using a static import, like so:
 *
 *                import static com.test.viewpagerfun.constants.ConstantsHolder.*;
 */
public final class ConstantsHolder {

    //private constructor: the class now can not be instantiated
    private ConstantsHolder(){
    }

    /*
      SHARED PREFERENCES
     */

    // specifies the file in which to save the shared preferences
    public static final String PREFS_FILE = "PREFS_FILE";
    public static final String PREFS_REMAINING_NOTES = "PREFS_REMAINING_NOTES";

    /*
       EXTRA/REQUEST CONSTANTS, E.G: INTENT.PUTEXTRA(EXTRA_REMAINING_REVIEWS, BUNDLE);
     */
    public static final String REQUEST_CODE = "REQUEST_CODE";
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    public static final String EXTRA_ADD_NOTE = "de.test.roomdatabaseexample.EXTRA_ADD";
    public static final String EXTRA_EDIT_NOTE = "de.test.roomdatabaseexample.EXTRA_ID";

    public static final String EXTRA_ID = "de.test.roomdatabaseexample.EXTRA_ID";
    public static final String EXTRA_TITLE = "de.test.roomdatabaseexample.EXTRA_TITLE";

    /*
       BUNDLE
     */
    public static final String BUNDLE_ADD_NOTE = "BUNDLE_ADD_NOTE";
    public static final String BUNDLE_EDIT_NOTE = "BUNDLE_EDIT_NOTE";
    public static final String BUNDLE_REMAINING_NOTES = "BUNDLE_REMAINING_NOTES";

    /*
       OPTIMAL STRING ALIGNMENT DISTANCE (LEVENSHTEIN)
     */

    //denotes how many mismatches are allowed every 'MIN_MISMATCH_LENGTH' characters
    public static final float MIN_MISMATCH_LENGTH = 5;

}
