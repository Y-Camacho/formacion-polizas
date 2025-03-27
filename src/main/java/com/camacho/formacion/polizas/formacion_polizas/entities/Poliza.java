package com.camacho.formacion.polizas.formacion_polizas.entities;

import org.springframework.data.mongodb.core.mapping.Field;

//import org.springframework.data.mongodb.core.mapping.Field;

public class Poliza {

    @Field(name = "Numero")
    private int number;

    @Field(name = "Modalidad")
    private String modality;

    @Field(name = "Prima")
    private double price;

    public Poliza() {
    }
    public Poliza(int numero, String modalidad, double prima) {
        this.number = numero;
        this.modality = modalidad;
        this.price = prima;
    }

    public int getNumber() {
        return number;
    }
    public void setNumber(int numero) {
        this.number = numero;
    }
    public String getModality() {
        return modality;
    }
    public void setModality(String modalidad) {
        this.modality = modalidad;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double prima) {
        this.price = prima;
    }
    @Override
    public String toString() {
        return "Poliza [Numero=" + number + ", Modalidad=" + modality + ", Prima=" + price + "]";
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + number;
        result = prime * result + ((modality == null) ? 0 : modality.hashCode());
        long temp;
        temp = Double.doubleToLongBits(price);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Poliza other = (Poliza) obj;
        if (number != other.number)
            return false;
        if (modality == null) {
            if (other.modality != null)
                return false;
        } else if (!modality.equals(other.modality))
            return false;
        if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
            return false;
        return true;
    }
}
