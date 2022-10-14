package io.redick.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Redick01
 */
@TableName("name")
@Data
public class NameEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;
}
