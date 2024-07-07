package com.tecknobit.pandorocore.helpers

import com.tecknobit.equinox.inputs.InputValidator
import com.tecknobit.pandorocore.helpers.InputsValidator.InputStatus.*
import com.tecknobit.pandorocore.records.Group.GROUP_DESCRIPTION_MAX_LENGTH
import com.tecknobit.pandorocore.records.Group.GROUP_NAME_MAX_LENGTH
import com.tecknobit.pandorocore.records.Note
import com.tecknobit.pandorocore.records.Note.NOTE_CONTENT_MAX_LENGTH
import com.tecknobit.pandorocore.records.Project.*
import com.tecknobit.pandorocore.records.Project.RepositoryPlatform.isValidPlatform
import com.tecknobit.pandorocore.records.ProjectUpdate.TARGET_VERSION_MAX_LENGTH
import org.apache.commons.validator.routines.UrlValidator

class InputsValidator : InputValidator() {

    companion object {

        /**
         * `urlValidator` helper to validate the urls values
         */
        private val urlValidator: UrlValidator = UrlValidator.getInstance()

        /**
         * Function to check the validity of the credentials
         *
         * @param email: email to check
         * @param password: password to check
         * @return whether the credentials are valid as [InputStatus]
         */
        fun areCredentialsValid(email: String?, password: String?): InputStatus {
            if (isEmailValid(email)) {
                return if (isPasswordValid(password))
                    OK
                else
                    WRONG_PASSWORD
            }
            return WRONG_EMAIL
        }

        /**
         * Function to check the validity of an input
         *
         * @param item: the item between **email** and **password**
         * @param input: input to check
         * @return whether the input is valid as [Boolean]
         */
        fun isInputValid(
            item: String,
            input: String?
        ): Boolean {
            return if (item == "email")
                isEmailValid(input)
            else
                isPasswordValid(input)
        }

        /**
         * Function to check if all the notes of the list are correct
         *
         * @param notes: list of notes
         * @return whether all the notes of the list are correct as boolean
         */
        fun areNotesValid(
            notes: List<String?>?
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

        /**
         * Function to check whether the change notes are all done before the publishing of the update
         *
         * @param changeNotes: the change notes to check
         * @return whether the change notes are all done before the publishing of the update as [Boolean]
         */
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
        }

        /**
         * Function to check the validity of a content for a note
         *
         * @param content: content to check
         * @return whether the content is valid as [Boolean]
         */
        fun isContentNoteValid(
            content: String?
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
            members: List<String?>?
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
            groupName: String?
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
            groupDescription: String?
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
            projectName: String?
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
            description: String?
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
            shortDescription: String?
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
            version: String?
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
            repository: String?
        ): Boolean {
            return repository != null && repository.isEmpty() || (urlValidator.isValid(repository) && isValidPlatform(
                repository
            ))
        }

    }

    /**
     * **InputStatus** -> list of available input statuses
     */
    enum class InputStatus {

        /**
         * **OK** -> input status
         */
        OK,

        /**
         * **WRONG_PASSWORD** -> input status
         */
        WRONG_PASSWORD,

        /**
         * **WRONG_EMAIL** -> input status
         */
        WRONG_EMAIL

    }

    /**
     * **ScreenType** -> list of available types for the connect screen
     */
    enum class ScreenType {

        /**
         * **SignUp** -> when the user need to sign up for the first time
         */
        SignUp,

        /**
         * **SignIn** -> when the user need to sign in its account
         */
        SignIn;

        /**
         * Function to create the title to show
         *
         * @param screenType: the type from create the title
         * @return title to show as [String]
         */
        fun createTitle(screenType: ScreenType): String {
            return when (screenType) {
                SignUp -> "Sign up"
                SignIn -> "Sign In"
            }
        }

        /**
         * Function to create the message to show
         *
         * @param screenType: the type from create the message
         * @return message to show as [String]
         */
        fun createMessage(screenType: ScreenType): String {
            return when (screenType) {
                SignIn -> "Are you new to Pandoro?"
                SignUp -> "Have an account?"
            }
        }

        /**
         * Function to create the title link to show
         *
         * @param screenType: the type from create the title link
         * @return title link to show as [String]
         */
        fun createTitleLink(screenType: ScreenType): String {
            return when (screenType) {
                SignUp -> "Sign In"
                SignIn -> "Sign up"
            }
        }

    }

}