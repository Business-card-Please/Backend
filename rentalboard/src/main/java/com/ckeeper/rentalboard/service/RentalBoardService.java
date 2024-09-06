package com.ckeeper.rentalboard.service;

import com.ckeeper.rentalboard.dto.RentalBoardRequest;
import com.ckeeper.rentalboard.dto.RentalBoardSelectRequest;
import com.ckeeper.rentalboard.entity.RentalBoardEntity;
import com.ckeeper.rentalboard.repository.RentalBoardRepository;
import com.ckeeper.rentalboard.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RentalBoardService {
    private final RentalBoardRepository rentalBoardRepository;
    private final JwtUtil jwtUtil;

    RentalBoardService(RentalBoardRepository rentalBoardRepository, JwtUtil jwtUtil) {
        this.rentalBoardRepository = rentalBoardRepository;
        this.jwtUtil = jwtUtil;
    }

    public void createBoard(HttpServletRequest request, RentalBoardRequest rentalBoardRequest){
        String accessToken = jwtUtil.getTokenValue(request,"access_token");
        Claims owner = jwtUtil.validateToken(accessToken);
        RentalBoardEntity rentalBoardEntity = new RentalBoardEntity();
        rentalBoardEntity.setNickname(owner.getSubject());
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

    public void selectBoard(HttpServletRequest request, RentalBoardSelectRequest rentalBoardSelectRequest) {
        Optional<RentalBoardEntity> entity = rentalBoardRepository.findById(Long.valueOf(rentalBoardSelectRequest.getIdx()));
        if (rentalBoardSelectRequest.getType().equals("all")) {

        }
    }
}
