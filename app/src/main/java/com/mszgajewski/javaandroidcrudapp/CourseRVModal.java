package com.mszgajewski.javaandroidcrudapp;

import android.os.Parcel;
import android.os.Parcelable;

public class CourseRVModal implements Parcelable {

    private String itemName ;
    private String itemPrize;
    private String itemSuited;
    private String itemImg;
    private String itemLink;
    private String itemDesc;
    private String courseId;

    public CourseRVModal(){}

    public CourseRVModal(String itemName, String itemPrize, String itemSuited, String itemImg, String itemLink, String itemDesc, String courseId) {
        this.itemName = itemName;
        this.itemPrize = itemPrize;
        this.itemSuited = itemSuited;
        this.itemImg = itemImg;
        this.itemLink = itemLink;
        this.itemDesc = itemDesc;
        this.courseId = courseId;
    }

    protected CourseRVModal(Parcel in) {
        itemName = in.readString();
        itemPrize = in.readString();
        itemSuited = in.readString();
        itemImg = in.readString();
        itemLink = in.readString();
        itemDesc = in.readString();
        courseId = in.readString();
    }

    public static final Creator<CourseRVModal> CREATOR = new Creator<CourseRVModal>() {
        @Override
        public CourseRVModal createFromParcel(Parcel in) {
            return new CourseRVModal(in);
        }

        @Override
        public CourseRVModal[] newArray(int size) {
            return new CourseRVModal[size];
        }
    };

    public String getItemName() { return itemName; }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrize() {
        return itemPrize;
    }

    public void setItemPrize(String itemPrize) {
        this.itemPrize = itemPrize;
    }

    public String getItemSuited() {
        return itemSuited;
    }

    public void setItemSuited(String itemSuited) {
        this.itemSuited = itemSuited;
    }

    public String getItemImg() {
        return itemImg;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }

    public String getItemLink() {
        return itemLink;
    }

    public void setItemLink(String itemLink) {
        this.itemLink = itemLink;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(itemName);
        parcel.writeString(itemPrize);
        parcel.writeString(itemSuited);
        parcel.writeString(itemImg);
        parcel.writeString(itemLink);
        parcel.writeString(itemDesc);
        parcel.writeString(courseId);
    }
}
