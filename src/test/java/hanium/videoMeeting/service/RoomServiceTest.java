package hanium.videoMeeting.service;

import hanium.videoMeeting.DTO.RoomDto;
import hanium.videoMeeting.advice.exception.ExistedRoomTitleException;
import hanium.videoMeeting.domain.Room;
import hanium.videoMeeting.repository.RoomRepository;
import io.openvidu.java.client.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class RoomServiceTest {

    @Autowired private OpenVidu openVidu;
    @Autowired private RoomService roomService;
    @Autowired private RoomRepository roomRepository;

    @DisplayName("룸 생성 테스트")
    @Test
    public void createRoomTest() throws Exception {
        //given
        RoomDto roomDto = new RoomDto("test","12345");
        Long roomId = roomService.createRoom(roomDto);

        //when
        Room createdRoom = roomRepository.findById(roomId).orElse(null);

        //then
        assertThat(createdRoom).isNotNull();
        assertThat(createdRoom.getTitle()).isEqualTo(roomDto.getTitle());
        assertThat(createdRoom.getPassword()).isEqualTo(roomDto.getPassword());
        assertThat(createdRoom.getPeople_num()).isEqualTo(0);
        assertThat(createdRoom.getSession()).isNotNull();
        System.out.println("생성 날짜 : " + createdRoom.getStart_time());
        System.out.println("세션 ID : " + createdRoom.getSession());


        // 테스트를 위해 만든 세션 제거
        List<Session> activeSessions = openVidu.getActiveSessions();
        activeSessions.forEach(s -> {
            try {
                s.close();
            } catch (OpenViduJavaClientException | OpenViduHttpException e) {
                e.printStackTrace();
            }
        });

    }
    
    @Test
    public void createDuplicateTitle() throws Exception {
        //given
        RoomDto roomDto = new RoomDto("duplicationTest","12345");
        Long roomId = roomService.createRoom(roomDto);

        //when
        RoomDto dupRoomDto = new RoomDto("duplicationTest","789456");
        try{
            Long dupRoomId = roomService.createRoom(roomDto);
        } catch (ExistedRoomTitleException e){
            return;
        }

        //then
        fail("중복 예외가 발생해야합니다.");

    }


}