package com.tecknobit.pandoro.helpers

import com.tecknobit.pandoro.helpers.InputStatus.*
import com.tecknobit.pandoro.records.Group.GROUP_DESCRIPTION_MAX_LENGTH
import com.tecknobit.pandoro.records.Group.GROUP_NAME_MAX_LENGTH
import com.tecknobit.pandoro.records.Note
import com.tecknobit.pandoro.records.Note.NOTE_CONTENT_MAX_LENGTH
import com.tecknobit.pandoro.records.Project.*
import com.tecknobit.pandoro.records.Project.RepositoryPlatform.isValidPlatform
import com.tecknobit.pandoro.records.ProjectUpdate.TARGET_VERSION_MAX_LENGTH
import com.tecknobit.pandoro.records.users.User.*
import org.apache.commons.validator.routines.EmailValidator
import org.apache.commons.validator.routines.UrlValidator

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

/**
 * **validator** -> the validator to check the validity of the emails
 */
private val validator: EmailValidator = EmailValidator.getInstance()

/**
 * **urlValidator** -> the validator to check the validity of the URLS
 */
private val urlValidator: UrlValidator = UrlValidator.getInstance()

/**
 * Function to check the validity of a server address
 *
 * @param serverAddress: server address to check
 * @return whether the server address is valid as [Boolean]
 */
fun isServerAddressValid(serverAddress: String?): Boolean {
    return serverAddress != null && urlValidator.isValid(serverAddress)
}

/**
 * Function to check the validity of a name
 *
 * @param name: name to check
 * @return whether the name is valid as [Boolean]
 */
fun isNameValid(name: String?): Boolean {
    return name != null && name.length in 1..USER_NAME_MAX_LENGTH
}

/**
 * Function to check the validity of a surname
 *
 * @param surname: surname to check
 * @return whether the surname is valid as [Boolean]
 */
fun isSurnameValid(surname: String?): Boolean {
    return surname != null && surname.length in 1..USER_SURNAME_MAX_LENGTH
}

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
fun isInputValid(item: String, input: String?): Boolean {
    return if (item == "email")
        isEmailValid(input)
    else
        isPasswordValid(input)
}

/**
 * Function to check the validity of an email
 *
 * @param email: email to check
 * @return whether the email is valid as [Boolean]
 */
fun isEmailValid(email: String?): Boolean {
    return email != null && validator.isValid(email) && email.length in 1..EMAIL_MAX_LENGTH
}

/**
 * Function to check the validity of a password
 *
 * @param password: password to check
 * @return whether the password is valid as [Boolean]
 */
fun isPasswordValid(password: String?): Boolean {
    return password != null && password.length in PASSWORD_MIN_LENGTH..PASSWORD_MAX_LENGTH
}

/**
 * Function to check if all the notes of the list are correct
 *
 * @param notes: list of notes
 * @return whether all the notes of the list are correct as boolean
 */
fun areNotesValid(notes: List<String?>?): Boolean {
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
fun areAllChangeNotesDone(changeNotes: List<Note>?): Boolean {
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
fun isContentNoteValid(content: String?): Boolean {
    return content != null && content.length in 1..NOTE_CONTENT_MAX_LENGTH
}

/**
 * Function to check the validity of a members list
 *
 * @param members: members list to check
 * @return whether the members list is valid as [Boolean]
 */
fun checkMembersValidity(members: List<String?>?): Boolean {
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
fun isGroupNameValid(groupName: String?): Boolean {
    return groupName != null && groupName.length in 1..GROUP_NAME_MAX_LENGTH
}

/**
 * Function to check the validity of a group description
 *
 * @param groupDescription: group description to check
 * @return whether the group description is valid as [Boolean]
 */
fun isGroupDescriptionValid(groupDescription: String?): Boolean {
    return groupDescription != null && groupDescription.length in 1..GROUP_DESCRIPTION_MAX_LENGTH
}

/**
 * Function to check the validity of a project name
 *
 * @param projectName: project name to check
 * @return whether the project name is valid as [Boolean]
 */
fun isValidProjectName(projectName: String?): Boolean {
    return projectName != null && projectName.length in 1..PROJECT_NAME_MAX_LENGTH
}

/**
 * Function to check the validity of a project description
 *
 * @param description: project description to check
 * @return whether the project description is valid as [Boolean]
 */
fun isValidProjectDescription(description: String?): Boolean {
    return description != null && description.length in 1..PROJECT_DESCRIPTION_MAX_LENGTH
}

/**
 * Function to check the validity of a project short description
 *
 * @param shortDescription: project short description to check
 * @return whether the project short description is valid as [Boolean]
 */
fun isValidProjectShortDescription(shortDescription: String?): Boolean {
    return shortDescription != null && shortDescription.length in 1..PROJECT_SHORT_DESCRIPTION_MAX_LENGTH
}

/**
 * Function to check the validity of a version
 *
 * @param version: target version to check
 * @return whether the version is valid as [Boolean]
 */
fun isValidVersion(version: String?): Boolean {
    return version != null && version.length in 1..TARGET_VERSION_MAX_LENGTH
}

/**
 * Function to check the validity of a repository url
 *
 * @param repository: repository to check
 * @return whether the repository is valid as [Boolean]
 */
fun isValidRepository(repository: String?): Boolean {
    return repository != null && repository.isEmpty() || (urlValidator.isValid(repository) && isValidPlatform(repository))
}