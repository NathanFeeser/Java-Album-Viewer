import org.junit.Test;


public class JUnitTests {
	/* ------------------------ MainJFrame.java tests ------------------------ */
	@Test
	public void verifyGUICreation() {
		MainJFrame main = new MainJFrame();
		org.junit.Assert.assertNotNull("Failure - GUI not created properly", main.bottomPanel);
	}
	@Test
	public void verifyGlobalSettings() {
		MainJFrame main = new MainJFrame();
		org.junit.Assert.assertEquals("Failure - Interval is not set to 1 initially", main.INTERVAL, 1);
	}
	/* ------------------------ TopPanel.java tests ------------------------ */
	@Test
	public void verifyAlbumsCreated() {
		MainJFrame main = new MainJFrame();
		org.junit.Assert.assertNotNull("Failure - Albums not created properly", TopPanel.getAlbums());
	}
	@Test
	public void verifySplitPaneCreation() {
		MainJFrame main = new MainJFrame();
		org.junit.Assert.assertNotNull("Failure - Albums not created properly", TopPanel.getSplitPane());
	}
	/* ---------------------------- Album.java tests ------------------------- */
	@Test
	public void verifyAlbumDirectotyCreation() {
		MainJFrame main = new MainJFrame();
		org.junit.Assert.assertSame("Failure - Album directoty not setup correctly", Album.albumDir, "Albums/");
	}
	@Test
	public void verifyAlbumListNotNull() {
		MainJFrame main = new MainJFrame();
		org.junit.Assert.assertNotNull("Failure - albumList should not be null", Album.albumList);
	}
	@Test
	public void verifyIconListNotNull() {
		MainJFrame main = new MainJFrame();
		org.junit.Assert.assertNull("Failure - iconList should be null at this point", Album.iconList);
	}
	@Test
	public void verifyMultipleAlbumVariables() {
		MainJFrame main = new MainJFrame();
		org.junit.Assert.assertNotNull("Failure - listModel should not be null", Album.listModel);
		org.junit.Assert.assertNotNull("Failure - albums should not be null", Album.albums);
		org.junit.Assert.assertNotNull("Failure - albumLabel should not be null", Album.albumLabel);
		org.junit.Assert.assertNotNull("Failure - picture should not be null", Album.picture);
		org.junit.Assert.assertNotNull("Failure - iconWidthHeight should not be null", Album.iconWidthHeight);
		org.junit.Assert.assertNotNull("Failure - iconNames should not be null", Album.iconNames);
	}
}
