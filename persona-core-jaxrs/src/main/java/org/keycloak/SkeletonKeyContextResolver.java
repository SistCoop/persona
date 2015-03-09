package org.keycloak;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * Any class with package org.jboss.resteasy.skeleton.key will use NON_DEFAULT
 * inclusion
 * 
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@Provider
public class SkeletonKeyContextResolver implements ContextResolver<Object> {

	@Override
	public Object getContext(Class<?> type) {
		// TODO Auto-generated method stub
		return null;
	}

}
