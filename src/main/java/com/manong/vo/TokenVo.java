package com.manong.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName TokenVo
 * @Description TODO
 * @Author 40706
 * @Date 2022/7/17 20:50
 * @Version
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenVo {
//    过期时间
    private Long expireTime;
//    token信息
    private String token;
}
