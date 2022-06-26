package com.Bytepad.server.serviceImplementation;

import com.Bytepad.server.models.Admin;
import com.Bytepad.server.repositories.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "adminDetails")
public class AdminDetails implements UserDetailsService {

    @Autowired
    private AdminRepo adminRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Admin admin = adminRepo.findByUsername(username);

        String roles[] = admin.getRole().split(",");
        List<SimpleGrantedAuthority> rolesList = new ArrayList<>();
        for(String r:roles){
            rolesList.add(new SimpleGrantedAuthority(r));
        }

        if(admin == null){
            throw new UsernameNotFoundException("Could not find admin");
        }

        return new org.springframework.security.core.userdetails.User(admin.getUsername(),admin.getPassword(),true,true,true,true,rolesList);


    }
}
