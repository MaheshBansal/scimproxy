package info.simplecloud.core;

import info.simplecloud.core.exceptions.InvalidUser;
import info.simplecloud.core.exceptions.UnknownEncoding;
import info.simplecloud.core.exceptions.UnknownExtension;
import info.simplecloud.core.extensions.EnterpriseAttributes;
import info.simplecloud.core.extensions.types.Manager;
import info.simplecloud.core.types.Address;
import info.simplecloud.core.types.MultiValuedType;
import info.simplecloud.core.types.Name;

import java.util.ArrayList;
import java.util.List;

import org.apache.xmlbeans.impl.util.Base64;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserTest {

    @BeforeClass
    public static void setUp() {
        User.registerExtension(EnterpriseAttributes.class);
    }

    @Test
    public void parse() throws UnknownEncoding, InvalidUser {
        String base64User = "ew0KCSJzY2hlbWFzIjogWyJ1cm46c2NpbTpzY2hlbWFzOmNvcmU6MS4wIl0sDQoJImlkIjogIjAwNUQwMDAwMDAxQXoxdSIsDQoJImV4dGVybmFsSWQiOiAiNzAxOTg0IiwNCgkidXNlck5hbWUiOiAiYmplbnNlbkBleGFtcGxlLmNvbSIsDQoJIm5hbWUiOiB7DQoJCSJmb3JtYXR0ZWQiOiAiTXMuIEJhcmJhcmEgSiBKZW5zZW4gSUlJIiwNCgkJImZhbWlseU5hbWUiOiAiSmVuc2VuIiwNCgkJImdpdmVuTmFtZSI6ICJCYXJiYXJhIiwNCgkJIm1pZGRsZU5hbWUiOiAiSmFuZSIsDQoJCSJob25vcmlmaWNQcmVmaXgiOiAiTXMuIiwNCgkJImhvbm9yaWZpY1N1ZmZpeCI6ICJJSUkiDQoJfSwNCgkiZGlzcGxheU5hbWUiOiAiQmFicyBKZW5zZW4iLA0KCSJuaWNrTmFtZSI6ICJCYWJzIiwNCgkicHJvZmlsZVVybCI6ICJodHRwczovL2xvZ2luLmV4YW1wbGUuY29tL2JqZW5zZW4iLA0KCSJlbWFpbHMiOiBbDQoJCXsNCgkJICAidmFsdWUiOiAiYmplbnNlbkBleGFtcGxlLmNvbSIsDQoJCSAgInR5cGUiOiAid29yayIsDQoJCSAgInByaW1hcnkiOiB0cnVlDQoJCX0sDQoJCXsNCgkJICAidmFsdWUiOiAiYmFic0BqZW5zZW4ub3JnIiwNCgkJICAidHlwZSI6ICJob21lIg0KCQl9DQoJXSwNCgkiYWRkcmVzc2VzIjogWw0KCQl7DQoJCSAgInR5cGUiOiAid29yayIsDQoJCSAgInN0cmVldEFkZHJlc3MiOiAiMTAwIFVuaXZlcnNhbCBDaXR5IFBsYXphIiwNCgkJICAibG9jYWxpdHkiOiAiSG9sbHl3b29kIiwNCgkJICAicmVnaW9uIjogIkNBIiwNCgkJICAicG9zdGFsQ29kZSI6ICI5MTYwOCIsDQoJCSAgImNvdW50cnkiOiAiVVNBIiwNCgkJICAiZm9ybWF0dGVkIjogIjEwMCBVbml2ZXJzYWwgQ2l0eSBQbGF6YVxuSG9sbHl3b29kLCBDQSA5MTYwOCBVU0EiLA0KCQkgICJwcmltYXJ5IjogdHJ1ZQ0KCQl9LA0KCQl7DQoJCSAgInR5cGUiOiAiaG9tZSIsDQoJCSAgInN0cmVldEFkZHJlc3MiOiAiNDU2IEhvbGx5d29vZCBCbHZkIiwNCgkJICAibG9jYWxpdHkiOiAiSG9sbHl3b29kIiwNCgkJICAicmVnaW9uIjogIkNBIiwNCgkJICAicG9zdGFsQ29kZSI6ICI5MTYwOCIsDQoJCSAgImNvdW50cnkiOiAiVVNBIiwNCgkJICAiZm9ybWF0dGVkIjogIjQ1NiBIb2xseXdvb2QgQmx2ZFxuSG9sbHl3b29kLCBDQSA5MTYwOCBVU0EiDQoJCX0NCgldLA0KCSJwaG9uZU51bWJlcnMiOiBbDQoJCXsNCgkJICAidmFsdWUiOiAiODAwLTg2NC04Mzc3IiwNCgkJICAidHlwZSI6ICJ3b3JrIg0KCQl9LA0KCQl7DQoJCSAgInZhbHVlIjogIjgxOC0xMjMtNDU2NyIsDQoJCSAgInR5cGUiOiAibW9iaWxlIg0KCQl9DQoJXSwNCgkiaW1zIjogWw0KCQl7DQoJCSAgInZhbHVlIjogInNvbWVhaW1oYW5kbGUiLA0KCQkgICJ0eXBlIjogImFpbSINCgkJfQ0KCV0sDQoJInBob3RvcyI6IFsNCgkJew0KCQkgICJ2YWx1ZSI6ICJodHRwczovL3Bob3Rvcy5leGFtcGxlLmNvbS9wcm9maWxlcGhvdG8vNzI5MzAwMDAwMDBDY25lL0YiLA0KCQkgICJ0eXBlIjogInBob3RvIg0KCQl9LA0KCQl7DQoJCSAgInZhbHVlIjogImh0dHBzOi8vcGhvdG9zLmV4YW1wbGUuY29tL3Byb2ZpbGVwaG90by83MjkzMDAwMDAwMENjbmUvVCIsDQoJCSAgInR5cGUiOiAidGh1bWJuYWlsIg0KCQl9DQoJXSwNCgkidXNlclR5cGUiOiAiRW1wbG95ZWUiLA0KCSJ0aXRsZSI6ICJUb3VyIEd1aWRlIiwNCgkicHJlZmVycmVkTGFuZ3VhZ2UiOiAiZW5fVVMiLA0KCSJsb2NhbGUiOiAiZW5fVVMiLA0KCSJ0aW1lem9uZSI6ICJBbWVyaWNhL0RlbnZlciIsDQoJIm1lbWJlck9mIjogWw0KCQl7DQoJCSAgImRpc3BsYXkiOiAiVG91ciBHdWlkZXMiLA0KCQkgICJ2YWx1ZSI6ICIwMDMwMDAwMDAwNU4yWTZBQSIsDQoJCSAgInByaW1hcnkiOiB0cnVlDQoJCX0sDQoJCXsNCgkJICAiZGlzcGxheSI6ICJFbXBsb3llZXMiLA0KCQkgICJ2YWx1ZSI6ICIwMDMwMDAwMDAwNU4zNEg3OCINCgkJfSwNCgkJew0KCQkgICJkaXNwbGF5IjogIlVTIEVtcGxveWVlcyIsDQoJCSAgInZhbHVlIjogIjAwMzAwMDAwMDA1Tjk4WVQxIg0KCQl9DQoJXSwNCgkibWV0YSI6IHsNCgkJImNyZWF0ZWQiOiAiMjAxMC0wMS0yM1QwNDo1NjoyMloiLA0KCQkibGFzdE1vZGlmaWVkIjogIjIwMTEtMDUtMTNUMDQ6NDI6MzRaIg0KCX0NCn0=";
        String stringUser = new String(Base64.decode(base64User.getBytes()));
        User user = new User(stringUser, Resource.ENCODING_JSON);

        Assert.assertEquals("005D0000001Az1u", user.getId());
        Assert.assertEquals("701984", user.getExternalId());
        Assert.assertEquals("bjensen@example.com", user.getUserName());
        Assert.assertEquals("Ms. Barbara J Jensen III", user.getName().getFormatted());
        Assert.assertEquals("Jensen", user.getName().getFamilyName());
        Assert.assertEquals("Barbara", user.getName().getGivenName());
        Assert.assertEquals("Jane", user.getName().getMiddleName());
        Assert.assertEquals("Ms.", user.getName().getHonorificPrefix());
        Assert.assertEquals("III", user.getName().getHonorificSuffix());
        Assert.assertEquals("Babs Jensen", user.getDisplayName());
        Assert.assertEquals("Babs", user.getNickName());
        Assert.assertEquals("https://login.example.com/bjensen", user.getProfileUrl());

        {
            List<MultiValuedType<String>> emails = user.getEmails();
            Assert.assertEquals(2, emails.size());
            for (MultiValuedType<String> email : emails) {
                if (!("bjensen@example.com".equals(email.getValue()) && "work".equals(email.getType()) && email.isPrimary())
                        && !("babs@jensen.org".equals(email.getValue()) && "home".equals(email.getType()) && !email.isPrimary())) {
                    Assert.fail("email not matching");
                }
            }
        }

        List<MultiValuedType<String>> phoneNumbers = user.getPhoneNumbers();
        for (MultiValuedType<String> phonenumber : phoneNumbers) {
            if (!("800-864-8377".equals(phonenumber.getValue()) && "work".equals(phonenumber.getType()) && !phonenumber.isPrimary())
                    && !("818-123-4567".equals(phonenumber.getValue()) && "mobile".equals(phonenumber.getType()) && !phonenumber
                            .isPrimary())) {
                Assert.fail("email not matching");
            }
        }
        Assert.assertEquals("Employee", user.getUserType());
        Assert.assertEquals("Tour Guide", user.getTitle());
        Assert.assertEquals("en_US", user.getPreferredLanguage());
        Assert.assertEquals("en_US", user.getLocale());
        Assert.assertEquals("America/Denver", user.getTimezone());

        // Assert.assertEquals(new GregorianCalendar(arg0, arg1, arg2, arg3,
        // arg4, arg5), user.getMeta().getCreated());
        // Assert.assertEquals(new GregorianCalendar(arg0, arg1, arg2, arg3,
        // arg4, arg5), user.getMeta().getCreated());

        // TODO validate result
    }

    @Test
    public void parseEnterpriseUser() throws UnknownEncoding, InvalidUser {
        String base64User = "ew0KCSJzY2hlbWFzIjogWyJ1cm46c2NpbTpzY2hlbWFzOmNvcmU6MS4wIiwgInVybjpzY2ltOnNjaGVtYXM6ZXh0ZW5zaW9uOmVudGVycHJpc2U6MS4wIl0sDQoJImlkIjogIjAwNUQwMDAwMDAxQXoxdSIsDQoJImV4dGVybmFsSWQiOiAiNzAxOTg0IiwNCgkidXNlck5hbWUiOiAiYmplbnNlbkBleGFtcGxlLmNvbSIsDQoJIm5hbWUiOiB7DQoJCSJmb3JtYXR0ZWQiOiAiTXMuIEJhcmJhcmEgSiBKZW5zZW4gSUlJIiwNCgkJImZhbWlseU5hbWUiOiAiSmVuc2VuIiwNCgkJImdpdmVuTmFtZSI6ICJCYXJiYXJhIiwNCgkJIm1pZGRsZU5hbWUiOiAiSmFuZSIsDQoJCSJob25vcmlmaWNQcmVmaXgiOiAiTXMuIiwNCgkJImhvbm9yaWZpY1N1ZmZpeCI6ICJJSUkiDQoJfSwNCgkiZGlzcGxheU5hbWUiOiAiQmFicyBKZW5zZW4iLA0KCSJuaWNrTmFtZSI6ICJCYWJzIiwNCgkicHJvZmlsZVVybCI6ICJodHRwczovL2xvZ2luLmV4YW1wbGUuY29tL2JqZW5zZW4iLA0KCSJlbWFpbHMiOiBbDQoJCXsNCgkJICAidmFsdWUiOiAiYmplbnNlbkBleGFtcGxlLmNvbSIsDQoJCSAgInR5cGUiOiAid29yayIsDQoJCSAgInByaW1hcnkiOiB0cnVlDQoJCX0sDQoJCXsNCgkJICAidmFsdWUiOiAiYmFic0BqZW5zZW4ub3JnIiwNCgkJICAidHlwZSI6ICJob21lIg0KCQl9DQoJXSwNCgkiYWRkcmVzc2VzIjogWw0KCQl7DQoJCSAgInR5cGUiOiAid29yayIsDQoJCSAgInN0cmVldEFkZHJlc3MiOiAiMTAwIFVuaXZlcnNhbCBDaXR5IFBsYXphIiwNCgkJICAibG9jYWxpdHkiOiAiSG9sbHl3b29kIiwNCgkJICAicmVnaW9uIjogIkNBIiwNCgkJICAicG9zdGFsQ29kZSI6ICI5MTYwOCIsDQoJCSAgImNvdW50cnkiOiAiVVNBIiwNCgkJICAiZm9ybWF0dGVkIjogIjEwMCBVbml2ZXJzYWwgQ2l0eSBQbGF6YVxuSG9sbHl3b29kLCBDQSA5MTYwOCBVU0EiLA0KCQkgICJwcmltYXJ5IjogdHJ1ZQ0KCQl9LA0KCQl7DQoJCSAgInR5cGUiOiAiaG9tZSIsDQoJCSAgInN0cmVldEFkZHJlc3MiOiAiNDU2IEhvbGx5d29vZCBCbHZkIiwNCgkJICAibG9jYWxpdHkiOiAiSG9sbHl3b29kIiwNCgkJICAicmVnaW9uIjogIkNBIiwNCgkJICAicG9zdGFsQ29kZSI6ICI5MTYwOCIsDQoJCSAgImNvdW50cnkiOiAiVVNBIiwNCgkJICAiZm9ybWF0dGVkIjogIjQ1NiBIb2xseXdvb2QgQmx2ZFxuSG9sbHl3b29kLCBDQSA5MTYwOCBVU0EiDQoJCX0NCgldLA0KCSJwaG9uZU51bWJlcnMiOiBbDQoJCXsNCgkJICAidmFsdWUiOiAiODAwLTg2NC04Mzc3IiwNCgkJICAidHlwZSI6ICJ3b3JrIg0KCQl9LA0KCQl7DQoJCSAgInZhbHVlIjogIjgxOC0xMjMtNDU2NyIsDQoJCSAgInR5cGUiOiAibW9iaWxlIg0KCQl9DQoJXSwNCgkiaW1zIjogWw0KCQl7DQoJCSAgInZhbHVlIjogInNvbWVhaW1oYW5kbGUiLA0KCQkgICJ0eXBlIjogImFpbSINCgkJfQ0KCV0sDQoJInBob3RvcyI6IFsNCgkJew0KCQkgICJ2YWx1ZSI6ICJodHRwczovL3Bob3Rvcy5leGFtcGxlLmNvbS9wcm9maWxlcGhvdG8vNzI5MzAwMDAwMDBDY25lL0YiLA0KCQkgICJ0eXBlIjogInBob3RvIg0KCQl9LA0KCQl7DQoJCSAgInZhbHVlIjogImh0dHBzOi8vcGhvdG9zLmV4YW1wbGUuY29tL3Byb2ZpbGVwaG90by83MjkzMDAwMDAwMENjbmUvVCIsDQoJCSAgInR5cGUiOiAidGh1bWJuYWlsIg0KCQl9DQoJXSwNCgkidXNlclR5cGUiOiAiRW1wbG95ZWUiLA0KCSJ0aXRsZSI6ICJUb3VyIEd1aWRlIiwNCgkibWFuYWdlciI6IHsNCgkJImRpc3BsYXlOYW1lIjogIk1hbmR5IFBlcHBlcmlkZ2UiDQoJfSwNCgkicHJlZmVycmVkTGFuZ3VhZ2UiOiAiZW5fVVMiLA0KCSJsb2NhbGUiOiAiZW5fVVMiLA0KCSJ0aW1lem9uZSI6ICJBbWVyaWNhL0RlbnZlciIsDQoJIm1lbWJlck9mIjogWw0KCQl7DQoJCSAgImRpc3BsYXkiOiAiVG91ciBHdWlkZXMiLA0KCQkgICJ2YWx1ZSI6ICIwMDMwMDAwMDAwNU4yWTZBQSIsDQoJCSAgInByaW1hcnkiOiB0cnVlDQoJCX0sDQoJCXsNCgkJICAiZGlzcGxheSI6ICJFbXBsb3llZXMiLA0KCQkgICJ2YWx1ZSI6ICIwMDMwMDAwMDAwNU4zNEg3OCINCgkJfSwNCgkJew0KCQkgICJkaXNwbGF5IjogIlVTIEVtcGxveWVlcyIsDQoJCSAgInZhbHVlIjogIjAwMzAwMDAwMDA1Tjk4WVQxIg0KCQl9DQoJXSwNCgkidXJuOnNjaW06c2NoZW1hczpleHRlbnNpb246ZW50ZXJwcmlzZToxLjAiOiB7DQoJCSJlbXBsb3llZU51bWJlciI6ICI3MDE5ODQiLA0KCQkiY29zdENlbnRlciI6ICI0MTMwIiwNCgkJIm9yZ2FuaXphdGlvbiI6ICJVbml2ZXJzYWwgU3R1ZGlvcyIsDQoJCSJkaXZpc2lvbiI6ICJUaGVtZSBQYXJrIiwNCgkJImRlcGFydG1lbnQiOiAiVG91ciBPcGVyYXRpb25zIiwNCgkJIm1hbmFnZXIiOiB7DQoJCQkJIm1hbmFnZXJJZCI6ICIwMDVEMDAwMDAwMUFRUkUiLA0KCQkJCSJkaXNwbGF5TmFtZSI6ICJKb2huIFNtaXRoIg0KCQl9DQoJfSwNCgkibWV0YSI6IHsNCgkJImNyZWF0ZWQiOiAiMjAxMC0wMS0yM1QwNDo1NjoyMloiLA0KCQkibGFzdE1vZGlmaWVkIjogIjIwMTEtMDUtMTNUMDQ6NDI6MzRaIg0KCX0NCn0=";
        String stringUser = new String(Base64.decode(base64User.getBytes()));
        new User(stringUser, Resource.ENCODING_JSON);

        // TODO validate result
    }

    @Test
    public void create() throws Exception {

        User user = new User("ABCDE-12345-EFGHI-78910");
        // TODO use all setters, all getters, setAttribute and getAttribute
        // TODO check that all attributes matches specification
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

        // TODO validate result
    }

    @Test
    public void encode() throws Exception {
        User user = new User("ABCDE-12345-EFGHI-78910");

        user.setAttribute("userName", "kaan");
        user.setAttribute("externalId", "djfkhasdjkfha");
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

        EnterpriseAttributes ea = user.getExtension(EnterpriseAttributes.class);
        ea.setDivision("Test division");
        ea.setManager(new Manager("MI-123ABC", "Hugo Boss"));

        List<MultiValuedType<String>> emails = new ArrayList<MultiValuedType<String>>();
        emails.add(new MultiValuedType<String>("karl@andersson.se", "home", false, false));
        emails.add(new MultiValuedType<String>("karl.andersson@work.com", "work", true, false));
        user.setAttribute("emails", emails);

        user.getUser(Resource.ENCODING_JSON);
        user.getUser(Resource.ENCODING_XML);

        // TODO validate result

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

        List<MultiValuedType<String>> emails = new ArrayList<MultiValuedType<String>>();
        emails.add(new MultiValuedType<String>("karl@andersson.se", "home", false, false));
        emails.add(new MultiValuedType<String>("karl.andersson@work.com", "work", true, false));
        user.setAttribute("emails", emails);

        List<String> includeAttributes = new ArrayList<String>();
        includeAttributes.add("userName");
        includeAttributes.add("title");

        user.getUser(Resource.ENCODING_JSON, includeAttributes);
        user.getUser(Resource.ENCODING_XML, includeAttributes);

        // TODO validate result
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

        List<MultiValuedType<String>> emails = new ArrayList<MultiValuedType<String>>();
        emails.add(new MultiValuedType<String>("karl@andersson.se", "home", false, false));
        emails.add(new MultiValuedType<String>("karl.andersson@work.com", "work", true, false));
        user.setAttribute("emails", emails);

        List<String> includeAttributes = new ArrayList<String>();
        includeAttributes.add("userName");
        includeAttributes.add("title");
        String stringUser = user.getUser(Resource.ENCODING_JSON);

        User user2 = new User(stringUser, Resource.ENCODING_JSON);
        stringUser = user2.getUser(Resource.ENCODING_XML);

        User user3 = new User(stringUser, Resource.ENCODING_XML);
        stringUser = user3.getUser(Resource.ENCODING_XML);

        // TODO validate result
    }

    @Test
    public void jsonPatch() {
        // TODO implement test
    }

    @Test
    public void xmlPatch() {
        // TODO implement test
    }

    @Test
    public void sort() {
        // TODO implement test
    }

    @Test
    public void equals() throws UnknownExtension {
        User user1 = new User("id-123");
        User user1eq1 = new User("id-123");
        User user1noteq1 = new User("not equal");
        User user1noteq2 = new User("id-123");
        User user1noteq3 = new User("id-123");
        User user1noteq4 = new User("id-123");
        User user1noteq5 = new User("id-123");

        Assert.assertEquals(user1eq1, user1);
        Assert.assertFalse(user1.equals(user1noteq1));
        Assert.assertFalse(user1noteq1.equals(user1));
        Assert.assertEquals(user1noteq2, user1);
        Assert.assertEquals(user1noteq3, user1);
        Assert.assertEquals(user1noteq4, user1);
        Assert.assertEquals(user1noteq5, user1);

        user1.setName(new Name("Mr. Frans Friskus", "Friskus", "Frans", null, "mr.", null));
        user1eq1.setName(new Name("Mr. Frans Friskus", "Friskus", "Frans", null, "mr.", null));
        user1noteq2.setName(new Name("Mr. Frans 1 Friskus", "Friskus", "Frans", "1", "mr.", null));
        user1noteq3.setName(new Name("Mr. Frans Friskus", "Friskus", "Frans", null, "mr.", null));
        user1noteq4.setName(new Name("Mr. Frans Friskus", "Friskus", "Frans", null, "mr.", null));
        user1noteq5.setName(new Name("Mr. Frans Friskus", "Friskus", "Frans", null, "mr.", null));

        Assert.assertEquals(user1eq1, user1);
        Assert.assertFalse(user1.equals(user1noteq1));
        Assert.assertFalse(user1noteq1.equals(user1));
        Assert.assertFalse(user1.equals(user1noteq2));
        Assert.assertFalse(user1noteq2.equals(user1));
        Assert.assertEquals(user1noteq3, user1);
        Assert.assertEquals(user1noteq4, user1);
        Assert.assertEquals(user1noteq5, user1);

        EnterpriseAttributes ea1 = user1.getExtension(EnterpriseAttributes.class);
        ea1.setEmployeeNumber("abc123");
        EnterpriseAttributes ea2 = user1eq1.getExtension(EnterpriseAttributes.class);
        ea2.setEmployeeNumber("abc123");
        EnterpriseAttributes ea3 = user1noteq3.getExtension(EnterpriseAttributes.class);
        ea3.setEmployeeNumber("not equal");
        EnterpriseAttributes ea4 = user1noteq4.getExtension(EnterpriseAttributes.class);
        ea4.setEmployeeNumber("abc123");
        EnterpriseAttributes ea5 = user1noteq5.getExtension(EnterpriseAttributes.class);
        ea5.setEmployeeNumber("abc123");

        Assert.assertEquals(user1eq1, user1);
        Assert.assertFalse(user1.equals(user1noteq1));
        Assert.assertFalse(user1noteq1.equals(user1));
        Assert.assertFalse(user1.equals(user1noteq2));
        Assert.assertFalse(user1noteq2.equals(user1));
        Assert.assertFalse(user1.equals(user1noteq3));
        Assert.assertFalse(user1noteq3.equals(user1));
        Assert.assertEquals(user1noteq4, user1);
        Assert.assertEquals(user1noteq5, user1);

        List<MultiValuedType<String>> l1 = new ArrayList<MultiValuedType<String>>();
        l1.add(new MultiValuedType<String>("String1", "type1", true, false));
        user1.setGroups(l1);
        List<MultiValuedType<String>> l2 = new ArrayList<MultiValuedType<String>>();
        l2.add(new MultiValuedType<String>("String1", "type1", true, false));
        user1eq1.setGroups(l2);
        List<MultiValuedType<String>> l3 = new ArrayList<MultiValuedType<String>>();
        l3.add(new MultiValuedType<String>("not equal", "type1", true, false));
        user1noteq4.setGroups(l3);
        List<MultiValuedType<String>> l4 = new ArrayList<MultiValuedType<String>>();
        l4.add(new MultiValuedType<String>("String1", "type1", true, false));
        user1noteq5.setGroups(l4);

        Assert.assertEquals(user1eq1, user1);
        Assert.assertFalse(user1.equals(user1noteq1));
        Assert.assertFalse(user1noteq1.equals(user1));
        Assert.assertFalse(user1.equals(user1noteq2));
        Assert.assertFalse(user1noteq2.equals(user1));
        Assert.assertFalse(user1.equals(user1noteq3));
        Assert.assertFalse(user1noteq3.equals(user1));
        Assert.assertFalse(user1.equals(user1noteq4));
        Assert.assertFalse(user1noteq4.equals(user1));
        Assert.assertEquals(user1noteq5, user1);

        List<MultiValuedType<Address>> plural1 = new ArrayList<MultiValuedType<Address>>();
        Address workAddress = new Address();
        workAddress.setCountry("Sweden");
        workAddress.setRegion("Stockholm");
        plural1.add(new MultiValuedType<Address>(workAddress, "work", true, false));
        user1.setAddresses(plural1);

        List<MultiValuedType<Address>> plural2 = new ArrayList<MultiValuedType<Address>>();
        Address workAddress2 = new Address();
        workAddress2.setCountry("Sweden");
        workAddress2.setRegion("Stockholm");
        plural2.add(new MultiValuedType<Address>(workAddress2, "work", true, false));
        user1eq1.setAddresses(plural2);

        List<MultiValuedType<Address>> plural3 = new ArrayList<MultiValuedType<Address>>();
        Address workAddress3 = new Address();
        workAddress3.setCountry("Sweden");
        workAddress3.setRegion("not equal");
        plural3.add(new MultiValuedType<Address>(workAddress3, "work", true, false));
        user1noteq1.setAddresses(plural3);

        Assert.assertEquals(user1eq1, user1);
        Assert.assertFalse(user1.equals(user1noteq1));
        Assert.assertFalse(user1noteq1.equals(user1));
        Assert.assertFalse(user1.equals(user1noteq2));
        Assert.assertFalse(user1noteq2.equals(user1));
        Assert.assertFalse(user1.equals(user1noteq3));
        Assert.assertFalse(user1noteq3.equals(user1));
        Assert.assertFalse(user1.equals(user1noteq4));
        Assert.assertFalse(user1noteq4.equals(user1));
        Assert.assertFalse(user1.equals(user1noteq5));
        Assert.assertFalse(user1noteq5.equals(user1));

    }

}
