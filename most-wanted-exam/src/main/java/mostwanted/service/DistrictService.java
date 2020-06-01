package mostwanted.service;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface DistrictService {

    Boolean districtsAreImported();

    String readDistrictsJsonFile() throws IOException;

    String importDistricts(String districtsFileContent) throws FileNotFoundException;
}
