package dev.sasikanth.gaze.data

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate

@Keep
@Parcelize
@Entity(tableName = "apods")
data class APod(
    @PrimaryKey
    @ColumnInfo(name = "date")
    val date: LocalDate,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "explanation")
    val explanation: String,
    @ColumnInfo(name = "copyright")
    val copyright: String?,
    @SerializedName("media_type")
    @ColumnInfo(name = "media_type")
    val mediaType: String?,
    @SerializedName("service_version")
    @ColumnInfo(name = "service_version")
    val serviceVersion: String?,
    @SerializedName("url")
    @ColumnInfo(name = "url")
    val thumbnailUrl: String?,
    @SerializedName("hdurl")
    @ColumnInfo(name = "hd_url")
    val hdUrl: String?
) : Parcelable
