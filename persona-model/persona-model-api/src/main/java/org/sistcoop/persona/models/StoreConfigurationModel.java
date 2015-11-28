package org.sistcoop.persona.models;

/**
 * @author <a href="mailto:carlosthe19916@sistcoop.com">Carlos Feria</a>
 */

public interface StoreConfigurationModel extends Model {

    String getId();

    String getDenominacion();

    String getCarpetaRaiz();

    void setCarpetaRaiz(String carpetaRaiz);

    String getCarpetaFoto();

    void setCarpetaFoto(String carpetaFoto);

    String getCarpetaFirma();

    void setCarpetaFirma(String carpetaFirma);

    String getCarpetaTemporal();

    void setCarpetaTemporal(String carpetaTemporal);

    String getAppKey();

    void setAppKey(String appKey);

    String getAppSecret();

    void setAppSecret(String appSecret);

    String getToken();

    void setToken(String token);

    boolean isDefault();

    void setDefault(boolean isDefault);

}
