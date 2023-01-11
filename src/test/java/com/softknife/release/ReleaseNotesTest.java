package com.softknife.release;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.lang.invoke.MethodHandles;


@SpringBootTest(classes = ReleaseNotesApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ReleaseNotesTest {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public String readFile(String fileName) throws IOException {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		return IOUtils.toString(classloader.getResourceAsStream(fileName), "UTF-8");
	}

}
