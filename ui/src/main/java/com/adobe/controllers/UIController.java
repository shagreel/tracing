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

import com.adobe.models.UserPref;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
public class UIController {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/")
    public String greeting(HttpSession session, Model model) {
        model.addAttribute("name", getNameFromSession(session));
        return "index";
    }

    @GetMapping("/login")
    public String login(HttpSession session, Model model) {
        model.addAttribute("name", getNameFromSession(session));
        return "login";
    }

    @PostMapping("/savelogin")
    public RedirectView savelogin(HttpSession session,
                                  @RequestParam("name") String name) {
        session.setAttribute("username", name);
        return new RedirectView("/");
    }

    @GetMapping("/destination")
    public String destination(HttpSession session, Model model,
                              @RequestParam(name="user", required=false) String user,
                              @RequestParam(name="dest", required=false, defaultValue="unknown") String dest) {
        String name = getNameFromSession(session);
        model.addAttribute("name", name);
        if (user == null) {
            user = name;
        }
        UserPref pref = restTemplate.postForEntity("http://userpref:8083/user/"+user+"/pref/"+dest, null,  UserPref.class).getBody();
        return "index";
    }

    private String getNameFromSession(HttpSession session) {
        Object name = session.getAttribute("username");
        if (name == null) {
            return "default";
        } else {
            return (String)name;
        }
    }
}
