package com.ll.jsbwtl.domain.catagory.entity;

import com.ll.jsbwtl.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;


// 예시 코드입니다. (지우시고 자유롭게 개발하셔도 돼요)
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Category extends BaseEntity {
    private String content;
}

