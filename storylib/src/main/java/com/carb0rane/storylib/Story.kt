package com.carb0rane.storylib

import android.os.Parcel
import android.os.Parcelable

data class Story(
    var storyImageURL: String ,
    var storyOwnerName: String ,
    var storyTime: String,

) : Parcelable {



    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )



    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(storyImageURL)
        dest.writeString(storyOwnerName)
        dest.writeString(storyTime)

    }

    companion object CREATOR : Parcelable.Creator<Story> {
        override fun createFromParcel(parcel: Parcel): Story {
            return Story(parcel)

        }

        override fun newArray(size: Int): Array<Story?> {
            return arrayOfNulls(size)
        }
    }

}