package com.application.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="Role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id")
    private Long id;

    @Column(name="Role_Name")
    private String roleName;


    @Column(name="can_create")
    private boolean CREATE = false;

    @Column(name="can_read")
    private boolean READ = false;

    @Column(name="can_update")
    private boolean UPDATE = false;

    @Column(name="can_delete")
    private boolean DELETE = false;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;



    public Role() {

    }

    public Role(String roleName, boolean isCreate,boolean isRead,boolean isUpdate,boolean isDelete) {
        this.roleName = roleName;
        this.CREATE = isCreate;
        this.READ = isRead;
        this.UPDATE = isUpdate;
        this.DELETE = isDelete;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }


    public boolean isCREATE() {
        return CREATE;
    }

    public void setCREATE(boolean CREATE) {
        this.CREATE = CREATE;
    }

    public boolean isREAD() {
        return READ;
    }

    public void setREAD(boolean READ) {
        this.READ = READ;
    }

    public boolean isUPDATE() {
        return UPDATE;
    }

    public void setUPDATE(boolean UPDATE) {
        this.UPDATE = UPDATE;
    }

    public boolean isDELETE() {
        return DELETE;
    }

    public void setDELETE(boolean DELETE) {
        this.DELETE = DELETE;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

}
