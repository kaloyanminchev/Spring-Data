import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class App {
    public static final String GRINGOTTS_PU = "gringotts";
    public static final String SALES_PU = "sales";
    public static final String UNIVERSITY_PU = "university";
    public static final String HOSPITAL_PU = "hospital";
    public static final String BILLS_PAYMENT_SYSTEM_PU = "bills_payment_system";
    public static final String FOOTBALL_BETTING_PU = "football_bookmaker_system";

    public static void main(String[] args) {

        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory(BILLS_PAYMENT_SYSTEM_PU);

//        EntityManager manager = factory.createEntityManager();
//        Engine engine = new Engine(manager);
//        engine.run();
    }
}
