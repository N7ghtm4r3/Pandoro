package com.tecknobit.pandoro.services;

import com.tecknobit.apimanager.annotations.Structure;
import com.tecknobit.equinox.environment.records.EquinoxItem;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import org.json.JSONObject;

import java.io.Serializable;

import static com.tecknobit.equinox.environment.records.EquinoxUser.NAME_KEY;

/**
 * The {@code PandoroItem} class is useful to give the base details structure for a <b>Pandoro's item class</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxItem
 * @see Serializable
 */
@Entity
@Structure
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class PandoroItem extends EquinoxItem implements Serializable {

    /**
     * {@code AUTHOR_KEY} author key
     */
    public static final String AUTHOR_KEY = "author";

    /**
     * {@code CREATION_DATE_KEY} creation date key
     */
    public static final String CREATION_DATE_KEY = "creation_date";

    /**
     * {@code name} of the item
     */
    @Column(name = NAME_KEY)
    protected final String name;

    /**
     * Constructor to init a {@link PandoroItem} object
     *
     * @param jItem: Pandoro's item details as {@link JSONObject}
     */
    public PandoroItem(JSONObject jItem) {
        super(jItem);
        name = hItem.getString(NAME_KEY);
    }

    /**
     * Constructor to init a {@link PandoroItem} object
     *
     * @param id:   identifier of the item
     * @param name: of the item
     */
    public PandoroItem(String id, String name) {
        super(id);
        this.name = name;
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

}
