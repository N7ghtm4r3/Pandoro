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

/**
 * The **ListManager** interface is useful to manage a list of items in the UI
 *
 * @author N7ghtm4r3 - Tecknobit
 */
interface ListManager {

    /**
     * Function to refresh a list of items to display in the UI
     *
     * No-any params required
     */
    fun refreshValues()

    /**
     * Function to check whether the list to display in the UI need to be refreshed due changes
     *
     * @param currentList: the current list displayed
     * @param newList: the new hypothetical list to set and display
     * @param T: the type of the items of the lists
     *
     * @return whether refresh the list as [Boolean]
     */
    fun <T : PandoroItemStructure> needToRefresh(currentList: List<T>, newList: List<T>): Boolean {
        return ((currentList.isEmpty() && newList.isNotEmpty()) ||
                (JSONArray(currentList).toString() != JSONArray(newList).toString()))
    }

}

/**
 * The **SingleItemManager** interface is useful to manage a single item in the UI
 *
 * @author N7ghtm4r3 - Tecknobit
 */
interface SingleItemManager {

    /**
     * Function to refresh an item to display in the UI
     *
     * No-any params required
     */
    fun refreshItem() {}

    /**
     * Function to check whether the item to display in the UI need to be refreshed due changes
     *
     * @param currentItem: the current item displayed
     * @param newItem: the new hypothetical item to set and display
     * @param T: the type of the item
     *
     * @return whether refresh the item as [Boolean]
     */
    fun <T : PandoroItemStructure> needToRefresh(currentItem: T, newItem: T): Boolean {
        return currentItem.toString() != newItem.toString()
    }

}