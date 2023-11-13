package com.tecknobit.pandoro.records;

import com.tecknobit.apimanager.annotations.Structure;
import com.tecknobit.apimanager.formatters.JsonHelper;
import jakarta.persistence.Transient;
import org.json.JSONObject;

import java.io.Serializable;

@Structure
public abstract class PandoroItemStructure implements Serializable {

    @Transient
    protected final JsonHelper hItem;

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
