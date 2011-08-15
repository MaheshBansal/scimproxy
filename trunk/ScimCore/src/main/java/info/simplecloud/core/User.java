package info.simplecloud.core;

import info.simplecloud.core.annotations.Attribute;
import info.simplecloud.core.exceptions.InvalidUser;
import info.simplecloud.core.exceptions.UnknownAttribute;
import info.simplecloud.core.exceptions.UnknownEncoding;
import info.simplecloud.core.handlers.ComplexHandler;
import info.simplecloud.core.handlers.PluralHandler;
import info.simplecloud.core.handlers.StringHandler;
import info.simplecloud.core.types.Address;
import info.simplecloud.core.types.Meta;
import info.simplecloud.core.types.Name;
import info.simplecloud.core.types.PluralType;

import java.util.ArrayList;
import java.util.List;

public class User extends Resource {

    private static List<Class<?>> extensionTypes = new ArrayList<Class<?>>();
    static {
        String extensionsString = System.getProperty("info.simplecloud.core.ScimUser.extensions");
        if (extensionsString != null) {
            String[] extensionNames = extensionsString.split(";");
            for (String extensionName : extensionNames) {
                try {
                    extensionTypes.add(Class.forName(extensionName));
                } catch (ClassNotFoundException e) {
                    // Nothing we can do
                }
            }
        }
    }

    public static void registerExtension(Class<?> clazz) {
        if (!extensionTypes.contains(clazz)) {
            extensionTypes.add(clazz);
        }
    }

    private String                    externalId;
    private String                    userName;
    private String                    displayName;
    private String                    nickName;
    private String                    profileUrl;
    private String                    userType;
    private String                    title;
    private String                    preferredLanguage;
    private String                    locale;
    private String                    password;
    private Name                      name;
    private List<PluralType<String>>  phoneNumbers;
    private List<PluralType<String>>  emails;
    private List<PluralType<String>>  ims;
    private List<PluralType<String>>  photos;
    private List<PluralType<String>>  memberOf;
    private List<PluralType<Address>> addresses;

    public User(String user, String encoding) throws UnknownEncoding, InvalidUser {
        super(user, encoding, extensionTypes);
    }

    public User(String id) {
        super(id, extensionTypes);
    }

    public String getUser(String encoding) throws UnknownEncoding {
        return super.getResource(encoding, null);
    }

    public String getUser(String encoding, List<String> includeAttributes) throws UnknownEncoding {
        return super.getResource(encoding, includeAttributes);
    }

    @Override
    public void patch(String patch, String encoding) throws UnknownEncoding, InvalidUser, UnknownAttribute {
        User userPatch = new User(patch, encoding);
        super.patch(userPatch);
    }

    public static List<User> getUsers(String users, String encoding) throws UnknownEncoding, InvalidUser {
        List<Resource> userlist = new ArrayList<Resource>();
        List<User> resultlist = new ArrayList<User>();
        // TODO This is ugly
        Resource.getResources(users, encoding, userlist);
        for (Resource r : userlist) {
            if (r instanceof User) {
                resultlist.add((User) r);
            }
        }
        return resultlist;
    }

    @Override
    public boolean equals(Object otherObj) {
        if (otherObj == this) {
            return true;
        }

        if (!(otherObj instanceof User)) {
            return false;
        }
        User otherScimUser = (User) otherObj;

        return super.equals(otherScimUser);
    }

    @Attribute(name = "externalId", handler = StringHandler.class)
    public String getExternalId() {
        return this.externalId;
    }

    @Attribute(name = "userName", handler = StringHandler.class)
    public String getUserName() {
        return this.userName;
    }

    @Attribute(name = "displayName", handler = StringHandler.class)
    public String getDisplayName() {
        return this.displayName;
    }

    @Attribute(name = "nickName", handler = StringHandler.class)
    public String getNickName() {
        return this.nickName;
    }

    @Attribute(name = "profileUrl", handler = StringHandler.class)
    public String getProfileUrl() {
        return this.profileUrl;
    }

    @Attribute(name = "userType", handler = StringHandler.class)
    public String getUserType() {
        return this.userType;
    }

    @Attribute(name = "title", handler = StringHandler.class)
    public String getTitle() {
        return this.title;
    }

    @Attribute(name = "preferredLanguage", handler = StringHandler.class)
    public String getPreferredLanguage() {
        return this.preferredLanguage;
    }

    @Attribute(name = "locale", handler = StringHandler.class)
    public String getLocale() {
        return this.locale;
    }

    @Attribute(name = "password", handler = StringHandler.class)
    public String getPassword() {
        return this.password;
    }

    @Attribute(name = "name", handler = ComplexHandler.class, type = Name.class)
    public Name getName() {
        return this.name;
    }

    @Attribute(name = "phoneNumbers", handler = PluralHandler.class, internalHandler = StringHandler.class)
    public List<PluralType<String>> getPhoneNumbers() {
        return this.phoneNumbers;
    }

    @Attribute(name = "emails", handler = PluralHandler.class, internalHandler = StringHandler.class)
    public List<PluralType<String>> getEmails() {
        return this.emails;
    }

    @Attribute(name = "ims", handler = PluralHandler.class, internalHandler = StringHandler.class)
    public List<PluralType<String>> getIms() {
        return this.ims;
    }

    @Attribute(name = "photos", handler = PluralHandler.class, internalHandler = StringHandler.class)
    public List<PluralType<String>> getPhotos() {
        return this.photos;
    }

    @Attribute(name = "memberOf", handler = PluralHandler.class, internalHandler = StringHandler.class)
    public List<PluralType<String>> getMemberOf() {
        return this.memberOf;
    }

    @Attribute(name = "addresses", handler = PluralHandler.class, internalHandler = ComplexHandler.class, internalType = Address.class)
    public List<PluralType<Address>> getAddresses() {
        return this.addresses;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public void setPhoneNumbers(List<PluralType<String>> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public void setEmails(List<PluralType<String>> emails) {
        this.emails = emails;
    }

    public void setIms(List<PluralType<String>> ims) {
        this.ims = ims;
    }

    public void setPhotos(List<PluralType<String>> photos) {
        this.photos = photos;
    }

    public void setMemberOf(List<PluralType<String>> memberOf) {
        this.memberOf = memberOf;
    }

    public void setAddresses(List<PluralType<Address>> addresses) {
        this.addresses = addresses;
    }

}
