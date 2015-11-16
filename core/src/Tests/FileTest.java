package Tests;

import com.szparag.ijtest1.Configurators.ConfigSystem;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

/**
 * Created by Szparagowy Krul 3000 on 14/06/2015.
 */
public class FileTest {

    ConfigSystem configSystem;

    @Test
    public void fileValidator(){
        try {
            configSystem = new ConfigSystem("android/assets/configgame.ini");
            configSystem.fileWrite("gameconfig", "turncount", "20");
            configSystem.fileWrite("gameconfig", "turncount", "60");
            assertEquals("60", configSystem.getTurncountString());

        } catch (IOException ioexc) {
            System.out.println("IOException (iexc):");
            System.out.println(ioexc.getCause());
            System.out.println(ioexc.getMessage());
        }
    }
}
