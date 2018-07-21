package web.signIn.dto;

import java.util.Collection;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import query.model.user.UserEntity;

@EqualsAndHashCode
public final class UserPrincipal implements UserDetails {

    @JsonIgnore
    private UserEntity user;

    public UserPrincipal(UserEntity user) {
        this.user = user;
    }

    public String getId() {
        return user.getId().toString();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(user.getUserType().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isActivated();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isActivated();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isActivated();
    }

    @Override
    public boolean isEnabled() {
        return user.isActivated();
    }
}
