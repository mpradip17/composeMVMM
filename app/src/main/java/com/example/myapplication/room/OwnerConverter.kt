package com.example.myapplication.room

import androidx.room.TypeConverter
import com.example.myapplication.network.response.License
import com.google.gson.Gson

class LicenseConverter {

    @TypeConverter
    fun fromLicense(license: License?): String? {
        return license?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toLicense(licenseString: String?): License? {
        return licenseString?.let { Gson().fromJson(it, License::class.java) }
    }
}