package com.example.springredisdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestDescriptor {

   private String accountId;
   private String clientIp;
   private  String requestType;
}
