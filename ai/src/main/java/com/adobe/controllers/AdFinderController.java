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
package com.adobe.controllers;

import com.adobe.models.ImageFinderService;
import com.adobe.models.ImagePath;
import com.adobe.models.UserPrefCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@RestController
public class AdFinderController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Random rand = new Random();

    @Autowired
    private ImageFinderService imageFinder;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value="/mlAdResolve/{user}", method = RequestMethod.GET)
    public ImagePath findAd(@PathVariable String user) {
        insertRandomFailure();
        UserPrefCount top;
        try {
            top = restTemplate.getForEntity("http://userpref:8083/user/" + user + "/top", UserPrefCount.class).getBody();
        } catch (Exception e) {
            logger.error("Message=\"UserPref Service Artificial Failure\"", e);
            top = new UserPrefCount().setCount(3).setId("default");
        }
        return imageFinder.findByUser(top);
    }

    private void insertRandomFailure() {
        if (rand.nextInt(1000) < 10) {
            throw new RuntimeException("Artificial AI exception generated");
        }
    }
}