package info.simplecloud.core;

import info.simplecloud.core.annotations.Complex;
import info.simplecloud.core.annotations.Extension;
import info.simplecloud.core.exceptions.InvalidUser;
import info.simplecloud.core.exceptions.UnknownAttribute;
import info.simplecloud.core.exceptions.UnknownEncoding;

import java.util.ArrayList;
import java.util.List;

import x0.scimSchemasCore1.GroupDocument;

@Extension(schema="urn:scim:schemas:core:1.0")
@Complex(xmlType = x0.scimSchemasCore1.Group.class, xmlDoc = GroupDocument.class)
public class Group extends Resource {
    private static List<Class<?>> extensionTypes = new ArrayList<Class<?>>();
    static {
        String extensionsString = System.getProperty("info.simplecloud.core.group.extensions");
        if (extensionsString != null) {
            String[] extensionNames = extensionsString.split(";");
            for (String extensionName : extensionNames) {
                try {
                    extensionTypes.add(Class.forName(extensionName));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Failed reading group extensions", e);
                }
            }
        }
    }

    public static void registerExtension(Class<?> clazz) {
        if (!extensionTypes.contains(clazz)) {
            extensionTypes.add(clazz);
        }
    }

    public Group(String user, String encoding) throws UnknownEncoding, InvalidUser {
        super(user, encoding, extensionTypes);
    }

    public Group(String id) {
        super(id, extensionTypes);
    }

    public String getGroup(String encoding) throws UnknownEncoding {
        return super.getResource(encoding, null);
    }

    public String getGroup(String encoding, List<String> includeAttributes) throws UnknownEncoding {
        return super.getResource(encoding, includeAttributes);
    }

    @Override
    public void patch(String patch, String encoding) throws UnknownEncoding, InvalidUser, UnknownAttribute {
        Group groupPatch = new Group(patch, encoding);
        super.patch(groupPatch);
    }

    @Override
    public boolean equals(Object otherObj) {
        if (otherObj == this) {
            return true;
        }

        if (!(otherObj instanceof Group)) {
            return false;
        }
        Group otherGroup = (Group) otherObj;

        return super.equals(otherGroup);
    }

}
