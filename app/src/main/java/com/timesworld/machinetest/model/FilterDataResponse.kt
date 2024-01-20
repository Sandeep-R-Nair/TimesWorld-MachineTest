package com.timesworld.machinetest.model

data class FilterDataResponse(val data: List<FilterCategory>)

data class FilterCategory(
    val name: String,
    val slug: String,
    val taxonomies: List<Taxonomy>
)

data class Taxonomy(
    val id: Int,
    val Guid: String,
    val slug: String,
    val name: String
)
