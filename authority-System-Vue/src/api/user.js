import http from '@/utils/request'

/**
 * 用户登录方法
 * @param data 
 * @returns 
 */
export async function login(data) {
  return await http.login("/api/user/login", data)
}

/**
 * 获取登录用户信息
 * @param token 
 * @returns 
 */
export async function getInfo() {
  return await http.get("/api/sysUser/getInfo")
}

/**
 * 退出登录
 * @returns 
 */
export async function logout(params) {
  return await http.post("/api/sysUser/logout", params)
}

/**
 * 获取用户菜单信息
 */
export async function getMenuList() {
  return await http.get("/api/sysUser/getMenuList")
}

export default {
  /**
  * 查询用户列表
  * @param params
  * @returns
  */
  async getUserList(params) {
    return await http.get("/api/user/list", params);
  }
}