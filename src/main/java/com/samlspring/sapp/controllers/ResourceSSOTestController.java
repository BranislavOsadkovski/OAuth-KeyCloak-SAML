package com.samlspring.sapp.controllers;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("slide")
public class ResourceSSOTestController {

    private static final Logger LOG = Logger.getRootLogger();
    @RequestMapping(method = RequestMethod.GET,path = "/learn")
    public String slideShow(){

        LOG.info(this.getClass().getSimpleName()+ " GET path='/learn' ");
        return "logout";
    }

    @RequestMapping(method = RequestMethod.GET,path = "/learn1")
    public String learn1(){

        return "data2";
    }
    @RequestMapping(method = RequestMethod.GET,path = "/learn3")
    public String slide (){
 
        return "data3";
    }
}
