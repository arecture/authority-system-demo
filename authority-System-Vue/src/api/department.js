import http from '@/utils/request';
export default {
  /**
  * 查询部门列表
  * @param params
  * @returns
  */
  async getDepartmentList(params) {
    return await http.get("/api/department/list", params);
  },
  /**
  * 获取所属部门列表
  */
  async getParentTreeList() {
    return await http.get("/api/department/parent/list");
  },
  /**
   * 新增部门
   * @param params 
   * @returns 
   */
  async addDept(params){
    return await http.post("/api/department/add",params)
  },

  /**
   * 修改部门
   * @param params 
   * @returns 
   */
  async updateDept(params){
    return await http.put("/api/department/update",params)
  },

  /**
  * 检查部门下是否存在子部门
  */
  async checkDepartment(params){
    return await http.getRestApi("/api/department/check",params);
  },

  /**
  * 删除部门
  * @returns
  */
  async deleteById(params) {
    return await http.delete("/api/department/delete", params);
  }

} 