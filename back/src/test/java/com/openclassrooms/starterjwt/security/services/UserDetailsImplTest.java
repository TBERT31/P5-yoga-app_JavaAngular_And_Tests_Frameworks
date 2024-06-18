package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

class UserDetailsImplTest {

    @Test
    void testUserDetailsImpl() {
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("testUser")
                .firstName("Test")
                .lastName("User")
                .admin(true)
                .password("password")
                .build();

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("testUser");
        assertThat(user.getFirstName()).isEqualTo("Test");
        assertThat(user.getLastName()).isEqualTo("User");
        assertThat(user.getAdmin()).isTrue();
        assertThat(user.getPassword()).isEqualTo("password");
    }

    @Test
    void testGetAuthorities() {
        UserDetailsImpl user = UserDetailsImpl.builder().build();
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertThat(authorities).isInstanceOf(HashSet.class);
        assertThat(authorities).isEmpty();
    }

    @Test
    void testIsAccountNonExpired() {
        UserDetailsImpl user = UserDetailsImpl.builder().build();
        assertThat(user.isAccountNonExpired()).isTrue();
    }

    @Test
    void testIsAccountNonLocked() {
        UserDetailsImpl user = UserDetailsImpl.builder().build();
        assertThat(user.isAccountNonLocked()).isTrue();
    }

    @Test
    void testIsCredentialsNonExpired() {
        UserDetailsImpl user = UserDetailsImpl.builder().build();
        assertThat(user.isCredentialsNonExpired()).isTrue();
    }

    @Test
    void testIsEnabled() {
        UserDetailsImpl user = UserDetailsImpl.builder().build();
        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    void testEquals() {
        UserDetailsImpl user1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl user2 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl user3 = UserDetailsImpl.builder().id(2L).build();

        assertThat(user1).isEqualTo(user2);
        assertThat(user1).isNotEqualTo(user3);
        assertThat(user1).isNotEqualTo(null);
        assertThat(user1).isNotEqualTo(new Object());
    }

    @Test
    void testEqualsDifferentIds() {
        UserDetailsImpl user1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl user2 = UserDetailsImpl.builder().id(2L).build();

        assertThat(user1).isNotEqualTo(user2);
    }
}
