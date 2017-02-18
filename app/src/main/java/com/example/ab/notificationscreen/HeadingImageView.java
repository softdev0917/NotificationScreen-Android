/*
 * Copyright (c) 2016 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.example.ab.notificationscreen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class HeadingImageView extends ImageView {

  public float mRadius = 20.0f;

  public HeadingImageView(Context context){
    super(context);
  }

  public HeadingImageView(Context context, AttributeSet attrs){
    super(context, attrs);
  }

  public HeadingImageView(Context context, AttributeSet attrs, int defStyle){
    super(context, attrs, defStyle);
  }

  public HeadingImageView(Context context, float radius){
    super(context);
    this.mRadius = radius;
  }

  @Override
  protected void onDraw(Canvas canvas){
    Path clipPath = new Path();
    RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
    clipPath.addRoundRect(rect, mRadius, mRadius, Path.Direction.CW);
    canvas.clipPath(clipPath);
    super.onDraw(canvas);
  }
}
