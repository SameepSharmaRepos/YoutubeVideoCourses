package com.example.videoteacher.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Courses")
data class Course(
    @PrimaryKey
    val id: String,
    val name: String,
    val pubId: String,
    val pubName: String,
    val description: String,
    val image: String,
    val category: String,
    val source: String,
    val isSaved: Boolean,
    val isDownloaded: Boolean,
    var totalVideos: Int,
    val publishedOn: Date,
    val lastPlayed: Date? = null,
    val addedOn: Date
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readSerializable() as Date,
        parcel.readSerializable() as Date,
        parcel.readSerializable() as Date
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(pubId)
        parcel.writeString(pubName)
        parcel.writeString(description)
        parcel.writeString(image)
        parcel.writeString(category)
        parcel.writeString(source)
        parcel.writeByte(if (isSaved) 1 else 0)
        parcel.writeByte(if (isDownloaded) 1 else 0)
        parcel.writeInt(totalVideos)
        parcel.writeSerializable(publishedOn)
        parcel.writeSerializable(lastPlayed)
        parcel.writeSerializable(addedOn)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Course> {
        override fun createFromParcel(parcel: Parcel): Course {
            return Course(parcel)
        }

        override fun newArray(size: Int): Array<Course?> {
            return arrayOfNulls(size)
        }
    }
}