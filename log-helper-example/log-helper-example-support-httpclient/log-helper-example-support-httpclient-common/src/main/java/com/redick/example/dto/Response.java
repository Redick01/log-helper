package com.redick.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Redick01
 *  2022/3/31 13:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {

    private Integer code;

    private String message;

    private Object data;
}
