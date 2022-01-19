package dmp.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.stream.Collectors;

public class ReadPropertyFile {

    public HashMap<String, String> readFileProperty(String propertyFileName) {
        Properties prop = new Properties();
        try (InputStream inputStream = ReadPropertyFile.class.getClassLoader().getResourceAsStream(propertyFileName)) {
            if (inputStream == null) {
                System.out.println("Property "+propertyFileName +" file not found");
            }
            prop.load(inputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop.entrySet().stream().collect(
                Collectors.toMap(
                        e -> String.valueOf(e.getKey()),
                        e -> String.valueOf(e.getValue()),
                        (prev, next) -> next, HashMap::new
                ));
    }
}
