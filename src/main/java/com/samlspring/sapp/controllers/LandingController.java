package com.samlspring.sapp.controllers;
import com.samlspring.sapp.SAMLcore.SAMLUserDetailsServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.metadata.MetadataManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.http.HttpResponse;

@Controller
@RequestMapping("authenticator")
public class LandingController {
    private static final Logger LOG = Logger.getRootLogger();

    @Autowired
    MetadataManager metadataManager;
    @Autowired
    SAMLUserDetailsServiceImpl detailsService;


    @RequestMapping(path = "/landing" , method = RequestMethod.GET)
    public String landing(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();



        String userId = detailsService.loadUserBySAML((SAMLCredential) auth.getCredentials()).getID();
        model.addAttribute("userID",userId);

        LOG.info(this.getClass().getSimpleName() + " GET path='/landing'");
        return "landing";
    }


    @RequestMapping(path = "/" , method = RequestMethod.GET)
    public String auth(@RequestParam HttpResponse response) {

        LOG.info(this.getClass().getSimpleName() + " GET path='/'");


            return "redirect:/";
    }

}