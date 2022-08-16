package com.example.android_managementapp.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;


@Entity(tableName = "course_table", foreignKeys = @ForeignKey(entity = Category.class,
        parentColumns = "id", childColumns = "category_id", onDelete = CASCADE))
public class Course extends BaseObservable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "course_id")
    private int courseID;

    @ColumnInfo(name = "course_name")
    private String courseName;

    @ColumnInfo(name = "unit_price")
    private String unitPrice;

    @ColumnInfo(name = "category_id")
    private int categoryID;

    @Ignore
    public Course() {
    }

    public Course(int courseID, String courseName, String unitPrice, int categoryID) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.unitPrice = unitPrice;
        this.categoryID = categoryID;
    }

    @Bindable
    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
        notifyPropertyChanged(BR.courseID);
    }

    @Bindable
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
        notifyPropertyChanged(BR.courseName);

    }

    @Bindable
    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
        notifyPropertyChanged(BR.unitPrice);

    }

    @Bindable
    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
        notifyPropertyChanged(BR.categoryID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return courseID == course.courseID && categoryID == course.categoryID && courseName.equals(course.courseName) && unitPrice.equals(course.unitPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseID, courseName, unitPrice, categoryID);
    }
}
