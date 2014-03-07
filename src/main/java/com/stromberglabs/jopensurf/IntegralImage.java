/*
This work was derived from Chris Evan's opensurf project and re-licensed as the
3 clause BSD license with permission of the original author. Thank you Chris! 

Copyright (c) 2010, Andrew Stromberg
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither Andrew Stromberg nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Andrew Stromberg BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.stromberglabs.jopensurf;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.Serializable;

/**
 * ABOUT generateIntegralImage
 * 
 * When OpenSURF stores it's version of the integral image, some slight rounding actually
 * occurs, it doesn't maintain the same values from when it calculates the integral image
 * to when it calls BoxIntegral on the same data
 * @author astromberg
 *
 * Example from C++ OpenSURF - THIS DOESN'T HAPPEN IN THE JAVA VERSION
 * 
 * IntegralImage Values at Calculation Time:
 * integralImage[11][9] = 33.69019699
 * integralImage[16][9] = 47.90196228
 * integralImage[11][18] = 65.84313202
 * integralImage[16][18] = 93.58038330
 * 
 * 
 * integralImage[11][18] = 65.84313202
 * que? integralImage[18][11] = 64.56079102
 * 
 * IntegralImage Values at BoxIntegral Time:
 * img[11][9] = 33.83921814
 * img[11][18] = 64.56079102
 * img[16][9] = 48.76078796
 * img[16][18] = 93.03530884
 *
 */
public class IntegralImage implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private float[][] mIntImage;
	private int width = -1;
	private int height = -1;

  public IntegralImage(ImageWrapper image) {
		mIntImage = new float[image.getWidth()][image.getHeight()];
		width = image.getWidth();
		height = image.getHeight();
		
		float sum;
		for (int y = 0; y < height; y++){
			sum = 0F;
			for (int x = 0; x < width; x++){
				int[] pixel = image.getPixelAt(x, y);
				float intensity = calculateIntensity(pixel);
				sum += intensity;
				if (y == 0){
					mIntImage[x][y] = sum;
				} else {
					mIntImage[x][y] = sum + mIntImage[x][y-1];
				}
			}
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	float getTableValue(int column, int row) {
		return mIntImage[column][row];
	}

  /**
   * Returns the intensity integral over the square defined starting at row, col and extending down
   * and to the right by rows, cols in size.
   */
  public float getIntegralValue(int row, int col, int rows, int cols){
		int r1 = row - 1;
		int c1 = col - 1;
		int r2 = row + rows - 1;
		int c2 = col + cols - 1;

		float A = isInBounds(r1, c1) ? mIntImage[r1][c1] : 0;
		float B = isInBounds(r1, c2) ? mIntImage[r1][c2] : 0;
		float C = isInBounds(r2, c1) ? mIntImage[r2][c1] : 0;
		float D = isInBounds(r2, c2) ? mIntImage[r2][c2] : 0;

		return Math.max(0F, A - B - C + D);
	}
  
  private boolean isInBounds(int x, int y) {
    return x >= 0 && x < width && y >= 0 && y < height;
  }

  static float calculateIntensity(int[] pixel) {
    double weightedIntensity = 0.299D * pixel[0] + 0.587D * pixel[1] + 0.114D * pixel[2];
    return (float) (weightedIntensity / 255D);
  }
}
