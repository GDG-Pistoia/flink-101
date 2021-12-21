package it.gdgpistoia.utils;

import lombok.SneakyThrows;
import lombok.val;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class PropertiesLoader {

    private File get(String filename){
        URL resource = getClass().getClassLoader().getResource(filename);
        if (resource == null) {
            throw new IllegalArgumentException("file not found!");
        } else {
            return new File(resource.getFile());
        }
    }

    private static PropertiesLoader getResourceLoader(){
        return new PropertiesLoader();
    }

    @SneakyThrows
    public static Properties load(String filename){
        val loader = getResourceLoader();
        InputStream input = new FileInputStream(loader.get(filename).getPath());
        Properties prop = new Properties();
        prop.load(input);
        return prop;
    }
}
