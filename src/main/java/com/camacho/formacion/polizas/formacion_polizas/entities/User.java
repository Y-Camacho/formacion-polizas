package com.camacho.formacion.polizas.formacion_polizas.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Field(name = "name")
    private String name;

    @Field(name = "lastName")
    private String lastname;

    @Field(name = "email")
    private String email;

    @Field(name = "password")
    private String passw;

    @Field(name = "createAt")
    private LocalDateTime createAt;

    @Field(name = "updateAt")
    private LocalDateTime updateAt;

    @Field(name = "policies")
    private List<Poliza> policies;

    public User() {
        this.createAt = LocalDateTime.now();
    }
    public User(String name, String lastname, String email, String passw, LocalDateTime createAt,
            LocalDateTime updateAt, List<Poliza> polizas) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.passw = passw;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.policies = polizas;
    }

    public void actualizarAcceso() {
        this.updateAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassw() {
        return passw;
    }
    public void setPassw(String passw) {
        this.passw = passw;
    }
    public LocalDateTime getCreateAt() {
        return createAt;
    }
    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
    public LocalDateTime getUpdateAt() {
        return updateAt;
    }
    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
    public List<Poliza> getPolicies() {
        return policies;
    }
    public void setPolicies(List<Poliza> polizas) {
        this.policies = polizas;
    }
    @Override
    public String toString() {
        return "{id=" + id + ", name=" + name + ", lastname=" + lastname + ", email=" + email + ", passw=" + passw
                + ", createAt=" + createAt + ", updateAt=" + updateAt + ", polizas=" + policies + "}";
    }
    
}
