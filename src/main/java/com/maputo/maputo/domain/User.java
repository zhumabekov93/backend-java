package com.maputo.maputo.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

//We need serializable to be implemented in order to transform it from java class to a certain stream that can be saved in db.
//It makes this transition easier from moving representation of data to another.
@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String profileImageUrl;
    private Date lastLoginDate; //last login date
    private Date lastLoginDateDisplay;//actual login date
    private Date joinDate;
    private String role; //ROLE_USER, ROLE_ADMIN
    private String[] authorities;//{read,write, delete etc.}
    private boolean isActive;
    private boolean isNotBlocked;

    public User(Long id, String userId, String firstName, String lastName, String username, String password, String email, String profileImageUrl, Date lastLoginDate, Date lastLoginDateDisplay, Date joinDate, String role, String[] authorities, boolean isActive, boolean isNotBlocked) {
        this.id = id;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.lastLoginDate = lastLoginDate;
        this.lastLoginDateDisplay = lastLoginDateDisplay;
        this.joinDate = joinDate;
        this.role = role;
        this.authorities = authorities;
        this.isActive = isActive;
        this.isNotBlocked = isNotBlocked;
    }

    public User() {}

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public Date getLastLoginDateDisplay() {
        return lastLoginDateDisplay;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public String getRole() {
        return role;
    }

    public String[] getAuthorities() {
        return authorities;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isNotBlocked() {
        return isNotBlocked;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public void setLastLoginDateDisplay(Date lastLoginDateDisplay) {
        this.lastLoginDateDisplay = lastLoginDateDisplay;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setAuthorities(String[] authorities) {
        this.authorities = authorities;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setNotBlocked(boolean notBlocked) {
        isNotBlocked = notBlocked;
    }
}
