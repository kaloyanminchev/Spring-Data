package mostwanted.service.impls;

import mostwanted.service.RaceService;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@Service
public class RaceServiceImpl implements RaceService {
    @Override
    public Boolean racesAreImported() {
        return false;
    }

    @Override
    public String readRacesXmlFile() throws IOException {
        return null;
    }

    @Override
    public String importRaces() throws JAXBException {
        return null;
    }
}
