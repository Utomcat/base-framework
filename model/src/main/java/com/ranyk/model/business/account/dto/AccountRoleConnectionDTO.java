package com.ranyk.model.business.account.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CLASS_NAME: AccountRoleConnectionDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入后端的账户角色关联数据接受对象或后端业务类和业务类之间相互传递的账户角色关联实体封装类
 * @date: 2025-12-18
 */
@Data
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AccountRoleConnectionDTO  implements Serializable {

    @Serial
    private static final long serialVersionUID = 8986390793639813867L;
    /**
     * 主键 ID
     */
    private Long id;
    /**
     * 账户信息数据 ID
     */
    private Long accountId;
    /**
     * 角色信息数据 ID
     */
    private Long roleId;
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
