package com.samlspring.sapp.SAMLcore;

import com.samlspring.sapp.entityImpl.User;
import org.apache.log4j.Logger;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.schema.impl.XSAnyImpl;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SAMLUserDetailsServiceImpl implements SAMLUserDetailsService {

    private static final Logger LOG = Logger.getRootLogger();

    private String userID;
    private List<Attribute> attributes;
    private User user;
    public String loadUserId() {
        return userID;
    }

    /**
     * Parses local user from SAML request and loads it with SAMLCredential object
     * The method is supposed to identify local account of user referenced by
     * data in the SAML assertion and return UserDetails object describing the user.
     * <p>
     * <p>
     * <p>
     * org.opensaml.saml2.core.Attribute  has method getAttributeValues() that returns java.util.List with generic type XMLObject List<XMLObject> that needs to be unmarshalled using
     * XML parser Unmarshal XMLObject for saml2 Attribute.class that contains Attribute values
     *
     * @param credential
     * @return User
     * @throws UsernameNotFoundException
     */
    public User loadUserBySAML(SAMLCredential credential)
            throws UsernameNotFoundException {

        attributes = credential.getAttributes();

        credential.getAttribute("samlID").getAttributeValues().forEach((value) -> LOG.debug("ID : " + getAttributeValue(value)));
        credential.getAttribute("Username").getAttributeValues().forEach((value) -> LOG.debug("Username : " + getAttributeValue(value)));
        credential.getAttribute("FirstName").getAttributeValues().forEach((value) -> LOG.debug("First Name : " + getAttributeValue(value)));
        credential.getAttribute("LastName").getAttributeValues().forEach((value) -> LOG.debug("Last Name : " + getAttributeValue(value)));
        credential.getAttribute("Email").getAttributeValues().forEach((value) -> LOG.debug("Email : " + getAttributeValue(value)));
        credential.getAttribute("locale").getAttributeValues().forEach((value) -> LOG.debug("locale : " + getAttributeValue(value)));

        credential.getAttributes().forEach((attribute) -> {
            if (attribute.getName().equals("Role"))
                attribute.getAttributeValues().forEach((value) -> LOG.debug("ROLE :" + getAttributeValue(value)));
        });


        String samlID= getAttributeValue(credential.getAttribute("samlID").getAttributeValues().stream().findFirst().get());
        String Username= getAttributeValue(credential.getAttribute("Username").getAttributeValues().stream().findFirst().get());
        String FirstName= getAttributeValue(credential.getAttribute("FirstName").getAttributeValues().stream().findFirst().get());
        String LastName= getAttributeValue(credential.getAttribute("LastName").getAttributeValues().stream().findFirst().get());
        String Email= getAttributeValue(credential.getAttribute("Email").getAttributeValues().stream().findFirst().get());
        String locale= getAttributeValue(credential.getAttribute("locale").getAttributeValues().stream().findFirst().get());

        List<String> roles = new ArrayList<>();

                credential.getAttributes().forEach((attribute -> {
                    if(attribute.getName().equals("Role")){
                        roles.add( getAttributeValue(attribute.getAttributeValues().stream().findFirst().get()));
                    }
                }));
        userID = credential.getNameID().getValue();
//        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
//        authorities.add(authority);

        /** In a real scenario, this implementation has to locate user in a arbitrary
         *  dataStore based on information present in the SAMLCredential and
         *  returns such data in a form of application specific UserDetails object.
         */

//        return new User(userID, "<abc123>", true, true, true, true, authorities);  // <- org.springframework.security.core.userdetails.User
        user = new User(samlID,Username,FirstName,LastName,Email,locale,roles);

        LOG.info(user.toString());
        return  user;
    }





    /**
     * SAMLAssertionAttributeParser
     *
     */
    private String getAttributeValue(XMLObject attributeValue) {
        return attributeValue == null ?
                null :
                attributeValue instanceof XSString ?
                        getStringAttributeValue((XSString) attributeValue) :
                        attributeValue instanceof XSAnyImpl ?
                                getAnyAttributeValue((XSAnyImpl) attributeValue) :
                                attributeValue.toString();
    }

    private String getStringAttributeValue(XSString attributeValue) {
        return attributeValue.getValue();
    }

    private String getAnyAttributeValue(XSAnyImpl attributeValue) {
        return attributeValue.getTextContent();
    }

    public User loadUser() {
        return this.user;
    }
}
