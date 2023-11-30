package com.tecknobit.pandoro.helpers;

import com.tecknobit.apimanager.annotations.Wrapper;
import com.tecknobit.apimanager.exceptions.SaveData;

import java.security.NoSuchAlgorithmException;
import java.util.prefs.Preferences;

import static com.tecknobit.apimanager.apis.APIRequest.SHA256_ALGORITHM;
import static com.tecknobit.apimanager.apis.APIRequest.base64Digest;
import static java.util.UUID.randomUUID;

/**
 * The {@code ServerProtector} class is useful to protect the server from the unauthorized accesses <br>
 * This class will generate a secret to share with the users allowed to use this server
 *
 * @author N7ghtm4r3 - Tecknobit
 */
public class ServerProtector {

    /**
     * {@code SERVER_SECRET_KEY} the server secret key
     */
    public static final String SERVER_SECRET_KEY = "server_secret";

    /**
     * {@code RECREATE_SERVER_SECRET_COMMAND} command to recreate the server secret
     */
    public static final String RECREATE_SERVER_SECRET_COMMAND = "rss";

    /**
     * {@code DELETE_SERVER_SECRET_COMMAND} command to delete the current server secret
     */
    public static final String DELETE_SERVER_SECRET_COMMAND = "dss";

    /**
     * {@code DELETE_SERVER_SECRET_AND_INTERRUPT_COMMAND} command to delete the current server secret and interrupt the
     * server workflow
     */
    public static final String DELETE_SERVER_SECRET_AND_INTERRUPT_COMMAND = "dssi";

    /**
     * {@code preferences} instance to manage the storage of the server secret
     */
    private final Preferences preferences;

    /**
     * {@code saveMessage} the message to print when the server secret has been generated
     *                   the start of the message is <b>"Note: is not an error, but is an alert!
     *                   Please you should safely save: server_secret_token_generated"</b>
     */
    private final String saveMessage;

    /**
     * Constructor to init the {@link ServerProtector}
     *
     * @param storagePath:  instance to manage the storage of the server secret
     * @param saveMessage: the message to print when the server secret has been generated,
     *                   the start of the message is <b>"Note: is not an error, but is an alert!
     *                   Please you should safely save: server_secret_token_generated"</b>
     */
    public ServerProtector(String storagePath, String saveMessage) {
        this.preferences = Preferences.userRoot().node(storagePath);
        if (saveMessage.startsWith(" "))
            saveMessage = saveMessage.trim();
        this.saveMessage = " " + saveMessage;
    }

    /**
     * Method to launch the server protector
     *
     * @param args: the arguments where check if there are any commands to execute
     * @throws NoSuchAlgorithmException when the hash algorithm is not valid
     * @throws SaveData                 to permit the administrator of the server to store safely the secret generated
     * @apiNote the commands scheme:
     * <ul>
     *     <li>
     *         <b>rss</b> -> launch your java application with "rss" to recreate the server secret <br>
     *                       e.g java -jar your_jar.jar rss
     *     </li>
     *     <li>
     *         <b>dss</b> -> launch your java application with "dss" to delete the current server secret <br>
     *                       e.g java -jar your_jar.jar dss
     *     </li>
     *     <li>
     *         <b>dssi</b> -> launch your java application with "dssi" to delete the current server secret and interrupt
     *                        the current workflow of the server <br>
     *                        e.g java -jar your_jar.jar dssi
     *     </li>
     * </ul>
     * @implNote if no commands are found and if there is already a server secret will be performed the normal workflow of the server,
     * if is not exists will be launch the {@link #generateServerSecret()}
     */
    public void launch(String[] args) throws NoSuchAlgorithmException, SaveData {
        if (getCurrentServerSecret() == null)
            generateServerSecret();
        for (String arg : args) {
            if (arg.equals(RECREATE_SERVER_SECRET_COMMAND))
                recreateServerSecret();
            if (arg.equals(DELETE_SERVER_SECRET_COMMAND))
                deleteServerSecret();
            if (arg.equals(DELETE_SERVER_SECRET_AND_INTERRUPT_COMMAND))
                deleteServerSecret(true);
        }
    }

    /**
     * Method to generate and store a new server secret <br>
     * No-any params required
     *
     * @throws NoSuchAlgorithmException when the hash algorithm is not valid
     * @throws SaveData                 to permit the administrator of the server to store safely the secret generated
     */
    public void generateServerSecret() throws NoSuchAlgorithmException, SaveData {
        String serverSecret = randomUUID().toString().replaceAll("-", "");
        storeServerSecret(serverSecret);
        throw new SaveData(serverSecret + saveMessage);
    }

    /**
     * Method to regenerate and store a new server secret, overwriting the current secret <br>
     * No-any params required
     *
     * @throws NoSuchAlgorithmException when the hash algorithm is not valid
     * @throws SaveData                 to permit the administrator of the server to store safely the secret generated
     */
    public void recreateServerSecret() throws NoSuchAlgorithmException, SaveData {
        generateServerSecret();
    }

    /**
     * Method to store a new server secret
     *
     * @param serverSecret: the server secret to store
     * @throws NoSuchAlgorithmException when the hash algorithm is not valid
     */
    private void storeServerSecret(String serverSecret) throws NoSuchAlgorithmException {
        preferences.put(SERVER_SECRET_KEY, hash(serverSecret));
    }

    /**
     * Method to compare a server secret with the current stored by the server
     *
     * @param serverSecretToCompare: the server secret to compare with the current stored
     * @return whether the input server secret matches with the current stored by the server
     */
    public boolean serverSecretMatches(String serverSecretToCompare) {
        String currentServerSecret = getCurrentServerSecret();
        if (serverSecretToCompare == null)
            return false;
        try {
            return currentServerSecret.equals(hash(serverSecretToCompare));
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    /**
     * Method to delete the current server secret <br>
     * No-any params required
     */
    @Wrapper
    public void deleteServerSecret() {
        deleteServerSecret(false);
    }

    /**
     * Method to delete the current server secret
     *
     * @param interruptRuntime: whether interrupt the server workflow after server secret deletion
     */
    public void deleteServerSecret(boolean interruptRuntime) {
        preferences.remove(SERVER_SECRET_KEY);
        if (interruptRuntime)
            throw new RuntimeException("The server secret has been deleted");
    }

    /**
     * Method to get the current server secret <br>
     * No-any params required
     *
     * @return the current server secret as {@link String}
     */
    private String getCurrentServerSecret() {
        return preferences.get(SERVER_SECRET_KEY, null);
    }

    /**
     * Method to hash a server secret
     *
     * @param serverSecret: the server secret to hash
     * @return the server secret hashed as {@link String}
     * @throws NoSuchAlgorithmException when the hash algorithm is not valid
     */
    private String hash(String serverSecret) throws NoSuchAlgorithmException {
        return base64Digest(serverSecret, SHA256_ALGORITHM);
    }

}
