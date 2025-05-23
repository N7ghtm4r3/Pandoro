package com.tecknobit.pandoro.services;

import com.tecknobit.apimanager.annotations.Structure;
import com.tecknobit.equinoxbackend.environment.services.builtin.entity.EquinoxItem;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

import java.io.Serializable;

import static com.tecknobit.equinoxcore.helpers.CommonKeysKt.NAME_KEY;

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
     * {@code name} of the item
     */
    @Column(name = NAME_KEY)
    protected final String name;

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
     * Method to get {@link #name} instance
     *
     * @return {@link #name} instance as {@link String}
     */
    public String getName() {
        return name;
    }

}
