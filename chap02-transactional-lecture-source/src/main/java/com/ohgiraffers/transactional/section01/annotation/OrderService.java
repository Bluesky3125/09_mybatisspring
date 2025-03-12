package com.ohgiraffers.transactional.section01.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    /* 설명. Order 도메인과 Menu 도메인 두 개를 하나의 Service에서 트랜잭션 상 쓰는 경우를 상정 */
    private OrderMapper orderMapper;
    private MenuMapper menuMapper;

    @Autowired
    public OrderService(OrderMapper orderMapper, MenuMapper menuMapper) {
        this.orderMapper = orderMapper;
        this.menuMapper = menuMapper;
    }

    /* 설명. Service 계층의 메소드가 DML 작업용 트랜잭션이면 @Transactional을 추가한다. */
    @Transactional(rollbackFor = Exception.class)
    public void registNewOrder(OrderDTO orderInfo) {

        /* 목차. 1. 주문한 메뉴 코드 추출(DTO에서) */

        /* 목차. 2. 주문한 메뉴 별로 Menu 엔티티에 담아서 조회(select)해 오기(부가적인 메뉴의 정보 추출) */

        /* 목차. 3. 이 주문 건에 대한 주문 총 합계를 계산 */

        /* 목차. 4. 1부터 3까지 하면 tbl_order 테이블에 추가(insert) */

        /* 목차. 5. tbl_order_menu 테이블에도 주문한 메뉴 개수만큼 메뉴를 추가(insert) */

    }
}
