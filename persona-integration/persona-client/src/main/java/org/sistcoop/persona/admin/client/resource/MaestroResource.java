/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sistcoop.persona.admin.client.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author carlosthe19916@gmail.com
 */

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public interface MaestroResource {

	@GET
	@Path("tiposPersona")
	public List<String> getAllTipoPersonas();

	@GET
	@Path("estadosCiviles")
	public List<String> getAllEstadosCiviles();

	@GET
	@Path("sexos")
	public List<String> getAllSexos();

	@GET
	@Path("tiposEmpresa")
	public List<String> getAllTiposEmpresa();

}
