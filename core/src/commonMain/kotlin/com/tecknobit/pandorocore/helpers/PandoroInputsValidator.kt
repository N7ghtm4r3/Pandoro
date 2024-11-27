package com.tecknobit.pandorocore.helpers

import com.tecknobit.equinoxcore.helpers.InputsValidator

object PandoroInputsValidator : InputsValidator() {

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
        "^(https?|ftp|file|mailto|data|ws|wss)://(?:[A-Za-z0-9-]+\\.)+[A-Za-z]{2,}(?::\\d{2,5})?(?:/[A-Za-z0-9%&=?./_-]*)?(?:#[A-Za-z0-9_-]*)?$"

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
     * Function to check the validity of a members list
     *
     * @param members: members list to check
     * @return whether the members list is valid as [Boolean]
     */
    fun checkMembersValidity(
        members: List<String?>?,
    ): Boolean {
        if (members.isNullOrEmpty())
            return false
        var membersCorrect = true
        for (member in members) {
            membersCorrect = isEmailValid(member)
            if (!membersCorrect)
                break
        }
        return membersCorrect
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
     * Function to check the validity of a project short description
     *
     * @param shortDescription: project short description to check
     * @return whether the project short description is valid as [Boolean]
     */
    fun isValidProjectShortDescription(
        shortDescription: String?,
    ): Boolean {
        return isInputValid(shortDescription) && shortDescription!!.length in 1..PROJECT_SHORT_DESCRIPTION_MAX_LENGTH
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

    private fun isInputValid(field: String?): Boolean {
        return !field.isNullOrEmpty()
    }

}