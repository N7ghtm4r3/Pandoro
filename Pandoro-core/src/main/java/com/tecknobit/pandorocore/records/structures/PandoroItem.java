package com.tecknobit.pandorocore.records.structures;

import com.tecknobit.apimanager.annotations.Structure;
import com.tecknobit.pandorocore.records.users.PublicUser;
import jakarta.persistence.*;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * The {@code PandoroItem} class is useful to give the base details structure for a <b>Pandoro's item class</b>
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see PandoroItemStructure
 * @see Serializable
 */
@Entity
@Structure
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class PandoroItem extends PandoroItemStructure {

    /**
     * {@code IDENTIFIER_KEY} identifier key
     */
    public static final String IDENTIFIER_KEY = "id";

    /**
     * {@code AUTHOR_KEY} author key
     */
    public static final String AUTHOR_KEY = "author";

    /**
     * {@code CREATION_DATE_KEY} creation date key
     */
    public static final String CREATION_DATE_KEY = "creation_date";

    /**
     * {@code id} identifier of the item
     */
    @Id
    @Column(name = IDENTIFIER_KEY)
    protected final String id;

    /**
     * {@code name} of the item
     */
    @Column(name = PublicUser.NAME_KEY)
    protected final String name;

    /**
     * Constructor to init a {@link PandoroItem} object
     *
     * @param jItem: Pandoro's item details as {@link JSONObject}
     */
    public PandoroItem(JSONObject jItem) {
        super(jItem);
        id = hItem.getString(IDENTIFIER_KEY);
        name = hItem.getString(PublicUser.NAME_KEY);
    }

    /**
     * Constructor to init a {@link PandoroItem} object
     *
     * @param id:   identifier of the item
     * @param name: of the item
     */
    public PandoroItem(String id, String name) {
        super(null);
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

}
