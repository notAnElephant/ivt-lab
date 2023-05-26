package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;

  // Create mock instances of TorpedoStore
  TorpedoStore primaryTorpedoStore;
  TorpedoStore secondaryTorpedoStore;

  @BeforeEach
  public void init() {
    this.primaryTorpedoStore = mock(TorpedoStore.class);
    this.secondaryTorpedoStore = mock(TorpedoStore.class);
    this.ship = new GT4500(primaryTorpedoStore, secondaryTorpedoStore);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(primaryTorpedoStore.fire(1)).thenReturn(true);
    when(primaryTorpedoStore.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStore.fire(1)).thenReturn(true);
    when(secondaryTorpedoStore.isEmpty()).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(primaryTorpedoStore, times(1)).fire(1);
    verify(secondaryTorpedoStore, times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_All_Success() {
    // Arrange
    when(primaryTorpedoStore.fire(1)).thenReturn(true);
    when(primaryTorpedoStore.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStore.fire(1)).thenReturn(true);
    when(secondaryTorpedoStore.isEmpty()).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);
    verify(primaryTorpedoStore, times(1)).fire(1);
    verify(secondaryTorpedoStore, times(1)).fire(1);
  
  }

  @Test
  public void testSingleModeSingleShotFiredIncorrectly() {
    // Arrange
    when(primaryTorpedoStore.isEmpty()).thenReturn(true);
    //when(primaryTorpedoStore.getTorpedoCount()).thenReturn(1);

    // Act
    boolean firingSuccess = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(false, firingSuccess);
  }

  @Test
  public void testSingleModeTwoShotsFiredCorrectly() {
    // Arrange
    when(primaryTorpedoStore.fire(1)).thenReturn(true);
    when(primaryTorpedoStore.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStore.fire(1)).thenReturn(true);
    when(secondaryTorpedoStore.isEmpty()).thenReturn(false);

    // Act
    boolean firstShotSuccess = ship.fireTorpedo(FiringMode.SINGLE);
    boolean secondShotSuccess = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, firstShotSuccess);
    assertEquals(true, secondShotSuccess);
  }

  @Test
  public void testSingleModeFiringWithEmptyTorpedoStores() {
    // Arrange
    when(primaryTorpedoStore.getTorpedoCount()).thenReturn(0);
    when(secondaryTorpedoStore.getTorpedoCount()).thenReturn(0);
    
    // Act
    boolean firingSuccess = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(false, firingSuccess);
  }

  @Test
  public void testAllModeFiringWithNonEmptyTorpedoStores() {
    // Arrange
    when(primaryTorpedoStore.fire(1)).thenReturn(true);
    when(secondaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean firingSuccess = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, firingSuccess);
    assertEquals(0, primaryTorpedoStore.getTorpedoCount());
    assertEquals(0, secondaryTorpedoStore.getTorpedoCount());
  }

  @Test
  public void testAllModeFiringWithEmptyTorpedoStores() {
    // Arrange
    when(primaryTorpedoStore.getTorpedoCount()).thenReturn(0);
    when(secondaryTorpedoStore.getTorpedoCount()).thenReturn(0);

    // Act
    boolean firingSuccess = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(false, firingSuccess);
  }

  @Test
  public void testBySourceCode_FireAllWithOneEmpty() {
     // Arrange
    when(primaryTorpedoStore.isEmpty()).thenReturn(false);
    when(primaryTorpedoStore.fire(1)).thenReturn(true);
    when(secondaryTorpedoStore.isEmpty()).thenReturn(true);

    // Act
    boolean firingSuccess = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, firingSuccess);
    verify(primaryTorpedoStore, times(1)).fire(1);
    verify(secondaryTorpedoStore, times(0)).fire(1);
  }

  @Test
  public void testSingleFireOnlySecondIsEmpty() {
     // Arrange
    when(primaryTorpedoStore.getTorpedoCount()).thenReturn(0);
    when(primaryTorpedoStore.fire(1)).thenReturn(false);
    when(primaryTorpedoStore.isEmpty()).thenReturn(false);
    
    when(secondaryTorpedoStore.isEmpty()).thenReturn(true);
    
    // Act
    boolean firingSuccess = ship.fireTorpedo(FiringMode.SINGLE);
    firingSuccess = ship.fireTorpedo(FiringMode.SINGLE);
    
    // Assert
    assertEquals(false, firingSuccess);
  }
}
