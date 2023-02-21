package fr.imt_atlantique.example;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.List;

public class User implements Parcelable {
    private String lastName;
    private String firstName;
    private String birthday;
    private String birthCity;
    private String departement;
    private List<String> phones;

    public User(String firstName, String lastName, String birthday, String birthCity, String departement, List<String> phones) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.birthCity = birthCity;
        this.departement = departement;
        this.phones = phones;
    }

    protected User(Parcel in) {
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.birthday = in.readString();
        this.birthCity = in.readString();
        this.departement = in.readString();
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
        dest.writeString(this.departement);
        dest.writeStringList(this.phones);
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

    public String getDepartement() {
        return departement;
    }

    public List<String> getPhones() {
        return phones;
    }
}
