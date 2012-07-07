package info.simplecloud.scimproxy.compliance.enteties;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Statistics {
    @XmlElement(name = "success")
    private int success = 0;

    @XmlElement(name = "failed")
    private int failed      = 0;

    public Statistics() {
    }

    public Statistics(int success, int failed) {
        this.success = success;
        this.failed = failed;
    }
}
