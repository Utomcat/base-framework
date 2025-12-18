package com.ranyk.model.business.userinfo.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CLASS_NAME: UserBaseDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入后端或后端业务类和业务类之间相互传递的用户信息实体类封装对象
 * @date: 2025-11-15
 */
@Data
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserBaseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -9219733037261827396L;
    /**
     * 主键 ID
     */
    private Long id;
    /**
     * 用户全名
     */
    private String userName;
    /**
     * 用户姓氏
     */
    private String lastName;
    /**
     * 用户名字
     */
    private String firstName;
    /**
     * 用户性别 0: 未知(默认); 1: 男; 2: 女;
     */
    private byte sex;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户座机/固定电话
     */
    private String fixedLinePhone;
    /**
     * 用户电话
     */
    private String phone;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 数据创建时间
     */
    private LocalDateTime createTime;
    /**
     * 数据创建人 ID
     */
    private Long createId;
    /**
     * 数据更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 数据更新人 ID
     */
    private Long updateId;

}
