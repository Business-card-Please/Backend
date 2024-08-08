package com.ckeeper.account.service;

import com.ckeeper.account.dto.RegistrationRequest1;
import com.ckeeper.account.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;

    @Autowired
    public RegistrationService(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    public void register1(RegistrationRequest1 registrationRequest1){

    }

}
