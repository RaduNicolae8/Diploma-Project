package com.diploma.project.service;

import com.diploma.project.dto.MarketplaceServiceDTO;
import com.diploma.project.dto.MarketplaceUserDTO;
import com.diploma.project.exception.CustomException;
import com.diploma.project.mapper.MarketplaceUserMapper;
import com.diploma.project.model.MarketplaceService;
import com.diploma.project.model.MarketplaceUser;
import com.diploma.project.repository.MarketplaceUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MarketplaceUserService extends GenericService<MarketplaceUser, MarketplaceUserDTO>{

    MarketplaceUserService(MarketplaceUserRepository marketplaceUserRepository, MarketplaceUserMapper marketplaceUserMapper){
        super(marketplaceUserRepository, marketplaceUserMapper);
    }

    public MarketplaceUserDTO update(MarketplaceUserDTO marketplaceUserDTO){
        log.debug("Update MarketplaceUser {}", marketplaceUserDTO.toString());
        MarketplaceUser marketplaceUser = getGenericMapper().toEntity(marketplaceUserDTO);
        Optional<MarketplaceUser> byId = getJpaRepository().findById(marketplaceUser.getId());
        if (byId.isEmpty()) {
            throw new CustomException("MarketplaceUser with id " + marketplaceUser.getId() + " does not exist!", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
        }
        MarketplaceUser save = getJpaRepository().save(marketplaceUser);
        log.debug("Updated marketplaceUser with id {}", save.getId());
        return getGenericMapper().toDTO(save);
    }

    public MarketplaceUserDTO findByUsername(String email) {
        Optional<MarketplaceUser> byUsername= ((MarketplaceUserRepository) getJpaRepository()).findByEmail(email);
        if (byUsername.isEmpty()){
            throw new RuntimeException("This EasyService user does not exist!");
        }
        return getGenericMapper().toDTO(byUsername.get());
    }

    public void updateResetPasswordToken(String token, String email) throws CustomException {
        Optional<MarketplaceUser> byUsername = ((MarketplaceUserRepository) getJpaRepository()).findByEmail(email);
        if (byUsername.isEmpty()) {
            throw new CustomException("EasyService user with username " + email + " does not exist!", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
        }
        MarketplaceUser hospitalUser = byUsername.get();
        hospitalUser.setResetPasswordToken(token);
        getJpaRepository().save(hospitalUser);
    }

    public MarketplaceUserDTO findByResetPasswordToken(String token) {
        Optional<MarketplaceUser> byResetPasswordToken = ((MarketplaceUserRepository) getJpaRepository()).findByResetPasswordToken(token);
        if (byResetPasswordToken.isEmpty()) {
            throw new RuntimeException("This EasyService user does not exist!");
        }
        return getGenericMapper().toDTO(byResetPasswordToken.get());
    }

    public void updatePassword(MarketplaceUserDTO marketplaceUserDTO, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        MarketplaceUser marketplaceUser = getGenericMapper().toEntity(marketplaceUserDTO);
        marketplaceUser.setPassword(passwordEncoder.encode(newPassword));
        marketplaceUser.setResetPasswordToken(null);
        getJpaRepository().save(marketplaceUser);
    }

}
