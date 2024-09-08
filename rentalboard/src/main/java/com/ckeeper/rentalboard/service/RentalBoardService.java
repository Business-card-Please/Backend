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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<RentalBoardEntity> selectBoard(RentalBoardSelectRequest rentalBoardSelectRequest,String department1, String department2) {
        if(rentalBoardSelectRequest.getType().equals("default")) {
            PageRequest pageRequest = PageRequest.of(0, rentalBoardSelectRequest.getSize(), Sort.by(Sort.Direction.DESC, "cdatetime"));
            Page<RentalBoardEntity> result = rentalBoardRepository.findByDateTimeTypeDefault(
                    rentalBoardSelectRequest.getDatetime(),
                    department1,
                    department2,
                    pageRequest
            );

            List<RentalBoardEntity> rentalBoardList = result.getContent();
            return rentalBoardList;
        }else if(rentalBoardSelectRequest.getType().equals("hotkeyword")){
            PageRequest pageRequest = PageRequest.of(0, rentalBoardSelectRequest.getSize(), Sort.by(Sort.Direction.DESC, "cdatetime"));
            Page<RentalBoardEntity> result = rentalBoardRepository.findByDateTimeTypeHotkeyword(
                    rentalBoardSelectRequest.getDatetime(),
                    department1,
                    department2,
                    rentalBoardSelectRequest.getData(),
                    pageRequest
            );

            List<RentalBoardEntity> rentalBoardList = result.getContent();
            return rentalBoardList;
        }else if(rentalBoardSelectRequest.getType().equals("search")){
            PageRequest pageRequest = PageRequest.of(0, rentalBoardSelectRequest.getSize(), Sort.by(Sort.Direction.DESC, "cdatetime"));
            Page<RentalBoardEntity> result = rentalBoardRepository.findByDateTimeTypeSearch(
                    rentalBoardSelectRequest.getDatetime(),
                    rentalBoardSelectRequest.getData(),
                    pageRequest
            );

            List<RentalBoardEntity> rentalBoardList = result.getContent();
            return rentalBoardList;
        }
        return null;
    }
}
