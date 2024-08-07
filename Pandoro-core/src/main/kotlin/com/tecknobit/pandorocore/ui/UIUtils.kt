package com.tecknobit.pandorocore.ui

import com.tecknobit.apimanager.annotations.Wrapper
import com.tecknobit.apimanager.formatters.JsonHelper
import com.tecknobit.equinox.environment.records.EquinoxItem
import com.tecknobit.equinox.environment.records.EquinoxUser.*
import com.tecknobit.pandorocore.records.ProjectUpdate
import com.tecknobit.pandorocore.records.structures.PandoroItem.IDENTIFIER_KEY
import com.tecknobit.pandorocore.records.users.User.LANGUAGE_KEY
import org.json.JSONArray
import java.util.*

/**
 * the primary color value
 */
const val PRIMARY_COLOR: String = "#07020d"

/**
 * the background color value
 */
const val BACKGROUND_COLOR: String = "#f9f6f0"

/**
 * the ice gray color of the application
 */
const val ICE_GRAY_COLOR: String = "#dae2ff"

/**
 * the dwarf white color of the application
 */
const val DWARF_WHITE_COLOR: String = "#fafdfd"

/**
 * the custom gray color value
 */
const val CUSTOM_GRAY_COLOR: String = "#e6e8e9"

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
    fun <T : EquinoxItem> needToRefresh(currentList: List<T>, newList: List<T>): Boolean {
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
    fun <T : EquinoxItem> needToRefresh(currentItem: T, newItem: T): Boolean {
        return currentItem.toString() != newItem.toString()
    }

}

/**
 * Function to format as **markdown** the notes of an update
 *
 * @param update: the update from format the notes
 *
 * @return the notes of an update formatted as markdown as [String]
 */
fun formatNotesAsMarkdown(update: ProjectUpdate): String {
    val builder = StringBuilder()
    update.notes.forEach { note ->
        builder.append("- ").append(note.content).append("\n")
    }
    return builder.toString()
}

/**
 * This **LocalUser** class is useful to manage the credentials of the user in local
 *
 * @author Tecknobit - N7ghtm4r3
 */
abstract class LocalUser {

    companion object {

        /**
         * **SERVER_ADDRESS_KEY** -> server address key
         */
        const val SERVER_ADDRESS_KEY = "server_address"

    }

    /**
     * **host** -> the host to used in the requests
     */
    var host: String? = null

    /**
     * Function to init the user credentials
     *
     * No-any params required
     */
    abstract fun initUserCredentials()

    /**
     * Function to init the user credentials
     *
     * @param response: the response of the auth request
     * @param host: the host to used in the requests
     * @param name: the name of the user
     * @param surname: the surname of the user
     * @param email: the email of the user
     * @param password: the password of the user
     * @param language: the language of the user
     */
    open fun initUserSession(
        response: JsonHelper,
        host: String?,
        name: String,
        surname: String,
        email: String?,
        password: String?,
        language: String?
    ) {
        storeUserValue(IDENTIFIER_KEY, response.getString(IDENTIFIER_KEY))
        storeUserValue(TOKEN_KEY, response.getString(TOKEN_KEY))
        storeHost(host)
        storeProfilePic(response.getString(PROFILE_PIC_KEY))
        storeName(name)
        storeSurname(surname)
        storeEmail(email)
        storePassword(password)
        storeLanguage(language)
        initUserCredentials()
    }

    /**
     * Function to store the host value
     *
     * @param host: the host to used in the requests
     */
    @Wrapper
    fun storeHost(host: String?) {
        storeUserValue(SERVER_ADDRESS_KEY, host, false)
        this.host = host
    }

    /**
     * Function to store the profile pic value
     *
     * @param profilePic: the profile pic of the user
     * @param refreshUser: whether refresh the user
     * @return the user profile picture path
     */
    @Wrapper
    open fun storeProfilePic(
        profilePic: String?,
        refreshUser: Boolean = false
    ): String {
        val profilePicValue = "$host/$profilePic"
        storeUserValue(PROFILE_PIC_KEY, profilePicValue, refreshUser)
        return profilePicValue
    }

    /**
     * Function to store the name value
     *
     * @param name: the name of the user
     */
    @Wrapper
    private fun storeName(name: String?) {
        storeUserValue(NAME_KEY, name, false)
    }

    /**
     * Function to store the surname value
     *
     * @param surname: the surname of the user
     */
    @Wrapper
    private fun storeSurname(surname: String?) {
        storeUserValue(SURNAME_KEY, surname, false)
    }

    /**
     * Function to store the email value
     *
     * @param email: the email of the user
     * @param refreshUser: whether refresh the user
     */
    @Wrapper
    fun storeEmail(
        email: String?,
        refreshUser: Boolean = false
    ) {
        var vEmail = email
        if (vEmail != null)
            vEmail = vEmail.lowercase(Locale.getDefault())
        storeUserValue(EMAIL_KEY, vEmail, refreshUser)
    }

    /**
     * Function to store the password value
     *
     * @param password: the password of the user
     * @param refreshUser: whether refresh the user
     */
    @Wrapper
    fun storePassword(
        password: String?,
        refreshUser: Boolean = false
    ) {
        storeUserValue(PASSWORD_KEY, password, refreshUser)
    }

    /**
     * Function to store the language value
     *
     * @param language: the language of the user
     * @param refreshUser: whether refresh the user
     */
    @Wrapper
    fun storeLanguage(
        language: String?,
        refreshUser: Boolean = false
    ) {
        storeUserValue(LANGUAGE_KEY, language, refreshUser)
    }

    /**
     * Function to store a user value
     *
     * @param key: the key of the value to store
     * @param value: the value to store
     * @param refreshUser: whether refresh the user
     */
    open fun storeUserValue(
        key: String,
        value: String?,
        refreshUser: Boolean = false
    ) {
        if (refreshUser)
            initUserCredentials()
    }

    /**
     * Function to disconnect the user and clear its properties stored
     *
     * No-any params required
     */
    abstract fun logout()

}