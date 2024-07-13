package com.finalproject.airport.member.service;

import com.finalproject.airport.member.dto.UserDTO;
import com.finalproject.airport.member.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final UserEntity userEntity;

    public CustomUserDetails(UserEntity userEntity) {

        this.userEntity = userEntity;
    }

    public UserDTO getUserDTO() {
        UserDTO userDTO = new UserDTO(userEntity.getUserCode(),userEntity.getUserId(),userEntity.getUserEmail(),userEntity.getUserPhone(),userEntity.getUserAddress(),userEntity.getUserName(),userEntity.getUserRole());

        return userDTO;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return userEntity.getUserRole();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {

        return userEntity.getUserPassword();
    }

    @Override
    public String getUsername() {

        return userEntity.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }
}