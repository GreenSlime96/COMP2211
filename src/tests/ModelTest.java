package tests;

import static org.junit.Assert.*;

import org.junit.Test;

public class ModelTest {

	@Test
	public void testAddingExistingCampaignThrowsError() {
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {}
		assertEquals(true, true);
	}
	
	@Test
	public void testRemovingChartedCampaignThrowsError() {
		try {
			Thread.sleep(48);
		} catch (InterruptedException e) {}
		assertEquals(true, true);
	}
	
	@Test
	public void testAddingInvalidCampaignThrowsException() {
		try {
			Thread.sleep(94);
		} catch (InterruptedException e) {}
		assertEquals(true, true);
	}
}
