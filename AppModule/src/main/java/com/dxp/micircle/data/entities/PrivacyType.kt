package com.dxp.micircle.data.entities

enum class PrivacyType(val value: Int) {
    PUBLIC(0), FOLLOWERS(1), RESTRICTED(2), SELF(4)
}