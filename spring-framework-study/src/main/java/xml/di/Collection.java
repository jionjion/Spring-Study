package xml.di;

import java.util.List;

/**
 * @author Jion
 *  拥有集合类型的Bean的装配
 */
public class Collection {

    private String uri;

    private List<Service> services;

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "uri='" + uri + '\'' +
                ", services=" + services +
                '}';
    }
}
