package com.szparag.ijtest1.Configurators;

import java.io.IOException;

/**
 * Created by Szparagowy Krul 3000 on 11/06/2015.
 */
public interface ConfigFileReadWrite {

    public boolean fileInitialize() throws IOException;

    public boolean fileRead() throws IOException;

    public void updateStats();

    public void fileWrite(String sectionname, String optionname, String optionvalue) throws IOException;

    public void fileWrite() throws IOException;

    public void debugPrinter();

}
