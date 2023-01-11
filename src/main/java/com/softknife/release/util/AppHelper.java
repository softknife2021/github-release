package com.softknife.release.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author amatsaylo on 2019-07-20
 * @project release-notes
 */
@Component
public class AppHelper {

    public String readFile(String fileName) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(fileName);
        InputStream inputStream = classPathResource.getInputStream();
        return IOUtils.toString(inputStream);
    }

    public void createFile(String reportDir, String reportData) throws IOException {
        FileUtils.touch(new File(reportDir));
        FileUtils.writeStringToFile(new File(reportDir), reportData, "UTF-8");
    }

    public String ymlToJson(String yaml) throws IOException {
        ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
        Object obj = yamlReader.readValue(yaml, Object.class);
        ObjectMapper jsonWriter = new ObjectMapper();
        return jsonWriter.writeValueAsString(obj);
    }
}
