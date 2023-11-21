package com.tecknobit.pandoro.helpers.ui

/**
 * the primary color value
 */
const val PRIMARY_COLOR: String = "#07020d"

/**
 * the background color value
 */
const val BACKGROUND_COLOR: String = "#f9f6f0"

/**
 * the green color value
 */

const val GREEN_COLOR: String = "#61892f"

/**
 * the yellow color value
 */

const val YELLOW_COLOR: String = "#bfae19"

/**
 * the red color value
 */
const val RED_COLOR: String = "#A81515"

interface ListManager {

    fun refreshValues()

}

interface SingleItemManager {

    fun refreshItem() {}

}