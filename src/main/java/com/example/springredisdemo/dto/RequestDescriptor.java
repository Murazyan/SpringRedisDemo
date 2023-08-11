package com.example.springredisdemo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDescriptor {

   private String accountId;
   private String clientIp;
   private  String requestType;
}
