package com.example.android_managementapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.android_managementapp.databinding.ActivityMainBinding;
import com.example.android_managementapp.model.Category;
import com.example.android_managementapp.model.Course;
import com.example.android_managementapp.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mainActivityViewModel;
    private ArrayList<Category> categoriesList;
    private ActivityMainBinding activityMainBinding;
    private MainActivityClickHandler handler;
    private Category selectedCategory;

    // Recycler view
    private RecyclerView courseRecyclerView;
    private CourseAdapter courseAdapter;
    private ArrayList<Course> coursesList;

    private static final int ADD_COURSE_REQUEST_CODE = 1;
    private static final int EDIT_COURSE_REQUEST_CODE = 2;
    public int selectedCourseID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        handler = new MainActivityClickHandler();
        activityMainBinding.setClickHandlers(handler);


        mainActivityViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {

                categoriesList = (ArrayList<Category>) categories;
                for(Category c: categories){
                    Log.i("TAG", c.getCategoryName());
                }
                showOnSpinner();
            }
        });

        mainActivityViewModel.getCourseOfSelectedCategory(1).observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                for(Course c: courses){
                    Log.i("TAG", c.getCourseName());
                }
            }
        });
    }

    private void showOnSpinner() {
        ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<>(this,R.layout.spinner_item, categoriesList);
        categoryArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        activityMainBinding.setSpinnerAdapter(categoryArrayAdapter);


    }

    public void loadCourseArrayList(int categoryID){
        mainActivityViewModel.getCourseOfSelectedCategory(categoryID).observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                coursesList = (ArrayList<Course>) courses;
                loadRecyclerView();
            }
        });
    }

    private void loadRecyclerView() {
        courseRecyclerView = activityMainBinding.secondaryLayout.recyclerView;
        courseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseRecyclerView.setHasFixedSize(true);

        courseAdapter = new CourseAdapter();
        courseRecyclerView.setAdapter(courseAdapter);
        courseAdapter.setCourses(coursesList);

        // Edit the course
        courseAdapter.setListener(new CourseAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Course course) {
                selectedCourseID = course.getCourseID();
                Intent i = new Intent(MainActivity.this, AddEditActivity.class);
                i.putExtra(AddEditActivity.COURSE_ID, selectedCourseID);
                i.putExtra(AddEditActivity.COURSE_NAME, course.getCourseName());
                i.putExtra(AddEditActivity.UNIT_PRICE, course.getUnitPrice());

                startActivityForResult(i, EDIT_COURSE_REQUEST_CODE);
            }
        });

        // Delete Course
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Course courseDelete = coursesList.get(viewHolder.getAdapterPosition());
                mainActivityViewModel.deleteCourse(courseDelete);
            }
        }).attachToRecyclerView(courseRecyclerView);
    }

    public class MainActivityClickHandler{

        public void onFABClicked(View view){
            Intent i = new Intent(MainActivity.this, AddEditActivity.class);
            startActivityForResult(i, ADD_COURSE_REQUEST_CODE);

        }

        // AdapterView<?>: any type of AdapterView
        public void onSelectItem(AdapterView<?> parent, View view, int pos, long id) {
            selectedCategory = (Category) parent.getItemAtPosition(pos);
            String message = "Id is: " + selectedCategory.getId() +
                    "\n name is " + selectedCategory.getCategoryName();
            Toast.makeText(parent.getContext(), message, Toast.LENGTH_SHORT).show();
            loadCourseArrayList(selectedCategory.getId());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int selectedCategoryID = selectedCategory.getId();
        if (requestCode == ADD_COURSE_REQUEST_CODE && resultCode == RESULT_OK){
            Course course = new Course();
            course.setCategoryID(selectedCategoryID);
            course.setCourseName(data.getStringExtra(AddEditActivity.COURSE_NAME));
            course.setUnitPrice(data.getStringExtra(AddEditActivity.UNIT_PRICE));
            mainActivityViewModel.addNewCourse(course);
        } else if (requestCode == EDIT_COURSE_REQUEST_CODE && resultCode == RESULT_OK) {
            Course course = new Course();
            course.setCategoryID(selectedCategoryID);
            course.setCourseName(data.getStringExtra(AddEditActivity.COURSE_NAME));
            course.setUnitPrice(data.getStringExtra(AddEditActivity.UNIT_PRICE));
            course.setCourseID(selectedCourseID);
            mainActivityViewModel.addNewCourse(course);
        }


    }
}