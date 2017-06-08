package com.indra.elections.peruvian.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Partido {

@SerializedName("Value")
@Expose
private Integer value;
@SerializedName("idPartido")
@Expose
private String idPartido;
@SerializedName("siglas")
@Expose
private String siglas;

public Integer getValue() {
return value;
}

public void setValue(Integer value) {
this.value = value;
}

public String getIdPartido() {
return idPartido;
}

public void setIdPartido(String idPartido) {
this.idPartido = idPartido;
}

public String getSiglas() {
return siglas;
}

public void setSiglas(String siglas) {
this.siglas = siglas;
}

}


