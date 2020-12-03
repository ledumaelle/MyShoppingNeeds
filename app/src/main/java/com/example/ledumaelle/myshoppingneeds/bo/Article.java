package com.example.ledumaelle.myshoppingneeds.bo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Article implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "price")
    private Float price;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "rate")
    private Float rate;

    @ColumnInfo(name = "url")
    private String url;

    @ColumnInfo(name = "state")
    private boolean state;

    @ColumnInfo(name = "nameFile")
    private String nameFile;

    public Article() {
    }

    public Article(String name, Float price, String description, Float rate, String url, boolean state, String nameFile) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.rate = rate;
        this.url = url;
        this.state = state;
        this.nameFile = nameFile;
    }

    protected Article(Parcel in) {
        id = in.readInt();
        name = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readFloat();
        }
        description = in.readString();
        if (in.readByte() == 0) {
            rate = null;
        } else {
            rate = in.readFloat();
        }
        url = in.readString();
        state = in.readByte() != 0;
        nameFile = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(price);
        }
        dest.writeString(description);
        if (rate == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(rate);
        }
        dest.writeString(url);
        dest.writeByte((byte) (state ? 1 : 0));
        dest.writeString(nameFile);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", rate=" + rate +
                ", url='" + url + '\'' +
                ", state=" + state +
                ", nameFile='" + nameFile + '\'' +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return ((Article) obj).getId() == this.getId();
    }
}
