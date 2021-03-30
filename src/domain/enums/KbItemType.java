package domain.enums;

/**
 * The enum Kb item type.
 */
public enum KbItemType {
    /**
     * Hardware kb item type.
     */
    HARDWARE

    ,
    /**
     * Software kb item type.
     */
    SOFTWARE

    ,
    /**
     * Infrastructure kb item type.
     */
    INFRASTRUCTURE

    ,
    /**
     * Database kb item type.
     */
    DATABASE

    ,
    /**
     * Network kb item type.
     */
    NETWORK

    ,
    /**
     * Other kb item type.
     */
    OTHER
    
    ,
    /**
     * kb item has been archived (logical delete)
     * visible for support manager, invisible for the others
     */
    ARCHIVED

}
