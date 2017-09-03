package arqshow.jpaext;

import java.util.Arrays;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class GameRepositoryTest {

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(JavaArchive.class, "test.jar")
                .addPackage(Game.class.getPackage())
                // required for remote containers in order to run tests with FEST-Asserts
                .addPackages(true, "org.fest")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("test-persistence.xml", "persistence.xml");
    }

    @Inject
    GameRepository gameRepository;

    @Test
    @UsingDataSet("games.yml")
    public void shouldFindAllGames() throws Exception {
        List<Game> allGames = gameRepository.fetchAll();
        Assert.assertEquals(3, allGames.size());
    }

    @Test
    @UsingDataSet({"games.yml", "platforms.yml"})
    public void shouldFindAllGamesForPc() throws Exception {
        Platform pc = gameRepository.getPlatform("PC");
        List<Game> gamesForPc = gameRepository.fetchAllFor(pc);
        Assert.assertEquals(2, gamesForPc.size());

        List<String> titles = Arrays.asList("Batman Arkham Asylum", "Baldur's Gate");
        Assert.assertTrue(gamesForPc.stream().map(g -> g.getTitle()).collect(Collectors.toList()).containsAll(titles));
    }

    @Test
    @UsingDataSet({"games.yml", "platforms.yml"})
    public void shouldFindAllGamesForXbox360() throws Exception {
        Platform pc = gameRepository.getPlatform("XBOX 360");
        List<Game> gamesForXBox = gameRepository.fetchAllFor(pc);
        Assert.assertEquals(1, gamesForXBox.size());

        Assert.assertTrue(gamesForXBox.stream().map(g -> g.getTitle()).collect(Collectors.toList()).contains("Batman Arkham Asylum"));
    }

    @Test
    @UsingDataSet({"games.yml", "platforms.yml"})
    public void shouldFindOldestGame() throws Exception {
        List<Game> oldestGames = gameRepository.findOldestGames();
        Assert.assertEquals(1, oldestGames.size());

        Assert.assertTrue(oldestGames.stream().map(g -> g.getTitle()).collect(Collectors.toList()).contains("Baldur's Gate"));

        Assert.assertTrue(1998 == oldestGames.get(0).getYear());
    }

}
