package com.hayagou.myapp.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class ListResponse<T> {
    private List<T> list;
    private int totalCount; // 조회된 데이터 전체 row의 수
    private int totalPage; // totalCount/perPage if totalCount % perPage > 0 totalPage++
    private int currentPage; // 현재 페이지
}