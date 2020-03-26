package com.kakaointerntask.bank.service.implement;

import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.repository.BankUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailsService")
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final BankUserRepository bankUserRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        BankUser bankUser = bankUserRepository.findBankUserByIdAndActive(id, true)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("id %s was not found", id)));
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(bankUser.isAdmin())
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        else
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new User(bankUser.getId(), bankUser.getPassword(), authorities);
    }
}
