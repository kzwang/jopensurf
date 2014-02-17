package com.stromberglabs.jopensurf;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * It sure would be nice to have some real tests =/
 */
public class IntegralImageTest {
  @Test
	public void testCalculateIntensity() throws Exception {
    float maximumError = 0.001F;
		assertEquals(1F, IntegralImage.calculateIntensity(new int[]{255, 255, 255}), maximumError);
    assertEquals(0F, IntegralImage.calculateIntensity(new int[]{0, 0, 0}), maximumError);
    assertEquals(0.498F, IntegralImage.calculateIntensity(new int[]{127, 127, 127}), maximumError);
	}

  @Test
  public void testCalculateIntegralImage_TestTableValues() throws Exception {
    int[][][] fakeImage = new int[][][] {
      {{255, 255, 255, 255}, {255, 255, 255, 255}},
      {{255, 255, 255, 255}, {255, 255, 255, 255}},
      {{255, 255, 255, 255}, {255, 255, 255, 255}},
    };
    float[][] expectedTableValuesImage = new float[][] {
      { 1.0F, 2.0F},
      { 2.0F, 4.0F},
      { 3.0F, 6.0F},
    };

    ImageWrapper image = new ImageWrapper(fakeImage);
    IntegralImage integralImage = new IntegralImage(new ImageWrapper(fakeImage));
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        assertEquals(expectedTableValuesImage[y][x], integralImage.getTableValue(x, y), 0);
      }
    }
  }

  @Test
  public void testCalculateIntegralImage_TestOnePixelEqualOriginal() throws Exception {
    int[][][] fakeImage = new int[][][] {
      {{255, 255, 255, 255}, {255, 255, 255, 255}},
      {{255, 255, 255, 255}, {255, 255, 255, 255}},
      {{255, 255, 255, 255}, {255, 255, 255, 255}},
    };
    float[][] expectedOneByOneIntegrals = new float[][] {
      { 1.0F, 1.0F},
      { 1.0F, 1.0F},
      { 1.0F, 1.0F},
    };

    ImageWrapper image = new ImageWrapper(fakeImage);
    IntegralImage integralImage = new IntegralImage(image);
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        assertEquals(expectedOneByOneIntegrals[y][x],
            integralImage.getIntegralValue(x, y, 1, 1), 0);
      }
    }
  }

  @Test
  public void testCalculateIntegralImage_TestBasicImage() throws Exception {
    int[][][] fakeImage = new int[][][] {
         /*       row 1                 row 2               row 3                row  4        */
  /* 1 */{{255, 255, 255, 255}, {  0,   0,   0,   0}, {255, 255, 255, 255}, {255, 255, 255, 255}},
  /* 2 */{{  0,   0,   0,   0}, {  0,   0,   0,   0}, {255, 255, 255, 255}, {255, 255, 255, 255}},
  /* 3 */{{255, 255, 255, 255}, {255, 255, 255, 255}, {255, 255, 255, 255}, {255, 255, 255, 255}},
  /* 4 */{{255, 255, 255, 255}, {255, 255, 255, 255}, {255, 255, 255, 255}, {255, 255, 255, 255}},
    };

    ImageWrapper image = new ImageWrapper(fakeImage);
    IntegralImage integralImage = new IntegralImage(image);
    assertEquals(4F /* The four bottom right */,
        integralImage.getIntegralValue(2 /* x */, 2 /* y */, 2 /* height */, 2 /* width */), 0);
    assertEquals(3F /* The three in the middle */,
        integralImage.getIntegralValue(1 /* x */, 1 /* y */, 2 /* height */, 2 /* width */), 0);
    assertEquals(1F /* The one of four in the upper left */,
        integralImage.getIntegralValue(0 /* x */, 0 /* y */, 2 /* height */, 2 /* width */), 0);
  }
}
