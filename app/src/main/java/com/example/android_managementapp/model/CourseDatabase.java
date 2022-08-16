package com.example.android_managementapp.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Category.class,Course.class}, version = 1)
public abstract class CourseDatabase extends RoomDatabase {

    public abstract CategoryDAO categoryDAO();
    public abstract CourseDAO courseDAO();

    // Singleton Pattern
    private static CourseDatabase instance;

    // Using synchronized to prevent any creation of the objects. its means one instance of the database on during the runtime or anytime the application is running
    public static synchronized CourseDatabase getInstance(Context context){
        if (null == instance){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CourseDatabase.class, "courses_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    // Callback
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // Insert data when db is created
            initializeData();
        }
        // Insert data when db is created
    };

    private static void initializeData() {

        CourseDAO courseDAO = instance.courseDAO();
        CategoryDAO categoryDAO = instance.categoryDAO();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // Categories
                Category category1 = new Category();
                category1.setCategoryName("Front End");
                category1.setCategoryDescription("Web development interface");

                Category category2 = new Category();
                category2.setCategoryName("Back End");
                category2.setCategoryDescription("Web development database");

                categoryDAO.insert(category1);
                categoryDAO.insert(category2);

                // Course
                Course course1 = new Course();
                course1.setCourseName("HTML");
                course1.setUnitPrice("100$");
                course1.setCategoryID(1);

                Course course2 = new Course();
                course2.setCourseName("CSS");
                course2.setUnitPrice("200$");
                course2.setCategoryID(1);

                Course course3 = new Course();
                course3.setCourseName("PHP");
                course3.setUnitPrice("500$");
                course3.setCategoryID(2);

                Course course4 = new Course();
                course4.setCourseName("AJAX");
                course4.setUnitPrice("600$");
                course4.setCategoryID(2);

                courseDAO.insert(course1);
                courseDAO.insert(course2);
                courseDAO.insert(course3);
                courseDAO.insert(course4);

            }
        });
    }
}
