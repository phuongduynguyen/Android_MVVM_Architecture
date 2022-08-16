package com.example.android_managementapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android_managementapp.model.Category;
import com.example.android_managementapp.model.Course;
import com.example.android_managementapp.model.CourseShopRepository;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    // Repository
    private CourseShopRepository repository;

    // Livedata
    private LiveData<List<Category>> allCategories;
    private LiveData<List<Course>> courseOfSelectedCategory;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        // Create one instance of Repository
        repository = new CourseShopRepository(application);
    }

    public LiveData<List<Category>> getAllCategories(){
        allCategories = repository.getCategories();
        return allCategories;
    }

    public LiveData<List<Course>> getCourseOfSelectedCategory(int categoryID){
        courseOfSelectedCategory = repository.getCourses(categoryID);
        return courseOfSelectedCategory;
    }

    public void addNewCourse(Course course){
        repository.insertCourse(course);
    }

    public void updateCourse(Course course){
        repository.updateCourse(course);
    }

    public void deleteCourse(Course course){
        repository.deleteCourse(course);
    }
}

