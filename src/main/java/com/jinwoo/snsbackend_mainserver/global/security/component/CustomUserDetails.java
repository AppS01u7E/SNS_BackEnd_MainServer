package com.jinwoo.snsbackend_mainserver.global.security.component;

import com.jinwoo.snsbackend_mainserver.domain.auth.entity.Member;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import neiseApi.Neis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;


@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    @Autowired
    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        GrantedAuthority grantedAuthority = () -> member.getRole().name();
        return Collections.singleton(grantedAuthority);
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
