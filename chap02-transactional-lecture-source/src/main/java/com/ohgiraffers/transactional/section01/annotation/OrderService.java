package com.ohgiraffers.transactional.section01.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    /* 설명. Order 도메인과 Menu 도메인 두 개를 하나의 Service에서 트랜잭션 상 쓰는 경우를 상정 */
    private final OrderMapper orderMapper;
    private final MenuMapper menuMapper;

    @Autowired
    public OrderService(OrderMapper orderMapper, MenuMapper menuMapper) {
        this.orderMapper = orderMapper;
        this.menuMapper = menuMapper;
    }

    /* 설명. Service 계층의 메소드가 DML 작업용 트랜잭션이면 @Transactional을 추가한다. */
    @Transactional(rollbackFor = Exception.class)
    public void registNewOrder(OrderDTO orderInfo) {

        /* 목차. 1. 주문한 메뉴 코드 추출(DTO에서) */
        /* 목차. 1-1. for문 버전 */
//        List<Integer> menuCodes = new ArrayList<>();
//        List<OrderMenuDTO> orderMenus = orderInfo.getOrderMenus();
//        for (OrderMenuDTO orderMenu : orderMenus) {
//            menuCodes.add(orderMenu.getMenuCode());
//        }

        /* 목차. 1-2. stream 버전 */
        List<Integer> menuCodes = orderInfo.getOrderMenus()
                .stream()
                .map(OrderMenuDTO::getMenuCode)
                .collect(Collectors.toList());

        Map<String, List<Integer>> map = new HashMap<>();
        map.put("menuCodes", menuCodes);

        /* 목차. 2. 주문한 메뉴 별로 Menu 엔티티에 담아서 조회(select)해 오기(부가적인 메뉴의 정보 추출) */
        List<Menu> menus = menuMapper.selectMenuByMenuCodes(map);
//        menus.forEach(System.out::println);

        /* 목차. 3. 이 주문 건에 대한 주문 총 합계를 계산 */
        int totalOrderPrice = calcTotalPrice(orderInfo.getOrderMenus(), menus);

        /* 목차. 4. 1부터 3까지 하면 tbl_order 테이블에 추가(insert) */
        /* 목차. 4-1. OrderDTO -> Order */
        Order order = new Order(orderInfo.getOrderDate(), orderInfo.getOrderTime(), totalOrderPrice);

        /* 목차. 4-2. Order로 insert (selectKey를 통해 insert한 주문번호가 orderCode에 담긴 상태로 돌아옴) */
        orderMapper.registOrder(order);
        System.out.println("tbl_order 테이블에 insert 후 Order 객체(Service 계층): " + order);

        /* 목차. 5. tbl_order_menu 테이블에도 주문한 메뉴 개수만큼 메뉴를 추가(insert) */
        /* 목차. 5-1. OrderDTO -> List<OrderMenuDTO> -> List<OrderMenu> */
        List<OrderMenuDTO> orderMenuDTO = orderInfo.getOrderMenus();
        for (OrderMenuDTO menuDTO : orderMenuDTO) {
            orderMapper.registOrderMenu(
                    new OrderMenu(
                              order.getOrderCode()
                            , menuDTO.getMenuCode()
                            , menuDTO.getOrderAmount()
                    )
            );
        }
    }

    private int calcTotalPrice(List<OrderMenuDTO> orderMenus, List<Menu> menus) {
        int totalPrice = 0;

        int orderMenusSize = orderMenus.size();
        for (int i = 0; i < orderMenusSize; i++) {          // 주문한 메뉴 수만큼 반복
            OrderMenuDTO orderMenu = orderMenus.get(i);     // 메뉴 수량 추출을 위해
            Menu menu = menus.get(i);                       // 메뉴 단가 추출을 위해
            totalPrice += orderMenu.getOrderAmount() * menu.getMenuPrice();
        }

        return totalPrice;
    }
}
