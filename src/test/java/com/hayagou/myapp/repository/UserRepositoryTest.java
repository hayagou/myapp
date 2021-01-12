package com.hayagou.myapp.repository;

import com.hayagou.myapp.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUserRepository(){
        long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기

        for(int i = 0 ; i< 100 ; i++){

            User user = User.builder().email("email").password("password").build();
            userRepository.save(user);
        }


        List<User> userList = userRepository.findAll();

//        for (User temp: userList) {
//            System.out.println(temp.getEmail());
//        }

        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
        System.out.println("시간차이(m) : "+secDiffTime);
    }

}