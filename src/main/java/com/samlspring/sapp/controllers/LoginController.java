package com.samlspring.sapp.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml.metadata.MetadataManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;


@Controller
@RequestMapping("/saml")
public class LoginController {

    private static final Logger LOG = Logger.getRootLogger();

    @Autowired
    MetadataManager metadataManager;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String idpSelection(HttpServletRequest request, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        //always false
        if (auth == null) {
            LOG.debug("Authentication instance is : " + auth);
        } else {
            LOG.info(this.getClass().getSimpleName() + " GET path='/saml/login'");
        }

//      auth == null -> always going to be false so the if block it defeats its purpose
        if (auth == null || (auth instanceof AnonymousAuthenticationToken)) // (auth instanceof AnonymousAuthenticationToken)-> always going to be true..
        {
            //get IdP names from the MetadataManager
            Set<String> idps = metadataManager.getIDPEntityNames();

            //add IdP to MVC model
            for (String idp : idps)
            model.addAttribute("idps", idps);
            LOG.info("Adding to Model attribute idps: " + idps);
            return "/login";

        } else {
            LOG.info("User already logged in! redirect:/landing");
            return "redirect:/saml/login?idp=http://localhost:8180/auth/realms/crc";
        }
    }


}
