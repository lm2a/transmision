package com.indra.elections.peruvian.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Eleccion {

@SerializedName("Colegio")
@Expose
private String colegio;
@SerializedName("Eleccion")
@Expose
private Votacion votacion;
@SerializedName("Fecha")
@Expose
private String fecha;
@SerializedName("Maquina")
@Expose
private String maquina;
@SerializedName("Municipio")
@Expose
private String municipio;
@SerializedName("Provincia")
@Expose
private String provincia;
@SerializedName("TipoMaquina")
@Expose
private String tipoMaquina;
@SerializedName("Version")
@Expose
private String version;

public String getColegio() {
return colegio;
}

public void setColegio(String colegio) {
this.colegio = colegio;
}

public Votacion getVotacion() {
return votacion;
}

public void setVotacion(Votacion votacion) {
this.votacion = votacion;
}

public String getFecha() {
return fecha;
}

public void setFecha(String fecha) {
this.fecha = fecha;
}

public String getMaquina() {
return maquina;
}

public void setMaquina(String maquina) {
this.maquina = maquina;
}

public String getMunicipio() {
return municipio;
}

public void setMunicipio(String municipio) {
this.municipio = municipio;
}

public String getProvincia() {
return provincia;
}

public void setProvincia(String provincia) {
this.provincia = provincia;
}

public String getTipoMaquina() {
return tipoMaquina;
}

public void setTipoMaquina(String tipoMaquina) {
this.tipoMaquina = tipoMaquina;
}

public String getVersion() {
return version;
}

public void setVersion(String version) {
this.version = version;
}

}