package org.dbiir.tristar.common;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum CCType {

    SER("SERIALIZABLE"),
    SI_ELT("SI_ELT"),
    RC_ELT("RC_ELT"),
    SI_FOR_UPDATE("SI_FOR_UPDATE"),
    /* Promote */
    RC_FOR_UPDATE("RC_FOR_UPDATE"),
    SI_TAILOR("SI_TAILOR"),
    RC_TAILOR("RC_TAILOR"),
    RC_TAILOR_LOCK("RC_TAILOR_LOCK"),
    DYNAMIC("DYNAMIC"),
    DYNAMIC_B("DYNAMIC_B"),
    DYNAMIC_A("DYNAMIC_A"),
    /* pure */
    RC("RC"),
    SI("SI"),
    SWITCH("SWITCH"),
    NUM_CC("NUM_CC");
    private final String name;

    CCType(String name) {
        this.name = name;
    }

    protected static final Map<Integer, CCType> idx_lookup = new HashMap<>();
    protected static final Map<String, CCType> name_lookup = new HashMap<>();

    static {
        for (CCType vt : EnumSet.allOf(CCType.class)) {
            CCType.idx_lookup.put(vt.ordinal(), vt);
            CCType.name_lookup.put(vt.name().toUpperCase(), vt);
        }
    }

    public String getName() {
        return name;
    }

    public static CCType get(String name) {
        return (CCType.name_lookup.get(name.toUpperCase()));
    }
}
