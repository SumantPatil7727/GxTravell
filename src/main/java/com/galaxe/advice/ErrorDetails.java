package com.galaxe.advice;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {
  private Integer errorCode;
  private LocalDateTime time;
  private String msg;
  private String status;
}
