package ch.epfl.rigel.coordinates;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import ch.epfl.rigel.math.Angle;

public class StereographicProjectionTest {

  @Test
  public void StereographicProjectionApplyTest() {     
      assertEquals(new StereographicProjection(HorizontalCoordinates.ofDeg(45,45)).apply(HorizontalCoordinates.ofDeg(45,30)).y(),
                  -0.13165249758739583 , 1e-10);  
  }

  @Test
  public void StereographicProjectionInverseApply() {
      assertEquals(new StereographicProjection(HorizontalCoordinates.ofDeg(45,45)).inverseApply(CartesianCoordinates.of(10,0)).az(),
              3.648704634091643 , 1e-10);  
  }
  
  @Test
  public void StereographicProjectionApplyToAngle() {
      assertEquals(new StereographicProjection (HorizontalCoordinates.ofDeg(23, 45)).applyToAngle(Angle.ofDeg(1/2.0)),
              0.00436333005262522 , 1e-10);  
  }
  
  @Test
  public void StereographicCircleCenterForParallel() {
      assertEquals(new StereographicProjection(HorizontalCoordinates.ofDeg(45,45)).circleCenterForParallel(HorizontalCoordinates.ofDeg(0,27)).y(),
              0.6089987400733187 , 1e-10);  
  }
  
  @Test
  public void StereographicCircleCenterForParallel2() {
      assertEquals(new StereographicProjection(HorizontalCoordinates.ofDeg(45,45)).circleRadiusForParallel(HorizontalCoordinates.ofDeg(0,27)),
              0.767383180397855 , 1e-10);  
  }
}
