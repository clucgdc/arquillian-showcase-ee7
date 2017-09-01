package arqshow.cdi.singleton;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.junit.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class SingletonTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(OnlyOne.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    Instance<OnlyOne> singleton;

    @Test
    public void should_be_only_one() throws Exception {
        long firstId = singleton.get().getId();
        long secondId = singleton.get().getId();
        Assert.assertEquals(firstId, secondId);
    }
}
