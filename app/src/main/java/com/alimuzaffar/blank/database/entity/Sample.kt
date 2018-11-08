package com.alimuzaffar.blank.database.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
class Sample {
    @PrimaryKey
    @SerializedName("id")
    @Expose
    var id: String

    @SerializedName("login")
    @Expose
    var login: String? = null

    @SerializedName("avatar_url")
    @Expose
    var avatar_url: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("company")
    @Expose
    var company: String? = null

    @SerializedName("blog")
    @Expose
    var blog: String? = null

    @Expose
    var lastRefresh: Date? = null

    // --- CONSTRUCTORS ---

    constructor(id: String) {
        this.id = id
    }

    @Ignore
    constructor(id: String, login: String, avatar_url: String, name: String, company: String, blog: String, lastRefresh: Date) {
        this.id = id
        this.login = login
        this.avatar_url = avatar_url
        this.name = name
        this.company = company
        this.blog = blog
        this.lastRefresh = lastRefresh
    }
}
