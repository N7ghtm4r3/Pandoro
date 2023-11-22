package com.tecknobit.pandoro.helpers.ui

import com.tecknobit.pandoro.records.structures.PandoroItemStructure
import org.json.JSONArray

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

    fun <T : PandoroItemStructure> needToRefresh(currentList: List<T>, newList: List<T>): Boolean {
        return ((currentList.isEmpty() && newList.isNotEmpty()) ||
                (JSONArray(currentList).toString() != JSONArray(newList).toString()))
    }

}

interface SingleItemManager {

    fun refreshItem() {}

    fun <T : PandoroItemStructure> needToRefresh(currentItem: T, newItem: T): Boolean {
        return currentItem.toString() != newItem.toString()
    }

}