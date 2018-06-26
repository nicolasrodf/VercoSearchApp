package com.example.nicolas.vercoapp.model;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

    private String trademark;
    private String model;
    private Double price;
    private Double discount;
    private String sex;
    private String type;
    private String labels; //
    private Bitmap photoBitmap;

    public Product() {
    }

    public Product(String trademark, String model, Double price, Double discount, String sex, String type, String labels, Bitmap photoBitmap) {
        this.trademark = trademark;
        this.model = model;
        this.price = price;
        this.discount = discount;
        this.sex = sex;
        this.type = type;
        this.labels = labels;
        this.photoBitmap = photoBitmap;
    }

    public String getTrademark() {
        return trademark;
    }

    public String getModel() {
        return model;
    }

    public Double getPrice() {
        return price;
    }

    public Double getDiscount() {
        return discount;
    }

    public String getSex() {
        return sex;
    }

    public String getType() {
        return type;
    }

    public String getLabels() {
        return labels;
    }

    public Bitmap getPhotoBitmap() {
        return photoBitmap;
    }

    protected Product(Parcel in) {
        trademark = in.readString();
        model = in.readString();
        price = in.readByte() == 0x00 ? null : in.readDouble();
        discount = in.readByte() == 0x00 ? null : in.readDouble();
        sex = in.readString();
        type = in.readString();
        labels = in.readString();
        photoBitmap = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trademark);
        dest.writeString(model);
        if (price == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(price);
        }
        if (discount == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(discount);
        }
        dest.writeString(sex);
        dest.writeString(type);
        dest.writeString(labels);
        dest.writeValue(photoBitmap);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
