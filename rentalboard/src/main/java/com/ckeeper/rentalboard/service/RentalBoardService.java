package com.ckeeper.rentalboard.service;

import com.ckeeper.rentalboard.dto.RentalBoardRequest;
import com.ckeeper.rentalboard.entity.RentalBoardEntity;
import com.ckeeper.rentalboard.repository.RentalBoardRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class RentalBoardService {
    private final RentalBoardRepository rentalBoardRepository;

    RentalBoardService(RentalBoardRepository rentalBoardRepository) {
        this.rentalBoardRepository = rentalBoardRepository;
    }

    public void createBoard(HttpServletRequest request, RentalBoardRequest rentalBoardRequest){
        RentalBoardEntity rentalBoardEntity = new RentalBoardEntity();
        rentalBoardEntity.setNickname(rentalBoardRequest.getOwner());
        rentalBoardEntity.setTitle(rentalBoardRequest.getTitle());
        rentalBoardEntity.setContent(rentalBoardRequest.getContent());
        rentalBoardEntity.setLecture(rentalBoardRequest.getLecture());
        rentalBoardEntity.setDepartment(rentalBoardRequest.getDepartment());

        rentalBoardRepository.save(rentalBoardEntity);
    }
}
