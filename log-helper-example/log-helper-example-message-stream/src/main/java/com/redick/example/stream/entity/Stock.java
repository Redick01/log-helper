package com.redick.example.stream.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author Redick01
 */
@Data
public class Stock implements Serializable {

    private String productId;

    private Integer totalCount;

    private String productName;

    private Date beginTime;

    private Date endTime;

    private String productDesc;
}
