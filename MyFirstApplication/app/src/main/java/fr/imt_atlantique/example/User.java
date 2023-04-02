package fr.imt_atlantique.example;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {
    private String lastName;
    private String firstName;
    private String birthday;
    private String birthCity;
    private String department;
    private String photoPath;
    private List<String> phones;

    public User(String firstName, String lastName, String birthday, String birthCity, String department, String photoPath, List<String> phones) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.birthCity = birthCity;
        this.department = department;
        this.photoPath = photoPath;
        this.phones = phones;
    }

    protected User(Parcel in) {
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.birthday = in.readString();
        this.birthCity = in.readString();
        this.department = in.readString();
        this.photoPath = in.readString();
        this.phones = new ArrayList<>();
        in.readStringList(this.phones);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.birthday);
        dest.writeString(this.birthCity);
        dest.writeString(this.department);
        dest.writeString(this.photoPath);
        dest.writeStringList(this.phones);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setBirthCity(String birthCity) {
        this.birthCity = birthCity;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getBirthCity() {
        return birthCity;
    }

    public String getDepartment() {
        return department;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public List<String> getPhones() {
        return phones;
    }
}
