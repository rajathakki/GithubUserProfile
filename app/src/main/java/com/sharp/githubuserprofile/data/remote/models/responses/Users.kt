package com.sharp.githubuserprofile.data.remote.models.responses

import com.squareup.moshi.Json

data class Users(
    val name: String?,
    @Json(name = "id") var id: Int,
    @Json(name = "node_id") var nodeId: String?,
    @Json(name = "avatar_url") var avatar: String?,
    @Json(name = "gravatar_id") var gravatarId: String?,
    @Json(name = "url") var url: String?,
    @Json(name = "html_url") var htmlUrl: String?,
    @Json(name = "followers_url") var followers: String?,
    @Json(name = "following_url") var followings: String?,
    @Json(name = "gists_url") var gists: String?,
    @Json(name = "starred_url") var starred: String?,
    @Json(name = "subscriptions") var subscriptions: String?,
    @Json(name = "organizations") var organizations: String?,
    @Json(name = "repos_url") var repo: String?,
    @Json(name = "events_url") var events: String?,
    @Json(name = "received_event") var receivedEvent: String?,
    @Json(name = "type") var type: String?,
    @Json(name = "site_admin") var siteAdmin: Boolean,
)
