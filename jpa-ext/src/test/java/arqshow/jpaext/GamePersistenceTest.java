package arqshow.jpaext;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class GamePersistenceTest {

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(JavaArchive.class, "test.jar")
                .addPackage(Game.class.getPackage())
                // required for remote containers in order to run tests with FEST-Asserts
                .addPackages(true, "org.fest")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("test-persistence.xml", "persistence.xml");
    }

    @PersistenceContext
    EntityManager em;

    @Test
    @InSequence(1)
    @Transactional(TransactionMode.ROLLBACK)
    public void shouldPersistsPcGames() throws Exception {
        // given
        Game batman = new Game("Batman Arkham Asylum", 2009,
                BigDecimal.valueOf(17.0));
        Game baldurs = new Game("Baldur's Gate", 1998, BigDecimal.valueOf(9.0));
        Platform pc = new Platform("PC");
        pc.addGames(batman, baldurs);

        // when
        em.persist(batman);
        em.persist(baldurs);
        em.flush();
        em.clear();
        @SuppressWarnings("unchecked")
        List<Game> games = em.createQuery(selectAllInJPQL(Game.class))
                .getResultList();

        Assert.assertEquals(2, games.size());
    }

    @Test
    @InSequence(2)
    @Transactional
    // COMMIT after test execution is default transaction behavior
    public void shouldPersistsPcPlatformWithGames() throws Exception {
        // given
        Game batman = new Game("Batman Arkham Asylum", 2009,
                BigDecimal.valueOf(17.0));
        Game baldurs = new Game("Baldur's Gate", 1998, BigDecimal.valueOf(9.0));
        Platform pc = new Platform("PC");
        pc.addGames(batman, baldurs);

        // when
        em.persist(pc);
        em.flush();
        em.clear();
        @SuppressWarnings("unchecked")
        List<Platform> platforms = em.createQuery(
                selectAllInJPQL(Platform.class)).getResultList();

        // then
        Assert.assertEquals(1, platforms.size());
        Assert.assertTrue(2 == platforms.get(0).getGames().size());

        List<Game> expectedGames = Arrays.asList(batman, baldurs);
        Assert.assertTrue(platforms.get(0).getGames().containsAll(expectedGames));
    }

    @Test
    @InSequence(3)
    @ShouldMatchDataSet("datasets/pc-games.yml")
    public void shouldPersistsPcGamesAndVerifiesStateAfterTestExecution()
            throws Exception {
        // given
        Game batman = new Game("Batman Arkham Asylum", 2009,
                BigDecimal.valueOf(17.0));
        Game baldurs = new Game("Baldur's Gate", 1998, BigDecimal.valueOf(9.0));
        Platform pc = new Platform("PC");
        pc.addGames(batman, baldurs);

        // when
        em.persist(pc);

        // then
        // state is verified using @ShouldMatchDataSet feature
    }

    // Private helper methods
    private String selectAllInJPQL(Class<?> clazz) {
        return "SELECT entity FROM " + clazz.getSimpleName() + " entity";
    }
}
