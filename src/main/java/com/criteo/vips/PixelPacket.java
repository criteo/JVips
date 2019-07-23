/*
  Copyright (c) 2019 Criteo

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package com.criteo.vips;

public class PixelPacket extends Vips {
    double r;
    double g;
    double b;
    double a;

    public PixelPacket(double r, double g, double b, double a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public PixelPacket(double r, double g, double b) {
        this(r, g, b, 255.0);
    }

    public double getRed() {
        return r;
    }

    public double getGreen() {
        return g;
    }

    public double getBlue() {
        return b;
    }

    public double getAlpha() {
        return a;
    }

    public void setRed(double r) {
        this.r = r;
    }

    public void setGreen(double g) {
        this.g = g;
    }

    public void setBlue(double b) {
        this.b = b;
    }

    public void setAlpha(double a) {
        this.a = a;
    }

    public double[] getComponents() {
        return new double[] { r, g, b, a };
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PixelPacket) {
            PixelPacket pixelPacket = (PixelPacket) o;

            return r == pixelPacket.r && g == pixelPacket.g && b == pixelPacket.b && a == pixelPacket.a;
        }
        return false;
    }
}
