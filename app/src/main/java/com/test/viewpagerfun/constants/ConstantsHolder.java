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
       EXTRA CONSTANTS, E.G: INTENT.PUTEXTRA(EXTRA_REMAINING_REVIEWS, BUNDLE);
     */

    // save remaining reviews.
    public static final String EXTRA_REMAINING_REVIEWS = "EXTRA_REMAINING_REVIEWS";


    /*
       BUNDLE
     */
    public static final String BUNDLE_REMAINING_NOTES = "BUNDLE_REMAINING_NOTES";


}
