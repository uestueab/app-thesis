package com.test.viewpagerfun.constants;

/**
*
 * USAGE:   when using the constants in another class, it could be useful to omit the
 *          class prefix 'Permissions' prefix by using a static import, like so:
 *
 *                import static com.test.viewpagerfun.constants.ConstantsHolder.*;
 */
public final class Permissions {

    //private constructor: the class now can not be instantiated
    private Permissions(){
    }

    /*
        App permission requests
     */
    public static final int REQUEST_MICROPHONE = 200;


}
