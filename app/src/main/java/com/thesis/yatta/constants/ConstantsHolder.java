package com.thesis.yatta.constants;

/**
 * This class holds key constants that otherwise would be defined in many other places.
 * It's a public final class, that means it cannot be extended by any other class.
 *
 * USAGE:   when using the constants in another class, it could be useful to omit the
 *          class prefix 'ConstantsHolder' prefix by using a static import, like so:
 *
 *                import static com.thesis.yatta.constants.ConstantsHolder.*;
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
    public static final String PREFS_REMAINING_FLASH_CARDS = "prefs_remaining_flashCards";
    public static final  String PREFS_ONBOARDING_PASSED = "prefs_onboarding_passed";

    public static final String PREFS_ADVANCED_EXPERIMENTAL = "prefs_advanced_experimental";
    public static final String PREFS_DISPLAY_ANIMATION = "prefs_display_animation";
    public static final String PREFS_DISPLAY_THEME = "prefs_display_theme";
    public static final String PREFS_GENERAL_LANGUAGE = "prefs_general_language";
    public static final String PREFS_GENERAL_NOTIFICATIONS = "prefs_general_notifications";
    public static final String PREFS_PLAY_PRONUNCIATION = "prefs_play_pronunciation";
    public static final String PREFS_REVIEW_BACK = "prefs_review_back";
    public static final String PREFS_REVIEW_HAPTIC = "prefs_review_haptic";
    public static final String PREFS_REVIEW_MISMATCH_TOAST = "prefs_review_mismatch_toast";
    public static final String PREFS_REVIEW_SHUFFLE = "prefs_review_shuffle";
    public static final String PREFS_SHOW_DIAGRAM = "prefs_show_diagram";

    public static final String PREFS_FEEDBACK = "prefs_feedback";

    /*
       EXTRA/REQUEST CONSTANTS, E.G: INTENT.PUTEXTRA(EXTRA_REMAINING_REVIEWS, BUNDLE);
     */
    public static final String REQUEST_CODE = "REQUEST_CODE";
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    public static final String EXTRA_ADD_NOTE = "de.thesis.roomdatabaseexample.EXTRA_ADD";
    public static final String EXTRA_EDIT_NOTE = "de.thesis.roomdatabaseexample.EXTRA_ID";

    public static final String EXTRA_ID = "de.thesis.roomdatabaseexample.EXTRA_ID";
    public static final String EXTRA_TITLE = "de.thesis.roomdatabaseexample.EXTRA_TITLE";

    /*
       BUNDLE
     */
    public static final String BUNDLE_ADD_NOTE = "BUNDLE_ADD_NOTE";
    public static final String BUNDLE_EDIT_NOTE = "BUNDLE_EDIT_NOTE";
    public static final String BUNDLE_REMAINING_NOTES = "BUNDLE_REMAINING_NOTES";

    /*
       OPTIMAL STRING ALIGNMENT DISTANCE (LEVENSHTEIN)
     */

    //deflashCards how many mismatches are allowed every 'MIN_MISMATCH_LENGTH' characters
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

    /*
        FEATURES
     */

    // For notifications
    public static final String APP_CLOSED_AT = "APP_CLOSED_AT";
    public static final String NOTIFY_DELAY_TIME = "NOTIFY_DELAY_TIME";

    // 1-Day in milliseconds: 1 * 24 * 60 * 60 * 1000
    // public static final long NOTIFY_DEFAULT_DELAY_TIME = 86400000;

    // 15-Min in milliseconds: 15 * 60 * 1000;
     public static final long NOTIFY_DEFAULT_DELAY_TIME = 900000;

}
