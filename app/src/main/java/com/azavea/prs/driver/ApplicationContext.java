package com.azavea.prs.driver;

import android.app.Application;

/**
 * Created by kat on 11/22/15.
 */

public class ApplicationContext extends Application {
    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static Application getApplication() {
        return application;
    }
}
