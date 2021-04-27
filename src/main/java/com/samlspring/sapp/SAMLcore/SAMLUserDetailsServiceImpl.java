package com.samlspring.sapp.SAMLcore;

import com.samlspring.sapp.entityImpl.LocalUser;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.schema.impl.XSAnyImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SAMLUserDetailsServiceImpl implements SAMLUserDetailsService {

    /**
     * Parses local user from SAML request and loads it with SAMLCredential object
     * The method is supposed to identify local account of user referenced by
     * data in the SAML assertion and return UserDetails object describing the user.
     * <p>
     * org.opensaml.saml2.core.Attribute  has method getSAMLAttributeValues() that returns java.util.List with generic type XMLObject List<XMLObject> that needs to be unmarshalled using
     * XML parser Unmarshal XMLObject for saml2 Attribute.class that contains Attribute values
     *
     * @param credential
     * @return User
     * @throws UsernameNotFoundException
     */
    public LocalUser loadUserBySAML(SAMLCredential credential)
            throws UsernameNotFoundException {

        String samlID = getSAMLAttributeValue(credential.getAttribute("samlID").getAttributeValues().stream().findFirst().get());
        String Username = getSAMLAttributeValue(credential.getAttribute("Username").getAttributeValues().stream().findFirst().get());
        String FirstName = getSAMLAttributeValue(credential.getAttribute("FirstName").getAttributeValues().stream().findFirst().get());
        String LastName = getSAMLAttributeValue(credential.getAttribute("LastName").getAttributeValues().stream().findFirst().get());
        String Email = getSAMLAttributeValue(credential.getAttribute("Email").getAttributeValues().stream().findFirst().get());
        String locale = getSAMLAttributeValue(credential.getAttribute("locale").getAttributeValues().stream().findFirst().get());

        List<String> roles = new ArrayList<>();

        credential.getAttributes().forEach((attribute -> {
            if (attribute.getName().equals("Role")) {
                roles.add(getSAMLAttributeValue(attribute.getAttributeValues().stream().findFirst().get()));
            }
        }));

        LocalUser localUser = new LocalUser(samlID, Username, FirstName, LastName, Email, locale, roles);

        return localUser;
    }


    /**
     * SAMLAssertionAttributeParser
     * <p>
     * XmlObject requires some unpacking to work with
     */
    private String getSAMLAttributeValue(XMLObject attributeValue) {
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


}
