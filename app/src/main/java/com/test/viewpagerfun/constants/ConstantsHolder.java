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
    public static final String PREFS_FILE = "prefs_file";
    public static final String PREFS_REMAINING_NOTES = "prefs_remaining_notes";

    public static final String PREFS_GENERAL_NOTIFICATIONS = "prefs_general_notifications";
    public static final String PREFS_GENERAL_LANGUAGE = "prefs_general_language";
    public static final String PREFS_DISPLAY_THEME = "prefs_display_theme";
    public static final String PREFS_DISPLAY_ANIMATION = "prefs_display_animation";
    public static final String PREFS_REVIEW_MISMATCH_TOAST = "prefs_review_mismatch_toast";
    public static final String PREFS_REVIEW_SHUFFLE = "prefs_review_shuffle";
    public static final String PREFS_REVIEW_HAPTIC = "prefs_review_haptic";
    public static final String PREFS_REVIEW_BACK = "prefs_review_back";
    public static final String PREFS_ADVANCED_EXPERIMENTAL = "prefs_advanced_experimental";

    public static final String PREFS_FEEDBACK = "prefs_feedback";

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


    /*
      THEMES
     */
    public static final String THEME_LIGHT = "theme_light";
    public static final String THEME_GRUVBOX = "theme_gruvbox";
    public static final String THEME_BREEZE = "theme_breeze";

    /*
        URI
     */
    public static final String URI_REPO = "https://github.com/uestueab/thesis";
    public static final String URI_REPO_ISSUES = "https://github.com/uestueab/thesis/issues";
}
