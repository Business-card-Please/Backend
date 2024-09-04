package com.ckeeper.rentalboard.service;

import com.ckeeper.rentalboard.dto.RentalBoardRequest;
import com.ckeeper.rentalboard.entity.RentalBoardEntity;
import com.ckeeper.rentalboard.repository.RentalBoardRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public void updateBoard(HttpServletRequest request, RentalBoardRequest rentalBoardRequest){
        Optional<RentalBoardEntity> entity = rentalBoardRepository.findById(Long.valueOf(rentalBoardRequest.getIdx()));
        entity.get().setTitle(rentalBoardRequest.getTitle());
        entity.get().setLecture(rentalBoardRequest.getLecture());
        entity.get().setDepartment(rentalBoardRequest.getDepartment());
        entity.get().setContent(rentalBoardRequest.getContent());

        rentalBoardRepository.save(entity.get());
    }

    public void deleteBoard(HttpServletRequest request, RentalBoardRequest rentalBoardRequest){
        Optional<RentalBoardEntity> entity = rentalBoardRepository.findById(Long.valueOf(rentalBoardRequest.getIdx()));
        rentalBoardRepository.delete(entity.get());
    }

    public
}
