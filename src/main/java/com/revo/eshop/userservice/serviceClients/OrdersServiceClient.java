package com.revo.eshop.userservice.serviceClients;

import com.revo.eshop.userservice.ui.model.orders.OrdersResponseModel;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "orders-ws")
@Component
public interface OrdersServiceClient {

    @GetMapping("/orders/users/{userId}")
    List<OrdersResponseModel> getOrdersByUserId(@PathVariable String userId);

}