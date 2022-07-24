<template>
  <el-main>
<!-- 查询条件 -->
    <el-form ref="searchForm" label-width="80px" :inline="true" size="small">
      <el-form-item>
        <el-input v-model="departmentName" placeholder="请输入部门名称"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search">查询</el-button>
        <el-button icon="el-icon-refresh-right">重置</el-button>
        <el-button type="success" icon="el-icon-plus">新增</el-button>
      </el-form-item>
    </el-form>
    <el-table
    :data="tableData"
    style="width: 100%;margin-bottom: 20px;"
    row-key="id"
    border
    stripe
    default-expand-all
    :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
    <!-- prop属性：填写数据的属性名称 -->
    <!-- label属性：表格表头标题 -->
    <el-table-column prop="departmentName" label="部门名称"/>
    <el-table-column prop="parentName" label="所属部门"/>
    <el-table-column prop="phone" label="部门电话"/>
    <el-table-column prop="address" label="部门地址"/>
    <el-table-column label="操作">
      <template slot-scope="scope">
        <el-button
          size="mini"
          icon="el-icon-edit-outline"
          type="primary"
          @click="handleEdit(scope.row)">编辑</el-button>
        <el-button
          size="mini"
          type="danger"
          icon="el-icon-delete-solid"
          @click="handleDelete(scope.row)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  </el-main>
</template>

<script>
// 导入department脚本文件
import departmentApi from '@/api/department'
export default {
  name:"department",
  data() {
    return {
      departmentName:"",
      tableData:[],
    }
  },
  created() {
    this.search();
  },
  methods: {
    async search(){
      let res = await departmentApi.getDepartmentList(this.departmentName)
      if(res.success){
        this.tableData = res.data;
      }
    }
  },
}
</script>
<style lang="scss" scoped></style>