package com.ranyk.model.business.userinfo.vo;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: UserBaseVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 后端返回前端用户基本信息实体类封装对象
 * @date: 2025-11-15
 */
@Data
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserBaseVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -2007006961223846209L;
    /**
     * 用户 ID
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
    private Integer sex;
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
     * 用户头像地址
     */
    private String avatar;
}
