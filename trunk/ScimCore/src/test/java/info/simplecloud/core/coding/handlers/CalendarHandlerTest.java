package info.simplecloud.core.coding.handlers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class CalendarHandlerTest {
    private static CalendarHandler  ch               = new CalendarHandler();
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    @Test
    public void encode() throws JSONException {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        JSONObject scimUserJson = new JSONObject();

        ch.encode(scimUserJson, "testkey", cal);

        String stringDate = simpleDateFormat.format(date);

        Assert.assertEquals(stringDate, scimUserJson.getString("testkey"));
    }

    @Test
    public void decode() throws JSONException {

        Date date = new Date();
        String dateString = simpleDateFormat.format(date);

        JSONObject scimUserJson = new JSONObject();
        scimUserJson.put("testkey", dateString);

        Calendar cal = (Calendar) ch.decode(scimUserJson, "testkey");

        Assert.assertEquals(date.getTime() / 1000, cal.getTime().getTime() / 1000);
    }

}
