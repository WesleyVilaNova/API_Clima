package com.example.apicomeiateste.model;

public class TempCidadeModel {
    String tempMin;
    String tempMax;
    String cidade;
    String country;
    String humidade;

    public TempCidadeModel(String tempMin, String tempMax, String cidade, String country, String humidade) {
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.cidade = cidade;
        this.country = country;
        this.humidade = humidade;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHumidade() {
        return humidade;
    }

    public void setHumidade(String humidade) {
        this.humidade = humidade;
    }
}
