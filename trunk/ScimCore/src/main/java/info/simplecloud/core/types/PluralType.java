package info.simplecloud.core.types;

public class PluralType<T> implements Comparable<PluralType<?>> {
    private T       value;
    private String  type;
    private boolean primary;
    private boolean delete;

    // TODO add display

    public PluralType(T value, String type, boolean primary, boolean delete) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot create plural object without value");
        }
        this.value = value;
        this.type = type;
        this.primary = primary;
        this.delete = delete;
    }

    public T getValue() {
        return this.value;
    }

    public String getType() {
        return this.type;
    }

    public boolean isPrimary() {
        return this.primary;
    }

    @Override
    public boolean equals(Object otherObj) {
        if (!(otherObj instanceof PluralType)) {
            return false;
        }
        PluralType otherPlural = (PluralType) otherObj;

        return this.value.equals(otherPlural.value);
    }

    @Override
    public String toString() {
        return "Value: '" + this.value.toString() + "', type: '" + this.type + "', primary: " + this.primary;
    }

    @Override
    public int compareTo(PluralType<?> other) {
        if (this.getValue() instanceof Comparable) {
            Comparable value = (Comparable) this.getValue();
            return value.compareTo(other.getValue());
        }

        return 0;
    }

    public boolean isDelete() {
        return this.delete;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
}
