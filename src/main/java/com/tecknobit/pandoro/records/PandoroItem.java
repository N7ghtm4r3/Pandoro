package com.tecknobit.pandoro.records;

import com.tecknobit.apimanager.annotations.Structure;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * The {@code PandoroItem} class is useful to give the base details structure for a <b>Pandoro's item class</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Serializable
 */
@Structure
public abstract class PandoroItem implements Serializable {

    /**
     * {@code id} identifier of the item
     */
    protected final String id;

    /**
     * {@code name} of the item
     */
    protected final String name;

    /**
     * Constructor to init a {@link PandoroItem} object
     *
     * @param id:   identifier of the item
     * @param name: of the item
     */
    public PandoroItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Method to get {@link #id} instance <br>
     * No-any params required
     *
     * @return {@link #id} instance as {@link String}
     */
    public String getId() {
        return id;
    }

    /**
     * Method to get {@link #name} instance <br>
     * No-any params required
     *
     * @return {@link #name} instance as {@link String}
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a string representation of the object <br>
     * No-any params required
     *
     * @return a string representation of the object as {@link String}
     */
    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }

}
