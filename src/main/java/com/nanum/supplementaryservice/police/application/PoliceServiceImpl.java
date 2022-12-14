package com.nanum.supplementaryservice.police.application;

import com.nanum.exception.PoliceChangeNotAcceptableException;
import com.nanum.exception.PoliceNotFoundException;
import com.nanum.kafka.messagequeue.KafkaProducer;
import com.nanum.supplementaryservice.client.UserServiceClient;
import com.nanum.supplementaryservice.client.vo.UserDto;
import com.nanum.supplementaryservice.client.vo.UserResponse;
import com.nanum.supplementaryservice.note.application.NoteService;
import com.nanum.supplementaryservice.police.domain.Police;
import com.nanum.supplementaryservice.police.domain.Status;
import com.nanum.supplementaryservice.police.domain.Type;
import com.nanum.supplementaryservice.police.dto.PoliceChangeStatusDto;
import com.nanum.supplementaryservice.police.dto.PoliceDto;
import com.nanum.supplementaryservice.police.infrastructure.PoliceRepository;
import com.nanum.supplementaryservice.police.vo.PoliceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PoliceServiceImpl implements PoliceService{
    private final PoliceRepository policeRepository;
    private final NoteService noteService;

    private final KafkaProducer kafkaProducer;

    private final UserServiceClient userServiceClient;
    @Override
    public boolean createPolice(PoliceDto policeDto) {

        /* user*/
        userServiceClient.getUser(policeDto.getReportedUserId());
        userServiceClient.getUser(policeDto.getReporterId());


        //check user1, user2

        // check
        if(Type.BOARD.equals(policeDto.getType())){
            // board check
        } else if (Type.CHAT.equals(policeDto.getType())) {
            // chat check
        }else{
            noteService.existsById(policeDto.getTypeId());
        }

        boolean result = policeRepository.existsByReporterIdAndReportedUserId(policeDto.getReporterId(), policeDto.getReportedUserId());
        if(result){
            return false;
        }
        Police police = policeDto.policeDtoTOEntity();
        policeRepository.save(police);
        return true;
    }

    @Override
    public Page<PoliceResponse> retrievePolices(Pageable pageable) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<Long> reportedUsers = new ArrayList<>();
        List<Long> reporters = new ArrayList<>();
        Page<PoliceResponse> policeResponses = policeRepository.findAll(pageable).map(police ->
        {
            reportedUsers.add(police.getReportedUserId());
            reporters.add(police.getReporterId());
            return modelMapper.map(police, PoliceResponse.class);
        });

        if(policeResponses.getContent().size()<1){
            return policeResponses;
        }
        UserResponse<List<UserDto>> reportedUserList = userServiceClient.getUsersById(reportedUsers);
        UserResponse<List<UserDto>> reporterList = userServiceClient.getUsersById(reporters);


        return  policeResponses.
                map(block -> {
                    for (UserDto user: reportedUserList.getResult()) {
                        if(user.getUserId().equals(block.getReportedUserId())){
                            block.setReportedUser(user);
                        }
                    }
                    for (UserDto user: reporterList.getResult()) {
                        if(user.getUserId().equals(block.getReporterId())){
                            block.setReporter(user);
                        }
                    }
                    return block;
                });
    }

    @Override
    public boolean acceptedPolice(Long policeId) {
        Optional<Police> police = policeRepository.findById(policeId);
        if(police.isEmpty()){
            throw new PoliceNotFoundException(String.format("ID[%s] not found",policeId));
        }

        if(police.get().getStatus().equals(Status.COMPLETE)){
            throw new PoliceChangeNotAcceptableException(String.format("ID[%s]??? ????????? ?????? ?????????????????????.",policeId));
        }


        /* send this user to the kafka */
        kafkaProducer.sendUserId("user-topic", police.get().getReportedUserId());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        PoliceChangeStatusDto changeStatusDto = modelMapper.map(police.get(), PoliceChangeStatusDto.class);
        changeStatusDto.setStatus(Status.COMPLETE);


        Police finalPolice = changeStatusDto.policeDtoTOEntity();
        policeRepository.save(finalPolice);
        return true;
    }

    @Override
    public void deletePolice(Long policeId) {
        try{
            policeRepository.deleteById(policeId);
        }catch (PoliceNotFoundException ex){
            throw new PoliceNotFoundException(String.format("not completed",ex));
        }

    }
}
