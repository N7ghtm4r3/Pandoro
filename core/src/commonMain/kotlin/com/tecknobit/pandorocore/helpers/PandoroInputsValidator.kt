package com.tecknobit.pandorocore.helpers

import com.tecknobit.equinoxcore.annotations.Validator
import com.tecknobit.equinoxcore.helpers.InputsValidator
import com.tecknobit.pandorocore.enums.RepositoryPlatform.Companion.isValidPlatform

object PandoroInputsValidator : InputsValidator() {

    /**
     * `PROJECT_NAME_MAX_LENGTH` the max length of the name for a project
     */
    const val PROJECT_NAME_MAX_LENGTH: Int = 25

    /**
     * `PROJECT_DESCRIPTION_MAX_LENGTH` the max length of the description for a project
     */
    const val PROJECT_DESCRIPTION_MAX_LENGTH: Int = 65535

    /**
     * `GROUP_NAME_MAX_LENGTH` the max length of the name for a group
     */
    const val GROUP_NAME_MAX_LENGTH: Int = 25

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
     * `URL_REGEX` regular expression to validate the urls value
     */
    // TODO: TO REMOVE WHEN FIXED INTO InputsValidator
    const val URL_REGEX =
        "^[a-zA-Z][a-zA-Z0-9+.-]*://(([a-zA-Z0-9\\-]+\\.)+[a-zA-Z]{2,6}|\\d{1,3}(?:\\.\\d{1,3}){3})(?::\\d{1,5})?(/\\S*)?(\\?(\\S*))?(#(\\S*))?$"

    /**
     * `urlValidator` helper to validate the urls values
     */
    // TODO: TO REMOVE WHEN FIXED INTO InputsValidator
    val urlValidator = Regex(URL_REGEX)

    /**
     * Function to check if all the notes of the list are correct
     *
     * @param notes: list of notes
     * @return whether all the notes of the list are correct as boolean
     */
    @Validator
    fun areNotesValid(
        notes: List<String?>?,
    ): Boolean {
        if (notes == null)
            return false
        notes.forEach { note ->
            if (!isContentNoteValid(note))
                return false
        }
        return true
    }

    /**
     * Function to check the validity of a content for a note
     *
     * @param content: content to check
     * @return whether the content is valid as [Boolean]
     */
    @Validator
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
    @Validator
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
    @Validator
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
    @Validator
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
    @Validator
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
    @Validator
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
    @Validator
    fun isValidRepository(
        repository: String?,
    ): Boolean {
        return repository != null && repository.isEmpty() || (urlValidator.matches(repository!!) &&
                isValidPlatform(repository))
    }

}