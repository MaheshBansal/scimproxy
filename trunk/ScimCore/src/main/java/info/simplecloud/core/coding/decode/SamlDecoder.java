package info.simplecloud.core.coding.decode;

import info.simplecloud.core.ScimUser;
import info.simplecloud.core.execeptions.FailedToSetValue;
import info.simplecloud.core.execeptions.InvalidUser;
import info.simplecloud.core.execeptions.UnhandledAttributeType;
import info.simplecloud.core.execeptions.UnknownType;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public class SamlDecoder implements IUserDecoder {

    @Override
    public void addMe(Map<String, IUserDecoder> decoders) {
        // TODO Auto-generated method stub

    }



    @Override
    public void decode(String user, ScimUser data) throws InvalidUser, UnhandledAttributeType, FailedToSetValue, UnknownType,
            InstantiationException, IllegalAccessException {
        // TODO Auto-generated method stub
        
    }



	@Override
	public void decode(String userList, List<ScimUser> users) throws InvalidUser, UnhandledAttributeType, FailedToSetValue, UnknownType, InstantiationException, IllegalAccessException, ParseException {
		// TODO Auto-generated method stub
		
	}

}
