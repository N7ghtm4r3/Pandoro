package com.tecknobit.pandoro.records.structures;

import com.tecknobit.apimanager.annotations.Structure;
import jakarta.persistence.*;
import org.json.JSONObject;

import java.io.Serializable;

import static com.tecknobit.pandoro.controllers.PandoroController.IDENTIFIER_KEY;
import static com.tecknobit.pandoro.services.UsersHelper.NAME_KEY;

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
     * {@code id} identifier of the item
     */
    @Id
    @Column(name = IDENTIFIER_KEY)
    protected final String id;

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
        id = hItem.getString(IDENTIFIER_KEY);
        name = hItem.getString(NAME_KEY);
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
