package com.tecknobit.pandorocore.helpers

import com.tecknobit.equinoxcore.helpers.InputsValidator
import com.tecknobit.pandorocore.enums.RepositoryPlatform.Companion.isValidPlatform

object PandoroInputsValidator : InputsValidator() {

    /**
     * `WRONG_PROJECT_ICON_MESSAGE` the message to warn the user about an invalid icon for a project
     */
    const val WRONG_PROJECT_ICON_MESSAGE = "wrong_project_icon_key"

    /**
     * `WRONG_GROUP_LOGO_MESSAGE` the message to warn the user about an invalid logo for a group
     */
    const val WRONG_GROUP_LOGO_MESSAGE = "wrong_group_logo_key"

    /**
     * `PROJECT_NAME_MAX_LENGTH` the max length of the name for a project
     */
    const val PROJECT_NAME_MAX_LENGTH: Int = 15

    /**
     * `PROJECT_DESCRIPTION_MAX_LENGTH` the max length of the description for a project
     */
    const val PROJECT_DESCRIPTION_MAX_LENGTH: Int = 65535

    /**
     * `GROUP_NAME_MAX_LENGTH` the max length of the name for a group
     */
    const val GROUP_NAME_MAX_LENGTH: Int = 15

    /**
     * `GROUP_DESCRIPTION_MAX_LENGTH` the max description of the name for a group
     */
    const val GROUP_DESCRIPTION_MAX_LENGTH: Int = 65535

    /**
     * `NOTE_CONTENT_MAX_LENGTH` the max length of the content for a note
     */
    const val NOTE_CONTENT_MAX_LENGTH: Int = 65535

    /**
     * `TARGET_VERSION_MAX_LENGTH` the max length of the target version for an update
     */
    const val TARGET_VERSION_MAX_LENGTH: Int = 20

    /**
     * `EMAIL_REGEX` regular expression to validate the emails value
     */
    @Deprecated(message = "To use the original one", level = DeprecationLevel.WARNING)
    private const val EMAIL_REGEX =
        "^(?![.])(?!.*\\.\\.{2})[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[A-Za-z0-9-]+\\.)+[A-Za-z]{2,}$"

    /**
     * `URL_REGEX` regular expression to validate the urls value
     */
    @Deprecated(message = "To use the original one", level = DeprecationLevel.WARNING)
    private const val URL_REGEX =
        "^(https?|ftp|file|mailto|data|ws|wss)://(?:[A-Za-z0-9-]+\\.)*[A-Za-z0-9-]+(?::\\d{1,5})?(?:/[A-Za-z0-9%&=?./_-]*)?(?:#[A-Za-z0-9_-]*)?\$"

    /**
     * `emailValidator` helper to validate the emails values
     */
    @Deprecated(message = "To use the original one", level = DeprecationLevel.WARNING)
    private val emailValidator = Regex(EMAIL_REGEX)

    /**
     * `urlValidator` helper to validate the urls values
     */
    @Deprecated(message = "To use the original one", level = DeprecationLevel.WARNING)
    private val urlValidator = Regex(URL_REGEX)

    @Deprecated(
        message = "USE THE BUILT-IN ONE",
        replaceWith = ReplaceWith("isHostValid()")
    )
    fun isHostAddressValid(
        hostAddress: String,
    ): Boolean {
        return urlValidator.matches(hostAddress)
    }

    /**
     * Function to check if all the notes of the list are correct
     *
     * @param notes: list of notes
     * @return whether all the notes of the list are correct as boolean
     */
    fun areNotesValid(
        notes: List<String?>?,
    ): Boolean {
        if (notes == null)
            return false
        var notesCorrect = notes.isNotEmpty()
        for (note in notes) {
            notesCorrect = isContentNoteValid(note)
            if (!notesCorrect)
                break
        }
        return notesCorrect
    }

    // TODO: TO IMPLEMENT
    /**
     * Function to check whether the change notes are all done before the publishing of the update
     *
     * @param changeNotes: the change notes to check
     * @return whether the change notes are all done before the publishing of the update as [Boolean]
     *
    fun areAllChangeNotesDone(
    changeNotes: List<Note>?
    ): Boolean {
    if (changeNotes == null)
    return false
    changeNotes.forEach { note ->
    if (!note.isMarkedAsDone)
    return false
    }
    return true
    }*/

    /**
     * Function to check the validity of a content for a note
     *
     * @param content: content to check
     * @return whether the content is valid as [Boolean]
     */
    fun isContentNoteValid(
        content: String?,
    ): Boolean {
        return isInputValid(content) && content!!.length in 1..NOTE_CONTENT_MAX_LENGTH
    }

    /**
     * Function to check the validity of a group name
     *
     * @param groupName: group name to check
     * @return whether the group name is valid as [Boolean]
     */
    fun isGroupNameValid(
        groupName: String?,
    ): Boolean {
        return isInputValid(groupName) && groupName!!.length in 1..GROUP_NAME_MAX_LENGTH
    }

    /**
     * Function to check the validity of a group description
     *
     * @param groupDescription: group description to check
     * @return whether the group description is valid as [Boolean]
     */
    fun isGroupDescriptionValid(
        groupDescription: String?,
    ): Boolean {
        return isInputValid(groupDescription) && groupDescription!!.length in 1..GROUP_DESCRIPTION_MAX_LENGTH
    }

    /**
     * Function to check the validity of a project name
     *
     * @param projectName: project name to check
     * @return whether the project name is valid as [Boolean]
     */
    fun isValidProjectName(
        projectName: String?,
    ): Boolean {
        return isInputValid(projectName) && projectName!!.length in 1..PROJECT_NAME_MAX_LENGTH
    }

    /**
     * Function to check the validity of a project description
     *
     * @param description: project description to check
     * @return whether the project description is valid as [Boolean]
     */
    fun isValidProjectDescription(
        description: String?,
    ): Boolean {
        return isInputValid(description) && description!!.length in 1..PROJECT_DESCRIPTION_MAX_LENGTH
    }

    /**
     * Function to check the validity of a version
     *
     * @param version: target version to check
     * @return whether the version is valid as [Boolean]
     */
    fun isValidVersion(
        version: String?,
    ): Boolean {
        return isInputValid(version) && version!!.length in 1..TARGET_VERSION_MAX_LENGTH
    }

    /**
     * Function to check the validity of a repository url
     *
     * @param repository: repository to check
     * @return whether the repository is valid as [Boolean]
     */
    fun isValidRepository(
        repository: String?,
    ): Boolean {
        return repository != null && repository.isEmpty() || (urlValidator.matches(repository!!) &&
                isValidPlatform(repository))
    }

    @Deprecated(
        message = "USE THE BUILT-IN ONE",
        replaceWith = ReplaceWith("the super method")
    )
    private fun isInputValid(field: String?): Boolean {
        return !field.isNullOrEmpty()
    }

}