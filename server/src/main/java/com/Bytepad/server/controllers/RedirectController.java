package com.Bytepad.server.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class RedirectController {

    /**
     * Redirects every incorrect request made on base URL to the home page
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/{[path:[^\\.]*}")
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/");
    }
}