package com.tecknobit.pandoro.records.structures;

import com.tecknobit.apimanager.annotations.Structure;
import com.tecknobit.apimanager.formatters.JsonHelper;
import jakarta.persistence.Transient;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * The {@code PandoroItemStructure} class is useful to give the base details structure for a <b>Pandoro's item class</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see Serializable
 */
@Structure
public abstract class PandoroItemStructure implements Serializable {

    /**
     * {@code hItem} the helper to manage the JSON information
     */
    @Transient
    protected final JsonHelper hItem;

    /**
     * Constructor to init a {@link PandoroItemStructure} object
     *
     * @param jItem: the helper to manage the JSON information
     */
    public PandoroItemStructure(JSONObject jItem) {
        if (jItem == null)
            jItem = new JSONObject();
        hItem = new JsonHelper(jItem);
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
