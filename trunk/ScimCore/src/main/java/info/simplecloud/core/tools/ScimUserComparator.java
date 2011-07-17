package info.simplecloud.core.tools;

import info.simplecloud.core.ScimUser;

import java.util.Comparator;

public class ScimUserComparator<T> implements Comparator<ScimUser> {

    private String  compareAttribute;
    private boolean sortAscending;

    public ScimUserComparator(String compareAttribute, boolean sortAscending) {
        this.compareAttribute = compareAttribute;
        this.sortAscending = sortAscending;
    }

    @Override
    public int compare(ScimUser user1, ScimUser user2) {
        Comparable<T> obj1 = user1.getComparable(this.compareAttribute);
        Comparable<T> obj2 = user2.getComparable(this.compareAttribute);
        if (obj1 == obj2) {
            return 0;
        } else if (obj1 == null) {
            return 1;
        } else if (obj2 == null) {
            return -1;
        }
        return (this.sortAscending ? obj1.compareTo((T) obj2) : obj2.compareTo((T) obj1));
    }

}
