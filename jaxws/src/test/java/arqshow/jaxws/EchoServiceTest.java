package arqshow.jaxws;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class EchoServiceTest {

    private static Service service;

    @ArquillianResource
    private URL url;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class).
                addPackage(EchoServiceImpl.class.getPackage());
    }

    @Before
    public void setupClass() throws MalformedURLException {
        service = Service.create(
                new URL(url, "EchoService?wsdl"),
                new QName("http://jaxws.arqshow/", "EchoService"));
    }

    @Test
    public void canEcho() {
        EchoService echoServicePort = service.getPort(EchoService.class);
        Assert.assertEquals("hello", echoServicePort.echo("hello"));
    }
}
