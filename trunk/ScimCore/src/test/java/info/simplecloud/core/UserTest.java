package info.simplecloud.core;

import info.simplecloud.core.types.Name;
import info.simplecloud.core.types.PluralType;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class UserTest {
    @Test
    public void create() throws Exception {
        User user = new User("ABCDE-12345-EFGHI-78910");

        user.setAttribute("userName", "kaan");
        user.setAttribute("externalId", "djfkhasdjkfha");
        user.setAttribute("name", new Name());
        user.setAttribute("name.givenName", "Karl");
        user.setAttribute("name.familyName", "Andersson");
        user.setAttribute("displayName", "Kalle");
        user.setAttribute("nickName", "Kalle Anka");
        user.setAttribute("profileUrl", "https://example.com");
        user.setAttribute("title", "master");
        user.setAttribute("userType", "super");
        user.setAttribute("preferredLanguage", "swedish");
        user.setAttribute("locale", "sv");
        user.setAttribute("password", "kan123!");

        List<String> schemas = new ArrayList<String>();
        schemas.add("urn:scim:schemas:core:1.0");
        user.setAttribute("schemas", schemas);

        user.toString();
    }

    @Test
    public void encode() throws Exception {
        User user = new User("ABCDE-12345-EFGHI-78910");

        user.setAttribute("userName", "kaan");
        user.setAttribute("externalId", "djfkhasdjkfha");
        user.setAttribute("name", new Name());
        user.setAttribute("name.givenName", "Karl");
        user.setAttribute("name.familyName", "Andersson");
        user.setAttribute("displayName", "Kalle");
        user.setAttribute("nickName", "Kalle Anka");
        user.setAttribute("profileUrl", "https://example.com");
        user.setAttribute("title", "master");
        user.setAttribute("userType", "super");
        user.setAttribute("preferredLanguage", "swedish");
        user.setAttribute("locale", "sv");
        user.setAttribute("password", "kan123!");

        List<PluralType<String>> emails = new ArrayList<PluralType<String>>();
        emails.add(new PluralType<String>("karl@andersson.se", "home", false, false));
        emails.add(new PluralType<String>("karl.andersson@work.com", "work", true, false));
        user.setAttribute("emails", emails);

        user.getUser(Resource.ENCODING_JSON);
        user.getUser(Resource.ENCODING_XML);
    }

    @Test
    public void encodePartial() throws Exception {
        User user = new User("ABCDE-12345-EFGHI-78910");

        user.setAttribute("userName", "kaan");
        user.setAttribute("externalId", "djfkhasdjkfha");
        user.setAttribute("name", new Name());
        user.setAttribute("name.givenName", "Karl");
        user.setAttribute("name.familyName", "Andersson");
        user.setAttribute("displayName", "Kalle");
        user.setAttribute("nickName", "Kalle Anka");
        user.setAttribute("profileUrl", "https://example.com");
        user.setAttribute("title", "master");
        user.setAttribute("userType", "super");
        user.setAttribute("preferredLanguage", "swedish");
        user.setAttribute("locale", "sv");
        user.setAttribute("password", "kan123!");

        List<PluralType<String>> emails = new ArrayList<PluralType<String>>();
        emails.add(new PluralType<String>("karl@andersson.se", "home", false, false));
        emails.add(new PluralType<String>("karl.andersson@work.com", "work", true, false));
        user.setAttribute("emails", emails);

        List<String> includeAttributes = new ArrayList<String>();
        includeAttributes.add("userName");
        includeAttributes.add("title");

        user.getUser(Resource.ENCODING_JSON, includeAttributes);
        user.getUser(Resource.ENCODING_XML, includeAttributes);
    }

    @Test
    public void decode() throws Exception {
        User user = new User("ABCDE-12345-EFGHI-78910");

        user.setAttribute("userName", "kaan");
        user.setAttribute("externalId", "djfkhasdjkfha");
        user.setAttribute("name", new Name());
        user.setAttribute("name.givenName", "Karl");
        user.setAttribute("name.familyName", "Andersson");
        user.setAttribute("displayName", "Kalle");
        user.setAttribute("nickName", "Kalle Anka");
        user.setAttribute("profileUrl", "https://example.com");
        user.setAttribute("title", "master");
        user.setAttribute("userType", "super");
        user.setAttribute("preferredLanguage", "swedish");
        user.setAttribute("locale", "sv");
        user.setAttribute("password", "kan123!");

        List<PluralType<String>> emails = new ArrayList<PluralType<String>>();
        emails.add(new PluralType<String>("karl@andersson.se", "home", false, false));
        emails.add(new PluralType<String>("karl.andersson@work.com", "work", true, false));
        user.setAttribute("emails", emails);

        List<String> includeAttributes = new ArrayList<String>();
        includeAttributes.add("userName");
        includeAttributes.add("title");
        String stringUser = user.getUser(Resource.ENCODING_JSON); 
        //System.out.println(stringUser);
        User user2 = new User(stringUser, Resource.ENCODING_JSON);
        stringUser = user2.getUser(Resource.ENCODING_XML);
        //System.out.println(stringUser);        
        User user3 = new User(stringUser, Resource.ENCODING_XML);
        stringUser = user3.getUser(Resource.ENCODING_XML);
        //System.out.println(stringUser);
        //System.out.println(user3.getUser(Resource.ENCODING_JSON));
        
    }

    @Test
    public void patch() {

    }

    @Test
    public void sort() {

    }

}
