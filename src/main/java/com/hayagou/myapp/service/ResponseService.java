package com.hayagou.myapp.service;

import com.hayagou.myapp.model.response.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // 해당 Class가 Service임을 명시합니다.
public class ResponseService {

    @Value("${info}")
    private String info;

//    // enum으로 api 요청 결과에 대한  message를 정의합니다.
    public enum ResponseMessage {
        SUCCESS( "성공하였습니다.");


        String msg;

        ResponseMessage(String msg) {
            this.msg = msg;
        }


        public String getMsg() {
            return msg;
        }
    }
    // 단일건 결과를 처리하는 메소드
    public <T> DataResponse<T> getResponse(T data) {
        DataResponse<T> result = new DataResponse<>();
        result.setData(data);
        return result;
    }

    public <T> DataResponse<T> getResponse(T data, ResponseMessage message) {
        DataResponse<T> result = new DataResponse<>();
        result.setData(data);

        return result;
    }

    // 다중건 결과를 처리하는 메소드
    public <T> ListResponse<T> getListResult(List<T> list) {
        ListResponse<T> result = new ListResponse<>();
        result.setList(list);
        return result;
    }




    public <T> ListResponse<T> getListResponse(List<T> list) {
        ListResponse<T> result = new ListResponse<>();
        result.setList(list);
        return result;
    }

    // 실패 결과만 처리하는 메소드
    public ErrorResponse getFailResult(int code, String msg) {
        ErrorResponseInfo info = new ErrorResponseInfo();
        info.setCode(code);
        info.setMsg(msg);
        info.setInfo(this.info);
        return new ErrorResponse(info);
    }

}