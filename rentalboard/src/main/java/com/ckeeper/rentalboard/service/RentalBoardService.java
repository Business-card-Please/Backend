package com.ckeeper.rentalboard.service;

import com.ckeeper.rentalboard.dto.RentalBoardRequest;
import com.ckeeper.rentalboard.dto.RentalBoardSelectRequest;
import com.ckeeper.rentalboard.entity.RentalBoardEntity;
import com.ckeeper.rentalboard.repository.RentalBoardRepository;
import com.ckeeper.rentalboard.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public Map<String,Object> selectBoard(RentalBoardSelectRequest rentalBoardSelectRequest) {
        Page<RentalBoardEntity> result = null;
        PageRequest pageRequest = PageRequest.of(0, rentalBoardSelectRequest.getSize(), Sort.by(Sort.Direction.DESC, "cdatetime"));

        if(rentalBoardSelectRequest.getType().equals("default")) {
            result = rentalBoardRepository.findByDateTimeTypeDefault(
                    rentalBoardSelectRequest.getDatetime(),
                    rentalBoardSelectRequest.getDepartment1(),
                    rentalBoardSelectRequest.getDepartment2(),
                    pageRequest
            );
        }else if(rentalBoardSelectRequest.getType().equals("hotkeyword")){
            result = rentalBoardRepository.findByDateTimeTypeHotkeyword(
                    rentalBoardSelectRequest.getDatetime(),
                    rentalBoardSelectRequest.getDepartment1(),
                    rentalBoardSelectRequest.getDepartment2(),
                    rentalBoardSelectRequest.getData(),
                    pageRequest
            );
        }else if(rentalBoardSelectRequest.getType().equals("search")){
            result = rentalBoardRepository.findByDateTimeTypeSearch(
                    rentalBoardSelectRequest.getDatetime(),
                    rentalBoardSelectRequest.getData(),
                    pageRequest
            );
        }

        List<RentalBoardEntity> rentalBoardList = result.getContent();

        Map<String,Object> test = new HashMap<>();
        test.put("current",rentalBoardList);
        test.put("isNext",result.hasNext());
        return test;
    }

    public Integer selectBoardDetail(long boardIdx,String viewer){
        Optional<RentalBoardEntity> entity = rentalBoardRepository.findById(boardIdx);
        if(!entity.get().getNickname().equals(viewer)){
            entity.get().setViewcount(entity.get().getViewcount()+1);
            rentalBoardRepository.save(entity.get());
        }
        return entity.get().getViewcount();
    }
}
