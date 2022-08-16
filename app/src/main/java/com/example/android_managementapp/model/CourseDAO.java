package com.example.android_managementapp.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CourseDAO {

    @Insert
    void insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("SELECT * FROM course_table")
    LiveData<List<Course>> getAllCourse();

    @Query("SELECT * FROM course_table WHERE category_id==:categoryID")
    LiveData<List<Course>> getCourses(int categoryID);

}
