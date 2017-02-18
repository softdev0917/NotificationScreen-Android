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

package com.example.ab.notificationscreen.notificationmodels;

public enum ModelType {
  NEW_NOTIFICATION_SECTION(0),
  PREVIOUS_NOTIFICATION_SECTION(1),
  LIKES_NOTIFICATION(2),
  FRIEND_REQUEST_NOTIFICATION(3),
  COMMENTS_NOTIFICATION(4),
  FOLLOW_NOTIFICATION(5),
  MENTIONS_NOTIFICATION(6),
  TAGGING_NOTIFICATION(7),
  SCREENSHOTS_NOTIFICATION(8),
  TOOK_SCREENSHOT_NOTIFICATION(9),
  LIKED_POST_NOTIFICATION(10),
  TEXTPOST_NOTIFICATION(11);

  private int value;

  private ModelType(int value) {
    this.value = value;
  }

  public static ModelType fromValue(int value) {
    for (ModelType type: ModelType.values()) {
      if (type.value == value) {
        return type;
      }
    }

    return null;
  }

  public int value() {
    return value;
  }
}
