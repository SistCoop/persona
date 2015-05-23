
/*
 * Copyright 2014 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sistcoop.persona;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.keycloak.adapters.HttpFacade;
import org.keycloak.adapters.HttpFacade.Request;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;

/**
 *
 * @author Juraci Paixão Kröhling <juraci at kroehling.de>
 */
public class PathBasedKeycloakConfigResolver implements KeycloakConfigResolver {

    private final Map<String, KeycloakDeployment> cache = new ConcurrentHashMap<String, KeycloakDeployment>();
	
	@Override
    public KeycloakDeployment resolve(HttpFacade.Request request) {
        String path = request.getURI();
        
        
        System.out.print("Path:");
        System.out.println(path);
        
        System.out.println("remote:");
        System.out.println(request.getRemoteAddr());
        
        /*int multitenantIndex = path.indexOf("sucursales/");
        if (multitenantIndex == -1) {
            throw new IllegalStateException("Not able to resolve realm from the request path!");
        }*/

        /*String realm = path.substring(path.indexOf("sucursales/")).split("/")[1];
        if (realm.contains("?")) {
            realm = realm.split("\\?")[0];
        }*/

        KeycloakDeployment deployment = cache.get(path);
        if (null == deployment) {
            // not found on the simple cache, try to load it from the file system
        	InputStream is = null;
        	//if(realm.equalsIgnoreCase("master"))
        		is = getClass().getResourceAsStream("/" + "master" + "-keycloak.json");
        	//else
        	//	is = getClass().getResourceAsStream("/" + "default" + "-keycloak.json");
            if (is == null) {
                throw new IllegalStateException("Not able to find the file /" + path + "-keycloak.json");
            }
            deployment = KeycloakDeploymentBuilder.build(is);
            //cache.put(path, deployment);
        }

        return deployment;
    }

    

}