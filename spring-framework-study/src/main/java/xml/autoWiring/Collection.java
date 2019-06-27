package xml.autoWiring;

/**
 * @author Jion
 */
public class Collection {

    Service service;

    public void setService(Service service) {
        this.service = service;
    }

    public Collection() {
        super();
    }

    public Collection(Service service) {
        this.service = service;
    }

    public void save(){
        service.save();
        System.out.println("Collection 层保存方法...");
    }
}
