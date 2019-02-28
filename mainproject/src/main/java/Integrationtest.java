package com.project.org;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

@RunWith(DevAppServerTestRunner.class)
@DevAppServerTest(IntegrationTest.TestConfig.class)
public class IntegrationTest {

  public class TestConfig extends BaseDevAppServerTestConfig {

    @Override public File getSdkRoot() {
      // You may need to tweak this.
      return new File("../../appengine-java-sdk-1.9.15");
    }

    @Override public File getAppDir() {
      return new File("war");
    }

    @Override
    public List<URL> getClasspath() {
      // There may be an easier way to do this.
      List<URL> classPath = new ArrayList<>();
      try {
        String separator = System.getProperty("path.separator");
        String[] pathElements = System.getProperty("java.class.path").split(separator);
        for (String pathElement : pathElements) {
          classPath.add(new File(pathElement).toURI().toURL());
        }
      }
      catch (MalformedURLException e) {
        throw new RuntimeException(e);
      }
      return classPath;
    }
  }

  private final LocalServiceTestHelper testHelper;
  private final String port;

  public HelloWorldTest() {
    testHelper = new LocalServiceTestHelper();
    port = System.getProperty("appengine.devappserver.test.port");
  }

  @Before public void setUpServer() {
    testHelper.setUp();
  }

  @After public void tearDown() {
    testHelper.tearDown();
  }

  @Test public void testHello() throws Exception {
    URL url = new URL("http://localhost:" + port + "/hello");
    HTTPResponse response = URLFetchServiceFactory.getURLFetchService().fetch(url);
    assertEquals(200, response.getResponseCode());
    assertEquals("used", new String(response.getContent(), "UTF-8"));
  }
}
