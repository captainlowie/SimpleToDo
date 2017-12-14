package com.app.captainlowie.simpletodo;

import android.provider.BaseColumns;

/**
 * Created by eduard on 24.11.17.
 */

public class TaskContract {

    public static final String DB_NAME = "simpletodo.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks";

        public static final String COL_TASK_TITLE = "title";
    }

}
