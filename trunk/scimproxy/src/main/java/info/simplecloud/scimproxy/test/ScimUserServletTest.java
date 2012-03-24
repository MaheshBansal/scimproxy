package info.simplecloud.scimproxy.test;

import info.simplecloud.scimproxy.ScimUserServlet;
import info.simplecloud.scimproxy.config.Config;
import info.simplecloud.scimproxy.storage.StorageDelegator;

public class ScimUserServletTest extends ScimUserServlet{

	private static final long serialVersionUID = 1L;

    protected StorageDelegator getUserDelegator(String session) {
    	return StorageDelegator.getInstance(session, Config.UNIT_TEST_STORAGE_TYPE);
    }

}
