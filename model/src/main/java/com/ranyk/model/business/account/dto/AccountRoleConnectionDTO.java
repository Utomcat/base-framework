package com.ranyk.model.business.account.dto;

import com.ranyk.model.base.dto.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * CLASS_NAME: AccountRoleConnectionDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入后端的账户角色关联数据接受对象或后端业务类和业务类之间相互传递的账户角色关联实体封装类
 * @date: 2025-12-18
 */
@Data
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccountRoleConnectionDTO extends BaseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8986390793639813867L;
    /**
     * 账户信息数据 ID
     */
    private Long accountId;
    /**
     * 角色信息数据 ID
     */
    private Long roleId;

    // 以下为额外属性
    /**
     * 账户数据 ID List 集合
     */
    private List<Long> accountIds;
    /**
     * 角色数据 ID List 集合
     */
    private List<Long> roleIds;

}
