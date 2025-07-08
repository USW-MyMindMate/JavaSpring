package com.example.MyMindMate.routine;

import com.example.MyMindMate.member.domain.User;
import com.example.MyMindMate.member.repository.UserRepository;
import com.example.MyMindMate.routine.dto.RoutineWithLogResponse;
import com.example.MyMindMate.routine.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final UserRepository userRepository;

    public List<RoutineWithLogResponse> getRoutineAndRoutineLog (List<User> childs){

        List<Long> userIds = childs.stream()
                .map(User::getId)
                .collect(Collectors.toList());

        return routineRepository.findRoutineWithLogByUserIdIn(userIds);

    }
}
