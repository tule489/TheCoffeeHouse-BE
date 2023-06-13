package com.theCoffeeHouse.theCoffeeHouse.Service;

import com.theCoffeeHouse.theCoffeeHouse.Models.User;
import com.theCoffeeHouse.theCoffeeHouse.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> user = repository.findByUsername(username);
        if (user.size() == 0) {
            throw new UsernameNotFoundException("User not found");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.get(0).getRole()));

        return new org.springframework.security.core.userdetails.User(
                user.get(0).getUsername(),
                user.get(0).getPassword(),
                authorities
        );
    }
}
