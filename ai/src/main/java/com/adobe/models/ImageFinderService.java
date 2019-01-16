/*
Copyright 2019 Adobe. All rights reserved.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.adobe.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ImageFinderService {
    Logger logger = LoggerFactory.getLogger(getClass());
    Random rand = new Random();

    Map<String, String> images = new HashMap<>();

    public ImageFinderService() {
        images.put("brazil", "/static/ads/costarica.png");
        images.put("rome", "/static/ads/paris.png");
        images.put("thailand", "/static/ads/beijing.png");
        images.put("default", "/static/ads/paris.png");
    }

    public ImagePath findByUser(UserPrefCount pref) {
        insertRandomSlowDown();
        return new ImagePath().setPath(images.get(pref.getId()));
    }

    private void insertRandomSlowDown() {
        try {
            boolean failure = (rand.nextInt(1000) < 10);
            int sleepTime;
            if (failure) {
                sleepTime = rand.nextInt(8000) + 2000;
                logger.error("Message=\"Ad Finder Artificial Failure\" Millis={}", sleepTime);
            } else {
                sleepTime = rand.nextInt(30) + 20;
            }
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {}
    }
}
