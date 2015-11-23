-- JBoss, Home of Professional Open Source
-- Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
-- contributors by the @authors tag. See the copyright.txt in the
-- distribution for a full listing of individual contributors.
--
-- Licensed under the Apache License, Version 2.0 (the 'License');
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
-- http://www.apache.org/licenses/LICENSE-2.0
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an 'AS IS' BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

-- You can use this file to load seed data into the database using SQL statements
INSERT INTO TIPO_DOCUMENTO (ABREVIATURA,DENOMINACION,CANTIDAD_CARACTERES,TIPO_PERSONA,ESTADO,optlk) VALUES ('DNI','Documento Nacional de Identidad',8,'NATURAL','T',1);
INSERT INTO TIPO_DOCUMENTO (ABREVIATURA,DENOMINACION,CANTIDAD_CARACTERES,TIPO_PERSONA,ESTADO,optlk) VALUES ('PASAPORTE','Pasaporte',11,'NATURAL','T',1);
INSERT INTO TIPO_DOCUMENTO (ABREVIATURA,DENOMINACION,CANTIDAD_CARACTERES,TIPO_PERSONA,ESTADO,optlk) VALUES ('P.NAC.','Partida de Nacimiento',11,'NATURAL','F',1);
INSERT INTO TIPO_DOCUMENTO (ABREVIATURA,DENOMINACION,CANTIDAD_CARACTERES,TIPO_PERSONA,ESTADO,optlk) VALUES ('RUC','Registro Unico de Contribuyente',11,'JURIDICA','T',1);