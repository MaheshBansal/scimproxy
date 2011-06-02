package info.simplecloud.core;

import java.util.Comparator;

public class ScimUserComparator implements Comparator<ScimUser> {

    private String compareAttribute;
    private boolean sortAscending;

    public ScimUserComparator(String compareAttribute, boolean sortAscending) {
        this.compareAttribute = compareAttribute;
        this.sortAscending = sortAscending;
    }
    
    @Override
    public int compare(ScimUser user1, ScimUser user2) {
        Comparable obj1 = user1.getRaw(this.compareAttribute);
        Comparable obj2 = user2.getRaw(this.compareAttribute);

        return this.sortAscending ? obj1.compareTo(obj2) : obj2.compareTo(obj1);
    }

}
