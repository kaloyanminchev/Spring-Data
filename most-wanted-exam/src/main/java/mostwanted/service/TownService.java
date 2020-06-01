package mostwanted.service;



import mostwanted.domain.entities.Town;

import java.io.FileNotFoundException;
import java.io.IOException;


public interface TownService {

    Boolean townsAreImported();

    String readTownsJsonFile() throws IOException;

    String importTowns(String townsFileContent) throws FileNotFoundException;

    String exportRacingTowns();

    Town getTownByName(String name);
}
