package info.simplecloud.core.types;

import info.simplecloud.core.Attribute;
import info.simplecloud.core.coding.handlers.StringHandler;

public class Name extends ComplexType {
    public static final String ATTRIBUTE_FORMATTED        = "formatted";
    public static final String ATTRIBUTE_FAMILY_NAME      = "familyName";
    public static final String ATTRIBUTE_GIVEN_NAME       = "givenName";
    public static final String ATTRIBUTE_MIDDLE_NAME      = "middleName";
    public static final String ATTRIBUTE_HONORIFIC_PREFIX = "honorificPrefix";
    public static final String ATTRIBUTE_HONORIFIC_SUFFIX = "honorificSuffix";

    public Name(String formatted, String familyName, String givenName, String middleName, String honorificPrefix, String honorificSuffix) {
        this.setFormatted(formatted);
        this.setFamilyName(familyName);
        this.setGivenName(givenName);
        this.setMiddleName(middleName);
        this.setHonorificPrefix(honorificPrefix);
        this.setHonorificSuffix(honorificSuffix);
    }

    public Name() {
        // Need this too
    }

    @Attribute(schemaName = "formatted", codingHandler = StringHandler.class)
    public String getFormatted() {
        return super.getAttributeString(ATTRIBUTE_FORMATTED);
    }

    @Attribute(schemaName = "familyName", codingHandler = StringHandler.class)
    public String getFamilyName() {
        return super.getAttributeString(ATTRIBUTE_FAMILY_NAME);
    }

    @Attribute(schemaName = "givenName", codingHandler = StringHandler.class)
    public String getGivenName() {
        return super.getAttributeString(ATTRIBUTE_GIVEN_NAME);
    }

    @Attribute(schemaName = "middleName", codingHandler = StringHandler.class)
    public String getMiddleName() {
        return super.getAttributeString(ATTRIBUTE_MIDDLE_NAME);
    }

    @Attribute(schemaName = "honorificPrefix", codingHandler = StringHandler.class)
    public String getHonorificPrefix() {
        return super.getAttributeString(ATTRIBUTE_HONORIFIC_PREFIX);
    }

    @Attribute(schemaName = "honorificSuffix", codingHandler = StringHandler.class)
    public String getHonorificSuffix() {
        return super.getAttributeString(ATTRIBUTE_HONORIFIC_SUFFIX);
    }

    public void setFormatted(String formatted) {
        super.setAttribute(ATTRIBUTE_FORMATTED, formatted);
    }

    public void setFamilyName(String familyName) {
        super.setAttribute(ATTRIBUTE_FAMILY_NAME, familyName);
    }

    public void setGivenName(String givenName) {
        super.setAttribute(ATTRIBUTE_GIVEN_NAME, givenName);
    }

    public void setMiddleName(String middleName) {
        super.setAttribute(ATTRIBUTE_MIDDLE_NAME, middleName);
    }

    public void setHonorificPrefix(String honorificPrefix) {
        super.setAttribute(ATTRIBUTE_HONORIFIC_PREFIX, honorificPrefix);
    }

    public void setHonorificSuffix(String honorificSuffix) {
        super.setAttribute(ATTRIBUTE_HONORIFIC_SUFFIX, honorificSuffix);
    }

    @Override
    public boolean equals(Object otherObj) {
        if (this == otherObj) {
            return true;
        }
        if (!(otherObj instanceof Name)) {
            return false;
        }
        Name otherName = (Name) otherObj;

        return super.equals(otherName);
    }
}
