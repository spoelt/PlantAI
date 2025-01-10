package com.spoelt.plant_ai.domain.model

data class Plant(
    val id: Int = 0,
    val name: String,
    val photo: String?,
    val temperature: String?,
    val humidity: String?,
    val light: String?,
    val fertilizer: String?,
    val additionalTips: String?,
)
